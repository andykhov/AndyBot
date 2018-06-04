import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;

public class GuildMusicManager {
    public final AudioPlayer player; //player for a guild
    public final TrackHandler trackHandler; //trackhandler for player

    public GuildMusicManager(AudioPlayerManager manager) {
        player = manager.createPlayer();
        trackHandler = new TrackHandler(player);
        player.addListener(trackHandler);
    }

    public AudioProvider getAudioProvider() {
        return new AudioProvider(player);
    }
}