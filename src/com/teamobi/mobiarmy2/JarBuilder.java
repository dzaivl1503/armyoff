package com.teamobi.mobiarmy2;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;

public class JarBuilder {
    public static void main(String[] args) {
        try {
            Path projectRoot = Paths.get(".");
            Path binDir = projectRoot.resolve("bin");
            Path manifestPath = projectRoot.resolve("META-INF").resolve("MANIFEST.MF");
            Path outputJar = projectRoot.resolve("Army2Offline.jar");

            Manifest manifest = new Manifest();
            if (Files.exists(manifestPath)) {
                try (InputStream is = Files.newInputStream(manifestPath)) {
                    manifest.read(is);
                }
            }

            try (JarOutputStream jos = new JarOutputStream(Files.newOutputStream(outputJar), manifest)) {
                Files.walk(binDir).forEach(path -> {
                    if (Files.isDirectory(path)) {
                        return;
                    }
                    String relativeName = binDir.relativize(path).toString().replace('\\', '/');
                    if (relativeName.equalsIgnoreCase("META-INF/MANIFEST.MF")) {
                        return;
                    }
                    try {
                        JarEntry entry = new JarEntry(relativeName);
                        jos.putNextEntry(entry);
                        Files.copy(path, jos);
                        jos.closeEntry();
                    } catch (Exception exception) {
                    }
                });
            }
            long size = Files.size(outputJar);
            System.out.println("[OK] Da tao thanh cong Army2Offline.jar (" + size + " bytes)!");
        } catch (Exception e) {
            System.err.println("[LOI] Khong the dong goi JAR: " + e.getMessage());
        }
    }
}
