package epicbot.commands.owner;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import net.dv8tion.jda.core.Permission;

/**
 * @author Kyle Minter (Kale Monkey)
 */
public class Shutdown extends Command
{
	public Shutdown()
	{
		this.name = "shutdown";
		this.guildOnly = false;
		this.hidden = true;
		this.ownerCommand = true;
		this.botPermissions = new Permission[] {Permission.MESSAGE_WRITE};
	}
	
	public void execute(CommandEvent event)
	{
		event.reply("Shutting down!");
		System.exit(0);
	}
}
