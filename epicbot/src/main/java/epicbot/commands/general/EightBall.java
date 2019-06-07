package epicbot.commands.general;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import net.dv8tion.jda.core.Permission;

/**
 * @author Kyle Minter (Kale Monkey)
 */
public class EightBall extends Command
{
	public EightBall()
	{
		this.name = "8ball";
		this.help = "Gives a percentage of how gay a user is.";
		this.category = new Category("General");
		this.guildOnly = false;
		this.botPermissions = new Permission[] {Permission.MESSAGE_WRITE};
	}
	
	public void execute(CommandEvent event)
	{
		// Array of responses from an 8ball.
		String[] responses = {"It is certain.", "It is decidedly so.", "Without a doubt.", "Yes - definitely.", "You may rely on it.", "As I see it, yes.", "Most likely.", "Outlook good.",
				"Yes.", "Signs point to yes.", "Reply hazy, try again.", "Ask again later.", "Better not tell you now.", "Cannot predict now.", "Concentrate and ask again.",
				"Don't count on it.","My reply is no.", "My sources say no.", "Outlook not so good.", "Very doubtful."};
		
		// Sends random response.
		event.reply(responses[(int)(Math.random() * responses.length)]);
	}
}
