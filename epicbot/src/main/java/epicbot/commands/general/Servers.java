package epicbot.commands.general;

import java.net.InetSocketAddress;
import java.net.Socket;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import epicbot.entities.Server;
import epicbot.settings.SettingsManager;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.kronos.rkon.core.Rcon;

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
		if (event.getArgs().equals(""))
		{
			// Loads the provided servers from the Config.json.
			Server[] servers = SettingsManager.getInstance().getSettings().getServers();
			
			// Starts building an embedded message with the server's status.
			EmbedBuilder eb = new EmbedBuilder();
			eb.setAuthor("Epic", "https://github.com/KaleMonkey/epicbot");
			for (int i = 0; i < servers.length; i++)
			{
				// Checks if the server is listening on the provided port.
				if (isServerListening(servers[i].getHostName(), servers[i].getPort()))
				{
					String result = ":large_blue_circle: Online";
					
					try
					{
						Rcon rcon = new Rcon(servers[i].getHostName(), servers[i].getRconPort(), servers[i].getRconPassword().getBytes());
						result += "\n" + rcon.command("list").split(" ")[2] + " players online";
					}
					catch (Exception e)
					{
						result += "\nUnable to get player count";
					}
					
					// If it is online a field will be added to the message.
					eb.addField(servers[i].getServerName(), result, true);
					
				}
				else
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
	
	/**
	 * Checks if there is a server running on the provided host and port.
	 * @param host ip or hostname of the computer running the server
	 * @param port the port that the server is running on
	 * @return true if there is a server running at the host on the specified port, false if there is not.
	 */
	public static boolean isServerListening(String host, int port)
	{
		Socket s = new Socket();
		
		try
		{
			s.connect(new InetSocketAddress(host, port), 1000);
			return true;
		}
		catch (Exception e)
		{
			return false;
		}
		finally
		{
			if (s != null)
			{
				try {s.close();}
				catch (Exception e) {}
			}
		}
	}
}
