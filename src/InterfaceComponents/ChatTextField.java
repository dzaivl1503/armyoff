/*
 * Decompiled with CFR 0.152.
 */
package InterfaceComponents;

import CLib.TField;
import CLib.mGraphics;
import coreLG.CCanvas;

public class ChatTextField {
    public TField tfChat;
    public static ChatTextField instance;
    public static boolean isShow;
    byte typeTF;

    public static ChatTextField gI() {
        return instance == null ? (instance = new ChatTextField()) : instance;
    }

    public void setChat(byte by) {
        isShow = !isShow;
        this.typeTF = by;
        if (isShow) {
            this.tfChat.setPoiter();
        }
    }

    public void commandTab(int n, int n2) {
        switch (n) {
            case 0: {
                this.tfChat.setText("");
                isShow = false;
                if (CCanvas.isTouch) break;
                this.tfChat.setFocus(true);
                break;
            }
            case 1: {
                this.sendChat();
            }
        }
    }

    protected ChatTextField() {
    }

    public void init() {
        this.tfChat.width = CCanvas.hw;
    }

    public void keyPressed(int n) {
        this.tfChat.keyPressed(n);
    }

    public void updatekey() {
        this.tfChat.update();
    }

    public void paint(mGraphics mGraphics2) {
        this.tfChat.paint(mGraphics2);
    }

    public void updatePointer() {
    }

    public void sendChat() {
        isShow = false;
    }

    static {
        isShow = false;
    }
}

