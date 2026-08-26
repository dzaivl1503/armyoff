/*
 * Decompiled with CFR 0.152.
 */
package screen;

import CLib.mGraphics;
import com.teamobi.mobiarmy2.GameMidlet;
import com.teamobi.mobiarmy2.OfflineSave;
import coreLG.CCanvas;
import coreLG.TerrainMidlet;
import effect.Cloud;
import model.Font;
import model.IAction;
import model.Language;
import network.Command;
import screen.CScreen;
import screen.OfflineEditorScr;
import screen.SettingsScr;

public class MainMenuScr
extends CScreen {
    private static final String[] LABELS = new String[]{"CH\u01a0I TI\u1ebeP", "CH\u01a0I M\u1edaI", "C\u00c0I \u0110\u1eb6T", "EDITOR", "THOAT"};
    private static final String NEW_GAME_MSG = "B\u1ea1n mu\u1ed1n ch\u01a1i m\u1edbi kh\u00f4ng?";
    private static final String NO_SAVE_MSG = "Ch\u01b0a c\u00f3 d\u1eef li\u1ec7u l\u01b0u. H\u00e3y ch\u1ecdn Ch\u01a1i m\u1edbi \u0111\u1ec3 t\u1ea1o nh\u00e2n v\u1eadt.";
    private static final String INVALID_NAME_MSG = "T\u00ean ph\u1ea3i c\u00f3 t\u1eeb 1 \u0111\u1ebfn 16 k\u00fd t\u1ef1, ch\u1ec9 g\u1ed3m ch\u1eef, s\u1ed1 ho\u1eb7c d\u1ea5u g\u1ea1ch d\u01b0\u1edbi.";
    private int select;
    private static final String ENTER_NAME_MSG = "Nh\u1eadp t\u00ean nh\u00e2n v\u1eadt";

    public MainMenuScr() {
        this.nameCScreen = " MainMenuScr screen!";
        this.select = 0;
        this.center = new Command(Language.select(), new IAction(){

            public void perform() {
                MainMenuScr.this.doSelect();
            }
        });
    }

    public void show() {
        this.select = 0;
        super.show();
    }

    private void doSelect() {
        if (this.select == 0) {
            this.doContinue();
            return;
        }
        if (this.select == 1) {
            this.doNewGame();
            return;
        }
        if (this.select == 2) {
            this.doSettings();
            return;
        }
        if (this.select == 3) {
            OfflineEditorScr.open(this);
            return;
        }
        this.doExit();
    }

    private void doSettings() {
        if (CCanvas.settingsScr == null) {
            CCanvas.settingsScr = new SettingsScr();
        }
        CCanvas.settingsScr.show(this);
    }

    private void doContinue() {
        if (!OfflineSave.hasSave()) {
            this.showNoSaveDialog();
            return;
        }
        if (!GameMidlet.continueOfflineGame()) {
            this.showNoSaveDialog();
            return;
        }
        GameMidlet.enterOfflineMenu();
    }

    private void showNoSaveDialog() {
        CCanvas.startOKDlg(NO_SAVE_MSG);
    }

    private void doNewGame() {
        CCanvas.startYesNoDlg(NEW_GAME_MSG, new IAction(){

            public void perform() {
                CCanvas.endDlg();
                MainMenuScr.this.showNameInput("");
            }
        }, new IAction(){

            public void perform() {
                CCanvas.endDlg();
            }
        });
    }

    private void showNameInput(String string) {
        String string2 = string == null ? "" : string;
        CCanvas.inputDlg.setInfo(ENTER_NAME_MSG, new IAction(){

            public void perform() {
                String string = CCanvas.inputDlg.tfInput.getText();
                String string2 = string = string == null ? "" : string.trim();
                if (!MainMenuScr.this.isValidPlayerName(string)) {
                    final String string3 = string;
                    CCanvas.endDlg();
                    CCanvas.startOKDlg(MainMenuScr.INVALID_NAME_MSG, new IAction(){

                        public void perform() {
                            MainMenuScr.this.showNameInput(string3);
                        }
                    });
                    return;
                }
                CCanvas.endDlg();
                GameMidlet.startNewOfflineGame(string);
                GameMidlet.enterOfflineMenu();
            }
        }, null, 3);
        CCanvas.inputDlg.tfInput.setMaxTextLenght(16);
        CCanvas.inputDlg.tfInput.setText(string2);
        CCanvas.inputDlg.tfInput.title = ENTER_NAME_MSG;
        CCanvas.inputDlg.show();
        if (CCanvas.isTouch) {
            CCanvas.inputDlg.tfInput.doChangeToTextBox();
        }
    }

    private boolean isValidPlayerName(String string) {
        if (string == null || string.length() < 1 || string.length() > 16) {
            return false;
        }
        for (int i = 0; i < string.length(); ++i) {
            boolean bl;
            char c = string.charAt(i);
            boolean bl2 = bl = c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z' || c >= '0' && c <= '9';
            if (bl || c == '_' || c >= '\u0080') continue;
            return false;
        }
        return true;
    }

    private void doExit() {
        TerrainMidlet.exit();
    }

    public void update() {
        if (CCanvas.keyPressed[2]) {
            this.select = this.select > 0 ? this.select - 1 : LABELS.length - 1;
            CScreen.clearKey();
        }
        if (CCanvas.keyPressed[8]) {
            this.select = this.select < LABELS.length - 1 ? this.select + 1 : 0;
            CScreen.clearKey();
        }
        super.update();
    }

    public void paint(mGraphics mGraphics2) {
        mGraphics2.setColor(7852799);
        mGraphics2.fillRect(0, 0, CCanvas.width, CCanvas.hieght, false);
        Cloud.paintCloud(mGraphics2);
        int n = 36;
        int n2 = (LABELS.length - 1) * n + 22;
        int n3 = (CCanvas.hieght - n2) / 2;
        for (int i = 0; i < LABELS.length; ++i) {
            if (i == this.select) {
                mGraphics2.setColor(3374591);
                mGraphics2.fillRect(CCanvas.width / 2 - 90, n3, 180, 22, false);
            }
            Font.bigFont.drawString(mGraphics2, LABELS[i], CCanvas.width / 2, n3, mGraphics.HCENTER | mGraphics.TOP);
            mGraphics2.setColor(0);
            n3 += n;
        }
        super.paint(mGraphics2);
    }

    public void onPointerPressed(int n, int n2, int n3) {
        int n4 = (LABELS.length - 1) * 36 + 22;
        int n5 = (CCanvas.hieght - n4) / 2;
        int n6 = (n2 - n5) / 36;
        if (n6 >= 0 && n6 < LABELS.length) {
            this.select = n6;
            this.doSelect();
            return;
        }
        super.onPointerPressed(n, n2, n3);
    }
}

