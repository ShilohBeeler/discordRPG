package com.shymain.discordRPG;

import java.io.IOException;

import org.json.JSONException;

import sx.blah.discord.api.DiscordException;
import sx.blah.discord.api.MissingPermissionsException;
import sx.blah.discord.handle.EventSubscriber;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;
import sx.blah.discord.util.HTTP429Exception;

public class input {

	@EventSubscriber
	public static void commands(MessageReceivedEvent event) throws JSONException, IOException, MissingPermissionsException, HTTP429Exception, DiscordException
	{
		String command;
		String allArguments = "";
		String[] arguments = null;
		
		String rawMessage = event.getMessage().getContent();
		
		String parts[] = rawMessage.split(" ", 2);
		command = parts[0];
		if(parts.length == 2)
		{
			allArguments = parts[1];
			arguments = allArguments.split(" ");
		}
		commands:
		if(command.equalsIgnoreCase(".fight") || command.equalsIgnoreCase(".attack"))
		{
			if(monster.currentFights.containsKey(event.getMessage().getAuthor()))
			{
				monster.attack(event.getMessage().getAuthor(), event.getMessage().getChannel());
				break commands;
			}else{
				monster.startFight(event);
			}
		}
	}
	
}
