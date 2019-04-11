package epicbot.commands.general;

import epicbot.Epic;
import epicbot.commands.Command;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.exceptions.InsufficientPermissionException;

public class CatFact implements Command
{
	private static final String commandName = "Catfact";
	private static final String commandDescription = "Sends a random cat fact.";
	private static final String commandUsage = "`" + Epic.settings.getCommandPrefix() + "catfact`";
	private static final boolean commandGuildOnly = false;
	
	/**
	 * Returns the name of the command.
	 * @return the command name
	 */
	public String getName()
	{
		return commandName;
	}
	
	/**
	 * Returns the description of the command
	 * @return the command description
	 */
	public String getDescription()
	{
		return commandDescription;
	}
	
	/**
	 * Returns the usage instructions of the command
	 * @return the command description
	 */
	public String getUsage()
	{
		return commandUsage;
	}
	
	/**
	 * Checks if the command can be only used in a server.
	 * @return true if it can only be used in a server, false if it can be used elsewhere
	 */
	public boolean GuildOnly()
	{
		return commandGuildOnly;
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
