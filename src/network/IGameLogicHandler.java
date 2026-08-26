/*
 * Decompiled with CFR 0.152.
 */
package network;

import CLib.Image;
import java.util.Vector;
import model.AvatarInfo;
import model.MsgInfo;
import model.PlayerInfo;
import model.UserData;

public interface IGameLogicHandler {
    public void onLoginSuccess();

    public void onLoginFail(String var1);

    public void onConnectOK();

    public void onConnectFail();

    public void onDisconnect();

    public void onRoomList(Vector var1);

    public void onBoardList(byte var1, Vector var2);

    public void onJoinGameSuccess(int var1, int var2, Vector var3, byte var4);

    public void onJoinGameFail(String var1);

    public void onSomeOneJoinBoard(int var1, PlayerInfo var2);

    public void onSomeOneLeaveBoard(int var1, int var2);

    public void onSomeOneReady(int var1, boolean var2);

    public void onOwnerSetMoney(int var1);

    public void onChatFromBoard(String var1, int var2);

    public void onKicked(int var1, String var2);

    public void onStartGame(byte var1, byte var2, Vector var3, int var4, byte var5);

    public void onMove(byte var1, byte var2, int var3, byte[] var4, int var5);

    public void onForceLose(byte var1, byte var2, int var3);

    public void onMoveAndWin(byte var1, byte var2, int var3, byte var4, byte var5);

    public void onOpponentWantDraw(byte var1, byte var2);

    public void onGameDraw(byte var1, byte var2);

    public void onDenyDraw(byte var1, byte var2);

    public void onWantLose(byte var1, byte var2, int var3);

    public void onRichestList(int var1, Vector var2);

    public void onStrongestList(int var1, Vector var2);

    public void onRegisterInfo(String var1, boolean var2, String var3, String var4);

    public void onRegisterInfo2(String var1, boolean var2, String var3, String var4);

    public void onChargeMoneySms(String var1, String var2, String var3);

    public void onFriendList(Vector var1);

    public void onClanMemberList(byte var1, Vector var2);

    public void onInviteList(Vector var1);

    public void onSearchResult(Vector var1);

    public void onAddFriendResult(byte var1);

    public void onDelFriendResult(byte var1);

    public void onMatchResult(Vector var1);

    public void onChatFrom(MsgInfo var1);

    public void onMyUserData(UserData var1);

    public void onAvatar(AvatarInfo var1);

    public void onPing();

    public void onAvatarList(Vector var1);

    public void onBuyAvtarSuccess(short var1);

    public void onMoneyInfo(Vector var1);

    public void onServerMessage(String var1);

    public void onServerInfo(String var1);

    public void onVersion(String var1, String var2);

    public void onAdminCommandResponse(String var1);

    public void onSomeOneFinish(byte var1, byte var2, int var3, byte var4, int var5, int var6);

    public void onStopGame(byte var1, byte var2, int var3, byte[] var4);

    public void onMoveError(byte var1, byte var2, String var3);

    public void onSetMoneyError(String var1);

    public void onStartArmy(byte var1, byte var2, short[] var3, short[] var4);

    public void onMoveArmy(byte var1, short var2, short var3);

    public void onUpdateXY(byte var1, short var2, short var3);

    public void onFireArmy(byte var1, byte var2, short var3, short var4, short var5, byte var6);

    public void onUpdateHP(byte var1, short var2);

    public void onNextTurn(byte var1);

    public void onWind(byte var1);

    public void onUseItem(int var1, byte var2);

    public void onChooseGun(int var1, byte var2);

    public void onMapChanged(byte var1);

    public void onChangeTeam(int var1, byte var2);

    public void onBonusMoney(int var1, int var2, int var3);

    public void onURL(String var1, String var2, byte var3);

    public void onGetImage(short var1, Image var2);

    public void onXepHanglist(byte var1, byte var2, Vector var3, String var4);
}

