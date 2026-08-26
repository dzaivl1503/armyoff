/*
 * Decompiled with CFR 0.152.
 */
package model;

import java.io.InputStream;

public class GetString {
    public static byte[] array;
    public static String strdata;

    public GetString() {
        InputStream inputStream = this.getClass().getResourceAsStream("/agent.txt");
        try {
            array = new byte[inputStream.available()];
            inputStream.read(array);
            strdata = new String(array, "UTF-8");
        }
        catch (Exception exception) {
            strdata = "";
        }
    }
}

