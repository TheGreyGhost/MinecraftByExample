# MBE65_CAPABILITY

This example shows you how to use the Forge extension "Capability"

A Capability is a way to attach information and behaviours to minecraft objects (eg Entity, ItemStack, TileEntity) at run time.  It is an example of a "Decorator" design pattern [link](https://www.geeksforgeeks.org/decorator-pattern/?ref=lbp).  Using Capability has a number of advantages compared to other coding methods:
* It lets you easily attach information to vanilla objects or objects that other mods have added
* For your own objects, your object's class doesn't need to implement interfaces or extend methods.  This helps avoid the monster "god classes" that vanilla is cursed with, such as Entity, Item, or Block
* It is very easy to add or remove Capabilities in any combination you want without having to refactor the objects that you're attaching them to. 
* You can use Capabilities which have been defined by other mods (for example: a "magic mana" power source), without having to statically link them: if the magic_mana mod is installed, your mod can access its capability, but if the magic_mana mod is not installed, then your mod will still function correctly.   

On the down side:
* Capabilities are not as efficient as interfaces and methods
* they can be more complicated to set up initially
* if you need the Capability information on the client (eg for rendering) then usually they need to be manually synchronised to the client.

You can attach capability to most vanilla objects- if you look for classes which implement ICapabilityProvider you can see the complete list.  These classes currently are:
* Entity
* ItemStack
* TileEntity
* World
* Dimension
* Chunk

Capabilities can be added in one of three ways:
1) If you are extending a base object which already implements ICapabilityProvider (eg an Entity, or a TileEntity) then you can override the getCapability() method directly.
2) If you are adding a new Item, you can override the initCapability() method to return an ICapabilityProvider, which is then attached to the ItemStack instance whenever one is created. 
3) Otherwise- (eg for attaching to vanilla objects) use AttachCapabilityEvent to specify an ICapabilityProvider 

This example uses two different Capabilities, each of which can be attached to ItemStacks and Entities.  The gameplay mechanic is:
1) There are two projectile weapons: Elemental Air crossbow, and Elemental Fire bow
2) When the player shoots an Entity with one of the bows, the entity takes no damage but becomes tagged with that element.
3) An entity tagged with Elemental Air gets levitated up into the air a short distance.  Repeated shots with the Air bow causes the magnitude of the effect to increase each time.
4) An entity tagged with Elemental Fire receives a speed boost.  Repeated shots with the Fire bow increases the amount of speed boost.
5) If an entity is tagged with both Fire and Air, it will immediately explode.

The algorithm to achieve this is:
1) There are two Capability types: Fire and Air
2) The Elemental Air crossbow has the Air capability, and the Elemental Fire bow has the Fire capability
3) When a bow is fired, it tags the fired arrow ItemStack with either Air or Fire
4) When the arrow strikes an Entity, it tags the Entity with the Air or Fire Capability as appropriate

See CapabilityAttachedToEntity.png for a diagram of how the capability is attached for this example.

In summary:
1) We define a Capability such as CAPABILITY_ELEMENTAL_FIRE
2) We create an Interface for that capability (either abstract or concrete), eg ElementalFire 
2) We define a CapabilityProvider for an entity, which answers the question "do you have CAPABILITY_ELEMENTAL_FIRE? " with "yes, here is the ElementalFire you should use to access it"

See also MBE32 for another example of Capabilities.

The pieces you need to understand are located in:

* `StartupCommon` - used for registering the various components
* `ItemXXX` - used to impart elemental energy to the arrow
* `ElementalAir / ElementalFire` - the class used to actually store the air / fire charge level
* `CapabilityElementalXXX` - The type of capability 
* `CapabilityProviderXXX` - used to specify which capabilities are relevant for the object that it's attached to  

## Common errors

* Capabilities don't retain information during saving & reloading --your NBT read/write code is wrong / mismatched


### Capabilities
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

You might wonder what the point of using a Capability is- you could instead just use a method in your ItemFlowerBag class to keep its own ItemStackHandler as an NBT tag attached to the item, and handle the loading/saving to NBT directly.
The advantage of using the Capability is that other mods will be able to interact with the ItemStack to retrieve items from it.  For an ItemFlowerBag that might not be terribly useful (since there are no vanilla items which will ask other items if they can store objects), but if you are making a new block container of some sort, then it might allow vanilla objects to interact with your container.

### Synchronising Capabilities from server to client
By default, capability information attached to an object is only valid on the server; it is not transmitted to the client.  If you want to access capability information on the client, you must synchronise it manually:
1) For Entity - use a custom packet or use an EntityDataManager with DataParameter
2) For World, Dimension or Chunk - use a custom packet 
3) For TileEntity- implement getUpdatePacket(), getUpdateTag(), onDataPacket(), and handleUpdateTag() to serialise+deserialise your capability
4) For ItemStack: for a vanilla Item: use a custom packet.  For one of your own Items: override Item.getShareTag() and readShareTag() to transmit your capability 

### For more information about capabilities:
https://gist.github.com/williewillus/c8dc2a1e7963b57ef436c699f25a710d

https://mcforge.readthedocs.io/en/latest/datastorage/capabilities/

Some notes on using capabilities defined by other mods:
* You use @CapabilityInject to inject the other mod's capability. You can put this annotation on a method too (see the javadocs) and that method will then only be called if the other mod is present.
* In that method you can then enable your mod's compatibility features. If you put this method in a separate class, that class will never load unless the other mod is present.
* You include the mod's source as a dependency at build time (so that your code knows how to access the interface), but you don't need to ship the other mod with yours.
* Beware - You need to "shield" the part of your code that uses the code from the other mod. You cannot reference it from other parts of your code, only from that @CapabilityInject method. 


