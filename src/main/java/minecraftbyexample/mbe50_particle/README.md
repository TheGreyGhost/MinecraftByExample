# MBE50_PARTICLE

This example shows you how to use some simple Particles (formerly called "EntityFX"). Vanilla examples are the love hearts when taming an animal; the smoke coming from a burning torch; the sparkles from a portal; see `EnumParticleTypes` for more examples.

The example is a block (FlameEmitter) which, when you place it in the world:

1. spits out random vanilla lava particles
1. if any mobs are nearby--fires a string of fireballs at the mob's head. Otherwise, fires a string of fireballs straight upwards. The fireballs are created using a custom texture and have customised movement / aging.

The pieces you need to understand are located in:

* `StartupClientOnly`, `StartupCommon`
* `BlockFlameEmitter`
* `FlameFX`
* `TextureStitcherBreathFX`
* `resources\assets\minecraftbyexample\lang\en_US.lang` -- for the displayed name of the block

The example also uses the following files to define the appearance of the block, see mbe02 for more background information:

* `resources\assets\minecraftbyexample\blockstates\mbe50_block_flame_emitter` -- for the blockstate definition
* `resources\assets\minecraftbyexample\models\block\mbe50_block_flame_emitter` -- for the model used to render the block

The block will appear in the Blocks tab in the creative inventory.

## Common errors

* spawning the Particle on the server side instead of client only.
* messing up the coordinate settings and/or movement, so that the Particle is rendered out of the player's view
* `NoClassDefFoundError` when you install your mod on a dedicated server:   you have accidentally used client-only code in a part of your code that is invoked on the dedicated server. See [http://greyminecraftcoder.blogspot.com.au/2013/11/how-forge-starts-up-your-code.html](http://greyminecraftcoder.blogspot.com.au/2013/11/how-forge-starts-up-your-code.html)
