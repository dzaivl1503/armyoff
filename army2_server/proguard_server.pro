-injars Army2Server.jar
-outjars Army2Server_protected.jar

-libraryjars lib/mysql-connector-j-8.3.0.jar
-dontwarn **
-dontnote **
-ignorewarnings

-keep public class Army2Server {
    public static void main(java.lang.String[]);
}
-keep class com.mysql.** { *; }
-keep interface ** { *; }
