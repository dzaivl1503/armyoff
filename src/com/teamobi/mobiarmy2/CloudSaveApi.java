/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  javax.microedition.io.Connector
 *  javax.microedition.io.HttpConnection
 */
package com.teamobi.mobiarmy2;

import CLib.RMS;
import com.teamobi.mobiarmy2.JsonLite;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Vector;
import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;
import model.IAction2;

public final class CloudSaveApi {
    private static final String BASE_URL = "https://emu.cheehouse.io.vn/api";
    private static final String USER_AGENT = "MobiArmy2Offline/1.0";
    private static final String TOKEN_KEY = "cloudToken";
    private static final String EMAIL_KEY = "cloudEmail";

    private CloudSaveApi() {
    }

    public static boolean isLoggedIn() {
        String string = RMS.loadRMSString(TOKEN_KEY);
        return string != null && string.length() > 0;
    }

    public static String getLinkedEmail() {
        String string = RMS.loadRMSString(EMAIL_KEY);
        return string == null ? "" : string;
    }

    public static void logout() {
        RMS.clearRMS(TOKEN_KEY);
        RMS.clearRMS(EMAIL_KEY);
    }

    public static void login(final String string, final String string2, final IAction2 iAction2) {
        new Thread(new Runnable(){

            public void run() {
                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append("{\"email\":\"").append(CloudSaveApi.jsonEscape(string)).append("\",\"password\":\"").append(CloudSaveApi.jsonEscape(string2)).append("\"}");
                RawResponse rawResponse = CloudSaveApi.request("POST", "/auth/login", CloudSaveApi.toUtf8(stringBuffer.toString()), "application/json", false, null, null);
                Result result = new Result();
                if (rawResponse == null) {
                    result.ok = false;
                    result.error = "Kh\u00f4ng th\u1ec3 k\u1ebft n\u1ed1i m\u00e1y ch\u1ee7.";
                } else if (rawResponse.status >= 200 && rawResponse.status < 300) {
                    String string3 = JsonLite.getObject(CloudSaveApi.fromUtf8(rawResponse.body), "data");
                    String string22 = JsonLite.getString(string3, "token");
                    if (string22 == null || string22.length() == 0) {
                        result.ok = false;
                        result.error = "Ph\u1ea3n h\u1ed3i m\u00e1y ch\u1ee7 kh\u00f4ng h\u1ee3p l\u1ec7.";
                    } else {
                        RMS.saveRMSString(CloudSaveApi.TOKEN_KEY, string22);
                        RMS.saveRMSString(CloudSaveApi.EMAIL_KEY, string);
                        result.ok = true;
                    }
                } else {
                    result.ok = false;
                    result.error = CloudSaveApi.errorMessage(rawResponse);
                }
                iAction2.perform(result);
            }
        }).start();
    }

    public static void uploadSave(final String string, final byte[] byArray, final IAction2 iAction2) {
        new Thread(new Runnable(){

            public void run() {
                RawResponse rawResponse = CloudSaveApi.request("PUT", "/cloud-save/blob", byArray, "application/octet-stream", true, "X-Save-Name", CloudSaveApi.percentEncodeUtf8(string == null ? "" : string));
                Result result = new Result();
                if (rawResponse == null) {
                    result.ok = false;
                    result.error = "Kh\u00f4ng th\u1ec3 k\u1ebft n\u1ed1i m\u00e1y ch\u1ee7.";
                } else if (rawResponse.status >= 200 && rawResponse.status < 300) {
                    result.ok = true;
                } else {
                    result.ok = false;
                    result.error = CloudSaveApi.errorMessage(rawResponse);
                }
                iAction2.perform(result);
            }
        }).start();
    }

    public static void downloadSave(final String string, final IAction2 iAction2) {
        new Thread(new Runnable(){

            public void run() {
                RawResponse rawResponse = CloudSaveApi.request("GET", "/cloud-save/" + string + "/blob", null, null, true, null, null);
                Result result = new Result();
                if (rawResponse == null) {
                    result.ok = false;
                    result.error = "Kh\u00f4ng th\u1ec3 k\u1ebft n\u1ed1i m\u00e1y ch\u1ee7.";
                } else if (rawResponse.status >= 200 && rawResponse.status < 300) {
                    result.ok = true;
                    result.data = rawResponse.body;
                } else {
                    result.ok = false;
                    result.error = CloudSaveApi.errorMessage(rawResponse);
                }
                iAction2.perform(result);
            }
        }).start();
    }

    public static void deleteSave(final String string, final IAction2 iAction2) {
        new Thread(new Runnable(){

            public void run() {
                RawResponse rawResponse = CloudSaveApi.request("POST", "/cloud-save/" + string + "/delete", null, null, true, null, null);
                Result result = new Result();
                if (rawResponse == null) {
                    result.ok = false;
                    result.error = "Kh\u00f4ng th\u1ec3 k\u1ebft n\u1ed1i m\u00e1y ch\u1ee7.";
                } else if (rawResponse.status >= 200 && rawResponse.status < 300) {
                    result.ok = true;
                } else {
                    result.ok = false;
                    result.error = CloudSaveApi.errorMessage(rawResponse);
                }
                iAction2.perform(result);
            }
        }).start();
    }

    public static void listSaves(final IAction2 iAction2) {
        new Thread(new Runnable(){

            public void run() {
                RawResponse rawResponse = CloudSaveApi.request("GET", "/cloud-save", null, null, true, null, null);
                Result result = new Result();
                if (rawResponse == null) {
                    result.ok = false;
                    result.error = "Kh\u00f4ng th\u1ec3 k\u1ebft n\u1ed1i m\u00e1y ch\u1ee7.";
                } else if (rawResponse.status >= 200 && rawResponse.status < 300) {
                    String string = CloudSaveApi.fromUtf8(rawResponse.body);
                    String string2 = JsonLite.getArrayRaw(string, "data");
                    Vector vector = JsonLite.splitArrayObjects(string2);
                    Vector<CloudSaveEntry> vector2 = new Vector<CloudSaveEntry>();
                    for (int i = 0; i < vector.size(); ++i) {
                        String string3 = (String)vector.elementAt(i);
                        CloudSaveEntry cloudSaveEntry = new CloudSaveEntry();
                        cloudSaveEntry.id = JsonLite.getString(string3, "id");
                        cloudSaveEntry.name = JsonLite.getString(string3, "name");
                        cloudSaveEntry.hasData = JsonLite.getBoolean(string3, "hasData");
                        cloudSaveEntry.createdAt = JsonLite.getString(string3, "createdAt");
                        vector2.addElement(cloudSaveEntry);
                    }
                    result.ok = true;
                    result.entries = vector2;
                } else {
                    result.ok = false;
                    result.error = CloudSaveApi.errorMessage(rawResponse);
                }
                iAction2.perform(result);
            }
        }).start();
    }

    private static String errorMessage(RawResponse rawResponse) {
        String string = JsonLite.getString(CloudSaveApi.fromUtf8(rawResponse.body), "message");
        return string != null && string.length() > 0 ? string : "L\u1ed7i m\u00e1y ch\u1ee7 (" + rawResponse.status + ").";
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private static RawResponse request(String string, String string2, byte[] byArray, String string3, boolean bl, String string4, String string5) {
        HttpConnection httpConnection = null;
        OutputStream outputStream = null;
        InputStream inputStream = null;
        try {
            Object object;
            httpConnection = (HttpConnection)Connector.open((String)(BASE_URL + string2), (int)3, (boolean)true);
            try {
                httpConnection.setRequestMethod(string);
            }
            catch (IllegalArgumentException illegalArgumentException) {
                string = "POST";
                httpConnection.setRequestMethod(string);
            }
            httpConnection.setRequestProperty("User-Agent", USER_AGENT);
            httpConnection.setRequestProperty("Accept", "application/json");
            httpConnection.setRequestProperty("Connection", "close");
            if (string3 != null) {
                httpConnection.setRequestProperty("Content-Type", string3);
            }
            if (string4 != null) {
                httpConnection.setRequestProperty(string4, string5);
            }
            if (bl && (object = RMS.loadRMSString(TOKEN_KEY)) != null && ((String)object).length() > 0) {
                httpConnection.setRequestProperty("Authorization", "Bearer " + (String)object);
            }
            if (byArray != null) {
                httpConnection.setRequestProperty("Content-Length", String.valueOf(byArray.length));
                outputStream = httpConnection.openOutputStream();
                outputStream.write(byArray);
                outputStream.flush();
            }
            object = new RawResponse();
            ((RawResponse)object).status = httpConnection.getResponseCode();
            try {
                inputStream = httpConnection.openInputStream();
                ((RawResponse)object).body = CloudSaveApi.readAll(inputStream);
            }
            catch (Exception exception) {
                ((RawResponse)object).body = new byte[0];
            }
            RawResponse rawResponse2 = (RawResponse)object;
            return rawResponse2;
        }
        catch (Exception exception) {
            RawResponse rawResponse = null;
            return rawResponse;
        }
        finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
            }
            catch (Exception exception) {}
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            }
            catch (Exception exception) {}
            try {
                if (httpConnection != null) {
                    httpConnection.close();
                }
            }
            catch (Exception exception) {}
        }
    }

    private static byte[] readAll(InputStream inputStream) throws IOException {
        int n;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] byArray = new byte[1024];
        while ((n = inputStream.read(byArray)) != -1) {
            byteArrayOutputStream.write(byArray, 0, n);
        }
        return byteArrayOutputStream.toByteArray();
    }

    private static byte[] toUtf8(String string) {
        try {
            return string.getBytes("UTF-8");
        }
        catch (Exception exception) {
            return string.getBytes();
        }
    }

    private static String fromUtf8(byte[] byArray) {
        if (byArray == null) {
            return "";
        }
        try {
            return new String(byArray, "UTF-8");
        }
        catch (Exception exception) {
            return new String(byArray);
        }
    }

    private static String jsonEscape(String string) {
        if (string == null) {
            return "";
        }
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < string.length(); ++i) {
            char c = string.charAt(i);
            if (c == '\"' || c == '\\') {
                stringBuffer.append('\\').append(c);
                continue;
            }
            if (c == '\n') {
                stringBuffer.append("\\n");
                continue;
            }
            if (c == '\r') {
                stringBuffer.append("\\r");
                continue;
            }
            if (c == '\t') {
                stringBuffer.append("\\t");
                continue;
            }
            if (c < ' ') continue;
            stringBuffer.append(c);
        }
        return stringBuffer.toString();
    }

    private static String percentEncodeUtf8(String string) {
        byte[] byArray = CloudSaveApi.toUtf8(string);
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < byArray.length; ++i) {
            boolean bl;
            int n = byArray[i] & 0xFF;
            boolean bl2 = bl = n >= 65 && n <= 90 || n >= 97 && n <= 122 || n >= 48 && n <= 57 || n == 45 || n == 95 || n == 46 || n == 126;
            if (bl) {
                stringBuffer.append((char)n);
                continue;
            }
            stringBuffer.append('%');
            String string2 = Integer.toHexString(n).toUpperCase();
            if (string2.length() < 2) {
                stringBuffer.append('0');
            }
            stringBuffer.append(string2);
        }
        return stringBuffer.toString();
    }

    private static final class RawResponse {
        int status;
        byte[] body;

        private RawResponse() {
        }
    }

    public static final class CloudSaveEntry {
        public String id;
        public String name;
        public boolean hasData;
        public String createdAt;
    }

    public static final class Result {
        public boolean ok;
        public String error;
        public byte[] data;
        public boolean flag;
        public Vector entries;
    }
}

