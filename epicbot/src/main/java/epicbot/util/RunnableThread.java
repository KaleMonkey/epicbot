package epicbot.util;

import epicbot.commands.CommandHandler;
import net.dv8tion.jda.core.audit.ActionType;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.guild.GuildBanEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberLeaveEvent;
import net.dv8tion.jda.core.events.message.MessageBulkDeleteEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 * @author Kyle Minter (Kale Monkey)
 */
public class RunnableThread implements Runnable
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
			// Parses the event.
			CommandHandler.parseEvent(e);
			// Runs automod on the message.
		}
		// Checks if event is a bulk message delete event.
		else if (event instanceof MessageBulkDeleteEvent)
		{
			MessageBulkDeleteEvent mbde = (MessageBulkDeleteEvent)event;
			
			// Signal the automod because messages were deleted in bulk.
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
			
			// Checks if member leaving was due to a kick.
			if (!(gmle.getGuild().getAuditLogs().getLast().getType().equals(ActionType.KICK)))
			{
				// If the member leave event is not cause by a kick then the leave message will be sent.
				AutoMod.sendLeaveMessage(gmle);
			}
			else
			{
				// If the member leave event is a kick it will be logged.
				AutoMod.logKick();
			}
		}
		else if (event instanceof GuildBanEvent)
		{
			GuildBanEvent gbe = (GuildBanEvent)event;
			
			// Logs the ban.
			AutoMod.logBan();
		}
	}
}
