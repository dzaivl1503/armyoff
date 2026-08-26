/*
 * Mobi Army 2 Offline Cloud & Server Account Management Screen
 */
package screen;

import CLib.mGraphics;
import com.teamobi.mobiarmy2.CloudSaveApi;
import com.teamobi.mobiarmy2.GameMidlet;
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

public class CloudLoginScr extends CScreen {
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
        this.nTab = CCanvas.width >= 200 ? 5 : 4;
        int n = 30;
        int n2 = 20;
        if (this.linked) {
            this.boxW = this.nTab * 32 + 56;
            this.tUser = null;
            this.tPass = null;
            int n3 = 100;
            this.boxH = n + n3 + n2;
            this.boxTop = CCanvas.hieght / 2 - this.boxH / 2;
            this.center = new Command("Lưu lại", new IAction() {
                public void perform() {
                    CloudLoginScr.this.doManualSave();
                }
            });
            this.left = new Command("Tùy chọn", new IAction() {
                public void perform() {
                    CloudLoginScr.this.openLoggedInMenu();
                }
            });
            this.right = new Command("Vào game", new IAction() {
                public void perform() {
                    CloudLoginScr.this.enterGame();
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
            this.tUser.title = "Tài khoản / Email";

            n10 = this.tUser.y + n6 + n8;
            this.tPass = new TField();
            this.tPass.x = n5;
            this.tPass.y = n10 + n7;
            this.tPass.width = n4;
            this.tPass.height = n6;
            this.tPass.setIputType(2);
            this.tPass.title = Language.pass();

            this.center = new Command(Language.signIn(), new IAction() {
                public void perform() {
                    CloudLoginScr.this.doLogin();
                }
            });
            this.left = new Command("Menu", new IAction() {
                public void perform() {
                    CloudLoginScr.this.openNotLoggedInMenu();
                }
            });
            this.right = new Command(Language.back(), new IAction() {
                public void perform() {
                    if (CloudLoginScr.this.lastScr != null) {
                        CloudLoginScr.this.lastScr.show();
                    } else if (CCanvas.mainMenuScr != null) {
                        CCanvas.mainMenuScr.show();
                    }
                }
            });
            this.focus = 0;
            this.focusUpdate();
        }
    }

    private void focusUpdate() {
        if (this.tUser != null && this.tPass != null) {
            this.tUser.setisFocus(this.focus == 0);
            this.tPass.setisFocus(this.focus == 1);
        }
    }

    private void doLogin() {
        if (this.tUser == null || this.tPass == null || this.tUser.getText().trim().length() == 0 || this.tPass.getText().trim().length() == 0) {
            CCanvas.startOKDlg("Vui lòng nhập đầy đủ tên tài khoản và mật khẩu.");
            return;
        }
        final String u = this.tUser.getText().trim();
        final String p = this.tPass.getText().trim();
        CCanvas.startWaitDlg("Đang kết nối máy chủ...");
        CloudSaveApi.login(u, p, new IAction2() {
            public void perform(Object object) {
                CloudSaveApi.Result result = (CloudSaveApi.Result) object;
                if (!result.ok) {
                    CCanvas.endDlg();
                    CCanvas.startOKDlg(result.error);
                    return;
                }

                CCanvas.startWaitDlg("Đang đồng bộ dữ liệu từ Server...");
                CloudSaveApi.downloadCurrentSave(new IAction2() {
                    public void perform(Object objSave) {
                        CCanvas.endDlg();
                        CloudSaveApi.fetchRemoteConfig(null);
                        CCanvas.startOKDlg("Đăng nhập thành công! Đã đồng bộ dữ liệu từ Server.", new IAction() {
                            public void perform() {
                                CloudLoginScr.this.enterGame();
                            }
                        });
                    }
                });
            }
        });
    }

    private void doRegister() {
        if (this.tUser == null || this.tPass == null || this.tUser.getText().trim().length() == 0 || this.tPass.getText().trim().length() == 0) {
            CCanvas.startOKDlg("Vui lòng nhập tên tài khoản và mật khẩu muốn đăng ký.");
            return;
        }
        final String u = this.tUser.getText().trim();
        final String p = this.tPass.getText().trim();
        CCanvas.startWaitDlg("Đang đăng ký tài khoản mới...");
        CloudSaveApi.register(u, p, new IAction2() {
            public void perform(Object object) {
                CCanvas.endDlg();
                CloudSaveApi.Result result = (CloudSaveApi.Result) object;
                if (!result.ok) {
                    CCanvas.startOKDlg(result.error);
                    return;
                }

                GameMidlet.startNewOfflineGame(u);
                if (TerrainMidlet.myInfo != null) {
                    TerrainMidlet.myInfo.name = u;
                }
                OfflineSave.save();
                CloudSaveApi.uploadCurrentSave(null);
                CloudSaveApi.fetchRemoteConfig(null);

                CCanvas.startOKDlg("Đăng ký thành công tài khoản " + u + "!", new IAction() {
                    public void perform() {
                        CloudLoginScr.this.enterGame();
                    }
                });
            }
        });
    }

    private void doConfigureServerUrl() {
        CCanvas.inputDlg.setInfo("Nhập địa chỉ máy chủ (Server URL):", new IAction() {
            public void perform() {
                String input = CCanvas.inputDlg.tfInput.getText();
                CCanvas.endDlg();
                if (input != null && input.trim().length() > 0) {
                    CloudSaveApi.setServerUrl(input.trim());
                    CCanvas.startOKDlg("Đã cập nhật máy chủ:\n" + CloudSaveApi.getServerUrl());
                }
            }
        }, null, 0);
        CCanvas.inputDlg.tfInput.setMaxTextLenght(120);
        CCanvas.inputDlg.tfInput.setText(CloudSaveApi.getServerUrl());
        CCanvas.inputDlg.show();
        if (CCanvas.isTouch) {
            CCanvas.inputDlg.tfInput.doChangeToTextBox();
        }
    }

    private void doManualSave() {
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

    private void enterGame() {
        if (!OfflineSave.hasSave()) {
            String name = CloudSaveApi.getLinkedEmail();
            if (name == null || name.length() == 0) name = "Chiến Binh";
            GameMidlet.startNewOfflineGame(name);
        }
        if (!GameMidlet.continueOfflineGame()) {
            GameMidlet.startNewOfflineGame("Chiến Binh");
        }
        if (CloudSaveApi.isLoggedIn() && TerrainMidlet.myInfo != null) {
            String name = CloudSaveApi.getLinkedEmail();
            if (name != null && name.trim().length() > 0) {
                TerrainMidlet.myInfo.name = name.trim();
            }
        }
        GameMidlet.enterOfflineMenu();
    }

    private void loadSaves() {
        this.savesLoading = true;
        CloudSaveApi.listSaves(new IAction2() {
            public void perform(Object object) {
                CloudSaveApi.Result result = (CloudSaveApi.Result) object;
                CloudLoginScr.this.savesLoading = false;
                if (!result.ok) {
                    CloudLoginScr.this.saves = new Vector();
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

    private void openNotLoggedInMenu() {
        Vector<Command> v = new Vector<Command>();
        v.addElement(new Command("Đăng Nhập", new IAction() {
            public void perform() {
                CloudLoginScr.this.doLogin();
            }
        }));
        v.addElement(new Command("Đăng Ký Tài Khoản Mới", new IAction() {
            public void perform() {
                CloudLoginScr.this.doRegister();
            }
        }));
        v.addElement(new Command("Cài Đặt Máy Chủ (Server URL)", new IAction() {
            public void perform() {
                CloudLoginScr.this.doConfigureServerUrl();
            }
        }));
        CCanvas.menu.startAt(v, 0);
    }

    private void openLoggedInMenu() {
        Vector<Command> vector = new Vector<Command>();
        vector.addElement(new Command("Lưu Lại (Gửi Lên Server)", new IAction() {
            public void perform() {
                CloudLoginScr.this.doManualSave();
            }
        }));
        vector.addElement(new Command("Tải Lại Từ Server", new IAction() {
            public void perform() {
                CCanvas.startWaitDlg("Đang tải dữ liệu từ máy chủ...");
                CloudSaveApi.downloadCurrentSave(new IAction2() {
                    public void perform(Object obj) {
                        CCanvas.endDlg();
                        CloudSaveApi.Result res = (CloudSaveApi.Result) obj;
                        if (res != null && res.ok) {
                            CCanvas.startOKDlg("Đã tải và nạp thành công dữ liệu từ Server!");
                        } else {
                            CCanvas.startOKDlg("Không thể tải save: " + (res != null ? res.error : ""));
                        }
                    }
                });
            }
        }));
        vector.addElement(new Command("Đồng Bộ Nhiệm Vụ Mới", new IAction() {
            public void perform() {
                CCanvas.startWaitDlg("Đang tải cấu hình nhiệm vụ...");
                CloudSaveApi.fetchRemoteConfig(new IAction2() {
                    public void perform(Object obj) {
                        CCanvas.endDlg();
                        CloudSaveApi.Result res = (CloudSaveApi.Result) obj;
                        if (res != null && res.ok) {
                            CCanvas.startOKDlg("Đã cập nhật nhiệm vụ và phần thưởng mới từ Admin!");
                        } else {
                            CCanvas.startOKDlg("Không thể kết nối máy chủ để lấy nhiệm vụ.");
                        }
                    }
                });
            }
        }));
        vector.addElement(new Command("Cài Đặt Máy Chủ (Server URL)", new IAction() {
            public void perform() {
                CloudLoginScr.this.doConfigureServerUrl();
            }
        }));
        vector.addElement(new Command("Đăng Xuất", new IAction() {
            public void perform() {
                CloudSaveApi.logout();
                CloudLoginScr.this.refreshState();
                CCanvas.startOKDlg("Đã đăng xuất khỏi tài khoản.");
            }
        }));
        CCanvas.menu.startAt(vector, 0);
    }

    public void update() {
        if (!this.linked && this.tUser != null && this.tPass != null) {
            this.tUser.update();
            this.tPass.update();
            if (CCanvas.keyPressed[2]) {
                this.focus = 0;
                this.focusUpdate();
                CScreen.clearKey();
            }
            if (CCanvas.keyPressed[8]) {
                this.focus = 1;
                this.focusUpdate();
                CScreen.clearKey();
            }
        }
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

    public void keyPressed(int n) {
        if (!this.linked) {
            if (n == 10 || n == 13) {
                if (this.focus == 0) {
                    this.focus = 1;
                    this.focusUpdate();
                } else {
                    this.doLogin();
                }
                return;
            }
            if (n == -1 || n == -38 || n == 2) {
                this.focus = 0;
                this.focusUpdate();
                return;
            }
            if (n == -2 || n == -39 || n == 8) {
                this.focus = 1;
                this.focusUpdate();
                return;
            }
            if (this.focus == 0 && this.tUser != null) {
                this.tUser.keyPressed(n);
                return;
            } else if (this.focus == 1 && this.tPass != null) {
                this.tPass.keyPressed(n);
                return;
            }
        }
        super.keyPressed(n);
    }

    public void onPointerPressed(int n, int n2, int n3) {
        super.onPointerPressed(n, n2, n3);
        if (!this.linked && this.tUser != null && this.tPass != null) {
            if (CCanvas.isPointer(this.tUser.x, this.tUser.y - 10, this.tUser.width, this.tUser.height + 20, n3)) {
                this.focus = 0;
                this.focusUpdate();
            } else if (CCanvas.isPointer(this.tPass.x, this.tPass.y - 10, this.tPass.width, this.tPass.height + 20, n3)) {
                this.focus = 1;
                this.focusUpdate();
            }
        }
    }

    public void paint(mGraphics mGraphics2) {
        mGraphics2.setColor(7852799);
        mGraphics2.fillRect(0, 0, CCanvas.width, CCanvas.hieght, false);
        Cloud.paintCloud(mGraphics2);
        CloudLoginScr.paintBorderRect(mGraphics2, this.boxTop, this.nTab, this.boxH, "Máy chủ Cloud");

        if (this.linked) {
            int n = this.boxTop + 30;
            String string = "Tài khoản: " + CloudSaveApi.getLinkedEmail();
            int n2 = Font.normalYFont.getWidth(string);
            mGraphics2.setColor(3374591);
            mGraphics2.fillRect(CCanvas.width / 2 - n2 / 2 - 6, n - 1, n2 + 12, 16, false);
            Font.normalYFont.drawString(mGraphics2, string, CCanvas.width / 2, n, 2);
            n += 20;

            String serverInfo = "Server: " + CloudSaveApi.getServerUrl();
            Font.borderFont.drawString(mGraphics2, serverInfo, CCanvas.width / 2, n, 2);
            n += 18;

            Font.borderFont.drawString(mGraphics2, "Dữ liệu được tự động đồng bộ khi lưu.", CCanvas.width / 2, n, 2);
            n += 18;
            Font.borderFont.drawString(mGraphics2, "Bấm 'LƯU LẠI' để gửi dữ liệu lên Server ngay.", CCanvas.width / 2, n, 2);
        } else {
            Font.borderFont.drawString(mGraphics2, "Tài khoản:", this.tUser.x, this.tUser.y - 14, 0);
            this.tUser.paint(mGraphics2);
            Font.borderFont.drawString(mGraphics2, Language.pass() + ":", this.tPass.x, this.tPass.y - 14, 0);
            this.tPass.paint(mGraphics2);
        }
        super.paint(mGraphics2);
    }
}
