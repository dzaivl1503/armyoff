/*
 * Decompiled with CFR 0.152.
 */
package com.teamobi.mobiarmy2;

import CLib.RMS;
import com.teamobi.mobiarmy2.OfflineChest;
import com.teamobi.mobiarmy2.OfflineMission;
import com.teamobi.mobiarmy2.OfflineTeamItems;
import coreLG.TerrainMidlet;
import item.Item;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.util.Vector;
import model.PlayerInfo;
import screen.ChangePlayerCSr;
import shop.ShopItem;

public final class OfflineSave {
    private static final String RMS_KEY = "offlineSave_v1";
    private static final int MAGIC = 1296118322;
    private static final int VERSION_CLASS_PROGRESS = 2;
    private static final int VERSION_SPECIAL_SHOP = 3;
    private static final int VERSION_TEAM_ITEMS = 4;
    private static final int VERSION_SQUAD = 5;
    private static final int VERSION_ITEM_LOADOUT = 6;
    private static final int VERSION_DRABY_GEMS = 7;
    private static final int VERSION_TRANGPHUC = 8;
    static final int VERSION_CRAFT_TIER = 9;
    private static final int VERSION_WORN_SLOT5 = 10;
    private static final int VERSION_MISSION_CLOUD_LINK = 11;
    private static final int VERSION_EXP_CARD = 12;
    private static boolean editorLastSaveSuccessful;

    public static String getSaveKey() {
        if (CloudSaveApi.isLoggedIn()) {
            String email = CloudSaveApi.getLinkedEmail();
            if (email != null && email.trim().length() > 0) {
                return "save_" + email.trim().toLowerCase();
            }
        }
        return "offlineSave_v1";
    }

    public static boolean hasSave() {
        byte[] byArray = RMS.loadRMS(getSaveKey());
        if (byArray == null || byArray.length < 8) {
            return false;
        }
        try {
            DataInputStream dataInputStream = new DataInputStream(new ByteArrayInputStream(byArray));
            if (dataInputStream.readInt() != 1296118322) {
                return false;
            }
            byte by = dataInputStream.readByte();
            return by >= 1 && by <= 12;
        }
        catch (Exception exception) {
            return false;
        }
    }

    public static void save() {
        editorLastSaveSuccessful = false;
        if (TerrainMidlet.myInfo == null) {
            return;
        }
        try {
            int n;
            int n2;
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
            PlayerInfo playerInfo = TerrainMidlet.myInfo;
            playerInfo.saveCurrentClassProgress();
            dataOutputStream.writeInt(1296118322);
            dataOutputStream.writeByte(12);
            dataOutputStream.writeUTF(playerInfo.name);
            dataOutputStream.writeInt(playerInfo.xu);
            dataOutputStream.writeInt(playerInfo.luong);
            dataOutputStream.writeInt(playerInfo.gun);
            dataOutputStream.writeInt(playerInfo.level2);
            dataOutputStream.writeInt(playerInfo.exp);
            dataOutputStream.writeInt(playerInfo.nextExp);
            dataOutputStream.writeInt(playerInfo.cup);
            dataOutputStream.writeByte(playerInfo.level2Percen);
            dataOutputStream.writeShort(playerInfo.point);
            for (n2 = 0; n2 < 10; ++n2) {
                dataOutputStream.writeByte(ChangePlayerCSr.isUnlock[n2]);
                dataOutputStream.writeInt(ChangePlayerCSr.gunXu[n2]);
                dataOutputStream.writeInt(ChangePlayerCSr.gunLuong[n2]);
                dataOutputStream.writeBoolean(TerrainMidlet.isVip[n2]);
            }
            for (n2 = 0; n2 < 10; ++n2) {
                for (n = 0; n < 5; ++n) {
                    dataOutputStream.writeShort(playerInfo.equipID[n2][n]);
                }
            }
            for (n2 = 0; n2 < 36; ++n2) {
                dataOutputStream.writeByte(ShopItem.getI((int)n2).num);
            }
            OfflineChest.writeEntries(dataOutputStream);
            OfflineChest.writeWornEntries(dataOutputStream);
            for (n2 = 0; n2 < 18; ++n2) {
                dataOutputStream.writeInt(OfflineMission.progress[n2]);
            }
            for (n2 = 0; n2 < 54; ++n2) {
                dataOutputStream.writeBoolean(OfflineMission.claimed[n2]);
            }
            dataOutputStream.writeInt(OfflineMission.loginStreak);
            dataOutputStream.writeInt(OfflineMission.lastLoginYmd);
            playerInfo.ensureClassProgress();
            for (n2 = 0; n2 < 10; ++n2) {
                dataOutputStream.writeInt(playerInfo.classExp[n2]);
                dataOutputStream.writeInt(playerInfo.classLevel2[n2]);
                dataOutputStream.writeInt(playerInfo.classLevel2Percen[n2]);
                dataOutputStream.writeInt(playerInfo.classNextExp[n2]);
                dataOutputStream.writeShort(playerInfo.classPoint[n2]);
                for (n = 0; n < 5; ++n) {
                    dataOutputStream.writeShort(playerInfo.classAbility[n2][n]);
                }
            }
            OfflineTeamItems.write(dataOutputStream);
            for (n2 = 0; n2 < playerInfo.squadExtra.length; ++n2) {
                dataOutputStream.writeByte(playerInfo.squadExtra[n2]);
            }
            for (n2 = 0; n2 < 5; ++n2) {
                dataOutputStream.writeShort(playerInfo.equipID[10][n2]);
            }
            dataOutputStream.writeInt(playerInfo.classExp[10]);
            dataOutputStream.writeInt(playerInfo.classLevel2[10]);
            dataOutputStream.writeInt(playerInfo.classLevel2Percen[10]);
            dataOutputStream.writeInt(playerInfo.classNextExp[10]);
            dataOutputStream.writeShort(playerInfo.classPoint[10]);
            for (n2 = 0; n2 < 5; ++n2) {
                dataOutputStream.writeShort(playerInfo.classAbility[10][n2]);
            }
            dataOutputStream.writeByte(ChangePlayerCSr.isUnlock[10]);
            dataOutputStream.writeInt(ChangePlayerCSr.gunXu[10]);
            dataOutputStream.writeInt(ChangePlayerCSr.gunLuong[10]);
            dataOutputStream.writeBoolean(TerrainMidlet.isVip[10]);
            OfflineChest.writeGun10WornEntries(dataOutputStream);
            for (n2 = 0; n2 < playerInfo.itemLoadout.length; ++n2) {
                for (n = 0; n < playerInfo.itemLoadout[n2].length; ++n) {
                    dataOutputStream.writeInt(playerInfo.itemLoadout[n2][n]);
                }
            }
            for (n2 = 0; n2 < playerInfo.equipID.length; ++n2) {
                dataOutputStream.writeShort(playerInfo.equipID[n2][5]);
            }
            OfflineChest.writeWornSlot5Entries(dataOutputStream);
            dataOutputStream.writeInt(OfflineMission.progress[18]);
            dataOutputStream.writeBoolean(OfflineMission.claimed[54]);
            OfflineTeamItems.writeExpCard(dataOutputStream);
            dataOutputStream.writeShort(OfflineMission.progress.length);
            for (n2 = 0; n2 < OfflineMission.progress.length; ++n2) {
                dataOutputStream.writeInt(OfflineMission.progress[n2]);
            }
            dataOutputStream.writeShort(OfflineMission.claimed.length);
            for (n2 = 0; n2 < OfflineMission.claimed.length; ++n2) {
                dataOutputStream.writeBoolean(OfflineMission.claimed[n2]);
            }
            dataOutputStream.close();
            RMS.saveRMS(getSaveKey(), byteArrayOutputStream.toByteArray());
            editorLastSaveSuccessful = true;
            CloudSaveApi.syncSaveSilently();
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public static boolean load() {
        byte[] byArray = RMS.loadRMS(getSaveKey());
        if (byArray == null) {
            return false;
        }
        try {
            int n;
            int n2;
            DataInputStream dataInputStream = new DataInputStream(new ByteArrayInputStream(byArray));
            if (dataInputStream.readInt() != 1296118322) {
                return false;
            }
            byte by = dataInputStream.readByte();
            if (by < 1 || by > 12) {
                return false;
            }
            PlayerInfo playerInfo = TerrainMidlet.myInfo = new PlayerInfo();
            playerInfo.IDDB = 1;
            playerInfo.name = dataInputStream.readUTF();
            playerInfo.xu = dataInputStream.readInt();
            playerInfo.luong = dataInputStream.readInt();
            playerInfo.gun = (byte)dataInputStream.readInt();
            playerInfo.level2 = dataInputStream.readInt();
            playerInfo.exp = dataInputStream.readInt();
            playerInfo.nextExp = dataInputStream.readInt();
            playerInfo.cup = dataInputStream.readInt();
            playerInfo.level2Percen = dataInputStream.readByte();
            playerInfo.point = dataInputStream.readShort();
            for (n2 = 0; n2 < 10; ++n2) {
                ChangePlayerCSr.isUnlock[n2] = dataInputStream.readByte();
                ChangePlayerCSr.gunXu[n2] = dataInputStream.readInt();
                ChangePlayerCSr.gunLuong[n2] = dataInputStream.readInt();
                TerrainMidlet.isVip[n2] = dataInputStream.readBoolean();
            }
            for (n2 = 0; n2 < 10; ++n2) {
                for (int i = 0; i < 5; ++i) {
                    playerInfo.equipID[n2][i] = dataInputStream.readShort();
                }
            }
            Vector<Item> vector = new Vector<Item>();
            for (n2 = 0; n2 < 36; ++n2) {
                vector.addElement(new Item((byte)n2, dataInputStream.readByte(), 1000, 5));
            }
            ShopItem.setItemVector(vector);
            try {
                n = dataInputStream.readShort();
                OfflineChest.queueLoad(OfflineChest.readEntries(dataInputStream, n, by));
                if (by >= 3) {
                    OfflineChest.readWornEntries(dataInputStream);
                }
            }
            catch (EOFException eOFException) {
                OfflineChest.queueLoad(null);
            }
            try {
                for (n = 0; n < 18; ++n) {
                    OfflineMission.progress[n] = dataInputStream.readInt();
                }
                for (n = 0; n < 54; ++n) {
                    OfflineMission.claimed[n] = dataInputStream.readBoolean();
                }
                OfflineMission.loginStreak = dataInputStream.readInt();
                OfflineMission.lastLoginYmd = dataInputStream.readInt();
            }
            catch (EOFException eOFException) {
                OfflineMission.reset();
            }
            if (by >= 2) {
                try {
                    for (n2 = 0; n2 < 10; ++n2) {
                        playerInfo.classExp[n2] = dataInputStream.readInt();
                        playerInfo.classLevel2[n2] = dataInputStream.readInt();
                        playerInfo.classLevel2Percen[n2] = dataInputStream.readInt();
                        playerInfo.classNextExp[n2] = dataInputStream.readInt();
                        playerInfo.classPoint[n2] = dataInputStream.readShort();
                        for (int i = 0; i < 5; ++i) {
                            playerInfo.classAbility[n2][i] = dataInputStream.readShort();
                        }
                    }
                    playerInfo.loadCurrentClassProgress();
                }
                catch (EOFException eOFException) {
                    playerInfo.migrateCurrentProgressToClass();
                }
            } else {
                playerInfo.migrateCurrentProgressToClass();
            }
            if (by >= 4) {
                try {
                    OfflineTeamItems.read(dataInputStream);
                }
                catch (EOFException eOFException) {
                    OfflineTeamItems.reset();
                }
            } else {
                OfflineTeamItems.reset();
            }
            if (by >= 5) {
                try {
                    for (n2 = 0; n2 < playerInfo.squadExtra.length; ++n2) {
                        playerInfo.squadExtra[n2] = dataInputStream.readByte();
                    }
                    for (n2 = 0; n2 < 5; ++n2) {
                        playerInfo.equipID[10][n2] = dataInputStream.readShort();
                    }
                    playerInfo.classExp[10] = dataInputStream.readInt();
                    playerInfo.classLevel2[10] = dataInputStream.readInt();
                    playerInfo.classLevel2Percen[10] = dataInputStream.readInt();
                    playerInfo.classNextExp[10] = dataInputStream.readInt();
                    playerInfo.classPoint[10] = dataInputStream.readShort();
                    for (n2 = 0; n2 < 5; ++n2) {
                        playerInfo.classAbility[10][n2] = dataInputStream.readShort();
                    }
                    if (playerInfo.gun == 10) {
                        playerInfo.loadCurrentClassProgress();
                    }
                    ChangePlayerCSr.isUnlock[10] = dataInputStream.readByte();
                    ChangePlayerCSr.gunXu[10] = dataInputStream.readInt();
                    ChangePlayerCSr.gunLuong[10] = dataInputStream.readInt();
                    TerrainMidlet.isVip[10] = dataInputStream.readBoolean();
                }
                catch (EOFException eOFException) {
                }
            }
            if (by >= 7) {
                try {
                    OfflineChest.readGun10WornEntries(dataInputStream);
                }
                catch (EOFException eOFException) {
                }
            }
            if (by >= 6) {
                try {
                    for (n2 = 0; n2 < playerInfo.itemLoadout.length; ++n2) {
                        for (int i = 0; i < playerInfo.itemLoadout[n2].length; ++i) {
                            playerInfo.itemLoadout[n2][i] = dataInputStream.readInt();
                        }
                    }
                }
                catch (EOFException eOFException) {
                }
            }
            if (by >= 8) {
                try {
                    for (n2 = 0; n2 < playerInfo.equipID.length; ++n2) {
                        playerInfo.equipID[n2][5] = dataInputStream.readShort();
                    }
                }
                catch (EOFException eOFException) {
                }
            }
            if (by >= 10) {
                try {
                    OfflineChest.readWornSlot5Entries(dataInputStream);
                }
                catch (EOFException eOFException) {
                }
            }
            if (by >= 11) {
                try {
                    OfflineMission.progress[18] = dataInputStream.readInt();
                    OfflineMission.claimed[54] = dataInputStream.readBoolean();
                }
                catch (EOFException eOFException) {
                }
            }
            if (by >= 12) {
                try {
                    OfflineTeamItems.readExpCard(dataInputStream);
                }
                catch (EOFException eOFException) {
                }
            }
            try {
                int progLen = dataInputStream.readShort();
                for (n2 = 0; n2 < progLen; ++n2) {
                    int pVal = dataInputStream.readInt();
                    if (n2 < OfflineMission.progress.length) {
                        OfflineMission.progress[n2] = pVal;
                    }
                }
                int claimLen = dataInputStream.readShort();
                for (n2 = 0; n2 < claimLen; ++n2) {
                    boolean cVal = dataInputStream.readBoolean();
                    if (n2 < OfflineMission.claimed.length) {
                        OfflineMission.claimed[n2] = cVal;
                    }
                }
            }
            catch (EOFException eOFException) {
            }
            dataInputStream.close();
            return true;
        }
        catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }
    }

    public static void clear() {
        try {
            RMS.saveRMS(RMS_KEY, new byte[0]);
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public static byte[] exportBytes() {
        OfflineSave.save();
        return RMS.loadRMS(getSaveKey());
    }

    public static boolean importBytes(byte[] byArray) {
        if (byArray == null || byArray.length == 0) {
            return false;
        }
        try {
            RMS.saveRMS(getSaveKey(), byArray);
        }
        catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }
        return OfflineSave.load();
    }

    public static boolean editorWasLastSaveSuccessful() {
        return editorLastSaveSuccessful;
    }
}

