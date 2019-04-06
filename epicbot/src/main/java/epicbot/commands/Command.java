package epicbot.commands;

import epicbot.commands.general.About;
import epicbot.commands.general.Help;
import epicbot.commands.general.NSFW;
import epicbot.commands.general.Ping;
import epicbot.commands.general.Servers;
import epicbot.commands.moderation.Ban;
import epicbot.commands.moderation.Kick;
import epicbot.commands.moderation.Mute;
import epicbot.commands.moderation.Unmute;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 * @author Kyle Minter (Kale Monkey)
 */
public interface Command
{
	public static final Command[] MOD_COMMANDS = {new Mute(), new Unmute(), new Ban(), new Kick()};
	public static final Command[] GENERAL_COMMANDS = {new About(), new Help(), new Ping(), new NSFW(), new Servers()};
	public static final Command[][] COMMANDS = {GENERAL_COMMANDS, MOD_COMMANDS};
	
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
	 * Attempts to execute the command.
	 * @param event the event containing the message
	 */
	void execute(MessageReceivedEvent event);
}