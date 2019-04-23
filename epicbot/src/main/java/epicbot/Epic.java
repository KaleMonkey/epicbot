package epicbot;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.security.auth.login.LoginException;

import com.jagrosh.jdautilities.command.CommandClientBuilder;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;

import epicbot.commands.general.About;
import epicbot.commands.general.CatFact;
import epicbot.commands.general.NSFW;
import epicbot.commands.general.Servers;
import epicbot.commands.moderation.Ban;
import epicbot.commands.moderation.Kick;
import epicbot.commands.moderation.Mute;
import epicbot.commands.moderation.Unmute;
import epicbot.commands.owner.Ping;
import epicbot.commands.owner.ReloadConfig;
import epicbot.commands.owner.Restart;
import epicbot.commands.owner.Shutdown;
import epicbot.commands.tag.CreateTag;
import epicbot.commands.tag.DeleteTag;
import epicbot.commands.tag.GetTag;
import epicbot.entities.Tag;
import epicbot.settings.Settings;
import epicbot.settings.SettingsManager;
import epicbot.util.Listener;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Game;

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
	private static ExecutorService executor;
		
	/**
	 * @param args the command line arguments
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException
	{
		// Sets up the bot for running.	
		setupBot();
	}
	
	/**
	 * Returns the API.
	 * @return a JDA instance
	 */
	public static JDA getAPI()
	{
		return api;
	}
	
	/**
	 * Returns the executor for the bot.
	 * @return the ExecutorService
	 */
	public static ExecutorService getExecutorService()
	{
		return executor;
	}
	
	/**
	 * Sets the bot up.
	 * @throws IOException 
	 */
	public static void setupBot()
	{	
		
		try
		{
			System.out.println("\n[Epic]: Setting up bot.\n");
			
			// Creates SettingsManager and gets the Settings object.
			Settings settings = SettingsManager.getInstance().getSettings();
			
			// Creates the thread pool for the bot.
			executor = Executors.newCachedThreadPool();
			// Creates the client so we can setup the bot for start-up.
			EventWaiter waiter = new EventWaiter();
			CommandClientBuilder client = new CommandClientBuilder()
					.setOwnerId(settings.getOwnerID())
					.setPrefix(settings.getCommandPrefix())
					.setGame(Game.listening(settings.getCommandPrefix() + "help"));
			
			// Adds commands.
			client.addCommands(
					// General commands.
					new About(),
					new NSFW(),
					new Servers(),
					new CatFact(),
					// Moderation commands.
					new Mute(),
					new Unmute(),
					new Kick(),
					new Ban(),
					// Tag commands.
					new CreateTag(),
					new DeleteTag(),
					new GetTag(),
					// Music commands.
					
					// Owner commands.
					new Ping(),
					new ReloadConfig(),
					new Shutdown(),
					new Restart());
			
			JDABuilder jdaBuilder = new JDABuilder(AccountType.BOT).setToken(settings.getBotToken());
			
			// Adds event listener.
			Listener eventListener = new Listener();
			jdaBuilder.addEventListener(eventListener, client.build(), waiter);
			
			// Now that the bot is all setup, it will login.
			api = jdaBuilder.build();
			// Blocks until the JDA is completely loaded.
			api.awaitReady();			
			System.out.println("\n[Epic]: Finished building JDA!\n");
			
			// Loads "Tags.ser"
			Tag.loadTags();
		}
		catch (IllegalArgumentException e)
        {
            try
            {
            	System.out.println("\n[Epic]: No login details provided! Please provide a botToken in the config.\n");
            	System.out.println("Press Enter to terminate...");
            	System.in.read();
            	System.exit(NO_USERNAME_PASS_COMBO);
            }
            catch (IOException ioe)
            {
            	e.printStackTrace();
            }
        }
        catch (LoginException e)
        {
            try
            {	System.out.println("\n[Epic]: The botToken provided in the Config.json was incorrect.");
            	System.out.println("Did you modify the Config.json after it was created?\n");
            	System.out.println("Press Enter to terminate...");
            	System.in.read();
            	System.exit(BAD_USERNAME_PASS_COMBO);
            }
            catch (IOException ioe)
            {
            	e.printStackTrace();
            }
        }
        catch (InterruptedException e)
        {
            try
            {	System.out.println("\n[Epic]: The login thread was interrupted!\n");
            	System.out.println("Press Enter to terminate...");
            	System.in.read();
            	System.exit(UNABLE_TO_CONNECT_TO_DISCORD);
            }
            catch (IOException ioe)
            {
            	e.printStackTrace();
            }
        }
	}

}
