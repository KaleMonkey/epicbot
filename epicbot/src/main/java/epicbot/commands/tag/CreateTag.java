package epicbot.commands.tag;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import epicbot.entities.Tag;
import epicbot.settings.SettingsManager;
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
		String[] message = event.getMessage().getContentDisplay().split(" ");
		
		if (message.length < 3)
		{
			event.reply("You provided illegal arguments! Try `" + SettingsManager.getInstance().getSettings().getCommandPrefix() +
					"help createtag` to get help with this command.");
			return;
		}
		
		String tagName = message[1];
		String tagContent = "";
		for (int i = 2; i < message.length; i++)
		{
			tagContent += message[i] + " ";
		}
		
		Tag tag = new Tag(tagName, tagContent);
		if (Tag.getTag(tag) == null)
		{
			Tag.addTag(tag);
			event.reply("Created tag!");
		}
		else
		{
			event.reply("The tag name must be unique!");
		}
	}
}
