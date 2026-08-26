/*
 * Decompiled with CFR 0.152.
 */
package effect;

import CLib.mGraphics;
import CLib.mImage;
import screen.GameScr;

public class Effect {
    private static int[] clips = new int[4];
    private static mImage s_imgTransparent = GameScr.s_imgTransparent;
    static int[] s_transparentBuf;
    static mImage s_transparentImg;
    static final int TRANSPARENT_BUF_H = 1;
    static final int BACK_IMAGE_HEIGHT = 1;
    static final boolean DRAW_TransparentRect_USE_DrawRGB = false;

    public static void Flash(mGraphics mGraphics2, int n, int n2, int n3, int n4, int n5) {
        int[] nArray = new int[n3 * n4];
        for (int i = 0; i < nArray.length; ++i) {
            nArray[i] = n5 << 24 | 0xFFFFFF;
        }
        mGraphics2.drawRGB(nArray, 0, n3, n, n2, n3, n4, true);
    }

    public static void DrawBlurImage(mGraphics mGraphics2, mImage mImage2, int n, int n2, int n3) {
        if (n3 == 0) {
            mGraphics2.drawImage(mImage2, n, n2, mGraphics.TOP | mGraphics.LEFT, false);
        } else {
            Effect.clips[0] = mGraphics2.getClipX();
            Effect.clips[1] = mGraphics2.getClipY();
            Effect.clips[2] = mGraphics2.getClipWidth();
            Effect.clips[3] = mGraphics2.getClipHeight();
            int n4 = n3 * 2;
            int n5 = mImage2.image.getWidth();
            int n6 = mImage2.image.getHeight();
            int[] nArray = new int[n5 * n6];
            mImage2.getRGB(nArray, 0, n5, 0, 0, n5, n6);
            if (nArray != null) {
                int n7 = Math.max(n, clips[0]);
                int n8 = Math.max(n2, clips[1]);
                int n9 = Math.min(n + n5, clips[0] + clips[2]) - n7;
                int n10 = Math.min(n2 + n6, clips[1] + clips[3]) - n8;
                if (n9 > 0 && n10 > 0) {
                    mGraphics2.setClip(n7, n8, n9, n10);
                    for (int i = 0; i < n6; i += n4) {
                        for (int j = 0; j < n5; j += n4) {
                            if ((i + n4 / 2) * n5 + j + n4 / 2 > nArray.length) continue;
                            mGraphics2.fillRect(n + j, n2 + i, n4, n4, false);
                        }
                    }
                    mGraphics2.setClip(clips[0], clips[1], clips[2], clips[3]);
                }
            }
        }
    }

    public static void FillTransparentRect(mGraphics mGraphics2, int n, int n2, int n3, int n4) {
        Effect.clips[0] = mGraphics2.getClipX();
        Effect.clips[1] = mGraphics2.getClipY();
        Effect.clips[2] = mGraphics2.getClipWidth();
        Effect.clips[3] = mGraphics2.getClipHeight();
        int n5 = Math.max(n, clips[0]);
        int n6 = Math.max(n2, clips[1]);
        int n7 = Math.min(n + n3, clips[0] + clips[2]) - n5;
        int n8 = Math.min(n2 + n4, clips[1] + clips[3]) - n6;
        if (n7 > 0 && n8 > 0) {
            mGraphics2.setClip(n5, n6, n7, n8);
            for (int i = 0; i < n3 / 120 + 1; ++i) {
                for (int j = 0; j < n4 / 40 + 1; ++j) {
                    mGraphics2.drawImage(s_imgTransparent, n + 120 * i, n2 + 40 * j, 0, false);
                }
            }
            mGraphics2.setClip(clips[0], clips[1], clips[2], clips[3]);
        }
    }

    public static void FillTransparentRectRGB(mGraphics mGraphics2, int n, int n2, int n3, int n4, int n5) {
        Effect.clips[0] = mGraphics2.getClipX();
        Effect.clips[1] = mGraphics2.getClipY();
        Effect.clips[2] = mGraphics2.getClipWidth();
        Effect.clips[3] = mGraphics2.getClipHeight();
        int n6 = Math.max(n, clips[0]);
        int n7 = Math.max(n2, clips[1]);
        int n8 = Math.min(n + n3, clips[0] + clips[2]) - n6;
        int n9 = Math.min(n2 + n4, clips[1] + clips[3]) - n7;
        if (n8 > 0 && n9 > 0) {
            mGraphics2.setClip(n6, n7, n8, n9);
            int[] nArray = new int[n3 * n4];
            mGraphics2.setClip(clips[0], clips[1], clips[2], clips[3]);
        }
    }

    private static void fillTransparentRect(mGraphics mGraphics2, int n, int n2, int n3, int n4, int n5, int n6, int n7, int n8, boolean bl) {
        int n9;
        n8 = 255 * n8 / 100;
        if (s_transparentBuf == null || s_transparentBuf.length / 1 < n3) {
            s_transparentBuf = new int[n3 * 1];
        }
        int n10 = s_transparentBuf.length;
        int n11 = (n8 << 24) + (n5 << 16) + (n6 << 8) + n7;
        for (n9 = 0; n9 < n10 && s_transparentBuf[n9] != n11; ++n9) {
            Effect.s_transparentBuf[n9] = n11;
        }
        if (!bl) {
            s_transparentImg = mImage.createImage(s_transparentBuf, n3, 1);
        }
        for (n9 = 0; n9 < n4; ++n9) {
            if (bl) {
                mGraphics2.drawRGB(s_transparentBuf, 0, n3, n, n2 + n9, n3, 1, true);
                continue;
            }
            mGraphics2.drawRegion(s_transparentImg, 0, 0, n3, 1, 0, n, n2 + n9, 0, false);
        }
    }

    public static void fillTransparentRect(mGraphics mGraphics2, int n, int n2, int n3, int n4, int n5, int n6, boolean bl) {
        if (n3 > 0 && n4 > 0) {
            Effect.fillTransparentRect(mGraphics2, n, n2, n3, n4, n5 >> 16 & 0xFF, n5 >> 8 & 0xFF, n5 & 0xFF, n6, bl);
        }
    }

    public static void fillTransparentRect(mGraphics mGraphics2, int n, int n2, int n3, int n4, int n5, int n6) {
        Effect.fillTransparentRect(mGraphics2, n, n2, n3, n4, n5 >> 16 & 0xFF, n5 >> 8 & 0xFF, n5 & 0xFF, n6, false);
    }

    public static void transparentRGB(int[] nArray, int n) {
        n = 255 * n / 100;
        int n2 = nArray.length;
        for (int i = 0; i < n2; ++i) {
            int n3 = i;
            nArray[n3] = nArray[n3] & 0xFFFFFF;
            if (nArray[i] == 0xFF00FF) continue;
            int n4 = i;
            nArray[n4] = nArray[n4] | n << 24;
        }
    }
}

