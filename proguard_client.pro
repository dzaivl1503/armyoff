-injars bin
-outjars Army2Offline_obf.jar

-libraryjars lib/cldcapi11.jar
-libraryjars lib/midpapi20.jar
-libraryjars lib/microemu-nokiaui.jar
-libraryjars lib/microemu-siemensapi.jar
-libraryjars lib/microemu-jsr-75.jar
-libraryjars lib/microemu-jsr-82.jar
-libraryjars lib/microemu-jsr-120.jar
-libraryjars lib/microemu-jsr-135.jar
-libraryjars lib/microemulator.jar

-dontwarn **
-dontnote **
-ignorewarnings

-microedition
-overloadaggressively
-repackageclasses ''
-allowaccessmodification

-keep public class com.teamobi.mobiarmy2.GameMidlet {
    public *;
}

-keep public class Launcher {
    public static void main(java.lang.String[]);
}

-keep public class com.teamobi.mobiarmy2.JarBuilder {
    public static void main(java.lang.String[]);
}
