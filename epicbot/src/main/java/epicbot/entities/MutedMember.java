package epicbot.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import epicbot.settings.SettingsManager;
import epicbot.util.Logger;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;

/**
 * @author Kyle Minter (Kale Monkey)
 */
public class MutedMember
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
					// Unmutes the user.
					mutedMember.getGuild().getController().removeRolesFromMember(mutedMember, SettingsManager.getInstance().getSettings().getMuteRole(mutedMember.getGuild())).queue();
					// If the user getting unmuted is not a bot they will be sent a message telling them they got unmuted.
					if (!(mutedMember.getUser().isBot()))
					{
						mutedMember.getUser().openPrivateChannel().queue((channel) ->
						{
							channel.sendMessage("You have been unmuted in the \"" + mutedMember.getGuild().getName() + "\" discord server after " + time + " minute(s) because your mute time has expired.").queue();
						});
					}
					// Logs the unmute.
					Logger.logMuteExpiration(mutedMember.getGuild(), mutedMember, time);
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
		if (i > -1)
		{
			if (mutedMembers.get(i).getTimer() != null)
			{
				mutedMembers.get(i).getTimer().cancel();
			}
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
		Role mute = SettingsManager.getInstance().getSettings().getMuteRole(g);
		
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