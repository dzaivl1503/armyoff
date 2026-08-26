/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  javax.microedition.io.Connector
 *  javax.microedition.io.SocketConnection
 */
package CLib;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import javax.microedition.io.Connector;
import javax.microedition.io.SocketConnection;
import model.CRes;

public class mSocket {
    SocketConnection _socket;
    private boolean closed;

    public mSocket(String string, int n) {
        try {
            this._socket = (SocketConnection)Connector.open((String)("socket://" + string + ":" + n));
        }
        catch (Exception iOException) {
            iOException.printStackTrace();
            CRes.err("====> ConnectException ");
        }
    }

    public void close() {
        try {
            if (this._socket != null) {
                this._socket.close();
            }
        }
        catch (Exception iOException) {
            iOException.printStackTrace();
        }
        finally {
            this.closed = true;
        }
    }

    public void setKeepAlive(boolean bl) {
        try {
            if (this._socket != null) {
                this._socket.setSocketOption((byte)2, bl ? 1 : 0);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public DataOutputStream getOutputStream() {
        try {
            return this._socket.openDataOutputStream();
        }
        catch (Exception iOException) {
            iOException.printStackTrace();
            return null;
        }
    }

    public DataInputStream getInputStream() {
        try {
            return this._socket.openDataInputStream();
        }
        catch (Exception iOException) {
            iOException.printStackTrace();
            return null;
        }
    }

    public String getIP() {
        if (this._socket == null) {
            return "do not connect!";
        }
        try {
            return this._socket.getAddress() + " " + this._socket.getPort();
        }
        catch (Exception iOException) {
            return "do not connect!";
        }
    }

    public byte getState() {
        if (this._socket == null || this.closed) {
            return (byte)(this.closed ? 0 : -1);
        }
        return 1;
    }
}

