/*
 * Decompiled with CFR 0.152.
 */
package Equipment;

import CLib.mGraphics;
import CLib.mImage;
import Equipment.Equip;
import Equipment.EquipGlass;
import Equipment.TypeEquip;
import java.util.Vector;
import coreLG.CCanvas;
import coreLG.TerrainMidlet;
import model.PlayerInfo;
import player.CPlayer;
import shop.ShopEquipCatalog;

public class PlayerEquip {
    public static final byte SLOT_SUNG = 0;
    public static final byte SLOT_NON = 1;
    public static final byte SLOT_GIAP = 2;
    public static final byte OFFLINE_DEFAULT_SLOT_COUNT = 3;
    public byte glass;
    public byte type;
    public byte id;
    public static Vector playerData;
    public static mImage[] imgData;
    public static byte[][] data;
    public static byte[][][] header;
    public static mImage[] bullets;
    public Equip[] equips = new Equip[5];
    public static Vector headers;
    int d;

    public PlayerEquip() {
    }

    public PlayerEquip(short[][] sArray) {
        this.glass = (byte)sArray[0][0];
        int len = Math.min(this.equips.length, sArray.length);
        for (int i = 0; i < len; ++i) {
            this.equips[i] = PlayerEquip.createEquip((byte)sArray[i][0], (byte)sArray[i][1], sArray[i][2]);
        }
    }

    public void addOneEquip(Equip equip) {
        for (int i = 0; i < this.equips.length; ++i) {
            if (this.equips[i] == null) continue;
            for (int j = 0; j < 5; ++j) {
                this.equips[i].inv_attAddPoint[j] = equip.inv_attAddPoint[j];
            }
        }
    }

    public static void addGlassEquip(Vector vector) {
        playerData = vector;
    }

    public static short getStarterEquipId(byte by, byte by2) {
        Equip equip;
        int n;
        EquipGlass equipGlass = PlayerEquip.getEquipGlass(by);
        if (equipGlass == null) {
            return 0;
        }
        TypeEquip typeEquip = equipGlass.getType(by2);
        if (typeEquip == null || typeEquip.equip == null || typeEquip.equip.size() == 0) {
            return 0;
        }
        short s = 0;
        for (n = 0; n < typeEquip.equip.size(); ++n) {
            equip = (Equip)typeEquip.equip.elementAt(n);
            if (equip == null || equip.id <= 0 || equip.level != 1 || s != 0 && equip.id >= s) continue;
            s = equip.id;
        }
        if (s != 0) {
            return s;
        }
        for (n = 0; n < typeEquip.equip.size(); ++n) {
            equip = (Equip)typeEquip.equip.elementAt(n);
            if (equip == null || equip.id <= 0) continue;
            return equip.id;
        }
        return 0;
    }

    public static void applyDefaultOfflineEquipIds(PlayerInfo playerInfo) {
        if (playerInfo == null || playerData == null) {
            return;
        }
        for (int n = 0; n < 11; ++n) {
            for (int i = 0; i < 3; ++i) {
                short s = playerInfo.equipID[n][i];
                if (s != 0 && (s <= 0 || PlayerEquip.getEquip((byte)n, (byte)i, s) != null)) continue;
                playerInfo.equipID[n][i] = PlayerEquip.getStarterEquipId((byte)n, (byte)i);
            }
        }
    }

    public static EquipGlass getEquipGlass(byte by) {
        if (playerData == null) {
            return null;
        }
        for (int i = 0; i < playerData.size(); ++i) {
            EquipGlass equipGlass = (EquipGlass)playerData.elementAt(i);
            if (equipGlass == null || equipGlass.glassID != by) continue;
            return equipGlass;
        }
        return null;
    }

    private static short borrowIcon(byte by, short s) {
        Equip equip = PlayerEquip.getEquip((byte)0, by, s);
        return equip != null ? equip.icon : (short)0;
    }

    public static void installDrabyEquipGlass() {
        if (playerData == null || PlayerEquip.getEquipGlass((byte)10) != null) {
            return;
        }
        EquipGlass equipGlass = new EquipGlass(10);
        equipGlass.maxDamage = (short)380;
        Vector<TypeEquip> vector = new Vector<TypeEquip>();
        Vector<Equip> vector2 = new Vector<Equip>();
        vector2.addElement(PlayerEquip.buildDrabyEquip((short)111, (byte)0, 0, -20, "H\u1ecfa ph\u00e1o Draby I", 1000, -1, (byte)5, (short)307, new byte[]{0, 8, 0, 0, 0}));
        vector2.addElement(PlayerEquip.buildDrabyEquip((short)112, (byte)0, 0, -20, "H\u1ecfa ph\u00e1o Draby II", 5000, -1, (byte)10, (short)308, new byte[]{0, 16, 0, 0, 0}));
        vector2.addElement(PlayerEquip.buildDrabyEquip((short)113, (byte)0, 0, -20, "H\u1ecfa ph\u00e1o Draby III", 10000, -1, (byte)15, (short)309, new byte[]{0, 26, 0, 0, 0}));
        vector2.addElement(PlayerEquip.buildDrabyEquip((short)114, (byte)0, 0, -20, "H\u1ecfa ph\u00e1o Draby IV", 20000, 17, (byte)25, (short)310, new byte[]{0, 38, 0, 0, 0}));
        vector2.addElement(PlayerEquip.buildDrabyEquip((short)115, (byte)0, 0, -20, "H\u1ecfa ph\u00e1o Draby b\u1ea1c", 200000, 160, (byte)10, (short)311, new byte[]{0, 55, 0, 0, 0}));
        vector2.addElement(PlayerEquip.buildDrabyEquip((short)116, (byte)0, 0, -20, "H\u1ecfa ph\u00e1o Draby v\u00e0ng", -1, 400, (byte)50, (short)312, new byte[]{0, 75, 0, 0, 0}));
        TypeEquip typeEquip = new TypeEquip(0);
        typeEquip.addEquip(vector2);
        vector.addElement(typeEquip);
        Vector<Equip> vector3 = new Vector<Equip>();
        vector3.addElement(PlayerEquip.buildDrabyEquip((short)151, (byte)1, 1, -46, "V\u01b0\u01a1ng mi\u1ec7n l\u1eeda I", 1000, -1, (byte)5, (short)331, new byte[]{5, 0, 0, 0, 0}));
        vector3.addElement(PlayerEquip.buildDrabyEquip((short)152, (byte)1, 1, -46, "V\u01b0\u01a1ng mi\u1ec7n l\u1eeda II", 5000, -1, (byte)10, (short)332, new byte[]{9, 0, 0, 0, 0}));
        vector3.addElement(PlayerEquip.buildDrabyEquip((short)153, (byte)1, 1, -46, "V\u01b0\u01a1ng mi\u1ec7n l\u1eeda III", 10000, -1, (byte)15, (short)333, new byte[]{14, 0, 0, 0, 0}));
        vector3.addElement(PlayerEquip.buildDrabyEquip((short)154, (byte)1, 1, -46, "V\u01b0\u01a1ng mi\u1ec7n l\u1eeda IV", 20000, 17, (byte)25, (short)334, new byte[]{20, 0, 0, 0, 0}));
        vector3.addElement(PlayerEquip.buildDrabyEquip((short)155, (byte)1, 1, -46, "V\u01b0\u01a1ng mi\u1ec7n l\u1eeda b\u1ea1c", 200000, 160, (byte)10, (short)335, new byte[]{29, 0, 0, 0, 0}));
        vector3.addElement(PlayerEquip.buildDrabyEquip((short)156, (byte)1, 1, -46, "V\u01b0\u01a1ng mi\u1ec7n l\u1eeda v\u00e0ng", -1, 400, (byte)50, (short)336, new byte[]{40, 0, 0, 0, 0}));
        vector3.addElement(PlayerEquip.buildDrabyEquip((short)157, (byte)1, 6, -46, "Batman", -1, 50, (byte)1, PlayerEquip.borrowIcon((byte)1, (short)22), new byte[]{5, 5, 5, 5, 5}));
        vector3.addElement(PlayerEquip.buildDrabyEquip((short)158, (byte)1, 7, -46, "Ironman", -1, 50, (byte)1, PlayerEquip.borrowIcon((byte)1, (short)26), new byte[]{5, 5, 5, 5, 5}));
        vector3.addElement(PlayerEquip.buildDrabyEquip((short)159, (byte)1, 8, -46, "Zombie", -1, 50, (byte)1, PlayerEquip.borrowIcon((byte)1, (short)33), new byte[]{5, 5, 5, 5, 5}));
        vector3.addElement(PlayerEquip.buildDrabyEquip((short)160, (byte)1, 9, -46, "B\u00ed ng\u00f4", -1, 50, (byte)1, PlayerEquip.borrowIcon((byte)1, (short)36), new byte[]{5, 5, 5, 5, 5}));
        TypeEquip typeEquip2 = new TypeEquip(1);
        typeEquip2.addEquip(vector3);
        vector.addElement(typeEquip2);
        Vector<Equip> vector5 = new Vector<Equip>();
        vector5.addElement(PlayerEquip.buildDrabyEquip((short)131, (byte)2, 3, -36, "Gi\u00e1p v\u1ea3y l\u1eeda I", 1000, -1, (byte)5, (short)319, new byte[]{0, 0, 10, 0, 0}));
        vector5.addElement(PlayerEquip.buildDrabyEquip((short)132, (byte)2, 3, -36, "Gi\u00e1p v\u1ea3y l\u1eeda II", 5000, -1, (byte)10, (short)320, new byte[]{0, 0, 18, 0, 0}));
        vector5.addElement(PlayerEquip.buildDrabyEquip((short)133, (byte)2, 3, -36, "Gi\u00e1p v\u1ea3y l\u1eeda III", 10000, -1, (byte)15, (short)321, new byte[]{0, 0, 28, 0, 0}));
        vector5.addElement(PlayerEquip.buildDrabyEquip((short)134, (byte)2, 3, -36, "Gi\u00e1p v\u1ea3y l\u1eeda IV", 20000, 17, (byte)25, (short)322, new byte[]{0, 0, 40, 0, 0}));
        vector5.addElement(PlayerEquip.buildDrabyEquip((short)135, (byte)2, 3, -36, "Gi\u00e1p v\u1ea3y l\u1eeda b\u1ea1c", 200000, 160, (byte)10, (short)323, new byte[]{0, 0, 58, 0, 0}));
        vector5.addElement(PlayerEquip.buildDrabyEquip((short)136, (byte)2, 3, -36, "Gi\u00e1p v\u1ea3y l\u1eeda v\u00e0ng", -1, 400, (byte)50, (short)324, new byte[]{0, 0, 80, 0, 0}));
        TypeEquip typeEquip4 = new TypeEquip(2);
        typeEquip4.addEquip(vector5);
        vector.addElement(typeEquip4);
        Vector<Equip> vector6 = new Vector<Equip>();
        vector6.addElement(PlayerEquip.buildDrabyEquip((short)141, (byte)3, 4, -40, "K\u00ednh l\u1eeda I", 1000, -1, (byte)5, (short)325, new byte[]{0, 0, 0, 5, 0}));
        vector6.addElement(PlayerEquip.buildDrabyEquip((short)142, (byte)3, 4, -40, "K\u00ednh l\u1eeda II", 5000, -1, (byte)10, (short)326, new byte[]{0, 0, 0, 9, 0}));
        vector6.addElement(PlayerEquip.buildDrabyEquip((short)143, (byte)3, 4, -40, "K\u00ednh l\u1eeda III", 10000, -1, (byte)15, (short)327, new byte[]{0, 0, 0, 14, 0}));
        vector6.addElement(PlayerEquip.buildDrabyEquip((short)144, (byte)3, 4, -40, "K\u00ednh l\u1eeda IV", 20000, 17, (byte)25, (short)328, new byte[]{0, 0, 0, 20, 0}));
        vector6.addElement(PlayerEquip.buildDrabyEquip((short)145, (byte)3, 4, -40, "K\u00ednh l\u1eeda b\u1ea1c", 200000, 160, (byte)10, (short)329, new byte[]{0, 0, 0, 29, 0}));
        vector6.addElement(PlayerEquip.buildDrabyEquip((short)146, (byte)3, 4, -40, "K\u00ednh l\u1eeda v\u00e0ng", -1, 400, (byte)50, (short)330, new byte[]{0, 0, 0, 40, 0}));
        TypeEquip typeEquip5 = new TypeEquip(3);
        typeEquip5.addEquip(vector6);
        vector.addElement(typeEquip5);
        Vector<Equip> vector7 = new Vector<Equip>();
        vector7.addElement(PlayerEquip.buildDrabyEquip((short)121, (byte)4, 5, -40, "C\u00e1nh r\u1ed3ng l\u1eeda I", 1000, -1, (byte)5, (short)313, new byte[]{0, 0, 0, 0, 5}));
        vector7.addElement(PlayerEquip.buildDrabyEquip((short)122, (byte)4, 5, -40, "C\u00e1nh r\u1ed3ng l\u1eeda II", 5000, -1, (byte)10, (short)314, new byte[]{0, 0, 0, 0, 10}));
        vector7.addElement(PlayerEquip.buildDrabyEquip((short)123, (byte)4, 5, -40, "C\u00e1nh r\u1ed3ng l\u1eeda III", 10000, -1, (byte)15, (short)315, new byte[]{0, 0, 0, 0, 16}));
        vector7.addElement(PlayerEquip.buildDrabyEquip((short)124, (byte)4, 5, -40, "C\u00e1nh r\u1ed3ng l\u1eeda IV", 20000, 17, (byte)25, (short)316, new byte[]{0, 0, 0, 0, 24}));
        vector7.addElement(PlayerEquip.buildDrabyEquip((short)125, (byte)4, 5, -40, "C\u00e1nh r\u1ed3ng l\u1eeda b\u1ea1c", 200000, 160, (byte)10, (short)317, new byte[]{0, 0, 0, 0, 35}));
        vector7.addElement(PlayerEquip.buildDrabyEquip((short)126, (byte)4, 5, -40, "C\u00e1nh r\u1ed3ng l\u1eeda v\u00e0ng", -1, 400, (byte)50, (short)318, new byte[]{0, 0, 0, 0, 50}));
        TypeEquip typeEquip6 = new TypeEquip(4);
        typeEquip6.addEquip(vector7);
        vector.addElement(typeEquip6);
        equipGlass.addType(vector);
        playerData.addElement(equipGlass);
    }

    public static void installTrangPhucPlaceholders() {
    }

    public static short[] getMaskSetData(byte glass, short maskId) {
        switch (glass) {
            case 0:
                if (maskId == 22) return new short[]{24, 22, 23, -1, 25};
                if (maskId == 26) return new short[]{28, 26, 27, -1, -1};
                if (maskId == 33) return new short[]{35, 33, 34, -1, -1};
                if (maskId == 36) return new short[]{38, 36, 37, -1, -1};
                break;
            case 1:
                if (maskId == 15) return new short[]{17, 15, 16, -1, 18};
                if (maskId == 19) return new short[]{21, 19, 20, -1, -1};
                if (maskId == 30) return new short[]{32, 30, 31, -1, -1};
                if (maskId == 33) return new short[]{35, 33, 34, -1, -1};
                break;
            case 2:
                if (maskId == 19) return new short[]{21, 19, 20, -1, 22};
                if (maskId == 23) return new short[]{25, 23, 24, -1, -1};
                if (maskId == 30) return new short[]{32, 30, 31, -1, -1};
                if (maskId == 33) return new short[]{35, 33, 34, -1, -1};
                break;
            case 3:
                if (maskId == 20) return new short[]{22, 20, 27, 23, 21};
                if (maskId == 24) return new short[]{26, 24, 25, -1, -1};
                if (maskId == 31) return new short[]{33, 31, 32, -1, -1};
                if (maskId == 34) return new short[]{35, 34, 33, -1, -1};
                break;
            case 4:
                if (maskId == 21) return new short[]{23, 21, 22, -1, 24};
                if (maskId == 25) return new short[]{27, 25, 26, -1, -1};
                if (maskId == 30) return new short[]{32, 30, 31, -1, -1};
                if (maskId == 33) return new short[]{35, 33, 34, -1, -1};
                break;
            case 5:
                if (maskId == 18) return new short[]{20, 18, 19, -1, 22};
                if (maskId == 21) return new short[]{23, 21, 22, -1, -1};
                if (maskId == 29) return new short[]{31, 29, 30, -1, -1};
                if (maskId == 32) return new short[]{34, 32, 33, -1, -1};
                break;
            case 6:
                if (maskId == 17) return new short[]{19, 17, 18, -1, 20};
                if (maskId == 21) return new short[]{23, 21, 22, -1, -1};
                if (maskId == 30) return new short[]{32, 30, 31, -1, -1};
                if (maskId == 33) return new short[]{35, 33, 34, -1, -1};
                break;
            case 7:
                if (maskId == 18) return new short[]{20, 18, 19, -1, 21};
                if (maskId == 22) return new short[]{24, 22, 23, -1, -1};
                if (maskId == 31) return new short[]{33, 31, 32, -1, -1};
                if (maskId == 34) return new short[]{36, 34, 35, -1, -1};
                break;
            case 8:
                if (maskId == 18) return new short[]{20, 18, 19, -1, 21};
                if (maskId == 22) return new short[]{24, 22, 23, -1, -1};
                if (maskId == 30) return new short[]{32, 30, 31, -1, -1};
                if (maskId == 33) return new short[]{35, 33, 34, -1, -1};
                break;
            case 9:
                if (maskId == 15) return new short[]{17, 15, 16, -1, 18};
                if (maskId == 19) return new short[]{21, 19, 20, -1, -1};
                if (maskId == 30) return new short[]{32, 30, 31, -1, -1};
                if (maskId == 33) return new short[]{35, 33, 34, -1, -1};
                break;
            case 10:
                if (maskId == 157) return new short[]{114, 157, 134, -1, 124};
                if (maskId == 158) return new short[]{114, 158, 134, -1, -1};
                if (maskId == 159) return new short[]{114, 159, 134, -1, -1};
                if (maskId == 160) return new short[]{114, 160, 134, -1, -1};
                break;
        }
        return null;
    }

    public static boolean isMaskItem(byte by, short s) {
        if (s <= 0) {
            return false;
        }
        if (PlayerEquip.getMaskSetData(by, s) != null) {
            return true;
        }
        String string = ShopEquipCatalog.resolveName(by, s);
        if (string == null) {
            return false;
        }
        return PlayerEquip.isCostumeName(string);
    }

    public static boolean isCostumeName(String string) {
        if (string == null) {
            return false;
        }
        String string2 = string.trim().toLowerCase();
        return string2.equals("batman") || string2.equals("ironman") || string2.equals("zombie") || string2.equals("b\u00ed ng\u00f4") || string2.indexOf("iron") >= 0 || string2.indexOf("batman") >= 0 || string2.indexOf("zombie") >= 0 || string2.indexOf("m\u1eb7t n\u1ea1") >= 0 || string2.indexOf("mat na") >= 0;
    }

    public static void migrateCostumeHats(PlayerInfo playerInfo) {
    }

    public static void ensureFullSuitWhenWearingMask(PlayerInfo playerInfo) {
    }

    private static Equip buildDrabyEquip(short s, byte by, int n, int n2, String string, int n3, int n4, byte by2, short s2, byte[] byArray) {
        int n5;
        Equip equip = new Equip();
        equip.id = s;
        equip.type = by;
        equip.glass = (byte)10;
        equip.level = by2;
        equip.icon = s2;
        equip.name = string;
        equip.xu = n3;
        equip.luong = n4;
        equip.date = (byte)-1;
        equip.bullet = 0;
        int n6 = 6;
        equip.x = new short[n6];
        equip.y = new short[n6];
        equip.w = new byte[n6];
        equip.h = new byte[n6];
        equip.dx = new byte[n6];
        equip.dy = new byte[n6];
        for (n5 = 0; n5 < n6; ++n5) {
            equip.x[n5] = (short)(n * 32);
            equip.y[n5] = 0;
            equip.w[n5] = 32;
            equip.h[n5] = 32;
            equip.dx[n5] = -18;
            equip.dy[n5] = (byte)n2;
        }
        for (n5 = 0; n5 < 5; ++n5) {
            equip.inv_ability[n5] = byArray[n5];
            equip.shop_ability[n5] = byArray[n5];
        }
        return equip;
    }

    public static Equip getEquip(byte by, byte by2, short s) {
        EquipGlass equipGlass = PlayerEquip.getEquipGlass(by);
        if (equipGlass != null) {
            Equip equip;
            TypeEquip typeEquip = equipGlass.getType(by2);
            if (typeEquip != null && (equip = typeEquip.getEquip(s)) != null) {
                equip.glass = by;
                equip.type = by2;
                if (PlayerEquip.isMaskItem(by, s)) {
                    equip.vip = 1;
                }
                return equip;
            }
            if (equipGlass.type != null) {
                for (int i = 0; i < equipGlass.type.size(); ++i) {
                    TypeEquip te = (TypeEquip)equipGlass.type.elementAt(i);
                    if (te != null && (equip = te.getEquip(s)) != null) {
                        equip.glass = by;
                        equip.type = te.typeID;
                        if (PlayerEquip.isMaskItem(by, s)) {
                            equip.vip = 1;
                        }
                        return equip;
                    }
                }
            }
            return null;
        }
        return null;
    }

    private static void copyShopStats(Equip equip, Equip equip2) {
        for (int i = 0; i < 5; ++i) {
            equip.shop_ability[i] = equip2.shop_ability[i];
            equip.shop_percen[i] = equip2.shop_percen[i];
            equip.inv_ability[i] = equip2.inv_ability[i];
            equip.inv_percen[i] = equip2.inv_percen[i];
        }
    }

    public static Equip createEquip(byte by, byte by2, short s) {
        Equip equip = new Equip();
        Equip equip2 = PlayerEquip.getEquip(by, by2, s);
        if (equip2 != null) {
            equip.glass = equip2.glass;
            equip.type = equip2.type;
            equip.id = equip2.id;
            equip.name = equip2.name != null ? equip2.name : "";
            equip.date = equip2.date;
            equip.x = equip2.x;
            equip.y = equip2.y;
            equip.dx = equip2.dx;
            equip.dy = equip2.dy;
            equip.w = equip2.w;
            equip.h = equip2.h;
            equip.level = equip2.level;
            equip.frame = equip2.frame;
            equip.icon = equip2.icon;
            equip.xu = equip2.xu;
            equip.luong = equip2.luong;
            equip.bullet = equip2.bullet;
            equip.index = equip2.index;
            equip.frame = equip2.frame;
            equip.vip = equip2.vip;
            PlayerEquip.copyShopStats(equip, equip2);
            return equip;
        }
        return null;
    }

    public short getActiveMaskId() {
        if (this.equips != null && this.equips.length > 1 && this.equips[1] != null) {
            if (PlayerEquip.getMaskSetData(this.glass, this.equips[1].id) != null) {
                return this.equips[1].id;
            }
        }
        if (TerrainMidlet.isVip != null && this.glass >= 0 && this.glass < TerrainMidlet.isVip.length && TerrainMidlet.isVip[this.glass]) {
            if (TerrainMidlet.myInfo != null && TerrainMidlet.myInfo.equipVipID != null) {
                short vipId = TerrainMidlet.myInfo.equipVipID[this.glass][1];
                if (vipId > 0 && PlayerEquip.getMaskSetData(this.glass, vipId) != null) {
                    return vipId;
                }
            }
        }
        return -1;
    }

    public void paintGiap(int n, int n2, int n3, int n4, mGraphics mGraphics2) {
        if (this.glass == 10) {
            return;
        }
        short maskId = this.getActiveMaskId();
        if (maskId > 0) {
            short[] setData = PlayerEquip.getMaskSetData(this.glass, maskId);
            if (setData != null && setData[2] > 0) {
                Equip equip = PlayerEquip.getEquip(this.glass, (byte)2, setData[2]);
                if (equip != null) {
                    equip.drawImage(mGraphics2, n3, n4, n, n2);
                }
            }
            return;
        }
        Equip equip = this.equips != null && this.equips.length > 2 ? this.equips[2] : null;
        if (equip == null && this.glass >= 0 && this.glass < 10) {
            short defaultArmor = PlayerEquip.getStarterEquipId(this.glass, (byte)2);
            if (defaultArmor > 0) {
                equip = PlayerEquip.getEquip(this.glass, (byte)2, defaultArmor);
            }
        }
        if (equip != null) {
            equip.drawImage(mGraphics2, n3, n4, n, n2);
        }
    }

    public void paintNon(int n, int n2, int n3, int n4, mGraphics mGraphics2) {
        if (this.glass == 10) {
            return;
        }
        short maskId = this.getActiveMaskId();
        if (maskId > 0) {
            Equip equip = PlayerEquip.getEquip(this.glass, (byte)1, maskId);
            if (equip != null) {
                equip.drawImage(mGraphics2, n3, n4, n, n2);
            }
            return;
        }
        if (this.equips != null && this.equips.length > 1 && this.equips[1] != null) {
            this.equips[1].drawImage(mGraphics2, n3, n4, n, n2);
        }
    }

    public void paintKinh(int n, int n2, int n3, int n4, mGraphics mGraphics2) {
        if (this.glass == 10) {
            return;
        }
        short maskId = this.getActiveMaskId();
        if (maskId > 0) {
            short[] setData = PlayerEquip.getMaskSetData(this.glass, maskId);
            if (setData != null && setData[3] > 0) {
                Equip equip = PlayerEquip.getEquip(this.glass, (byte)3, setData[3]);
                if (equip != null) {
                    equip.drawImage(mGraphics2, n3, n4, n, n2);
                }
            }
            return;
        }
        if (this.equips != null && this.equips.length > 3 && this.equips[3] != null) {
            this.equips[3].drawImage(mGraphics2, n3, n4, n, n2);
        }
    }

    public void paintCanh(int n, int n2, int n3, int n4, mGraphics mGraphics2) {
        if (this.glass == 10) {
            return;
        }
        short maskId = this.getActiveMaskId();
        if (maskId > 0) {
            short[] setData = PlayerEquip.getMaskSetData(this.glass, maskId);
            if (setData != null && setData[4] > 0) {
                Equip equip = PlayerEquip.getEquip(this.glass, (byte)4, setData[4]);
                if (equip != null) {
                    equip.drawImage(mGraphics2, n3, n4, n, n2);
                }
            }
            return;
        }
        if (this.equips != null && this.equips.length > 4 && this.equips[4] != null) {
            this.equips[4].drawImage(mGraphics2, n3, n4, n, n2);
        }
    }

    public void paintSung(int n, int n2, int n3, int n4, mGraphics mGraphics2) {
        if (this.glass == 10) {
            return;
        }
        short maskId = this.getActiveMaskId();
        if (maskId > 0) {
            short[] setData = PlayerEquip.getMaskSetData(this.glass, maskId);
            if (setData != null && setData[0] > 0) {
                Equip equip = PlayerEquip.getEquip(this.glass, (byte)0, setData[0]);
                if (equip != null) {
                    equip.drawImage(mGraphics2, n3, n4, n, n2);
                }
            }
            return;
        }
        Equip equip = this.equips != null && this.equips.length > 0 ? this.equips[0] : null;
        if (equip == null && this.glass >= 0 && this.glass < 10) {
            short defaultGun = PlayerEquip.getStarterEquipId(this.glass, (byte)0);
            if (defaultGun > 0) {
                equip = PlayerEquip.getEquip(this.glass, (byte)0, defaultGun);
            }
        }
        if (equip != null) {
            equip.drawImage(mGraphics2, n3, n4, n, n2);
        }
    }



    public void paintFace(int n, int n2, int n3, int n4, mGraphics mGraphics2) {
        if (this.glass < 0 || CPlayer.pImg == null || this.glass >= CPlayer.pImg.length) {
            return;
        }
        mImage mImage2 = CPlayer.pImg[this.glass];
        if (mImage2 == null || mImage2.image == null) {
            return;
        }
        int frameH = (this.glass == 3 || this.glass == 7 || this.glass == 10) ? 32 : (this.glass == 12 ? 28 : (this.glass == 14 ? 30 : 24));
        int totalFrames = mImage2.image.getHeight() / frameH;
        if (totalFrames <= 0) {
            frameH = mImage2.image.getHeight();
            totalFrames = 1;
        }
        if (n4 < 0) {
            n4 = 0;
        } else if (n4 >= totalFrames) {
            n4 = totalFrames - 1;
        }
        mGraphics2.drawRegion(mImage2, 0, n4 * frameH, mImage2.image.getWidth(), frameH, n3, n, n2, mGraphics.BOTTOM | mGraphics.HCENTER, false);
    }

    public void paint(mGraphics mGraphics2, int n, int n2, int n3, int n4) {
        if (this.glass == 10) {
            this.paintFace(n3, n4, n, n2, mGraphics2);
            return;
        }
        if (this.getActiveMaskId() <= 0) {
            if (this.equips[0] == null && this.glass >= 0 && this.glass < 10) {
                short defaultGun = PlayerEquip.getStarterEquipId(this.glass, (byte)0);
                if (defaultGun > 0) {
                    this.equips[0] = PlayerEquip.createEquip(this.glass, (byte)0, defaultGun);
                }
            }
            if (this.equips[2] == null && this.glass >= 0 && this.glass < 10) {
                short defaultArmor = PlayerEquip.getStarterEquipId(this.glass, (byte)2);
                if (defaultArmor > 0) {
                    this.equips[2] = PlayerEquip.createEquip(this.glass, (byte)2, defaultArmor);
                }
            }
        }

        this.paintSung(n3, n4, n, n2, mGraphics2);
        this.paintCanh(n3, n4, n, n2, mGraphics2);
        this.paintFace(n3, n4, n, n2, mGraphics2);
        this.paintGiap(n3, n4, n, n2, mGraphics2);
        this.paintNon(n3, n4, n, n2, mGraphics2);
        this.paintKinh(n3, n4, n, n2, mGraphics2);
    }

    static {
        imgData = new mImage[12];
        data = new byte[12][];
        header = new byte[12][12][];
        bullets = new mImage[12];
        headers = new Vector();
    }
}

