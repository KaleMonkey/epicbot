package epicbot.commands.moderation;

import java.util.List;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import epicbot.entities.MutedMember;
import epicbot.settings.SettingsManager;
import epicbot.util.Logger;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.exceptions.HierarchyException;

/**
 * @author Kyle Minter (Kale Monkey)
 */
public class Unmute extends Command
{
	public Unmute()
	{
		this.name = "unmute";
		this.help = "Unmutes the specified user. You must have an OPed role to use this command.";
		this.arguments = "<@user>";
		this.category = new Category("Moderation");
		this.guildOnly = true;
		this.requiredRole = SettingsManager.getInstance().getSettings().getOpedRole();
		this.botPermissions = new Permission[] {Permission.MESSAGE_WRITE, Permission.MANAGE_ROLES};
	}
	
	public void execute(CommandEvent event)
	{
		try
		{
			// Gets the arguments for the command.
			Member memberToUnmute = getMemberToUnmute(event);
			
			if (!MutedMember.isMuted(event.getGuild(), memberToUnmute.getRoles()))
			{
				// If the user is not muted the automated response will be sent.
				event.reply("The user provided is not muted!");
				return;
			}
			
			// Unmutes the user.
			event.getGuild().getController().removeRolesFromMember(memberToUnmute, SettingsManager.getInstance().getSettings().getMuteRole(event.getGuild())).queue();
			MutedMember.removeMutedMember(new MutedMember(memberToUnmute));
			
			// Sends message confirming that the unmute worked.
			event.reply("Unmuted " + memberToUnmute.getEffectiveName() + ".");
			
			// If the user getting muted is not a bot they will be sent a message telling them they got unmuted.
			if (!(memberToUnmute.getUser().isBot()))
			{
				memberToUnmute.getUser().openPrivateChannel().queue((channel) ->
				{
					channel.sendMessage("You have been unmuted because a mod took mercy on you.").queue();
				});
			}
			
			// Logs the unmute.
			Logger.logUnmute(event, memberToUnmute);
		}
		catch (IllegalArgumentException e)
		{
			// If the arguments are invalid the automated message will be sent.
			event.reply("You provided illegal arguments! Try `" +
					SettingsManager.getInstance().getSettings().getCommandPrefix() + "help unmute` to get help with this command.");
		}
		catch (HierarchyException e)
		{
			System.out.println("\n[Error]: The bot role is lower in the role hierarchy than the muted role!");
			System.out.println("[Error]: Please raise the bot's role in the role hierarchy.\n");
		}
	}
	
	/**
	 * Returns the member to unmute from the message received event.
	 * @param event the message received event
	 * @return the member to unmute
	 * @throws IllegalArgumentException
	 */
	private static Member getMemberToUnmute(CommandEvent event) throws IllegalArgumentException
	{
		// Creates a member variable and gives it a default value of null.
		Member member = null;
		
		// Checks if the message mentions a member.
		List<Member> mentionedMembers = event.getMessage().getMentionedMembers(event.getGuild());
		if (mentionedMembers.size() == 1)
		{
			member = mentionedMembers.get(0);
		}
		
		// If member is still at it's default value an exception will be thrown.
		if (member == null)
		{
			throw new IllegalArgumentException("Argument Invalid!");
		}
		else
		{
			return member;
		}
	}
}
