package epicbot.commands.moderation;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import epicbot.Epic;
import epicbot.commands.Command;
import epicbot.util.AutoMod;
import epicbot.util.CommandHandler;
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
	private static final String commandUsage = "`" + Epic.settings.getCommandPrefix() + "mute <User> <Time> <Reason>`\nUser must be an @" +
			" (example: @Epic Gamer Bot#6375).\nTime must be a whole number greater than 0.\nReason is not required, but is recomended.";
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
			Member memberToMute = getMemberToMute(event);
			int muteTime = getMuteTime(event);
			String muteReason = getMuteReason(event);
			
			// Checks if the user getting muted has an OPed role.
			if (CommandHandler.checkPerms(event.getGuild(), memberToMute.getRoles()))
			{
				// If the user has an OPed role an automated message will be sent.
				event.getChannel().sendMessage("Now, now. You mods play nice.").queue();
				return;
			}
			
			if (MutedMember.isMuted(event.getGuild(), memberToMute.getRoles()))
			{
				// If the user is already muted the automated message will be sent.
				event.getChannel().sendMessage("Relax man. This person is already muted!").queue();
				return;
			}
			
			// Mutes the user.
			event.getGuild().getController().addRolesToMember(memberToMute, CommandHandler.getMuteRole(event.getGuild())).queue();
			if (muteTime > 0)
			{
				// Sends message confirming that the mute worked.
				event.getChannel().sendMessage("Muted " + memberToMute.getEffectiveName() + " for " + muteTime + " minutes.").queue();
				
				// If the user getting muted is not a bot they will be sent a message telling them they got muted.
				if (!(memberToMute.getUser().isBot()))
				{
					memberToMute.getUser().openPrivateChannel().queue((channel) ->
					{
						channel.sendMessage("You have been muted for " + muteTime + " minutes because \"" + muteReason + "\".").queue();
					});
				}
				
				// Sets up a timer to unmute the user after the given time.
				MutedMember.addMutedMember(new MutedMember(memberToMute, muteTime));
				
			}
			else
			{
				// Sends message confirming that the mute worked.
				event.getChannel().sendMessage("Muted " + memberToMute.getEffectiveName() + ".").queue();
				
				// If the user getting muted is not a bot they will be sent a message telling them they got muted.
				if (!(memberToMute.getUser().isBot()))
				{
					memberToMute.getUser().openPrivateChannel().queue((channel) ->
					{
						channel.sendMessage("You have been muted because \"" + muteReason + "\".").queue();
					});
				}
			}
			
			// Logs the mute.
			AutoMod.logMute(event, memberToMute, muteTime, muteReason);
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
	 * Returns the member to mute from a message received event.
	 * @param event the message received event
	 * @return the member to mute
	 * @throws IllegalArgumentException
	 */
	private static Member getMemberToMute(MessageReceivedEvent event) throws IllegalArgumentException
	{
		// Creates a member variable and gives it a default value of null.
		Member member = null;
		
		// Checks if the message mentions a member.
		List<Member> mentionedMembers = event.getMessage().getMentionedMembers(event.getGuild());
		if (mentionedMembers.size() == 1)
		{
			// If there is a mentioned member, member will become that member.
			member = mentionedMembers.get(0);
		}
		
		// If member is still at it's default value an exception will be thrown.
		if (member == null)
		{
			throw new IllegalArgumentException("Argument Invalid!");
		}
		else
		{
			return member;
		}
	}
	
	/**
	 * Returns the mute time from a message received event
	 * @param event the message received event
	 * @return the mute time
	 * @throws IllegalArgumentException
	 */
	private static int getMuteTime(MessageReceivedEvent event) throws IllegalArgumentException
	{
		// Creates a time variable and gives it a default value of -1.
		int time = -1;
		
		// Checks if the message has an integer in the place for time.
		if (event.getMessage().getContentRaw().split(" ").length >= 3)
		{
			if (isInt(event.getMessage().getContentRaw().split(" ")[2]))
			{
				int num = Integer.parseInt(event.getMessage().getContentRaw().split(" ")[2]);
				if (num >= 0)
				{
					// If there is an integer in the place of time, time will become that number.
					time = num;
				}
			}
		}
		
		// If time is still at it's default value an exception will be thrown.
		if (time == -1)
		{
			throw new IllegalArgumentException("Argument Invalid!");
		}
		else
		{
			return time;
		}
	}
	
	/**
	 * Returns the mute reason from a message received event.
	 * @param event the message received event
	 * @return the reason for the mute
	 */
	private static String getMuteReason(MessageReceivedEvent event)
	{
		// Creates a reason variable and gives it a default value of "No reason provided".
		String reason = "*No reason provided*";
		
		// Checks if the message has a reason.
		String[] message = event.getMessage().getContentRaw().split(" ");
		if (message.length >= 4)
		{
			reason = "";
			for (int i = 3; i < message.length; i++)
			{
				reason += (" " + message[i]);
			}
			// If there is a reason provided, reason will equal it.
			reason = reason.substring(1);
		}
		
		return reason;
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
}

/**
 * @author Kyle Minter (Kale Monkey)
 */
class MutedMember
{
	private static List<MutedMember> mutedMembers = new ArrayList<MutedMember>();
	private Member mutedMember;
	private Timer timer;
	
	/**
	 * Constructs a MutedUser object with a given member and time to be muted.
	 * @param member member that is muted
	 * @param time how long the member will be muted for
	 */
	public MutedMember(Member member, int time)
	{
		mutedMember = member;
		timer = new Timer();
		
		timer.schedule(new TimerTask()
		{
			public void run()
			{
				// If the user is still muted they will be unmuted.
				if (isMuted(mutedMember.getGuild(), mutedMember.getRoles()))
				{
					mutedMember.getGuild().getController().removeRolesFromMember(mutedMember, CommandHandler.getMuteRole(mutedMember.getGuild())).queue();
				}
			}
		}, time * 60000);
	}
	
	/**
	 * Constructs a MutedUser object with a given member.
	 * @param member member that is muted
	 */
	public MutedMember(Member member)
	{
		mutedMember = member;
	}
	
	/**
	 * Adds a MutedMember to a list of MutedMember objects.
	 * @param newMutedMember the MutedMember to add
	 */
	public static void addMutedMember(MutedMember newMutedMember)
	{
		mutedMembers.add(newMutedMember);
	}
	
	/**
	 * Removes a MutedMember from a list of MutedMember objects.
	 * @param mutedMember the MutedMember to remove
	 */
	public static void removeMutedMember(MutedMember mutedMember)
	{
		int i = mutedMembers.indexOf(mutedMember);
		if (i != -1)
		{
			mutedMembers.get(mutedMembers.indexOf(mutedMember)).getTimer().cancel();;
			mutedMembers.remove(i);
		}
	}
	
	/**
	 * Checks to see if the user is muted or not.
	 * @param g the guild the message was sent in
	 * @param r the user roles
	 * @return true if the user is muted, false if they are not
	 */
	public static boolean isMuted(Guild g, List<Role> r)
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
	 * Returns the timer of the MutedMember.
	 * @return the timer
	 */
	public Timer getTimer()
	{
		return timer;
	}
	
	/**
	 * Returns the member of the MutedMember.
	 * @return the member
	 */
	public Member getMember()
	{
		return mutedMember;
	}
	
	/**
	 * Returns true if the two MutedMember objects have the same member, false if they do not.
	 * @param other the object to compare to
	 * @return true if members are equal, false if they don't
	 */
	public boolean equals(Object other)
	{
		MutedMember o = (MutedMember)other;
		return mutedMember.equals(o.getMember());
	}
}