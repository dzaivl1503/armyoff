/*
 * Decompiled with CFR 0.152.
 */
package network;

import CLib.RMS;
import CLib.mImage;
import CLib.mSystem;
import Equipment.Equip;
import Equipment.PlayerEquip;
import InApp.MainActivity;
import com.teamobi.mobiarmy2.GameMidlet;
import coreLG.CCanvas;
import coreLG.TerrainMidlet;
import effect.GiftEffect;
import item.BM;
import item.Bullet;
import item.Item;
import java.util.Vector;
import map.MM;
import model.BoardInfo;
import model.CRes;
import model.CTime;
import model.Clan;
import model.ClanItem;
import model.Fomula;
import model.Font;
import model.IAction;
import model.ImageIcon;
import model.Language;
import model.LuckyGift;
import model.MaterialIconMn;
import model.Mission;
import model.MoneyInfo;
import model.MsgInfo;
import model.PlayerInfo;
import model.RoomInfo;
import model.TimeBomb;
import network.Command;
import network.GameLogicHandler;
import network.GameService;
import network.IGameLogicHandler;
import network.IMessageHandler;
import network.Message;
import network.Session_ME;
import player.CPlayer;
import player.PM;
import screen.BoardListScr;
import screen.CScreen;
import screen.ChangePlayerCSr;
import screen.GameScr;
import screen.LevelScreen;
import screen.LoginScr;
import screen.LuckyGame;
import screen.LuckyGifrScreen;
import screen.MenuScr;
import screen.MoneyScr;
import screen.MoneyScrIOS;
import screen.PrepareScr;
import screen.QuangCao;
import screen.RoomListScr2;
import shop.ShopBietDoi;
import shop.ShopEquipment;
import shop.ShopItem;

public class MessageHandler
implements IMessageHandler {
    IGameLogicHandler gameLogicHandler;
    protected static MessageHandler instance;
    public static boolean nextTurnFlag;
    public static boolean lag;
    public static Object LOCK;
    public static int dem;
    public static long currt;
    public static long timePing;

    public static MessageHandler gI() {
        if (instance == null) {
            instance = new MessageHandler();
        }
        return instance;
    }

    public void onConnectOK() {
        this.gameLogicHandler.onConnectOK();
    }

    public void onConnectionFail() {
        this.gameLogicHandler.onConnectFail();
    }

    public void onDisconnected() {
        this.gameLogicHandler.onDisconnect();
    }

    /*
     * WARNING - void declaration
     */
    public void onMessage(Message message) {
        if (message != null) {
            try {
                block7 : switch (message.command) {
                    case 4: {
                        this.gameLogicHandler.onLoginFail(message.reader().readUTF());
                        break;
                    }
                    case 3: {
                        int string;
                        int vector = 0;
                        CRes.out("=========> Cmd_Server2Client.LOGIN_SUCESS:");
                        GameService.gI().platform_request();
                        GameService.gI().bangxephang((byte)-1, -1);
                        currt = System.currentTimeMillis();
                        GameService.gI().ping(dem, -1L);
                        TerrainMidlet.myInfo = new PlayerInfo();
                        TerrainMidlet.myInfo.name = LoginScr.user.toLowerCase();
                        if (GameMidlet.server == 2) {
                        }
                        TerrainMidlet.myInfo.IDDB = message.reader().readInt();
                        TerrainMidlet.myInfo.xu = message.reader().readInt();
                        TerrainMidlet.myInfo.luong = message.reader().readInt();
                        TerrainMidlet.myInfo.gun = message.reader().readByte();
                        CRes.out(" TerrainMidlet.myInfo.gun " + TerrainMidlet.myInfo.gun);
                        TerrainMidlet.myInfo.clanID = message.reader().readShort();
                        TerrainMidlet.myInfo.isMaster = message.reader().readByte();
                        boolean i = false;
                        while (vector < 10) {
                            int n6;
                            TerrainMidlet.isVip[vector] = message.reader().readBoolean();
                            if (!TerrainMidlet.isVip[vector]) {
                                for (n6 = 0; n6 < 5; ++n6) {
                                    TerrainMidlet.myInfo.equipID[vector][n6] = message.reader().readShort();
                                }
                            } else {
                                for (n6 = 0; n6 < 5; ++n6) {
                                    TerrainMidlet.myInfo.equipVipID[vector][n6] = message.reader().readShort();
                                }
                                for (n6 = 0; n6 < 5; ++n6) {
                                    TerrainMidlet.myInfo.equipID[vector][n6] = message.reader().readShort();
                                }
                            }
                            ++vector;
                        }
                        Vector<Item> by = new Vector<Item>();
                        for (int i2 = 0; i2 < 36; ++i2) {
                            string = message.reader().readByte();
                            int s = message.reader().readInt();
                            s = message.reader().readInt();
                            by.addElement(new Item((byte)i2, (byte)string, s, s));
                        }
                        ShopItem.setItemVector(by);
                        for (string = 0; string < 10; ++string) {
                            if (string < 3) {
                                ChangePlayerCSr.isUnlock[string] = 1;
                                ChangePlayerCSr.gunXu[string] = 0;
                                ChangePlayerCSr.gunLuong[string] = 0;
                                continue;
                            }
                            ChangePlayerCSr.isUnlock[string] = message.reader().readByte();
                            ChangePlayerCSr.gunXu[string] = message.reader().readShort() * 1000;
                            ChangePlayerCSr.gunLuong[string] = message.reader().readShort();
                        }
                        MenuScr.suKienStr = message.reader().readUTF().toUpperCase();
                        MenuScr.linkWapStr = message.reader().readUTF().toUpperCase();
                        MenuScr.linkTeam = message.reader().readUTF().toUpperCase();
                        MenuScr.getIdMenu(0);
                        if (!LoginScr.isLoadData) {
                            CCanvas.sendMapData();
                            break;
                        }
                        GameService.gI().sendVersion((byte)5, (byte)0);
                        GameLogicHandler.gI().onLoginSuccess();
                        break;
                    }
                    case -28: {
                        Vector<RoomInfo> vector = new Vector<RoomInfo>();
                        byte n7 = message.reader().readByte();
                        int i = -1;
                        int n = -1;
                        while (message.reader().available() > 0) {
                            Object i2;
                            RoomInfo by13 = new RoomInfo();
                            by13.id = message.reader().readByte();
                            if (by13.id != -1) {
                                by13.boardID = message.reader().readByte();
                                by13.mapID = message.reader().readByte();
                                by13.playerMax = String.valueOf(message.reader().readByte()) + "/" + message.reader().readByte();
                                by13.money = message.reader().readInt();
                                i2 = by13.mapID == 100 ? Language.random() : MM.mapName[by13.mapID];
                                by13.name = "P" + by13.id + "-" + by13.boardID + " " + (CCanvas.width > 200 ? "(" + (String)i2 + ")" : "");
                                by13.lv = (byte)i;
                                vector.addElement(by13);
                                continue;
                            }
                            by13.name = message.reader().readUTF();
                            vector.addElement(by13);
                            n = (byte)(n + 1);
                            i = (byte)(i + 1);
                            i2 = new RoomInfo();
                            ((RoomInfo)i2).name = Language.createZone();
                            ((RoomInfo)i2).lv = (byte)i;
                            ((RoomInfo)i2).boardID = (byte)-1;
                            vector.addElement((RoomInfo)i2);
                        }
                        CCanvas.endDlg();
                        CCanvas.roomListScr2.isEmptyRoom = true;
                        CCanvas.roomListScr2.setRoomList(vector);
                        CCanvas.roomListScr2.show();
                        if (CCanvas.gameScr == null) break;
                        CCanvas.gameScr.onClearMap();
                        CCanvas.gameScr = null;
                        break;
                    }
                    case 6: {
                        Vector<RoomInfo> vector = new Vector<RoomInfo>();
                        while (message.reader().available() > 0) {
                            byte bl;
                            RoomInfo sArray = new RoomInfo();
                            sArray.id = message.reader().readByte();
                            sArray.roomFree = bl = message.reader().readByte();
                            sArray.roomWait = message.reader().readByte();
                            sArray.lv = message.reader().readByte();
                            vector.addElement(sArray);
                        }
                        CCanvas.endDlg();
                        if (CCanvas.roomListScr2 == null) {
                            CCanvas.roomListScr2 = new RoomListScr2();
                        }
                        CCanvas.roomListScr2.isEmptyRoom = false;
                        CCanvas.roomListScr2.setRoomList(vector);
                        CCanvas.roomListScr2.show();
                        if (CCanvas.gameScr != null) {
                            CCanvas.gameScr.onClearMap();
                            CCanvas.gameScr = null;
                        }
                        GameService.gI().changeRoomName();
                        break;
                    }
                    case 7: {
                        if (CCanvas.curScr == CCanvas.prepareScr) {
                            return;
                        }
                        Vector<BoardInfo> n = new Vector<BoardInfo>();
                        byte string = message.reader().readByte();
                        while (message.reader().available() > 0) {
                            BoardInfo moneyInfo = new BoardInfo();
                            moneyInfo.boardID = message.reader().readByte();
                            moneyInfo.nPlayer = message.reader().readByte();
                            moneyInfo.maxPlayer = message.reader().readByte();
                            moneyInfo.isPass = message.reader().readBoolean();
                            moneyInfo.money = message.reader().readInt();
                            moneyInfo.isPlaying = message.reader().readBoolean();
                            moneyInfo.name = message.reader().readUTF();
                            moneyInfo.mode = message.reader().readByte();
                            n.addElement(moneyInfo);
                        }
                        CCanvas.endDlg();
                        if (CCanvas.boardListScr == null) {
                            CCanvas.boardListScr = new BoardListScr();
                        }
                        CCanvas.boardListScr.roomID = string;
                        CCanvas.boardListScr.setBoardList(n);
                        CCanvas.boardListScr.show();
                        if (CCanvas.gameScr == null) break;
                        CCanvas.gameScr.onClearMap();
                        CCanvas.gameScr = null;
                        break;
                    }
                    case 8: {
                        int playerInfo = message.reader().readInt();
                        int string = message.reader().readInt();
                        byte object = message.reader().readByte();
                        byte by6 = message.reader().readByte();
                        Vector<PlayerInfo> n = new Vector<PlayerInfo>();
                        while (message.reader().available() > 0) {
                            PlayerInfo gift = new PlayerInfo();
                            gift.IDDB = message.reader().readInt();
                            if (gift.IDDB == -1) {
                                gift.name = "";
                            } else {
                                gift.clanID = message.reader().readShort();
                                gift.name = message.reader().readUTF();
                                gift.xu = message.reader().readInt();
                                gift.level2 = message.reader().readUnsignedByte();
                                gift.getQuanHam();
                                gift.gun = message.reader().readByte();
                                for (int object2 = 0; object2 < 5; ++object2) {
                                    gift.equipID[gift.gun][object2] = message.reader().readShort();
                                    gift.getMyEquip(1);
                                }
                                gift.isReady = message.reader().readBoolean();
                            }
                            n.addElement(gift);
                        }
                        CCanvas.endDlg();
                        GameScr.trainingMode = false;
                        CCanvas.prepareScr.setPlayers(playerInfo, string, n);
                        for (int string9 = 0; string9 < n.size(); ++string9) {
                            PlayerInfo string2 = (PlayerInfo)n.elementAt(string9);
                            if (string2.IDDB != playerInfo) continue;
                            string2.isReady = true;
                        }
                        if (CCanvas.prepareScr == null) {
                            CCanvas.prepareScr = new PrepareScr();
                        }
                        CCanvas.prepareScr.show();
                        CCanvas.prepareScr.onResetPrepare();
                        CCanvas.prepareScr.getIcon();
                        this.gameLogicHandler.onJoinGameSuccess(playerInfo, string, n, object);
                        break;
                    }
                    case 12: {
                        PlayerInfo n = new PlayerInfo();
                        byte playerInfo = message.reader().readByte();
                        n.IDDB = message.reader().readInt();
                        n.clanID = message.reader().readShort();
                        n.name = message.reader().readUTF();
                        n.level2 = message.reader().readUnsignedByte();
                        n.getQuanHam();
                        n.gun = message.reader().readByte();
                        for (int n9 = 0; n9 < 5; ++n9) {
                            n.equipID[n.gun][n9] = message.reader().readShort();
                            n.getMyEquip(2);
                        }
                        n.isReady = false;
                        CCanvas.prepareScr.setAt(playerInfo, n);
                        GameService.gI().getClanIcon(n.clanID);
                        this.gameLogicHandler.onSomeOneJoinBoard(playerInfo, n);
                        break;
                    }
                    case 14: {
                        int n = message.reader().readInt();
                        int playerInfo = message.reader().readInt();
                        CCanvas.prepareScr.playerLeave(n);
                        CCanvas.prepareScr.setOwner(playerInfo);
                        if (CCanvas.curScr == CCanvas.gameScr) {
                            for (int by3 = 0; by3 < PM.p.length; ++by3) {
                                CPlayer by = PM.p[by3];
                                if (by == null || n != by.IDDB) continue;
                                if (PrepareScr.currLevel != 7) {
                                    PM.p[by3].setState((byte)5);
                                    break;
                                }
                                PM.p[by3] = null;
                                break;
                            }
                        }
                        if (PrepareScr.currLevel != 7) {
                            GameScr.cam.setPlayerMode(PM.curP);
                        }
                        Session_ME.receiveSynchronized = 0;
                        break;
                    }
                    case 16: {
                        int n = message.reader().readInt();
                        boolean i = message.reader().readBoolean();
                        this.gameLogicHandler.onSomeOneReady(n, i);
                        break;
                    }
                    case 19: {
                        message.reader().readShort();
                        int n = message.reader().readInt();
                        CCanvas.prepareScr.setMoney(n);
                        break;
                    }
                    case 9: {
                        int n = message.reader().readInt();
                        String moneyInfo = message.reader().readUTF();
                        this.gameLogicHandler.onChatFromBoard(moneyInfo, n);
                        break;
                    }
                    case 11: {
                        message.reader().readShort();
                        int vector = message.reader().readInt();
                        String by = message.reader().readUTF();
                        this.gameLogicHandler.onKicked(vector, by);
                        CRes.out("NHAN M KICK id: " + vector);
                        break;
                    }
                    case 29: {
                        Vector<PlayerInfo> vector = new Vector<PlayerInfo>();
                        while (message.reader().available() > 0) {
                            PlayerInfo n8 = new PlayerInfo();
                            n8.IDDB = message.reader().readInt();
                            n8.name = message.reader().readUTF();
                            n8.xu = message.reader().readInt();
                            n8.gun = message.reader().readByte();
                            n8.clanID = message.reader().readShort();
                            n8.isReady = message.reader().readByte() != 0;
                            n8.level2 = message.reader().readUnsignedByte();
                            n8.level2Percen = message.reader().readByte();
                            n8.getQuanHam();
                            short[] s2 = new short[5];
                            for (int i = 0; i < 5; ++i) {
                                s2[i] = message.reader().readShort();
                                n8.equipID[n8.gun][i] = s2[i];
                                n8.getMyEquip(3);
                            }
                            vector.addElement(n8);
                        }
                        this.gameLogicHandler.onFriendList(vector);
                        break;
                    }
                    case 36: {
                        Vector<PlayerInfo> by = new Vector<PlayerInfo>();
                        while (message.reader().available() > 0) {
                            PlayerInfo by2 = new PlayerInfo();
                            by2.IDDB = message.reader().readInt();
                            by2.name = message.reader().readUTF();
                            by.addElement(by2);
                        }
                        this.gameLogicHandler.onSearchResult(by);
                        break;
                    }
                    case 32: {
                        byte by = message.reader().readByte();
                        this.gameLogicHandler.onAddFriendResult(by);
                        break;
                    }
                    case 33: {
                        byte msgInfo = message.reader().readByte();
                        this.gameLogicHandler.onDelFriendResult(msgInfo);
                        break;
                    }
                    case 5: {
                        MsgInfo vector = new MsgInfo();
                        vector.fromID = message.reader().readInt();
                        vector.fromName = message.reader().readUTF();
                        vector.message = message.reader().readUTF();
                        this.gameLogicHandler.onChatFrom(vector);
                        break;
                    }
                    case 122: {
                        try {
                            Object s4;
                            Vector<Object> exception = new Vector<Object>();
                            CCanvas.isPurchaseIOS = CCanvas.isIos();
                            if (CCanvas.isIos()) {
                                MoneyInfo s3;
                                for (int s = 0; s < 5; ++s) {
                                    s3 = new MoneyInfo();
                                    s3.id = MainActivity.google_productIds[s];
                                    s3.info = MainActivity.google_listGems[s];
                                    s3.smsContent = MainActivity.google_price[s];
                                    exception.addElement(s3);
                                }
                                if (GameMidlet.versioncode < 11 && GameMidlet.versionByte >= 240) {
                                    s3 = new MoneyInfo();
                                    s3.id = "napWeb";
                                    s3.info = message.reader().readUTF();
                                    s3.smsContent = "";
                                    MoneyScrIOS.url_Nap = message.reader().readUTF();
                                    exception.addElement(s3);
                                }
                                this.gameLogicHandler.onMoneyInfo(exception);
                                break;
                            }
                            if (GameMidlet.versionByte >= 240) {
                                MoneyInfo s = new MoneyInfo();
                                s.id = "napWeb";
                                s.info = message.reader().readUTF();
                                s.smsContent = "";
                                MoneyScr.url_Nap = message.reader().readUTF();
                                exception.addElement(s);
                                this.gameLogicHandler.onMoneyInfo(exception);
                                break;
                            }
                            byte s = message.reader().readByte();
                            if (s == 0) {
                                while (message.reader().available() > 0) {
                                    s4 = new MoneyInfo();
                                    ((MoneyInfo)s4).id = message.reader().readUTF();
                                    ((MoneyInfo)s4).info = message.reader().readUTF();
                                    ((MoneyInfo)s4).smsContent = message.reader().readUTF();
                                    exception.addElement(s4);
                                }
                                this.gameLogicHandler.onMoneyInfo(exception);
                            }
                            if (s == 2) {
                                s4 = message.reader().readUTF();
                                String sArray = message.reader().readUTF();
                                String i = message.reader().readUTF();
                                this.gameLogicHandler.onChargeMoneySms((String)s4, sArray, i);
                            }
                        }
                        catch (Exception exception) {}
                        break;
                    }
                    case 45: {
                        this.gameLogicHandler.onServerMessage(message.reader().readUTF());
                        break;
                    }
                    case 46: {
                        this.gameLogicHandler.onServerInfo(message.reader().readUTF());
                        break;
                    }
                    case 48: {
                        try {
                            this.gameLogicHandler.onVersion(message.reader().readUTF(), message.reader().readUTF());
                        }
                        catch (Exception n) {}
                        break;
                    }
                    case 47: {
                        this.gameLogicHandler.onAdminCommandResponse(message.reader().readUTF());
                        break;
                    }
                    case 52: {
                        int string = message.reader().readInt();
                        int by4 = message.reader().readInt();
                        int by5 = message.reader().readInt();
                        if (string == TerrainMidlet.myInfo.IDDB) {
                            TerrainMidlet.myInfo.xu = by5;
                        }
                        if (CCanvas.curScr != CCanvas.gameScr) break;
                        CCanvas.gameScr.activeMoney2Fly(by4, string);
                        break;
                    }
                    case 10: {
                        String sArray = message.reader().readUTF();
                        this.gameLogicHandler.onSetMoneyError(sArray);
                        break;
                    }
                    case 20: {
                        byte n;
                        CCanvas.isInGameRunTime = true;
                        CCanvas.prepareScr.bossInfos.removeAllElements();
                        short[] by = new short[5];
                        if (GameScr.trainingMode) {
                            for (n = 0; n < 5; ++n) {
                                by[n] = message.reader().readShort();
                            }
                        }
                        n = message.reader().readByte();
                        byte by9 = message.reader().readByte();
                        int string = message.reader().readUnsignedShort();
                        boolean playerInfo = false;
                        int string6 = PrepareScr.currLevel == 7 ? (int)message.reader().readByte() : 8;
                        short[] by42 = new short[string6];
                        short[] sArray = new short[string6];
                        short[] sArray2 = new short[string6];
                        for (int i = 0; i < string6; ++i) {
                            by42[i] = message.reader().readShort();
                            if (by42[i] != -1) {
                                sArray[i] = message.reader().readShort();
                                sArray2[i] = message.reader().readShort();
                                continue;
                            }
                            sArray[i] = -1;
                        }
                        if (CCanvas.gameScr == null) {
                            CCanvas.gameScr = new GameScr();
                        }
                        if (!GameScr.trainingMode) {
                            CCanvas.gameScr.initGame(n, by9, by42, sArray, sArray2, string);
                            CCanvas.gameScr.show(CCanvas.prepareScr);
                        } else {
                            CCanvas.menuScr.doTraining(n, by9, by42, sArray, sArray2, by);
                        }
                        if (CCanvas.prepareScr.itemCur[4] >= 0) {
                            ShopItem.getI((int)12).num = (byte)(ShopItem.getI((int)12).num - 1);
                        }
                        if (CCanvas.prepareScr.itemCur[5] >= 0) {
                            ShopItem.getI((int)13).num = (byte)(ShopItem.getI((int)13).num - 1);
                        }
                        if (CCanvas.prepareScr.itemCur[6] >= 0) {
                            ShopItem.getI((int)14).num = (byte)(ShopItem.getI((int)14).num - 1);
                        }
                        if (CCanvas.prepareScr.itemCur[7] >= 0) {
                            ShopItem.getI((int)15).num = (byte)(ShopItem.getI((int)15).num - 1);
                        }
                        CCanvas.endDlg();
                        CCanvas.menu.showMenu = false;
                        BM.removeTornado();
                        CScreen.isSetClip = false;
                        nextTurnFlag = true;
                        break;
                    }
                    case 93: {
                        byte by = message.reader().readByte();
                        short by10 = message.reader().readShort();
                        short n = message.reader().readShort();
                        GameScr.pm.flyTo(by, by10, n);
                        GameScr.cam.setPlayerMode(by);
                        if (CCanvas.curScr != CCanvas.gameScr) break;
                        Session_ME.receiveSynchronized = 1;
                        break;
                    }
                    case 21: {
                        byte by = message.reader().readByte();
                        short by11 = message.reader().readShort();
                        short n15 = message.reader().readShort();
                        CRes.out("=========================> rec move = " + by11 + "_" + n15);
                        PM.p[by].xToNow = by11;
                        PM.p[by].yToNow = n15;
                        if (PM.p[by].x == by11 && PM.p[by].y == n15) break;
                        GameScr.pm.movePlayer(by, by11, n15);
                        if (by == GameScr.myIndex || PM.p[by].isRunSpeed) break;
                        Session_ME.receiveSynchronized = 1;
                        break;
                    }
                    case 53: {
                        byte by = message.reader().readByte();
                        short by66 = message.reader().readShort();
                        short byArray2 = message.reader().readShort();
                        GameScr.pm.updatePlayerXY(by, by66, byArray2);
                        PM.p[by].bulletType = (byte)-1;
                        break;
                    }
                    case 22:
                    case 84: {
                        byte who = message.reader().readByte();
                        byte bType = message.reader().readByte();
                        byte string5 = message.reader().readByte();
                        byte iVal = message.reader().readByte();
                        short i3 = message.reader().readShort();
                        short by43 = message.reader().readShort();
                        short s = message.reader().readShort();
                        byte by = 0;
                        if (iVal == 17 || iVal == 19) {
                            by = message.reader().readByte();
                        }
                        if (iVal == 14 || iVal == 40) {
                            BM.angle = message.reader().readByte();
                            BM.force = message.reader().readByte();
                        }
                        if (iVal == 44 || iVal == 45 || iVal == 47) {
                            BM.angle = message.reader().readByte();
                            CRes.out("ANGLE= " + BM.angle);
                        }
                        byte by3 = message.reader().readByte();
                        int n2 = message.reader().readByte();
                        BM.nOrbit = n2;
                        short[][] sArrayArray = new short[n2][];
                        short[][] sArrayArray2 = new short[n2][];
                        short[][] sArrayArray3 = new short[n2][];
                        short[][] sArrayArray4 = new short[n2][];
                        for (int j = 0; j < n2; ++j) {
                            int n3;
                            int n = message.reader().readShort();
                            short[] sArray = new short[n];
                            short[] sArray3 = new short[n];
                            short[] sArray4 = new short[n];
                            short[] sArray5 = new short[n];
                            if (by == 0) {
                                for (n3 = 0; n3 < n; ++n3) {
                                    if (n3 == 0) {
                                        sArray[n3] = message.reader().readShort();
                                        sArray3[n3] = message.reader().readShort();
                                        sArray4[n3] = sArray[n3];
                                        sArray5[n3] = sArray3[n3];
                                        continue;
                                    }
                                    if (n3 == n - 1 && iVal == 49) {
                                        try {
                                            sArray4[n3] = message.reader().readShort();
                                            sArray5[n3] = message.reader().readShort();
                                            if (iVal != 49) break;
                                            Bullet.dXLaser = message.reader().readByte();
                                            Bullet.dYLaser = message.reader().readByte();
                                            if (Bullet.dXLaser == 0) break;
                                            while (Math.abs(Bullet.dXLaser) < 15) {
                                                Bullet.dXLaser += Bullet.dXLaser;
                                                Bullet.dYLaser += Bullet.dYLaser;
                                            }
                                            break;
                                        }
                                        catch (Exception exception) {
                                            CRes.out("error");
                                            break;
                                        }
                                    }
                                    sArray[n3] = message.reader().readByte();
                                    sArray3[n3] = message.reader().readByte();
                                    sArray4[n3] = (short)(sArray4[n3 - 1] + sArray[n3]);
                                    sArray5[n3] = (short)(sArray5[n3 - 1] + sArray3[n3]);
                                }
                            }
                            if (by == 1) {
                                for (n3 = 0; n3 < n; ++n3) {
                                    sArray4[n3] = message.reader().readShort();
                                    sArray5[n3] = message.reader().readShort();
                                }
                            }
                            sArrayArray[j] = sArray4;
                            sArrayArray2[j] = sArray5;
                            if (iVal != 48) continue;
                            int n4 = message.reader().readByte();
                            short[] sArray6 = new short[n4];
                            short[] sArray7 = new short[n4];
                            for (int k = 0; k < n4; ++k) {
                                sArray6[k] = message.reader().readShort();
                                sArray7[k] = message.reader().readShort();
                            }
                            sArrayArray3[j] = sArray6;
                            sArrayArray4[j] = sArray7;
                        }
                        byte by4 = message.reader().readByte();
                        int n = -1;
                        int n5 = -1;
                        if (by4 == 1 || by4 == 2) {
                            n = message.reader().readShort();
                            n5 = message.reader().readShort();
                        } else if (by4 != 3) {
                        }
                        PM.p[string5].shoot(by, string5, i3, by43, iVal, sArrayArray, sArrayArray2, by3, by, s, sArrayArray3, sArrayArray4, n, n5);
                        if (CCanvas.curScr != CCanvas.gameScr) break;
                        Session_ME.receiveSynchronized = 1;
                        break;
                    }
                    case 51: {
                        byte by = message.reader().readByte();
                        int by12 = message.reader().readUnsignedShort();
                        byte n21 = message.reader().readByte();
                        if (PrepareScr.currLevel != 7) {
                            if (PM.p[by] == null) break;
                            PM.p[by].updateHP(by12, n21);
                            break;
                        }
                        if (PM.findPlayerByIndex(by) == null) break;
                        PM.findPlayerByIndex(by).updateHP(by12, n21);
                        break;
                    }
                    case 83: {
                        CCanvas.menuScr.show();
                        if (CCanvas.gameScr != null) {
                            CCanvas.gameScr.onClearMap();
                            CCanvas.gameScr = null;
                        }
                        CScreen.isSetClip = true;
                        Session_ME.receiveSynchronized = 0;
                        break;
                    }
                    case 24: {
                        byte by = message.reader().readByte();
                        GameScr.pm.setNextPlayer(by);
                        GameScr.bm.nBull = 0;
                        if (!nextTurnFlag) break;
                        nextTurnFlag = false;
                        CCanvas.endDlg();
                        break;
                    }
                    case -92: {
                        break;
                    }
                    case 25: {
                        byte by = message.reader().readByte();
                        byte string = message.reader().readByte();
                        GameScr.changeWind(by, string);
                        break;
                    }
                    case 26: {
                        byte n = message.reader().readByte();
                        byte byArray = message.reader().readByte();
                        CRes.err("=======> USED ITEM = " + byArray);
                        PM.p[n].UseItem(byArray, true, 0);
                        break;
                    }
                    case 69: {
                        CRes.out("=========> Cmd_Server2Client.CHOOSE_GUN: ");
                        int n = message.reader().readInt();
                        byte string4 = message.reader().readByte();
                        if (n == TerrainMidlet.myInfo.IDDB) {
                            TerrainMidlet.myInfo.gun = string4;
                        }
                        if (CCanvas.curScr == CCanvas.changePScr) {
                            CCanvas.changePScr.onChangeGun();
                        }
                        CCanvas.endDlg();
                        break;
                    }
                    case 70: {
                        break;
                    }
                    case 71: {
                        int by67 = message.reader().readInt();
                        byte exception = message.reader().readByte();
                        this.gameLogicHandler.onChangeTeam(by67, exception);
                        break;
                    }
                    case 50: {
                        byte n = message.reader().readByte();
                        byte exception = message.reader().readByte();
                        int by17 = message.reader().readInt();
                        CCanvas.gameScr.setWin(n, exception, by17);
                        CCanvas.prepareScr.resetReady();
                        CCanvas.prepareScr.readyDelay = 5;
                        Session_ME.receiveSynchronized = 0;
                        break;
                    }
                    case 34: {
                        int by68 = message.reader().readInt();
                        if (by68 != -1) {
                            String byArray = message.reader().readUTF();
                            int vector = message.reader().readInt();
                            byte by18 = message.reader().readByte();
                            byte i = message.reader().readByte();
                            int string12 = message.reader().readInt();
                            int n = message.reader().readInt();
                            int n6 = message.reader().readInt();
                            int n7 = message.reader().readInt();
                            PlayerInfo playerInfo = new PlayerInfo();
                            CCanvas.archScreen.level = by18;
                            CCanvas.archScreen.levelPercen = i;
                            CCanvas.archScreen.xu = vector;
                            CCanvas.archScreen.luong = string12;
                            CCanvas.archScreen.exp = n;
                            CCanvas.archScreen.nextExp = n6;
                            CCanvas.archScreen.cup = n7;
                            CCanvas.archScreen.rank = message.reader().readUTF();
                            if (CCanvas.iconMn.isExist(TerrainMidlet.myInfo.clanID)) {
                                CCanvas.archScreen.imgClan = new mImage(CCanvas.iconMn.getImage(TerrainMidlet.myInfo.clanID));
                            } else {
                                GameService.gI().getClanIcon(TerrainMidlet.myInfo.clanID);
                            }
                            String string = String.valueOf(Language.name()) + ": " + byArray + ". " + Language.money() + ": " + vector + Language.xu() + "-" + string12 + Language.luong() + ". Level:" + by18 + "+" + i + "%";
                            if (CCanvas.curScr == CCanvas.prepareScr) {
                                CCanvas.startOKDlg(string);
                                break;
                            }
                            CCanvas.endDlg();
                            CCanvas.archScreen.show();
                            break;
                        }
                        CCanvas.startOKDlg(Language.cantsee());
                        break;
                    }
                    case 72: {
                        byte by = message.reader().readByte();
                        byte[] by16 = new byte[by];
                        byte[] by69 = new byte[by];
                        for (int string8 = 0; string8 < by; ++string8) {
                            by16[string8] = message.reader().readByte();
                            by69[string8] = message.reader().readByte();
                        }
                        int byArray = message.reader().readInt();
                        int string = message.reader().readInt();
                        ShopItem.receiveAItemBuy(by, by16, by69, byArray, string);
                        break;
                    }
                    case 74: {
                        byte string = message.reader().readByte();
                        ChangePlayerCSr.isUnlock[string + ChangePlayerCSr.gunPassiveIndexSub] = 1;
                        CCanvas.changePScr.doChangePlayer();
                        CCanvas.endDlg();
                        break;
                    }
                    case 42: {
                        GameMidlet.timePingPaint = (int)((mSystem.currentTimeMillis() - timePing) / 2L);
                        GameMidlet.ping = true;
                        CCanvas.isReconnect = false;
                        break;
                    }
                    case 63: {
                        String by70 = message.reader().readUTF();
                        final String n = message.reader().readUTF();
                        final String by71 = message.reader().readUTF();
                        CRes.out(n);
                        CRes.out("sms://" + by71);
                        CCanvas.startYesNoDlg(by70, new IAction(){

                            public void perform() {
                                TerrainMidlet.sendSMS(n, "sms://" + by71, new IAction(){

                                    public void perform() {
                                        CCanvas.startOKDlg(Language.sendSuccess());
                                    }
                                }, new IAction(){

                                    public void perform() {
                                        CCanvas.startOKDlg(Language.sendFail());
                                    }
                                });
                            }
                        }, new IAction(){

                            public void perform() {
                                CCanvas.endDlg();
                            }
                        });
                        break;
                    }
                    case 75: {
                        byte by = message.reader().readByte();
                        if (CCanvas.curScr != CCanvas.luckyGifrScreen) {
                            CCanvas.curScr = CCanvas.prepareScr;
                            CCanvas.prepareScr.resetReady();
                            CCanvas.prepareScr.show();
                            PrepareScr.curMap = by;
                            if (by != 27 && by != 100) {
                                try {
                                    CCanvas.startWaitDlgWithoutCancel(Language.pleaseWait(), 3);
                                    if (MM.maps != null) {
                                        MM.maps.removeAllElements();
                                    }
                                    GameScr.mm.createMap(by);
                                    CCanvas.endDlg();
                                }
                                catch (Exception string) {
                                    CCanvas.endDlg();
                                }
                            }
                            if (!CRes.isNullOrEmpty(GameScr.res)) {
                                GameService.gI().luckGift((byte)-3);
                            }
                            System.gc();
                            break;
                        }
                        PrepareScr.curMap = by;
                        if (by != 27 && by != 100) {
                            try {
                                CCanvas.startWaitDlgWithoutCancel(Language.pleaseWait(), 3);
                                if (MM.maps != null) {
                                    MM.maps.removeAllElements();
                                }
                                GameScr.mm.createMap(by);
                                CCanvas.endDlg();
                            }
                            catch (Exception string7) {
                                CCanvas.endDlg();
                            }
                        }
                        if (CRes.isNullOrEmpty(GameScr.res)) break;
                        GameService.gI().luckGift((byte)-3);
                        break;
                    }
                    case 64: {
                        int n;
                        int by26 = 0;
                        byte by = message.reader().readByte();
                        byte[] i4 = new byte[by];
                        message.reader().read(i4, 0, by);
                        byte string = message.reader().readByte();
                        short[] n23 = new short[string];
                        boolean n24 = false;
                        while (by26 < string) {
                            n23[by26] = message.reader().readShort();
                            ++by26;
                        }
                        byte byArray = message.reader().readByte();
                        byte[] byArray2 = new byte[byArray];
                        message.reader().read(byArray2, 0, byArray);
                        by = message.reader().readByte();
                        byte[] byArray3 = new byte[by];
                        message.reader().read(byArray3, 0, by);
                        PM.MAX_PLAYER = message.reader().readByte();
                        int n8 = message.reader().readByte();
                        PrepareScr.mapBossID = new byte[n8];
                        for (int i = 0; i < n8; ++i) {
                            PrepareScr.mapBossID[i] = message.reader().readByte();
                        }
                        PrepareScr.bossID = new byte[n8];
                        for (n = 0; n < n8; ++n) {
                            PrepareScr.bossID[n] = message.reader().readByte();
                        }
                        PM.NUMB_PLAYER = message.reader().readByte();
                        Bullet.BULLset_WIND_AFFECT = i4;
                        for (n = 0; n < n23.length; ++n) {
                            CRes.out(String.valueOf(this.getClass().getName()) + " debug: " + n23[n]);
                        }
                        CPlayer.angleLock = n23;
                        CPlayer.angleLockMain = n23;
                        ChangePlayerCSr.power = byArray2;
                        ChangePlayerCSr.number = byArray3;
                        CCanvas.changePScr = new ChangePlayerCSr();
                        CCanvas.roomListScr2 = new RoomListScr2();
                        break;
                    }
                    case 76: {
                        byte bl;
                        byte sArray;
                        PrepareScr.currentRoom = sArray = message.reader().readByte();
                        byte sArray10 = message.reader().readByte();
                        String byArray = message.reader().readUTF();
                        PrepareScr.currLevel = bl = message.reader().readByte();
                        BoardListScr.setBoardName(sArray10, byArray);
                        break;
                    }
                    case 78: {
                        boolean string = message.reader().readBoolean();
                        if (string) {
                            int i = message.reader().readByte();
                            Vector<PlayerInfo> n = new Vector<PlayerInfo>();
                            for (int byArray = 0; byArray < i; ++byArray) {
                                PlayerInfo s = new PlayerInfo();
                                s.name = message.reader().readUTF();
                                s.IDDB = message.reader().readInt();
                                s.gun = message.reader().readByte();
                                s.xu = message.reader().readInt();
                                s.level2 = message.reader().readUnsignedByte();
                                s.level2Percen = message.reader().readUnsignedByte();
                                for (int by48 = 0; by48 < 5; ++by48) {
                                    s.equipID[s.gun][by48] = message.reader().readShort();
                                }
                                s.getQuanHam();
                                s.getMyEquip(4);
                                s.isReady = true;
                                n.addElement(s);
                            }
                            CCanvas.endDlg();
                            GameLogicHandler.gI().onInviteList(n);
                            break;
                        }
                        CRes.out("Someone invite ");
                        String by19 = message.reader().readUTF();
                        final byte n = message.reader().readByte();
                        final byte byArray = message.reader().readByte();
                        final String nArray = message.reader().readUTF();
                        CCanvas.startYesNoDlg(by19, new IAction(){

                            public void perform() {
                                PrepareScr.currentRoom = n;
                                BoardListScr.setBoardName(byArray, nArray);
                                GameService.gI().joinBoard(n, byArray, "");
                            }
                        }, new IAction(){

                            public void perform() {
                                CCanvas.endDlg();
                            }
                        });
                        break;
                    }
                    case 94: {
                        GameService.gI().changeItem(CCanvas.prepareScr.itemCur);
                        break;
                    }
                    case 86: {
                        String string = message.reader().readUTF();
                        String by20 = message.reader().readUTF();
                        byte n = message.reader().readByte();
                        this.gameLogicHandler.onURL(string, by20, n);
                        break;
                    }
                    case 87: {
                        String n;
                        TerrainMidlet.myInfo.name = n = message.reader().readUTF();
                        break;
                    }
                    case 88: {
                        int n = message.reader().readByte();
                        RoomListScr2.roomLevelText = new String[n];
                        for (int by21 = 0; by21 < n; ++by21) {
                            String n9 = message.reader().readUTF();
                            String lvlStr = message.reader().readUTF();
                            RoomListScr2.roomLevelText[by21] = Language.language == 0 ? n9 : lvlStr;
                        }
                        break;
                    }
                    case 89: {
                        int timeBomb = 0;
                        byte n = message.reader().readByte();
                        short[] by22 = new short[n];
                        short[] n40 = new short[n];
                        int n25 = -1;
                        boolean by31 = false;
                        while (timeBomb < n) {
                            PlayerInfo n35 = new PlayerInfo();
                            n35.IDDB = message.reader().readInt();
                            n35.name = message.reader().readUTF();
                            n35.maxHP = message.reader().readInt();
                            n35.gun = message.reader().readByte();
                            n25 = n35.gun;
                            n35.isBoss = true;
                            by22[timeBomb] = message.reader().readShort();
                            n40[timeBomb] = message.reader().readShort();
                            if (PrepareScr.currLevel == 7) {
                                n35.index = message.reader().readByte();
                            }
                            CCanvas.prepareScr.bossInfos.addElement(n35);
                            ++timeBomb;
                        }
                        GameScr.pm.initBoss(by22, n40);
                        if (n25 != 23 && n25 != 24) break;
                        Session_ME.receiveSynchronized = 1;
                        break;
                    }
                    case 92: {
                        int by72 = message.reader().readShort();
                        MM.undestroyTile = new short[by72];
                        for (int by23 = 0; by23 < by72; ++by23) {
                            MM.undestroyTile[by23] = message.reader().readShort();
                        }
                        break;
                    }
                    case 90: {
                        byte by = message.reader().readByte();
                        switch (by) {
                            case 0: {
                                break block7;
                            }
                            case 1: {
                                byte by24 = message.reader().readByte();
                                if (by24 != CCanvas.mapIconVersion) {
                                    int bl = message.reader().readUnsignedShort();
                                    PrepareScr.fileData = new byte[bl];
                                    message.reader().read(PrepareScr.fileData, 0, bl);
                                    PrepareScr.init();
                                    CCanvas.saveVersion("iconversion2", by24);
                                    CCanvas.saveData("icondata2", PrepareScr.fileData);
                                }
                                LoginScr.currTime = 45;
                                LoginScr.maxTime = 60;
                                GameService.gI().sendVersion((byte)3, CCanvas.mapIconVersion);
                                break block7;
                            }
                            case 2: {
                                LoginScr.isWait = true;
                                CRes.out("====================================================> NHAN FILE PACK 3");
                                byte by25 = message.reader().readByte();
                                if (by25 != CCanvas.mapValuesVersion) {
                                    CRes.err(String.valueOf(this.getClass().getName()) + " cmd:90 load SUB_FILEPACK_3  ");
                                    int s = message.reader().readUnsignedShort();
                                    byte[] byArrVal = new byte[s];
                                    message.reader().read(byArrVal, 0, s);
                                    CCanvas.readMess(byArrVal, (byte)0);
                                    CCanvas.saveData("valuesdata2", byArrVal);
                                    CCanvas.saveVersion("valuesversion2", by25);
                                } else {
                                    CCanvas.startWaitDlgWithoutCancel(Language.pleaseWait(), 5);
                                    LoginScr.isWait = false;
                                }
                                LoginScr.currTime = 15;
                                LoginScr.maxTime = 30;
                                GameService.gI().sendVersion((byte)1, CCanvas.tileMapVersion);
                                CRes.err("MessageHandler ================> SUB_FILEPACK_3 Load Done");
                                break block7;
                            }
                            case 3: {
                                CRes.out("====================================================> NHAN FILE PACK 4");
                                by = message.reader().readByte();
                                if (by != CCanvas.playerVersion) {
                                    int by27 = message.reader().readUnsignedShort();
                                    CPlayer.fileData = new byte[by27];
                                    CRes.err("SUB_FILEPACK_4 ======================> playerData fileLenght4 = " + by27);
                                    message.reader().read(CPlayer.fileData, 0, by27);
                                    CPlayer.init();
                                    CCanvas.saveVersion("playerVersion2", by);
                                    CCanvas.saveData("playerdata2", CPlayer.fileData);
                                }
                                LoginScr.currTime = 60;
                                LoginScr.maxTime = 75;
                                GameService.gI().sendVersion((byte)4, CCanvas.equipVersion);
                                CRes.err("MessageHandler ================> SUB_FILEPACK_4 Load Done PlayerData");
                                break block7;
                            }
                            case 4: {
                                CRes.out("====================================================> NHAN FILE PACK 5");
                                int n = message.reader().readByte();
                                if (n != CCanvas.equipVersion) {
                                    int sArray = message.reader().readInt();
                                    byte[] stringArray = new byte[sArray];
                                    message.reader().read(stringArray, 0, sArray);
                                    CCanvas.readMess(stringArray, (byte)1);
                                    CCanvas.saveVersion("equipVersion2", (byte)n);
                                    CCanvas.saveData("equipdata2", stringArray);
                                    stringArray = null;
                                }
                                LoginScr.currTime = 75;
                                LoginScr.maxTime = 90;
                                GameService.gI().sendVersion((byte)5, CCanvas.equipVersion);
                                CRes.err("MessageHandler ================> SUB_FILEPACK_5 Load Done Equipment");
                                break block7;
                            }
                            case 5: {
                                CRes.out("====================================================> NHAN FILE PACK 6");
                                byte string = message.reader().readByte();
                                if (string != CCanvas.levelCVersion) {
                                    int sArray = 0;
                                    int j = message.reader().readUnsignedShort();
                                    byte[] byArrLvl = new byte[j];
                                    message.reader().read(byArrLvl, 0, j);
                                    CCanvas.readMess(byArrLvl, (byte)2);
                                    CCanvas.saveVersion("levelCVersion2", (byte)string);
                                    CCanvas.saveData("levelCData2", byArrLvl);
                                    boolean clan = false;
                                    while (sArray < CCanvas.nBigImage) {
                                        GameService.gI().getBigImage((byte)sArray);
                                        ++sArray;
                                    }
                                } else {
                                    int n33;
                                    boolean i = false;
                                    for (n33 = 0; n33 < CCanvas.nBigImage; ++n33) {
                                        byte[] byBigImg = CCanvas.loadData("bigImage" + n33);
                                        if (byBigImg == null) {
                                            i = false;
                                            break;
                                        }
                                        PlayerEquip.imgData[n33] = mImage.createImage(byBigImg, 0, byBigImg.length, "bigImage" + n33);
                                        i = true;
                                        Object n = null;
                                    }
                                    if (i) {
                                        GameService.gI().sendVersion((byte)6, (byte)0);
                                        this.gameLogicHandler.onLoginSuccess();
                                    } else {
                                        for (n33 = 0; n33 < CCanvas.nBigImage; ++n33) {
                                            RMS.clearRMS("bigImage" + n33);
                                        }
                                        int s = message.reader().readUnsignedShort();
                                        byte[] string3 = new byte[s];
                                        message.reader().read(string3, 0, s);
                                        CCanvas.readMess(string3, (byte)2);
                                        CCanvas.saveVersion("levelCVersion2", (byte)string);
                                        CCanvas.saveData("levelCData2", string3);
                                        for (int j = 0; j < CCanvas.nBigImage; ++j) {
                                            GameService.gI().getBigImage((byte)j);
                                        }
                                        string3 = null;
                                    }
                                }
                                CRes.err("MessageHandler ================> SUB_FILEPACK_6 Load Done");
                                break block7;
                            }
                        }
                        break;
                    }
                    case 95: {
                        CRes.out("CAPTURE");
                        byte by = message.reader().readByte();
                        byte by5 = message.reader().readByte();
                        PM.p[by].capture(by5);
                        if (CCanvas.curScr != CCanvas.gameScr) break;
                        Session_ME.receiveSynchronized = 1;
                        break;
                    }
                    case 96: {
                        byte n = message.reader().readByte();
                        byte playerInfo = message.reader().readByte();
                        PM.p[playerInfo].isPoison = true;
                        PM.p[playerInfo].poisonEff = true;
                        PM.p[n].sLook = 3;
                        if (CCanvas.curScr != CCanvas.gameScr) break;
                        Session_ME.receiveSynchronized = 1;
                        break;
                    }
                    case 97: {
                        short n;
                        int i = message.reader().readInt();
                        TerrainMidlet.myInfo.exp = message.reader().readInt();
                        TerrainMidlet.myInfo.nextExp = message.reader().readInt();
                        CRes.out("expAdd= " + i);
                        byte n29 = message.reader().readByte();
                        if (n29 == 0) {
                            TerrainMidlet.myInfo.level2Percen = message.reader().readByte();
                            CRes.out("percen 0 = " + TerrainMidlet.myInfo.level2Percen);
                        }
                        if (CCanvas.curScr == CCanvas.gameScr && PM.p[GameScr.myIndex] != null) {
                            PM.p[GameScr.myIndex].updateExp(i);
                        }
                        if (n29 != 1) break;
                        int mImage2 = message.reader().readUnsignedByte();
                        byte s = message.reader().readByte();
                        TerrainMidlet.myInfo.point = n = message.reader().readShort();
                        CRes.out("currLevel= " + mImage2 + " currPoint= " + n);
                        TerrainMidlet.myInfo.level2 = mImage2;
                        TerrainMidlet.myInfo.level2Percen = s;
                        CRes.out("percen 1 = " + TerrainMidlet.myInfo.level2Percen);
                        break;
                    }
                    case 99: {
                        int by28 = 0;
                        TerrainMidlet.myInfo.level2 = message.reader().readUnsignedByte();
                        TerrainMidlet.myInfo.level2Percen = message.reader().readByte();
                        TerrainMidlet.myInfo.getQuanHam();
                        CRes.out("level=" + TerrainMidlet.myInfo.lvl);
                        TerrainMidlet.myInfo.point = message.reader().readShort();
                        boolean by = false;
                        while (by28 < 5) {
                            TerrainMidlet.myInfo.ability[by28] = message.reader().readShort();
                            CRes.out("my ability= " + TerrainMidlet.myInfo.ability[by28]);
                            ++by28;
                        }
                        TerrainMidlet.myInfo.exp = message.reader().readInt();
                        TerrainMidlet.myInfo.nextExp = message.reader().readInt();
                        TerrainMidlet.myInfo.cup = message.reader().readInt();
                        TerrainMidlet.myInfo.getAttribute();
                        if (!MenuScr.viewInfo) break;
                        MenuScr.viewInfo = false;
                        CCanvas.endDlg();
                        if (CCanvas.levelScreen == null) {
                            CCanvas.levelScreen = new LevelScreen();
                        }
                        CCanvas.levelScreen.show(CCanvas.menuScr);
                        break;
                    }
                    case 100: {
                        byte vector = message.reader().readByte();
                        PM.p[vector].lucky();
                        Session_ME.receiveSynchronized = 1;
                        break;
                    }
                    case 104: {
                        byte playerInfo;
                        byte clan;
                        byte by = message.reader().readByte();
                        if (by == 0) {
                            int i = message.reader().readInt();
                            clan = message.reader().readByte();
                            playerInfo = message.reader().readByte();
                            short by47 = message.reader().readShort();
                            String by51 = message.reader().readUTF();
                            int n = message.reader().readByte();
                            byte[] byArray = new byte[n];
                            for (int j = 0; j < n; ++j) {
                                byArray[j] = message.reader().readByte();
                            }
                            byte by6 = message.reader().readByte();
                            byte by7 = message.reader().readByte();
                            int n10 = message.reader().readUnsignedByte();
                            Equip equip = PlayerEquip.getEquip(clan, playerInfo, by47);
                            equip.getInvAtribute(byArray);
                            Equip equip2 = null;
                            if (equip != null) {
                                equip2 = new Equip();
                                equip2.id = equip.id;
                                equip2.type = equip.type;
                                equip2.icon = equip.icon;
                                equip2.glass = equip.glass;
                                equip2.x = equip.x;
                                equip2.y = equip.y;
                                equip2.w = equip.w;
                                equip2.h = equip.h;
                                equip2.dx = equip.dx;
                                equip2.dy = equip.dy;
                                equip2.date = by6;
                                equip2.name = String.valueOf(by51) + (n10 != 0 ? " " + n10 : "");
                                equip2.dbKey = i;
                                equip2.level = equip.level;
                                equip2.vip = by7;
                                equip2.slot = (byte)3;
                                for (int j = 0; j < 5; ++j) {
                                    equip2.inv_attAddPoint[j] = equip.inv_attAddPoint[j];
                                    equip2.inv_ability[j] = equip.inv_ability[j];
                                    equip2.inv_percen[j] = equip.inv_percen[j];
                                }
                                if (TerrainMidlet.myInfo.myEquip.equips[equip2.type] != null && TerrainMidlet.myInfo.myEquip.equips[equip2.type].id == equip2.id) {
                                    TerrainMidlet.myInfo.myEquip.equips[equip2.type].dbKey = i;
                                }
                            }
                            CCanvas.equipScreen.addEquip(equip2);
                        }
                        if (by == 1) {
                            String n = message.reader().readUTF();
                            CCanvas.inventory.requestServer(n);
                        }
                        if (by != 2) break;
                        PlayerInfo by34 = TerrainMidlet.myInfo;
                        for (clan = 0; clan < 10; ++clan) {
                            for (playerInfo = 0; playerInfo < 5; ++playerInfo) {
                                by34.equipID[clan][playerInfo] = message.reader().readShort();
                            }
                        }
                        break;
                    }
                    case 101: {
                        int n;
                        int n11;
                        CRes.out("=====> Cmd_Server2Client.INVENTORY: 101");
                        Vector<Equip> vector = new Vector<Equip>();
                        CRes.out("=======>Cmd_Server2Client.INVENTORY: TerrainMidlet.myInfo == null " + (TerrainMidlet.myInfo == null));
                        int by35 = message.reader().readByte();
                        short[] i = new short[by35];
                        String[] by38 = new String[by35];
                        int[] n12 = new int[by35];
                        byte[] n36 = new byte[by35];
                        byte[] byArray = new byte[by35];
                        byte[] byArray4 = new byte[by35];
                        byte[] byArray5 = new byte[by35];
                        byte[] byArray6 = new byte[by35];
                        int[] nArray = new int[by35];
                        CRes.out("=======> Cmd_Server2Client.INVENTORY: lenI" + by35);
                        for (n11 = 0; n11 < by35; ++n11) {
                            n12[n11] = message.reader().readInt();
                            n36[n11] = message.reader().readByte();
                            byArray[n11] = message.reader().readByte();
                            i[n11] = message.reader().readShort();
                            by38[n11] = message.reader().readUTF();
                            n = message.reader().readByte();
                            byte[] byArray7 = new byte[n];
                            for (int j = 0; j < n; ++j) {
                                byArray7[j] = message.reader().readByte();
                            }
                            byArray4[n11] = message.reader().readByte();
                            byArray5[n11] = message.reader().readByte();
                            byArray6[n11] = message.reader().readByte();
                            nArray[n11] = message.reader().readUnsignedByte();
                            Equip equip = PlayerEquip.createEquip(n36[n11], byArray[n11], i[n11]);
                            equip.level2 = nArray[n11];
                            equip.removeAbility();
                            equip.getInvAtribute(byArray7);
                            Equip equip3 = new Equip();
                            if (equip == null) continue;
                            equip.date = byArray4[n11];
                            equip.name = by38[n11];
                            equip3.id = equip.id;
                            equip3.type = equip.type;
                            equip3.frame = equip.frame;
                            equip3.x = equip.x;
                            equip3.y = equip.y;
                            equip3.w = equip.w;
                            equip3.h = equip.h;
                            equip3.dx = equip.dx;
                            equip3.dy = equip.dy;
                            equip3.icon = equip.icon;
                            equip3.type = equip.type;
                            equip3.glass = n36[n11];
                            equip3.date = equip.date;
                            equip3.name = String.valueOf(equip.name) + (equip.level2 != 0 ? " " + equip.level2 : "");
                            equip3.dbKey = n12[n11];
                            equip3.level = equip.level;
                            equip3.slot = byArray5[n11];
                            equip3.vip = byArray6[n11];
                            TerrainMidlet.myInfo.getMyEquip(5);
                            equip3.removeAbility();
                            equip3.getInvAtribute(byArray7);
                            vector.addElement(equip3);
                        }
                        for (n11 = 0; n11 < 5; ++n11) {
                            n = message.reader().readInt();
                            if (TerrainMidlet.myInfo.myEquip.equips[n11] == null) continue;
                            TerrainMidlet.myInfo.myEquip.equips[n11].dbKey = n;
                            TerrainMidlet.myInfo.dbKey[n11] = n;
                        }
                        CCanvas.equipScreen.getEquip(vector);
                        CCanvas.endDlg();
                        break;
                    }
                    case 102: {
                        byte n = message.reader().readByte();
                        if (n == 0) {
                            CCanvas.endDlg();
                            CCanvas.equipScreen.resetEquip();
                            CCanvas.menuScr.show();
                        }
                        if (n == 1) {
                            CCanvas.equipScreen.getLastEquip();
                            CCanvas.menuScr.show();
                            CCanvas.endDlg();
                        }
                        if (n != 2) break;
                        for (int by36 = 0; by36 < 10; ++by36) {
                            for (int byArray = 0; byArray < 5; ++byArray) {
                                TerrainMidlet.myInfo.equipID[by36][byArray] = message.reader().readShort();
                                if (by36 != 0) continue;
                                CRes.out("my equip= " + TerrainMidlet.myInfo.equipID[by36][byArray]);
                            }
                        }
                        if (CCanvas.curScr != CCanvas.inventory) {
                            CCanvas.equipScreen.init();
                            CCanvas.equipScreen.show(CCanvas.menuScr);
                        }
                        break;
                    }
                    case 103: {
                        Vector<Equip> by = new Vector<Equip>();
                        int by37 = message.reader().readShort();
                        for (int string = 0; string < by37; ++string) {
                            byte string4 = message.reader().readByte();
                            byte playerInfo = message.reader().readByte();
                            short i = message.reader().readShort();
                            String string5 = message.reader().readUTF();
                            int n = message.reader().readInt();
                            int n13 = message.reader().readInt();
                            byte by8 = message.reader().readByte();
                            byte by9 = message.reader().readByte();
                            Equip equip = PlayerEquip.getEquip(string4, playerInfo, i);
                            if (equip == null) continue;
                            equip.date = by8;
                            equip.name = string5;
                            equip.xu = n;
                            equip.luong = n13;
                            equip.level = by9;
                            equip.glass = string4;
                            equip.isSelect = false;
                            equip.index = string;
                            by.addElement(equip);
                        }
                        if (CCanvas.shopEquipScr == null) {
                            CCanvas.shopEquipScr = new ShopEquipment();
                        }
                        CCanvas.shopEquipScr.setItems(by);
                        CCanvas.menuScr.doEquipItem();
                        CCanvas.endDlg();
                        break;
                    }
                    case 105: {
                        int by = message.reader().readInt();
                        int vector = message.reader().readInt();
                        TerrainMidlet.myInfo.xu = by;
                        TerrainMidlet.myInfo.luong = vector;
                        CRes.out("xu= " + by + " luong= " + vector);
                        break;
                    }
                    case 106: {
                        byte by = message.reader().readByte();
                        byte n = message.reader().readByte();
                        CCanvas.gameScr.checkEyeSmoke(n, by);
                        break;
                    }
                    case 107: {
                        byte by = message.reader().readByte();
                        byte s7 = message.reader().readByte();
                        CCanvas.gameScr.checkFreeze(s7, by);
                        break;
                    }
                    case 108: {
                        byte by = message.reader().readByte();
                        CCanvas.gameScr.checkPostion(by);
                        break;
                    }
                    case 109: {
                        CRes.out("DAT BOM HEN GIO");
                        byte by = message.reader().readByte();
                        byte vector = message.reader().readByte();
                        if (by == 0) {
                            int by10 = message.reader().readInt();
                            int string15 = message.reader().readInt();
                            CRes.out("bomb x= " + by10 + " bomb y= " + string15);
                            TimeBomb string = new TimeBomb(vector, by10, string15);
                            CCanvas.gameScr.addTimeBomb(string);
                        }
                        if (by != 1) break;
                        CRes.out("BOM EXPLORE id=" + vector);
                        CCanvas.gameScr.explodeTimeBomb(vector);
                        break;
                    }
                    case 112: {
                        CRes.out("Tra ve 4 Slot");
                        ShopItem.getI((int)12).num = message.reader().readByte();
                        ShopItem.getI((int)13).num = message.reader().readByte();
                        ShopItem.getI((int)14).num = message.reader().readByte();
                        ShopItem.getI((int)15).num = message.reader().readByte();
                        break;
                    }
                    case 113: {
                        byte clan = message.reader().readByte();
                        byte i = message.reader().readByte();
                        CRes.out("angry= " + i);
                        PM.p[clan].updateAngry(i);
                        break;
                    }
                    case 116: {
                        CCanvas.endDlg();
                        byte s = message.reader().readByte();
                        Vector<Clan> by40 = new Vector<Clan>();
                        while (message.reader().available() > 0) {
                            Clan by41 = new Clan();
                            by41.id = message.reader().readShort();
                            by41.name = message.reader().readUTF();
                            by41.count = (byte)message.reader().readUnsignedByte();
                            by41.max = (byte)message.reader().readUnsignedByte();
                            by41.master = message.reader().readUTF();
                            by41.money = message.reader().readInt();
                            by41.money2 = message.reader().readInt();
                            by41.cup = message.reader().readInt();
                            by41.level = message.reader().readByte();
                            by41.percen = message.reader().readByte();
                            by41.slogan = message.reader().readUTF();
                            by40.addElement(by41);
                        }
                        if (by40.size() > 0) {
                            CCanvas.topClanScreen.show(CCanvas.curScr);
                            CCanvas.topClanScreen.getClanList(s, by40);
                            break;
                        }
                        CCanvas.startOKDlg(Language.clanSize());
                        break;
                    }
                    case 117: {
                        CRes.out("Cmd_Server2Client.CLAN_INFO: ======> Clan Info");
                        CCanvas.endDlg();
                        Clan by = new Clan();
                        by.id = message.reader().readShort();
                        by.name = message.reader().readUTF();
                        by.count = (byte)message.reader().readUnsignedByte();
                        by.max = (byte)message.reader().readUnsignedByte();
                        by.master = message.reader().readUTF();
                        by.money = message.reader().readInt();
                        by.money2 = message.reader().readInt();
                        by.cup = message.reader().readInt();
                        by.exp = message.reader().readInt();
                        by.nextExp = message.reader().readInt();
                        by.level = message.reader().readByte();
                        by.percen = message.reader().readByte();
                        by.slogan = message.reader().readUTF();
                        by.date = message.reader().readUTF();
                        int n = message.reader().readByte();
                        by.item = new String[n];
                        by.time = new int[n];
                        for (int byArray = 0; byArray < n; ++byArray) {
                            by.item[byArray] = message.reader().readUTF();
                            by.time[byArray] = message.reader().readInt();
                        }
                        CCanvas.clanScreen.clan = by;
                        CCanvas.clanScreen.show(CCanvas.curScr);
                        break;
                    }
                    case 115: {
                        short vector = message.reader().readShort();
                        short n = message.reader().readShort();
                        byte[] byArray = new byte[n];
                        CRes.out("======> Cmd_Server2Client.CLAN_ICON lenImg = " + n);
                        mImage by45 = null;
                        if (n > 1) {
                            message.reader().read(byArray, 0, n);
                            by45 = mImage.createImage(byArray, 0, (int)n, "");
                        } else {
                            by45 = CRes.empty;
                        }
                        if (by45 == null) {
                            by45 = CRes.imgEr;
                        }
                        Clan by57 = new Clan();
                        by57.id = vector;
                        by57.icon = by45;
                        if (!CCanvas.iconMn.isExist(by57.id)) {
                            CCanvas.iconMn.addIcon(by57);
                        }
                        GameLogicHandler.gI().onGetImage(vector, by45.image);
                        break;
                    }
                    case 118: {
                        byte by39 = message.reader().readByte();
                        Vector<PlayerInfo> bl = new Vector<PlayerInfo>();
                        String string14 = message.reader().readUTF();
                        while (message.reader().available() > 0) {
                            PlayerInfo n = new PlayerInfo();
                            n.IDDB = message.reader().readInt();
                            n.name = message.reader().readUTF();
                            CRes.out("name= " + n.name);
                            n.xu = message.reader().readInt();
                            n.gun = message.reader().readByte();
                            n.isReady = message.reader().readByte() != 0;
                            n.level2 = message.reader().readUnsignedByte();
                            n.level2Percen = message.reader().readByte();
                            n.STT = message.reader().readUnsignedByte();
                            n.cup = message.reader().readInt();
                            n.getQuanHam();
                            short[] fomula = new short[5];
                            for (int object = 0; object < 5; ++object) {
                                fomula[object] = message.reader().readShort();
                                n.equipID[n.gun][object] = fomula[object];
                                n.getMyEquip(6);
                            }
                            n.clanContribute1 = message.reader().readUTF();
                            n.clanContribute2 = message.reader().readUTF();
                            bl.addElement(n);
                        }
                        this.gameLogicHandler.onClanMemberList(by39, bl);
                        break;
                    }
                    case 110: {
                        byte i;
                        Vector<LuckyGame.Gift> by = new Vector<LuckyGame.Gift>();
                        for (int string = 0; string < 10; ++string) {
                            i = message.reader().readByte();
                            byte n34 = message.reader().readByte();
                            int by65 = message.reader().readInt();
                            LuckyGame.Gift by59 = new LuckyGame.Gift(i, n34, by65);
                            by.addElement(by59);
                        }
                        i = message.reader().readByte();
                        CCanvas.endDlg();
                        CCanvas.luckyGame.getGifts(by, i);
                        Session_ME.receiveSynchronized = 1;
                        break;
                    }
                    case 119: {
                        byte by = message.reader().readByte();
                        byte by44 = message.reader().readByte();
                        byte by46 = message.reader().readByte();
                        CRes.out("Gift= " + by46);
                        String string17 = PM.p[by44].name;
                        if (by46 == 0) {
                            int n = message.reader().readUnsignedShort();
                            String string = String.valueOf(string17) + ": +" + n + Language.xu();
                            CCanvas.gameScr.vGift.addElement(new GiftEffect(string, null));
                        }
                        if (by46 == 1) {
                            byte by11 = message.reader().readByte();
                            byte by12 = message.reader().readByte();
                            String string = String.valueOf(string17) + " : +" + by12 + "x " + Item.ITEM_NAME[by11];
                            CCanvas.gameScr.vGift.addElement(new GiftEffect(string, null));
                        }
                        if (by46 == 2) {
                            byte by13 = message.reader().readByte();
                            byte by14 = message.reader().readByte();
                            short s = message.reader().readShort();
                            Equip equip = PlayerEquip.getEquip(by13, by14, s);
                            String string = String.valueOf(string17) + " : +" + message.reader().readUTF();
                            CCanvas.gameScr.vGift.addElement(new GiftEffect(string, equip));
                        }
                        if (by46 == 3) {
                            byte by15 = message.reader().readByte();
                            String string = String.valueOf(string17) + " : +" + by15 + "xp";
                            CCanvas.gameScr.vGift.addElement(new GiftEffect(string, null));
                        }
                        if (by46 != 4) break;
                        String string = message.reader().readUTF();
                        CCanvas.gameScr.vGift.addElement(new GiftEffect("+ " + string, null));
                        break;
                    }
                    case 120: {
                        CRes.out(" =========================> Save BIG Image ");
                        byte string = message.reader().readByte();
                        int n = message.reader().readUnsignedShort();
                        byte[] by = new byte[n];
                        message.reader().read(by, 0, n);
                        CCanvas.saveData("bigImage" + string, by);
                        if (PlayerEquip.imgData == null) {
                            PlayerEquip.imgData = new mImage[string + 1];
                        }
                        PlayerEquip.imgData[string] = mImage.createImage(by, 0, n, "");
                        by = null;
                        LoginScr.maxTime = 90 + string;
                        if (string == 9) {
                            LoginScr.isWait = false;
                            GameService.gI().sendVersion((byte)6, (byte)0);
                            CCanvas.endDlg();
                            this.gameLogicHandler.onLoginSuccess();
                        }
                        CRes.out(" =========================> Load BigImage  DONE!");
                        break;
                    }
                    case -120: {
                        CRes.out(" =========================> Save GET_BIG_EQUIP_HD Image ");
                        CRes.out(" =========================> Save BIG Image ");
                        byte bl = message.reader().readByte();
                        CRes.out(" =========================> Save idBigImg " + bl);
                        int by = message.reader().readInt();
                        CRes.out(" =========================> Save lenBigImg " + by);
                        byte[] s = new byte[by];
                        message.reader().read(s, 0, by);
                        CCanvas.saveData("bigImage" + bl, s);
                        if (PlayerEquip.imgData == null) {
                            PlayerEquip.imgData = new mImage[bl + 1];
                        }
                        PlayerEquip.imgData[bl] = mImage.createImage(s, 0, by, "");
                        s = null;
                        LoginScr.maxTime = 90 + bl;
                        if (bl != 9) break;
                        LoginScr.isWait = false;
                        GameService.gI().sendVersion((byte)6, (byte)0);
                        CCanvas.endDlg();
                        this.gameLogicHandler.onLoginSuccess();
                        break;
                    }
                    case 121: {
                        String msgInfo = "";
                        boolean exception = message.reader().readBoolean();
                        if (!exception) {
                            String by = message.reader().readUTF();
                            CCanvas.startOKDlg(by);
                            break;
                        }
                        CCanvas.startOKDlg(Language.dangkySucceed(), new IAction(){

                            public void perform() {
                                LoginScr loginScr = (LoginScr)CCanvas.curScr;
                                if (loginScr != null) {
                                    loginScr.setLogin();
                                }
                            }
                        });
                        break;
                    }
                    case -93: {
                        boolean by = message.reader().readBoolean();
                        String string = message.reader().readUTF();
                        string = message.reader().readUTF();
                        String clanItem = message.reader().readUTF();
                        this.gameLogicHandler.onRegisterInfo2(string, by, string, clanItem);
                        break;
                    }
                    case 123: {
                        System.out.println("CHAT TO TEAM");
                        MsgInfo message2 = new MsgInfo();
                        message2.fromName = String.valueOf(message.reader().readUTF()) + " " + Language.chatAll();
                        message2.message = message.reader().readUTF();
                        this.gameLogicHandler.onChatFrom(message2);
                        break;
                    }
                    case 124: {
                        byte by = message.reader().readByte();
                        byte i = message.reader().readByte();
                        PM.p[by].ghostHit(i);
                        PM.p[by].checkGhostLook(PM.p[i].x, PM.p[by].x);
                        if (CCanvas.curScr != CCanvas.gameScr) break;
                        Session_ME.receiveSynchronized = 1;
                        break;
                    }
                    case 125: {
                        byte n = message.reader().readByte();
                        if (n != 0) break;
                        int i = message.reader().readByte();
                        for (i = 0; i < i; ++i) {
                            byte vector = message.reader().readByte();
                            short s = message.reader().readShort();
                            String string = message.reader().readUTF();
                            String string6 = message.reader().readUTF();
                            Equip equip = new Equip();
                            equip.id = vector;
                            equip.name = string;
                            equip.strDetail = string6;
                            equip.isMaterial = true;
                            equip.icon = vector;
                            equip.num = s;
                            if (equip.materialIcon == null && MaterialIconMn.isExistIcon(vector)) {
                                equip.materialIcon = MaterialIconMn.getImageFromID(vector);
                            }
                            CCanvas.equipScreen.addMaterial(equip);
                        }
                        break;
                    }
                    case 126: {
                        Message by = message;
                        try {
                            byte by73;
                            byte i = by.reader().readByte();
                            int by16 = by.reader().readUnsignedByte();
                            short s = by.reader().readShort();
                            byte[] byArray = new byte[s];
                            by.reader().read(byArray, 0, s);
                            if (i == 0) {
                                MaterialIconMn.addIcon(new ImageIcon(by16, byArray, s));
                                CCanvas.equipScreen.getMaterialIcon(by16, byArray, s);
                            }
                            if (i == 1) {
                                MaterialIconMn.addIcon(new ImageIcon(by16, byArray, s));
                                CCanvas.shopLinhtinh.getMaterialIcon(by16, byArray, s);
                            }
                            if (i == 2) {
                                GameScr.mm.addImage(by16, byArray, s);
                            }
                            if (i == 3) {
                                by73 = by.reader().readByte();
                                CCanvas.luckyGifrScreen.getGiftByItemID(by73).setIcon(byArray, s);
                            }
                            if (i == 4) {
                                by73 = by.reader().readByte();
                            }
                            byArray = null;
                        }
                        catch (Exception n) {
                            n.printStackTrace();
                        }
                        break;
                    }
                    case 17: {
                        byte by = message.reader().readByte();
                        if (by != 0) break;
                        String vector = message.reader().readUTF();
                        CCanvas.inventory.combineYesNo(vector);
                        break;
                    }
                    case 27: {
                        int by = message.reader().readByte();
                        CRes.out("================> inventory Update type= " + by);
                        for (int n = 0; n < by; ++n) {
                            byte string;
                            Object object;
                            int i;
                            byte by17 = message.reader().readByte();
                            CRes.out("action= " + by17);
                            if (by17 == 0) {
                                i = message.reader().readInt();
                                byte by18 = message.reader().readByte();
                                CRes.out("nRemove= " + by18);
                                CCanvas.equipScreen.removeEquip(i, by18);
                                CCanvas.inventory.removeEquip(i, by18);
                                continue;
                            }
                            if (by17 == 2) {
                                i = message.reader().readInt();
                                CRes.out("======> INVENTORY_UPDATE IDDB= " + i);
                                int n14 = message.reader().readByte();
                                byte[] objBytes = new byte[n14];
                                for (int j = 0; j < n14; ++j) {
                                    objBytes[j] = message.reader().readByte();
                                    CRes.out("====> INVENTORY_UPDATE ability= " + (int)objBytes[j]);
                                }
                                byte by19 = message.reader().readByte();
                                byte by20 = message.reader().readByte();
                                Equip equip = null;
                                if (CCanvas.curScr == CCanvas.inventory) {
                                    equip = CCanvas.inventory.getEquip(i);
                                }
                                if (CCanvas.curScr == CCanvas.equipScreen) {
                                    equip = CCanvas.equipScreen.getEquip(i);
                                }
                                equip.getInvAtribute(objBytes);
                                equip.slot = by19;
                                equip.date = by20;
                                if (CCanvas.curScr == CCanvas.inventory) {
                                    CCanvas.inventory.getDetail();
                                }
                                if (CCanvas.curScr == CCanvas.equipScreen) {
                                    CCanvas.equipScreen.getDetail();
                                }
                                if (TerrainMidlet.myInfo.myEquip.equips[equip.type] != null && TerrainMidlet.myInfo.myEquip.equips[equip.type].dbKey == equip.dbKey) {
                                    TerrainMidlet.myInfo.myEquip.equips[equip.type].changeToEquip(equip);
                                    TerrainMidlet.myInfo.clearAttAddPoint();
                                }
                                CCanvas.equipScreen.getBaseAttribute();
                                continue;
                            }
                            if (by17 == 1) {
                                string = message.reader().readByte();
                                String string7 = message.reader().readUTF();
                                object = message.reader().readUTF();
                                Equip equip = new Equip();
                                equip.id = string;
                                equip.name = string7;
                                equip.strDetail = (String)object;
                                equip.isMaterial = true;
                                CCanvas.equipScreen.addEquip(equip, false);
                                continue;
                            }
                            if (by17 != 3) continue;
                            string = message.reader().readByte();
                            byte by21 = message.reader().readByte();
                            object = message.reader().readUTF();
                            String string8 = message.reader().readUTF();
                            Equip equip = new Equip();
                            equip.id = string;
                            equip.num = by21;
                            equip.name = (String)object;
                            equip.strDetail = string8;
                            equip.isMaterial = true;
                            CCanvas.equipScreen.addEquip(equip, true);
                        }
                        CCanvas.inventory.unSelectEquip();
                        break;
                    }
                    case 80: {
                        byte vector = message.reader().readByte();
                        CCanvas.gameScr.checkInvisible2(vector);
                        break;
                    }
                    case 59: {
                        byte by = message.reader().readByte();
                        CCanvas.gameScr.checkVampire(by);
                        break;
                    }
                    case -2: {
                        CRes.out("VIP EQUIP");
                        byte i = message.reader().readByte();
                        if (i == 0) {
                            TerrainMidlet.isVip[TerrainMidlet.myInfo.gun] = false;
                            TerrainMidlet.myInfo.getMyEquip(7);
                        } else {
                            for (int by54 = 0; by54 < 5; ++by54) {
                                short by56 = message.reader().readShort();
                                CRes.out(" vip ID= " + by56);
                                TerrainMidlet.myInfo.equipVipID[TerrainMidlet.myInfo.gun][by54] = by56;
                            }
                            TerrainMidlet.myInfo.getVipEquip();
                            TerrainMidlet.isVip[TerrainMidlet.myInfo.gun] = true;
                        }
                        CCanvas.equipScreen.getBaseAttribute();
                        break;
                    }
                    case -3: {
                        Vector<Equip> i = new Vector<Equip>();
                        while (message.reader().available() > 0) {
                            byte n = message.reader().readByte();
                            String string = message.reader().readUTF();
                            string = message.reader().readUTF();
                            int n15 = message.reader().readInt();
                            int n16 = message.reader().readInt();
                            byte by = message.reader().readByte();
                            byte by22 = message.reader().readByte();
                            Equip equip = new Equip();
                            equip.id = n;
                            equip.name = string;
                            equip.strDetail = string;
                            equip.xu = n15;
                            equip.luong = n16;
                            equip.date = by;
                            equip.isMaterial = true;
                            if (MaterialIconMn.isExistIcon(n)) {
                                equip.materialIcon = MaterialIconMn.getImageFromID(n);
                            } else {
                                GameService.gI().getMaterialIcon((byte)1, n, -1);
                            }
                            if (by22 == 0) {
                                equip.isBuyNum = true;
                            }
                            i.addElement(equip);
                        }
                        CCanvas.endDlg();
                        CCanvas.shopLinhtinh.setItems(i);
                        CCanvas.shopLinhtinh.show(CCanvas.menuScr);
                        break;
                    }
                    case -6: {
                        byte n = message.reader().readByte();
                        MM.maps.removeAllElements();
                        if (CCanvas.curScr == CCanvas.gameScr) {
                            CCanvas.curScr = CCanvas.prepareScr;
                        }
                        GameScr.mm.createMap(n);
                        System.gc();
                        CCanvas.endDlg();
                        break;
                    }
                    case -7: {
                        int by55 = 0;
                        boolean by = false;
                        while (by55 < 5) {
                            int string = message.reader().readInt();
                            if (TerrainMidlet.myInfo.myEquip.equips[by55] != null) {
                                TerrainMidlet.myInfo.myEquip.equips[by55].dbKey = string;
                            }
                            ++by55;
                        }
                        break;
                    }
                    case -10: {
                        int n = 0;
                        boolean by = false;
                        while (n < 8) {
                            GameScr.num[n] = message.reader().readByte();
                            ++n;
                        }
                        break;
                    }
                    case -12: {
                        if (CCanvas.shopBietDoi == null) {
                            CCanvas.shopBietDoi = new ShopBietDoi();
                        }
                        int vector = message.reader().readByte();
                        Vector<ClanItem> by58 = new Vector<ClanItem>();
                        for (int luckyGift = 0; luckyGift < vector; ++luckyGift) {
                            ClanItem clanItem = new ClanItem();
                            clanItem.id = message.reader().readByte();
                            clanItem.name = message.reader().readUTF();
                            clanItem.xu = message.reader().readInt();
                            clanItem.luong = message.reader().readInt();
                            clanItem.expDate = message.reader().readByte();
                            clanItem.levelRequire = message.reader().readByte();
                            by58.addElement(clanItem);
                        }
                        CCanvas.shopBietDoi.setItems(by58);
                        CCanvas.shopBietDoi.show();
                        break;
                    }
                    case -14: {
                        byte n = message.reader().readByte();
                        if (n == -1) {
                            int i = message.reader().readByte();
                            MenuScr.subMenuString[7] = new String[i];
                            for (int n17 = 0; n17 < i; ++n17) {
                                MenuScr.subMenuString[7][n17] = message.reader().readUTF().toUpperCase();
                            }
                            break;
                        }
                        byte mission = message.reader().readByte();
                        String by = message.reader().readUTF();
                        Vector<PlayerInfo> vector = new Vector<PlayerInfo>();
                        while (message.reader().available() > 0) {
                            PlayerInfo playerInfo = new PlayerInfo();
                            playerInfo.IDDB = message.reader().readInt();
                            playerInfo.name = message.reader().readUTF();
                            playerInfo.gun = message.reader().readByte();
                            playerInfo.clanID = message.reader().readShort();
                            playerInfo.level2 = message.reader().readUnsignedByte();
                            playerInfo.level2Percen = message.reader().readByte();
                            playerInfo.getQuanHam();
                            playerInfo.STT = message.reader().readUnsignedByte();
                            for (int i = 0; i < 5; ++i) {
                                playerInfo.equipID[playerInfo.gun][i] = message.reader().readShort();
                            }
                            playerInfo.aa = message.reader().readUTF();
                            CRes.out("aa= " + playerInfo.aa);
                            playerInfo.isReady = true;
                            playerInfo.getMyEquip(8);
                            vector.addElement(playerInfo);
                        }
                        this.gameLogicHandler.onXepHanglist((byte)-n, mission, vector, by);
                        break;
                    }
                    case -17: {
                        Object object;
                        byte by;
                        int by23;
                        byte n = message.reader().readByte();
                        CRes.out("===>lucky gift action = " + n);
                        if (n == 0) {
                            by23 = message.reader().readByte();
                            byte string21 = message.reader().readByte();
                            by = message.reader().readByte();
                            String string = message.reader().readUTF();
                            object = new LuckyGift();
                            ((LuckyGift)object).id = by23;
                            ((LuckyGift)object).type = string21;
                            ((LuckyGift)object).info = string;
                            ((LuckyGift)object).itemID = by;
                            ((LuckyGift)object).isWait = true;
                            ((LuckyGift)object).isServerSend = true;
                            CCanvas.luckyGifrScreen.setGiftByItemID((LuckyGift)object);
                            if (((LuckyGift)object).type == 2) {
                                GameService.gI().getMaterialIcon((byte)3, ((LuckyGift)object).itemID, by23);
                            }
                        }
                        if (n == -1) {
                            if (CCanvas.curScr == CCanvas.gameScr) break;
                            by23 = message.reader().readByte();
                            String string = message.reader().readUTF();
                            if (CCanvas.width < 200) {
                                CCanvas.startOKDlg(string);
                            }
                            LuckyGifrScreen.info = Font.normalFont.splitFontBStrInLine(string, CCanvas.width - 80);
                            LuckyGifrScreen.time = new CTime();
                            LuckyGifrScreen.time.initTimeInterval((byte)by23);
                            LuckyGifrScreen.time.resetTime();
                            CCanvas.luckyGifrScreen.isShow = false;
                            CCanvas.luckyGifrScreen.show();
                        }
                        if (n != -2) break;
                        for (by23 = 0; by23 < 12; ++by23) {
                            LuckyGift luckyGift = new LuckyGift();
                            by = message.reader().readByte();
                            if (by == -1) continue;
                            byte by24 = message.reader().readByte();
                            object = message.reader().readUTF();
                            luckyGift.id = by23;
                            luckyGift.type = by;
                            luckyGift.info = (String)object;
                            luckyGift.itemID = by24;
                            luckyGift.isServerSend = true;
                            luckyGift.isWait = true;
                            CCanvas.luckyGifrScreen.setGiftByItemID(luckyGift);
                            CCanvas.luckyGifrScreen.giftDelete[by23] = -1;
                            if (luckyGift.type != 2) continue;
                            GameService.gI().getMaterialIcon((byte)3, luckyGift.itemID, (byte)by23);
                        }
                        CCanvas.luckyGifrScreen.isShow = true;
                        CCanvas.luckyGifrScreen.show();
                        break;
                    }
                    case -18: {
                        byte string = message.reader().readByte();
                        CRes.out("FOMULA= " + string);
                        if (string == 0) {
                            String string9 = message.reader().readUTF();
                            CRes.out("fInfo= " + string9);
                            CCanvas.startOKDlg(string9, new IAction(){

                                public void perform() {
                                    CCanvas.fomulaScreen.lastScr.show();
                                }
                            });
                        }
                        if (string != 1) break;
                        CCanvas.fomulaScreen.fomulas.removeAllElements();
                        CCanvas.endDlg();
                        byte string20 = message.reader().readByte();
                        int n = message.reader().readByte();
                        CRes.out(" nFomular= " + n);
                        for (int i = 0; i < n; ++i) {
                            boolean bl;
                            int n18;
                            Fomula fomula = new Fomula();
                            byte by = message.reader().readByte();
                            String string10 = message.reader().readUTF();
                            byte by25 = message.reader().readByte();
                            byte by26 = message.reader().readByte();
                            byte by27 = message.reader().readByte();
                            CRes.out("id item create= " + by + " type Fomula= " + by27 + " gun= " + by26);
                            fomula.e = PlayerEquip.createEquip(by26, by27, by);
                            fomula.e.name = string10;
                            CRes.out("Name equip= " + string10);
                            fomula.levelRequire = by25;
                            fomula.ID = string20;
                            int n19 = message.reader().readByte();
                            fomula.imgMaterial = new mImage[n19];
                            fomula.numMaterial = new String[n19];
                            fomula.materialName = new String[n19];
                            fomula.idImage = new int[n19];
                            for (int j = 0; j < n19; ++j) {
                                String string11;
                                byte by28 = message.reader().readByte();
                                fomula.materialName[j] = string11 = message.reader().readUTF();
                                if (MaterialIconMn.isExistIcon(by28)) {
                                    fomula.imgMaterial[j] = MaterialIconMn.getImageFromID(by28);
                                } else {
                                    GameService.gI().getMaterialIcon((byte)4, by28, (byte)i);
                                }
                                fomula.idImage[j] = by28;
                                n18 = message.reader().readUnsignedByte();
                                int blInt = message.reader().readUnsignedByte();
                                fomula.numMaterial[j] = String.valueOf(blInt) + "/" + n18;
                                CRes.out("Image id= " + by28 + " numMaterial= " + fomula.numMaterial[i]);
                            }
                            byte by29 = message.reader().readByte();
                            String string12 = message.reader().readUTF();
                            n18 = message.reader().readByte();
                            string12 = String.valueOf(string12) + (n18 != 0 ? " " + n18 : "");
                            bl = message.reader().readBoolean();
                            boolean bl2 = message.reader().readBoolean();
                            int n20 = message.reader().readByte();
                            fomula.ability = new String[n20];
                            for (int j = 0; j < n20; ++j) {
                                fomula.ability[j] = message.reader().readUTF();
                            }
                            fomula.h1 = n20 * 18;
                            fomula.isHave = bl;
                            CRes.out("is Have= " + bl);
                            fomula.equipRequire = PlayerEquip.createEquip(by26, by27, by29);
                            fomula.equipRequire.name = string12;
                            fomula.finish = bl2;
                            CRes.out("is Finish= " + bl2);
                            CCanvas.fomulaScreen.setFomula(fomula);
                        }
                        if (CCanvas.curScr == CCanvas.inventory) {
                            CCanvas.fomulaScreen.show(CCanvas.inventory);
                        }
                        if (CCanvas.curScr == CCanvas.shopLinhtinh) {
                            CCanvas.fomulaScreen.show(CCanvas.shopLinhtinh);
                        }
                        break;
                    }
                    case -19: {
                        int string = message.reader().readByte();
                        for (int by = 0; by < string; ++by) {
                            byte by30 = message.reader().readByte();
                            String string13 = message.reader().readUTF();
                            byte by31 = message.reader().readByte();
                            if (CCanvas.roomListScr2 == null) continue;
                            CCanvas.roomListScr2.updateRoomName(by30, by31, string13);
                        }
                        break;
                    }
                    case -22: {
                        CCanvas.clanScreen.clan.money = message.reader().readInt();
                        CCanvas.clanScreen.clan.money2 = message.reader().readInt();
                        break;
                    }
                    case -23: {
                        CCanvas.endDlg();
                        Vector<Mission> bl = new Vector<Mission>();
                        while (message.reader().available() > 0) {
                            Mission string = new Mission();
                            string.id = message.reader().readByte();
                            string.level = message.reader().readByte();
                            string.name = message.reader().readUTF();
                            string.reward = message.reader().readUTF();
                            string.require = message.reader().readInt();
                            string.have = message.reader().readInt();
                            string.isComplete = message.reader().readBoolean();
                            bl.addElement(string);
                        }
                        CCanvas.missionScreen.setMission(bl);
                        CCanvas.missionScreen.show();
                        break;
                    }
                    case -24: {
                        int by;
                        byte string = message.reader().readByte();
                        TerrainMidlet.myInfo.cup = by = message.reader().readInt();
                        if (CCanvas.curScr != CCanvas.gameScr || PM.p[GameScr.myIndex] == null) break;
                        PM.p[GameScr.myIndex].updateCup(string);
                        break;
                    }
                    case -25: {
                        final int exception = message.reader().readInt();
                        String string = message.reader().readUTF();
                        CCanvas.startYesNoDlg(string, new IAction(){

                            public void perform() {
                                GameService.gI().get_more_day((byte)1, exception);
                            }
                        });
                        break;
                    }
                    case -100: {
                        CRes.out("Quang cao-----------------------------------------------------------------------------------");
                        String string = message.reader().readUTF();
                        String string14 = message.reader().readUTF();
                        String string15 = message.reader().readUTF();
                        MenuScr.getIdMenu(1);
                        MenuScr.menuString[MenuScr.MENU_QUANGCAO] = string.toUpperCase();
                        MenuScr.gameContent = string14;
                        MenuScr.gameLink = string15;
                        if (CCanvas.quangCaoScr == null) {
                            CCanvas.quangCaoScr = new QuangCao();
                        }
                        QuangCao.content = string14;
                        QuangCao.link = string15;
                        CCanvas.quangCaoScr.getCommand();
                        CRes.out("game = " + string + " strContent= " + string14 + " linkGame= " + string15);
                        break;
                    }
                    case -26: {
                        String string = message.reader().readUTF();
                        byte by = message.reader().readByte();
                        CRes.out("==============> agent= " + string + " provider= " + by);
                        TerrainMidlet.PROVIDER = by;
                        CRes.saveRMSInt("provider", by);
                        TerrainMidlet.AGENT = string;
                        CRes.saveRMS_String("agent", string);
                        break;
                    }
                    case -101: {
                        boolean bl = message.reader().readBoolean();
                        if (bl) {
                            CCanvas.msgdlg.setInfo("\u0110\u0103ng k\u00ed th\u00e0nh c\u00f4ng", null, new Command("OK", new IAction(){

                                public void perform() {
                                    CCanvas.endDlg();
                                }
                            }), null);
                            CCanvas.msgdlg.show();
                            break;
                        }
                        String string = message.reader().readUTF();
                        CCanvas.msgdlg.setInfo(string, null, new Command("OK", new IAction(){

                            public void perform() {
                                CCanvas.endDlg();
                            }
                        }), null);
                        CCanvas.msgdlg.show();
                        break;
                    }
                    case -103: {
                        byte by = message.reader().readByte();
                        String string = message.reader().readUTF();
                        if (by == 0) {
                            CCanvas.startOKDlg(string, new IAction(){

                                public void perform() {
                                    CCanvas.inputDlg.setInfo(Language.createCharName(), new IAction(){

                                        public void perform() {
                                            if (CCanvas.inputDlg.tfInput.getText().length() != 0) {
                                                GameService.gI().onSendChangeRequest(CCanvas.inputDlg.tfInput.getText());
                                            }
                                        }
                                    }, null, 1);
                                    CCanvas.inputDlg.show();
                                }
                            });
                            break;
                        }
                        CCanvas.startOKDlg(string);
                        break;
                    }
                    case 30: {
                        CCanvas.endDlg();
                    }
                }
            }
            catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }

    public void setGameLogicHandler(IGameLogicHandler iGameLogicHandler) {
        this.gameLogicHandler = iGameLogicHandler;
    }

    static {
        nextTurnFlag = false;
        lag = false;
        LOCK = new Object();
        dem = 0;
    }
}

