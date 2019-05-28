package epicbot.util;

import java.awt.Color;

import com.jagrosh.jdautilities.command.CommandEvent;

import epicbot.settings.SettingsManager;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;

/**
 * @author Kyle Minter (Kale Monkey)
 */
public class Logger
{	
	/**
	 * Logs a mute in the specified log channel.
	 * @param event the event parsed during the command
	 * @param memberToMute the member that got muted
	 * @param muteTime how long the member is to be muted
	 * @param muteReason the reason the member was muted
	 */
	public static void logMute(CommandEvent event, Member memberToMute, int muteTime, String muteReason)
	{
		// Logs the mute with the correct information.
		String t = "";
		if (muteTime> 0)
		{
			t = " for " + muteTime + " minutes";
		}
		else
		{
			t = " indefinatly";
		}
		EmbedBuilder eb = new EmbedBuilder();
		eb.setColor(Color.RED);
		eb.setAuthor("Epic", "https://github.com/KaleMonkey/epicbot");
		eb.addField("Mute", event.getAuthor().getName() + " muted " + memberToMute.getEffectiveName() + t + " because \"" + muteReason + ".\"", false);
		SettingsManager.getInstance().getSettings().getLogChannel(event.getGuild()).sendMessage(eb.build()).queue();
	}
	
	/**
	 * Logs the unmute in the specified log channel.
	 * @param event the event parsed during the command
	 * @param memberToUnmute the member that got unmuted
	 */
	public static void logUnmute(CommandEvent event, Member memberToUnmute)
	{
		// Logs the unmute with the correct information.
		EmbedBuilder eb = new EmbedBuilder();
		eb.setColor(Color.GREEN);
		eb.setAuthor("Epic", "https://github.com/KaleMonkey/epicbot");
		eb.addField("Unmute", event.getAuthor().getName() + " unmuted " + memberToUnmute.getEffectiveName() + ".", false);
		SettingsManager.getInstance().getSettings().getLogChannel(event.getGuild()).sendMessage(eb.build()).queue();
	}
	
	/**
	 * Logs the kick in the specified log channel.
	 * @param event the event parsed during the command
	 * @param memberToKick the member that got kicked
	 * @param kickReason the reason the member was kicked
	 */
	public static void logKick(CommandEvent event, Member memberToKick, String kickReason)
	{
		// Logs the kick with the correct information.
		EmbedBuilder eb = new EmbedBuilder();
		eb.setColor(Color.RED);
		eb.setAuthor("Epic", "https://github.com/KaleMonkey/epicbot");
		eb.addField("Kick", event.getAuthor().getName() + " kicked " + memberToKick.getEffectiveName() + " because \"" + kickReason + "\".", false);
		SettingsManager.getInstance().getSettings().getLogChannel(event.getGuild()).sendMessage(eb.build()).queue();
	}
	
	/**
	 * Logs the ban in the specified log channel.
	 * @param event the event parsed during the command
	 * @param memberToBan the member that got banned
	 * @param banReason the reason the member got banned
	 */
	public static void logBan(CommandEvent event, Member memberToBan, String banReason)
	{
		// Logs the ban with the correct information.
		EmbedBuilder eb = new EmbedBuilder();
		eb.setColor(Color.RED);
		eb.setAuthor("Epic", "https://github.com/KaleMonkey/epicbot");
		eb.addField("Ban", event.getAuthor().getName() + " banned " + memberToBan.getEffectiveName() + " because \"" + banReason + ".\"", false);
		SettingsManager.getInstance().getSettings().getLogChannel(event.getGuild()).sendMessage(eb.build()).queue();
	}
}
