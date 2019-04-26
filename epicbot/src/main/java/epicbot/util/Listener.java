package epicbot.util;

import java.util.List;
import java.util.concurrent.TimeUnit;

import epicbot.Epic;
import epicbot.entities.MutedMember;
import epicbot.settings.SettingsManager;
import net.dv8tion.jda.core.audit.ActionType;
import net.dv8tion.jda.core.audit.AuditLogEntry;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberLeaveEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberRoleAddEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberRoleRemoveEvent;
import net.dv8tion.jda.core.hooks.EventListener;

/**
 * @author Kyle Minter (Kale Monkey)
 */
public class Listener implements EventListener
{
	public void onEvent(Event event)
	{
		// Starts a new thread to do stuff on.
		Epic.getExecutorService().execute(new RunnableThread(event));
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
		// Checks if the event was a role being added to a user in a server.
		else if (event instanceof GuildMemberRoleAddEvent)
		{
			GuildMemberRoleAddEvent gmrae = (GuildMemberRoleAddEvent)event;
			
			// Gets the added roles.
			List<Role> addedRoles = gmrae.getRoles();
			
			for (Role role : addedRoles)
			{
				// If any of the added roles matches the mute role the member will be added to the MutedMember list.
				if (role.equals(SettingsManager.getInstance().getSettings().getMuteRole(gmrae.getGuild())))
				{
					MutedMember.addMutedMember(new MutedMember(gmrae.getMember()));
					
					// If the user getting muted is not a bot they will be sent a message telling them they got mmuted.
					if (!(gmrae.getUser().isBot()))
					{
						gmrae.getUser().openPrivateChannel().queue((channel) ->
						{
							channel.sendMessage("You have been muted in" + gmrae.getGuild() + "server because \"*No reason provided*\".").queue();
						});
					}
				}
			}
		}
		// Checks if the event was a role being removed from a user in a server.
		else if (event instanceof GuildMemberRoleRemoveEvent)
		{
			GuildMemberRoleRemoveEvent gmrre = (GuildMemberRoleRemoveEvent)event;
			
			// Gets the removed roles.
			List<Role> removedRoles = gmrre.getRoles();
			
			for (Role role : removedRoles)
			{
				// If any of the removed roles matches the mute role the member will be removed from the MutedMember list.
				if (role.equals(SettingsManager.getInstance().getSettings().getMuteRole(gmrre.getGuild())))
				{
					MutedMember.removeMutedMember(new MutedMember(gmrre.getMember()));
					
					// If the user getting unmuted is not a bot they will be sent a message telling them they got unmuted.
					if (!(gmrre.getUser().isBot()))
					{
						gmrre.getUser().openPrivateChannel().queue((channel) ->
						{
							channel.sendMessage("You have been unmuted in " + gmrre.getGuild() + "because a mod took mercy on you.").queue();
						});
					}
				}
			}
		}
	}
}
