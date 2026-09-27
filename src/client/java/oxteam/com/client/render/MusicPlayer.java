package oxteam.com.client.render;

import net.minecraft.client.Minecraft;

import javax.sound.sampled.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MusicPlayer {

    private static final List<String> playlist = new ArrayList<>();
    private static int currentTrack = -1;
    private static boolean playing = false;
    private static float volume = 0.7f;

    private static Clip currentClip = null;

    public static File getMusicFolder() {
        File folder = new File(Minecraft.getInstance().gameDirectory, "zeroxvisual/music");
        if (!folder.exists()) folder.mkdirs();
        return folder;
    }

    public static void loadPlaylist() {
        playlist.clear();
        File folder = getMusicFolder();
        File[] files = folder.listFiles();
        if (files == null) return;
        for (File f : files) {
            String n = f.getName().toLowerCase();
            if (n.endsWith(".mp3") || n.endsWith(".wav")
                    || n.endsWith(".ogg") || n.endsWith(".aiff")
                    || n.endsWith(".au")) {
                playlist.add(f.getName());
            }
        }
    }

    public static List<String> getPlaylist() { return playlist; }
    public static int getCurrentTrack() { return currentTrack; }

    public static String getCurrentTrackName() {
        if (currentTrack < 0 || currentTrack >= playlist.size()) return "—";
        return playlist.get(currentTrack);
    }

    public static boolean isPlaying() { return playing; }
    public static float getVolume() { return volume; }

    public static void setVolume(float v) {
        volume = Math.max(0f, Math.min(1f, v));
        if (currentClip != null && currentClip.isOpen()) {
            try {
                FloatControl gain = (FloatControl) currentClip.getControl(FloatControl.Type.MASTER_GAIN);
                gain.setValue(20f * (float) Math.log10(Math.max(0.0001f, volume)));
            } catch (Exception ignored) {}
        }
    }

    public static void play() {
        if (playlist.isEmpty()) loadPlaylist();
        if (playlist.isEmpty()) return;
        if (currentTrack < 0) currentTrack = 0;

        stop();

        try {
            File file = new File(getMusicFolder(), playlist.get(currentTrack));
            AudioInputStream audio = AudioSystem.getAudioInputStream(file);
            currentClip = AudioSystem.getClip();
            currentClip.open(audio);

            try {
                FloatControl gain = (FloatControl) currentClip.getControl(FloatControl.Type.MASTER_GAIN);
                gain.setValue(20f * (float) Math.log10(Math.max(0.0001f, volume)));
            } catch (Exception ignored) {}

            currentClip.addLineListener(event -> {
                if (event.getType() == LineEvent.Type.STOP && playing) {
                    next();
                }
            });

            currentClip.start();
            playing = true;
        } catch (Exception e) {
            e.printStackTrace();
            playing = false;
        }
    }

    public static void stop() {
        if (currentClip != null) {
            try {
                currentClip.stop();
                currentClip.close();
            } catch (Exception ignored) {}
            currentClip = null;
        }
        playing = false;
    }

    public static void pause() {
        if (currentClip != null) {
            currentClip.stop();
            playing = false;
        }
    }

    public static void next() {
        if (playlist.isEmpty()) loadPlaylist();
        if (playlist.isEmpty()) return;
        currentTrack = (currentTrack + 1) % playlist.size();
        boolean wasPlaying = playing;
        stop();
        if (wasPlaying) play();
    }

    public static void prev() {
        if (playlist.isEmpty()) loadPlaylist();
        if (playlist.isEmpty()) return;
        currentTrack = (currentTrack - 1 + playlist.size()) % playlist.size();
        boolean wasPlaying = playing;
        stop();
        if (wasPlaying) play();
    }

    public static void toggle() {
        if (playing) pause();
        else play();
    }
}