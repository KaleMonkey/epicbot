package epicbot.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import epicbot.Epic;

/**
 * @author Kyle Minter (Kale Monkey)
 */
public class Tag implements Serializable
{
	private static final long serialVersionUID = 1;
	
	private static ArrayList<Tag> tags = new ArrayList<Tag>();
	
	private String name;
	private String content;
	
	/**
	 * Constructs a tag object with a given name and content.
	 * @param n the name of the tag
	 * @param c the content of the tag
	 */
	public Tag (String n, String c)
	{
		name = n;
		content = c;
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
	 * Returns the name of the tag.
	 * @return the name of the tag
	 */
	public String getName()
	{
		return name;
	}
	
	/**
	 * Sets the name of the tag.
	 * @param n the name of the tag
	 */
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
		return content;
	}
	
	/**
	 * Sets the content of the tag.
	 * @param c the content of the tag
	 */
	public void setContent(String c)
	{
		content = c;
	}
	
	/**
	 * Returns the current tag as a string.
	 * @return a string version of the tag
	 */
	public String toString()
	{
		return name + "-" + content;
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
	 * Adds a tag to an array list of tags.
	 * @param tag the tag to be added
	 */
	public static void addTag(Tag tag)
	{
		tags.add(tag);
		saveTags();
	}
	
	/**
	 * Removes a tag from an array list of tags.
	 * @param tag the tag to remove
	 */
	public static void removeTag(Tag tag)
	{
		tags.remove(tag);
		saveTags();
	}
	
	/**
	 * Returns a tag from an array list of tags. 
	 * @param tag the tag to return
	 * @return a tag object if it is found, null if it isn't
	 */
	public static Tag getTag(Tag tag)
	{
		int i = tags.indexOf(tag);
		if (i == -1)
		{
			return null;
		}
		return tags.get(i);
	}
	
	/**
	 * Saves an array list of tags to "Tags.ser".
	 */
	public static void saveTags()
	{
		try
		{
			// If "Tags.ser" does not exist it will be created.
			File file = new File(Epic.class.getProtectionDomain().getCodeSource().getLocation().getPath() + "Tags.ser");
			if (!file.exists())
			{
				file.createNewFile();
			}
			
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
			File file = new File(Epic.class.getProtectionDomain().getCodeSource().getLocation().getPath() + "Tags.ser");
			if (!file.exists())
			{
				file.createNewFile();
			}
			
			// Writes the array list holding all of the tags to "Tags.ser".
			FileInputStream fis = new FileInputStream("Tags.ser");
			ObjectInputStream ois = new ObjectInputStream(fis);
			tags = (ArrayList<Tag>)ois.readObject();
			ois.close();
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