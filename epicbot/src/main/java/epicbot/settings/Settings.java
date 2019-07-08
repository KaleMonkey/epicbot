package epicbot.settings;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import epicbot.entities.Server;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.TextChannel;

/**
 * @author Kyle Minter (Kale Monkey)
 */
public class Settings
{
	private String botToken;
	private String ownerID;
	private String commandPrefix;
	private String opedRole;
	private String muteRole;
	private String botBanRole;
	private String minigameRole;
	private String[] toggleableRoles;
	private String logChannelName;
	private Server[] servers;
	
	/**
	 * Sets the bot token.
	 * @param token the bot token
	 */
	public void setBotToken(String token)
	{
		botToken = token;
	}
	
	/**
	 * Gets the bot token.
	 * @return the bot token
	 */
	public String getBotToken()
	{
		return botToken;
	}
	
	/**
	 * Sets the owner id.
	 * @param id the owner id
	 */
	public void setOwnerID(String id)
	{
		ownerID = id;
	}
	
	/**
	 * Gets the owner id.
	 * @return the owner id
	 */
	public String getOwnerID()
	{
		return ownerID;
	}
	
	/**
	 * Sets the command prefix.
	 * @param prefix the command prefix
	 */
	public void setCommandPrefix(String prefix)
	{
		commandPrefix = prefix;
	}
	
	/**
	 * Gets the command prefix.
	 * @return the command prefix
	 */
	public String getCommandPrefix()
	{
		return commandPrefix;
	}
	
	/**
	 * Sets the oped role.
	 * @param role the oped role
	 */
	public void setOpedRole(String role)
	{
		opedRole = role;
	}
	
	/**
	 * Gets the oped role.
	 * @return the oped role
	 */
	public String getOpedRole()
	{
		return opedRole;
	}
	
	/**
	 * Sets the mute role.
	 * @param role the mute role
	 */
	public void setMuteRole(String role)
	{
		muteRole = role;
	}
	
	/**
	 * Sets the bot ban role.
	 * @param role the bot ban role
	 */
	public void setBotBanRole(String role)
	{
		botBanRole = role;
	}
	
	/**
	 * Sets the minigame role.
	 * @param role the minigame role
	 */
	public void setMinigameRole(String role)
	{
		minigameRole = role;
	}
	
	/**
	 * Sets the toggleable roles.
	 * @param roles the toggleable roles
	 */
	public void setToggleableRoles(String[] roles)
	{
		toggleableRoles = roles;
	}
	
	/**
	 * Sets the log channel name.
	 * @param channelName the log channel name
	 */
	public void setLogChannelName(String channelName)
	{
		logChannelName = channelName;
	}
	
	/**
	 * Sets the game servers.
	 * @param servers the game servers
	 */
	public void setServers(Server[] servers)
	{
		this.servers = servers;
	}
	
	/**
	 * Gets the game servers
	 * @return the game servers
	 */
	public Server[] getServers()
	{
		return servers;
	}
	
	/**
	 * Returns the oped role for the given server.
	 * @param g the guild to get the role from
	 * @return the oped role
	 */
	public Role getOpedRole(Guild g)
	{
		List<Role> r = g.getRolesByName(opedRole, true);
		
		// Checks if the server has a oped role with the same name as the one provided in the config.json.
		if (r.size() > 0)
		{
			// If the role does exist in the server it will be returned.
			return r.get(0);
		}
		
		// If the role does not exist in the server it will be made.
		List<Permission> perms = new ArrayList<Permission>(Arrays.asList(Permission.BAN_MEMBERS, Permission.KICK_MEMBERS,
					Permission.MESSAGE_MENTION_EVERYONE, Permission.MESSAGE_MANAGE, Permission.VIEW_AUDIT_LOGS,
					Permission.VOICE_DEAF_OTHERS, Permission.VOICE_MOVE_OTHERS, Permission.VOICE_MUTE_OTHERS));
		g.getController().createRole().setName(opedRole).setColor(new Color(52, 152, 219)).setMentionable(true).setHoisted(true).setPermissions(perms).queue();
		
		// Because it takes a hot second for JDA to create the role we must wait.
		while (r.size() < 1)
		{
			r = g.getRolesByName(opedRole, true);
		}
		
		// Once the role is finally created we will return it.
		return r.get(0);
	}
	
	/**
	 * Returns the mute role for the given server.
	 * @param g the guild to get the role from
	 * @return the mute role
	 */
	public Role getMuteRole(Guild g)
	{
		List<Role> r = g.getRolesByName(muteRole, true);
		
		// Checks if the server has a mute role with the same name as the one provided in the config.json.
		if (r.size() > 0)
		{
			// If the role does exist in the server it will be returned.
			return r.get(0);
		}
		
		// If the role does not exist in the server it will be made.
		g.getController().createRole().setName(muteRole).setColor(Color.RED).setMentionable(false).setHoisted(true).setPermissions(new ArrayList<Permission>()).queue();
		
		// Because it takes a hot second for JDA to create the role we must wait.
		while (r.size() < 1)
		{
			r = g.getRolesByName(muteRole, true);
		}
		
		// Once the role is finally created we will return it.
		return r.get(0);
	}
	
	/**
	 * Returns the bot ban role for the given server.
	 * @param g the guild to get the role from
	 * @return the bot ban role
	 */
	public Role getBotBanRole(Guild g)
	{
		List<Role> r = g.getRolesByName(botBanRole, true);
		
		// Checks if the server has a bot ban role with the same name as the one provided in the config.json.
		if (r.size() > 0)
		{
			// If the role does exist in the server it will be returned.
			return r.get(0);
		}
		
		// If the role does not exist in the server it will be made.
		g.getController().createRole().setName(botBanRole).setColor(new Color(153, 170, 181)).setMentionable(false).setHoisted(false).queue();
		
		// Because it takes a hot second for JDA to create the role we must wait.
		while (r.size() < 1)
		{
			r = g.getRolesByName(botBanRole, true);
		}
		
		// Once the role is finally created we will return it.
		return r.get(0);
	}
	
	/**
	 * Returns the minigame role for the given server.
	 * @param g the guild to get the role from
	 * @return the minigame role
	 */
	public Role getMinigameRole(Guild g)
	{
		List<Role> r = g.getRolesByName(minigameRole, true);
		
		// Checks if the server has a minigames role with the same name as the one provided in the config.json.
		if (r.size() > 0)
		{
			// If the role does exist in the server it will be returned.
			return r.get(0);
		}
		
		// If the role does not exist in the server it will be made.
		g.getController().createRole().setName(minigameRole).setColor(new Color(153, 170, 181)).setMentionable(false).setHoisted(false).setPermissions(new ArrayList<Permission>()).queue();
		
		// Because it takes a hot second for JDA to create the role we must wait.
		while (r.size() < 1)
		{
			r = g.getRolesByName(minigameRole, true);
		}
		
		// Once the role is finally created we will return it.
		return r.get(0);
	}
	
	/**
	 * Returns the toggle-able roles for the given server.
	 * @param g the guild to get the role from
	 * @return a list of toggle-able roles
	 */
	public List<Role> getToggleableRoles(Guild g)
	{
		// Create a list to store our toggle-able roles.
		List<Role> tRoles = new ArrayList<Role>();
		
		for (int i = 0; i < toggleableRoles.length; i++)
		{
			List<Role> r = g.getRolesByName(toggleableRoles[i], true);
			if (r.size() > 0)
			{
				// If the role does exist in the server it will be added to our list of toggle-able roles.
				tRoles.add(r.get(0));
			}
			else
			{
				// If the role does not exist in the server it will be made.
				g.getController().createRole().setName(toggleableRoles[i]).setColor(new Color(153, 170, 181)).setMentionable(false).setHoisted(false).queue();
				
				// Because it takes a hot second for JDA to create the role we must wait.
				while (r.size() < 1)
				{
					r = g.getRolesByName(toggleableRoles[i], true);
				}
				
				// Once the role is finally created we add it to our list of toggle-able roles.
				tRoles.add(r.get(0));
			}
		}
		
		// Now that we have got our list of toggle-able roles we will return it.
		return tRoles;
	}
	
	/**
	 * Returns the log channel for the given server.
	 * @param g the guild to get the channel from
	 * @return the log channel
	 */
	public TextChannel getLogChannel(Guild g)
	{
		List<TextChannel> c = g.getTextChannelsByName(logChannelName, true);
		
		// Checks if the server has a log channel with the same name as the provided in the Config.json.
		if (c.size() > 0)
		{
			// If the channel does exist in the server it will be returned.
			return c.get(0);
		}
		
		// If the channel does not exist in the server it will be made.
		List<Permission> opedAllow = new ArrayList<Permission>(Arrays.asList(Permission.MESSAGE_READ));
		List<Permission> opedDisallow = new ArrayList<Permission>(Arrays.asList(Permission.MESSAGE_WRITE, Permission.MESSAGE_MANAGE));
		List<Permission> everyone = new ArrayList<Permission>(Arrays.asList(Permission.VIEW_CHANNEL, Permission.MESSAGE_WRITE, Permission.MESSAGE_READ));
		g.getController().createTextChannel(logChannelName).addPermissionOverride(getOpedRole(g), opedAllow, opedDisallow).addPermissionOverride(g.getPublicRole(), new ArrayList<Permission>(), everyone).queue();
		
		// Because it takes a hot second for JDA to create the channel we must wait.
		while (c.size() < 1)
		{
			c = g.getTextChannelsByName(logChannelName, true);
		}
		
		// Once the channel is finally created we will return it.
		return c.get(0);
	}
	
	/**
	 * Checks if the provided array list of user roles matches any of the guild roles that are OPed.
	 * @param g The guild in which we are checking if the user has the role
	 * @param r an array list of user roles
	 * @return true if a any of the user roles matches any of the OPed roles in the guild, false if they don't
	 */
	public boolean checkPerms(Guild g, List<Role> r)
	{	
		for (Role userRole : r)
		{
			if (userRole.equals(getOpedRole(g)))
			{
				// If a user role matches the OPed role, true will be returned.
				return true;
			}
		}
		
		// If none of the user roles matches the OPed role, false will be returned.
		return false;
	}
}