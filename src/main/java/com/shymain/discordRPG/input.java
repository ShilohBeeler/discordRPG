package com.shymain.discordRPG;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import sx.blah.discord.api.DiscordException;
import sx.blah.discord.api.MissingPermissionsException;
import sx.blah.discord.handle.EventSubscriber;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IPrivateChannel;
import sx.blah.discord.util.HTTP429Exception;

public class Input {
	
	public static String command;
	public static String allArguments;
	public static String[] arguments;
	public static String rawMessage;
	public static String[] parts;
	public static String item;

	@EventSubscriber
	public static void commands(MessageReceivedEvent event) throws JSONException, IOException, MissingPermissionsException, HTTP429Exception, DiscordException
	{
		allArguments = "";
		arguments = null;
		
		rawMessage = event.getMessage().getContent();
		
		parts = rawMessage.split(" ", 2);
		command = parts[0];
		if(parts.length == 2)
		{
			allArguments = parts[1];
			arguments = allArguments.split(" ");
		}
		item = allArguments;
		item = item.replaceAll(" ", "_");
<<<<<<< HEAD
		if(command.equalsIgnoreCase(".help")){
			help(event);
		}else if(command.equalsIgnoreCase(".inv") || command.equalsIgnoreCase(".inventory"))
=======
		item = item.toLowerCase();
		if(command.equalsIgnoreCase(".info")|| command.equalsIgnoreCase(".about"))
		{
			event.getMessage().getChannel().sendMessage("*discordRPG* bot programmed by **Shymain**!\n"
					+ "Version: 0.1.1\n"
					+ "Fork me on GitHub! https://github.com/Shymain/discordRPG");
		}	
		else if(command.equalsIgnoreCase(".inv") || command.equalsIgnoreCase(".inventory"))
>>>>>>> master
		{
			Player.getInventory(event);
		}else if(command.equalsIgnoreCase(".use"))
		{
			useHandling(event);
		}else if(command.equalsIgnoreCase(".equip")){
			Player.equip(event.getMessage().getAuthor(), event.getMessage().getChannel(), item);
		}else if(command.equalsIgnoreCase(".unequip")){
			Player.unequip(event.getMessage().getAuthor(), event.getMessage().getChannel(), item);
		}else if(command.equalsIgnoreCase(".equipment") || command.equalsIgnoreCase(".body")){
			Player.getEquip(event.getMessage().getAuthor(), event.getMessage().getChannel());
		}else if(command.equalsIgnoreCase(".skills") || command.equalsIgnoreCase(".stats")){
			Player.getSkills(event.getMessage().getAuthor(), event.getMessage().getChannel());
		}else if(event.getMessage().getChannel().getID().equalsIgnoreCase("157558822842531840"))
		{
			shop(event);
		}else if(event.getMessage().getChannel().getID().equalsIgnoreCase("157564348263432192"))
		{
			admin(event);
		}else if(event.getMessage().getChannel().isPrivate())
			{
			privateChannels(event);
		}else if(event.getMessage().getChannel().getGuild().getID().equalsIgnoreCase("157558660732682241"))
		{
			floorCommands(event);
		}
	}
	
	public static void help(MessageReceivedEvent event) throws MissingPermissionsException, HTTP429Exception, DiscordException
	{
		IPrivateChannel pm = null;
		try {
			pm = event.getClient().getOrCreatePMChannel(event.getMessage().getAuthor());
		} catch (Exception e) {
			event.getMessage().getChannel().sendMessage("You managed to find a new exception. Good Job.");
			return;
		}
		if(arguments == null)
		{
		pm.sendMessage("**Command sections:**\n"
				+ "*floor* is general use commands in the server.\n"
				+ "*shop* shows transactional commands for the shop.\n"
				+ "*pm* shows pm-only commands.\n"
				+ "*trade* is a list of commands relevant for trading.\n"
				+ "Type *.help [section]* for more information.");
		return;
		}
		String[] splitted = allArguments.split(" ");
		String subcommand = splitted[0];
		if(subcommand.equalsIgnoreCase("floor"))
		{
			pm.sendMessage("**Floor Commands:**\n"
					+ "*.join* if the bot didn't add you automatically.\n"
					+ "*.inv* displays your inventory.\n"
					+ "*.use [item]* uses said item.\n"
					+ "*.equip [item]* equips item.\n"
					+ "*.unequip [slot]* unequips item in slot.\n"
					+ "*.body* lists equipped items.\n"
					+ "*.skills* lists skills.\n"
					+ "*.mine* mines a rock.\n"
					+ "*.chop* cuts down a tree.");	
		}else if(subcommand.equalsIgnoreCase("shop"))
		{
			pm.sendMessage("**Shop Commands:**\n"
					+ "*.wares* displays the purchasable item.\n"
					+ "*.inv* will display your inventory.\n"
					+ "*.buy [item]* will purchase the specified item.\n"
					+ "*.price [item]* will show the selling price of the specified item.\n"
					+ "*.sell [item] <number>* will sell the specified item, optionally in the specified amount.");
		}else if(subcommand.equalsIgnoreCase("pm"))
		{
			pm.sendMessage("**PM Commands:**\n"
					+ "*.fight* either starts a battle or attacks an enemy.\n");
		}else if(subcommand.equalsIgnoreCase("trade"))
		{
			pm.sendMessage("**Trade Commands:**\n"
					+ "*.trade open [player]* requests to trade with [player].\n"
					+ "*.trade accept* accepts a trade request.\n"
					+ "*.trade reject* rejects a trade request.\n"
					+ "*.trade add [item] <number>* adds items to your trade offer, with a default amount of one.\n"
					+ "*.trade remove [item] <number>* remove items from your trade offer, with a default amount of one.\n"
					+ "*.trade summary* shows the current state of the trade.\n"
					+ "*.trade confirm* confirms your offer and locks .trade add and .trade remove.\n"
					+ "*.trade cancel* quits the trade at any point.");
		}
	}
	
	public static void privateChannels(MessageReceivedEvent event) throws MissingPermissionsException, HTTP429Exception, DiscordException, JSONException, IOException
	{
		if(command.equalsIgnoreCase(".fight") || command.equalsIgnoreCase(".attack"))
		{
			String holding = Player.getSlot(event.getMessage().getAuthor(), event.getMessage().getChannel(), "hand");
			boolean can_fight = Item.getBool(holding, "can_fight");
			if(!can_fight)
			{
				event.getMessage().getChannel().sendMessage("It would be foolish to fight a monster without a weapon equipped!");
				return;
			}
			if(Monster.currentFights.containsKey(event.getMessage().getAuthor()))
			{
				Monster.attack(event.getMessage().getAuthor(), event.getMessage().getChannel());
			}else{
				Monster.startFight(event);
			}
		}
	}
	
	public static void shop(MessageReceivedEvent event) throws MissingPermissionsException, HTTP429Exception, DiscordException, JSONException, IOException
	{
<<<<<<< HEAD
		if(command.equalsIgnoreCase(".wares") || command.equalsIgnoreCase(".items") || command.equalsIgnoreCase(".shop"))
=======
		if(command.equalsIgnoreCase(".help"))
		{
			event.getMessage().getChannel().sendMessage("*.wares* displays the purchasable item.\n"
					+ "*.inv* will display your inventory.\n"
					+ "*.buy [item]* will purchase the specified item.\n"
					+ "*.price [item]* will show the selling price of the specified item.\n"
					+ "*.sell [item]* will sell the specified item.");
		}else if(command.equalsIgnoreCase(".wares") || command.equalsIgnoreCase(".items") || command.equalsIgnoreCase(".shop"))
>>>>>>> master
		{
			Store.displayWares(event);
		}else if(command.equalsIgnoreCase(".buy"))
		{
			if(arguments==null)
			{
				event.getMessage().getChannel().sendMessage("Buy what?");
			}
			Store.buyItem(event, item);
		}else if(command.equalsIgnoreCase(".sell"))
		{
			String[] test = allArguments.split(" ");
			int k = test.length;
			int number = 0;
			String theitem = "";
			if(k<1)
			{
				return;
			}
			if(StringUtils.isNumeric(test[k-1]))
			{
				for(int j = 0;j<k-1;j++)
				{
					theitem+=test[j]+"_";
				}
				theitem = theitem.substring(0, theitem.lastIndexOf("_"));
				number = Integer.parseInt(test[k-1]);
			}
			else
		    {
		    	theitem = item;
		    	number = 1;
		    }
		    Store.sellItem(event, theitem, number);
		}else if(command.equalsIgnoreCase(".value") || command.equalsIgnoreCase(".price"))
		{
			Store.valueItem(event, item);
		}
	}
	
	public static void floorCommands(MessageReceivedEvent event) throws JSONException, IOException, MissingPermissionsException, HTTP429Exception, DiscordException
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
<<<<<<< HEAD
=======
		}else if(command.equalsIgnoreCase(".help"))
		{
			IPrivateChannel pm = null;
			try {
				pm = event.getClient().getOrCreatePMChannel(event.getMessage().getAuthor());
			} catch (Exception e) {
				event.getMessage().getChannel().sendMessage("You managed to find a new exception. Good Job.");
				return;
			}
			pm.sendMessage("**Floor Commands:**\n"
					+ "*.join* if the bot didn't add you automatically.\n"
					+ "*.inv* displays your inventory.\n"
					+ "*.use [item]* uses said item.\n"
					+ "*.equip [item]* equips item.\n"
					+ "*.unequip [slot]* unequips item in slot.\n"
					+ "*.body* lists equipped items.\n"
					+ "*.skills* lists skills.\n"
					+ "*.mine* mines a rock.\n"
					+ "*.chop* cuts a tree.\n");
>>>>>>> master
		}else if(command.equalsIgnoreCase(".mine"))
		{
			String holding = Player.getSlot(event.getMessage().getAuthor(), event.getMessage().getChannel(), "hand");
			boolean can_mine = Item.getBool(holding, "can_mine");
			if(!can_mine)
			{
				event.getMessage().getChannel().sendMessage("You don't have a pickaxe equipped.");
				return;
			}
			event.getMessage().getChannel().sendMessage("You swing your pick at the rock.");
			Floor.mineRock(event.getMessage().getAuthor(), event.getMessage().getChannel());
		}else if(command.equalsIgnoreCase(".chop") || command.equalsIgnoreCase(".cut"))
		{
			String holding = Player.getSlot(event.getMessage().getAuthor(), event.getMessage().getChannel(), "hand");
			boolean can_chop = Item.getBool(holding, "can_chop");
			if(!can_chop)
			{
				event.getMessage().getChannel().sendMessage("You don't have an axe equipped.");
				return;
			}
			event.getMessage().getChannel().sendMessage("You swing your axe at the tree.");
			Floor.cutTree(event.getMessage().getAuthor(), event.getMessage().getChannel());
		}else if(command.equalsIgnoreCase(".trade"))
		{
			if(arguments.length<1)
			{
				event.getMessage().getChannel().sendMessage("Please specify a subcommand!");
				return;
			}
			String[] resplit = allArguments.split(" ", 2);
			String subcommand = resplit[0];
			if(subcommand.equalsIgnoreCase("accept"))
			{
				Trade.accept(event.getMessage().getChannel(), event.getMessage().getAuthor());
			}else if(subcommand.equalsIgnoreCase("reject"))
			{
				Trade.reject(event.getMessage().getChannel(), event.getMessage().getAuthor());
			}else if(subcommand.equalsIgnoreCase("summary"))
			{
				Trade.display(event.getMessage().getChannel(), event.getMessage().getAuthor());
			}else if(subcommand.equalsIgnoreCase("cancel"))
			{
				Trade.cancel(event.getMessage().getChannel(), event.getMessage().getAuthor());
			}else if(subcommand.equalsIgnoreCase("confirm"))
			{
				Trade.confirm(event.getMessage().getChannel(), event.getMessage().getAuthor());
			}else if(resplit.length>1)
			{
				String args = resplit[1];
				String id = args;
				id = id.replace("<@", "");
				id = id.replace(">", "");
				String[] test = args.split(" ");
				int k = test.length;
				int number = 0;
				String theitem = "";
				if(k<1)
				{
					return;
				}
				if(StringUtils.isNumeric(test[k-1]))
				{
					for(int j = 0;j<k-1;j++)
					{
						theitem+=test[j]+"_";
					}
					theitem = theitem.substring(0, theitem.lastIndexOf("_"));
					number = Integer.parseInt(test[k-1]);
				}
				else
			    {
			    	theitem = args.replace(" ", "_");
			    	number = 1;
			    }
				if(subcommand.equalsIgnoreCase("open"))
				{
					Trade.open(event.getMessage().getChannel(), event.getMessage().getAuthor(), event.getMessage().getChannel().getGuild().getUserByID(id));
				}else if(subcommand.equalsIgnoreCase("add"))
				{
					Trade.add(event.getMessage().getChannel(), event.getMessage().getAuthor(), theitem, number);
				}else if(subcommand.equalsIgnoreCase("remove"))
				{
					Trade.remove(event.getMessage().getChannel(), event.getMessage().getAuthor(), theitem, number);
				}
				
			}
			
		}
	}
	
	public static void useHandling(MessageReceivedEvent event) throws MissingPermissionsException, HTTP429Exception, DiscordException, JSONException, IOException
	{
		if(arguments==null)
		{
			event.getMessage().getChannel().sendMessage("Use what?");
			return;
		}
		JSONObject json = new JSONObject(DiscordRPG.readFile(Player.file));
		JSONObject json2 = new JSONObject(DiscordRPG.readFile(Item.file));
		JSONObject items = json2.getJSONObject("items");
		JSONObject player = json.getJSONObject("players").getJSONObject(event.getMessage().getAuthor().getID());
		if(player.getJSONObject("inventory").isNull(item))
		{
			event.getMessage().getChannel().sendMessage("Your \"summoning items from out of nowhere\" skill is not yet at level 99. Using an item you do not possess is not possible.");
			return;
		}
		if(items.isNull(item))
		{
			event.getMessage().getChannel().sendMessage("Maybe if you believe *just* a little harder, that nonexistant item will appear!");
			return;
		}
		
		event.getMessage().getChannel().sendMessage(items.getJSONObject(item).getString("flavor_text"));
		if(items.getJSONObject(item).getBoolean("to_remove"))
		{
			Player.inventoryRemove(event.getMessage().getAuthor(), item, 1);
		}
		if(items.getJSONObject(item).has("heal"))
		{
			Player.heal(event, items.getJSONObject(item).getInt("heal"));
		}
	}
	
	public static void admin(MessageReceivedEvent event) throws JSONException, IOException, MissingPermissionsException, HTTP429Exception, DiscordException
	{
		if(command.equalsIgnoreCase(".item"))
		{
			if(allArguments.length()<2)
			{
				return;
			}
			String[] moreSplitting = allArguments.split(" ", 2);
			String[] split = allArguments.split(" ", 4);
			String subcommand = moreSplitting[0];
			String allArgs = moreSplitting[1];
			allArgs = allArgs.toLowerCase();
			allArgs = allArgs.replace(" ", "_");
			if(subcommand.equalsIgnoreCase("create"))
			{
				Item.create(event.getMessage().getAuthor(), event.getMessage().getChannel(), allArgs);
			}else if(subcommand.equalsIgnoreCase("edit"))
			{
				Item.value(event.getMessage().getAuthor(), event.getMessage().getChannel(), split[1], split[2], split[3]);
			}else if(subcommand.equalsIgnoreCase("remove"))
			{
				Item.remove(event.getMessage().getAuthor(), event.getMessage().getChannel(), allArgs);
			}
		}
	}
}
