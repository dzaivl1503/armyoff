/*
 * Decompiled with CFR 0.152.
 */
package com.teamobi.mobiarmy2;

import java.util.Vector;

final class JsonLite {
    private JsonLite() {
    }

    static String getString(String string, String string2) {
        if (string == null) {
            return null;
        }
        int n = JsonLite.findKey(string, string2);
        if (n < 0) {
            return null;
        }
        int n2 = string.indexOf(58, n);
        if (n2 < 0) {
            return null;
        }
        int n3 = JsonLite.skipWs(string, n2 + 1);
        if (n3 >= string.length()) {
            return null;
        }
        if (string.charAt(n3) == '\"') {
            return JsonLite.parseString(string, n3);
        }
        return null;
    }

    static boolean getBoolean(String string, String string2) {
        if (string == null) {
            return false;
        }
        int n = JsonLite.findKey(string, string2);
        if (n < 0) {
            return false;
        }
        int n2 = string.indexOf(58, n);
        if (n2 < 0) {
            return false;
        }
        int n3 = JsonLite.skipWs(string, n2 + 1);
        return n3 < string.length() && JsonLite.matchesAt(string, n3, "true");
    }

    static String getObject(String string, String string2) {
        if (string == null) {
            return null;
        }
        int n = JsonLite.findKey(string, string2);
        if (n < 0) {
            return null;
        }
        int n2 = string.indexOf(58, n);
        if (n2 < 0) {
            return null;
        }
        int n3 = JsonLite.skipWs(string, n2 + 1);
        if (n3 < string.length() && string.charAt(n3) == '{') {
            return JsonLite.extractBraced(string, n3);
        }
        return null;
    }

    static String getArrayRaw(String string, String string2) {
        if (string == null) {
            return null;
        }
        int n = JsonLite.findKey(string, string2);
        if (n < 0) {
            return null;
        }
        int n2 = string.indexOf(58, n);
        if (n2 < 0) {
            return null;
        }
        int n3 = JsonLite.skipWs(string, n2 + 1);
        if (n3 < string.length() && string.charAt(n3) == '[') {
            return JsonLite.extractBracketed(string, n3);
        }
        return null;
    }

    static Vector splitArrayObjects(String string) {
        Vector<String> vector = new Vector<String>();
        if (string == null || string.length() < 2) {
            return vector;
        }
        int n = 1;
        int n2 = string.length() - 1;
        while (n < n2) {
            char c = string.charAt(n);
            if (c == '{') {
                String string2 = JsonLite.extractBraced(string, n);
                vector.addElement(string2);
                n += string2.length();
                continue;
            }
            ++n;
        }
        return vector;
    }

    private static String extractBracketed(String string, int n) {
        int n2;
        int n3 = 1;
        for (n2 = n + 1; n2 < string.length() && n3 > 0; ++n2) {
            char c = string.charAt(n2);
            if (c == '\"') {
                ++n2;
                while (n2 < string.length() && string.charAt(n2) != '\"') {
                    if (string.charAt(n2) == '\\') {
                        ++n2;
                    }
                    ++n2;
                }
                continue;
            }
            if (c == '[' || c == '{') {
                ++n3;
                continue;
            }
            if (c != ']' && c != '}') continue;
            --n3;
        }
        return string.substring(n, n2);
    }

    private static int findKey(String string, String string2) {
        String string3 = "\"" + string2 + "\"";
        int n = string3.length();
        int n2 = 0;
        block0: for (int i = 0; i < string.length(); ++i) {
            char c = string.charAt(i);
            if (c == '\"') {
                int n3;
                if (n2 == 1 && JsonLite.matchesAt(string, i, string3) && (n3 = JsonLite.skipWs(string, i + n)) < string.length() && string.charAt(n3) == ':') {
                    return i;
                }
                ++i;
                while (i < string.length()) {
                    n3 = string.charAt(i);
                    if (n3 == 92) {
                        i += 2;
                        continue;
                    }
                    if (n3 == 34) continue block0;
                    ++i;
                }
                continue;
            }
            if (c == '{' || c == '[') {
                ++n2;
                continue;
            }
            if (c != '}' && c != ']' || --n2 >= 0) continue;
            return -1;
        }
        return -1;
    }

    private static int skipWs(String string, int n) {
        char c;
        while (n < string.length() && ((c = string.charAt(n)) == ' ' || c == '\n' || c == '\r' || c == '\t')) {
            ++n;
        }
        return n;
    }

    private static String parseString(String string, int n) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = n + 1; i < string.length(); ++i) {
            char c = string.charAt(i);
            if (c == '\\' && i + 1 < string.length()) {
                char c2;
                if ((c2 = string.charAt(++i)) == 'n') {
                    stringBuffer.append('\n');
                    continue;
                }
                if (c2 == 'r') {
                    stringBuffer.append('\r');
                    continue;
                }
                if (c2 == 't') {
                    stringBuffer.append('\t');
                    continue;
                }
                stringBuffer.append(c2);
                continue;
            }
            if (c == '\"') {
                return stringBuffer.toString();
            }
            stringBuffer.append(c);
        }
        return stringBuffer.toString();
    }

    private static String extractBraced(String string, int n) {
        int n2;
        int n3 = 1;
        for (n2 = n + 1; n2 < string.length() && n3 > 0; ++n2) {
            char c = string.charAt(n2);
            if (c == '\"') {
                ++n2;
                while (n2 < string.length() && string.charAt(n2) != '\"') {
                    if (string.charAt(n2) == '\\') {
                        ++n2;
                    }
                    ++n2;
                }
                continue;
            }
            if (c == '{') {
                ++n3;
                continue;
            }
            if (c != '}') continue;
            --n3;
        }
        return string.substring(n, n2);
    }

    private static boolean matchesAt(String string, int n, String string2) {
        int n2 = n + string2.length();
        return n2 <= string.length() && string.substring(n, n2).equals(string2);
    }
}

