/*
 * Mobi Army 2 Offline Mission System with All 12 Gun Missions and Remote Config
 */
package com.teamobi.mobiarmy2;

import coreLG.CCanvas;
import coreLG.TerrainMidlet;
import java.util.Calendar;
import java.util.Vector;
import model.CRes;
import model.Mission;
import model.PlayerInfo;
import player.CPlayer;
import player.PM;
import screen.PrepareScr;

public final class OfflineMission {
    public static final int MISSION_TYPE_COUNT = 28;
    static final int LEGACY_MISSION_TYPE_COUNT = 18;
    static final int LEGACY_CLAIMED_COUNT = 54;
    public static final int MISSION_ID_CLOUD_LINK = 27;

    // Gun missions start at ID 13 and go up to 24 (Gun 0 to Gun 11)
    public static final int GUN_MISSION_START_ID = 13;
    public static final int GUN_MISSION_COUNT = 12;

    public static int[] ID = new int[]{
        0, 0, 0,
        1, 1, 1,
        2, 2, 2,
        3, 3, 3,
        4, 4, 4,
        5, 5, 5,
        6, 6, 6,
        7, 7, 7,
        8, 8, 8,
        9, 9, 9,
        10, 10, 10,
        11, 11, 11,
        12, 12, 12,
        // Gun 0: Gunner
        13, 13, 13,
        // Gun 1: Miss 6
        14, 14, 14,
        // Gun 2: Electician
        15, 15, 15,
        // Gun 3: King Kong
        16, 16, 16,
        // Gun 4: Rocketer
        17, 17, 17,
        // Gun 5: Granos
        18, 18, 18,
        // Gun 6: Chicky
        19, 19, 19,
        // Gun 7: Tarzan
        20, 20, 20,
        // Gun 8: Apache
        21, 21, 21,
        // Gun 9: Magenta
        22, 22, 22,
        // Gun 10: Draby
        23, 23, 23,
        // Gun 11: Cow Girl
        24, 24, 24,
        // Login streak
        25, 25, 25,
        // 5+ players win
        26, 26, 26,
        // Cloud link
        27
    };

    public static int[] LEVEL = new int[]{
        1, 2, 3,
        1, 2, 3,
        1, 2, 3,
        1, 2, 3,
        1, 2, 3,
        1, 2, 3,
        1, 2, 3,
        1, 2, 3,
        1, 2, 3,
        1, 2, 3,
        1, 2, 3,
        1, 2, 3,
        1, 2, 3,
        // Gun 0..11
        1, 2, 3,
        1, 2, 3,
        1, 2, 3,
        1, 2, 3,
        1, 2, 3,
        1, 2, 3,
        1, 2, 3,
        1, 2, 3,
        1, 2, 3,
        1, 2, 3,
        1, 2, 3,
        1, 2, 3,
        // Login & multi & cloud
        1, 2, 3,
        1, 2, 3,
        1
    };

    public static String[] NAME = new String[]{
        "Lv 1: Thắng 100 ván solo", "Lv 2: Thắng 1000 ván solo", "Lv 3: Thắng 10000 ván solo",
        "Lv 1: Bắn 100.000 điểm HP", "Lv 2: Bắn 1.000.000 điểm HP", "Lv 3: Bắn 1.000.000.000 điểm HP",
        "Lv 1: Thắng 50 ván đấu UFO", "Lv 2: Thắng 100 ván đấu UFO", "Lv 3: Thắng 200 ván đấu UFO",
        "Lv 1: Thắng 50 ván đấu Khí Cầu", "Lv 2: Thắng 100 ván đấu Khí Cầu", "Lv 3: Thắng 200 ván đấu Khí Cầu",
        "Lv 1: Thắng 50 ván đấu Ma", "Lv 2: Thắng 100 ván đấu Ma", "Lv 3: Thắng 200 ván đấu Ma",
        "Lv 1: Ném 200 quả B52", "Lv 2: Ném 2000 quả B52", "Lv 3: Ném 20000 quả B52",
        "Lv 1: Tiêu diệt 200 Tarzan", "Lv 2: Tiêu diệt 2000 Tarzan", "Lv 3: Tiêu diệt 20000 Tarzan",
        "Lv 1: Tiêu diệt 200 Chicky", "Lv 2: Tiêu diệt 2000 Chicky", "Lv 3: Tiêu diệt 20000 Chicky",
        "Lv 1: Tiêu diệt 200 Magenta", "Lv 2: Tiêu diệt 2000 Magenta", "Lv 3: Tiêu diệt 20000 Magenta",
        "Lv 1: Tạo 1 viên ngọc cấp 8", "Lv 2: Tạo 5 viên ngọc cấp 8", "Lv 3: Tạo 10 viên ngọc cấp 8",
        "Lv 1: Tạo 1 viên ngọc cấp 9", "Lv 2: Tạo 5 viên ngọc cấp 9", "Lv 3: Tạo 10 viên ngọc cấp 9",
        "Lv 1: Tạo 1 viên ngọc cấp 10", "Lv 2: Tạo 5 viên ngọc cấp 10", "Lv 3: Tạo 10 viên ngọc cấp 10",
        "Lv 1: Bắn 100 phát siêu cao, siêu xa", "Lv 2: Bắn 1.000 phát siêu cao, siêu xa", "Lv 3: Bắn 10.000 phát siêu cao, siêu xa",
        // Gun missions with 5, 50, 500 wins
        "Lv 1: Dùng Gunner thắng 5 ván", "Lv 2: Dùng Gunner thắng 50 ván", "Lv 3: Dùng Gunner thắng 500 ván",
        "Lv 1: Dùng Miss 6 thắng 5 ván", "Lv 2: Dùng Miss 6 thắng 50 ván", "Lv 3: Dùng Miss 6 thắng 500 ván",
        "Lv 1: Dùng Electician thắng 5 ván", "Lv 2: Dùng Electician thắng 50 ván", "Lv 3: Dùng Electician thắng 500 ván",
        "Lv 1: Dùng King Kong thắng 5 ván", "Lv 2: Dùng King Kong thắng 50 ván", "Lv 3: Dùng King Kong thắng 500 ván",
        "Lv 1: Dùng Rocketer thắng 5 ván", "Lv 2: Dùng Rocketer thắng 50 ván", "Lv 3: Dùng Rocketer thắng 500 ván",
        "Lv 1: Dùng Granos thắng 5 ván", "Lv 2: Dùng Granos thắng 50 ván", "Lv 3: Dùng Granos thắng 500 ván",
        "Lv 1: Dùng Chicky thắng 5 ván", "Lv 2: Dùng Chicky thắng 50 ván", "Lv 3: Dùng Chicky thắng 500 ván",
        "Lv 1: Dùng Tarzan thắng 5 ván", "Lv 2: Dùng Tarzan thắng 50 ván", "Lv 3: Dùng Tarzan thắng 500 ván",
        "Lv 1: Dùng Apache thắng 5 ván", "Lv 2: Dùng Apache thắng 50 ván", "Lv 3: Dùng Apache thắng 500 ván",
        "Lv 1: Dùng Magenta thắng 5 ván", "Lv 2: Dùng Magenta thắng 50 ván", "Lv 3: Dùng Magenta thắng 500 ván",
        "Lv 1: Dùng Draby thắng 5 ván", "Lv 2: Dùng Draby thắng 50 ván", "Lv 3: Dùng Draby thắng 500 ván",
        "Lv 1: Dùng Cow Girl thắng 5 ván", "Lv 2: Dùng Cow Girl thắng 50 ván", "Lv 3: Dùng Cow Girl thắng 500 ván",
        // Login & multi & cloud
        "Lv 1: Đăng nhập 30 ngày", "Lv 2: Đăng nhập 90 ngày", "Lv 3: Đăng nhập 270 ngày",
        "Lv 1: Thắng 500 ván đấu trên 5 người chơi", "Lv 2: Thắng 1000 ván đấu trên 5 người chơi", "Lv 3: Thắng 2000 ván đấu trên 5 người chơi",
        "Liên kết tài khoản Cloud"
    };

    public static String[] REWARD = new String[]{
        "+1k xp, +100 danh dự", "+10k xp, +1k danh dự", "+100k xp, +10k danh dự",
        "+5k xp, +500 danh dự", "+50k xp, +2k danh dự", "+500k xp, +8k danh dự",
        "+2k xp, +500 danh dự", "+20k xp, +2k danh dự", "+200k xp, +8k danh dự",
        "+2k xp, +500 danh dự", "+20k xp, +2k danh dự", "+200k xp, +8k danh dự",
        "+2k xp, +500 danh dự", "+20k xp, +2k danh dự", "+200k xp, +8k danh dự",
        "+2k xp, +200 danh dự", "+20k xp, +1k danh dự", "+200k xp, +5k danh dự",
        "+2k xp, +200 danh dự", "+20k xp, +1k danh dự", "+200k xp, +5k danh dự",
        "+2k xp, +200 danh dự", "+20k xp, +1k danh dự", "+200k xp, +5k danh dự",
        "+2k xp, +200 danh dự", "+20k xp, +1k danh dự", "+200k xp, +5k danh dự",
        "+1k xp, +100 danh dự", "+10k xp, +500 danh dự", "+100k xp, +1k danh dự",
        "+5k xp, +500 danh dự", "+50k xp, +5k danh dự", "+500k xp, +50k danh dự",
        "+50k xp, +5k danh dự", "+500k xp, +50k danh dự", "+5Tr xp, +500k danh dự",
        "+5k xp, +100 danh dự", "+25k xp, +1k danh dự", "+125k xp, +10k danh dự",
        // Gun rewards
        "+5k xp, +10k xu, +50 lượng", "+50k xp, +50k xu, +200 lượng", "+500k xp, +500k xu, +1k lượng",
        "+5k xp, +10k xu, +50 lượng", "+50k xp, +50k xu, +200 lượng", "+500k xp, +500k xu, +1k lượng",
        "+5k xp, +10k xu, +50 lượng", "+50k xp, +50k xu, +200 lượng", "+500k xp, +500k xu, +1k lượng",
        "+5k xp, +10k xu, +50 lượng", "+50k xp, +50k xu, +200 lượng", "+500k xp, +500k xu, +1k lượng",
        "+5k xp, +10k xu, +50 lượng", "+50k xp, +50k xu, +200 lượng", "+500k xp, +500k xu, +1k lượng",
        "+5k xp, +10k xu, +50 lượng", "+50k xp, +50k xu, +200 lượng", "+500k xp, +500k xu, +1k lượng",
        "+5k xp, +10k xu, +50 lượng", "+50k xp, +50k xu, +200 lượng", "+500k xp, +500k xu, +1k lượng",
        "+5k xp, +10k xu, +50 lượng", "+50k xp, +50k xu, +200 lượng", "+500k xp, +500k xu, +1k lượng",
        "+5k xp, +10k xu, +50 lượng", "+50k xp, +50k xu, +200 lượng", "+500k xp, +500k xu, +1k lượng",
        "+5k xp, +10k xu, +50 lượng", "+50k xp, +50k xu, +200 lượng", "+500k xp, +500k xu, +1k lượng",
        "+5k xp, +10k xu, +50 lượng", "+50k xp, +50k xu, +200 lượng", "+500k xp, +500k xu, +1k lượng",
        "+5k xp, +10k xu, +50 lượng", "+50k xp, +50k xu, +200 lượng", "+500k xp, +500k xu, +1k lượng",
        // Login & multi & cloud
        "+10k xp, +500 danh dự", "+30k xp, +1,5k danh dự", "+90k xp, +4,5k danh dự",
        "+5k xp, +500 danh dự", "+10k xp, +1k danh dự", "+20k xp, +2k danh dự",
        "+1000 xu, +100 lượng, +1000 exp, 1 Ngọc cấp 10 ngẫu nhiên"
    };

    public static int[] REQUIRE = new int[]{
        100, 1000, 10000,
        100000, 1000000, 1000000000,
        50, 100, 200,
        50, 100, 200,
        50, 100, 200,
        200, 2000, 20000,
        200, 2000, 20000,
        200, 2000, 20000,
        200, 2000, 20000,
        1, 5, 10,
        1, 5, 10,
        1, 5, 10,
        100, 1000, 10000,
        // Gun 0..11: 5, 50, 500
        5, 50, 500,
        5, 50, 500,
        5, 50, 500,
        5, 50, 500,
        5, 50, 500,
        5, 50, 500,
        5, 50, 500,
        5, 50, 500,
        5, 50, 500,
        5, 50, 500,
        5, 50, 500,
        5, 50, 500,
        // Login & multi & cloud
        30, 90, 270,
        500, 1000, 2000,
        1
    };

    public static int[] EXP = new int[]{
        1000, 10000, 100000,
        5000, 50000, 500000,
        2000, 20000, 200000,
        2000, 20000, 200000,
        2000, 20000, 200000,
        2000, 20000, 200000,
        2000, 20000, 200000,
        2000, 20000, 200000,
        2000, 20000, 200000,
        1000, 10000, 100000,
        5000, 50000, 500000,
        50000, 500000, 5000000,
        5000, 25000, 125000,
        // Gun 0..11
        5000, 50000, 500000,
        5000, 50000, 500000,
        5000, 50000, 500000,
        5000, 50000, 500000,
        5000, 50000, 500000,
        5000, 50000, 500000,
        5000, 50000, 500000,
        5000, 50000, 500000,
        5000, 50000, 500000,
        5000, 50000, 500000,
        5000, 50000, 500000,
        5000, 50000, 500000,
        // Login & multi & cloud
        10000, 30000, 90000,
        5000, 10000, 20000,
        1000
    };

    public static int[] CUP = new int[]{
        100, 1000, 10000,
        500, 2000, 8000,
        500, 2000, 8000,
        500, 2000, 8000,
        500, 2000, 8000,
        200, 1000, 5000,
        200, 1000, 5000,
        200, 1000, 5000,
        200, 1000, 5000,
        100, 500, 1000,
        500, 5000, 50000,
        5000, 50000, 500000,
        100, 1000, 10000,
        // Gun 0..11
        200, 1000, 5000,
        200, 1000, 5000,
        200, 1000, 5000,
        200, 1000, 5000,
        200, 1000, 5000,
        200, 1000, 5000,
        200, 1000, 5000,
        200, 1000, 5000,
        200, 1000, 5000,
        200, 1000, 5000,
        200, 1000, 5000,
        200, 1000, 5000,
        // Login & multi & cloud
        500, 1500, 4500,
        500, 1000, 2000,
        0
    };

    public static int[] REWARD_XU = new int[]{
        5000, 50000, 500000,
        10000, 100000, 1000000,
        10000, 50000, 500000,
        10000, 50000, 500000,
        10000, 50000, 500000,
        10000, 50000, 500000,
        10000, 50000, 500000,
        10000, 50000, 500000,
        10000, 50000, 500000,
        10000, 50000, 500000,
        20000, 200000, 1000000,
        100000, 1000000, 10000000,
        10000, 50000, 500000,
        // Gun 0..11
        10000, 50000, 500000,
        10000, 50000, 500000,
        10000, 50000, 500000,
        10000, 50000, 500000,
        10000, 50000, 500000,
        10000, 50000, 500000,
        10000, 50000, 500000,
        10000, 50000, 500000,
        10000, 50000, 500000,
        10000, 50000, 500000,
        10000, 50000, 500000,
        10000, 50000, 500000,
        // Login & multi & cloud
        50000, 150000, 450000,
        50000, 100000, 200000,
        1000
    };

    public static int[] REWARD_LUONG = new int[]{
        10, 50, 200,
        20, 100, 500,
        20, 50, 200,
        20, 50, 200,
        20, 50, 200,
        10, 50, 200,
        10, 50, 200,
        10, 50, 200,
        10, 50, 200,
        10, 50, 200,
        20, 100, 500,
        100, 500, 2000,
        10, 50, 200,
        // Gun 0..11
        50, 200, 1000,
        50, 200, 1000,
        50, 200, 1000,
        50, 200, 1000,
        50, 200, 1000,
        50, 200, 1000,
        50, 200, 1000,
        50, 200, 1000,
        50, 200, 1000,
        50, 200, 1000,
        50, 200, 1000,
        50, 200, 1000,
        // Login & multi & cloud
        50, 150, 450,
        50, 100, 200,
        100
    };

    public static int[] progress = new int[64];
    public static boolean[] claimed = new boolean[128];
    public static int loginStreak;
    public static int lastLoginYmd;

    private OfflineMission() {
    }

    public static void reset() {
        progress = new int[64];
        claimed = new boolean[128];
        loginStreak = 0;
        lastLoginYmd = 0;
    }

    public static void onCloudAccountLinked() {
        OfflineMission.addProgress(MISSION_ID_CLOUD_LINK, 1);
    }

    public static void addProgress(int n, int n2) {
        if (n < 0 || n >= progress.length || n2 <= 0) {
            return;
        }
        long l = (long)progress[n] + (long)n2;
        OfflineMission.progress[n] = l > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int)l;
    }

    public static void onLogin() {
        Calendar calendar = Calendar.getInstance();
        int n = calendar.get(Calendar.YEAR) * 10000 + (calendar.get(Calendar.MONTH) + 1) * 100 + calendar.get(Calendar.DAY_OF_MONTH);
        if (n == lastLoginYmd) {
            return;
        }
        lastLoginYmd = n;
        ++loginStreak;
        OfflineMission.addProgress(25, 1);
    }

    public static void onBossWin(CPlayer bossPlayer) {
        OfflineMission.addProgress(0, 1);
        if (bossPlayer != null) {
            if (bossPlayer.gun == 16) {
                OfflineMission.addProgress(2, 1);
            } else if (bossPlayer.gun == 17) {
                OfflineMission.addProgress(3, 1);
            } else if (bossPlayer.gun == 25 || bossPlayer.gun == 26) {
                OfflineMission.addProgress(4, 1);
            }
        }
        PlayerInfo playerInfo = TerrainMidlet.myInfo;
        if (playerInfo != null) {
            int g = playerInfo.gun;
            if (g >= 0 && g < GUN_MISSION_COUNT) {
                OfflineMission.addProgress(GUN_MISSION_START_ID + g, 1);
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

    public static void onPvpBotWin(int totalPlayers) {
        OfflineMission.addProgress(0, 1);
        PlayerInfo playerInfo = TerrainMidlet.myInfo;
        if (playerInfo != null) {
            int g = playerInfo.gun;
            if (g >= 0 && g < GUN_MISSION_COUNT) {
                OfflineMission.addProgress(GUN_MISSION_START_ID + g, 1);
            }
        }
        if (totalPlayers > 5) {
            OfflineMission.addProgress(26, 1);
        }
    }

    private static Mission buildEntry(int n) {
        Mission mission = new Mission();
        mission.id = ID[n];
        mission.level = LEVEL[n];
        mission.name = NAME[n];
        mission.reward = REWARD[n];
        mission.require = REQUIRE[n];
        int curProg = (mission.id >= 0 && mission.id < progress.length) ? progress[mission.id] : 0;
        mission.have = curProg > REQUIRE[n] ? REQUIRE[n] : curProg;
        mission.isComplete = curProg >= REQUIRE[n];
        mission.isGetReward = (n >= 0 && n < claimed.length) && claimed[n];
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
        if (n3 < REWARD_XU.length) playerInfo.xu += REWARD_XU[n3];
        if (n3 < REWARD_LUONG.length) playerInfo.luong += REWARD_LUONG[n3];
        OfflineCombat.grantExpDirect(EXP[n3]);

        if (n == MISSION_ID_CLOUD_LINK) {
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

    public static void applyRemoteConfig(String json) {
        if (json == null || json.length() == 0) return;
        try {
            String missionsRaw = JsonLite.getArrayRaw(json, "missions");
            Vector vec = JsonLite.splitArrayObjects(missionsRaw);
            if (vec == null || vec.size() == 0) return;

            for (int i = 0; i < vec.size(); ++i) {
                String item = (String) vec.elementAt(i);
                int mid = JsonLite.getInt(item, "id", -1);
                int mlevel = JsonLite.getInt(item, "level", 1);
                int idx = indexOf(mid, mlevel);
                if (idx >= 0 && idx < NAME.length) {
                    String name = JsonLite.getString(item, "name");
                    if (name != null && name.length() > 0) NAME[idx] = name;
                    int req = JsonLite.getInt(item, "require", -1);
                    if (req > 0) REQUIRE[idx] = req;
                    int exp = JsonLite.getInt(item, "reward_exp", -1);
                    if (exp >= 0) EXP[idx] = exp;
                    int xu = JsonLite.getInt(item, "reward_xu", -1);
                    if (xu >= 0) REWARD_XU[idx] = xu;
                    int luong = JsonLite.getInt(item, "reward_luong", -1);
                    if (luong >= 0) REWARD_LUONG[idx] = luong;
                    int cup = JsonLite.getInt(item, "reward_cup", -1);
                    if (cup >= 0) CUP[idx] = cup;
                    REWARD[idx] = "+" + (EXP[idx] >= 1000 ? (EXP[idx] / 1000 + "k") : EXP[idx]) + " xp, +" +
                            (REWARD_XU[idx] >= 1000 ? (REWARD_XU[idx] / 1000 + "k") : REWARD_XU[idx]) + " xu, +" +
                            REWARD_LUONG[idx] + " lượng";
                }
            }
            System.out.println("[OfflineMission] Applied remote missions configuration successfully (" + vec.size() + " items)");
        } catch (Exception e) {
            System.err.println("[OfflineMission] Failed to parse remote missions: " + e.getMessage());
        }
    }
}
