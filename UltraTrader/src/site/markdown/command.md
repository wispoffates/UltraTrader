## Commands ##
UltraTrader was designed to have a limited amount of direct commands, there are some informative commands, and there will
be some help commands.

### Player Commands ###
**myshops** - */trader myshops*
> This will list any shops that the player currently owns
>
> player with permissions "trader.admin.viewothers can other players shops with `/trader myshops <player>`

**create** - */trader create*
> Adds the traits to an NPC to make it a shop, will add any required traits and can use the
> arguments defined in permission limits (eg. /trader create worldguard)

**delete** - */trader delete 0*
> Will delete a shop from the server, the argument is the shopid which can be retrieved from
> `/trader myshops`. Will only work for admins, and shop owners.

**credits** - */traderplayer credits*
> Will show the credits for UltraTrader

**toggleop** - */traderplayer toggleop*
> Will toggle whether or not a player is checked for Op/Ownership when purchasing or sell items.

**info** - */trader info*
> Will display info about a trader that is selected (with the stick)

### Admin Commands ###
**debug** - */traderadmin debug*
> This will turn all log debug message in the console. Best current use would be to
> find any Language key/tags you might be missing.
