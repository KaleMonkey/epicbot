package epicbot.commands.tag;

import epicbot.Epic;
import epicbot.commands.Command;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class CreateTag implements Command
{
	private static final String commandName = "CreateTag";
	private static final String commandDescription = "Creates a new tag with a given name and content.";
	private static final String commandUsage = "`" + Epic.settings.getCommandPrefix() + "createtag <Name> <Content>`\nName must be unique." +
	"\nContent is the content of the tag.";
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
		
	}
}
