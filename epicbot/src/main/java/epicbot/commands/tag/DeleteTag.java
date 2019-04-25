package epicbot.commands.tag;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import epicbot.commands.general.Help;
import epicbot.entities.Tag;
import net.dv8tion.jda.core.Permission;

public class DeleteTag extends Command
{
	public DeleteTag()
	{
		this.name = "deletetag";
		this.help = "Deletes the specified tag.";
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
			event.reply("You did not provide the name of the tag you want to delete!" + Help.getHelp(this.name));
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
				Tag.removeTag(tag);
				event.reply("Deleted tag!");
			}
			else
			{
				event.reply("Tag " + args[0] + " does not exist!");
			}
		}
	}
}
