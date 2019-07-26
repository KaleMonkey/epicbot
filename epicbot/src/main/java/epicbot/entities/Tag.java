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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

/**
 * @author Kyle Minter (Kale Monkey)
 */
public class Tag implements Serializable, Comparable<Tag>
{
	private static final long serialVersionUID = 2;
	private static final Path tagsFile = new File(".").toPath().resolve("Tags.ser");
	private static ArrayList<Tag> tags = new ArrayList<Tag>();
	
	private String name;
	private String content;
	private long authorId;
	private String date;
	private int timesUsed;
	
	/**
	 * Constructs a tag object with a given name, content, and author id.
	 * @param n the name of the tag
	 * @param c the content of the tag
	 * @param id the id the author
	 */
	public Tag (String n, String c, long id)
	{
		name = n;
		content = c;
		authorId = id;
		date = new SimpleDateFormat("MM/dd/yyyy").format(new Date());
	}
	
	/**
	 * Constructs a tag object with a given name.
	 * @param n the name of the tag
	 */
	public Tag (String n)
	{
		name = n;
	}
	
	/**
	 * Constructs a tag object with a given name and author id.
	 * @param n n the name of the tag
	 * @param id the id the author
	 */
	public Tag (String n, long id)
	{
		name = n;
		authorId = id;
	}
	
	/**
	 * Returns the name of the tag.
	 * @return the name of the tag
	 */
	public String getName()
	{
		return name;
	}
	
	public void setName(String n)
	{
		name = n;
	}
	
	/**
	 * Returns the content of the tag.
	 * @return the content of the tag
	 */
	public String getContent()
	{
		timesUsed++;
		return content;
	}
	
	/**
	 * Returns the author of the tag.
	 * @return the author of the tag
	 */
	public long getAuthorId()
	{
		return authorId;
	}
	
	/**
	 * Returns the date the tag was created.
	 * @return the date the tag was created
	 */
	public String getDate()
	{
		return date;
	}
	
	/**
	 * Returns the amount of times the tag has been used.
	 * @return the amount of times the tag has been used
	 */
	public int getUses()
	{
		return timesUsed;
	}
	
	/**
	 * Returns the current tag as a string.
	 * @return a string version of the tag
	 */
	public String toString()
	{
		return name + "-" + content + "-" + authorId + "-" + date + "-" + timesUsed;
	}
	
	/**
	 * Returns whether two tags have the same name.
	 * @param other the other object to compare to
	 * @return true if the two tag are equal, false if they are not
	 */
	public boolean equals(Object other)
	{
		Tag o = (Tag)other;
		return (name.equals(o.name));
	}
	
	/**
	 * Compares two tag objects.
	 * @param other the other object to compare to
	 * @return 
	 */
	public int compareTo(Tag o)
	{
		return this.name.compareTo(o.getName());
	}
	
	/**
	 * Adds a tag to an array list of tags.
	 * @param tag the tag to be added
	 */
	public static void addTag(Tag tag)
	{
		tags.add(tag);
		Collections.sort(tags);
		saveTags();
	}
	
	/**
	 * Removes a tag from an array list of tags.
	 * @param tag the tag to remove
	 */
	public static void removeTag(Tag tag)
	{
		tags.remove(tag);
		Collections.sort(tags);
		saveTags();
	}
	
	/**
	 * Removes all tags with a given author id.
	 * @param id the author id of the tags you want to remove
	 */
	public static void removeTagsById(long id)
	{
		for (int i = tags.size() - 1; i > -1; i--)
		{
			if (tags.get(i).getAuthorId() == id)
			{
				tags.remove(i);
			}
		}
	}
	
	/**
	 * Removes all tags from the tags list.
	 */
	public static void removeAllTags()
	{
		tags.clear();
	}
	
	/**
	 * Returns a tag from an array list of tags. 
	 * @param tag the tag to return
	 * @return a tag object if it is found, null if it isn't
	 */
	public static Tag getTag(Tag tag)
	{
		int i = Collections.binarySearch(tags, tag);
		if (i < 0)
		{
			return null;
		}
		return tags.get(i);
	}
	
	public static ArrayList<Tag> getAllTags()
	{
		return tags;
	}
	
	/**
	 * Saves an array list of tags to "Tags.ser".
	 */
	public static void saveTags()
	{
		try
		{
			// Writes the array list holding all of the tags to "Tags.ser".
			FileOutputStream fos = new FileOutputStream("Tags.ser");
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(tags);
			oos.flush();
			oos.close();
		}
		catch (FileNotFoundException e)
		{
			System.out.println("\n[Epic]: Could not find \"Tags.ser\"!\n");
		}
		catch (IOException e)
		{
			System.out.println("\n[Epic]: Could not save tags to \"Tags.ser\"!\n");
		}
	}
	
	/**
	 * Loads an array list of tags from "Tags.ser".
	 */
	@SuppressWarnings("unchecked")
	public static void loadTags()
	{
		try
		{
			// If "Tags.ser" does not exist it will be created.
			if (!tagsFile.toFile().exists())
			{
				tagsFile.toFile().createNewFile();
				
				// Writes to the file so the bot doesn't give the "Could not find "Tags.ser"" error on start-up.
				FileOutputStream fos = new FileOutputStream("Tags.ser");
				ObjectOutputStream oos = new ObjectOutputStream(fos);
				oos.writeObject(tags);
				oos.flush();
				oos.close();
			}
			
			// Reads from "Tags.ser" and loads it into the array list holding all of the tags.
			FileInputStream fis = new FileInputStream(tagsFile.toFile());
			ObjectInputStream ois = new ObjectInputStream(fis);
			tags = (ArrayList<Tag>)ois.readObject();
			ois.close();
			System.out.println("\n[Epic]: Loaded \"Tags.ser\"!\n");
		}
		catch (FileNotFoundException e)
		{
			System.out.println("\n[Epic]: Could not find \"Tags.ser\"!\n");
		}
		catch (IOException e)
		{
			System.out.println("\n[Epic]: Could not load tags from \"Tags.ser\"!");
		}
		catch (ClassNotFoundException e)
		{
			
		}
	}
}