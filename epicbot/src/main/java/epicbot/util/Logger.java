package epicbot.util;

import java.awt.Color;

import com.jagrosh.jdautilities.command.CommandEvent;

import epicbot.settings.SettingsManager;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.guild.member.GuildMemberRoleAddEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberRoleRemoveEvent;

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
		if (muteTime > 0)
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
	 * Logs a mute in the specified log channel.
	 * @param event the event parsed during the command
	 * @param mod the mod who muted the memberToMute
	 * @param memberToMute the member that got muted
	 * @param muteTime how long the member is to be muted
	 * @param muteReason the reason the member was muted
	 */
	public static void logMute(GuildMemberRoleAddEvent event, User mod, Member memberToMute, int muteTime, String muteReason)
	{
		// Logs the mute with the correct information.
		String t = "";
		if (muteTime > 0)
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
		eb.addField("Mute", mod.toString() + " muted " + memberToMute.getEffectiveName() + t + " because \"" + muteReason + ".\"", false);
		SettingsManager.getInstance().getSettings().getLogChannel(event.getGuild()).sendMessage(eb.build()).queue();
	}
	
	/**
	 * Logs a mute expiration in the specified log channel.
	 * @param guild the guild the member was muted in
	 * @param mutedMember the member whose mute expired
	 * @param time the amount of time the member was muted
	 */
	public static void logMuteExpiration(Guild guild, Member mutedMember, int time)
	{
		EmbedBuilder eb = new EmbedBuilder();
		eb.setColor(Color.GREEN);
		eb.setAuthor("Epic", "https://github.com/KaleMonkey/epicbot");
		eb.addField("Mute Expiration", mutedMember.getEffectiveName() + " has been unmuted after " + time + " minute(s) because their mute time has expired.", false);
		SettingsManager.getInstance().getSettings().getLogChannel(guild).sendMessage(eb.build()).queue();
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
	 * Logs the unmute in the specified log channel.
	 * @param event the event parsed during the command
	 * @param mod the mod who unmuted the memberToUnmute
	 * @param memberToUnmute the member that got unmuted
	 */
	public static void logUnmute(GuildMemberRoleRemoveEvent event, User mod, Member memberToUnmute)
	{
		// Logs the unmute with the correct information.
		EmbedBuilder eb = new EmbedBuilder();
		eb.setColor(Color.GREEN);
		eb.setAuthor("Epic", "https://github.com/KaleMonkey/epicbot");
		eb.addField("Unmute", mod.getName() + " unmuted " + memberToUnmute.getEffectiveName() + ".", false);
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
