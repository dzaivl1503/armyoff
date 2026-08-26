/*
 * Mobi Army 2 Main Menu Screen
 */
package screen;

import CLib.mGraphics;
import com.teamobi.mobiarmy2.CloudSaveApi;
import com.teamobi.mobiarmy2.GameMidlet;
import com.teamobi.mobiarmy2.OfflineSave;
import coreLG.CCanvas;
import coreLG.TerrainMidlet;
import effect.Cloud;
import model.Font;
import model.IAction;
import model.IAction2;
import model.Language;
import network.Command;
import screen.CScreen;
import screen.CloudLoginScr;
import screen.SettingsScr;

public class MainMenuScr extends CScreen {
    private static final String[] LABELS = new String[]{
        "CHƠI TIẾP",
        "ĐĂNG NHẬP / CLOUD",
        "LƯU DỮ LIỆU",
        "CÀI ĐẶT",
        "THOAT"
    };
    private static final String NO_SAVE_MSG = "Chưa có dữ liệu lưu. Hãy chọn Đăng Nhập / Cloud hoặc Chơi Tiếp để tạo nhân vật.";
    private int select;

    public MainMenuScr() {
        this.nameCScreen = " MainMenuScr screen!";
        this.select = 0;
        this.center = new Command(Language.select(), new IAction() {
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
            this.doCloudLogin();
            return;
        }
        if (this.select == 2) {
            this.doSaveAndSync();
            return;
        }
        if (this.select == 3) {
            this.doSettings();
            return;
        }
        this.doExit();
    }

    private void doCloudLogin() {
        CloudLoginScr scr = new CloudLoginScr();
        scr.show(this);
    }

    private void doSaveAndSync() {
        CCanvas.startWaitDlg("Đang lưu RMS và đồng bộ lên Server...");
        CloudSaveApi.manualSaveAndSync(new IAction2() {
            public void perform(Object obj) {
                CCanvas.endDlg();
                CloudSaveApi.Result res = (CloudSaveApi.Result) obj;
                if (res != null) {
                    CCanvas.startOKDlg(res.error);
                }
            }
        });
    }

    private void doSettings() {
        if (CCanvas.settingsScr == null) {
            CCanvas.settingsScr = new SettingsScr();
        }
        CCanvas.settingsScr.show(this);
    }

    private void doContinue() {
        if (!OfflineSave.hasSave()) {
            if (CloudSaveApi.isLoggedIn()) {
                String email = CloudSaveApi.getLinkedEmail();
                GameMidlet.startNewOfflineGame(email != null && email.length() > 0 ? email : "Chiến Binh");
            } else {
                GameMidlet.startNewOfflineGame("Chiến Binh");
            }
        }
        if (!GameMidlet.continueOfflineGame()) {
            GameMidlet.startNewOfflineGame("Chiến Binh");
        }
        // Background sync config
        CloudSaveApi.fetchRemoteConfig(null);
        GameMidlet.enterOfflineMenu();
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
        int n = 34;
        int n2 = (LABELS.length - 1) * n + 22;
        int n3 = (CCanvas.hieght - n2) / 2;
        for (int i = 0; i < LABELS.length; ++i) {
            if (i == this.select) {
                mGraphics2.setColor(3374591);
                mGraphics2.fillRect(CCanvas.width / 2 - 95, n3, 190, 22, false);
            }
            Font.bigFont.drawString(mGraphics2, LABELS[i], CCanvas.width / 2, n3, mGraphics.HCENTER | mGraphics.TOP);
            mGraphics2.setColor(0);
            n3 += n;
        }
        super.paint(mGraphics2);
    }

    public void onPointerPressed(int n, int n2, int n3) {
        int n4 = (LABELS.length - 1) * 34 + 22;
        int n5 = (CCanvas.hieght - n4) / 2;
        int n6 = (n2 - n5) / 34;
        if (n6 >= 0 && n6 < LABELS.length) {
            this.select = n6;
            this.doSelect();
            return;
        }
        super.onPointerPressed(n, n2, n3);
    }
}
