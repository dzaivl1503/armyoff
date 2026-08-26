/*
 * Decompiled with CFR 0.152.
 */
package network;

import CLib.Image;
import CLib.RMS;
import CLib.mImage;
import CLib.mSound;
import CLib.mSystem;
import com.teamobi.mobiarmy2.GameMidlet;
import coreLG.CCanvas;
import coreLG.TerrainMidlet;
import java.util.Vector;
import model.AvatarInfo;
import model.CRes;
import model.Font;
import model.IAction;
import model.Language;
import model.MsgInfo;
import model.PlayerInfo;
import model.UserData;
import network.Command;
import network.GameService;
import network.IGameLogicHandler;
import network.Session_ME;
import player.PM;
import screen.BoardListScr;
import screen.ConfigScr;
import screen.ListScr;
import screen.LoginScr;
import screen.MenuScr;
import screen.PrepareScr;

public class GameLogicHandler
implements IGameLogicHandler {
    public static boolean isServerThongBao;
    private static GameLogicHandler instance;
    public static boolean isTryGetIPFromWap;

    public static GameLogicHandler gI() {
        return instance;
    }

    public static byte[] loadRMS(String string) {
        return RMS.loadRMS(string);
    }

    public static void saveRMS(String string, byte[] byArray) {
        try {
            RMS.saveRMS(string, byArray);
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public static void saveIP(String string) {
        GameLogicHandler.saveRMS("ARMY2", string.getBytes());
    }

    public static String loadIP() {
        byte[] byArray = GameLogicHandler.loadRMS("AMRY2");
        return byArray == null ? null : new String(byArray);
    }

    public void onConnectFail() {
        CCanvas.startOKDlg(Language.connectFail(), new IAction(){

            public void perform() {
                GameLogicHandler.this.onResetGame();
            }
        });
    }

    public void onConnectOK() {
        CCanvas.startWaitDlgWithoutCancel(Language.pleaseWait(), 10000);
    }

    public void onDisconnect() {
        this.onResetGame();
    }

    public void onResetGame() {
        try {
            CCanvas.onClearCCanvas();
            Session_ME.gI().close(1534);
            Session_ME.gI().start = true;
            CCanvas.endDlg();
            mSystem.my_Gc();
        }
        catch (Exception exception) {
        }
        Session_ME.gI().connected = false;
        GameMidlet.pingCount = 0;
        CCanvas.loadScreen();
        CCanvas.serverListScreen.show();
    }

    public void onLoginFail(String string) {
        CCanvas.startOKDlg(string);
    }

    public void onLoginSuccess() {
        CRes.out("========> Login thangh cong nha ae!!!!!!!");
        if (LoginScr.remember == 1) {
            if (GameMidlet.server != 2) {
                TerrainMidlet.myInfo.name = LoginScr.user;
            }
            CRes.saveRMS_String("caroun", LoginScr.user);
            CRes.saveRMS_String("caropass", LoginScr.pass);
        } else {
            if (GameMidlet.server != 2) {
                TerrainMidlet.myInfo.name = LoginScr.user;
            }
            CRes.saveRMS_String("caroun", "");
            CRes.saveRMS_String("caropass", "");
        }
        if (CCanvas.menuScr == null) {
            CCanvas.menuScr = new MenuScr();
        }
        CCanvas.menuScr.show();
        if (CCanvas.msgScr.list.size() != 0) {
            CCanvas.msgScr.show(CCanvas.menuScr);
        }
    }

    public void onRoomList(Vector vector) {
    }

    public void onBoardList(byte by, Vector vector) {
    }

    public void onJoinGameSuccess(int n, int n2, Vector vector, byte by) {
        CRes.out(this.getClass().getName() + " onJoinGameSuccess ownerID/money/map  " + n + "/" + n2 + "/" + by);
    }

    public void onJoinGameFail(String string) {
        CCanvas.msgdlg.setInfo(string, null, new Command("OK", new IAction(){

            public void perform() {
                CCanvas.endDlg();
                CCanvas.startWaitDlg(Language.getRoomlist());
                GameService.gI().requestRoomList();
            }
        }), null);
        CCanvas.msgdlg.show();
    }

    public void onSomeOneJoinBoard(int n, PlayerInfo playerInfo) {
        try {
            mSound.playSound(3, mSound.volumeSound, 1);
            TerrainMidlet.vibrate(ConfigScr.vibrate);
        }
        catch (Exception exception) {
        }
    }

    public void onSomeOneLeaveBoard(int n, int n2) {
    }

    public void onSomeOneReady(int n, boolean bl) {
        if (n == TerrainMidlet.myInfo.IDDB) {
            CCanvas.endDlg();
        }
        CCanvas.prepareScr.setReady(n, bl);
    }

    public void onOwnerSetMoney(int n) {
    }

    public void onChatFromBoard(String string, int n) {
        if (CCanvas.prepareScr.isShowing()) {
            if (PrepareScr.currLevel != 7) {
                CCanvas.prepareScr.showChat(n, string, 90);
            }
        } else if (CCanvas.gameScr != null && CCanvas.gameScr.isShowing()) {
            CCanvas.gameScr.showChat(n, string, 90);
        }
    }

    public void onKicked(int n, String string) {
        CCanvas.prepareScr.playerLeave(n);
        if (PrepareScr.currLevel != 7) {
            if (n == TerrainMidlet.myInfo.IDDB) {
                if (BoardListScr.boardList == null) {
                    CCanvas.menuScr.show();
                } else {
                    CCanvas.boardListScr.show();
                }
                CCanvas.msgdlg.setInfo(string, null, new Command("OK", new IAction(){

                    public void perform() {
                        CCanvas.endDlg();
                    }
                }), null);
                CCanvas.msgdlg.show();
            } else if (PM.getPlayerByIDDB(n) != null) {
                PM.getPlayerByIDDB((int)n).IDDB = -1;
            }
        } else if (n == TerrainMidlet.myInfo.IDDB) {
            CCanvas.boardListScr.show();
        } else if (PM.getPlayerByIDDB(n) != null) {
            PM.getPlayerByIDDB((int)n).IDDB = -1;
        }
    }

    public void onStartGame(byte by, byte by2, Vector vector, int n, byte by3) {
    }

    public void onMove(byte by, byte by2, int n, byte[] byArray, int n2) {
    }

    public void onForceLose(byte by, byte by2, int n) {
    }

    public void onMoveAndWin(byte by, byte by2, int n, byte by3, byte by4) {
    }

    public void onOpponentWantDraw(byte by, byte by2) {
    }

    public void onGameDraw(byte by, byte by2) {
    }

    public void onDenyDraw(byte by, byte by2) {
    }

    public void onWantLose(byte by, byte by2, int n) {
    }

    public void onRichestList(int n, Vector vector) {
    }

    public void onStrongestList(int n, Vector vector) {
        if (CCanvas.listScr == null) {
            CCanvas.listScr = new ListScr();
        }
        CCanvas.endDlg();
        CCanvas.listScr.page = n;
        CCanvas.listScr.setList(1, vector);
        CCanvas.listScr.isArmy2 = true;
        CCanvas.listScr.show(CCanvas.menuScr);
    }

    public void onXepHanglist(byte by, byte by2, Vector vector, String string) {
        if (CCanvas.listScr == null) {
            CCanvas.listScr = new ListScr();
        }
        CCanvas.endDlg();
        CCanvas.listScr.typeList = string;
        CCanvas.listScr.page = by2;
        CCanvas.listScr.setList(by, vector);
        CCanvas.listScr.isArmy2 = true;
        CCanvas.listScr.show(CCanvas.menuScr);
    }

    public void onRegisterInfo(String string, boolean bl, String string2, String string3) {
    }

    public void onRegisterInfo2(final String string, boolean bl, final String string2, String string3) {
        if (!bl) {
            CCanvas.startOKDlg(Language.ExistsNick());
        } else {
            CCanvas.startYesNoDlg(string3, new IAction(){

                public void perform() {
                    TerrainMidlet.sendSMS(string, "sms://" + string2, new IAction(){

                        public void perform() {
                        }
                    }, new IAction(){

                        public void perform() {
                        }
                    });
                }
            }, new IAction(){

                public void perform() {
                    CCanvas.endDlg();
                }
            });
        }
    }

    public void onChargeMoneySms(final String string, final String string2, String string3) {
        CCanvas.startYesNoDlg(string3, new IAction(){

            public void perform() {
                TerrainMidlet.sendSMS(string, "sms://" + string2, new IAction(){

                    public void perform() {
                    }
                }, new IAction(){

                    public void perform() {
                    }
                });
            }
        }, new IAction(){

            public void perform() {
                CCanvas.endDlg();
            }
        });
    }

    public void onFriendList(Vector vector) {
        if (CCanvas.listScr == null) {
            CCanvas.listScr = new ListScr();
        }
        CCanvas.endDlg();
        CCanvas.listScr.setList(2, vector);
        CCanvas.listScr.isArmy2 = true;
        if (CCanvas.curScr == CCanvas.prepareScr) {
            CCanvas.listScr.show(CCanvas.prepareScr);
        } else if (CCanvas.curScr == CCanvas.menuScr) {
            CCanvas.listScr.show(CCanvas.menuScr);
        }
    }

    public void onInviteList(Vector vector) {
        if (CCanvas.listScr == null) {
            CCanvas.listScr = new ListScr();
        }
        CCanvas.endDlg();
        CCanvas.listScr.isArmy2 = true;
        CCanvas.listScr.setInviteList(3, vector);
        if (CCanvas.curScr == CCanvas.prepareScr) {
            CCanvas.listScr.show(CCanvas.prepareScr);
        } else if (CCanvas.curScr == CCanvas.menuScr) {
            CCanvas.listScr.show(CCanvas.menuScr);
        }
    }

    public void onSearchResult(Vector vector) {
        if (vector.size() > 0) {
            for (int i = 0; i < vector.size(); ++i) {
                PlayerInfo playerInfo = (PlayerInfo)vector.elementAt(i);
                if (!playerInfo.name.equals(CCanvas.inputDlg.tfInput.getText())) continue;
                CCanvas.startWaitDlg(Language.them() + " " + playerInfo.name + " " + Language.vao());
                GameService.gI().addFriend(playerInfo.IDDB);
            }
        } else {
            CCanvas.startOKDlg(Language.notFindID());
        }
    }

    public void onAddFriendResult(byte by) {
        if (CCanvas.listScr == null) {
            CCanvas.listScr = new ListScr();
        }
        if (by == 0) {
            CCanvas.startOKDlg(Language.addFriendSuccess());
            if (CCanvas.curScr == CCanvas.listScr && CCanvas.listScr.type == 2) {
                GameService.gI().requestFriendList();
            }
        } else if (by == 2) {
            CCanvas.startOKDlg(Language.cannotaddFriend());
        } else {
            CCanvas.startOKDlg(Language.isExist());
        }
    }

    public void onDelFriendResult(byte by) {
        if (by == 0) {
            CCanvas.startOKDlg(Language.deleteFriendSc());
            GameService.gI().requestFriendList();
        } else {
            CCanvas.startOKDlg(Language.cannotdelete());
        }
    }

    public void onMatchResult(Vector vector) {
    }

    public void onChatFrom(MsgInfo msgInfo) {
        if (CCanvas.curScr != CCanvas.msgScr) {
            ++CCanvas.msgPopup.nMessage;
            CCanvas.msgPopup.show();
        }
        CCanvas.msgScr.addMsg(msgInfo);
    }

    public void onMyUserData(UserData userData) {
    }

    public void onAvatar(AvatarInfo avatarInfo) {
    }

    public void onPing() {
    }

    public void onAvatarList(Vector vector) {
    }

    public void onBuyAvtarSuccess(short s) {
    }

    public void onMoneyInfo(Vector vector) {
        CCanvas.endDlg();
        if (CCanvas.isIos()) {
            CCanvas.moneyScrIOS.setAvatarList(vector);
        } else {
            CCanvas.moneyScr.setAvatarList(vector);
        }
    }

    public void onServerInfo(String string) {
        CCanvas.infoPopup.setInfo(string);
        CCanvas.infoPopup.show();
    }

    public void onServerMessage(String string) {
        CCanvas.startOKDlg(string);
    }

    public void onVersion(String string, final String string2) {
        IAction iAction = new IAction(){

            public void perform() {
                mSystem.connectHTTP(string2);
            }
        };
        CCanvas.msgdlg.setInfo(string, new Command("Download", iAction), new Command("", iAction), new Command(Language.close(), new IAction(){

            public void perform() {
                CCanvas.endDlg();
            }
        }));
        CCanvas.msgdlg.show();
    }

    public void onURL(String string, String string2, final byte by) {
        if (TerrainMidlet.myInfo != null && TerrainMidlet.myInfo.name != null) {
            Font.replace(string2, "@username", TerrainMidlet.myInfo.name);
        }
        IAction iAction = new IAction(){

            public void perform() {
                IAction iAction = new IAction(){

                    public void perform() {
                    }
                };
                if (by == 0) {
                    if (CCanvas.isBB) {
                        CCanvas.msgdlg.setInfo(Language.Question(), null, new Command(Language.exit(), iAction), new Command(Language.no(), new IAction(){

                            public void perform() {
                                CCanvas.endDlg();
                            }
                        }));
                    } else {
                        CCanvas.msgdlg.setInfo(Language.Question(), new Command(Language.exit(), iAction), new Command("", iAction), new Command(Language.no(), new IAction(){

                            public void perform() {
                                CCanvas.endDlg();
                            }
                        }));
                    }
                } else if (by != 1 && by == 2) {
                    CCanvas.endDlg();
                }
            }
        };
        CCanvas.msgdlg.setInfo(string, new Command("OK", iAction), new Command("", iAction), new Command(Language.no(), new IAction(){

            public void perform() {
                CCanvas.endDlg();
            }
        }));
        CCanvas.msgdlg.show();
    }

    public void onAdminCommandResponse(String string) {
    }

    public void onSomeOneFinish(byte by, byte by2, int n, byte by3, int n2, int n3) {
    }

    public void onStopGame(byte by, byte by2, int n, byte[] byArray) {
    }

    public void onMoveError(byte by, byte by2, String string) {
    }

    public void onSetMoneyError(String string) {
        isServerThongBao = true;
        CCanvas.startOKDlg(string, new IAction(){

            public void perform() {
                isServerThongBao = false;
            }
        });
    }

    public void onFireArmy(byte by, byte by2, short s, short s2, short s3, byte by3) {
    }

    public void onMoveArmy(byte by, short s, short s2) {
    }

    public void onUpdateXY(byte by, short s, short s2) {
    }

    public void onStartArmy(byte by, byte by2, short[] sArray, short[] sArray2) {
    }

    public void onUpdateHP(byte by, short s) {
    }

    public void onNextTurn(byte by) {
    }

    public void onWind(byte by) {
    }

    public void onUseItem(int n, byte by) {
    }

    public void onChooseGun(int n, byte by) {
    }

    public void onMapChanged(byte by) {
        CCanvas.endDlg();
        CCanvas.prepareScr.onMapChanged(by);
    }

    public void onChangeTeam(int n, byte by) {
        CCanvas.endDlg();
        CCanvas.prepareScr.onChangeTeam(n, by);
    }

    public void onBonusMoney(int n, int n2, int n3) {
    }

    public void onClanMemberList(byte by, Vector vector) {
        if (CCanvas.listScr == null) {
            CCanvas.listScr = new ListScr();
        }
        CCanvas.endDlg();
        CCanvas.listScr.page = by;
        CCanvas.listScr.setList(5, vector);
        CCanvas.listScr.isArmy2 = true;
        CCanvas.listScr.show(CCanvas.clanScreen);
    }

    public void onGetImage(short s, Image image) {
        if (CCanvas.curScr == CCanvas.topClanScreen) {
            CCanvas.topClanScreen.findClan((int)s).icon = new mImage(image);
        }
        if (CCanvas.curScr == CCanvas.clanScreen) {
            CCanvas.clanScreen.clan.icon = new mImage(image);
        }
        if (CCanvas.curScr == CCanvas.listScr) {
            CCanvas.listScr.getPlayerIcon(s, image);
        }
        if (CCanvas.curScr == CCanvas.prepareScr) {
            CCanvas.prepareScr.getPlayerIcon(s, new mImage(image));
        }
    }

    static {
        instance = new GameLogicHandler();
    }
}

