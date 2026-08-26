/*
 * Decompiled with CFR 0.152.
 */
package network;

import CLib.Graphics;
import model.IAction;

public class Command {
    public String caption;
    public IAction action;

    public Command(String string, IAction iAction) {
        this.caption = string;
        this.action = iAction;
    }

    public void paint(Graphics graphics) {
    }
}

