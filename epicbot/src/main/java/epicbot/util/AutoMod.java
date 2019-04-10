package epicbot.util;

import java.awt.Color;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberLeaveEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 * @author Kyle Minter (Kale Monkey)
 */
public class AutoMod
{
	/**
	 * Sends a welcome message to a user when they join a discord server.
	 * @param event the member join event
	 */
	public static void sendWelcomeMessage(GuildMemberJoinEvent event)
	{
		if (!(event.getUser().isBot()))
		{
			// Opens a private channel with the user and sends the welcome message.
			event.getUser().openPrivateChannel().queue((channel) ->
			{
				channel.sendMessage("Welcome to the " + event.getGuild().getName() + " discord server!").queue();
			});
		}
	}
	
	/**
	 * Sends a leaving message to a user when they leave a discord server.
	 * @param event the member leave event
	 */
	public static void sendLeaveMessage(GuildMemberLeaveEvent event)
	{
		if (!(event.getUser().isBot()))
		{
			// Opens a private channel with the user and sends the leave message.
			event.getUser().openPrivateChannel().queue((channel) ->
			{
				channel.sendMessage("Sorry to see you leave " + event.getGuild().getName() + "!").queue();
			});
		}
	}
	
	/**
	 * Logs a mute in the specified log channel.
	 * @param event the event parsed during the command
	 * @param arguments the arguments obtained during the command
	 */
	public static void logMute(MessageReceivedEvent event, Member memberToMute, int muteTime, String muteReason)
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
		CommandHandler.getLogChannel(event.getGuild()).sendMessage(eb.build()).queue();
	}
	
	/**
	 * Logs the unmute in the specified log channel.
	 * @param event the event parsed during the command
	 * @param arguments the arguments obtained during the command
	 */
	public static void logUnmute(MessageReceivedEvent event, Member memberToUnmute)
	{
		// Logs the unmute with the correct information.
		EmbedBuilder eb = new EmbedBuilder();
		eb.setColor(Color.GREEN);
		eb.setAuthor("Epic Gamer Bot", "https://github.com/KaleMonkey/epicbot");
		eb.addField("Unmute", event.getAuthor().getName() + " unmuted " + memberToUnmute.getEffectiveName() + ".", false);
		CommandHandler.getLogChannel(event.getGuild()).sendMessage(eb.build()).queue();
	}
	
	/**
	 * Logs the kick in the specified log channel.
	 */
	public static void logKick(MessageReceivedEvent event, Member memberToKick, String kickReason)
	{
		// Logs the kick with the correct information.
		EmbedBuilder eb = new EmbedBuilder();
		eb.setColor(Color.RED);
		eb.setAuthor("Epic Gamer Bot", "https://github.com/KaleMonkey/epicbot");
		eb.addField("Kick", event.getAuthor().getName() + " kicked " + memberToKick.getEffectiveName() + " because \"" + kickReason + "\".", false);
		CommandHandler.getLogChannel(event.getGuild()).sendMessage(eb.build()).queue();
	}
	
	/**
	 * Logs the ban in the specified log channel.
	 */
	public static void logBan(MessageReceivedEvent event, Member memberToBan, String banReason)
	{
		// Logs the ban with the correct information.
		EmbedBuilder eb = new EmbedBuilder();
		eb.setColor(Color.RED);
		eb.setAuthor("Epic Gamer Bot", "https://github.com/KaleMonkey/epicbot");
		eb.addField("Ban", event.getAuthor().getName() + " banned " + memberToBan.getEffectiveName() + " because \"" + banReason + ".\"", false);
		CommandHandler.getLogChannel(event.getGuild()).sendMessage(eb.build()).queue();
	}
	
	/**
	 * Logs the unban in the specified log channel.
	 */
	public static void logUnban(MessageReceivedEvent event, User userToUnban)
	{
		// Logs the unban with the correct information.
		EmbedBuilder eb = new EmbedBuilder();
		eb.setColor(Color.GREEN);
		eb.setAuthor("Epic Gamer Bot", "https://github.com/KaleMonkey/epicbot");
		eb.addField("Unban", event.getAuthor().getName() + " unbanned " + userToUnban.getName() + ".", false);
		CommandHandler.getLogChannel(event.getGuild()).sendMessage(eb.build()).queue();
	}
}
