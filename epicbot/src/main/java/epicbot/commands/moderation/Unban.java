package epicbot.commands.moderation;

import java.util.List;

import epicbot.Epic;
import epicbot.commands.Command;
import epicbot.util.AutoMod;
import epicbot.util.CommandHandler;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 * @author Kyle Minter (Kale Monkey)
 */
public class Unban implements Command
{
	private static final String commandName = "Unban";
	private static final String commandDescription = "Unbans the specified user. You must have an OPed role to use this command.";
	private static final String commandUsage = "`" + Epic.settings.getCommandPrefix() + "unban <User>`\nUser must be an @" +
			" (example: @Epic Gamer Bot#6375).";
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
			User userToUnban = getUserToUnban(event);
			
			// Unbans the user.
			event.getGuild().getController().unban(userToUnban).queue();
			
			// Sends message confirming that the ban worked.
			event.getChannel().sendMessage("Banned " + userToUnban.getName() + ".").queue();
			
			// If the user getting banned is not a bot they will be sent a message telling them they got unbanned.
			if (!(userToUnban.isBot()))
			{
				userToUnban.openPrivateChannel().queue((channel) ->
				{
					channel.sendMessage("You have been unbanned from the " + event.getGuild().getName() + " server.").queue();
				});
			}
			
			// Logs the ban.
			AutoMod.logUnban(event, userToUnban);
		}
		catch (IllegalArgumentException e)
		{
			System.out.println("illegal argument");
		}
		catch (Exception e)
		{
			System.out.println("well something went wrong");
		}
	}
	
	/**
	 * Returns the member to unban from a message received event.
	 * @param event the message received event
	 * @return the member to unban
	 * @throws IllegalArgumentException
	 */
	private static User getUserToUnban(MessageReceivedEvent event) throws IllegalArgumentException
	{
		// Creates a member variable and gives it a default value of null.
		User user = null;
		
		// Checks if the message mentions a member.
		List<User> mentionedUsers = event.getMessage().getMentionedUsers();
		System.out.println(mentionedUsers);
		if (mentionedUsers.size() == 1)
		{
			// If there is a mentioned member, member will become that member.
			user = mentionedUsers.get(0);
		}
		
		// If member is still at it's default value an exception will be thrown.
		if (user == null)
		{
			System.out.println("this bitch null");
			throw new IllegalArgumentException("Argument Invalid!");
		}
		else
		{
			return user;
		}
	}
}
