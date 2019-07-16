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
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberLeaveEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberRoleAddEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberRoleRemoveEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

/**
 * @author Kyle Minter (Kale Monkey)
 */
public class Listener extends ListenerAdapter
{
	public void onMessageReceived(MessageReceivedEvent event)
	{
		Epic.getExecutorService().execute(new Thread(() ->
		{
			List<User> mentionedUsers = event.getMessage().getMentionedUsers();
			for (User m : mentionedUsers)
			{
				if (m.equals(Epic.getAPI().getSelfUser()))
				{
					String[] shitposts = {"bruh", "stop", "can you fuck off?", "why are you @ing me right now..", "i will end you loser", "fuck you"};
					event.getChannel().sendMessage(shitposts[(int)(Math.random() * shitposts.length)]).queue();
					return;
				}
			}
		}));
	}
	
	public void onGuildMemberJoin(GuildMemberJoinEvent event)
	{
		Epic.getExecutorService().execute(new Thread(() ->
		{
			// Sends the welcome message to the user.
			if (!(event.getUser().isBot()))
			{
				// Opens a private channel with the user and sends the welcome message.
				event.getUser().openPrivateChannel().queue((channel) ->
				{
					channel.sendMessage("Welcome to the \"" + event.getGuild().getName() + "\" discord server!").queue();
				});
			}
		}));
	}
	
	public void onGuildMemberLeave(GuildMemberLeaveEvent event)
	{
		Epic.getExecutorService().execute(new Thread(() ->
		{
			// Checks to see if the user is in any other servers that the bot is in.
			long id = event.getUser().getIdLong();
			if (Epic.getAPI().getUserById(id) == null)
			{
				// If the bot can no longer see the user then all of their tags will be deleted.
				Tag.removeTagsById(id);
			}
		}));
	}
	
	public void onGuildMemberRoleAdd(GuildMemberRoleAddEvent event)
	{
		Epic.getExecutorService().execute(new Thread(() ->
		{
			// Gets the added roles.
			List<Role> addedRoles = event.getRoles();
			
			for (Role role : addedRoles)
			{
				// If any of the added roles matches the mute role the mute will be logged.
				if (role.equals(SettingsManager.getInstance().getSettings().getMuteRole(event.getGuild())))
				{
					try
					{
						List<AuditLogEntry> list = event.getGuild().getAuditLogs().cache(false).limit(1).submit().get(30, TimeUnit.SECONDS);
						
						for (AuditLogEntry ale : list)
						{
							if (ale.getType().equals(ActionType.MEMBER_ROLE_UPDATE))
							{
								// If the mute role was added through the bot it won't be logged because it will have already been logged.
								if (!(ale.getUser().equals(Epic.getAPI().getSelfUser())))
								{
									// Logs the mute.
									Logger.logMute(event, ale.getUser(), event.getMember(), 0, "*No reason provided*");
									
									// If the user getting muted is not a bot they will be sent a message telling them they got mmuted.
									if (!(event.getUser().isBot()))
									{
										event.getUser().openPrivateChannel().queue((channel) ->
										{
											channel.sendMessage("You have been muted in the \"" + event.getGuild().getName() + "\" discord server because \"*No reason provided*\".").queue();
										});
									}
									return;
								}
							}
						}
					}catch (Exception e){}
				}
			}
		}));
	}
	
	public void onGuildMemberRoleRemove(GuildMemberRoleRemoveEvent event)
	{
		Epic.getExecutorService().execute(new Thread(() ->
		{
			// Gets the removed roles.
			List<Role> removedRoles = event.getRoles();
			
			for (Role role : removedRoles)
			{
				// If any of the removed roles matches the mute role the unmute will be logged.
				if (role.equals(SettingsManager.getInstance().getSettings().getMuteRole(event.getGuild())))
				{
					try
					{
						List<AuditLogEntry> list = event.getGuild().getAuditLogs().cache(false).limit(1).submit().get(30, TimeUnit.SECONDS);
						
						for (AuditLogEntry ale : list)
						{
							if (ale.getType().equals(ActionType.MEMBER_ROLE_UPDATE))
							{
								// If the mute role was removed through the bot it won't be logged because it will have already been logged.
								if (!(ale.getUser().equals(Epic.getAPI().getSelfUser())))
								{
									Logger.logUnmute(event, ale.getUser(), event.getMember());
									
									// If the user getting unmuted is not a bot they will be sent a message telling them they got unmuted.
									if (!(event.getUser().isBot()))
									{
										event.getUser().openPrivateChannel().queue((channel) ->
										{
											channel.sendMessage("You have been unmuted in the \"" + event.getGuild().getName() + "\" discord server because a mod lifted your mute.").queue();
										});
									}
									return;
								}
							}
						}
					}catch (Exception e){}
				}
			}
		}));
	}
}