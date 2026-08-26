/*
 * Decompiled with CFR 0.152.
 */
package com.teamobi.mobiarmy2;

import Equipment.Equip;
import Equipment.EquipGlass;
import Equipment.PlayerEquip;
import com.teamobi.mobiarmy2.OfflineTeamItems;
import model.CRes;
import model.PlayerInfo;

public final class OfflineEquipmentStats {
    private static final int[] BASE_ATTACK = new int[]{280, 290, 300, 420, 321, 310, 450, 340, 431, 410, 380};

    private OfflineEquipmentStats() {
    }

    public static int[] calculate(PlayerInfo playerInfo) {
        int n;
        int[] nArray = new int[5];
        if (playerInfo == null) {
            nArray[0] = 1000;
            nArray[1] = 280;
            return nArray;
        }
        playerInfo.ensureCombatEquip();
        int[] nArray2 = new int[5];
        int[] nArray3 = new int[5];
        if (playerInfo.myEquip != null) {
            for (n = 0; n < playerInfo.myEquip.equips.length; ++n) {
                Equip equip = playerInfo.myEquip.equips[n];
                if (equip == null) continue;
                for (int i = 0; i < 5; ++i) {
                    int n2 = i;
                    nArray2[n2] = nArray2[n2] + equip.inv_ability[i];
                    int n3 = i;
                    nArray3[n3] = nArray3[n3] + equip.inv_percen[i];
                }
            }
        }
        nArray[0] = 1000 + playerInfo.ability[0] * 10 + nArray2[0] * 10;
        nArray[0] = nArray[0] + (1000 + playerInfo.ability[0]) * nArray3[0] / 100;
        n = OfflineEquipmentStats.baseAttack(playerInfo.gun);
        nArray[1] = n * ((playerInfo.ability[1] + nArray2[1]) / 3 + 100 + nArray3[1]) / 100;
        nArray[2] = (playerInfo.ability[2] + nArray2[2]) * 10;
        nArray[2] = nArray[2] + nArray[2] * nArray3[2] / 100;
        nArray[3] = (playerInfo.ability[3] + nArray2[3]) * 10;
        nArray[3] = nArray[3] + nArray[3] * nArray3[3] / 100;
        nArray[4] = (playerInfo.ability[4] + nArray2[4]) * 10;
        nArray[4] = nArray[4] + nArray[4] * nArray3[4] / 100;
        nArray[0] = OfflineTeamItems.applyStat(0, nArray[0]);
        nArray[1] = OfflineTeamItems.applyStat(1, nArray[1]);
        nArray[2] = OfflineTeamItems.applyStat(2, nArray[2]);
        nArray[3] = OfflineTeamItems.applyStat(3, nArray[3]);
        if (nArray[0] < 1) {
            nArray[0] = 1;
        }
        if (nArray[1] < 1) {
            nArray[1] = n;
        }
        return nArray;
    }

    public static int maxHp(PlayerInfo playerInfo) {
        return OfflineEquipmentStats.calculate(playerInfo)[0];
    }

    public static int attack(PlayerInfo playerInfo) {
        return OfflineEquipmentStats.calculate(playerInfo)[1];
    }

    public static int defense(PlayerInfo playerInfo) {
        return OfflineEquipmentStats.calculate(playerInfo)[2];
    }

    public static int luck(PlayerInfo playerInfo) {
        return OfflineEquipmentStats.calculate(playerInfo)[3];
    }

    public static boolean rollLucky(PlayerInfo playerInfo) {
        int n = OfflineEquipmentStats.luck(playerInfo);
        if (n <= 0) {
            return false;
        }
        int n2 = n > 7500 ? 7500 : n;
        return CRes.random(0, 10000) <= n2;
    }

    public static int reduceDamage(PlayerInfo playerInfo, int n) {
        return OfflineEquipmentStats.reduceDamage(OfflineEquipmentStats.defense(playerInfo), n);
    }

    public static int reduceDamage(int n, int n2) {
        if (n2 <= 0) {
            return 0;
        }
        if (n <= 0) {
            return n2;
        }
        long l = (long)n * 0x100000L / 384000L;
        if (l >= 0x100000L) {
            return 1;
        }
        long l2 = 0x100000L;
        long l3 = 0x100000L - l;
        for (int i = 0; i < 64; ++i) {
            l2 = l2 * l3 >> 20;
        }
        int n3 = (int)((long)n2 * l2 >> 20);
        return n3 < 1 ? 1 : n3;
    }

    private static int baseAttack(byte by) {
        EquipGlass equipGlass = PlayerEquip.getEquipGlass(by);
        if (equipGlass != null && equipGlass.maxDamage > 0) {
            return equipGlass.maxDamage;
        }
        int n = by & 0xFF;
        return n < BASE_ATTACK.length ? BASE_ATTACK[n] : 280;
    }
}

