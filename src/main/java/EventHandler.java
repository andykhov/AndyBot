import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IVoiceChannel;
import sx.blah.discord.handle.audio.IAudioManager;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.util.MissingPermissionsException;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class EventHandler {
    private final AudioPlayerManager playerManager;
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

    private void loadAndPlay(IChannel channel, String url) {
        GuildMusicManager manager = getGuildAudioPlayer(channel.getGuild());
        /*
        playerManager.loadItemOrdered(manager, url, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                BotUtils.sendMessage(channel, "Adding to queue "  + track.getInfo().title); 
            }
        });*/
    }

    private synchronized GuildMusicManager getGuildAudioPlayer(IGuild guild) {
        GuildMusicManager musicManager = musicManagers.get(guild.getLongID());

        if (musicManager == null) {
            musicManager = new GuildMusicManager(playerManager);
            musicManagers.put(guild.getLongID(), musicManager);
        }

        guild.getAudioManager().setAudioProvider(musicManager.getAudioProvider());

        return musicManager;
    }

    /* queue given AudioTrack to guild's track handler */
    private void play(IGuild guild, GuildMusicManager manager, AudioTrack track) {
        connectToFirstVoiceChannel(guild.getAudioManager());
        manager.trackHandler.queue(track);
    }

    private static void connectToFirstVoiceChannel(IAudioManager audioManager) {
        for (IVoiceChannel voiceChannel : audioManager.getGuild().getVoiceChannels()) {
            if (voiceChannel.isConnected()) {
                return;
            }
        }

        for (IVoiceChannel voiceChannel : audioManager.getGuild().getVoiceChannels()) {
            try {
                voiceChannel.join();
            } catch (MissingPermissionsException e) {
                // log.warn("Cannot enter voice channel {}", voiceChannel.getName(), e);
            }
        }
    }




    /* returns true if user has the top role in the guild */
    private boolean hasTopRole(IGuild guild, IUser user) {
        List<IRole> roles = guild.getRoles();
        return user.hasRole(roles.get(roles.size() - 1));
    }
}