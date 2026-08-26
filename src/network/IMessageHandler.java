/*
 * Decompiled with CFR 0.152.
 */
package network;

import network.Message;

public interface IMessageHandler {
    public void onMessage(Message var1);

    public void onConnectionFail();

    public void onDisconnected();

    public void onConnectOK();
}

