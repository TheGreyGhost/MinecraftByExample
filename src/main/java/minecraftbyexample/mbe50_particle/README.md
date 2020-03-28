# MBE50_PARTICLE

This example shows you how to use some simple Particles (formerly called "EntityFX"). Vanilla examples are the love hearts when taming an animal; the smoke coming from a burning torch; the sparkles from a portal; see `ParticleTypes` and `ParticleManager` for more examples.

The example is a block (FlameEmitter) which, when you place it in the world:

1. spits out random vanilla lava particles
1. if any mobs are nearby--fires a string of fireballs at the mob's head. Otherwise, fires a string of fireballs straight upwards. The fireballs are created using a custom texture and have customised movement / aging.  The fireball size (diameter) and tint (colour) are varied depending on where the block is place.

The pieces you need to understand are located in:

* `StartupClientOnly`, `StartupCommon`
* `BlockFlameEmitter`
* `FlameParticleType, FlameParticleData, FlameParticleFactory, FlameParticle (see Background Information below)
* `resources\assets\minecraftbyexample\particles\mbe50_flame_particle_type_registry_name` -- points to texture files for the particle
* `resources\assets\minecraftbyexample\textures\particle\mbe50_flame_fx.png` -- texture of the particle

The example also uses the following files to define the appearance of the block, see mbe02 for more background information:
* `resources\assets\minecraftbyexample\blockstates\mbe50_block_flame_emitter_registry_name` -- for the blockstate definition
* `resources\assets\minecraftbyexample\models\block\mbe50_block_flame_emitter_registry_name` -- for the model used to render the block
* `resources\assets\minecraftbyexample\lang\en_US.lang` -- for the displayed name of the block

The block will appear in the Blocks tab in the creative inventory.

See also
* Lots of useful info on particles [here](https://gist.github.com/williewillus/353c872bcf1a6ace9921189f6100d09a)
* Vanilla list of particles [here](https://minecraft.gamepedia.com/Particles)

## Common errors

* spawning the Particle on the server side instead of client only.
* messing up the coordinate settings and/or movement, so that the Particle is rendered out of the player's view
* `NoClassDefFoundError` when you install your mod on a dedicated server:   you have accidentally used client-only code in a part of your code that is invoked on the dedicated server. See [http://greyminecraftcoder.blogspot.com.au/2013/11/how-forge-starts-up-your-code.html](http://greyminecraftcoder.blogspot.com.au/2013/11/how-forge-starts-up-your-code.html)
* Infinite loop in the loading screen: Vanilla is throwing an exception while loading your particle / texture; see ParticleManager.loadTextureLists.  Might be caused by incorrect use of registerFactory method (see StartupClientOnly::onParticleFactoryRegistration) for more explanation
* particles have the wrong appearance / render strangely - using the wrong RenderType 

## Background Information
Taken from [WillieWillus primer](https://gist.github.com/williewillus/353c872bcf1a6ace9921189f6100d09a)

###Particles
Particles are now handled in a much more complete and cohesive manner. There are two components to the particle system, the logical side (teaching the game about your particle, how to spawn it, etc.) and the rendering side (teaching the game how to render your particle).

###Logical Particles
IParticleType
IParticleType describes the general "type" of the particle (e.g. "redstone dust"). This class is similar to Block and Item, you subclass it and register it on both sides using the standard Forge registry events (RegistryEvent.Register<IParticleType>).

###IParticleData
This class is to IParticleType as ItemStack is to Item. That is, IParticleType describes the general type of the particle, while IParticleData describes a specific instance of it (e.g. "redstone dust with mint green color"). It's the unit of handling for particles, and is what you pass to world.addParticle to actually spawn stuff.

Subclasses of this type carry additional data that describe a particle's appearance. For example, vanilla's ItemParticleData, used for showing the item pickup animation, holds the ItemStack being picked up, and RedstoneParticleData, as mentioned above, holds the color of the particle. If you're implementing your own data, you'll have to teach the game how to write/read it from the network PacketBuffer, as well as how to read it from the command line (to support the /particle command). See vanilla for examples.

If your particle needs no additional data, then just use BasicParticleType, a convenience class provided by vanilla that implements both IParticleType and IParticleData as a singleton that writes and reads nothing from the network. The vast majority of vanilla particles in ParticleTypes use this.

###Particle
The Particle class is what actually represents a moving particle in game. It's client-side only and has a tick method for you to do custom movement, etc.

###Particle Factories
The above two classes are all that's needed serverside -- the server only needs to know the particle type, the additional data, and how to get it onto the network. On the client, we have to concern ourselves with how to get from the IParticleData instance to a subclass of Particle, the actual particle type. This is the job of IParticleFactory. During ParticleFactoryRegisterEvent, you must inform the game by calling Minecraft.getInstance().particleManager.registerFactory(yourType, yourIParticleFactory). The interface only has one method, which receives the world, position, motion, and IParticleData (basically everything you passed to world.addParticle) and returns a Particle. The code here depends purely on your own needs, and can be as simple as just constructing your Particle subclass and passing the IParticleData to it. Again, see vanilla for examples.

###Example: Botania
Botania wisps uses vanilla's systems for the above, though it renders the particles on its own (so the next section doesn't apply). The particle type class is here, and the data carried by each particle is here. As you can see, the factory literally just pulls data out of the data class and passes it into the particle type FXWisp. To spawn it, I call a WispParticleData.wisp helper, then pass it to world.addParticle. Example. This also demonstrates that your IParticleData should be immutable, and if so, you can reuse it as many times as you want.

###Rendering Particles
Particle JSON
That's most of the story done, but now we want to actually render the particle. First off, note that for any of your IParticleType, you need a particle json corresponding to it. For botania:wisp, there exists assets/botania/particles/wisp.json with a json object inside. This is required even if you aren't using vanilla's animated sprite system.

###AnimatedSprite system
What's the animated sprite system? It's the vanilla way of having "default-looking" particles (billboarded flat textures, possibly switching between multiple textures during the particle's lifetime. For an example, view the json and code for particle type minecraft:poof (/assets/minecraft/particles/poof.json). You can see that it has a list of particle textures, which will be loaded and stitched by the game on startup. Also observe that vanilla's registerFactory call for POOF is a weird overload that requires a function that takes an IAnimatedSprite and itself produces a factory. That IAnimatedSprite is the code representation of the list of textures in the json. IAnimatedSprite has two methods, one to select a texture randomly, and one to select a texture based on how old the particle is. This is how for example vanilla's dust particles fade away into fewer pixels, by switching textures based on particle age. This all sounds super abstract but if you just follow what vanilla does for POOF from the factory all the way to the PoofParticle constructor and tick methods (especially what it does with the IAnimatedSprite every tick), it'll be pretty clear what's going on. Subclass from SpriteTexturedParticle, create the json, and call registerFactory, and update the sprite every tick as necessary. The actual rendering is pretty much completely handled by vanilla. Note that you don't have to have multiple textures to use this system, all "default-looking" vanilla particles use this, even if they have only one texture.

###Doing your own rendering
Simply override renderParticle and feed your vertex data into the passed BufferBuidler (or do your own GL rendering for IParticleRenderType.CUSTOM). Note that you'll have to pick a IParticleRenderType which determines the GL state under which your particle renders, or create your own implementation.

###Spawning Particles
A couple notes on spawning particles. The World.addParticle methods only work clientside, meaning that they do not communicate over the network and calling them on the logical server does nothing. To actually send a message, use the ServerWorld.addParticle overloads, which actually build a packet with your serialized IParticleData and send it to the client, which will then deserialize it , call the factory, and render it.

Of course, if you're spawning tens to hundreds of particles, it would be prudent to send a single custom packet to the client and have the for loop 1 to 100 clientside, instead of looping server side and sending 100 particle packets, wasting bandwidth.

Additionally note that after all this, your particle should be completely supported by the /particle vanilla command, provided you implemented the necessary method in your IParticleData Deserializer.

###Miscellaneous notes
Vanilla also has EmitterParticle which is a particle which is "attached" to a player and emits other particles.
