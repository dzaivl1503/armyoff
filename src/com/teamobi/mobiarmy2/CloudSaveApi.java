/*
 * Mobi Army 2 Offline Cloud Save API & Server Sync Client
 */
package com.teamobi.mobiarmy2;

import CLib.RMS;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Vector;
import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;
import model.IAction2;

public final class CloudSaveApi {
    public static final String DEFAULT_BASE_URL = "http://160.191.242.130:8080/api";
    private static final String USER_AGENT = "MobiArmy2Offline/1.0";
    private static final String TOKEN_KEY = "cloudToken";
    private static final String EMAIL_KEY = "cloudEmail";
    private static final String SERVER_URL_KEY = "cloudServerUrl";

    private static boolean isSyncing = false;

    private CloudSaveApi() {
    }

    public static String getServerUrl() {
        String url = RMS.loadRMSString(SERVER_URL_KEY);
        if (url == null || url.trim().length() == 0) {
            return DEFAULT_BASE_URL;
        }
        return url.trim();
    }

    public static void setServerUrl(String url) {
        if (url == null || url.trim().length() == 0) {
            RMS.saveRMSString(SERVER_URL_KEY, DEFAULT_BASE_URL);
        } else {
            String clean = url.trim();
            if (clean.endsWith("/")) {
                clean = clean.substring(0, clean.length() - 1);
            }
            if (!clean.endsWith("/api")) {
                clean = clean + "/api";
            }
            RMS.saveRMSString(SERVER_URL_KEY, clean);
        }
    }

    public static boolean isLoggedIn() {
        String token = RMS.loadRMSString(TOKEN_KEY);
        return token != null && token.length() > 0;
    }

    public static String getLinkedEmail() {
        String email = RMS.loadRMSString(EMAIL_KEY);
        return email == null ? "" : email;
    }

    public static void logout() {
        RMS.clearRMS(TOKEN_KEY);
        RMS.clearRMS(EMAIL_KEY);
    }

    public static void register(final String username, final String password, final IAction2 callback) {
        new Thread(new Runnable() {
            public void run() {
                StringBuffer sb = new StringBuffer();
                sb.append("{\"email\":\"").append(CloudSaveApi.jsonEscape(username))
                  .append("\",\"password\":\"").append(CloudSaveApi.jsonEscape(password)).append("\"}");

                RawResponse resp = CloudSaveApi.request("POST", "/auth/register", CloudSaveApi.toUtf8(sb.toString()), "application/json", false, null, null);
                Result res = new Result();
                if (resp == null) {
                    res.ok = false;
                    res.error = "Không thể kết nối máy chủ (" + getServerUrl() + ").";
                } else if (resp.status >= 200 && resp.status < 300) {
                    String body = CloudSaveApi.fromUtf8(resp.body);
                    String data = JsonLite.getObject(body, "data");
                    String token = JsonLite.getString(data, "token");
                    if (token == null || token.length() == 0) {
                        token = JsonLite.getString(body, "token");
                    }
                    if (token == null || token.length() == 0) {
                        res.ok = false;
                        res.error = "Phản hồi máy chủ không hợp lệ.";
                    } else {
                        RMS.saveRMSString(TOKEN_KEY, token);
                        RMS.saveRMSString(EMAIL_KEY, username);
                        res.ok = true;
                        res.error = "Đăng ký thành công!";
                        // Link mission
                        OfflineMission.onCloudAccountLinked();
                    }
                } else {
                    res.ok = false;
                    res.error = CloudSaveApi.errorMessage(resp);
                }
                if (callback != null) callback.perform(res);
            }
        }).start();
    }

    public static void login(final String username, final String password, final IAction2 callback) {
        new Thread(new Runnable() {
            public void run() {
                StringBuffer sb = new StringBuffer();
                sb.append("{\"email\":\"").append(CloudSaveApi.jsonEscape(username))
                  .append("\",\"password\":\"").append(CloudSaveApi.jsonEscape(password)).append("\"}");

                RawResponse resp = CloudSaveApi.request("POST", "/auth/login", CloudSaveApi.toUtf8(sb.toString()), "application/json", false, null, null);
                Result res = new Result();
                if (resp == null) {
                    res.ok = false;
                    res.error = "Không thể kết nối máy chủ (" + getServerUrl() + ").";
                } else if (resp.status >= 200 && resp.status < 300) {
                    String body = CloudSaveApi.fromUtf8(resp.body);
                    String data = JsonLite.getObject(body, "data");
                    String token = JsonLite.getString(data, "token");
                    if (token == null || token.length() == 0) {
                        token = JsonLite.getString(body, "token");
                    }
                    if (token == null || token.length() == 0) {
                        res.ok = false;
                        res.error = "Phản hồi máy chủ không hợp lệ.";
                    } else {
                        RMS.saveRMSString(TOKEN_KEY, token);
                        RMS.saveRMSString(EMAIL_KEY, username);
                        res.ok = true;
                        // Link mission
                        OfflineMission.onCloudAccountLinked();
                        // Auto fetch remote mission config
                        fetchRemoteConfig(null);
                    }
                } else {
                    res.ok = false;
                    res.error = CloudSaveApi.errorMessage(resp);
                }
                if (callback != null) callback.perform(res);
            }
        }).start();
    }

    public static void fetchRemoteConfig(final IAction2 callback) {
        new Thread(new Runnable() {
            public void run() {
                RawResponse resp = CloudSaveApi.request("GET", "/game/config", null, null, false, null, null);
                Result res = new Result();
                if (resp != null && resp.status >= 200 && resp.status < 300) {
                    String json = CloudSaveApi.fromUtf8(resp.body);
                    OfflineMission.applyRemoteConfig(json);
                    res.ok = true;
                } else {
                    res.ok = false;
                }
                if (callback != null) callback.perform(res);
            }
        }).start();
    }

    public static void uploadCurrentSave(final IAction2 callback) {
        byte[] bytes = OfflineSave.exportBytes();
        uploadSave(getLinkedEmail(), bytes, callback);
    }

    public static void downloadCurrentSave(final IAction2 callback) {
        downloadSave("current", new IAction2() {
            public void perform(Object obj) {
                Result res = (Result) obj;
                if (res != null && res.ok && res.data != null && res.data.length > 0) {
                    OfflineSave.importBytes(res.data);
                }
                if (callback != null) callback.perform(res);
            }
        });
    }

    public static void syncSaveSilently() {
        if (!isLoggedIn() || isSyncing) {
            return;
        }
        isSyncing = true;
        new Thread(new Runnable() {
            public void run() {
                try {
                    byte[] bytes = OfflineSave.exportBytes();
                    if (bytes != null && bytes.length > 0) {
                        CloudSaveApi.request("PUT", "/cloud-save/blob", bytes, "application/octet-stream", true, "X-Save-Name", CloudSaveApi.percentEncodeUtf8(getLinkedEmail()));
                    }
                } catch (Exception ignored) {
                } finally {
                    isSyncing = false;
                }
            }
        }).start();
    }

    public static void manualSaveAndSync(final IAction2 callback) {
        // 1. Instant local RMS save
        OfflineSave.save();

        if (!isLoggedIn()) {
            Result res = new Result();
            res.ok = true;
            res.flag = false; // local only
            res.error = "Đã lưu vào máy (RMS). Chưa đăng nhập tài khoản Cloud để đồng bộ lên Server.";
            if (callback != null) callback.perform(res);
            return;
        }

        byte[] bytes = OfflineSave.exportBytes();
        uploadSave(getLinkedEmail(), bytes, new IAction2() {
            public void perform(Object obj) {
                Result res = (Result) obj;
                if (res != null && res.ok) {
                    res.flag = true; // synced with server
                    res.error = "Đã lưu vào máy và đồng bộ lên Server thành công!";
                } else if (res != null) {
                    res.flag = false;
                    res.error = "Đã lưu vào máy (RMS), nhưng không thể gửi lên Server: " + res.error;
                }
                if (callback != null) callback.perform(res);
            }
        });
    }

    public static void uploadSave(final String saveName, final byte[] data, final IAction2 callback) {
        new Thread(new Runnable() {
            public void run() {
                String uName = (saveName != null && saveName.length() > 0) ? saveName : getLinkedEmail();
                int len = data != null ? data.length : 0;
                System.out.println("[CLIENT SAVE] Uploading save for `" + uName + "` (" + len + " bytes) to " + getServerUrl() + "/cloud-save/blob");
                RawResponse resp = CloudSaveApi.request("PUT", "/cloud-save/blob", data, "application/octet-stream", true, "X-Save-Name", CloudSaveApi.percentEncodeUtf8(uName == null ? "" : uName));
                Result res = new Result();
                if (resp == null) {
                    System.err.println("[CLIENT SAVE] Connection failed to " + getServerUrl());
                    res.ok = false;
                    res.error = "Không thể kết nối máy chủ (" + getServerUrl() + ").";
                } else if (resp.status >= 200 && resp.status < 300) {
                    System.out.println("[CLIENT SAVE] Upload SUCCESS! Response HTTP " + resp.status);
                    res.ok = true;
                } else {
                    String err = CloudSaveApi.errorMessage(resp);
                    System.err.println("[CLIENT SAVE] Upload FAILED: HTTP " + resp.status + " -> " + err);
                    res.ok = false;
                    res.error = err;
                }
                if (callback != null) callback.perform(res);
            }
        }).start();
    }

    public static void downloadSave(final String saveId, final IAction2 callback) {
        new Thread(new Runnable() {
            public void run() {
                RawResponse resp = CloudSaveApi.request("GET", "/cloud-save/" + saveId + "/blob", null, null, true, null, null);
                Result res = new Result();
                if (resp == null) {
                    res.ok = false;
                    res.error = "Không thể kết nối máy chủ (" + getServerUrl() + ").";
                } else if (resp.status >= 200 && resp.status < 300) {
                    res.ok = true;
                    res.data = resp.body;
                } else {
                    res.ok = false;
                    res.error = CloudSaveApi.errorMessage(resp);
                }
                if (callback != null) callback.perform(res);
            }
        }).start();
    }

    public static void deleteSave(final String saveId, final IAction2 callback) {
        new Thread(new Runnable() {
            public void run() {
                RawResponse resp = CloudSaveApi.request("POST", "/cloud-save/" + saveId + "/delete", null, null, true, null, null);
                Result res = new Result();
                if (resp == null) {
                    res.ok = false;
                    res.error = "Không thể kết nối máy chủ.";
                } else if (resp.status >= 200 && resp.status < 300) {
                    res.ok = true;
                } else {
                    res.ok = false;
                    res.error = CloudSaveApi.errorMessage(resp);
                }
                if (callback != null) callback.perform(res);
            }
        }).start();
    }

    public static void listSaves(final IAction2 callback) {
        new Thread(new Runnable() {
            public void run() {
                RawResponse resp = CloudSaveApi.request("GET", "/cloud-save", null, null, true, null, null);
                Result res = new Result();
                if (resp == null) {
                    res.ok = false;
                    res.error = "Không thể kết nối máy chủ.";
                } else if (resp.status >= 200 && resp.status < 300) {
                    String json = CloudSaveApi.fromUtf8(resp.body);
                    String dataRaw = JsonLite.getArrayRaw(json, "data");
                    Vector list = JsonLite.splitArrayObjects(dataRaw);
                    Vector entries = new Vector();
                    for (int i = 0; i < list.size(); ++i) {
                        String s = (String) list.elementAt(i);
                        CloudSaveEntry entry = new CloudSaveEntry();
                        entry.id = JsonLite.getString(s, "id");
                        entry.name = JsonLite.getString(s, "name");
                        entry.hasData = JsonLite.getBoolean(s, "hasData");
                        entry.createdAt = JsonLite.getString(s, "createdAt");
                        entries.addElement(entry);
                    }
                    res.ok = true;
                    res.entries = entries;
                } else {
                    res.ok = false;
                    res.error = CloudSaveApi.errorMessage(resp);
                }
                if (callback != null) callback.perform(res);
            }
        }).start();
    }

    private static String errorMessage(RawResponse resp) {
        if (resp == null || resp.body == null || resp.body.length == 0) {
            return "Lỗi máy chủ (" + (resp != null ? resp.status : -1) + ").";
        }
        String msg = JsonLite.getString(CloudSaveApi.fromUtf8(resp.body), "message");
        return msg != null && msg.length() > 0 ? msg : "Lỗi máy chủ (" + resp.status + ").";
    }

    private static RawResponse request(String method, String path, byte[] body, String contentType, boolean needAuth, String customHeaderKey, String customHeaderVal) {
        HttpConnection conn = null;
        OutputStream os = null;
        InputStream is = null;
        try {
            String fullUrl = getServerUrl() + path;
            conn = (HttpConnection) Connector.open(fullUrl, 3, true);
            try {
                conn.setRequestMethod(method);
            } catch (IllegalArgumentException e) {
                conn.setRequestMethod("POST");
            }
            conn.setRequestProperty("User-Agent", USER_AGENT);
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Connection", "close");
            if (contentType != null) {
                conn.setRequestProperty("Content-Type", contentType);
            }
            if (customHeaderKey != null) {
                conn.setRequestProperty(customHeaderKey, customHeaderVal);
            }
            if (needAuth) {
                String token = RMS.loadRMSString(TOKEN_KEY);
                if (token != null && token.length() > 0) {
                    conn.setRequestProperty("Authorization", "Bearer " + token);
                }
                String email = RMS.loadRMSString(EMAIL_KEY);
                if (email != null && email.length() > 0) {
                    conn.setRequestProperty("X-User-Name", email.trim());
                }
            }
            if (body != null) {
                conn.setRequestProperty("Content-Length", String.valueOf(body.length));
                os = conn.openOutputStream();
                os.write(body);
                os.flush();
            }
            RawResponse raw = new RawResponse();
            raw.status = conn.getResponseCode();
            try {
                is = conn.openInputStream();
                raw.body = CloudSaveApi.readAll(is);
            } catch (Exception e) {
                raw.body = new byte[0];
            }
            return raw;
        } catch (Exception e) {
            return null;
        } finally {
            try { if (os != null) os.close(); } catch (Exception ignored) {}
            try { if (is != null) is.close(); } catch (Exception ignored) {}
            try { if (conn != null) conn.close(); } catch (Exception ignored) {}
        }
    }

    private static byte[] readAll(InputStream is) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        int n;
        while ((n = is.read(buf)) != -1) {
            baos.write(buf, 0, n);
        }
        return baos.toByteArray();
    }

    private static byte[] toUtf8(String s) {
        try {
            return s.getBytes("UTF-8");
        } catch (Exception e) {
            return s.getBytes();
        }
    }

    private static String fromUtf8(byte[] b) {
        if (b == null) return "";
        try {
            return new String(b, "UTF-8");
        } catch (Exception e) {
            return new String(b);
        }
    }

    private static String jsonEscape(String s) {
        if (s == null) return "";
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < s.length(); ++i) {
            char c = s.charAt(i);
            if (c == '\"' || c == '\\') {
                sb.append('\\').append(c);
            } else if (c == '\n') {
                sb.append("\\n");
            } else if (c == '\r') {
                sb.append("\\r");
            } else if (c == '\t') {
                sb.append("\\t");
            } else if (c >= ' ') {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    private static String percentEncodeUtf8(String s) {
        byte[] bytes = CloudSaveApi.toUtf8(s);
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < bytes.length; ++i) {
            int n = bytes[i] & 0xFF;
            boolean safe = (n >= 65 && n <= 90) || (n >= 97 && n <= 122) || (n >= 48 && n <= 57) || n == 45 || n == 95 || n == 46 || n == 126;
            if (safe) {
                sb.append((char) n);
            } else {
                sb.append('%');
                String hex = Integer.toHexString(n).toUpperCase();
                if (hex.length() < 2) sb.append('0');
                sb.append(hex);
            }
        }
        return sb.toString();
    }

    private static final class RawResponse {
        int status;
        byte[] body;
        private RawResponse() {}
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
