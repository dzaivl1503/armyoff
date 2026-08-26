/*
 * Decompiled with CFR 0.152.
 */
package model;

import CLib.Image;
import CLib.LibSysTem;
import CLib.RMS;
import CLib.mImage;
import com.teamobi.mobiarmy2.MotherCanvas;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.util.Random;
import model.FilePack;
import model.Position;

public final class CRes {
    private static final boolean DEBUG_LOG = false;
    public static final int COLOR_FOCUS_MENU = 16767817;
    public static final int LIGHT_BLUE = 0x7AAFFF;
    public static final int DARK_BLUE = 84643;
    public static final int LIGHT_BLUE_POUP = 2276847;
    public static final int DARK_BLUE_POUP = 0x1177BB;
    public static final int LIGHT_BLUE_COMMAND = 2263535;
    public static final int DARK_BLUE_COMMAND = 1133755;
    public static final float PI = 3.14f;
    public static Random r = new Random();
    private static short[] sin = new short[]{0, 18, 36, 54, 71, 89, 107, 125, 143, 160, 178, 195, 213, 230, 248, 265, 282, 299, 316, 333, 350, 367, 384, 400, 416, 433, 449, 465, 481, 496, 512, 527, 543, 558, 573, 587, 602, 616, 630, 644, 658, 672, 685, 698, 711, 724, 737, 749, 761, 773, 784, 796, 807, 818, 828, 839, 849, 859, 868, 878, 887, 896, 904, 912, 920, 928, 935, 943, 949, 956, 962, 968, 974, 979, 984, 989, 994, 998, 1002, 1005, 1008, 1011, 1014, 1016, 1018, 1020, 1022, 1023, 1023, 1024, 1024};
    private static short[] cos;
    private static int[] tan;
    public static mImage imgMenu;
    public static mImage imgCam;
    public static mImage imgX;
    public static mImage imgEr;
    public static mImage imgTickGreen;
    public static mImage imgTickGreen_0;
    public static mImage newServer;
    public static mImage empty;
    public static FilePack filePak;
    public static final String TEXT_RESET = "\u001b[0m";
    public static final String TEXT_BLACK = "\u001b[30m";
    public static final String TEXT_RED = "\u001b[31m";
    public static final String TEXT_GREEN = "\u001b[32m";
    public static final String TEXT_YELLOW = "\u001b[33m";
    public static final String TEXT_BLUE = "\u001b[34m";
    public static final String TEXT_PURPLE = "\u001b[35m";
    public static final String TEXT_CYAN = "\u001b[36m";
    public static final String TEXT_WHITE = "\u001b[37m";

    public static void init() {
        cos = new short[91];
        tan = new int[91];
        for (int i = 0; i <= 90; ++i) {
            CRes.cos[i] = sin[90 - i];
            CRes.tan[i] = cos[i] == 0 ? Integer.MAX_VALUE : (sin[i] << 10) / cos[i];
        }
    }

    public static final int sin(int n) {
        if ((n = CRes.fixangle(n)) >= 0 && n < 90) {
            return sin[n];
        }
        if (n >= 90 && n < 180) {
            return sin[180 - n];
        }
        return n >= 180 && n < 270 ? -sin[n - 180] : -sin[360 - n];
    }

    public static final int cos(int n) {
        if ((n = CRes.fixangle(n)) >= 0 && n < 90) {
            return cos[n];
        }
        if (n >= 90 && n < 180) {
            return -cos[180 - n];
        }
        return n >= 180 && n < 270 ? -cos[n - 180] : cos[360 - n];
    }

    public static final int tan(int n) {
        if ((n = CRes.fixangle(n)) >= 0 && n < 90) {
            return tan[n];
        }
        if (n >= 90 && n < 180) {
            return -tan[180 - n];
        }
        return n >= 180 && n < 270 ? tan[n - 180] : -tan[360 - n];
    }

    public static final int atan(int n) {
        for (int i = 0; i <= 90; ++i) {
            if (tan[i] < n) continue;
            return i;
        }
        return 0;
    }

    public static final int angle(int n, int n2) {
        int n3;
        if (n != 0) {
            int n4 = Math.abs((n2 << 10) / n);
            n3 = CRes.atan(n4);
            if (n2 >= 0 && n < 0) {
                n3 = 180 - n3;
            }
            if (n2 < 0 && n < 0) {
                n3 += 180;
            }
            if (n2 < 0 && n >= 0) {
                n3 = 360 - n3;
            }
        } else {
            n3 = n2 > 0 ? 90 : 270;
        }
        return n3;
    }

    public static String formatIntoDDHHMMSS(int n, boolean bl) {
        int n2 = n / 86400;
        int n3 = n % 86400;
        int n4 = n3 / 3600;
        int n5 = (n3 %= 3600) / 60;
        int n6 = n3 % 60;
        if (n6 < 0) {
            n6 = 0;
        }
        if (n5 < 0) {
            n5 = 0;
        }
        if (n4 < 0) {
            n4 = 0;
        }
        return bl ? (n2 > 0 ? n2 + "n " + (n4 < 10 ? "0" : "") : "") + n4 + ":" + (n5 < 10 ? "0" : "") + n5 + ":" + (n6 < 10 ? "0" : "") + n6 : (n2 > 0 ? n2 + " ng\u00e0y " : "") + n4 + " ng\u00e0y.";
    }

    public static int myAngle(int n, int n2) {
        int n3 = CRes.angle(n, n2);
        if (n3 >= 315) {
            n3 = 360 - n3;
        }
        return n3;
    }

    public static final int fixangle(int n) {
        if (n >= 360) {
            n -= 360;
        }
        if (n < 0) {
            n += 360;
        }
        return n;
    }

    public static final int subangle(int n, int n2) {
        int n3 = n2 - n;
        if (n3 < -180) {
            return n3 + 360;
        }
        return n3 > 180 ? n3 - 360 : n3;
    }

    public static int random(int n, int n2) {
        return n + r.nextInt(n2 - n);
    }

    public static int random(int n) {
        return r.nextInt() % n;
    }

    public static boolean isLand(int n) {
        return (n & 0xFF000000) != 0;
    }

    public static boolean inRect(int n, int n2, int n3, int n4, int n5, int n6) {
        return n >= n3 && n < n3 + n5 && n2 >= n4 && n2 < n4 + n6;
    }

    public static boolean isHit(int n, int n2, int n3, int n4, int n5, int n6, int n7, int n8) {
        return n + n3 >= n5 && n <= n5 + n7 && n2 + n4 >= n6 && n2 <= n6 + n8;
    }

    public static int sqrt(int n) {
        int n2;
        if (n <= 0) {
            return 0;
        }
        int n3 = (n + 1) / 2;
        while (Math.abs((n2 = n3) - (n3 = n3 / 2 + n / (2 * n3))) > 1) {
        }
        return n3;
    }

    public static int distance(int n, int n2, int n3, int n4) {
        return CRes.sqrt((n - n3) * (n - n3) + (n2 - n4) * (n2 - n4));
    }

    public static final byte[] loadFile(String string) {
        try {
            int n;
            InputStream inputStream = "".getClass().getResourceAsStream(string);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            while ((n = inputStream.read()) != -1) {
                byteArrayOutputStream.write(n);
            }
            inputStream.close();
            inputStream = null;
            byte[] byArray = byteArrayOutputStream.toByteArray();
            byteArrayOutputStream.close();
            return byArray;
        }
        catch (Exception exception) {
            return null;
        }
    }

    public static final byte[] loadRMSData(String string) {
        try {
            return RMS.loadRMS(string);
        }
        catch (Exception exception) {
            return null;
        }
    }

    public static void saveRMSInt(String string, int n) {
        try {
            RMS.saveRMS(string, new byte[]{(byte)n});
        }
        catch (Exception exception) {
        }
    }

    public static void saveRMS_String(String string, String string2) {
        try {
            RMS.saveRMS(string, string2.getBytes());
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public static String loadRMS_String(String string) {
        byte[] byArray = CRes.loadRMSData(string);
        return byArray == null ? "" : new String(byArray);
    }

    public static int loadRMSInt(String string) {
        try {
            byte[] byArray = CRes.loadRMSData(string);
            return byArray == null ? -1 : byArray[0];
        }
        catch (Exception exception) {
            return -1;
        }
    }

    public static final int byte2int(byte by) {
        return by & 0xFF;
    }

    public static final int getShort(int n, byte[] byArray) {
        return CRes.byte2int(byArray[n]) << 8 | CRes.byte2int(byArray[n + 1]);
    }

    public static void delRMS() {
        try {
            RMS.clearAll();
        }
        catch (Exception exception) {
        }
    }

    public static Position transTextLimit(Position position, int n) {
        position.x += position.y;
        if (position.y == -1 && Math.abs(position.x) > n) {
            position.y *= -1;
        }
        if (position.y == 1 && position.x > 5) {
            position.y *= -1;
        }
        return position;
    }

    public static String getMoneys(int n) {
        String string = "";
        int n2 = n / 1000 + 1;
        for (int i = 0; i < n2; ++i) {
            if (n < 1000) {
                string = n + string;
                break;
            }
            int n3 = n % 1000;
            string = n3 == 0 ? ".000" + string : (n3 < 10 ? ".00" + n3 + string : (n3 < 100 ? ".0" + n3 + string : "." + n3 + string));
            n /= 1000;
        }
        return string;
    }

    public static final void out(String string, String string2) {
    }

    public static final void out(String string) {
    }

    public static final void err(String string) {
    }

    public static boolean CheckDelRMS(String string) {
        return !string.equals("MAIN_user_pass") && !string.equals("MAIN_CONFIG") && !string.equals("MAIN_gameversion") && !string.equals("MAIN_IndexServer") && !string.equals("MAIN_ListServer") && !string.equals("MAIN_LevelScreen") && !string.equals("PLAYER_AUTO_HPMP") && !string.equals("isLowDevice") && !string.equals("PLAYER_AUTO_SKILL");
    }

    public static int abs(int n) {
        return n < 0 ? -n : n;
    }

    public static DataInputStream openFile(String string) {
        return new DataInputStream(LibSysTem.getResourceAsStream(string));
    }

    public static boolean isIosNetwork() {
        return Thread.currentThread().getName() != MotherCanvas.mainThreadName;
    }

    public static String SubStr(String string, int n, int n2) {
        return string.substring(n, n2);
    }

    public static String format(String string, String string2) {
        return CRes.format(string, new String[]{string2});
    }

    public static String format(String string, String[] stringArray) {
        int n = 0;
        int n2 = 0;
        int n3 = string.indexOf("%s");
        if (n3 == -1) {
            return string;
        }
        int n4 = string.length();
        if (stringArray != null) {
            for (int i = 0; i < stringArray.length; ++i) {
                n4 += stringArray[i].length();
            }
        }
        StringBuffer stringBuffer = new StringBuffer(n4);
        while (n3 != -1) {
            stringBuffer.append(string.substring(n2, n3));
            if (stringArray != null && n < stringArray.length) {
                stringBuffer.append(stringArray[n]);
            }
            ++n;
            n2 = n3 + 2;
            n3 = string.indexOf("%s", n2);
        }
        if (n2 < string.length()) {
            stringBuffer.append(string.substring(n2));
        }
        return stringBuffer.toString();
    }

    public static String fixString(String string) {
        return string;
    }

    public static void saveRMSShort(String string, short[] sArray) {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
            for (int i = 0; i < sArray.length; ++i) {
                dataOutputStream.writeShort(sArray[i]);
            }
            RMS.saveRMS(string, byteArrayOutputStream.toByteArray());
            dataOutputStream.close();
        }
        catch (Exception exception) {
        }
    }

    public static byte[] loadRMS(String string) {
        return RMS.loadRMS(string);
    }

    public static void delRMS(String string) {
        RMS.clearRMS(string);
    }

    public static void delAllRms() {
        RMS.clearAll();
    }

    public static void onSaveToFile(String string, String string2) {
    }

    public static void onSaveToFile(Image image, String string, boolean bl) {
    }

    public static void onSaveToFile(Image image, String string, String string2) {
    }

    public static void onSaveToFile(Image image, String string) {
    }

    public static String[] split(String string, String string2) {
        int n = 0;
        int n2 = 0;
        int n3 = string2.length();
        int n4 = string.indexOf(string2, n2);
        while (n4 != -1) {
            n2 = n4 + n3;
            n4 = string.indexOf(string2, n2);
            ++n;
        }
        String[] stringArray = new String[n + 1];
        int n5 = string.indexOf(string2);
        int n6 = 0;
        int n7 = 0;
        while (n5 != -1) {
            stringArray[n7] = string.substring(n6, n5);
            n6 = n5 + n3;
            n5 = string.indexOf(string2, n6);
            ++n7;
        }
        stringArray[n7] = string.substring(n6, string.length());
        return stringArray;
    }

    public static float Lerp(float f, float f2, float f3) {
        return f + f3 * (f2 - f);
    }

    public static float Clamp01(float f) {
        if (f < 0.0f) {
            f = 0.0f;
        } else if (f > 1.0f) {
            f = 1.0f;
        }
        return f;
    }

    public static int Clamp(int n, int n2, int n3) {
        if (n < n2) {
            n = n2;
        } else if (n > n3) {
            n = n3;
        }
        return n;
    }

    public static boolean isNullOrEmpty(String string) {
        boolean bl = false;
        try {
            bl = string == null || string.equals("");
        }
        catch (Exception exception) {
            bl = false;
        }
        return bl;
    }

    static {
        CRes.init();
        try {
            imgMenu = mImage.createImage("/iconmenu.png");
            imgCam = mImage.createImage("/iconcam.png");
            imgX = mImage.createImage("/x.png");
            imgEr = mImage.createImage("/er.png");
            imgTickGreen = mImage.createImage("/tick1.png");
            imgTickGreen_0 = mImage.createImage("/tick0.png");
            newServer = mImage.createImage("/gui/new.png");
            empty = mImage.createImage("/gui/nothing.png");
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}

