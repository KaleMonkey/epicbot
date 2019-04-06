package epicbot.commands.general;

import epicbot.Epic;
import epicbot.commands.Command;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 * @author Kyle Minter (Kale Monkey)
 */
public class Ping implements Command
{
	private static final String commandName = "Ping";
	private static final String commandDescription = "Shows the time, in milliseconds, it takes for the bot to respond.";
	private static final String commandUsage = "`" + Epic.settings.getCommandPrefix() + "ping`";
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
			event.getChannel().sendMessage("Took " + event.getJDA().getPing() + " milliseconds to respond!").queue();
		}
		catch (Exception e)
		{
			// idk. incase anything goes wrong.
		}
	}
}
