package epicbot.commands.tag;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import epicbot.entities.Tag;
import epicbot.settings.SettingsManager;
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
		try
		{
			String[] message = event.getMessage().getContentDisplay().split(" ");
			
			if (message.length > 2 || message.length == 1)
			{
				event.reply("You provided illegal arguments! Try `" + SettingsManager.getInstance().getSettings().getCommandPrefix() +
						"help tag` to get help with this command.");
			}
			else
			{
				Tag tag = Tag.getTag(new Tag(message[1]));
				
				if (tag != null)
				{
					event.reply(Tag.getTag(tag).getContent());
				}
				else
				{
					event.reply("Tag \"" + message[1] + "\" does not exist!");
				}
			}
		}
		catch (Exception e)
		{
			
		}
	}
}
