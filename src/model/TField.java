/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  javax.microedition.lcdui.Command
 *  javax.microedition.lcdui.CommandListener
 *  javax.microedition.lcdui.Display
 *  javax.microedition.lcdui.Displayable
 *  javax.microedition.lcdui.TextBox
 *  javax.microedition.midlet.MIDlet
 */
package model;

import CLib.mGraphics;
import com.teamobi.mobiarmy2.GameMidlet;
import coreLG.CCanvas;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.TextBox;
import javax.microedition.midlet.MIDlet;
import model.Font;
import model.IAction;
import model.Language;
import network.Command;
import screen.CScreen;

public class TField {
    public String name;
    public int x;
    public int y;
    public int width;
    public int height;
    private boolean isFocus;
    public boolean lockArrow = false;
    public boolean paintFocus = true;
    public static int typeXpeed = 2;
    private static final int[] MAX_TIME_TO_CONFIRM_KEY = new int[]{18, 14, 11, 9, 6, 4, 2};
    private static int CARET_HEIGHT = 0;
    private static final int CARET_WIDTH = 1;
    private static final int CARET_SHOWING_TIME = 5;
    private static final int TEXT_GAP_X = 4;
    private static final int MAX_SHOW_CARET_COUNER = 10;
    public static final int INPUT_TYPE_ANY = 0;
    public static final int INPUT_TYPE_NUMERIC = 1;
    public static final int INPUT_TYPE_PASSWORD = 2;
    public static final int INPUT_ALPHA_NUMBER_ONLY = 3;
    private static String[] print = new String[]{"", ""};
    private static String[] printA = new String[]{"0", "1", "abc2", "def3", "ghi4", "jkl5", "mno6", "pqrs7", "tuv8", "wxyz9", "0", "0"};
    private String text = "";
    private String passwordText = "";
    private String paintedText = "";
    private int caretPos = 0;
    private int counter = 0;
    private int maxTextLenght = 500;
    private int offsetX = 0;
    private int lastKey = -1984;
    private int keyInActiveState = 0;
    private int indexOfActiveChar = 0;
    private int showCaretCounter = 10;
    private int inputType = 0;
    public static boolean isQwerty;
    public static int typingModeAreaWidth;
    public static int mode;
    public static final String[] modeNotify;
    public static final int NOKIA = 0;
    public static final int MOTO = 1;
    public static final int ORTHER = 2;
    public static int changeModeKey;
    private boolean isVisible = true;
    private static Font curFont;
    private boolean isOpenInput;
    private TextBox nativeTextBox;
    private javax.microedition.lcdui.Command nativeOkCommand;
    private javax.microedition.lcdui.Command nativeCancelCommand;
    public String title = "";
    public String textPreferent = "";
    public Command cmdClear;
    private CScreen parentScr;
    private String subStringContent = "";
    public String nameDebug = "";
    int holdCount;

    public TField(CScreen cScreen) {
        this.text = "";
        this.parentScr = cScreen;
        this.init();
    }

    public TField() {
        this.text = "";
        this.init();
    }

    public TField(String string, int n, int n2) {
        this.text = string;
        this.maxTextLenght = n;
        this.inputType = n2;
        this.init();
    }

    private void init() {
        curFont = Font.normalYFont;
        CARET_HEIGHT = curFont.getHeight() + 1;
        this.cmdClear = new Command(Language.delete(), new IAction(){

            public void perform() {
                TField.this.clear();
            }
        });
        if (this.parentScr != null) {
            this.parentScr.right = this.cmdClear;
        }
        typingModeAreaWidth = this.width - 13;
        this.subStringContent = "";
    }

    public void doChangeToTextBox() {
        if (this.isOpenInput || GameMidlet.gameCanvas == null || GameMidlet.instance == null) {
            return;
        }
        try {
            TextBox textBox;
            int n = 0;
            if (this.inputType == 1) {
                n = 2;
            } else if (this.inputType == 2) {
                n = 65536;
            } else if (this.inputType == 3) {
                n = 524288;
            }
            int n2 = this.maxTextLenght;
            if (n2 < 1) {
                n2 = 1;
            }
            final TField tField = this;
            this.nativeTextBox = textBox = new TextBox(this.title == null || this.title.length() == 0 ? "Nh\u1eadp d\u1eef li\u1ec7u" : this.title, this.text, n2, n);
            this.nativeOkCommand = new javax.microedition.lcdui.Command("OK", 4, 1);
            this.nativeCancelCommand = new javax.microedition.lcdui.Command("H\u1ee7y", 3, 2);
            textBox.addCommand(this.nativeOkCommand);
            textBox.addCommand(this.nativeCancelCommand);
            textBox.setCommandListener(new CommandListener(){

                public void commandAction(javax.microedition.lcdui.Command command, Displayable displayable) {
                    if (command == tField.nativeOkCommand) {
                        tField.setText(textBox.getString());
                    }
                    tField.closeNativeTextBox();
                }
            });
            this.isOpenInput = true;
            Display.getDisplay((MIDlet)GameMidlet.instance).setCurrent((Displayable)textBox);
        }
        catch (Throwable throwable) {
            System.out.println("[DEBUG-TFIELD] doChangeToTextBox failed: " + throwable);
            throwable.printStackTrace();
            this.isOpenInput = false;
            this.nativeTextBox = null;
        }
    }

    private void closeNativeTextBox() {
        this.isOpenInput = false;
        this.nativeTextBox = null;
        try {
            Display.getDisplay((MIDlet)GameMidlet.instance).setCurrent((Displayable)GameMidlet.gameCanvas);
        }
        catch (Throwable throwable) {
        }
    }

    public void setisFocus(boolean bl) {
        this.isFocus = bl;
    }

    public void setisVisible(boolean bl) {
        this.isVisible = bl;
    }

    public void setHint(String string) {
        this.text = string;
    }

    public static void setVendorTypeMode(int n) {
        if (n == 1) {
            TField.print[0] = "0";
            TField.print[10] = " *";
            TField.print[11] = "#";
            changeModeKey = 35;
        } else if (n == 0) {
            TField.print[0] = " 0";
            TField.print[10] = "*";
            TField.print[11] = "#";
            changeModeKey = 35;
        } else if (n == 2) {
            TField.print[0] = "0";
            TField.print[10] = "*";
            TField.print[11] = " #";
            changeModeKey = 42;
        }
    }

    public void clear() {
        if (this.caretPos > 0 && this.text.length() > 0) {
            this.text = this.text.substring(0, this.caretPos - 1) + this.text.substring(this.caretPos, this.text.length());
            --this.caretPos;
            this.refreshTextView();
        }
    }

    private void keyPressedAny(int n) {
        String[] stringArray = this.inputType != 2 && this.inputType != 3 ? print : printA;
        if (n == this.lastKey) {
            this.indexOfActiveChar = (this.indexOfActiveChar + 1) % stringArray[n - 48].length();
            char c = stringArray[n - 48].charAt(this.indexOfActiveChar);
            c = mode == 0 ? Character.toLowerCase(c) : (mode == 1 ? Character.toUpperCase(c) : (mode == 2 ? Character.toUpperCase(c) : stringArray[n - 48].charAt(stringArray[n - 48].length() - 1)));
            String string = this.text.substring(0, this.caretPos - 1) + c;
            if (this.caretPos < this.text.length()) {
                string = string + this.text.substring(this.caretPos, this.text.length());
            }
            this.text = string;
            this.keyInActiveState = MAX_TIME_TO_CONFIRM_KEY[typeXpeed];
            this.refreshTextView();
        } else if (this.text.length() < this.maxTextLenght) {
            if (mode == 1 && this.lastKey != -1984) {
                mode = 0;
            }
            this.indexOfActiveChar = 0;
            char c = stringArray[n - 48].charAt(this.indexOfActiveChar);
            c = mode == 0 ? Character.toLowerCase(c) : (mode == 1 ? Character.toUpperCase(c) : (mode == 2 ? Character.toUpperCase(c) : stringArray[n - 48].charAt(stringArray[n - 48].length() - 1)));
            String string = this.text.substring(0, this.caretPos) + c;
            if (this.caretPos < this.text.length()) {
                string = string + this.text.substring(this.caretPos, this.text.length());
            }
            this.text = string;
            this.keyInActiveState = MAX_TIME_TO_CONFIRM_KEY[typeXpeed];
            ++this.caretPos;
            this.refreshTextView();
        }
        this.lastKey = n;
    }

    public static void keyPressedAscii() {
        if (++mode > 3) {
            mode = 0;
        }
    }

    private void keyPressedAscii(int n) {
        if ((this.inputType != 2 && this.inputType != 3 || n >= 48 && n <= 57 || n >= 65 && n <= 90 || n >= 97 && n <= 122) && this.text.length() < this.maxTextLenght) {
            String string = this.text.substring(0, this.caretPos) + (char)n;
            if (this.caretPos < this.text.length()) {
                string = string + this.text.substring(this.caretPos, this.text.length());
            }
            this.text = string;
            ++this.caretPos;
            this.refreshTextView();
        }
    }

    public void keyHold(int n) {
        ++this.holdCount;
        if (this.holdCount > 15 && !isQwerty && n < print.length) {
            this.clear();
            this.keyPressedAscii(print[n].charAt(print[n].length() - 1));
            this.keyInActiveState = MAX_TIME_TO_CONFIRM_KEY[typeXpeed];
            this.holdCount = 0;
        }
        if (this.holdCount > 20 && n == 19) {
            this.setText("");
            this.holdCount = 0;
        }
    }

    public boolean keyPressed(int n) {
        if (n != 8 && n != -8 && n != 204) {
            this.holdCount = 0;
            if (n >= 65 && n <= 122) {
                isQwerty = true;
                typingModeAreaWidth = 0;
            }
            if (isQwerty) {
                if (n == 45) {
                    if (n == this.lastKey && this.keyInActiveState < MAX_TIME_TO_CONFIRM_KEY[typeXpeed]) {
                        this.text = this.text.substring(0, this.caretPos - 1) + '_';
                        this.refreshTextView();
                        this.lastKey = -1984;
                        return false;
                    }
                    this.lastKey = 45;
                }
                if (n >= 32) {
                    this.keyPressedAscii(n);
                    return false;
                }
            }
            if (n == changeModeKey) {
                if (++mode > 3) {
                    mode = 0;
                }
                this.keyInActiveState = 1;
                this.lastKey = n;
                return false;
            }
            if (n == 42) {
                n = 58;
            }
            if (n == 35) {
                n = 59;
            }
            if (n >= 48 && n <= 59) {
                if (this.inputType != 0 && this.inputType != 2 && this.inputType != 3) {
                    if (this.inputType == 1) {
                        this.keyPressedAscii(n);
                        this.keyInActiveState = 1;
                    }
                } else {
                    this.keyPressedAny(n);
                }
            } else {
                this.indexOfActiveChar = 0;
                this.lastKey = -1984;
                if (n == 14 && !this.lockArrow) {
                    if (this.caretPos > 0) {
                        --this.caretPos;
                        this.setOffset();
                        this.showCaretCounter = 10;
                        return false;
                    }
                } else if (n == 15 && !this.lockArrow) {
                    if (this.caretPos < this.text.length()) {
                        ++this.caretPos;
                        this.setOffset();
                        this.showCaretCounter = 10;
                        return false;
                    }
                } else {
                    if (n == 19) {
                        this.clear();
                        return false;
                    }
                    this.lastKey = n;
                }
            }
            return true;
        }
        this.clear();
        return true;
    }

    public void paint(mGraphics mGraphics2) {
        if (this.isVisible) {
            boolean bl = this.isFocused();
            this.paintedText = this.inputType == 2 ? this.passwordText : this.text;
            mGraphics2.setColor(1521982);
            if (bl) {
                if (this.paintFocus) {
                    mGraphics2.setColor(4156571);
                    mGraphics2.fillRect(this.x + 1, this.y + 1 - 2, this.width - 1 + 2, this.height - 1, false);
                }
            } else {
                mGraphics2.setColor(1521982);
                mGraphics2.fillRect(this.x + 1, this.y + 1 - 2, this.width - 1 + 2, this.height - 1, false);
            }
            int n = 4 + this.offsetX + this.x + 1;
            curFont.drawString(mGraphics2, this.subStringContent, this.x + 5, this.y + 2, 0);
            if (bl && this.keyInActiveState == 0 && (this.showCaretCounter > 0 || this.counter / 5 % 2 == 0)) {
                this.caretPos = this.paintedText.length();
                mGraphics2.setColor(0xFFFFFF);
                int n2 = 4 + this.x + curFont.getWidth(this.subStringContent) - 1 + 1;
                int n3 = this.y + (this.height - CARET_HEIGHT) / 2 - 1;
                if (n2 > this.x + 1 + this.width - 2) {
                    n2 = this.x + 1 + this.width - 2;
                }
                mGraphics2.fillRect(n2, n3, 1, CARET_HEIGHT, false);
            }
        }
    }

    private String getSubString(int n) {
        String string = "";
        for (int i = this.text.length() - 1; i >= 0 && curFont.getWidth(string = this.inputType == 2 ? string + "*" : this.text.charAt(i) + string) <= n; --i) {
        }
        return string;
    }

    public void setOffset() {
        this.paintedText = this.inputType == 2 ? this.passwordText : this.text;
    }

    private void updateOffsetx() {
        if (this.caretPos > this.paintedText.length()) {
            this.caretPos = this.paintedText.length();
        }
        if (this.offsetX < 0 && curFont.getWidth(this.paintedText) + this.offsetX < this.width - 4 - 13 - typingModeAreaWidth) {
            this.offsetX = this.width - 10 - typingModeAreaWidth - curFont.getWidth(this.paintedText);
        }
        if (this.offsetX + curFont.getWidth(this.paintedText.substring(0, this.caretPos)) <= 0) {
            this.offsetX = -curFont.getWidth(this.paintedText.substring(0, this.caretPos));
            this.offsetX += 40;
        } else if (this.offsetX + curFont.getWidth(this.paintedText.substring(0, this.caretPos)) >= this.width - 12 - typingModeAreaWidth) {
            this.offsetX = this.width - 10 - typingModeAreaWidth - curFont.getWidth(this.paintedText.substring(0, this.caretPos)) - 8;
        }
        if (this.offsetX > 0) {
            this.offsetX = 0;
        }
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
        if (this.isVisible) {
            ++this.counter;
            if (this.keyInActiveState > 0) {
                --this.keyInActiveState;
                if (this.keyInActiveState == 0) {
                    this.indexOfActiveChar = 0;
                    if (mode == 1 && this.lastKey != changeModeKey) {
                        mode = 0;
                    }
                    this.lastKey = -1984;
                    this.refreshTextView();
                }
            }
            if (this.showCaretCounter > 0) {
                --this.showCaretCounter;
            }
            this.updateOffsetx();
        }
    }

    public void setTextBox() {
        if (CCanvas.isPointer(this.x, this.y, this.width, this.height, 0)) {
            if (!this.isFocus) {
                this.isFocus = true;
            } else {
                this.doChangeToTextBox();
            }
        } else {
            this.isFocus = false;
        }
    }

    public String getText() {
        return this.text;
    }

    public void setText(String string) {
        if (string != null) {
            this.lastKey = -1984;
            this.keyInActiveState = 0;
            this.indexOfActiveChar = 0;
            this.text = string;
            this.caretPos = string.length();
            this.refreshTextView();
        }
    }

    public void insertText(String string) {
        this.text = this.text.substring(0, this.caretPos) + string + this.text.substring(this.caretPos);
        this.caretPos += string.length();
        this.refreshTextView();
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

    public boolean isNotNumber() {
        try {
            int n = Integer.parseInt(this.getText());
            return false;
        }
        catch (Exception exception) {
            return true;
        }
    }

    public boolean isNonOrEmpty(String string) {
        return false;
    }

    public void resetTextBox() {
        this.isOpenInput = false;
        this.nativeTextBox = null;
        this.text = "";
        this.title = "";
        this.textPreferent = "";
        this.paintedText = "";
        this.subStringContent = "";
        this.offsetX = 0;
        this.caretPos = 0;
        this.lastKey = -1984;
        this.keyInActiveState = 0;
        this.indexOfActiveChar = 0;
        isQwerty = false;
        mode = 0;
        typingModeAreaWidth = this.width - 13;
    }

    private void refreshTextView() {
        this.setPasswordTest();
        this.setOffset();
        this.subStringContent = this.getSubString(this.width - 10);
    }

    static {
        mode = 0;
        modeNotify = new String[]{"abc", "Abc", "ABC", "123"};
        changeModeKey = 35;
        curFont = Font.normalYFont;
    }
}

