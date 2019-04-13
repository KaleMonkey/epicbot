package epicbot.util;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import epicbot.Epic;
import epicbot.commands.Command;
import net.dv8tion.jda.core.Permission;
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
	 * Returns the oped role for the given server.
	 * @param g the guild to get the role from
	 * @return the oped role
	 */
	public static Role getOpedRole(Guild g)
	{
		List<Role> r = g.getRolesByName(Epic.settings.getOpedRole(), true);
		// Checks if the server has a oped role with the same name as the one provided in the config.json.
		if (r.size() > 0)
		{
			// If the role does exist in the server it will be returned.
			return r.get(0);
		}
		// If the role does not exist in the server it will be made.
		List<Permission> perms = new ArrayList<Permission>(Arrays.asList(Permission.BAN_MEMBERS, Permission.KICK_MEMBERS,
					Permission.MESSAGE_MENTION_EVERYONE, Permission.MESSAGE_MANAGE, Permission.VIEW_AUDIT_LOGS,
					Permission.VOICE_DEAF_OTHERS, Permission.VOICE_MOVE_OTHERS, Permission.VOICE_MUTE_OTHERS));
		g.getController().createRole().setName(Epic.settings.getOpedRole()).setColor(new Color(52, 152, 219)).setMentionable(true).setHoisted(true).setPermissions(perms).queue();
		// Because it takes a hot second for JDA to create the role we must wait.
		while (r.size() < 1)
		{
			r = g.getRolesByName(Epic.settings.getOpedRole(), true);
		}
		// Once the role is finally created we will return it.
		return r.get(0);
	}
	
	/**
	 * Returns the mute role for the given server.
	 * @param g the guild to get the role from
	 * @return the mute role
	 */
	public static Role getMuteRole(Guild g)
	{
		List<Role> r = g.getRolesByName(Epic.settings.getMuteRole(), true);
		// Checks if the server has a mute role with the same name as the one provided in the config.json.
		if (r.size() > 0)
		{
			// If the role does exist in the server it will be returned.
			return r.get(0);
		}
		// If the role does not exist in the server it will be made.
		g.getController().createRole().setName(Epic.settings.getMuteRole()).setColor(Color.RED).setMentionable(false).setHoisted(true).setPermissions(new ArrayList<Permission>()).queue();
		// Because it takes a hot second for JDA to create the role we must wait.
		while (r.size() < 1)
		{
			r = g.getRolesByName(Epic.settings.getMuteRole(), true);
		}
		// Once the role is finally created we will return it.
		return r.get(0);
	}
	
	/**
	 * Returns the NSFW role for the given server.
	 * @param g the guild to get the role from
	 * @return if the role exists in the server the NSFW role will be returned, if it doesn't null will be returned instead
	 */
	public static Role getNsfwRole(Guild g)
	{
		List<Role> r = g.getRolesByName(Epic.settings.getNsfwRole(), true);
		// Checks if the server has a mute role with the same name as the one provided in the config.json.
		if (r.size() > 0)
		{
			// If the role does exist in the server it will be returned.
			return r.get(0);
		}
		// If the role does not exist in the server null will be returned.
		return null;
	}
	
	/**
	 * Returns the log channel for the given server.
	 * @param g the guild to get the channel from
	 * @return the log channel
	 */
	public static TextChannel getLogChannel(Guild g)
	{
		List<TextChannel> c = g.getTextChannelsByName(Epic.settings.getLogChannelName(), true);
		// Checks if the server has a log channel with the same name as the provided in the config.json.
		if (c.size() > 0)
		{
			// If the channel does exist in the server it will be returned.
			return c.get(0);
		}
		// If the channel does not exist in the server it will be made.
		List<Permission> opedAllow = new ArrayList<Permission>(Arrays.asList(Permission.MESSAGE_READ));
		List<Permission> opedDisallow = new ArrayList<Permission>(Arrays.asList(Permission.MESSAGE_WRITE, Permission.MESSAGE_MANAGE));
		List<Permission> everyone = new ArrayList<Permission>(Arrays.asList(Permission.VIEW_CHANNEL, Permission.MESSAGE_WRITE, Permission.MESSAGE_READ));
		g.getController().createTextChannel(Epic.settings.getLogChannelName()).addPermissionOverride(getOpedRole(g), opedAllow, opedDisallow).addPermissionOverride(g.getPublicRole(), new ArrayList<Permission>(), everyone).queue();
		// Because it takes a hot second for JDA to create the channel we must wait.
		while (c.size() < 1)
		{
			c = g.getTextChannelsByName(Epic.settings.getLogChannelName(), true);
		}
		// Once the channel is finally created we will return it.
		return c.get(0);
	}
	
	/**
	 * Checks if the provided array list of user roles matches any of the guild roles that are OPed.
	 * @param g The guild in which we are checking if the user has the role
	 * @param r an array list of user roles
	 * @return true if a any of the user roles matches any of the OPed roles in the guild, false if they don't
	 */
	public static boolean checkPerms(Guild g, List<Role> r)
	{	
		for (Role userRole : r)
		{
			if (userRole.equals(getOpedRole(g)))
			{
				// If a user role matches the OPed role, true will be returned.
				return true;
			}
		}
		// If none of the user roles matches the OPed role, false will be returned.
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
		for (Command[] ct : Command.COMMANDS)
		{
			for (Command c : ct)
			{
				if (c.getName().equalsIgnoreCase(command))
				{
					// If the string matches a command the command object will be returned.
					return c;
				}
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
