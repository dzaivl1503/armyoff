/*
 * Decompiled with CFR 0.152.
 */
package network;

import com.teamobi.mobiarmy2.GameMidlet;
import coreLG.CCanvas;
import coreLG.TerrainMidlet;
import java.io.DataInputStream;
import java.io.IOException;
import network.IMessageHandler;
import network.Message;
import network.RelayMemberInfo;
import network.RelayRoomInfo;
import network.RelayService;
import screen.PrepareScr;

public class RelayHandler
implements IMessageHandler {
    public void onConnectOK() {
        RelayService.sendHello();
    }

    public void onConnectionFail() {
        if (RelayService.state != 0) {
            RelayService.quit();
            CCanvas.startOKDlg("Kh\u00f4ng k\u1ebft n\u1ed1i \u0111\u01b0\u1ee3c server multiplayer. Th\u1eed l\u1ea1i sau.");
        }
    }

    public void onDisconnected() {
        if (RelayService.state != 0) {
            RelayService.quit();
            CCanvas.startOKDlg("M\u1ea5t k\u1ebft n\u1ed1i server multiplayer.");
        }
        System.out.println("RELAYCLI DISCONNECTED");
    }

    public void onMessage(Message message) {
        try {
            DataInputStream dataInputStream = message.reader();
            switch (message.command) {
                case 1: {
                    this.onHello(dataInputStream);
                    break;
                }
                case 2: {
                    this.onRoomList(dataInputStream);
                    break;
                }
                case 3: {
                    this.onCreateRoom(dataInputStream);
                    break;
                }
                case 4: {
                    this.onJoinRoom(dataInputStream);
                    break;
                }
                case 5: {
                    this.onLeaveAck();
                    break;
                }
                case 6: {
                    this.onMemberJoin(dataInputStream);
                    break;
                }
                case 7: {
                    this.onMemberLeave(dataInputStream);
                    break;
                }
                case 8: {
                    this.onNewHost(dataInputStream);
                    break;
                }
                case 9: {
                    this.onChat(dataInputStream);
                    break;
                }
                case 12: {
                    this.onError(dataInputStream);
                    break;
                }
                case 11: {
                    break;
                }
                case 10: {
                    break;
                }
            }
        }
        catch (IOException iOException) {
            iOException.printStackTrace();
        }
    }

    private void onHello(DataInputStream dataInputStream) throws IOException {
        if (dataInputStream.readByte() != 1) {
            String string = dataInputStream.readUTF();
            RelayService.quit();
            CCanvas.endDlg();
            CCanvas.startOKDlg("Server t\u1eeb ch\u1ed1i: " + string);
            return;
        }
        RelayService.sessionId = dataInputStream.readInt();
        RelayService.overrideIDDB();
        RelayService.state = 2;
        RelayService.sendProfile();
        RelayService.requestRoomList();
        CCanvas.endDlg();
        RelayService.openLobby();
        System.out.println("RELAYCLI HELLO_OK id=" + RelayService.sessionId);
    }

    private void onRoomList(DataInputStream dataInputStream) throws IOException {
        RelayService.rooms.removeAllElements();
        int n = dataInputStream.readShort();
        for (int i = 0; i < n; ++i) {
            RelayRoomInfo relayRoomInfo = new RelayRoomInfo();
            relayRoomInfo.id = dataInputStream.readInt();
            relayRoomInfo.name = dataInputStream.readUTF();
            relayRoomInfo.cur = dataInputStream.readByte();
            relayRoomInfo.max = dataInputStream.readByte();
            relayRoomInfo.inBattle = dataInputStream.readByte() != 0;
            relayRoomInfo.hostName = dataInputStream.readUTF();
            relayRoomInfo.hostVersion = dataInputStream.readUTF();
            RelayService.rooms.addElement(relayRoomInfo);
        }
        if (CCanvas.relayLobbyScr != null) {
            CCanvas.relayLobbyScr.onRoomsChanged();
        }
        System.out.println("RELAYCLI ROOM_LIST n=" + n);
    }

    private void onCreateRoom(DataInputStream dataInputStream) throws IOException {
        if (dataInputStream.readByte() != 1) {
            CCanvas.startOKDlg(dataInputStream.readUTF());
            return;
        }
        RelayService.resetRoom();
        RelayService.roomId = dataInputStream.readInt();
        RelayService.roomName = "Ph\u00f2ng " + RelayService.roomId;
        RelayService.hostId = RelayService.sessionId;
        RelayMemberInfo relayMemberInfo = new RelayMemberInfo();
        relayMemberInfo.sessionId = RelayService.sessionId;
        relayMemberInfo.name = TerrainMidlet.myInfo == null ? "?" : TerrainMidlet.myInfo.name;
        relayMemberInfo.version = GameMidlet.OFFLINE_VERSION_TEXT;
        relayMemberInfo.gun = TerrainMidlet.myInfo == null ? (byte)0 : TerrainMidlet.myInfo.gun;
        RelayService.members.addElement(relayMemberInfo);
        RelayService.state = 3;
        RelayService.openRoom();
        System.out.println("RELAYCLI ROOM_CREATED id=" + RelayService.roomId);
    }

    private void onJoinRoom(DataInputStream dataInputStream) throws IOException {
        if (dataInputStream.readByte() != 1) {
            CCanvas.startOKDlg(dataInputStream.readUTF());
            return;
        }
        RelayService.resetRoom();
        RelayService.roomId = dataInputStream.readInt();
        RelayService.roomName = dataInputStream.readUTF();
        dataInputStream.readByte();
        RelayService.hostId = dataInputStream.readInt();
        int n = dataInputStream.readShort();
        for (int i = 0; i < n; ++i) {
            RelayMemberInfo relayMemberInfo = new RelayMemberInfo();
            relayMemberInfo.sessionId = dataInputStream.readInt();
            relayMemberInfo.name = dataInputStream.readUTF();
            relayMemberInfo.version = dataInputStream.readUTF();
            relayMemberInfo.gun = dataInputStream.readByte();
            RelayService.members.addElement(relayMemberInfo);
        }
        RelayService.state = 3;
        RelayService.openRoom();
        System.out.println("RELAYCLI ROOM_JOINED id=" + RelayService.roomId + " members=" + n);
    }

    private void onLeaveAck() {
        RelayService.resetRoom();
        RelayService.state = 2;
        RelayService.requestRoomList();
        RelayService.openLobby();
    }

    private void onMemberJoin(DataInputStream dataInputStream) throws IOException {
        RelayMemberInfo relayMemberInfo = new RelayMemberInfo();
        relayMemberInfo.sessionId = dataInputStream.readInt();
        relayMemberInfo.name = dataInputStream.readUTF();
        relayMemberInfo.version = dataInputStream.readUTF();
        relayMemberInfo.gun = dataInputStream.readByte();
        RelayService.members.addElement(relayMemberInfo);
        RelayService.rebuildPrepareRoster();
        System.out.println("RELAYCLI MEMBER_JOIN id=" + relayMemberInfo.sessionId + " name=" + relayMemberInfo.name + " total=" + RelayService.members.size());
    }

    private void onMemberLeave(DataInputStream dataInputStream) throws IOException {
        int n = dataInputStream.readInt();
        RelayMemberInfo relayMemberInfo = RelayService.findMember(n);
        if (relayMemberInfo != null) {
            RelayService.members.removeElement(relayMemberInfo);
            RelayService.rebuildPrepareRoster();
        }
        System.out.println("RELAYCLI MEMBER_LEAVE id=" + n + " total=" + RelayService.members.size());
    }

    private void onNewHost(DataInputStream dataInputStream) throws IOException {
        RelayService.hostId = dataInputStream.readInt();
        RelayService.rebuildPrepareRoster();
    }

    private void onChat(DataInputStream dataInputStream) throws IOException {
        int n = dataInputStream.readInt();
        String string = dataInputStream.readUTF();
        if (CCanvas.prepareScr != null && PrepareScr.isRelayRoom) {
            CCanvas.prepareScr.showChat(n, string, 90);
        }
        System.out.println("RELAYCLI CHAT from=" + n + " text=" + string);
    }

    private void onError(DataInputStream dataInputStream) throws IOException {
        dataInputStream.readByte();
        CCanvas.startOKDlg(dataInputStream.readUTF());
    }
}

