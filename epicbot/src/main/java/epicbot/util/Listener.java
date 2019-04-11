package epicbot.util;

import java.util.List;
import java.util.concurrent.TimeUnit;

import net.dv8tion.jda.core.audit.ActionType;
import net.dv8tion.jda.core.audit.AuditLogEntry;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberLeaveEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.EventListener;

/**
 * @author Kyle Minter (Kale Monkey)
 */
public class Listener implements EventListener
{
	public void onEvent(Event event)
	{
		// Creates a new thread to do everything on.
		RunnableThread rt = new RunnableThread(event);
		Thread t = new Thread(rt);
		t.start();
	}
}

/**
 * @author Kyle Minter (Kale Monkey)
 */
class RunnableThread implements Runnable
{
	private Event event;
	
	/**
	 * Constructs a RunnableThread object with a given event.
	 * @param event the event that started the new thread
	 */
	public RunnableThread(Event event)
	{
		this.event = event;
	}
	
	/**
	 * Figures out what type of Event the event is and completes the appropriate actions. 
	 */
	public void run()
	{
		// Checks if event is a guild message received event.
		if (event instanceof MessageReceivedEvent)
		{
			MessageReceivedEvent e = (MessageReceivedEvent)event;
			
			if (e.getMessage().isFromType(ChannelType.TEXT) && e.getMessage().getMentionedUsers().indexOf(event.getJDA().getSelfUser()) != -1)
			{
				// If message is from a server and mentions the bot a shit post will be sent in response.
				String[] shitposts = {"stfu loser", "why tf are you @ing me", "@ me again and ill ban you", "ur mom gey"};
				e.getChannel().sendMessage(shitposts[(int)(Math.random() * shitposts.length)]).queue();
			}
			else
			{
				// Parses the event.
				CommandHandler.parseEvent(e);
			}
			// Runs automod on the message.
		}
		// Checks if event is a member join event.
		else if (event instanceof GuildMemberJoinEvent)
		{
			GuildMemberJoinEvent gmje = (GuildMemberJoinEvent)event;
			
			// Sends the welcome message to the user.
			AutoMod.sendWelcomeMessage(gmje);
		}
		// Checks if event is a member leave event.
		else if (event instanceof GuildMemberLeaveEvent)
		{
			GuildMemberLeaveEvent gmle = (GuildMemberLeaveEvent)event;
			
			try
			{
				// Checks if member leaving was due to a kick or ban.
				List<AuditLogEntry> list = gmle.getGuild().getAuditLogs().cache(false).limit(1).submit().get(30, TimeUnit.SECONDS);
				for (AuditLogEntry ale : list)
				{
					if (!(ale.getType().equals(ActionType.KICK) || ale.getType().equals(ActionType.BAN)))
					{
						// If the member leave event is not cause by a kick then the leave message will be sent.
						AutoMod.sendLeaveMessage(gmle);
					}
				}
			}
			catch (Exception e)
			{
				System.out.println("Exception thown during kick/ban check.");
			}
		}
	}
}
