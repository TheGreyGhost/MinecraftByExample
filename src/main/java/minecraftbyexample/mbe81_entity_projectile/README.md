# MBE81_ENTITY_PROJECTILE

This example shows you how to create simple projectile entities- 
1) Throwable entities - vanilla examples are Snowball, Egg
2) Other Projectiles - vanilla examples are Arrow and LLamaSpit (both are IProjectiles)

-------EmojiEntity (mbe81a) -------<br>
This can be thrown like a snowball, randomly producing either a happy face projectile or a grumpy face projectile.   The Entity contains an ItemStack which is used to render the projectile using a SpriteRenderer, i.e. it just draws the icon of the item, rotated to directly face the viewer.  When the projectile hits its target, it applies a potion effect.

The pieces you need to understand are located in:

* `StartupClientOnly`, `StartupCommon`
* `EmojiItem`
* `EmojiEntity`
* `resources\assets\minecraftbyexample\models\item\mbe81a_emoji_***_registry_name` -- defines the appearance of the item when held and when in flight
* `resources\assets\minecraftbyexample\textures\item\mbe81a_***.png` -- texture of the item icon and the rendered projectile in flight

## Key points to note:
You need to have:
* RenderingRegistry.registerEntityRenderingHandler
* public IPacket<?> createSpawnPacket() {return NetworkHooks.getEntitySpawningPacket(this);}
*   public static void onEntityTypeRegistration(RegistryEvent.Register<EntityType<?>> entityTypeRegisterEvent) {
If you forget any of these or get them incorrect, your code won't work and might fail silently.

-------BoomerangEntity (mbe81b) -------<br>
This entity is a projectile that demonstrates two different ways of controlling the flight path of a projectile:
1) Pre-programmed flight path (follow a fixed path in [x,y,z] coordinates)
2) Ballistic motion, i.e. manipulate the velocity [vx,vy,vz] (called "motion" in the code) and adjust the position [x,y,z] every tick based
   on the velocity.

The usage is as follows:
1) The player holds a boomerang item in their hand
2) The player holds down the right mouse button to "charge up" their throw, similar to a bow with arrow
3) When the player releases the right mouse button, a boomerang entity is spawned.  For short charge-up time, the speed is slow and the boomerang does not travel far.  For long charge-up time, the speed is fast and the boomerang travels further.
4) The boomerang entity starts off in "in flight" mode - it follows a pre-determined curved flight path (see boomerang_flight_path.png).  If the boomerang doesn't hit anything, it will curve back around to return to the player, who will catch it.
5) If the flight path of the boomerang is interrupted, eg a) it hits something; or b) the player has moved and does not catch it;  then the boomerang changes to "not in flight" mode and behaves like a thrown object (falls to the ground)
6) If the boomerang hits a block, it breaks the block.  Each time it breaks a block, it loses some of its momentum.  When the boomerang has no momentum left, it stops flying (bounces off the block).
7) If the boomerang hits an entity, it causes damage to the entity and stops flying (bounces off)
<br>
The entity can be enchanted with a number of different enchantments similar to bows, arrows, weapons.<br>
<br>
The model for the BoomerangEntity is a wavefront object;  Blockbench is also suitable for generating obj files.<br>
<br>
If you use Blender to generate wavefront (obj) files, you need to make sure that all faces are either quads or triangles (no "n-gons" > 4).<br>
If your obj file contains lines like this, you have a problem...<br>
f   60/7/2 150/8/2 157/9/2  15/10/2                 // 4 points = ok<br>
f 153/19/2 68/20/2 87/21/2 170/22/2                 // 4 points = ok<br>
f  75/1/1  150/2/1  60/3/1   11/4/1 59/5/1 151/6/1  // 6 points = will not render<br>
The easiest way to fix it is to select all faces (in edit mode) then:<br>
1) Triangulate faces; then
2) Tris to Quads<br>
<br>
Google is your friend here!<br>
<br>
Alternatively you can use BlockBench in "entity model" and export it as wavefront.<br>
<br>
Personally I find BlockBench easier to use when making the "blocky" style of minecraft models, and it's much easier to learn than Blender. <br>
<br>
The pieces you need to understand are located in:

* `StartupClientOnly`, `StartupCommon`
* `BoomerangItem`
* `BoomerangEntity`
* `BoomerangRenderer`
* `resources\assets\minecraftbyexample\models\item\mbe81b_boomerang_charge_` -- slowly raises the height of the item while it is "charging up"
* `resources\assets\minecraftbyexample\models\item\mbe81b_boomerang_registry_name_` -- defines the appearance of the item when held
* `resources\assets\minecraftbyexample\textures\item\mbe81a_***.png` -- texture of the item icon and the rendered projectile in flight

* `resources\assets\minecraftbyexample\models\entity\mbe81b_boomerang_wrapper.json` -- entity model wrapper for the wavefront model
* `resources\assets\minecraftbyexample\models\entity\mbe81b_boomerang.obj` -- the wavefront model for the boomerang
* `resources\assets\minecraftbyexample\models\entity\mbe81b_boomerang.mtl` -- the waverfront material file, contains the path to the texture 
map_Kd minecraftbyexample:model/mbe81b_boomerang_texture
* `resources\assets\minecraftbyexample\textures\model\mbe81b_boomerang_texture.png` -- the texture for the boomerang

## Common errors
* My entity doesn't appear on the client; no errors in the console<br>
You may have incorrectly implemented createSpawnPacket for your Entity

* My entity doesn't appear on the client; there is a renderinghandler not found error in the console<br>
Your renderer is not registered properly. 

Useful tips for debugging entity rendering:
1) Does the entity exist on the client?  Put a breakpoint into YourEntity::tick()
2) Press F3+B to show entity outline (hitbox) and facing direction
3) WorldRenderer::updateCameraAndRender() breakpoint at the iprofiler.endStartSection("entities");
--> entity is present, is associated with the correct renderer, is within the viewing frustrum, is at the [x,y,z] that you expect
