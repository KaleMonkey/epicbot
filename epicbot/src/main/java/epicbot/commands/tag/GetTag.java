package epicbot.commands.tag;

import epicbot.Epic;
import epicbot.commands.Command;
import epicbot.util.Tag;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class GetTag implements Command
{
	private static final String commandName = "Tag";
	private static final String commandDescription = "Sends the content of the specified tag.";
	private static final String commandUsage = "`" + Epic.settings.getCommandPrefix() + "tag <Name>`\nName must be the name of an existing tag.";
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
			String[] message = event.getMessage().getContentDisplay().split(" ");
			
			if (message.length > 2 || message.length == 1)
			{
				event.getChannel().sendMessage("You provided illegal arguments! Try `" + Epic.settings.getCommandPrefix() +
						"help tag` to get help with this command.").queue();
			}
			else
			{
				Tag tag = Tag.getTag(new Tag(message[1]));
				
				if (tag != null)
				{
					event.getChannel().sendMessage(Tag.getTag(tag).getContent()).queue();
				}
				else
				{
					event.getChannel().sendMessage("Tag \"" + message[1] + "\" does not exist!").queue();
				}
			}
		}
		catch (Exception e)
		{
			
		}
	}
}
