/*
 * Decompiled with CFR 0.152.
 */
package screen;

import CLib.mGraphics;
import CLib.mImage;
import com.teamobi.mobiarmy2.GameMidlet;
import com.teamobi.mobiarmy2.OfflineBossFight;
import com.teamobi.mobiarmy2.OfflineBossReward;
import com.teamobi.mobiarmy2.OfflinePvpBot;
import coreLG.CCanvas;
import coreLG.CONFIG;
import coreLG.TerrainMidlet;
import effect.Cloud;
import item.Bullet;
import item.Item;
import item.MyItemIcon;
import java.util.Vector;
import map.MM;
import model.ChatPopup;
import model.FilePack;
import model.Font;
import model.IAction;
import model.Language;
import model.PlayerInfo;
import model.TField;
import network.Command;
import network.GameService;
import network.RelayService;
import player.CPlayer;
import screen.BoardListScr;
import screen.CScreen;
import screen.GameScr;
import screen.LevelScreen;
import screen.RoomListScr;
import shop.ShopItem;

public class PrepareScr
extends CScreen {
    public static final String[] GUN_NAME = new String[]{"GUNNER", "MISS 6", "ELECTICIAN", "KING KONG", "ROCKETER", "GRANOS", "CHICKY", "TARZAN", "APACHE", "MAGENTA", "DRABY", "COW GIRL"};
    private static final int MAPCOUNT = MM.NUM_MAP;
    boolean isStartGame;
    public static byte numPlayer = (byte)8;
    public static byte currentRoom;
    public static byte currLevel;
    public Vector playerInfos = new Vector();
    public Vector bossInfos = new Vector();
    public int showChatInterval;
    public static mImage[] imgMap;
    public static mImage imgGun;
    public static mImage imgQuanHam;
    public static mImage imgBack;
    public static mImage khungMap;
    public static mImage line;
    public static mImage iconChat;
    public static byte[] mapBossID;
    public static byte[] bossID;
    private int[] x_pos;
    private int[] y_pos;
    int x_player = 30;
    int y_player = 60;
    int y_owner = 0;
    int money;
    int ownerID;
    int indexOfMe = -1;
    int height = 30;
    int y_Map = 0;
    int x_Name = 0;
    public static byte curMap;
    private byte numMap = (byte)3;
    public static byte numCurItemSlot;
    public int[] itemCur;
    public static MyItemIcon prepareScrItemIcon;
    public static byte numUseSlot;
    private boolean isChooseItem;
    private boolean isChooseGun;
    private int selectedItem;
    private int selectedGunLv2;
    private int curUsing;
    int x_item;
    int y_item;
    int y_item_touch;
    int x_item_touch;
    public static short bulletType;
    public static TField tfChat;
    boolean isGameEnd;
    Command cmdExit;
    Command cmdStart;
    Command cmdReady;
    Command cmdMenu;
    Command cmdChangeTeam;
    Command cmdAddFriend;
    Command cmdSelect;
    Command cmdLeft;
    Command cmdRight;
    Command cmdBack;
    Command cmdBanBe;
    Command cmdTinNhan;
    Command cmdMap;
    Command cmdInvite;
    Command findRoom;
    static int roomID;
    static int zoneID;
    public static mImage[] imgReady;
    public static mImage[] imgCloud;
    public static mImage rockImg;
    public static mImage rock2Img;
    public static mImage glassFly;
    public static mImage chickenHair;
    public static mImage cloud1;
    public static mImage imgSun;
    public static mImage randomMap;
    public static FilePack filePak;
    public static byte[] fileData;
    public int chatDelay;
    public int readyDelay;
    int xPaintMap;
    int yPaintMap;
    int anchorPainMap;
    public static boolean isBossRoom;
    public static boolean isPvpBotRoom;
    public static boolean isRelayRoom;
    boolean isTouchItem;

    public static void init() {
        int n;
        CCanvas.roomListScr = new RoomListScr();
        imgMap = new mImage[MM.NUM_MAP];
        try {
            filePak = new FilePack(fileData);
            if (filePak != null) {
                for (n = 0; n < MM.NUM_MAP; ++n) {
                    PrepareScr.imgMap[n] = filePak.loadImage(MM.mapFileName[n] + ".png");
                }
                khungMap = filePak.loadImage("khungmap.png");
            }
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
        filePak = null;
        for (n = 0; n < MAPCOUNT - 1; ++n) {
            String string = CCanvas.getClassPathConfig(CONFIG.PATH_MAP + "map" + n + ".png");
            PrepareScr.imgMap[n] = mImage.createImage(string);
        }
    }

    public void getPlayerIcon(short s, mImage mImage2) {
        for (int i = 0; i < this.playerInfos.size(); ++i) {
            PlayerInfo playerInfo = (PlayerInfo)this.playerInfos.elementAt(i);
            if (playerInfo.clanID != s) continue;
            playerInfo.clanIcon = mImage2;
        }
    }

    public void initItemCurrent() {
        if (TerrainMidlet.myInfo != null) {
            PlayerInfo playerInfo = TerrainMidlet.myInfo;
            byte by = playerInfo.gun;
            if (by >= 0 && by < playerInfo.itemLoadout.length) {
                for (int i = 0; i < this.itemCur.length && i < playerInfo.itemLoadout[by].length; ++i) {
                    this.itemCur[i] = playerInfo.itemLoadout[by][i];
                }
                return;
            }
        }
        for (int i = 0; i < this.itemCur.length; ++i) {
            this.itemCur[i] = -2;
        }
        this.itemCur[0] = 0;
        this.itemCur[1] = 0;
        this.itemCur[2] = 1;
        this.itemCur[3] = 1;
    }

    public static int[] getStrainingItem() {
        int[] nArray = new int[numCurItemSlot];
        nArray[0] = 0;
        nArray[1] = 0;
        nArray[2] = 0;
        nArray[3] = 0;
        nArray[4] = 0;
        nArray[5] = 0;
        return nArray;
    }

    public int[] copyItemCurrent() {
        int n = this.itemCur.length;
        int[] nArray = new int[n];
        for (int i = 0; i < n; ++i) {
            nArray[i] = this.itemCur[i];
        }
        return nArray;
    }

    public int getIDBySeat(int n) {
        return ((PlayerInfo)this.playerInfos.elementAt((int)n)).IDDB;
    }

    public void setAt(int n, PlayerInfo playerInfo) {
        this.playerInfos.setElementAt(playerInfo, n);
    }

    public void doViewMessage() {
        Vector<Command> vector = new Vector<Command>();
        for (int i = 0; i < this.playerInfos.size(); ++i) {
            final PlayerInfo playerInfo = (PlayerInfo)this.playerInfos.elementAt(i);
            if (playerInfo.IDDB == TerrainMidlet.myInfo.IDDB || playerInfo.IDDB == -1) continue;
            vector.addElement(new Command(playerInfo.name, new IAction(){

                public void perform() {
                    GameService.gI().requestInfoOf(playerInfo.IDDB);
                    CCanvas.startWaitDlg(Language.pleaseWait());
                }
            }));
        }
        CCanvas.menu.startAt(vector, 0);
    }

    public void initPosPlayers() {
        this.x_pos = new int[numPlayer];
        this.y_pos = new int[numPlayer];
        this.x_pos[0] = 24;
        int n = CCanvas.hieght - PrepareScr.offlineStandLift();
        this.y_pos[0] = n - 66;
        int n2 = 48;
        this.x_pos[1] = CCanvas.width - 24;
        this.y_pos[1] = this.y_pos[0];
        this.x_pos[2] = 62;
        this.y_pos[2] = this.y_pos[0] - n2;
        this.x_pos[3] = CCanvas.width - 62;
        this.y_pos[3] = this.y_pos[0] - n2;
    }

    public PrepareScr() {
        int n;
        for (n = 0; n < 8; ++n) {
            this.playerInfos.addElement(new PlayerInfo());
        }
        this.itemCur = new int[numCurItemSlot];
        this.isChooseItem = false;
        this.isChooseGun = false;
        this.curUsing = 0;
        this.x_item = 0;
        this.y_item = 0;
        this.y_item_touch = 0;
        this.x_item_touch = 0;
        this.chatDelay = 0;
        this.xPaintMap = CCanvas.hw;
        this.yPaintMap = 36;
        this.anchorPainMap = 3;
        this.initPosPlayers();
        this.initItemCurrent();
        n = CCanvas.isTouch ? 25 : 0;
        this.x_item = CCanvas.width - (Item.iWitdh * 18 + 5);
        this.y_item = CCanvas.hieght - (cmdH + 42) + 5;
        this.x_item_touch = CCanvas.width / 2 - 80 + 12;
        this.y_item_touch = CCanvas.hieght / 2 - 40;
        switch (CCanvas.width) {
            case 176: {
                this.x_player = 10;
                this.y_player = 40;
                this.height = 30;
                this.y_Map = 15;
                this.x_Name = 3;
                this.y_owner = 2;
                break;
            }
            case 240: {
                this.x_player = 30;
                this.y_player = 60;
                this.height = 45;
                this.y_Map = 20;
                this.x_Name = 10;
                this.y_owner = 8;
                break;
            }
            case 320: {
                this.x_player = 30;
                this.y_player = 40;
                this.height = 33;
                this.y_Map = 20;
                this.x_Name = 10;
                this.y_owner = 2;
            }
        }
        tfChat = new TField();
        PrepareScr.tfChat.x = 2;
        PrepareScr.tfChat.y = CCanvas.hieght - ITEM_HEIGHT - 25;
        if (CCanvas.isTouch) {
            PrepareScr.tfChat.y = CCanvas.hieght - CScreen.cmdH - ITEM_HEIGHT;
        }
        PrepareScr.tfChat.width = CCanvas.width - 4;
        PrepareScr.tfChat.height = ITEM_HEIGHT + 2;
        tfChat.setisFocus(true);
        this.cmdMenu = new Command("Menu", new IAction(){

            public void perform() {
                PrepareScr.this.doShowMenuPrepare();
            }
        });
        this.cmdChangeTeam = new Command(Language.changeTeam(), new IAction(){

            public void perform() {
                PrepareScr.this.doChangeTeam();
            }
        });
        this.cmdAddFriend = new Command(Language.makeFriend(), new IAction(){

            public void perform() {
                PrepareScr.this.doAddFriend();
            }
        });
        this.findRoom = new Command(Language.findArea(), new IAction(){

            public void perform() {
                PrepareScr.this.doFindRoom();
            }
        });
        this.cmdMap = new Command(Language.selectMap(), new IAction(){

            public void perform() {
                PrepareScr.this.doSelectMap();
            }
        });
        this.cmdInvite = new Command(Language.findFriend(), new IAction(){

            public void perform() {
                PrepareScr.this.doInvite();
            }
        });
        this.cmdReady = new Command(Language.ready(), new IAction(){

            public void perform() {
                PrepareScr.this.doReady();
            }
        });
        this.cmdStart = new Command(Language.begin(), new IAction(){

            public void perform() {
                PrepareScr.this.doStartGame();
            }
        });
        this.cmdExit = new Command(Language.leaveBattle(), new IAction(){

            public void perform() {
                PlayerInfo playerInfo = (PlayerInfo)PrepareScr.this.playerInfos.elementAt(PrepareScr.this.indexOfMe);
                if (playerInfo.isReady && playerInfo.IDDB != PrepareScr.this.ownerID) {
                    CCanvas.startOKDlg(Language.cannotLeave());
                } else {
                    PrepareScr.this.doLeaveBoard();
                }
            }
        });
        this.cmdBack = new Command(Language.back(), new IAction(){

            public void perform() {
                PrepareScr.this.isTouchItem = false;
                PrepareScr.this.isChooseItem = false;
            }
        });
        this.cmdBanBe = new Command(Language.friends(), new IAction(){

            public void perform() {
                PrepareScr.this.doShowFriend();
            }
        });
        this.cmdTinNhan = new Command(Language.message(), new IAction(){

            public void perform() {
                PrepareScr.this.doViewTinNhan();
            }
        });
        this.cmdSelect = new Command(Language.select(), new IAction(){

            public void perform() {
                PrepareScr.this.doChooseItemEquip();
            }
        });
        this.cmdLeft = new Command("Left", new IAction(){

            public void perform() {
                curMap = curMap > 0 ? (byte)(curMap - 1) : (byte)(PrepareScr.this.numMap - 1);
            }
        });
        this.cmdRight = new Command(Language.back(), new IAction(){

            public void perform() {
                if (PrepareScr.this.isChooseItem) {
                    PrepareScr.this.isChooseItem = false;
                } else if (PrepareScr.this.isChooseGun) {
                    PrepareScr.this.isChooseGun = false;
                } else {
                    curMap = curMap < PrepareScr.this.numMap - 1 ? (byte)(curMap + 1) : (byte)0;
                }
            }
        });
        this.left = this.cmdMenu;
        this.right = this.cmdExit;
        this.center = null;
    }

    private void doChooseItemEquip() {
        if (this.curUsing == 0) {
            if (this.selectedItem >= this.itemCur.length) {
                return;
            }
            if (this.itemCur[this.selectedItem] == -1) {
                CCanvas.startOKDlg(Language.buyMoreBag());
            } else {
                ShopItem.checkItemWhenChose(this.itemCur);
                Item item = ShopItem.getI(PrepareScr.prepareScrItemIcon.select);
                if (this.isChooseItem && !item.isPassive_Item) {
                    if (item.num <= 0 && !item.isFreeItem) {
                        CCanvas.startOKDlg(Language.empty());
                    } else if (item.numUsed < item.nCurMaxUsed) {
                        this.itemCur[this.selectedItem] = item.type;
                        if (TerrainMidlet.myInfo != null) {
                            PlayerInfo playerInfo = TerrainMidlet.myInfo;
                            byte by = playerInfo.gun;
                            if (by >= 0 && by < playerInfo.itemLoadout.length && this.selectedItem < playerInfo.itemLoadout[by].length) {
                                playerInfo.itemLoadout[by][this.selectedItem] = item.type;
                            }
                        }
                        this.isChooseItem = false;
                        this.showTipItem();
                    } else {
                        CCanvas.startOKDlg(Language.chicothe() + item.nCurMaxUsed + Language.itemnay());
                    }
                } else {
                    this.isChooseItem = true;
                }
            }
        } else if (this.curUsing == 1) {
            if (this.isChooseGun) {
                CCanvas.startWaitDlg(Language.pleaseWait());
                this.isChooseGun = false;
                if (this.selectedGunLv2 == 3 && ShopItem.getI((int)11).num == 0) {
                    return;
                }
                GameService.gI().changeGun((byte)this.selectedGunLv2);
            } else {
                this.isChooseGun = true;
                this.showTipChooseWeapon(this.x_item - 45, this.y_item - 22);
            }
        }
    }

    private void doFindRoom() {
        CCanvas.inputDlg.setInfo(Language.nhapSoPhong(), new IAction(){

            public void perform() {
                if (CCanvas.inputDlg.tfInput.getText() != null && !CCanvas.inputDlg.tfInput.getText().equals("") && !CCanvas.inputDlg.tfInput.isNotNumber()) {
                    roomID = Integer.parseInt(CCanvas.inputDlg.tfInput.getText());
                    CCanvas.inputDlg.setInfo(Language.nhapKhuVuc(), new IAction(){

                        public void perform() {
                            if (CCanvas.inputDlg.tfInput.getText() != null && !CCanvas.inputDlg.tfInput.getText().equals("")) {
                                zoneID = Integer.parseInt(CCanvas.inputDlg.tfInput.getText());
                                GameService.gI().requestEmptyRoom((byte)2, (byte)-1, String.valueOf(roomID * 1000 + zoneID));
                                CCanvas.endDlg();
                                CCanvas.startOKDlg(Language.pleaseWait());
                            }
                        }
                    }, new IAction(){

                        public void perform() {
                            CCanvas.endDlg();
                        }
                    }, 1);
                }
            }
        }, new IAction(){

            public void perform() {
                CCanvas.endDlg();
            }
        }, 1);
        CCanvas.inputDlg.show();
    }

    private void doShowFriend() {
        GameService.gI().requestFriendList();
        CCanvas.startWaitDlg(Language.pleaseWait());
    }

    private void doViewTinNhan() {
        CCanvas.msgScr.show(this);
    }

    protected void doChangeTeam() {
        GameService.gI().changeTeam();
        CCanvas.startWaitDlg(Language.pleaseWait());
    }

    public void onChangeTeam(int n, byte by) {
        int n2 = this.getNumberByID(n);
        this.playerInfos.setElementAt((PlayerInfo)this.playerInfos.elementAt(n2), by);
        this.playerInfos.setElementAt(new PlayerInfo(), n2);
        if (n == TerrainMidlet.myInfo.IDDB) {
            this.indexOfMe = by;
        }
    }

    protected void doAddFriend() {
        Vector<Command> vector = new Vector<Command>();
        for (int i = 0; i < this.playerInfos.size(); ++i) {
            final PlayerInfo playerInfo = (PlayerInfo)this.playerInfos.elementAt(i);
            if (playerInfo.IDDB == TerrainMidlet.myInfo.IDDB || playerInfo.IDDB == -1) continue;
            vector.addElement(new Command(playerInfo.name, new IAction(){

                public void perform() {
                    GameService.gI().addFriend(playerInfo.IDDB);
                    CCanvas.startWaitDlg(Language.adding() + " " + playerInfo.name);
                }
            }));
        }
        CCanvas.menu.startAt(vector, 0);
    }

    protected void doKick() {
        Vector<Command> vector = new Vector<Command>();
        for (int i = 0; i < this.playerInfos.size(); ++i) {
            final PlayerInfo playerInfo = (PlayerInfo)this.playerInfos.elementAt(i);
            if (playerInfo.IDDB == TerrainMidlet.myInfo.IDDB || playerInfo.IDDB == -1) continue;
            vector.addElement(new Command(playerInfo.name, new IAction(){

                public void perform() {
                    if (playerInfo.isReady) {
                        CCanvas.startOKDlg(Language.cannotKick());
                    } else {
                        GameService.gI().kick(playerInfo.IDDB);
                        playerInfo.IDDB = -1;
                        playerInfo.name = "";
                    }
                }
            }));
        }
        CCanvas.menu.startAt(vector, 0);
    }

    protected void doStartGame() {
        int n;
        if (isRelayRoom) {
            if (RelayService.members.size() < 2) {
                CCanvas.startOKDlg("C\u1ea7n \u00edt nh\u1ea5t 2 ng\u01b0\u1eddi ch\u01a1i \u0111\u1ec3 b\u1eaft \u0111\u1ea7u.");
            } else {
                CCanvas.startOKDlg("Tr\u1eadn \u0111\u1ea5u multiplayer \u0111ang \u0111\u01b0\u1ee3c ph\u00e1t tri\u1ec3n (giai \u0111o\u1ea1n 4).");
            }
            return;
        }
        if (GameMidlet.pendingOfflineBossRoomIndex >= 0) {
            CCanvas.startWaitDlg(Language.loading());
            GameMidlet.beginPendingOfflineBossFight();
            return;
        }
        if (GameMidlet.pendingOfflinePvpBot) {
            CCanvas.startWaitDlg(Language.loading());
            GameMidlet.beginPendingOfflinePvpBotFight();
            return;
        }
        int n2 = 0;
        int n3 = 0;
        int n4 = 0;
        int n5 = 0;
        for (n = 0; n < this.playerInfos.size(); ++n) {
            PlayerInfo playerInfo = (PlayerInfo)this.playerInfos.elementAt(n);
            if (playerInfo.IDDB != TerrainMidlet.myInfo.IDDB && playerInfo.IDDB != -1) {
                if (playerInfo.isReady) {
                    ++n2;
                } else {
                    ++n3;
                }
            }
            if (playerInfo.IDDB == -1) continue;
            if (n % 2 == 0) {
                ++n4;
                continue;
            }
            ++n5;
        }
        if (n2 != 0 && n3 <= 0) {
            if (this.money > TerrainMidlet.myInfo.xu) {
                CCanvas.startOKDlg(Language.notEnoughMoney());
            }
            CCanvas.startWaitDlg(Language.loading());
            if (this.itemCur != null && this.itemCur.length > 0) {
                for (n = 0; n < this.itemCur.length; ++n) {
                    if (n == this.itemCur.length - 1 && ShopItem.getI((int)15).num <= 0) {
                        this.itemCur[n] = -1;
                    }
                    if (n == this.itemCur.length - 2 && ShopItem.getI((int)14).num <= 0) {
                        this.itemCur[n] = -1;
                    }
                    if (n == this.itemCur.length - 3 && ShopItem.getI((int)13).num <= 0) {
                        this.itemCur[n] = -1;
                    }
                    if (n != this.itemCur.length - 4 || ShopItem.getI((int)12).num > 0) continue;
                    this.itemCur[n] = -1;
                }
            }
            GameService.gI().changeItem(this.itemCur);
            GameService.gI().startGame();
        } else {
            CCanvas.startOKDlg(Language.notReady());
        }
    }

    protected void doChat() {
        String string;
        if (this.chatDelay == 0 && !(string = tfChat.getText()).trim().equals("")) {
            tfChat.setText("");
            if (isRelayRoom) {
                RelayService.sendChat(string);
            } else {
                GameService.gI().chatToBoard(string);
                this.showChat(TerrainMidlet.myInfo.IDDB, string, 90);
            }
            this.chatDelay = 30;
        }
    }

    private void doReady() {
        if (isRelayRoom) {
            PlayerInfo playerInfo = (PlayerInfo)this.playerInfos.elementAt(this.indexOfMe);
            playerInfo.isReady = !playerInfo.isReady;
            return;
        }
        if (GameMidlet.pendingOfflineBossRoomIndex >= 0) {
            CCanvas.startWaitDlg(Language.loading());
            GameMidlet.beginPendingOfflineBossFight();
            return;
        }
        if (GameMidlet.pendingOfflinePvpBot) {
            CCanvas.startWaitDlg(Language.loading());
            GameMidlet.beginPendingOfflinePvpBotFight();
            return;
        }
        if (this.readyDelay == 0) {
            boolean bl;
            boolean bl2 = bl = !((PlayerInfo)this.playerInfos.elementAt((int)this.indexOfMe)).isReady;
            if (bl) {
                if (TerrainMidlet.myInfo.xu < this.money) {
                    CCanvas.startOKDlg(Language.cannotReady());
                    return;
                }
                this.readyDelay = 30;
            }
            CCanvas.startWaitDlg(Language.areReady());
            if (bl) {
                if (this.itemCur != null && this.itemCur.length > 0) {
                    for (int i = 0; i < this.itemCur.length; ++i) {
                        if (i == this.itemCur.length - 1 && ShopItem.getI((int)15).num <= 0) {
                            this.itemCur[i] = -1;
                        }
                        if (i == this.itemCur.length - 2 && ShopItem.getI((int)14).num <= 0) {
                            this.itemCur[i] = -1;
                        }
                        if (i == this.itemCur.length - 3 && ShopItem.getI((int)13).num <= 0) {
                            this.itemCur[i] = -1;
                        }
                        if (i != this.itemCur.length - 4 || ShopItem.getI((int)12).num > 0) continue;
                        this.itemCur[i] = -1;
                    }
                }
                GameService.gI().changeItem(this.itemCur);
            }
            GameService.gI().ready(bl);
            this.resetItemEquip();
        }
    }

    protected void doSelectMap() {
        CCanvas.roomListScr.isBoss = currLevel == 5;
        CCanvas.roomListScr.show();
    }

    private void doInvite() {
        GameService.gI().inviteFriend(true, -1);
        CCanvas.startOKDlg(Language.pleaseWait());
    }

    protected void doSettingMoney() {
        CCanvas.inputDlg.setInfo(Language.totalstakes(), new IAction(){

            public void perform() {
                try {
                    int n = Integer.parseInt(CCanvas.inputDlg.tfInput.getText());
                    if (n < 0) {
                        return;
                    }
                    CCanvas.endDlg();
                    if (n > TerrainMidlet.myInfo.xu) {
                        CCanvas.startOKDlg(Language.onlyHave() + TerrainMidlet.myInfo.xu);
                        return;
                    }
                    GameService.gI().setMoney(n);
                    for (int i = 0; i < PrepareScr.this.playerInfos.size(); ++i) {
                        ((PlayerInfo)PrepareScr.this.playerInfos.elementAt((int)i)).isReady = false;
                    }
                }
                catch (Exception exception) {
                }
            }
        }, new IAction(){

            public void perform() {
                CCanvas.endDlg();
            }
        }, 1);
        CCanvas.inputDlg.show();
    }

    private void doSettingPassword() {
        CCanvas.inputDlg.setInfo(Language.setPass(), new IAction(){

            public void perform() {
                GameService.gI().setPassword(CCanvas.inputDlg.tfInput.getText());
                CCanvas.startOKDlg(Language.setPassed());
            }
        }, new IAction(){

            public void perform() {
                CCanvas.endDlg();
            }
        }, 3);
        CCanvas.inputDlg.show();
    }

    private void doSettingBoardName() {
        CCanvas.inputDlg.setInfo(Language.setBoardName(), new IAction(){

            public void perform() {
                if (!CCanvas.inputDlg.tfInput.getText().equals("")) {
                    String string = CCanvas.inputDlg.tfInput.getText();
                    GameService.gI().setBoardName(string);
                    BoardListScr.boardName = string;
                    CCanvas.startOKDlg(Language.setBoardNamefinish());
                }
            }
        }, new IAction(){

            public void perform() {
                CCanvas.endDlg();
            }
        }, 0);
        CCanvas.inputDlg.show();
    }

    protected void doLeaveBoard() {
        if (isRelayRoom) {
            RelayService.leaveRoom();
            return;
        }
        GameService.gI().leaveBoard();
    }

    protected void doSetMaxPlayer() {
        Vector<Command> vector = new Vector<Command>();
        vector.addElement(new Command("4 vs 4", new IAction(){

            public void perform() {
                GameService.gI().setMaxPlayer(8);
            }
        }));
        vector.addElement(new Command("3 vs 3", new IAction(){

            public void perform() {
                GameService.gI().setMaxPlayer(6);
            }
        }));
        vector.addElement(new Command("2 vs 2", new IAction(){

            public void perform() {
                GameService.gI().setMaxPlayer(4);
            }
        }));
        vector.addElement(new Command("1 vs 1", new IAction(){

            public void perform() {
                GameService.gI().setMaxPlayer(2);
            }
        }));
        CCanvas.menu.startAt(vector, 0);
    }

    public void doShowMenuPrepare() {
        Vector<Command> vector = new Vector<Command>();
        vector.addElement(new Command("N\u00e2ng c\u1ea5p", new IAction(){

            public void perform() {
                if (CCanvas.levelScreen == null) {
                    CCanvas.levelScreen = new LevelScreen();
                }
                CCanvas.levelScreen.show(PrepareScr.this);
            }
        }));
        if (isBossRoom && curMap >= 30 && curMap <= 39) {
            vector.addElement(new Command("Ph\u1ea7n th\u01b0\u1edfng", new IAction(){

                public void perform() {
                    byte by = (byte)(curMap - 30);
                    CCanvas.startOKDlg(OfflineBossReward.describeRewards(by));
                }
            }));
        }
        vector.addElement(new Command("R\u1eddi ph\u00f2ng", new IAction(){

            public void perform() {
                PrepareScr.this.doLeaveBoard();
            }
        }));
        CCanvas.menu.startAt(vector, 0);
    }

    public void onMapChanged(byte by) {
        CCanvas.endDlg();
        curMap = by;
    }

    public void onSomeOneChangeGun(int n, byte by) {
        this.getPlayerFromID((int)n).gun = by;
        this.showChat(n, "\u0110\u1ed5i s\u00fang\n" + GUN_NAME[by] + " !", 40);
    }

    public int getNumberByID(int n) {
        for (int i = 0; i < this.playerInfos.size(); ++i) {
            PlayerInfo playerInfo = (PlayerInfo)this.playerInfos.elementAt(i);
            if (playerInfo.IDDB != n) continue;
            return i;
        }
        return -1;
    }

    private void showTipWeapon() {
        byte by = ((PlayerInfo)this.playerInfos.elementAt((int)this.indexOfMe)).gun;
    }

    private void showTipChooseWeapon(int n, int n2) {
    }

    private void resetItemEquip() {
        this.itemCur = ShopItem.checkSetItem(this.itemCur);
    }

    private void showTipItem() {
    }

    public void update() {
        if (isRelayRoom && !RelayService.isActive()) {
            isRelayRoom = false;
            if (CCanvas.menuScr != null) {
                CCanvas.menuScr.show();
            }
            return;
        }
        if (this.readyDelay > 0) {
            --this.readyDelay;
        }
        if (this.chatDelay > 0) {
            --this.chatDelay;
        }
        if (!this.isChooseItem) {
            this.left = this.cmdMenu;
            if (this.ownerID == TerrainMidlet.myInfo.IDDB) {
                this.right = this.cmdStart;
                this.cmdStart.caption = Language.begin();
            } else {
                this.right = currLevel != 7 ? this.cmdReady : this.cmdStart;
                this.cmdReady.caption = Language.ready();
                for (int i = 0; i < this.playerInfos.size(); ++i) {
                    PlayerInfo playerInfo = (PlayerInfo)this.playerInfos.elementAt(i);
                    if (playerInfo.IDDB != TerrainMidlet.myInfo.IDDB || !playerInfo.isReady) continue;
                    this.cmdReady.caption = Language.noReady();
                    this.center = null;
                    this.right = this.cmdReady;
                }
            }
        } else {
            this.right = this.cmdBack;
        }
        this.doChat();
        tfChat.update();
        if (this.isChooseItem) {
            prepareScrItemIcon.update();
        }
        Cloud.updateCloud();
    }

    public void mainLoop() {
        super.mainLoop();
        if (this.isChooseItem) {
            prepareScrItemIcon.mainLoop();
        }
    }

    public void paint(mGraphics mGraphics2) {
        PrepareScr.paintDefaultBg(mGraphics2);
        this.paintBack(mGraphics2);
        Cloud.paintCloud(mGraphics2);
        if (!GameScr.trainingMode) {
            this.paintPlayerPos(mGraphics2);
            this.paintPlayer(mGraphics2);
            this.paintMap(mGraphics2);
            this.paintItem(mGraphics2, this.x_item, this.y_item);
            if (this.isChooseItem) {
                this.paintChooseItem(CScreen.w - PrepareScr.prepareScrItemIcon.shopW >> 1, this.y_item - 102, mGraphics2);
            }
            mGraphics2.translate(-mGraphics2.getTranslateX(), -mGraphics2.getTranslateY());
            if (CCanvas.isTouch) {
                mGraphics2.drawImage(iconChat, 30, 10, 0, false);
            }
            super.paint(mGraphics2);
        }
    }

    static int offlineStandLift() {
        return 28;
    }

    private void paintPlayerPos(mGraphics mGraphics2) {
        int n;
        if (CCanvas.isTouch) {
            mGraphics2.translate(0, -15);
            mGraphics2.translate(0, -5);
        }
        int n2 = PrepareScr.offlineStandLift();
        int n3 = CCanvas.hieght - n2;
        mGraphics2.setColor(12965614);
        mGraphics2.fillRect(0, n3 - 96, CCanvas.width, 45, false);
        for (n = 0; n < CCanvas.width / PrepareScr.line.image.getWidth(); ++n) {
            mGraphics2.drawRegion(line, 0, 16, 33, 8, 0, (n + 1) * 33, n3 - 96, 0, false);
        }
        mGraphics2.fillRect(0, n3 - 68, CCanvas.width, 68 + n2, false);
        mGraphics2.fillRect(0, n3 - 96 - 12, 75, 45, false);
        mGraphics2.fillRect(CCanvas.width - 75, n3 - 96 - 12, 75, 45, false);
        mGraphics2.setColor(6985149);
        mGraphics2.drawRect(-1, n3 - 96 - 12, 76, 45, false);
        mGraphics2.drawRect(CCanvas.width - 76, n3 - 96 - 12, 76, 45, false);
        mGraphics2.setColor(9289);
        mGraphics2.drawLine(76, n3 - 96 - 12, 76, n3 - 96 + 33, false);
        mGraphics2.drawLine(CCanvas.width - 75, n3 - 96 - 12, CCanvas.width - 75, n3 - 96 + 33, false);
        for (n = 0; n < CCanvas.width; ++n) {
            mGraphics2.drawRegion(line, 0, 16, 33, 8, 0, n * 33, n3 - 68, 0, false);
        }
        mGraphics2.setColor(6985149);
        mGraphics2.drawLine(0, n3 - 60, CCanvas.width, n3 - 60, false);
        for (n = 0; n < 2; ++n) {
            mGraphics2.drawRegion(line, 0, 16, 33, 8, 0, n * 33, n3 - 96 - 20, 0, false);
            mGraphics2.drawRegion(line, 0, 16, 33, 8, 0, CCanvas.width - n * 33, n3 - 96 - 20, 24, false);
        }
        mGraphics2.drawRegion(line, 0, 0, 33, 8, 0, 66, n3 - 96 - 20, 0, false);
        mGraphics2.drawRegion(line, 0, 8, 33, 8, 0, CCanvas.width - 66, n3 - 96 - 20, 24, false);
        if (CCanvas.hieght >= 300) {
            mGraphics2.drawRegion(line, 0, 16, 33, 8, 0, CCanvas.width / 2, n3 - 96, mGraphics.TOP | mGraphics.HCENTER, false);
            mGraphics2.drawRegion(line, 0, 0, 33, 8, 0, CCanvas.width / 2 + 16, n3 - 96, 0, false);
            mGraphics2.drawRegion(line, 0, 8, 33, 8, 0, CCanvas.width / 2 - 16, n3 - 96, 24, false);
            mGraphics2.setColor(12965614);
            mGraphics2.fillRect(CCanvas.width / 2 - 30, n3 - 96 + 8, 60, 20, false);
            mGraphics2.setColor(6985149);
            mGraphics2.drawRect(CCanvas.width / 2 - 29, n3 - 96 + 7, 58, 21, false);
            mGraphics2.setColor(9289);
            mGraphics2.drawRect(CCanvas.width / 2 - 30, n3 - 96 + 7, 60, 21, false);
        }
        mGraphics2.translate(0, -mGraphics2.getTranslateY());
    }

    private void paintMap(mGraphics mGraphics2) {
        int n;
        boolean bl = false;
        if (CCanvas.hieght < 300) {
            if (CCanvas.width <= 300) {
                Font.borderFont.drawString(mGraphics2, Language.room() + ": " + currentRoom, CCanvas.hw, 10, 2);
                Font.borderFont.drawString(mGraphics2, BoardListScr.boardName, CCanvas.hw, 25, 2);
                Font.borderFont.drawString(mGraphics2, curMap + 1 + ". " + MM.mapName[curMap], CCanvas.hw, 40, 2);
                bl = false;
            } else {
                bl = true;
                mGraphics2.setColor(0xFFFFFF);
                mGraphics2.fillRect(CCanvas.hw - 88, 13, 76, 46, false);
                Font.borderFont.drawString(mGraphics2, Language.room() + ": " + currentRoom, CCanvas.hw, 12, 0);
                Font.borderFont.drawString(mGraphics2, BoardListScr.boardName, CCanvas.hw, 27, 0);
                Font.borderFont.drawString(mGraphics2, curMap + 1 + ". " + MM.mapName[curMap], CCanvas.hw, 42, 0);
                this.xPaintMap = CCanvas.hw - 50;
                this.yPaintMap = 36;
                this.anchorPainMap = 3;
                if (this.money != 0) {
                    Font.borderFont.drawString(mGraphics2, this.money + Language.xu(), CCanvas.width / 2, 65, 3);
                }
            }
            if (CCanvas.width < 200 && this.money != 0) {
                Font.borderFont.drawString(mGraphics2, this.money + Language.xu(), CCanvas.width / 2, 60, 3);
            }
        } else {
            if (this.money != 0) {
                Font.borderFont.drawString(mGraphics2, this.money + Language.xu(), CCanvas.width / 2, 120, 3);
            }
            bl = true;
            this.xPaintMap = CCanvas.hw;
            this.yPaintMap = 36;
            this.anchorPainMap = 3;
            mGraphics2.setColor(0xFFFFFF);
            mGraphics2.fillRect(this.xPaintMap - 38, this.yPaintMap - 26 + 1, 76, this.yPaintMap + 10, false);
            Font.borderFont.drawString(mGraphics2, Language.room() + ": " + currentRoom, this.xPaintMap, 85, 2);
            Font.borderFont.drawString(mGraphics2, BoardListScr.boardName, this.xPaintMap, 100, 2);
            Font.borderFont.drawString(mGraphics2, curMap + 1 + ". " + MM.mapName[curMap], this.xPaintMap, 70, 2);
            if (curMap >= 30 && curMap <= 39) {
                byte by = (byte)(curMap - 30);
                int n2 = OfflineBossFight.winXuReward(by);
                n = OfflineBossFight.winLuongReward(by);
                String string = OfflineBossReward.tierName(by);
                Font.borderFont.drawString(mGraphics2, "Tier " + string + " - " + Language.xu() + " " + n2 + " - " + Language.luong() + " " + n, this.xPaintMap, 115, 2);
            }
        }
        if (bl) {
            try {
                if (imgMap[curMap] != null) {
                    mGraphics2.drawImage(imgMap[curMap], this.xPaintMap, this.yPaintMap, mGraphics.VCENTER | mGraphics.HCENTER, false);
                }
            }
            catch (Exception exception) {
                mGraphics2.drawImage(randomMap, this.xPaintMap, this.yPaintMap, mGraphics.VCENTER | mGraphics.HCENTER, false);
            }
            mGraphics2.drawImage(khungMap, this.xPaintMap, this.yPaintMap, 3, false);
        }
        PlayerInfo playerInfo = TerrainMidlet.myInfo;
        String string = CCanvas.width >= 200 ? playerInfo.getStrMoney() : playerInfo.getStrMoney2();
        n = 35;
        int n3 = 19;
        int n4 = 20;
        int n5 = 26;
        int n6 = 17;
        Font.normalFont.drawString(mGraphics2, "Lvl " + playerInfo.level2 + "  " + string, 6, CCanvas.hieght - cmdH - n, 0, false);
        Font.normalFont.drawString(mGraphics2, "Exp ", 6, CCanvas.hieght - cmdH - n3, 0, false);
        LevelScreen.paintLevelPercen(mGraphics2, 5, CCanvas.hieght - cmdH - n4);
        if (CCanvas.width > 200) {
            int n7 = 105 + (this.x_item - 105) / 2;
            mGraphics2.drawImage(CScreen.cup, n7, CCanvas.hieght - cmdH - n5, 3, false);
            Font.borderFont.drawString(mGraphics2, playerInfo.cup + " ", n7, CCanvas.hieght - cmdH - n6, 3);
        }
    }

    public void paintBack(mGraphics mGraphics2) {
        for (int i = 0; i < CCanvas.width; i += 32) {
            mGraphics2.drawImage(imgBack, i, CCanvas.hieght - 96 - 15, 0, false);
        }
    }

    public static void paintQuanHam(int n, int n2, int n3, int n4, mGraphics mGraphics2) {
        try {
            mGraphics2.drawRegion(imgQuanHam, 0, 12 * n, 12, 12, 0, n2, n3, n4, false);
        }
        catch (Exception exception) {
        }
    }

    public void paintPlayer(mGraphics mGraphics2) {
        mGraphics2.translate(0, -15);
        if (this.playerInfos != null && !GameScr.trainingMode) {
            int n;
            boolean bl;
            int n2;
            int n3;
            PlayerInfo playerInfo;
            int n4;
            int n5;
            int n6;
            int n7;
            int n8;
            if (currLevel != 7) {
                n8 = isPvpBotRoom ? Math.min(OfflinePvpBot.pendingSquadCount, this.playerInfos.size()) : this.playerInfos.size();
                n7 = 0;
                n6 = 0;
                if (isPvpBotRoom) {
                    n5 = CCanvas.hieght - PrepareScr.offlineStandLift();
                    n7 = n5 - 66;
                    n6 = n7 - 48;
                }
                for (n4 = 0; n4 < n8; ++n4) {
                    boolean bl2;
                    playerInfo = (PlayerInfo)this.playerInfos.elementAt(n4);
                    if (playerInfo.IDDB == -1) continue;
                    n5 = this.x_pos[n4];
                    n3 = this.y_pos[n4];
                    if (isPvpBotRoom) {
                        n2 = n4 % 4;
                        n5 = n2 == 0 || n2 == 2 ? 24 : 62;
                        n3 = n2 >= 2 ? n6 : n7;
                    }
                    boolean bl3 = bl = n4 % 2 != 0;
                    n2 = isPvpBotRoom ? 2 : (!bl ? 2 : 0);
                    CPlayer.paintSimplePlayer(playerInfo.gun, 5, n5 + (n2 == 2 ? 2 : -2), n3 + (CCanvas.isTouch ? 8 : 13), n2, playerInfo.myEquip, mGraphics2);
                    if (playerInfo.clanIcon != null) {
                        mGraphics2.drawImage(playerInfo.clanIcon, n5 - 18, n3 - (cmdH - 4), 0, false);
                    }
                    PrepareScr.paintQuanHam(playerInfo.nQuanHam2, n5, n3 - 15, mGraphics.VCENTER | mGraphics.HCENTER, mGraphics2);
                    if (n4 % 2 == 0) {
                        Font.smallFontRed.drawString(mGraphics2, playerInfo.name.toUpperCase(), n5 + 5, n3 + 15, 2);
                    } else {
                        Font.smallFontYellow.drawString(mGraphics2, playerInfo.name.toUpperCase(), n5 + 5, n3 + 15, 2);
                    }
                    n = !isRelayRoom ? (n4 == 0 ? 1 : 0) : (playerInfo.IDDB == this.ownerID ? 1 : 0);
                    bl2 = !isRelayRoom ? (playerInfo.isReady && n4 != 0) : (playerInfo.isReady && playerInfo.IDDB != this.ownerID);
                    if (bl2) {
                        Font.borderFont.drawString(mGraphics2, "ok", n5, n3 - 32, 2);
                    }
                    if (n == 0) continue;
                    Font.borderFont.drawString(mGraphics2, Language.boss(), n5, n3 - 32, 2);
                }
            } else {
                for (n4 = 0; n4 < this.playerInfos.size(); ++n4) {
                    playerInfo = (PlayerInfo)this.playerInfos.elementAt(n4);
                    if (playerInfo.IDDB == -1) continue;
                    bl = n4 % 2 != 0;
                    CPlayer.paintSimplePlayer(playerInfo.gun, 5, this.x_pos[0] + (!bl ? 2 : -2), this.y_pos[0] + (CCanvas.isTouch ? 8 : 13), !bl ? 2 : 0, playerInfo.myEquip, mGraphics2);
                    if (playerInfo.clanIcon != null) {
                        mGraphics2.drawImage(playerInfo.clanIcon, this.x_pos[0] - 18, this.y_pos[0] - (cmdH - 4), 0, false);
                    }
                    PrepareScr.paintQuanHam(playerInfo.nQuanHam2, this.x_pos[0], this.y_pos[0] - 15, mGraphics.VCENTER | mGraphics.HCENTER, mGraphics2);
                    if (n4 % 2 == 0) {
                        Font.smallFontRed.drawString(mGraphics2, playerInfo.name.toUpperCase(), this.x_pos[0] + 5, this.y_pos[0] + 15, 2);
                    } else {
                        Font.smallFontYellow.drawString(mGraphics2, playerInfo.name.toUpperCase(), this.x_pos[0] + 5, this.y_pos[0] + 15, 2);
                    }
                    if (playerInfo.isReady && playerInfo.IDDB != this.ownerID) {
                        Font.borderFont.drawString(mGraphics2, "ok", this.x_pos[0], this.y_pos[0] - 32, 2);
                    }
                    if (playerInfo.IDDB != this.ownerID) continue;
                    Font.borderFont.drawString(mGraphics2, Language.boss(), this.x_pos[0], this.y_pos[0] - 32, 2);
                }
            }
            if (CCanvas.hieght > 220 && currLevel == 5) {
                n8 = 28;
                n7 = CCanvas.hieght - PrepareScr.offlineStandLift() - 53 - n8;
                if (isPvpBotRoom) {
                    n6 = CCanvas.hieght - PrepareScr.offlineStandLift();
                    n5 = n6 - 66;
                    n3 = n5 - 48;
                    n2 = this.bossInfos.size();
                    for (n = 0; n < n2; ++n) {
                        PlayerInfo playerInfo2 = (PlayerInfo)this.bossInfos.elementAt(n);
                        int n10 = n % 4;
                        int n11 = n10 == 0 || n10 == 2 ? CCanvas.width - 24 : CCanvas.width - 62;
                        int n12 = n10 >= 2 ? n3 : n5;
                        CPlayer.paintSimplePlayer(playerInfo2.gun, 5, n11 - 2, n12 + (CCanvas.isTouch ? 8 : 13), 0, playerInfo2.myEquip, mGraphics2);
                        if (n % 2 == 0) {
                            Font.smallFontRed.drawString(mGraphics2, GUN_NAME[playerInfo2.gun], n11 + 5, n12 + 15, 2);
                        } else {
                            Font.smallFontYellow.drawString(mGraphics2, GUN_NAME[playerInfo2.gun], n11 + 5, n12 + 15, 2);
                        }
                        Font.borderFont.drawString(mGraphics2, "ok", n11, n12 - 32, 2);
                    }
                } else {
                    n4 = curMap - 30;
                    if (n4 >= 0 && n4 < bossID.length) {
                        CPlayer.paintSimplePlayer(bossID[n4], CCanvas.gameTick % 10 > 5 ? 0 : 1, CCanvas.width / 2, n7 - (CCanvas.isTouch ? 4 : 0), 2, null, mGraphics2);
                    }
                }
            }
            mGraphics2.translate(0, -mGraphics2.getTranslateY());
        }
    }

    public void paintChooseItem(int n, int n2, mGraphics mGraphics2) {
        int n3 = (CCanvas.hieght - CScreen.cmdH) / 2 - 80;
        PrepareScr.paintBorderRect(mGraphics2, n3, CCanvas.isTouch ? 4 : 3, CCanvas.isTouch ? 138 : 118, Language.chonItem());
        prepareScrItemIcon.paint(n, n3 + 25, mGraphics2, true, ShopItem.getItemNum());
        prepareScrItemIcon.setPosTitle(CCanvas.width / 2, n3 + 3);
        int n4 = CCanvas.width / 2;
        int n5 = n3 + (CCanvas.isTouch ? 115 : 95);
        Font.normalFont.drawString(mGraphics2, ShopItem.getI((int)PrepareScr.prepareScrItemIcon.select).decription, n4, n5, 3);
    }

    public void paintChooseGun(int n, int n2, mGraphics mGraphics2) {
        for (int i = 0; i < 10; ++i) {
            if (i == this.selectedGunLv2) {
                mGraphics2.setColor(CCanvas.gameTick % 10 > 5 ? 0xFFFF00 : 0xFF0000);
                mGraphics2.fillRect(n + i * 26 - 1, n2 - 1, 26, 18, false);
            }
            mGraphics2.drawRegion(imgGun, 0, i * 16, 24, 16, 0, n + i * 26, n2, 0, false);
        }
    }

    public void getIcon() {
        for (int i = 0; i < this.playerInfos.size(); ++i) {
            PlayerInfo playerInfo = (PlayerInfo)this.playerInfos.elementAt(i);
            GameService.gI().getClanIcon(playerInfo.clanID);
        }
    }

    public void show() {
        super.show();
        CPlayer.isShooting = false;
        if (CCanvas.gameScr != null) {
            CCanvas.gameScr.onClearMap();
            CCanvas.gameScr = null;
        }
        this.x_item = CCanvas.width - (Item.iWitdh * 18 + 5);
        this.y_item = CCanvas.hieght - (cmdH + 42) + 5;
        this.x_item_touch = CCanvas.width / 2 - 80 + 12;
        this.y_item_touch = CCanvas.hieght / 2 - 40;
        this.initPosPlayers();
        prepareScrItemIcon.setIAction(new IAction(){

            public void perform() {
                PrepareScr.this.doChooseItemEquip();
            }
        });
        this.resetItemEquip();
        this.resetReady();
        PrepareScr.prepareScrItemIcon.titleTem = Language.chonItem();
    }

    public void onResetPrepare() {
        this.resetItemEquip();
    }

    public void paintItem(mGraphics mGraphics2, int n, int n2) {
        Item.DrawSetItem(mGraphics2, this.itemCur, this.selectedItem, n, n2, false, null);
        for (int i = 0; i < this.itemCur.length; ++i) {
            if (i == this.itemCur.length - 1) {
                this.paintPassiveItem_NumUse(ShopItem.getI((int)15).num, n + 54 + 9, n2 + 28, mGraphics2);
            }
            if (i == this.itemCur.length - 2) {
                this.paintPassiveItem_NumUse(ShopItem.getI((int)14).num, n + 36 + 9, n2 + 28, mGraphics2);
            }
            if (i == this.itemCur.length - 3) {
                this.paintPassiveItem_NumUse(ShopItem.getI((int)13).num, n + 18 + 9, n2 + 28, mGraphics2);
            }
            if (i != this.itemCur.length - 4) continue;
            this.paintPassiveItem_NumUse(ShopItem.getI((int)12).num, n + 9, n2 + 28, mGraphics2);
        }
    }

    public void paintPassiveItem_NumUse(int n, int n2, int n3, mGraphics mGraphics2) {
        Font.smallFontYellow.drawString(mGraphics2, String.valueOf(n), n2, n3, 0);
    }

    public String getPlayerNameFromID(int n) {
        for (int i = 0; i < this.playerInfos.size(); ++i) {
            PlayerInfo playerInfo = (PlayerInfo)this.playerInfos.elementAt(i);
            if (playerInfo.IDDB != n) continue;
            return playerInfo.name;
        }
        return "";
    }

    public PlayerInfo getPlayerFromID(int n) {
        for (int i = 0; i < this.playerInfos.size(); ++i) {
            PlayerInfo playerInfo = (PlayerInfo)this.playerInfos.elementAt(i);
            if (playerInfo.IDDB != n) continue;
            return playerInfo;
        }
        return null;
    }

    public void setPlayers(int n, int n2, Vector vector) {
        int n3;
        this.money = n2;
        this.playerInfos = new Vector();
        for (n3 = 0; n3 < vector.size(); ++n3) {
            this.playerInfos.addElement(vector.elementAt(n3));
        }
        this.setOwner(n);
        for (n3 = 0; n3 < vector.size(); ++n3) {
            if (((PlayerInfo)vector.elementAt((int)n3)).IDDB != TerrainMidlet.myInfo.IDDB) continue;
            this.indexOfMe = n3;
            break;
        }
    }

    public void setOwner(int n) {
        this.ownerID = n;
        for (int i = 0; i < this.playerInfos.size(); ++i) {
            PlayerInfo playerInfo = (PlayerInfo)this.playerInfos.elementAt(i);
            if (playerInfo.IDDB != this.ownerID) continue;
            playerInfo.isReady = true;
        }
    }

    public void showChat(int n, String string, int n2) {
        int n3 = this.getNumberByID(n);
        ChatPopup chatPopup = new ChatPopup();
        chatPopup.show(n2, this.x_pos[n3], this.y_pos[n3] - 30, string);
        CCanvas.arrPopups.addElement(chatPopup);
    }

    public void stopGame() {
        this.isGameEnd = true;
    }

    public void playerLeave(int n) {
        for (int i = 0; i < this.playerInfos.size(); ++i) {
            PlayerInfo playerInfo = (PlayerInfo)this.playerInfos.elementAt(i);
            if (playerInfo.IDDB != n) continue;
            playerInfo.IDDB = -1;
            playerInfo.name = "";
            playerInfo.isReady = false;
        }
    }

    public void resetReady() {
        for (int i = 0; i < this.playerInfos.size(); ++i) {
            PlayerInfo playerInfo = (PlayerInfo)this.playerInfos.elementAt(i);
            playerInfo.isReady = playerInfo.IDDB == this.ownerID;
        }
    }

    public void setReady(int n, boolean bl) {
        for (int i = 0; i < this.playerInfos.size(); ++i) {
            PlayerInfo playerInfo = (PlayerInfo)this.playerInfos.elementAt(i);
            if (playerInfo.IDDB != n) continue;
            playerInfo.isReady = bl;
        }
    }

    public void setMoney(int n) {
        this.money = n;
        for (int i = 0; i < this.playerInfos.size(); ++i) {
            ((PlayerInfo)this.playerInfos.elementAt((int)i)).isReady = false;
        }
    }

    public Vector getPlayerInfos() {
        return this.playerInfos;
    }

    public void keyPressed(int n) {
        if (this.handleItemKeyInput()) {
            return;
        }
        super.keyPressed(n);
    }

    private boolean handleItemKeyInput() {
        if (this.curUsing != 0 || GameScr.trainingMode) {
            return false;
        }
        if (this.isChooseItem) {
            if (CCanvas.keyPressed[2] || CCanvas.keyPressed[4] || CCanvas.keyPressed[6] || CCanvas.keyPressed[8]) {
                prepareScrItemIcon.onPointerPressed(0, 0, 0);
                return true;
            }
            if (CCanvas.keyPressed[5]) {
                CCanvas.keyPressed[5] = false;
                this.doChooseItemEquip();
                return true;
            }
            if (CCanvas.keyPressed[13]) {
                CCanvas.keyPressed[13] = false;
                this.isTouchItem = false;
                this.isChooseItem = false;
                return true;
            }
            return false;
        }
        if (CCanvas.keyPressed[2] || CCanvas.keyPressed[4] || CCanvas.keyPressed[6] || CCanvas.keyPressed[8]) {
            this.moveSelectedItemByKeys();
            return true;
        }
        if (CCanvas.keyPressed[5]) {
            CCanvas.keyPressed[5] = false;
            this.doChooseItemEquip();
            return true;
        }
        return false;
    }

    private void moveSelectedItemByKeys() {
        if (CCanvas.keyPressed[4]) {
            --this.selectedItem;
        }
        if (CCanvas.keyPressed[6]) {
            ++this.selectedItem;
        }
        if (CCanvas.keyPressed[2]) {
            this.selectedItem -= 4;
        }
        if (CCanvas.keyPressed[8]) {
            this.selectedItem += 4;
        }
        while (this.selectedItem < 0) {
            this.selectedItem += this.itemCur.length;
        }
        while (this.selectedItem >= this.itemCur.length) {
            this.selectedItem -= this.itemCur.length;
        }
        CScreen.clearKey();
    }

    public void onPointerPressed(int n, int n2, int n3) {
        if (this.curUsing == 0 && !GameScr.trainingMode) {
            if (this.isChooseItem) {
                prepareScrItemIcon.onPointerPressed(n, n2, n3);
                if (CCanvas.keyPressed[5]) {
                    CCanvas.keyPressed[5] = false;
                    this.doChooseItemEquip();
                }
            } else if (CCanvas.keyPressed[2] || CCanvas.keyPressed[4] || CCanvas.keyPressed[6] || CCanvas.keyPressed[8]) {
                this.moveSelectedItemByKeys();
            } else if (CCanvas.keyPressed[5]) {
                CCanvas.keyPressed[5] = false;
                this.doChooseItemEquip();
            }
        }
        super.onPointerPressed(n, n2, n3);
    }

    public void onPointerReleased(int n, int n2, int n3) {
        super.onPointerReleased(n, n2, n3);
        if (this.isChooseItem) {
            prepareScrItemIcon.onPointerReleased(n, n2, n3);
        } else {
            if (this.anchorPainMap == 3) {
                this.xPaintMap -= 38;
                this.yPaintMap -= 23;
            }
            if (CCanvas.isPointer(this.xPaintMap, this.yPaintMap, 76, 46, n3)) {
                if (TerrainMidlet.myInfo.IDDB == this.ownerID && !isBossRoom && !isPvpBotRoom) {
                    this.doSelectMap();
                }
            } else if (CCanvas.isPointer(this.x_item - 10, this.y_item - 10, 300, 50, n3)) {
                this.isTouchItem = true;
                if (CCanvas.isDoubleClick) {
                    this.isChooseItem = true;
                    this.center = null;
                    this.left = null;
                }
                if (CCanvas.isPointer(this.x_item, this.y_item, 300, 50, n3)) {
                    int n4 = (n2 - this.y_item) / 20 * 4 + (n - this.x_item) / 20;
                    if (this.selectedItem != n4) {
                        this.selectedItem = n4;
                    } else if (this.center != null && this.center.action != null && CCanvas.isDoubleClick) {
                        this.cmdSelect.action.perform();
                    }
                }
            } else {
                this.isTouchItem = false;
                if (CCanvas.isPointer(30, 0, 50, 50, n3)) {
                    tfChat.doChangeToTextBox();
                }
            }
        }
    }

    public void onPointerDragged(int n, int n2, int n3) {
        super.onPointerDragged(n, n2, n3);
        if (this.isChooseItem) {
            prepareScrItemIcon.onPointerDragged(n, n2, n3);
        }
    }

    static {
        curMap = 0;
        numCurItemSlot = (byte)8;
        numUseSlot = (byte)4;
        bulletType = 0;
        roomID = -1;
        zoneID = -1;
        imgReady = new mImage[9];
        PrepareScr.imgReady[0] = GameScr.imgReady[0];
        PrepareScr.imgReady[1] = GameScr.imgReady[1];
        PrepareScr.imgReady[2] = GameScr.imgReady[2];
        PrepareScr.imgReady[3] = GameScr.imgReady[3];
        PrepareScr.imgReady[4] = GameScr.imgReady[4];
        imgQuanHam = GameScr.imgQuanHam;
        imgBack = GameScr.imgBack;
        imgGun = Bullet.imgGun;
        try {
            randomMap = mImage.createImage("/randomMap.png");
            line = mImage.createImage("/map/line.png");
            iconChat = mImage.createImage("/iconChat.png");
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
        isBossRoom = false;
        isPvpBotRoom = false;
        isRelayRoom = false;
    }
}

