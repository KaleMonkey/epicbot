package epicbot.commands.general;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import epicbot.commands.moderation.BotBan;
import epicbot.settings.SettingsManager;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.ChannelType;

/**
 * @author Kyle Minter (Kale Monkey)
 */
public class Gay extends Command
{
	public Gay()
	{
		this.name = "gay";
		this.help = "Gives a percentage of how gay a user is.";
		this.category = new Category("General");
		this.guildOnly = false;
		this.botPermissions = new Permission[] {Permission.MESSAGE_WRITE};
	}
	
	public void execute(CommandEvent event)
	{
		if (event.getChannelType() == ChannelType.TEXT && BotBan.isBotBanned(event.getMember()))
		{
			event.reply("You are bot banned on this server! You must be unbanned to use any of my commands.");
		}
		else if (event.getArgs().equals(""))
		{
			if (event.getAuthor().getId().equals(SettingsManager.getInstance().getSettings().getOwnerID()))
			{
				event.reply(event.getAuthor().getAsMention() + " is 0% gay.");
			}
			else
			{
				int percentage = (int)(Math.random() * 101);
				event.reply(event.getAuthor().getAsMention() + " is " + percentage + "% gay.");
			}
		}
		else
		{
			event.reply("This command does not have any arguments!" + Help.getHelp(this.name));
		}
	}
}
