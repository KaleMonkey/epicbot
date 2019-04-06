package epicbot.commands.moderation;

import java.util.List;

import epicbot.Epic;
import epicbot.commands.Command;
import epicbot.util.AutoMod;
import epicbot.util.CommandHandler;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.exceptions.HierarchyException;

/**
 * @author Kyle Minter (Kale Monkey)
 */
public class Unmute implements Command
{
	private static final String commandName = "Unmute";
	private static final String commandDescription = "Unmutes the given user. You must have an OPed role to use this command.";
	private static final String commandUsage = "`" + Epic.settings.getCommandPrefix() + "unmute <User>`\n User must be an @" +
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
			Member memberToUnmute = getMemberToUnmute(event);
			
			if (!isMuted(event.getGuild(), memberToUnmute.getRoles()))
			{
				// If the user is not muted the automated response will be sent.
				event.getChannel().sendMessage("The user provided is not muted!").queue();
				return;
			}
			
			// Unmutes the user.
			event.getGuild().getController().removeRolesFromMember(memberToUnmute, CommandHandler.getMuteRole(event.getGuild())).queue();
			
			// Sends message confirming that the unmute worked.
			event.getChannel().sendMessage("Unmuted " + memberToUnmute.getEffectiveName() + ".").queue();
			
			// If the user getting muted is not a bot they will be sent a message telling them they got unmuted.
			if (!(memberToUnmute.getUser().isBot()))
			{
				memberToUnmute.getUser().openPrivateChannel().queue((channel) ->
				{
					channel.sendMessage("You have been unmuted because a mod took mercy on you.").queue();
				});
			}
			
			// Logs the unmute.
			AutoMod.logUnmute(event, memberToUnmute);
		}
		catch (IllegalArgumentException e)
		{
			// If the arguments are invalid the automated message will be sent.
			event.getChannel().sendMessage("You provided illegal arguments! Try `" +
					Epic.settings.getCommandPrefix() + "help unmute` to get help with this command.").queue();
		}
		catch (HierarchyException e)
		{
			System.out.println("\n[Error]: The bot role is lower in the role hierarchy than the muted role!");
			System.out.println("[Error]: Please raise the bot's role in the role hierarchy.\n");
		}
	}
	
	/**
	 * Returns the member to unmute from the message received event.
	 * @param event the message received event
	 * @return the member to unmute
	 * @throws IllegalArgumentException
	 */
	private static Member getMemberToUnmute(MessageReceivedEvent event) throws IllegalArgumentException
	{
		// Creates a member variable and gives it a default value of null.
		Member member = null;
		
		// Checks if the message mentions a member.
		List<Member> mentionedMembers = event.getMessage().getMentionedMembers(event.getGuild());
		if (mentionedMembers.size() == 1)
		{
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
	 * Checks to see if the user is muted or not.
	 * @param g the guild the message was sent in
	 * @param r the user roles
	 * @return true if the user is muted, false if they are not
	 */
	private boolean isMuted(Guild g, List<Role> r)
	{
		// Gets the mute role for the guild.
		Role mute = CommandHandler.getMuteRole(g);
		
		// Checks to see if the provided user roles matches the mute role in the guild.
		for (Role userRole : r)
		{
			if (userRole.equals(mute))
			{
				// If a user role matches the mute role, true will be returned.
				return true;
			}
		}
		// If none of the user roles matches the mute role in the guild, false will be returned.
		return false;
	}
}
