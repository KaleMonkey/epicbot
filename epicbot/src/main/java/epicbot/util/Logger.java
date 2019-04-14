package epicbot.util;

import java.awt.Color;

import com.jagrosh.jdautilities.command.CommandEvent;

import epicbot.Epic;
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
	 * @param arguments the arguments obtained during the command
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
		eb.setAuthor("Epic Gamer Bot", "https://github.com/KaleMonkey/epicbot");
		eb.addField("Mute", event.getAuthor().getName() + " muted " + memberToMute.getEffectiveName() + t + " because \"" + muteReason + ".\"", false);
		Epic.settings.getLogChannel(event.getGuild()).sendMessage(eb.build()).queue();
	}
	
	/**
	 * Logs the unmute in the specified log channel.
	 * @param event the event parsed during the command
	 * @param arguments the arguments obtained during the command
	 */
	public static void logUnmute(CommandEvent event, Member memberToUnmute)
	{
		// Logs the unmute with the correct information.
		EmbedBuilder eb = new EmbedBuilder();
		eb.setColor(Color.GREEN);
		eb.setAuthor("Epic Gamer Bot", "https://github.com/KaleMonkey/epicbot");
		eb.addField("Unmute", event.getAuthor().getName() + " unmuted " + memberToUnmute.getEffectiveName() + ".", false);
		Epic.settings.getLogChannel(event.getGuild()).sendMessage(eb.build()).queue();
	}
	
	/**
	 * Logs the kick in the specified log channel.
	 */
	public static void logKick(CommandEvent event, Member memberToKick, String kickReason)
	{
		// Logs the kick with the correct information.
		EmbedBuilder eb = new EmbedBuilder();
		eb.setColor(Color.RED);
		eb.setAuthor("Epic Gamer Bot", "https://github.com/KaleMonkey/epicbot");
		eb.addField("Kick", event.getAuthor().getName() + " kicked " + memberToKick.getEffectiveName() + " because \"" + kickReason + "\".", false);
		Epic.settings.getLogChannel(event.getGuild()).sendMessage(eb.build()).queue();
	}
	
	/**
	 * Logs the ban in the specified log channel.
	 */
	public static void logBan(CommandEvent event, Member memberToBan, String banReason)
	{
		// Logs the ban with the correct information.
		EmbedBuilder eb = new EmbedBuilder();
		eb.setColor(Color.RED);
		eb.setAuthor("Epic Gamer Bot", "https://github.com/KaleMonkey/epicbot");
		eb.addField("Ban", event.getAuthor().getName() + " banned " + memberToBan.getEffectiveName() + " because \"" + banReason + ".\"", false);
		Epic.settings.getLogChannel(event.getGuild()).sendMessage(eb.build()).queue();
	}
}
