package minecraftbyexample.mbe65_capability;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ArmorStandEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * Created by TGG on 20/06/2020.
 *
 * AttachCapabilitiesEvent<Entity>: Fires only for entities.
 * AttachCapabilitiesEvent<TileEntity>: Fires only for tile entities.
 * AttachCapabilitiesEvent<ItemStack>: Fires only for item stacks.
 * AttachCapabilitiesEvent<World>: Fires only for worlds.
 * AttachCapabilitiesEvent<Chunk>: Fires only for chunks.
 *
 */
public class CapabilityAttachEventHandler {

  @SubscribeEvent
  public static void attachCapabilityToEntityHandler(AttachCapabilitiesEvent<Entity> event) {
    Entity entity = event.getObject();
    if (entity instanceof AbstractArrowEntity
        || entity instanceof LivingEntity        ) {
      event.addCapability(new ResourceLocation("minecraftbyexample:mbe65_capability_provider_entities") , new CapabilityProviderEntities());
    }
  }
}
