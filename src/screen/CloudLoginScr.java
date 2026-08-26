/*
 * Decompiled with CFR 0.152.
 */
package screen;

import CLib.mGraphics;
import com.teamobi.mobiarmy2.CloudSaveApi;
import com.teamobi.mobiarmy2.OfflineMission;
import com.teamobi.mobiarmy2.OfflineSave;
import coreLG.CCanvas;
import coreLG.TerrainMidlet;
import effect.Cloud;
import java.util.Vector;
import model.Font;
import model.IAction;
import model.IAction2;
import model.Language;
import model.TField;
import network.Command;
import screen.CScreen;

public class CloudLoginScr
extends CScreen {
    private static final int ROW_H = 16;
    private TField tUser;
    private TField tPass;
    private int focus;
    private CScreen lastScr;
    private boolean linked;
    private int nTab;
    private int boxTop;
    private int boxH;
    private int boxW;
    private Vector saves;
    private boolean savesLoading;
    private int selectedSave = -1;

    public CloudLoginScr() {
        this.nameCScreen = " CloudLoginScr screen!";
        this.refreshState();
    }

    public void show(CScreen cScreen) {
        this.lastScr = cScreen;
        this.refreshState();
        super.show();
    }

    private void refreshState() {
        this.linked = CloudSaveApi.isLoggedIn();
        this.nTab = CCanvas.width >= 200 ? 4 : 3;
        int n = 30;
        int n2 = 20;
        if (this.linked) {
            this.nTab += 2;
            this.boxW = this.nTab * 32 + 56;
            this.tUser = null;
            this.tPass = null;
            int n3 = 96;
            this.boxH = n + n3 + n2;
            this.boxTop = CCanvas.hieght / 2 - this.boxH / 2;
            this.center = new Command("Tu\u1ef3 ch\u1ecdn", new IAction(){

                public void perform() {
                    CloudLoginScr.this.openOptionsMenu();
                }
            });
            this.left = null;
            this.right = new Command(Language.back(), new IAction(){

                public void perform() {
                    CloudLoginScr.this.lastScr.show();
                }
            });
            this.loadSaves();
        } else {
            this.boxW = this.nTab * 32 + 56;
            int n4 = this.boxW - 70;
            if (n4 < 70) {
                n4 = 70;
            }
            int n5 = CCanvas.width / 2 - n4 / 2;
            int n6 = ITEM_HEIGHT + 2;
            int n7 = 14;
            int n8 = 12;
            int n9 = n7 + n6 + n8 + n7 + n6;
            this.boxH = n + n9 + n2;
            this.boxTop = CCanvas.hieght / 2 - this.boxH / 2;
            int n10 = this.boxTop + n;
            this.tUser = new TField();
            this.tUser.x = n5;
            this.tUser.y = n10 + n7;
            this.tUser.width = n4;
            this.tUser.height = n6;
            this.tUser.setIputType(0);
            this.tUser.title = "Email";
            n10 = this.tUser.y + n6 + n8;
            this.tPass = new TField();
            this.tPass.x = n5;
            this.tPass.y = n10 + n7;
            this.tPass.width = n4;
            this.tPass.height = n6;
            this.tPass.setIputType(2);
            this.tPass.title = Language.pass();
            this.center = new Command(Language.signIn(), new IAction(){

                public void perform() {
                    CloudLoginScr.this.doLogin();
                }
            });
            this.focus = 0;
            this.focusUpdate();
            this.left = new Command(Language.back(), new IAction(){

                public void perform() {
                    CloudLoginScr.this.lastScr.show();
                }
            });
        }
    }

    private void focusUpdate() {
        this.tUser.setisFocus(this.focus == 0);
        this.tPass.setisFocus(this.focus == 1);
        this.right = this.focus == 0 ? this.tUser.cmdClear : this.tPass.cmdClear;
    }

    private void doLogin() {
        if (this.tUser.getText().length() == 0 || this.tPass.getText().length() == 0) {
            CCanvas.startOKDlg("Vui l\u00f2ng nh\u1eadp \u0111\u1ee7 email v\u00e0 m\u1eadt kh\u1ea9u.");
            return;
        }
        String string = this.tUser.getText();
        String string2 = this.tPass.getText();
        CCanvas.startWaitDlg(Language.pleaseWait());
        CloudSaveApi.login(string, string2, new IAction2(){

            public void perform(Object object) {
                CCanvas.endDlg();
                CloudSaveApi.Result result = (CloudSaveApi.Result)object;
                if (!result.ok) {
                    CCanvas.startOKDlg(result.error);
                    return;
                }
                OfflineMission.onCloudAccountLinked();
                CloudLoginScr.this.refreshState();
            }
        });
    }

    private void loadSaves() {
        this.savesLoading = true;
        CloudSaveApi.listSaves(new IAction2(){

            public void perform(Object object) {
                CloudSaveApi.Result result = (CloudSaveApi.Result)object;
                CloudLoginScr.this.savesLoading = false;
                if (!result.ok) {
                    CloudLoginScr.this.saves = new Vector();
                    CCanvas.startOKDlg("Kh\u00f4ng t\u1ea3i \u0111\u01b0\u1ee3c danh s\u00e1ch save: " + result.error);
                    return;
                }
                CloudLoginScr.this.saves = result.entries;
                int n = CloudLoginScr.this.saves.size();
                if (CloudLoginScr.this.selectedSave >= n) {
                    CloudLoginScr.this.selectedSave = n - 1;
                }
                if (CloudLoginScr.this.selectedSave < 0 && n > 0) {
                    CloudLoginScr.this.selectedSave = 0;
                }
            }
        });
    }

    private void openOptionsMenu() {
        Vector<Command> vector = new Vector<Command>();
        vector.addElement(new Command("T\u1ea1o save m\u1edbi", new IAction(){

            public void perform() {
                CloudLoginScr.this.doUploadNew();
            }
        }));
        vector.addElement(new Command("T\u1ea3i xu\u1ed1ng save \u0111\u00e3 ch\u1ecdn", new IAction(){

            public void perform() {
                CloudLoginScr.this.doDownloadSelected();
            }
        }));
        vector.addElement(new Command("X\u00f3a save \u0111\u00e3 ch\u1ecdn", new IAction(){

            public void perform() {
                CloudLoginScr.this.doDeleteSelected();
            }
        }));
        vector.addElement(new Command("\u0110\u0103ng xu\u1ea5t", new IAction(){

            public void perform() {
                CloudSaveApi.logout();
                CloudLoginScr.this.refreshState();
                CCanvas.startOKDlg("\u0110\u00e3 \u0111\u0103ng xu\u1ea5t kh\u1ecfi Cloud Save.");
            }
        }));
        CCanvas.menu.startAt(vector, 0);
    }

    private void doUploadNew() {
        CCanvas.startWaitDlg(Language.pleaseWait());
        String string = TerrainMidlet.myInfo != null ? TerrainMidlet.myInfo.name : "";
        CloudSaveApi.uploadSave(string, OfflineSave.exportBytes(), new IAction2(){

            public void perform(Object object) {
                CCanvas.endDlg();
                CloudSaveApi.Result result = (CloudSaveApi.Result)object;
                if (!result.ok) {
                    CCanvas.startOKDlg("T\u1ea3i l\u00ean th\u1ea5t b\u1ea1i: " + result.error);
                    return;
                }
                CCanvas.startOKDlg("\u0110\u00e3 t\u1ea1o save m\u1edbi tr\u00ean Cloud.");
                CloudLoginScr.this.loadSaves();
            }
        });
    }

    private void doDownloadSelected() {
        final CloudSaveApi.CloudSaveEntry cloudSaveEntry = this.getSelectedEntry();
        if (cloudSaveEntry == null) {
            CCanvas.startOKDlg("Ch\u01b0a ch\u1ecdn save n\u00e0o.");
            return;
        }
        CCanvas.startYesNoDlg("T\u1ea3i save \"" + CloudLoginScr.displayName(cloudSaveEntry) + "\" s\u1ebd ghi \u0111\u00e8 save hi\u1ec7n t\u1ea1i tr\u00ean m\u00e1y. Ti\u1ebfp t\u1ee5c?", new IAction(){

            public void perform() {
                CCanvas.startWaitDlg(Language.pleaseWait());
                CloudSaveApi.downloadSave(cloudSaveEntry.id, new IAction2(){

                    public void perform(Object object) {
                        CCanvas.endDlg();
                        CloudSaveApi.Result result = (CloudSaveApi.Result)object;
                        if (!result.ok) {
                            CCanvas.startOKDlg("T\u1ea3i save th\u1ea5t b\u1ea1i: " + result.error);
                            return;
                        }
                        boolean bl = OfflineSave.importBytes(result.data);
                        CCanvas.startOKDlg(bl ? "\u0110\u00e3 kh\u00f4i ph\u1ee5c save t\u1eeb Cloud." : "T\u1ea3i v\u1ec1 th\u00e0nh c\u00f4ng nh\u01b0ng kh\u00f4ng \u00e1p d\u1ee5ng \u0111\u01b0\u1ee3c save.");
                    }
                });
            }
        });
    }

    private void doDeleteSelected() {
        final CloudSaveApi.CloudSaveEntry cloudSaveEntry = this.getSelectedEntry();
        if (cloudSaveEntry == null) {
            CCanvas.startOKDlg("Ch\u01b0a ch\u1ecdn save n\u00e0o.");
            return;
        }
        CCanvas.startYesNoDlg("X\u00f3a save \"" + CloudLoginScr.displayName(cloudSaveEntry) + "\" kh\u1ecfi Cloud? Kh\u00f4ng th\u1ec3 ho\u00e0n t\u00e1c.", new IAction(){

            public void perform() {
                CCanvas.startWaitDlg(Language.pleaseWait());
                CloudSaveApi.deleteSave(cloudSaveEntry.id, new IAction2(){

                    public void perform(Object object) {
                        CCanvas.endDlg();
                        CloudSaveApi.Result result = (CloudSaveApi.Result)object;
                        if (!result.ok) {
                            CCanvas.startOKDlg("X\u00f3a th\u1ea5t b\u1ea1i: " + result.error);
                            return;
                        }
                        CCanvas.startOKDlg("\u0110\u00e3 x\u00f3a save.");
                        CloudLoginScr.this.loadSaves();
                    }
                });
            }
        });
    }

    private CloudSaveApi.CloudSaveEntry getSelectedEntry() {
        if (this.saves == null || this.selectedSave < 0 || this.selectedSave >= this.saves.size()) {
            return null;
        }
        return (CloudSaveApi.CloudSaveEntry)this.saves.elementAt(this.selectedSave);
    }

    private static String displayName(CloudSaveApi.CloudSaveEntry cloudSaveEntry) {
        return cloudSaveEntry.name != null && cloudSaveEntry.name.length() > 0 ? cloudSaveEntry.name : "(Kh\u00f4ng t\u00ean)";
    }

    private static String formatDate(String string) {
        if (string == null || string.length() < 16) {
            return "";
        }
        String string2 = string.substring(0, 4);
        String string3 = string.substring(5, 7);
        String string4 = string.substring(8, 10);
        String string5 = string.substring(11, 13);
        String string6 = string.substring(14, 16);
        return string4 + "/" + string3 + "/" + string2 + " " + string5 + ":" + string6;
    }

    public void update() {
        if (this.linked && this.saves != null && this.saves.size() > 0) {
            int n = this.saves.size();
            if (CCanvas.keyPressed[2]) {
                this.selectedSave = this.selectedSave > 0 ? this.selectedSave - 1 : n - 1;
                CScreen.clearKey();
            }
            if (CCanvas.keyPressed[8]) {
                this.selectedSave = this.selectedSave < n - 1 ? this.selectedSave + 1 : 0;
                CScreen.clearKey();
            }
        }
        super.update();
    }

    public void onPointerPressed(int n, int n2, int n3) {
        super.onPointerPressed(n, n2, n3);
        if (this.linked) {
            int n4;
            int n5;
            if (this.saves != null && this.saves.size() > 0 && (n5 = (n2 - (n4 = this.boxTop + 30 + 16)) / 16) >= 0 && n5 < this.saves.size()) {
                this.selectedSave = n5;
            }
            return;
        }
        if (CCanvas.isPointer(this.tUser.x, this.tUser.y, this.tUser.width, this.tUser.height, n3)) {
            if (this.focus != 0) {
                this.focus = 0;
            } else {
                this.tUser.doChangeToTextBox();
            }
            this.focusUpdate();
        } else if (CCanvas.isPointer(this.tPass.x, this.tPass.y, this.tPass.width, this.tPass.height, n3)) {
            if (this.focus != 1) {
                this.focus = 1;
            } else {
                this.tPass.doChangeToTextBox();
            }
            this.focusUpdate();
        }
    }

    public void paint(mGraphics mGraphics2) {
        mGraphics2.setColor(7852799);
        mGraphics2.fillRect(0, 0, CCanvas.width, CCanvas.hieght, false);
        Cloud.paintCloud(mGraphics2);
        CloudLoginScr.paintBorderRect(mGraphics2, this.boxTop, this.nTab, this.boxH, "CLOUD SAVE");
        if (this.linked) {
            int n = this.boxTop + 30;
            String string = CloudSaveApi.getLinkedEmail();
            int n2 = Font.normalYFont.getWidth(string);
            mGraphics2.setColor(3374591);
            mGraphics2.fillRect(CCanvas.width / 2 - n2 / 2 - 6, n - 1, n2 + 12, 16, false);
            Font.normalYFont.drawString(mGraphics2, string, CCanvas.width / 2, n, 2);
            n += 16;
            if (this.savesLoading) {
                Font.borderFont.drawString(mGraphics2, "\u0110ang t\u1ea3i danh s\u00e1ch...", CCanvas.width / 2, n, 2);
            } else if (this.saves == null || this.saves.size() == 0) {
                Font.borderFont.drawString(mGraphics2, "Ch\u01b0a c\u00f3 save n\u00e0o tr\u00ean Cloud.", CCanvas.width / 2, n, 2);
            } else {
                for (int i = 0; i < this.saves.size(); ++i) {
                    CloudSaveApi.CloudSaveEntry cloudSaveEntry = (CloudSaveApi.CloudSaveEntry)this.saves.elementAt(i);
                    if (i == this.selectedSave) {
                        mGraphics2.setColor(3374591);
                        mGraphics2.fillRect(CCanvas.width / 2 - this.boxW / 2 + 10, n - 1, this.boxW - 20, 16, false);
                        mGraphics2.setColor(0);
                    }
                    String string2 = i + 1 + ". " + CloudLoginScr.displayName(cloudSaveEntry) + " - " + CloudLoginScr.formatDate(cloudSaveEntry.createdAt);
                    Font.borderFont.drawString(mGraphics2, string2, CCanvas.width / 2, n, 2);
                    n += 16;
                }
            }
        } else {
            Font.borderFont.drawString(mGraphics2, "Email:", this.tUser.x, this.tUser.y - 14, 0);
            this.tUser.paint(mGraphics2);
            Font.borderFont.drawString(mGraphics2, Language.pass() + ":", this.tPass.x, this.tPass.y - 14, 0);
            this.tPass.paint(mGraphics2);
        }
        super.paint(mGraphics2);
    }
}

