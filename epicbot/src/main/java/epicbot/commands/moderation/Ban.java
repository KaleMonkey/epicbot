package epicbot.commands.moderation;

import java.util.List;

import epicbot.Epic;
import epicbot.commands.Command;
import epicbot.util.AutoMod;
import epicbot.util.CommandHandler;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.exceptions.HierarchyException;

/**
 * @author Kyle Minter (Kale Monkey)
 */
public class Ban implements Command
{
	private static final String commandName = "Ban";
	private static final String commandDescription = "Bans the specified user. You must have an OPed role to use this command.";
	private static final String commandUsage = "`" + Epic.settings.getCommandPrefix() + "ban <User> <Reason>`\nUser must be an @" +
			" (example: @Epic Gamer Bot#6375).\nReason is not required, but is recomended.";
	private static final boolean commandGuildOnly = true;
	
	/**
	 * Returns the name of the command.
	 * @return the command name
	 */
	public String getName()
	{
		return commandName;
	}
	
	/**
	 * Returns the description of the command
	 * @return the command description
	 */
	public String getDescription()
	{
		return commandDescription;
	}
	
	/**
	 * Returns the usage instructions of the command
	 * @return the command description
	 */
	public String getUsage()
	{
		return commandUsage;
	}
	
	/**
	 * Checks if the command can be only used in a server.
	 * @return true if it can only be used in a server, false if it can be used elsewhere
	 */
	public boolean GuildOnly()
	{
		return commandGuildOnly;
	}
	
	/**
	 * Attempts to execute the command.
	 * @param event the event containing the message
	 */
	public void execute(MessageReceivedEvent event)
	{
		try
		{
			// Checks if the user calling the command has the required role for this command.
			if (!CommandHandler.checkPerms(event.getGuild(), event.getMember().getRoles()))
			{
				// If the user does not have the required role for this command the automated response will be sent.
				event.getChannel().sendMessage("You do not have the required permissions to use this command!").queue();
				return;
			}
			
			// Gets the arguments for the command.
			Member memberToBan = getMemberToBan(event);
			String banReason = getBanReason(event);
			
			// Checks if the user getting banned has an OPed role.
			if (CommandHandler.checkPerms(event.getGuild(), memberToBan.getRoles()))
			{
				// If the user has an OPed role an automated message will be sent.
				event.getChannel().sendMessage("Now, now. You mods play nice.").queue();
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
			event.getChannel().sendMessage("Banned " + memberToBan.getEffectiveName() + ".").queue();
			
			// If the user getting banned is not a bot they will be sent a message telling them they got banned.
			if (!(memberToBan.getUser().isBot()))
			{
				memberToBan.getUser().openPrivateChannel().queue((channel) ->
				{
					channel.sendMessage("You have been banned from the " + event.getGuild().getName() + " server because \"" + banReason + "\".").queue();
				});
			}
			
			// Logs the ban.
			AutoMod.logBan(event, memberToBan, banReason);
		}
		catch (IllegalArgumentException e)
		{
			// If the arguments are invalid the automated message will be sent.
			event.getChannel().sendMessage("You provided illegal arguments! Try `" +
				Epic.settings.getCommandPrefix() + "help ban` to get help with this command.").queue();
		}
		catch (HierarchyException e)
		{
			System.out.println("\n[Error]: The bot role is lower in the role hierarchy than the muted role!");
			System.out.println("[Error]: Please raise the bot's role in the role hierarchy.\n");
		}
	}
	
	/**
	 * Returns the member to ban from a message received event.
	 * @param event the message received event
	 * @return the member to ban
	 * @throws IllegalArgumentException
	 */
	private static Member getMemberToBan(MessageReceivedEvent event) throws IllegalArgumentException
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
	private static String getBanReason(MessageReceivedEvent event)
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
