package epicbot.commands.moderation;

import java.util.List;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import epicbot.Epic;
import epicbot.util.Logger;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Member;

public class Kick extends Command
{	
	public Kick()
	{
		this.name = "kick";
		this.help = "Kick the specified user. You must have an OPed role to use this command.";
		this.arguments = "<@user> [reason]";
		this.category = new Category("Moderation");
		this.guildOnly = true;
		this.requiredRole = Epic.settings.getOpedRole();
		this.botPermissions = new Permission[] {Permission.MESSAGE_WRITE, Permission.KICK_MEMBERS};
	}
	
	public void execute(CommandEvent event)
	{
		try
		{			
			// Gets the arguments for the command.
			Member memberToKick = getMemberToKick(event);
			String kickReason = getKickReason(event);
			
			// Checks if the user getting kicked has an OPed role.
			if (Epic.settings.checkPerms(event.getGuild(), memberToKick.getRoles()))
			{
				// If the user has an OPed role an automated message will be sent.
				event.reply("Now, now. You mods play nice.");
				return;
			}
			
			// Kicks the user.
			if (kickReason.equals("*No reason provided*"))
			{
				event.getGuild().getController().kick(memberToKick).queue();
			}
			else
			{
				event.getGuild().getController().kick(memberToKick, kickReason).queue();
			}
			
			// Sends message confirming that the kick worked.
			event.reply("Kicked " + memberToKick.getEffectiveName() + ".");
			
			// If the user getting banned is not a bot they will be sent a message telling them they got kicked.
			if (!(memberToKick.getUser().isBot()))
			{
				memberToKick.getUser().openPrivateChannel().queue((channel) ->
				{
					channel.sendMessage("You have been kicked from the " + event.getGuild().getName() + " server because \"" + kickReason + "\".").queue();
				});
			}
			
			// Logs the ban.
			Logger.logKick(event, memberToKick, kickReason);
		}
		catch (IllegalArgumentException e)
		{
			// If the arguments are invalid the automated message will be sent.
			event.reply("You provided illegal arguments! Try `" +
				Epic.settings.getCommandPrefix() + "help kick` to get help with this command.");
		}
	}
	
	/**
	 * Returns the member to kick from a message received event.
	 * @param event the message received event
	 * @return the member to kick
	 * @throws IllegalArgumentException
	 */
	private static Member getMemberToKick(CommandEvent event)
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
	 * Returns the kick reason from a message received event.
	 * @param event the message received event
	 * @return the reason for the kick
	 */
	private static String getKickReason(CommandEvent event)
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
