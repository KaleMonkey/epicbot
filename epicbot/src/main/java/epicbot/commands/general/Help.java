package epicbot.commands.general;

import java.util.List;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.command.impl.CommandClientImpl;

import epicbot.Epic;
import epicbot.commands.moderation.BotBan;
import epicbot.settings.SettingsManager;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;

/**
 * @author Kyle Minter (Kale Monkey)
 */
public class Help extends Command
{
	public Help()
	{
		this.name = "help";
		this.help = "Sends a list of commands or gets usage instructions for a specified command.";
		this.arguments = "[command (optional)]";
		this.category = new Category("General");
		this.guildOnly = false;
		this.botPermissions = new Permission[] {Permission.MESSAGE_WRITE};
	}
	
	public void execute(CommandEvent event)
	{
		if (BotBan.isBotBanned(event.getMember()))
		{
			event.reply("You are bot banned on this server! You must be unbanned to use any of my commands.");
			return;
		}
		
		// If there are more than one argument an error message will be sent.
		if (event.getArgs().split(" ").length > 1)
		{
			event.reply("You provided too many arguments!" + getHelp(this.name));
		}
		else if ((event.getArgs().split(" "))[0].equalsIgnoreCase("me"))
		{
			event.reply("just kill yourself fag");
		}
		else 
		{
			// Grabs a list of commands.
			List<Command> commands = null;
			List<Object> listeners = Epic.getAPI().getRegisteredListeners();
			for (Object l : listeners)
			{
				if (l instanceof CommandClientImpl)
				{
					commands = ((CommandClientImpl)l).getCommands();
				}
			}
			
			// If the command call has no arguments a list of commands will be sent.
			if (event.getArgs().equals(""))
			{
				//Starts building an embedded message.
				EmbedBuilder eb =new EmbedBuilder();
				eb.setAuthor("Epic", "https://github.com/KaleMonkey/epicbot");
				eb.setDescription("Try `" + SettingsManager.getInstance().getSettings().getCommandPrefix() + "help [command]` to get specific usage instructions for a command!");
				
				// Assembles a list of all the commands.
				String temp = "";
				for (Command command : commands)
				{
					// If a command is an owner command it will be excluded from the list.
					if (!command.isOwnerCommand())
					{
						temp += command.getName() + "\n";
					}
				}
				
				// Puts all the command names into a field.
				eb.addField("Commands", temp, true);
				
				// Opens a private channel with the user and sends the embedded message.
				event.getAuthor().openPrivateChannel().queue((channel) ->
				{
					channel.sendMessage(eb.build()).queue();
				});
			}
			else
			{
				Command cmd = null;
				
				// Checks if the provided argument matches any of the command names.
				for (Command command : commands)
				{
					if (event.getArgs().split(" ")[0].equalsIgnoreCase(command.getName()))
					{
						// If the argument matches a command name the command object is saved.
						cmd = command;
					}
				}
				
				// If cmd is still null an error message will be sent.
				if (cmd == null)
				{
					event.reply("The command provided does not match any know commands!" + getHelp(this.name));
				}
				else
				{
					// Builds the help message for the specified command.
					EmbedBuilder eb = new EmbedBuilder();
					eb.setAuthor("Epic", "https://github.com/KaleMonkey/epicbot");
					eb.setTitle(cmd.getName());
					eb.setDescription(cmd.getHelp());
					if (cmd.getArguments() != null)
					{
						eb.addField("Arguments", cmd.getArguments(), true);
					}
					
					// Opens a private channel with the user and sends the embedded message.
					event.getAuthor().openPrivateChannel().queue((channel) ->
					{
						channel.sendMessage(eb.build()).queue();
					});
				}
			}
		}
	}
	
	public static String getHelp(String cmdName)
	{
		return " Try `" + SettingsManager.getInstance().getSettings().getCommandPrefix() + "help " + cmdName + "` to get help with this command!";
	}
}
