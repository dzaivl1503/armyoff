/*
 * Mobi Army 2 Leaderboard System (12 Gun Rankings & Wealth/Level Rankings)
 */
package com.teamobi.mobiarmy2;

import Equipment.PlayerEquip;
import coreLG.CCanvas;
import coreLG.TerrainMidlet;
import java.util.Vector;
import model.IAction2;
import model.PlayerInfo;
import screen.ListScr;
import screen.MenuScr;
import screen.PrepareScr;

public final class OfflineLeaderboard {
    public static final String[] GUN_NAMES = new String[]{
        "Gunner", "Miss 6", "Electician", "King Kong", "Rocketer", 
        "Granos", "Chicky", "Tarzan", "Apache", "Magenta", "Draby", "Cow Girl"
    };

    public static final String[] TOP_MENU_LABELS = new String[]{
        "TOP CAO THỦ",
        "TOP ĐẠI GIA XU",
        "TOP ĐẠI GIA LƯỢNG",
        "TOP ĐIỂM CÚP",
        "TOP GUNNER",
        "TOP MISS 6",
        "TOP ELECTICIAN",
        "TOP KING KONG",
        "TOP ROCKETER",
        "TOP GRANOS",
        "TOP CHICKY",
        "TOP TARZAN",
        "TOP APACHE",
        "TOP MAGENTA",
        "TOP DRABY",
        "TOP COW GIRL"
    };

    private OfflineLeaderboard() {
    }

    public static void showLeaderboard(final int type) {
        CCanvas.startWaitDlg("Đang tải Bảng Xếp Hạng...");
        String url = CloudSaveApi.getServerUrl() + "/leaderboard?type=" + type;
        
        GameMidlet.connectHTTP(url, new IAction2() {
            public void perform(Object obj) {
                CCanvas.endDlg();
                String res = (obj != null) ? (String) obj : "";
                Vector list = parseLeaderboardJson(res, type);
                if (list == null || list.isEmpty()) {
                    list = generateFallbackList(type);
                }
                displayList(type, list);
            }
        });
    }

    private static void displayList(int type, Vector list) {
        if (CCanvas.listScr == null) {
            CCanvas.listScr = new ListScr();
        }
        CCanvas.listScr.lastSCreen = (CCanvas.menuScr != null ? CCanvas.menuScr : CCanvas.curScr);
        CCanvas.listScr.typeList = getCategoryTitle(type);
        CCanvas.listScr.setList(0, list);
        CCanvas.listScr.show(CCanvas.listScr.lastSCreen);
    }

    public static String getCategoryTitle(int type) {
        if (type >= 0 && type < TOP_MENU_LABELS.length) {
            return TOP_MENU_LABELS[type];
        }
        return "BẢNG XẾP HẠNG";
    }

    private static Vector parseLeaderboardJson(String json, int type) {
        if (json == null || json.trim().isEmpty() || !json.contains("[")) {
            return null;
        }
        try {
            String arrStr = JsonLite.getArrayRaw(json, "data");
            if (arrStr == null) arrStr = json;
            Vector items = JsonLite.splitArrayObjects(arrStr);
            if (items == null || items.isEmpty()) return null;

            Vector list = new Vector();
            PlayerInfo me = TerrainMidlet.myInfo;
            for (int i = 0; i < items.size(); ++i) {
                String item = (String) items.elementAt(i);
                PlayerInfo p = new PlayerInfo();
                p.STT = (short) (i + 1);
                p.name = JsonLite.getString(item, "username");
                if (p.name == null || p.name.isEmpty()) p.name = "Chiến Binh " + (i + 1);
                
                if (type >= 4 && type <= 15) {
                    p.gun = (byte) (type - 4);
                } else {
                    p.gun = (byte) JsonLite.getInt(item, "gun", me != null ? me.gun : 0);
                }
                
                p.level2 = JsonLite.getInt(item, "level", 1);

                if (me != null && p.name.equalsIgnoreCase(me.name)) {
                    if (type >= 4 && type <= 15) {
                        int gunIdx = type - 4;
                        p.gun = (byte) gunIdx;
                        p.level2 = (me.classLevel2 != null && gunIdx < me.classLevel2.length) ? me.classLevel2[gunIdx] : 1;
                        p.aa = "Lv " + p.level2;
                    }
                    for (int s = 0; s < 6; ++s) {
                        p.equipID[p.gun][s] = me.equipID[p.gun][s];
                    }
                } else {
                    PlayerEquip.applyDefaultOfflineEquipIds(p);
                }

                p.nQuanHam2 = (byte) Math.min(15, Math.max(0, p.level2 / 5));
                p.aa = JsonLite.getString(item, "score");
                if (p.aa == null || p.aa.isEmpty()) {
                    p.aa = "Lv " + p.level2;
                }

                p.getMyEquip(10);
                p.ensureCombatEquip();
                list.addElement(p);
            }
            return list;
        } catch (Exception e) {
            return null;
        }
    }

    private static Vector generateFallbackList(int type) {
        Vector list = new Vector();
        PlayerInfo me = TerrainMidlet.myInfo;
        byte targetGun = (byte) (type >= 4 && type <= 15 ? (type - 4) : (me != null ? me.gun : 0));
        
        // 1. Player entry
        if (me != null) {
            PlayerInfo myEntry = new PlayerInfo();
            myEntry.STT = 1;
            myEntry.name = me.name;
            myEntry.gun = targetGun;
            if (type >= 4 && type <= 15) {
                int gunIdx = type - 4;
                myEntry.level2 = (me.classLevel2 != null && gunIdx < me.classLevel2.length) ? me.classLevel2[gunIdx] : 1;
                myEntry.aa = "Lv " + myEntry.level2;
            } else if (type == 1) { // Xu
                myEntry.level2 = me.level2;
                myEntry.aa = formatMoney(me.xu) + " Xu";
            } else if (type == 2) { // Lượng
                myEntry.level2 = me.level2;
                myEntry.aa = formatMoney(me.luong) + " Lượng";
            } else if (type == 3) { // Cúp
                myEntry.level2 = me.level2;
                myEntry.aa = me.cup + " Cúp";
            } else { // Top Cao Thủ
                myEntry.level2 = me.level2;
                myEntry.aa = "Lv " + me.level2;
            }
            myEntry.nQuanHam2 = (byte) Math.min(15, Math.max(0, myEntry.level2 / 5));
            for (int s = 0; s < 6; ++s) {
                myEntry.equipID[myEntry.gun][s] = me.equipID[myEntry.gun][s];
            }
            myEntry.getMyEquip(10);
            myEntry.ensureCombatEquip();
            list.addElement(myEntry);
        }

        // 2. Legend NPC ranks
        String[] legendNames = new String[]{
            "Bá Vương Xạ Thủ", "Thánh Súng VIP", "Huyền Thoại Army", 
            "Độc Cô Cầu Bại", "Thiện Xạ PRO", "Thần Gió Xuyên Tâm", "Chúa Tể Đạn Lạc"
        };
        int baseLevel = 75;
        for (int i = 0; i < legendNames.length; ++i) {
            PlayerInfo bot = new PlayerInfo();
            bot.STT = (short) (list.size() + 1);
            bot.name = legendNames[i];
            bot.gun = (type >= 4 && type <= 15) ? targetGun : (byte) ((i * 3) % 12);
            bot.level2 = baseLevel - (i * 6);
            bot.nQuanHam2 = (byte) Math.min(15, Math.max(0, bot.level2 / 5));
            if (type == 1) {
                bot.aa = formatMoney((long) (10000000 - i * 1200000)) + " Xu";
            } else if (type == 2) {
                bot.aa = formatMoney((long) (50000 - i * 6000)) + " Lượng";
            } else if (type == 3) {
                bot.aa = (10000 - i * 1100) + " Cúp";
            } else {
                bot.aa = "Lv " + bot.level2;
            }
            PlayerEquip.applyDefaultOfflineEquipIds(bot);
            bot.getMyEquip(10);
            bot.ensureCombatEquip();
            list.addElement(bot);
        }
        return list;
    }

    private static String formatMoney(long n) {
        String s = String.valueOf(n);
        StringBuilder sb = new StringBuilder();
        int count = 0;
        for (int i = s.length() - 1; i >= 0; --i) {
            sb.append(s.charAt(i));
            if (++count % 3 == 0 && i > 0) {
                sb.append('.');
            }
        }
        return sb.reverse().toString();
    }
}
