package epicbot.util;

/**
 * @author Kyle Minter (Kale Monkey)
 */
public class Settings
{
	private String botToken;
	private String commandPrefix;
	private String opedRole;
	private String muteRole;
	private String nsfwRole;
	private String logChannelName;
	
	public String getBotToken()
	{
		return botToken;
	}
	
	public void setBotToken(String token)
	{
		botToken = token;
	}
	
	public String getCommandPrefix()
	{
		return commandPrefix;
	}
	
	public void setCommandPrefix(String prefix)
	{
		commandPrefix = prefix;
	}
	
	public String getOpedRole()
	{
		return opedRole;
	}
	
	public void setOpedRole(String role)
	{
		opedRole = role;
	}
	
	public String getMuteRole()
	{
		return muteRole;
	}
	
	public void setMuteRole(String role)
	{
		muteRole = role;
	}
	
	public String getNsfwRole()
	{
		return nsfwRole;
	}
	
	public void setNsfwRole(String role)
	{
		nsfwRole = role;
	}
	
	public String getLogChannelName()
	{
		return logChannelName;
	}
	
	public void setLogChannelName(String channelName)
	{
		logChannelName = channelName;
	}
}
