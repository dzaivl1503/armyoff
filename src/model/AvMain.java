/*
 * Decompiled with CFR 0.152.
 */
package model;

import CLib.mFont;
import CLib.mGraphics;
import CLib.mImage;
import coreLG.CCanvas;
import model.FrameImage;

public class AvMain {
    public static final byte COLOR_WHITE = 0;
    public static final byte COLOR_GREEN = 1;
    public static final byte COLOR_PURPLE = 2;
    public static final byte COLOR_ORANGE = 3;
    public static final byte COLOR_BLUE = 4;
    public static final byte COLOR_YELLOW = 5;
    public static final byte COLOR_RED = 6;
    public static final byte COLOR_BLACK = 7;
    public static final byte COLOR_BROWN = 8;
    public static final int color_black = -15463396;
    public static final int color_black_light = -12698050;
    public static final int color_white = -855308;
    public static final int color_gray_dark_bg = -12502207;
    public static final int color_gray_bg = -11646386;
    public static final int color_gray_light_bg = -8948105;
    public static final int color_red_dark_bg = -12311500;
    public static final int color_red = -1685698;
    public static final int color_yellow_dark = -1392637;
    public static final int color_yellow_lemon = -2436002;
    public static final int color_brown = -5281755;
    public static final int color_brown_yellow = -805042;
    public static final int color_brown_light = -6924746;
    public static final int color_brown_dark = -8041424;
    public static final int color_purple_dark = -11063742;
    public static final int color_purple_violet = -6998324;
    public static final int color_blue = -12953632;
    public static final int color_orange = -1546729;
    public static final int color_orange_dark_bg = -2982612;
    public static final int color_green_dark = -13990400;
    public static final int color_green_light = -8329666;
    public static final int color_gold = -1127168;
    public static final int color_yellow = -4616367;
    public static final int color_yellow_bg_select = -3756155;
    public static final int color_brown_light_bg_select = -8300224;
    public static final int color_brown_bg_select = -10928849;
    public static final int color_brown_dark_bg_select = -13359589;
    public static final int color_coffee_light_bg_select = -1400237;
    public static final int color_coffee_bg_select = -3308998;
    public static final int color_coffee_dark_bg_select = -9944034;
    public static final int color_item_white = -789515;
    public static final int color_item_green = -10755328;
    public static final int color_item_purple = -6597485;
    public static final int color_item_orange = -3898817;
    public static final int color_item_blue = -12025137;
    public static final int color_item_yellow = -5790905;
    public static final int color_item_red = -2537914;
    public static final int color_item_black = -14737890;
    public static final int color_font_item_white = -789515;
    public static final int color_font_item_green = -10755328;
    public static final int color_font_item_purple = -4881665;
    public static final int color_font_item_orange = -1535687;
    public static final int color_font_item_blue = -10771969;
    public static final int color_font_item_yellow = -1844169;
    public static final int color_font_item_red = -42149;
    public static final int color_font_item_black = -14079960;
    public static final int color_font_item_brown = -11919604;
    public static final int color_hp_mon_0 = -16399358;
    public static final int color_hp_mon_1 = -12002232;
    public static final int color_hp_mon_2 = -10298782;
    public static final int color_hp_mon_3 = -7675510;
    public static final int color_hp_mon_4 = -4592711;
    public static final int color_hp_mon_5 = -65794;
    public static int[] sizeUpgradeEff = new int[]{2, 1, 1};
    public static int[][] colorUpgradeEffect = new int[][]{{-1, -1776411, -3289393, -5066061, -7105388, -8882056}, {-16715264, -16718592, -16724992, -16731392, -16738048, -16744448}, {-3276545, -4718363, -6094644, -7405389, -8781671, -10092416}, {-33024, -1740032, -3381760, -5023488, -6730752, -8372224}, {-16740097, -16743707, -16749108, -16752717, -16756071, -16759680}, {-1024, -1714176, -4142080, -5000960, -6713344, -8486912}, {16711705, 15007767, 13369364, 11730962, 0x99000F, 0x80000D}};
    public static int wimg;
    public static mImage[] imghitScr;
    public static mImage[] imgTab;
    public static mImage imgSelected_hand;
    public static mImage imgHpBar_0;
    public static mImage imgHpBar_1;
    public static mImage imgHpBar_2;
    public static mImage imgFocus;
    public static mImage imgFocus_rec;
    public static mImage imgDelay;
    public static mImage imgItem_bound;
    public static mImage imgBoard;
    public static mImage imgBg_corner;
    public static mImage imgMain_Corner;
    public static mImage imgVS;
    public static mImage imgDelay4;
    public static mImage imgRank2;
    public static mImage imgTicket;
    public static mImage imgMax;
    public static FrameImage fraPk;
    public static FrameImage fraFogetPass;
    public static FrameImage fraTextField;
    public static FrameImage fraTextField1;
    public static FrameImage imgLoadImage;
    public static FrameImage frabtn_vuong0;
    public static FrameImage frabtn_vuong1;
    public static FrameImage fra_Money;
    public static FrameImage fraQuest;
    public static FrameImage fraDelay;
    public static FrameImage fraDelay2;
    public static FrameImage fraStarIcon;
    public static FrameImage fraHPboss;
    public static FrameImage fraHPboss_1;
    public static FrameImage fraHPMon;
    public static FrameImage fraIconNpc;
    public static FrameImage fraPlusSkillPoint;
    public static FrameImage fraResist;
    public static FrameImage fraAura;
    public static FrameImage fraSmallArrow;
    public static FrameImage fraRank;
    public static FrameImage fraCorner;
    public static FrameImage fra_PVE_Bar_0;
    public static FrameImage fra_PVE_Bar_1;
    public static FrameImage fraDOT;
    public static FrameImage fraNoti;
    public static FrameImage fraBg_info;
    public static FrameImage fraSkull;
    public static FrameImage fraIconHoatDong;
    public static FrameImage frabtn_vuong_1_0;
    public static FrameImage frabtn_vuong_1_1;
    public static FrameImage fraIdClass;
    public static final int SELECTED_COLOR = -9930353;
    public static int x_cmdLeft;
    public static int y_cmdLeft;
    public static int x_cmdRight;
    public static int y_cmdRight;
    public static int x_cmdCenter;
    public static int y_cmdCenter;
    public static int cmx_tabname;
    public static int cmx_tabnamemax;
    public static int cmx_tabnamemax_old;
    public static int count_cmx_tabname;
    public static long time_cmx_tabname;
    FrameImage fraWarning;
    public static final byte stt_online = 0;
    public static final byte stt_off = 1;
    public static int[] color;

    public AvMain() {
        x_cmdLeft = 1;
    }

    public void paint(mGraphics mGraphics2) {
        CCanvas.resetTrans(mGraphics2);
        this.paintCmd(mGraphics2);
    }

    public static void paintLoadImg(mGraphics mGraphics2, int n, int n2, int n3, int n4) {
        if (n >= 0) {
            imgLoadImage.drawFrame(n, n2, n3, 0, n4, mGraphics2);
        } else {
            imgLoadImage.drawFrame(CCanvas.gameTick / 2 % AvMain.imgLoadImage.nFrame, n2, n3, 0, n4, mGraphics2);
        }
    }

    public void update() {
        AvMain.update_cmx_TabName(cmx_tabnamemax);
    }

    public void keypress(int n) {
    }

    public void paintCmd(mGraphics mGraphics2) {
    }

    public void paintCmd_OnlyText(mGraphics mGraphics2) {
        if (CCanvas.currentDialog == null) {
            CCanvas.resetTrans(mGraphics2);
            if (CCanvas.isSmallScreen) {
                this.paintCmd_OnlyText_Small(mGraphics2);
            }
        }
    }

    public void paintCmd_OnlyText_Small(mGraphics mGraphics2) {
    }

    public void commandTab(int n, int n2) {
    }

    public void commandMenu(int n, int n2) {
    }

    public void commandPointer(int n, int n2) {
    }

    public static void paintRect_Item(mGraphics mGraphics2, int n, int n2, int n3, int n4, int n5, boolean bl, int n6, int[] nArray) {
    }

    public static void paintRect_Item(mGraphics mGraphics2, int n, int n2, int n3, int n4, int n5, boolean bl, int n6) {
        int[] nArray = new int[]{-10928849, -8300224, -13359589};
        if (bl) {
            nArray = new int[]{-3308998, -1400237, -9944034};
        }
    }

    public static void paintRect(mGraphics mGraphics2, int n, int n2, int n3, int n4, boolean bl) {
    }

    public static void paintTab_menu(mGraphics mGraphics2, int n, int n2, int n3, int n4, boolean bl) {
    }

    public static void paint_MainTab(mGraphics mGraphics2, int n, int n2, int n3, int n4, int n5, int n6, int n7, boolean bl) {
        int n8;
        int n9;
        int n10 = mImage.getImageHeight(AvMain.imgTab[2].image);
        int n11 = mImage.getImageWidth(AvMain.imgTab[2].image);
        int n12 = mImage.getImageHeight(AvMain.imgTab[7].image);
        int n13 = mImage.getImageWidth(AvMain.imgTab[7].image);
        int n14 = mImage.getImageWidth(AvMain.imgTab[1].image);
        int n15 = mImage.getImageHeight(AvMain.imgTab[1].image);
        int n16 = mImage.getImageHeight(AvMain.imgTab[3].image);
        if (bl) {
            mGraphics2.setColor(-11646386);
        } else {
            mGraphics2.setColor(-2982612);
        }
        int n17 = 2;
        int n18 = n3 - 2 * n17;
        int n19 = n4 - 2 * n17;
        int n20 = n18 / n11;
        int n21 = n19 / n11;
        for (n9 = 0; n9 < n20; ++n9) {
            n8 = n + n17 + n9 * n11;
        }
        for (n9 = 0; n9 < n21; ++n9) {
            n8 = n2 + n17 + n9 * n11;
        }
    }

    public static void paintTabName(mGraphics mGraphics2, int n, int n2, int n3, String string, int n4) {
    }

    public static void update_cmx_TabName(int n) {
        if (cmx_tabnamemax_old != cmx_tabnamemax) {
            cmx_tabnamemax_old = cmx_tabnamemax;
            AvMain.reset_cmx_tabname();
        }
        if (n > 0) {
            if (cmx_tabname >= cmx_tabnamemax) {
                if (++count_cmx_tabname > 50) {
                    AvMain.reset_cmx_tabname();
                }
            } else if (time_cmx_tabname - CCanvas.timeNow < 0L) {
                ++cmx_tabname;
            }
        }
    }

    public static void reset_cmx_tabname() {
        cmx_tabname = 0;
        count_cmx_tabname = 0;
        time_cmx_tabname = CCanvas.timeNow + 1500L;
    }

    public static void paint_LinkTab(mGraphics mGraphics2, int n, int n2, int n3, int n4, int n5) {
        mGraphics2.setColor(-15463396);
        for (int i = 0; i < n5; ++i) {
            int n6;
            if (n4 == 0) {
                n6 = n + i * n3;
            }
            if (n4 != 0) {
                n6 = n2 + i * n3;
            }
            int n7 = n4 == 0 ? 0 : 4;
            mImage mImage2 = imgTab[3];
            if (n7 != 4) continue;
            mImage2 = imgTab[17];
        }
    }

    public static void paintbutton(mGraphics mGraphics2, int n, int n2, int n3, int n4, int n5) {
        AvMain.paintbutton_vuong(mGraphics2, n5, n, n2, n3, n4);
    }

    public static void paintbutton_vuong(mGraphics mGraphics2, int n, int n2, int n3, int n4, int n5) {
        FrameImage frameImage;
        FrameImage frameImage2 = n == 0 ? frabtn_vuong0 : frabtn_vuong1;
        FrameImage frameImage3 = frameImage = n == 0 ? frabtn_vuong_1_0 : frabtn_vuong_1_1;
        if (n4 < frameImage2.frameWidth * 2) {
            n4 = frameImage2.frameWidth * 2;
        }
        if (n5 < frameImage2.frameWidth * 2) {
            n5 = frameImage2.frameWidth * 2;
        }
        int n6 = n4 - frameImage2.frameWidth * 2;
        int n7 = n5 - frameImage2.frameHeight * 2;
        if (n6 > 0 || n7 > 0) {
            int n8;
            int n9 = n6 / frameImage.frameWidth;
            if (n6 % frameImage.frameWidth > 0) {
                ++n9;
            }
            for (n8 = 0; n8 < n9; ++n8) {
            }
            if (n7 > 0) {
                n8 = n7 / frameImage.frameHeight;
                if (n7 % frameImage.frameHeight > 0) {
                    ++n8;
                }
                for (int i = 0; i < n8; ++i) {
                }
            }
        }
    }

    public static void fill(int n, int n2, int n3, int n4, int n5, mGraphics mGraphics2) {
        mGraphics2.setColor(n5);
    }

    public static void paintRectText(mGraphics mGraphics2, int n, int n2, int n3, int n4, boolean bl) {
        int n5;
        int n6 = AvMain.fraTextField.frameWidth;
        int n7 = AvMain.fraTextField.frameHeight;
        int n8 = n3 - 2 * n6;
        int n9 = n4 - 2 * n7;
        mGraphics2.setColor(-8041424);
        int n10 = n8 / n6;
        if (n8 % n6 > 0) {
            ++n10;
        }
        for (n5 = 0; n5 < n10; ++n5) {
        }
        n5 = n9 / n7;
        if (n9 % n7 > 0) {
            ++n5;
        }
        for (int i = 0; i < n5; ++i) {
        }
    }

    public void updatekey() {
    }

    public void updatekeyPC() {
    }

    public void updatekeyPCForTField() {
    }

    public void updatePointer() {
        boolean bl = CCanvas.isTouch;
    }

    public static void Font3dWhite(mGraphics mGraphics2, String string, int n, int n2, int n3) {
    }

    public static void Font3dColor(mGraphics mGraphics2, String string, int n, int n2, int n3, byte by) {
    }

    public static void Font3dColorAndColor(mGraphics mGraphics2, String string, int n, int n2, int n3, byte by, byte by2) {
    }

    public static void FontBorderColor(mGraphics mGraphics2, String string, int n, int n2, int n3, int n4) {
    }

    public static int getColor_ItemBg(int n) {
        switch (n) {
            case 1: {
                return -13990400;
            }
            case 2: {
                return -6597485;
            }
            case 3: {
                return -3898817;
            }
            default: {
                return -11646386;
            }
            case 5:
        }
        return -5790905;
    }

    public static byte getColor_Item_Upgrade(short s) {
        switch (s) {
            case 0:
            case 1: {
                return 0;
            }
            case 2:
            case 3: {
                return 4;
            }
            case 4:
            case 5: {
                return 5;
            }
            case 6:
            case 7: {
                return 2;
            }
            case 8:
            case 9:
            case 10: {
                return 3;
            }
        }
        return 6;
    }

    public static int getColor(int n) {
        switch (n) {
            case 0: {
                return -855308;
            }
            case 1: {
                return -8329666;
            }
            case 2: {
                return -6998324;
            }
            case 3: {
                return -1546729;
            }
            case 4: {
                return -12953632;
            }
            case 5: {
                return -2436002;
            }
            case 6: {
                return -1685698;
            }
            case 7: {
                return -15463396;
            }
            case 8: {
                return -5281755;
            }
        }
        return -6998324;
    }

    public static mFont setTextColor(int n, int n2) {
        return mFont.tahoma_7b_white;
    }

    public static int resetSelect(int n, int n2, boolean bl) {
        if (n < 0) {
            n = bl ? n2 : 0;
        } else if (n > n2) {
            n = bl ? 0 : n2;
        }
        return n;
    }

    public void paintWarning(mGraphics mGraphics2, int n, int n2, int n3) {
        if (this.fraWarning != null) {
            int n4 = n3 / this.fraWarning.frameWidth;
            if (n3 % this.fraWarning.frameWidth != 0) {
                ++n4;
            }
            for (int i = 0; i < n4; ++i) {
                int n5 = CCanvas.gameTick / 3 % this.fraWarning.nFrame;
                int n6 = n - n3 / 2 + i * this.fraWarning.frameWidth;
                int n7 = n2 - this.fraWarning.frameHeight / 3;
                if (i != n4 - 1) continue;
                n6 = n - n3 / 2 + n3 - this.fraWarning.frameWidth;
            }
        }
    }

    public static void paintIconString(mGraphics mGraphics2, String string, mFont mFont2, FrameImage frameImage, int n, int n2, int n3, int n4, int n5, int n6, mFont mFont3) {
        try {
            if (frameImage == null) {
                return;
            }
            int n7 = n3;
            if (n5 == 1) {
                n7 = n3 - (frameImage.frameWidth + mFont2.getWidth(string) + n6);
            } else if (n5 == 2) {
                n7 = n3 - (frameImage.frameWidth + mFont2.getWidth(string) + n6) / 2;
            }
            int n8 = n7 + frameImage.frameWidth + n6;
            int n9 = n2 + CCanvas.gameTick / 3 % n;
            frameImage.drawFrame(n9, n7, n4, 0, mGraphics.LEFT | mGraphics.TOP, mGraphics2);
            int n10 = n4 + (frameImage.frameHeight - mFont2.getHeight()) / 2 + 1;
        }
        catch (Exception exception) {
        }
    }

    public static int getRankImg(int n) {
        int n2 = 3;
        if (n > 0 && n < 4) {
            n2 = n - 1;
        }
        return n2;
    }

    public static void paintIconRank(mGraphics mGraphics2, int n, int n2, int n3) {
    }

    public static int getColor_Rank(int n) {
        switch (n) {
            case 1: {
                return -2537914;
            }
            case 2: {
                return -6597485;
            }
            case 3: {
                return -5790905;
            }
        }
        return -11646386;
    }

    public static mImage getImage(String string, String string2) {
        mImage mImage2 = null;
        return mImage2;
    }

    public static FrameImage getFraImage(String string, String string2, int n) {
        FrameImage frameImage = null;
        Object var4_4 = null;
        return frameImage;
    }

    public static void paintImgDeley(mGraphics mGraphics2, int n, int n2, int n3, int n4) {
    }

    public static void paintDeley(mGraphics mGraphics2, int n, int n2, int n3, int n4) {
        mGraphics2.setClip(n, n2, n3, n4);
        mGraphics2.saveCanvas();
        mGraphics2.ClipRec(n, n2, n3, n4);
        int n5 = 16;
        int n6 = n3 / n5 + (n3 % n5 > 0 ? 1 : 0);
        int n7 = n4 / n5 + (n4 % n5 > 0 ? 1 : 0);
        for (int i = 0; i < n7; ++i) {
            for (int j = 0; j < n6; ++j) {
                int n8 = n + j * 16;
                n8 = n2 + i * 16;
            }
        }
        mGraphics2.restoreCanvas();
        CCanvas.resetTrans(mGraphics2);
    }

    static {
        imghitScr = new mImage[3];
        imgTab = new mImage[22];
        color = new int[]{-15463396, -855308};
    }
}

