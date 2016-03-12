# discordRPG v0.2
RPG for Discord.

This project is essentially supposed to create a text-based MMORPG-style game (specifically taking inspiration from aspects of Runescape), playable by any and all.

# Current Features:

*Skills*: Mining, Woodcutting, and Fighting! All of these three give experience, and have skill levels calculated.

*Shops*: Buy, value, and sell items! The wares available are dependant on rank, but not randomized yet.

*Inventory Management*: You can equip items, unequip items, and manage your inventory.

*Weather*: Ambient weather that dynamically affects the world around you, in ways including speeding up the growth of trees during rain!

*Trading*: Trade with other players! Floating trading inventory keeps track of both players' items, and counts the value for you! Once one player has confirmed, you're protected by the other player losing the ability to add or remove items from the trade!

#Project Goal:
To create a bot which works as an MMORPG host for Discord, and make it easy to fork and customize to your liking both through code and Discord commands!

# Discord Server:
http://discord.gg/0pn4SBaPr03DR4rk

# Commands:

**Command Sections**:

|Section|Description|
|---|---|
|`floor`|general use commands in the server|
|`shop`|transactional commands for the shop|
|`pm`|pm only commands|
|`trade`|commands relevant to inter-player trading|

**Commands**:

|Command|Description|
|---|---|
|**Floor Commands**|
|`.join`|adds you to the game if the bot didn't add you automatically.|
|`.inv`|displays your inventory.|
|`.use [item]`|shows flavor text for item, as well as possibly using it and causing effects.|
|`.equip [item]`|equips item in appropriate slot if said slot is empty.|
|`.unequip [slot]`|removes item from slot.|
|`.body`|lists equipment and health.|
|`.skills`|lists different skills, what level they are, how much XP you have, and how much to the next level.|
|`.mine`|mines a rock.|
|`.chop`|chops down a tree.|
|**Shop Commands**|
|`.wares`|displays all currently purchasable items.|
|`.buy [item]`|buys the specified item.|
|`.sell [item] <number>`|sells the specified item in quantity specified, with default quantity of 1.|
|`.price [item]`|checks the price for the specified item.|
|**PM Commands**|
|`.fight`|either starts a fight or attacks in a current fight.|
|**Trade Commands**|
|`.trade open [@player]`|requests to trade with specified player.|
|`.trade accept`|accepts a trade request.|
|`.trade reject`|rejects a trade request.|
|`.trade add [item] <number>`|adds the specified item in quantity specified, with defaulty quantity of 1.|
|`.trade remove [item] <number>`|removes the specified item in quantity specified, with defaulty quantity of 1.|
|`.trade summary`|shows the current floating inventory of the trade.|
|`.trade confirm`|confirms the trade and locks down the add and remove commands.|
|`.trade cancel`|immediately ends the trade.|
