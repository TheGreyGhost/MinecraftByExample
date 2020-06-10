# MBE31_INVENTORY_FURNACE

Code by Brandon3055 and TheGreyGhost


Vazkii as the creator of Botania 
https://botaniamod.net/




	public Botania() {
		instance = this;
		proxy = DistExecutor.runForDist(() -> ClientProxy::new, () -> ServerProxy::new);
		proxy.registerHandlers();
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonSetup);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(IMCSender::enqueue);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(IMCHandler::handle);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::loadComplete);
		ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ConfigHandler.CLIENT_SPEC);
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ConfigHandler.COMMON_SPEC);
		MinecraftForge.EVENT_BUS.addListener(this::serverAboutToStart);
		MinecraftForge.EVENT_BUS.addListener(this::serverStarting);
		MinecraftForge.EVENT_BUS.addListener(this::serverStopping);
	}

Capabilities:
https://gist.github.com/williewillus/c8dc2a1e7963b57ef436c699f25a710d
https://mcforge.readthedocs.io/en/latest/datastorage/capabilities/

The terminology that Forge uses for capabilities is a bit confusing, because they use the same word (Capability) to refer to both the type of Capability (eg Capability<IItemHandler> such as CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) and the "storage" class ('interface instance') that the Capability supplies (e.g. IItemHandler) 

The key concepts you need to understand:
1) A Capability represents some extra functionality that is added to your object (ItemStack, Entity, etc)
2) Your object needs to have a CapabilityProvider.  This is used for two main purposes
  1) When Forge wants to know if your object supports a given Capability<Type>, it asks the CapabilityProvider using the getCapability() method.  If the type is supported, the CapabilityProvider returns the corresponding interface instance, which Forge then uses to interact with the Capability.
  2) To read/write the Capability information to permanent storage, i.e. NBT tags. 
3) A CapabilityProvider can be attached to objects in one of two ways:
  1) By overriding the appropriate method for the object (eg IForgeItem::initCapabilities()), which is useful for classes that you have added to the game yourself
  2) Using AttachCapabilitiesEvent, which allows you to attach your own Capability to vanilla objects
4) Each instance of your object gets its own instance of CapabilityProvider
5) The CapabilityProvider uses "Lazy initialisation", i.e. instead of fully initialising your interface instance when the CapabilityProvider is created, it waits until the first time that someone requests the interface instance.

A concrete example:
We want to give our ItemFlowerBag the ability to store Flowers.
1) The Capability that lets us do this is CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.  This Capability has a corresponding interface instance of ItemStackhandlerFlowerBag, which can store up to 16 ItemStacks.
2) Our CapabilityProvider is CapabilityProviderFlowerBag.  
  1) When CapabilityProviderFlowerBag::getCapability is called with the CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, it returns an ItemStackhandlerFlowerBag, which the caller can use to look in the bag or add more flowers.
  2) When an ItemFlowerBag is saved to disk, CapabilityProviderFlowerBag writes the ItemStackhandlerFlowerBag to NBT.  Likewise, when loading an ItemFlowerBag, CapabilityProviderFlowerBag reads the ItemStackhandlerFlowerBag out of NBT.
3) We attach the CapabilityProviderFlowerBag using ItemFlowerBag::initCapabilities
4) This creates a new instance of CapabilityProviderFlowerBag for each time that an ItemFlowerBag ItemStack is created 
5) Our CapabilityProviderFlowerBag does not actually create an ItemStackhandlerFlowerBag until the first time that someone calls CapabilityProviderFlowerBag::getCapability

In this example, we use a GUI to interact with the Capability via a Container:
The GUI talks to the Container, which manipulates the ItemStackHandlerFlowerBag that it obtained from the ItemFlowerBag.

You might wonder what the point of using a Capability is- you could instead just use method in your ItemFlowerBag class to keep its own ItemStackHandler as an NBT tag attached to the item, and handle the loading/saving to NBT directly.
The advantage of using the Capability is that other mods will be able to interact with the ItemStack to retrieve items from it.  For an ItemFlowerBag that might not be terribly useful (since there are no vanilla items which will ask other items if they can store objects), but if you are making a new block container of some sort, then it might allow vanilla objects to interact with your container.

ItemHandlerHelper


This example shows how to make an inventory block with added functionality similar to a furnace (with a few extra slots). It builds on example MBE30 for a simple inventory block.

The example will show you:

1. how to create a `Block` with a linked `TileEntity` that will store items permanently
1. how to set up a `ContainerType`, `Container`, and `ContainerScreen`, to add/remove items from the `TileEntity`.
1. how to synchronise the `Container between client and server
1. how to force block re-rendering and re-lighting when the TE state changes.

The TurboFurnace(TM) will appear in the Miscellaneous tab in the creative inventory.

The pieces you need to understand are located in:

* `StartupCommon` and `StartupClientOnly` - used for registering the various components
* `BlockInventoryFurnace`
* `TileEntityFurnace` - permanent storage of items (ie on disk), some logic for smelting
* `ContainerFurnace`  - used to collate items together into one place (eg from player inventory and furnace inventory)
* `ContainerScreenFurnace` - used to draw the GUI for the container
* `FurnaceZoneContents` - used to store the items which are in the input zone, fuel zone, and output zone
* `FurnaceStateData` - used to track the furnace state (cook time, burn time for each slot)
* `resources\assets\minecraftbyexample\textures\gui\mbe30_inventory_furnace_bg.png`

See ReadMeFurnaceDataFlow.png for a diagram of the dataflow between the classes.


The example uses some other resources as well for block rendering etc, these aren't the focus of this example, see examples mbe01, mbe02, mbe03 for more background.  This example doesn't do all of the furnace effects (eg smoke & flame). See the vanilla `FurnaceBlock.animateTick()` for that.

The key to understanding how this example works is to keep in mind the client-server synchronisation that is required. On the server, the data (items in slots, and burning/cooking info) are all stored in the `TileEntity`.  There is also a client TileEntity.

1. when loading the game from disk, the server must send the TileEntity information to the client.  This is done as follows:
    1. server calls `getUpdateTag()` to create an NBT containing the tileentity info. `writeToNBT()` is  used to create this NBT
    1. the packet is sent through the network to the client
    1. upon arrival at the client, it calls `handleUpdateTag()`, which then calls `readFromNBT()` which is used to populate the client TE data from the packet
1. when just one `tileentity` needs to be updated (not a whole chunk at once), then vanilla uses a different set of methods to do the synchronising:
    1. server calls `getUpdatePacket()` to create a packet containing the NBT info. `writeToNBT()` is used to create this NBT
    1. the packet is sent through the network to the client
    1. upon arrival at the client, it calls `onDataPacket()`, which then calls `readFromNBT()` which is used to populate the client TE data from the packet
    
The synchronisation for the GUI is different.  It takes place through two Containers, one on the server and one on the client:   
1. When the furnace GUI is opened, two Containers are created.  The Container on the server is linked to the TE. The Container on the client is NOT linked to a TE.  It is synchronised with the server container instead.  Manipulation of items in slots is handled automatically between the two containers, as well as other data such as fuel burn time, using `trackedInt`s. 
If more than one player has the furnace open at the same time, the information is sent to each of them.
1. When the items in the slots change on the server, the `tileentity` is marked as "dirty" so that the updated information will be sent to all clients, to make sure they stay in synch, regardless of whether they have the container open or not. Caching where possible will help reduce the amount of network traffic and CPU time.

Useful vanilla classes for further info: `AbstractFurnaceBlock`, `AbstractFurnaceTileEntity`, `AbstractFurnaceContainer`, `AbstractFurnaceScreen`

See also
* Background information on [Containers](http://greyminecraftcoder.blogspot.com/2020/04/containers-1144.html)

## Futher notes
Depending on your container, there are a few other interfaces you might want to implement-
IRecipeHolder, IRecipeHelperPopulator, IRecipeShownListener, RecipeBookContainer - for vanilla's recipe helper
ISidedInventoryProvider - if you want a hopper to be able to draw items from your furnace

## Common errors

* "Array index out of bounds"--you have messed up the mapping of slot numbers to inventories using addSlotToContainer
* Items appear in strange spots, or can't click on slots--you have messed up the [x,y] positions of the slots; you may have forgotten to offset by guiLeft and guiTop
* Items disappear after loading and reloading--your NBT read/write code is wrong / mismatched
* Items don't work properly in their slots (eg won't burn)--the order you added the slots to your container using `addSlotToContainer` doesn't match the order of the items in the `TileEntityFurnace` `itemstack` array
* Container doesn't synchronise properly when loading game--`getUpdatePacket()`, `getUpdateTag()`, `onDataPacket()`, and/or `handleUpdateTag()` are probably wrong
