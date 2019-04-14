package epicbot.commands.moderation;

import java.util.List;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import epicbot.Epic;
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
		this.help = "Bans the specified user. You must have an OPed role to use this command.";
		this.arguments = "<@user> [reason]";
		this.category = new Category("Moderation");
		this.guildOnly = true;
		this.requiredRole = Epic.settings.getOpedRole();
		this.botPermissions = new Permission[] {Permission.MESSAGE_WRITE, Permission.BAN_MEMBERS};
	}
	
	public void execute(CommandEvent event)
	{
		try
		{
			// Gets the arguments for the command.
			Member memberToBan = getMemberToBan(event);
			String banReason = getBanReason(event);
			
			// Checks if the user getting banned has an OPed role.
			if (Epic.settings.checkPerms(event.getGuild(), memberToBan.getRoles()))
			{
				// If the user has an OPed role an automated message will be sent.
				event.reply("Now, now. You mods play nice.");
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
		catch (IllegalArgumentException e)
		{
			// If the arguments are invalid the automated message will be sent.
			event.reply("You provided illegal arguments! Try `" +
				Epic.settings.getCommandPrefix() + "help ban` to get help with this command.");
		}
	}
	
	/**
	 * Returns the member to ban from a message received event.
	 * @param event the message received event
	 * @return the member to ban
	 * @throws IllegalArgumentException
	 */
	private static Member getMemberToBan(CommandEvent event) throws IllegalArgumentException
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
			throw new IllegalArgumentException("Argument Invalid!");
		}
		else
		{
			return member;
		}
	}
	
	/**
	 * Returns the ban reason from a message received event.
	 * @param event the message received event
	 * @return the reason for the ban
	 */
	private static String getBanReason(CommandEvent event)
	{
		// Creates a reason variable and gives it a default value of "No reason provided".
		String reason = "*No reason provided*";
		
		// Checks if the message has a reason.
		String[] message = event.getMessage().getContentRaw().split(" ");
		if (message.length >= 3)
		{
			reason = "";
			for (int i = 2; i < message.length; i++)
			{
				reason += (" " + message[i]);
			}
			// If there is a reason provided, reason will equal it.
			reason = reason.substring(1);
		}
		
		return reason;
	}
}
