/*
 * Decompiled with CFR 0.152.
 */
package screen;

import CLib.mGraphics;
import Equipment.Equip;
import com.teamobi.mobiarmy2.GameMidlet;
import com.teamobi.mobiarmy2.OfflineChest;
import com.teamobi.mobiarmy2.OfflineSave;
import coreLG.CCanvas;
import coreLG.TerrainMidlet;
import item.Item;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Vector;
import model.Font;
import model.IAction;
import model.InputDlg;
import model.PlayerInfo;
import network.Command;
import screen.CScreen;
import screen.ChangePlayerCSr;
import screen.EquipScreen;
import shop.OfflineShopEquip;
import shop.ShopItem;
import shop.ShopLinhTinh;

public final class OfflineEditorScr
extends CScreen {
    private static final String[] CATEGORIES = new String[]{"NHAN VAT", "TIEN TE", "CHI SO", "KHO DO", "TRANG BI", "NGOC", "VAT PHAM", "NHIEM VU", "MO KHO", "TIEN ICH"};
    private static final int ROW_HEIGHT = 24;
    private static final int TOP = 34;
    private static OfflineEditorScr instance;
    private final Vector entries = new Vector();
    private CScreen previous;
    private byte[] openingSnapshot;
    private byte[] persistedSnapshot;
    private int category = -1;
    private int activeCategory = -1;
    private int entryGeneration;
    private int selected;
    private int firstVisible;
    private int bulkCategory;
    private int pointerStartY;
    private int pointerStartFirst;
    private int pointerRow = -1;
    private boolean pointerMoved;

    private OfflineEditorScr() {
        this.nameCScreen = "OfflineEditorScr";
        this.center = new Command("SUA", new IAction(){

            public void perform() {
                OfflineEditorScr.this.activate();
            }
        });
        this.right = new Command("TRO LAI", new IAction(){

            public void perform() {
                OfflineEditorScr.this.goBack();
            }
        });
    }

    public static void open(CScreen cScreen) {
        if (TerrainMidlet.myInfo == null && !GameMidlet.continueOfflineGame()) {
            CCanvas.startOKDlg("Khong doc duoc save offline");
            return;
        }
        byte[] byArray = OfflineSave.exportBytes();
        if (TerrainMidlet.myInfo == null || byArray == null) {
            CCanvas.startOKDlg("Profile offline chua san sang");
            return;
        }
        instance = new OfflineEditorScr();
        OfflineEditorScr.instance.previous = cScreen;
        instance.initializeSnapshots(byArray);
        instance.openRoot();
        instance.show();
    }

    private void initializeSnapshots(byte[] byArray) {
        this.openingSnapshot = OfflineEditorScr.snapshot(byArray);
        this.persistedSnapshot = OfflineEditorScr.snapshot(this.openingSnapshot);
    }

    static int clamp(int n, int n2, int n3) {
        if (n2 > n3) {
            throw new IllegalArgumentException("range");
        }
        return n < n2 ? n2 : (n > n3 ? n3 : n);
    }

    static int parseInt(String string, int n, int n2) {
        if (string == null || string.trim().length() == 0) {
            throw new IllegalArgumentException("So khong hop le");
        }
        try {
            int n3 = Integer.parseInt(string.trim());
            if (n3 < n || n3 > n2) {
                throw new IllegalArgumentException("So ngoai pham vi");
            }
            return n3;
        }
        catch (NumberFormatException numberFormatException) {
            throw new IllegalArgumentException("So khong hop le");
        }
    }

    static byte[] snapshot(byte[] byArray) {
        if (byArray == null) {
            return null;
        }
        byte[] byArray2 = new byte[byArray.length];
        System.arraycopy(byArray, 0, byArray2, 0, byArray.length);
        return byArray2;
    }

    static boolean isValidYmd(int n) {
        boolean bl;
        if (n == 0) {
            return true;
        }
        if (n < 10101) {
            return false;
        }
        int n2 = n / 10000;
        int n3 = n / 100 % 100;
        int n4 = n % 100;
        if (n3 < 1 || n3 > 12 || n4 < 1) {
            return false;
        }
        int[] nArray = new int[]{31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        boolean bl2 = bl = n2 % 4 == 0 && (n2 % 100 != 0 || n2 % 400 == 0);
        if (bl) {
            nArray[1] = 29;
        }
        return n4 <= nArray[n3 - 1];
    }

    static int cycleExisting(int n, int n2, int[] nArray) {
        if (nArray == null || nArray.length == 0) {
            throw new IllegalArgumentException("Khong co item hop le");
        }
        for (int i = 0; i < nArray.length; ++i) {
            if (nArray[i] != n) continue;
            int n3 = (i + (n2 < 0 ? -1 : 1) + nArray.length) % nArray.length;
            return nArray[n3];
        }
        return nArray[0];
    }

    static boolean isExisting(int n, int[] nArray) {
        for (int i = 0; nArray != null && i < nArray.length; ++i) {
            if (nArray[i] != n) continue;
            return true;
        }
        return false;
    }

    static int[] filterEquipIds(Vector vector, int n, int n2) {
        Object object;
        int[] nArray = new int[vector == null ? 0 : vector.size()];
        int n3 = 0;
        for (int i = 0; vector != null && i < vector.size(); ++i) {
            object = vector.elementAt(i);
            if (!(object instanceof Equip)) continue;
            Equip equip = (Equip)object;
            if (equip.isMaterial || equip.glass != n || equip.type != n2 || OfflineEditorScr.isExisting(equip.id, nArray, n3)) continue;
            nArray[n3++] = equip.id;
        }
        int[] resArr = new int[n3];
        System.arraycopy(nArray, 0, resArr, 0, n3);
        return resArr;
    }

    static boolean isReferencedAnywhere(short s, short[][] sArray, short[][] sArray2) {
        return OfflineEditorScr.containsId(sArray, s) || OfflineEditorScr.containsId(sArray2, s);
    }

    private static boolean containsId(short[][] sArray, short s) {
        for (int i = 0; sArray != null && i < sArray.length; ++i) {
            for (int j = 0; sArray[i] != null && j < sArray[i].length; ++j) {
                if (sArray[i][j] != s) continue;
                return true;
            }
        }
        return false;
    }

    private static boolean isExisting(int n, int[] nArray, int n2) {
        for (int i = 0; i < n2; ++i) {
            if (nArray[i] != n) continue;
            return true;
        }
        return false;
    }

    static int[] composeLoadoutIds(int[] nArray) {
        int[] nArray2 = new int[(nArray == null ? 0 : nArray.length) + 1];
        nArray2[0] = -2;
        int n = 1;
        for (int i = 0; nArray != null && i < nArray.length; ++i) {
            int n2 = nArray[i];
            if (n2 < 0 || OfflineEditorScr.isExisting(n2, nArray2, n)) continue;
            nArray2[n++] = n2;
        }
        int[] nArray3 = new int[n];
        System.arraycopy(nArray2, 0, nArray3, 0, n);
        return nArray3;
    }

    static int resetAllowedId(Vector vector, int n, int n2) {
        return vector == null ? OfflineEditorScr.clamp(0, n, n2) : (Integer)vector.firstElement();
    }

    static void resetLoadoutSlot(int[] nArray, int n) {
        if (nArray == null || n < 0 || n >= nArray.length) {
            return;
        }
        nArray[n] = nArray.length > 3 && n < 4 ? (n < 2 ? 0 : 1) : -2;
    }

    static int[] filterGemIds(int[] nArray) {
        return OfflineEditorScr.filterIds(nArray, 0, 49);
    }

    static int[] composeSocketIds(int[] nArray) {
        int n = nArray == null ? 0 : nArray.length;
        int[] nArray2 = new int[n + 1];
        nArray2[0] = -1;
        if (n > 0) {
            System.arraycopy(nArray, 0, nArray2, 1, n);
        }
        return nArray2;
    }

    static int[] filterAbilityIds(int[] nArray) {
        return OfflineEditorScr.filterIds(nArray, 0, 127);
    }

    private static int[] filterIds(int[] nArray, int n, int n2) {
        int[] nArray2 = new int[nArray == null ? 0 : nArray.length];
        int n3 = 0;
        for (int i = 0; nArray != null && i < nArray.length; ++i) {
            int n4;
            int n5 = nArray[i];
            for (n4 = 0; n4 < n3 && nArray2[n4] != n5; ++n4) {
            }
            if (n5 < n || n5 > n2 || n4 != n3) continue;
            nArray2[n3++] = n5;
        }
        int[] nArray3 = new int[n3];
        System.arraycopy(nArray2, 0, nArray3, 0, n3);
        return nArray3;
    }

    static int validatePercent(int n) {
        if (n < 0 || n > 100) {
            throw new IllegalArgumentException("Phan tram phai trong 0..100");
        }
        return n;
    }

    static Vector ensureInventory(Vector vector) {
        return vector == null ? new Vector() : vector;
    }

    static int dragFirstVisible(int n, int n2, int n3, int n4) {
        int n5 = Math.max(0, n3 - Math.max(1, n4));
        return OfflineEditorScr.clamp(n - n2 / 24, 0, n5);
    }

    private void openRoot() {
        this.category = -1;
        this.activeCategory = -1;
        ++this.entryGeneration;
        this.entries.removeAllElements();
        for (int i = 0; i < CATEGORIES.length; ++i) {
            final int n = i;
            this.entries.addElement(new ActionEntry(CATEGORIES[i], new IAction(){

                @Override
                public void perform() {
                    OfflineEditorScr.this.openCategory(n);
                }
            }));
        }
        this.resetSelection();
    }

    private void openCategory(int n) {
        this.category = n;
        this.activeCategory = n;
        ++this.entryGeneration;
        this.entries.removeAllElements();
        if (n == 0) {
            this.addProfile();
        } else if (n == 1) {
            this.addField("XU", TerrainMidlet.myInfo, "xu", null, 0, Integer.MAX_VALUE, false, null);
            this.addField("LUONG", TerrainMidlet.myInfo, "luong", null, 0, Integer.MAX_VALUE, false, null);
        } else if (n == 2) {
            this.addCombat();
        } else if (n == 3) {
            this.addInventory(false);
        } else if (n == 4) {
            this.addEquipped();
        } else if (n == 5) {
            this.addInventory(true);
        } else if (n == 6) {
            this.addItems();
        } else if (n == 7) {
            this.addMissions();
        } else if (n == 8) {
            this.addUnlocks();
        } else {
            this.addUtilities();
        }
        if (this.entries.size() == 0) {
            this.entries.addElement(new ActionEntry("CHUA LOAD DU LIEU", new IAction(){

                public void perform() {
                    CCanvas.startOKDlg("Collection chua duoc load");
                }
            }));
        }
        this.resetSelection();
    }

    private void addProfile() {
        PlayerInfo playerInfo = TerrainMidlet.myInfo;
        this.addField("TEN", playerInfo, "name", null, 0, 0, true, null);
        this.addField("CLASS/GUN", playerInfo, "gun", null, 0, this.classCount(playerInfo) - 1, false, null);
        this.addField("EXP", playerInfo, "exp", null, 0, Integer.MAX_VALUE, false, null);
        this.addField("LEVEL", playerInfo, "level2", null, 0, Integer.MAX_VALUE, false, null);
        this.addField("LEVEL %", playerInfo, "level2Percen", null, 0, 100, false, null);
        this.addField("NEXT EXP", playerInfo, "nextExp", null, 0, Integer.MAX_VALUE, false, null);
        this.addField("POINT", playerInfo, "point", null, 0, Short.MAX_VALUE, false, null);
        this.addField("CUP", playerInfo, "cup", null, 0, Integer.MAX_VALUE, false, null);
        this.addArray("CLASS EXP", playerInfo, "classExp", 0, Integer.MAX_VALUE);
        this.addArray("CLASS LEVEL", playerInfo, "classLevel2", 0, Integer.MAX_VALUE);
        this.addArray("CLASS %", playerInfo, "classLevel2Percen", 0, 100);
        this.addArray("CLASS NEXT EXP", playerInfo, "classNextExp", 0, Integer.MAX_VALUE);
        this.addArray("CLASS POINT", playerInfo, "classPoint", 0, Short.MAX_VALUE);
    }

    private void addCombat() {
        PlayerInfo playerInfo = TerrainMidlet.myInfo;
        this.addMatrix("CLASS ABILITY", playerInfo, "classAbility", 0, Short.MAX_VALUE, null);
    }

    private void addInventory(boolean bl) {
        Vector vector = EquipScreen.inventory;
        Vector vector2 = TerrainMidlet.myInfo.itemME;
        EquipScreen.inventory = vector = OfflineEditorScr.ensureInventory(vector);
        this.addCatalogOwnershipActions(bl);
        Vector vector3 = this.validGemIds();
        int[] nArray = this.validAbilityIds();
        if (bl && vector3.size() <= 1) {
            this.entries.addElement(new ActionEntry("GEM CATALOG CHUA LOAD", new IAction(){

                public void perform() {
                    CCanvas.startOKDlg("Chi sua gem tu catalog/template dang load");
                }
            }));
        }
        if (nArray.length <= 1) {
            this.entries.addElement(new ActionEntry("ABILITY CATALOG CHUA LOAD", new IAction(){

                public void perform() {
                    CCanvas.startOKDlg("Ability chi sua tu catalog/template dang load");
                }
            }));
        }
        this.addEquipVector(vector, bl, true, vector3, nArray);
        if (vector2 != vector) {
            this.addEquipVector(vector2, bl, false, vector3, nArray);
        }
    }

    private void addEquipVector(Vector vector, boolean bl, boolean bl2, Vector vector2, int[] nArray) {
        if (vector == null) {
            return;
        }
        for (int i = 0; i < vector.size(); ++i) {
            Object e = vector.elementAt(i);
            if (!(e instanceof Equip)) continue;
            Equip equip = (Equip)e;
            String string = new StringBuilder().append(equip.name == null ? new StringBuilder().append("ID ").append(equip.id).toString() : equip.name).append(" ").toString();
            if (!bl) {
                this.addField(new StringBuilder().append(string).append("SL").toString(), equip, "num", null, 0, Integer.MAX_VALUE, false, null);
                this.addField(new StringBuilder().append(string).append("LEVEL").toString(), equip, "level", null, 0, 127, false, null);
                this.addField(new StringBuilder().append(string).append("LEVEL2").toString(), equip, "level2", null, 0, Integer.MAX_VALUE, false, null);
                this.addField(new StringBuilder().append(string).append("CRAFT").toString(), equip, "craftTier", null, 0, 127, false, null);
            }
            if (vector2.size() > 0) {
                this.addArray(new StringBuilder().append(string).append("SOCKET").toString(), equip, "socketGems", -128, 127, vector2);
            }
            this.addArray(new StringBuilder().append(string).append("ATTR").toString(), equip, "inv_attAddPoint", Short.MIN_VALUE, Short.MAX_VALUE);
            if (nArray.length > 1) {
                for (int j = 0; equip.inv_ability != null && j < equip.inv_ability.length; ++j) {
                    this.entries.addElement(new AbilityEntry(new StringBuilder().append(string).append("ABILITY ").append(j).toString(), equip, j, nArray));
                }
                this.addArray(new StringBuilder().append(string).append("PERCENT").toString(), equip, "inv_percen", 0, 100);
            }
            if (!bl2) continue;
            final Equip equip2 = equip;
            this.entries.addElement(new ActionEntry(new StringBuilder().append("REMOVE ").append(string).toString(), new IAction(){

                @Override
                public void perform() {
                    OfflineEditorScr.this.confirmRemoveOwnedEquip(equip2);
                }
            }));
        }
    }

    private void addCatalogOwnershipActions(boolean bl) {
        int n;
        Vector vector;
        if (!bl && this.addTemplateActions("ADD EQUIP ", vector = OfflineShopEquip.buildShopItems(), false) == 0) {
            this.entries.addElement(new ActionEntry("EQUIP CATALOG CHUA LOAD", new IAction(){

                public void perform() {
                    CCanvas.startOKDlg("Khong add equip khi catalog chua load");
                }
            }));
        }
        if ((n = this.addTemplateActions(bl ? "ADD GEM/MATERIAL " : "ADD MATERIAL ", ShopLinhTinh.items, true)) == 0) {
            n = this.addTemplateActions(bl ? "ADD GEM/MATERIAL " : "ADD MATERIAL ", EquipScreen.inventory, true);
        }
        if (n == 0) {
            this.entries.addElement(new ActionEntry("MATERIAL CATALOG CHUA LOAD", new IAction(){

                public void perform() {
                    CCanvas.startOKDlg("Khong add gem/material khi template chua load");
                }
            }));
        }
    }

    private int addTemplateActions(String string, Vector vector, boolean bl) {
        int n = 0;
        for (int i = 0; vector != null && i < vector.size(); ++i) {
            Object e = vector.elementAt(i);
            if (!(e instanceof Equip)) continue;
            Equip equip = (Equip)e;
            if (bl && !equip.isMaterial) continue;
            final Equip equip2 = equip;
            this.entries.addElement(new ActionEntry(new StringBuilder().append(string).append(OfflineEditorScr.equipLabel(equip)).toString(), new IAction(){

                @Override
                public void perform() {
                    OfflineEditorScr.this.addOwnedEquip(equip2);
                }
            }));
            ++n;
        }
        return n;
    }

    private void addOwnedEquip(Equip equip) {
        if (equip == null || EquipScreen.inventory == null) {
            CCanvas.startOKDlg("Catalog/inventory chua load");
            return;
        }
        Equip equip2 = new Equip();
        equip2.changeToEquip(equip);
        equip2.dbKey = OfflineChest.nextDbKey();
        equip2.num = Math.max(1, equip.num);
        equip2.isMaterial = equip.isMaterial;
        equip2.level = equip.level;
        equip2.level2 = equip.level2;
        equip2.craftTier = equip.craftTier;
        equip2.date = (byte)-1;
        equip2.slot = (byte)equip2.socketCount();
        OfflineChest.ensureDisplayName(equip2);
        EquipScreen.inventory.addElement(equip2);
        this.refreshEquipment();
        this.openCategory(this.category);
    }

    private void confirmRemoveOwnedEquip(final Equip equip) {
        CCanvas.startYesNoDlg(new StringBuilder().append("REMOVE ").append(OfflineEditorScr.equipLabel(equip)).append("?").toString(), new IAction(){

            @Override
            public void perform() {
                OfflineEditorScr.this.removeOwnedEquip(equip);
            }
        });
    }

    private void removeOwnedEquip(Equip equip) {
        if (this.isEquipped(equip)) {
            CCanvas.startOKDlg("Khong the remove trang bi dang mac");
            return;
        }
        EquipScreen.inventory.removeElement(equip);
        this.refreshEquipment();
        this.openCategory(this.category);
    }

    private boolean isEquipped(Equip equip) {
        PlayerInfo playerInfo = TerrainMidlet.myInfo;
        return equip != null && !equip.isMaterial && OfflineEditorScr.isReferencedAnywhere(equip.id, playerInfo.equipID, playerInfo.equipVipID);
    }

    private static String equipLabel(Equip equip) {
        return equip.name == null || equip.name.length() == 0 ? new StringBuilder().append("ID ").append(equip.id).toString() : equip.name;
    }

    private void addEquipped() {
        Vector vector = this.loadedEquipCandidates();
        this.addEquippedArray("EQUIP", TerrainMidlet.myInfo.equipID, vector);
        this.addEquippedArray("VIP", TerrainMidlet.myInfo.equipVipID, vector);
    }

    private void addEquippedArray(String string, short[][] sArray, Vector vector) {
        for (int i = 0; sArray != null && i < sArray.length; ++i) {
            for (int j = 0; sArray[i] != null && j < sArray[i].length; ++j) {
                int[] nArray = OfflineEditorScr.filterEquipIds(vector, i, j);
                if (nArray.length > 0) {
                    this.entries.addElement(new EquipIdEntry(new StringBuilder().append(string).append(" ").append(i).append(".").append(j).toString(), sArray, i, j, nArray));
                    continue;
                }
                final int n = i;
                final int n2 = j;
                this.entries.addElement(new ActionEntry(new StringBuilder().append(string).append(" ").append(i).append(".").append(j).append(" = ").append(sArray[i][j]).append(" (READ ONLY)").toString(), new IAction(){

                    @Override
                    public void perform() {
                        CCanvas.startOKDlg("Khong co equip dung class/slot cho " + n + "." + n2);
                    }
                }));
            }
        }
    }

    private void addItems() {
        int n;
        int[] nArray = this.validItemIds();
        if (nArray.length == 1) {
            this.entries.addElement(new ActionEntry("ITEM CATALOG CHUA LOAD", new IAction(){

                public void perform() {
                    CCanvas.startOKDlg("Loadout chi co the dat ve -2 khi item catalog rong");
                }
            }));
        }
        for (int i = 0; TerrainMidlet.myInfo.itemLoadout != null && i < TerrainMidlet.myInfo.itemLoadout.length; ++i) {
            for (n = 0; TerrainMidlet.myInfo.itemLoadout[i] != null && n < TerrainMidlet.myInfo.itemLoadout[i].length; ++n) {
                this.entries.addElement(new LoadoutEntry(new StringBuilder().append("LOADOUT ").append(i).append(".").append(n).toString(), i, n, nArray));
            }
        }
        this.addItemQuantities();
        this.addArray("SQUAD", TerrainMidlet.myInfo, "squadExtra", -128, 127);
        for (n = 0; n < 5; ++n) {
            this.entries.addElement(new TeamHoursEntry(new StringBuilder().append("TEAM ITEM ").append(n).append(" HOURS").toString(), n, false));
        }
        this.entries.addElement(new TeamHoursEntry("EXP CARD HOURS", 0, true));
    }

    private void addItemQuantities() {
        int n = 0;
        try {
            int n2 = ShopItem.getItemNum().length;
            for (int i = 0; i < n2; ++i) {
                Item item = ShopItem.getI(i);
                if (item == null) continue;
                this.entries.addElement(new ItemQuantityEntry(i));
                ++n;
            }
        }
        catch (RuntimeException runtimeException) {
        }
        if (n == 0) {
            this.entries.addElement(new ActionEntry("ITEM QUANTITY CATALOG CHUA LOAD", new IAction(){

                public void perform() {
                    CCanvas.startOKDlg("Khong co ShopItem runtime de sua quantity");
                }
            }));
        }
    }

    private int[] validItemIds() {
        try {
            Object object;
            int n = ShopItem.getItemNum().length;
            int[] nArray = new int[n];
            int n2 = 0;
            for (int i = 0; i < n; ++i) {
                object = ShopItem.getI(i);
                if (object == null) continue;
                nArray[n2++] = ((Item)object).type;
            }
            object = new int[n2];
            System.arraycopy(nArray, 0, object, 0, n2);
            return OfflineEditorScr.composeLoadoutIds((int[])object);
        }
        catch (RuntimeException runtimeException) {
            return OfflineEditorScr.composeLoadoutIds(null);
        }
    }

    private void addMissions() {
        Class clazz = OfflineEditorScr.owner("com.teamobi.mobiarmy2.OfflineMission");
        this.addArray("PROGRESS", clazz, "progress", 0, Integer.MAX_VALUE);
        this.addArray("CLAIMED", clazz, "claimed", 0, 1);
        this.addField("LOGIN STREAK", clazz, "loginStreak", null, 0, Integer.MAX_VALUE, false, null);
        this.addField("LAST LOGIN YMD", clazz, "lastLoginYmd", null, 0, 99991231, false, null);
    }

    private void addUnlocks() {
        ChangePlayerCSr.ensureGunData();
        int n = Math.min(Math.min(ChangePlayerCSr.isUnlock.length, ChangePlayerCSr.gunXu.length), Math.min(ChangePlayerCSr.gunLuong.length, TerrainMidlet.isVip.length));
        for (int i = 0; i < n; ++i) {
            this.entries.addElement(new ArrayEntry(new StringBuilder().append("GUN ").append(i).append(" UNLOCK").toString(), ChangePlayerCSr.isUnlock, i, 0, 1));
            this.entries.addElement(new ArrayEntry(new StringBuilder().append("GUN ").append(i).append(" XU").toString(), ChangePlayerCSr.gunXu, i, 0, Integer.MAX_VALUE));
            this.entries.addElement(new ArrayEntry(new StringBuilder().append("GUN ").append(i).append(" LUONG").toString(), ChangePlayerCSr.gunLuong, i, 0, Integer.MAX_VALUE));
            this.entries.addElement(new ArrayEntry(new StringBuilder().append("GUN ").append(i).append(" VIP").toString(), TerrainMidlet.isVip, i, 0, 1));
        }
    }

    private void addUtilities() {
        this.entries.addElement(new ActionEntry(new StringBuilder().append("CATEGORY: ").append(CATEGORIES[this.bulkCategory]).toString(), new IAction(){

            public void perform() {
                OfflineEditorScr.this.bulkCategory = (OfflineEditorScr.this.bulkCategory + 1) % 9;
                OfflineEditorScr.this.openCategory(9);
            }
        }));
        this.entries.addElement(new ActionEntry("MAX CATEGORY", new IAction(){

            public void perform() {
                OfflineEditorScr.this.confirmBulk(true);
            }
        }));
        this.entries.addElement(new ActionEntry("RESET CATEGORY", new IAction(){

            public void perform() {
                OfflineEditorScr.this.confirmBulk(false);
            }
        }));
        this.entries.addElement(new ActionEntry("SAVE", new IAction(){

            public void perform() {
                OfflineEditorScr.this.saveAndReload();
            }
        }));
        this.entries.addElement(new ActionEntry("RELOAD", new IAction(){

            public void perform() {
                OfflineEditorScr.this.reload();
            }
        }));
        this.entries.addElement(new ActionEntry("UNDO", new IAction(){

            public void perform() {
                OfflineEditorScr.this.undoSession();
            }
        }));
    }

    private void addField(String string, Object object, String string2, int[] nArray, int n, int n2, boolean bl, Vector vector) {
        try {
            this.entries.addElement(new FieldEntry(string, object, string2, nArray, n, n2, bl, vector));
        }
        catch (RuntimeException runtimeException) {
        }
    }

    private void addArray(String string, Object object, String string2, int n, int n2) {
        this.addArray(string, object, string2, n, n2, null);
    }

    private void addArray(String string, Object object, String string2, int n, int n2, Vector vector) {
        try {
            Field field = object instanceof Class ? ((Class)object).getField(string2) : object.getClass().getField(string2);
            Object object2 = field.get(object instanceof Class ? null : object);
            for (int i = 0; object2 != null && i < Array.getLength(object2); ++i) {
                this.addField(new StringBuilder().append(string).append(" ").append(i).toString(), object, string2, new int[]{i}, n, n2, false, vector);
            }
        }
        catch (Exception exception) {
        }
    }

    private void addMatrix(String string, Object object, String string2, int n, int n2, Vector vector) {
        try {
            Field field = object.getClass().getField(string2);
            Object object2 = field.get(object);
            for (int i = 0; object2 != null && i < Array.getLength(object2); ++i) {
                Object object3 = Array.get(object2, i);
                for (int j = 0; object3 != null && j < Array.getLength(object3); ++j) {
                    this.addField(new StringBuilder().append(string).append(" ").append(i).append(".").append(j).toString(), object, string2, new int[]{i, j}, n, n2, false, vector);
                }
            }
        }
        catch (Exception exception) {
        }
    }

    private Vector loadedEquipCandidates() {
        Vector vector = new Vector();
        try {
            this.appendValues(vector, OfflineShopEquip.buildShopItems());
        }
        catch (RuntimeException runtimeException) {
        }
        this.appendValues(vector, EquipScreen.inventory);
        this.appendValues(vector, TerrainMidlet.myInfo.itemME);
        return vector;
    }

    private void appendValues(Vector vector, Vector vector2) {
        for (int i = 0; vector2 != null && i < vector2.size(); ++i) {
            vector.addElement(vector2.elementAt(i));
        }
    }

    private boolean hasRuntimeInventory() {
        return EquipScreen.inventory != null && EquipScreen.inventory.size() > 0 || TerrainMidlet.myInfo.itemME != null && TerrainMidlet.myInfo.itemME.size() > 0;
    }

    private Vector validGemIds() {
        Vector vector = new Vector();
        this.collectMaterialIds(vector, ShopLinhTinh.items);
        this.collectMaterialIds(vector, EquipScreen.inventory);
        this.collectMaterialIds(vector, TerrainMidlet.myInfo.itemME);
        return OfflineEditorScr.toVector(OfflineEditorScr.composeSocketIds(OfflineEditorScr.filterGemIds(OfflineEditorScr.toIntArray(vector))));
    }

    private void collectMaterialIds(Vector vector, Vector vector2) {
        if (vector2 == null) {
            return;
        }
        for (int i = 0; i < vector2.size(); ++i) {
            Object e = vector2.elementAt(i);
            if (!(e instanceof Equip)) continue;
            Equip equip = (Equip)e;
            if (!equip.isMaterial) continue;
            this.addId(vector, equip.id);
        }
    }

    private int[] validAbilityIds() {
        Vector vector = new Vector();
        this.addId(vector, 0);
        this.collectAbilityIds(vector, OfflineShopEquip.buildShopItems());
        this.collectAbilityIds(vector, ShopLinhTinh.items);
        this.collectAbilityIds(vector, EquipScreen.inventory);
        this.collectAbilityIds(vector, TerrainMidlet.myInfo.itemME);
        return OfflineEditorScr.filterAbilityIds(OfflineEditorScr.toIntArray(vector));
    }

    private void collectAbilityIds(Vector vector, Vector vector2) {
        for (int i = 0; vector2 != null && i < vector2.size(); ++i) {
            Object e = vector2.elementAt(i);
            if (!(e instanceof Equip)) continue;
            Equip equip = (Equip)e;
            this.collectByteIds(vector, equip.inv_ability);
            this.collectByteIds(vector, equip.shop_ability);
        }
    }

    private void collectByteIds(Vector vector, byte[] byArray) {
        for (int i = 0; byArray != null && i < byArray.length; ++i) {
            this.addId(vector, byArray[i]);
        }
    }

    private static int[] toIntArray(Vector vector) {
        int[] nArray = new int[vector.size()];
        for (int i = 0; i < nArray.length; ++i) {
            nArray[i] = (Integer)vector.elementAt(i);
        }
        return nArray;
    }

    private static Vector toVector(int[] nArray) {
        Vector<Integer> vector = new Vector<Integer>();
        for (int i = 0; i < nArray.length; ++i) {
            vector.addElement(new Integer(nArray[i]));
        }
        return vector;
    }

    private void collectArrayIds(Vector vector, short[][] sArray) {
        for (int i = 0; sArray != null && i < sArray.length; ++i) {
            for (int j = 0; sArray[i] != null && j < sArray[i].length; ++j) {
                this.addId(vector, sArray[i][j]);
            }
        }
    }

    private void addId(Vector vector, int n) {
        Integer n2 = new Integer(n);
        if (!vector.contains(n2)) {
            vector.addElement(n2);
        }
    }

    private void collectEquipIds(Vector vector, Vector vector2) {
        if (vector2 == null) {
            return;
        }
        for (int i = 0; i < vector2.size(); ++i) {
            Integer n;
            Object e = vector2.elementAt(i);
            if (!(e instanceof Equip)) continue;
            Equip equip = (Equip)e;
            if (equip.isMaterial || vector.contains(n = new Integer(equip.id))) continue;
            vector.addElement(n);
        }
    }

    private int classCount(PlayerInfo playerInfo) {
        playerInfo.ensureClassProgress();
        return playerInfo.classExp == null || playerInfo.classExp.length == 0 ? 1 : playerInfo.classExp.length;
    }

    private static Class owner(String string) {
        try {
            return Class.forName(string);
        }
        catch (ClassNotFoundException classNotFoundException) {
            throw new IllegalStateException(string);
        }
    }

    private static boolean validName(String string) {
        if (string == null || string.length() == 0 || string.length() > 16) {
            return false;
        }
        for (int i = 0; i < string.length(); ++i) {
            boolean bl;
            char c = string.charAt(i);
            boolean bl2 = bl = c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z' || c >= '0' && c <= '9';
            if (bl || c == '_' || c >= '\u0080') continue;
            return false;
        }
        return true;
    }

    private void editInt(String string, int n, final int n2, final int n3, final IntSetter intSetter) {
        this.input(string, String.valueOf(n), 1, new ValueSetter(){

            @Override
            public void set(String string) {
                intSetter.set(OfflineEditorScr.parseInt(string, n2, n3));
            }
        });
    }

    private void editText(String string, String string2, ValueSetter valueSetter) {
        this.input(string, string2, 0, valueSetter);
    }

    private void input(String string, String string2, int n, final ValueSetter valueSetter) {
        if (CCanvas.inputDlg == null) {
            CCanvas.inputDlg = new InputDlg();
        }
        final InputDlg inputDlg = CCanvas.inputDlg;
        inputDlg.setInfo(string, new IAction(){

            @Override
            public void perform() {
                String string = inputDlg.tfInput.getText();
                OfflineEditorScr.closeInput(inputDlg);
                try {
                    valueSetter.set(string);
                }
                catch (RuntimeException runtimeException) {
                    CCanvas.startOKDlg(runtimeException.getMessage() == null ? "Gia tri khong hop le" : runtimeException.getMessage());
                }
            }
        }, new IAction(){

            @Override
            public void perform() {
                OfflineEditorScr.closeInput(inputDlg);
            }
        }, n);
        inputDlg.tfInput.setText(string2 == null ? "" : string2);
        inputDlg.show();
    }

    private static void closeInput(InputDlg inputDlg) {
        inputDlg.close();
        CCanvas.currentDialog = null;
    }

    private int teamHours(int n, boolean bl) {
        try {
            Class<?> clazz = Class.forName("com.teamobi.mobiarmy2.OfflineTeamItems");
            Method method = bl ? clazz.getMethod("expCardRemainingHours", new Class[0]) : clazz.getMethod("remainingHours", Integer.TYPE);
            Object object = bl ? method.invoke(null, new Object[0]) : method.invoke(null, new Integer(n));
            return (Integer)object;
        }
        catch (Exception exception) {
            return 0;
        }
    }

    private void setTeamHours(int n, boolean bl, int n2) {
        try {
            Method method;
            Class<?> clazz = Class.forName("com.teamobi.mobiarmy2.OfflineTeamItems");
            Method method2 = method = bl ? clazz.getMethod("editorSetExpCardRemainingHours", Integer.TYPE) : clazz.getMethod("editorSetRemainingHours", Integer.TYPE, Integer.TYPE);
            if (bl) {
                method.invoke(null, new Integer(n2));
            } else {
                method.invoke(null, new Integer(n), new Integer(n2));
            }
        }
        catch (Exception exception) {
            throw new IllegalStateException("Bridge team item chua san sang");
        }
    }

    private void afterFieldMutation(String string) {
        if (this.category == 0 && ("exp".equals(string) || "level2".equals(string) || "level2Percen".equals(string) || "nextExp".equals(string) || "point".equals(string))) {
            TerrainMidlet.myInfo.saveCurrentClassProgress();
            this.refreshCombat();
        } else if ("gun".equals(string) || string.startsWith("class") || this.category == 2) {
            this.refreshCombat();
        }
        if (this.category == 3 || this.category == 4 || this.category == 5) {
            this.refreshEquipment();
        }
    }

    private void refreshCombat() {
        PlayerInfo playerInfo = TerrainMidlet.myInfo;
        playerInfo.ensureClassProgress();
        playerInfo.loadCurrentClassProgress();
        playerInfo.getAttribute();
        playerInfo.setAllEquipEffect();
    }

    private void refreshEquipment() {
        PlayerInfo playerInfo = TerrainMidlet.myInfo;
        playerInfo.ensureCombatEquip();
        playerInfo.getMyEquip(0);
        playerInfo.setAllEquipEffect();
        OfflineChest.captureWorn(playerInfo);
    }

    private void confirmBulk(final boolean bl) {
        CCanvas.startYesNoDlg(new StringBuilder().append(bl ? "MAX " : "RESET ").append(CATEGORIES[this.bulkCategory]).append("?").toString(), new IAction(){

            @Override
            public void perform() {
                OfflineEditorScr.this.openCategory(OfflineEditorScr.this.bulkCategory);
                for (int i = 0; i < OfflineEditorScr.this.entries.size(); ++i) {
                    Entry entry = (Entry)OfflineEditorScr.this.entries.elementAt(i);
                    if (bl) {
                        entry.maximize();
                        continue;
                    }
                    entry.reset();
                }
                OfflineEditorScr.this.openCategory(9);
            }
        });
    }

    private void saveAndReload() {
        try {
            OfflineSave.save();
            if (!this.wasLastSaveSuccessful()) {
                throw new IllegalStateException("save");
            }
            byte[] byArray = OfflineSave.exportBytes();
            if (!(this.wasLastSaveSuccessful() && byArray != null && OfflineSave.importBytes(byArray) && OfflineSave.load())) {
                throw new IllegalStateException("verify");
            }
            this.persistedSnapshot = OfflineEditorScr.snapshot(byArray);
            this.rebuildActiveCategory();
            CCanvas.startOKDlg("Da luu");
        }
        catch (Throwable throwable) {
            if (this.restorePersistedSnapshot()) {
                this.rebuildActiveCategory();
                CCanvas.startOKDlg("Luu that bai; da khoi phuc snapshot");
            }
            CCanvas.startOKDlg("FATAL: khong the khoi phuc snapshot");
        }
    }

    private void reload() {
        if (!OfflineSave.load()) {
            if (!this.restorePersistedSnapshot()) {
                CCanvas.startOKDlg("FATAL: khong the khoi phuc snapshot");
                return;
            }
            this.rebuildActiveCategory();
            CCanvas.startOKDlg("Reload that bai; da khoi phuc snapshot");
            return;
        }
        this.rebuildActiveCategory();
    }

    private void undoSession() {
        if (!this.restoreOpeningSnapshot()) {
            CCanvas.startOKDlg("FATAL: khong the khoi phuc snapshot");
            return;
        }
        this.rebuildActiveCategory();
        CCanvas.startOKDlg("Da hoan tac");
    }

    private void rebuildActiveCategory() {
        int n = this.selected;
        if (this.activeCategory < 0) {
            this.openRoot();
        } else {
            this.openCategory(this.activeCategory);
        }
        this.selected = this.entries.size() == 0 ? 0 : OfflineEditorScr.clamp(n, 0, this.entries.size() - 1);
        this.keepVisible();
    }

    private boolean restoreOpeningSnapshot() {
        return this.openingSnapshot != null && OfflineSave.importBytes(OfflineEditorScr.snapshot(this.openingSnapshot)) && OfflineSave.load();
    }

    private boolean restorePersistedSnapshot() {
        byte[] byArray = this.persistedSnapshotForRestore();
        return byArray != null && OfflineSave.importBytes(byArray) && OfflineSave.load();
    }

    private byte[] persistedSnapshotForRestore() {
        return OfflineEditorScr.snapshot(this.persistedSnapshot);
    }

    private boolean wasLastSaveSuccessful() {
        try {
            Method method = OfflineEditorScr.owner("com.teamobi.mobiarmy2.OfflineSave").getMethod("editorWasLastSaveSuccessful", new Class[0]);
            return (Boolean)method.invoke(null, new Object[0]);
        }
        catch (Exception exception) {
            return false;
        }
    }

    private void activate() {
        if (this.entries.size() > 0) {
            ((Entry)this.entries.elementAt(this.selected)).edit();
        }
    }

    private void goBack() {
        if (this.category >= 0) {
            this.openRoot();
        } else if (this.previous != null) {
            this.previous.show();
        }
    }

    private void resetSelection() {
        this.selected = 0;
        this.firstVisible = 0;
    }

    public void update() {
        if (CCanvas.keyPressed[2]) {
            this.selected = this.selected <= 0 ? this.entries.size() - 1 : this.selected - 1;
            this.keepVisible();
            OfflineEditorScr.clearKey();
        } else if (CCanvas.keyPressed[8]) {
            this.selected = this.selected >= this.entries.size() - 1 ? 0 : this.selected + 1;
            this.keepVisible();
            OfflineEditorScr.clearKey();
        }
        super.update();
    }

    private void keepVisible() {
        int n = this.visibleRows();
        if (this.selected < this.firstVisible) {
            this.firstVisible = this.selected;
        } else if (this.selected >= this.firstVisible + n) {
            this.firstVisible = this.selected - n + 1;
        }
    }

    private int visibleRows() {
        return Math.max(1, (CCanvas.hieght - 34 - ITEM_HEIGHT) / 24);
    }

    public void paint(mGraphics mGraphics2) {
        mGraphics2.setColor(7852799);
        mGraphics2.fillRect(0, 0, CCanvas.width, CCanvas.hieght, false);
        Font.bigFont.drawString(mGraphics2, this.category < 0 ? "OFFLINE EDITOR" : CATEGORIES[this.category], CCanvas.width / 2, 8, mGraphics.HCENTER | mGraphics.TOP);
        int n = this.visibleRows();
        int n2 = Math.min(this.entries.size(), this.firstVisible + n);
        for (int i = this.firstVisible; i < n2; ++i) {
            Entry entry = (Entry)this.entries.elementAt(i);
            int n3 = 34 + (i - this.firstVisible) * 24;
            if (i == this.selected) {
                mGraphics2.setColor(3374591);
                mGraphics2.fillRect(8, n3, CCanvas.width - 16, 22, false);
            }
            Font.normalFont.drawString(mGraphics2, entry.label, 14, n3 + 4, mGraphics.LEFT | mGraphics.TOP);
            String string = entry.value();
            if (string.length() <= 0) continue;
            Font.normalYFont.drawString(mGraphics2, string, CCanvas.width - 14, n3 + 4, mGraphics.RIGHT | mGraphics.TOP);
        }
        this.paintCommand(mGraphics2);
    }

    public void onPointerPressed(int n, int n2, int n3) {
        if (n2 >= 34 && n2 < CCanvas.hieght - ITEM_HEIGHT) {
            this.pointerStartY = n2;
            this.pointerStartFirst = this.firstVisible;
            this.pointerRow = this.firstVisible + (n2 - 34) / 24;
            this.pointerMoved = false;
            return;
        }
        super.onPointerPressed(n, n2, n3);
    }

    public void onPointerDragged(int n, int n2, int n3) {
        if (this.pointerRow >= 0) {
            if (Math.abs(n2 - this.pointerStartY) >= 6) {
                this.pointerMoved = true;
            }
            this.firstVisible = OfflineEditorScr.dragFirstVisible(this.pointerStartFirst, n2 - this.pointerStartY, this.entries.size(), this.visibleRows());
            this.selected = OfflineEditorScr.clamp(this.selected, this.firstVisible, Math.min(this.entries.size() - 1, this.firstVisible + this.visibleRows() - 1));
            return;
        }
        super.onPointerDragged(n, n2, n3);
    }

    public void onPointerReleased(int n, int n2, int n3) {
        if (this.pointerRow >= 0) {
            int n4;
            if (!this.pointerMoved && (n4 = this.firstVisible + (n2 - 34) / 24) >= 0 && n4 < this.entries.size()) {
                this.selected = n4;
                this.activate();
            }
            this.pointerRow = -1;
            this.pointerMoved = false;
            return;
        }
        super.onPointerReleased(n, n2, n3);
    }

    private static interface IntSetter {
        public void set(int var1);
    }

    private static interface ValueSetter {
        public void set(String var1);
    }

    private final class ActionEntry
    extends Entry {
        private final IAction action;

        ActionEntry(String string, IAction iAction) {
            super(string);
            this.action = iAction;
        }

        String value() {
            return "";
        }

        void edit() {
            this.action.perform();
        }
    }

    private final class AbilityEntry
    extends Entry {
        private final Equip equip;
        private final int index;
        private final int[] validAbilityIds;

        AbilityEntry(String string, Equip equip, int n, int[] nArray) {
            super(string);
            this.equip = equip;
            this.index = n;
            this.validAbilityIds = nArray;
        }

        String value() {
            return String.valueOf(this.equip.inv_ability[this.index]);
        }

        void edit() {
            this.equip.inv_ability[this.index] = (byte)OfflineEditorScr.cycleExisting(this.equip.inv_ability[this.index], 1, this.validAbilityIds);
            OfflineEditorScr.this.refreshEquipment();
        }

        void maximize() {
            this.equip.inv_ability[this.index] = (byte)this.validAbilityIds[this.validAbilityIds.length - 1];
            OfflineEditorScr.this.refreshEquipment();
        }

        void reset() {
            this.equip.inv_ability[this.index] = 0;
            OfflineEditorScr.this.refreshEquipment();
        }
    }

    private final class EquipIdEntry
    extends Entry {
        private final short[][] values;
        private final int row;
        private final int column;
        private final int[] allowedIds;

        EquipIdEntry(String string, short[][] sArray, int n, int n2, int[] nArray) {
            super(string);
            this.values = sArray;
            this.row = n;
            this.column = n2;
            this.allowedIds = nArray;
        }

        String value() {
            return String.valueOf(this.values[this.row][this.column]);
        }

        void edit() {
            this.values[this.row][this.column] = (short)OfflineEditorScr.cycleExisting(this.values[this.row][this.column], 1, this.allowedIds);
            OfflineEditorScr.this.refreshEquipment();
        }

        void maximize() {
            this.values[this.row][this.column] = (short)this.allowedIds[this.allowedIds.length - 1];
            OfflineEditorScr.this.refreshEquipment();
        }

        void reset() {
            this.values[this.row][this.column] = (short)this.allowedIds[0];
            OfflineEditorScr.this.refreshEquipment();
        }
    }

    private final class LoadoutEntry
    extends Entry {
        private final int row;
        private final int column;
        private final int[] validItemIds;

        LoadoutEntry(String string, int n, int n2, int[] nArray) {
            super(string);
            this.row = n;
            this.column = n2;
            this.validItemIds = nArray;
        }

        String value() {
            return String.valueOf(TerrainMidlet.myInfo.itemLoadout[this.row][this.column]);
        }

        void edit() {
            TerrainMidlet.myInfo.itemLoadout[this.row][this.column] = OfflineEditorScr.cycleExisting(TerrainMidlet.myInfo.itemLoadout[this.row][this.column], 1, this.validItemIds);
        }

        void maximize() {
            TerrainMidlet.myInfo.itemLoadout[this.row][this.column] = this.validItemIds[this.validItemIds.length - 1];
        }

        void reset() {
            OfflineEditorScr.resetLoadoutSlot(TerrainMidlet.myInfo.itemLoadout[this.row], this.column);
        }
    }

    private final class TeamHoursEntry
    extends Entry {
        private final int index;
        private final boolean expCard;

        TeamHoursEntry(String string, int n, boolean bl) {
            super(string);
            this.index = n;
            this.expCard = bl;
        }

        String value() {
            return String.valueOf(OfflineEditorScr.this.teamHours(this.index, this.expCard));
        }

        void edit() {
            OfflineEditorScr.this.editInt(this.label, OfflineEditorScr.this.teamHours(this.index, this.expCard), 0, 8760, new IntSetter(){

                public void set(int n) {
                    OfflineEditorScr.this.setTeamHours(TeamHoursEntry.this.index, TeamHoursEntry.this.expCard, n);
                }
            });
        }

        void maximize() {
            OfflineEditorScr.this.setTeamHours(this.index, this.expCard, 8760);
        }

        void reset() {
            OfflineEditorScr.this.setTeamHours(this.index, this.expCard, 0);
        }
    }

    private final class ItemQuantityEntry
    extends Entry {
        private final int index;

        ItemQuantityEntry(int n) {
            super(new StringBuilder().append("ITEM ").append(n).append(" QUANTITY").toString());
            this.index = n;
        }

        String value() {
            return String.valueOf(ShopItem.getI((int)this.index).num);
        }

        void edit() {
            final Item item = ShopItem.getI(this.index);
            OfflineEditorScr.this.editInt(this.label, item.num, 0, 127, new IntSetter(){

                @Override
                public void set(int n) {
                    item.num = (byte)n;
                }
            });
        }

        void maximize() {
            ShopItem.getI((int)this.index).num = (byte)127;
        }

        void reset() {
            ShopItem.getI((int)this.index).num = 0;
        }
    }

    private final class ArrayEntry
    extends Entry {
        private final Object array;
        private final int index;
        private final int minimum;
        private final int maximum;

        ArrayEntry(String string, Object object, int n, int n2, int n3) {
            super(string);
            this.array = object;
            this.index = n;
            this.minimum = n2;
            this.maximum = n3;
        }

        String value() {
            return String.valueOf(Array.get(this.array, this.index));
        }

        void edit() {
            Object object = Array.get(this.array, this.index);
            if (object instanceof Boolean) {
                Array.setBoolean(this.array, this.index, (Boolean)object == false);
                return;
            }
            OfflineEditorScr.this.editInt(this.label, ((Number)object).intValue(), this.minimum, this.maximum, new IntSetter(){

                public void set(int n) {
                    ArrayEntry.this.write(n);
                }
            });
        }

        void maximize() {
            if (Array.get(this.array, this.index) instanceof Boolean) {
                Array.setBoolean(this.array, this.index, true);
            } else {
                this.write(this.maximum);
            }
        }

        void reset() {
            if (Array.get(this.array, this.index) instanceof Boolean) {
                Array.setBoolean(this.array, this.index, false);
            } else {
                this.write(OfflineEditorScr.clamp(0, this.minimum, this.maximum));
            }
        }

        private void write(int n) {
            Class<?> clazz = this.array.getClass().getComponentType();
            if (clazz == Byte.TYPE) {
                Array.setByte(this.array, this.index, (byte)n);
            } else {
                Array.setInt(this.array, this.index, n);
            }
        }
    }

    private final class FieldEntry
    extends Entry {
        private final Object target;
        private final String fieldName;
        private final int[] indexes;
        private final int minimum;
        private final int maximum;
        private final boolean text;
        private final Vector allowedIds;

        FieldEntry(String string, Object object, String string2, int[] nArray, int n, int n2, boolean bl, Vector vector) {
            super(string);
            this.target = object;
            this.fieldName = string2;
            this.indexes = nArray;
            this.minimum = n;
            this.maximum = n2;
            this.text = bl;
            this.allowedIds = vector;
        }

        String value() {
            Object object = this.read();
            return object == null ? "-" : String.valueOf(object);
        }

        void edit() {
            if (this.text) {
                OfflineEditorScr.this.editText(this.label, this.value(), new ValueSetter(){

                    public void set(String string) {
                        if ("name".equals(FieldEntry.this.fieldName) && !OfflineEditorScr.validName(string)) {
                            throw new IllegalArgumentException("Ten phai co 1-16 ky tu");
                        }
                        FieldEntry.this.write(string);
                        OfflineEditorScr.this.afterFieldMutation(FieldEntry.this.fieldName);
                    }
                });
                return;
            }
            Object object = this.read();
            if (object instanceof Boolean) {
                this.write((Boolean)object == false);
                OfflineEditorScr.this.afterFieldMutation(this.fieldName);
                return;
            }
            OfflineEditorScr.this.editInt(this.label, ((Number)object).intValue(), this.minimum, this.maximum, new IntSetter(){

                public void set(int n) {
                    if ("lastLoginYmd".equals(FieldEntry.this.fieldName) && !OfflineEditorScr.isValidYmd(n)) {
                        throw new IllegalArgumentException("Ngay YYYYMMDD khong hop le");
                    }
                    if ("inv_percen".equals(FieldEntry.this.fieldName)) {
                        n = OfflineEditorScr.validatePercent(n);
                    }
                    if (FieldEntry.this.allowedIds != null && !FieldEntry.this.allowedIds.contains(new Integer(n))) {
                        throw new IllegalArgumentException("ID chua duoc load");
                    }
                    if (FieldEntry.this.target == TerrainMidlet.myInfo && "gun".equals(FieldEntry.this.fieldName)) {
                        TerrainMidlet.myInfo.switchGunProgress((byte)n);
                        OfflineEditorScr.this.refreshCombat();
                        return;
                    }
                    FieldEntry.this.writeNumber(n);
                    OfflineEditorScr.this.afterFieldMutation(FieldEntry.this.fieldName);
                }
            });
        }

        void maximize() {
            Object object = this.read();
            if (object instanceof Boolean) {
                this.write(Boolean.TRUE);
            } else if (!this.text) {
                this.writeNumber(this.allowedIds == null ? this.maximum : (Integer)this.allowedIds.lastElement());
            }
            OfflineEditorScr.this.afterFieldMutation(this.fieldName);
        }

        void reset() {
            Object object = this.read();
            if (object instanceof Boolean) {
                this.write(Boolean.FALSE);
            } else if (!this.text) {
                this.writeNumber(OfflineEditorScr.resetAllowedId(this.allowedIds, this.minimum, this.maximum));
            }
            OfflineEditorScr.this.afterFieldMutation(this.fieldName);
        }

        private Object read() {
            try {
                Object object = this.field().get(this.staticTarget());
                for (int i = 0; this.indexes != null && i < this.indexes.length; ++i) {
                    object = Array.get(object, this.indexes[i]);
                }
                return object;
            }
            catch (Exception exception) {
                throw new IllegalStateException(exception.toString());
            }
        }

        private void write(Object object) {
            try {
                Field field = this.field();
                if (this.indexes == null || this.indexes.length == 0) {
                    field.set(this.staticTarget(), object);
                    return;
                }
                Object object2 = field.get(this.staticTarget());
                for (int i = 0; i < this.indexes.length - 1; ++i) {
                    object2 = Array.get(object2, this.indexes[i]);
                }
                Array.set(object2, this.indexes[this.indexes.length - 1], object);
            }
            catch (Exception exception) {
                throw new IllegalStateException(exception.toString());
            }
        }

        private void writeNumber(int n) {
            Object object = this.read();
            if (object instanceof Byte) {
                this.write(new Byte((byte)n));
            } else if (object instanceof Short) {
                this.write(new Short((short)n));
            } else {
                this.write(new Integer(n));
            }
        }

        private Object staticTarget() {
            return this.target instanceof Class ? null : this.target;
        }

        private Field field() throws Exception {
            Class<?> clazz = this.target instanceof Class ? (Class<?>)this.target : this.target.getClass();
            return clazz.getField(this.fieldName);
        }
    }

    private abstract class Entry {
        final String label;

        Entry(String string) {
            this.label = string;
        }

        abstract String value();

        abstract void edit();

        void maximize() {
        }

        void reset() {
        }
    }
}

