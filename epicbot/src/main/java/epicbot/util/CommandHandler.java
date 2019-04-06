package epicbot.util;

import java.util.List;

import epicbot.Epic;
import epicbot.commands.Command;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 * @author Kyle Minter (Kale Monkey)
 */
public class CommandHandler
{	
	/**
	 * Returns the mute role for the bot.
	 * @param g the guild that the event happened in
	 * @return the mute role
	 */
	public static Role getMuteRole(Guild g)
	{
		List<Role> r = g.getRolesByName(Epic.settings.getMuteRole(), true);
		if (r.size() > 0)
		{
			return r.get(0);
		}
		return null;
	}
	
	/**
	 * Returns the NSFW role for the bot.
	 * @param g the guild that the event happened in
	 * @return the NSFW role
	 */
	public static Role getNsfwRole(Guild g)
	{
		List<Role> r = g.getRolesByName(Epic.settings.getNsfwRole(), true);
		if (r.size() > 0)
		{
			return r.get(0);
		}
		return null;
	}
	
	/**
	 * Returns the log channel for the bot.
	 * @param g the guild that the event happened in
	 * @return the log channel
	 */
	public static TextChannel getLogChannel(Guild g)
	{
		List<TextChannel> c = g.getTextChannelsByName(Epic.settings.getLogChannelName(), true);
		if (c.size() > 0)
		{
			return c.get(0);
		}
		return null;
	}
	
	/**
	 * Checks if the provided array list of user roles matches any of the guild roles that are OPed.
	 * @param g the guild object that the event happened in
	 * @param r an array list of user roles
	 * @return true if a any of the user roles matches any of the OPed roles in the guild, false if they don't
	 */
	public static boolean checkPerms(Guild g, List<Role> r)
	{	
		// Gets any of the roles in the guild that is OPed.
		List<Role> guildRoles = g.getRolesByName(Epic.settings.getOpedRole(), true);
		
		// Checks to see if the provided user roles matches any of the OPed roles in the guild.
		for (Role op : guildRoles)
		{
			for (Role userRole : r)
			{
				if (userRole.equals(op))
				{
					// If a user role matches an OPed role, true will be returned.
					return true;
				}
			}
		}
		// If none of the user roles matches any of the OPed roles in the guild, false will be returned.
		return false;
	}
	
	/**
	 * Checks if the provided string matches any known commands. If it does the command object will be returned, if not null will be returned.
	 * @param command the command to be checked
	 * @return the command object that the provided string matches.
	 */
	public static Command checkCommands(String command)
	{
		// Checks if the provided string matches any of the known commands.
		for (int i = 0; i < Command.COMMANDS.length; i++)
		{
			if (command.equalsIgnoreCase(Command.COMMANDS[i].getName()))
			{
				// If the string matches a command the command object will be returned.
				return Command.COMMANDS[i];
			}
		}
		// If the string does not match any of the commands null will be returned.
		return null;
	}
	
	/**
	 * Checks to see if the message provided is calling a command and if that command exist. 
	 * @param event the event containing the message
	 */
	public static void parseEvent(MessageReceivedEvent event)
	{	
		// Ignores bot messages so it doesn't reply to itself or other bots and checks if message starts with command prefix.
		if (!event.getMessage().getAuthor().isBot() && event.getMessage().getContentRaw().startsWith(Epic.settings.getCommandPrefix()))
		{
			// Creates an array of the message split up by spaces and removes the prefix.
			String[] m = event.getMessage().getContentRaw().substring(1).split(" ");
			
			// Checks if the first index of the message matches any of the commands.
			for (int i = 0; i < Command.COMMANDS.length; i++)
			{
				Command command = checkCommands(m[0]);
				if (command != null)
				{
					if (!(event.isFromType((ChannelType.TEXT))) && command.GuildOnly())
					{
						// If the message is from a DM or group chat and the command can only be used in a server...
						// the automated message will be sent.
						event.getChannel().sendMessage("This command can only be used in a server!").queue();
						// Ends the method once the message has been sent.
						return;
					}
					else
					{
						// If the first index of the message does match a command, the event will be sent to the command for parsing.
						command.execute(event);
						// Ends the method once execute is called.
						return;
					}
				}
			}
			/*
			 * DISABLED THIS FEATURE DUE TO FEEDBACK. UNCOMMENT TO RE-ENABLE.
			// If the first index of the message does not match any of the commands, the automated message will be sent.
			event.getChannel().sendMessage("That isn't a known command! Try `" + (Epic.settings.getCommandPrefix()) + "help` to see a list of commands.").queue();
			*/
		}
		// If the message does not start with the command prefix it is simply forgotten.
	}
}
