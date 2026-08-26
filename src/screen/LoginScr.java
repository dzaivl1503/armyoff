/*
 * Decompiled with CFR 0.152.
 */
package screen;

import CLib.RMS;
import CLib.mGraphics;
import CLib.mImage;
import CLib.mSound;
import CLib.mSystem;
import com.teamobi.mobiarmy2.GameMidlet;
import coreLG.CCanvas;
import coreLG.CONFIG;
import coreLG.TerrainMidlet;
import effect.Cloud;
import effect.Explosion;
import effect.SmokeManager;
import item.Bullet;
import java.util.Vector;
import map.Background;
import map.CMap;
import map.MM;
import model.CRes;
import model.FilePack;
import model.Font;
import model.GetString;
import model.IAction;
import model.Language;
import model.TField;
import network.Command;
import network.GameLogicHandler;
import network.GameService;
import network.Session_ME;
import screen.CScreen;
import screen.ConfigScr;
import screen.GameScr;
import screen.PrepareScr;

public class LoginScr
extends CScreen {
    public static String user = "";
    public static String pass = "";
    private TField tUser;
    private TField tPass;
    private TField tEmail;
    public int focus;
    int xC;
    int wC;
    int yL;
    int defYL;
    public static mImage lgGame;
    public static mImage imgPlane;
    public static mImage stone;
    public static mImage missile;
    public int plX;
    public int plY;
    public int lY;
    public int lX;
    public int logoDes;
    public int lineX;
    public int lineY;
    public static boolean isWait;
    public static int currTime;
    public static int maxTime;
    public boolean isDemo;
    public boolean isPlane;
    public boolean isCloud;
    public boolean isAskSound;
    public boolean isForward;
    public boolean isMenu;
    public int demoStat;
    public static boolean isLoadData;
    public static mImage imgVMB;
    public static mImage imgCheck;
    public static int startTimeOut;
    String numSupport = "";
    int xLog;
    int yLog;
    public int cmy = -1500;
    private boolean isRegister;
    int finishDemo;
    Command cmdSignIn;
    Command cmdMenu;
    Command cmdForward;
    Command cmdRemember;
    Command cmdYes;
    Command cmdNo;
    Command cmdSelect;
    Command cmdRegister;
    Command cmdBack;
    private static String charName;
    private static String email_phone;
    private static String password;
    int dyLogo;
    int dyT = 0;
    int ty = 17;
    int deltaX;
    int tX;
    boolean activeFall;
    boolean explore;
    boolean logoII;
    public int[] xB = new int[]{CCanvas.width + 15, CCanvas.width + 15};
    public int[] yB = new int[2];
    int speed;
    int tII;
    int[] xCl1 = new int[]{-50, 0, 30, CCanvas.width / 2, CCanvas.width - 10, CCanvas.width + 20, 100};
    int[] xCl2 = new int[]{CCanvas.width / 2 - 20, 50, CCanvas.width / 2 + 40, 100, CCanvas.width - 40, CCanvas.width - 10, 100};
    int[] xBl = new int[]{CCanvas.width - 80, CCanvas.width / 2, CCanvas.width / 2 - 50, 30};
    public static int remember;
    boolean isStone = true;
    int[] cloudX = new int[]{CCanvas.width, CCanvas.width + 100, CCanvas.width - 10, CCanvas.width + 50};
    int[] cloudY = new int[]{40, 70, 90, 160};
    int[] cloudVX = new int[]{-5, -3, -4, -3};
    int[] cloudVY = new int[4];
    public static int volume;

    public void show() {
        CCanvas.splashScr = null;
        CScreen.isSetClip = true;
        this.resetTF();
        this.tUser.name = "TUser";
        this.tPass.name = "TPass";
        super.show();
    }

    public LoginScr() {
        this.nameCScreen = " LoginScr screen!";
        this.initDemoData();
        this.isAskSound = true;
        this.isDemo = true;
        lgGame = GameScr.logoGame;
        if (GameScr.sm == null) {
            GameScr.sm = new SmokeManager();
        }
        GameScr.sm.addSmoke(-100, -100, (byte)19);
        GameScr.exs = new Vector();
        new Explosion(-100, -100, 0);
        GameScr.curGRAPHIC_LEVEL = (byte)CRes.loadRMSInt("Graphic");
        if (GameScr.curGRAPHIC_LEVEL == -1) {
            GameScr.curGRAPHIC_LEVEL = 1;
        }
        CMap.isDrawRGB = CRes.loadRMSInt("drawRGB") == 0;
        this.yLog = CCanvas.width >= 200 ? CCanvas.hieght - cmdH - 125 : CCanvas.hieght - 125;
        this.plX = -100;
        this.plY = this.cmy + 60;
        this.lX = -100;
        this.lY = this.plY + 120;
        this.yB[0] = this.lY - 39;
        this.yB[1] = -1450;
        this.defYL = CCanvas.hh - 80;
        MM.mapHeight = CCanvas.hieght;
        this.wC = CCanvas.width - 30;
        if (this.wC < 70) {
            this.wC = 70;
        }
        if (this.wC > 99) {
            this.wC = 99;
        }
        if (CCanvas.width < 200) {
            this.wC = 70;
        }
        this.xC = (CCanvas.width - this.wC >> 1) + 29;
        this.tUser = new TField();
        this.tUser.y = CCanvas.hh - ITEM_HEIGHT - 9;
        this.tUser.width = this.wC;
        this.tUser.height = ITEM_HEIGHT + 2;
        this.tUser.setisFocus(true);
        this.tUser.setIputType(3);
        this.tPass = new TField();
        this.tPass.y = CCanvas.hh - 4;
        this.tPass.width = this.wC;
        this.tPass.height = ITEM_HEIGHT + 2;
        this.tPass.setisFocus(false);
        this.tPass.setIputType(2);
        this.tEmail = new TField();
        this.tEmail.y = CCanvas.hh - 8;
        this.tEmail.width = this.wC;
        this.tEmail.height = ITEM_HEIGHT + 2;
        this.tEmail.setisFocus(false);
        this.tEmail.setIputType(3);
        this.tUser.nameDebug = "TField ===> tUser login";
        this.tPass.nameDebug = "TField ===> tPass login";
        this.tEmail.nameDebug = "TField ===> tEmail login";
        this.tUser.setText(CRes.loadRMS_String("caroun"));
        this.tPass.setText(CRes.loadRMS_String("caropass"));
        remember = CRes.loadRMSInt("remember");
        if (remember == -1) {
            remember = 0;
        }
        this.initSignIn();
    }

    public String getUrlUpdateGame() {
        return "http://wap.teamobi.com?info=checkupdate&game=3&version=" + GameMidlet.version + "&provider=" + TerrainMidlet.PROVIDER;
    }

    public void connect() {
        Session_ME.gI().connect(GameMidlet.IP, GameMidlet.PORT);
        if (TerrainMidlet.isTeamClient) {
            GameService.gI().setProvider(TerrainMidlet.PROVIDER);
            new GetString();
            GameService.gI().getString("abc");
            GameService.gI().platform_request();
        } else {
            TerrainMidlet.PROVIDER = (byte)CRes.loadRMSInt("provider");
            TerrainMidlet.AGENT = CRes.loadRMS_String("agent");
            if (TerrainMidlet.AGENT == null) {
                TerrainMidlet.AGENT = "";
            }
            if (TerrainMidlet.PROVIDER != -1) {
                GameService.gI().setProvider(TerrainMidlet.PROVIDER);
                GameService.gI().getString(TerrainMidlet.AGENT);
            }
        }
        CCanvas.startWaitDlgWithoutCancel(Language.connecting(), 111111);
    }

    protected void doForgetPass(final String string) {
        CCanvas.startYesNoDlg(Language.usingPhone(), new IAction(){

            public void perform() {
                if (!Session_ME.gI().isConnected()) {
                    LoginScr.this.connect();
                } else {
                    CCanvas.startWaitDlg(Language.pleaseWait());
                }
                GameService.gI().requestService((byte)4, string);
            }
        }, new IAction(){

            public void perform() {
                CCanvas.startOKDlg(Language.usingPhone2());
            }
        });
    }

    public void setRegister() {
        this.isRegister = true;
        this.tUser.resetTextBox();
        this.tEmail.resetTextBox();
        this.tPass.resetTextBox();
    }

    public void setLogin() {
        this.isRegister = false;
        this.center = this.cmdSignIn;
        this.initSignIn();
    }

    private void initDemoData() {
        this.isAskSound = false;
        this.demoStat = 0;
        volume = CRes.loadRMSInt("sound");
        if (volume > 0) {
            mSound.setVolume(volume);
        }
    }

    public void doRegister() {
        CCanvas.startYesNoDlg(Language.dangKyGam(), new IAction(){

            public void perform() {
                mSystem.openUrl(GameMidlet.linkReg);
            }
        }, new IAction(){

            public void perform() {
                CCanvas.endDlg();
            }
        });
    }

    public void doLogin() {
        user = this.tUser.getText().toLowerCase().trim();
        pass = this.tPass.getText();
        if (user.equals("")) {
            CCanvas.startOKDlg(Language.idPlease());
        } else if (pass.equals("")) {
            CCanvas.msgdlg.setInfo(Language.passPlease(), null, new Command("OK", new IAction(){

                public void perform() {
                    CCanvas.endDlg();
                    LoginScr.this.focus = 1;
                    LoginScr.this.tUser.setisFocus(false);
                    LoginScr.this.tPass.setisFocus(true);
                    LoginScr.this.right = ((LoginScr)LoginScr.this).tPass.cmdClear;
                }
            }), null);
            CCanvas.msgdlg.show();
        } else {
            if (!Session_ME.gI().connected) {
                this.connect();
            } else {
                CCanvas.startWaitDlgWithoutCancel(Language.logging(), 1);
            }
            if (!Session_ME.gI().isConnected()) {
                this.connect();
            }
            GameService.gI().getProviderAgent();
            GameService.gI().login(user, pass, GameMidlet.version);
        }
    }

    public void Effect() {
        this.dyT += 3;
        this.lY += this.dyT;
        if (this.lY > this.logoDes) {
            this.lY = this.logoDes;
            this.dyT = -this.ty;
            this.ty -= 4;
        }
    }

    public void planeUpdate() {
        int n;
        int n2;
        int n3;
        int[] nArray;
        this.plX += 2 + this.speed;
        if (this.plX >= CCanvas.width / 2 - 10) {
            nArray = this.xB;
            nArray[0] = nArray[0] - (10 + this.speed);
            if (!this.activeFall) {
                GameScr.sm.addSmoke(this.xB[0] + 20, this.yB[0] + 5, (byte)19);
            }
        }
        if (this.xB[1] > -15 && this.plX > CCanvas.width / 3) {
            nArray = this.xB;
            nArray[1] = nArray[1] - (12 + this.speed);
            if (!this.explore && this.speed == 0) {
                GameScr.sm.addSmoke(this.xB[1] + 20, this.yB[1] + 5, (byte)19);
            }
        }
        if (!this.activeFall) {
            this.lX = this.plX + this.deltaX;
            this.lineX = this.lX - 1;
            this.lineY = this.lY - 40;
            ++this.tX;
            if (this.tX == 30) {
                this.tX = 0;
            }
            if (CCanvas.gameTick % 2 == 0) {
                this.deltaX = this.tX <= 15 ? --this.deltaX : ++this.deltaX;
            }
        }
        if (this.xB[1] <= CCanvas.width && this.plX >= 0 && this.xB[1] <= this.plX + 80 && !this.explore) {
            this.explore = true;
            new Explosion(this.plX + 80, this.plY, 0);
            if (this.speed == 0) {
                for (n3 = 0; n3 < 6; ++n3) {
                    n2 = CRes.random(n3 + 1);
                    n = CRes.random(-8, -5);
                    GameScr.sm.addRock(this.plX + 80, this.plY, n2, n, (byte)3);
                }
            }
        }
        if (this.xB[0] <= CCanvas.width && this.plX >= 0 && this.xB[0] <= this.plX - 12) {
            if (!this.activeFall) {
                this.activeFall = true;
                new Explosion(this.lineX, this.lineY, 0);
                if (this.speed == 0) {
                    for (n3 = 0; n3 < 6; ++n3) {
                        n2 = CRes.random(n3 + 1);
                        n = CRes.random(-8, -5);
                        GameScr.sm.addRock(this.lineX, this.lineY, n2, n, (byte)3);
                    }
                }
            }
            if (this.activeFall) {
                this.lX -= 2 + this.speed / 3;
            }
            if (this.speed == 15) {
                this.lX -= 2 + this.speed / 3;
            }
            if (this.lX < CCanvas.width / 2 - 20) {
                this.lX = CCanvas.width / 2 - 20;
            }
            this.dyLogo += 1 + this.speed;
            this.lY += this.dyLogo / 2 + this.speed;
            n3 = 0;
            while (n3 < this.cloudY.length) {
                nArray = this.cloudY;
                int n4 = n3++;
                nArray[n4] = nArray[n4] - (2 + this.speed);
            }
            if (GameScr.exs.size() == 0 || this.speed != 0) {
                this.cmy += this.dyLogo / 2 + 2 + this.speed;
            }
            if (this.cmy > 0) {
                this.cmy = 0;
            }
            this.lineX = this.plX + this.deltaX - 1;
            if (this.lY > this.logoDes + 10) {
                this.lY = this.logoDes + 10;
                this.isDemo = false;
                this.isMenu = true;
                this.isForward = false;
                this.logoII = true;
                this.cmy = 0;
                this.right = this.tUser.cmdClear;
                this.left = this.cmdMenu;
                this.center = this.cmdSignIn;
            }
        }
    }

    public void update() {
        int n;
        if (this.cmy > 0) {
            this.cmy = 0;
        }
        this.speed = this.isForward ? 15 : 0;
        if (this.isAskSound) {
            Cloud.updateCloud();
        } else if (this.isDemo) {
            this.updateCloud();
            this.planeUpdate();
        } else {
            if (this.logoII) {
                ++this.tII;
                if (this.tII == 20) {
                    this.tII = 0;
                    this.logoII = false;
                    GameScr.sm.addLazer(CCanvas.width, 0, CCanvas.width / 2 + 45, this.tUser.y - 50, 0);
                    if (!this.isForward) {
                        new Explosion(CCanvas.width / 2 + 40, this.tUser.y - 80, 0);
                        new Explosion(CCanvas.width / 2 + 40, this.tUser.y - 50, 0);
                        new Explosion(CCanvas.width / 2 + 40, this.tUser.y - 20, 0);
                    }
                    for (n = 0; n < 6; ++n) {
                        GameScr.sm.addRock(CCanvas.width / 2 + 40, this.tUser.y - 50, CRes.random(n + 1), CRes.random(-8, -5), (byte)3);
                    }
                    for (n = 0; n < 6; ++n) {
                        GameScr.sm.addRock(CCanvas.width / 2 + 40, this.tUser.y - 50, -CRes.random(n + 1), CRes.random(-8, -5), (byte)2);
                    }
                    this.isStone = false;
                }
            }
            if (this.isMenu) {
                this.Effect();
            }
            Cloud.updateCloud();
            Cloud.balloonUpdate();
            this.tUser.update();
            this.tPass.update();
            this.tEmail.update();
            if (isWait && (currTime += 2) > maxTime) {
                currTime = maxTime;
            }
        }
        GameScr.sm.update();
        for (n = 0; n < GameScr.exs.size(); ++n) {
            ((Explosion)GameScr.exs.elementAt(n)).update();
        }
        super.update();
    }

    public void keyPressed(int n) {
        if (CCanvas.currentDialog == null) {
            if (this.focus == 0) {
                this.tUser.keyPressed(n);
            } else {
                this.tPass.keyPressed(n);
            }
        }
        super.keyPressed(n);
    }

    public void paintBackG(mGraphics mGraphics2) {
        Background.paintMenuBackGround(mGraphics2);
    }

    public void paintDemo(mGraphics mGraphics2) {
        int[] nArray;
        int n;
        int n2;
        mGraphics2.setColor(6606845);
        mGraphics2.fillRect(0, this.cmy, CCanvas.width, CCanvas.hieght, false);
        mGraphics2.setColor(7612928);
        for (n2 = 0; n2 < 4; ++n2) {
            mGraphics2.drawImage(Cloud.imgCloud[n2 % 2], this.cloudX[n2], this.cloudY[n2] - 1500, 0, false);
        }
        for (n2 = 0; n2 < 7; ++n2) {
            n = -1200 + n2 * 120;
            if (n < this.cmy - 50 || n > this.cmy + CCanvas.hieght) continue;
            mGraphics2.drawImage(Cloud.imgCloud[0], this.xCl1[n2], n, 0, false);
            nArray = this.xCl1;
            int n3 = n2;
            nArray[n3] = nArray[n3] + this.speed;
        }
        for (n2 = 0; n2 < 7; ++n2) {
            n = -1100 + n2 * 200;
            if (n < this.cmy - 50 || n > this.cmy + CCanvas.hieght) continue;
            mGraphics2.drawImage(Cloud.imgCloud[1], this.xCl2[n2] + n2 * 4 - 90, n, 0, false);
            if (CCanvas.gameTick % 2 != 0) continue;
            nArray = this.xCl2;
            int n4 = n2;
            nArray[n4] = nArray[n4] + this.speed;
        }
        for (n2 = 0; n2 < 4; ++n2) {
            n = -700 + n2 * 300;
            if (n < this.cmy - 50 || n > this.cmy + CCanvas.hieght) continue;
            mGraphics2.drawImage(Background.balloon, this.xBl[n2] - 50, n, 0, false);
        }
        mGraphics2.drawLine(this.plX - 5, this.plY + 48, this.lineX, this.lineY, true);
        mGraphics2.drawLine(this.plX + 5, this.plY + 48, this.lineX, this.lineY, true);
        mGraphics2.drawImage(imgPlane, this.plX, this.plY, mGraphics.VCENTER | mGraphics.HCENTER, true);
        if (!this.activeFall) {
            mGraphics2.drawImage(Background.mocxich, this.lX, this.lY - 39, mGraphics.TOP | mGraphics.HCENTER, true);
            mGraphics2.drawRegion(missile, 0, 0, 15, 15, 2, this.xB[0], this.yB[0], 0, true);
        }
        if (!this.explore) {
            mGraphics2.drawRegion(missile, 0, 0, 15, 15, 2, this.xB[1], this.yB[1], 0, true);
        }
    }

    public void remember() {
        remember = remember == 0 ? 1 : 0;
        CRes.saveRMSInt("remember", remember);
    }

    public void paintMenuLogin(mGraphics mGraphics2) {
        int n;
        this.paintBackG(mGraphics2);
        Cloud.paintBalloonWithCloud(mGraphics2);
        Background.paintTree(mGraphics2);
        int n2 = CCanvas.width >= 200 ? 4 : 3;
        int n3 = CCanvas.isTouch ? 120 : 100;
        LoginScr.paintBorderRect(mGraphics2, this.yLog, n2, n3, "");
        mGraphics2.drawImage(lgGame, this.lX, this.lY, 3, false);
        mGraphics2.drawImage(GameScr.logoII, LoginScr.lgGame.image.getWidth() + this.lX, this.tUser.y - 50, 3, false);
        if (this.isStone) {
            mGraphics2.drawImage(Background.b, CCanvas.width / 2 + 20, this.tUser.y - 95, 0, false);
            mGraphics2.drawImage(Background.b, CCanvas.width / 2 + 25, this.tUser.y - 70, 0, false);
            mGraphics2.drawImage(Background.b, CCanvas.width / 2 + 17, this.tUser.y - 63, 0, false);
        }
        int n4 = (CCanvas.hieght - this.yLog - h - cmdH) / 21;
        for (n = 0; n < n4 + 1; ++n) {
            mGraphics2.drawImage(Background.a, this.tUser.x + 65, this.yLog + h + n * 21, 0, false);
        }
        GameScr.sm.paint(mGraphics2);
        for (n = 0; n < GameScr.exs.size(); ++n) {
            ((Explosion)GameScr.exs.elementAt(n)).paint(mGraphics2);
        }
        this.tUser.paint(mGraphics2);
        Font.borderFont.drawString(mGraphics2, Language.id() + ":", this.tUser.x - 59, this.tUser.y + 4, 0, false);
        mGraphics2.setClip(0, 0, CCanvas.width, CCanvas.hieght);
        this.tPass.y = this.tUser.y + 28;
        Font.borderFont.drawString(mGraphics2, Language.pass() + ":", this.tPass.x - 59, this.tPass.y, 0);
        n = CCanvas.width >= 200 ? 0 : 30;
        int n5 = this.tPass.y + 36 - 10;
        Font.borderFont.drawString(mGraphics2, Language.remember(), this.xLog + 120 - n - (Language.language == 0 ? 0 : 30), n5, 0, true);
        mGraphics2.drawRegion(imgCheck, 0, remember * 16, 18, 16, 0, this.xLog + 150 - n, n5, 0, false);
        Font.normalFont.drawString(mGraphics2, Language.forgotPass() + "?", this.xLog + 10 - n - (Language.language == 0 ? 0 : 30), this.tPass.y + 35 + 10, 0, true);
        Font.normalFont.drawString(mGraphics2, Language.reg(), this.xLog + 120 - n - (Language.language == 0 ? 0 : 30), this.tPass.y + 35 + 10, 0, true);
        this.tPass.paint(mGraphics2);
    }

    public void paintMenuRegisterFreeAccount(mGraphics mGraphics2) {
        int n;
        this.paintBackG(mGraphics2);
        Cloud.paintBalloonWithCloud(mGraphics2);
        Background.paintTree(mGraphics2);
        Font.smallFont.drawString(mGraphics2, GameMidlet.version, CCanvas.width - 2, 2, 1);
        int n2 = CCanvas.width >= 200 ? 4 : 3;
        int n3 = CCanvas.isTouch ? 120 : 100;
        LoginScr.paintBorderRect(mGraphics2, this.yLog, n2, n3, "");
        mGraphics2.drawImage(lgGame, this.lX, this.lY, 3, false);
        mGraphics2.drawImage(GameScr.logoII, CCanvas.width / 2 + 50, this.tUser.y - 50, 3, false);
        if (this.isStone) {
            mGraphics2.drawImage(Background.b, CCanvas.width / 2 + 20, this.tUser.y - 95, 0, false);
            mGraphics2.drawImage(Background.b, CCanvas.width / 2 + 25, this.tUser.y - 70, 0, false);
            mGraphics2.drawImage(Background.b, CCanvas.width / 2 + 17, this.tUser.y - 63, 0, false);
        }
        int n4 = (CCanvas.hieght - this.yLog - h - cmdH) / 21;
        for (n = 0; n < n4 + 1; ++n) {
            mGraphics2.drawImage(Background.a, this.tUser.x + 65, this.yLog + h + n * 21, 0, false);
        }
        GameScr.sm.paint(mGraphics2);
        for (n = 0; n < GameScr.exs.size(); ++n) {
            ((Explosion)GameScr.exs.elementAt(n)).paint(mGraphics2);
        }
        this.tUser.paint(mGraphics2);
        Font.borderFont.drawString(mGraphics2, Language.id() + ":", this.tUser.x - 59, this.tUser.y + 4, 0, false);
        mGraphics2.setClip(0, 0, CCanvas.width, CCanvas.hieght);
        this.tEmail.y = this.tUser.y + 28;
        Font.borderFont.drawString(mGraphics2, Language.email_phone() + ":", this.tEmail.x - 59, this.tEmail.y, 0);
        this.tEmail.paint(mGraphics2);
        mGraphics2.setClip(0, 0, CCanvas.width, CCanvas.hieght);
        this.tPass.y = this.tUser.y + 56;
        Font.borderFont.drawString(mGraphics2, Language.pass() + ":", this.tPass.x - 59, this.tPass.y, 0);
        this.tPass.paint(mGraphics2);
    }

    private void updateCloud() {
        for (int i = 0; i < 4; ++i) {
            int[] nArray = this.cloudX;
            int n = i;
            nArray[n] = nArray[n] + (this.cloudVX[i] * (2 - i % 2) - this.speed);
            nArray = this.cloudY;
            int n2 = i;
            nArray[n2] = nArray[n2] + this.cloudVY[i] * (2 - i % 2);
            if (this.demoStat == 0 && this.cloudX[i] + Cloud.imgCloud[i % 2].image.getWidth() < 0) {
                this.cloudX[i] = CCanvas.width + 50;
            }
            if (this.demoStat != 1 || this.cloudY[i] + Cloud.imgCloud[i % 2].image.getHeight() >= 0) continue;
            this.cloudY[i] = CCanvas.hieght + 30 * i;
            this.cloudVY[i] = -1;
            this.cloudVX[i] = 0;
            this.cloudX[i] = CCanvas.gameTick % 100 + i * 30;
        }
    }

    public void paint(mGraphics mGraphics2) {
        int n = 4;
        int n2 = n * 32 + 23 + 33;
        if (n2 >= CCanvas.width) {
            n2 = --n * 32 + 23 + 33;
        }
        this.xLog = CCanvas.width / 2 - n2 / 2;
        this.tUser.x = this.xLog + 70;
        this.tUser.y = this.yLog + 25;
        this.tPass.x = this.xLog + 70;
        this.tPass.y = this.yLog + 50;
        this.tEmail.x = this.xLog + 70;
        this.tPass.y = this.yLog + 75;
        if (CCanvas.isTouch) {
            this.tPass.y = this.yLog + 60;
        }
        if (this.isAskSound) {
            this.paintBackG(mGraphics2);
            this.paintAskSound(mGraphics2);
        } else {
            mGraphics2.translate(0, -this.cmy);
            if (this.isDemo) {
                this.paintDemo(mGraphics2);
            }
            if (this.isRegister) {
                this.paintMenuRegisterFreeAccount(mGraphics2);
            } else {
                this.paintMenuLogin(mGraphics2);
            }
            if (isWait && !GameLogicHandler.isServerThongBao) {
                CCanvas.msgdlg.setInfo(Language.download() + " " + currTime + "%", null, null, null);
            }
            GameMidlet.serverInformation(Font.normalFont, mGraphics2);
        }
        super.paint(mGraphics2);
    }

    private void paintAskSound(mGraphics mGraphics2) {
        this.paintCommand(mGraphics2);
        int n = CCanvas.hh - 38;
        Font.borderFont.drawString(mGraphics2, Language.graphicQuality(), CCanvas.hw, n, 2);
        Font.borderFont.drawString(mGraphics2, ConfigScr.graphicText[GameScr.curGRAPHIC_LEVEL], CCanvas.hw, n + 18, 2);
        mGraphics2.drawRegion(PrepareScr.imgReady[3], 0, 0, 13, 11, 4, CCanvas.hw - 30 + CCanvas.gameTick % 3, n + 27, 3, false);
        mGraphics2.drawRegion(PrepareScr.imgReady[3], 0, 0, 13, 11, 7, CCanvas.hw + 30 - CCanvas.gameTick % 3, n + 27, 3, false);
        Font.normalFont.drawString(mGraphics2, Language.soundOn(), 3, CCanvas.hieght - 17, 0);
        Font.normalFont.drawString(mGraphics2, Language.soundOff(), CCanvas.width - 8, CCanvas.hieght - 17, 1);
    }

    public void focusUpdate() {
        if (this.isRegister) {
            if (this.focus == 0) {
                this.tUser.setisFocus(true);
                this.tPass.setisFocus(false);
                this.tEmail.setisFocus(false);
                this.right = this.tUser.cmdClear;
                this.center = this.cmdRegister;
                this.left = this.cmdBack;
            }
            if (this.focus == 1) {
                this.tUser.setisFocus(false);
                this.tPass.setisFocus(true);
                this.tEmail.setisFocus(false);
                this.right = this.tPass.cmdClear;
                this.center = this.cmdRegister;
                this.left = this.cmdBack;
            }
            if (this.focus == 3) {
                this.tUser.setisFocus(false);
                this.tPass.setisFocus(false);
                this.tEmail.setisFocus(true);
                this.right = this.tEmail.cmdClear;
                this.center = this.cmdRegister;
                this.left = this.cmdBack;
            }
        } else {
            if (this.focus == 0) {
                this.tUser.setisFocus(true);
                this.tPass.setisFocus(false);
                this.right = this.tUser.cmdClear;
                this.center = this.cmdSignIn;
            }
            if (this.focus == 1) {
                this.tUser.setisFocus(false);
                this.tPass.setisFocus(true);
                this.right = this.tPass.cmdClear;
                this.center = this.cmdSignIn;
            }
            if (this.focus == 2) {
                this.tUser.setisFocus(false);
                this.tPass.setisFocus(false);
                this.right = null;
                this.center = this.cmdRemember;
            }
        }
    }

    public void onPointerPressed(int n, int n2, int n3) {
        try {
            super.onPointerPressed(n, n2, n3);
            if (this.isMenu) {
                if (CCanvas.keyPressed[2]) {
                    --this.focus;
                    if (this.focus < 0) {
                        this.focus = 2;
                    }
                    this.focusUpdate();
                    CScreen.clearKey();
                }
                if (CCanvas.keyPressed[8]) {
                    ++this.focus;
                    if (this.focus > 2) {
                        this.focus = 0;
                    }
                    CScreen.clearKey();
                    this.focusUpdate();
                }
                if (this.isRegister) {
                    if (CCanvas.isPointer(this.tUser.x, this.tUser.y, this.tUser.width, this.tUser.height, n3)) {
                        this.tUser.textPreferent = this.tUser.getText();
                        if (this.focus != 0) {
                            this.focus = 0;
                        } else {
                            this.tUser.doChangeToTextBox();
                        }
                        this.focusUpdate();
                    }
                    if (CCanvas.isPointer(this.tPass.x, this.tPass.y, this.tPass.width, this.tPass.height, n3)) {
                        this.tPass.textPreferent = this.tPass.getText();
                        if (this.focus != 1) {
                            this.focus = 1;
                        } else {
                            this.tPass.doChangeToTextBox();
                        }
                        this.focusUpdate();
                    }
                    if (CCanvas.isPointer(this.tEmail.x, this.tEmail.y, this.tEmail.width, this.tEmail.height, n3)) {
                        this.tEmail.textPreferent = this.tEmail.getText();
                        if (this.focus != 3) {
                            this.focus = 3;
                        } else {
                            this.tEmail.doChangeToTextBox();
                        }
                        this.focusUpdate();
                    }
                } else {
                    if (CCanvas.isPointer(this.xLog + 140 - this.deltaX, this.tPass.y + 28, 30, 30, n3)) {
                        this.remember();
                    }
                    if (CCanvas.isPointer(this.tUser.x, this.tUser.y, this.tUser.width, this.tUser.height, n3)) {
                        this.tUser.title = Language.signIn();
                        this.tUser.textPreferent = this.tUser.getText();
                        if (this.focus != 0) {
                            this.focus = 0;
                        } else {
                            this.tUser.doChangeToTextBox();
                        }
                        this.focusUpdate();
                    }
                    if (CCanvas.isPointer(this.tPass.x, this.tPass.y, this.tPass.width, this.tPass.height, n3)) {
                        this.tPass.title = Language.signIn();
                        this.tPass.textPreferent = this.tPass.getText();
                        if (this.focus != 1) {
                            this.focus = 1;
                        } else {
                            this.tPass.doChangeToTextBox();
                        }
                        this.focusUpdate();
                    }
                }
            }
            if (this.isAskSound) {
                boolean bl;
                if (!CCanvas.keyPressed[4]) {
                    bl = keyLeft;
                }
                if (!CCanvas.keyPressed[6]) {
                    bl = keyRight;
                }
                bl = CCanvas.isPointerClick[n3];
            } else if (this.isDemo) {
                if (CCanvas.keyPressed[13] || CCanvas.isPointer(CCanvas.hw, CCanvas.hh - 25, 50, 50, n3)) {
                    this.isForward = true;
                    LoginScr.clearKey();
                }
            } else if (this.isForward) {
                return;
            }
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void onPointerReleased(int n, int n2, int n3) {
        super.onPointerReleased(n, n2, n3);
        if (this.isAskSound) {
            if (CCanvas.isPointer(CCanvas.hw - 50, CCanvas.hh - 25, 50, 50, n3)) {
                if ((GameScr.curGRAPHIC_LEVEL = (byte)(GameScr.curGRAPHIC_LEVEL + 1)) > 2) {
                    GameScr.curGRAPHIC_LEVEL = 0;
                }
                RMS.saveRMSInt("Graphic", GameScr.curGRAPHIC_LEVEL);
                LoginScr.clearKey();
            }
            if (CCanvas.isPointer(CCanvas.hw, CCanvas.hh - 25, 50, 50, n3)) {
                if ((GameScr.curGRAPHIC_LEVEL = (byte)(GameScr.curGRAPHIC_LEVEL - 1)) < 0) {
                    GameScr.curGRAPHIC_LEVEL = (byte)2;
                }
                RMS.saveRMSInt("Graphic", GameScr.curGRAPHIC_LEVEL);
                LoginScr.clearKey();
            }
        }
        if (!this.isDemo) {
            if (!this.isRegister && CCanvas.isPointer(this.xLog + 10 - this.deltaX - (Language.language == 0 ? 0 : 30), this.tPass.y + 35 + 10, 100, 25, n3)) {
                if (CCanvas.curScr == CCanvas.loginScr) {
                    CCanvas.inputDlg.setInfo(Language.id(), new IAction(){

                        public void perform() {
                            String string = CCanvas.inputDlg.tfInput.getText();
                            if (!string.equals("")) {
                                LoginScr.this.doForgetPass(string);
                            }
                        }
                    }, new IAction(){

                        public void perform() {
                            CCanvas.endDlg();
                        }
                    }, 3);
                    CCanvas.inputDlg.show();
                } else {
                    this.doForgetPass(TerrainMidlet.myInfo.name);
                }
            }
            if (CCanvas.isPointer(this.xLog + 120 - this.deltaX - (Language.language == 0 ? 0 : 30), this.tPass.y + 35 + 10, 100, 25, n3)) {
                if (GameMidlet.server == 2) {
                    this.isRegister = true;
                    this.left = this.cmdBack;
                    this.focus = 0;
                    this.tUser.setisFocus(true);
                    this.tPass.setisFocus(false);
                    this.tEmail.setisFocus(false);
                    this.center = this.cmdRegister;
                    this.right = null;
                    this.setRegister();
                } else {
                    this.doRegister();
                }
            }
        }
    }

    public void resetTF() {
        this.tUser.x = -this.xC;
        this.tPass.x = CCanvas.width + this.xC;
        this.tEmail.x = -this.xC;
    }

    public void initSignIn() {
        this.logoDes = CCanvas.width >= 200 ? this.yLog - 40 : this.tUser.y - 40;
        this.focus = 0;
        this.cmdSignIn = new Command(Language.signIn(), new IAction(){

            public void perform() {
                LoginScr.this.doLogin();
            }
        });
        this.cmdMenu = new Command("Menu", new IAction(){

            public void perform() {
                Vector<Command> vector = new Vector<Command>();
                vector.addElement(new Command(Language.callhotline(), new IAction(){

                    public void perform() {
                        if (LoginScr.this.numSupport.equals("")) {
                            if (!Session_ME.gI().isConnected()) {
                                CCanvas.startWaitDlgWithoutCancel(Language.connecting(), 10);
                                LoginScr.this.connect();
                            } else {
                                CCanvas.startWaitDlg(Language.pleaseWait());
                            }
                            GameService.gI().requestService((byte)5, null);
                        }
                    }
                }));
                vector.addElement(new Command(Language.xoadulieu(), new IAction(){

                    public void perform() {
                        CRes.delRMS();
                    }
                }));
                vector.addElement(new Command(Language.chonmaychu(), new IAction(){

                    public void perform() {
                        GameService.gI().disconnect();
                        CCanvas.serverListScreen.show();
                    }
                }));
                if (CCanvas.isDebugging()) {
                    vector.addElement(new Command(Language.backVersion(), new IAction(){

                        public void perform() {
                            GameMidlet.versionByte = (short)239;
                            GameMidlet.version = "2.3.9";
                            GameService.gI().disconnect();
                            GameMidlet.doUpdateServer();
                            CCanvas.serverListScreen.show();
                        }
                    }));
                }
                vector.addElement(new Command(Language.exit(), new IAction(){

                    public void perform() {
                        GameMidlet.exit();
                        System.exit(-1);
                        GameService.gI().disconnect();
                        Session_ME.gI().close(1111);
                    }
                }));
                CCanvas.menu.startAt(vector, 0);
            }
        });
        this.cmdForward = new Command(Language.forward(), new IAction(){

            public void perform() {
                LoginScr.this.isForward = true;
            }
        });
        this.cmdSelect = new Command(Language.select(), new IAction(){

            public void perform() {
                LoginScr.this.isAskSound = false;
                LoginScr.this.isDemo = true;
                LoginScr.this.center = null;
                LoginScr.this.right = LoginScr.this.cmdForward;
                RMS.saveRMSInt("Graphic", GameScr.curGRAPHIC_LEVEL);
            }
        });
        this.cmdRemember = new Command(Language.remember(), new IAction(){

            public void perform() {
                LoginScr.this.remember();
            }
        });
        this.cmdYes = new Command(Language.soundOn(), new IAction(){

            public void perform() {
                LoginScr.this.isAskSound = false;
                LoginScr.this.isDemo = true;
                mSound.setVolume(volume);
                CRes.saveRMSInt("sound", volume);
                LoginScr.this.right = LoginScr.this.cmdForward;
                LoginScr.this.left = null;
                LoginScr.clearKey();
            }
        });
        this.cmdNo = new Command(Language.soundOff(), new IAction(){

            public void perform() {
                LoginScr.this.isAskSound = false;
                LoginScr.this.isDemo = true;
                volume = 0;
                mSound.setVolume(0);
                CRes.saveRMSInt("sound", volume);
                LoginScr.this.right = LoginScr.this.cmdForward;
                LoginScr.this.left = null;
                LoginScr.clearKey();
            }
        });
        this.cmdBack = new Command(Language.back(), new IAction(){

            public void perform() {
                LoginScr.this.isRegister = false;
                LoginScr.this.isDemo = false;
                LoginScr.this.isMenu = true;
                LoginScr.this.isForward = false;
                LoginScr.this.logoII = true;
                LoginScr.this.cmy = 0;
                LoginScr.this.right = ((LoginScr)LoginScr.this).tUser.cmdClear;
                LoginScr.this.left = LoginScr.this.cmdMenu;
                LoginScr.this.center = LoginScr.this.cmdSignIn;
                LoginScr.clearKey();
            }
        });
        this.cmdRegister = new Command(Language.reg(), new IAction(){

            public void perform() {
                charName = LoginScr.this.tUser.getText().toLowerCase();
                password = LoginScr.this.tPass.getText().toLowerCase();
                email_phone = LoginScr.this.tEmail.getText().toLowerCase();
                if (!(charName.equals("") || email_phone.equals("") || password.equals(""))) {
                    GameService.gI().requestRegister(charName, email_phone, password);
                    CCanvas.startWaitDlg(Language.pleaseWait());
                } else {
                    CCanvas.msgdlg.setInfo(Language.dangkyFail(), null, new Command("OK", new IAction(){

                        public void perform() {
                            CCanvas.endDlg();
                            LoginScr.this.focus = 0;
                            LoginScr.this.tUser.setisFocus(true);
                            LoginScr.this.tPass.setisFocus(false);
                            LoginScr.this.tEmail.setisFocus(false);
                        }
                    }), null);
                    CCanvas.msgdlg.show();
                }
            }
        });
        if (GameScr.curGRAPHIC_LEVEL != 2) {
            GameScr.mm.createBackGround();
        }
        if (this.isAskSound && RMS.loadRMSInt("Graphic") != -1) {
            this.isDemo = true;
            this.isAskSound = false;
            this.right = this.cmdForward;
        }
        if (RMS.loadRMSInt("Graphic") == -1) {
            this.isAskSound = true;
            this.center = this.cmdSelect;
        }
    }

    static {
        currTime = 0;
        maxTime = 15;
        isLoadData = false;
        try {
            new FilePack(CCanvas.getClassPathConfig(CONFIG.PATH_GUI + "gui"));
            imgCheck = mImage.createImage("/remember.png");
            imgPlane = Background.bigBalloon;
            missile = Bullet.rocket2;
            Object var0 = null;
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
        charName = "";
        email_phone = "";
        password = "";
        volume = 50;
    }
}

