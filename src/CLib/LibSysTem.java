/*
 * Decompiled with CFR 0.152.
 */
package CLib;

import com.teamobi.mobiarmy2.GameMidlet;
import java.io.InputStream;

public class LibSysTem {
    public static String res = "res";
    public static String font = "FontSys/x";

    public static InputStream openResource(String string) {
        try {
            return Class.forName("CLib.LibSysTem").getResourceAsStream(string);
        }
        catch (ClassNotFoundException classNotFoundException) {
            return null;
        }
    }

    public static InputStream getResourceAsStream(String string) {
        InputStream inputStream = LibSysTem.openResource("/" + res + string);
        if (inputStream == null) {
            throw new IllegalArgumentException("InputStream cannot found path= " + string);
        }
        return inputStream;
    }

    public static void openWeb(String string) {
        try {
            GameMidlet.instance.platformRequest(string);
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}

