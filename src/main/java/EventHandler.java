import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IRole;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class EventHandler {
    private final AudioPlayerManager = playerManager;
    private final Map<Long, GuildMusicManager> musicManagers; // coordinates GuildMusicManagers for each guild

    public EventHandler() {
        this.musicManagers = new HashMap<>();
        this.playerManager = new DefaultAudioPlayerManager();
        AudioSourceManagers.registerRemoteSources(playerManager);
        AudioSourceManagers.registerLocalSource(playerManager);
    }

    @EventSubscriber
    public void onMessageReceived(MessageReceivedEvent event) {
        /* ">hello" command */
        if (event.getMessage().getContent().startsWith(BotUtils.BOT_PREFIX + "hello")) {
            BotUtils.sendMessage(event.getChannel(), "Hi there, I'm AndyBot!");
        } 
        /* ">logout" command - check if author of message has permission */
        else if (event.getMessage().getContent().startsWith(BotUtils.BOT_PREFIX + "logout")) {
            if (hasTopRole(event.getGuild(), event.getAuthor())) {
                BotUtils.sendMessage(event.getChannel(), "Logging out...");
                event.getClient().logout();
            } else {
                BotUtils.sendMessage(event.getChannel(), "You do not have permission to log me out");
            }
        }
    }

    private synchronized GuildMusicManager getGuildAudioPlayer(IGuild guild) {
        GuildMusicManager musicManager = musicManagers.get(guild.getLongId());

        if (musicManager == null) {
            musicManager = new GuildMusicManager();
            musicManagers.put(guild.getLongId(), musicManager);
        }

        guild.getAudioManager().setAudioProvider(musicManager);

        return musicManager;
    }

    private void play(IGuild guild, GuildMusicManager musicManager, AudioTrack track) {
        GuildMusicManager manager = getGuildAudioPlayer(guild);
        manager.trackHandler.queue(track);
    }



    /* returns true if user has the top role in the guild */
    private boolean hasTopRole(IGuild guild, IUser user) {
        List<IRole> roles = guild.getRoles();
        return user.hasRole(roles.get(roles.size() - 1));
    }
}