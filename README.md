# epicbot
A discord bot using JDA.

CHANGE LOG
	epicbot-0.0.1-SNAPSHOT
		-Initial release.

	epicbot-0.0.2-SNAPSHOT
		-Added moderation logging.
		-Added ban and kick commands.
		-Added "Reason" argument to the mute command.
		-Fixed the Github link on the about command.
		-Disabled the "This isn't a known command" response because of feedback.
		-Added welcome/leave messages for users.
		-Added current build and libraries sections to about command.
		-Added ping command.

	epicbot-0.0.3-SNAPSHOT
		-Changed the wording in "help" command from "Try >help <Command>" to the config's prefix.
		-Removed ".jar" from the current build section of the "about" command.
		-Added "catfact" command.
		-Deletes "This might take a while." message when server status message is sent.
		-Log messages are now embedded messages with a color based on the action being logged.
		-If a server does not have a NSFW role it is assumed that it doesn't want to support the feature and an automated message is sent.
		-If a server does not already have a OPed or mute role matching the one in the config.json one will be made.
		-Help now sends the list of commands sorted by command type.
		-Created tag system and associated commands.
		-The bot will now respond to messages that are from a server and mention the bot with a random shit post.
		-Mute timers will now be ended once "unmute" command is called to avoid having unnecessary threads running and having worse performance because of it.
  
  epicbot-0.0.4-SNAPSHOT
    Overall:
      -The bot no longer creates "Start.bat" and newer versions will be made into .exe's.
      -Now uses a cached thread pool for better performance.
      -All references to "Epic Gamer Bot" have been changed to "Epic."
      -The bot no longer sends shitposts when mentioned.
      -Commands now give specific error messages.
      -Fixed the "Could not find "Tags.ser"" error on startup.
    Commands:
      -Added "Gay" command.
      -Added owner commands "ReloadConfig", "Restart", and "Shutdown."
      -"Ping" command is now an owner command.
      -Mod commands no longer send "You must have an OPed role to use this command."
      -"Servers" command no longer sends the "This might take a while." message and is a lot faster when checking if servers are listening.
      -"Servers" command now shows the player count of a server.
    Mutes/Unmutes:
      -Now logs mutes and unmutes that are done manually through discord.
      -Users are now notified of the server a mute/unmute occured in instead of just a mute/unmute occuring.
    Tags:
      -Tags now store the date they were created, the author, and the amount of times they have been used.
      -All tag related commands are now integrated into one command known as "Tag."
      -Only the tag author can delete a tag.
      (These updates mean any tags created with the older version of the bot will not work)
    Config:
      -Servers can be added in the "Config.json" for use in the "Servers" command.
      -An owner ID can be put in the "Config.json" so a user can use the owner commands.
