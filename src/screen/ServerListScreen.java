/*
 * Decompiled with CFR 0.152.
 */
package screen;

import CLib.mGraphics;
import com.teamobi.mobiarmy2.GameMidlet;
import coreLG.CCanvas;
import model.CRes;
import model.Font;
import model.GetString;
import model.IAction;
import model.Language;
import network.Command;
import network.GameService;
import network.Session_ME;
import screen.CScreen;
import screen.LoginScr;

public class ServerListScreen
extends CScreen {
    public static String[] nameServer;
    public static String[] address;
    public static boolean[] newServer;
    public static short[] port;
    public int selected;
    public int yPaint = 0;

    public ServerListScreen() {
        this.indexScreen = 1;
        this.nameCScreen = " ServerListScreen screen!";
        this.center = new Command(Language.select(), new IAction(){

            public void perform() {
                String string = nameServer[ServerListScreen.this.selected] + ":" + address[ServerListScreen.this.selected] + ":" + port[ServerListScreen.this.selected];
                ServerListScreen.this.OnConnectToServer(string);
                CCanvas.loginScr = new LoginScr();
                CCanvas.loginScr.show();
                Session_ME.gI().start = false;
            }
        });
        this.left = new Command(Language.update(), new IAction(){

            public void perform() {
                nameServer = null;
                GameMidlet.doUpdateServer();
            }
        });
        this.right = new Command(Language.exit(), new IAction(){

            public void perform() {
                GameMidlet.exit();
                System.exit(-1);
            }
        });
    }

    private void OnConnectToServer(String string) {
        string.trim();
        String[] stringArray = CRes.split(string, ":");
        String string2 = stringArray[0].trim().toLowerCase();
        String string3 = stringArray[1];
        String string4 = stringArray[2];
        GameMidlet.IP = string3;
        GameMidlet.PORT = Short.parseShort(string4);
        Session_ME.gI().connect(GameMidlet.IP, GameMidlet.PORT);
        GameMidlet.serverName = stringArray[0];
        if (GameMidlet.isTeamClient) {
            GameService.gI().setProvider(GameMidlet.PROVIDER);
            new GetString();
            GameService.gI().getString("abc");
            GameService.gI().platform_request();
        } else {
            GameMidlet.PROVIDER = (byte)CRes.loadRMSInt("provider");
            GameMidlet.AGENT = CRes.loadRMS_String("agent");
            if (GameMidlet.AGENT == null) {
                GameMidlet.AGENT = "";
            }
            if (GameMidlet.PROVIDER != -1) {
                GameService.gI().setProvider(GameMidlet.PROVIDER);
                GameService.gI().getString(GameMidlet.AGENT);
            }
        }
    }

    public void paint(mGraphics mGraphics2) {
        mGraphics2.setColor(7852799);
        mGraphics2.fillRect(0, 0, w, h, false);
        if (nameServer != null) {
            this.yPaint = CCanvas.hieght / 2 - ITEM_HEIGHT;
            mGraphics2.setColor(16767817);
            mGraphics2.fillRect(0, this.yPaint + this.selected * 20 - 3, CCanvas.width, ITEM_HEIGHT, true);
            Font.borderFont.drawString(mGraphics2, Language.chonmaychu(), CCanvas.width / 2, this.yPaint - ITEM_HEIGHT - 5, 3);
            for (int i = 0; i < nameServer.length; ++i) {
                if (nameServer[i] == null) continue;
                Font.normalFont.drawString(mGraphics2, nameServer[i], CCanvas.width / 2, this.yPaint + i * 20, 2);
            }
        }
        super.paint(mGraphics2);
    }

    public void update() {
        super.update();
    }

    public void onPointerPressed(int n, int n2, int n3) {
        super.onPointerPressed(n, n2, n3);
        if (nameServer != null) {
            if (CCanvas.keyPressed[2]) {
                --this.selected;
                if (this.selected < 0) {
                    this.selected = nameServer.length - 1;
                }
                CScreen.clearKey();
            }
            if (CCanvas.keyPressed[8]) {
                ++this.selected;
                if (this.selected > nameServer.length - 1) {
                    this.selected = 0;
                }
                CScreen.clearKey();
            }
        }
    }

    public void onPointerReleased(int n, int n2, int n3) {
        super.onPointerReleased(n, n2, n3);
        if (nameServer != null) {
            if (!CCanvas.keyPressed[8] && !keyDown) {
                if (CCanvas.keyPressed[2] || keyUp) {
                    ServerListScreen.clearKey();
                    --this.selected;
                    if (this.selected < 0) {
                        this.selected = nameServer.length - 1;
                    }
                }
            } else {
                ServerListScreen.clearKey();
                ++this.selected;
                if (this.selected > nameServer.length - 1) {
                    this.selected = 0;
                }
            }
            if (n2 < CCanvas.hieght - cmdH) {
                int n4 = (n2 - this.yPaint) / 20;
                if (n4 == this.selected && CCanvas.isDoubleClick && this.center != null) {
                    this.center.action.perform();
                }
                if (n4 >= 0 && n4 < nameServer.length) {
                    this.selected = n4;
                }
            }
        }
        GameMidlet.server = (byte)(this.selected >= 3 ? -1 : this.selected);
        if (GameMidlet.versionByte >= 240) {
            GameMidlet.server = (byte)2;
        }
    }

    public void show() {
        super.show();
        GameMidlet.loadIP();
    }
}

