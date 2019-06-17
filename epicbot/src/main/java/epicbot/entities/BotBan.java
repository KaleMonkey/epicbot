package epicbot.entities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Path;
import java.util.ArrayList;

import net.dv8tion.jda.core.entities.Member;

public class BotBan implements Serializable
{
	private static final long serialVersionUID = 1;
	private static final Path bansFile = new File(".").toPath().resolve("BotBans.ser");
	private static ArrayList<BotBan> botBans = new ArrayList<BotBan>();
	private long userId;
	private long guildId;
	
	/** Constructs a BotBan object with a given member.
	 * @param member the member to BotBan
	 */
	public BotBan (Member member)
	{
		userId = member.getUser().getIdLong();
		guildId = member.getGuild().getIdLong();
	}
	
	/**
	 * Returns the user id of the BotBan.
	 * @return the user id
	 */
	public long getUserId()
	{
		return userId;
	}
	
	/**
	 * Returns the guild id of the BotBan.
	 * @return the guild id
	 */
	public long getGuildId()
	{
		return guildId;
	}
	
	/**
	 * Adds a BotBan to the bot bans list.
	 * @param member the member to BotBan
	 */
	public static void addBotBan(Member member)
	{
		botBans.add(new BotBan(member));
		saveBotBans();
	}
	
	/**
	 * Removes a BotBan from the bot bans list.
	 * @param member the member that is BotBanned
	 */
	public static void removeBotBan(Member member)
	{
		botBans.remove(new BotBan(member));
		saveBotBans();
	}
	
	/**
	 * Checks if a member is bot banned.
	 * @param member the member to check
	 * @return true if the member is bot banned, false if the member is not
	 */
	public static boolean isBotBanned(Member member)
	{
		if (member != null)
		{
			return botBans.contains(new BotBan(member));
		}
		else
		{
			return false;
		}
	}
	
	/**
	 * Returns whether two BotBan are the same.
	 * @param other the other object to compare to
	 * @return true if the two BotBans are equal, false if they are not
	 */
	public boolean equals(Object other)
	{
		BotBan o = (BotBan)other;
		return (userId == o.getUserId() && guildId == o.getGuildId());
	}
	
	/**
	 * Saves an array list of bot bans to "BotBans.ser".
	 */
	public static void saveBotBans()
	{
		try
		{
			// Writes the array list holding all of the bot bans to "BotBans.ser".
			FileOutputStream fos = new FileOutputStream("BotBans.ser");
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(botBans);
			oos.flush();
			oos.close();
		}
		catch (FileNotFoundException e)
		{
			System.out.println("\n[Epic]: Could not find \"BotBans.ser\"!\n");
		}
		catch (IOException e)
		{
			System.out.println("\n[Epic]: Could not save tags to \"BotBans.ser\"!\n");
		}
	}
	
	/**
	 * Loads an array list of BotBans from "BotBans.ser".
	 */
	@SuppressWarnings("unchecked")
	public static void loadBotBans()
	{
		try
		{
			// If "BotBans.ser" does not exist it will be created.
			if (!bansFile.toFile().exists())
			{
				bansFile.toFile().createNewFile();
				
				// Writes to the file so the bot doesn't give the "Could not find "BotBan.ser"" error on start-up.
				FileOutputStream fos = new FileOutputStream("BotBans.ser");
				ObjectOutputStream oos = new ObjectOutputStream(fos);
				oos.writeObject(botBans);
				oos.flush();
				oos.close();
			}
			
			// Reads from "BotBans.ser" and loads it into the array list holding all of the bot bans.
			FileInputStream fis = new FileInputStream(bansFile.toFile());
			ObjectInputStream ois = new ObjectInputStream(fis);
			botBans = (ArrayList<BotBan>)ois.readObject();
			ois.close();
		}
		catch (FileNotFoundException e)
		{
			System.out.println("\n[Epic]: Could not find \"BotBans.ser\"!\n");
		}
		catch (IOException e)
		{
			System.out.println("\n[Epic]: Could not load tags from \"BotBans.ser\"!");
		}
		catch (ClassNotFoundException e)
		{
			
		}
	}
}
