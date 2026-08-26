package com.teamobi.mobiarmy2;

import CLib.RMS;
import com.teamobi.mobiarmy2.MotherCanvas;
import coreLG.CCanvas;
import model.IAction;

public final class OfflineSettings {
    private static final String RMS_KEY_DELAY = "offlineSettings_delay";
    private static final String RMS_KEY_FPS = "offlineSettings_fps";
    public static final int[] FPS_OPTIONS = new int[]{15, 25, 30, 45, 60, 90, 120};
    public static final String[] SHORT_LABELS = new String[]{
        "X0.5 (15 FPS)", "X0.8 (25 FPS)", "X1.0 (30 FPS)", "X1.5 (45 FPS)", "X2.0 (60 FPS)", "X3.0 (90 FPS)", "X4.0 (120 FPS)"
    };
    public static final String[] SPEED_LABELS = SHORT_LABELS;
    private static int speedDelay = -1;
    private static int fpsIndex = -1;

    private OfflineSettings() {
    }

    public static int getSpeedDelay() {
        if (speedDelay < 0) {
            int d = RMS.loadRMSInt(RMS_KEY_DELAY);
            if (d >= 1 && d <= 200) {
                speedDelay = d;
            } else {
                speedDelay = 30;
            }
        }
        return speedDelay;
    }

    public static void applySpeedDelay() {
        MotherCanvas.gameDelay = getSpeedDelay();
    }

    public static void saveSpeedDelay(int delay) {
        if (delay < 1) delay = 1;
        if (delay > 200) delay = 200;
        speedDelay = delay;
        RMS.saveRMSInt(RMS_KEY_DELAY, delay);
        applySpeedDelay();
    }

    public static void applyFps() {
        applySpeedDelay();
    }

    public static int getFpsIndex() {
        if (fpsIndex < 0) {
            int n = RMS.loadRMSInt(RMS_KEY_FPS);
            int n2 = -1;
            for (int i = 0; i < FPS_OPTIONS.length; ++i) {
                if (FPS_OPTIONS[i] != n) continue;
                n2 = i;
                break;
            }
            fpsIndex = n2 >= 0 ? n2 : 2;
        }
        return fpsIndex;
    }

    public static void saveFpsIndex(int n) {
        if (n < 0 || n >= FPS_OPTIONS.length) {
            return;
        }
        fpsIndex = n;
        RMS.saveRMSInt(RMS_KEY_FPS, FPS_OPTIONS[n]);
        int[] delays = new int[]{66, 40, 30, 22, 16, 11, 8};
        saveSpeedDelay(delays[n]);
    }

    public static String getCurrentSpeedLabel() {
        int d = getSpeedDelay();
        if (d <= 5) return d + "ms (Max T\u1ed1c \u0110\u1ed9)";
        if (d <= 10) return d + "ms (C\u1ef1c Nhanh)";
        if (d <= 20) return d + "ms (Si\u00eau Nhanh)";
        if (d <= 30) return d + "ms (Nhanh)";
        if (d <= 40) return d + "ms (Chu\u1ea9n)";
        return d + "ms (Ch\u1eadm)";
    }

    public static String nextSpeed() {
        int next = (getFpsIndex() + 1) % FPS_OPTIONS.length;
        saveFpsIndex(next);
        return getCurrentSpeedLabel();
    }

    public static String prevSpeed() {
        int prev = (getFpsIndex() - 1 + FPS_OPTIONS.length) % FPS_OPTIONS.length;
        saveFpsIndex(prev);
        return getCurrentSpeedLabel();
    }

    public static void openSpeedInputPopup() {
        openSpeedInputPopup(null);
    }

    public static void openSpeedInputPopup(final IAction onDone) {
        int current = getSpeedDelay();
        CCanvas.inputDlg.setInfo("\u0110\u1ed9 tr\u1ec5 (1-100 ms) [S\u1ed1 c\u00e0ng nh\u1ecf c\u00e0ng nhanh]:", new IAction() {
            public void perform() {
                try {
                    String text = CCanvas.inputDlg.tfInput.getText();
                    if (text != null && text.trim().length() > 0) {
                        int val = Integer.parseInt(text.trim());
                        saveSpeedDelay(val);
                    }
                } catch (Exception e) {
                    saveSpeedDelay(30);
                } finally {
                    CCanvas.endDlg();
                }
                if (onDone != null) {
                    onDone.perform();
                }
            }
        }, new IAction() {
            public void perform() {
                CCanvas.endDlg();
                if (onDone != null) {
                    onDone.perform();
                }
            }
        }, 1);
        CCanvas.inputDlg.tfInput.setText("" + current);
        CCanvas.inputDlg.show();
    }
}
