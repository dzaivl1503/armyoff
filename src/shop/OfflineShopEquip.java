/*
 * Decompiled with CFR 0.152.
 */
package shop;

import Equipment.Equip;
import Equipment.EquipGlass;
import Equipment.PlayerEquip;
import Equipment.TypeEquip;
import com.teamobi.mobiarmy2.GameMidlet;
import com.teamobi.mobiarmy2.OfflineChest;
import com.teamobi.mobiarmy2.OfflineSave;
import coreLG.CCanvas;
import coreLG.TerrainMidlet;
import java.util.Vector;
import model.Language;
import model.PlayerInfo;
import screen.EquipScreen;
import screen.MenuScr;
import shop.ShopEquipCatalog;
import shop.ShopEquipment;

public final class OfflineShopEquip {
    private OfflineShopEquip() {
    }

    public static Vector buildShopItems() {
        Vector<Equip> vector = new Vector<Equip>();
        if (PlayerEquip.playerData == null || TerrainMidlet.myInfo == null) {
            return vector;
        }
        ShopEquipCatalog.ensureLoaded();
        byte by = TerrainMidlet.myInfo.gun;
        Vector vector2 = ShopEquipCatalog.getEntriesForGlass(by);
        for (int i = 0; i < vector2.size(); ++i) {
            Equip equip = OfflineShopEquip.createShopEquip((ShopEquipCatalog.Entry)vector2.elementAt(i));
            if (equip == null) continue;
            equip.index = vector.size();
            vector.addElement(equip);
        }
        return vector;
    }

    private static Equip createShopEquip(ShopEquipCatalog.Entry entry) {
        if (entry == null) {
            return null;
        }
        byte by = OfflineShopEquip.findEquipType(entry.glass, entry.equipId);
        Equip equip = PlayerEquip.createEquip(entry.glass, by, entry.equipId);
        if (equip == null) {
            return null;
        }
        equip.glass = entry.glass;
        equip.type = by;
        equip.name = entry.name;
        equip.xu = entry.xu;
        equip.luong = entry.luong;
        equip.date = (byte)-1;
        equip.isSelect = false;
        equip.getStrShopDetail();
        return equip;
    }

    private static byte findEquipType(byte by, short s) {
        EquipGlass equipGlass = PlayerEquip.getEquipGlass(by);
        if (equipGlass == null || equipGlass.type == null) {
            return 0;
        }
        for (int i = 0; i < equipGlass.type.size(); ++i) {
            TypeEquip typeEquip = (TypeEquip)equipGlass.type.elementAt(i);
            if (typeEquip == null || typeEquip.getEquip(s) == null) continue;
            return typeEquip.typeID;
        }
        return 0;
    }

    public static void openShop() {
        GameMidlet.ensureOfflineAssetsLoaded();
        if (PlayerEquip.playerData == null) {
            CCanvas.startOKDlg("Ch\u01b0a c\u00f3 d\u1eef li\u1ec7u trang b\u1ecb (equipdata2).");
            return;
        }
        ShopEquipCatalog.ensureLoaded();
        Vector vector = OfflineShopEquip.buildShopItems();
        if (vector.size() == 0) {
            CCanvas.startOKDlg("Kh\u00f4ng c\u00f3 item trong c\u1eeda h\u00e0ng (thi\u1ebfu res/shop_equipment.txt?).");
            return;
        }
        if (CCanvas.shopEquipScr == null) {
            CCanvas.shopEquipScr = new ShopEquipment();
        }
        CCanvas.shopEquipScr.setItems(vector);
        if (CCanvas.menuScr == null) {
            CCanvas.menuScr = new MenuScr();
        }
        CCanvas.menuScr.doEquipItem();
        CCanvas.endDlg();
    }

    public static void buyEquip(Equip equip, byte by) {
        if (equip == null || TerrainMidlet.myInfo == null) {
            return;
        }
        PlayerInfo playerInfo = TerrainMidlet.myInfo;
        if (equip.level > playerInfo.level2) {
            CCanvas.startOKDlg(Language.banphaitren() + equip.level + Language.moicothe());
            return;
        }
        int n = equip.xu;
        int n2 = equip.luong;
        if (by == 0) {
            if (n < 0 || playerInfo.xu < n) {
                CCanvas.startOKDlg(Language.noMoney());
                return;
            }
            playerInfo.xu -= n;
        } else {
            if (n2 < 0 || playerInfo.luong < n2) {
                CCanvas.startOKDlg(Language.noMoney());
                return;
            }
            playerInfo.luong -= n2;
        }
        equip.dbKey = OfflineChest.nextDbKey();
        equip.date = (byte)-1;
        equip.slot = (byte)equip.socketCount();
        OfflineChest.add(equip);
        OfflineSave.save();
        CCanvas.endDlg();
        CCanvas.startOKDlg("\u0110\u00e3 mua. Trang b\u1ecb \u0111ang n\u1eb1m trong r\u01b0\u01a1ng \u0111\u1ed3.");
    }

    public static void sellEquip(int[] nArray) {
        if (nArray == null || TerrainMidlet.myInfo == null) {
            return;
        }
        PlayerInfo playerInfo = TerrainMidlet.myInfo;
        int n = 0;
        int n2 = 0;
        boolean bl = false;
        boolean bl2 = false;
        for (int i = 0; i < nArray.length; ++i) {
            int n3 = -nArray[i] - 1;
            Equip equip = OfflineShopEquip.findInventoryEquip(n3);
            if (equip == null || equip.isMaterial) continue;
            if (OfflineShopEquip.isCurrentlyWorn(playerInfo, equip)) {
                bl = true;
                continue;
            }
            if (equip.socketCount() > 0) {
                bl2 = true;
                continue;
            }
            int n4 = equip.xu > 0 ? equip.xu / 2 : (equip.luong > 0 ? equip.luong * 500 : 0);
            n += n4;
            EquipScreen.inventory.removeElement(equip);
            ++n2;
        }
        if (n2 > 0) {
            playerInfo.xu += n;
            OfflineSave.save();
            if (CCanvas.equipScreen != null) {
                CCanvas.equipScreen.getMyEquip();
            }
        }
        String string = n2 > 0 ? "\u0110\u00e3 b\u00e1n " + n2 + " trang b\u1ecb, nh\u1eadn " + n + " xu." : (bl ? "Kh\u00f4ng th\u1ec3 b\u00e1n trang b\u1ecb \u0111ang s\u1eed d\u1ee5ng." : (bl2 ? "Trang b\u1ecb c\u00f2n g\u1eafn ng\u1ecdc - h\u00e3y th\u00e1o ng\u1ecdc tr\u01b0\u1edbc khi b\u00e1n." : "Kh\u00f4ng c\u00f3 trang b\u1ecb n\u00e0o \u0111\u01b0\u1ee3c b\u00e1n."));
        CCanvas.startOKDlg(string);
    }

    private static Equip findInventoryEquip(int n) {
        for (int i = 0; i < EquipScreen.inventory.size(); ++i) {
            Equip equip = (Equip)EquipScreen.inventory.elementAt(i);
            if (equip.isMaterial || equip.dbKey != n) continue;
            return equip;
        }
        return null;
    }

    private static boolean isCurrentlyWorn(PlayerInfo playerInfo, Equip equip) {
        if (equip.type < 0 || equip.type >= 6) {
            return false;
        }
        return playerInfo.equipID[playerInfo.gun][equip.type] == equip.id;
    }
}

