# discordRPG v0.8
NOTE: THIS IS BLATANTLY OUTDATED AND HASN'T BEEN TOUCHED IN FOREVER. IT WILL NOT WORK WITHOUT A LOT OF FRONT-END CHANGES. PLEASE DON'T ASK ME TO HELP, I DON'T CARE ABOUT THIS PROJECT ANY MORE.

RPG for Discord.

This project is essentially supposed to create a text-based MMORPG-style game (specifically taking inspiration from aspects of Runescape), playable by any and all.

# Current Features:

*Skills*: Mining, Woodcutting, and Fighting included by default! All of these three give experience, and have skill levels calculated.

*Refineries*: Smelt ores, carve wood, craft pendants! Gives experience to skills as well.

*Shops*: Buy, value, and sell items! The wares available are dependant on rank, but not randomized yet.

*Inventory Management*: You can equip items, unequip items, and manage your inventory.

*Weather*: Ambient weather! No longer has an effect on skills, but we're trying to figure it out again!

*Trading*: Trade with other players! Floating trading inventory keeps track of both players' items, and counts the value for you! Once one player has confirmed, you're protected by the other player losing the ability to add or remove items from the trade!

*Customizability*: You can create, delete, and edit floors, events, refineries, and even commands! With just a few simple commands, you can create a new event, tie the event to a new command, and start using it!

# Project Goal:
To create a bot which works as an MMORPG host for Discord, and make it easy to fork and customize to your liking both through code and Discord commands!

# Discord Server:
http://discord.gg/0pn4SBaPr03DR4rk

# Commands:

Note: "." is the default prefix, but the prefix can be changed by the server admin.

**Command Sections**:

|Section|Description|
|---|---|
|`floor`|general use commands in the server|
|`shop`|transactional commands for the shop|
|`pm`|pm only commands|
|`trade`|commands relevant to inter-player trading|
|`custom`|commands created by the admin|
|`admin`|commands only usable in the admin channel|

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
|`.trade add [item] <number>`|adds the specified item in quantity specified, with default quantity of 1.|
|`.trade remove [item] <number>`|removes the specified item in quantity specified, with default quantity of 1.|
|`.trade summary`|shows the current floating inventory of the trade.|
|`.trade confirm`|confirms the trade and locks down the add and remove commands.|
|`.trade cancel`|immediately ends the trade.|
|**Custom Commands**|
|`none`|these are server specific, and can only be found through .help custom on each server|
|**Admin Commands**|
|`.floor create [id]`|initializes a new floor for the channel with the id [id]|
|`.floor delete [id]`|removes the floor and all data for the channel with the id [id]|
|`.floor refinery add [refinery] [id]`|adds the refinery [refinery] to the floor [id]|
|`.floor refinery delete [refinery] [id]`|removes the refinery [refinery] from the floor [id]|
|`.floor event add [event] [id]`|adds the event [event] to the floor [id]|
|`.floor event delete [event] [id]`|removes the event [event] from the floor [id]|
|`.floor event edit [event] [id] [key] [value]`|changes [key] to have [value] for the [event] on floor [id]|
|`.event create [name]`|creates a new event [name]|
|`.event delete [name]`|deletes the event [name]|
|`.event edit [event] [key] [value]`|changes [key] to have [value] for [event]|
|`.refinery create [name]`|creates a new refinery [name]|
|`.refinery delete [name]`|deletes the refinery [name]|
|`.refinery setskill [name] [skill]`|changes the refinery [name] to use the [skill] specified for XP and level required|
|`.refinery input add [name] [item]`|initializes a new [item] input for the refinery [name]|
|`.refinery input delete [name] [item]`|deletes the [item] input for the refinery [name]|
|`.refinery input edit [name] [item] [key] [value]`|edits the [key] to have the value [value] for the [item] input for the refinery [name]|
|`.command create [name]`|creates a new command with the name [name]|
|`.command delete [name]`|deletes the command with the name [name]|
|`.command tie [refinery | event] [name] [r/e name]`|ties the command [name] to the [event | refinery] with the name [r/e name]|
