/*
 * Decompiled with CFR 0.152.
 */
package Equipment;

import Equipment.TypeEquip;
import java.util.Vector;

public class EquipGlass {
    public EquipGlass(int n) {
        this((byte)n);
    }

    public byte glassID;
    public Vector type;
    public short maxDamage;

    public EquipGlass(byte by) {
        this.glassID = by;
    }

    public void addType(Vector vector) {
        this.type = vector;
    }

    public TypeEquip getType(int n) {
        for (int i = 0; i < this.type.size(); ++i) {
            TypeEquip typeEquip = (TypeEquip)this.type.elementAt(i);
            if (typeEquip == null || typeEquip.typeID != n) continue;
            return typeEquip;
        }
        return null;
    }
}

