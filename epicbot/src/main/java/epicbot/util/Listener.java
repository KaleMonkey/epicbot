package epicbot.util;

import java.util.List;
import java.util.concurrent.TimeUnit;

import net.dv8tion.jda.core.audit.ActionType;
import net.dv8tion.jda.core.audit.AuditLogEntry;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberLeaveEvent;
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
		if (event instanceof GuildMemberJoinEvent)
		{
			GuildMemberJoinEvent gmje = (GuildMemberJoinEvent)event;
			
			// Sends the welcome message to the user.
			if (!(gmje.getUser().isBot()))
			{
				// Opens a private channel with the user and sends the welcome message.
				gmje.getUser().openPrivateChannel().queue((channel) ->
				{
					channel.sendMessage("Welcome to the " + gmje.getGuild().getName() + " discord server!").queue();
				});
			}
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
						if (!(gmle.getUser().isBot()))
						{
							// Opens a private channel with the user and sends the leave message.
							gmle.getUser().openPrivateChannel().queue((channel) ->
							{
								channel.sendMessage("Sad to see you leave " + gmle.getGuild().getName() + "!").queue();
							});
						}
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
