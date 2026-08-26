/*
 * Decompiled with CFR 0.152.
 */
package model;

import CLib.mImage;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.File;
import java.io.FileInputStream;
import model.CRes;
import model.IAction2;

public class FilePack {
    private String[] fname;
    private int[] fpos;
    private int[] flen;
    private byte[] fullData;
    private int nFile;
    private int hSize;
    private String name;
    private byte[] code = new byte[]{78, 103, 117, 121, 101, 110, 86, 97, 110, 77, 105, 110, 104};
    private int codeLen = this.code.length;
    private DataInputStream file;

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public FilePack(byte[] byArray) throws Exception {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byArray);
        this.file = new DataInputStream(byteArrayInputStream);
        int n = 0;
        int n2 = 0;
        this.hSize = 0;
        try {
            this.nFile = this.file.readUnsignedByte();
            ++this.hSize;
            this.fname = new String[this.nFile];
            this.fpos = new int[this.nFile];
            this.flen = new int[this.nFile];
            for (int i = 0; i < this.nFile; ++i) {
                int n3 = this.encode(this.file.readByte());
                if (n3 < 0) {
                    throw new IOException("Invalid FilePack entry name length: " + n3);
                }
                byte[] byArray2 = new byte[n3];
                this.file.read(byArray2);
                this.encode(byArray2);
                this.fname[i] = new String(byArray2);
                this.fpos[i] = n;
                this.flen[i] = this.encode(this.file.readUnsignedShort());
                n += this.flen[i];
                n2 += this.flen[i];
                this.hSize += n3 + 3;
            }
            this.fullData = new byte[n2];
            this.file.readFully(this.fullData);
            this.encode(this.fullData);
        }
        catch (EOFException eOFException) {
            CRes.out("=====================>  11Cause: " + eOFException.getClass());
            CRes.out("=====================>  11Cause: " + eOFException.getMessage());
        }
        catch (IOException iOException) {
            CRes.out("=====================>  Cause: " + iOException.getClass());
            CRes.out("=====================>  Cause: " + iOException.getMessage());
        }
        finally {
            if (this.file != null) {
                this.file.close();
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public FilePack(String string) throws Exception {
        int n = 0;
        int n2 = 0;
        this.name = string;
        this.hSize = 0;
        this.open();
        if (this.file == null) {
            this.fname = new String[0];
            this.fpos = new int[0];
            this.flen = new int[0];
            this.fullData = new byte[0];
            return;
        }
        try {
            this.nFile = this.encode(this.file.readUnsignedByte());
            ++this.hSize;
            this.fname = new String[this.nFile];
            this.fpos = new int[this.nFile];
            this.flen = new int[this.nFile];
            for (int i = 0; i < this.nFile; ++i) {
                int n3 = this.encode(this.file.readByte());
                if (n3 < 0) {
                    throw new IOException("Invalid FilePack entry name length: " + n3);
                }
                byte[] byArray = new byte[n3];
                this.file.read(byArray);
                this.encode(byArray);
                this.fname[i] = new String(byArray);
                this.fpos[i] = n;
                this.flen[i] = this.encode(this.file.readUnsignedShort());
                n += this.flen[i];
                n2 += this.flen[i];
                this.hSize += n3 + 3;
            }
            this.fullData = new byte[n2];
            this.file.readFully(this.fullData);
            this.encode(this.fullData);
        }
        catch (IOException iOException) {
        }
        finally {
            if (this.file != null) {
                this.file.close();
            }
        }
    }

    public FilePack(String string, boolean bl) {
    }

    private int encode(int n) {
        return n;
    }

    private void encode(byte[] byArray) {
        int n = byArray.length;
        for (int i = 0; i < n; ++i) {
            int n2 = i;
            byArray[n2] = (byte)(byArray[n2] ^ this.code[i % this.codeLen]);
        }
    }

    private void open() {
        try {
            String path = this.name;
            if (path == null) return;
            if (!path.startsWith("/")) path = "/" + path;
            InputStream inputStream = FilePack.class.getResourceAsStream(path);
            if (inputStream == null && path.startsWith("/res/")) {
                inputStream = FilePack.class.getResourceAsStream(path.substring(4));
            }
            if (inputStream == null && !path.startsWith("/res/")) {
                inputStream = FilePack.class.getResourceAsStream("/res" + path);
            }
            if (inputStream == null) {
                String cl = path.startsWith("/") ? path.substring(1) : path;
                inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(cl);
            }
            if (inputStream == null && path.startsWith("/res/")) {
                String cl = path.substring(5);
                inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(cl);
            }
            if (inputStream == null) {
                File f = new File(path.startsWith("/") ? path.substring(1) : path);
                if (f.exists()) inputStream = new FileInputStream(f);
            }
            if (inputStream != null) {
                this.file = new DataInputStream(inputStream);
            } else {
                System.err.println("[FilePack] Resource not found: " + this.name);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void openbyArray(byte[] byArray) {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byArray);
        this.file = new DataInputStream(byteArrayInputStream);
    }

    public byte[] loadFile(String string) throws Exception {
        for (int i = 0; i < this.nFile; ++i) {
            if (this.fname[i].compareTo(string) != 0) continue;
            byte[] byArray = new byte[this.flen[i]];
            System.arraycopy(this.fullData, this.fpos[i], byArray, 0, this.flen[i]);
            return byArray;
        }
        throw new Exception("File '" + string + "' not found!");
    }

    public mImage loadImage(String string) {
        if (string == null) {
            return null;
        }
        if (string == "") {
            return null;
        }
        for (int i = 0; i < this.nFile; ++i) {
            if (this.fname[i].compareTo(string) != 0) continue;
            return mImage.createImage(this.fullData, this.fpos[i], this.flen[i], string);
        }
        return null;
    }

    public mImage loadImage(String string, IAction2 iAction2) {
        for (int i = 0; i < this.nFile; ++i) {
            if (this.fname[i].compareTo(string) != 0) continue;
            return mImage.createImage(this.fullData, this.fpos[i], this.flen[i], iAction2);
        }
        return null;
    }

    public mImage loadImage(String string, boolean bl) {
        if (!bl) {
            return null;
        }
        for (int i = 0; i < this.nFile; ++i) {
            if (this.fname[i].compareTo(string) != 0) continue;
            return mImage.createImage(this.fullData, this.fpos[i], this.flen[i], true);
        }
        return null;
    }

    public void LoadAllNameInFile() {
        for (int i = 0; i < this.nFile; ++i) {
            System.out.println("Name File " + this.fname[i]);
        }
    }

    public String FileName(int n) {
        return this.fname[n];
    }

    public int lenght() {
        return this.fname.length;
    }

    public mImage loadImage(String string, String string2) {
        if (string == null) {
            return null;
        }
        if (string == "") {
            return null;
        }
        for (int i = 0; i < this.nFile; ++i) {
            if (this.fname[i].compareTo(string) != 0) continue;
            return mImage.createImage(this.fullData, this.fpos[i], this.flen[i], string, string2);
        }
        return null;
    }
}

