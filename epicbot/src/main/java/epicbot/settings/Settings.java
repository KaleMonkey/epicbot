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
	private String nsfwRole;
	private String logChannelName;
	private Server[] servers;
	
	public void setBotToken(String token)
	{
		botToken = token;
	}
	
	public String getBotToken()
	{
		return botToken;
	}
	
	public void setOwnerID(String id)
	{
		ownerID = id;
	}
	
	public String getOwnerID()
	{
		return ownerID;
	}
	
	public void setCommandPrefix(String prefix)
	{
		commandPrefix = prefix;
	}
	
	public String getCommandPrefix()
	{
		return commandPrefix;
	}
	
	public void setOpedRole(String role)
	{
		opedRole = role;
	}
	
	public String getOpedRole()
	{
		return opedRole;
	}
	
	public void setMuteRole(String role)
	{
		muteRole = role;
	}
	
	public void setNsfwRole(String role)
	{
		nsfwRole = role;
	}
	
	public void setLogChannelName(String channelName)
	{
		logChannelName = channelName;
	}
	
	public void setServers(Server[] s)
	{
		servers = s;
	}
	
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
	 * Returns the NSFW role for the given server.
	 * @param g the guild to get the role from
	 * @return if the role exists in the server the NSFW role will be returned, if it doesn't null will be returned instead
	 */
	public Role getNsfwRole(Guild g)
	{
		List<Role> r = g.getRolesByName(nsfwRole, true);
		// Checks if the server has a mute role with the same name as the one provided in the config.json.
		if (r.size() > 0)
		{
			// If the role does exist in the server it will be returned.
			return r.get(0);
		}
		// If the role does not exist in the server null will be returned.
		return null;
	}
	
	/**
	 * Returns the log channel for the given server.
	 * @param g the guild to get the channel from
	 * @return the log channel
	 */
	public TextChannel getLogChannel(Guild g)
	{
		List<TextChannel> c = g.getTextChannelsByName(logChannelName, true);
		// Checks if the server has a log channel with the same name as the provided in the config.json.
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