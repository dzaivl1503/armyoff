/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  javax.microedition.lcdui.Display
 *  javax.microedition.midlet.MIDlet
 */
package coreLG;

import CLib.RMS;
import CLib.mSystem;
import com.teamobi.mobiarmy2.GameMidlet;
import com.teamobi.mobiarmy2.TemCanvas;
import coreLG.CCanvas;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import javax.microedition.lcdui.Display;
import javax.microedition.midlet.MIDlet;
import model.CRes;
import model.IAction;
import model.PlayerInfo;
import network.GameLogicHandler;
import network.GameService;
import network.MessageHandler;
import network.Session_ME;
import screen.ServerListScreen;

public class TerrainMidlet {
    public static final String version = "2.4.1";
    public static byte PROVIDER;
    public static final byte BIG_PROVIDER = 0;
    public static TemCanvas temCanvas;
    public static TerrainMidlet instance;
    int a = 0;
    public static int PORT;
    public static String IP;
    public static PlayerInfo myInfo;
    public static String AGENT;
    public static byte filePackVersion;
    public static boolean[] isVip;
    public static boolean isTeamClient;
    public static String linkGetHost;

    public TerrainMidlet() {
        String string;
        if (temCanvas == null) {
            CRes.init();
            temCanvas = new TemCanvas();
            TerrainMidlet.temCanvas.gamecanvas = new CCanvas();
            temCanvas.start();
        }
        TerrainMidlet.temCanvas.gamecanvas.isRunning = true;
        InputStream inputStream = this.getClass().getResourceAsStream("/provider.txt");
        try {
            byte[] byArray = new byte[inputStream.available()];
            inputStream.read(byArray);
            string = new String(byArray, "UTF-8");
            PROVIDER = Byte.parseByte(string);
        }
        catch (Exception exception) {
        }
        string = GameLogicHandler.loadIP();
        if (string != null && string.length() > 0) {
            try {
                int n = string.indexOf(":");
                String string2 = string.substring(0, n);
                String string3 = string.substring(n + 1);
                IP = string2;
                PORT = Integer.parseInt(string3);
            }
            catch (Exception exception) {
                System.err.println("===> error midlet connects " + exception);
            }
        }
        MessageHandler.gI().setGameLogicHandler(GameLogicHandler.gI());
        Session_ME.gI().setHandler(MessageHandler.gI());
        GameService.gI().setSession(Session_ME.gI());
    }

    public static void saveIP() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
        try {
            dataOutputStream.writeByte(ServerListScreen.nameServer.length);
            for (int i = 0; i < ServerListScreen.nameServer.length; ++i) {
                dataOutputStream.writeUTF(ServerListScreen.nameServer[i]);
                dataOutputStream.writeUTF(ServerListScreen.address[i]);
                dataOutputStream.writeShort(ServerListScreen.port[i]);
            }
            try {
                RMS.saveRMS("ipArmy2", byteArrayOutputStream.toByteArray());
            }
            catch (Exception exception) {
                exception.printStackTrace();
            }
            dataOutputStream.close();
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
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

    public static String connectHTTP(String string) {
        return mSystem.connectHTTP(string);
    }

    protected void destroyApp(boolean bl) {
        if (temCanvas != null) {
            TerrainMidlet.temCanvas.gamecanvas.stopGame();
            temCanvas = null;
        }
        this.notifyDestroyed();
    }

    public void showMyCanvas() {
    }

    protected void pauseApp() {
        this.notifyPaused();
    }

    protected void startApp() {
        this.showMyCanvas();
        instance = this;
    }

    public static void exit() {
        instance.destroyApp(false);
    }

    public static void sendSMS(String string, String string2, IAction iAction, IAction iAction2) {
    }

    public static void vibrate(int n) {
        try {
            Display.getDisplay((MIDlet)GameMidlet.instance).vibrate(CRes.abs(n * 10));
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void notifyDestroyed() {
        GameMidlet.instance.notifyDestroyed();
    }

    public void notifyPaused() {
    }

    static {
        PORT = 19152;
        IP = "192.168.1.88";
        isVip = new boolean[11];
        isTeamClient = true;
        linkGetHost = "http://gmb.teamobi.com/srvip/army2list.txt";
    }
}

