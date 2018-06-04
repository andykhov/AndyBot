import sx.blah.discord.api.IDiscordClient;

public class Main {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("ERROR: Please include bot token as first argument");
        }

        IDiscordClient client = BotUtils.buildDiscordClient(args[0]); //builds client with token
        client.getDispatcher().registerListener(new EventHandler());
        client.login(); //login to invited guilds
    }
}
