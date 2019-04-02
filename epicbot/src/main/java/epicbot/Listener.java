package epicbot;

import epicbot.util.RunnableThread;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.hooks.EventListener;

/**
 * @author Kyle Minter (Kale Monkey)
 */
public class Listener implements EventListener
{
	public void onEvent(Event event)
	{
		// Creates a new thread to do everything on.
		RunnableThread rt = new RunnableThread(event);
		Thread t = new Thread(rt);
		t.start();
	}
}
