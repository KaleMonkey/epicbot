package epicbot.commands.general;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import epicbot.entities.BotBan;
import net.dv8tion.jda.core.Permission;

/**
 * @author Kyle Minter (Kale Monkey)
 */
public class EightBall extends Command
{
	public EightBall()
	{
		this.name = "8ball";
		this.help = "Shakes the 8ball.";
		this.category = new Category("General");
		this.guildOnly = false;
		this.botPermissions = new Permission[] {Permission.MESSAGE_WRITE};
	}
	
	public void execute(CommandEvent event)
	{
		if (BotBan.isBotBanned(event.getMember()))
		{
			event.reply("You are bot banned on this server! You must be unbanned to use any of my commands.");
			return;
		}
		
		// Array of responses from an 8ball.
		String[] responses = {"It is certain.", "It is decidedly so.", "Without a doubt.", "Yes - definitely.", "You may rely on it.", "As I see it, yes.", "Most likely.", "Outlook good.",
				"Yes.", "Signs point to yes.", "Don't count on it.","My reply is no.", "My sources say no.", "Outlook not so good.", "Very doubtful."};
		
		// Sends random response.
		event.reply(responses[(int)(Math.random() * responses.length)]);
	}
}