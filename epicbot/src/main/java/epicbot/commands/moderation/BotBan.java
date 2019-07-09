package epicbot.commands.moderation;

import java.util.List;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import epicbot.commands.general.Help;
import epicbot.settings.SettingsManager;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;

public class BotBan extends Command
{
	public BotBan()
	{
		this.name = "botban";
		this.help = "Bot bans the specified user.";
		this.arguments = "<@user>";
		this.category = new Category("Moderation");
		this.guildOnly = true;
		this.requiredRole = SettingsManager.getInstance().getSettings().getOpedRole();
		this.botPermissions = new Permission[] {Permission.MESSAGE_WRITE, Permission.MANAGE_ROLES};
	}
	
	public void execute(CommandEvent event)
	{
		try
		{
			if (event.getChannelType() == ChannelType.TEXT && isBotBanned(event.getMember()))
			{
				event.reply("You are bot banned on this server! You must be unbanned to use any of my commands.");
			}
			else if (event.getArgs().split(" ").length < 1)
			{
				event.reply("You did not provide the necessary arguments for this command!" + Help.getHelp(this.name));
				return;
			}
			else if (event.getArgs().split(" ").length > 1)
			{
				event.reply("You provided too many arguments for this command!" + Help.getHelp(this.name));
				return;
			}
			else
			{
				// Gets the member to bot ban.
				Member memberToBotBan = getMemberToBotBan(event);
				
				// Checks if the user getting bot banned has an OPed role.
				if (SettingsManager.getInstance().getSettings().checkPerms(event.getGuild(), memberToBotBan.getRoles()))
				{
					// If the user has an OPed role an automated message will be sent.
					event.reply("You cannot bot ban other mods!");
					return;
				}
				
				// Checks if the user is already bot banned.
				if (isBotBanned(memberToBotBan))
				{
					// Removes the bot ban role.
					event.getGuild().getController().removeRolesFromMember(memberToBotBan, SettingsManager.getInstance().getSettings().getBotBanRole(event.getGuild())).queue();
					
					// Sends message confirming that the unbot ban worked.
					event.reply(memberToBotBan.getUser().getName() + " is no longer bot banned!");
					
					// If the user getting unbot banned is not a bot they will be sent a message telling them they got muted.
					if (!(memberToBotBan.getUser().isBot()))
					{
						memberToBotBan.getUser().openPrivateChannel().queue((channel) ->
						{
							channel.sendMessage("You have been un-bot banned in the \"" + event.getGuild().getName() + "\" discord server.").queue();
						});
					}
				}
				else
				{
					// Adds the bot ban role.
					event.getGuild().getController().addRolesToMember(memberToBotBan, SettingsManager.getInstance().getSettings().getBotBanRole(event.getGuild())).queue();
					
					// Sends message confirming that the bot ban worked.
					event.reply(memberToBotBan.getUser().getName() + " is now bot banned!");
					
					// If the user getting bot banned is not a bot they will be sent a message telling them they got muted.
					if (!(memberToBotBan.getUser().isBot()))
					{
						memberToBotBan.getUser().openPrivateChannel().queue((channel) ->
						{
							channel.sendMessage("You have been bot banned in the \"" + event.getGuild().getName() + "\" discord server.").queue();
						});
					}
				}
			}
		}
		catch (IllegalArgumentException e)
		{
			return;
		}
	}
	
	/*
	 * Returns the member to bot ban from a command event.
	 * @param event the command event
	 * @return the member to bot ban
	 * @throws IllegalArgumentException
	 */
	private Member getMemberToBotBan(CommandEvent event) throws IllegalArgumentException
	{
		// Creates a member variable and gives it a default value of null.
		Member member = null;
		
		// Checks if the message mentions a member.
		List<Member> mentionedMembers = event.getMessage().getMentionedMembers(event.getGuild());
		if (mentionedMembers.size() == 1)
		{
			// If there is a mentioned member, member will become that member.
			member = mentionedMembers.get(0);
		}
		
		// If member is still at it's default value an exception will be thrown.
		if (member == null)
		{
			event.reply("You did not provide a user to bot ban!" + Help.getHelp(this.name));
			throw new IllegalArgumentException("Argument Invalid!");
		}
		else
		{
			return member;
		}
	}
	
	/**
	 * Checks if a user is bot banned.
	 * @param member the member we are checking
	 * @return true if the user is bot banned, false if they aren't
	 */
	public static boolean isBotBanned(Member member)
	{
		List<Role> r = member.getRoles();
		
		for (Role userRole : r)
		{
			if (userRole.equals(SettingsManager.getInstance().getSettings().getBotBanRole(member.getGuild())))
			{
				// If a user role matches the bot ban role, true will be returned.
				return true;
			}
		}
		
		// If none of the user roles matches the bot ban role, false will be returned.
		return false;
	}
}
