import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;

public class Army2Server {
    private static final int PORT = 8080;

    private static final Map<String, String> userPasswords = new ConcurrentHashMap<>();
    private static final Map<String, String> tokenToUser = new ConcurrentHashMap<>();

    private static File getConfigFile() {
        File f1 = new File("config/missions.json");
        if (f1.exists()) return f1;
        File f2 = new File("server/config/missions.json");
        if (f2.exists()) return f2;
        return f1;
    }

    private static File getDataDir() {
        File f1 = new File("data");
        if (f1.exists()) return f1;
        File f2 = new File("server/data");
        if (f2.exists()) return f2;
        f1.mkdirs();
        return f1;
    }

    private static File getSavesDir() {
        File dir = new File(getDataDir(), "saves");
        if (!dir.exists()) dir.mkdirs();
        return dir;
    }

    private static File getUsersFile() {
        return new File(getDataDir(), "users.json");
    }

    public static void main(String[] args) {
        try {
            int port = PORT;
            if (args.length > 0) {
                try {
                    port = Integer.parseInt(args[0]);
                } catch (Exception ignored) {}
            }

            getDataDir();
            getSavesDir();
            loadUsers();

            HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
            server.setExecutor(Executors.newCachedThreadPool());

            // Routes
            server.createContext("/api/auth/register", new RegisterHandler());
            server.createContext("/api/auth/login", new LoginHandler());
            server.createContext("/api/game/config", new ConfigHandler());
            server.createContext("/api/missions/config", new ConfigHandler());
            server.createContext("/api/cloud-save", new CloudSaveHandler());
            server.createContext("/api/status", new StatusHandler());
            server.createContext("/", new RootHandler());

            server.start();
            System.out.println("=================================================");
            System.out.println("  MOBI ARMY 2 OFFLINE - HYBRID ADMIN SERVER");
            System.out.println("  Server is running at: http://localhost:" + port);
            System.out.println("  Config file: " + getConfigFile().getAbsolutePath());
            System.out.println("  Data folder: " + getDataDir().getAbsolutePath());
            System.out.println("=================================================");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static synchronized void loadUsers() {
        File file = getUsersFile();
        if (!file.exists()) return;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) sb.append(line);
            String json = sb.toString().trim();
            if (json.startsWith("{") && json.endsWith("}")) {
                json = json.substring(1, json.length() - 1);
                String[] pairs = json.split(",");
                for (String pair : pairs) {
                    int colon = pair.indexOf(':');
                    if (colon > 0) {
                        String u = cleanJsonStr(pair.substring(0, colon));
                        String p = cleanJsonStr(pair.substring(colon + 1));
                        if (!u.isEmpty()) {
                            userPasswords.put(u.toLowerCase(), p);
                        }
                    }
                }
            }
            System.out.println("[DB] Loaded " + userPasswords.size() + " accounts from users.json");
        } catch (Exception e) {
            System.err.println("[DB] Could not load users.json: " + e.getMessage());
        }
    }

    private static synchronized void saveUsers() {
        File file = getUsersFile();
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8))) {
            writer.write("{\n");
            int i = 0;
            int size = userPasswords.size();
            for (Map.Entry<String, String> entry : userPasswords.entrySet()) {
                writer.write("  \"" + escapeJson(entry.getKey()) + "\": \"" + escapeJson(entry.getValue()) + "\"");
                if (++i < size) writer.write(",");
                writer.write("\n");
            }
            writer.write("}\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String cleanJsonStr(String s) {
        s = s.trim();
        if (s.startsWith("\"") && s.endsWith("\"") && s.length() >= 2) {
            return s.substring(1, s.length() - 1);
        }
        return s;
    }

    private static String escapeJson(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "\\r");
    }

    private static String extractJsonField(String json, String field) {
        if (json == null) return "";
        String pattern = "\"" + field + "\"";
        int idx = json.indexOf(pattern);
        if (idx < 0) return "";
        int colon = json.indexOf(':', idx + pattern.length());
        if (colon < 0) return "";
        int startQuote = json.indexOf('"', colon);
        if (startQuote < 0) return "";
        int endQuote = json.indexOf('"', startQuote + 1);
        if (endQuote < 0) return "";
        return json.substring(startQuote + 1, endQuote);
    }

    private static String getUserFromToken(HttpExchange exchange) {
        String auth = exchange.getRequestHeaders().getFirst("Authorization");
        if (auth != null && auth.startsWith("Bearer ")) {
            String token = auth.substring(7).trim();
            return tokenToUser.get(token);
        }
        return null;
    }

    private static void sendJsonResponse(HttpExchange exchange, int status, String json) throws IOException {
        byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        exchange.sendResponseHeaders(status, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }

    private static void sendBinaryResponse(HttpExchange exchange, int status, byte[] bytes) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "application/octet-stream");
        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        exchange.sendResponseHeaders(status, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }

    private static byte[] readRequestBody(HttpExchange exchange) throws IOException {
        InputStream is = exchange.getRequestBody();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buf = new byte[4096];
        int n;
        while ((n = is.read(buf)) != -1) {
            baos.write(buf, 0, n);
        }
        return baos.toByteArray();
    }

    // Handlers
    static class RegisterHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
                exchange.getResponseHeaders().set("Access-Control-Allow-Methods", "POST, OPTIONS");
                exchange.getResponseHeaders().set("Access-Control-Allow-Headers", "*");
                exchange.sendResponseHeaders(204, -1);
                return;
            }
            if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                sendJsonResponse(exchange, 405, "{\"ok\":false,\"message\":\"Method not allowed\"}");
                return;
            }

            byte[] body = readRequestBody(exchange);
            String json = new String(body, StandardCharsets.UTF_8);
            String username = extractJsonField(json, "email");
            if (username.isEmpty()) username = extractJsonField(json, "username");
            String password = extractJsonField(json, "password");

            username = username.toLowerCase().trim();
            if (username.length() < 3 || password.length() < 3) {
                sendJsonResponse(exchange, 400, "{\"ok\":false,\"message\":\"Tên tài khoản và mật khẩu phải từ 3 ký tự trở lên!\"}");
                return;
            }

            if (userPasswords.containsKey(username)) {
                sendJsonResponse(exchange, 400, "{\"ok\":false,\"message\":\"Tài khoản này đã tồn tại, vui lòng chọn tên khác!\"}");
                return;
            }

            userPasswords.put(username, password);
            saveUsers();

            String token = UUID.randomUUID().toString();
            tokenToUser.put(token, username);

            System.out.println("[AUTH] Registered new account: " + username);
            sendJsonResponse(exchange, 200, "{\"ok\":true,\"token\":\"" + token + "\",\"data\":{\"token\":\"" + token + "\"},\"message\":\"Đăng ký tài khoản thành công!\"}");
        }
    }

    static class LoginHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
                exchange.getResponseHeaders().set("Access-Control-Allow-Methods", "POST, OPTIONS");
                exchange.getResponseHeaders().set("Access-Control-Allow-Headers", "*");
                exchange.sendResponseHeaders(204, -1);
                return;
            }
            if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                sendJsonResponse(exchange, 405, "{\"ok\":false,\"message\":\"Method not allowed\"}");
                return;
            }

            byte[] body = readRequestBody(exchange);
            String json = new String(body, StandardCharsets.UTF_8);
            String username = extractJsonField(json, "email");
            if (username.isEmpty()) username = extractJsonField(json, "username");
            String password = extractJsonField(json, "password");

            username = username.toLowerCase().trim();
            if (!userPasswords.containsKey(username) || !userPasswords.get(username).equals(password)) {
                sendJsonResponse(exchange, 401, "{\"ok\":false,\"message\":\"Sai tên tài khoản hoặc mật khẩu!\"}");
                return;
            }

            String token = UUID.randomUUID().toString();
            tokenToUser.put(token, username);

            System.out.println("[AUTH] User logged in: " + username);
            sendJsonResponse(exchange, 200, "{\"ok\":true,\"token\":\"" + token + "\",\"data\":{\"token\":\"" + token + "\"},\"message\":\"Đăng nhập thành công!\"}");
        }
    }

    static class ConfigHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
            File configFile = getConfigFile();
            if (configFile.exists()) {
                byte[] bytes = new byte[(int) configFile.length()];
                try (FileInputStream fis = new FileInputStream(configFile)) {
                    fis.read(bytes);
                }
                exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
                exchange.sendResponseHeaders(200, bytes.length);
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(bytes);
                }
            } else {
                sendJsonResponse(exchange, 404, "{\"ok\":false,\"message\":\"Config file not found\"}");
            }
        }
    }

    static class CloudSaveHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
            String method = exchange.getRequestMethod();
            String path = exchange.getRequestURI().getPath();

            if ("OPTIONS".equalsIgnoreCase(method)) {
                exchange.getResponseHeaders().set("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
                exchange.getResponseHeaders().set("Access-Control-Allow-Headers", "*");
                exchange.sendResponseHeaders(204, -1);
                return;
            }

            String username = getUserFromToken(exchange);
            if (username == null) {
                sendJsonResponse(exchange, 401, "{\"ok\":false,\"message\":\"Chưa đăng nhập hoặc phiên làm việc hết hạn!\"}");
                return;
            }

            File saveFile = new File(getSavesDir(), username + ".bin");

            if ("GET".equalsIgnoreCase(method)) {
                if (path.endsWith("/blob") || path.contains("/blob")) {
                    if (saveFile.exists()) {
                        byte[] bytes = new byte[(int) saveFile.length()];
                        try (FileInputStream fis = new FileInputStream(saveFile)) {
                            fis.read(bytes);
                        }
                        System.out.println("[SAVE] Downloaded save for " + username + " (" + bytes.length + " bytes)");
                        sendBinaryResponse(exchange, 200, bytes);
                    } else {
                        sendJsonResponse(exchange, 404, "{\"ok\":false,\"message\":\"Chưa có bản lưu nào trên máy chủ.\"}");
                    }
                } else {
                    boolean has = saveFile.exists();
                    String date = has ? new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(saveFile.lastModified())) : "";
                    String res = "{\"ok\":true,\"data\":[{\"id\":\"current\",\"name\":\"Bản lưu chính\",\"hasData\":" + has + ",\"createdAt\":\"" + date + "\"}]}";
                    sendJsonResponse(exchange, 200, res);
                }
            } else if ("PUT".equalsIgnoreCase(method) || "POST".equalsIgnoreCase(method)) {
                byte[] blob = readRequestBody(exchange);
                if (blob.length > 0) {
                    try (FileOutputStream fos = new FileOutputStream(saveFile)) {
                        fos.write(blob);
                    }
                    System.out.println("[SAVE] Uploaded and saved data for " + username + " (" + blob.length + " bytes)");
                    sendJsonResponse(exchange, 200, "{\"ok\":true,\"message\":\"Đã lưu và đồng bộ dữ liệu lên Server thành công!\"}");
                } else {
                    sendJsonResponse(exchange, 400, "{\"ok\":false,\"message\":\"Dữ liệu gửi lên trống.\"}");
                }
            } else {
                sendJsonResponse(exchange, 405, "{\"ok\":false,\"message\":\"Method not allowed\"}");
            }
        }
    }

    static class StatusHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            sendJsonResponse(exchange, 200, "{\"status\":\"ONLINE\",\"users\":" + userPasswords.size() + ",\"uptime\":\"OK\"}");
        }
    }

    static class RootHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String html = "<html><head><title>Army 2 Server</title><meta charset='utf-8'></head>" +
                    "<body style='font-family:sans-serif;text-align:center;padding:50px;background:#1e1e2e;color:#fff'>" +
                    "<h2>Mobi Army 2 Offline - Hybrid Admin Server</h2>" +
                    "<p style='color:#a6adc8'>Server đang hoạt động bình thường trên cổng " + PORT + "</p>" +
                    "<p><a href='/api/game/config' style='color:#89b4fa'>Xem file cấu hình Missions (JSON)</a></p>" +
                    "</body></html>";
            byte[] bytes = html.getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
            exchange.sendResponseHeaders(200, bytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(bytes);
            }
        }
    }
}
