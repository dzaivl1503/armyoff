/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  javax.microedition.lcdui.Display
 *  javax.microedition.lcdui.Displayable
 *  javax.microedition.midlet.MIDlet
 *  javax.microedition.midlet.MIDletStateChangeException
 */
package com.teamobi.mobiarmy2.j2me;

import com.teamobi.mobiarmy2.j2me.MenuCanvas;
import com.teamobi.mobiarmy2.j2me.SplashCanvas;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

public class Army2Midlet
extends MIDlet {
    private static Army2Midlet instance;
    private Display display;
    private boolean started = false;

    public Army2Midlet() {
        instance = this;
    }

    public static Army2Midlet getInstance() {
        return instance;
    }

    protected void startApp()  {
        if (this.started) {
            return;
        }
        this.started = true;
        this.display = Display.getDisplay((MIDlet)this);
        this.display.setCurrent((Displayable)new SplashCanvas(this));
    }

    protected void pauseApp() {
    }

    protected void destroyApp(boolean bl)  {
    }

    public void showMenu() {
        this.display.setCurrent((Displayable)new MenuCanvas(this));
    }

    public void exitGame() {
        try {
            this.destroyApp(true);
        }
        catch (Exception mIDletStateChangeException) {
        }
        this.notifyDestroyed();
    }
}

