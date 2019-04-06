package epicbot.commands.general;

import java.io.File;

import epicbot.Epic;
import epicbot.commands.Command;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.exceptions.InsufficientPermissionException;

/**
 * @author Kyle Minter (Kale Monkey)
 */
public class About implements Command
{
	private static final String commandName = "About";
	private static final String commandDescription = "Shows info about the bot such as the creator and github page.";
	private static final String commandUsage = "`" + Epic.settings.getCommandPrefix() + "about`";
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
		// Since this command doesn't have any arguments we will just check that "about" is the only thing sent in the message.
		if (event.getMessage().getContentRaw().substring(1).equalsIgnoreCase("about"))
		{
			try
			{
				EmbedBuilder eb = new EmbedBuilder();
				eb.setAuthor("Epic Gamer Bot", "https://github.com/KaleMonkey/epicbot");
				eb.setDescription("The Epic bot is a discord bot created for the Epic Gamers discord server. It is currently in development and is prone to bugs. If you find any bugs report them to Kale!");
				eb.addField("Author", "Kale", true);
				eb.addField("GitHub", "https://github.com/KaleMonkey/epicbot", true);
				eb.addField("Libraries", "JDA - https://github.com/DV8FromTheWorld/JDA\nGSON - https://github.com/google/gson", true);
				File currentBuild = new File(Epic.class.getProtectionDomain().getCodeSource().getLocation().getPath());
				eb.addField("Current Build", currentBuild.getName().substring(0, currentBuild.getName().length() - 4), false);
				MessageEmbed m = eb.build();
				event.getChannel().sendMessage(m).queue();
			}
			catch (InsufficientPermissionException e)
			{
				System.out.println("\n[Epic]: Unable to send messages in channel \"" + event.getChannel().getName() + "\"!");
				System.out.println("[Epic]: Please give the bot permissions!");
			}
		}
	}
}
