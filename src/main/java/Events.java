import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

public class Events {

    @EventSubscriber
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getMessage().getContent().startsWith(BotUtils.BOT_PREFIX + "hello")) {
            BotUtils.sendMessage(event.getChannel(), "Hi there, I'm AndyBot!");
        }
    }
}