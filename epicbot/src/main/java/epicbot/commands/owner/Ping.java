package epicbot.commands.owner;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import epicbot.commands.general.Help;
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
		if (event.getArgs().equals(""))
		{
			event.reply("Took " + event.getJDA().getPing() + "ms to respond!");	
		}
		else
		{
			event.reply("This command does not have any arguments!" + Help.getHelp(this.name));
		}
	}
}
