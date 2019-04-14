package epicbot.commands.general;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.exceptions.InsufficientPermissionException;

public class CatFact extends Command
{
	public CatFact()
	{
		this.name = "catfact";
		this.aliases = new String[] {"hitthisniggawithacatfact"};
		this.help = "Sends a random cat fact.";
		this.category = new Category("General");
		this.guildOnly= false;
		this.botPermissions = new Permission[] {Permission.MESSAGE_WRITE};
	}
	
	public void execute(CommandEvent event)
	{
		String[] facts = {"Fact 35: The ancient Egyptians were the first to tame the cat in about 3000 BC.",
				"Fact 69: Cats share 95.6% of their DNA with tigers.",
				"Fact 101: Cats are asleep for 70% of their lives.",
				"Fact 256: A group of cats is called a \"clowder.\"",
				"Fact 386: The first cat in space was a French cat named Felicette (a.k.a. “Astrocat”) In 1963, France blasted the cat into outer space. Electrodes implanted in her brains sent neurological signals back to Earth. She survived the trip.",
				"Fact 420: A cat can jump up to five times its own height in a single bound.",
				"Fact 763: Cat kidneys are super efficient, they can rehydrate by drinking seawater.",
				"Fact 972: When a cat chases its prey, it keeps its head level. Dogs and humans bob their heads up and down."};
		
		EmbedBuilder eb = new EmbedBuilder();
		eb.setAuthor("Epic Gamer Bot", "https://github.com/KaleMonkey/epicbot");
		eb.setTitle("Cat Fact");
		eb.setDescription(facts[(int)(Math.random() * facts.length)]);
		event.reply(eb.build());
	}
	
	/**
	 * Attempts to execute the command.
	 * @param event the event containing the message
	 */
	public void execute(MessageReceivedEvent event)
	{
		// Since this command doesn't have any arguments we will just check that "about" is the only thing sent in the message.
		if (event.getMessage().getContentRaw().substring(1).equalsIgnoreCase("catfact"))
		{
			try
			{
				String[] facts = {"Fact 35: The ancient Egyptians were the first to tame the cat in about 3000 BC.",
						"Fact 69: Cats share 95.6% of their DNA with tigers.",
						"Fact 101: Cats are asleep for 70% of their lives.",
						"Fact 256: A group of cats is called a \"clowder.\"",
						"Fact 386: The first cat in space was a French cat named Felicette (a.k.a. “Astrocat”) In 1963, France blasted the cat into outer space. Electrodes implanted in her brains sent neurological signals back to Earth. She survived the trip.",
						"Fact 420: A cat can jump up to five times its own height in a single bound.",
						"Fact 763: Cat kidneys are super efficient, they can rehydrate by drinking seawater.",
						"Fact 972: When a cat chases its prey, it keeps its head level. Dogs and humans bob their heads up and down."};
				
				EmbedBuilder eb = new EmbedBuilder();
				eb.setAuthor("Epic Gamer Bot", "https://github.com/KaleMonkey/epicbot");
				eb.setTitle("Cat Fact");
				eb.setDescription(facts[(int)(Math.random() * facts.length)]);
				event.getChannel().sendMessage(eb.build()).queue();
			}
			catch (InsufficientPermissionException e)
			{
				System.out.println("\n[Epic]: Unable to send messages in channel \"" + event.getChannel().getName() + "\"!");
				System.out.println("[Epic]: Please give the bot permissions!");
			}
		}
	}
}
