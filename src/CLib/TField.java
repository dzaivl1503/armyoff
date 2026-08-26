/*
 * Decompiled with CFR 0.152.
 */
package CLib;

import CLib.mFont;
import CLib.mGraphics;
import CLib.myEditText;
import com.teamobi.mobiarmy2.TemCanvas;
import com.teamobi.mobiarmy2.TemMidlet;

public class TField {
    public int x;
    public int y;
    public int width = 120 * mGraphics.zoomLevel;
    public int height = 30 * mGraphics.zoomLevel;
    public int widthTouch = 0;
    public boolean isFocus;
    public boolean lockArrow = false;
    public boolean paintFocus = true;
    public boolean isChangeFocus = true;
    public static int typeXpeed = 2;
    public static final int CARET_WIDTH = 1;
    public static final int CARET_SHOWING_TIME = 5;
    public static final int TEXT_GAP_X = 4;
    private static final int MAX_SHOW_CARET_COUNER = 10;
    public static final int INPUT_TYPE_ANY = 0;
    public static final int INPUT_TYPE_NUMERIC = 1;
    public static final int INPUT_TYPE_PASSWORD = 2;
    public static final int INPUT_ALPHA_NUMBER_ONLY = 3;
    private String text = "";
    private String passwordText = "";
    public String paintedText = "";
    public int caretPos = 0;
    public int counter = 0;
    private int maxTextLenght = 500;
    public int offsetX = 0;
    public boolean isCloseKey = true;
    public int keyInActiveState = 0;
    public int showCaretCounter = 10;
    public int inputType = 0;
    public static boolean isQwerty;
    public static int typingModeAreaWidth;
    public static int mode;
    public static final String[] modeNotify;
    public static final int NOKIA = 0;
    public static final int MOTO = 1;
    public static final int ORTHER = 2;
    public static int changeModeKey;
    public static TemCanvas c;
    public static TemMidlet m;
    public static int timeChangeMode;
    public static int xDu;
    public static int yDu;
    public String sDefaust = "";
    public static boolean isOpenTextBox;
    public boolean visible = false;
    public String title = "";
    public String strnull = "";
    public myEditText editText;
    public int ID;
    boolean isposition = false;
    int yt = 0;
    int tempTime = -1;
    int xCamText = 0;
    int timeFocus = 0;

    public void doChangeToTextBox() {
    }

    public static int getHeight() {
        return 20;
    }

    public void positionLogin() {
    }

    public static boolean setNormal(char c) {
        return c >= '0' && c <= '9' || c >= 'A' && c <= 'Z' || c >= 'a' && c <= 'z';
    }

    public static void setVendorTypeMode(int n) {
    }

    private void init() {
    }

    public TField() {
        this.text = "";
        this.init();
        this.setheightText();
    }

    public TField(int n, int n2) {
        this.text = "";
        this.x = n;
        this.y = n2;
        this.init();
        this.setFocus(false);
        this.setheightText();
    }

    public TField(int n, int n2, int n3) {
        this.text = "";
        this.x = n;
        this.y = n2;
        this.width = n3;
        this.widthTouch = 0;
        this.init();
        this.setFocus(false);
        this.setheightText();
    }

    public TField(int n, int n2, int n3, int n4) {
        this.text = "";
        this.x = n;
        this.y = n2;
        this.width = n3;
        this.widthTouch = n4;
        this.init();
        this.setFocus(false);
        this.setheightText();
    }

    public TField(String string, int n, int n2) {
        this.text = string;
        this.maxTextLenght = n;
        this.inputType = n2;
        this.setheightText();
        this.init();
    }

    public void setheightText() {
        this.height = 20;
    }

    public void setStringNull(String string) {
        this.strnull = string;
    }

    public void clear() {
        if (this.caretPos > 0 && this.text.length() > 0) {
            this.text = this.text.substring(0, this.caretPos - 1) + this.text.substring(this.caretPos, this.text.length());
            --this.caretPos;
            this.setOffset();
            this.setPasswordTest();
        }
    }

    public void setPoiter() {
        isOpenTextBox = true;
        this.doChangeToTextBox();
    }

    public void setOffset() {
        this.paintedText = this.inputType == 2 ? this.passwordText : this.text;
        if (this.offsetX < 0 && mFont.tahoma_8b_brown.getWidth(this.paintedText) + this.offsetX < this.width - 4 - 13 - typingModeAreaWidth) {
            this.offsetX = this.width - 10 - typingModeAreaWidth - mFont.tahoma_8b_brown.getWidth(this.paintedText);
        }
        if (this.offsetX + mFont.tahoma_7_white.getWidth(this.paintedText.substring(0, this.caretPos)) <= 0) {
            this.offsetX = -mFont.tahoma_8b_brown.getWidth(this.paintedText.substring(0, this.caretPos));
            this.offsetX += 40;
        } else if (this.offsetX + mFont.tahoma_8b_brown.getWidth(this.paintedText.substring(0, this.caretPos)) >= this.width - 12 - typingModeAreaWidth) {
            this.offsetX = this.width - 10 - typingModeAreaWidth - mFont.tahoma_8b_brown.getWidth(this.paintedText.substring(0, this.caretPos)) - 8;
        }
        if (this.offsetX > 0) {
            this.offsetX = 0;
        }
    }

    public void keyHold(int n) {
    }

    public boolean keyPressed(int n) {
        return true;
    }

    public void pointerRelease(int n, int n2) {
    }

    public void paint(mGraphics mGraphics2) {
        this.paintedText = this.inputType == 2 ? this.passwordText : this.text;
        this.paintTf(mGraphics2);
    }

    public void paintByList(mGraphics mGraphics2) {
        this.paintedText = this.inputType == 2 ? this.passwordText : this.text;
        this.paintTfByList(mGraphics2);
    }

    public void paintTfByList(mGraphics mGraphics2) {
        int n;
        boolean bl = this.isFocused();
        int n2 = 0;
        mFont mFont2 = mFont.tahoma_8b_black;
        if (this.inputType == 2) {
            this.paintedText = this.passwordText;
            n2 = 2;
        } else {
            this.paintedText = this.text;
        }
        int n3 = 0;
        mGraphics2.setColor(-4155296);
        ++this.timeFocus;
        if (bl && (n = this.paintedText.length()) > 0 && this.caretPos > 0) {
            n3 = mFont.tahoma_8b_black.getWidth(this.paintedText.substring(0, this.caretPos));
        }
        if (this.paintedText.length() == 0 && !bl) {
            n2 = 0;
            this.paintedText = this.strnull;
            mFont2 = mFont.tahoma_8b_brown;
        }
        mFont2.drawString(mGraphics2, this.paintedText, this.x + 4, this.y + this.height / 2 - 5 + n2, 0, true);
        if (bl && this.timeFocus % 10 > 6) {
            mGraphics2.setColor(0);
            mGraphics2.fillRect(this.x + 3 + n3, this.y + this.height / 2 - 7, 1, 14, false);
        }
    }

    public void paintTf(mGraphics mGraphics2) {
        int n;
        boolean bl = this.isFocused();
        int n2 = 0;
        mFont mFont2 = mFont.tahoma_8b_black;
        if (this.inputType == 2) {
            this.paintedText = this.passwordText;
            n2 = 2;
        } else {
            this.paintedText = this.text;
        }
        int n3 = 0;
        mGraphics2.setColor(-4155296);
        ++this.timeFocus;
        if (bl && (n = this.paintedText.length()) > 0 && this.caretPos > 0) {
            n3 = mFont.tahoma_8b_black.getWidth(this.paintedText.substring(0, this.caretPos));
        }
        mGraphics2.setClip(this.x + 2, this.y + 2, this.width - 4, this.height - 3);
        n = mGraphics2.getTranslateX();
        int n4 = mGraphics2.getTranslateY();
        mGraphics2.translate(-this.xCamText, 0);
        if (this.paintedText.length() == 0 && !bl) {
            n2 = 0;
            this.paintedText = this.strnull;
            mFont2 = mFont.tahoma_8b_brown;
        }
        mFont2.drawString(mGraphics2, this.paintedText, this.x + 4, this.y + this.height / 2 - 5 + n2, 0, true);
        if (bl && this.timeFocus % 10 > 6) {
            mGraphics2.setColor(0);
            mGraphics2.fillRect(this.x + 3 + n3, this.y + this.height / 2 - 7, 1, 14, false);
        }
        mGraphics2.translate(n, n4);
    }

    public boolean isFocused() {
        return this.isFocus;
    }

    private void setPasswordTest() {
        if (this.inputType == 2) {
            this.passwordText = "";
            for (int i = 0; i < this.text.length(); ++i) {
                this.passwordText = this.passwordText + "*";
            }
            if (this.keyInActiveState > 0 && this.caretPos > 0) {
                this.passwordText = this.passwordText.substring(0, this.caretPos - 1) + this.text.charAt(this.caretPos - 1) + this.passwordText.substring(this.caretPos, this.passwordText.length());
            }
        }
    }

    public void update() {
        if (this.isFocused()) {
            String string = this.inputType == 2 ? this.passwordText : this.text;
            this.xCamText = -this.width / 2 + this.caretPos * 5 + 4;
            int n = mFont.tahoma_8b_black.getWidth(string) - this.width + 8;
            if (this.xCamText > n) {
                this.xCamText = n;
            }
            if (this.xCamText < 0) {
                this.xCamText = 0;
            }
        } else {
            this.xCamText = 0;
        }
    }

    public void updatePoiter() {
    }

    public void updatepointerByList() {
        this.doChangeToTextBox();
    }

    public void setTextBox() {
    }

    public String getText() {
        return this.text;
    }

    public void setText(String string) {
        if (string != null) {
            this.text = string;
            this.paintedText = string;
            this.setPasswordTest();
            this.caretPos = string.length();
            this.setOffset();
        }
    }

    public void insertText(String string) {
        this.text = this.text.substring(0, this.caretPos) + string + this.text.substring(this.caretPos);
        this.setPasswordTest();
        this.caretPos += string.length();
        this.setOffset();
    }

    public int getMaxTextLenght() {
        return this.maxTextLenght;
    }

    public void setMaxTextLenght(int n) {
        this.maxTextLenght = n;
    }

    public int getIputType() {
        return this.inputType;
    }

    public void setIputType(int n) {
        this.inputType = n;
    }

    public void setFocus(boolean bl) {
        this.isFocus = bl;
    }

    static {
        mode = 0;
        modeNotify = new String[]{"abc", "Abc", "ABC", "123"};
        changeModeKey = 11;
    }
}

