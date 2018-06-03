import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IRole;
import java.util.List;

public class EventHandler {

    @EventSubscriber
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getMessage().getContent().startsWith(BotUtils.BOT_PREFIX + "hello")) {
            BotUtils.sendMessage(event.getChannel(), "Hi there, I'm AndyBot!");
        } else if (event.getMessage().getContent().startsWith(BotUtils.BOT_PREFIX + "logout")) {
            if (hasTopRole(event.getGuild(), event.getAuthor())) {
                BotUtils.sendMessage(event.getChannel(), "Logging out...");
                event.getClient().logout();
            } else {
                BotUtils.sendMessage(event.getChannel(), "You do not have permission to log me out");
            }
        } else if (event.getMessage().getContent().startsWith(BotUtils.BOT_PREFIX + "roles")) {
            BotUtils.sendMessage(event.getChannel(), event.getGuild().getRoles().get(event.getGuild().getRoles().size() - 1).getName());
        }
    }

    //returns true if user has the top role in the guild
    private boolean hasTopRole(IGuild guild, IUser user) {
        List<IRole> roles = guild.getRoles();
        return user.hasRole(roles.get(roles.size() - 1));
    }
}