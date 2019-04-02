package epicbot.util;

import java.util.List;

import epicbot.commands.CommandHandler;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberLeaveEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class AutoMod
{
	public static void sendWelcomeMessage(GuildMemberJoinEvent event)
	{
		// Opens a private channel with the user and sends the welcome message.
		event.getUser().openPrivateChannel().queue((channel) ->
		{
			channel.sendMessage("Welcome to the " + event.getGuild().getName() + " discord server!").queue();
		});
	}
	
	public static void sendLeaveMessage(GuildMemberLeaveEvent event)
	{
		// Opens a private channel with the user and sends the leave message.
		event.getUser().openPrivateChannel().queue((channel) ->
		{
			channel.sendMessage("Sorry to see you leave " + event.getGuild().getName() + "!").queue();
		});
	}
	
	public static void logMute(MessageReceivedEvent event, List<Object> arguments)
	{
		// Logs the mute with the correct information.
		String t = "";
		if ((int)(arguments.get(1)) > 0)
		{
			t = " for " + (int)(arguments.get(1)) + " minutes";
		}
		else
		{
			t = " indefinatly";
		}
		String m = event.getAuthor().getName() + " muted " + ((Member)(arguments.get(0))).getEffectiveName() + t + " because \"" + (String)(arguments.get(2)) + ".\"";
		CommandHandler.getLogChannel(event.getGuild()).sendMessage(m).queue();
	}
	
	public static void logUnmute(MessageReceivedEvent event, List<Object> arguments)
	{
		// Logs the unmute with the correct information.
		CommandHandler.getLogChannel(event.getGuild()).sendMessage(event.getAuthor().getName() + " unmuted " + ((Member)(arguments.get(0))).getEffectiveName() + ".").queue();
	}
	
	public static void logKick()
	{
		
	}
	
	public static void logBan()
	{
		
	}
}
