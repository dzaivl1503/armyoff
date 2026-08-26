/*
 * Decompiled with CFR 0.152.
 */
package shop;

import CLib.LibSysTem;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;
import java.util.Vector;
import model.CRes;

public final class ShopEquipCatalog {
    private static final Hashtable catalogByKey = new Hashtable();
    private static final Vector allEntries = new Vector();
    private static boolean loaded;

    private ShopEquipCatalog() {
    }

    public static void ensureLoaded() {
        if (loaded) {
            return;
        }
        loaded = true;
        ShopEquipCatalog.loadFromAssets();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private static void loadFromAssets() {
        InputStream inputStream = null;
        try {
            inputStream = LibSysTem.openResource("/res/shop_equipment.txt");
            if (inputStream == null) {
                CRes.out("ShopEquipCatalog: missing res/shop_equipment.txt");
                return;
            }
            String string = ShopEquipCatalog.readAll(inputStream);
            int n = 0;
            int n2 = string.length();
            while (n < n2) {
                int n3 = string.indexOf(10, n);
                String string2 = n3 < 0 ? string.substring(n) : string.substring(n, n3);
                ShopEquipCatalog.parseLine(string2);
                if (n3 < 0) break;
                n = n3 + 1;
            }
            CRes.out("ShopEquipCatalog: loaded " + allEntries.size() + " items");
        }
        catch (Exception exception) {
            CRes.out("ShopEquipCatalog load error: " + exception.getMessage());
        }
        finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                }
                catch (IOException iOException) {}
            }
        }
    }

    private static String readAll(InputStream inputStream) throws IOException {
        int n;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] byArray = new byte[512];
        while ((n = inputStream.read(byArray)) != -1) {
            byteArrayOutputStream.write(byArray, 0, n);
        }
        return new String(byteArrayOutputStream.toByteArray(), "UTF-8");
    }

    private static void parseLine(String string) {
        String string2 = string.trim();
        if (string2.length() == 0 || string2.charAt(0) == '#') {
            return;
        }
        String[] stringArray = CRes.split(string2, "\t");
        if (stringArray.length < 5) {
            return;
        }
        Entry entry = new Entry();
        entry.glass = (byte)Integer.parseInt(stringArray[0].trim());
        entry.equipId = (short)Integer.parseInt(stringArray[1].trim());
        entry.name = stringArray[2].trim();
        entry.xu = Integer.parseInt(stringArray[3].trim());
        entry.luong = Integer.parseInt(stringArray[4].trim());
        catalogByKey.put(ShopEquipCatalog.key(entry.glass, entry.equipId), entry);
        allEntries.addElement(entry);
    }

    public static String key(byte by, short s) {
        return by + "_" + s;
    }

    public static Entry get(byte by, short s) {
        ShopEquipCatalog.ensureLoaded();
        return (Entry)catalogByKey.get(ShopEquipCatalog.key(by, s));
    }

    public static String resolveName(byte by, short s) {
        Entry entry = ShopEquipCatalog.get(by, s);
        if (entry != null && entry.name != null && entry.name.length() > 0) {
            return entry.name;
        }
        return "Trang b\u1ecb " + s;
    }

    public static Vector getEntriesForGlass(byte by) {
        ShopEquipCatalog.ensureLoaded();
        Vector<Entry> vector = new Vector<Entry>();
        for (int i = 0; i < allEntries.size(); ++i) {
            Entry entry = (Entry)allEntries.elementAt(i);
            if (entry.glass != by) continue;
            vector.addElement(entry);
        }
        return vector;
    }

    public static final class Entry {
        public byte glass;
        public short equipId;
        public String name;
        public int xu;
        public int luong;
    }
}

