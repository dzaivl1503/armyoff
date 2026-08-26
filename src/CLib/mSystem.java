/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  javax.microedition.io.Connector
 *  javax.microedition.io.HttpConnection
 */
package CLib;

import CLib.Image;
import CLib.LibSysTem;
import CLib.mGraphics;
import CLib.mImage;
import CLib.mVector;
import com.teamobi.mobiarmy2.TemMidlet;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;
import model.CRes;
import model.IAction2;

public class mSystem {
    public static mImage imgCircle_30;
    public static mImage imgCircle_20;
    public static mImage imgCircle_0;
    public static mImage imgCircle_45;
    public static float deltaTime;
    public static float fixedDeltaTime;
    public static String ww;
    public static boolean isMaHoa;
    public static boolean isIP_TrucTiep;
    public static boolean isIP_GDX;
    public static boolean isj2me;
    public static int dyCharStep;
    public static boolean isImgLocal;
    public static final byte INDEX_SV_GLOBAL = 2;
    public static boolean isIphone;
    public static int ID_REGION;
    public static String[][] listServer;
    public static String[][] listServer_VN;
    public static String[][] listServer_In_Do;
    public static String[][] listServer_Usa;
    public static boolean isOnConnectFail;
    public static boolean isOnConnectOK;
    public static boolean isOnDisconnect;
    public static boolean isOnLoginFail;
    public static String reasonFail;

    public static long currentTimeMillis() {
        return System.currentTimeMillis();
    }

    public static float deltaTime() {
        return fixedDeltaTime;
    }

    public static void my_Gc() {
        System.gc();
    }

    public static int[][] new_M_Int(int n, int n2) {
        int[][] object;
        if (TemMidlet.DIVICE == 2) {
            object = new int[n][];
            for (int i = 0; i < ((int[][])object).length; ++i) {
                object[i] = new int[n2];
            }
        } else {
            object = new int[n][n2];
        }
        return object;
    }

    public static String[][] new_M_String(int n, int n2) {
        String[][] stringArray;
        if (TemMidlet.DIVICE == 2) {
            stringArray = new String[n][];
            for (int i = 0; i < stringArray.length; ++i) {
                stringArray[i] = new String[n2];
            }
        } else {
            stringArray = new String[n][n2];
        }
        return stringArray;
    }

    public static byte[][] new_M_Byte(int n, int n2) {
        byte[][] object;
        if (TemMidlet.DIVICE == 2) {
            object = new byte[n][];
            for (int i = 0; i < ((byte[][])object).length; ++i) {
                object[i] = new byte[n2];
            }
        } else {
            object = new byte[n][n2];
        }
        return object;
    }

    public static byte[][][] new_M_Byte(int n, int n2, int n3) {
        byte[][][] object;
        if (TemMidlet.DIVICE == 2) {
            object = new byte[n][][];
            for (int i = 0; i < ((byte[][][])object).length; ++i) {
                object[i] = mSystem.new_M_Byte(n2, n3);
            }
        } else {
            object = new byte[n][n2][n3];
        }
        return object;
    }

    public static void openUrl(String string) {
        LibSysTem.openWeb(string);
    }

    public static Image createPixmap(String string) {
        try {
            String string2 = "/x" + mGraphics.zoomLevel + string;
            InputStream inputStream = LibSysTem.openResource("/" + LibSysTem.res + string2);
            if (inputStream == null) {
                return null;
            }
            byte[] byArray = mSystem.readAll(inputStream);
            return Image.createImage(byArray, 0, byArray.length);
        }
        catch (Exception exception) {
            System.out.println("KHONG LOAD DC PIXMAP:" + string);
            return null;
        }
    }

    private static byte[] readAll(InputStream inputStream) throws IOException {
        int n;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] byArray = new byte[1024];
        while ((n = inputStream.read(byArray)) != -1) {
            byteArrayOutputStream.write(byArray, 0, n);
        }
        return byteArrayOutputStream.toByteArray();
    }

    public static Image createPixmap(int n, int n2) {
        return Image.createImage(n * mGraphics.zoomLevel, n2 * mGraphics.zoomLevel);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static String connectHTTP(String string) {
        HttpConnection httpConnection = null;
        InputStream inputStream = null;
        try {
            httpConnection = (HttpConnection)Connector.open((String)string);
            inputStream = httpConnection.openInputStream();
            ww = new String(mSystem.readAll(inputStream), "UTF-8");
            CRes.out("Conennect http =======> " + ww);
        }
        catch (Exception exception) {
            CRes.out("Conennect http Fail =======> " + ww);
        }
        finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (httpConnection != null) {
                    httpConnection.close();
                }
            }
            catch (Exception exception) {}
        }
        return ww;
    }

    public static void connectHTTP(final String string, final IAction2 iAction2) {
        new Thread(new Runnable(){

            public void run() {
                String string2 = mSystem.connectHTTP(string);
                if (iAction2 != null) {
                    iAction2.perform(string2);
                }
            }
        }).start();
    }

    public static String[] split(String string, String string2) {
        mVector mVector2 = new mVector();
        int n = string.indexOf(string2);
        while (n >= 0) {
            mVector2.addElement(string.substring(0, n));
            string = string.substring(n + string2.length());
            n = string.indexOf(string2);
        }
        mVector2.addElement(string);
        String[] stringArray = new String[mVector2.size()];
        if (mVector2.size() > 0) {
            for (int i = 0; i < mVector2.size(); ++i) {
                stringArray[i] = (String)mVector2.elementAt(i);
            }
        }
        return stringArray;
    }

    public static void setClientType(byte by, boolean bl) {
        TemMidlet.DIVICE = by;
        isIP_TrucTiep = bl;
    }

    public static String getPackageName() {
        return "";
    }

    public static int getHour() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(11);
    }

    public static int getMinute() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(12);
    }

    public static String getMoth() {
        Calendar calendar = Calendar.getInstance();
        return String.valueOf(calendar.get(2));
    }

    public static String getDayOfMonth() {
        Calendar calendar = Calendar.getInstance();
        return String.valueOf(calendar.get(5));
    }

    public static String getLong() {
        return "";
    }

    public static String getLat() {
        return "";
    }

    public static boolean isHideNaptien() {
        return false;
    }

    public static boolean isIpAppstore() {
        return true;
    }

    public static void doChangeMenuNapapple() {
    }

    public static String getImei() {
        String string = "";
        try {
            string = System.getProperty("com.imei");
            if (string == null || string.equals("null") || string.equals("")) {
                string = System.getProperty("phone.imei");
            }
            if (string == null || string.equals("null") || string.equals("")) {
                string = System.getProperty("com.nokia.IMEI");
            }
            if (string == null || string.equals("null") || string.equals("")) {
                string = System.getProperty("com.nokia.mid.imei");
            }
            if (string == null || string.equals("null") || string.equals("")) {
                string = System.getProperty("com.sonyericsson.imei");
            }
            if (string == null || string.equals("null") || string.equals("")) {
                string = System.getProperty("IMEI");
            }
            if (string == null || string.equals("null") || string.equals("")) {
                string = System.getProperty("com.motorola.IMEI");
            }
            if (string == null || string.equals("null") || string.equals("")) {
                string = System.getProperty("com.samsung.imei");
            }
            if (string == null || string.equals("null") || string.equals("")) {
                string = System.getProperty("com.siemens.imei");
            }
            if (string == null || string.equals("null") || string.equals("")) {
                string = System.getProperty("imei");
            }
        }
        catch (Exception exception) {
            return string == null ? "" : string;
        }
        return string == null ? "" : string;
    }

    public static String getModel() {
        return "";
    }

    public static int getRam() {
        return 0;
    }

    public static void onConnectFail() {
        isOnConnectFail = true;
    }

    public static void onConnectOK() {
        isOnConnectOK = true;
    }

    public static void onDisconnect() {
        isOnDisconnect = true;
    }

    public static void onLoginFail(String string) {
        isOnLoginFail = true;
        reasonFail = string;
    }

    static {
        fixedDeltaTime = 0.02f;
        ww = "";
        isMaHoa = false;
        isIP_TrucTiep = false;
        isIP_GDX = true;
        isj2me = true;
        dyCharStep = 0;
        isImgLocal = true;
        isIphone = true;
        listServer = new String[][]{{"name_Server", "com.teamobi.army2"}, {"name_Server", "com.teamobi.army2"}, {"name_Server", "com.teamobi.army2"}, {"name_Server", "com.teamobi.army2"}, {"name_Server", "com.teamobi.army2"}, {"name_Server", "com.teamobi.army2"}};
        listServer_VN = new String[][]{{"name_Server", "com.teamobi.army2"}, {"name_Server", "com.teamobi.army2"}, {"Global Server", "com.teamobi.army2"}, {"name_Server", "com.teamobi.army2"}, {"name_Server", "com.teamobi.army2"}, {"name_Server", "com.teamobi.army2"}};
        listServer_In_Do = new String[][]{{"Indo Naga", "com.teamobi.army2"}, {"Indo Garuda(new)", "com.teamobi.army2"}, {"Knight Age (ENG)", "com.teamobi.army2"}};
        listServer_Usa = new String[][]{{"Fire Dragon (ENG)", "46.137.254.172"}, {"Sky Dragon (SPN)", "54.254.156.202"}, {"Knight Age (ENG)", "com.teamobi.army2"}};
    }
}

