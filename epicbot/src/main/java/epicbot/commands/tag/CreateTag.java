package epicbot.commands.tag;

import epicbot.Epic;
import epicbot.commands.Command;
import epicbot.util.Tag;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class CreateTag implements Command
{
	private static final String commandName = "CreateTag";
	private static final String commandDescription = "Creates a tag with a given name and content.";
	private static final String commandUsage = "`" + Epic.settings.getCommandPrefix() + "tag <Name> <Content>`\nName must be the name of an existing tag." +
			"\nContent can be anything.";
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
		String[] message = event.getMessage().getContentDisplay().split(" ");
		
		if (message.length < 3)
		{
			event.getChannel().sendMessage("You provided illegal arguments! Try `" + Epic.settings.getCommandPrefix() +
					"help createtag` to get help with this command.").queue();
			return;
		}
		
		String tagName = message[1];
		String tagContent = "";
		for (int i = 2; i < message.length; i++)
		{
			tagContent += message[i] + " ";
		}
		
		Tag tag = new Tag(tagName, tagContent);
		if (Tag.getTag(tag) == null)
		{
			Tag.addTag(tag);
			event.getChannel().sendMessage("Created tag!").queue();
		}
		else
		{
			event.getChannel().sendMessage("The tag name must be unique!").queue();
		}
	}
}
