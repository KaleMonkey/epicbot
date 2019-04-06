package epicbot;

import javax.security.auth.login.LoginException;

import epicbot.util.Listener;
import epicbot.util.Settings;
import epicbot.util.SettingsManager;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;

/**
 * @author Kyle Minter (Kale Monkey)
 */
public class Epic
{
	// Non error, no action exit codes.
	public static final int NEWLY_CREATED_START = 1;
	public static final int NEWLY_CREATED_CONFIG = 2;
	
	// Error exit codes.
	public static final int UNABLE_TO_CONNECT_TO_DISCORD = 3;
	public static final int BAD_USERNAME_PASS_COMBO = 4;
	public static final int NO_USERNAME_PASS_COMBO = 5;
	
	private static JDA api;
	
	public static Settings settings;
		
	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args)
	{
		// Sets up the bot for running.
		setupBot();
	}
	
	/**
	 * Sets the bot up.
	 */
	public static void setupBot()
	{	
		
		try
		{
			System.out.println("\n[Epic]: Setting up bot.\n");
			
			// Creates SettingsManager and gets the Settings object.
			settings = SettingsManager.getInstance().getSettings();
			
			// Creates the builder so we can setup the bot for start-up.
			JDABuilder jdaBuilder = new JDABuilder(AccountType.BOT).setToken(settings.getBotToken());
			
			// Adds event listener.
			Listener eventListener = new Listener();
			jdaBuilder.addEventListener(eventListener);
			
			// Now that the bot is all setup, it will login.
			api = jdaBuilder.build();
			// Blocks until the JDA is completely loaded.
			api.awaitReady();			
			System.out.println("\n[Epic]: Finished building JDA!\n");
		}
		catch (IllegalArgumentException e)
        {
            System.out.println("\n[Epic]: No login details provided! Please provide a botToken in the config.\n");
            System.exit(NO_USERNAME_PASS_COMBO);
        }
        catch (LoginException e)
        {
            System.out.println("\n[Epic]: The botToken provided in the Config.json was incorrect.");
            System.out.println("Did you modify the Config.json after it was created?\n");
            System.exit(BAD_USERNAME_PASS_COMBO);
        }
        catch (InterruptedException e)
        {
            System.out.println("\n[Epic]: The login thread was interrupted!\n");
            System.exit(UNABLE_TO_CONNECT_TO_DISCORD);
        }
	}

}
