/*
 * Decompiled with CFR 0.152.
 */
package screen;

import CLib.mGraphics;
import coreLG.CCanvas;
import model.Font;
import model.IAction;
import model.Language;
import network.Command;
import screen.CScreen;

public class QuangCao
extends CScreen {
    public static String content;
    public static String link;
    public static String[] contents;

    public QuangCao() {
        this.nameCScreen = " QuangCao screen!";
    }

    public void getCommand() {
        contents = Font.borderFont.splitFontBStrInLine(content, CCanvas.width - 30);
        this.right = new Command(Language.exit(), new IAction(){

            public void perform() {
                CCanvas.menuScr.show();
            }
        });
        this.center = new Command("T\u1ea3i game", new IAction(){

            public void perform() {
            }
        });
    }

    public void paint(mGraphics mGraphics2) {
        QuangCao.paintDefaultBg(mGraphics2);
        if (contents != null) {
            for (int i = 0; i < contents.length; ++i) {
                Font.borderFont.drawString(mGraphics2, contents[i], CCanvas.width / 2, 30 + i * 20, 2);
            }
        }
        super.paint(mGraphics2);
    }
}

