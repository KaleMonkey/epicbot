package epicbot.commands;

import java.net.Socket;

import epicbot.Epic;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 * @author Kyle Minter (Kale Monkey)
 */
public class Servers implements Command
{
	private static final String commandName = "Servers";
	private static final String commandDescription = "Gets a list of servers provided by Kale and shows if they are running or not";
	private static final String commandUsage = "`" + Epic.settings.getCommandPrefix() + "servers`";
	private static final boolean commandGuildOnly = false;
	
	/**
	 * Returns the name of the command.
	 * @return the command name
	 */
	public String getName()
	{
		return commandName;
	}
	
	/**
	 * Returns the description of the command
	 * @return the command description
	 */
	public String getDescription()
	{
		return commandDescription;
	}
	
	/**
	 * Returns the usage instructions of the command
	 * @return the command description
	 */
	public String getUsage()
	{
		return commandUsage;
	}
	
	/**
	 * Checks if the command can be only used in a server.
	 * @return true if it can only be used in a server, false if it can be used elsewhere
	 */
	public boolean GuildOnly()
	{
		return commandGuildOnly;
	}
	
	/**
	 * Attempts to execute the command.
	 * @param event the event containing the message
	 */
	public void execute(MessageReceivedEvent event)
	{
		try
		{
			if (event.getMessage().getContentRaw().substring(1).equalsIgnoreCase("servers"))
			{
				event.getChannel().sendMessage("This might take a second.").queue();
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
				event.getChannel().sendMessage(eb.build()).queue();
			}
		}
		catch (Exception e)
		{
			// Idk, just in case something goes wrong lmfao.
		}
	}
	
	/**
	 * Checks if there is a server running on the provided host and port.
	 * @param host ip or hostname of the computer running the server
	 * @param port the port that the server is running on
	 * @return true if there is a server running at the host on the specified port, false if there is not.
	 */
	private static boolean isServerListening(String host, int port)
	{
		Socket s = null;
		
		try
		{
			s = new Socket(host, port);
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
