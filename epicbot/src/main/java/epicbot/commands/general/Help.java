package epicbot.commands.general;

import java.util.ArrayList;
import java.util.List;

import epicbot.Epic;
import epicbot.commands.Command;
import epicbot.util.CommandHandler;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 * @author Kyle Minter (Kale Monkey)
 */
public class Help implements Command
{
	private static final String commandName = "Help";
	private static final String commandDescription = "Provides a list of known commands or specific usage intructions for a command.";
	private static final String commandUsage = "`" + Epic.settings.getCommandPrefix() + "help <Command>`" +
			"\nCommand must be a known command.";
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
			if (event.getMessage().getContentRaw().substring(1).equalsIgnoreCase("help"))
			{
				// Starts building an embedded message.
				EmbedBuilder eb = new EmbedBuilder();
				eb.setAuthor("Epic Gamer Bot", "https://github.com/KaleMonkey/epicbot");
				eb.setDescription("Try `" + Epic.settings.getCommandPrefix() + "help <Command>` to get specific usage instructions for a command!");
				
				// Adds all the command names to a string.
				String gen = "";
				String mod = "";
				for (Command command : Command.GENERAL_COMMANDS)
				{
					gen += command.getName() + "\n";
				}
				for (Command command : Command.MOD_COMMANDS)
				{
					mod += command.getName() + "\n";
				}
				
				// Puts all the command names into a field.
				eb.addField("General", gen, true);
				eb.addField("Moderation", mod, true);
				
				// Opens a private channel with the user and sends the embedded message.
				event.getAuthor().openPrivateChannel().queue((channel) ->
				{
					channel.sendMessage(eb.build()).queue();
				});
			}
			else
			{
				List<Object> arguments = getArguments(event);
				
				// Builds an embedded message with the command's name, description, and usage instructions.
				EmbedBuilder eb = new EmbedBuilder();
				eb.setAuthor("Epic Gamer Bot", "https://github.com/KaleMonkey/epicbot");
				eb.setTitle(((Command)arguments.get(0)).getName());
				eb.setDescription(((Command)arguments.get(0)).getDescription());
				eb.addField("Usage", ((Command)arguments.get(0)).getUsage(), false);
				
				// Opens a private channel with the user and sends the embedded message.
				event.getAuthor().openPrivateChannel().queue((channel) ->
				{
					channel.sendMessage(eb.build()).queue();
				});
			}
		}
		catch (IllegalArgumentException e)
		{
			// If the arguments are invalid the automated message will be sent.
			event.getChannel().sendMessage("You provided illegal arguments! Try `" +
					Epic.settings.getCommandPrefix() + "help` to get a list of commands.").queue();
		}
	}
	
	private List<Object> getArguments(MessageReceivedEvent event) throws IllegalArgumentException
	{
		String[] m = event.getMessage().getContentRaw().substring(1).split(" ");
		
		if (m.length < 1)
		{
			throw new IllegalArgumentException();
		}
		
		Command command = CommandHandler.checkCommands(m[1]);
		if (command == null)
		{
			throw new IllegalArgumentException();
		}
		
		List<Object> arguments = new ArrayList<Object>();
		arguments.add(command);
		return arguments;
	}
}
