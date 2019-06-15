package epicbot.commands.moderation;

import java.util.List;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import epicbot.commands.general.Help;
import epicbot.entities.BotBan;
import epicbot.settings.SettingsManager;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Member;

public class BotBanCMD extends Command
{
	public BotBanCMD()
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
			if (event.getArgs().split(" ").length < 1)
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
				if (BotBan.isBotBanned(memberToBotBan))
				{
					BotBan.removeBotBan(memberToBotBan);
					event.reply(memberToBotBan.getUser().getName() + " is no longer bot banned!");
				}
				else
				{
					BotBan.addBotBan(memberToBotBan);
					event.reply(memberToBotBan.getUser().getName() + " is now bot banned!");
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
}
