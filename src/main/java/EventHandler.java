import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

public class EventHandler {

    @EventSubscriber
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getMessage().getContent().startsWith(BotUtils.BOT_PREFIX + "hello")) {
            BotUtils.sendMessage(event.getChannel(), "Hi there, I'm AndyBot!");
        } else if (event.getMessage().getContent().startsWith(BotUtils.BOT_PREFIX + "logout")) {
            BotUtils.sendMessage(event.getChannel(), "Logging out...");
            event.getClient().logout();
        }
    }
}