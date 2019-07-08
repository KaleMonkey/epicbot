package epicbot.commands.general;

import java.util.List;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import epicbot.commands.moderation.BotBan;
import epicbot.settings.SettingsManager;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Role;

public class RoleMe extends Command
{
	public RoleMe()
	{
		this.name = "roleme";
		this.help = "Adds a given role to the user. If no role is provided a list of toggleable roles will be given.";
		this.arguments = "[role (optional)]";
		this.category = new Category("General");
		this.guildOnly = true;
		this.botPermissions = new Permission[] {Permission.MESSAGE_WRITE, Permission.MANAGE_ROLES};
	}
	
	public void execute(CommandEvent event)
	{
		if (event.getChannelType() == ChannelType.TEXT && BotBan.isBotBanned(event.getMember()))
		{
			event.reply("You are bot banned on this server! You must be unbanned to use any of my commands.");
		}
		else if (event.getArgs().split(" ").length > 1)
		{
			event.reply("You provided too many arguments!" + Help.getHelp(this.name));
		}
		else
		{
			List<Role> roles = SettingsManager.getInstance().getSettings().getToggleableRoles(event.getGuild());
			roles.add(SettingsManager.getInstance().getSettings().getMinigameRole(event.getGuild()));
			
			// If the command call has no arguments a list of toggle-able roles will be sent.
			if (event.getArgs().equals(""))
			{
				//Starts building an embedded message.
				EmbedBuilder eb = new EmbedBuilder();
				eb.setAuthor("Epic", "https://github.com/KaleMonkey/epicbot");
				eb.setDescription("Try `" + SettingsManager.getInstance().getSettings().getCommandPrefix() + "roleme [role]` to toggle a specific role!");
				
				// Assembles a list of all the toggle-able roles.
				String temp = "";
				for (Role role : roles)
				{
					temp += role.getName() + "\n";
				}
							
				// Puts all the toggle-able role names into a field.
				eb.addField("Roles", temp, true);
				
				// Sends the list of toggle-able roles.
				event.reply(eb.build());
			}
			else
			{
				Role role = null;
				
				// Checks if the provided argument matches any of the toggle-able role names.
				for (Role r : roles)
				{
					if (event.getArgs().split(" ")[0].equalsIgnoreCase(r.getName()))
					{
						// If the argument matches a toggle-able role name the role object is saved.
						role = r;
					}
				}
				
				// If role is still null an error message will be sent.
				if (role == null)
				{
					event.reply("The role provided does not match any know toggle-able roles!" + Help.getHelp(this.name));
				}
				else
				{
					// Checks to see if the user already has the role.
					List<Role> userRoles = event.getMember().getRoles();
					for (Role r : userRoles)
					{
						if (r.equals(role))
						{
							// If the user already has the role it will be removed.
							event.getGuild().getController().removeRolesFromMember(event.getMember(), role).queue();
							
							// Sends a message confirming that the role was removed.
							EmbedBuilder eb = new EmbedBuilder();
							eb.setAuthor("Epic", "https://github.com/KaleMonkey/epicbot");
							if (role.equals(SettingsManager.getInstance().getSettings().getMinigameRole(event.getGuild())))
							{
								eb.setDescription("Removed the role \"" + role.getName() + "\" and deleted all your minigame data!");
								// TODO:
							}
							else
							{
								eb.setDescription("Removed the role \"" + role.getName() + "\"!");
							}
							event.reply(eb.build());
							return;
						}
					}
					
					// If the user does not have the role it will be added.
					event.getGuild().getController().addRolesToMember(event.getMember(), role).queue();
					
					// Sends a message confirming that the role was added.
					EmbedBuilder eb = new EmbedBuilder();
					eb.setAuthor("Epic", "https://github.com/KaleMonkey/epicbot");
					if (role.equals(SettingsManager.getInstance().getSettings().getMinigameRole(event.getGuild())))
					{
						eb.setDescription("Added the role  \"" + role.getName() + "\" and created your minigame data!");
						// TODO:
					}
					else
					{
						eb.setDescription("Added the role  \"" + role.getName() + "\"!");
					}
					event.reply(eb.build());
				}
			}
		}
	}
}
