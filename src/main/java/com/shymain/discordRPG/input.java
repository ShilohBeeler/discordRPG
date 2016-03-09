package com.shymain.discordRPG;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import sx.blah.discord.api.DiscordException;
import sx.blah.discord.api.MissingPermissionsException;
import sx.blah.discord.handle.EventSubscriber;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;
import sx.blah.discord.util.HTTP429Exception;

public class Input {

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
		if(event.getMessage().getChannel().getID().equalsIgnoreCase("156840527164211200"))
		{
			if(command.equalsIgnoreCase(".help"))
			{
				event.getMessage().getChannel().sendMessage("*.wares* displays the purchasable item.\n"
						+ "*.inv* will display your inventory.\n"
						+ "*.buy [item]* will purchase the specified item.\n"
						/*+ "*.price [item]* will show the selling price of the specified item.\n"
						+ "*.sell [item]* will sell the specified item."*/);
			}else if(command.equalsIgnoreCase(".wares") || command.equalsIgnoreCase(".items"))
			{
				Store.displayWares(event);
			}else if(command.equalsIgnoreCase(".buy"))
			{
				if(arguments==null)
				{
					event.getMessage().getChannel().sendMessage("Buy what?");
					break commands;
				}
				Store.buyItem(event, arguments[0]);
			}else if(command.equalsIgnoreCase(".inv") || command.equalsIgnoreCase(".inventory"))
			{
				Player.getInventory(event);
			}
		}else if(event.getMessage().getChannel().isPrivate()){
			if(command.equalsIgnoreCase(".help"))
			{
				event.getMessage().getChannel().sendMessage("*.fight* either starts a battle or attacks an enemy.\n"
						+ "*.inv* displays your inventory's state.");
			}else if(command.equalsIgnoreCase(".fight") || command.equalsIgnoreCase(".attack"))
			{
				if(Monster.currentFights.containsKey(event.getMessage().getAuthor()))
				{
					Monster.attack(event.getMessage().getAuthor(), event.getMessage().getChannel());
				}else{
					Monster.startFight(event);
				}
			}else if(command.equalsIgnoreCase(".inv") || command.equalsIgnoreCase(".inventory"))
			{
				Player.getInventory(event);
			}
		}else
		{
			if(command.equalsIgnoreCase(".join"))
			{
				JSONObject json = new JSONObject(DiscordRPG.readFile(Player.file));
				if(json.getJSONObject("players").isNull(event.getMessage().getAuthor().getID()))
				{
					Player.create(event.getMessage().getAuthor());
					event.getMessage().getChannel().sendMessage("You have joined the game.\nMost commands are done via PM. Type .help anywhere to see available commands in that location.");
				}else{
					event.getMessage().getChannel().sendMessage("You are already in the system!");
				}
			}else if(command.equalsIgnoreCase(".help"))
			{
				event.getMessage().getChannel().sendMessage("*.join* if you are not already added to the game.");
			}else if(command.equalsIgnoreCase(".mine"))
			{
				event.getMessage().getChannel().sendMessage("You swing your pick at the rock.");
				Floor.mineRock(event.getMessage().getAuthor(), event.getMessage().getChannel());
			}
		}
	}
	
}
