## Commands ##
UltraTrader was designed to have a limited amount of direct commands, there are some informative commands, and there will
be some help commands.

### Player Commands ###
**myshops** - */trader myshops*
> This will list any shops that the player currently owns
>
> *TODO* - admins can list other players shops.

**create** - */trader create*
>Adds the traits to an NPC to make it a shop, will add any required traits and can use the
>arguments defined in permission limits (eg. /trader create worldguard)

**delete** - */trader delete 0*
>Will delete a shop from the server, the argument is the shopid which can be retrieved from
>`/trader myshops`. Will only work for admins, and shop owners.

**credits - */traderplayer credits*
> Will show the credits for UltraTrader

### Admin Commands ###
**debug** - */traderadmin debug*
> This will turn all log debug message in the console. Best current use would be to
find any Language key/tags you might be missing.