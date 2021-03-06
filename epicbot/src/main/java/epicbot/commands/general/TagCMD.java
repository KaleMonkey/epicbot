package epicbot.commands.general;

import java.util.ArrayList;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import epicbot.Epic;
import epicbot.commands.moderation.BotBan;
import epicbot.entities.Tag;
import epicbot.settings.SettingsManager;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.ChannelType;

public class TagCMD extends Command
{
	public TagCMD()
	{
		this.name = "tag";
		this.aliases = new String[] {"t"};
		this.help = "This command is used for getting, creating, deleting, renaming, listing, or getting a tag's details.\n"
				+ "To get a tag's content do `" + SettingsManager.getInstance().getSettings().getCommandPrefix() + "tag [tag name]`.\n"
				+ "To create a tag do `" + SettingsManager.getInstance().getSettings().getCommandPrefix() + "tag add [tag name] [tag content]`.\n"
				+ "To delete a tag do `" + SettingsManager.getInstance().getSettings().getCommandPrefix() + "tag delete [tag name]`.\n"
				+ "To rename a tag do `" + SettingsManager.getInstance().getSettings().getCommandPrefix() + "tag rename [tag name]`.\n"
				+ "To list all tags do `" + SettingsManager.getInstance().getSettings().getCommandPrefix() + "tag all`.\n"
				+ "To get a tag's details do `" + SettingsManager.getInstance().getSettings().getCommandPrefix() + "tag details [tag name]`.";
		this.arguments = "[sub command]";
		this.category = new Category("General");
		this.guildOnly = false;
		this.botPermissions = new Permission[] {Permission.MESSAGE_WRITE};
	}
	
	public void execute(CommandEvent event)
	{
		if (event.getChannelType() == ChannelType.TEXT && BotBan.isBotBanned(event.getMember()))
		{
			event.reply("You are bot banned on this server! You must be unbanned to use any of my commands.");
			return;
		}
		
		// Puts the arguments into an array.
		String[] args = event.getArgs().split(" ");
		
		// Finds out if a sub command is being called.
		switch (args[0])
		{
			case "add":
				// If the first argument equals "add" the create tag command will be called.
				createTag(event);
				break;
			case "delete":
				// If the first argument equals "delete" the delete tag command will be called.
				deleteTag(event);
				break;
			case "rename":
				// If the first argument equals "rename" the rename tag command will be called.
				renameTag(event);
				break;
			case "details":
				// If the first argument equals "details" the get tag details command will be called.
				getTagDetails(event);
				break;
			case "all":
				// If the first argument equals "all" the get all tags command will be called.
				getAllTags(event);
				break;
			default:
				// If the first argument doesn't equal any of the sub commands the bot will default to the get tag command.
				getTag(event);
		}
	}
	
	private void getTag(CommandEvent event)
	{
		// Puts the arguments into an array.
		String[] args = event.getArgs().split(" ");
		
		if (args[0].equals(""))
		{
			// If there are no arguments are provided an error message will be sent.
			event.reply("You did not provide a tag name!" + Help.getHelp(this.name));
		}
		else if (args.length > 1)
		{
			// If there are more than one argument provided an error message will be sent.
			event.reply("You provided too many arguments for this command!" + Help.getHelp(this.name));
		}
		else
		{
			// Gets the tag object with the given name.
			Tag tag = Tag.getTag(new Tag(args[0]));
			
			if (tag != null)
			{
				// If the tag exists the content will be sent.
				event.reply(Tag.getTag(tag).getContent());
			}
			else
			{
				// If the tag doesn't exist an error message will be sent.
				event.reply("Tag \"" + args[0] + "\" does not exist!");
			}
		}
	}
	
	private void createTag(CommandEvent event)
	{
		// Puts the arguments into an array.
		String[] args = event.getArgs().split(" ");
		
		// If there are less than 3 arguments an error message will be sent.
		if (args.length < 3)
		{
			event.reply("You did not provide the necessary arguments for this command!" + Help.getHelp(this.name));
			return;
		}
		
		// Puts the name and content of the tag the user wants to make into variables.
		String tagName = args[1];
		String tagContent = "";
		for (int i = 2; i < args.length; i++)
		{
			tagContent += args[i] + " ";
		}
		
		// Creates a tag object with the given name, content, and author id.
		Tag tag = new Tag(tagName, tagContent, event.getAuthor().getIdLong());
		if (Tag.getTag(tag) == null)
		{
			// If there isn't already a tag with the given name it will be added to the list of tags.
			Tag.addTag(tag);
			event.reply("Created tag!");
		}
		else
		{
			// If there is already a tag with the given name an error message will be sent.
			event.reply("The tag name must be unique!" + Help.getHelp(this.name));
		}
	}
	
	private void deleteTag(CommandEvent event)
	{
		// Puts the arguments into an array.
		String[] args = event.getArgs().split(" ");
		
		// If there is less than 2 arguments an error message will be sent.
		if (args.length < 2)
		{
			event.reply("You did not provide the name of the tag you want to delete!" + Help.getHelp(this.name));
		}
		// If there are more than 2 arguments an error message will be sent.
		else if (args.length > 2)
		{
			event.reply("You provided too many arguments for this command!" + Help.getHelp(this.name));
		}
		else
		{
			// Gets the tag with the given name.
			Tag tag = Tag.getTag(new Tag(args[1]));
			
			if (tag != null)
			{
				// If a tag with the given name exists we will check if the user is the author of the tag.
				if (tag.getAuthorId() == event.getAuthor().getIdLong() || event.getAuthor().getId().equals(SettingsManager.getInstance().getSettings().getOwnerID()))
				{
					// If the user is the author the tag will be deleted.
					Tag.removeTag(tag);
					event.reply("Deleted tag!");
				}
				else
				{
					// If the user is not the author an error message will be sent.
					event.reply("You do not own this tag!");
				}
			}
			else
			{
				// If a tag with the given name does not exist an error message will be sent.
				event.reply("Tag " + args[1] + " does not exist!");
			}
		}
	}
	
	private void getTagDetails(CommandEvent event)
	{
		// Puts the arguments into an array.
		String[] args = event.getArgs().split(" ");
		
		// If there is less than 2 arguments an error message will be sent.
		if (args.length < 2)
		{
			event.reply("You did not provide a tag name!" + Help.getHelp(this.name));
		}
		// If there are more than 2 arguments an error message will be sent.
		else if (args.length > 2)
		{
			event.reply("You provided too many arguments for this command!" + Help.getHelp(this.name));
		}
		else
		{
			// Gets the tag with the given name.
			Tag tag = Tag.getTag(new Tag(args[1]));
			
			if (tag == null)
			{
				// If the tag with the given name does not exist an error message will be sent.
				event.reply("Tag \"" + args[0] + "\" does not exist!");
			}
			else
			{
				// If the tag with the given name does exist the tag details will be sent.
				EmbedBuilder eb = new EmbedBuilder();
				eb.setAuthor("Epic", "https://github.com/KaleMonkey/epicbot");
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
		// Puts the arguments into an array.
		String[] args = event.getArgs().split(" ");
		
		// If there is more than 1 argument an error message will be sent.
		if (args.length > 1)
		{
			event.reply("You provided too many arguments for this command!" + Help.getHelp(this.name));
		}
		else
		{
			// Starts building a embedded message for the tags.
			EmbedBuilder eb = new EmbedBuilder();
			eb.setAuthor("Epic", "https://github.com/KaleMonkey/epicbot");
			eb.setTitle("Tags");
			
			// Gets all of the tags.
			ArrayList<Tag> tags = Tag.getAllTags();
			
			if (tags.size() > 0)
			{
				// If there is at least 1 tag every tag's details will be added to the list.
				for (Tag tag : tags)
				{
					eb.addField(tag.getName(), "Author: " + Epic.getAPI().getUserById(tag.getAuthorId()).getName() + "\nDate Created: " + tag.getDate() + "\nTimes Used: " + tag.getUses() + "\nContent: " + tag.getContent(), false);
				}
			}
			else
			{
				// If there are no tags a message will be added to the embedded message.
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