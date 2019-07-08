package epicbot.commands.general;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import epicbot.commands.moderation.BotBan;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.ChannelType;

/**
 * @author Kyle Minter (Kale Monkey)
 */
public class BruhMoment extends Command
{
	public BruhMoment()
	{
		this.name = "bruhmoment";
		this.help = "Marks a bruh moment in chat.";
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
			event.reply("Now this is a bruh moment!");
		}
		else
		{
			event.reply("This command does not have any arguments!" + Help.getHelp(this.name));
		}
	}
}
