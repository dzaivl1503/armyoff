/*
 * Decompiled with CFR 0.152.
 */
package network;

import CLib.mSocket;
import CLib.mSystem;
import CLib.mVector;
import coreLG.CCanvas;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import model.CRes;
import model.Language;
import network.IMessageHandler;
import network.ISession;
import network.Message;

public class Session_ME
implements ISession {
    protected static Session_ME instance = new Session_ME();
    private final Sender sender = new Sender();
    private DataOutputStream dos;
    public DataInputStream dis;
    public static IMessageHandler messageHandler;
    private static mSocket _mSocket;
    public boolean connected;
    public boolean connecting;
    public boolean start = true;
    public Thread initThread;
    public Thread collectorThread;
    public Thread sendThread;
    public int sendByteCount;
    public int recvByteCount;
    private boolean getKeyComplete;
    public byte[] key = null;
    private byte curR;
    private byte curW;
    private long timeConnected;
    public String strRecvByteCount = "";
    public static mVector recieveMsg;
    public static int receiveSynchronized;
    public static int countRead;
    private int errip = 0;
    public static String h;
    public static int p;
    public static boolean isCancel;
    private int countMsg = 0;
    int err3 = 0;

    public static Session_ME gI() {
        return instance;
    }

    public boolean isConnected() {
        return this.connected;
    }

    public void setHandler(IMessageHandler iMessageHandler) {
        messageHandler = iMessageHandler;
    }

    public void connect(String string, int n) {
        h = string;
        p = n;
        CRes.out("==========================> connect to  " + this.connected + " _ " + this.connecting);
        if (!this.connected && !this.connecting) {
            CRes.out("==========================> connect to  " + string + " _ " + n);
            if (_mSocket != null && _mSocket.getState() == 1) {
                this.close(0);
            }
            this.sender.removeAllMessage();
            this.getKeyComplete = false;
            _mSocket = null;
            this.initThread = new Thread(new NetworkInit(string, n));
            this.initThread.start();
        }
    }

    public void onRecieveMsg(Message message) {
        recieveMsg.addElement(message);
    }

    public static void update() {
        if (Session_ME.gI().connecting && !Session_ME.gI().start && _mSocket == null) {
            CCanvas.startWaitDlgWithoutCancel(Language.connecting(), 11111);
        } else if (receiveSynchronized <= 0 && recieveMsg.size() > 0) {
            Message message = (Message)recieveMsg.elementAt(0);
            if (message == null) {
                return;
            }
            recieveMsg.removeElementAt(0);
            messageHandler.onMessage(message);
        }
    }

    public void sendMessage(Message message) {
        this.sender.AddMessage(message);
    }

    private synchronized void doSendMessage(Message message) throws IOException {
        byte[] byArray = message.getData();
        try {
            int n;
            if (this.getKeyComplete) {
                n = this.writeKey(message.command);
                this.dos.writeByte(n);
                CRes.err("send cmd " + message.command + " _ " + n);
            } else {
                this.dos.writeByte(message.command);
            }
            if (byArray != null) {
                int n2;
                n = byArray.length;
                if (this.getKeyComplete) {
                    n2 = this.writeKey((byte)(n >> 8));
                    this.dos.writeByte(n2);
                    byte by = this.writeKey((byte)(n & 0xFF));
                    this.dos.writeByte(by);
                } else {
                    this.dos.writeShort(n);
                }
                if (this.getKeyComplete) {
                    for (n2 = 0; n2 < byArray.length; ++n2) {
                        byArray[n2] = this.writeKey(byArray[n2]);
                    }
                }
                this.dos.write(byArray);
                this.sendByteCount += 5 + byArray.length;
            } else {
                this.dos.writeShort(0);
                this.sendByteCount += 5;
            }
            this.dos.flush();
        }
        catch (IOException iOException) {
            iOException.printStackTrace();
        }
    }

    private byte readKey(byte by) {
        this.err3 = 2;
        byte[] byArray = this.key;
        byte by2 = this.curR;
        this.curR = (byte)(by2 + 1);
        byte by3 = (byte)(byArray[by2] & 0xFF ^ by & 0xFF);
        this.err3 = 4;
        if (this.curR >= this.key.length) {
            this.curR = (byte)(this.curR % this.key.length);
        }
        return by3;
    }

    private byte writeKey(byte by) {
        this.err3 = 2;
        byte[] byArray = this.key;
        byte by2 = this.curW;
        this.curW = (byte)(by2 + 1);
        byte by3 = (byte)(byArray[by2] & 0xFF ^ by & 0xFF);
        this.err3 = 5;
        if (this.curW >= this.key.length) {
            this.curW = (byte)(this.curW % this.key.length);
        }
        return by3;
    }

    private Message readMessage2(byte by) throws Exception {
        try {
            int n = this.dis.readInt();
            if (n > 0) {
                byte[] byArray = new byte[n];
                int n2 = 0;
                int n3 = 0;
                while (n2 != -1 && n3 < n) {
                    n2 = this.dis.read(byArray, n3, n - n3);
                    if (n2 <= 0) continue;
                    this.recvByteCount += 5 + (n3 += n2);
                }
                Message message = new Message(by, byArray);
                return message;
            }
        }
        catch (Exception exception) {
            exception.printStackTrace();
            CRes.out("" + exception.getMessage());
        }
        return null;
    }

    public Message readMessage3(byte by) {
        try {
            int n = this.dis.readShort();
            CRes.err("===========. readMessage3 dataLen = " + n);
            if (n > 0) {
                byte[] byArray = new byte[n];
                int n2 = 0;
                int n3 = 0;
                while (n2 != -1 && n3 < n) {
                    n2 = this.dis.read(byArray, n3, n - n3);
                    if (n2 <= 0) continue;
                    this.recvByteCount += 5 + (n3 += n2);
                }
                Message message = new Message(by, byArray);
                CRes.out("==========> readmessage 3 BigImage " + byArray.length);
                return message;
            }
        }
        catch (Exception exception) {
            exception.printStackTrace();
            CRes.out("" + exception.getMessage());
        }
        return null;
    }

    public void connect(String string, String string2) {
        h = string;
        p = Short.parseShort(string2);
        if (!this.connected && !this.connecting) {
            this.sender.removeAllMessage();
            this.getKeyComplete = false;
            _mSocket = null;
            this.initThread = new Thread(new NetworkInit(string, Short.parseShort(string2)));
            this.initThread.start();
        }
    }

    public void close(int n) {
        CRes.out("==========================> Clean network " + n);
        this.cleanNetwork();
    }

    private void cleanNetwork() {
        this.key = null;
        this.curR = 0;
        this.curW = 0;
        try {
            recieveMsg.removeAllElements();
            this.connected = false;
            this.connecting = false;
            if (_mSocket != null) {
                _mSocket.close();
                _mSocket = null;
            }
            if (this.dos != null) {
                this.dos.close();
                this.dos = null;
            }
            if (this.dis != null) {
                this.dis.close();
                this.dis = null;
            }
            this.sendThread = null;
            this.collectorThread = null;
            if (this.initThread != null && this.initThread.isAlive()) {
                this.initThread.interrupt();
                this.initThread = null;
            }
            System.gc();
        }
        catch (Exception exception) {
            _mSocket = null;
            recieveMsg.removeAllElements();
            System.gc();
            exception.printStackTrace();
        }
    }

    static {
        recieveMsg = new mVector();
        receiveSynchronized = 0;
        countRead = 0;
        h = "";
    }

    private class Sender
    implements Runnable {
        public final mVector sendingMessage = new mVector();
        int iErrIp = 0;

        public void AddMessage(Message message) {
            this.sendingMessage.addElement(message);
        }

        public void removeAllMessage() {
            if (this.sendingMessage != null) {
                this.sendingMessage.removeAllElements();
            }
        }

        public void run() {
            try {
                this.iErrIp = 0;
                while (Session_ME.this.connected) {
                    this.iErrIp = 1;
                    if (Session_ME.this.getKeyComplete) {
                        while (this.sendingMessage.size() > 0) {
                            this.iErrIp = 2;
                            Message message = (Message)this.sendingMessage.elementAt(0);
                            this.iErrIp = 300 + message.command;
                            this.sendingMessage.removeElementAt(0);
                            this.iErrIp = 400 + message.command;
                            Session_ME.this.doSendMessage(message);
                            this.iErrIp = 500 + message.command;
                        }
                    }
                    try {
                        this.iErrIp = 6;
                        Thread.sleep(10L);
                        this.iErrIp = 7;
                    }
                    catch (InterruptedException interruptedException) {
                        this.iErrIp = 8;
                    }
                }
                this.iErrIp = 9;
            }
            catch (IOException iOException) {
                iOException.printStackTrace();
            }
        }
    }

    private class NetworkInit
    implements Runnable {
        private final String host;
        private final int port;
        int iErr = 0;

        NetworkInit(String string, int n) {
            this.host = string;
            this.port = n;
        }

        public void run() {
            try {
                this.iErr = 0;
                isCancel = false;
                this.iErr = 1;
                new Thread(new Runnable(){

                    public void run() {
                        NetworkInit.this.iErr = 3;
                        try {
                            NetworkInit.this.iErr = 4;
                            Thread.sleep(20000L);
                        }
                        catch (InterruptedException interruptedException) {
                            NetworkInit.this.iErr = 5;
                        }
                        if (Session_ME.this.connecting) {
                            NetworkInit.this.iErr = 6;
                            try {
                                NetworkInit.this.iErr = 7;
                                _mSocket.close();
                            }
                            catch (Exception exception) {
                                NetworkInit.this.iErr = 8;
                            }
                            NetworkInit.this.iErr = 9;
                            isCancel = true;
                            Session_ME.this.connecting = false;
                            Session_ME.this.connected = false;
                            NetworkInit.this.iErr = 10;
                            messageHandler.onConnectionFail();
                            NetworkInit.this.iErr = 11;
                        }
                    }
                }).start();
                this.iErr = 12;
                Session_ME.this.connecting = true;
                Thread.currentThread().setPriority(1);
                this.iErr = 13;
                Session_ME.this.connected = true;
                this.iErr = 14;
                try {
                    this.iErr = 15;
                    CRes.out("1 do connect host =========> " + this.host + " __ " + this.port);
                    this.doConnect(this.host, this.port);
                    this.iErr = 16;
                    messageHandler.onConnectOK();
                    CCanvas.endDlg();
                    this.iErr = 17;
                }
                catch (Exception exception) {
                    exception.printStackTrace();
                    this.iErr = 18;
                    try {
                        this.iErr = 19;
                        Thread.sleep(500L);
                        this.iErr = 20;
                    }
                    catch (InterruptedException interruptedException) {
                        this.iErr = 21;
                    }
                    this.iErr = 22;
                    if (isCancel) {
                        return;
                    }
                    this.iErr = 23;
                    if (messageHandler != null) {
                        this.iErr = 24;
                        Session_ME.this.close(2);
                        this.iErr = 25;
                        messageHandler.onConnectionFail();
                        this.iErr = 26;
                    }
                }
            }
            catch (Exception exception) {
                exception.printStackTrace();
                CRes.err("throw Exception!!!!!");
            }
        }

        public void doConnect(String string, int n) throws Exception {
            _mSocket = new mSocket(string, n);
            if (_mSocket != null) {
                _mSocket.setKeepAlive(true);
                Session_ME.this.dos = _mSocket.getOutputStream();
                Session_ME.this.dis = _mSocket.getInputStream();
                new Thread(Session_ME.this.sender).start();
                Session_ME session_ME = Session_ME.this;
                session_ME.getClass();
                Session_ME.this.collectorThread = new Thread(session_ME.new MessageCollector(null));
                Session_ME.this.collectorThread.start();
                Session_ME.this.timeConnected = mSystem.currentTimeMillis();
                Session_ME.this.doSendMessage(new Message(-27));
                Session_ME.this.connecting = false;
                Session_ME.this.start = false;
            }
        }
    }

    private class MessageCollector
    implements Runnable {
        private MessageCollector() {
        }

        public void run() {
            Session_ME.this.errip = 0;
            try {
                Session_ME.this.errip = 1;
                try {
                    Session_ME.this.errip = 2;
                    while (Session_ME.this.isConnected()) {
                        Session_ME.this.errip = 3;
                        Message message = this.readMessage();
                        if (message == null) {
                            Session_ME.this.errip = 1200 + message.command;
                            break;
                        }
                        CRes.out("Receive message " + message.command);
                        Session_ME.this.errip = 4;
                        try {
                            Session_ME.this.errip = 500 + message.command;
                            if (message.command == -27) {
                                Session_ME.this.errip = 600 + message.command;
                                this.getKey(message);
                                Session_ME.this.errip = 700 + message.command;
                            } else {
                                Session_ME.this.errip = 800 + message.command;
                                Session_ME.this.onRecieveMsg(message);
                                Session_ME.this.errip = 900 + message.command;
                            }
                        }
                        catch (Exception exception) {
                            Session_ME.this.errip = 1000 + message.command;
                            exception.printStackTrace();
                            Session_ME.this.errip = 1100 + message.command;
                        }
                        try {
                            Session_ME.this.errip = 6;
                            Thread.sleep(100L);
                            Session_ME.this.errip = 7;
                        }
                        catch (InterruptedException interruptedException) {
                            Session_ME.this.errip = 8;
                        }
                    }
                    Session_ME.this.errip = 13;
                }
                catch (Exception exception) {
                    Session_ME.this.errip = 14;
                }
                Session_ME.this.errip = 15;
                if (Session_ME.this.connected) {
                    Session_ME.this.errip = 16;
                    if (messageHandler != null) {
                        Session_ME.this.errip = 17;
                        if (mSystem.currentTimeMillis() - Session_ME.this.timeConnected > 500L) {
                            messageHandler.onDisconnected();
                        } else {
                            messageHandler.onConnectionFail();
                        }
                        Session_ME.this.errip = 18;
                    }
                    Session_ME.this.errip = 19;
                    if (_mSocket != null) {
                        Session_ME.this.close(1);
                    }
                    Session_ME.this.errip = 20;
                }
            }
            catch (Exception exception) {
                exception.printStackTrace();
            }
        }

        private void getKey(Message message) throws IOException {
            Object object;
            int n;
            int n2 = message.reader().readByte();
            Session_ME.this.key = new byte[n2];
            for (n = 0; n < n2; ++n) {
                Session_ME.this.key[n] = message.reader().readByte();
            }
            for (n = 0; n < Session_ME.this.key.length - 1; ++n) {
                Object object2 = object = (Object)Session_ME.this.key;
                int n3 = n + 1;
                ((byte[])object2)[n3] = (byte)(((byte[])object2)[n3] ^ Session_ME.this.key[n]);
            }
            object = "";
            for (int i = 0; i < Session_ME.this.key.length - 1; ++i) {
                object = (String)object + Session_ME.this.key[i] + "_";
            }
            CRes.out("======> key is " + (String)object);
            Session_ME.this.getKeyComplete = true;
        }

        private Message readMessage() throws Exception {
            try {
                ++countRead;
                byte by = Session_ME.this.dis.readByte();
                if (Session_ME.this.getKeyComplete) {
                    by = Session_ME.this.readKey(by);
                }
                CRes.out("Receive 1 cmd " + by);
                if (by != -120 && by != 90) {
                    int n;
                    int n2;
                    if (Session_ME.this.getKeyComplete) {
                        byte by2 = Session_ME.this.dis.readByte();
                        n2 = Session_ME.this.dis.readByte();
                        n = (Session_ME.this.readKey(by2) & 0xFF) << 8 | Session_ME.this.readKey((byte)n2) & 0xFF;
                    } else {
                        n = Session_ME.this.dis.readUnsignedShort();
                    }
                    byte[] byArray = new byte[n];
                    n2 = 0;
                    int n3 = 0;
                    while (n2 != -1 && n3 < n) {
                        n2 = Session_ME.this.dis.read(byArray, n3, n - n3);
                        if (n2 <= 0) continue;
                        Session_ME session_ME = Session_ME.this;
                        session_ME.recvByteCount += 5 + (n3 += n2);
                    }
                    if (Session_ME.this.getKeyComplete) {
                        for (int i = 0; i < byArray.length; ++i) {
                            byArray[i] = Session_ME.this.readKey(byArray[i]);
                        }
                    }
                    Message message = new Message(by, byArray);
                    return message;
                }
                return Session_ME.this.readMessage2(by);
            }
            catch (EOFException eOFException) {
                CRes.out("====> Session readMessage() method  EOF exception ");
            }
            catch (Exception exception) {
                CRes.out("exception ");
            }
            return null;
        }

        MessageCollector(MessageCollector messageCollector) {
            this();
        }
    }
}

