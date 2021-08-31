# Crates Enhanced

### Description:
Crates Enhanced is a new Crates PaperMC plugin that supports custom items, fully configurable crate types and multiple crates for each type.

Here is how the plugin works: <br/>
A crate's possible rewards and the key to unlock it are defined by its type.
An unlimited amount of types can be created through ingame commands and an unlimited amount of crates can be assigned to a single type.
The chance to get a specific reward is determined by its weight. The rewards with more weight are more likely to be earned.

A crate doesn't have to be a chest or an enderchest block, it may very well be a grass block, allowing you to use custom crate block textures via resource packs.

### How To Use:
First of all prepare yourself an example key item, can be a custom item or a plain old diamond. <br/>
Now use `/crates type add <crate-type>` to create your crate type. <br/>
Now we need to add some rewards to this crate type. Get yourself some rewards that you think would be neat. And add them using `/crates edit <crate-type>`. <br/>
Prepare yourself a nice crate area, you should also put a block on where you want the crate to be, plugin does not place any blocks. Then run `/crates create <crate-name> <x> <y> <z> <world> <crate-type>`. <br/>
All done! Now you can right click to open the crate and get a random reward.

### Features:
- Custom item support
- Multiple item support
- Money reward with Vault support
- Command reward support
- Multiple crate and crate type support
- Translation support
- Freedom to choose your own key for the crates
- Seperate permission node for each crate type
- Easy on performance - Lightweight
- Easy to use GUI to edit crate types
- Free and open source

### Commands:
- /crates - Displays help.
- /crates help [page] - Displays help.
- /crates list crates|types - Displays a list of all crates and types.
- /crates list rewards <crate-type> - Displays a list of rewards for the crate type in a GUI.
- /crates create <crate-name> <x> <y> <z> <world> <crate-type> - Creates a crate.
- /crates delete <crate-name> - Deletes a crate.
- /crates type add|delete <crate-type> - Adds or deletes a crate type. The held item will be the key for the created crate type.
- /crates edit <crate-type> - Opens a GUI to let you edit the rewards and the key of the crate type.
- /crates give <player> key|reward <crate-type> [silent] - Gives a key or a random reward of the crate type to the player. A silent give won't print out any messages, hence more suitable for other plugins' use.

### Permissions:
- crates.* - Master permission node for the whole plugin.
- crates.use.* - Master permission node for using all crate types.

- crates.admin - Admin permission node for all editing, creating and deleting.
- crates.create - Permission node responsible for creating new crates.
- crates.delete - Permission node responsible for deleting crates.
- crates.edit - Permission node responsible for editing crate types.
- crates.give - Permission node responsible for giving keys.
- crates.list - Permission node responsible for listing crates, crate types and rewards.

### Support:
To report any bugs, or to make a feature request please use github issues.

### Planned Features:
- More animation variations
- More data saving variations
- Translation support - implemented in v2.1
- Command rewards - implemented in v2.0
- Multiple items at a time rewards - implemented in v2.0
