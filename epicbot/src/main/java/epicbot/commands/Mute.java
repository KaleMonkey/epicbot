package epicbot.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import epicbot.Epic;
import epicbot.util.AutoMod;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.exceptions.HierarchyException;

/**
 * @author Kyle Minter (Kale Monkey)
 */
public class Mute implements Command
{
	private static final String commandName = "Mute";
	private static final String commandDescription = "Mutes the specified user for a given amount of time. If the given time is 0," +
			" the mute will last forever or until the user gets unmuted. You must have an OPed role to use this command.";
	private static final String commandUsage = "`" + Epic.settings.getCommandPrefix() + "mute <User> <Time>`\nUser must be an @" +
			" (example: @Epic Gamer Bot#6375).\nTime must be a whole number greater than 0.";
	private static final boolean commandGuildOnly = true;
	
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
			// Checks if the user calling the command has the required role for this command.
			if (!CommandHandler.checkPerms(event.getGuild(), event.getMember().getRoles()))
			{
				// If the user does not have the required role for this command the automated response will be sent.
				event.getChannel().sendMessage("You do not have the required permissions to use this command!").queue();
				return;
			}
			
			// Gets the arguments for the command.
			List<Object> arguments = getArguments(event);
			
			// Checks if the user getting muted has an OPed role.
			if (CommandHandler.checkPerms(event.getGuild(), ((Member)arguments.get(0)).getRoles()))
			{
				// If the user has an OPed role an automated message will be sent.
				event.getChannel().sendMessage("Now, now. You mods play nice.").queue();
				return;
			}
			
			if (isMuted(event.getGuild(), ((Member)arguments.get(0)).getRoles()))
			{
				// If the user is already muted the automated message will be sent.
				event.getChannel().sendMessage("Relax man. This person is already muted!").queue();
				return;
			}
			
			// Mutes the user.
			event.getGuild().getController().addRolesToMember((Member)arguments.get(0), CommandHandler.getMuteRole(event.getGuild())).queue();
			if (((int)arguments.get(1)) > 0)
			{
				// Sends message confirming that the mute worked.
				event.getChannel().sendMessage("Muted " + ((Member)arguments.get(0)).getEffectiveName() + " for " + ((int)arguments.get(1) + " minutes.")).queue();
				
				// If the user getting muted is not a bot they will be sent a message telling them they got muted.
				if (!(((Member)(arguments.get(0))).getUser().isBot()))
				{
					((Member)arguments.get(0)).getUser().openPrivateChannel().queue((channel) ->
					{
						channel.sendMessage("You have been muted for " + ((int)arguments.get(1)) + " minutes because \"" +
							((String)arguments.get(2)) + "\".").queue();
					});
				}
				
				// Sets up a timer to unmute the user after the given time.
				scheduleUnmute((Member)(arguments.get(0)), (int)(arguments.get(1)));
				
			}
			else
			{
				// Sends message confirming that the mute worked.
				event.getChannel().sendMessage("Muted " + ((Member)arguments.get(0)).getEffectiveName() + ".").queue();
				
				// If the user getting muted is not a bot they will be sent a message telling them they got muted.
				if (!(((Member)(arguments.get(0))).getUser().isBot()))
				{
					((Member)arguments.get(0)).getUser().openPrivateChannel().queue((channel) ->
					{
						channel.sendMessage("You have been muted because \"" + ((String)(arguments.get(2)) + "\".")).queue();
					});
				}
			}
			
			// Logs the mute.
			AutoMod.logMute(event, arguments);
		}
		catch (IllegalArgumentException e)
		{
			// If the arguments are invalid the automated message will be sent.
			event.getChannel().sendMessage("You provided illegal arguments! Try `" +
					Epic.settings.getCommandPrefix() + "help mute` to get help with this command.").queue();
		}
		catch (HierarchyException e)
		{
			System.out.println("\n[Error]: The bot role is lower in the role hierarchy than the muted role!");
			System.out.println("[Error]: Please raise the bot's role in the role hierarchy.\n");
		}
	}
	
	/**
	 * Checks to see if the message contains the necessary arguments for the command.
	 * @param event the event containing the message
	 */
	private static List<Object> getArguments(MessageReceivedEvent event) throws IllegalArgumentException
	{
		// Creates the variables to store the possible arguments for this command.
		Member user = null;
		int time = -1;
		String reason = "*No reason provided.*";
		
		// Creates an array of the message, split up by spaces, starting from the first argument.
		String[] message;
		if (event.getMessage().getContentRaw().substring(1).split(" ").length > 1)
		{
			message = event.getMessage().getContentRaw().substring(6).split(" ");
		}
		else
		{
			throw new IllegalArgumentException("Argument Invalid!");
		}
		
		// Gets the first argument.
		{
			List<Member> mentioned = event.getMessage().getMentionedMembers(event.getGuild());
			if (mentioned.size() > 0)
			{
				user = mentioned.get(0);
			}
		}
		
		// Gets the second argument.
		{
			if (message.length > 1)
			{
				if (isInt(message[1]))
				{
					int num = Integer.parseInt(message[1]);
					if (num >= 0)
					{
						time = num;
					}
				}
			}
		}
		
		
		// Gets the third argument.
		{
			if (message.length > 2)
			{
				
				reason = "";
				for (int i = 2; i < message.length; i++)
				{
					reason += (" " + message[i]);
				}
				reason = reason.substring(1);
			}
		}
		
		// If any of the required arguments are still at their default value an exception will be thrown.
		if (user == null || time == -1)
		{
			throw new IllegalArgumentException("Argument Invalid!");
		}
		
		// Creates a list of the arguments and returns it.
		List<Object> arguments = new ArrayList<Object>();
		arguments.add(user);
		arguments.add(time);
		arguments.add(reason);
		return arguments;
	}
	
	/**
	 * Checks to see if the provided string contains only integers.
	 * @param str the string to be checked.
	 * @return true if the string only contains integers, false if it doesn't
	 */
	private static boolean isInt(String str)
	{
		try
		{
			Integer.parseInt(str);
			return true;
		}
		catch (NumberFormatException e)
		{
			return false;
		}
	}
	
	/**
	 * Checks to see if the user is muted or not.
	 * @param g the guild the message was sent in
	 * @param r the user roles
	 * @return true if the user is muted, false if they are not
	 */
	private boolean isMuted(Guild g, List<Role> r)
	{
		// Gets the mute role for the guild.
		Role mute = CommandHandler.getMuteRole(g);
		
		// Checks to see if the provided user roles matches the mute role in the guild.
		for (Role userRole : r)
		{
			if (userRole.equals(mute))
			{
				// If a user role matches the mute role, true will be returned.
				return true;
			}
		}
		// If none of the user roles matches the mute role in the guild, false will be returned.
		return false;
	}
	
	/**
	 * Unmutes the given user after a given time.
	 * @param user the member to be unmuted
	 * @param time the amount of time in minutes to wait before unmuting the user
	 */
	private void scheduleUnmute(Member user, int time)
	{
		// Creates a timer object and gives it a task.
		Timer timer = new Timer();
		timer.schedule(new TimerTask()
			{
				public void run()
				{
					// If the user is still muted they will be unmuted.
					if (isMuted(user.getGuild(), user.getRoles()))
					{
						user.getGuild().getController().removeRolesFromMember(user, CommandHandler.getMuteRole(user.getGuild())).queue();
					}
				}
			}, time * 60000);
	}
}
