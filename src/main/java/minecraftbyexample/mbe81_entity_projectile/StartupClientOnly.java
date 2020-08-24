package minecraftbyexample.mbe81_entity_projectile;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.SpriteRenderer;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

/**
 * User: The Grey Ghost
 * Date: 24/12/2014
 *
 * The Startup classes for this example are called during startup
 *  See MinecraftByExample class for more information
 */
public class StartupClientOnly
{
  /**
   * @param event
   */
  @SubscribeEvent
  public static void onClientSetupEvent(FMLClientSetupEvent event) {
    // Register the custom renderer for our entity
    RenderingRegistry.registerEntityRenderingHandler(StartupCommon.emojiEntityType, new emojiRenderFactory());
    // I've written out the factory as an explicit implementation to make it clearer, but most folks would just use a lambda instead, eg
    //    RenderingRegistry.registerEntityRenderingHandler(StartupCommon.emojiEntityType,
    //            erm -> new SpriteRenderer<>(erm, Minecraft.getInstance().getItemRenderer()));
  }

  // I've written this out as an explicit implementation to make it clearer, but most folks would just use a lambda instead
  private static class emojiRenderFactory implements IRenderFactory<EmojiEntity> {
    @Override
    public EntityRenderer<? super EmojiEntity> createRenderFor(EntityRendererManager manager) {
      ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
      return new SpriteRenderer<>(manager, itemRenderer);
    }
  }

  // I've written this out as an explicit implementation to make it clearer, but most folks would just use a lambda instead
  private static class emojiRenderFactory implements IRenderFactory<EmojiEntity> {
    @Override
    public EntityRenderer<? super EmojiEntity> createRenderFor(EntityRendererManager manager) {
      ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
      return new SpriteRenderer<>(manager, itemRenderer);
    }
  }


}


