/*
 * Decompiled with CFR 0.152.
 */
package lib;

import java.io.InputStream;

public class MyStream {
    public static InputStream readFile(String string) {
        return "".getClass().getResourceAsStream(string);
    }
}

