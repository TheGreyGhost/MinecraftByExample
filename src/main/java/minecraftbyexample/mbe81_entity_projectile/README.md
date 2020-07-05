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

## Common errors
* My entity doesn't appear on the client; no errors in the console<br>
You may have incorrectly implemented createSpawnPacket for your Entity

* My entity doesn't appear on the client; there is a renderinghandler not found error in the console<br>
Your renderer is not registered properly. 


