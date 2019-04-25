package epicbot.commands.tag;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import epicbot.commands.general.Help;
import epicbot.entities.Tag;
import net.dv8tion.jda.core.Permission;

public class CreateTag extends Command
{
	public CreateTag()
	{
		this.name = "createtag";
		this.help = "Creates a tag with a given name and content.";
		this.arguments = "[name] [content]";
		this.category = new Category("Tag");
		this.guildOnly = false;
		this.botPermissions = new Permission[] {Permission.MESSAGE_WRITE};
	}
	
	public void execute(CommandEvent event)
	{
		String[] args = event.getArgs().split(" ");
		
		if (args.length < 2)
		{
			event.reply("You did not provide the necessary arguments for this command!" + Help.getHelp(this.name));
			return;
		}
		
		String tagName = args[0];
		String tagContent = "";
		for (int i = 1; i < args.length; i++)
		{
			tagContent += args[i] + " ";
		}
		
		Tag tag = new Tag(tagName, tagContent, event.getAuthor());
		if (Tag.getTag(tag) == null)
		{
			Tag.addTag(tag);
			event.reply("Created tag!");
		}
		else
		{
			event.reply("The tag name must be unique!" + Help.getHelp(this.name));
		}
	}
}
