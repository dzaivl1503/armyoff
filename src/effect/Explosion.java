/*
 * Decompiled with CFR 0.152.
 */
package effect;

import CLib.Image;
import CLib.mGraphics;
import CLib.mImage;
import coreLG.CCanvas;
import coreLG.CONFIG;
import effect.Smoke;
import item.Item;
import model.CRes;
import model.FilePack;
import model.FrameImage;
import model.IAction2;
import player.PM;
import screen.GameScr;

public class Explosion {
    public Explosion(int n, int n2, int by) {
        this(n, n2, (byte)by);
    }

    public Explosion(int n, int n2, int by, byte by2, byte by3) {
        this(n, n2, (byte)by, by2, by3);
    }

    public Explosion(int n, int n2, int by, byte by2, int by3) {
        this(n, n2, (byte)by, by2, (byte)by3);
    }

    public Explosion(int n, int n2, int by, int by2, int by3) {
        this(n, n2, (byte)by, (byte)by2, (byte)by3);
    }

    public static mImage explode;
    public static mImage teleport;
    public static mImage waterBum;
    public static mImage itemEffImg;
    public static mImage invisibleEff;
    public static mImage imgNuke;
    public static mImage electric;
    public static mImage electric2;
    public static mImage freeze;
    public static mImage dongbang;
    public static mImage khoidoc;
    public static mImage timeBomb;
    static FilePack filePack;
    static mImage imgRain;
    static mImage poison_attack;
    FrameImage frmImg;
    int x;
    int y;
    int endY;
    int wE;
    int delay;
    int frameCountDelay;
    int curFrame = 0;
    public byte whoUseEffect;
    public byte itemID;
    int[][] array;
    public static final byte EXPLODE_NORMAL = 0;
    public static final byte EXPLORE_NORMAL_2 = 11;
    public static final byte TELEPORT = 1;
    public static final byte WATER_BUM = 2;
    public static final byte RAIN_EFFECT = 6;
    public static final byte ITEM_USE_EFFECT = 3;
    public static final byte ITEM_BLINK = 4;
    public static final byte ITEM_ICON_FLY = 5;
    public static final byte NUKE = 7;
    public static final byte ELECTRIC = 8;
    public static final byte ELECTRIC_2 = 9;
    public static final byte POISON = 10;
    public static final byte EYE_SMOKE = 12;
    public static final byte SUICIDE = 13;
    public static final byte FREEZE = 14;
    public static final byte POSION_SMOKE = 15;
    public byte type;
    int maxDelay = 1;

    public Explosion(int n, int n2, byte by) {
        this.x = n;
        this.y = n2;
        this.type = by;
        switch (by) {
            case 0:
            case 13: {
                this.frmImg = new FrameImage(Explosion.explode.image, 59, 64);
                break;
            }
            case 1: {
                this.frmImg = new FrameImage(Explosion.teleport.image, 32, 32);
                break;
            }
            case 2: {
                this.frmImg = new FrameImage(Explosion.waterBum.image, 32, 48);
            }
            default: {
                break;
            }
            case 6: {
                this.frmImg = new FrameImage(Explosion.imgRain.image, 13, 7);
                break;
            }
            case 7: {
                this.frmImg = new FrameImage(Explosion.imgNuke.image, 48, 62);
                break;
            }
            case 8: {
                this.frmImg = new FrameImage(Explosion.electric.image, 59, 64);
                break;
            }
            case 9: {
                this.frmImg = new FrameImage(Explosion.electric2.image, 59, 64);
                break;
            }
            case 10: {
                this.frmImg = new FrameImage(Explosion.poison_attack.image, 12, 12);
                break;
            }
            case 11: {
                this.frmImg = new FrameImage(Smoke.smokeNuke.image, 27, 27);
                break;
            }
            case 14: {
                this.frmImg = new FrameImage(Explosion.freeze.image, 40, 40);
                break;
            }
            case 15: {
                this.frmImg = new FrameImage(Explosion.khoidoc.image, 32, 32);
            }
        }
        GameScr.exs.addElement(this);
    }

    public Explosion(int n, int n2, byte by, byte by2, byte by3) {
        this.x = n;
        this.y = n2;
        this.type = by;
        this.itemID = by3;
        this.whoUseEffect = by2;
        if (by == 3 && itemEffImg != null && Explosion.itemEffImg.image != null) {
            this.frmImg = new FrameImage(Explosion.itemEffImg.image, 40, 40);
        } else if (by == 5) {
            this.y -= 24;
            this.endY = this.y - 50;
        }
        if (by3 == 4 && invisibleEff != null && Explosion.invisibleEff.image != null) {
            this.frmImg = new FrameImage(Explosion.invisibleEff.image, 24, 32);
        }
        if (by3 == 34 && invisibleEff != null && Explosion.invisibleEff.image != null) {
            this.frmImg = new FrameImage(Explosion.invisibleEff.image, 24, 32);
        }
        GameScr.exs.addElement(this);
    }

    public void update() {
        switch (this.type) {
            case 0:
            case 11:
            case 13: {
                if (this.curFrame != 5) break;
                GameScr.exs.removeElement(this);
                return;
            }
            case 1: {
                if (this.curFrame != 4) break;
                GameScr.exs.removeElement(this);
                return;
            }
            case 2: {
                if (this.curFrame != 4) break;
                GameScr.exs.removeElement(this);
                return;
            }
            case 3: {
                if (this.isValidEffectPlayer()) {
                    this.x = PM.p[this.whoUseEffect].x;
                    this.y = PM.p[this.whoUseEffect].y;
                }
                if (this.frmImg != null && this.curFrame != 4) break;
                GameScr.exs.removeElement(this);
                return;
            }
            case 4: {
                if (this.isValidEffectPlayer()) {
                    this.x = PM.p[this.whoUseEffect].x;
                    this.y = PM.p[this.whoUseEffect].y - 13;
                    if (this.itemID != 4 && this.itemID != 34) {
                        if (this.itemID != 3 || PM.p[this.whoUseEffect].isRunSpeed) break;
                        GameScr.exs.removeElement(this);
                        return;
                    }
                    if (!PM.p[this.whoUseEffect].isInvisible) {
                        GameScr.exs.removeElement(this);
                        return;
                    }
                    if (this.curFrame != 4) break;
                    this.curFrame = 0;
                    break;
                }
                GameScr.exs.removeElement(this);
                return;
            }
            case 5: {
                if (!this.isValidEffectPlayer()) {
                    GameScr.exs.removeElement(this);
                    return;
                }
                this.x = PM.p[this.whoUseEffect].x;
                --this.y;
                if (this.y >= this.endY) break;
                GameScr.exs.removeElement(this);
                return;
            }
            case 6: {
                if (this.curFrame != 2) break;
                GameScr.exs.removeElement(this);
                break;
            }
            case 7: {
                if (this.curFrame == 3) {
                    GameScr.exs.removeElement(this);
                    return;
                }
                this.maxDelay = 3;
                break;
            }
            case 8:
            case 9: {
                this.maxDelay = 2;
                if (this.curFrame != 7) break;
                GameScr.exs.removeElement(this);
                return;
            }
            case 10: {
                if (this.curFrame == 3) {
                    GameScr.exs.removeElement(this);
                    return;
                }
            }
            default: {
                break;
            }
            case 14: {
                this.maxDelay = 2;
                if (this.curFrame != 4) break;
                GameScr.exs.removeElement(this);
                break;
            }
            case 15: {
                this.maxDelay = 2;
                if (this.curFrame != 4) break;
                GameScr.exs.removeElement(this);
            }
        }
        ++this.delay;
        if (this.delay > this.maxDelay) {
            ++this.curFrame;
            this.delay = 0;
        }
    }

    public void paint(mGraphics mGraphics2) {
        switch (this.type) {
            case 0:
            case 1:
            case 3:
            case 7:
            case 8:
            case 9:
            case 11:
            case 14:
            case 15: {
                if (this.frmImg == null) break;
                this.frmImg.drawFrame(this.curFrame, this.x, this.y - 12, 0, mGraphics.HCENTER | mGraphics.VCENTER, mGraphics2);
                break;
            }
            case 2: {
                if (this.frmImg == null) break;
                this.frmImg.drawFrame(this.curFrame, this.x, this.y + 6, 0, mGraphics.HCENTER | mGraphics.BOTTOM, mGraphics2);
                break;
            }
            case 4: {
                if (!this.isValidEffectPlayer()) break;
                if (this.itemID != 4 && this.itemID != 34) {
                    if (PM.p[this.whoUseEffect].isInvisible || CCanvas.gameTick % 10 <= 2) break;
                    Item.DrawItem(mGraphics2, this.itemID, this.x - 8, this.y - 37);
                    break;
                }
                if (PM.p[this.whoUseEffect].team != PM.p[GameScr.myIndex].team && this.whoUseEffect != GameScr.myIndex) break;
                if (this.frmImg != null) {
                    this.frmImg.drawFrame(this.curFrame, this.x, this.y - 2, 0, mGraphics.HCENTER | mGraphics.VCENTER, mGraphics2);
                }
                if (CCanvas.gameTick % 10 <= 2) break;
                Item.DrawItem(mGraphics2, this.itemID, this.x - 8, this.y - 37);
                break;
            }
            case 5: {
                Item.DrawItem(mGraphics2, this.itemID, this.x - 8, this.y);
                break;
            }
            case 6: {
                this.frmImg.drawFrame(this.curFrame, this.x, this.y - 5, 0, mGraphics.HCENTER | mGraphics.VCENTER, mGraphics2);
                break;
            }
            case 10: {
                this.frmImg.drawFrame(this.curFrame, this.x, this.y - 6, 0, 3, mGraphics2);
                break;
            }
            case 12: {
                mGraphics2.setColor(0xFFFFFF);
                this.wE += 60;
                if (this.wE <= 1000) break;
                this.x = 0;
                this.wE = 0;
                GameScr.exs.removeElement(this);
                break;
            }
            case 13: {
                this.frmImg.drawFrame(this.curFrame, this.x, this.y - 12, 0, mGraphics.HCENTER | mGraphics.VCENTER, mGraphics2);
                this.frmImg.drawFrame(this.curFrame, this.x + 10, this.y - 12 + 10, 0, mGraphics.HCENTER | mGraphics.VCENTER, mGraphics2);
                this.frmImg.drawFrame(this.curFrame, this.x - 10, this.y - 12 + 10, 0, mGraphics.HCENTER | mGraphics.VCENTER, mGraphics2);
                this.frmImg.drawFrame(this.curFrame, this.x + 10, this.y - 12 - 10, 0, mGraphics.HCENTER | mGraphics.VCENTER, mGraphics2);
                this.frmImg.drawFrame(this.curFrame, this.x - 10, this.y - 12 - 10, 0, mGraphics.HCENTER | mGraphics.VCENTER, mGraphics2);
                this.frmImg.drawFrame(this.curFrame, this.x, this.y - 12 + 20, 0, mGraphics.HCENTER | mGraphics.VCENTER, mGraphics2);
                this.frmImg.drawFrame(this.curFrame, this.x, this.y - 12 - 20, 0, mGraphics.HCENTER | mGraphics.VCENTER, mGraphics2);
                this.frmImg.drawFrame(this.curFrame, this.x + 20, this.y - 12, 0, mGraphics.HCENTER | mGraphics.VCENTER, mGraphics2);
                this.frmImg.drawFrame(this.curFrame, this.x - 20, this.y - 12, 0, mGraphics.HCENTER | mGraphics.VCENTER, mGraphics2);
            }
        }
    }

    private boolean isValidEffectPlayer() {
        return PM.p != null && this.whoUseEffect >= 0 && this.whoUseEffect < PM.p.length && PM.p[this.whoUseEffect] != null;
    }

    static {
        try {
            filePack = new FilePack(CCanvas.getClassPathConfig(CONFIG.PATH_EFFECT + "effect"));
            explode = filePack.loadImage("ex3.png", new IAction2(){

                public void perform(Object object) {
                    CRes.onSaveToFile((Image)object, "ex3", true);
                }
            });
            teleport = filePack.loadImage("teleport_eff.png", new IAction2(){

                public void perform(Object object) {
                    CRes.onSaveToFile((Image)object, "teleport_eff", true);
                }
            });
            waterBum = filePack.loadImage("waterBum.png", new IAction2(){

                public void perform(Object object) {
                    CRes.onSaveToFile((Image)object, "waterBum", true);
                }
            });
            itemEffImg = filePack.loadImage("Eff.png", new IAction2(){

                public void perform(Object object) {
                    CRes.onSaveToFile((Image)object, "Eff", true);
                }
            });
            invisibleEff = filePack.loadImage("tangHinhEffect.png", new IAction2(){

                public void perform(Object object) {
                    CRes.onSaveToFile((Image)object, "tangHinhEffect", true);
                }
            });
            imgRain = filePack.loadImage("mua.png", new IAction2(){

                public void perform(Object object) {
                    CRes.onSaveToFile((Image)object, "mua", true);
                }
            });
            imgNuke = filePack.loadImage("bomnguyentu.png", new IAction2(){

                public void perform(Object object) {
                    CRes.onSaveToFile((Image)object, "bomnguyentu", true);
                }
            });
            electric = filePack.loadImage("electric.png", new IAction2(){

                public void perform(Object object) {
                    CRes.onSaveToFile((Image)object, "electric", true);
                }
            });
            electric2 = filePack.loadImage("electric2.png", new IAction2(){

                public void perform(Object object) {
                    CRes.onSaveToFile((Image)object, "electric2", true);
                }
            });
            freeze = filePack.loadImage("freeze.png", new IAction2(){

                public void perform(Object object) {
                    CRes.onSaveToFile((Image)object, "freeze", true);
                }
            });
            dongbang = filePack.loadImage("dongbang.png", new IAction2(){

                public void perform(Object object) {
                    CRes.onSaveToFile((Image)object, "dongbang", true);
                }
            });
            khoidoc = filePack.loadImage("khoidoc.png", new IAction2(){

                public void perform(Object object) {
                    CRes.onSaveToFile((Image)object, "khoidoc", true);
                }
            });
            timeBomb = filePack.loadImage("thuocno.png", new IAction2(){

                public void perform(Object object) {
                    CRes.onSaveToFile((Image)object, "thuocno", true);
                }
            });
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
        filePack = null;
        try {
            poison_attack = mImage.createImage("/effect/poison-attack.png");
        }
        catch (Exception exception) {
        }
    }
}

