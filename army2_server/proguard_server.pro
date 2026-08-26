-injars bin(Army2Server*.class)
-outjars bin_obf.jar

-libraryjars lib/mysql-connector-j-8.3.0.jar
-dontwarn **
-dontnote **
-ignorewarnings
-dontshrink
-dontoptimize

-keep public class Army2Server {
    public static void main(java.lang.String[]);
}
