package epicbot.commands.moderation;

import java.util.List;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import epicbot.commands.general.Help;
import epicbot.entities.BotBan;
import epicbot.settings.SettingsManager;
import epicbot.util.Logger;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Member;

/**
 * @author Kyle Minter (Kale Monkey)
 */
public class Ban extends Command
{	
	public Ban()
	{
		this.name = "ban";
		this.help = "Bans the specified user.";
		this.arguments = "<@user> [reason (optional)]";
		this.category = new Category("Moderation");
		this.guildOnly = true;
		this.requiredRole = SettingsManager.getInstance().getSettings().getOpedRole();
		this.botPermissions = new Permission[] {Permission.MESSAGE_WRITE, Permission.BAN_MEMBERS};
	}
	
	public void execute(CommandEvent event)
	{
		try
		{
			if (BotBan.isBotBanned(event.getMember()))
			{
				event.reply("You are bot banned on this server! You must be unbanned to use any of my commands.");
			}
			else if (event.getArgs().split(" ").length < 1)
			{
				event.reply("You did not provide the necessary arguments for this command!" + Help.getHelp(this.name));
			}
			else
			{
				// Gets the arguments for the command.
				Member memberToBan = getMemberToBan(event);
				String banReason = getBanReason(event);
				
				// Checks if the user getting banned has an OPed role.
				if (SettingsManager.getInstance().getSettings().checkPerms(event.getGuild(), memberToBan.getRoles()))
				{
					// If the user has an OPed role an automated message will be sent.
					event.reply("You cannot ban another mod!");
					return;
				}
				
				// Bans the user.
				if (banReason.equals("*No reason provided*"))
				{
					event.getGuild().getController().ban(memberToBan, 0).queue();
				}
				else
				{
					event.getGuild().getController().ban(memberToBan, 0, banReason).queue();
				}
				
				// Sends message confirming that the ban worked.
				event.reply("Banned " + memberToBan.getEffectiveName() + ".");
				
				// If the user getting banned is not a bot they will be sent a message telling them they got banned.
				if (!(memberToBan.getUser().isBot()))
				{
					memberToBan.getUser().openPrivateChannel().queue((channel) ->
					{
						channel.sendMessage("You have been banned from the " + event.getGuild().getName() + " server because \"" + banReason + "\".").queue();
					});
				}
				
				// Logs the ban.
				Logger.logBan(event, memberToBan, banReason);
			}
		}
		catch (IllegalArgumentException e)
		{
			return;
		}
	}
	
	/**
	 * Returns the member to ban from a command event.
	 * @param event the command event
	 * @return the member to ban
	 * @throws IllegalArgumentException
	 */
	private Member getMemberToBan(CommandEvent event) throws IllegalArgumentException
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
			event.reply("You did not provide a user to ban!" + Help.getHelp(this.name));
			throw new IllegalArgumentException("Argument Invalid!");
		}
		else
		{
			return member;
		}
	}
	
	/**
	 * Returns the ban reason from a command event.
	 * @param event the command event
	 * @return the reason for the ban
	 */
	private String getBanReason(CommandEvent event)
	{
		// Creates a reason variable and gives it a default value of "No reason provided".
		String reason = "*No reason provided*";
		
		// Checks if the message has a reason.
		String[] args = event.getArgs().split(" ");
		if (args.length >= 2)
		{
			reason = "";
			for (int i = 1; i < args.length; i++)
			{
				reason += (" " + args[i]);
			}
			// If there is a reason provided, reason will become it.
			reason = reason.substring(1);
		}
		
		return reason;
	}
}
