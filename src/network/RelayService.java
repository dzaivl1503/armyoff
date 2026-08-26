/*
 * Decompiled with CFR 0.152.
 */
package network;

import CLib.mSystem;
import com.teamobi.mobiarmy2.GameMidlet;
import com.teamobi.mobiarmy2.OfflineEquipmentStats;
import coreLG.CCanvas;
import coreLG.TerrainMidlet;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Vector;
import model.Language;
import model.PlayerInfo;
import network.Message;
import network.MessageHandler;
import network.RelayHandler;
import network.RelayMemberInfo;
import network.Session_ME;
import screen.BoardListScr;
import screen.PrepareScr;
import screen.RelayLobbyScr;

public final class RelayService {
    public static final String HOST = "127.0.0.1";
    public static final int PORT = 19153;
    public static final byte CMD_HELLO = 1;
    public static final byte CMD_ROOM_LIST = 2;
    public static final byte CMD_CREATE_ROOM = 3;
    public static final byte CMD_JOIN_ROOM = 4;
    public static final byte CMD_LEAVE_ROOM = 5;
    public static final byte CMD_MEMBER_JOIN = 6;
    public static final byte CMD_MEMBER_LEAVE = 7;
    public static final byte CMD_NEW_HOST = 8;
    public static final byte CMD_ROOM_CHAT = 9;
    public static final byte CMD_RELAY = 10;
    public static final byte CMD_PING = 11;
    public static final byte CMD_ERROR = 12;
    public static final byte CMD_SET_STATE = 13;
    public static final byte CMD_PROFILE = 14;
    public static final byte PROTO_VERSION = 1;
    public static final int ST_OFF = 0;
    public static final int ST_CONNECTING = 1;
    public static final int ST_LOBBY = 2;
    public static final int ST_ROOM = 3;
    public static int state = 0;
    public static int sessionId = -1;
    public static Vector rooms = new Vector();
    public static Vector members = new Vector();
    public static int roomId = -1;
    public static String roomName = "";
    public static int hostId = -1;
    private static RelayHandler handler;
    private static long lastPing;
    private static int savedIDDB;
    private static boolean iddbOverridden;

    private RelayService() {
    }

    public static boolean isActive() {
        return state != 0;
    }

    public static boolean isHost() {
        return sessionId > 0 && sessionId == hostId;
    }

    public static void enter() {
        PlayerInfo playerInfo = TerrainMidlet.myInfo;
        if (playerInfo == null) {
            return;
        }
        if (playerInfo.getSquadSize() > 0) {
            CCanvas.startOKDlg("Ch\u1ebf \u0111\u1ed9 Multiplayer ch\u1ec9 s\u1eed d\u1ee5ng 1 nh\u00e2n v\u1eadt. H\u00e3y v\u00e0o L\u1eadp \u0111\u1ed9i b\u1ecf c\u00e1c nh\u00e2n v\u1eadt ph\u1ee5 (ch\u1ec9 \u0111\u1ecbnh nh\u00e2n v\u1eadt ch\u00ednh) r\u1ed3i quay l\u1ea1i.");
            return;
        }
        if (state == 2) {
            RelayService.openLobby();
            return;
        }
        if (state == 3) {
            RelayService.openRoom();
            return;
        }
        GameMidlet.ensureOfflineAssetsLoaded();
        RelayService.resetAll();
        state = 1;
        if (handler == null) {
            handler = new RelayHandler();
        }
        Session_ME.gI().setHandler(handler);
        CCanvas.startWaitDlgWithoutCancel(Language.connecting(), 11111);
        Session_ME.gI().connect(HOST, 19153);
    }

    public static void quit() {
        state = 0;
        try {
            Session_ME.gI().close(0);
        }
        catch (Exception exception) {
        }
        Session_ME.gI().setHandler(MessageHandler.gI());
        RelayService.restoreIDDB();
        PrepareScr.isRelayRoom = false;
        RelayService.resetAll();
    }

    static void overrideIDDB() {
        if (!iddbOverridden && TerrainMidlet.myInfo != null) {
            savedIDDB = TerrainMidlet.myInfo.IDDB;
            iddbOverridden = true;
        }
        if (TerrainMidlet.myInfo != null) {
            TerrainMidlet.myInfo.IDDB = sessionId;
        }
    }

    private static void restoreIDDB() {
        if (iddbOverridden && TerrainMidlet.myInfo != null) {
            TerrainMidlet.myInfo.IDDB = savedIDDB;
        }
        iddbOverridden = false;
    }

    static void resetAll() {
        sessionId = -1;
        rooms.removeAllElements();
        RelayService.resetRoom();
    }

    static void resetRoom() {
        members.removeAllElements();
        roomId = -1;
        roomName = "";
        hostId = -1;
    }

    public static void tick() {
        if (state == 0) {
            return;
        }
        Session_ME.update();
        if (state >= 2 && Session_ME.gI().connected && mSystem.currentTimeMillis() - lastPing > 30000L) {
            lastPing = mSystem.currentTimeMillis();
            RelayService.send((byte)11, null);
        }
    }

    static void send(byte by, Message message) {
        Message message2 = message != null ? message : new Message(by);
        Session_ME.gI().sendMessage(message2);
    }

    static void sendHello() {
        try {
            Message message = new Message(1);
            DataOutputStream dataOutputStream = message.writer();
            dataOutputStream.writeByte(1);
            PlayerInfo playerInfo = TerrainMidlet.myInfo;
            dataOutputStream.writeUTF(playerInfo == null || playerInfo.name == null ? "?" : playerInfo.name);
            dataOutputStream.writeUTF(GameMidlet.OFFLINE_VERSION_TEXT);
            RelayService.send((byte)1, message);
        }
        catch (IOException iOException) {
            iOException.printStackTrace();
        }
    }

    static void sendProfile() {
        PlayerInfo playerInfo = TerrainMidlet.myInfo;
        if (playerInfo == null) {
            return;
        }
        try {
            int n;
            Message message = new Message(14);
            DataOutputStream dataOutputStream = message.writer();
            dataOutputStream.writeUTF(playerInfo.name == null ? "?" : playerInfo.name);
            dataOutputStream.writeByte(playerInfo.gun);
            dataOutputStream.writeShort(playerInfo.level2);
            int[] nArray = OfflineEquipmentStats.calculate(playerInfo);
            for (n = 0; n < 5; ++n) {
                dataOutputStream.writeInt(nArray[n]);
            }
            for (n = 0; n < 8; ++n) {
                int n2 = playerInfo.itemLoadout[playerInfo.gun][n];
                dataOutputStream.writeByte(n2 < -128 || n2 > 127 ? -2 : n2);
            }
            for (n = 0; n < 6; ++n) {
                dataOutputStream.writeShort(playerInfo.equipID[playerInfo.gun][n]);
            }
            RelayService.send((byte)14, message);
        }
        catch (IOException iOException) {
            iOException.printStackTrace();
        }
    }

    public static void requestRoomList() {
        RelayService.send((byte)2, null);
    }

    public static void createRoom() {
        PlayerInfo playerInfo = TerrainMidlet.myInfo;
        try {
            Message message = new Message(3);
            DataOutputStream dataOutputStream = message.writer();
            dataOutputStream.writeUTF("Ph\u00f2ng c\u1ee7a " + (playerInfo == null || playerInfo.name == null ? "?" : playerInfo.name));
            dataOutputStream.writeByte(8);
            RelayService.send((byte)3, message);
        }
        catch (IOException iOException) {
            iOException.printStackTrace();
        }
    }

    public static void joinRoom(int n) {
        try {
            Message message = new Message(4);
            message.writer().writeInt(n);
            RelayService.send((byte)4, message);
        }
        catch (IOException iOException) {
            iOException.printStackTrace();
        }
    }

    public static void leaveRoom() {
        RelayService.send((byte)5, null);
    }

    public static void sendChat(String string) {
        try {
            Message message = new Message(9);
            message.writer().writeUTF(string);
            RelayService.send((byte)9, message);
        }
        catch (IOException iOException) {
            iOException.printStackTrace();
        }
    }

    public static RelayMemberInfo findMember(int n) {
        for (int i = 0; i < members.size(); ++i) {
            RelayMemberInfo relayMemberInfo = (RelayMemberInfo)members.elementAt(i);
            if (relayMemberInfo.sessionId != n) continue;
            return relayMemberInfo;
        }
        return null;
    }

    static void openLobby() {
        if (CCanvas.relayLobbyScr == null) {
            CCanvas.relayLobbyScr = new RelayLobbyScr();
        }
        CCanvas.relayLobbyScr.show();
    }

    static void openRoom() {
        if (CCanvas.prepareScr == null) {
            CCanvas.prepareScr = new PrepareScr();
        }
        BoardListScr.boardName = roomName;
        RelayService.rebuildPrepareRoster();
        CCanvas.prepareScr.show();
    }

    static void rebuildPrepareRoster() {
        int n;
        if (CCanvas.prepareScr == null || state != 3) {
            return;
        }
        PrepareScr.isBossRoom = false;
        PrepareScr.isPvpBotRoom = false;
        PrepareScr.isRelayRoom = true;
        PrepareScr.currLevel = 0;
        Vector<PlayerInfo> vector = new Vector<PlayerInfo>();
        for (n = 0; n < members.size(); ++n) {
            RelayMemberInfo relayMemberInfo = (RelayMemberInfo)members.elementAt(n);
            PlayerInfo playerInfo = new PlayerInfo();
            playerInfo.IDDB = relayMemberInfo.sessionId;
            playerInfo.name = relayMemberInfo.name;
            playerInfo.gun = (byte)relayMemberInfo.gun;
            playerInfo.ensureCombatEquip();
            vector.addElement(playerInfo);
        }
        while (vector.size() < 8) {
            vector.addElement(new PlayerInfo());
            ++n;
        }
        CCanvas.prepareScr.setPlayers(hostId, 0, vector);
    }
}

