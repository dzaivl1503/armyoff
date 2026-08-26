/*
 * Decompiled with CFR 0.152.
 */
package com.teamobi.mobiarmy2;

import Equipment.Equip;
import Equipment.PlayerEquip;
import com.teamobi.mobiarmy2.OfflineSpecialShop;
import coreLG.CCanvas;
import coreLG.TerrainMidlet;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Vector;
import model.PlayerInfo;
import screen.EquipScreen;
import shop.ShopEquipCatalog;

public final class OfflineChest {
    private static Vector pendingLoad;
    private static byte[][] wornSlots;
    private static byte[][][] wornAbility;
    private static byte[][][] wornPercen;
    private static byte[][][] wornGems;

    private OfflineChest() {
    }

    public static void clear() {
        EquipScreen.inventory.removeAllElements();
        pendingLoad = null;
        wornSlots = new byte[11][6];
        wornAbility = new byte[11][6][5];
        wornPercen = new byte[11][6][5];
        wornGems = OfflineChest.createEmptyWornGems();
        if (CCanvas.equipScreen != null) {
            CCanvas.equipScreen.getMyEquip();
        }
    }

    public static void add(Equip equip) {
        if (equip == null || TerrainMidlet.myInfo == null) {
            return;
        }
        if (!equip.isMaterial) {
            for (int i = 0; i < EquipScreen.inventory.size(); ++i) {
                Equip exist = (Equip)EquipScreen.inventory.elementAt(i);
                if (exist != null && !exist.isMaterial && exist.glass == equip.glass && exist.type == equip.type && exist.id == equip.id) {
                    return;
                }
            }
        }
        if (CCanvas.equipScreen == null) {
            CCanvas.equipScreen = new EquipScreen();
        }
        equip.glass = TerrainMidlet.myInfo.gun;
        equip.isMaterial = false;
        if (equip.dbKey <= 0) {
            equip.dbKey = OfflineChest.nextDbKey();
        }
        equip.slot = (byte)equip.socketCount();
        OfflineChest.ensureDisplayName(equip);
        EquipScreen.inventory.insertElementAt(equip, 0);
        CCanvas.equipScreen.getMyEquip();
    }

    public static void removeWornItems() {
    }

    public static int nextDbKey() {
        Object object;
        int n = 0;
        for (int i = 0; i < EquipScreen.inventory.size(); ++i) {
            object = (Equip)EquipScreen.inventory.elementAt(i);
            if (((Equip)object).dbKey <= n) continue;
            n = ((Equip)object).dbKey;
        }
        object = TerrainMidlet.myInfo;
        if (object != null && ((PlayerInfo)object).myEquip != null && ((PlayerInfo)object).myEquip.equips != null) {
            for (int i = 0; i < ((PlayerInfo)object).myEquip.equips.length; ++i) {
                Equip equip = ((PlayerInfo)object).myEquip.equips[i];
                if (equip == null || equip.dbKey <= n) continue;
                n = equip.dbKey;
            }
        }
        return n + 1;
    }

    public static void queueLoad(Vector vector) {
        pendingLoad = vector;
    }

    public static void applyPendingLoad() {
        if (pendingLoad == null || PlayerEquip.playerData == null) {
            pendingLoad = null;
            return;
        }
        EquipScreen.inventory.removeAllElements();
        for (int i = 0; i < pendingLoad.size(); ++i) {
            Equip equip;
            SavedEntry savedEntry = (SavedEntry)pendingLoad.elementAt(i);
            if (!savedEntry.material && savedEntry.type == 5 && PlayerEquip.isMaskItem(savedEntry.glass, savedEntry.id)) {
                savedEntry.type = (byte)1;
            }
            if (!savedEntry.material) {
                boolean duplicate = false;
                for (int k = 0; k < EquipScreen.inventory.size(); ++k) {
                    Equip exist = (Equip)EquipScreen.inventory.elementAt(k);
                    if (exist != null && !exist.isMaterial && exist.glass == savedEntry.glass && exist.type == savedEntry.type && exist.id == savedEntry.id) {
                        duplicate = true;
                        break;
                    }
                }
                if (duplicate) {
                    continue;
                }
            }
            Equip equip2 = equip = savedEntry.material ? new Equip() : PlayerEquip.createEquip(savedEntry.glass, savedEntry.type, savedEntry.id);
            if (equip == null) continue;
            equip.glass = savedEntry.glass;
            equip.type = savedEntry.type;
            equip.id = savedEntry.id;
            equip.dbKey = savedEntry.dbKey;
            equip.date = savedEntry.date;
            equip.craftTier = savedEntry.craftTier;
            if (savedEntry.name != null && savedEntry.name.length() > 0) {
                equip.name = savedEntry.name;
            }
            equip.slot = savedEntry.slot;
            equip.isMaterial = savedEntry.material;
            equip.num = savedEntry.num;
            equip.socketGems = savedEntry.gems;
            for (int j = 0; j < 5; ++j) {
                equip.inv_ability[j] = OfflineChest.clampWornStat(savedEntry.ability[j]);
                equip.inv_percen[j] = OfflineChest.clampWornStat(savedEntry.percen[j]);
            }
            if (savedEntry.material) {
                equip.icon = savedEntry.id;
                equip.strDetail = OfflineSpecialShop.itemDetail(savedEntry.id);
                equip.materialIcon = OfflineSpecialShop.itemIcon(savedEntry.id);
            } else {
                OfflineChest.ensureDisplayName(equip);
            }
            EquipScreen.inventory.addElement(equip);
        }
        pendingLoad = null;
        if (CCanvas.equipScreen == null) {
            CCanvas.equipScreen = new EquipScreen();
        }
        if (TerrainMidlet.myInfo != null && TerrainMidlet.myInfo.myEquip != null) {
            OfflineChest.applyWorn(TerrainMidlet.myInfo);
            CCanvas.equipScreen.getMyEquip();
        }
    }

    public static Equip copyWornSlot(PlayerInfo playerInfo, byte by) {
        if (playerInfo == null || by < 0 || by >= 6) {
            return null;
        }
        short s = playerInfo.equipID[playerInfo.gun][by];
        if (s <= 0) {
            return null;
        }
        Equip equip = PlayerEquip.createEquip(playerInfo.gun, by, s);
        if (playerInfo.myEquip != null && playerInfo.myEquip.equips[by] != null && playerInfo.myEquip.equips[by].id == s) {
            if (equip == null) {
                equip = new Equip();
            }
            equip.changeToEquip(playerInfo.myEquip.equips[by]);
            equip.glass = playerInfo.gun;
            equip.type = by;
            equip.id = s;
        }
        if (equip != null) {
            OfflineChest.ensureDisplayName(equip);
        }
        return equip;
    }

    public static void ensureDisplayName(Equip equip) {
        if (equip == null) {
            return;
        }
        if (equip.name != null && equip.name.trim().length() > 0) {
            return;
        }
        equip.name = ShopEquipCatalog.resolveName(equip.glass, equip.id);
    }

    static Vector readEntries(DataInputStream dataInputStream, int n, int n2) throws IOException {
        Vector<SavedEntry> vector = new Vector<SavedEntry>();
        for (int i = 0; i < n; ++i) {
            SavedEntry savedEntry = new SavedEntry();
            savedEntry.glass = dataInputStream.readByte();
            savedEntry.type = dataInputStream.readByte();
            savedEntry.id = dataInputStream.readShort();
            savedEntry.dbKey = dataInputStream.readInt();
            savedEntry.date = dataInputStream.readByte();
            savedEntry.name = dataInputStream.readUTF();
            if (n2 >= 3) {
                int n3;
                savedEntry.material = dataInputStream.readBoolean();
                savedEntry.num = dataInputStream.readInt();
                savedEntry.slot = dataInputStream.readByte();
                for (n3 = 0; n3 < 5; ++n3) {
                    savedEntry.ability[n3] = dataInputStream.readByte();
                    savedEntry.percen[n3] = dataInputStream.readByte();
                }
                for (n3 = 0; n3 < 3; ++n3) {
                    savedEntry.gems[n3] = dataInputStream.readByte();
                }
                savedEntry.craftTier = n2 >= 9 ? dataInputStream.readByte() : (byte)0;
            } else {
                savedEntry.num = 1;
                savedEntry.slot = 0;
                savedEntry.craftTier = 0;
            }
            vector.addElement(savedEntry);
        }
        return vector;
    }

    static void writeEntries(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeShort(EquipScreen.inventory.size());
        for (int i = 0; i < EquipScreen.inventory.size(); ++i) {
            int n;
            Equip equip = (Equip)EquipScreen.inventory.elementAt(i);
            dataOutputStream.writeByte(equip.glass);
            dataOutputStream.writeByte(equip.type);
            dataOutputStream.writeShort(equip.id);
            dataOutputStream.writeInt(equip.dbKey);
            dataOutputStream.writeByte(equip.date);
            dataOutputStream.writeUTF(equip.name == null ? "" : equip.name);
            dataOutputStream.writeBoolean(equip.isMaterial);
            dataOutputStream.writeInt(equip.num);
            dataOutputStream.writeByte(equip.slot);
            for (n = 0; n < 5; ++n) {
                dataOutputStream.writeByte(equip.inv_ability[n]);
                dataOutputStream.writeByte(equip.inv_percen[n]);
            }
            for (n = 0; n < 3; ++n) {
                dataOutputStream.writeByte(equip.socketGems == null ? -1 : equip.socketGems[n]);
            }
            dataOutputStream.writeByte(equip.craftTier);
        }
    }

    private static byte[][][] createEmptyWornGems() {
        byte[][][] byArray = new byte[11][6][3];
        for (int i = 0; i < 11; ++i) {
            for (int j = 0; j < 6; ++j) {
                for (int k = 0; k < 3; ++k) {
                    byArray[i][j][k] = -1;
                }
            }
        }
        return byArray;
    }

    public static void captureWorn(PlayerInfo playerInfo) {
        if (playerInfo == null || playerInfo.myEquip == null) {
            return;
        }
        for (int i = 0; i < 5; ++i) {
            int n;
            Equip equip = playerInfo.myEquip.equips[i];
            if (equip == null) continue;
            OfflineChest.wornSlots[playerInfo.gun][i] = equip.slot;
            for (n = 0; n < 5; ++n) {
                OfflineChest.wornAbility[playerInfo.gun][i][n] = equip.inv_ability[n];
                OfflineChest.wornPercen[playerInfo.gun][i][n] = equip.inv_percen[n];
            }
            for (n = 0; n < 3; ++n) {
                OfflineChest.wornGems[playerInfo.gun][i][n] = equip.socketGems == null ? -1 : equip.socketGems[n];
            }
        }
    }

    public static void applyWorn(PlayerInfo playerInfo) {
        if (playerInfo == null || playerInfo.myEquip == null) {
            return;
        }
        for (int i = 0; i < 5; ++i) {
            OfflineChest.applyWornSlot(playerInfo, i);
        }
    }

    public static void applyWornSlot(PlayerInfo playerInfo, int n) {
        if (playerInfo == null || playerInfo.myEquip == null) {
            return;
        }
        Equip equip = playerInfo.myEquip.equips[n];
        if (equip != null) {
            int n2;
            equip.slot = wornSlots[playerInfo.gun][n];
            for (n2 = 0; n2 < 5; ++n2) {
                equip.inv_ability[n2] = wornAbility[playerInfo.gun][n][n2];
                equip.inv_percen[n2] = wornPercen[playerInfo.gun][n][n2];
            }
            for (n2 = 0; n2 < 3; ++n2) {
                equip.socketGems[n2] = wornGems[playerInfo.gun][n][n2];
            }
            equip.setInvAtribute();
        }
    }

    static void writeWornEntries(DataOutputStream dataOutputStream) throws IOException {
        OfflineChest.captureWorn(TerrainMidlet.myInfo);
        for (int i = 0; i < 10; ++i) {
            for (int j = 0; j < 5; ++j) {
                int n;
                dataOutputStream.writeByte(wornSlots[i][j]);
                for (n = 0; n < 5; ++n) {
                    dataOutputStream.writeByte(wornAbility[i][j][n]);
                    dataOutputStream.writeByte(wornPercen[i][j][n]);
                }
                for (n = 0; n < 3; ++n) {
                    dataOutputStream.writeByte(wornGems[i][j][n]);
                }
            }
        }
    }

    private static byte clampWornStat(byte by) {
        return by < 0 ? (byte)127 : (byte)by;
    }

    static void readWornEntries(DataInputStream dataInputStream) throws IOException {
        for (int i = 0; i < 10; ++i) {
            for (int j = 0; j < 5; ++j) {
                int n;
                OfflineChest.wornSlots[i][j] = dataInputStream.readByte();
                for (n = 0; n < 5; ++n) {
                    OfflineChest.wornAbility[i][j][n] = OfflineChest.clampWornStat(dataInputStream.readByte());
                    OfflineChest.wornPercen[i][j][n] = OfflineChest.clampWornStat(dataInputStream.readByte());
                }
                for (n = 0; n < 3; ++n) {
                    OfflineChest.wornGems[i][j][n] = dataInputStream.readByte();
                }
            }
        }
    }

    static void writeGun10WornEntries(DataOutputStream dataOutputStream) throws IOException {
        for (int i = 0; i < 5; ++i) {
            int n;
            dataOutputStream.writeByte(wornSlots[10][i]);
            for (n = 0; n < 5; ++n) {
                dataOutputStream.writeByte(wornAbility[10][i][n]);
                dataOutputStream.writeByte(wornPercen[10][i][n]);
            }
            for (n = 0; n < 3; ++n) {
                dataOutputStream.writeByte(wornGems[10][i][n]);
            }
        }
    }

    static void readGun10WornEntries(DataInputStream dataInputStream) throws IOException {
        for (int i = 0; i < 5; ++i) {
            int n;
            OfflineChest.wornSlots[10][i] = dataInputStream.readByte();
            for (n = 0; n < 5; ++n) {
                OfflineChest.wornAbility[10][i][n] = OfflineChest.clampWornStat(dataInputStream.readByte());
                OfflineChest.wornPercen[10][i][n] = OfflineChest.clampWornStat(dataInputStream.readByte());
            }
            for (n = 0; n < 3; ++n) {
                OfflineChest.wornGems[10][i][n] = dataInputStream.readByte();
            }
        }
    }

    static void writeWornSlot5Entries(DataOutputStream dataOutputStream) throws IOException {
        for (int i = 0; i < 11; ++i) {
            int n;
            dataOutputStream.writeByte(wornSlots[i][5]);
            for (n = 0; n < 5; ++n) {
                dataOutputStream.writeByte(wornAbility[i][5][n]);
                dataOutputStream.writeByte(wornPercen[i][5][n]);
            }
            for (n = 0; n < 3; ++n) {
                dataOutputStream.writeByte(wornGems[i][5][n]);
            }
        }
    }

    static void readWornSlot5Entries(DataInputStream dataInputStream) throws IOException {
        for (int i = 0; i < 11; ++i) {
            int n;
            OfflineChest.wornSlots[i][5] = dataInputStream.readByte();
            for (n = 0; n < 5; ++n) {
                OfflineChest.wornAbility[i][5][n] = OfflineChest.clampWornStat(dataInputStream.readByte());
                OfflineChest.wornPercen[i][5][n] = OfflineChest.clampWornStat(dataInputStream.readByte());
            }
            for (n = 0; n < 3; ++n) {
                OfflineChest.wornGems[i][5][n] = dataInputStream.readByte();
            }
        }
    }

    static {
        wornSlots = new byte[11][6];
        wornAbility = new byte[11][6][5];
        wornPercen = new byte[11][6][5];
        wornGems = OfflineChest.createEmptyWornGems();
    }

    private static final class SavedEntry {
        byte glass;
        byte type;
        short id;
        int dbKey;
        byte date;
        String name;
        boolean material;
        int num;
        byte slot;
        byte[] ability = new byte[5];
        byte[] percen = new byte[5];
        byte[] gems = new byte[]{-1, -1, -1};
        byte craftTier;

        private SavedEntry() {
        }
    }
}

