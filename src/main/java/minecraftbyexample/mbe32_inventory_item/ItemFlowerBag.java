/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Mar 16, 2015, 6:43:33 PM (GMT)]
 */
package minecraftbyexample.mbe32_inventory_item;

import minecraftbyexample.mbe30_inventory_basic.ContainerBasic;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.play.server.SCollectItemPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.*;
import net.minecraftforge.items.wrapper.InvWrapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ItemFlowerBag extends Item {

  private static final int MAXIMUM_NUMBER_OF_FLOWER_BAGS = 1;

	public ItemFlowerBag() {
    super(new Item.Properties().maxStackSize(MAXIMUM_NUMBER_OF_FLOWER_BAGS).group(ItemGroup.MISC) // the item will appear on the Miscellaneous tab in creative
    );
	}

  /**
   * When the player right clicks while holding the bag, open the inventory screen
   * @param world
   * @param player
   * @param hand
   * @return the new itemstack
   */
	@Nonnull
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, @Nonnull Hand hand) {
    ItemStack stack = player.getHeldItem(hand);
    if (!world.isRemote) {  // server only!
			INamedContainerProvider containerProviderFlowerBag = new ContainerProviderFlowerBag(this, stack);
			final int NUMBER_OF_FLOWER_SLOTS = 16;
			NetworkHooks.openGui((ServerPlayerEntity) player,
                           containerProviderFlowerBag,
                           (packetBuffer)->{packetBuffer.writeInt(NUMBER_OF_FLOWER_SLOTS);});
      // We use the packetBuffer to send the bag size; not necessary since it's always 16, but just for illustration purposes
		}
		return ActionResult.resultSuccess(stack);
	}

  /**
   *  If we use the item on a block with a ITEM_HANDLER_CAPABILITY, automatically transfer the entire contents of the flower bag
   *     into that block
   *  onItemUseFirst is a forge extension that is called before the block is activated
   *  If you use onItemUse, this will never get called for a container because the container will capture the click first
   * @param ctx
   * @return
   */
	@Nonnull
	@Override
	public ActionResultType onItemUseFirst(ItemStack stack, ItemUseContext ctx) {
	  World world = ctx.getWorld();
    if (world.isRemote()) return ActionResultType.PASS;

    BlockPos pos = ctx.getPos();
		Direction side = ctx.getFace();
		ItemStack itemStack = ctx.getItem();
		if (!(itemStack.getItem() instanceof ItemFlowerBag)) throw new AssertionError("Unexpected ItemFlowerBag type");
		ItemFlowerBag itemFlowerBag = (ItemFlowerBag)itemStack.getItem();

		TileEntity tileEntity = world.getTileEntity(pos);

		if (tileEntity == null) return ActionResultType.PASS;
    if (world.isRemote()) return ActionResultType.SUCCESS; // always succeed on client side

    // check if this object has an inventory- either Forge capability, or vanilla IInventory
    IItemHandler tileInventory;
    LazyOptional<IItemHandler> capability = tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, side);
    if (capability.isPresent()) {
      tileInventory = capability.orElseThrow(AssertionError::new);
    } else if (tileEntity instanceof IInventory) {
      tileInventory = new InvWrapper((IInventory)tileEntity);
    } else {
      return ActionResultType.FAIL;
    }

    // go through each flower ItemStack in our flower bag and try to insert as many as possible into the tile's inventory.
    ItemStackHandlerFlowerBag itemStackHandlerFlowerBag =  itemFlowerBag.getItemStackHandlerFlowerBag(itemStack);
    for (int i = 0; i < itemStackHandlerFlowerBag.getSlots(); i++) {
      ItemStack flower = itemStackHandlerFlowerBag.getStackInSlot(i);
      ItemStack flowersWhichDidNotFit = ItemHandlerHelper.insertItemStacked(tileInventory, flower, false);
      itemStackHandlerFlowerBag.setStackInSlot(i, flowersWhichDidNotFit);
    }

    tileEntity.markDirty();           // make sure that the tileEntity knows we have changed its contents
    return ActionResultType.SUCCESS;
	}

   // ------  Code used to generate a suitable Container for the contents of the FlowerBag

  /**
   * Uses an inner class as an INamedContainerProvider.  This does two things:
   *   1) Provides a name used when displaying the container, and
   *   2) Creates an instance of container on the server which is linked to the ItemFlowerBag
   * You could use SimpleNamedContainerProvider with a lambda instead, but I find this method easier to understand
   * I've used a static inner class instead of a non-static inner class for the same reason
   *
   */
  private static class ContainerProviderFlowerBag implements INamedContainerProvider {
    public ContainerProviderFlowerBag(ItemFlowerBag itemFlowerBag, ItemStack itemStackFlowerBag) {
      this.itemStackFlowerBag = itemStackFlowerBag;
      this.itemFlowerBag = itemFlowerBag;
    }

    @Override
    public ITextComponent getDisplayName() {
      return itemStackFlowerBag.getDisplayName();
    }

    /**
     * The name is misleading; createMenu has nothing to do with creating a Screen, it is used to create the Container on the server only
     */
    @Override
    public ContainerFlowerBag createMenu(int windowID, PlayerInventory playerInventory, PlayerEntity playerEntity) {
      ContainerFlowerBag newContainerServerSide =
              ContainerFlowerBag.createContainerServerSide(windowID, playerInventory,
                      itemFlowerBag.getItemStackHandlerFlowerBag(itemStackFlowerBag),
                      itemStackFlowerBag);
      return newContainerServerSide;
    }

    private ItemFlowerBag itemFlowerBag;
    private ItemStack itemStackFlowerBag;
  }


  // ---------------- Code related to Capabilities
  //

  // The CapabilityProvider returned from this method is used to specify which capabilities the ItemFlowerBag has
  @Nonnull
  @Override
  public ICapabilityProvider initCapabilities(ItemStack stack, CompoundNBT oldCapNbt) {

    return new CapabilityProviderFlowerBag();
  }

  /**
   * Retrieves the ItemStackHandlerFlowerBag for this itemStack (retrieved from the Capability)
   * @param itemStack
   * @return
   */
  private ItemStackHandlerFlowerBag getItemStackHandlerFlowerBag(ItemStack itemStack) {
    IItemHandler flowerBag = itemStack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElse(null);
    if (flowerBag == null || !(flowerBag instanceof ItemStackHandlerFlowerBag)) {
      LOGGER.error("ItemFlowerBag did not have the expected ITEM_HANDLER_CAPABILITY");
      return new ItemStackHandlerFlowerBag(1);
    }
    return (ItemStackHandlerFlowerBag)flowerBag;
  }

  private static final Logger LOGGER = LogManager.getLogger();
}
