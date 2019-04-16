package epicbot.commands.owner;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import epicbot.Epic;
import net.dv8tion.jda.core.Permission;

/**
 * @author Kyle Minter (Kale Monkey)
 */
public class Restart extends Command
{
	public Restart()
	{
		this.name = "restart";
		this.guildOnly = false;
		this.hidden = true;
		this.ownerCommand = true;
		this.botPermissions = new Permission[] {Permission.MESSAGE_WRITE};
	}
	
	public void execute(CommandEvent event)
	{
		event.reply("Restarting JDA!");
		Epic.getAPI().shutdownNow();
		Epic.setupBot();
	}
}
