/*
 * Decompiled with CFR 0.152.
 */
package Equipment;

import Equipment.Equip;
import java.util.Vector;

public class TypeEquip {
    public TypeEquip(int n) {
        this((byte)n);
    }

    public static final byte SUNG = 0;
    public static final byte NON = 1;
    public static final byte GIAP = 2;
    public static final byte KINH = 3;
    public static final byte CANH = 4;
    public static final byte TRANGPHUC = 5;
    public byte typeID;
    public Vector equip;

    public TypeEquip(byte by) {
        this.typeID = by;
    }

    public void addEquip(Vector vector) {
        this.equip = vector;
    }

    public Equip getEquip(short s) {
        for (int i = 0; i < this.equip.size(); ++i) {
            Equip equip = (Equip)this.equip.elementAt(i);
            if (equip == null || equip.id != s) continue;
            return equip;
        }
        return null;
    }
}

