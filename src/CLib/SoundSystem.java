/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  javax.microedition.media.Manager
 *  javax.microedition.media.Player
 *  javax.microedition.media.control.VolumeControl
 */
package CLib;

import CLib.LibSysTem;
import java.io.InputStream;
import javax.microedition.media.Manager;
import javax.microedition.media.Player;
import javax.microedition.media.control.VolumeControl;

public class SoundSystem {
    private Player player;
    public boolean isLoop;

    public SoundSystem(String string, boolean bl) {
        this.isLoop = bl;
        if (string.indexOf(".ogg") >= 0) {
            throw new IllegalArgumentException("ONLY SUPPORT FILE.WAV AMD ");
        }
        try {
            String string2;
            String string3;
            if (bl) {
                string3 = "/" + LibSysTem.res + "/music/" + string + ".mp3";
                string2 = "audio/mpeg";
            } else {
                string3 = "/" + LibSysTem.res + "/sound/" + string + ".wav";
                string2 = "audio/x-wav";
            }
            InputStream inputStream = LibSysTem.openResource(string3);
            if (inputStream != null) {
                this.player = Manager.createPlayer((InputStream)inputStream, (String)string2);
                this.player.realize();
                if (bl) {
                    this.player.setLoopCount(-1);
                }
            }
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void applyVolume(float f) {
        if (this.player == null) {
            return;
        }
        try {
            VolumeControl volumeControl = (VolumeControl)this.player.getControl("VolumeControl");
            if (volumeControl != null) {
                volumeControl.setLevel((int)(f * 100.0f));
            }
        }
        catch (Exception exception) {
        }
    }

    public void play(float f) {
        if (this.player == null) {
            return;
        }
        try {
            this.applyVolume(f);
            this.player.setMediaTime(0L);
            this.player.start();
        }
        catch (Exception exception) {
        }
    }

    public boolean isPlaying() {
        return this.player != null && this.player.getState() == 400;
    }

    public void pause() {
        if (this.player == null) {
            return;
        }
        try {
            this.player.stop();
        }
        catch (Exception exception) {
        }
    }

    public void stop() {
        this.pause();
    }

    public void setLooping(boolean bl) {
        if (this.player == null) {
            return;
        }
        try {
            this.player.setLoopCount(bl ? -1 : 1);
        }
        catch (Exception exception) {
        }
    }

    public void setVolume(float f, float f2) {
        this.applyVolume(f2);
    }
}

