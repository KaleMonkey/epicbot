package epicbot.commands.general;

import java.util.ArrayList;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import epicbot.Epic;
import epicbot.entities.Tag;
import epicbot.settings.SettingsManager;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;

public class TagCMD extends Command
{
	public TagCMD()
	{
		this.name = "tag";
		this.aliases = new String[] {"t"};
		this.help = "Sends a tag's content, creates a tag with a given name and content., deletes a tag with a given name, "
				+ "renames a tag with a given name, sends a tag's details, or lists all known tags depending on argument [cmd].\n"
				+ "If [cmd] is left empty a given tag's content will be sent.\nIf [cmd] equals \"add\" a tag will be created with a given name and content.\n"
				+ "If [cmd] equals \"delete\" a given tag will be deleted.\n"
				+ "If [cmd] equals \"rename\" a tag will be renamed with a given name.\n"
				+ "If [cmd] equals \"details\" a given tag's details will be sent\n"
				+ "If [cmd] equals \"all\" all known tags will be listed.";
		this.arguments = "[cmd]";
		this.category = new Category("General");
		this.guildOnly = false;
		this.botPermissions = new Permission[] {Permission.MESSAGE_WRITE};
	}
	
	public void execute(CommandEvent event)
	{
		String[] args = event.getArgs().split(" ");
		
		// find other cmds
		
		if (args[0].equalsIgnoreCase("add"))
		{
			// create tag
			createTag(event);
		}
		else if (args[0].equalsIgnoreCase("delete"))
		{
			// delete tag
			deleteTag(event);
		}
		else if (args[0].equalsIgnoreCase("rename"))
		{
			// rename tag
			renameTag(event);
			
		}
		else if (args[0].equalsIgnoreCase("details"))
		{
			// details tag
			getTagDetails(event);
		}
		else if (args[0].equalsIgnoreCase("all"))
		{
			// all tags
			getAllTags(event);
		}
		else
		{
			// default to get tag
			getTag(event);
		}
	}
	
	private void getTag(CommandEvent event)
	{
		String[] args = event.getArgs().split(" ");
		
		if (args[0].equals(""))
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
	
	private void createTag(CommandEvent event)
	{
		String[] args = event.getArgs().split(" ");
		
		if (args.length < 3)
		{
			event.reply("You did not provide the necessary arguments for this command!" + Help.getHelp(this.name));
			return;
		}
		
		String tagName = args[1];
		String tagContent = "";
		for (int i = 2; i < args.length; i++)
		{
			tagContent += args[i] + " ";
		}
		
		Tag tag = new Tag(tagName, tagContent, event.getAuthor().getIdLong());
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
	
	private void deleteTag(CommandEvent event)
	{
		String[] args = event.getArgs().split(" ");
		
		if (args.length < 2)
		{
			event.reply("You did not provide the name of the tag you want to delete!" + Help.getHelp(this.name));
		}
		else if (args.length > 2)
		{
			event.reply("You provided too many arguments for this command!" + Help.getHelp(this.name));
		}
		else
		{
			Tag tag = Tag.getTag(new Tag(args[1]));
			
			if (tag != null)
			{
				if (tag.getAuthorId() == event.getAuthor().getIdLong() || event.getAuthor().getId().equals(SettingsManager.getInstance().getSettings().getOwnerID()))
				{
					Tag.removeTag(tag);
					event.reply("Deleted tag!");
				}
				else
				{
					event.reply("You do not own this tag!");
				}
			}
			else
			{
				event.reply("Tag " + args[1] + " does not exist!");
			}
		}
	}
	
	private void getTagDetails(CommandEvent event)
	{
		String[] args = event.getArgs().split(" ");
		
		if (args.length < 2)
		{
			event.reply("You did not provide a tag name!" + Help.getHelp(this.name));
		}
		else if (args.length > 2)
		{
			event.reply("You provided too many arguments for this command!" + Help.getHelp(this.name));
		}
		else
		{
			Tag tag = Tag.getTag(new Tag(args[1]));
			
			if (tag == null)
			{
				event.reply("Tag \"" + args[0] + "\" does not exist!");
			}
			else
			{
				EmbedBuilder eb = new EmbedBuilder();
				eb.setAuthor("Epic Gamer Bot", "https://github.com/KaleMonkey/epicbot");
				eb.addField(tag.getName(), "Author: " + Epic.getAPI().getUserById(tag.getAuthorId()).getName() + "\nDate Created: " + tag.getDate() + "\nTimes Used: " + tag.getUses() + "\nContent: " + tag.getContent(), false);
				event.reply(eb.build());
			}
		}
	}
	
	private void renameTag(CommandEvent event)
	{
		String[] args = event.getArgs().split(" ");
		if (args.length < 2)
		{
			event.reply("You did not provide a tag name to rename!" + Help.getHelp(this.name));
		}
		else if (args.length < 3)
		{
			event.reply("You did not provide a name to rename the tag with!" + Help.getHelp(this.name));
		}
		else if (args.length > 3)
		{
			event.reply("You provided too many arguments for this command!" + Help.getHelp(this.name));
		}
		else
		{
			Tag tag = Tag.getTag(new Tag(args[1]));
			
			if (tag == null)
			{
				event.reply("Tag " + args[1] + " does not exist!");
			}
			else
			{
				tag.setName(args[2]);
				event.reply("Tag " + args[1] + " has been renamed to " + args[2] + "!");
			}
		}
	}
	
	private void getAllTags(CommandEvent event)
	{
		String[] args = event.getArgs().split(" ");
		
		if (args.length > 1)
		{
			event.reply("You provided too many arguments for this command!" + Help.getHelp(this.name));
		}
		else
		{
			EmbedBuilder eb = new EmbedBuilder();
			eb.setAuthor("Epic Gamer Bot", "https://github.com/KaleMonkey/epicbot");
			eb.setTitle("Tags");
			ArrayList<Tag> tags = Tag.getAllTags();
			if (tags.size() > 0)
			{
				for (Tag tag : tags)
				{
					eb.addField(tag.getName(), "Author: " + Epic.getAPI().getUserById(tag.getAuthorId()).getName() + "\nDate Created: " + tag.getDate() + "\nTimes Used: " + tag.getUses() + "\nContent: " + tag.getContent(), false);
				}
			}
			else
			{
				eb.setDescription("There are currently no tags.");
			}
			
			// Opens a private channel with the user and sends the embedded message.
			event.getAuthor().openPrivateChannel().queue((channel) ->
			{
				channel.sendMessage(eb.build()).queue();
			});
		}
	}
}
