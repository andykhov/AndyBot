import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.playback.AudioFrame;
import sx.blah.discord.handle.audio.AudioEncodingType;
import sx.blah.discord.handle.audio.IAudioProvider;

/* wrapper class for AudioPlayer to behave as an AudioProvider */
public class AudioProvider implements IAudioProvider {
    private final AudioPlayer player;
    private AudioFrame lastFrame;

    public AudioProvider(AudioPlayer player) {
        this.player = player;
    }

    @Override
    public boolean isReady() {
        if (lastFrame == null) {
            lastFrame = player.provide();
        }

        return lastFrame != null;
    }

    @Override
    public byte[] provide() {
        if (lastFrame == null) {
            lastFrame = player.provide();
        }

        byte[] data = lastFrame != null ? lastFrame.getData() : null;

        return data;
    }

    @Override
    public int getChannels() {
        return 2;
    }

    @Override
    public AudioEncodingType getAudioEncodingType() {
        return AudioEncodingType.OPUS;
    }
}