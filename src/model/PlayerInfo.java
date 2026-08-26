/*
 * Decompiled with CFR 0.152.
 */
package model;

import CLib.mImage;
import Equipment.Equip;
import Equipment.PlayerEquip;
import com.teamobi.mobiarmy2.OfflineChest;
import com.teamobi.mobiarmy2.OfflineCombat;
import com.teamobi.mobiarmy2.OfflineEquipmentStats;
import java.util.Vector;
import model.CRes;
import model.Language;
import model.LevelDetail;
import coreLG.TerrainMidlet;

public class PlayerInfo {
    public String name;
    public int IDDB = -1;
    public int exp;
    public int level2;
    public int level2Percen;
    public int nextExp;
    public int STT;
    public byte index;
    public short point;
    public int win;
    public String aa;
    public int cup;
    public byte gun;
    public short clanID;
    public byte isMaster;
    public final byte YELLOW;
    public final byte GREEN;
    public final byte RED = (byte)2;
    public short[][] equipID = new short[11][6];
    public short[][] equipVipID = new short[11][6];
    public int[][] itemLoadout = new int[11][8];
    public short[] gunDame;
    public short[] attribute;
    public byte[] UpOrDown1;
    public byte[] UpOrDown2;
    public int[] attAddPoint1;
    public int[] attAddPoint2;
    public boolean isVip;
    public boolean isReady;
    public int xu;
    public int luong;
    public int nQuanHam2;
    public Vector itemME;
    public boolean isBoss;
    public int maxHP;
    public LevelDetail level;
    public byte lvl;
    public byte lvl2;
    public short[] ability;
    public int[] classExp;
    public int[] classLevel2;
    public int[] classLevel2Percen;
    public int[] classNextExp;
    public short[] classPoint;
    public short[][] classAbility;
    public String[] detail;
    public PlayerEquip myEquip;
    public PlayerEquip myVipEquip;
    public int[] dbKey;
    public static String[] strLevelCaption;
    public static int[] levelCaption;
    public mImage clanIcon;
    public static int vipID;
    public String clanContribute1;
    public String clanContribute2;
    public byte[] squadExtra;

    public PlayerInfo() {
        this.YELLOW = 0;
        this.GREEN = 1;
        for (int i = 0; i < this.itemLoadout.length; ++i) {
            this.initItemLoadoutDefaults((byte)i);
        }
        this.gunDame = new short[11];
        this.attribute = new short[5];
        this.UpOrDown1 = new byte[5];
        this.UpOrDown2 = new byte[5];
        this.attAddPoint1 = new int[5];
        this.attAddPoint2 = new int[5];
        this.level = new LevelDetail();
        this.ability = new short[5];
        this.classExp = new int[11];
        this.classLevel2 = new int[11];
        this.classLevel2Percen = new int[11];
        this.classNextExp = new int[11];
        this.classPoint = new short[11];
        this.classAbility = new short[11][5];
        this.detail = new String[5];
        this.dbKey = new int[6];
        this.squadExtra = new byte[]{-1, -1, -1};
    }

    public void initItemLoadoutDefaults(byte by) {
        if (by < 0 || by >= this.itemLoadout.length) {
            return;
        }
        for (int i = 0; i < this.itemLoadout[by].length; ++i) {
            this.itemLoadout[by][i] = -2;
        }
        if (this.itemLoadout[by].length > 3) {
            this.itemLoadout[by][0] = 0;
            this.itemLoadout[by][1] = 0;
            this.itemLoadout[by][2] = 1;
            this.itemLoadout[by][3] = 1;
        }
    }

    public int getSquadSize() {
        int n = 0;
        for (int i = 0; i < this.squadExtra.length; ++i) {
            if (this.squadExtra[i] < 0) continue;
            ++n;
        }
        return n;
    }

    public boolean isInSquad(byte by) {
        for (int i = 0; i < this.squadExtra.length; ++i) {
            if (this.squadExtra[i] != by) continue;
            return true;
        }
        return false;
    }

    public PlayerInfo createSquadSnapshot(byte by) {
        this.ensureClassProgress();
        PlayerInfo playerInfo = new PlayerInfo();
        playerInfo.IDDB = this.IDDB;
        playerInfo.name = this.name;
        playerInfo.gun = by;
        playerInfo.level2 = this.classLevel2[by] <= 0 ? 1 : this.classLevel2[by];
        playerInfo.clanIcon = this.clanIcon;
        playerInfo.nQuanHam2 = this.nQuanHam2;
        playerInfo.isReady = true;
        for (int i = 0; i < 5; ++i) {
            playerInfo.ability[i] = this.classAbility[by][i];
        }
        playerInfo.equipID = this.equipID;
        playerInfo.equipVipID = this.equipVipID;
        playerInfo.itemLoadout = this.itemLoadout;
        playerInfo.ensureCombatEquip();
        playerInfo.maxHP = OfflineEquipmentStats.maxHp(playerInfo);
        return playerInfo;
    }

    public void ensureClassProgress() {
        for (int i = 0; i < this.classLevel2.length; ++i) {
            if (this.classLevel2[i] <= 0) {
                this.classLevel2[i] = 1;
            }
            if (this.classNextExp[i] > 0) continue;
            this.classNextExp[i] = OfflineCombat.expThresholdForLevel(this.classLevel2[i]);
        }
    }

    public void initClassProgressDefaults() {
        for (int i = 0; i < this.classLevel2.length; ++i) {
            this.classExp[i] = 0;
            this.classLevel2[i] = (i == 0) ? 50 : 1;
            this.classLevel2Percen[i] = 0;
            this.classNextExp[i] = OfflineCombat.expThresholdForLevel(this.classLevel2[i]);
            this.classPoint[i] = (short)((i == 0) ? 100 : 0);
            for (int j = 0; j < 5; ++j) {
                this.classAbility[i][j] = 0;
            }
        }
        if (this.classAbility.length > 10) {
            this.classAbility[10][0] = 10;
            this.classAbility[10][1] = 0;
            this.classAbility[10][2] = 10;
            this.classAbility[10][3] = 10;
            this.classAbility[10][4] = 10;
        }
        this.loadCurrentClassProgress();
    }

    public void migrateCurrentProgressToClass() {
        this.ensureClassProgress();
        if (this.gun < 0 || this.gun >= this.classLevel2.length) {
            return;
        }
        this.classExp[this.gun] = this.exp;
        this.classLevel2[this.gun] = this.level2 <= 0 ? 1 : this.level2;
        this.classLevel2Percen[this.gun] = this.level2Percen;
        this.classNextExp[this.gun] = this.nextExp > 0 ? this.nextExp : OfflineCombat.expThresholdForLevel(this.classLevel2[this.gun]);
        this.classPoint[this.gun] = this.point;
        for (int i = 0; i < 5; ++i) {
            this.classAbility[this.gun][i] = this.ability[i];
        }
    }

    public void saveCurrentClassProgress() {
        if (this.gun < 0 || this.gun >= this.classLevel2.length) {
            return;
        }
        this.classExp[this.gun] = this.exp;
        this.classLevel2[this.gun] = this.level2;
        this.classLevel2Percen[this.gun] = this.level2Percen;
        this.classNextExp[this.gun] = this.nextExp;
        this.classPoint[this.gun] = this.point;
        for (int i = 0; i < 5; ++i) {
            this.classAbility[this.gun][i] = this.ability[i];
        }
    }

    public boolean grantClassExp(byte by, int n) {
        if (by < 0 || by >= this.classExp.length || n <= 0) {
            return false;
        }
        this.ensureClassProgress();
        if (this.classNextExp[by] <= 0) {
            this.classNextExp[by] = OfflineCombat.expThresholdForLevel(this.classLevel2[by]);
        }
        byte by2 = by;
        this.classExp[by2] = this.classExp[by2] + n;
        boolean bl = false;
        while (this.classNextExp[by] > 0 && this.classExp[by] >= this.classNextExp[by]) {
            byte by3 = by;
            this.classExp[by3] = this.classExp[by3] - this.classNextExp[by];
            byte by4 = by;
            this.classLevel2[by4] = this.classLevel2[by4] + 1;
            byte by5 = by;
            this.classPoint[by5] = (short)(this.classPoint[by5] + 2);
            this.classNextExp[by] = OfflineCombat.expThresholdForLevel(this.classLevel2[by]);
            bl = true;
        }
        int n2 = this.classLevel2Percen[by] = this.classNextExp[by] > 0 ? this.classExp[by] * 100 / this.classNextExp[by] : 0;
        if (by == this.gun) {
            this.loadCurrentClassProgress();
        }
        return bl;
    }

    public void loadCurrentClassProgress() {
        this.ensureClassProgress();
        if (this.gun < 0 || this.gun >= this.classLevel2.length) {
            return;
        }
        this.exp = this.classExp[this.gun];
        this.level2 = this.classLevel2[this.gun];
        this.level2Percen = this.classLevel2Percen[this.gun];
        this.nextExp = this.classNextExp[this.gun];
        this.point = this.classPoint[this.gun];
        for (int i = 0; i < 5; ++i) {
            this.ability[i] = this.classAbility[this.gun][i];
        }
        this.getAttribute();
    }

    public void switchGunProgress(byte by) {
        if (by < 0 || by >= this.classLevel2.length || by == this.gun) {
            return;
        }
        this.saveCurrentClassProgress();
        this.gun = by;
        this.loadCurrentClassProgress();
    }

    public int getClassLevel(int n) {
        this.ensureClassProgress();
        if (n < 0 || n >= this.classLevel2.length) {
            return 1;
        }
        return n == this.gun ? this.level2 : this.classLevel2[n];
    }

    public int getClassExp(int n) {
        this.ensureClassProgress();
        if (n < 0 || n >= this.classExp.length) {
            return 0;
        }
        return n == this.gun ? this.exp : this.classExp[n];
    }

    public int getClassNextExp(int n) {
        this.ensureClassProgress();
        if (n < 0 || n >= this.classNextExp.length) {
            return OfflineCombat.expThresholdForLevel(1);
        }
        return n == this.gun ? this.nextExp : this.classNextExp[n];
    }

    public void getAttribute() {
        for (int i = 0; i < this.ability.length; ++i) {
            this.attribute[i] = this.ability[i];
            this.attAddPoint1[i] = 0;
            this.attAddPoint2[i] = 0;
        }
    }

    public void setAllEquipEffect() {
        this.getAttribute();
        if (this.myEquip != null && this.myEquip.equips != null) {
            for (int i = 0; i < this.myEquip.equips.length; ++i) {
                if (this.myEquip.equips[i] == null) continue;
                this.myEquip.equips[i].setInvAtribute();
                this.addCurrEquip(this.myEquip.equips[i]);
            }
        }
        if (TerrainMidlet.isVip != null && this.gun >= 0 && this.gun < TerrainMidlet.isVip.length && TerrainMidlet.isVip[this.gun]) {
            short vipId = this.equipVipID[this.gun][1];
            if (vipId > 0) {
                Equip vipEquip = PlayerEquip.getEquip(this.gun, (byte)1, vipId);
                if (vipEquip != null) {
                    vipEquip.setInvAtribute();
                    this.addCurrEquip(vipEquip);
                }
            }
        }
    }

    public void getMyEquip(int n) {
        CRes.err(" ================> getMyEquip() " + n);
        if (PlayerEquip.playerData == null) {
            if (this.myEquip == null) {
                this.myEquip = new PlayerEquip();
            }
            return;
        }
        OfflineChest.captureWorn(this);
        short[] sArray = new short[]{this.gun, 0, this.equipID[this.gun][0]};
        short[] sArray2 = new short[]{this.gun, 1, this.equipID[this.gun][1]};
        short[] sArray3 = new short[]{this.gun, 2, this.equipID[this.gun][2]};
        short[] sArray4 = new short[]{this.gun, 3, this.equipID[this.gun][3]};
        short[] sArray5 = new short[]{this.gun, 4, this.equipID[this.gun][4]};
        this.myEquip = new PlayerEquip(new short[][]{sArray, sArray2, sArray3, sArray4, sArray5});
        OfflineChest.applyWorn(this);
        this.getVipEquip();
    }

    public void ensureCombatEquip() {
        PlayerEquip.migrateCostumeHats(this);
        PlayerEquip.applyDefaultOfflineEquipIds(this);
        if (this.myEquip == null || this.myEquip.glass != this.gun) {
            this.getMyEquip(10);
        }
        if (this.myVipEquip == null || this.myVipEquip.glass != this.gun) {
            this.getVipEquip();
        }
        if (this.myEquip == null) {
            this.myEquip = new PlayerEquip();
        }
        for (int i = 0; i < 5; ++i) {
            if (this.myEquip.equips[i] != null) continue;
            short s = this.equipID[this.gun][i];
            Equip equip = PlayerEquip.createEquip(this.gun, (byte)i, s);
            if (equip == null && s != -1 && i < 3) {
                this.equipID[this.gun][i] = s = PlayerEquip.getStarterEquipId(this.gun, (byte)i);
                equip = PlayerEquip.createEquip(this.gun, (byte)i, s);
            }
            this.myEquip.equips[i] = equip;
            OfflineChest.applyWornSlot(this, i);
        }
        if (this.myEquip.equips[0] == null) {
            short s0 = PlayerEquip.getStarterEquipId(this.gun, (byte)0);
            this.equipID[this.gun][0] = s0;
            this.myEquip.equips[0] = PlayerEquip.createEquip(this.gun, (byte)0, s0);
        }
        if (this.myEquip.equips[1] == null) {
            short s1 = PlayerEquip.getStarterEquipId(this.gun, (byte)1);
            this.equipID[this.gun][1] = s1;
            this.myEquip.equips[1] = PlayerEquip.createEquip(this.gun, (byte)1, s1);
        }
        if (this.myEquip.equips[2] == null) {
            short s2 = PlayerEquip.getStarterEquipId(this.gun, (byte)2);
            this.equipID[this.gun][2] = s2;
            this.myEquip.equips[2] = PlayerEquip.createEquip(this.gun, (byte)2, s2);
        }
    }

    public void getVipEquip() {
        if (PlayerEquip.playerData == null) {
            return;
        }
        short[] sArray = new short[]{this.gun, 0, this.equipVipID[this.gun][0]};
        short[] sArray2 = new short[]{this.gun, 1, this.equipVipID[this.gun][1]};
        short[] sArray3 = new short[]{this.gun, 2, this.equipVipID[this.gun][2]};
        short[] sArray4 = new short[]{this.gun, 3, this.equipVipID[this.gun][3]};
        short[] sArray5 = new short[]{this.gun, 4, this.equipVipID[this.gun][4]};
        short[] sArray6 = new short[]{this.gun, 5, this.equipVipID[this.gun][5]};
        this.myVipEquip = new PlayerEquip(new short[][]{sArray, sArray2, sArray3, sArray4, sArray5, sArray6});
    }

    public String getStrMoney() {
        return this.xu + Language.xu();
    }

    public String getStrMoney2() {
        return this.xu + Language.xu();
    }

    public void getAddAttPoint(Equip equip) {
        for (int i = 0; i < 5; ++i) {
            this.attAddPoint1[i] = equip.inv_ability[i];
            this.attAddPoint2[i] = equip.inv_percen[i];
        }
    }

    public void clearAttAddPoint() {
        for (int i = 0; i < 5; ++i) {
            this.attAddPoint1[i] = 0;
            this.attAddPoint2[i] = 0;
        }
    }

    public void addCurrEquip(Equip equip) {
        for (int i = 0; i < 5; ++i) {
            short[] sArray = this.attribute;
            int n = i;
            sArray[n] = (short)(sArray[n] + equip.inv_attAddPoint[i]);
        }
    }

    public void addChangeEquip(Equip equip, Equip equip2) {
        for (int i = 0; i < 5; ++i) {
            short s;
            if (equip2 != null) {
                s = equip2.inv_attAddPoint[i];
            }
            if (equip != null) {
                s = equip.inv_attAddPoint[i];
            }
            this.attAddPoint1[i] = 0;
            this.attAddPoint2[i] = 0;
            this.UpOrDown1[i] = 0;
            this.UpOrDown2[i] = 0;
        }
    }

    public void compareEquip(Equip equip, Equip equip2) {
        byte[] byArray = equip.inv_ability;
        byte[] byArray2 = null;
        byte[] byArray3 = equip.inv_percen;
        byte[] byArray4 = null;
        if (equip2 != null) {
            byArray2 = equip2.inv_ability;
            byArray4 = equip2.inv_percen;
        }
        for (int i = 0; i < 5; ++i) {
            byte by = 0;
            byte by2 = 0;
            if (equip != null) {
                by = byArray[i];
                by2 = byArray3[i];
            }
            byte by3 = 0;
            byte by4 = 0;
            if (equip2 != null) {
                by3 = byArray2[i];
                by4 = byArray4[i];
            }
            if (equip2 != null) {
                this.attAddPoint1[i] = by - by3;
                this.attAddPoint2[i] = by2 - by4;
            } else {
                equip2 = new Equip();
                for (int j = 0; j < 5; ++j) {
                    equip2.inv_ability[i] = 0;
                    equip2.inv_percen[i] = 0;
                }
                byArray2 = equip2.inv_ability;
                this.attAddPoint1[i] = by - byArray2[i];
                byArray4 = equip2.inv_percen;
                this.attAddPoint2[i] = by2 - byArray4[i];
            }
            this.UpOrDown1[i] = (byte)(this.attAddPoint1[i] == 0 ? 0 : (this.attAddPoint1[i] > 0 ? 1 : 2));
            this.UpOrDown2[i] = (byte)(this.attAddPoint2[i] == 0 ? 0 : (this.attAddPoint2[i] > 0 ? 1 : 2));
        }
    }

    public void getQuanHam() {
        if (levelCaption == null) {
            return;
        }
        for (int i = 0; i < levelCaption.length; ++i) {
            if (this.level2 < levelCaption[i]) continue;
            this.nQuanHam2 = i;
            break;
        }
    }

    public int getExp() {
        return this.exp;
    }
}

