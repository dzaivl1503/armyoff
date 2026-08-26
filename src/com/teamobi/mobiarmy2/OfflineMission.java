/*
 * Decompiled with CFR 0.152.
 */
package com.teamobi.mobiarmy2;

import com.teamobi.mobiarmy2.OfflineCombat;
import com.teamobi.mobiarmy2.OfflineSave;
import com.teamobi.mobiarmy2.OfflineSpecialShop;
import coreLG.CCanvas;
import coreLG.TerrainMidlet;
import java.util.Calendar;
import java.util.Vector;
import model.CRes;
import model.Mission;
import model.PlayerInfo;
import player.CPlayer;
import player.PM;

public final class OfflineMission {
    public static final int MISSION_TYPE_COUNT = 19;
    static final int LEGACY_MISSION_TYPE_COUNT = 18;
    static final int LEGACY_CLAIMED_COUNT = 54;
    public static final int MISSION_ID_CLOUD_LINK = 18;
    private static final int[] ID = new int[]{0, 0, 0, 1, 1, 1, 2, 2, 2, 3, 3, 3, 4, 4, 4, 5, 5, 5, 6, 6, 6, 7, 7, 7, 8, 8, 8, 9, 9, 9, 10, 10, 10, 11, 11, 11, 12, 12, 12, 13, 13, 13, 14, 14, 14, 15, 15, 15, 16, 16, 16, 17, 17, 17, 18};
    private static final int[] LEVEL = new int[]{1, 2, 3, 1, 2, 3, 1, 2, 3, 1, 2, 3, 1, 2, 3, 1, 2, 3, 1, 2, 3, 1, 2, 3, 1, 2, 3, 1, 2, 3, 1, 2, 3, 1, 2, 3, 1, 2, 3, 1, 2, 3, 1, 2, 3, 1, 2, 3, 1, 2, 3, 1, 2, 3, 1};
    private static final String[] NAME = new String[]{"Lv 1: Th\u1eafng 100 v\u00e1n solo", "Lv 2: Th\u1eafng 1000 v\u00e1n solo", "Lv 3: Th\u1eafng 10000 v\u00e1n solo", "Lv 1: B\u1eafn 100.000 \u0111i\u1ec3m HP", "Lv 2: B\u1eafn 1.000.000 \u0111i\u1ec3m HP", "Lv 3: B\u1eafn 1.000.000.000 \u0111i\u1ec3m HP", "Lv 1: Th\u1eafng 50 v\u00e1n \u0111\u1ea5u UFO", "Lv 2: Th\u1eafng 100 v\u00e1n \u0111\u1ea5u UFO", "Lv 3: Th\u1eafng 200 v\u00e1n \u0111\u1ea5u UFO", "Lv 1: Th\u1eafng 50 v\u00e1n \u0111\u1ea5u Kh\u00ed C\u1ea7u", "Lv 2: Th\u1eafng 100 v\u00e1n \u0111\u1ea5u Kh\u00ed C\u1ea7u", "Lv 3: Th\u1eafng 200 v\u00e1n \u0111\u1ea5u Kh\u00ed C\u1ea7u", "Lv 1: Th\u1eafng 50 v\u00e1n \u0111\u1ea5u Ma", "Lv 2: Th\u1eafng 100 v\u00e1n \u0111\u1ea5u Ma", "Lv 3: Th\u1eafng 200 v\u00e1n \u0111\u1ea5u Ma", "Lv 1: N\u00e9m 200 qu\u1ea3 B52", "Lv 2: N\u00e9m 2000 qu\u1ea3 B52", "Lv 3: N\u00e9m 20000 qu\u1ea3 B52", "Lv 1: Ti\u00eau di\u1ec7t 200 Tarzan", "Lv 2: Ti\u00eau di\u1ec7t 2000 Tarzan", "Lv 3: Ti\u00eau di\u1ec7t 20000 Tarzan", "Lv 1: Ti\u00eau di\u1ec7t 200 Chicky", "Lv 2: Ti\u00eau di\u1ec7t 2000 Chicky", "Lv 3: Ti\u00eau di\u1ec7t 20000 Chicky", "Lv 1: Ti\u00eau di\u1ec7t 200 Magenta", "Lv 2: Ti\u00eau di\u1ec7t 2000 Magenta", "Lv 3: Ti\u00eau di\u1ec7t 20000 Magenta", "Lv 1: T\u1ea1o 1 vi\u00ean ng\u1ecdc c\u1ea5p 8", "Lv 2: T\u1ea1o 5 vi\u00ean ng\u1ecdc c\u1ea5p 8", "Lv 3: T\u1ea1o 10 vi\u00ean ng\u1ecdc c\u1ea5p 8", "Lv 1: T\u1ea1o 1 vi\u00ean ng\u1ecdc c\u1ea5p 9", "Lv 2: T\u1ea1o 5 vi\u00ean ng\u1ecdc c\u1ea5p 9", "Lv 3: T\u1ea1o 10 vi\u00ean ng\u1ecdc c\u1ea5p 9", "Lv 1: T\u1ea1o 1 vi\u00ean ng\u1ecdc c\u1ea5p 10", "Lv 2: T\u1ea1o 5 vi\u00ean ng\u1ecdc c\u1ea5p 10", "Lv 3: T\u1ea1o 10 vi\u00ean ng\u1ecdc c\u1ea5p 10", "Lv 1: B\u1eafn 100 ph\u00e1t si\u00eau cao, si\u00eau xa", "Lv 2: B\u1eafn 1.000 ph\u00e1t si\u00eau cao, si\u00eau xa", "Lv 3: B\u1eafn 10.000 ph\u00e1t si\u00eau cao, si\u00eau xa", "Lv 1: D\u00f9ng Gunner th\u1eafng 200 v\u00e1n", "Lv 2: D\u00f9ng Gunner th\u1eafng 2.000 v\u00e1n", "Lv 3: D\u00f9ng Gunner th\u1eafng 20.000 v\u00e1n", "Lv 1: D\u00f9ng Miss 6 th\u1eafng 200 v\u00e1n", "Lv 2: D\u00f9ng Miss 6 th\u1eafng 2.000 v\u00e1n", "Lv 3: D\u00f9ng Miss 6 th\u1eafng 20.000 v\u00e1n", "Lv 1: D\u00f9ng Proton th\u1eafng 200 v\u00e1n", "Lv 2: D\u00f9ng Proton th\u1eafng 2.000 v\u00e1n", "Lv 3: D\u00f9ng Proton th\u1eafng 20.000 v\u00e1n", "Lv 1: \u0110\u0103ng nh\u1eadp 30 ng\u00e0y", "Lv 2: \u0110\u0103ng nh\u1eadp 90 ng\u00e0y", "Lv 3: \u0110\u0103ng nh\u1eadp 270 ng\u00e0y", "Lv 1: Th\u1eafng 500 v\u00e1n \u0111\u1ea5u tr\u00ean 5 ng\u01b0\u1eddi ch\u01a1i", "Lv 2: Th\u1eafng 1000 v\u00e1n \u0111\u1ea5u tr\u00ean 5 ng\u01b0\u1eddi ch\u01a1i", "Lv 3: Th\u1eafng 2000 v\u00e1n \u0111\u1ea5u tr\u00ean 5 ng\u01b0\u1eddi ch\u01a1i", "Li\u00ean k\u1ebft t\u00e0i kho\u1ea3n"};
    private static final String[] REWARD = new String[]{"+1k xp, +100 danh d\u1ef1", "+10k xp, +1k danh d\u1ef1", "+100k xp, +10k danh d\u1ef1", "+5k xp, +500 danh d\u1ef1", "+50k xp, +2k danh d\u1ef1", "+500k xp, +8k danh d\u1ef1", "+2k xp, +500 danh d\u1ef1", "+20k xp, +2k danh d\u1ef1", "+200k xp, +8k danh d\u1ef1", "+2k xp, +500 danh d\u1ef1", "+20k xp, +2k danh d\u1ef1", "+200k xp, +8k danh d\u1ef1", "+2k xp, +500 danh d\u1ef1", "+20k xp, +2k danh d\u1ef1", "+200k xp, +8k danh d\u1ef1", "+2k xp, +200 danh d\u1ef1", "+20k xp, +1k danh d\u1ef1", "+200k xp, +5k danh d\u1ef1", "+2k xp, +200 danh d\u1ef1", "+20k xp, +1k danh d\u1ef1", "+200k xp, +5k danh d\u1ef1", "+2k xp, +200 danh d\u1ef1", "+20k xp, +1k danh d\u1ef1", "+200k xp, +5k danh d\u1ef1", "+2k xp, +200 danh d\u1ef1", "+20k xp, +1k danh d\u1ef1", "+200k xp, +5k danh d\u1ef1", "+1k xp, +100 danh d\u1ef1", "+10k xp, +500 danh d\u1ef1", "+100k xp, +1k danh d\u1ef1", "+5k xp, +500 danh d\u1ef1", "+50k xp, +5k danh d\u1ef1", "+500k xp, +50k danh d\u1ef1", "+50k xp, +5k danh d\u1ef1", "+500k xp, +50k danh d\u1ef1", "+5Tr xp, +500k danh d\u1ef1", "+5k xp, +100 danh d\u1ef1", "+25k xp, +1k danh d\u1ef1", "+125k xp, +10k danh d\u1ef1", "+2k xp, +200 danh d\u1ef1", "+20k xp, +1k danh d\u1ef1", "+200k xp, +5k danh d\u1ef1", "+2k xp, +200 danh d\u1ef1", "+20k xp, +1k danh d\u1ef1", "+200k xp, +5k danh d\u1ef1", "+2k xp, +200 danh d\u1ef1", "+20k xp, +1k danh d\u1ef1", "+200k xp, +5k danh d\u1ef1", "+10k xp, +500 danh d\u1ef1", "+30k xp, +1,5k danh d\u1ef1", "+90k xp, +4,5k danh d\u1ef1", "+5k xp, +500 danh d\u1ef1", "+10k xp, +1k danh d\u1ef1", "+20k xp, +2k danh d\u1ef1", "+1000 xu, +100 l\u01b0\u1ee3ng, +1000 exp, 1 Ng\u1ecdc c\u1ea5p 10 ng\u1eabu nhi\u00ean"};
    private static final int[] REQUIRE = new int[]{100, 1000, 10000, 100000, 1000000, 1000000000, 50, 100, 200, 50, 100, 200, 50, 100, 200, 200, 2000, 20000, 200, 2000, 20000, 200, 2000, 20000, 200, 2000, 20000, 1, 5, 10, 1, 5, 10, 1, 5, 10, 100, 1000, 10000, 200, 2000, 20000, 200, 2000, 20000, 200, 2000, 20000, 30, 90, 270, 500, 1000, 2000, 1};
    private static final int[] EXP = new int[]{1000, 10000, 100000, 5000, 50000, 500000, 2000, 20000, 200000, 2000, 20000, 200000, 2000, 20000, 200000, 2000, 20000, 200000, 2000, 20000, 200000, 2000, 20000, 200000, 2000, 20000, 200000, 1000, 10000, 100000, 5000, 50000, 500000, 50000, 500000, 5000000, 5000, 25000, 125000, 2000, 20000, 200000, 2000, 20000, 200000, 2000, 20000, 200000, 10000, 30000, 90000, 5000, 10000, 20000, 1000};
    private static final int[] CUP = new int[]{100, 1000, 10000, 500, 2000, 8000, 500, 2000, 8000, 500, 2000, 8000, 500, 2000, 8000, 200, 1000, 5000, 200, 1000, 5000, 200, 1000, 5000, 200, 1000, 5000, 100, 500, 1000, 500, 5000, 50000, 5000, 50000, 500000, 100, 1000, 10000, 200, 1000, 5000, 200, 1000, 5000, 200, 1000, 5000, 500, 1500, 4500, 500, 1000, 2000, 0};
    public static int[] progress = new int[19];
    public static boolean[] claimed = new boolean[55];
    public static int loginStreak;
    public static int lastLoginYmd;

    private OfflineMission() {
    }

    public static void reset() {
        progress = new int[19];
        claimed = new boolean[55];
        loginStreak = 0;
        lastLoginYmd = 0;
    }

    public static void onCloudAccountLinked() {
        OfflineMission.addProgress(18, 1);
    }

    public static void addProgress(int n, int n2) {
        if (n < 0 || n >= 19 || n2 <= 0) {
            return;
        }
        long l = (long)progress[n] + (long)n2;
        OfflineMission.progress[n] = l > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int)l;
    }

    public static void onLogin() {
        Calendar calendar = Calendar.getInstance();
        int n = calendar.get(1) * 10000 + (calendar.get(2) + 1) * 100 + calendar.get(5);
        if (n == lastLoginYmd) {
            return;
        }
        lastLoginYmd = n;
        ++loginStreak;
        OfflineMission.addProgress(16, 1);
    }

    public static void onBossWin(CPlayer cPlayer) {
        CPlayer cPlayer2;
        OfflineMission.addProgress(0, 1);
        if (cPlayer != null) {
            if (cPlayer.gun == 16) {
                OfflineMission.addProgress(2, 1);
            } else if (cPlayer.gun == 17) {
                OfflineMission.addProgress(3, 1);
            } else if (cPlayer.gun == 25 || cPlayer.gun == 26) {
                OfflineMission.addProgress(4, 1);
            }
        }
        if ((cPlayer2 = PM.getMyPlayer()) != null) {
            if (cPlayer2.gun == 0) {
                OfflineMission.addProgress(13, 1);
            } else if (cPlayer2.gun == 1) {
                OfflineMission.addProgress(14, 1);
            } else if (cPlayer2.gun == 2) {
                OfflineMission.addProgress(15, 1);
            }
        }
    }

    public static void onUseItem(int n) {
        if (n == 8) {
            OfflineMission.addProgress(5, 1);
        }
    }

    public static void onKillPvpBot(byte by) {
        if (by == 7) {
            OfflineMission.addProgress(6, 1);
        } else if (by == 6) {
            OfflineMission.addProgress(7, 1);
        } else if (by == 9) {
            OfflineMission.addProgress(8, 1);
        }
    }

    public static void onSuperShot() {
        OfflineMission.addProgress(12, 1);
    }

    public static void onGemCrafted(int n, int n2) {
        if (n2 <= 0) {
            return;
        }
        int n3 = n % 10;
        if (n3 == 7) {
            OfflineMission.addProgress(9, n2);
        } else if (n3 == 8) {
            OfflineMission.addProgress(10, n2);
        } else if (n3 == 9) {
            OfflineMission.addProgress(11, n2);
        }
    }

    public static void onPvpBotWin(int n) {
        OfflineMission.addProgress(0, 1);
        PlayerInfo playerInfo = TerrainMidlet.myInfo;
        if (playerInfo != null) {
            if (playerInfo.gun == 0) {
                OfflineMission.addProgress(13, 1);
            } else if (playerInfo.gun == 1) {
                OfflineMission.addProgress(14, 1);
            } else if (playerInfo.gun == 2) {
                OfflineMission.addProgress(15, 1);
            }
        }
        if (n > 5) {
            OfflineMission.addProgress(17, 1);
        }
    }

    private static Mission buildEntry(int n) {
        Mission mission = new Mission();
        mission.id = ID[n];
        mission.level = LEVEL[n];
        mission.name = NAME[n];
        mission.reward = REWARD[n];
        mission.require = REQUIRE[n];
        mission.have = progress[ID[n]] > REQUIRE[n] ? REQUIRE[n] : progress[ID[n]];
        mission.isComplete = progress[ID[n]] >= REQUIRE[n];
        mission.isGetReward = claimed[n];
        return mission;
    }

    public static Vector buildMissionList() {
        Vector<Mission> vector = new Vector<Mission>();
        for (int i = 0; i < NAME.length; ++i) {
            vector.addElement(OfflineMission.buildEntry(i));
        }
        return vector;
    }

    public static void claimReward(int n, int n2) {
        int n3 = OfflineMission.indexOf(n, n2);
        if (n3 < 0 || claimed[n3] || progress[n] < REQUIRE[n3]) {
            return;
        }
        OfflineMission.claimed[n3] = true;
        PlayerInfo playerInfo = TerrainMidlet.myInfo;
        if (playerInfo == null) {
            return;
        }
        playerInfo.cup += CUP[n3];
        OfflineCombat.grantExpDirect(EXP[n3]);
        if (n == 18) {
            playerInfo.xu += 1000;
            playerInfo.luong += 100;
            int n4 = CRes.random(0, 5);
            OfflineSpecialShop.addMaterial((byte)(n4 * 10 + 9), 1);
        }
        OfflineSave.save();
        if (CCanvas.missionScreen != null) {
            CCanvas.missionScreen.setMission(OfflineMission.buildMissionList());
        }
    }

    private static int indexOf(int n, int n2) {
        for (int i = 0; i < ID.length; ++i) {
            if (ID[i] != n || LEVEL[i] != n2) continue;
            return i;
        }
        return -1;
    }
}

