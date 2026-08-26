/*
 * Decompiled with CFR 0.152.
 */
package screen;

import CLib.mGraphics;
import com.teamobi.mobiarmy2.GameMidlet;
import com.teamobi.mobiarmy2.OfflineCombat;
import com.teamobi.mobiarmy2.OfflinePvpBot;
import coreLG.CCanvas;
import coreLG.TerrainMidlet;
import effect.Cloud;
import java.util.Vector;
import map.MM;
import map.MapFile;
import model.CRes;
import model.Font;
import model.IAction;
import network.Command;
import screen.CScreen;

public class PvpBotSetupScr
extends CScreen {
    private static final String[] DIFFICULTY_NAMES = new String[]{"D\u1ec5", "Th\u01b0\u1eddng", "Kh\u00f3", "Si\u00eau kh\u00f3"};
    private int[] mapIds = new int[0];
    private String[] mapNames = new String[0];
    private int curMapSlot;
    private int botCount = 1;
    private byte difficulty = 1;
    private int curField;
    private static final int[] SUPER_HARD_MAP_IDS = new int[]{1, 2, 3, 6};

    public PvpBotSetupScr() {
        this.nameCScreen = "PvpBotSetupScr screen!";
        this.left = new Command("Hu\u1ef7", new IAction(){

            public void perform() {
                PvpBotSetupScr.this.doClose();
            }
        });
        this.right = new Command("B\u1eaft \u0111\u1ea7u", new IAction(){

            public void perform() {
                PvpBotSetupScr.this.doStart();
            }
        });
    }

    private static boolean isSuperHardMapAllowed(int n) {
        for (int i = 0; i < SUPER_HARD_MAP_IDS.length; ++i) {
            if (SUPER_HARD_MAP_IDS[i] != n) continue;
            return true;
        }
        return false;
    }

    private void buildMapList() {
        int n;
        int n2;
        Vector<Integer> vector = new Vector<Integer>();
        Vector<String> vector2 = new Vector<String>();
        boolean bl = this.difficulty == 3;
        for (int i = 0; i < MM.mapFiles.size(); ++i) {
            MapFile mapFile = (MapFile)MM.mapFiles.elementAt(i);
            n2 = mapFile.mapID & 0xFF;
            if (n2 >= 30 || mapFile.data == null || mapFile.data.length <= 0 || bl && !PvpBotSetupScr.isSuperHardMapAllowed(n2)) continue;
            vector.addElement(new Integer(n2));
            String string = MM.mapName != null && n2 < MM.mapName.length && MM.mapName[n2] != null ? MM.mapName[n2] : "Map " + n2;
            vector2.addElement(string);
        }
        n = vector.size();
        this.mapIds = new int[n + (n > 0 ? 1 : 0)];
        this.mapNames = new String[this.mapIds.length];
        for (n2 = 0; n2 < n; ++n2) {
            this.mapIds[n2] = (Integer)vector.elementAt(n2);
            this.mapNames[n2] = (String)vector2.elementAt(n2);
        }
        if (n > 0) {
            this.mapIds[n] = -1;
            this.mapNames[n] = "Ng\u1eabu nhi\u00ean";
        }
    }

    public void show(CScreen cScreen) {
        GameMidlet.ensureMapPackLoaded();
        this.buildMapList();
        if (this.curMapSlot >= this.mapIds.length) {
            this.curMapSlot = 0;
        }
        this.curField = 0;
        super.show(cScreen);
    }

    private void doClose() {
        CCanvas.endDlg();
        CCanvas.menuScr.show();
    }

    private void doStart() {
        int n;
        int n2;
        int n3;
        if (this.mapIds.length == 0) {
            CCanvas.startOKDlg("Kh\u00f4ng t\u1ea3i \u0111\u01b0\u1ee3c danh s\u00e1ch map. Th\u1eed l\u1ea1i sau.");
            return;
        }
        int n4 = n3 = TerrainMidlet.myInfo == null ? 1 : 1 + TerrainMidlet.myInfo.getSquadSize();
        if (n3 > this.botCount) {
            CCanvas.startOKDlg("\u0110\u1ed9i h\u00ecnh (" + n3 + " ng\u01b0\u1eddi) \u0111\u00f4ng h\u01a1n s\u1ed1 bot (" + this.botCount + "). Gi\u1ea3m b\u1edbt \u0111\u1ed9i h\u00ecnh ho\u1eb7c t\u0103ng s\u1ed1 bot r\u1ed3i th\u1eed l\u1ea1i.");
            return;
        }
        if (this.difficulty == 3) {
            int n5;
            n2 = OfflinePvpBot.winXuReward(this.difficulty) / 2;
            n = OfflinePvpBot.winLuongReward(this.difficulty) / 2;
            int n6 = TerrainMidlet.myInfo == null ? 0 : TerrainMidlet.myInfo.xu;
            int n7 = n5 = TerrainMidlet.myInfo == null ? 0 : TerrainMidlet.myInfo.luong;
            if (n6 < n2 || n5 < n) {
                CCanvas.startOKDlg("C\u1ea7n c\u00f3 \u00edt nh\u1ea5t " + n2 + " xu v\u00e0 " + n + " l\u01b0\u1ee3ng \u0111\u1ec3 tham gia Si\u00eau kh\u00f3.");
                return;
            }
        }
        if ((n2 = this.mapIds[this.curMapSlot]) == -1) {
            n = this.mapIds.length - 1;
            n2 = this.mapIds[CRes.random(0, n)];
        }
        OfflinePvpBot.pendingMapId = n2;
        OfflinePvpBot.pendingBotCount = this.botCount;
        OfflinePvpBot.pendingDifficulty = this.difficulty;
        GameMidlet.openOfflinePvpBotPrepare();
    }

    private void moveField(int n) {
        this.curField = (this.curField + n + 3) % 3;
    }

    private void moveValue(int n) {
        block7: {
            block8: {
                block9: {
                    block6: {
                        if (this.curField != 0) break block6;
                        if (this.mapIds.length == 0) {
                            return;
                        }
                        this.curMapSlot = (this.curMapSlot + n + this.mapIds.length) % this.mapIds.length;
                        break block7;
                    }
                    if (this.curField != 1) break block8;
                    this.botCount += n;
                    if (this.botCount >= 1) break block9;
                    this.botCount = 4;
                    break block7;
                }
                if (this.botCount <= 4) break block7;
                this.botCount = 1;
                break block7;
            }
            int n2 = this.difficulty + n;
            if (n2 < 0) {
                n2 = DIFFICULTY_NAMES.length - 1;
            } else if (n2 >= DIFFICULTY_NAMES.length) {
                n2 = 0;
            }
            this.difficulty = (byte)n2;
            String string = this.mapIds.length > 0 ? this.mapNames[this.curMapSlot] : null;
            this.buildMapList();
            this.curMapSlot = 0;
            if (string != null) {
                for (int i = 0; i < this.mapNames.length; ++i) {
                    if (!this.mapNames[i].equals(string)) continue;
                    this.curMapSlot = i;
                    break;
                }
            }
        }
    }

    public void onPointerPressed(int n, int n2, int n3) {
        super.onPointerPressed(n, n2, n3);
        if (CCanvas.keyPressed[2]) {
            this.moveField(-1);
        }
        if (CCanvas.keyPressed[8]) {
            this.moveField(1);
        }
        if (CCanvas.keyPressed[4]) {
            this.moveValue(-1);
        }
        if (CCanvas.keyPressed[6]) {
            this.moveValue(1);
        }
        if (CCanvas.isPointerClick[n3]) {
            int n4 = this.rowY(this.curField);
            if (CCanvas.isPointer(w / 2 - 90, n4 - 10, 40, 20, n3)) {
                this.moveValue(-1);
            } else if (CCanvas.isPointer(w / 2 + 50, n4 - 10, 40, 20, n3)) {
                this.moveValue(1);
            } else {
                for (int i = 0; i < 3; ++i) {
                    int n5 = this.rowY(i);
                    if (!CCanvas.isPointer(w / 2 - 90, n5 - 10, 180, 20, n3)) continue;
                    this.curField = i;
                    break;
                }
            }
        }
        PvpBotSetupScr.clearKey();
    }

    private int rowY(int n) {
        return h / 2 - 20 + n * 30;
    }

    public void update() {
        Cloud.updateCloud();
    }

    public void paint(mGraphics mGraphics2) {
        PvpBotSetupScr.paintDefaultBg(mGraphics2);
        Cloud.paintCloud(mGraphics2);
        Font.bigFont.drawString(mGraphics2, "PVP BOT", w / 2, h / 2 - 70, 2);
        String string = this.mapIds.length == 0 ? "(kh\u00f4ng c\u00f3 map)" : this.mapNames[this.curMapSlot];
        this.drawRow(mGraphics2, 0, "Map: " + string);
        this.drawRow(mGraphics2, 1, "S\u1ed1 bot: " + this.botCount + "/" + 4);
        this.drawRow(mGraphics2, 2, "\u0110\u1ed9 kh\u00f3: " + DIFFICULTY_NAMES[this.difficulty]);
        int n = OfflinePvpBot.winXuReward(this.difficulty);
        int n2 = OfflinePvpBot.winLuongReward(this.difficulty);
        int n3 = CCanvas.hieght - cmdH - 34;
        int n4 = OfflineCombat.bonusGemCount(this.botCount, this.difficulty);
        String string2 = "Th\u1eafng: +" + n + " xu +" + n2 + " l\u01b0\u1ee3ng";
        if (n4 > 0) {
            string2 = string2 + " +" + n4 + " ng\u1ecdc c\u1ea5p 9";
        }
        Font.normalRFont.drawString(mGraphics2, string2, w / 2, n3, 2);
        Font.normalRFont.drawString(mGraphics2, "Thua: -" + n + " xu -" + n2 + " l\u01b0\u1ee3ng", w / 2, n3 + 16, 2);
        super.paint(mGraphics2);
    }

    private void drawRow(mGraphics mGraphics2, int n, String string) {
        int n2 = this.rowY(n);
        if (n == this.curField) {
            mGraphics2.setColor(16767817);
            mGraphics2.fillRoundRect(w / 2 - 95, n2 - 11, 190, 22, 8, 8, false);
            Font.borderFont.drawString(mGraphics2, "\u25c4", w / 2 - 85, n2 - 5, 0);
            Font.borderFont.drawString(mGraphics2, "\u25ba", w / 2 + 75, n2 - 5, 0);
        }
        Font.borderFont.drawString(mGraphics2, string, w / 2, n2 - 5, 2);
    }
}

