package epicbot.commands.general;

import java.net.InetSocketAddress;
import java.net.Socket;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

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
		// Creates arrays of server names and the ports they are running on.
		String[] servers = {"Vanilla Tweaks", "Sky Factory", "CS:GO"};
		int[] ports = {25565, 25566, 27015};
		
		// Starts building an embedded message with the server's status.
		EmbedBuilder eb = new EmbedBuilder();
		eb.setAuthor("Epic Gamer Bot", "https://github.com/KaleMonkey/epicbot");
		for (int i = 0; i < servers.length; i++)
		{
			// Checks if the server is listening on the provided port.
			if (isServerListening("kale.ddns.net", ports[i]))
			{
				// If it is online a field will be added to the message.
				eb.addField(servers[i], ":large_blue_circle: Online", true);
			}
			else
			{
				// If it is offline a field will be added to the message.
				eb.addField(servers[i], ":red_circle: Offline", true);
			}
		}
		
		// Sends the generated message.
		event.reply(eb.build());
	}
	
	/**
	 * Checks if there is a server running on the provided host and port.
	 * @param host ip or hostname of the computer running the server
	 * @param port the port that the server is running on
	 * @return true if there is a server running at the host on the specified port, false if there is not.
	 */
	private static boolean isServerListening(String host, int port)
	{
		Socket s = new Socket();
		
		try
		{
			s.connect(new InetSocketAddress(host, port), 3);
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
