package epicbot.commands.tag;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import epicbot.Epic;
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
		try
		{
			String[] message = event.getMessage().getContentDisplay().split(" ");
			
			if (message.length > 2 || message.length == 1)
			{
				event.reply("You provided illegal arguments! Try `" + Epic.settings.getCommandPrefix() +
						"help deletetag` to get help with this command.");
			}
			else
			{
				Tag tag = Tag.getTag(new Tag(message[1]));
				
				if (tag != null)
				{
					Tag.removeTag(tag);
					event.reply("Deleted tag!");
				}
				else
				{
					event.reply("Tag " + message[1] + " does not exist!");
				}
			}
		}
		catch (Exception e)
		{
			
		}
	}
}
