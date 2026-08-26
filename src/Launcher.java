import org.microemu.app.Main;

public class Launcher {
    public static void main(String[] args) {
        try {
            System.out.println("Starting Army2 Offline via MicroEmulator...");
            Main.main(new String[]{
                "--appclassloader", "system",
                "com.teamobi.mobiarmy2.GameMidlet"
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
