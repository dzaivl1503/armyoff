/*
 * Decompiled with CFR 0.152.
 */
package model;

public class Position {
    public int x;
    public int y;
    public int xF;
    public int yF;
    public int xT;
    public int yT;
    int frame;

    public Position(int n, int n2) {
        this.x = n;
        this.y = n2;
    }

    public Position(int n, int n2, int n3) {
        this.x = n;
        this.y = n2;
        this.frame = n3;
    }

    public Position(int n, int n2, int n3, int n4) {
        this.xF = n;
        this.yF = n2;
        this.xT = n3;
        this.yT = n4;
    }
}

