/*
 * Decompiled with CFR 0.152.
 */
package player;

import CLib.mGraphics;
import com.teamobi.mobiarmy2.OfflineBossAI;
import com.teamobi.mobiarmy2.OfflinePvpBotAI;
import coreLG.CCanvas;
import coreLG.TerrainMidlet;
import effect.Camera;
import item.BM;
import map.MM;
import model.CRes;
import model.PlayerInfo;
import network.GameService;
import player.Boss;
import player.CPlayer;
import screen.CScreen;
import screen.GameScr;
import screen.PrepareScr;

public class PM {
    public static int MAX_PLAYER;
    public static int NUMB_PLAYER;
    public static CPlayer[] p;
    public static int[] npXsend;
    public static int[] npYsend;
    public static int[] npNumSend;
    public static byte curP;
    static int curUpdateP;
    public static short[] xPResult;
    public static short[] yPResult;
    public int playerCount;
    public int allCount;
    int planeX;
    int planeY;
    public static int tKC;
    public static int deltaYKC;
    private static final int STUCK_FALLING_TICKS_LIMIT = 60;
    private static int stuckFallingTicks;

    public void init() {
        p = new CPlayer[MAX_PLAYER];
        npXsend = new int[MAX_PLAYER];
        npYsend = new int[MAX_PLAYER];
        npNumSend = new int[MAX_PLAYER];
        xPResult = new short[MAX_PLAYER];
        yPResult = new short[MAX_PLAYER];
    }

    public void insertPlayer(short s, short s2, byte by, PlayerInfo playerInfo, int n) {
        boolean bl = TerrainMidlet.myInfo.IDDB != playerInfo.IDDB;
        PM.p[by] = new CPlayer(playerInfo.IDDB, by, s, s2, bl, 0, playerInfo.gun, playerInfo.myEquip, playerInfo.maxHP);
        PM.p[by].name = playerInfo.name;
        PM.p[by].nQuanHam = playerInfo.nQuanHam2;
        PM.p[by].hp = n;
        PM.p[by].hpRectW = n * 25 / PM.p[by].maxhp;
        PM.p[by].clanIcon = playerInfo.clanIcon;
        ++this.playerCount;
    }

    public void initPlayer(short[] sArray, short[] sArray2, short[] sArray3) {
        int n;
        this.playerCount = 0;
        this.allCount = 0;
        int n2 = 8;
        if (PrepareScr.currLevel == 7) {
            n2 = NUMB_PLAYER;
        }
        for (n = 0; n < n2; ++n) {
            PM.p[n] = null;
            if (sArray[n] != -1) {
                boolean bl;
                PlayerInfo playerInfo = (PlayerInfo)CCanvas.prepareScr.playerInfos.elementAt(n);
                boolean bl2 = bl = TerrainMidlet.myInfo.IDDB != playerInfo.IDDB;
                if (!playerInfo.isBoss) {
                    PM.p[n] = new CPlayer(playerInfo.IDDB, (byte)n, sArray[n], sArray2[n], bl, n % 2 == 0 ? 2 : 0, playerInfo.gun, playerInfo.myEquip, sArray3[n]);
                    PM.p[n].clanIcon = playerInfo.clanIcon;
                    PM.p[n].equip = playerInfo.myEquip;
                } else {
                    PM.p[n] = new Boss(playerInfo.IDDB, (byte)n, sArray[n], sArray2[n], bl, n % 2 == 0 ? 2 : 0, playerInfo.gun, sArray3[n]);
                }
                PM.p[n].name = playerInfo.name;
                PM.p[n].nQuanHam = playerInfo.nQuanHam2;
                PM.p[n].maxhp = sArray3[n];
                if (!bl) {
                    if (playerInfo.gun >= 0 && playerInfo.gun < playerInfo.itemLoadout.length) {
                        int[] nArray = playerInfo.itemLoadout[playerInfo.gun];
                        int[] nArray2 = new int[nArray.length];
                        for (int i = 0; i < nArray.length; ++i) {
                            nArray2[i] = nArray[i];
                        }
                        PM.p[n].item = nArray2;
                    } else {
                        PM.p[n].item = CCanvas.prepareScr.copyItemCurrent();
                    }
                    GameScr.myIndex = (byte)n;
                }
            }
            ++this.playerCount;
        }
        this.allCount = this.playerCount;
        n = 0;
        while (n < n2) {
            PM.npXsend[n] = -1;
            PM.npYsend[n] = -1;
            PM.npNumSend[n] = n++;
        }
    }

    public String getPlayerNameFromID(int n) {
        for (int i = 0; i < p.length; ++i) {
            if (PM.p[i].IDDB != n) continue;
            return PM.p[i].name;
        }
        return "";
    }

    public CPlayer getPlayerFromID(int n) {
        if (p == null) {
            return null;
        }
        for (int i = 0; i < p.length; ++i) {
            if (p[i] == null || PM.p[i].IDDB != n) continue;
            return p[i];
        }
        return null;
    }

    public void initBoss(short[] sArray, short[] sArray2) {
        for (int i = 0; i < sArray.length; ++i) {
            PM.p[i + this.allCount] = null;
            if (sArray[i] == -1) continue;
            PlayerInfo playerInfo = (PlayerInfo)CCanvas.prepareScr.bossInfos.elementAt(i);
            boolean bl = TerrainMidlet.myInfo.IDDB != playerInfo.IDDB;
            byte by = (byte)(i + this.allCount);
            PM.p[by] = new Boss(playerInfo.IDDB, by, sArray[i], sArray2[i], bl, 2, playerInfo.gun, playerInfo.maxHP);
            PM.p[by].name = playerInfo.name;
            PM.p[by].hp = playerInfo.maxHP;
            PM.p[by].maxhp = playerInfo.maxHP;
            PM.p[by].team = false;
            PM.p[by].index = by;
        }
        this.allCount += CCanvas.prepareScr.bossInfos.size();
        CCanvas.prepareScr.bossInfos.removeAllElements();
    }

    public static void getXYResult() {
        for (int i = 0; i < p.length; ++i) {
            CPlayer cPlayer = p[i];
            if (cPlayer != null) {
                PM.xPResult[i] = (short)PM.p[i].x;
                PM.yPResult[i] = (short)PM.p[i].y;
                continue;
            }
            PM.xPResult[i] = -1;
            PM.yPResult[i] = -1;
        }
    }

    public static int getIndexByIDDB(int n) {
        for (int i = 0; i < 8; ++i) {
            PlayerInfo playerInfo = (PlayerInfo)CCanvas.prepareScr.playerInfos.elementAt(i);
            if (playerInfo.IDDB != n) continue;
            return i;
        }
        return -1;
    }

    public static CPlayer getCurPlayer() {
        return p[curP] != null ? p[curP] : null;
    }

    public static CPlayer getMyPlayer() {
        if (p == null || GameScr.myIndex < 0 || GameScr.myIndex >= p.length) {
            return null;
        }
        return p[GameScr.myIndex];
    }

    public static boolean isAlly(CPlayer cPlayer, CPlayer cPlayer2) {
        if (cPlayer == null || cPlayer2 == null || cPlayer == cPlayer2) {
            return true;
        }
        if ((cPlayer.index & 1) == (cPlayer2.index & 1)) {
            return true;
        }
        return cPlayer.team == cPlayer2.team;
    }

    public static boolean isEnemyTarget(CPlayer cPlayer, CPlayer cPlayer2) {
        if (cPlayer == null || cPlayer2 == null || cPlayer2 == cPlayer) {
            return false;
        }
        if (cPlayer2.getState() == 5 || cPlayer2.hp <= 0) {
            return false;
        }
        return !PM.isAlly(cPlayer, cPlayer2);
    }

    public static CPlayer getPlayerByIndex(int n) {
        return p[n];
    }

    public static CPlayer getPlayerByIDDB(int n) {
        if (p == null) {
            return null;
        }
        for (int i = 0; i < p.length; ++i) {
            if (p[i] == null || PM.p[i].IDDB != n) continue;
            return p[i];
        }
        return null;
    }

    public static CPlayer findPlayerByIndex(int n) {
        if (p == null) {
            return null;
        }
        for (int i = 0; i < p.length; ++i) {
            if (p[i] == null || PM.p[i].index != n) continue;
            return p[i];
        }
        return null;
    }

    public void onPointerPressed(int n, int n2, int n3) {
        p[GameScr.myIndex].onPointerPressed(n, n2, n3);
    }

    public void onPointerDrag(int n, int n2, int n3) {
        p[GameScr.myIndex].onPointerDrag(n, n2, n3);
    }

    public void onPointerDragRighCorner(int n, int n2, int n3) {
    }

    public void onPointerHold(int n, int n2, int n3) {
        p[GameScr.myIndex].onPointerHold(n, n2, n3);
    }

    public void onPointerReleased(int n, int n2, int n3) {
        p[GameScr.myIndex].onPointerReleased(n, n, n3);
    }

    public void flyAnimation() {
        if (CCanvas.gameTick % 2 == 0) {
            if (++tKC == 10) {
                deltaYKC = 0;
                tKC = 0;
            }
            deltaYKC = tKC <= 5 ? ++deltaYKC : --deltaYKC;
        }
    }

    public void update() {
        int n = 0;
        boolean bl = false;
        for (int i = 0; i < p.length; ++i) {
            if (p[i] == null) continue;
            curUpdateP = i;
            p[i].update();
            if (PM.p[i].isAllowSendPosAfterShoot) {
                PM.npNumSend[n] = i;
                PM.npXsend[n] = PM.p[i].x;
                PM.npYsend[n] = PM.p[i].y;
                n = (byte)(n + 1);
            }
            if (!PM.p[i].falling || p[i].getState() == 5) continue;
            bl = true;
        }
        this.flyAnimation();
        if (BM.allSendENDSHOOT) {
            stuckFallingTicks = bl ? ++stuckFallingTicks : 0;
            if (!bl || stuckFallingTicks > 60) {
                stuckFallingTicks = 0;
                GameService.gI().shootResult();
                PM.getCurPlayer().shootFrame = false;
                BM.allSendENDSHOOT = false;
                GameService.gI().holeInfo(MM.vHoleInfo);
            }
        } else {
            stuckFallingTicks = 0;
        }
        OfflineBossAI.update();
        OfflinePvpBotAI.update();
    }

    public static boolean isLand(byte by) {
        CPlayer cPlayer = PM.getPlayerByIndex(by);
        return GameScr.mm.isLand(cPlayer.x, cPlayer.y);
    }

    public void paint(mGraphics mGraphics2) {
        for (int i = 0; i < p.length; ++i) {
            if (p[i] == null || PM.p[i].x + 75 <= Camera.x || PM.p[i].x - 75 >= Camera.x + CScreen.w) continue;
            if (GameScr.cantSee) {
                if (i != GameScr.myIndex) continue;
                p[i].paint(mGraphics2);
                continue;
            }
            p[i].paint(mGraphics2);
        }
    }

    public boolean isYourTurn() {
        return curP == GameScr.myIndex;
    }

    public boolean isMyPlayerUpdate() {
        return curUpdateP == GameScr.myIndex;
    }

    public void setNextPlayer(byte by) {
        if (CRes.isNullOrEmpty(GameScr.res)) {
            int n;
            for (n = 0; n < p.length; ++n) {
                if (p[n] == null) continue;
                PM.p[n].isPaintCountDown = false;
            }
            PM.p[by].isPaintCountDown = true;
            PM.p[by].active = true;
            if (p[by].getState() != 8) {
                p[by].setState((byte)0);
                p[by].onStartTurnAngle();
                p[by].checkAngleForSprite();
            }
            PM.p[by].movePoint = 0;
            PM.p[by].itemUsed = -1;
            if (PM.p[by].isUsedItem) {
                PM.p[by].isUsedItem = false;
            }
            if (PM.p[by].gun != 15) {
                PM.p[by].falling = true;
            }
            if (PM.p[by].isStopWind) {
                GameScr.changeWind(0, 0);
            }
            for (n = 0; n < p.length; ++n) {
                if (p[n] == null) continue;
                PM.p[n].shootFrame = false;
                if (p[n].getState() != 5) {
                    PM.p[n].isAllowSendPosAfterShoot = false;
                }
                BM.nBum = 0;
                if (n != GameScr.myIndex) continue;
                p[n].resetXYwhenNEXTTURN();
                if (PM.p[n].chophepGuiUpdateXY) continue;
                GameService.gI().move((short)PM.p[n].x, (short)PM.p[n].y);
                PM.p[n].chophepGuiUpdateXY = true;
            }
            GameScr.time.resetTime();
            curP = by;
            if (curP == GameScr.myIndex) {
                GameScr.cantSee = PM.p[PM.curP].cantSee;
                CPlayer.isShooting = false;
            }
            if (GameScr.cam != null) {
                GameScr.cam.setPlayerMode(by);
            }
        }
    }

    public void movePlayer(int n, short s, short s2) {
        PM.p[n].nextx = s;
        PM.p[n].nexty = s2;
        PM.p[n].isMove = true;
        PM.p[n].tMove = 0;
        if (n == GameScr.myIndex) {
            p[n].resetLastUpdateXY(s, s2);
        }
    }

    public void flyTo(int n, short s, short s2) {
        p[n].flyToPoint(s, s2);
    }

    public void updatePlayerXY(int n, short s, short s2) {
        PM.p[n].x = s;
        PM.p[n].y = s2;
        PM.p[n].nextx = s;
        PM.p[n].lastx = s;
        PM.p[n].nexty = s2;
        if (n == GameScr.myIndex) {
            p[n].resetLastUpdateXY(s, s2);
        }
    }

    public void setPlayerAfterSetWin(boolean bl) {
        for (int i = 0; i < p.length; ++i) {
            if (p[i] == null) continue;
            PM.p[i].active = false;
            if (PM.p[i].hp <= 0) continue;
            if (PM.p[i].team && bl) {
                p[i].setWin();
                continue;
            }
            if (PM.p[i].team || bl) continue;
            p[i].setWin();
        }
    }

    public void setPlayerAfterDraw() {
        for (int i = 0; i < p.length; ++i) {
            if (p[i] == null) continue;
            PM.p[i].active = false;
        }
    }

    public void onClearMap() {
    }
}

