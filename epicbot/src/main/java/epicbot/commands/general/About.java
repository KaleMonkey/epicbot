package epicbot.commands.general;

import java.io.File;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import epicbot.Epic;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.MessageEmbed;

/**
 * @author Kyle Minter (Kale Monkey)
 */
public class About extends Command
{
	public About()
	{
		this.name = "about";
		this.help = "Shows info about the bot such as the creator and github page.";
		this.category = new Category("General");
		this.guildOnly = false;
		this.botPermissions = new Permission[] {Permission.MESSAGE_WRITE};
	}
	
	public void execute(CommandEvent event)
	{
		EmbedBuilder eb = new EmbedBuilder();
		eb.setAuthor("Epic Gamer Bot", "https://github.com/KaleMonkey/epicbot");
		eb.setDescription("The Epic bot is a discord bot created for the Epic Gamers discord server. It is currently in development and is prone to bugs. If you find any bugs report them to Kale!");
		eb.addField("Author", "Kale", true);
		eb.addField("GitHub", "https://github.com/KaleMonkey/epicbot", true);
		eb.addField("Libraries", "JDA - https://github.com/DV8FromTheWorld/JDA\nJDA-Utilities - https://github.com/JDA-Applications/JDA-Utilities\nGSON - https://github.com/google/gson", true);
		File currentBuild = new File(Epic.class.getProtectionDomain().getCodeSource().getLocation().getPath());
		eb.addField("Current Build", currentBuild.getName().substring(0, currentBuild.getName().length() - 4), false);
		MessageEmbed m = eb.build();
		event.reply(m);
	}
}
