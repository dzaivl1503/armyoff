/*
 * Decompiled with CFR 0.152.
 */
package screen;

import CLib.mGraphics;
import com.teamobi.mobiarmy2.OfflineMission;
import coreLG.CCanvas;
import effect.Cloud;
import java.util.Vector;
import model.Font;
import model.IAction;
import model.Language;
import model.Mission;
import network.Command;
import screen.CScreen;
import screen.PrepareScr;

public class MissionScreen
extends CScreen {
    public Vector mission = new Vector();
    public int cmtoY;
    public int cmy;
    public int cmdy;
    public int cmvy;
    public int cmyLim;
    int selected;
    int disY;
    int pa = 0;
    boolean trans = false;
    int pxFirst;

    public MissionScreen() {
        this.right = new Command(Language.back(), new IAction(){

            public void perform() {
                CCanvas.menuScr.show();
            }
        });
        this.nameCScreen = " MissionScreen screen!";
    }

    public void show() {
        super.show();
    }

    public Mission getCurrMiss() {
        return this.selected >= 0 && this.selected < this.mission.size() ? (Mission)this.mission.elementAt(this.selected) : null;
    }

    private int listVisibleH() {
        return CCanvas.hieght - 25 - 21 - ITEM_HEIGHT;
    }

    public void paintList(mGraphics mGraphics2) {
        mGraphics2.translate(0, ITEM_HEIGHT + 25);
        mGraphics2.setClip(0, 1, CCanvas.width, this.listVisibleH());
        int n = -this.cmy;
        for (int i = 0; i < this.mission.size(); ++i) {
            if (i == this.selected) {
                mGraphics2.setColor(16765440);
                mGraphics2.fillRect(0, n, CCanvas.width, this.disY, true);
            }
            if (n + this.disY > 0 && n < this.listVisibleH()) {
                Mission mission = (Mission)this.mission.elementAt(i);
                Font.borderFont.drawString(mGraphics2, mission.name, 7, n + 10, 0);
                mGraphics2.setColor(1521982);
                mGraphics2.fillRect(5, n + 26, 102, 17, true);
                mGraphics2.setColor(2378093);
                mGraphics2.fillRect(6, n + 26 + 1, 100, 15, true);
                int n2 = mission.have * 100 / mission.require;
                mGraphics2.setColor(16767817);
                mGraphics2.fillRect(6, n + 26 + 1, n2, 15, true);
                Font.borderFont.drawString(mGraphics2, mission.have + "/" + mission.require, 56, n + 26, 2);
                Font.borderFont.drawString(mGraphics2, mission.reward, 7, n + 43, 0);
                if (!mission.isComplete) {
                    mGraphics2.drawImage(yes, CCanvas.width - 20, n + 32, 3, true);
                } else {
                    mGraphics2.drawImage(no, CCanvas.width - 20, n + 32, 3, true);
                }
            }
            n += this.disY;
        }
        mGraphics2.setClip(0, 0, 1000, 1000);
        mGraphics2.translate(0, -mGraphics2.getTranslateY());
    }

    public void paint(mGraphics mGraphics2) {
        mGraphics2.setClip(0, 0, CCanvas.width, CCanvas.hieght);
        MissionScreen.paintDefaultBg(mGraphics2);
        Cloud.paintCloud(mGraphics2);
        for (int i = 0; i <= CCanvas.width; i += 32) {
            mGraphics2.drawImage(PrepareScr.imgBack, i, CCanvas.hieght - 62, 0, false);
        }
        mGraphics2.setColor(1407674);
        mGraphics2.fillRect(0, 25, CCanvas.width, ITEM_HEIGHT, false);
        Font.bigFont.drawString(mGraphics2, Language.mission().toUpperCase(), CCanvas.width / 2, 3, mGraphics.HCENTER | mGraphics.TOP);
        Font.normalYFont.drawString(mGraphics2, Language.mission(), 10, 28, 0);
        Font.normalYFont.drawString(mGraphics2, Language.missionComplete(), CCanvas.width - 10, 28, 1);
        this.paintList(mGraphics2);
        super.paint(mGraphics2);
    }

    public void moveCamera() {
        if (this.cmy != this.cmtoY) {
            this.cmvy = this.cmtoY - this.cmy >> 2;
            this.cmy += this.cmvy;
        }
    }

    public void update() {
        this.moveCamera();
        Cloud.updateCloud();
        super.update();
    }

    public void getCommand() {
        final Mission mission = this.getCurrMiss();
        this.center = mission != null && this.getCurrMiss().isComplete && !this.getCurrMiss().isGetReward ? new Command(Language.nhanthuong(), new IAction(){

            public void perform() {
                OfflineMission.claimReward(mission.id, mission.level);
                MissionScreen.this.getCommand();
            }
        }) : null;
    }

    public void onPointerDragged(int n, int n2, int n3) {
        super.onPointerDragged(n, n2, n3);
        if (!this.trans) {
            this.pa = this.cmy;
            this.trans = true;
        }
        this.cmtoY = this.pa + (CCanvas.pyFirst[n3] - n2);
        if (this.cmtoY > this.cmyLim) {
            this.cmtoY = this.cmyLim;
        }
        if (this.cmtoY < 0) {
            this.cmtoY = 0;
        }
    }

    public void onPointerPressed(int n, int n2, int n3) {
        int n4;
        super.onPointerPressed(n, n2, n3);
        if (this.mission.size() > 0 && (CCanvas.keyPressed[2] || CCanvas.keyPressed[8])) {
            if (CCanvas.keyPressed[8]) {
                ++this.selected;
            }
            if (CCanvas.keyPressed[2]) {
                --this.selected;
            }
            if (this.selected < 0) {
                this.selected = this.mission.size() - 1;
            }
            if (this.selected >= this.mission.size()) {
                this.selected = 0;
            }
            this.cmtoY = this.selected * this.disY - this.listVisibleH() / 2;
            if (this.cmtoY < 0) {
                this.cmtoY = 0;
            }
            if (this.cmtoY > this.cmyLim) {
                this.cmtoY = this.cmyLim;
            }
            this.getCommand();
            CScreen.clearKey();
            return;
        }
        int n5 = 2 * ITEM_HEIGHT;
        this.selected = n4 = (this.cmtoY + n2 - n5) / this.disY;
        if (this.selected < 0) {
            this.selected = 0;
        }
        if (this.selected >= this.mission.size()) {
            this.selected = this.mission.size() == 0 ? 0 : this.mission.size() - 1;
        }
        this.getCommand();
    }

    public void onPointerReleased(int n, int n2, int n3) {
        super.onPointerReleased(n, n2, n3);
        this.trans = false;
        if (CCanvas.isPointer(0, 0, w, CCanvas.hieght - cmdH, n3)) {
            int n4 = 2 * ITEM_HEIGHT;
            int n5 = (this.cmtoY + n2 - n4) / this.disY;
            if (n5 == this.selected) {
                if (this.center != null) {
                    if (CCanvas.isDoubleClick) {
                        this.center.action.perform();
                    }
                } else if (this.left != null && CCanvas.isDoubleClick) {
                    this.left.action.perform();
                }
            }
            this.selected = n5;
            if (this.selected < 0) {
                this.selected = 0;
            }
            if (this.selected >= this.mission.size()) {
                this.selected = this.mission.size() == 0 ? 0 : this.mission.size() - 1;
            }
        }
    }

    public void onPointerHolder(int n, int n2, int n3) {
    }

    public void setMission(Vector vector) {
        this.mission = vector == null ? new Vector() : vector;
        this.selected = 0;
        this.cmtoY = 0;
        this.cmy = 0;
        this.disY = 65;
        this.cmyLim = this.mission.size() * this.disY - this.listVisibleH() + 10;
        if (this.cmyLim < 0) {
            this.cmyLim = 0;
        }
        this.getCommand();
    }
}

