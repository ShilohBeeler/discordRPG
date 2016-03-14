package com.shymain.discordRPG;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Optional;
import java.util.Timer;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.DiscordException;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.MissingPermissionsException;
import sx.blah.discord.handle.EventDispatcher;
import sx.blah.discord.handle.EventSubscriber;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;
import sx.blah.discord.handle.impl.events.ReadyEvent;
import sx.blah.discord.handle.impl.events.UserJoinEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.HTTP429Exception;

public class DiscordRPG {
	
	public static boolean ok = false;
	public static ConcurrentHashMap<Event, Integer> timedEvents = new ConcurrentHashMap<Event, Integer>();
	public static Timer timer = new Timer();
	public static Tick ticker = new Tick();
	public static HashMap<IUser, Event> eventStorage = new HashMap<IUser, Event>();

	
	public static IDiscordClient getClient(String email, String password, boolean login) throws DiscordException { //Returns an instance of the discord client
	    ClientBuilder clientBuilder = new ClientBuilder(); //Creates the ClientBuilder instance
	    clientBuilder.withLogin(email, password); //Adds the login info to the builder
	    if (login) {
	      return clientBuilder.login(); //Creates the client instance and logs the client in
	    } else {
	      return clientBuilder.build(); //Creates the client instance but it doesn't log the client in yet, you would have to call client.login() yourself
	    }
	  }
	
	public static void eventDistributer(String eventType, IUser user, IChannel channel) throws JSONException, IOException, MissingPermissionsException, HTTP429Exception, DiscordException
	{
		if(eventType.equalsIgnoreCase("defend"))
		{
			Monster.defend(user, channel);
		}else if(eventType.equalsIgnoreCase("RockRefreshEvent"))
		{
			Floor.addRock(channel);
		}else if(eventType.equalsIgnoreCase("TreeRefreshEvent"))
		{
			Floor.addTree(channel);
		}else if(eventType.equalsIgnoreCase("WeatherTestEvent"))
		{
			Ambient.weatherTest(channel);
		}
	}
	
	@EventSubscriber
	public void firstMessage(ReadyEvent event) throws MissingPermissionsException, HTTP429Exception, DiscordException, IOException
	{
		if(ok==false){
			ok = true;
			String blank = "Testing";
		    Optional<String> game = Optional.<String>of(blank);
		    event.getClient().updatePresence(false, game);
		Ambient.initialize(event);
		File f = new File(System.getProperty("user.home")+"/discordRPG");
		if(!f.exists())
		{
			f.mkdirs();
		}
		File g = new File(System.getProperty("user.home")+"/discordRPG/players.json");
		if(!g.exists())
		{
			g.createNewFile();
			FileWriter w = new FileWriter(System.getProperty("user.home")+"/discordRPG/players.json");
			w.write("{\"players\":{}}");
			w.flush();
			w.close();
		}
		File h = new File(System.getProperty("user.home")+"/discordRPG/floors.json");
		if(!h.exists())
		{
			h.createNewFile();
			FileWriter w = new FileWriter(System.getProperty("user.home")+"/discordRPG/floors.json");
			w.write("{\"floors\":{}}");
			w.flush();
			w.close();
			Floor.initialize();
		}
		File i = new File(System.getProperty("user.home")+"/discordRPG/monsters.json");
		if(!i.exists())
		{
			i.createNewFile();
			FileWriter w = new FileWriter(System.getProperty("user.home")+"/discordRPG/monsters.json");
			w.write("{\"monsters\":{}}");
			w.flush();
			w.close();
			Monster.initialize();
		}
		File j = new File(System.getProperty("user.home")+"/discordRPG/ranks.json");
		if(!j.exists())
		{
			j.createNewFile();
			FileWriter w = new FileWriter(System.getProperty("user.home")+"/discordRPG/ranks.json");
			w.write("{\"ranks\":{}}");
			w.flush();
			w.close();
			Store.initialize();
		}
		File k = new File(System.getProperty("user.home")+"/discordRPG/items.json");
		if(!k.exists())
		{
			k.createNewFile();
			InputStream inputStream = DiscordRPG.class.getResourceAsStream("/resources/items.json");
			String theString = IOUtils.toString(inputStream, "UTF-8");
			System.out.println(theString);
			JSONObject json = new JSONObject(theString);
			FileWriter w = new FileWriter(System.getProperty("user.home")+"/discordRPG/items.json");
			w.write(json.toString(3));
			w.flush();
			w.close();
			Store.initialize();
		}
		File l = new File(System.getProperty("user.home")+"/discordRPG/trades.json");
		if(!l.exists())
		{
			l.createNewFile();
			FileWriter w = new FileWriter(System.getProperty("user.home")+"/discordRPG/trades.json");
			w.write("{\"trades\":{}}");
			w.flush();
			w.close();
			Store.initialize();
		}
		}
	}
	
	@EventSubscriber
	public static void testMessages(MessageReceivedEvent event) throws JSONException, IOException, MissingPermissionsException, HTTP429Exception, DiscordException
	{
		JSONObject json = new JSONObject(readFile(Player.file));
		JSONObject player = json.getJSONObject("players");
		if(player.isNull(event.getMessage().getAuthor().getID()))
		{
			Player.create(event.getMessage().getAuthor());
			event.getMessage().getChannel().sendMessage("A new traveler arrives, sword in hand.\n"
				+ event.getMessage().getAuthor().mention() + ": Many functions take place via DM. Use .help anywhere in DM or this server to see commands available there.");
		}
		if(event.getMessage().getContent().startsWith("."))
			Input.commands(event);
	}

	@EventSubscriber
	public static void userJoins(UserJoinEvent event) throws JSONException, IOException, MissingPermissionsException, HTTP429Exception, DiscordException
	{
		Player.create(event.getUser());
		event.getGuild().getChannels().get(0).sendMessage("A new traveler arrives, sword in hand.\n"
				+ event.getUser().mention() + ": Many functions take place via DM. Use .help anywhere in DM or this server to see commands available there.");
		
	}
	
	public static void main(String[] args) throws DiscordException, JSONException, IOException{
		IDiscordClient client = DiscordRPG.getClient("discordrpg+beta@gmail.com", Password.getPass(), true);
		EventDispatcher dispatcher = client.getDispatcher();
	    dispatcher.registerListener(new DiscordRPG());
	    timer.schedule(ticker, 0, 1000);
	}

	public static String readFile( String file ) throws IOException {
	    BufferedReader reader = new BufferedReader( new FileReader (file));
	    String         line = null;
	    StringBuilder  stringBuilder = new StringBuilder();
	    String         ls = System.getProperty("line.separator");

	    try {
	        while( ( line = reader.readLine() ) != null ) {
	            stringBuilder.append( line );
	            stringBuilder.append( ls );
	        }

	        return stringBuilder.toString();
	    } finally {
	        reader.close();
	    }
	}
}

