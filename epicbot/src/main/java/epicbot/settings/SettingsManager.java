package epicbot.settings;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import epicbot.Epic;

/**
 * @author Kyle Minter (Kale Monkey)
 */
public class SettingsManager
{
	private static SettingsManager instance;
	private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
	private Settings settings;
	private final Path configFile = new File(".").toPath().resolve("Config.json");
	
	/**
	 * Creates SettingsManager.
	 */
	public SettingsManager() throws IOException
	{
		// Checks if Config.json exists. If it doesn't exist it will be created.
		if (!configFile.toFile().exists())
		{
			System.out.println("\n[Epic]: Creating default Config.json file.");
			System.out.println("[Epic]: You will need to edit the Config.json with your login information.\n");
			// Gets default Config.json and saves it then closes the program.
			this.settings = getDefaultSettings();
			saveSettings();
			System.out.println("Press Enter to terminate...");
			System.in.read();
			System.exit(Epic.NEWLY_CREATED_CONFIG);
		}
		// Loads the settings from Config.json.
		loadSettings();
	}
	
	/**
	 * Returns the SettingsManager object. If it has not been instantiated it will be.
	 */
	public static SettingsManager getInstance()
	{
		// Checks if SettingsManager has been instantiated already.
		if (instance == null)
		{
			// Instantiates the SettingsManager.
			try
			{
				instance = new SettingsManager();
			}
			catch (IOException e)
			{
				System.out.println("\n[Epic]: ngl this shouldn't have crashed. idk what happened.\n");
				e.printStackTrace();
			}
		}
		// Returns the SettingsManager.
		return instance;
	}
	/**
	 * Reads from Config.json and loads settings.
	 */
	public void loadSettings()
	{
		try
		{
			// Reads the Config.json file using the UTF 8 format.
			BufferedReader reader = Files.newBufferedReader(configFile, StandardCharsets.UTF_8);
			// Deserializes the .json file into the settings object.
			this.settings = gson.fromJson(reader, Settings.class);
			reader.close();
			System.out.println("\n[Epic]: Settings loaded!\n");
			saveSettings();
		}
		catch (IOException e)
		{
			System.out.println("\n[Epic]: Error loading settings!\n");
			e.printStackTrace();
		}
	}
	
	/**
	 * Saves settings in Config.json.
	 */
	public void saveSettings()
	{
        // Serializes settings into a .json file.
		String jsonOut = gson.toJson(this.settings);
        try {
            // Writes to the Config.json using the UTF 8 format.
        	BufferedWriter writer = Files.newBufferedWriter(configFile, StandardCharsets.UTF_8);
            writer.append(jsonOut);
            writer.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
	
	/**
	 * Returns the settings object.
	 * 
	 * @return settings object.
	 */
	public Settings getSettings()
	{
		return settings;
	}
	
	/**
	 * Returns a settings object with the default values for the Config.json file.
	 * 
	 * @return settings object.
	 */
    private Settings getDefaultSettings()
    {
        Settings newSettings = new Settings();
        newSettings.setBotToken("");
        newSettings.setCommandPrefix("");
        newSettings.setOpedRole("");
        newSettings.setMuteRole("");
        newSettings.setNsfwRole("");
        newSettings.setLogChannelName("");
        return newSettings;
    }
}