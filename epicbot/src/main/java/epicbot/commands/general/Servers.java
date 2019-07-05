package epicbot.commands.general;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import epicbot.commands.moderation.BotBan;
import epicbot.entities.Server;
import epicbot.settings.SettingsManager;
import epicbot.util.Rcon;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;

/**
 * @author Kyle Minter (Kale Monkey)
 */
public class Servers extends Command
{
	public Servers()
	{
		this.name = "servers";
		this.help = "Gets a list of servers provided by Kale and shows if they are running or not.";
		this.category = new Category("General");
		this.guildOnly = false;
		this.botPermissions = new Permission[] {Permission.MESSAGE_WRITE};
	}
	
	public void execute(CommandEvent event)
	{
		if (BotBan.isBotBanned(event.getMember()))
		{
			event.reply("You are bot banned on this server! You must be unbanned to use any of my commands.");
		}
		else if (event.getArgs().equals(""))
		{
			// Loads the provided servers from the Config.json.
			Server[] servers = SettingsManager.getInstance().getSettings().getServers();
			
			// Starts building an embedded message with the server's status.
			EmbedBuilder eb = new EmbedBuilder();
			eb.setAuthor("Epic", "https://github.com/KaleMonkey/epicbot");
			for (int i = 0; i < servers.length; i++)
			{
				// Checks if the server is listening on the provided port.
				try
				{
					Rcon rcon = new Rcon(servers[i].getHostName(), servers[i].getRconPort(), 1000, servers[i].getRconPassword().getBytes());
					
					// If it is online then a blue circle emoji will be added to the results.
					String result = ":large_blue_circle: Online";
					
					// Attemps to get the player count of the server.
					try
					{
						// If it is able to get the player count it will be added to the results.
						result += "\n" + rcon.command("list").split(" ")[2] + " players online";
					}
					catch (Exception e)
					{
						// If it is unable to get the player count then an error message will be added to the results
						result += "\nUnable to get player count";
					}
					
					// After the getting the player count has been attempted the results will be added to a field in the message.
					eb.addField(servers[i].getServerName(), result, true);
					
				}
				catch(Exception e)
				{
					// If it is offline a field will be added to the message.
					eb.addField(servers[i].getServerName(), ":red_circle: Offline", true);
				}
			}
			
			// Sends the generated message.
			event.reply(eb.build());
		}
		else
		{
			event.reply("This command does not have any arguments!" + Help.getHelp(this.name));
		}
	}
}