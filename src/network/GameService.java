/*
 * Decompiled with CFR 0.152.
 */
package network;

import CLib.Image;
import CLib.mSystem;
import com.teamobi.mobiarmy2.GameMidlet;
import com.teamobi.mobiarmy2.OfflineCombat;
import com.teamobi.mobiarmy2.OfflineEquipmentStats;
import com.teamobi.mobiarmy2.OfflineMapTiles;
import com.teamobi.mobiarmy2.OfflineMission;
import com.teamobi.mobiarmy2.OfflineSave;
import com.teamobi.mobiarmy2.OfflineSpecialShop;
import com.teamobi.mobiarmy2.OfflineTeamItems;
import coreLG.CCanvas;
import coreLG.TerrainMidlet;
import item.Bullet;
import item.Item;
import java.io.IOException;
import java.util.Vector;
import map.HoleInfo;
import model.CRes;
import model.Language;
import model.PlayerInfo;
import model.RoomInfo;
import model.UserData;
import network.GameLogicHandler;
import network.ISession;
import network.Message;
import network.MessageHandler;
import player.PM;
import screen.ChangePlayerCSr;
import screen.GameScr;
import screen.RoomListScr2;
import shop.OfflineShopEquip;
import shop.ShopItem;

public class GameService {
    ISession session;
    protected static GameService instance;

    public void setSession(ISession iSession) {
        this.session = iSession;
    }

    public static GameService gI() {
        if (instance == null) {
            instance = new GameService();
        }
        return instance;
    }

    public void login(String string, String string2, String string3) {
        Message message = new Message(1);
        try {
            message.writer().writeUTF(string);
            message.writer().writeUTF(string2);
            message.writer().writeUTF(string3);
            this.session.sendMessage(message);
            message.cleanup();
        }
        catch (IOException iOException) {
        }
    }

    public void requestRoomList() {
        this.requestEmptyRoom((byte)0, (byte)-1, null);
    }

    public void requestRoomListOffline() {
        Vector<RoomInfo> vector = new Vector<RoomInfo>();
        for (int i = 0; i < 3; ++i) {
            RoomInfo roomInfo = new RoomInfo();
            roomInfo.id = (byte)(i + 10);
            roomInfo.boardID = (byte)(i + 1);
            roomInfo.lv = (byte)i;
            roomInfo.name = Language.area() + " " + (i + 1);
            roomInfo.playerMax = Language.room() + " " + (i + 1);
            vector.addElement(roomInfo);
        }
        if (CCanvas.roomListScr2 == null) {
            CCanvas.roomListScr2 = new RoomListScr2();
        }
        CCanvas.roomListScr2.isEmptyRoom = false;
        CCanvas.roomListScr2.isOfflineBossList = false;
        CCanvas.roomListScr2.setRoomList(vector);
        CCanvas.roomListScr2.show();
    }

    public void requestBoardList(byte by) {
        Message message = new Message(7);
        try {
            message.writer().writeByte(by);
        }
        catch (IOException iOException) {
        }
        this.session.sendMessage(message);
        message.cleanup();
        CRes.out("SendM RequestBoardList BID: " + by);
    }

    public void requestEmptyRoom(byte by, byte by2, String string) {
        CRes.out("=========> Gameservice request empty room type = " + by + " lv = " + by2 + " id = " + string);
        Message message = new Message(-28);
        try {
            message.writer().writeByte(by);
            if (by == 1) {
                message.writer().writeByte(by2);
            }
            if (by == 2) {
                message.writer().writeUTF(string);
            }
        }
        catch (IOException iOException) {
        }
        this.session.sendMessage(message);
        message.cleanup();
    }

    public void joinBoard(byte by, byte by2, String string) {
        Message message = new Message(8);
        try {
            message.writer().writeByte(by);
            message.writer().writeByte(by2);
            message.writer().writeUTF(string);
        }
        catch (IOException iOException) {
        }
        this.session.sendMessage(message);
        message.cleanup();
    }

    public void setBoardName(String string) {
        Message message = new Message(54);
        try {
            message.writer().writeUTF(string);
        }
        catch (IOException iOException) {
        }
        this.session.sendMessage(message);
        message.cleanup();
    }

    public void changeMODE(byte by) {
        Message message = new Message(73);
        try {
            message.writer().writeByte(by);
        }
        catch (IOException iOException) {
        }
        this.session.sendMessage(message);
        message.cleanup();
        CRes.out("GUI M - CHANGE_MODE: " + (by == 0 ? "TEAM_MODE" : "FREE_MODE"));
    }

    public void leaveBoard() {
        GameMidlet.leaveOfflineBattle();
    }

    public void ready(boolean bl) {
        Message message = new Message(16);
        try {
            message.writer().writeBoolean(bl);
        }
        catch (IOException iOException) {
        }
        this.session.sendMessage(message);
        message.cleanup();
    }

    public void setMoney(int n) {
        Message message = new Message(19);
        try {
            message.writer().writeInt(n);
        }
        catch (IOException iOException) {
        }
        this.session.sendMessage(message);
        message.cleanup();
    }

    public void setPassword(String string) {
        Message message = new Message(18);
        try {
            message.writer().writeUTF(string);
        }
        catch (IOException iOException) {
        }
        this.session.sendMessage(message);
        message.cleanup();
    }

    public void chatToBoard(String string) {
        Message message = new Message(9);
        try {
            message.writer().writeUTF(string);
        }
        catch (IOException iOException) {
        }
        this.session.sendMessage(message);
        message.cleanup();
    }

    public void kick(int n) {
        Message message = new Message(11);
        try {
            message.writer().writeInt(n);
        }
        catch (IOException iOException) {
        }
        this.session.sendMessage(message);
        message.cleanup();
        CRes.out("SendM Kick: ID" + n);
    }

    public void joinAnyBoard(byte by) {
        Message message = new Message(28);
        try {
            message.writer().writeByte(by);
        }
        catch (IOException iOException) {
        }
        this.session.sendMessage(message);
        message.cleanup();
    }

    public void checkFall(byte by, boolean bl) {
        Message message = new Message(80);
        try {
            message.writer().writeByte(by);
            message.writer().writeBoolean(bl);
        }
        catch (IOException iOException) {
        }
        this.session.sendMessage(message);
        message.cleanup();
    }

    public void requestRichest(int n) {
        Message message = new Message(31);
        try {
            message.writer().writeByte(n);
        }
        catch (IOException iOException) {
        }
        this.session.sendMessage(message);
        message.cleanup();
    }

    public void requestStrongest(int n) {
        Message message = new Message(30);
        try {
            message.writer().writeByte(n);
        }
        catch (IOException iOException) {
        }
        this.session.sendMessage(message);
        message.cleanup();
    }

    public void requestRegister(String string, String string2, String string3) {
        Message message = new Message(121);
        try {
            message.writer().writeUTF(string);
            message.writer().writeUTF(string2);
            message.writer().writeUTF(string3);
        }
        catch (IOException iOException) {
        }
        this.session.sendMessage(message);
        message.cleanup();
    }

    public void requestRegister3(String string, String string2, String string3) {
        Message message = new Message(-93);
        try {
            message.writer().writeUTF(string);
            message.writer().writeUTF(string2);
            message.writer().writeUTF(string3);
        }
        catch (IOException iOException) {
        }
        this.session.sendMessage(message);
        message.cleanup();
    }

    public void requestFriendList() {
        Message message = new Message(29);
        this.session.sendMessage(message);
        message.cleanup();
    }

    public void searchFriend(String string) {
        Message message = new Message(36);
        try {
            message.writer().writeUTF(string);
        }
        catch (IOException iOException) {
        }
        this.session.sendMessage(message);
        message.cleanup();
    }

    public void addFriend(int n) {
        Message message = new Message(32);
        try {
            message.writer().writeInt(n);
        }
        catch (IOException iOException) {
        }
        this.session.sendMessage(message);
        message.cleanup();
    }

    public void deleteFriend(int n) {
        Message message = new Message(33);
        try {
            message.writer().writeInt(n);
        }
        catch (IOException iOException) {
        }
        this.session.sendMessage(message);
        message.cleanup();
    }

    public void requestAvatar(short s) {
        Message message = new Message(38);
        try {
            message.writer().writeShort(s);
        }
        catch (IOException iOException) {
        }
        this.session.sendMessage(message);
        message.cleanup();
    }

    public void chatTo(int n, String string) {
        Message message = new Message(5);
        try {
            message.writer().writeInt(n);
            message.writer().writeUTF(string);
        }
        catch (IOException iOException) {
        }
        this.session.sendMessage(message);
        message.cleanup();
    }

    public void requestUserData() {
        Message message = new Message(40);
        this.session.sendMessage(message);
        message.cleanup();
    }

    public void ping(int n, long l) {
        Message message = new Message(42);
        try {
            message.writer().writeInt(n);
            MessageHandler.timePing = mSystem.currentTimeMillis();
        }
        catch (IOException iOException) {
        }
        this.session.sendMessage(message);
        message.cleanup();
    }

    public void requestAvatarShop() {
        Message message = new Message(39);
        this.session.sendMessage(message);
        message.cleanup();
    }

    public void updateDateProfile(UserData userData) {
        Message message = new Message(41);
        try {
            message.writer().writeUTF(userData.fullname);
            message.writer().writeByte(userData.gender);
            message.writer().writeInt(userData.birthYear);
            message.writer().writeUTF(userData.address);
            message.writer().writeUTF(userData.idnumber);
        }
        catch (IOException iOException) {
        }
        this.session.sendMessage(message);
        message.cleanup();
    }

    public void buyAvatar(short s) {
        Message message = new Message(43);
        try {
            message.writer().writeShort(s);
        }
        catch (IOException iOException) {
        }
        this.session.sendMessage(message);
    }

    public void setProvider(byte by) {
        Message message = new Message(58);
        try {
            message.writer().writeByte(1);
        }
        catch (IOException iOException) {
        }
        this.session.sendMessage(message);
    }

    public void requestChargeMoneyInfo2(byte by, String string) {
        Message message = new Message(122);
        try {
            message.writer().writeByte(by);
            if (by == 1) {
                message.writer().writeUTF(string);
            }
        }
        catch (IOException iOException) {
            iOException.printStackTrace();
        }
        CRes.out("====>requestChargeMoneyInfo2; type = " + by + " id = " + string);
        this.session.sendMessage(message);
        message.cleanup();
    }

    public void training(byte by) {
        if (by == 1) {
            GameMidlet.leaveOfflineBattle();
            return;
        }
        Message message = new Message(83);
        try {
            message.writer().writeByte(by);
        }
        catch (IOException iOException) {
            iOException.printStackTrace();
        }
        this.session.sendMessage(message);
        message.cleanup();
    }

    public void doLoadCard(String string, String string2, String string3) {
        Message message = new Message(77);
        try {
            message.writer().writeUTF(string);
            message.writer().writeUTF(string2);
            message.writer().writeUTF(string3);
        }
        catch (IOException iOException) {
            iOException.printStackTrace();
        }
        CRes.out("====> send cmd 77");
        CRes.out("====> doLoadCard " + string);
        CRes.out("====> doLoadCard " + string2);
        CRes.out("====> doLoadCard " + string3);
        this.session.sendMessage(message);
        message.cleanup();
    }

    public void sendAdminCommand(String string) {
        Message message = new Message(47);
        try {
            message.writer().writeUTF(string);
        }
        catch (IOException iOException) {
        }
        this.session.sendMessage(message);
    }

    public void startGame() {
        Message message = new Message(20);
        this.session.sendMessage(message);
        message.cleanup();
    }

    public void mapSelect(byte by) {
        Message message = new Message(75);
        try {
            message.writer().writeByte(by);
        }
        catch (IOException iOException) {
            iOException.printStackTrace();
        }
        this.session.sendMessage(message);
        message.cleanup();
    }

    public void trainingMap() {
        Message message = new Message(-6);
        this.session.sendMessage(message);
        message.cleanup();
    }

    public void check_cross(byte n, int[] nArray, int[] nArray2, byte by) {
        if (OfflineCombat.isActiveBattle()) {
            OfflineCombat.applyExplosionDamage((byte)n, nArray, nArray2, by);
            return;
        }
        Message message = new Message(79);
        try {
            message.writer().writeByte(n);
            for (int i = 0; i < n; ++i) {
                CRes.out("x= " + nArray[i]);
                message.writer().writeInt(nArray[i]);
                message.writer().writeInt(nArray2[i]);
            }
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
        this.session.sendMessage(message);
        message.cleanup();
    }

    public void move(short s, short s2) {
        if (OfflineCombat.isActiveBattle()) {
            OfflineCombat.moveLocal(GameScr.myIndex, s, s2);
            return;
        }
        if (!GameScr.trainingMode) {
            Message message = new Message(21);
            CRes.out(" move to " + s + "," + s2);
            try {
                message.writer().writeShort(s);
                message.writer().writeShort(s2);
            }
            catch (Exception exception) {
            }
            this.session.sendMessage(message);
            message.cleanup();
        }
    }

    public void waitForFIRETraining(byte by, short s, short s2, short s3, byte by2, byte by3, byte by4) {
        if (OfflineCombat.isActiveBattle()) {
            OfflineCombat.onWaitForFire(by, s, s2, s3, by2, by3, by4);
            return;
        }
        Message message = new Message(84);
        try {
            message.writer().writeByte(by);
            message.writer().writeShort(s);
            message.writer().writeShort(s2);
            message.writer().writeShort(s3);
            message.writer().writeByte(by2);
            if (Bullet.isDoubleBull(by)) {
                message.writer().writeByte(by3);
            }
            message.writer().writeByte(by4);
        }
        catch (Exception exception) {
        }
        this.session.sendMessage(message);
        message.cleanup();
        CRes.out("SendWait_Fire bull: " + by + " nShoot: " + by4 + " force2= " + by3);
    }

    public void waitForFIRE(byte by, short s, short s2, short s3, byte by2, byte by3, byte by4) {
        if (OfflineCombat.isActiveBattle()) {
            OfflineCombat.onWaitForFire(by, s, s2, s3, by2, by3, by4);
            return;
        }
        Message message = new Message(22);
        try {
            message.writer().writeByte(by);
            message.writer().writeShort(s);
            message.writer().writeShort(s2);
            message.writer().writeShort(s3);
            message.writer().writeByte(by2);
            if (Bullet.isDoubleBull(by)) {
                message.writer().writeByte(by3);
            }
            message.writer().writeByte(by4);
        }
        catch (Exception exception) {
        }
        this.session.sendMessage(message);
        message.cleanup();
    }

    public void shootResult() {
        if (OfflineCombat.isActiveBattle()) {
            OfflineCombat.onShootResult();
            return;
        }
        if (!GameScr.trainingMode) {
            Message message = new Message(23);
            this.session.sendMessage(message);
            message.cleanup();
        }
    }

    public void requiredUpdateXY(short s, short s2) {
        CRes.out("==> requiredUpdateXY " + s + "_" + s2);
        if (!GameScr.trainingMode) {
            Message message = new Message(53);
            try {
                message.writer().writeShort(s);
                message.writer().writeShort(s2);
            }
            catch (Exception exception) {
                exception.printStackTrace();
            }
            this.session.sendMessage(message);
            message.cleanup();
        }
    }

    public void inviteFriend(boolean bl, int n) {
        Message message = new Message(78);
        try {
            message.writer().writeBoolean(bl);
            message.writer().writeInt(n);
        }
        catch (Exception exception) {
        }
        this.session.sendMessage(message);
        message.cleanup();
    }

    public void skipTurn() {
        if (OfflineCombat.isActiveBattle()) {
            OfflineCombat.onShootResult();
            return;
        }
        if (!GameScr.trainingMode) {
            Message message = new Message(49);
            this.session.sendMessage(message);
            message.cleanup();
        }
    }

    public void useItem(byte by) {
        if (OfflineCombat.isActiveBattle()) {
            if (PM.p != null && GameScr.myIndex >= 0 && GameScr.myIndex < PM.p.length && PM.p[GameScr.myIndex] != null) {
                PM.p[GameScr.myIndex].UseItem(by, true, 0);
                OfflineCombat.applyItemEffect(by);
                OfflineMission.onUseItem(by);
            }
            return;
        }
        if (!GameScr.trainingMode) {
            Message message = new Message(26);
            try {
                message.writer().writeByte(by);
            }
            catch (Exception exception) {
            }
            this.session.sendMessage(message);
            message.cleanup();
            CRes.out("==========> SendM UseITEM " + by);
        }
    }

    public void changeItem(int[] nArray) {
        Message message = new Message(68);
        try {
            for (int i = 0; i < nArray.length; ++i) {
                message.writer().writeByte(nArray[i]);
            }
        }
        catch (Exception exception) {
        }
        this.session.sendMessage(message);
        message.cleanup();
        CRes.out("SendM changeGun " + nArray.length);
    }

    public void changeGun(byte by) {
        ChangePlayerCSr.changeGunOffline(by);
    }

    public void selectMap(byte by) {
        Message message = new Message(70);
        try {
            message.writer().writeByte(by);
        }
        catch (Exception exception) {
        }
        this.session.sendMessage(message);
        message.cleanup();
        CRes.out("SendM ChangeMap " + by);
    }

    public void changeTeam() {
        Message message = new Message(71);
        this.session.sendMessage(message);
        message.cleanup();
    }

    public void disconnect() {
        Message message = new Message(-4);
        this.session.sendMessage(message);
        message.cleanup();
    }

    public void setMaxPlayer(int n) {
        Message message = new Message(56);
        try {
            message.writer().writeByte(n);
        }
        catch (Exception exception) {
        }
        this.session.sendMessage(message);
    }

    public void requestInfoOf(int n) {
        Message message = new Message(34);
        try {
            message.writer().writeInt(n);
        }
        catch (Exception exception) {
        }
        this.session.sendMessage(message);
    }

    public void requestChangePass(String string, String string2) {
        Message message = new Message(81);
        try {
            message.writer().writeUTF(string);
            message.writer().writeUTF(string2);
        }
        catch (Exception exception) {
        }
        this.session.sendMessage(message);
    }

    public void requestBuyItem(byte by, byte by2, byte by3) {
        this.buyItemOffline(by, by2, by3);
    }

    private void buyItemOffline(byte by, byte by2, byte by3) {
        Item item = ShopItem.getI(by2);
        if (by == 0) {
            int n = item.price * by3;
            if (n <= 0 || TerrainMidlet.myInfo.xu < n) {
                CCanvas.startOKDlg(Language.kocotien());
                return;
            }
            TerrainMidlet.myInfo.xu -= n;
        } else {
            int n = item.price2 * by3;
            if (n <= 0 || TerrainMidlet.myInfo.luong < n) {
                CCanvas.startOKDlg(Language.kocotien());
                return;
            }
            TerrainMidlet.myInfo.luong -= n;
        }
        ShopItem.receiveAItemBuy((byte)1, new byte[]{by2}, new byte[]{item.num}, TerrainMidlet.myInfo.xu, TerrainMidlet.myInfo.luong);
        OfflineSave.save();
    }

    public void buyGun(byte by, byte by2) {
        ChangePlayerCSr.buyGunOffline(by, by2);
    }

    public void requestService(byte by, String string) {
        if (string == null) {
            string = "";
        }
        Message message = new Message(85);
        try {
            message.writer().writeByte(by);
            message.writer().writeUTF(string);
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
        this.session.sendMessage(message);
    }

    public void zingConnect(String string, String string2, byte by, String string3) {
        Message message = new Message(87);
        try {
            message.writer().writeUTF(string);
            message.writer().writeUTF(string2);
            message.writer().write(by);
            message.writer().writeUTF(string3);
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
        this.session.sendMessage(message);
    }

    public void getString(String string) {
        Message message = new Message(127);
        try {
            CRes.out("STRING = " + string);
            message.writer().writeUTF(string);
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
        this.session.sendMessage(message);
    }

    public void getProviderAgent() {
        CRes.out("=====> get provider and agent");
        Message message = new Message(-26);
        this.session.sendMessage(message);
    }

    public void sendVersion(byte by, byte by2) {
        Message message = new Message(90);
        try {
            message.writer().writeByte(by);
            message.writer().writeByte(by2);
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
        this.session.sendMessage(message);
    }

    public void addPoint(byte[] byArray) {
        for (int i = 0; i < byArray.length && i < TerrainMidlet.myInfo.ability.length; ++i) {
            int n = i;
            TerrainMidlet.myInfo.ability[n] = (short)(TerrainMidlet.myInfo.ability[n] + byArray[i]);
        }
        TerrainMidlet.myInfo.maxHP = OfflineEquipmentStats.maxHp(TerrainMidlet.myInfo);
        TerrainMidlet.myInfo.saveCurrentClassProgress();
        OfflineSave.save();
    }

    public void changeEquip(int[] nArray) {
        PlayerInfo playerInfo = TerrainMidlet.myInfo;
        if (playerInfo != null) {
            if (CCanvas.equipScreen != null) {
                CCanvas.equipScreen.commitOfflineInventory();
            }
            if (playerInfo.myEquip != null && playerInfo.myEquip.equips != null) {
                for (int i = 0; i < playerInfo.myEquip.equips.length; ++i) {
                    if (playerInfo.myEquip.equips[i] == null) continue;
                    playerInfo.equipID[playerInfo.gun][i] = playerInfo.myEquip.equips[i].id;
                    playerInfo.dbKey[i] = playerInfo.myEquip.equips[i].dbKey;
                }
            }
            playerInfo.setAllEquipEffect();
            playerInfo.maxHP = OfflineEquipmentStats.maxHp(playerInfo);
            OfflineSave.save();
            if (CCanvas.equipScreen != null) {
                CCanvas.equipScreen.getLastEquip();
            }
        }
        CCanvas.endDlg();
        CCanvas.startOKDlg("\u0110\u00e3 l\u01b0u trang b\u1ecb.");
    }

    public void getShopEquip() {
        OfflineShopEquip.openShop();
    }

    public void buy_sell_Equip(byte by, int[] nArray, short s, byte by2) {
        if (by == 0 && CCanvas.shopEquipScr != null) {
            OfflineShopEquip.buyEquip(CCanvas.shopEquipScr.getCurrEq(), by2);
        } else if (by == 1) {
            OfflineShopEquip.sellEquip(nArray);
        }
    }

    public void sendRulet(byte by) {
        Message message = new Message(110);
        try {
            message.writer().writeByte(by);
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
        this.session.sendMessage(message);
    }

    public void charactorInfo() {
        Message message = new Message(99);
        this.session.sendMessage(message);
    }

    public void platform_request() {
        Message message = new Message(114);
        try {
            if (GameMidlet.DEVICE == 2) {
                message.writer().writeUTF("iphone");
            } else if (GameMidlet.DEVICE == 1) {
                message.writer().writeUTF("android");
            } else if (GameMidlet.DEVICE == 4) {
                message.writer().writeUTF("pc");
            } else if (GameMidlet.DEVICE == 0) {
                message.writer().writeUTF("j2me{HD}");
            } else {
                message.writer().writeUTF("j2me{HD}");
            }
            message.writer().writeByte(GameMidlet.versioncode);
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
        this.session.sendMessage(message);
    }

    public void vip_equip(byte by, int n) {
        CRes.out("GUI + " + by);
        Message message = new Message(-2);
        try {
            message.writer().writeByte(by);
            message.writer().writeInt(n);
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
        this.session.sendMessage(message);
    }

    public void topClan(byte by) {
        Message message = new Message(116);
        try {
            message.writer().writeByte(by);
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
        this.session.sendMessage(message);
    }

    public void getClanIcon(short s) {
        if (s != 0 && s != -1) {
            Image image = null;
            if (CCanvas.iconMn.isExist(s)) {
                CRes.out("tim thay icon");
                image = CCanvas.iconMn.getImage(s);
                GameLogicHandler.gI().onGetImage(s, image);
            } else {
                CRes.out("request icon");
                Message message = new Message(115);
                try {
                    message.writer().writeShort(s);
                }
                catch (Exception exception) {
                    exception.printStackTrace();
                }
                this.session.sendMessage(message);
            }
        }
    }

    public void clanInfo(short s) {
        Message message = new Message(117);
        try {
            message.writer().writeShort(s);
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
        this.session.sendMessage(message);
    }

    public void clanMember(byte by, short s) {
        Message message = new Message(118);
        try {
            message.writer().writeByte(by);
            message.writer().writeShort(s);
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
        this.session.sendMessage(message);
    }

    public void getBigImage(byte by) {
        Message message = new Message(120);
        try {
            message.writer().writeByte(by);
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
        this.session.sendMessage(message);
    }

    public void chatTeam(String string) {
        Message message = new Message(123);
        try {
            message.writer().writeUTF(string);
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
        this.session.sendMessage(message);
    }

    public void getMaterialIcon(byte by, int n, int n2) {
        if (by == 2) {
            OfflineMapTiles.tryLoadMapIcon(n);
            return;
        }
        CRes.out("get material icon " + n);
        Message message = new Message(126);
        try {
            message.writer().writeByte(by);
            message.writer().writeByte(n);
            if (by == 3) {
                message.writer().writeByte(n2);
            }
            if (by == 4) {
                message.writer().writeByte(n2);
            }
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
        this.session.sendMessage(message);
    }

    public void getFomula(byte by, byte by2, byte by3) {
        OfflineSpecialShop.formula(by, by2, by3);
    }

    public void imbue(byte by, byte by2, int[] nArray, byte[] byArray) {
        OfflineSpecialShop.imbue(by, by2, nArray, byArray);
    }

    public void getShopLinhtinh(byte by, byte by2, byte by3, byte by4) {
        OfflineSpecialShop.shop(by, by2, by3, by4);
    }

    public void signOut() {
        Message message = new Message(-4);
        this.session.sendMessage(message);
    }

    public void changeRoomName() {
        Message message = new Message(-19);
        this.session.sendMessage(message);
    }

    public void getShopBietDoi(byte by, byte by2, byte by3) {
        OfflineTeamItems.shop(by, by2, by3);
    }

    public void bangxephang(byte by, int n) {
        CRes.out("request list : page= " + n);
        Message message = new Message(-14);
        try {
            message.writer().writeByte(by);
            message.writer().writeByte(n);
        }
        catch (IOException iOException) {
        }
        this.session.sendMessage(message);
        message.cleanup();
    }

    public void luckGift(byte by) {
        CRes.out(" =======> send Lucky gift to server id == " + by);
        Message message = new Message(-17);
        try {
            message.writer().writeByte(by);
        }
        catch (IOException iOException) {
        }
        this.session.sendMessage(message);
        message.cleanup();
    }

    public void mission(byte by, byte by2) {
        Message message = new Message(-23);
        try {
            message.writer().writeByte(by);
            if (by == 1) {
                message.writer().writeByte(by2);
            }
        }
        catch (IOException iOException) {
        }
        this.session.sendMessage(message);
        message.cleanup();
    }

    public void inputMoney(byte by, int n) {
        Message message = new Message(-21);
        try {
            message.writer().writeByte(by);
            message.writer().writeInt(n);
        }
        catch (IOException iOException) {
        }
        this.session.sendMessage(message);
        message.cleanup();
    }

    public void get_more_day(byte by, int n) {
        CRes.out("Gia han");
        Message message = new Message(-25);
        try {
            message.writer().writeByte(by);
            message.writer().writeInt(n);
        }
        catch (IOException iOException) {
        }
        this.session.sendMessage(message);
        message.cleanup();
    }

    public void holeInfo(Vector vector) {
        if (OfflineCombat.isActiveBattle()) {
            if (vector != null) {
                vector.removeAllElements();
            }
            return;
        }
        if (!CCanvas.isDebugging()) {
            Message message = new Message(-92);
            try {
                message.writer().writeByte(vector.size());
                for (int i = 0; i < vector.size(); ++i) {
                    HoleInfo holeInfo = (HoleInfo)vector.elementAt(i);
                    message.writer().writeShort(holeInfo.mapID);
                    message.writer().writeShort(holeInfo.x);
                    message.writer().writeShort(holeInfo.y);
                    message.writer().writeByte(holeInfo.holeType);
                }
            }
            catch (IOException iOException) {
            }
            this.session.sendMessage(message);
            message.cleanup();
        }
    }

    public void debugServer() {
        Message message = new Message(-25);
        this.session.sendMessage(message);
        message.cleanup();
    }

    public void onRegisterNickFree(String string, String string2, String string3) {
        CRes.out("=========> START REGISTER NEW FREE ACCOUNT!");
        Message message = new Message(121);
        try {
            message.writer().writeUTF(string);
            message.writer().writeUTF(string2);
            message.writer().writeUTF(string3);
        }
        catch (Exception exception) {
        }
        this.session.sendMessage(message);
        message.cleanup();
    }

    public void onSendChangeRequest(String string) {
        Message message = new Message(-103);
        try {
            message.writer().writeUTF(string);
        }
        catch (Exception exception) {
        }
        this.session.sendMessage(message);
        message.cleanup();
    }

    public void onInApppurchaseToServer(String string, String string2) {
        if (string == null) {
            string = "Ko co product ID";
        }
        if (string2 == null) {
            string2 = "Ko co product token";
        }
        Message message = new Message(-102);
        try {
            message.writer().writeUTF(string);
            message.writer().writeUTF(string2);
        }
        catch (Exception exception) {
        }
        this.session.sendMessage(message);
        message.cleanup();
    }
}

