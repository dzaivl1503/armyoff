/*
 * Decompiled with CFR 0.152.
 */
package screen;

import CLib.RMS;
import CLib.mGraphics;
import CLib.mImage;
import CLib.mSound;
import coreLG.CCanvas;
import effect.Cloud;
import map.CMap;
import model.CRes;
import model.Font;
import model.IAction;
import model.Language;
import network.Command;
import screen.CScreen;
import screen.GameScr;
import screen.PrepareScr;

public class ConfigScr
extends CScreen {
    public static int vibrate;
    public int selected;
    public static mImage[] imgTick;
    public int[] level;
    boolean curDrawRGBType = CMap.isDrawRGB;
    public static String[] graphicText;

    public ConfigScr() {
        this.level = new int[2];
        this.center = new Command("OK", new IAction(){

            public void perform() {
                CCanvas.menuScr.show();
            }
        });
        this.nameCScreen = "ConfigScr screen!";
    }

    public void show() {
        this.level[0] = CRes.loadRMSInt("sound") / 10;
        this.level[1] = CRes.loadRMSInt("vibrate");
        if (GameScr.curGRAPHIC_LEVEL == -1) {
            GameScr.curGRAPHIC_LEVEL = 1;
        }
        this.curDrawRGBType = CRes.loadRMSInt("drawRGB") == 0;
        super.show();
    }

    public void paint(mGraphics mGraphics2) {
        int n;
        int n2;
        mGraphics2.setClip(0, 0, CCanvas.width, CCanvas.hieght);
        ConfigScr.paintDefaultBg(mGraphics2);
        Cloud.paintCloud(mGraphics2);
        for (n2 = 0; n2 <= CCanvas.width; n2 += 32) {
            mGraphics2.drawImage(PrepareScr.imgBack, n2, CCanvas.hieght - 62, 0, false);
        }
        Font.bigFont.drawString(mGraphics2, Language.option(), w >> 1, 5, 2);
        n2 = CCanvas.hw - 50;
        int n3 = CCanvas.hh - 60;
        if (CCanvas.gameTick % 10 > 2) {
            Font.borderFont.drawString(mGraphics2, "$", n2 - 15, n3 + 14 + 33 * this.selected, 0);
            Font.borderFont.drawString(mGraphics2, "#", n2 + 103, n3 + 14 + 33 * this.selected, 0);
        }
        Font.borderFont.drawString(mGraphics2, Language.amthanh() + ":", n2, n3, 0);
        n3 += ITEM_HEIGHT;
        for (n = 0; n < 10; ++n) {
            mGraphics2.drawImage(imgTick[n < this.level[0] ? 0 : 1], n2 + n * 10, n3, 0, false);
        }
        Font.borderFont.drawString(mGraphics2, Language.vibrate() + ":", n2, n3 += 14, 0);
        n3 += ITEM_HEIGHT;
        for (n = 0; n < 10; ++n) {
            mGraphics2.drawImage(imgTick[n < this.level[1] ? 0 : 1], n2 + n * 10, n3, 0, false);
        }
        Font.borderFont.drawString(mGraphics2, Language.imageQuality() + ":", n2, n3 + 13, 0);
        Font.borderFont.drawString(mGraphics2, graphicText[GameScr.curGRAPHIC_LEVEL], CCanvas.hw, n3 + 29, 2);
        Font.borderFont.drawString(mGraphics2, Language.graphicQuality() + ":", n2, n3 + 48, 0);
        Font.borderFont.drawString(mGraphics2, this.curDrawRGBType ? Language.macdinh() : Language.khac(), CCanvas.hw, n3 + 62, 2);
        super.paint(mGraphics2);
    }

    public void onPointerDragged(int n, int n2, int n3) {
        super.onPointerDragged(n, n2, n3);
    }

    public void onPointerPressed(int n, int n2, int n3) {
        super.onPointerPressed(n, n2, n3);
    }

    public void onPointerReleased(int n, int n2, int n3) {
        super.onPointerReleased(n, n2, n3);
        int n4 = CCanvas.hw - 50;
        int n5 = CCanvas.hh - 60;
        int n6 = (n2 - n5) / 33;
        if (this.selected != n6) {
            this.selected = n6;
        } else {
            if (CCanvas.isPointer(CCanvas.width / 2 - 100, 0, 100, CCanvas.hieght, n3)) {
                CCanvas.keyPressed[4] = true;
            }
            if (CCanvas.isPointer(CCanvas.width / 2, 0, 100, CCanvas.hieght, n3)) {
                CCanvas.keyPressed[6] = true;
            }
        }
        if (this.selected > 3) {
            this.selected = 0;
        } else if (this.selected < 0) {
            this.selected = 3;
        }
        boolean bl = false;
        if (!CCanvas.keyPressed[4] && !keyLeft) {
            if (CCanvas.keyPressed[6] || keyRight) {
                bl = true;
                CCanvas.keyPressed[6] = false;
                keyRight = false;
                if (this.selected == 2) {
                    if ((GameScr.curGRAPHIC_LEVEL = (byte)(GameScr.curGRAPHIC_LEVEL - 1)) < 0) {
                        GameScr.curGRAPHIC_LEVEL = 1;
                    }
                } else if (this.selected == 3) {
                    this.curDrawRGBType = !this.curDrawRGBType;
                } else {
                    int n7 = this.selected;
                    int n8 = this.level[n7];
                    this.level[n7] = n8 + 1;
                    int n9 = n8;
                    if (this.level[this.selected] > 10) {
                        this.level[this.selected] = 10;
                    }
                }
            }
        } else {
            bl = true;
            CCanvas.keyPressed[4] = false;
            keyLeft = false;
            if (this.selected == 2) {
                if ((GameScr.curGRAPHIC_LEVEL = (byte)(GameScr.curGRAPHIC_LEVEL + 1)) > 1) {
                    GameScr.curGRAPHIC_LEVEL = 0;
                }
            } else if (this.selected == 3) {
                this.curDrawRGBType = !this.curDrawRGBType;
            } else {
                int n10 = this.selected;
                int n11 = this.level[n10];
                this.level[n10] = n11 - 1;
                int n12 = n11;
                if (this.level[this.selected] < 0) {
                    this.level[this.selected] = 1;
                }
            }
        }
        if (bl) {
            bl = false;
            if (this.selected == 0) {
                mSound.setVolume(this.level[this.selected] * 10);
            } else if (this.selected == 1) {
                CRes.saveRMSInt("vibrate", this.level[this.selected]);
                vibrate = this.level[this.selected];
            } else {
                this.saveGraphicAndDrawRGB_RMS();
            }
        }
    }

    private void saveGraphicAndDrawRGB_RMS() {
        RMS.saveRMSInt("Graphic", GameScr.curGRAPHIC_LEVEL);
        CMap.isDrawRGB = this.curDrawRGBType;
        CRes.saveRMSInt("drawRGB", this.curDrawRGBType ? 0 : 1);
        if (GameScr.curGRAPHIC_LEVEL != 2) {
            GameScr.mm.createBackGround();
        } else {
            GameScr.mm.clearBackGround();
        }
    }

    public void update() {
        super.update();
        Cloud.updateCloud();
    }

    static {
        try {
            imgTick = new mImage[2];
            ConfigScr.imgTick[0] = mImage.createImage("/tick0.png");
            ConfigScr.imgTick[1] = mImage.createImage("/tick1.png");
            vibrate = CRes.loadRMSInt("vibrate");
            if (CRes.loadRMSInt("vibrate") == -1) {
                vibrate = 10;
                CRes.saveRMSInt("vibrate", 10);
            } else {
                mSound.volumeSound = CRes.loadRMSInt("vibrate");
            }
            if (CRes.loadRMSInt("sound") != -1) {
                mSound.volumeSound = (float)CRes.loadRMSInt("sound") / 100.0f;
            } else {
                CRes.saveRMSInt("sound", 100);
                mSound.volumeSound = 1.0f;
            }
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
        graphicText = Language.quality();
    }
}

