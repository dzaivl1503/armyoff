import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;

public class Army2Server {
    private static int PORT = 8080;
    private static String ADMIN_PASSWORD = "admin123";
    private static int STARTER_XU = 50000;
    private static int STARTER_LUONG = 100;
    private static double EVENT_EXP_RATE = 1.0;
    private static String DB_TYPE = "sqlite";

    private static final Map<String, String> tokenToUser = new ConcurrentHashMap<>();
    private static final Set<String> adminTokens = Collections.newSetFromMap(new ConcurrentHashMap<>());

    // Embedded in-memory / file SQL storage when external JDBC driver is not present
    private static final Map<String, UserRecord> usersMap = new ConcurrentHashMap<>();
    private static final Map<String, byte[]> savesMap = new ConcurrentHashMap<>();
    private static final List<MissionRecord> missionsList = Collections.synchronizedList(new ArrayList<>());

    public static void main(String[] args) {
        try {
            loadProperties();
            initDatabase();

            HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);
            server.setExecutor(Executors.newCachedThreadPool());

            // Client Endpoints
            server.createContext("/api/auth/register", new RegisterHandler());
            server.createContext("/api/auth/login", new LoginHandler());
            server.createContext("/api/game/config", new ConfigHandler());
            server.createContext("/api/missions/config", new ConfigHandler());
            server.createContext("/api/cloud-save", new CloudSaveHandler());
            server.createContext("/api/game/leaderboard", new LeaderboardHandler());
            server.createContext("/api/leaderboard", new LeaderboardHandler());
            server.createContext("/api/status", new StatusHandler());

            // Admin Dashboard & Admin APIs
            server.createContext("/admin", new AdminWebHandler());
            server.createContext("/api/admin/login", new AdminLoginHandler());
            server.createContext("/api/admin/users", new AdminUsersHandler());
            server.createContext("/api/admin/users/update", new AdminUserUpdateHandler());
            server.createContext("/api/admin/missions", new AdminMissionsHandler());
            server.createContext("/api/admin/config", new AdminConfigHandler());
            server.createContext("/", new RootHandler());

            server.start();
            System.out.println("=========================================================");
            System.out.println("   MOBI ARMY 2 VPS HYBRID SERVER (SQL + ADMIN PANEL)");
            System.out.println("   Server is running at: http://0.0.0.0:" + PORT);
            System.out.println("   Admin Web Dashboard : http://0.0.0.0:" + PORT + "/admin");
            System.out.println("   Default Admin Pass  : " + ADMIN_PASSWORD);
            System.out.println("   Loaded Users: " + usersMap.size() + " | Missions: " + missionsList.size());
            System.out.println("=========================================================");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String DB_HOST = "127.0.0.1";
    private static int DB_PORT = 3306;
    private static String DB_NAME = "army2_db";
    private static String DB_USER = "root";
    private static String DB_PASSWORD = "";

    public static Connection getDbConnection() throws SQLException {
        if ("mysql".equalsIgnoreCase(DB_TYPE)) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                try {
                    Class.forName("com.mysql.jdbc.Driver");
                } catch (ClassNotFoundException ignored) {}
            }
            String url = "jdbc:mysql://" + DB_HOST + ":" + DB_PORT + "/" + DB_NAME + "?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Ho_Chi_Minh&allowPublicKeyRetrieval=true&useSSL=false&autoReconnect=true";
            return DriverManager.getConnection(url, DB_USER, DB_PASSWORD);
        }
        return null;
    }

    private static void loadProperties() {
        File f = new File("config/server.properties");
        if (!f.exists()) f = new File("army2_server/config/server.properties");
        if (f.exists()) {
            try (FileInputStream fis = new FileInputStream(f)) {
                Properties p = new Properties();
                p.load(fis);
                PORT = Integer.parseInt(p.getProperty("server_port", "8080").trim());
                ADMIN_PASSWORD = p.getProperty("admin_password", "admin123").trim();
                STARTER_XU = Integer.parseInt(p.getProperty("starter_xu", "50000").trim());
                STARTER_LUONG = Integer.parseInt(p.getProperty("starter_luong", "100").trim());
                EVENT_EXP_RATE = Double.parseDouble(p.getProperty("event_exp_rate", "1.0").trim());
                DB_TYPE = p.getProperty("db_type", "mysql").trim();
                DB_HOST = p.getProperty("db_host", "127.0.0.1").trim();
                DB_PORT = Integer.parseInt(p.getProperty("db_port", "3306").trim());
                DB_NAME = p.getProperty("db_name", "army2_db").trim();
                DB_USER = p.getProperty("db_user", "root").trim();
                DB_PASSWORD = p.getProperty("db_password", "").trim();
                System.out.println("[CONFIG] Loaded settings from server.properties (DB_TYPE: " + DB_TYPE + ")");
            } catch (Exception e) {
                System.err.println("[CONFIG] Error reading server.properties: " + e.getMessage());
            }
        }
    }

    private static void initDatabase() {
        File dataDir = new File("data");
        if (!dataDir.exists() && new File("army2_server").exists()) dataDir = new File("army2_server/data");
        if (!dataDir.exists()) dataDir.mkdirs();

        // 1. MySQL direct connection & synchronization
        boolean mysqlLoaded = false;
        if ("mysql".equalsIgnoreCase(DB_TYPE)) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
            } catch (Exception ignored) {}
            try (Connection conn = getDbConnection()) {
                System.out.println("[MYSQL] Successfully connected to MySQL database `" + DB_NAME + "` at " + DB_HOST + ":" + DB_PORT);
                
                // Create user_guns table if not exists
                try (Statement stmt = conn.createStatement()) {
                    String sqlCreateGuns = "CREATE TABLE IF NOT EXISTS `user_guns` ("
                        + "`id` INT AUTO_INCREMENT PRIMARY KEY, "
                        + "`username` VARCHAR(64) NOT NULL, "
                        + "`gun_id` INT NOT NULL, "
                        + "`level` INT NOT NULL DEFAULT 1, "
                        + "`exp` BIGINT NOT NULL DEFAULT 0, "
                        + "`updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, "
                        + "UNIQUE KEY `user_gun_unique` (`username`, `gun_id`), "
                        + "INDEX `idx_gun_rank` (`gun_id`, `level` DESC, `exp` DESC)"
                        + ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4";
                    stmt.execute(sqlCreateGuns);
                } catch (Exception e) {
                    System.err.println("[MYSQL] Error ensuring user_guns table: " + e.getMessage());
                }

                String sqlUsers = "SELECT username, password, xu, luong, level, exp, cup, is_banned, created_at FROM users";
                try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sqlUsers)) {
                    while (rs.next()) {
                        UserRecord u = new UserRecord(
                            rs.getString("username"),
                            rs.getString("password"),
                            rs.getInt("xu"),
                            rs.getInt("luong"),
                            rs.getInt("level"),
                            rs.getLong("exp"),
                            rs.getInt("cup"),
                            rs.getInt("is_banned") == 1,
                            rs.getString("created_at")
                        );
                        usersMap.put(u.username.toLowerCase(), u);
                    }
                    System.out.println("[MYSQL] Loaded " + usersMap.size() + " users from MySQL database table `users`");
                    mysqlLoaded = true;
                }

                // Load gun levels from user_guns
                try {
                    String sqlGuns = "SELECT username, gun_id, level, exp FROM user_guns";
                    try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sqlGuns)) {
                        int gunRows = 0;
                        while (rs.next()) {
                            String uName = rs.getString("username");
                            int gunId = rs.getInt("gun_id");
                            int lvl = rs.getInt("level");
                            long exp = rs.getLong("exp");
                            UserRecord u = usersMap.get(uName.toLowerCase());
                            if (u != null) {
                                if (u.gunLevels != null && gunId >= 0 && gunId < u.gunLevels.length) {
                                    u.gunLevels[gunId] = Math.max(1, lvl);
                                }
                                if (u.gunExp != null && gunId >= 0 && gunId < u.gunExp.length) {
                                    u.gunExp[gunId] = exp;
                                }
                                gunRows++;
                            }
                        }
                        System.out.println("[MYSQL] Loaded " + gunRows + " gun records from MySQL table `user_guns`");
                    }
                } catch (Exception ignored) {}

                String sqlMissions = "SELECT mission_id, gun_id, name, description, target_wins, reward_xu, reward_luong, reward_exp FROM missions ORDER BY id ASC";
                try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sqlMissions)) {
                    missionsList.clear();
                    while (rs.next()) {
                        missionsList.add(new MissionRecord(
                            rs.getInt("mission_id"),
                            rs.getInt("gun_id"),
                            rs.getString("name"),
                            rs.getInt("target_wins"),
                            rs.getInt("reward_exp"),
                            rs.getInt("reward_xu"),
                            rs.getInt("reward_luong"),
                            0
                        ));
                    }
                    System.out.println("[MYSQL] Loaded " + missionsList.size() + " missions from MySQL database table `missions`");
                }
            } catch (Exception e) {
                System.err.println("[MYSQL] Could not connect to MySQL: " + e.getMessage() + ". Falling back to local file storage.");
            }
        }

        // 2. Load users from local JSON DB cache if MySQL not used or offline
        if (!mysqlLoaded) {
            File usersFile = new File(dataDir, "users_db.json");
            if (usersFile.exists()) {
                try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(usersFile), StandardCharsets.UTF_8))) {
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) sb.append(line);
                    parseUsersJson(sb.toString());
                } catch (Exception ignored) {}
            }
        }

        // 3. Fallback default missions if empty
        if (missionsList.isEmpty()) {
            File schemaFile = new File("sql/schema.sql");
            if (!schemaFile.exists()) schemaFile = new File("army2_server/sql/schema.sql");
            if (schemaFile.exists()) {
                loadMissionsFromSql(schemaFile);
            }
            if (missionsList.isEmpty()) {
                seedDefaultMissions();
            }
        }
        System.out.println("[DB] Database initialised successfully (" + missionsList.size() + " missions loaded)");
    }

    private static synchronized void saveUsersDatabase() {
        File dataDir = new File("data");
        if (!dataDir.exists() && new File("army2_server").exists()) dataDir = new File("army2_server/data");
        if (!dataDir.exists()) dataDir.mkdirs();
        File usersFile = new File(dataDir, "users_db.json");

        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(usersFile), StandardCharsets.UTF_8))) {
            bw.write("[\n");
            int i = 0;
            int size = usersMap.size();
            for (UserRecord u : usersMap.values()) {
                bw.write("  {\n");
                bw.write("    \"username\": \"" + escapeJson(u.username) + "\",\n");
                bw.write("    \"password\": \"" + escapeJson(u.password) + "\",\n");
                bw.write("    \"xu\": " + u.xu + ",\n");
                bw.write("    \"luong\": " + u.luong + ",\n");
                bw.write("    \"level\": " + u.level + ",\n");
                bw.write("    \"exp\": " + u.exp + ",\n");
                bw.write("    \"cup\": " + u.cup + ",\n");
                bw.write("    \"is_banned\": " + (u.isBanned ? 1 : 0) + ",\n");
                bw.write("    \"created_at\": \"" + escapeJson(u.createdAt) + "\"\n");
                bw.write("  }" + (++i < size ? "," : "") + "\n");
            }
            bw.write("]\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void parseUsersJson(String json) {
        if (json == null || json.trim().isEmpty()) return;
        try {
            int start = json.indexOf('{');
            while (start >= 0) {
                int end = json.indexOf('}', start);
                if (end < 0) break;
                String obj = json.substring(start, end + 1);
                String u = extractJsonField(obj, "username");
                String p = extractJsonField(obj, "password");
                if (!u.isEmpty()) {
                    UserRecord rec = new UserRecord(u, p);
                    rec.xu = extractJsonInt(obj, "xu", STARTER_XU);
                    rec.luong = extractJsonInt(obj, "luong", STARTER_LUONG);
                    rec.level = extractJsonInt(obj, "level", 1);
                    rec.exp = extractJsonLong(obj, "exp", 0);
                    rec.cup = extractJsonInt(obj, "cup", 0);
                    rec.isBanned = extractJsonInt(obj, "is_banned", 0) == 1;
                    rec.createdAt = extractJsonField(obj, "created_at");
                    usersMap.put(u.toLowerCase(), rec);
                }
                start = json.indexOf('{', end + 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void loadMissionsFromSql(File sqlFile) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(sqlFile), StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.startsWith("(") && line.contains(",")) {
                    // Sample: (0, 1, 'Lv 1: Thắng 100 ván solo', 100, 1000, 5000, 10, 100),
                    int endIdx = line.indexOf(')');
                    if (endIdx > 0) {
                        String content = line.substring(1, endIdx);
                        String[] parts = content.split(",(?=(?:[^']*'[^']*')*[^']*$)");
                        if (parts.length >= 8) {
                            try {
                                int id = Integer.parseInt(parts[0].trim());
                                int lvl = Integer.parseInt(parts[1].trim());
                                String name = parts[2].trim().replaceAll("^'|'$", "");
                                int req = Integer.parseInt(parts[3].trim());
                                int exp = Integer.parseInt(parts[4].trim());
                                int xu = Integer.parseInt(parts[5].trim());
                                int luong = Integer.parseInt(parts[6].trim());
                                int cup = Integer.parseInt(parts[7].trim());
                                missionsList.add(new MissionRecord(id, lvl, name, req, exp, xu, luong, cup));
                            } catch (Exception ignored) {}
                        }
                    }
                }
            }
        } catch (Exception ignored) {}
    }

    private static void seedDefaultMissions() {
        String[] guns = new String[]{"Gunner", "Miss 6", "Electician", "King Kong", "Rocketer", "Granos", "Chicky", "Tarzan", "Apache", "Magenta", "Draby", "Cow Girl"};
        for (int i = 0; i < guns.length; ++i) {
            int mid = 13 + i;
            missionsList.add(new MissionRecord(mid, 1, "Lv 1: Dùng " + guns[i] + " thắng 5 ván", 5, 5000, 10000, 50, 200));
            missionsList.add(new MissionRecord(mid, 2, "Lv 2: Dùng " + guns[i] + " thắng 50 ván", 50, 50000, 50000, 200, 1000));
            missionsList.add(new MissionRecord(mid, 3, "Lv 3: Dùng " + guns[i] + " thắng 500 ván", 500, 500000, 500000, 1000, 5000));
        }
    }

    // Models
    static class UserRecord {
        String username;
        String password;
        int xu = STARTER_XU;
        int luong = STARTER_LUONG;
        int level = 1;
        long exp = 0;
        int cup = 0;
        int[] gunLevels = new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        long[] gunExp = new long[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        boolean isBanned = false;
        String createdAt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date());

        UserRecord(String u, String p) {
            this.username = u;
            this.password = p;
        }

        UserRecord(String u, String p, int xu, int luong, int level, long exp, int cup, boolean isBanned, String createdAt) {
            this.username = u;
            this.password = p;
            this.xu = xu;
            this.luong = luong;
            this.level = level;
            this.exp = exp;
            this.cup = cup;
            this.isBanned = isBanned;
            this.createdAt = createdAt != null ? createdAt : new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date());
            if (this.gunLevels != null && this.gunLevels.length > 0) {
                this.gunLevels[0] = Math.max(1, level);
            }
        }
    }

    static class MissionRecord {
        int id;
        int level;
        String name;
        int require;
        int rewardExp;
        int rewardXu;
        int rewardLuong;
        int rewardCup;

        MissionRecord(int id, int level, String name, int require, int rewardExp, int rewardXu, int rewardLuong, int rewardCup) {
            this.id = id;
            this.level = level;
            this.name = name;
            this.require = require;
            this.rewardExp = rewardExp;
            this.rewardXu = rewardXu;
            this.rewardLuong = rewardLuong;
            this.rewardCup = rewardCup;
        }
    }

    // Utilities
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

    private static int extractJsonInt(String json, String field, int def) {
        if (json == null) return def;
        String pattern = "\"" + field + "\"";
        int idx = json.indexOf(pattern);
        if (idx < 0) return def;
        int colon = json.indexOf(':', idx + pattern.length());
        if (colon < 0) return def;
        int i = colon + 1;
        while (i < json.length() && (json.charAt(i) == ' ' || json.charAt(i) == '\t')) ++i;
        int end = i;
        while (end < json.length() && (json.charAt(end) == '-' || (json.charAt(end) >= '0' && json.charAt(end) <= '9'))) ++end;
        if (end > i) {
            try { return Integer.parseInt(json.substring(i, end)); } catch (Exception ignored) {}
        }
        return def;
    }

    private static long extractJsonLong(String json, String field, long def) {
        if (json == null) return def;
        String pattern = "\"" + field + "\"";
        int idx = json.indexOf(pattern);
        if (idx < 0) return def;
        int colon = json.indexOf(':', idx + pattern.length());
        if (colon < 0) return def;
        int i = colon + 1;
        while (i < json.length() && (json.charAt(i) == ' ' || json.charAt(i) == '\t')) ++i;
        int end = i;
        while (end < json.length() && (json.charAt(end) == '-' || (json.charAt(end) >= '0' && json.charAt(end) <= '9'))) ++end;
        if (end > i) {
            try { return Long.parseLong(json.substring(i, end)); } catch (Exception ignored) {}
        }
        return def;
    }

    private static String escapeJson(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "\\r");
    }

    private static String getUserFromToken(HttpExchange exchange) {
        String auth = exchange.getRequestHeaders().getFirst("Authorization");
        if (auth != null && auth.startsWith("Bearer ")) {
            String token = auth.substring(7).trim();
            String user = tokenToUser.get(token);
            if (user != null) return user;
        }
        String saveName = exchange.getRequestHeaders().getFirst("X-Save-Name");
        if (saveName != null && !saveName.trim().isEmpty()) {
            return saveName.trim().toLowerCase();
        }
        String userName = exchange.getRequestHeaders().getFirst("X-User-Name");
        if (userName != null && !userName.trim().isEmpty()) {
            return userName.trim().toLowerCase();
        }
        return null;
    }

    private static boolean isAdmin(HttpExchange exchange) {
        String auth = exchange.getRequestHeaders().getFirst("Authorization");
        if (auth != null && auth.startsWith("Bearer ")) {
            String token = auth.substring(7).trim();
            return adminTokens.contains(token);
        }
        return false;
    }

    private static void sendJsonResponse(HttpExchange exchange, int status, String json) throws IOException {
        byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        exchange.sendResponseHeaders(status, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) { os.write(bytes); }
    }

    private static void sendHtmlResponse(HttpExchange exchange, int status, String html) throws IOException {
        byte[] bytes = html.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        exchange.sendResponseHeaders(status, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) { os.write(bytes); }
    }

    private static byte[] readRequestBody(HttpExchange exchange) throws IOException {
        InputStream is = exchange.getRequestBody();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buf = new byte[4096];
        int n;
        while ((n = is.read(buf)) != -1) baos.write(buf, 0, n);
        return baos.toByteArray();
    }

    // =========================================================================
    // CLIENT HANDLERS
    // =========================================================================
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
            byte[] body = readRequestBody(exchange);
            String json = new String(body, StandardCharsets.UTF_8);
            String username = extractJsonField(json, "email");
            if (username.isEmpty()) username = extractJsonField(json, "username");
            String password = extractJsonField(json, "password");

            username = username.toLowerCase().trim();
            if (username.length() < 3 || password.length() < 3) {
                sendJsonResponse(exchange, 400, "{\"ok\":false,\"message\":\"Tên tài khoản và mật khẩu phải từ 3 ký tự!\"}");
                return;
            }
            if (usersMap.containsKey(username)) {
                sendJsonResponse(exchange, 400, "{\"ok\":false,\"message\":\"Tài khoản này đã tồn tại!\"}");
                return;
            }

            UserRecord u = new UserRecord(username, password);
            usersMap.put(username, u);
            saveUsersDatabase();

            if ("mysql".equalsIgnoreCase(DB_TYPE)) {
                try (Connection conn = getDbConnection()) {
                    String sql = "INSERT INTO users (username, password, xu, luong, level, exp, cup, role, is_banned) VALUES (?, ?, ?, ?, 1, 0, 0, 'player', 0)";
                    try (PreparedStatement ps = conn.prepareStatement(sql)) {
                        ps.setString(1, username);
                        ps.setString(2, password);
                        ps.setInt(3, STARTER_XU);
                        ps.setInt(4, STARTER_LUONG);
                        ps.executeUpdate();
                        System.out.println("[MYSQL] Inserted new user `" + username + "` directly into MySQL table `users`!");
                    }
                } catch (Exception e) {
                    System.err.println("[MYSQL] Error registering user in MySQL: " + e.getMessage());
                }
            }

            String token = UUID.randomUUID().toString();
            tokenToUser.put(token, username);
            System.out.println("[AUTH] New User Registered: " + username);
            sendJsonResponse(exchange, 200, "{\"ok\":true,\"token\":\"" + token + "\",\"data\":{\"token\":\"" + token + "\"},\"message\":\"Đăng ký thành công!\"}");
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
            byte[] body = readRequestBody(exchange);
            String json = new String(body, StandardCharsets.UTF_8);
            String username = extractJsonField(json, "email");
            if (username.isEmpty()) username = extractJsonField(json, "username");
            String password = extractJsonField(json, "password");

            username = username.toLowerCase().trim();

            if ("mysql".equalsIgnoreCase(DB_TYPE)) {
                try (Connection conn = getDbConnection()) {
                    String sql = "SELECT id, username, password, xu, luong, level, exp, cup, is_banned, created_at FROM users WHERE LOWER(username) = ?";
                    try (PreparedStatement ps = conn.prepareStatement(sql)) {
                        ps.setString(1, username);
                        try (ResultSet rs = ps.executeQuery()) {
                            if (rs.next()) {
                                UserRecord u = new UserRecord(
                                    rs.getString("username"),
                                    rs.getString("password"),
                                    rs.getInt("xu"),
                                    rs.getInt("luong"),
                                    rs.getInt("level"),
                                    rs.getLong("exp"),
                                    rs.getInt("cup"),
                                    rs.getInt("is_banned") == 1,
                                    rs.getString("created_at")
                                );
                                usersMap.put(username, u);
                            }
                        }
                    }
                } catch (Exception e) {
                    System.err.println("[MYSQL] Error querying user during login: " + e.getMessage());
                }
            }

            UserRecord u = usersMap.get(username);
            if (u == null || !u.password.equals(password)) {
                sendJsonResponse(exchange, 401, "{\"ok\":false,\"message\":\"Sai tên tài khoản hoặc mật khẩu!\"}");
                return;
            }
            if (u.isBanned) {
                sendJsonResponse(exchange, 403, "{\"ok\":false,\"message\":\"Tài khoản này đã bị khóa bởi Admin!\"}");
                return;
            }

            String token = UUID.randomUUID().toString();
            tokenToUser.put(token, username);
            System.out.println("[AUTH] User Logged In: " + username);
            sendJsonResponse(exchange, 200, "{\"ok\":true,\"token\":\"" + token + "\",\"data\":{\"token\":\"" + token + "\"},\"message\":\"Đăng nhập thành công!\"}");
        }
    }

    static class ConfigHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
            StringBuilder sb = new StringBuilder();
            sb.append("{\n");
            sb.append("  \"server_name\": \"Mobi Army 2 Hybrid Server\",\n");
            sb.append("  \"event_exp_rate\": ").append(EVENT_EXP_RATE).append(",\n");
            sb.append("  \"missions\": [\n");
            synchronized (missionsList) {
                for (int i = 0; i < missionsList.size(); ++i) {
                    MissionRecord m = missionsList.get(i);
                    sb.append("    { \"id\": ").append(m.id)
                      .append(", \"level\": ").append(m.level)
                      .append(", \"name\": \"").append(escapeJson(m.name)).append("\"")
                      .append(", \"require\": ").append(m.require)
                      .append(", \"reward_exp\": ").append(m.rewardExp)
                      .append(", \"reward_xu\": ").append(m.rewardXu)
                      .append(", \"reward_luong\": ").append(m.rewardLuong)
                      .append(", \"reward_cup\": ").append(m.rewardCup).append(" }");
                    if (i < missionsList.size() - 1) sb.append(",");
                    sb.append("\n");
                }
            }
            sb.append("  ]\n}");
            sendJsonResponse(exchange, 200, sb.toString());
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
            System.out.println("[SERVER SAVE] Received " + method + " " + path + " | User: " + (username != null ? username : "NULL (UNAUTHORIZED)"));
            if (username == null) {
                System.err.println("[SERVER SAVE] Rejected request: User not authenticated. Missing Token or X-User-Name.");
                sendJsonResponse(exchange, 401, "{\"ok\":false,\"message\":\"Chưa đăng nhập!\"}");
                return;
            }

            File dataDir = new File("data/saves");
            if (!dataDir.exists() && new File("army2_server").exists()) dataDir = new File("army2_server/data/saves");
            if (!dataDir.exists()) dataDir.mkdirs();
            File saveFile = new File(dataDir, username + ".bin");

            if ("GET".equalsIgnoreCase(method)) {
                if (path.endsWith("/blob") || path.contains("/blob")) {
                    byte[] b = null;
                    if ("mysql".equalsIgnoreCase(DB_TYPE)) {
                        try (Connection conn = getDbConnection()) {
                            String sql = "SELECT save_data FROM player_saves WHERE LOWER(username) = ?";
                            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                                ps.setString(1, username.toLowerCase());
                                try (ResultSet rs = ps.executeQuery()) {
                                    if (rs.next()) {
                                        b = rs.getBytes("save_data");
                                    }
                                }
                            }
                        } catch (Exception e) {
                            System.err.println("[MYSQL] Error downloading save from MySQL: " + e.getMessage());
                        }
                    }
                    if ((b == null || b.length == 0) && saveFile.exists()) {
                        b = new byte[(int) saveFile.length()];
                        try (FileInputStream fis = new FileInputStream(saveFile)) { fis.read(b); }
                    }
                    if (b != null && b.length > 0) {
                        exchange.getResponseHeaders().set("Content-Type", "application/octet-stream");
                        exchange.sendResponseHeaders(200, b.length);
                        try (OutputStream os = exchange.getResponseBody()) { os.write(b); }
                    } else {
                        sendJsonResponse(exchange, 404, "{\"ok\":false,\"message\":\"Chưa có bản lưu nào.\"}");
                    }
                } else {
                    boolean has = saveFile.exists();
                    String date = has ? new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date(saveFile.lastModified())) : "";
                    sendJsonResponse(exchange, 200, "{\"ok\":true,\"data\":[{\"id\":\"current\",\"name\":\"Bản lưu chính\",\"hasData\":" + has + ",\"createdAt\":\"" + date + "\"}]}");
                }
            } else if ("PUT".equalsIgnoreCase(method) || "POST".equalsIgnoreCase(method)) {
                byte[] blob = readRequestBody(exchange);
                System.out.println("[SERVER SAVE] Received PUT/POST save data payload: " + blob.length + " bytes for `" + username + "`");
                if (blob.length > 0) {
                    UserRecord rec = usersMap.get(username.toLowerCase());
                    if (rec == null) {
                        rec = new UserRecord(username, "");
                        usersMap.put(username.toLowerCase(), rec);
                    }
                    
                    decodeSaveStatsAndGunLevels(blob, rec);
                    System.out.println("[SERVER SAVE] Decoded stats -> Xu: " + rec.xu + ", Luong: " + rec.luong + ", Level: " + rec.level + ", Exp: " + rec.exp + ", Cup: " + rec.cup);
                    try (FileOutputStream fos = new FileOutputStream(saveFile)) { fos.write(blob); }

                    if ("mysql".equalsIgnoreCase(DB_TYPE)) {
                        try (Connection conn = getDbConnection()) {
                            String sqlSave = "REPLACE INTO player_saves (username, save_data, updated_at) VALUES (?, ?, NOW())";
                            try (PreparedStatement ps = conn.prepareStatement(sqlSave)) {
                                ps.setString(1, username);
                                ps.setBytes(2, blob);
                                ps.executeUpdate();
                            }
                            if (rec.xu >= 0) {
                                String sqlUser = "UPDATE users SET xu = ?, luong = ?, level = ?, exp = ?, cup = ? WHERE LOWER(username) = ?";
                                try (PreparedStatement ps = conn.prepareStatement(sqlUser)) {
                                    ps.setInt(1, rec.xu);
                                    ps.setInt(2, rec.luong);
                                    ps.setInt(3, rec.level);
                                    ps.setLong(4, rec.exp);
                                    ps.setInt(5, rec.cup);
                                    ps.setString(6, username.toLowerCase());
                                    ps.executeUpdate();
                                }
                            }
                            if (rec.gunLevels != null) {
                                String sqlGun = "REPLACE INTO user_guns (username, gun_id, level, exp, updated_at) VALUES (?, ?, ?, ?, NOW())";
                                try (PreparedStatement psGun = conn.prepareStatement(sqlGun)) {
                                    for (int g = 0; g < rec.gunLevels.length; ++g) {
                                        psGun.setString(1, username);
                                        psGun.setInt(2, g);
                                        psGun.setInt(3, rec.gunLevels[g]);
                                        psGun.setLong(4, (rec.gunExp != null && g < rec.gunExp.length) ? rec.gunExp[g] : 0);
                                        psGun.addBatch();
                                    }
                                    psGun.executeBatch();
                                }
                            }
                            System.out.println("[MYSQL] Saved game data & all gun levels for `" + username + "` into MySQL `player_saves`, `users`, and `user_guns`!");
                        } catch (Exception e) {
                            System.err.println("[MYSQL] Error updating save in MySQL: " + e.getMessage());
                        }
                    }

                    saveUsersDatabase();
                    sendJsonResponse(exchange, 200, "{\"ok\":true,\"message\":\"Đã đồng bộ lên CSDL MySQL!\"}");
                } else {
                    sendJsonResponse(exchange, 400, "{\"ok\":false,\"message\":\"Dữ liệu rỗng.\"}");
                }
            } else {
                sendJsonResponse(exchange, 405, "{\"ok\":false,\"message\":\"Method not allowed\"}");
            }
        }
    }

    private static void decodeSaveStatsAndGunLevels(byte[] blob, UserRecord u) {
        if (blob == null || blob.length < 20 || u == null) return;
        try {
            DataInputStream dis = new DataInputStream(new ByteArrayInputStream(blob));
            int magic = dis.readInt();
            if (magic != 1296118322) return;
            byte ver = dis.readByte();
            String pName = dis.readUTF();
            u.xu = dis.readInt();
            u.luong = dis.readInt();
            int curGun = dis.readInt();
            u.level = dis.readInt();
            u.exp = dis.readInt();
            int nextExp = dis.readInt();
            u.cup = dis.readInt();
            byte levelPercen = dis.readByte();
            short point = dis.readShort();

            // 10 guns unlock info
            for (int i = 0; i < 10; ++i) {
                dis.readByte();
                dis.readInt();
                dis.readInt();
                dis.readBoolean();
            }
            // 10 guns equips
            for (int i = 0; i < 10; ++i) {
                for (int j = 0; j < 5; ++j) {
                    dis.readShort();
                }
            }
            // 36 items
            for (int i = 0; i < 36; ++i) dis.readByte();

            // Chest items
            try {
                short chestCount = dis.readShort();
                if (chestCount > 0) {
                    dis.skipBytes(chestCount * 16);
                }
                if (ver >= 3) {
                    short wornCount = dis.readShort();
                    if (wornCount > 0) dis.skipBytes(wornCount * 4);
                }
            } catch (Exception ignored) {}

            // Missions
            try {
                dis.skipBytes(18 * 4);
                dis.skipBytes(54);
                dis.readInt();
                dis.readInt();
            } catch (Exception ignored) {}

            // 10 guns class progress
            if (ver >= 2) {
                try {
                    for (int i = 0; i < 10; ++i) {
                        int gExp = dis.readInt();
                        int gLvl = dis.readInt();
                        int gPct = dis.readInt();
                        int gNext = dis.readInt();
                        short gPt = dis.readShort();
                        for (int a = 0; a < 5; ++a) dis.readShort();
                        if (u.gunLevels != null && i < u.gunLevels.length) {
                            u.gunLevels[i] = Math.max(1, gLvl);
                        }
                        if (u.gunExp != null && i < u.gunExp.length) {
                            u.gunExp[i] = gExp;
                        }
                    }
                } catch (Exception ignored) {}
            }

            // Squad items
            if (ver >= 4) {
                try {
                    short teamCount = dis.readShort();
                    if (teamCount > 0) dis.skipBytes(teamCount * 3);
                } catch (Exception ignored) {}
            }

            // Gun 10 (Draby)
            if (ver >= 5) {
                try {
                    dis.skipBytes(10);
                    dis.skipBytes(10);
                    int gExp10 = dis.readInt();
                    int gLvl10 = dis.readInt();
                    int gPct10 = dis.readInt();
                    int gNext10 = dis.readInt();
                    short gPt10 = dis.readShort();
                    for (int a = 0; a < 5; ++a) dis.readShort();
                    if (u.gunLevels != null && 10 < u.gunLevels.length) {
                        u.gunLevels[10] = Math.max(1, gLvl10);
                    }
                    if (u.gunExp != null && 10 < u.gunExp.length) {
                        u.gunExp[10] = gExp10;
                    }
                } catch (Exception ignored) {}
            }

            if (curGun >= 0 && curGun < u.gunLevels.length && u.gunLevels[curGun] < u.level) {
                u.gunLevels[curGun] = u.level;
            }
        } catch (Exception ignored) {}
    }

    static class StatusHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            sendJsonResponse(exchange, 200, "{\"status\":\"ONLINE\",\"users\":" + usersMap.size() + ",\"missions\":" + missionsList.size() + "}");
        }
    }

    static class LeaderboardHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
            String query = exchange.getRequestURI().getQuery();
            int type = 0;
            if (query != null && query.contains("type=")) {
                try {
                    String s = query.substring(query.indexOf("type=") + 5);
                    if (s.contains("&")) s = s.substring(0, s.indexOf("&"));
                    type = Integer.parseInt(s.trim());
                } catch (Exception ignored) {}
            }

            // Try fetching real-time sorted data directly from MySQL
            if ("mysql".equalsIgnoreCase(DB_TYPE)) {
                try (Connection conn = getDbConnection()) {
                    String sql;
                    if (type >= 4 && type <= 15) {
                        int targetGun = type - 4;
                        sql = "SELECT g.username, g.level, g.exp, u.xu, u.luong, u.cup FROM user_guns g LEFT JOIN users u ON LOWER(g.username) = LOWER(u.username) WHERE g.gun_id = " + targetGun + " AND (u.is_banned IS NULL OR u.is_banned = 0) ORDER BY g.level DESC, g.exp DESC, g.updated_at ASC LIMIT 20";
                    } else if (type == 1) {
                        sql = "SELECT username, level, exp, xu, luong, cup FROM users WHERE is_banned = 0 ORDER BY xu DESC, level DESC LIMIT 20";
                    } else if (type == 2) {
                        sql = "SELECT username, level, exp, xu, luong, cup FROM users WHERE is_banned = 0 ORDER BY luong DESC, level DESC LIMIT 20";
                    } else if (type == 3) {
                        sql = "SELECT username, level, exp, xu, luong, cup FROM users WHERE is_banned = 0 ORDER BY cup DESC, level DESC LIMIT 20";
                    } else {
                        sql = "SELECT username, level, exp, xu, luong, cup FROM users WHERE is_banned = 0 ORDER BY level DESC, exp DESC, last_login DESC LIMIT 20";
                    }

                    try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
                        StringBuilder sb = new StringBuilder();
                        sb.append("{\"ok\":true,\"type\":").append(type).append(",\"data\":[");
                        int count = 0;
                        while (rs.next()) {
                            if (count > 0) sb.append(",");
                            String uName = rs.getString("username");
                            int lvl = rs.getInt("level");
                            int xu = rs.getInt("xu");
                            int luong = rs.getInt("luong");
                            int cup = rs.getInt("cup");
                            int gunId = (type >= 4 && type <= 15) ? (type - 4) : 0;

                            sb.append("{\"username\":\"").append(escapeJson(uName)).append("\"")
                              .append(",\"gun\":").append(gunId)
                              .append(",\"level\":").append(lvl)
                              .append(",\"xu\":").append(xu)
                              .append(",\"luong\":").append(luong)
                              .append(",\"cup\":").append(cup)
                              .append("}");
                            count++;
                        }
                        sb.append("]}");
                        if (count > 0) {
                            sendJsonResponse(exchange, 200, sb.toString());
                            return;
                        }
                    }
                } catch (Exception e) {
                    System.err.println("[MYSQL] Leaderboard query error: " + e.getMessage() + ". Falling back to memory list.");
                }
            }

            // Fallback in-memory sorting
            List<UserRecord> list = new ArrayList<>(usersMap.values());
            final int fType = type;
            list.sort(new Comparator<UserRecord>() {
                public int compare(UserRecord a, UserRecord b) {
                    if (fType == 1) return Integer.compare(b.xu, a.xu);
                    if (fType == 2) return Integer.compare(b.luong, a.luong);
                    if (fType == 3) return Integer.compare(b.cup, a.cup);
                    if (fType >= 4 && fType <= 15) {
                        int g = fType - 4;
                        int lvlA = (a.gunLevels != null && g < a.gunLevels.length) ? a.gunLevels[g] : 1;
                        int lvlB = (b.gunLevels != null && g < b.gunLevels.length) ? b.gunLevels[g] : 1;
                        return Integer.compare(lvlB, lvlA);
                    }
                    return Integer.compare(b.level, a.level);
                }
            });

            StringBuilder sb = new StringBuilder();
            sb.append("{\"ok\":true,\"type\":").append(type).append(",\"data\":[");
            int limit = Math.min(list.size(), 20);
            for (int i = 0; i < limit; ++i) {
                UserRecord u = list.get(i);
                int gunId = (fType >= 4 && fType <= 15) ? (fType - 4) : 0;
                int showLvl = (fType >= 4 && fType <= 15) ? ((u.gunLevels != null && gunId < u.gunLevels.length) ? u.gunLevels[gunId] : 1) : u.level;

                sb.append("{\"username\":\"").append(escapeJson(u.username)).append("\"")
                  .append(",\"gun\":").append(gunId)
                  .append(",\"level\":").append(showLvl)
                  .append(",\"xu\":").append(u.xu)
                  .append(",\"luong\":").append(u.luong)
                  .append(",\"cup\":").append(u.cup)
                  .append("}");
                if (i < limit - 1) sb.append(",");
            }
            sb.append("]}");
            sendJsonResponse(exchange, 200, sb.toString());
        }
    }

    // =========================================================================
    // ADMIN DASHBOARD & ADMIN APIS
    // =========================================================================
    static class AdminLoginHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            byte[] body = readRequestBody(exchange);
            String json = new String(body, StandardCharsets.UTF_8);
            String pass = extractJsonField(json, "password");
            if (ADMIN_PASSWORD.equals(pass)) {
                String token = "admin_" + UUID.randomUUID().toString();
                adminTokens.add(token);
                sendJsonResponse(exchange, 200, "{\"ok\":true,\"token\":\"" + token + "\"}");
            } else {
                sendJsonResponse(exchange, 401, "{\"ok\":false,\"message\":\"Sai mật khẩu Admin!\"}");
            }
        }
    }

    static class AdminUsersHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (!isAdmin(exchange)) {
                sendJsonResponse(exchange, 401, "{\"ok\":false,\"message\":\"Unauthorized\"}");
                return;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("{\"ok\":true,\"users\":[");
            int i = 0;
            int size = usersMap.size();
            for (UserRecord u : usersMap.values()) {
                sb.append("{\"username\":\"").append(escapeJson(u.username)).append("\"")
                  .append(",\"xu\":").append(u.xu)
                  .append(",\"luong\":").append(u.luong)
                  .append(",\"level\":").append(u.level)
                  .append(",\"cup\":").append(u.cup)
                  .append(",\"is_banned\":").append(u.isBanned)
                  .append(",\"created_at\":\"").append(escapeJson(u.createdAt)).append("\"}");
                if (++i < size) sb.append(",");
            }
            sb.append("]}");
            sendJsonResponse(exchange, 200, sb.toString());
        }
    }

    static class AdminUserUpdateHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (!isAdmin(exchange)) {
                sendJsonResponse(exchange, 401, "{\"ok\":false,\"message\":\"Unauthorized\"}");
                return;
            }
            byte[] body = readRequestBody(exchange);
            String json = new String(body, StandardCharsets.UTF_8);
            String username = extractJsonField(json, "username").toLowerCase().trim();
            UserRecord u = usersMap.get(username);
            if (u == null) {
                sendJsonResponse(exchange, 404, "{\"ok\":false,\"message\":\"Không tìm thấy người chơi!\"}");
                return;
            }

            int addXu = extractJsonInt(json, "add_xu", 0);
            int addLuong = extractJsonInt(json, "add_luong", 0);
            int setXu = extractJsonInt(json, "set_xu", -1);
            int setLuong = extractJsonInt(json, "set_luong", -1);
            int toggleBan = extractJsonInt(json, "toggle_ban", -1);
            String newPass = extractJsonField(json, "new_password");

            if (setXu >= 0) u.xu = setXu;
            else if (addXu != 0) u.xu += addXu;

            if (setLuong >= 0) u.luong = setLuong;
            else if (addLuong != 0) u.luong += addLuong;

            if (toggleBan == 1) u.isBanned = !u.isBanned;
            if (!newPass.isEmpty()) u.password = newPass;

            saveUsersDatabase();

            if ("mysql".equalsIgnoreCase(DB_TYPE)) {
                try (Connection conn = getDbConnection()) {
                    String sql = "UPDATE users SET xu = ?, luong = ?, is_banned = ?" + (!newPass.isEmpty() ? ", password = ?" : "") + " WHERE LOWER(username) = ?";
                    try (PreparedStatement ps = conn.prepareStatement(sql)) {
                        ps.setInt(1, u.xu);
                        ps.setInt(2, u.luong);
                        ps.setInt(3, u.isBanned ? 1 : 0);
                        if (!newPass.isEmpty()) {
                            ps.setString(4, u.password);
                            ps.setString(5, u.username.toLowerCase());
                        } else {
                            ps.setString(4, u.username.toLowerCase());
                        }
                        ps.executeUpdate();
                        System.out.println("[MYSQL] Admin updated user `" + u.username + "` in MySQL database table `users`!");
                    }
                } catch (Exception e) {
                    System.err.println("[MYSQL] Error updating user in MySQL: " + e.getMessage());
                }
            }

            System.out.println("[ADMIN] Updated user " + username + " (Xu: " + u.xu + ", Lượng: " + u.luong + ", Banned: " + u.isBanned + ")");
            sendJsonResponse(exchange, 200, "{\"ok\":true,\"message\":\"Đã cập nhật tài khoản " + username + "!\"}");
        }
    }

    static class AdminMissionsHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                if (!isAdmin(exchange)) {
                    sendJsonResponse(exchange, 401, "{\"ok\":false,\"message\":\"Unauthorized\"}");
                    return;
                }
                byte[] body = readRequestBody(exchange);
                String json = new String(body, StandardCharsets.UTF_8);
                int mid = extractJsonInt(json, "id", -1);
                int mlvl = extractJsonInt(json, "level", -1);
                int xu = extractJsonInt(json, "reward_xu", -1);
                int luong = extractJsonInt(json, "reward_luong", -1);
                int exp = extractJsonInt(json, "reward_exp", -1);
                int req = extractJsonInt(json, "require", -1);
                String name = extractJsonField(json, "name");

                synchronized (missionsList) {
                    for (MissionRecord m : missionsList) {
                        if (m.id == mid && m.level == mlvl) {
                            if (xu >= 0) m.rewardXu = xu;
                            if (luong >= 0) m.rewardLuong = luong;
                            if (exp >= 0) m.rewardExp = exp;
                            if (req > 0) m.require = req;
                            if (!name.isEmpty()) m.name = name;
                            break;
                        }
                    }
                }
                sendJsonResponse(exchange, 200, "{\"ok\":true,\"message\":\"Đã lưu nhiệm vụ!\"}");
            } else {
                new ConfigHandler().handle(exchange);
            }
        }
    }

    static class AdminConfigHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (!isAdmin(exchange)) {
                sendJsonResponse(exchange, 401, "{\"ok\":false,\"message\":\"Unauthorized\"}");
                return;
            }
            byte[] body = readRequestBody(exchange);
            String json = new String(body, StandardCharsets.UTF_8);
            double expRate = Double.parseDouble(extractJsonField(json, "event_exp_rate").isEmpty() ? "0" : extractJsonField(json, "event_exp_rate"));
            int sXu = extractJsonInt(json, "starter_xu", -1);
            int sLuong = extractJsonInt(json, "starter_luong", -1);

            if (expRate > 0) EVENT_EXP_RATE = expRate;
            if (sXu >= 0) STARTER_XU = sXu;
            if (sLuong >= 0) STARTER_LUONG = sLuong;

            sendJsonResponse(exchange, 200, "{\"ok\":true,\"message\":\"Đã cập nhật cài đặt máy chủ!\"}");
        }
    }

    static class AdminWebHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String html = "<!DOCTYPE html><html lang='vi'><head><meta charset='UTF-8'><meta name='viewport' content='width=device-width,initial-scale=1.0'>" +
                    "<title>Army 2 VPS Admin Dashboard</title>" +
                    "<link href='https://fonts.googleapis.com/css2?family=Outfit:wght@300;400;600;700&display=swap' rel='stylesheet'>" +
                    "<style>" +
                    "* { box-sizing: border-box; margin: 0; padding: 0; font-family: 'Outfit', sans-serif; }" +
                    "body { background: #0f111a; color: #e2e8f0; min-height: 100vh; padding: 20px; }" +
                    ".container { max-width: 1200px; margin: 0 auto; }" +
                    ".header { display: flex; justify-content: space-between; align-items: center; padding: 20px 0; border-bottom: 1px solid #2d3748; margin-bottom: 24px; }" +
                    ".logo { font-size: 24px; font-weight: 700; background: linear-gradient(135deg, #38bdf8, #818cf8); -webkit-background-clip: text; -webkit-text-fill-color: transparent; }" +
                    ".card { background: #1a1d2e; border: 1px solid #2d3748; border-radius: 16px; padding: 24px; margin-bottom: 24px; box-shadow: 0 8px 30px rgba(0,0,0,0.3); }" +
                    ".grid { display: grid; grid-template-columns: repeat(auto-fit, minmax(240px, 1fr)); gap: 16px; margin-bottom: 24px; }" +
                    ".stat { background: #242942; border-radius: 12px; padding: 18px; border-left: 4px solid #38bdf8; }" +
                    ".stat-num { font-size: 28px; font-weight: 700; color: #fff; margin-top: 8px; }" +
                    ".tabs { display: flex; gap: 12px; margin-bottom: 20px; border-bottom: 1px solid #2d3748; padding-bottom: 12px; }" +
                    ".tab-btn { background: none; border: none; color: #94a3b8; font-size: 16px; font-weight: 600; padding: 8px 16px; cursor: pointer; border-radius: 8px; transition: 0.2s; }" +
                    ".tab-btn.active { background: #38bdf8; color: #0f172a; }" +
                    "table { width: 100%; border-collapse: collapse; margin-top: 12px; }" +
                    "th, td { text-align: left; padding: 12px 16px; border-bottom: 1px solid #2d3748; font-size: 14px; }" +
                    "th { color: #94a3b8; font-weight: 600; }" +
                    "tr:hover { background: #242942; }" +
                    ".btn { background: #38bdf8; color: #0f172a; border: none; padding: 6px 14px; font-weight: 600; border-radius: 8px; cursor: pointer; transition: 0.2s; }" +
                    ".btn-danger { background: #f43f5e; color: #fff; }" +
                    ".btn-success { background: #10b981; color: #fff; }" +
                    "input { background: #0f111a; border: 1px solid #475569; color: #fff; padding: 8px 12px; border-radius: 8px; margin-right: 8px; outline: none; }" +
                    ".badge { padding: 4px 10px; border-radius: 20px; font-size: 12px; font-weight: 600; }" +
                    ".badge-active { background: rgba(16, 185, 129, 0.2); color: #34d399; }" +
                    ".badge-ban { background: rgba(244, 63, 94, 0.2); color: #fb7185; }" +
                    "</style></head><body>" +
                    "<div class='container'>" +
                    "  <div class='header'><div class='logo'>⚔️ MOBI ARMY 2 - VPS ADMIN PANEL</div><div id='authArea'><input type='password' id='admPass' placeholder='Mật khẩu Admin'><button class='btn' onclick='doAdminLogin()'>Đăng Nhập</button></div></div>" +
                    "  <div id='mainPanel' style='display:none;'>" +
                    "    <div class='grid'>" +
                    "      <div class='stat'><div>Tổng Người Chơi</div><div class='stat-num' id='stUsers'>0</div></div>" +
                    "      <div class='stat' style='border-left-color:#818cf8;'><div>Hệ Số Event EXP</div><div class='stat-num' id='stExp'>x1.0</div></div>" +
                    "      <div class='stat' style='border-left-color:#34d399;'><div>Tổng Nhiệm Vụ</div><div class='stat-num' id='stMissions'>82</div></div>" +
                    "    </div>" +
                    "    <div class='tabs'>" +
                    "      <button class='tab-btn active' onclick='showTab(\"users\")'>Quản Lý Người Chơi & Xu/Lượng</button>" +
                    "      <button class='tab-btn' onclick='showTab(\"missions\")'>Quản Lý Nhiệm Vụ 12 Gun</button>" +
                    "      <button class='tab-btn' onclick='showTab(\"config\")'>Cấu Hình Server</button>" +
                    "    </div>" +
                    "    <div id='tabUsers' class='card'><h3>Danh Sách Người Chơi</h3><div style='overflow-x:auto;'><table><thead><tr><th>Tài Khoản</th><th>Xu</th><th>Lượng</th><th>Cấp Độ</th><th>Trạng Thái</th><th>Hành Động</th></tr></thead><tbody id='userRows'></tbody></table></div></div>" +
                    "    <div id='tabMissions' class='card' style='display:none;'><h3>Danh Sách 82 Cấp Độ Nhiệm Vụ</h3><div style='overflow-x:auto;'><table><thead><tr><th>ID</th><th>Cấp</th><th>Tên Nhiệm Vụ</th><th>Yêu Cầu</th><th>Thưởng Xu</th><th>Thưởng Lượng</th><th>Thưởng EXP</th><th>Sửa</th></tr></thead><tbody id='missionRows'></tbody></table></div></div>" +
                    "    <div id='tabConfig' class='card' style='display:none;'><h3>Cài Đặt Sự Kiện & Quà Tân Thủ</h3><div style='margin-top:16px;display:flex;gap:12px;flex-wrap:wrap;align-items:center;'>" +
                    "      <label>Hệ số EXP:</label><input type='number' step='0.1' id='cfgExpRate' value='1.0' style='width:90px;'>" +
                    "      <label>Xu khởi tạo:</label><input type='number' id='cfgStarterXu' value='50000' style='width:120px;'>" +
                    "      <label>Lượng khởi tạo:</label><input type='number' id='cfgStarterLuong' value='100' style='width:100px;'>" +
                    "      <button class='btn btn-success' onclick='saveServerConfig()'>Lưu Cài Đặt</button>" +
                    "    </div></div>" +
                    "  </div>" +
                    "</div>" +
                    "<script>" +
                    "let admToken = '';" +
                    "async function doAdminLogin() {" +
                    "  const pass = document.getElementById('admPass').value;" +
                    "  const res = await fetch('/api/admin/login', {method:'POST', body:JSON.stringify({password:pass})});" +
                    "  const data = await res.json();" +
                    "  if(data.ok) {" +
                    "    admToken = data.token;" +
                    "    document.getElementById('authArea').innerHTML = '<span style=\"color:#34d399;font-weight:600;\">✓ Đã Đăng Nhập Admin</span>';" +
                    "    document.getElementById('mainPanel').style.display = 'block';" +
                    "    loadUsers(); loadMissions();" +
                    "  } else alert(data.message);" +
                    "}" +
                    "async function loadUsers() {" +
                    "  const res = await fetch('/api/admin/users', {headers:{'Authorization':'Bearer ' + admToken}});" +
                    "  const data = await res.json();" +
                    "  if(!data.ok) return;" +
                    "  document.getElementById('stUsers').innerText = data.users.length;" +
                    "  const tbody = document.getElementById('userRows');" +
                    "  tbody.innerHTML = data.users.map(u => `<tr>" +
                    "    <td><b>${u.username}</b></td>" +
                    "    <td>${u.xu.toLocaleString()}</td>" +
                    "    <td>${u.luong.toLocaleString()}</td>" +
                    "    <td>Lv ${u.level}</td>" +
                    "    <td><span class='badge ${u.is_banned ? 'badge-ban' : 'badge-active'}'>${u.is_banned ? 'BỊ KHÓA' : 'HOẠT ĐỘNG'}</span></td>" +
                    "    <td>" +
                    "      <button class='btn' onclick='editMoney(\"${u.username}\", ${u.xu}, ${u.luong})'>Cộng/Sửa Tiền</button> " +
                    "      <button class='btn btn-danger' onclick='toggleBan(\"${u.username}\")'>${u.is_banned ? 'Mở Khóa' : 'Khóa'}</button>" +
                    "    </td></tr>`).join('');" +
                    "}" +
                    "async function editMoney(u, xu, luong) {" +
                    "  const nXu = prompt(`Nhập số Xu mới cho ${u}:`, xu);" +
                    "  if(nXu === null) return;" +
                    "  const nLuong = prompt(`Nhập số Lượng mới cho ${u}:`, luong);" +
                    "  if(nLuong === null) return;" +
                    "  await fetch('/api/admin/users/update', {method:'POST', headers:{'Authorization':'Bearer ' + admToken}, body:JSON.stringify({username:u, set_xu:parseInt(nXu), set_luong:parseInt(nLuong)})});" +
                    "  loadUsers();" +
                    "}" +
                    "async function toggleBan(u) {" +
                    "  if(!confirm(`Xác nhận khóa / mở khóa tài khoản ${u}?`)) return;" +
                    "  await fetch('/api/admin/users/update', {method:'POST', headers:{'Authorization':'Bearer ' + admToken}, body:JSON.stringify({username:u, toggle_ban:1})});" +
                    "  loadUsers();" +
                    "}" +
                    "async function loadMissions() {" +
                    "  const res = await fetch('/api/game/config');" +
                    "  const data = await res.json();" +
                    "  if(!data.missions) return;" +
                    "  document.getElementById('stMissions').innerText = data.missions.length;" +
                    "  document.getElementById('stExp').innerText = 'x' + (data.event_exp_rate || 1.0);" +
                    "  document.getElementById('cfgExpRate').value = data.event_exp_rate || 1.0;" +
                    "  const tbody = document.getElementById('missionRows');" +
                    "  tbody.innerHTML = data.missions.map(m => `<tr>" +
                    "    <td>${m.id}</td><td>Lv ${m.level}</td><td><b>${m.name}</b></td><td>${m.require}</td><td>${m.reward_xu.toLocaleString()}</td><td>${m.reward_luong.toLocaleString()}</td><td>${m.reward_exp.toLocaleString()}</td>" +
                    "    <td><button class='btn' onclick='editMission(${m.id}, ${m.level}, \"${m.name}\", ${m.require}, ${m.reward_xu}, ${m.reward_luong}, ${m.reward_exp})'>Sửa</button></td></tr>`).join('');" +
                    "}" +
                    "async function editMission(id, lvl, name, req, xu, luong, exp) {" +
                    "  const nReq = prompt(`Số lượng yêu cầu cho nhiệm vụ:`, req);" +
                    "  if(nReq === null) return;" +
                    "  const nXu = prompt(`Thưởng Xu:`, xu);" +
                    "  if(nXu === null) return;" +
                    "  const nLuong = prompt(`Thưởng Lượng:`, luong);" +
                    "  if(nLuong === null) return;" +
                    "  const nExp = prompt(`Thưởng EXP:`, exp);" +
                    "  if(nExp === null) return;" +
                    "  await fetch('/api/admin/missions', {method:'POST', headers:{'Authorization':'Bearer ' + admToken}, body:JSON.stringify({id:id, level:lvl, require:parseInt(nReq), reward_xu:parseInt(nXu), reward_luong:parseInt(nLuong), reward_exp:parseInt(nExp)})});" +
                    "  loadMissions();" +
                    "}" +
                    "async function saveServerConfig() {" +
                    "  const rate = document.getElementById('cfgExpRate').value;" +
                    "  const sXu = document.getElementById('cfgStarterXu').value;" +
                    "  const sLuong = document.getElementById('cfgStarterLuong').value;" +
                    "  await fetch('/api/admin/config', {method:'POST', headers:{'Authorization':'Bearer ' + admToken}, body:JSON.stringify({event_exp_rate:rate, starter_xu:parseInt(sXu), starter_luong:parseInt(sLuong)})});" +
                    "  alert('Đã lưu cấu hình Server thành công!');" +
                    "}" +
                    "function showTab(t) {" +
                    "  document.querySelectorAll('.tab-btn').forEach(b => b.classList.remove('active'));" +
                    "  document.getElementById('tabUsers').style.display = t === 'users' ? 'block' : 'none';" +
                    "  document.getElementById('tabMissions').style.display = t === 'missions' ? 'block' : 'none';" +
                    "  document.getElementById('tabConfig').style.display = t === 'config' ? 'block' : 'none';" +
                    "  event.target.classList.add('active');" +
                    "}" +
                    "</script></body></html>";
            sendHtmlResponse(exchange, 200, html);
        }
    }

    static class RootHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String html = "<html><head><title>Army 2 Server</title><meta charset='utf-8'></head>" +
                    "<body style='font-family:sans-serif;text-align:center;padding:50px;background:#0f111a;color:#fff'>" +
                    "<h2>Mobi Army 2 Hybrid VPS Server</h2>" +
                    "<p style='color:#94a3b8;margin-top:8px;'>Server đang chạy mượt mà trên cổng " + PORT + "</p>" +
                    "<div style='margin-top:20px;'>" +
                    "  <a href='/admin' style='color:#38bdf8;font-weight:bold;text-decoration:none;background:#1e293b;padding:10px 20px;border-radius:8px;'>Trang Quản Trị Admin Web Dashboard</a>" +
                    "</div>" +
                    "</body></html>";
            sendHtmlResponse(exchange, 200, html);
        }
    }
}
