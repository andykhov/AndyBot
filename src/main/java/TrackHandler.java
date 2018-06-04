import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


public class TrackHandler extends AudioEventAdapter {
    private final AudioPlayer player;
    private final BlockingQueue<AudioTrack> trackQueue;

    /* @param this handler will use player as its AudioPlayer */
    public TrackHandler(AudioPlayer player) {
        this.player = player;
        trackQueue = new LinkedBlockingQueue<>();
    }

    /* plays the given track if no track is currently playing, or add to queue
     * @param track to play/queue
     */
    public void queue(AudioTrack track) {
        if (!player.startTrack(track, true)) {
            trackQueue.offer(track);
        }
    }

    /* play the next track, stops the current track playing */
    public void nextTrack() {
        player.startTrack(trackQueue.poll(), false); //if next track is null, the player stops
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        if (endReason.mayStartNext) {
            nextTrack();
        }
    }


}