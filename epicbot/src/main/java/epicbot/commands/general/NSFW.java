package epicbot.commands.general;

import java.util.List;

import epicbot.Epic;
import epicbot.commands.Command;
import epicbot.util.CommandHandler;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.exceptions.HierarchyException;

/**
 * @author Kyle Minter (Kale Monkey)
 */
public class NSFW implements Command
{
	private static final String commandName = "NSFW";
	private static final String commandDescription = "Adds the NSFW role to the user calling the command.";
	private static final String commandUsage = "`" + Epic.settings.getCommandPrefix() + "NSFW`";
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
			Member author = event.getMember();
			Role nsfwRole = CommandHandler.getNsfwRole(event.getGuild());
			
			// Checks if the author already has the NSFW role.
			if (hasRole(author, nsfwRole))
			{
				// If the author already has the NSFW role it will be removed.
				event.getGuild().getController().removeRolesFromMember(author, nsfwRole).queue();
				
				// Sends the response message.
				event.getChannel().sendMessage(getRandomRemoveResponse()).queue();
			}
			else
			{
				// If the author doesn't have the NSFW role it will be added.
				event.getGuild().getController().addRolesToMember(author, nsfwRole).queue();
				
				// Sends the response message.
				event.getChannel().sendMessage(getRandomAddResponse()).queue();
			}
		}
		catch (HierarchyException e)
		{
			System.out.println("\n[Error]: The bot role is lower in the role hierarchy than the muted role!");
			System.out.println("[Error]: Please raise the bot's role in the role hierarchy.\n");
		}
		catch (IllegalArgumentException e)
		{
			System.out.println("\n[Error]: There is no NSFW role provided in the config.json!");
			System.out.println("[Error]: Please edit the config.json if you want this command to work.\n");
		}
	}
	
	/**
	 * Checks if the user already has the NSFW role.
	 * @param author the author of the message as a Member object
	 * @param nsfwRole the NSFW role
	 * @return true if the user has the role, false if they don't
	 */
	private boolean hasRole(Member author, Role nsfwRole)
	{
		// Grabs the user roles.
		List<Role> roles = author.getRoles();
		
		// Checks if any of the user roles matches the NSFW role.
		for (Role r : roles)
		{
			if (r.equals(nsfwRole))
			{
				// If a user role matches the NSFW role, true will be returned.
				return true;
			}
		}
		// If none of the user roles matches the NSFW role, false will be returned.
		return false;
	}
	
	/**
	 * Gets a random response for the user if they received the NSFW role.
	 * @return a random response
	 */
	private String getRandomAddResponse()
	{
		String[] responses = {"I see you're a man of culture.",
				"Welcome to the darkside.",
				"You're in for one hell of a ride.",
				"Someone grab this man some lotion!"};
		return responses[(int)(Math.random() * responses.length)];
		
	}
	
	/**
	 * Gets a random response for the user if they removed the NSFW role.
	 * @return a random response
	 */
	private String getRandomRemoveResponse()
	{
		String[] responses = {"You've had enough, eh?",
				"You want some bleach for your eyes?",
				"I hope you can forget what you saw.",
				"Time to say goodbye to the anime tiddies."};
		return responses[(int)(Math.random() * responses.length)];
	}
}
