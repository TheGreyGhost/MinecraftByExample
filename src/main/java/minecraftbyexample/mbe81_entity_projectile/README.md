# MBE81_ENTITY_PROJECTILE

This example shows you how to create simple projectile entities- 
1) Throwable entities - vanilla examples are Snowball, Egg
2) Other IProjectiles - vanilla examples are Arrow and LLamaSpit

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
If you use Blender to generate wavefront (obj) files, you need to make sure that all faces are either quads or triangles (no "n-gons" > 4).
If your obj file contains lines like this, you have a problem...
f   60/7/2 150/8/2 157/9/2  15/10/2                 // 4 points = ok
f 153/19/2 68/20/2 87/21/2 170/22/2                 // 4 points = ok
f  75/1/1  150/2/1  60/3/1   11/4/1 59/5/1 151/6/1  // 6 points = will not render
The easiest way to fix it is to select all faces (in edit mode) then:
1) Triangulate faces; then
2) Tris to Quads<br>

Google is your friend here!

Alternatively you can use BlockBench in "entity model" and export it as wavefront.

Personally I find BlockBench easier to use when making the "blocky" style of minecraft models, and it's much easier to learn than Blender. 

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
