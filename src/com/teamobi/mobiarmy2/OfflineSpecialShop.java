/*
 * Decompiled with CFR 0.152.
 */
package com.teamobi.mobiarmy2;

import CLib.mImage;
import Equipment.Equip;
import Equipment.EquipGlass;
import Equipment.PlayerEquip;
import Equipment.TypeEquip;
import com.teamobi.mobiarmy2.OfflineChest;
import com.teamobi.mobiarmy2.OfflineCombat;
import com.teamobi.mobiarmy2.OfflineMission;
import com.teamobi.mobiarmy2.OfflineSave;
import com.teamobi.mobiarmy2.OfflineTeamItems;
import coreLG.CCanvas;
import coreLG.TerrainMidlet;
import java.util.Vector;
import model.CRes;
import model.Fomula;
import model.IAction;
import model.Language;
import model.MaterialIconMn;
import model.PlayerInfo;
import screen.EquipScreen;
import shop.ShopEquipCatalog;

public final class OfflineSpecialShop {
    private static int[] pendingIds;
    private static byte[] pendingCounts;
    private static byte pendingNum;
    private static final int[] EXP_POTION_IDS;
    private static final int[] EXP_POTION_AMOUNTS;
    private static final int[] EXP_POTION_PRICES_LUONG;
    public static final int EXP_POTION_ID = 90;
    private static final int X2_EXP_CARD_ID = 54;
    private static final int X2_EXP_CARD_HOURS = 6;
    private static final int RESET_UPGRADE_CARD_ID = 50;
    private static final String[] GEM_NAMES;
    private static final String[] MATERIAL_NAMES;
    private static final int[] SILVER_METAL_BASE;
    private static final int GEM_GROUP_FOR_CRAFT = 4;

    private static int expPotionIndex(int n) {
        for (int i = 0; i < EXP_POTION_IDS.length; ++i) {
            if (EXP_POTION_IDS[i] != n) continue;
            return i;
        }
        return -1;
    }

    public static boolean isExpPotion(int n) {
        return OfflineSpecialShop.expPotionIndex(n) >= 0;
    }

    private OfflineSpecialShop() {
    }

    public static void shop(byte by, byte by2, byte by3, byte by4) {
        if (by == 0) {
            CCanvas.endDlg();
            CCanvas.shopLinhtinh.setItems(OfflineSpecialShop.buildCatalogItems());
            CCanvas.shopLinhtinh.show(CCanvas.menuScr);
            return;
        }
        OfflineSpecialShop.buy(by2, by3, by4);
    }

    static Vector buildCatalogItems() {
        int n;
        Vector<Equip> vector = new Vector<Equip>();
        for (int i = 0; i < 5; ++i) {
            for (n = 0; n < 7; ++n) {
                vector.addElement(OfflineSpecialShop.makeShopItem((byte)(i * 10 + n)));
            }
        }
        vector.addElement(OfflineSpecialShop.makeShopItem((byte)50));
        vector.addElement(OfflineSpecialShop.makeShopItem((byte)54));
        for (n = 0; n < EXP_POTION_IDS.length; ++n) {
            vector.addElement(OfflineSpecialShop.makeShopItem((byte)EXP_POTION_IDS[n]));
        }
        for (int i = 57; i <= 73; ++i) {
            if (i > 61 && i < 62) continue;
            vector.addElement(OfflineSpecialShop.makeShopItem((byte)i));
        }
        return vector;
    }

    private static Equip makeShopItem(byte by) {
        Equip equip = new Equip();
        equip.id = by;
        equip.icon = by;
        equip.isMaterial = true;
        equip.isBuyNum = true;
        equip.num = 99;
        equip.date = 0;
        equip.xu = OfflineSpecialShop.priceXu(by);
        equip.luong = OfflineSpecialShop.priceLuong(by);
        equip.name = OfflineSpecialShop.itemName(by);
        equip.strDetail = OfflineSpecialShop.itemDetail(by);
        equip.materialIcon = OfflineSpecialShop.itemIcon(by);
        return equip;
    }

    public static mImage itemIcon(int n) {
        if (MaterialIconMn.isExistIcon(n)) {
            return MaterialIconMn.getImageFromID(n);
        }
        return mImage.createImage("/icon/item/" + n + ".png");
    }

    private static int priceXu(int n) {
        if (OfflineSpecialShop.isExpPotion(n)) {
            return -1;
        }
        if (n < 50) {
            return 1200 * (n % 10 + 1);
        }
        if (n >= 57 && n <= 61) {
            return 30000;
        }
        if (n >= 69 && n <= 73) {
            return 80000;
        }
        if (n >= 62 && n <= 68) {
            return 2500 + (n - 62) * 1000;
        }
        return 15000;
    }

    private static int priceLuong(int n) {
        int n2 = OfflineSpecialShop.expPotionIndex(n);
        if (n2 >= 0) {
            return EXP_POTION_PRICES_LUONG[n2];
        }
        if (n < 50) {
            return 2 * (n % 10 + 1);
        }
        if (n >= 57 && n <= 61) {
            return 20;
        }
        if (n >= 69 && n <= 73) {
            return 50;
        }
        return n >= 62 && n <= 68 ? 5 : 10;
    }

    public static String itemName(int n) {
        if (n >= 0 && n < 50) {
            return GEM_NAMES[n / 10] + " c\u1ea5p " + (n % 10 + 1);
        }
        if (n == 50) {
            return "Th\u1ebb \u0111\u1eb7t l\u1ea1i n\u00e2ng c\u1ea5p";
        }
        if (n == 54) {
            return "Th\u1ebb nh\u00e2n \u0111\u00f4i kinh nghi\u1ec7m";
        }
        int n2 = OfflineSpecialShop.expPotionIndex(n);
        if (n2 >= 0) {
            return "B\u00ecnh kinh nghi\u1ec7m " + EXP_POTION_AMOUNTS[n2];
        }
        if (n >= 57 && n <= 61) {
            return "C\u00f4ng th\u1ee9c b\u1ea1c - " + OfflineSpecialShop.slotName(n - 57);
        }
        if (n >= 62 && n <= 68) {
            return MATERIAL_NAMES[n - 62];
        }
        if (n >= 69 && n <= 73) {
            return "C\u00f4ng th\u1ee9c v\u00e0ng - " + OfflineSpecialShop.slotName(n - 69);
        }
        return "V\u1eadt ph\u1ea9m \u0111\u1eb7c bi\u1ec7t " + n;
    }

    public static String itemDetail(int n) {
        if (n >= 0 && n < 50) {
            return "Gh\u00e9p v\u00e0o trang b\u1ecb \u0111\u1ec3 t\u0103ng " + OfflineSpecialShop.abilityName(n / 10) + " +" + OfflineSpecialShop.gemValue(n) + ". D\u00f9ng 5 vi\u00ean \u0111\u1ec3 gh\u00e9p l\u00ean c\u1ea5p cao h\u01a1n.";
        }
        if (n >= 57 && n <= 61 || n >= 69 && n <= 73) {
            return Language.fomula() + " ch\u1ebf t\u1ea1o trang b\u1ecb " + OfflineSpecialShop.slotName(OfflineSpecialShop.formulaType(n)) + ".";
        }
        if (n >= 62 && n <= 68) {
            return "Nguy\u00ean li\u1ec7u d\u00f9ng trong c\u00f4ng th\u1ee9c ch\u1ebf t\u1ea1o trang b\u1ecb.";
        }
        if (n == 54) {
            return "S\u1eed d\u1ee5ng \u0111\u1ec3 k\u00edch ho\u1ea1t 6 gi\u1edd nh\u00e2n \u0111\u00f4i kinh nghi\u1ec7m (c\u1ed9ng d\u1ed3n n\u1ebfu \u0111ang c\u00f3 hi\u1ec7u l\u1ef1c).";
        }
        if (n == 50) {
            return "\u0110\u1eb7t l\u1ea1i 5 ch\u1ec9 s\u1ed1 n\u00e2ng c\u1ea5p (\u0111\u1ed3ng \u0111\u1ed9i/sinh l\u1ef1c/ph\u00f2ng th\u1ee7/may m\u1eafn/s\u1ee9c m\u1ea1nh) c\u1ee7a nh\u00e2n v\u1eadt \u0111ang d\u00f9ng v\u1ec1 m\u1eb7c \u0111\u1ecbnh v\u00e0 ho\u00e0n tr\u1ea3 \u0111i\u1ec3m \u0111\u1ec3 ph\u00e2n b\u1ed5 l\u1ea1i. M\u1ed7i l\u1ea7n d\u00f9ng ti\u00eau t\u1ed1n 1 th\u1ebb.";
        }
        int n2 = OfflineSpecialShop.expPotionIndex(n);
        if (n2 >= 0) {
            return "S\u1eed d\u1ee5ng \u0111\u1ec3 nh\u1eadn ngay " + EXP_POTION_AMOUNTS[n2] + " kinh nghi\u1ec7m.";
        }
        return "V\u1eadt ph\u1ea9m \u0111\u1eb7c bi\u1ec7t.";
    }

    private static String abilityName(int n) {
        switch (n) {
            case 0: {
                return "\u0111\u1ed3ng \u0111\u1ed9i";
            }
            case 1: {
                return "sinh l\u1ef1c";
            }
            case 2: {
                return "ph\u00f2ng th\u1ee7";
            }
            case 3: {
                return "may m\u1eafn";
            }
        }
        return "s\u1ee9c m\u1ea1nh";
    }

    private static String slotName(int n) {
        switch (n) {
            case 0: {
                return "v\u0169 kh\u00ed";
            }
            case 1: {
                return "m\u0169";
            }
            case 2: {
                return "\u00e1o gi\u00e1p";
            }
            case 3: {
                return "k\u00ednh";
            }
        }
        return "c\u00e1nh";
    }

    private static int formulaType(int n) {
        return n >= 69 ? n - 69 : n - 57;
    }

    private static int gemValue(int n) {
        return n % 10 * 2 + 1;
    }

    private static void buy(byte by, byte by2, byte by3) {
        int n;
        int n2 = by3 <= 0 ? 1 : by3 & 0xFF;
        int n3 = n = by == 1 ? OfflineSpecialShop.priceLuong(by2) : OfflineSpecialShop.priceXu(by2);
        if (n < 0) {
            return;
        }
        int n4 = n * n2;
        PlayerInfo playerInfo = TerrainMidlet.myInfo;
        if (playerInfo == null || by == 1 && playerInfo.luong < n4 || by != 1 && playerInfo.xu < n4) {
            CCanvas.startOKDlg(Language.kocotien());
            return;
        }
        if (by == 1) {
            playerInfo.luong -= n4;
        } else {
            playerInfo.xu -= n4;
        }
        OfflineSpecialShop.addMaterial(by2, n2);
        OfflineSave.save();
        CCanvas.startOKDlg("\u0110\u00e3 mua " + n2 + " " + OfflineSpecialShop.itemName(by2) + ".");
    }

    public static void imbue(byte by, byte by2, int[] nArray, byte[] byArray) {
        if (by == 0) {
            pendingNum = by2;
            pendingIds = nArray;
            pendingCounts = byArray;
            if (by2 == 1 && nArray != null && OfflineSpecialShop.isExpPotion(nArray[0])) {
                int n;
                int n2 = nArray[0];
                int n3 = OfflineSpecialShop.expPotionIndex(n2);
                Equip equip = OfflineSpecialShop.findMaterial(n2);
                if (equip == null || equip.num <= 0) {
                    CCanvas.startOKDlg("B\u1ea1n kh\u00f4ng c\u00f3 " + OfflineSpecialShop.itemName(n2) + ".");
                    CCanvas.inventory.unSelectEquip();
                    return;
                }
                int n4 = n = byArray == null ? 1 : byArray[0] & 0xFF;
                if (n <= 0) {
                    n = 1;
                }
                if (n > equip.num) {
                    n = equip.num;
                }
                pendingCounts = new byte[]{(byte)n};
                CCanvas.inventory.combineYesNo("D\u00f9ng " + n + " " + OfflineSpecialShop.itemName(n2) + " \u0111\u1ec3 nh\u1eadn " + n * EXP_POTION_AMOUNTS[n3] + " kinh nghi\u1ec7m?");
                return;
            }
            if (by2 == 1 && nArray != null && nArray[0] == 50) {
                Equip equip = OfflineSpecialShop.findMaterial(50);
                if (equip == null || equip.num <= 0) {
                    CCanvas.startOKDlg("B\u1ea1n kh\u00f4ng c\u00f3 Th\u1ebb \u0111\u1eb7t l\u1ea1i n\u00e2ng c\u1ea5p.");
                    CCanvas.inventory.unSelectEquip();
                    return;
                }
                pendingCounts = new byte[]{1};
                CCanvas.inventory.combineYesNo("D\u00f9ng 1 Th\u1ebb \u0111\u1eb7t l\u1ea1i n\u00e2ng c\u1ea5p \u0111\u1ec3 \u0111\u1eb7t l\u1ea1i \u0111i\u1ec3m n\u00e2ng c\u1ea5p c\u1ee7a nh\u00e2n v\u1eadt \u0111ang d\u00f9ng v\u00e0 ho\u00e0n tr\u1ea3 \u0111i\u1ec3m?");
                return;
            }
            if (by2 == 1 && nArray != null && nArray[0] == 54) {
                int n;
                Equip equip = OfflineSpecialShop.findMaterial(54);
                if (equip == null || equip.num <= 0) {
                    CCanvas.startOKDlg("B\u1ea1n kh\u00f4ng c\u00f3 Th\u1ebb nh\u00e2n \u0111\u00f4i kinh nghi\u1ec7m.");
                    CCanvas.inventory.unSelectEquip();
                    return;
                }
                int n5 = n = byArray == null ? 1 : byArray[0] & 0xFF;
                if (n <= 0) {
                    n = 1;
                }
                if (n > equip.num) {
                    n = equip.num;
                }
                pendingCounts = new byte[]{(byte)n};
                CCanvas.inventory.combineYesNo("D\u00f9ng " + n + " Th\u1ebb nh\u00e2n \u0111\u00f4i kinh nghi\u1ec7m \u0111\u1ec3 k\u00edch ho\u1ea1t " + n * 6 + " gi\u1edd nh\u00e2n \u0111\u00f4i kinh nghi\u1ec7m?");
                return;
            }
            if (by2 == 1 && nArray != null && nArray[0] >= 0 && nArray[0] < 50) {
                int n = (byArray[0] & 0xFF) / 5;
                if (n <= 0) {
                    CCanvas.startOKDlg("C\u1ea7n ch\u1ecdn \u00edt nh\u1ea5t 5 vi\u00ean ng\u1ecdc c\u00f9ng lo\u1ea1i.");
                    CCanvas.inventory.unSelectEquip();
                    return;
                }
                int n6 = 100 - (nArray[0] + 1) % 10 * 10;
                CCanvas.inventory.combineYesNo("Gh\u00e9p " + n + " l\u1ea7n, t\u1ec9 l\u1ec7 th\u00e0nh c\u00f4ng " + n6 + "%?");
                return;
            }
            if (by2 == 2 && nArray != null) {
                int n;
                int n7;
                n7 = nArray[0] < 0 ? 0 : (nArray[1] < 0 ? 1 : -1);
                n = nArray[0] >= 0 && nArray[0] < 50 ? 0 : (nArray[1] >= 0 && nArray[1] < 50 ? 1 : -1);
                if (n7 >= 0 && n >= 0) {
                    CCanvas.inventory.combineYesNo("Gh\u00e9p " + OfflineSpecialShop.itemName(nArray[n]) + " v\u00e0o trang b\u1ecb \u0111\u00e3 ch\u1ecdn?");
                    return;
                }
            }
            CCanvas.startOKDlg("H\u00e3y ch\u1ecdn 5 ng\u1ecdc c\u00f9ng lo\u1ea1i \u0111\u1ec3 gh\u00e9p, ho\u1eb7c 1 trang b\u1ecb v\u00e0 1 ng\u1ecdc \u0111\u1ec3 \u00e9p.");
            CCanvas.inventory.unSelectEquip();
            return;
        }
        OfflineSpecialShop.confirmImbue();
    }

    private static void confirmImbue() {
        if (pendingIds == null || pendingNum <= 0) {
            return;
        }
        if (pendingNum == 1 && OfflineSpecialShop.isExpPotion(pendingIds[0])) {
            OfflineSpecialShop.useExpPotion(pendingIds[0]);
        } else if (pendingNum == 1 && pendingIds[0] == 54) {
            OfflineSpecialShop.useX2ExpCard();
        } else if (pendingNum == 1 && pendingIds[0] == 50) {
            OfflineSpecialShop.useResetUpgradeCard();
        } else if (pendingNum == 1 && pendingIds[0] >= 0 && pendingIds[0] < 50) {
            OfflineSpecialShop.combineGem(pendingIds[0], pendingCounts[0] & 0xFF);
        } else if (pendingNum == 2) {
            int n = pendingIds[0] < 0 ? 0 : 1;
            int n2 = n == 0 ? 1 : 0;
            OfflineSpecialShop.socketGem(-pendingIds[n] - 1, pendingIds[n2]);
        }
        pendingIds = null;
        pendingCounts = null;
        pendingNum = 0;
        OfflineSave.save();
        if (CCanvas.equipScreen != null) {
            CCanvas.equipScreen.getMyEquip();
        }
    }

    private static void useExpPotion(int n) {
        int n2;
        int n3 = OfflineSpecialShop.expPotionIndex(n);
        if (n3 < 0) {
            return;
        }
        Equip equip = OfflineSpecialShop.findMaterial(n);
        if (equip == null || equip.num <= 0) {
            CCanvas.startOKDlg("B\u1ea1n kh\u00f4ng c\u00f3 " + OfflineSpecialShop.itemName(n) + ".");
            return;
        }
        int n4 = n2 = pendingCounts == null ? 1 : pendingCounts[0] & 0xFF;
        if (n2 <= 0) {
            n2 = 1;
        }
        if (n2 > equip.num) {
            n2 = equip.num;
        }
        equip.num -= n2;
        OfflineSpecialShop.cleanupMaterial(equip);
        int n5 = EXP_POTION_AMOUNTS[n3] * n2;
        int n6 = TerrainMidlet.myInfo.level2;
        OfflineCombat.grantExpDirect(n5);
        if (TerrainMidlet.myInfo.level2 == n6) {
            CCanvas.startOKDlg("\u0110\u00e3 nh\u1eadn " + n5 + " kinh nghi\u1ec7m.");
        }
    }

    private static void useX2ExpCard() {
        int n;
        Equip equip = OfflineSpecialShop.findMaterial(54);
        if (equip == null || equip.num <= 0) {
            CCanvas.startOKDlg("B\u1ea1n kh\u00f4ng c\u00f3 Th\u1ebb nh\u00e2n \u0111\u00f4i kinh nghi\u1ec7m.");
            return;
        }
        int n2 = n = pendingCounts == null ? 1 : pendingCounts[0] & 0xFF;
        if (n <= 0) {
            n = 1;
        }
        if (n > equip.num) {
            n = equip.num;
        }
        equip.num -= n;
        OfflineSpecialShop.cleanupMaterial(equip);
        int n3 = OfflineTeamItems.activateExpCard(n * 6);
        CCanvas.startOKDlg("\u0110\u00e3 k\u00edch ho\u1ea1t nh\u00e2n \u0111\u00f4i kinh nghi\u1ec7m, c\u00f2n l\u1ea1i " + n3 + " gi\u1edd.");
    }

    private static int[] defaultAbility(int n) {
        int[] nArray;
        if (n == 10) {
            int[] nArray2 = new int[5];
            nArray2[0] = 10;
            nArray2[1] = 0;
            nArray2[2] = 10;
            nArray2[3] = 10;
            nArray = nArray2;
            nArray2[4] = 10;
        } else {
            int[] nArray3 = new int[5];
            nArray3[0] = 0;
            nArray3[1] = 0;
            nArray3[2] = 0;
            nArray3[3] = 0;
            nArray = nArray3;
            nArray3[4] = 0;
        }
        return nArray;
    }

    private static void useResetUpgradeCard() {
        Equip equip = OfflineSpecialShop.findMaterial(50);
        if (equip == null || equip.num <= 0) {
            CCanvas.startOKDlg("B\u1ea1n kh\u00f4ng c\u00f3 Th\u1ebb \u0111\u1eb7t l\u1ea1i n\u00e2ng c\u1ea5p.");
            return;
        }
        --equip.num;
        OfflineSpecialShop.cleanupMaterial(equip);
        PlayerInfo playerInfo = TerrainMidlet.myInfo;
        int[] nArray = OfflineSpecialShop.defaultAbility(playerInfo.gun);
        int n = 0;
        for (int i = 0; i < 5; ++i) {
            int n2 = playerInfo.ability[i] - nArray[i];
            if (n2 > 0) {
                n += n2;
            }
            playerInfo.ability[i] = (short)nArray[i];
        }
        playerInfo.point = (short)(playerInfo.point + n);
        playerInfo.saveCurrentClassProgress();
        CCanvas.startOKDlg("\u0110\u00e3 \u0111\u1eb7t l\u1ea1i n\u00e2ng c\u1ea5p, ho\u00e0n tr\u1ea3 " + n + " \u0111i\u1ec3m.");
    }

    private static void combineGem(int n, int n2) {
        if (n % 10 == 9) {
            CCanvas.startOKDlg("Ng\u1ecdc \u0111\u00e3 \u0111\u1ea1t c\u1ea5p t\u1ed1i \u0111a.");
            return;
        }
        Equip equip = OfflineSpecialShop.findMaterial(n);
        int n3 = n2 / 5;
        if (equip == null || equip.num < n3 * 5 || n3 <= 0) {
            CCanvas.startOKDlg("Kh\u00f4ng \u0111\u1ee7 ng\u1ecdc \u0111\u1ec3 gh\u00e9p.");
            return;
        }
        int n4 = 0;
        int n5 = 100 - (n + 1) % 10 * 10;
        for (int i = 0; i < n3; ++i) {
            if (CRes.random(0, 100) < n5) {
                equip.num -= 5;
                ++n4;
                continue;
            }
            --equip.num;
        }
        OfflineSpecialShop.cleanupMaterial(equip);
        if (n4 > 0) {
            OfflineSpecialShop.addMaterial((byte)(n + 1), n4);
            OfflineMission.onGemCrafted(n + 1, n4);
        }
        CCanvas.startOKDlg("Gh\u00e9p xong: " + n4 + "/" + n3 + " l\u1ea7n th\u00e0nh c\u00f4ng.");
    }

    private static void socketGem(int n, int n2) {
        int n3;
        Equip equip = OfflineSpecialShop.findEquipment(n);
        Equip equip2 = OfflineSpecialShop.findMaterial(n2);
        if (equip == null || equip2 == null || equip2.num <= 0) {
            CCanvas.startOKDlg("Kh\u00f4ng t\u00ecm th\u1ea5y trang b\u1ecb ho\u1eb7c ng\u1ecdc.");
            return;
        }
        for (n3 = 0; n3 < 3 && equip.socketGems[n3] >= 0; ++n3) {
        }
        if (n3 >= 3) {
            CCanvas.startOKDlg("Trang b\u1ecb \u0111\u00e3 \u00e9p \u0111\u1ee7 3 vi\u00ean ng\u1ecdc.");
            return;
        }
        equip.socketGems[n3] = (byte)n2;
        equip.slot = (byte)equip.socketCount();
        int n4 = n2 / 10;
        int n5 = n4 == 0 ? 4 : (n4 == 1 ? 0 : (n4 == 2 ? 2 : (n4 == 3 ? 3 : 1)));
        int n6 = equip.inv_ability[n5] + OfflineSpecialShop.gemValue(n2);
        equip.inv_ability[n5] = (byte)(n6 > 127 ? 127 : n6);
        equip.setInvAtribute();
        --equip2.num;
        OfflineSpecialShop.cleanupMaterial(equip2);
        CCanvas.startOKDlg("Gh\u00e9p ng\u1ecdc th\u00e0nh c\u00f4ng. " + OfflineSpecialShop.abilityName(n4) + " +" + OfflineSpecialShop.gemValue(n2) + ".");
    }

    public static void requestRemoveGems(final Equip equip) {
        int n;
        if (equip == null || equip.socketCount() == 0) {
            return;
        }
        int n2 = 0;
        for (int i = 0; i < equip.socketGems.length; ++i) {
            n = equip.socketGems[i];
            if (n < 0) continue;
            n2 += OfflineSpecialShop.priceXu(n);
        }
        final int costXu = n2 / 4;
        CCanvas.startYesNoDlg("Th\u00e1o t\u1ea5t c\u1ea3 ng\u1ecdc v\u1edbi gi\u00e1 " + costXu + " xu?", new IAction(){

            public void perform() {
                PlayerInfo playerInfo = TerrainMidlet.myInfo;
                if (playerInfo == null || playerInfo.xu < costXu) {
                    CCanvas.startOKDlg(Language.kocotien());
                    return;
                }
                playerInfo.xu -= costXu;
                for (int i = 0; i < equip.socketGems.length; ++i) {
                    byte by = equip.socketGems[i];
                    if (by < 0) continue;
                    int n4 = by / 10;
                    int n2 = n4 == 0 ? 4 : (n4 == 1 ? 0 : (n4 == 2 ? 2 : (n4 == 3 ? 3 : 1)));
                    int n3 = equip.inv_ability[n2] - OfflineSpecialShop.gemValue(by);
                    equip.inv_ability[n2] = (byte)(n3 < -128 ? -128 : n3);
                    OfflineSpecialShop.addMaterial(by, 1);
                    equip.socketGems[i] = -1;
                }
                equip.slot = 0;
                equip.setInvAtribute();
                OfflineSave.save();
                CCanvas.inventory.unSelectEquip();
                CCanvas.endDlg();
                CCanvas.startOKDlg("\u0110\u00e3 th\u00e1o ng\u1ecdc v\u00e0 \u0111\u01b0a ng\u1ecdc v\u1ec1 r\u01b0\u01a1ng.");
            }
        });
    }

    public static void formula(byte by, byte by2, byte by3) {
        if (by2 == 1) {
            OfflineSpecialShop.showFormula(by);
        } else if (by2 == 2) {
            OfflineSpecialShop.craft(by, by3);
        }
    }

    private static int craftLevelRequire(boolean bl, int n) {
        return bl ? 40 + 10 * n : 5 + 5 * n;
    }

    private static int craftMetalAmount(boolean bl, int n, int n2) {
        if (bl) {
            int n3 = n - 1;
            return 100 + 75 * n3 + 5 * n3 * (n3 - 1) / 2;
        }
        return SILVER_METAL_BASE[n2] << n - 1;
    }

    private static int[] craftMaterialIds(boolean bl) {
        int[] nArray;
        if (bl) {
            int[] nArray2 = new int[4];
            nArray2[0] = 62;
            nArray2[1] = 63;
            nArray2[2] = 65;
            nArray = nArray2;
            nArray2[3] = 66;
        } else {
            int[] nArray3 = new int[3];
            nArray3[0] = 62;
            nArray3[1] = 63;
            nArray = nArray3;
            nArray3[2] = 65;
        }
        return nArray;
    }

    private static int craftGemId(boolean bl, int n) {
        int n2 = bl ? 5 + n : 3 + n;
        return 40 + (n2 - 1);
    }

    private static int craftGemCount() {
        return 2;
    }

    private static int craftFlatBonusMin(int n) {
        return 10 + 5 * n;
    }

    private static int craftFlatBonusMax(int n) {
        return OfflineSpecialShop.craftFlatBonusMin(n) + 5;
    }

    private static int craftPercentBonusMin(int n) {
        return 6 + 2 * n;
    }

    private static int craftPercentBonusMax(int n) {
        return OfflineSpecialShop.craftPercentBonusMin(n) + 2;
    }

    private static int craftPowerFlatMin(int n) {
        return 5 + 5 * n;
    }

    private static int craftPowerFlatMax(int n) {
        return OfflineSpecialShop.craftPowerFlatMin(n) + 5;
    }

    private static int craftPercentRangeMin(int n) {
        return 2 * n;
    }

    private static int craftPercentRangeMax(int n) {
        return OfflineSpecialShop.craftPercentRangeMin(n) + 2;
    }

    private static int craftAllFlatFixed(int n) {
        return 3 + 2 * n;
    }

    private static int craftExtraStatMin(boolean bl, int n) {
        return bl ? OfflineSpecialShop.craftFlatBonusMin(n) : OfflineSpecialShop.craftPowerFlatMin(n);
    }

    private static int craftExtraStatMax(boolean bl, int n) {
        return (bl ? OfflineSpecialShop.craftFlatBonusMax(n) : OfflineSpecialShop.craftPowerFlatMax(n)) * 13 / 10;
    }

    private static void showFormula(byte by) {
        boolean bl;
        int n = OfflineSpecialShop.formulaType(by);
        Equip equip = OfflineSpecialShop.findTierTarget((byte)n, bl = by >= 69);
        if (equip == null) {
            CCanvas.startOKDlg("Ch\u01b0a c\u00f3 trang b\u1ecb ph\u00f9 h\u1ee3p v\u1edbi c\u00f4ng th\u1ee9c n\u00e0y.");
            return;
        }
        String string = equip.name;
        int n2 = 0;
        for (int i = 1; i <= 5; ++i) {
            if (OfflineSpecialShop.findOwnedCraftLevel((byte)n, equip.id, i) == null) continue;
            n2 = i;
        }
        CCanvas.fomulaScreen.fomulas.removeAllElements();
        for (int i = 1; i <= 5; ++i) {
            CCanvas.fomulaScreen.setFomula(OfflineSpecialShop.buildFomulaForLevel(by, (byte)n, bl, i, equip, string));
        }
        CCanvas.fomulaScreen.select = Math.min(n2, 4);
        CCanvas.endDlg();
        CCanvas.fomulaScreen.show(CCanvas.curScr);
    }

    private static Fomula buildFomulaForLevel(byte by, byte by2, boolean bl, int n, Equip equip, String string) {
        int n2;
        int n3;
        boolean bl2;
        Object object;
        Fomula fomula = new Fomula();
        fomula.ID = by;
        Equip equip2 = new Equip();
        equip2.changeToEquip(equip);
        equip2.name = string + " c\u1ea5p " + n;
        fomula.e = equip2;
        if (n == 1) {
            Equip eqBase = bl ? OfflineSpecialShop.findBaseTierIV(by2) : OfflineSpecialShop.findBaseTierII(by2);
            fomula.equipRequire = eqBase != null ? eqBase : equip;
            bl2 = eqBase != null && OfflineSpecialShop.ownsTier(by2, eqBase);
        } else {
            Equip eqPrev = new Equip();
            eqPrev.changeToEquip(equip);
            eqPrev.name = string + " c\u1ea5p " + (n - 1);
            fomula.equipRequire = eqPrev;
            bl2 = OfflineSpecialShop.findOwnedCraftLevel(by2, equip.id, n - 1) != null;
        }
        fomula.isHave = bl2;
        fomula.levelRequire = OfflineSpecialShop.craftLevelRequire(bl, n);
        int[] matIds = OfflineSpecialShop.craftMaterialIds(bl);
        int n4 = OfflineSpecialShop.craftGemId(bl, n);
        int n5 = OfflineSpecialShop.craftGemCount();
        int n6 = matIds.length + 1;
        fomula.imgMaterial = new mImage[n6];
        fomula.idImage = new int[n6];
        fomula.materialName = new String[n6];
        fomula.numMaterial = new String[n6];
        boolean bl3 = OfflineSpecialShop.countMaterial(by) > 0 || TerrainMidlet.myInfo.xu >= OfflineSpecialShop.priceXu(by);
        boolean bl4 = bl2 && TerrainMidlet.myInfo.level2 >= fomula.levelRequire && bl3;
        for (n3 = 0; n3 < matIds.length; ++n3) {
            n2 = OfflineSpecialShop.craftMetalAmount(bl, n, n3);
            int n7 = OfflineSpecialShop.countMaterial(matIds[n3]);
            fomula.idImage[n3] = matIds[n3];
            fomula.materialName[n3] = OfflineSpecialShop.itemName(matIds[n3]);
            fomula.numMaterial[n3] = n7 + "/" + n2;
            fomula.imgMaterial[n3] = OfflineSpecialShop.itemIcon(matIds[n3]);
            if (n7 >= n2) continue;
            bl4 = false;
        }
        n2 = OfflineSpecialShop.countMaterial(n4);
        fomula.idImage[n3] = n4;
        fomula.materialName[n3] = OfflineSpecialShop.itemName(n4);
        fomula.numMaterial[n3] = n2 + "/" + n5;
        fomula.imgMaterial[n3] = OfflineSpecialShop.itemIcon(n4);
        if (n2 < n5) {
            bl4 = false;
        }
        Vector<String> vector = new Vector<String>();
        if (bl) {
            vector.addElement("T\u1ea5t c\u1ea3 +(" + OfflineSpecialShop.craftFlatBonusMin(n) + "-" + OfflineSpecialShop.craftFlatBonusMax(n) + ") +(" + OfflineSpecialShop.craftPercentBonusMin(n) + "-" + OfflineSpecialShop.craftPercentBonusMax(n) + ")%");
        } else {
            vector.addElement("S\u1ee9c m\u1ea1nh +(" + OfflineSpecialShop.craftPowerFlatMin(n) + "-" + OfflineSpecialShop.craftPowerFlatMax(n) + ") +(" + OfflineSpecialShop.craftPercentRangeMin(n) + "-" + OfflineSpecialShop.craftPercentRangeMax(n) + ")%");
            vector.addElement("\u0110\u1ed3ng \u0111\u1ed9i +(" + OfflineSpecialShop.craftPercentRangeMin(n) + "-" + OfflineSpecialShop.craftPercentRangeMax(n) + ")%");
            vector.addElement("T\u1ea5t c\u1ea3 +" + OfflineSpecialShop.craftAllFlatFixed(n));
        }
        if (n >= 4) {
            vector.addElement("Ng\u1eabu nhi\u00ean 1 ch\u1ec9 s\u1ed1 +(" + OfflineSpecialShop.craftExtraStatMin(bl, n) + "-" + OfflineSpecialShop.craftExtraStatMax(bl, n) + ")");
        }
        String[] stringArray = new String[vector.size()];
        for (n3 = 0; n3 < stringArray.length; ++n3) {
            stringArray[n3] = (String)vector.elementAt(n3);
        }
        fomula.ability = stringArray;
        fomula.h1 = fomula.ability.length * 18;
        fomula.finish = bl4;
        return fomula;
    }

    private static void craft(byte by, int n) {
        int n2;
        int n3;
        int n4;
        int n5;
        Equip equip;
        int n6;
        int n7;
        int n8 = OfflineSpecialShop.formulaType(by);
        boolean bl = by >= 69;
        int n9 = n + 1;
        if (n9 < 1 || n9 > 5) {
            return;
        }
        Equip equip2 = OfflineSpecialShop.findTierTarget((byte)n8, bl);
        if (equip2 == null) {
            CCanvas.startOKDlg("Kh\u00f4ng t\u00ecm th\u1ea5y trang b\u1ecb \u0111\u1ec3 ch\u1ebf t\u1ea1o.");
            return;
        }
        Equip equip3 = null;
        Equip equip4 = null;
        if (n9 == 1) {
            Equip equip5 = equip3 = bl ? OfflineSpecialShop.findBaseTierIV((byte)n8) : OfflineSpecialShop.findBaseTierII((byte)n8);
            if (equip3 == null || !OfflineSpecialShop.ownsTier((byte)n8, equip3)) {
                CCanvas.startOKDlg("C\u1ea7n \u0111ang s\u1edf h\u1eefu " + (equip3 == null ? "trang b\u1ecb ti\u1ec1n \u0111\u1ec1" : equip3.name) + " tr\u01b0\u1edbc.");
                return;
            }
        } else {
            equip4 = OfflineSpecialShop.findOwnedCraftLevel((byte)n8, equip2.id, n9 - 1);
            if (equip4 == null) {
                CCanvas.startOKDlg("C\u1ea7n \u0111ang s\u1edf h\u1eefu " + equip2.name + " c\u1ea5p " + (n9 - 1) + " tr\u01b0\u1edbc khi ch\u1ebf c\u1ea5p " + n9 + ".");
                return;
            }
        }
        if (TerrainMidlet.myInfo.level2 < (n7 = OfflineSpecialShop.craftLevelRequire(bl, n9))) {
            CCanvas.startOKDlg("Ch\u01b0a \u0111\u1ee7 c\u1ea5p \u0111\u1ed9.");
            return;
        }
        boolean bl2 = OfflineSpecialShop.countMaterial(by) >= 1;
        int n10 = OfflineSpecialShop.priceXu(by);
        if (!(bl2 || n10 >= 0 && TerrainMidlet.myInfo.xu >= n10)) {
            CCanvas.startOKDlg("Thi\u1ebfu s\u00e1ch c\u00f4ng th\u1ee9c v\u00e0 kh\u00f4ng \u0111\u1ee7 xu \u0111\u1ec3 t\u1ef1 mua (" + n10 + " xu).");
            return;
        }
        int[] nArray = OfflineSpecialShop.craftMaterialIds(bl);
        int n11 = OfflineSpecialShop.craftGemId(bl, n9);
        int n12 = OfflineSpecialShop.craftGemCount();
        for (n6 = 0; n6 < nArray.length; ++n6) {
            if (OfflineSpecialShop.countMaterial(nArray[n6]) >= OfflineSpecialShop.craftMetalAmount(bl, n9, n6)) continue;
            CCanvas.startOKDlg("Ch\u01b0a \u0111\u1ee7 nguy\u00ean li\u1ec7u ch\u1ebf t\u1ea1o.");
            return;
        }
        if (OfflineSpecialShop.countMaterial(n11) < n12) {
            CCanvas.startOKDlg("Ch\u01b0a \u0111\u1ee7 ng\u1ecdc ch\u1ebf t\u1ea1o.");
            return;
        }
        if (bl2) {
            OfflineSpecialShop.removeMaterial(by, 1);
        } else {
            TerrainMidlet.myInfo.xu -= n10;
        }
        for (n6 = 0; n6 < nArray.length; ++n6) {
            OfflineSpecialShop.removeMaterial(nArray[n6], OfflineSpecialShop.craftMetalAmount(bl, n9, n6));
        }
        OfflineSpecialShop.removeMaterial(n11, n12);
        if (n9 == 1) {
            OfflineSpecialShop.removeOwnedTier((byte)n8, equip3);
            equip = equip2;
            equip.dbKey = OfflineChest.nextDbKey();
            equip.date = (byte)-1;
            equip.slot = 0;
        } else {
            equip = equip4;
        }
        if (bl) {
            n5 = OfflineSpecialShop.craftFlatBonusMin(n9);
            n4 = OfflineSpecialShop.craftFlatBonusMax(n9);
            n3 = OfflineSpecialShop.craftPercentBonusMin(n9);
            n2 = OfflineSpecialShop.craftPercentBonusMax(n9);
            int n13 = n5 + CRes.random(0, n4 - n5 + 1);
            int n14 = n3 + CRes.random(0, n2 - n3 + 1);
            for (int i = 0; i < 5; ++i) {
                equip.inv_ability[i] = (byte)n13;
                equip.inv_percen[i] = (byte)n14;
            }
        } else {
            n5 = OfflineSpecialShop.craftPowerFlatMin(n9);
            n4 = OfflineSpecialShop.craftPowerFlatMax(n9);
            n3 = OfflineSpecialShop.craftPercentRangeMin(n9);
            n2 = OfflineSpecialShop.craftPercentRangeMax(n9);
            int n15 = n5 + CRes.random(0, n4 - n5 + 1);
            int n16 = n3 + CRes.random(0, n2 - n3 + 1);
            int n17 = n3 + CRes.random(0, n2 - n3 + 1);
            int n18 = OfflineSpecialShop.craftAllFlatFixed(n9);
            for (int i = 0; i < 5; ++i) {
                equip.inv_ability[i] = (byte)n18;
                equip.inv_percen[i] = 0;
            }
            equip.inv_ability[1] = (byte)(equip.inv_ability[1] + (byte)n15);
            equip.inv_percen[1] = (byte)n16;
            equip.inv_percen[4] = (byte)n17;
        }
        if (n9 >= 4) {
            n5 = OfflineSpecialShop.craftExtraStatMin(bl, n9);
            n4 = OfflineSpecialShop.craftExtraStatMax(bl, n9);
            n3 = CRes.random(0, 5);
            n2 = n5 + CRes.random(0, n4 - n5 + 1);
            int n19 = n3;
            equip.inv_ability[n19] = (byte)(equip.inv_ability[n19] + n2);
        }
        equip.craftTier = (byte)n9;
        equip.name = equip2.name + " " + OfflineSpecialShop.craftLevelSuffix(n9);
        equip.setInvAtribute();
        if (n9 == 1) {
            OfflineChest.add(equip);
        } else if (CCanvas.equipScreen != null) {
            CCanvas.equipScreen.getMyEquip();
        }
        OfflineSave.save();
        CCanvas.startOKDlg("Ch\u1ebf t\u1ea1o th\u00e0nh c\u00f4ng " + equip.name + " c\u1ea5p " + n9 + ".");
    }

    private static String craftLevelSuffix(int n) {
        switch (n) {
            case 1: {
                return "I";
            }
            case 2: {
                return "II";
            }
            case 3: {
                return "III";
            }
            case 4: {
                return "IV";
            }
            case 5: {
                return "V";
            }
        }
        return String.valueOf(n);
    }

    private static Equip currentEquip(int n) {
        PlayerInfo playerInfo = TerrainMidlet.myInfo;
        if (playerInfo == null || playerInfo.myEquip == null || n < 0 || n >= playerInfo.myEquip.equips.length) {
            return null;
        }
        return playerInfo.myEquip.equips[n];
    }

    private static Equip findTierTarget(byte by, boolean bl) {
        Equip equip;
        TypeEquip typeEquip;
        PlayerInfo playerInfo = TerrainMidlet.myInfo;
        EquipGlass equipGlass = PlayerEquip.getEquipGlass(playerInfo.gun);
        TypeEquip typeEquip2 = typeEquip = equipGlass == null ? null : equipGlass.getType(by);
        if (typeEquip == null || typeEquip.equip == null || typeEquip.equip.size() == 0) {
            return null;
        }
        OfflineSpecialShop.xuSortedAscending(by);
        Equip equip2 = null;
        Equip equip3 = null;
        for (int i = 0; i < typeEquip.equip.size(); ++i) {
            equip = (Equip)typeEquip.equip.elementAt(i);
            if (equip == null) continue;
            if (equip.xu <= 0 && equip.luong > 0) {
                equip2 = equip;
                continue;
            }
            if (equip.xu <= 0 || equip3 != null && equip.xu <= equip3.xu) continue;
            equip3 = equip;
        }
        Equip equip4 = equip = bl ? equip2 : equip3;
        if (equip == null) {
            return null;
        }
        Equip equip5 = PlayerEquip.createEquip(playerInfo.gun, by, equip.id);
        OfflineChest.ensureDisplayName(equip5);
        return equip5;
    }

    private static Equip[] xuSortedAscending(byte by) {
        Equip[] equipArray;
        int n;
        TypeEquip typeEquip;
        PlayerInfo playerInfo = TerrainMidlet.myInfo;
        EquipGlass equipGlass = PlayerEquip.getEquipGlass(playerInfo.gun);
        TypeEquip typeEquip2 = typeEquip = equipGlass == null ? null : equipGlass.getType(by);
        if (typeEquip == null || typeEquip.equip == null) {
            return new Equip[0];
        }
        Vector<Equip> vector = new Vector<Equip>();
        for (n = 0; n < typeEquip.equip.size(); ++n) {
            Equip equip = (Equip)typeEquip.equip.elementAt(n);
            ShopEquipCatalog.Entry catEntry;
            if (equip != null && equip.xu <= 0 && (catEntry = ShopEquipCatalog.get(playerInfo.gun, equip.id)) != null) {
                equip.xu = catEntry.xu;
                equip.luong = catEntry.luong;
                if (catEntry.name != null && catEntry.name.length() > 0) {
                    equip.name = catEntry.name;
                }
            }
            if (equip == null || equip.xu <= 0) continue;
            vector.addElement(equip);
        }
        int n2 = vector.size();
        Equip[] equipArrayRes = new Equip[n2];
        for (n = 0; n < n2; ++n) {
            equipArrayRes[n] = (Equip)vector.elementAt(n);
        }
        for (int i = 1; i < n2; ++i) {
            Equip equip = equipArrayRes[i];
            int j = i - 1;
            for (; j >= 0 && equipArrayRes[j].xu > equip.xu; --j) {
                equipArrayRes[j + 1] = equipArrayRes[j];
            }
            equipArrayRes[j + 1] = equip;
        }
        return equipArrayRes;
    }

    private static Equip findBaseTierII(byte by) {
        Equip[] equipArray = OfflineSpecialShop.xuSortedAscending(by);
        return equipArray.length >= 2 ? equipArray[1] : null;
    }

    private static Equip findBaseTierIV(byte by) {
        Equip[] equipArray = OfflineSpecialShop.xuSortedAscending(by);
        return equipArray.length >= 2 ? equipArray[equipArray.length - 2] : null;
    }

    private static Equip findOwnedCraftLevel(byte by, short s, int n) {
        PlayerInfo playerInfo = TerrainMidlet.myInfo;
        Equip equip = OfflineSpecialShop.currentEquip(by);
        if (equip != null && !equip.isMaterial && equip.id == s && equip.type == by && equip.glass == playerInfo.gun && equip.craftTier == n) {
            return equip;
        }
        for (int i = 0; i < EquipScreen.inventory.size(); ++i) {
            Equip equip2 = (Equip)EquipScreen.inventory.elementAt(i);
            if (equip2 == null || equip2.isMaterial || equip2.id != s || equip2.type != by || equip2.glass != playerInfo.gun || equip2.craftTier != n) continue;
            return equip2;
        }
        return null;
    }

    private static boolean ownsTier(byte by, Equip equip) {
        if (equip == null) {
            return false;
        }
        Equip equip2 = OfflineSpecialShop.currentEquip(by);
        if (equip2 != null && equip2.isSameEquip(equip)) {
            return true;
        }
        for (int i = 0; i < EquipScreen.inventory.size(); ++i) {
            Equip equip3 = (Equip)EquipScreen.inventory.elementAt(i);
            if (equip3 == null || equip3.isMaterial || !equip3.isSameEquip(equip)) continue;
            return true;
        }
        return false;
    }

    private static void removeOwnedTier(byte by, Equip equip) {
        if (equip == null) {
            return;
        }
        Equip equip2 = OfflineSpecialShop.currentEquip(by);
        if (equip2 != null && equip2.isSameEquip(equip)) {
            PlayerInfo playerInfo = TerrainMidlet.myInfo;
            playerInfo.equipID[playerInfo.gun][by] = -1;
            playerInfo.myEquip.equips[by] = null;
            return;
        }
        for (int i = 0; i < EquipScreen.inventory.size(); ++i) {
            Equip equip3 = (Equip)EquipScreen.inventory.elementAt(i);
            if (equip3 == null || equip3.isMaterial || !equip3.isSameEquip(equip)) continue;
            EquipScreen.inventory.removeElementAt(i);
            return;
        }
    }

    static void addMaterial(byte by, int n) {
        Equip equip = OfflineSpecialShop.findMaterial(by);
        if (equip == null) {
            equip = OfflineSpecialShop.makeShopItem(by);
            equip.xu = 0;
            equip.luong = 0;
            equip.num = n;
            equip.numSelected = 0;
            EquipScreen.inventory.insertElementAt(equip, 0);
        } else {
            equip.num += n;
        }
        if (CCanvas.equipScreen != null) {
            CCanvas.equipScreen.getMyEquip();
        }
    }

    private static Equip findMaterial(int n) {
        for (int i = 0; i < EquipScreen.inventory.size(); ++i) {
            Equip equip = (Equip)EquipScreen.inventory.elementAt(i);
            if (!equip.isMaterial || equip.id != n) continue;
            return equip;
        }
        return null;
    }

    private static Equip findEquipment(int n) {
        for (int i = 0; i < EquipScreen.inventory.size(); ++i) {
            Equip equip = (Equip)EquipScreen.inventory.elementAt(i);
            if (equip.isMaterial || equip.dbKey != n) continue;
            return equip;
        }
        return null;
    }

    private static int countMaterial(int n) {
        Equip equip = OfflineSpecialShop.findMaterial(n);
        return equip == null ? 0 : equip.num;
    }

    private static void removeMaterial(int n, int n2) {
        Equip equip = OfflineSpecialShop.findMaterial(n);
        if (equip != null) {
            equip.num -= n2;
            OfflineSpecialShop.cleanupMaterial(equip);
        }
    }

    private static void cleanupMaterial(Equip equip) {
        if (equip != null && equip.num <= 0) {
            EquipScreen.inventory.removeElement(equip);
        }
    }

    static {
        EXP_POTION_IDS = new int[]{88, 90, 91, 92};
        EXP_POTION_AMOUNTS = new int[]{200, 500, 1000, 10000};
        EXP_POTION_PRICES_LUONG = new int[]{20, 45, 85, 800};
        GEM_NAMES = new String[]{"Ng\u1ecdc \u0111\u1ed3ng \u0111\u1ed9i", "Ng\u1ecdc sinh l\u1ef1c", "Ng\u1ecdc ph\u00f2ng th\u1ee7", "Ng\u1ecdc may m\u1eafn", "Ng\u1ecdc s\u1ee9c m\u1ea1nh"};
        MATERIAL_NAMES = new String[]{"Nh\u00f4m", "S\u1eaft", "\u0110\u1ed3ng", "B\u1ea1c", "V\u00e0ng", "L\u00f4ng v\u0169", "G\u1ed7"};
        SILVER_METAL_BASE = new int[]{10, 40, 50};
    }
}

