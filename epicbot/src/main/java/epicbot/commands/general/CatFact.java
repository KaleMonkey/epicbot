package epicbot.commands.general;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import epicbot.commands.moderation.BotBan;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.ChannelType;

public class CatFact extends Command
{
	String[] facts = {"Fact 35: The ancient Egyptians were the first to tame the cat in about 3000 BC.",
			"Fact 1: Cats share 95.6% of their DNA with tigers.",
			"Fact 2: Cats are asleep for 70% of their lives.",
			"Fact 3: A group of cats is called a \"clowder.\"",
			"Fact 4: The first cat in space was a French cat named Felicette (a.k.a. ï¿½Astrocatï¿½) In 1963, France blasted the cat into outer space. Electrodes implanted in her brains sent neurological signals back to Earth. She survived the trip.",
			"Fact 5: Cat kidneys are super efficient, they can rehydrate by drinking seawater.",
			"Fact 6: When a cat chases its prey, it keeps its head level. Dogs and humans bob their heads up and down.",
			"Fact 7: Unlike humans, cats cannot detect sweetness-which likely explains why they are not drawn to it at all.",
			"Fact 8: A cat has the power to sometimes heal themselves by purring. A domestic cat's purr has a frequency of between 25 and 150 Hz, which happens to be the frequency at which muscles and bones best grow and repair themselves.",
			"Fact 9: Cats only use their meows to talk to humans, not each other. The only time they meow to communicate with other felines is when they are kittens to signal to their mother.",
			"Fact 10: Despite imagery of cats happily drinking milk from saucers, studies indicate that cats are actually lactose intolerant and should avoid it entirely.",
			"Fact 11: A cat’s cerebral cortex contains about twice as many neurons as that of dogs. Cats have 300 million neurons, whereas dogs have about 160 million.",
			"Fact 12: The average cat can jump 8 feet in a single bound–nearly six times its body length!",
			"Fact 13: A cat’s smell is their strongest sense, and they rely on this leading sense to identify people and objects; a feline’s sense of smell is 14x better than a human’s.",
			"Fact 14: Cats only sweat through their paws and nowhere else on their body.",
			"Fact 15: A cat only has the ability to move their jaw up and down, not side to side like a human can.",
			"Fact 16: A cat has the ability to rotate their ears 180 degrees–with the help of 32 muscles that they use to control them.",
			"Fact 17: 70% of your cat’s life is spent asleep.",
			"Fact 18: A cat’s nose is as unique as a human’s fingerprint.",
			"Fact 19: Cats have 3 eyelids.",
			"Fact 20: Your cat’s heart beats at a rate almost double that of yours, from 110-140 beats per minute.",
			"Fact 21: Owning a cat is actually proven to be beneficial for your health.",
			"Fact 22: Cats prefer to remain non-confrontational. They will not fight to show dominance, but rather to stake their territory. Cats will actually go to extremes to avoid one another in order to prevent a possible confrontation.",
			"Fact 23: Unlike humans, cats are usually lefties. Studies indicate that their left paw is typically their dominant paw.",
			"Fact 24: Some Siamese cats appear cross-eyed because the nerves from the left side of the brain go to mostly the right eye and the nerves from the right side of the brain go mostly to the left eye. This causes some double vision, which the cat tries to correct by “crossing” its eyes.",
			"Fact 25: Cats hate the water because their fur does not insulate well when it’s wet. The Turkish Van, however, is one cat that likes swimming. Bred in central Asia, its coat has a unique texture that makes it water resistant.",
			"Fact 26: A cat usually has about 12 whiskers on each side of its face.",
			"Fact 27: A cat’s eyesight is both better and worse than humans. It is better because cats can see in much dimmer light and they have a wider peripheral view. It’s worse because they don’t see color as well as humans do. Scientists believe grass appears red to cats.",
			"Fact 28: On average, cats spend 2/3 of every day sleeping. That means a nine-year-old cat has been awake for only three years of its life.",
			"Fact 29: The ability of a cat to find its way home is called “psi-traveling.” Experts think cats either use the angle of the sunlight to find their way or that cats have magnetized cells in their brains that act as compasses.",
			"Fact 30: Female cats tend to be right pawed, while male cats are more often left pawed. Interestingly, while 90% of humans are right handed, the remaining 10% of lefties also tend to be male.",
			"Fact 31: A cat’s back is extremely flexible because it has up to 53 loosely fitting vertebrae. Humans only have 34.",
			"Fact 32: One reason that kittens sleep so much is because a growth hormone is released only during sleep.",
			"Fact 33: Approximately 1/3 of cat owners think their pets are able to read their minds.",
			"Fact 34: The normal body temperature of a cat is between 100.5 ° and 102.5 °F. A cat is sick if its temperature goes below 100 ° or above 103 °F.",
			"Fact 35: A cat has 230 bones in its body. A human has 206. A cat has no collarbone, so it can fit through any opening the size of its head.",
			"Fact 36: In just seven years, a single pair of cats and their offspring could produce a staggering total of 420,000 kittens.",
			"Fact 37: Cats spend nearly 1/3 of their waking hours cleaning themselves.",
			"Fact 38: Grown cats have 30 teeth. Kittens have about 26 temporary teeth, which they lose when they are about 6 months old.",
			"Fact 39: Cats are extremely sensitive to vibrations. Cats are said to detect earthquake tremors 10 or 15 minutes before humans can.",
			"Fact 40: In contrast to dogs, cats have not undergone major changes during their domestication process.",
			"Fact 41: In Holland’s embassy in Moscow, Russia, the staff noticed that the two Siamese cats kept meowing and clawing at the walls of the building. Their owners finally investigated, thinking they would find mice. Instead, they discovered microphones hidden by Russian spies. The cats heard the microphones when they turned on.",
			"Fact 42: The cat who holds the record for the longest non-fatal fall is Andy. He fell from the 16th floor of an apartment building (about 200 ft/.06 km) and survived.",
			"Fact 43: The claws on the cat’s back paws aren’t as sharp as the claws on the front paws because the claws in the back don’t retract and, consequently, become worn.",
			"Fact 45: Cats can drink seawater.",
			"Fact 46: In terms of development, the first year of a cat’s life is equal to the first 15 years of a human life. After its second year, a cat is 25 in human years. And after that, each year of a cat’s life is equal to about 7 human years.",
			"Fact 47: A cat cannot see directly under its nose.",
			"Fact 48: Most cats have no eyelashes.",
			"Fact 49: Cats have five toes on each front paw, but only four on the back ones. It’s not uncommon, though, for cats to have extra toes. The cat with the most toes known had 32—eight on each paw!",
			"Fact 50: Meows are not innate cat language—they developed them to communicate with humans!"};
	
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
		if (event.getChannelType() == ChannelType.TEXT && BotBan.isBotBanned(event.getMember()))
		{
			event.reply("You are bot banned on this server! You must be unbanned to use any of my commands.");
		}
		else if (event.getArgs().equals(""))
		{
			EmbedBuilder eb = new EmbedBuilder();
			eb.setAuthor("Epic", "https://github.com/KaleMonkey/epicbot");
			eb.setTitle("Cat Fact");
			eb.setDescription(facts[(int)(Math.random() * facts.length)]);
			event.reply(eb.build());
		}
		else
		{
			event.reply("This command does not have any arguments!" + Help.getHelp(this.name));
		}
	}
}
