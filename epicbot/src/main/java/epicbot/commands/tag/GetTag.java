package epicbot.commands.tag;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import epicbot.commands.general.Help;
import epicbot.entities.Tag;
import net.dv8tion.jda.core.Permission;

public class GetTag extends Command
{
	public GetTag()
	{
		this.name = "tag";
		this.help = "Sends the content of the specified tag.";
		this.arguments = "[name]";
		this.category = new Category("Tag");
		this.guildOnly = false;
		this.botPermissions = new Permission[] {Permission.MESSAGE_WRITE};
	}
	
	public void execute(CommandEvent event)
	{
		String[] args = event.getArgs().split(" ");
		
		if (event.getArgs().equals(""))
		{
			event.reply("You did not provide a tag name!" + Help.getHelp(this.name));
		}
		else if (args.length > 1)
		{
			event.reply("You provided too many arguments for this command!" + Help.getHelp(this.name));
		}
		else
		{
			Tag tag = Tag.getTag(new Tag(args[0]));
			
			if (tag != null)
			{
				event.reply(Tag.getTag(tag).getContent());
			}
			else
			{
				event.reply("Tag \"" + args[0] + "\" does not exist!");
			}
		}
	}
}
