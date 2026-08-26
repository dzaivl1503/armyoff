/*
 * Decompiled with CFR 0.152.
 */
package screen;

import CLib.mGraphics;
import coreLG.CCanvas;
import effect.Cloud;
import model.Font;
import model.IAction;
import model.Language;
import network.Command;
import network.RelayRoomInfo;
import network.RelayService;
import screen.CScreen;

public class RelayLobbyScr
extends CScreen {
    int selected;
    private int listTop;

    public RelayLobbyScr() {
        this.right = new Command(Language.back(), new IAction(){

            public void perform() {
                RelayService.quit();
                CCanvas.menuScr.show();
            }
        });
        this.left = new Command("T\u1ea1o ph\u00f2ng", new IAction(){

            public void perform() {
                RelayService.createRoom();
            }
        });
        this.center = new Command("V\u00e0o ph\u00f2ng", new IAction(){

            public void perform() {
                RelayLobbyScr.this.joinSelected();
            }
        });
        this.nameCScreen = "RelayLobbyScr";
    }

    void joinSelected() {
        if (this.selected >= 0 && this.selected < RelayService.rooms.size()) {
            RelayRoomInfo relayRoomInfo = (RelayRoomInfo)RelayService.rooms.elementAt(this.selected);
            RelayService.joinRoom(relayRoomInfo.id);
        }
    }

    public void onRoomsChanged() {
        if (this.selected >= RelayService.rooms.size()) {
            this.selected = RelayService.rooms.size() == 0 ? 0 : RelayService.rooms.size() - 1;
        }
    }

    public void show() {
        super.show();
        RelayService.requestRoomList();
    }

    public void update() {
        Cloud.updateCloud();
        if (!RelayService.isActive() && CCanvas.menuScr != null) {
            CCanvas.menuScr.show();
            return;
        }
        if (RelayService.state == 3 && CCanvas.prepareScr != null) {
            CCanvas.prepareScr.show();
            return;
        }
        super.update();
    }

    public void paint(mGraphics mGraphics2) {
        mGraphics2.setClip(0, 0, CCanvas.width, CCanvas.hieght);
        RelayLobbyScr.paintDefaultBg(mGraphics2);
        Cloud.paintCloud(mGraphics2);
        Font.bigFont.drawString(mGraphics2, "MULTIPLAYER", CCanvas.width / 2, 8, mGraphics.HCENTER | mGraphics.TOP);
        int n = RelayService.rooms.size();
        int n2 = 8 + Font.bigFont.getHeight() + 10;
        Font.normalYFont.drawString(mGraphics2, "Danh s\u00e1ch ph\u00f2ng (" + n + ")", CCanvas.width / 2, n2, mGraphics.HCENTER | mGraphics.TOP);
        int n3 = this.listTop = n2 + Font.normalYFont.getHeight() + 10;
        if (n == 0) {
            Font.normalFont.drawString(mGraphics2, "Ch\u01b0a c\u00f3 ph\u00f2ng n\u00e0o.", CCanvas.width / 2, n3 + 10, 2);
            Font.normalFont.drawString(mGraphics2, "B\u1ea5m 'T\u1ea1o ph\u00f2ng' \u0111\u1ec3 m\u1edf ph\u00f2ng m\u1edbi.", CCanvas.width / 2, n3 + 30, 2);
        } else {
            for (int i = 0; i < n; ++i) {
                RelayRoomInfo relayRoomInfo = (RelayRoomInfo)RelayService.rooms.elementAt(i);
                if (i == this.selected) {
                    mGraphics2.setColor(16765440);
                    mGraphics2.fillRect(0, n3 - 2, CCanvas.width, ITEM_HEIGHT * 2, true);
                }
                Font.borderFont.drawString(mGraphics2, relayRoomInfo.name + "  (" + relayRoomInfo.cur + "/" + relayRoomInfo.max + ")", 7, n3, 0);
                Font.normalFont.drawString(mGraphics2, (relayRoomInfo.inBattle ? "\u0110ang \u0111\u00e1nh - " : "\u0110ang ch\u1edd - ") + "ch\u1ee7 ph\u00f2ng: " + relayRoomInfo.hostName, 7, n3 + Font.borderFont.getHeight() + 1, 0);
                n3 += ITEM_HEIGHT * 2;
            }
        }
        super.paint(mGraphics2);
    }

    public void onPointerPressed(int n, int n2, int n3) {
        int n4;
        super.onPointerPressed(n, n2, n3);
        int n5 = RelayService.rooms.size();
        if (n5 > 0 && (CCanvas.keyPressed[2] || CCanvas.keyPressed[8])) {
            if (CCanvas.keyPressed[8]) {
                ++this.selected;
            }
            if (CCanvas.keyPressed[2]) {
                --this.selected;
            }
            if (this.selected < 0) {
                this.selected = n5 - 1;
            }
            if (this.selected >= n5) {
                this.selected = 0;
            }
            CScreen.clearKey();
            return;
        }
        if (n5 > 0 && n2 >= this.listTop && (n4 = (n2 - this.listTop) / (ITEM_HEIGHT * 2)) >= 0 && n4 < n5) {
            this.selected = n4;
        }
    }
}

