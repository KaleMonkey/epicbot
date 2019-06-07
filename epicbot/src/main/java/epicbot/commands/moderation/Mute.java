package epicbot.commands.moderation;

import java.util.List;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import epicbot.commands.general.Help;
import epicbot.entities.MutedMember;
import epicbot.settings.SettingsManager;
import epicbot.util.Logger;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Member;

/**
 * @author Kyle Minter (Kale Monkey)
 */
public class Mute extends Command
{	
	public Mute()
	{
		this.name = "mute";
		this.help = "Mutes the specified user for a given amount of time. If the given time is 0," +
				" the mute will be indefinite or until the user gets unmuted.";
		this.arguments = "<@user> [time] [reason (optional)]";
		this.category = new Category("Moderation");
		this.guildOnly = true;
		this.requiredRole = SettingsManager.getInstance().getSettings().getOpedRole();
		this.botPermissions = new Permission[] {Permission.MESSAGE_WRITE, Permission.MANAGE_ROLES};
	}
	
	public void execute(CommandEvent event)
	{
		try
		{
			if (event.getArgs().split(" ").length < 2)
			{
				event.reply("You did not provide the necessary arguments for this command!" + Help.getHelp(this.name));
				return;
			}
			
			// Gets the arguments for the command.
			Member memberToMute = getMemberToMute(event);
			int muteTime = getMuteTime(event);
			String muteReason = getMuteReason(event);
			
			// Checks if the user getting muted has an OPed role.
			if (SettingsManager.getInstance().getSettings().checkPerms(event.getGuild(), memberToMute.getRoles()))
			{
				// If the user has an OPed role an automated message will be sent.
				event.reply("You cannot mute other mods!");
				return;
			}
			
			if (MutedMember.isMuted(event.getGuild(), memberToMute.getRoles()))
			{
				// If the user is already muted the automated message will be sent.
				event.reply("This person is already muted!");
				return;
			}
			
			// Mutes the user.
			event.getGuild().getController().addRolesToMember(memberToMute, SettingsManager.getInstance().getSettings().getMuteRole(event.getGuild())).queue();
			if (muteTime > 0)
			{
				// Sends message confirming that the mute worked.
				event.reply("Muted " + memberToMute.getEffectiveName() + " for " + muteTime + " minutes.");
				
				// Sets up a timer to unmute the user after the given time.
				MutedMember.addMutedMember(new MutedMember(memberToMute, muteTime));
				
			}
			else
			{
				// Sends message confirming that the mute worked.
				event.reply("Muted " + memberToMute.getEffectiveName() + ".");
				
				// Adds the member to the MutedMember list.
				MutedMember.addMutedMember(new MutedMember(memberToMute));
			}
			
			// Logs the mute.
			Logger.logMute(event, memberToMute, muteTime, muteReason);
		}
		catch (IllegalArgumentException e)
		{
			return;
		}
	}
	
	/*
	 * Returns the member to mute from a message received event.
	 * @param event the message received event
	 * @return the member to mute
	 * @throws IllegalArgumentException
	 */
	private Member getMemberToMute(CommandEvent event) throws IllegalArgumentException
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
			event.reply("You did not provide a user to mute!" + Help.getHelp(this.name));
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
	private int getMuteTime(CommandEvent event) throws IllegalArgumentException
	{
		// Creates a time variable and gives it a default value of -1.
		int time = -1;
		
		// Checks if the message has an integer in the place for time.
		if (event.getArgs().split(" ").length >= 2)
		{
			if (isInt(event.getArgs().split(" ")[1]))
			{
				int num = Integer.parseInt(event.getArgs().split(" ")[1]);
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
			event.reply("You did not provide a time for the mute!" + Help.getHelp(this.name));
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
	private static String getMuteReason(CommandEvent event)
	{
		// Creates a reason variable and gives it a default value of "No reason provided".
		String reason = "*No reason provided*";
		
		// Checks if the message has a reason.
		String[] args = event.getArgs().split(" ");
		if (args.length >= 3)
		{
			reason = "";
			for (int i = 2; i < args.length; i++)
			{
				reason += (" " + args[i]);
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