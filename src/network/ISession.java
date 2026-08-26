/*
 * Decompiled with CFR 0.152.
 */
package network;

import network.IMessageHandler;
import network.Message;

public interface ISession {
    public boolean isConnected();

    public void setHandler(IMessageHandler var1);

    public void connect(String var1, String var2);

    public void sendMessage(Message var1);
}

