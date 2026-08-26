/*
 * Decompiled with CFR 0.152.
 */
package screen;

import CLib.mGraphics;
import com.teamobi.mobiarmy2.OfflineSave;
import coreLG.CCanvas;
import coreLG.TerrainMidlet;
import effect.Cloud;
import item.Item;
import model.Font;
import model.IAction;
import model.Language;
import model.PlayerInfo;
import network.Command;
import screen.CScreen;
import screen.PrepareScr;
import shop.ShopItem;

public class ItemLoadoutScr
extends CScreen {
    private CScreen lastScr;
    private int selectedSlot;
    private final int[] workingItems = new int[8];
    private static final int SLOT_CELL = 20;
    private static final int SLOT_COLS = 4;
    private boolean pickerFocused;

    public ItemLoadoutScr() {
        this.nameCScreen = "ItemLoadoutScr screen!";
        this.left = new Command("Xo\u00e1 \u00f4", new IAction(){

            public void perform() {
                ItemLoadoutScr.this.clearSelectedSlot();
            }
        });
        this.center = new Command("G\u00e1n", new IAction(){

            public void perform() {
                ItemLoadoutScr.this.assignSelectedItem();
            }
        });
        this.right = new Command("Xong", new IAction(){

            public void perform() {
                ItemLoadoutScr.this.doClose();
            }
        });
    }

    public void show(CScreen cScreen) {
        this.lastScr = cScreen;
        this.selectedSlot = 0;
        this.pickerFocused = false;
        this.loadFromLoadout();
        super.show();
    }

    private void loadFromLoadout() {
        PlayerInfo playerInfo = TerrainMidlet.myInfo;
        if (playerInfo == null) {
            return;
        }
        byte by = playerInfo.gun;
        if (by < 0 || by >= playerInfo.itemLoadout.length) {
            return;
        }
        for (int i = 0; i < this.workingItems.length && i < playerInfo.itemLoadout[by].length; ++i) {
            this.workingItems[i] = playerInfo.itemLoadout[by][i];
        }
    }

    private void saveToLoadout() {
        PlayerInfo playerInfo = TerrainMidlet.myInfo;
        if (playerInfo == null) {
            return;
        }
        byte by = playerInfo.gun;
        if (by < 0 || by >= playerInfo.itemLoadout.length) {
            return;
        }
        for (int i = 0; i < this.workingItems.length && i < playerInfo.itemLoadout[by].length; ++i) {
            playerInfo.itemLoadout[by][i] = this.workingItems[i];
        }
        OfflineSave.save();
    }

    private void clearSelectedSlot() {
        if (this.selectedSlot < 0 || this.selectedSlot >= this.workingItems.length) {
            return;
        }
        this.workingItems[this.selectedSlot] = -2;
        this.saveToLoadout();
    }

    private void assignSelectedItem() {
        if (this.selectedSlot < 0 || this.selectedSlot >= this.workingItems.length) {
            return;
        }
        Item item = ShopItem.getI(PrepareScr.prepareScrItemIcon.select);
        if (item == null || item.isPassive_Item) {
            return;
        }
        if (item.num <= 0 && !item.isFreeItem) {
            CCanvas.startOKDlg(Language.empty());
            return;
        }
        if (item.numUsed >= item.nCurMaxUsed) {
            CCanvas.startOKDlg(Language.chicothe() + item.nCurMaxUsed + Language.itemnay());
            return;
        }
        for (int i = 0; i < this.workingItems.length; ++i) {
            if (i == this.selectedSlot || this.workingItems[i] != item.type) continue;
            CCanvas.startOKDlg("V\u1eadt ph\u1ea9m n\u00e0y \u0111\u00e3 \u0111\u01b0\u1ee3c g\u00e1n v\u00e0o \u00f4 kh\u00e1c.");
            return;
        }
        this.workingItems[this.selectedSlot] = item.type;
        this.saveToLoadout();
    }

    private void doClose() {
        CCanvas.endDlg();
        this.lastScr.show();
    }

    public void update() {
        Cloud.updateCloud();
        PrepareScr.prepareScrItemIcon.mainLoop();
    }

    private int slotAreaX() {
        return w / 2 - 40;
    }

    private int pickerX() {
        return w / 2 - PrepareScr.prepareScrItemIcon.getWidth() / 2;
    }

    private int titleY() {
        return (h >> 1) - 110;
    }

    private int slotY() {
        return (h >> 1) - 80;
    }

    private int pickerY() {
        int n;
        int n2;
        int n3 = this.slotY() + 60;
        if (n3 > (n2 = CCanvas.hieght - cmdH - (n = CCanvas.isTouch ? 138 : 118) - 2)) {
            n3 = n2;
        }
        return n3;
    }

    public void paint(mGraphics mGraphics2) {
        ItemLoadoutScr.paintDefaultBg(mGraphics2);
        Cloud.paintCloud(mGraphics2);
        PlayerInfo playerInfo = TerrainMidlet.myInfo;
        String string = playerInfo != null && playerInfo.gun >= 0 && playerInfo.gun < PrepareScr.GUN_NAME.length ? PrepareScr.GUN_NAME[playerInfo.gun] : "";
        Font.bigFont.drawString(mGraphics2, string, w / 2, this.titleY(), 2);
        int n = this.slotAreaX();
        int n2 = this.slotY();
        Item.DrawSetItem(mGraphics2, this.workingItems, this.selectedSlot, n, n2, false, null);
        int n3 = this.pickerY();
        ItemLoadoutScr.paintBorderRect(mGraphics2, n3, CCanvas.isTouch ? 4 : 3, CCanvas.isTouch ? 138 : 118, Language.chonItem());
        PrepareScr.prepareScrItemIcon.paint(this.pickerX(), n3 + 25, mGraphics2, true, ShopItem.getItemNum());
        PrepareScr.prepareScrItemIcon.setPosTitle(w / 2, n3 + 3);
        Item item = ShopItem.getI(PrepareScr.prepareScrItemIcon.select);
        if (item != null) {
            Font.normalFont.drawString(mGraphics2, item.decription, w / 2, n3 + (CCanvas.isTouch ? 115 : 95), 3);
        }
        mGraphics2.setColor(0xFFFFFF);
        super.paint(mGraphics2);
    }

    public void onPointerPressed(int n, int n2, int n3) {
        super.onPointerPressed(n, n2, n3);
        boolean bl = CCanvas.keyPressed[2];
        boolean bl2 = CCanvas.keyPressed[8];
        boolean bl3 = CCanvas.keyPressed[4];
        boolean bl4 = CCanvas.keyPressed[6];
        if (!this.pickerFocused) {
            if (bl || bl2 || bl3 || bl4) {
                int n4 = this.selectedSlot / 4;
                int n5 = this.selectedSlot % 4;
                int n6 = (this.workingItems.length - 1) / 4;
                if (bl3) {
                    n5 = (n5 - 1 + 4) % 4;
                }
                if (bl4) {
                    n5 = (n5 + 1) % 4;
                }
                if (bl && n4 > 0) {
                    --n4;
                }
                if (bl2) {
                    if (n4 >= n6) {
                        this.pickerFocused = true;
                    } else {
                        ++n4;
                    }
                }
                if (!this.pickerFocused) {
                    this.selectedSlot = n4 * 4 + n5;
                    if (this.selectedSlot >= this.workingItems.length) {
                        this.selectedSlot = this.workingItems.length - 1;
                    }
                }
                ItemLoadoutScr.clearKey();
            }
            return;
        }
        if (bl && PrepareScr.prepareScrItemIcon.select < PrepareScr.prepareScrItemIcon.getIndexW()) {
            this.pickerFocused = false;
            ItemLoadoutScr.clearKey();
            return;
        }
        PrepareScr.prepareScrItemIcon.onPointerPressed(n, n2, n3);
    }

    public void onPointerDragged(int n, int n2, int n3) {
        super.onPointerDragged(n, n2, n3);
        PrepareScr.prepareScrItemIcon.onPointerDragged(n, n2, n3);
    }

    public void onPointerReleased(int n, int n2, int n3) {
        int n4;
        super.onPointerReleased(n, n2, n3);
        PrepareScr.prepareScrItemIcon.onPointerReleased(n, n2, n3);
        int n5 = this.slotAreaX();
        int n6 = this.slotY();
        if (CCanvas.isPointer(n5, n6, 80, 40, n3) && (n4 = (n2 - n6) / 20 * 4 + (n - n5) / 20) >= 0 && n4 < this.workingItems.length) {
            this.selectedSlot = n4;
        }
        ItemLoadoutScr.clearKey();
    }
}

