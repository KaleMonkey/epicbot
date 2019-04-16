package epicbot.commands.owner;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import net.dv8tion.jda.core.Permission;

/**
 * @author Kyle Minter (Kale Monkey)
 */
public class Ping extends Command
{
	public Ping()
	{
		this.name = "ping";
		this.guildOnly = false;
		this.hidden = true;
		this.ownerCommand = true;
		this.botPermissions = new Permission[] {Permission.MESSAGE_WRITE};
	}
	
	public void execute(CommandEvent event)
	{
		event.reply("Took " + event.getJDA().getPing() + "ms to respond!");
	}
}
