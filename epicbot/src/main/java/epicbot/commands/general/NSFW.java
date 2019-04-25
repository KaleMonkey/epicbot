package epicbot.commands.general;

import java.util.List;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import epicbot.settings.SettingsManager;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;

/**
 * @author Kyle Minter (Kale Monkey)
 */
public class NSFW extends Command
{
	public NSFW()
	{
		this.name = "nsfw";
		this.help = "Adds/removes the server's NSFW role to the user calling the command.";
		this.category = new Category("General");
		this.guildOnly = true;
		this.botPermissions = new Permission[] {Permission.MESSAGE_WRITE, Permission.MANAGE_ROLES};
	}
	
	public void execute(CommandEvent event)
	{
		if (event.getArgs().equals(""))
		{
			Member author = event.getMember();
			Role nsfwRole = SettingsManager.getInstance().getSettings().getNsfwRole(event.getGuild());
			
			// If there is not a dedicated NSFW role in the server it is assumed that the server doesn't want that functionality.
			if (nsfwRole == null)
			{
				// Sends the automated response.
				event.reply("This server doesn't support this command!");
			}
			
			// Checks if the author already has the NSFW role.
			if (hasRole(author, nsfwRole))
			{
				// If the author already has the NSFW role it will be removed.
				event.getGuild().getController().removeRolesFromMember(author, nsfwRole).queue();
				
				// Sends the response message.
				event.reply(getRandomRemoveResponse());
			}
			else
			{
				// If the author doesn't have the NSFW role it will be added.
				event.getGuild().getController().addRolesToMember(author, nsfwRole).queue();
				
				// Sends the response message.
				event.reply(getRandomAddResponse());
			}
		}
		else
		{
			event.reply("This command does not have any arguments!" + Help.getHelp(this.name));
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
