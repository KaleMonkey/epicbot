package epicbot.commands;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 * @author Kyle Minter (Kale Monkey)
 */
public interface Command
{
	public static final Command[] COMMANDS = {new Help(),
			new Mute(),
			new Unmute(),
			new About(),
			new NSFW(),
			new Servers()};
	
	/**
	 * Returns the name of the command.
	 * @return the command name
	 */
	String getName();
	
	/**
	 * Returns the description of the command
	 * @return the command description
	 */
	String getDescription();
	
	/**
	 * Returns the usage instructions of the command
	 * @return the command description
	 */
	String getUsage();
	
	/**
	 * Checks if the command can be only used in a server.
	 * @return true if it can only be used in a server, false if it can be used elsewhere
	 */
	boolean GuildOnly();
	
	/**
	 * Checks to see if the message contains the necessary arguments for the command.
	 * @param event the event containing the message
	 */
	void execute(MessageReceivedEvent event);
}