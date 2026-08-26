/*
 * Decompiled with CFR 0.152.
 */
package CLib;

import CLib.SoundSystem;
import model.CRes;

public class mSound {
    public static float volumeSound = 0.0f;
    public static float volumeMusic = 0.0f;
    private static final int MAX_VOLUME = 10;
    public static int[] soundID;
    public static int CurMusic;
    public static boolean isMusic;
    public static boolean isSound;
    public static SoundSystem[] music;
    public static SoundSystem[] sound;
    public static boolean isEnableSound;
    public static int sizeMusic;
    public static int sizeSound;

    public static void init() {
        int n;
        music = new SoundSystem[sizeMusic];
        for (n = 0; n < music.length; ++n) {
            mSound.music[n] = new SoundSystem(String.valueOf(n), true);
        }
        sound = new SoundSystem[sizeSound];
        for (n = 0; n < sound.length; ++n) {
            boolean bl = false;
            mSound.sound[n] = new SoundSystem(String.valueOf(n), bl);
        }
        System.gc();
    }

    public static int getSoundPoolSource(int n, String string) {
        return n;
    }

    public static void playSound(int n, float f, int n2) {
    }

    public static void SetLoopSound(int n, float f, int n2) {
    }

    public static void UnSetLoopAll() {
    }

    public static void playMus(int n, float f, boolean bl) {
        if (isMusic) {
            if (music != null) {
                for (int i = 0; i < music.length; ++i) {
                    if (music[i] == null || !music[i].isPlaying() || i == n) continue;
                    music[i].pause();
                }
            }
            if (n >= 0 && n <= music.length - 1) {
                try {
                    music[n].setVolume(f, f);
                    music[n].setLooping(bl);
                    music[n].play(f);
                }
                catch (IllegalStateException illegalStateException) {
                    illegalStateException.printStackTrace();
                }
            }
        }
    }

    public static void pauseMusic(int n) {
    }

    public static void pauseCurMusic() {
        for (int i = 0; i < music.length; ++i) {
            if (!music[i].isPlaying()) continue;
            music[i].pause();
            CurMusic = i;
        }
    }

    public static void resumeMusic(int n) {
    }

    public static void resumeAll() {
    }

    public static void releaseAll() {
    }

    public static void stopAll() {
    }

    public static void stopSoundAll() {
        if (sound != null) {
            for (int i = 0; i < sound.length; ++i) {
                if (sound[i] == null) continue;
                sound[i].stop();
            }
        }
    }

    public static void setVolume(int n, int n2, int n3) {
    }

    public static void setVolume(int n) {
        CRes.saveRMSInt("sound", n);
        volumeSound = (float)n / 100.0f;
    }

    static {
        CurMusic = -1;
        isMusic = true;
        isSound = true;
        isEnableSound = true;
        sizeMusic = 1;
        sizeSound = 3;
    }
}

