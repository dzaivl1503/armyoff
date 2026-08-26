/*
 * Decompiled with CFR 0.152.
 */
package screen;

import CLib.Image;
import CLib.mGraphics;
import CLib.mImage;
import CLib.mSystem;
import Equipment.Equip;
import com.teamobi.mobiarmy2.GameMidlet;
import com.teamobi.mobiarmy2.MotherCanvas;
import com.teamobi.mobiarmy2.OfflineBossFight;
import com.teamobi.mobiarmy2.OfflineBotUfo;
import com.teamobi.mobiarmy2.OfflineBulletAssets;
import com.teamobi.mobiarmy2.OfflineCombat;
import com.teamobi.mobiarmy2.OfflineItemLogic;
import com.teamobi.mobiarmy2.OfflineLuckyGift;
import com.teamobi.mobiarmy2.OfflineSave;
import com.teamobi.mobiarmy2.OfflineSettings;
import coreLG.CCanvas;
import coreLG.CONFIG;
import coreLG.TerrainMidlet;
import effect.Camera;
import effect.Effect;
import effect.Explosion;
import effect.GiftEffect;
import effect.SmokeManager;
import effect.Snow;
import item.BM;
import item.Bullet;
import item.Item;
import java.util.Vector;
import map.Background;
import map.CMap;
import map.MM;
import map.MiniMap;
import model.CRes;
import model.CTime;
import model.ChatPopup;
import model.FilePack;
import model.Font;
import model.FrameImage;
import model.IAction;
import model.IAction2;
import model.Language;
import model.TField;
import model.TimeBomb;
import network.Command;
import network.GameService;
import network.Session_ME;
import player.Boss;
import player.CPlayer;
import player.PM;
import screen.CScreen;
import screen.MenuScr;
import screen.PrepareScr;

public class GameScr
extends CScreen {
    static int WIDTH = 1000;
    public static int HEIGHT = 1000;
    public static final byte GRAPHIC_HIGH = 0;
    public static final byte GRAPHIC_MEDIUM = 1;
    public static final byte GRAPHIC_LOW = 2;
    public static byte curGRAPHIC_LEVEL = 0;
    public static boolean whiteEffect;
    public static boolean electricEffect;
    public static boolean freezeEffect;
    public static boolean suicideEffect;
    public static boolean poisonEffect;
    public boolean nukeEffect;
    public int tN;
    public int tW = 0;
    public int wE;
    public int xE;
    public int tE = 0;
    public static int xNuke;
    public static int yNuke;
    public static int yElectric;
    public static int xElectric;
    public static int xFreeze;
    public static int yFreeze;
    public static int xSuicide;
    public static int ySuicide;
    public static int xPoison;
    public static int yPoison;
    public static mImage airFighter;
    public static mImage imgMode;
    public static mImage lock;
    public static mImage lockImg;
    public static mImage crosshair;
    public static mImage imgInfoPopup;
    public static mImage s_imgITEM;
    public static mImage imgTeam;
    public static mImage imgPlane;
    public static mImage logoGame;
    public static mImage logoII;
    public static mImage imgQuanHam;
    public static mImage imgBack;
    public static mImage imgMap;
    public static mImage trangbiTileImg;
    public static mImage shopTileImg;
    public static mImage tienBarImg;
    public static mImage soLuongBarImg;
    public static mImage buyBar;
    public static mImage ladySexyImg;
    public static mImage imgCurPos;
    public static mImage imgSmallCloud;
    public static mImage imgArrowRed;
    public static mImage imgRoomStat;
    public static mImage imgTrs;
    public static mImage imgIcon;
    public static mImage itemBarImg;
    public static mImage imgChat;
    public static mImage s_imgTransparent;
    public static mImage arrowMenu;
    public static mImage wind1;
    public static mImage wind2;
    public static mImage wind3;
    public static mImage trai;
    public static mImage phai;
    public static mImage crossHair2;
    public static mImage[] imgReady;
    public static mImage[] imgMsg;
    public static Vector timeBombs;
    public static TField tfChat;
    public static MM mm;
    public static PM pm;
    public static Camera cam;
    public static BM bm;
    public static Vector exs;
    public static SmokeManager sm;
    public static CTime time;
    public Vector vGift = new Vector();
    public static int windx;
    public static int windy;
    int teamSize;
    int mapID;
    public static boolean trainingMode;
    mImage pause;
    public static int tickCount;
    public static byte ID_Turn;
    public static mImage s_imgAngle;
    public static FrameImage s_frBar;
    public static FrameImage s_frWind;
    public static boolean isDarkEffect;
    public static int s_iPlane_x;
    public static int s_iPlane_y;
    public static int s_iBombTargetX;
    public static byte room;
    public static byte board;
    byte exBonus;
    int moneyBonus;
    int luongBonus;
    String itemBonusText;
    int expBonus;
    int moneyY = -100;
    public static String res;
    int moneyBonus2;
    int moneyY2;
    boolean isMoney2Fly;
    int whoGetMoney2;
    boolean isMoneyFly;
    int nBoLuot;
    private boolean isSelectItem;
    public static int curItemSelec;
    public long timeDelayClosePauseMenu;
    private static final String CONTINUE_RESULT_LABEL = "Ti\u1ebfp t\u1ee5c";
    public static byte myIndex;
    Vector chatList = new Vector();
    int chatDelay;
    int MAX_CHAT_DELAY = 40;
    Snow snow;
    public static boolean iconOnOf;
    public boolean isShowPausemenu;
    public long timeShowPauseMenu;
    int chatWait = 0;
    boolean isChat;
    public boolean isFly;
    public String text = "";
    public int xFly;
    public int yFly;
    public int tFly;
    public Equip equip;
    public static int trainingStep;
    public static boolean isUpdateHP;
    int left = 0;
    int right = 1;
    int up = 2;
    int down = 3;
    public static boolean cantSee;
    public byte whoCantSee;
    public static int xL;
    public static int yL;
    public static int xR;
    public static int yR;
    public static int xF;
    public static int yF;
    public static int xU;
    public static int yU;
    public static int xD;
    public static int yD;
    static mImage imgArrow;
    public static int windAngle;
    public static int windPower;
    public int t1;
    public int t2;
    public int dem;
    boolean b;
    public static byte[] num;
    boolean isPressXL;
    boolean isPressXR;
    boolean isPressXF;
    private static final int ITEM_SELECT_COLUMNS = 4;
    private static final int ITEM_SELECT_TOUCH_CELL = 40;
    private static final int ITEM_SELECT_ICON_SIZE = 16;
    private static final int ITEM_SELECT_KEYPAD_GAP = 2;
    private static final int ITEM_SELECT_POPUP_HEIGHT = 130;
    private static final int MENU_BTN_X = 3;
    private static final int MENU_BTN_Y = 3;
    private static final int MENU_BTN_SIZE = 22;
    private static final int CAMERA_BTN_X = 28;
    private static final int CAMERA_BTN_Y = 3;
    private static final int CAMERA_BTN_SIZE = 22;
    private static final int HOTBAR_SLOT = 20;
    private static final int POW_ITEM_ICON_ID = 38;

    public static void initMenuEffects() {
        if (sm == null) {
            sm = new SmokeManager();
            sm.addSmoke(-100, -100, (byte)19);
        }
        if (exs == null) {
            exs = new Vector();
            new Explosion(-100, -100, 0);
        }
        if ((curGRAPHIC_LEVEL = (byte)CRes.loadRMSInt("Graphic")) == -1) {
            curGRAPHIC_LEVEL = 1;
        }
        CMap.isDrawRGB = CRes.loadRMSInt("drawRGB") == 0;
    }

    public static boolean useLegacyTouchButtons() {
        return CCanvas.gamePad == null;
    }

    public GameScr() {
        this.initGamescr();
    }

    public void initGamescr() {
        mm = new MM();
        pm = new PM();
        pm.init();
        cam = new Camera();
        bm = new BM();
        sm = new SmokeManager();
        time = new CTime();
        tfChat = new TField();
        GameScr.tfChat.x = 2;
        GameScr.tfChat.y = CCanvas.hieght - ITEM_HEIGHT - 25;
        if (CCanvas.isTouch) {
            GameScr.tfChat.y = CCanvas.hieght - CScreen.cmdH - ITEM_HEIGHT;
        }
        GameScr.tfChat.width = CCanvas.width - 4;
        GameScr.tfChat.height = ITEM_HEIGHT + 2;
        tfChat.setisFocus(true);
        GameScr.tfChat.nameDebug = "Tfield ====> Gamescr";
        this.nameCScreen = "GameScr screen!";
    }

    public boolean isShowingResult() {
        return res != null && !res.equals("");
    }

    public boolean isSelectingItem() {
        return this.isSelectItem;
    }

    private static int getItemSelectCount() {
        CPlayer cPlayer = PM.getMyPlayer();
        return cPlayer == null || cPlayer.item == null ? 0 : cPlayer.item.length;
    }

    private static int getItemSelectRows() {
        int n = GameScr.getItemSelectCount();
        return n <= 0 ? 1 : (n + 4 - 1) / 4;
    }

    private static int getTouchItemGridX() {
        return CCanvas.hw - 80;
    }

    private static int getTouchItemGridY() {
        return CCanvas.hh - GameScr.getItemSelectRows() * 40 / 2;
    }

    private static int getTouchItemIconX() {
        return GameScr.getTouchItemGridX() + 12;
    }

    private static int getTouchItemIconY() {
        return GameScr.getTouchItemGridY() + 12;
    }

    private static int getItemSelectPopupY() {
        return CCanvas.hh - 65;
    }

    private void moveItemSelection(int n) {
        if (PM.getMyPlayer() == null) {
            return;
        }
        int n2 = PM.getMyPlayer().item.length;
        if (n2 <= 0) {
            return;
        }
        if ((curItemSelec += n) < 0) {
            curItemSelec = n2 - 1;
        }
        if (curItemSelec >= n2) {
            curItemSelec = 0;
        }
    }

    private void closeItemSelect() {
        this.isSelectItem = false;
        this.timeDelayClosePauseMenu = mSystem.currentTimeMillis() + 300L;
        CScreen.clearKey();
    }

    private void confirmSelectedItem() {
        if (PM.getMyPlayer() == null || !this.isSelectItem) {
            return;
        }
        int[] nArray = PM.getMyPlayer().item;
        if (curItemSelec < 0 || curItemSelec >= nArray.length) {
            return;
        }
        int n = nArray[curItemSelec];
        int n2 = curItemSelec;
        if (n < 0 || n > 37) {
            return;
        }
        if (trainingMode) {
            this.closeItemSelect();
            PM.getMyPlayer().UseItem(n, true, n2);
            if (n == 0) {
                PM.p[0].hp += 30;
            }
            return;
        }
        if (PM.getMyPlayer().itemUsed != -1 || PM.getMyPlayer().isUsedItem) {
            return;
        }
        if (!pm.isYourTurn()) {
            return;
        }
        if (PrepareScr.currLevel == 7 && num[curItemSelec] == 0) {
            return;
        }
        this.closeItemSelect();
        PM.getMyPlayer().UseItem(n, false, n2);
    }

    private void handleItemPadInput() {
        if (CCanvas.keyPressed[2]) {
            this.moveItemSelection(-Item.iWitdh);
        }
        if (CCanvas.keyPressed[8]) {
            this.moveItemSelection(Item.iWitdh);
        }
        if (CCanvas.keyPressed[4]) {
            this.moveItemSelection(-1);
        }
        if (CCanvas.keyPressed[6]) {
            this.moveItemSelection(1);
        }
        if (CCanvas.keyPressed[5]) {
            this.confirmSelectedItem();
        }
        if (CCanvas.keyPressed[13] || CCanvas.keyPressed[12]) {
            this.closeItemSelect();
        }
    }

    private boolean isContinuePointer(int n, int n2, int n3) {
        int n4 = CCanvas.hieght - 18;
        int n5 = Font.borderFont.getWidth(CONTINUE_RESULT_LABEL) + 40;
        int n6 = Font.borderFont.getHeight() + 12;
        return CCanvas.isPointer(CCanvas.hw - n5 / 2, n4 - n6, n5, n6, n3);
    }

    public void continueAfterResult() {
        if (!this.isShowingResult()) {
            return;
        }
        byte by = OfflineBossFight.currentRoomIndex;
        res = "";
        this.isMoneyFly = false;
        this.isMoney2Fly = false;
        Session_ME.receiveSynchronized = 0;
        CScreen.isSetClip = true;
        if (trainingMode) {
            GameService.gI().training((byte)1);
            trainingMode = false;
            CCanvas.menuScr.show();
            return;
        }
        MiniMap.leaveBattle();
        if (PrepareScr.isPvpBotRoom && OfflineLuckyGift.consumePending()) {
            OfflineLuckyGift.startAfterWin();
        } else if (PrepareScr.isPvpBotRoom) {
            PrepareScr.isPvpBotRoom = false;
            GameMidlet.openPvpBotSetup();
        } else if (by >= 0 && by < MenuScr.BOSS_ROOM_NAMES.length) {
            GameMidlet.openOfflineBossRoom(by);
        } else {
            GameMidlet.openOfflineBossRoomList();
        }
    }

    public void initGame(byte by, byte by2, short[] sArray, short[] sArray2, short[] sArray3, int n) {
        isDarkEffect = false;
        s_iPlane_x = -1;
        s_iPlane_y = -1;
        s_iBombTargetX = -1;
        this.isMoneyFly = false;
        res = "";
        this.nBoLuot = 3;
        iconOnOf = true;
        if (curGRAPHIC_LEVEL != 2) {
            mm.createBackGround();
        }
        switch (curGRAPHIC_LEVEL) {
            case 0:
            case 1: {
                if (Background.isLoadImage) break;
                Background.isLoadImage = true;
                Background.initImage();
                break;
            }
            case 2: {
                Background.removeImage();
            }
        }
        this.initGamescr();
        pm.init();
        CPlayer.isStopFire = false;
        exs = new Vector();
        timeBombs = new Vector();
        if (n != 0) {
            CCanvas.gameScr.flyText("+" + n + Language.diemdongdoi(), CCanvas.width / 2, CCanvas.hieght - 50, null);
        }
        time.initTimeInterval(by2);
        this.snow = null;
        if (curGRAPHIC_LEVEL != 2) {
            if (Background.curBGType == 2) {
                this.snow = new Snow();
                this.snow.startSnow(0);
            }
            if (Background.curBGType == 10) {
                this.snow = new Snow();
                if (MM.mapID == 34) {
                    this.snow.waterY = 35;
                }
                if (MM.mapID == 35) {
                    this.snow.waterY = 30;
                }
                if (MM.mapID == 38) {
                    this.snow.waterY = 80;
                }
                if (MM.mapID == 39) {
                    this.snow.waterY = 0;
                }
                this.snow.startSnow(1);
            }
        }
        pm.initPlayer(sArray, sArray2, sArray3);
        for (int i = 0; i < PM.p.length; ++i) {
            if (PM.p[i] == null) continue;
            PM.p[i].cantSee = false;
        }
        CPlayer cPlayer = PM.getMyPlayer();
        if (cPlayer != null) {
            OfflineBulletAssets.prepareCombat(cPlayer);
        }
        cantSee = false;
        Bullet.webId = 200;
        MiniMap.enterBattle();
    }

    private void onDragCamera(int n, int n2, int n3) {
        if (Camera.mode == 0) {
            int n4 = n - CCanvas.pxFirst[n3];
            int n5 = n2 - CCanvas.pyFirst[n3];
            if (n4 > 1) {
                Camera.dx2 -= Math.abs(n4) >> 2;
            } else if (n4 < -1) {
                Camera.dx2 += Math.abs(n4) >> 2;
            }
            if (n5 > 1) {
                Camera.dy2 -= Math.abs(n5) >> 2;
            } else if (n5 < -1) {
                Camera.dy2 += Math.abs(n5) >> 2;
            }
        }
    }

    public void selectedItemPanelRealeased(int n, int n2, int n3) {
        int n4;
        int n5;
        int n6;
        int n7;
        int n8;
        int n9 = GameScr.getItemSelectCount();
        if (n9 <= 0) {
            this.closeItemSelect();
            return;
        }
        int n10 = GameScr.getTouchItemGridX();
        if (CCanvas.isPointer(n10, n8 = GameScr.getTouchItemGridY(), n7 = 160, n6 = GameScr.getItemSelectRows() * 40, n3) && (n5 = (n2 - n8) / 40 * 4 + (n - n10) / 40) >= 0 && n5 < n9) {
            if (n5 != curItemSelec) {
                curItemSelec = n5;
            } else if (CCanvas.isDoubleClick) {
                this.confirmSelectedItem();
            }
        }
        if (!CCanvas.isPointer(n4 = CCanvas.hw - (n5 = 184) / 2, GameScr.getItemSelectPopupY(), n5, 130, n3) && this.isSelectItem) {
            this.closeItemSelect();
            this.timeDelayClosePauseMenu = mSystem.currentTimeMillis() + 550L;
        }
    }

    public void flyText(String string, int n, int n2, Equip equip) {
        this.isFly = true;
        this.text = string;
        this.xFly = n;
        this.yFly = n2;
        this.equip = equip;
    }

    protected void doSetForce() {
        int curF = PM.getMyPlayer() != null && PM.getMyPlayer().maxforce > 0 ? PM.getMyPlayer().maxforce : 30;
        CCanvas.inputDlg.setInfo("L\u1ef1c max (1-30)", new IAction(){

            public void perform() {
                boolean needForce2 = false;
                try {
                    String str = CCanvas.inputDlg.tfInput.getText();
                    if (str != null && str.trim().length() > 0) {
                        int f = Integer.parseInt(str.trim());
                        if (f < 1) f = 1;
                        if (f > 30) f = 30;
                        if (PM.getMyPlayer() != null) {
                            PM.getMyPlayer().maxforce = f;
                        }
                    }
                    if (PM.getMyPlayer() != null && (PM.getMyPlayer().gun == 6 || PM.getMyPlayer().gun == 8)) {
                        needForce2 = true;
                    }
                }
                catch (Exception exception) {
                    if (PM.getMyPlayer() != null) {
                        PM.getMyPlayer().maxforce = 30;
                    }
                }
                finally {
                    CCanvas.endDlg();
                }

                if (needForce2) {
                    int curF2 = PM.getMyPlayer().maxforce2 > 0 ? PM.getMyPlayer().maxforce2 : 30;
                    CCanvas.inputDlg.setInfo("L\u1ef1c max 2 (1-30)", new IAction(){

                        public void perform() {
                            try {
                                String str2 = CCanvas.inputDlg.tfInput.getText();
                                if (str2 != null && str2.trim().length() > 0) {
                                    int f2 = Integer.parseInt(str2.trim());
                                    if (f2 < 1) f2 = 1;
                                    if (f2 > 30) f2 = 30;
                                    if (PM.getMyPlayer() != null) {
                                        PM.getMyPlayer().maxforce2 = f2;
                                    }
                                }
                            }
                            catch (Exception exception) {
                                if (PM.getMyPlayer() != null) {
                                    PM.getMyPlayer().maxforce2 = 30;
                                }
                            }
                            finally {
                                CCanvas.endDlg();
                            }
                        }
                    }, new IAction(){

                        public void perform() {
                            CCanvas.endDlg();
                        }
                    }, 1);
                    CCanvas.inputDlg.tfInput.setText("" + curF2);
                    CCanvas.inputDlg.show();
                }
            }
        }, new IAction(){

            public void perform() {
                CCanvas.endDlg();
            }
        }, 1);
        CCanvas.inputDlg.tfInput.setText("" + curF);
        CCanvas.inputDlg.show();
    }

    public void doShowPauseMenu() {
        if (this.isShowingResult()) {
            return;
        }
        this.isShowPausemenu = true;
        Vector<Command> vector = new Vector<Command>();
        vector.addElement(new Command(Language.CONTINUE(), new IAction(){

            public void perform() {
                GameScr.this.isShowPausemenu = false;
                GameScr.this.timeShowPauseMenu = mSystem.currentTimeMillis() + 300L;
            }
        }));
        vector.addElement(new Command("L\u1ef0C MAX", new IAction(){

            public void perform() {
                GameScr.this.isShowPausemenu = false;
                GameScr.this.timeShowPauseMenu = mSystem.currentTimeMillis() + 300L;
                GameScr.this.doSetForce();
            }
        }));
        if (pm.isYourTurn() && !trainingMode && !BM.active && PM.getMyPlayer().active && this.nBoLuot > 0) {
            vector.addElement(new Command(Language.SKIP(), new IAction(){

                public void perform() {
                    time.skipTurn();
                    --GameScr.this.nBoLuot;
                    GameScr.this.isShowPausemenu = false;
                    GameScr.this.timeShowPauseMenu = mSystem.currentTimeMillis() + 300L;
                }
            }));
        }
        vector.addElement(new Command("T\u1ed0C \u0110\u1ed8: " + OfflineSettings.getCurrentSpeedLabel(), new IAction(){

            public void perform() {
                GameScr.this.isShowPausemenu = false;
                GameScr.this.timeShowPauseMenu = mSystem.currentTimeMillis() + 300L;
                OfflineSettings.openSpeedInputPopup();
            }
        }));
        vector.addElement(new Command("L\u01afU GAME", new IAction(){

            public void perform() {
                OfflineSave.save();
                CCanvas.startOKDlg("\u0110\u00e3 l\u01b0u game!");
                GameScr.this.isShowPausemenu = false;
                GameScr.this.timeShowPauseMenu = mSystem.currentTimeMillis() + 300L;
            }
        }));
        if (OfflineCombat.isActiveBattle()) {
            vector.addElement(new Command(Language.LEAVEBATTLE(), new IAction(){

                public void perform() {
                    GameScr.this.isShowPausemenu = false;
                    CCanvas.startYesNoDlg(Language.wantExit(), new IAction(){

                        public void perform() {
                            CCanvas.endDlg();
                            GameMidlet.leaveOfflineBattle();
                        }
                    });
                }
            }));
        }
        CCanvas.pausemenu.startAt(vector);
    }

    public void doShowSpeedMenu() {
        this.isShowPausemenu = true;
        Vector<Command> vector = new Vector<Command>();
        for (int i = 0; i < OfflineSettings.FPS_OPTIONS.length; ++i) {
            final int speedIdx = i;
            String prefix = (i == OfflineSettings.getFpsIndex()) ? "[V] " : "     ";
            vector.addElement(new Command(prefix + OfflineSettings.SPEED_LABELS[i], new IAction(){

                public void perform() {
                    OfflineSettings.saveFpsIndex(speedIdx);
                    GameScr.this.isShowPausemenu = false;
                    GameScr.this.timeShowPauseMenu = mSystem.currentTimeMillis() + 300L;
                    CCanvas.startOKDlg("\u0110\u00e3 \u0111\u1ed5i t\u1ed1c \u0111\u1ed9 game: " + OfflineSettings.getCurrentSpeedLabel());
                }
            }));
        }
        CCanvas.pausemenu.startAt(vector);
    }

    public void addTimeBomb(TimeBomb timeBomb) {
        timeBombs.addElement(timeBomb);
        GameScr.waitting();
    }

    public void explodeTimeBomb(int n) {
        for (int i = 0; i < timeBombs.size(); ++i) {
            TimeBomb timeBomb = (TimeBomb)timeBombs.elementAt(i);
            if (timeBomb.id != n) continue;
            timeBomb.isExplore = true;
            mm.makeHole(timeBomb.x, timeBomb.y, (byte)57, 9);
            return;
        }
        GameScr.waitting();
    }

    public static void waitting() {
        CTime.seconds += 2;
        CCanvas.tNotify = 0;
        CCanvas.lockNotify = true;
        if (CCanvas.curScr == CCanvas.gameScr) {
            Session_ME.receiveSynchronized = 1;
        }
    }

    public void exitGiuaChung() {
        if (pm != null && PM.p != null && PM.p[myIndex] != null) {
            GameService.gI().leaveBoard();
            CScreen.isSetClip = true;
            return;
        }
    }

    public void doExit() {
        MiniMap.leaveBattle();
        for (int i = 0; i < PM.MAX_PLAYER; ++i) {
            if (PM.p[i] == null) continue;
            PM.p[i] = null;
        }
        CCanvas.prepareScr.show();
        Session_ME.receiveSynchronized = 0;
    }

    public void update() {
        int n;
        if (trainingMode) {
            this.doTraining();
        }
        if (this.chatWait > 0) {
            --this.chatWait;
        }
        tfChat.update();
        this.updateChat();
        bm.update();
        pm.update();
        sm.update();
        cam.update();
        if (this.snow != null) {
            this.snow.update();
        }
        for (n = 0; n < timeBombs.size(); ++n) {
            TimeBomb timeBomb = (TimeBomb)timeBombs.elementAt(n);
            if (timeBomb == null) continue;
            timeBomb.update();
        }
        for (n = 0; n < exs.size(); ++n) {
            ((Explosion)exs.elementAt(n)).update();
        }
        time.update();
        if (++tickCount > 10000) {
            tickCount = 0;
        }
        if (this.isMoneyFly) {
            --this.moneyY;
            if (this.moneyY < 50) {
                this.isMoneyFly = false;
                this.moneyY = h / 2 - 15;
            }
        }
        if (this.isMoney2Fly) {
            --this.moneyY2;
            if (this.moneyY2 < PM.p[this.whoGetMoney2].y + 100) {
                this.isMoney2Fly = false;
            }
        }
        if (this.vGift.size() != 0) {
            ++this.tFly;
            if (this.tFly == 10) {
                for (n = 0; n < this.vGift.size(); ++n) {
                    if (((GiftEffect)this.vGift.elementAt((int)n)).isFly) continue;
                    ((GiftEffect)this.vGift.elementAt((int)n)).isFly = true;
                    break;
                }
                this.tFly = 0;
            }
        }
        for (n = 0; n < this.vGift.size(); ++n) {
            ((GiftEffect)this.vGift.elementAt(n)).update();
        }
    }

    public void mainLoop() {
        super.mainLoop();
        mm.update();
        MiniMap.update();
        this.applyFreeCameraKeys();
        cam.mainLoop();
    }

    private void applyFreeCameraKeys() {
        if (Camera.mode != 0 || CCanvas.pausemenu.isShow || CCanvas.currentDialog != null) {
            return;
        }
        int n = 10;
        if (CCanvas.keyHold[4]) {
            Camera.dx2 -= n;
        }
        if (CCanvas.keyHold[6]) {
            Camera.dx2 += n;
        }
        if (CCanvas.keyHold[2]) {
            Camera.dy2 -= n;
        }
        if (CCanvas.keyHold[8]) {
            Camera.dy2 += n;
        }
    }

    private void doTraining() {
        switch (trainingStep) {
            case 0: {
                if (PM.p[0].falling) break;
                trainingStep = -1;
                CCanvas.startOKDlg(Language.training1(), new IAction(){

                    public void perform() {
                        CCanvas.startOKDlg(Language.training2(), new IAction(){

                            public void perform() {
                                trainingStep = 1;
                            }
                        });
                    }
                });
                break;
            }
            case 1: {
                if (PM.p[0].movePoint <= 20) break;
                trainingStep = -1;
                CCanvas.startOKDlg(Language.trainin3(), new IAction(){

                    public void perform() {
                        CCanvas.startOKDlg(Language.training4(), new IAction(){

                            public void perform() {
                                CCanvas.startOKDlg(Language.training5(), new IAction(){

                                    public void perform() {
                                        trainingStep = 2;
                                    }
                                });
                            }
                        });
                    }
                });
                break;
            }
            case 2: {
                if (PM.p[1].y <= 514) break;
                trainingStep = -1;
                CCanvas.startOKDlg(Language.training6(), new IAction(){

                    public void perform() {
                        trainingStep = 3;
                    }
                });
                break;
            }
            case 3: {
                if (PM.getMyPlayer().hp != 100 && PM.p[1].y <= MM.mapHeight && PM.getMyPlayer().y <= MM.mapHeight) break;
                trainingStep = -1;
                CCanvas.startOKDlg(Language.training7(), new IAction(){

                    public void perform() {
                        trainingStep = 0;
                        trainingMode = false;
                        GameService.gI().training((byte)1);
                        CCanvas.menuScr.show();
                    }
                });
            }
        }
    }

    public void activeMoney2Fly(int n, int n2) {
        if (PM.p[PM.getIndexByIDDB(n2)] != null && PM.p != null) {
            if (n > 0) {
                this.showChat(n2, " +" + n + Language.xu());
            } else {
                this.showChat(n2, " " + n + Language.xu());
            }
        }
    }

    public void setWin(byte by, byte by2, int n, int n2) {
        this.setWin(by, by2, n, n2, "");
    }

    public void setWin(byte by, byte by2, int n, int n2, String string) {
        this.setWin(by, by2, n);
        this.luongBonus = n2;
        this.itemBonusText = string;
    }

    public void setExpBonus(int n) {
        this.expBonus = n;
    }

    public void setWin(byte by, byte by2, int n) {
        this.chatList.removeAllElements();
        this.exBonus = by2;
        this.expBonus = 0;
        this.moneyBonus = n;
        this.luongBonus = 0;
        this.itemBonusText = "";
        this.moneyY = CScreen.h / 2;
        this.isMoneyFly = true;
        time.stop();
        if (by == 0) {
            res = Language.RAW();
            pm.setPlayerAfterDraw();
        } else if (PM.p[myIndex] != null) {
            boolean bl = false;
            if (by == 1) {
                res = Language.WIN();
                bl = PM.p[GameScr.myIndex].team;
            } else {
                res = Language.LOSE();
                bl = !PM.p[GameScr.myIndex].team;
            }
            if (bl == PM.p[GameScr.myIndex].team) {
                pm.setPlayerAfterSetWin(bl);
            }
        }
    }

    public static int[][] getPointAround(int n, int n2, int n3) {
        int[] nArray = new int[n3];
        int[] nArray2 = new int[n3];
        nArray[6] = n - 10;
        nArray2[6] = n2;
        nArray[5] = n + 10;
        nArray2[5] = n2;
        nArray[4] = n;
        nArray2[4] = n2 - 35;
        nArray[3] = n;
        nArray2[3] = n2 - 70;
        nArray[2] = n - 30;
        nArray2[2] = n2 - 70;
        nArray[1] = n + 30;
        nArray2[1] = n2 - 70;
        nArray[0] = n;
        nArray2[0] = n2 - 85;
        return new int[][]{nArray, nArray2};
    }

    public void checkEyeSmoke(byte by, byte by2) {
        if (by2 == 1) {
            PM.p[by].cantSee = false;
            if (by == myIndex) {
                cantSee = false;
            }
        } else {
            PM.p[by].cantSee = true;
            if (by == myIndex) {
                cantSee = true;
            }
        }
        GameScr.waitting();
    }

    public void checkInvisible2(byte by) {
        PM.p[by].isInvisible = false;
        cam.setPlayerMode(by);
        GameScr.waitting();
    }

    public void checkVampire(byte by) {
        PM.p[by].isVampire = false;
        cam.setPlayerMode(by);
        GameScr.waitting();
    }

    public void checkFreeze(byte by, byte by2) {
        PM.p[by].isFreeze = by2 != 1;
        GameScr.waitting();
    }

    public void checkPostion(byte by) {
        PM.p[by].isPoison = true;
        GameScr.waitting();
    }

    private void paintTouch(mGraphics mGraphics2) {
        int n;
        int n2;
        if (!GameScr.useLegacyTouchButtons() || trai == null || phai == null || crossHair2 == null) {
            return;
        }
        int n3 = 0;
        int n4 = CCanvas.hieght - GameScr.phai.image.height - 10;
        xL = n3 + 35;
        yL = n4;
        xR = n3 + 140;
        yR = n4;
        xF = CCanvas.width - (GameScr.crossHair2.image.width + GameScr.crossHair2.image.width / 2);
        yF = CCanvas.hieght - (GameScr.crossHair2.image.height + GameScr.crossHair2.image.height / 2);
        if (CCanvas.isDebugging()) {
            mGraphics2.setColor(16765440);
            n2 = 30;
            n = 30;
            mGraphics2.fillRect(xF - n2 / 2, yF - n / 2, GameScr.crossHair2.image.getWidth() + n2, GameScr.crossHair2.image.getHeight() + n, false);
        }
        if (this.isPressXF) {
            mGraphics2.drawImage(crossHair2, xF + 2, yF + 2, mGraphics.TOP | mGraphics.LEFT, false);
        } else {
            mGraphics2.drawImage(crossHair2, xF, yF, mGraphics.TOP | mGraphics.LEFT, false);
        }
        if (CCanvas.isDebugging()) {
            mGraphics2.setColor(16765440);
            n2 = 30;
            n = 30;
            mGraphics2.fillRect(xL - GameScr.trai.image.width / 2 - n2 / 2, yL - GameScr.trai.image.height / 2 - n / 2, GameScr.trai.image.getWidth() + n2, GameScr.trai.image.getHeight() + n, false);
        }
        if (this.isPressXL) {
            mGraphics2.drawImage(trai, xL + 2, yL + 2, mGraphics.VCENTER | mGraphics.HCENTER, false);
        } else {
            mGraphics2.drawImage(trai, xL, yL, mGraphics.VCENTER | mGraphics.HCENTER, false);
        }
        if (CCanvas.isDebugging()) {
            mGraphics2.setColor(16765440);
            n2 = 30;
            n = 30;
            mGraphics2.fillRect(xR - GameScr.phai.image.width / 2 - n2 / 2, yR - GameScr.trai.image.height / 2 - n / 2, GameScr.phai.image.getWidth() + n2, GameScr.phai.image.getHeight() + n, false);
        }
        if (this.isPressXR) {
            mGraphics2.drawImage(phai, xR + 2, yR + 2, mGraphics.VCENTER | mGraphics.HCENTER, false);
        } else {
            mGraphics2.drawImage(phai, xR, yR, mGraphics.VCENTER | mGraphics.HCENTER, false);
        }
    }

    public void paint(mGraphics mGraphics2) {
        int n;
        int n2;
        int n3;
        int n4;
        Camera.translate(mGraphics2);
        if (curGRAPHIC_LEVEL != 2) {
            mm.paintBackGround(mGraphics2);
        } else {
            mGraphics2.setColor(6483442);
            mGraphics2.fillRect(Camera.x, Camera.y, CCanvas.width, CCanvas.hieght, false);
        }
        if (this.snow != null) {
            this.snow.paintSmallSnow(mGraphics2);
        }
        mm.paint(mGraphics2);
        for (n4 = 0; n4 < this.vGift.size(); ++n4) {
            ((GiftEffect)this.vGift.elementAt(n4)).paint(mGraphics2);
        }
        if (isDarkEffect) {
            Effect.FillTransparentRect(mGraphics2, Camera.x, Camera.y, w, h);
        }
        if (cantSee) {
            mGraphics2.setColor(0xFFFFFF);
            mGraphics2.fillRect(Camera.x, Camera.y, w, h, false);
        }
        for (n4 = 0; n4 < timeBombs.size(); ++n4) {
            TimeBomb timeBomb = (TimeBomb)timeBombs.elementAt(n4);
            if (timeBomb == null) continue;
            timeBomb.paint(mGraphics2);
        }
        pm.paint(mGraphics2);
        OfflineItemLogic.paintUfoAssist(mGraphics2);
        OfflineBotUfo.paintAll(mGraphics2);
        for (n4 = 0; n4 < PM.p.length; ++n4) {
            if (PM.p[n4] == null || !PM.p[n4].isFreeze) continue;
            mGraphics2.drawImage(Explosion.dongbang, PM.p[n4].x, PM.p[n4].y - 12, 3, false);
        }
        sm.paint(mGraphics2);
        bm.paint(mGraphics2);
        if (MM.isHaveWaterOrGlass) {
            mm.paintWater(mGraphics2);
        }
        for (n4 = 0; n4 < exs.size(); ++n4) {
            ((Explosion)exs.elementAt(n4)).paint(mGraphics2);
        }
        if (this.snow != null) {
            this.snow.paintBigSnow(mGraphics2);
        }
        if (whiteEffect) {
            mGraphics2.setColor(0xFFFFFF);
            mGraphics2.fillArc(Camera.x + CCanvas.width / 2 - this.xE, Camera.y + CCanvas.width / 2 - this.xE, this.wE, this.wE, 0, 360, false);
            this.xE += 30;
            this.wE += 60;
            if (this.xE > CCanvas.width + 100) {
                this.xE = 0;
                this.wE = 0;
                int[][] nArray = GameScr.getPointAround(xNuke, yNuke, 7);
                for (int i = 0; i < 7; ++i) {
                    new Explosion(nArray[0][i], nArray[1][i], 7);
                }
                whiteEffect = false;
            }
        }
        if (electricEffect) {
            ++this.tE;
            if (this.tE % 2 == 0) {
                new Explosion(xElectric + CRes.random(-20, 20), yElectric + CRes.random(-20, 20), 8);
            }
            if (this.tE == 10) {
                this.tE = 0;
                electricEffect = false;
            }
        }
        if (freezeEffect) {
            ++this.tE;
            if (this.tE % 2 == 0) {
                new Explosion(xFreeze + CRes.random(-50, 50), yFreeze + CRes.random(-50, 50), 14);
            }
            if (this.tE == 30) {
                this.tE = 0;
                freezeEffect = false;
            }
        }
        if (suicideEffect) {
            ++this.tE;
            if (this.tE % 2 == 0) {
                new Explosion(xSuicide + CRes.random(-50, 50), ySuicide + CRes.random(-50, 50), 0);
            }
            if (this.tE == 60) {
                this.tE = 0;
                suicideEffect = false;
            }
        }
        if (poisonEffect) {
            ++this.tE;
            if (this.tE % 2 == 0) {
                new Explosion(xPoison + CRes.random(-50, 50), yPoison + CRes.random(-50, 50), 15);
            }
            if (this.tE == 60) {
                this.tE = 0;
                poisonEffect = false;
            }
        }
        if (CCanvas.isDebugging()) {
            for (n4 = 0; n4 < MM.mapWidth / 100; ++n4) {
                mGraphics2.setColor(0xFF0000);
                mGraphics2.drawLine(100 * (n4 + 1), 0, 100 * (n4 + 1), MM.mapHeight, false);
                Font.normalFont.drawString(mGraphics2, String.valueOf(n4), 50 + n4 * 100, CCanvas.h / 2, 0);
            }
        }
        if (Camera.shaking == 2 && tickCount / 2 % 2 == 0) {
            mGraphics2.setColor(0xFF0000);
            mGraphics2.fillRect(Camera.x, Camera.y, w, 10, false);
            mGraphics2.fillRect(Camera.x, Camera.y + h - 10, w, 10, false);
            mGraphics2.fillRect(Camera.x, Camera.y, 10, h, false);
            mGraphics2.fillRect(Camera.x + w - 10, Camera.y, 10, h, false);
        }
        if (!trainingMode) {
            time.paint(mGraphics2);
        }
        if ((pm.isYourTurn() || trainingMode) && PM.getMyPlayer() != null) {
            n3 = PM.getMyPlayer().getState() == 3 ? PM.getMyPlayer().force : 0;
            n2 = PM.getMyPlayer().getState() == 3 ? PM.getMyPlayer().force_2 : 0;
            int n5 = PM.getMyPlayer().movePoint;
            n = PM.getMyPlayer().lastForcePoint;
            int n6 = PM.getMyPlayer().lastForcePoint_2;
            if (!this.isSelectItem && MotherCanvas.getNumberFingerOnScreen() < 2 && Camera.mode != 0 && !CPlayer.isShooting) {
                GameScr.onDrawPowerBar(mGraphics2, Camera.x + (w >> 1), Camera.y + h - 25 + 5, n3, n, n5);
                if (PM.getMyPlayer().isDoublePower) {
                    GameScr.onDrawSecondPowerBar(mGraphics2, Camera.x + (w >> 1), Camera.y + h - 25 - 15 + 5, n2, n6, n5);
                }
                GameScr.onDrawAngleBar(mGraphics2, Camera.x + (w >> 1), Camera.y + h - 25 + 8, PM.getMyPlayer().angle);
            }
            if (!CPlayer.isShooting && PM.getMyPlayer().getState() != 5) {
                PM.getMyPlayer().drawKegoc(mGraphics2);
            }
        }
        if (!pm.isYourTurn()) {
            mGraphics2.translate(-mGraphics2.getTranslateX(), -mGraphics2.getTranslateY());
        } else if (CCanvas.isTouch) {
            mGraphics2.translate(-mGraphics2.getTranslateX(), -mGraphics2.getTranslateY());
            if (GameScr.useLegacyTouchButtons() && !this.isSelectItem && MotherCanvas.getNumberFingerOnScreen() < 2 && Camera.mode != 0 && !CPlayer.isShooting) {
                this.paintTouch(mGraphics2);
            }
        } else {
            mGraphics2.translate(-mGraphics2.getTranslateX(), -mGraphics2.getTranslateY());
        }
        if (this.isSelectItem) {
            if (CCanvas.isTouch) {
                mGraphics2.translate(-mGraphics2.getTranslateX(), -mGraphics2.getTranslateY());
                GameScr.paintBorderRect(mGraphics2, GameScr.getItemSelectPopupY(), 4, 130, Language.chonItem());
                GameScr.onDrawItem(mGraphics2, GameScr.getTouchItemIconX(), GameScr.getTouchItemIconY());
                mGraphics2.drawImage(CRes.imgMenu, 25, 5, 0, false);
            } else {
                n3 = 18;
                n2 = 4 * n3 - 2;
                n = GameScr.getItemSelectRows() * n3 - 2;
                GameScr.onDrawItem(mGraphics2, Camera.x + CCanvas.hw - n2 / 2, Camera.y + CCanvas.hh - n / 2);
            }
        }
        this.drawSCORE(mGraphics2);
        if (PM.getCurPlayer() != null) {
            int n7;
            String string = PM.getCurPlayer().name;
            int n8 = n7 = CCanvas.isTouch ? 25 : 0;
            if (string != null && !(PM.getCurPlayer() instanceof Boss)) {
                (PM.getCurPlayer().team ? Font.smallFontRed : Font.smallFontYellow).drawString(mGraphics2, string.toUpperCase(), CScreen.w - 16, 22 + n7, 2);
            }
        }
        mGraphics2.translate(-mGraphics2.getTranslateX(), -mGraphics2.getTranslateY());
        this.drawWind(mGraphics2);
        this.paintOfflineHotbar(mGraphics2);
        this.paintOfflineMenuButton(mGraphics2);
        if (Camera.mode == 0) {
            this.drawWhenFreeCam(mGraphics2);
        }
        if (CCanvas.currentDialog == null && !this.isSelectItem) {
            this.drawMenuCameraIcon(mGraphics2);
        }
        if (!CRes.isNullOrEmpty(tfChat.getText()) && this.isChat) {
            this.isChat = false;
            if (this.chatWait == 0) {
                String string = tfChat.getText();
                GameService.gI().chatToBoard(string);
                tfChat.setText("");
                this.showChat(TerrainMidlet.myInfo.IDDB, string);
                CCanvas.gameScr.showChat(TerrainMidlet.myInfo.IDDB, string, 90);
                this.chatWait = this.chatDelay;
            } else {
                tfChat.setText("");
            }
            GameScr.clearKey();
        }
        this.drawChat(mGraphics2);
        if (CCanvas.isDebugging()) {
            int n9 = CCanvas.width - 2 - Font.normalRFont.getWidth(GameMidlet.version);
            int n10 = CCanvas.hieght - Font.normalRFont.getHeight() * 2;
            Font.normalRFont.drawString(mGraphics2, String.valueOf(GameMidlet.timePingPaint), n9, n10, 2, false);
            Font.normalRFont.drawString(mGraphics2, "CAM: " + Camera.getMode(), n9, n10 - 15, 2, false);
            if (pm.isYourTurn()) {
                Font.normalRFont.drawString(mGraphics2, "SHOOT: " + CPlayer.isShooting, n9, n10 - 30, 2, false);
            }
            if (CCanvas.isPointerDown[0]) {
                Font.normalFont.drawString(mGraphics2, CCanvas.pX[0] + "/" + CCanvas.pY[0], CCanvas.pX[0], CCanvas.pY[0] - 15, 2, false);
            }
        }
        if (CCanvas.currentDialog != null) {
            super.paintCommand(mGraphics2);
        }
    }

    private void drawWhenFreeCam(mGraphics mGraphics2) {
        Font.borderFont.drawString(mGraphics2, Language.cameraMode(), CCanvas.hw, CCanvas.hh - 15, 2);
        int n = 0;
        if (CCanvas.gameTick % 10 > 4) {
            n = 2;
        }
        mGraphics2.drawImage(imgArrow, 0 + n, CCanvas.hh, mGraphics.LEFT | mGraphics.VCENTER, false);
        mGraphics2.drawRegion(imgArrow, 0, 0, GameScr.imgArrow.image.getWidth(), GameScr.imgArrow.image.getHeight(), 2, CCanvas.width - n, CCanvas.hh, mGraphics.VCENTER | mGraphics.RIGHT, false);
        mGraphics2.drawRegion(imgArrow, 0, 0, GameScr.imgArrow.image.getWidth(), GameScr.imgArrow.image.getHeight(), 5, CCanvas.hw, 25 + n, mGraphics.TOP | mGraphics.HCENTER, false);
        mGraphics2.drawRegion(imgArrow, 0, 0, GameScr.imgArrow.image.getWidth(), GameScr.imgArrow.image.getHeight(), 6, CCanvas.hw, CCanvas.hieght - 30 - n, mGraphics.BOTTOM | mGraphics.HCENTER, false);
    }

    private void drawMenuCameraIcon(mGraphics mGraphics2) {
        mGraphics2.translate(-mGraphics2.getTranslateX(), -mGraphics2.getTranslateY());
        int camBtnX = CCanvas.width - 25;
        int camBtnY = 3;
        if (CRes.imgCam != null && CRes.imgCam.image != null) {
            int n = camBtnX + (22 - CRes.imgCam.image.getWidth()) / 2;
            int n2 = camBtnY + (22 - CRes.imgCam.image.getHeight()) / 2;
            mGraphics2.drawImage(CRes.imgCam, n, n2, 0, false);
        } else {
            mGraphics2.setColor(0x003366);
            mGraphics2.fillRect(camBtnX, camBtnY, 22, 22, false);
            mGraphics2.setColor(0xFFA500);
            mGraphics2.drawRect(camBtnX, camBtnY, 22, 22, false);
            Font.smallFontYellow.drawString(mGraphics2, "CAM", camBtnX + 1, camBtnY + 4, 0);
        }
    }

    public static void changeWind(int n, int n2) {
        windx = n;
        windy = n2;
        windAngle = CRes.fixangle(CRes.angle(windx, -windy));
        windPower = CRes.sqrt(windx * windx + windy * windy);
    }

    public void drawWind(mGraphics mGraphics2) {
        if (Camera.mode != 0) {
            mGraphics2.drawImage(wind1, CCanvas.width / 2, 22, 3, true);
            this.dem = !this.b ? ++this.dem : --this.dem;
            if (this.dem > 5) {
                this.b = true;
            }
            if (this.dem < 0) {
                this.b = false;
            }
            mGraphics2.drawImage(wind2, CCanvas.w / 2, 22, 3, true);
            if (windPower != 0) {
                int n = 13 * CRes.cos(CRes.fixangle(windAngle)) >> 10;
                int n2 = 13 * CRes.sin(CRes.fixangle(windAngle)) >> 10;
                mGraphics2.drawImage(wind3, CCanvas.w / 2 + 2 + n, 22 - n2, mGraphics.VCENTER | mGraphics.HCENTER, true);
            }
            Font.borderFont.drawString(mGraphics2, String.valueOf(windPower), CCanvas.w / 2, 15, 3);
            Font.borderFont.drawString(mGraphics2, Language.windAngle() + ": " + windAngle, CCanvas.w / 2, 45, 2);
            Font.borderFont.drawString(mGraphics2, "L\u01b0\u1ee3t: " + OfflineCombat.turnCounter(), 4, 45, 0);
        }
    }

    public void drawSCORE(mGraphics mGraphics2) {
        if (!res.equals("")) {
            String[] stringArray;
            int n;
            mGraphics2.translate(-mGraphics2.getTranslateX(), -mGraphics2.getTranslateY());
            Font.bigFont.drawString(mGraphics2, res, CCanvas.hw, 80, mGraphics.HCENTER | mGraphics.VCENTER);
            Font.borderFont.drawString(mGraphics2, Language.money() + ": " + this.moneyBonus + Language.xu(), CCanvas.hw, this.moneyY, mGraphics.HCENTER | mGraphics.VCENTER);
            if (this.luongBonus != 0) {
                Font.borderFont.drawString(mGraphics2, (this.luongBonus > 0 ? "+" : "") + this.luongBonus + Language.luong(), CCanvas.hw, this.moneyY + 18, mGraphics.HCENTER | mGraphics.VCENTER);
            }
            int n2 = n = this.luongBonus != 0 ? 2 : 1;
            if (this.expBonus > 0) {
                Font.borderFont.drawString(mGraphics2, "+" + this.expBonus + " KN", CCanvas.hw, this.moneyY + n * 18, mGraphics.HCENTER | mGraphics.VCENTER);
                ++n;
            }
            if (this.itemBonusText != null && this.itemBonusText.length() > 0 && (stringArray = Font.borderFont.splitFontBStrInLine(this.itemBonusText, CCanvas.width - 40)) != null) {
                int n3 = this.moneyY + n * 18;
                for (int i = 0; i < stringArray.length; ++i) {
                    Font.borderFont.drawString(mGraphics2, stringArray[i], CCanvas.hw, n3, mGraphics.HCENTER | mGraphics.VCENTER);
                    n3 += 16;
                }
            }
            int n4 = CCanvas.hieght - 18;
            Font.borderFont.drawString(mGraphics2, CONTINUE_RESULT_LABEL, CCanvas.hw, n4, mGraphics.HCENTER | mGraphics.BOTTOM);
        }
        if (this.isMoney2Fly) {
            Font.borderFont.drawString(mGraphics2, "+" + this.moneyBonus2 + Language.xu(), PM.p[this.whoGetMoney2].x, this.moneyY2, mGraphics.HCENTER | mGraphics.VCENTER);
        }
    }

    public void show(CScreen cScreen) {
        lastSCreen = cScreen;
        CScreen.isSetClip = false;
        super.show();
    }

    public void show() {
        super.show();
        CScreen.isSetClip = false;
    }

    protected void onClose() {
        super.onClose();
        CScreen.isSetClip = true;
    }

    public static void onDrawPowerBar(mGraphics mGraphics2, int n, int n2, int n3, int n4, int n5) {
        if (s_frBar != null) {
            s_frBar.drawFrame(1, n - 54 - 10, n2 + 5, 3, 0, mGraphics2, false);
            s_frBar.drawFrame(3, n + 2 + 10, n2 + 5, 3, 0, mGraphics2, false);
            s_frBar.fillFrame(0, n - 54 - 10, n2 + 5, (60 - n5) * 100 / 60, 3, 0, mGraphics2, true);
            s_frBar.fillFrame(2, n + 2 + 10, n2 + 5, n3 * 100 / 30, 3, 0, mGraphics2, true);
            s_frBar.drawFrame(5, n - 53 - 10, n2 - 7, 3, 0, mGraphics2, true);
            s_frBar.fillFrame(4, n - 54 - 10, n2 - 7, PM.getMyPlayer().angryX * 100 / 100, 3, 0, mGraphics2, true);
            if (n4 > 0) {
                mGraphics2.setColor(0xFB7F7F);
                mGraphics2.drawLine(n + 2 + n4 * 49 / 30 + 10, n2 + 7, n + 2 + n4 * 49 / 30 + 10, n2 + 7 + 7, false);
            }
        }
    }

    public static void onDrawSecondPowerBar(mGraphics mGraphics2, int n, int n2, int n3, int n4, int n5) {
        if (Camera.mode != 0) {
            s_frBar.drawFrame(3, n + 2 + 10, n2 + 8, 0, 0, mGraphics2);
            s_frBar.fillFrame(2, n + 2 + 10, n2 + 8, n3 * 100 / 30, 3, 0, mGraphics2, true);
            if (n4 > 0) {
                mGraphics2.setColor(0xFB7F7F);
                mGraphics2.drawLine(n + 2 + n4 * 49 / 30 + 10, n2 + 10, n + 2 + n4 * 49 / 30 + 10, n2 + 8 + 9, false);
            }
        }
    }

    public static void onDrawAngleBar(mGraphics mGraphics2, int n, int n2, int n3) {
        if (Camera.mode != 0) {
            mGraphics2.drawImage(s_imgAngle, n, n2 + 2, mGraphics.TOP | mGraphics.HCENTER, false);
            Font.smallFontYellow.drawString(mGraphics2, "" + (n3 >= 90 ? 180 - n3 : n3), n, n2 + 4, 2);
        }
    }

    private void paintOfflineMenuButton(mGraphics mGraphics2) {
        if (CRes.imgMenu != null && CRes.imgMenu.image != null) {
            int n = 3 + (22 - CRes.imgMenu.image.getWidth()) / 2;
            int n2 = 3 + (22 - CRes.imgMenu.image.getHeight()) / 2;
            mGraphics2.drawImage(CRes.imgMenu, n, n2, 0, false);
        }
    }

    private boolean isOfflineMenuButtonTap(int n) {
        return CCanvas.isPointer(3, 3, 22, 22, n);
    }

    private boolean isOfflineCameraButtonTap(int n) {
        int camBtnX = CCanvas.width - 28;
        return CCanvas.isPointer(camBtnX, 2, 26, 26, n);
    }

    private void paintOfflineHotbar(mGraphics mGraphics2) {
        boolean bl;
        int n;
        CPlayer cPlayer = PM.getMyPlayer();
        if (cPlayer == null || cPlayer.item == null) {
            return;
        }
        int n2 = 9;
        int n3 = n2 * 20;
        int n4 = CCanvas.width / 2 - n3 / 2;
        int n5 = CCanvas.hieght - CScreen.cmdH - 20 - 6;
        boolean bl2 = pm.isYourTurn() && cPlayer.itemUsed == -1 && !cPlayer.isUsedItem;
        for (int i = 0; i < 8; ++i) {
            n = n4 + i * 20;
            bl = i < cPlayer.item.length && cPlayer.item[i] >= 0;
            mGraphics2.setColor(bl && bl2 ? 3163936 : 0x2D2D2D);
            mGraphics2.fillRect(n, n5, 18, 18, false);
            if (bl) {
                try {
                    mGraphics2.drawRegion(s_imgITEM, 0, (cPlayer.item[i] + 2) * 16, 16, 16, 0, n + 1, n5 + 1, 0, false);
                }
                catch (Exception exception) {
                }
            }
            Font.smallFontYellow.drawString(mGraphics2, String.valueOf(i + 1), n + 1, n5 + 20 - 9, 0);
        }
        n = n4 + 160;
        boolean bl3 = bl = cPlayer.isAngry && cPlayer.itemUsed == -1 && !cPlayer.isUsedItem;
        mGraphics2.setColor(bl ? (CCanvas.gameTick % 10 > 5 ? 0xFFFF00 : 0xFF0000) : 0x2D2D2D);
        mGraphics2.fillRect(n, n5, 18, 18, false);
        try {
            mGraphics2.drawRegion(s_imgITEM, 0, 640, 16, 16, 0, n + 1, n5 + 1, 0, false);
        }
        catch (Exception exception) {
            mGraphics2.setColor(bl ? (CCanvas.gameTick % 10 > 5 ? 0xFFFF00 : 0xFF0000) : 0x2D2D2D);
            mGraphics2.fillRect(n, n5, 18, 18, false);
            Font.smallFontYellow.drawString(mGraphics2, "P", n + 6, n5 + 3, 0);
        }
        Font.smallFontYellow.drawString(mGraphics2, "9", n + 1, n5 + 20 - 9, 0);
    }

    private boolean handleOfflineHotbarKey(int n) {
        if (n < 49 || n > 57) {
            return false;
        }
        CPlayer cPlayer = PM.getMyPlayer();
        if (cPlayer == null || !pm.isYourTurn() || cPlayer.itemUsed != -1 || cPlayer.isUsedItem) {
            return true;
        }
        int n2 = n - 49;
        if (n2 == 8) {
            OfflineCombat.useSpecialSkill();
            return true;
        }
        if (cPlayer.item != null && n2 < cPlayer.item.length && cPlayer.item[n2] >= 0) {
            cPlayer.UseItem(cPlayer.item[n2], false, n2);
        }
        return true;
    }

    public static void onDrawItem(mGraphics mGraphics2, int n, int n2) {
        if (!CCanvas.isTouch) {
            mGraphics2.setColor(16767817);
            mGraphics2.fillRect(Camera.x, n2 - 1, CCanvas.width, CCanvas.isTouch ? 43 : 36, false);
        }
        Item.DrawSetItem(mGraphics2, PM.getMyPlayer().item, curItemSelec, n, n2, CCanvas.isTouch, (byte[])(PrepareScr.currLevel == 7 ? num : null));
        Font.borderFont.drawString(mGraphics2, Language.use(), Camera.x + 5, Camera.y + CCanvas.hieght - Font.normalFont.getHeight() - 4, 0);
        Font.borderFont.drawString(mGraphics2, Language.close(), Camera.x + CCanvas.width - 5, Camera.y + CCanvas.hieght - Font.normalFont.getHeight() - 4, 1);
    }

    public static void onDrawArrow(mGraphics mGraphics2, int n, int n2, int n3, boolean bl) {
        if (bl) {
            n2 += 2 * (tickCount / 2 % 2);
        }
        mGraphics2.setColor(n3);
        mGraphics2.fillRect(n, n2, 11, 2, false);
        mGraphics2.fillTriangle(n, n2 + 3, n + 11, n2 + 3, n + 5, n2 + 9, false);
    }

    public void showChat(int n, String string) {
        if (PrepareScr.currLevel != 7) {
            this.chatList.addElement(CCanvas.prepareScr.getPlayerNameFromID(n) + ": " + string);
        } else {
            this.chatList.addElement(pm.getPlayerNameFromID(n) + ": " + string);
        }
        if (this.chatDelay == 0) {
            this.chatDelay = this.MAX_CHAT_DELAY;
        }
    }

    public void updateChat() {
        if (this.chatDelay > 0) {
            --this.chatDelay;
            if (this.chatDelay == 0) {
                if (this.chatList.size() > 0) {
                    this.chatList.removeElementAt(0);
                }
                if (this.chatList.size() > 0) {
                    this.chatDelay = this.MAX_CHAT_DELAY;
                }
            }
        }
    }

    public void drawChat(mGraphics mGraphics2) {
        if (this.chatList.size() != 0) {
            String string = (String)this.chatList.elementAt(0);
            int n = this.MAX_CHAT_DELAY - this.chatDelay;
            if (n > 10) {
                n = 10;
            }
            int n2 = CCanvas.width;
            for (int i = 0; i < n; ++i) {
                n2 >>= 1;
            }
            Font.borderFont.drawString(mGraphics2, string, 3 + n2, CCanvas.hieght - 14, 0);
        }
    }

    public void showChat(int n, String string, int n2) {
        ChatPopup chatPopup = new ChatPopup();
        CPlayer cPlayer = pm.getPlayerFromID(n);
        if (cPlayer != null) {
            chatPopup.show(n2, cPlayer.x - Camera.x, cPlayer.y - Camera.y - 30, string);
            CCanvas.arrPopups.addElement(chatPopup);
        }
    }

    public void openChat() {
        if (tfChat != null) {
            this.isChat = true;
            tfChat.doChangeToTextBox();
        }
    }

    public void onMaxForceKey() {
        this.doSetForce();
    }

    public void toggleCamera() {
        if (Camera.mode != 0) {
            Camera.mode = 0;
            GameScr.clearKey();
        } else if (BM.active && GameScr.bm.bullets.size() > 0) {
            cam.setBulletMode((Bullet)GameScr.bm.bullets.elementAt(0));
            GameScr.clearKey();
        } else {
            cam.setPlayerMode(PM.curP);
            GameScr.clearKey();
        }
    }

    public void keyPressed(int n) {
        if (CCanvas.currentDialog != null || CCanvas.pausemenu.isShow) {
            return;
        }
        if (this.isShowingResult()) {
            if (n == -5 || n == 10) {
                this.continueAfterResult();
                CScreen.clearKey();
            }
            return;
        }
        if (this.isSelectItem) {
            this.handleItemPadInput();
            CScreen.clearKey();
            return;
        }
        if (this.handleOfflineHotbarKey(n)) {
            CScreen.clearKey();
            return;
        }
        switch (n) {
            case -6: {
                if (!this.isShowPausemenu) {
                    this.doShowPauseMenu();
                }
                CScreen.clearKey();
                break;
            }
            case -7: {
                this.toggleCamera();
                break;
            }
        }
    }

    public void onClearMap() {
        mm.onClearMap();
        sm.onClearMap();
        System.gc();
    }

    public void onPointerPressed(int n, int n2, int n3) {
        if (this.isShowingResult()) {
            if (CCanvas.keyPressed[5] || this.isContinuePointer(n, n2, n3)) {
                this.continueAfterResult();
                CScreen.clearKey();
            }
            return;
        }
        if (this.isSelectItem) {
            this.handleItemPadInput();
            CScreen.clearKey();
            return;
        }
        if (!this.isShowPausemenu && this.isOfflineMenuButtonTap(n3)) {
            this.doShowPauseMenu();
            CScreen.clearKey();
            return;
        }
        if (!this.isShowPausemenu && this.isOfflineCameraButtonTap(n3)) {
            this.toggleCamera();
            CScreen.clearKey();
            return;
        }
        if (Camera.mode == 1 && mSystem.currentTimeMillis() - this.timeDelayClosePauseMenu > 550L) {
            pm.onPointerPressed(n, n2, n3);
        }
        super.onPointerPressed(n, n2, n3);
    }

    public void onPointerHold(int n, int n2, int n3) {
        if (this.isShowingResult()) {
            return;
        }
        if (!this.isSelectItem && !this.isShowPausemenu && mSystem.currentTimeMillis() - this.timeDelayClosePauseMenu >= 300L && mSystem.currentTimeMillis() - this.timeShowPauseMenu >= 300L && Camera.mode == 1) {
            pm.onPointerHold(n, n2, n3);
            if (GameScr.useLegacyTouchButtons() && trai != null && phai != null && crossHair2 != null) {
                int n4 = 30;
                int n5 = 30;
                if (CCanvas.isPointer(xL - GameScr.trai.image.width / 2 - n4 / 2, yL - GameScr.trai.image.height / 2 - n5 / 2, GameScr.trai.image.getWidth() + n4, GameScr.trai.image.getHeight() + n5, n3) && pm.isYourTurn()) {
                    this.isPressXL = true;
                    return;
                }
                if (CCanvas.isPointer(xR - GameScr.phai.image.width / 2 - n4 / 2, yR - GameScr.trai.image.height / 2 - n5 / 2, GameScr.phai.image.getWidth() + n4, GameScr.phai.image.getHeight() + n5, n3) && pm.isYourTurn()) {
                    this.isPressXR = true;
                    return;
                }
                n4 = 30;
                n5 = 30;
                if (CCanvas.isPointer(xF - n4 / 2, yF - n5 / 2, GameScr.crossHair2.image.getWidth() + n4, GameScr.crossHair2.image.getHeight() + n5, n3) && pm.isYourTurn()) {
                    this.isPressXF = true;
                }
            }
        }
    }

    public void onPointerDragged(int n, int n2, int n3) {
        if (!this.isSelectItem && !this.isShowPausemenu && mSystem.currentTimeMillis() - this.timeDelayClosePauseMenu >= 300L && mSystem.currentTimeMillis() - this.timeShowPauseMenu >= 300L) {
            try {
                if (pm.isYourTurn() || !(PM.p[PM.curP] instanceof Boss)) {
                    if (MotherCanvas.getNumberFingerOnScreen() >= 2) {
                        if (Camera.mode == 1 && n3 == 1) {
                            pm.onPointerDragRighCorner(n, n2, n3);
                        }
                    } else {
                        if (Camera.mode == 1) {
                            pm.onPointerDrag(n, n2, n3);
                        }
                        if (Camera.mode == 0) {
                            this.onDragCamera(n, n2, n3);
                        }
                    }
                }
            }
            catch (Exception exception) {
            }
            super.onPointerDragged(n, n2, n3);
        }
    }

    public void onPointerReleased(int n, int n2, int n3) {
        this.isPressXF = false;
        this.isPressXR = false;
        this.isPressXL = false;
        if (Camera.mode == 1 && !this.isSelectItem) {
            pm.onPointerReleased(n, n2, n3);
        }
        if (this.isSelectItem) {
            if (CCanvas.isTouchOnGamePad(n, n2) || CCanvas.isTouchOnGamePad(CCanvas.pxLast[n3], CCanvas.pyLast[n3])) {
                return;
            }
            this.timeDelayClosePauseMenu = mSystem.currentTimeMillis() + 550L;
            this.selectedItemPanelRealeased(n, n2, n3);
        } else {
            if (CCanvas.pausemenu.isShow) {
                CCanvas.pausemenu.onPointerRealeased(n, n2, n3);
            } else if (this.isOfflineMenuButtonTap(n3)) {
                this.doShowPauseMenu();
            }
            this.isShowPausemenu = CCanvas.pausemenu.isShow;
            if (CCanvas.pausemenu.isShow) {
                return;
            }
        }
    }

    public void onPaintSliderRightConer(mGraphics mGraphics2, int n, int n2) {
    }

    public void notClearMap(int n) {
        System.gc();
    }

    static {
        imgReady = new mImage[9];
        imgMsg = new mImage[2];
        timeBombs = new Vector();
        FilePack filePack = null;
        try {
            filePack = new FilePack(CCanvas.getClassPathConfig(CONFIG.PATH_GUI + "gui"));
            airFighter = filePack.loadImage("fighter.png", new IAction2(){

                public void perform(Object object) {
                    CRes.onSaveToFile((Image)object, "fighter");
                }
            });
            imgMode = filePack.loadImage("mode.png", new IAction2(){

                public void perform(Object object) {
                    CRes.onSaveToFile((Image)object, "mode");
                }
            });
            lock = filePack.loadImage("lock2.png", new IAction2(){

                public void perform(Object object) {
                    CRes.onSaveToFile((Image)object, "lock2");
                }
            });
            lockImg = filePack.loadImage("lock.png", new IAction2(){

                public void perform(Object object) {
                    CRes.onSaveToFile((Image)object, "lock");
                }
            });
            crosshair = filePack.loadImage("hongTam.png", new IAction2(){

                public void perform(Object object) {
                    CRes.onSaveToFile((Image)object, "hongTam");
                }
            });
            imgInfoPopup = filePack.loadImage("popupRound.png", new IAction2(){

                public void perform(Object object) {
                    CRes.onSaveToFile((Image)object, "popupRound");
                }
            });
            s_imgITEM = filePack.loadImage("item.png", new IAction2(){

                public void perform(Object object) {
                    CRes.onSaveToFile((Image)object, "item");
                }
            });
            imgPlane = filePack.loadImage("fighter.png", new IAction2(){

                public void perform(Object object) {
                    CRes.onSaveToFile((Image)object, "fighter");
                }
            });
            logoGame = mImage.createImage("/gui/logoGame.png");
            GameScr.imgReady[0] = filePack.loadImage("on.png", new IAction2(){

                public void perform(Object object) {
                    CRes.onSaveToFile((Image)object, "on");
                }
            });
            GameScr.imgReady[1] = filePack.loadImage("off.png", new IAction2(){

                public void perform(Object object) {
                    CRes.onSaveToFile((Image)object, "off");
                }
            });
            GameScr.imgReady[2] = filePack.loadImage("r2.png", new IAction2(){

                public void perform(Object object) {
                    CRes.onSaveToFile((Image)object, "r2");
                }
            });
            GameScr.imgReady[3] = filePack.loadImage("arrowup.png", new IAction2(){

                public void perform(Object object) {
                    CRes.onSaveToFile((Image)object, "arrowup");
                }
            });
            GameScr.imgReady[4] = filePack.loadImage("tile1.png", new IAction2(){

                public void perform(Object object) {
                    CRes.onSaveToFile((Image)object, "tile1");
                }
            });
            imgQuanHam = filePack.loadImage("quanham.png", new IAction2(){

                public void perform(Object object) {
                    CRes.onSaveToFile((Image)object, "quanham");
                }
            });
            imgBack = filePack.loadImage("menubg.png", new IAction2(){

                public void perform(Object object) {
                    CRes.onSaveToFile((Image)object, "menubg");
                }
            });
            imgCurPos = filePack.loadImage("curMapPos.png", new IAction2(){

                public void perform(Object object) {
                    CRes.onSaveToFile((Image)object, "curMapPos");
                }
            });
            imgSmallCloud = PrepareScr.cloud1;
            imgArrowRed = filePack.loadImage("arrowRed.png", new IAction2(){

                public void perform(Object object) {
                    CRes.onSaveToFile((Image)object, "arrowRed");
                }
            });
            imgRoomStat = filePack.loadImage("stat.png", new IAction2(){

                public void perform(Object object) {
                    CRes.onSaveToFile((Image)object, "stat");
                }
            });
            imgTrs = filePack.loadImage("trs.png", new IAction2(){

                public void perform(Object object) {
                    CRes.onSaveToFile((Image)object, "trs");
                }
            });
            imgIcon = filePack.loadImage("icon.png", new IAction2(){

                public void perform(Object object) {
                    CRes.onSaveToFile((Image)object, "icon");
                }
            });
            s_imgAngle = filePack.loadImage("angle.png", new IAction2(){

                public void perform(Object object) {
                    CRes.onSaveToFile((Image)object, "angle");
                }
            });
            imgChat = filePack.loadImage("chat.png", new IAction2(){

                public void perform(Object object) {
                }
            });
            s_imgTransparent = filePack.loadImage("transparent.png", new IAction2(){

                public void perform(Object object) {
                    CRes.onSaveToFile((Image)object, "transparent");
                }
            });
            GameScr.imgMsg[0] = filePack.loadImage("msg0.png", new IAction2(){

                public void perform(Object object) {
                    CRes.onSaveToFile((Image)object, "msg0");
                }
            });
            GameScr.imgMsg[1] = filePack.loadImage("msg1.png", new IAction2(){

                public void perform(Object object) {
                    CRes.onSaveToFile((Image)object, "msg1");
                }
            });
            logoII = filePack.loadImage("logo_2.png", new IAction2(){

                public void perform(Object object) {
                    CRes.onSaveToFile((Image)object, "logo_2");
                }
            });
            arrowMenu = filePack.loadImage("arrowMenu.png", new IAction2(){

                public void perform(Object object) {
                    CRes.onSaveToFile((Image)object, "arrowMenu");
                }
            });
            if (!CCanvas.isGDX()) {
                mImage.createImage("/gui/nut2.png", new IAction2(){

                    public void perform(Object object) {
                        trai = new mImage((Image)object);
                        CRes.onSaveToFile((Image)object, "nut2");
                    }
                });
                mImage.createImage("/gui/nut1.png", new IAction2(){

                    public void perform(Object object) {
                        phai = new mImage((Image)object);
                        CRes.onSaveToFile((Image)object, "nut1");
                    }
                });
                mImage.createImage("/gui/nut3.png", new IAction2(){

                    public void perform(Object object) {
                        crossHair2 = new mImage((Image)object);
                        CRes.onSaveToFile((Image)object, "nut3");
                    }
                });
            }
            mImage.createImage("/wind.png", new IAction2(){

                public void perform(Object object) {
                    wind1 = new mImage((Image)object);
                    CRes.onSaveToFile((Image)object, "wind");
                }
            });
            mImage.createImage("/wind2.png", new IAction2(){

                public void perform(Object object) {
                    wind2 = new mImage((Image)object);
                }
            });
            mImage.createImage("/gui/wind3.png", new IAction2(){

                public void perform(Object object) {
                    wind3 = new mImage((Image)object);
                }
            });
            mImage.createImage("/gui/barMove.png", new IAction2(){

                public void perform(Object object) {
                    s_frBar = new FrameImage((Image)object, 53, 12, false);
                }
            });
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
        filePack = null;
        tickCount = 0;
        ID_Turn = 0;
        isDarkEffect = false;
        s_iPlane_x = -1;
        s_iPlane_y = -1;
        s_iBombTargetX = -1;
        res = "";
        try {
            imgArrow = mImage.createImage("/arrow.png");
        }
        catch (Exception exception) {
        }
        num = new byte[8];
    }
}

