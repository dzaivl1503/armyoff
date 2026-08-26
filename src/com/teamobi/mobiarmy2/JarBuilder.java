package com.teamobi.mobiarmy2;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;

public class JarBuilder {
    public static void main(String[] args) {
        try {
            Path projectRoot = Paths.get(".");
            Path binDir = projectRoot.resolve("bin");
            Path manifestPath = projectRoot.resolve("META-INF").resolve("MANIFEST.MF");
            Path outputJar = projectRoot.resolve("Army2Offline.jar");
            Path tempRawJar = projectRoot.resolve("Army2Offline_raw.jar");
            Path obfJar = projectRoot.resolve("Army2Offline_obf.jar");

            Manifest manifest = new Manifest();
            if (Files.exists(manifestPath)) {
                try (InputStream is = Files.newInputStream(manifestPath)) {
                    manifest.read(is);
                }
            } else {
                manifest.getMainAttributes().putValue("Manifest-Version", "1.0");
                manifest.getMainAttributes().putValue("MIDlet-1", "MobiArmy2, /icon.png, com.teamobi.mobiarmy2.GameMidlet");
                manifest.getMainAttributes().putValue("MIDlet-Name", "MobiArmy2");
                manifest.getMainAttributes().putValue("MIDlet-Vendor", "Teamobi");
                manifest.getMainAttributes().putValue("MIDlet-Version", "2.4.2");
                manifest.getMainAttributes().putValue("MicroEdition-Configuration", "CLDC-1.1");
                manifest.getMainAttributes().putValue("MicroEdition-Profile", "MIDP-2.0");
                manifest.getMainAttributes().putValue("Main-Class", "Launcher");
            }

            // 1. Pack all files into raw JAR
            try (JarOutputStream jos = new JarOutputStream(Files.newOutputStream(tempRawJar), manifest)) {
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

            // 2. Check if ProGuard is available for auto-obfuscation
            Path proguardJar = projectRoot.resolve("tools").resolve("proguard").resolve("proguard.jar");
            Path proguardCfg = projectRoot.resolve("proguard_client.pro");
            boolean obfuscated = false;

            if (Files.exists(proguardJar) && Files.exists(proguardCfg)) {
                try {
                    System.out.println("[OBF] Dang ma hoa Obfuscate ma nguon (ProGuard)...");
                    ProcessBuilder pb = new ProcessBuilder(
                        "java", "-jar", proguardJar.toAbsolutePath().toString(),
                        "@" + proguardCfg.toAbsolutePath().toString()
                    );
                    pb.directory(projectRoot.toFile());
                    pb.redirectErrorStream(true);
                    Process process = pb.start();
                    int exitCode = process.waitFor();
                    if (exitCode == 0 && Files.exists(obfJar)) {
                        // Re-inject proper manifest into obfJar if needed
                        Path finalTemp = projectRoot.resolve("Army2Offline_final.jar");
                        try (JarFile jf = new JarFile(obfJar.toFile());
                             JarOutputStream jos = new JarOutputStream(Files.newOutputStream(finalTemp), manifest)) {
                            jf.stream().forEach(entry -> {
                                if (entry.getName().equalsIgnoreCase("META-INF/MANIFEST.MF")) {
                                    return;
                                }
                                try (InputStream is = jf.getInputStream(entry)) {
                                    jos.putNextEntry(new JarEntry(entry.getName()));
                                    byte[] buf = new byte[8192];
                                    int len;
                                    while ((len = is.read(buf)) > 0) {
                                        jos.write(buf, 0, len);
                                    }
                                    jos.closeEntry();
                                } catch (Exception e) {
                                }
                            });
                        }
                        Files.move(finalTemp, outputJar, StandardCopyOption.REPLACE_EXISTING);
                        Files.deleteIfExists(obfJar);
                        obfuscated = true;
                    }
                } catch (Exception e) {
                    System.err.println("[OBF WARNING] ProGuard error: " + e.getMessage());
                }
            }

            if (!obfuscated) {
                Files.move(tempRawJar, outputJar, StandardCopyOption.REPLACE_EXISTING);
            } else {
                Files.deleteIfExists(tempRawJar);
            }

            long size = Files.size(outputJar);
            System.out.println("[OK] Da tao thanh cong Army2Offline.jar " + (obfuscated ? "(Auto Obfuscated) " : "") + "(" + size + " bytes)!");
        } catch (Exception e) {
            System.err.println("[LOI] Khong the dong goi JAR: " + e.getMessage());
        }
    }
}
