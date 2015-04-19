package minecraftbyexample.mbe04_block_smartblockmodel;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Created by TheGreyGhost on 19/04/2015.
 */
public class ModelBakeEventHandler {
  public static final ModelBakeEventHandler instance = new ModelBakeEventHandler();

  private ModelBakeEventHandler() {};

  @SubscribeEvent
  public void onModelBakeEvent(ModelBakeEvent event)
  {
    TextureAtlasSprite base = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("minecraft:blocks/slime");
    TextureAtlasSprite overlay = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("minecraft:blocks/redstone_block");
    IBakedModel customModel = new CustomModel(base, overlay);
    event.modelRegistry.putObject(ClientProxy.blockLocation, customModel);
    event.modelRegistry.putObject(ClientProxy.itemLocation, customModel);
  }
}
