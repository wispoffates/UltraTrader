## UltraTrader

### What is UltraTrader
CitiTraders was a popular bukkit plugin that works with Citizens to create very flexible traders/shops with NPCs. UltraTrader is
a re-write of this plugin from the ground up. Originally written by tehBeard, and maintained by tenowg, I have decided to re-write
it from the ground up with 100% new code.

### Dependencies
* Vault (required to handle all Economy functions)
    * Will eventually handle external banks.
    * Player banks
* (Optional) Citizens 2, although shops will function the same they will only be able to be attached to blocks and item, to use NPCs
you will need Citizens 2.

### How it Works
First off, I have taken some lessons on how this was done in CitiTraders, and hopefully have learned how to
do this 1000% more efficiently than before.

By taking shops/stores and separating them from the containers that hold them. Each shop/store is its own
instance, not relying on the object that is supporting it. What does this do? It makes it so that anything
can be a shop as long as there is some way to persist the data.

A Block will be able to be a clickable shop because I can save the location information into a Map, or reload metadata
on startup, an Item can be a shop (right click on anything not already a shop) and it will open a shop, because I can save the
shop information in Lore data, same goes for NPCs, I can just save the only piece of important information on the NPC,
SHOP ID.

The Shop ID is all I use to signify a shop.

This allows for many different options, such as Multiple NPCs opening the same store (think MCDonald's Chain) in this case tho,
they all use the same wallet, same settings, same price lists, and same inventory. It allows for Item's and NPCs to open the same
store, think about selling an item that can open your store from afar?

---

### Documentation
* [Installation](installation.html "Installation")
* [Localization](localization.html "Localization")
* [Setting Shop Limits](limits.html "Setting Shop Limits")
* [Permissions](permission.html "Permissions")
* [Commands] (command.html "Commands")
* [Create a Trader](createtrader.html "Create a Trader")
* [Configuration](configuration.html "Configuration")
* [Administration](admin.html "Administration")
