/*
 * Decompiled with CFR 0.152.
 */
package model;

public class RoomInfo {
    public byte id;
    public byte roomFree;
    public byte roomWait;
    public byte boardID;
    public String[] roomName;
    public byte lv;
    public byte mapID;
    public int stat;
    public String name = "";
    public String playerMax = "";
    public int money;

    public RoomInfo(byte by, byte by2, byte by3, byte by4) {
        this.id = by;
        this.roomFree = by2;
        this.roomWait = by3;
        this.lv = by4;
    }

    public void getStat() {
        int n;
        int n2 = this.roomFree + this.roomWait;
        if (n2 == 0) {
            n2 = 1;
        }
        if ((n = this.roomFree / n2) >= 0 && n <= 0) {
            this.stat = 2;
        } else if (n > 0 && n <= 0) {
            this.stat = 1;
        } else if (n > 0 && n <= 1) {
            this.stat = 0;
        }
    }

    public RoomInfo() {
    }
}

