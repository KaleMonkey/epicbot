package epicbot.util;

import java.util.List;
import java.util.concurrent.TimeUnit;

import epicbot.Epic;
import epicbot.entities.Tag;
import epicbot.settings.SettingsManager;
import net.dv8tion.jda.core.audit.ActionType;
import net.dv8tion.jda.core.audit.AuditLogEntry;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberLeaveEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberRoleAddEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberRoleRemoveEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
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
		if (event instanceof MessageReceivedEvent)
		{
			MessageReceivedEvent mre = (MessageReceivedEvent)event;
			
			List<User> mentionedUsers = mre.getMessage().getMentionedUsers();
			for (User m : mentionedUsers)
			{
				if (m.equals(Epic.getAPI().getSelfUser()))
				{
					String[] shitposts = {"bruh", "stop", "can you fuck off?", "why are you @ing me right now..", "i will end you loser", "fuck you"};
					mre.getChannel().sendMessage(shitposts[(int)(Math.random() * shitposts.length)]).queue();
					return;
				}
			}

		}
		else if (event instanceof GuildMemberJoinEvent)
		{
			GuildMemberJoinEvent gmje = (GuildMemberJoinEvent)event;
			
			// Sends the welcome message to the user.
			if (!(gmje.getUser().isBot()))
			{
				// Opens a private channel with the user and sends the welcome message.
				gmje.getUser().openPrivateChannel().queue((channel) ->
				{
					channel.sendMessage("Welcome to the \"" + gmje.getGuild().getName() + "\" discord server!").queue();
				});
			}
		}
		// Checks if event is a member leave event.
		else if (event instanceof GuildMemberLeaveEvent)
		{
			GuildMemberLeaveEvent gmle = (GuildMemberLeaveEvent)event;
			
			try
			{
				// Checks to see if the user is in any other servers that the bot is in.
				long id = gmle.getUser().getIdLong();
				if (Epic.getAPI().getUserById(id) == null)
				{
					// If the bot can no longer see the user then all of their tags will be deleted.
					Tag.removeTagsById(id);
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
				// If any of the added roles matches the mute role the mute will be logged.
				if (role.equals(SettingsManager.getInstance().getSettings().getMuteRole(gmrae.getGuild())))
				{
					try
					{
						List<AuditLogEntry> list = gmrae.getGuild().getAuditLogs().cache(false).limit(1).submit().get(30, TimeUnit.SECONDS);
						
						for (AuditLogEntry ale : list)
						{
							if (ale.getType().equals(ActionType.MEMBER_ROLE_UPDATE))
							{
								// If the mute role was added through the bot it won't be logged because it will have already been logged.
								if (!(ale.getUser().equals(Epic.getAPI().getSelfUser())))
								{
									// Logs the mute.
									Logger.logMute(gmrae, ale.getUser(), gmrae.getMember(), 0, "*No reason provided*");
									
									// If the user getting muted is not a bot they will be sent a message telling them they got mmuted.
									if (!(gmrae.getUser().isBot()))
									{
										//gmrae.getUser().openPrivateChannel().queue((channel) ->
										//{
											//channel.sendMessage("You have been muted in the\" " + gmrae.getGuild().getName() + "\" discord server because \"*No reason provided*\".").queue();
										//});
									}
									return;
								}
							}
						}
					}catch (Exception e){}
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
				// If any of the removed roles matches the mute role the unmute will be logged.
				if (role.equals(SettingsManager.getInstance().getSettings().getMuteRole(gmrre.getGuild())))
				{
					
					try
					{
						List<AuditLogEntry> list = gmrre.getGuild().getAuditLogs().cache(false).limit(1).submit().get(30, TimeUnit.SECONDS);
						
						for (AuditLogEntry ale : list)
						{
							if (ale.getType().equals(ActionType.MEMBER_ROLE_UPDATE))
							{
								// If the mute role was removed through the bot it won't be logged because it will have already been logged.
								if (!(ale.getUser().equals(Epic.getAPI().getSelfUser())))
								{
									Logger.logUnmute(gmrre, ale.getUser(), gmrre.getMember());
									
									// If the user getting unmuted is not a bot they will be sent a message telling them they got unmuted.
									if (!(gmrre.getUser().isBot()))
									{
										gmrre.getUser().openPrivateChannel().queue((channel) ->
										{
											channel.sendMessage("You have been unmuted in the \"" + gmrre.getGuild().getName() + "\" discord server because a mod took mercy on you.").queue();
										});
									}
									return;
								}
							}
						}
					}catch (Exception e){}
				}
			}
		}
	}
}
