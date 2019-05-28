package epicbot.commands.owner;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import epicbot.Epic;
import epicbot.commands.general.Help;
import epicbot.settings.SettingsManager;
import net.dv8tion.jda.core.Permission;

/**
 * @author Kyle Minter (Kale Monkey)
 */
public class ReloadConfig extends Command
{
	public ReloadConfig()
	{
		this.name = "reloadconfig";
		this.guildOnly = false;
		this.hidden = true;
		this.ownerCommand = true;
		this.botPermissions = new Permission[] {Permission.MESSAGE_WRITE};
	}
	
	public void execute(CommandEvent event)
	{
		if (event.getArgs().equals(""))
		{
			event.reply("Reloading config!");
			SettingsManager.getInstance().loadSettings();
			Epic.getAPI().shutdownNow();
			Epic.setupBot();
		}
		else
		{
			event.reply("This command does not have any arguments!" + Help.getHelp(this.name));
		}
	}
}
