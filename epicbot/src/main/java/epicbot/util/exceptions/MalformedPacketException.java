package epicbot.util.exceptions;

import java.io.IOException;

/**
 * THIS CODE IS TAKEN FROM ANOTHER AUTHOR AND HAS BEEN EDITED SLIGHTLY.
 * TO VIEW THE ORIGINAL CODE FOLLOW THIS LINK: https://github.com/Kronos666/rkon-core
 * CREDIT GOES TO THE ORIGINAL AUTHOR.
 * @author Kyle Minter (Kale Monkey)
 */
@SuppressWarnings("serial")
public class MalformedPacketException extends IOException
{
	
	public MalformedPacketException(String message)
	{
		super(message);
	}
	
}