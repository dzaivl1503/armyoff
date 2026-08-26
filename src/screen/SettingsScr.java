package screen;

import CLib.mGraphics;
import com.teamobi.mobiarmy2.OfflineSettings;
import coreLG.CCanvas;
import effect.Cloud;
import model.Font;
import model.IAction;
import model.Language;
import network.Command;
import screen.CScreen;

public class SettingsScr extends CScreen {
    private CScreen lastScr;

    public SettingsScr() {
        this.nameCScreen = " SettingsScr screen!";
        this.center = new Command("Ch\u1ec9nh", new IAction() {
            public void perform() {
                SettingsScr.this.openPopup();
            }
        });
        this.left = new Command("Nh\u1eadp S\u1ed1", new IAction() {
            public void perform() {
                SettingsScr.this.openPopup();
            }
        });
        this.right = new Command(Language.back(), new IAction() {
            public void perform() {
                SettingsScr.this.goBack();
            }
        });
    }

    public void show(CScreen cScreen) {
        this.lastScr = cScreen;
        super.show();
        this.openPopup();
    }

    private void openPopup() {
        OfflineSettings.openSpeedInputPopup(new IAction() {
            public void perform() {
                SettingsScr.this.goBack();
            }
        });
    }

    private void goBack() {
        if (this.lastScr != null) {
            this.lastScr.show();
        } else if (CCanvas.menuScr != null) {
            CCanvas.menuScr.show();
        }
    }

    public void update() {
        if (CCanvas.keyPressed[5] || CCanvas.keyPressed[12]) {
            this.openPopup();
            CScreen.clearKey();
        }
        if (CCanvas.keyPressed[13]) {
            this.goBack();
            CScreen.clearKey();
        }
        super.update();
    }

    public void paint(mGraphics g) {
        g.setColor(0x0a192f);
        g.fillRect(0, 0, CCanvas.width, CCanvas.hieght, false);
        Cloud.paintCloud(g);

        int panelW = Math.min(CCanvas.width - 24, 280);
        int panelH = 120;
        int panelX = (CCanvas.width - panelW) / 2;
        int panelY = (CCanvas.hieght - panelH) / 2 - 10;

        g.setColor(0x102a45);
        g.fillRoundRect(panelX, panelY, panelW, panelH, 12, 12, false);
        g.setColor(0x00d2ff);
        g.drawRoundRect(panelX, panelY, panelW, panelH, 12, 12, false);

        Font.bigFont.drawString(g, "C\u00c0I \u0110\u1eb6T T\u1ed0C \u0110\u1ed8", CCanvas.hw, panelY + 12, 2);
        Font.borderFont.drawString(g, "\u0110\u1ed9 tr\u1ec5 hi\u1ec7n t\u1ea1i: " + OfflineSettings.getCurrentSpeedLabel(), CCanvas.hw, panelY + 45, 2);
        Font.smallFontYellow.drawString(g, "(S\u1ed1 ms c\u00e0ng nh\u1ecf -> Game c\u00e0ng nhanh)", CCanvas.hw, panelY + 70, 2);
        Font.smallFont.drawString(g, "Nh\u1ea5p chu\u1ed9t ho\u1eb7c b\u1ea5m Ch\u1ecdn \u0111\u1ec3 nh\u1eadp s\u1ed1", CCanvas.hw, panelY + 92, 2);

        super.paintCommand(g);
    }

    public void onPointerPressed(int x, int y, int index) {
        if (CCanvas.isPointer(0, 0, CCanvas.width, CCanvas.hieght, index)) {
            this.openPopup();
        }
        super.onPointerPressed(x, y, index);
    }
}
