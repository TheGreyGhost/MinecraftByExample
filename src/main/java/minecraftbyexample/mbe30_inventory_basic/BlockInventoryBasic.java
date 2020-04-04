package minecraftbyexample.mbe30_inventory_basic;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ContainerBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemGroup;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.CampfireTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.block.BlockRenderType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;

/**
 * User: brandon3055
 * Date: 06/01/2015
 *
 * BlockInventoryBasic is a simple inventory capable of storing 9 item stacks. The block itself doesn't do much more
 * then any regular block except create a tile entity when placed, open a gui when right clicked and drop tne
 * inventory's contents when harvested. The actual storage is handled by the tile entity.
 */

//Note that in 1.10.*, extending BlockContainer can cause rendering problems if you don't extend getRenderType()
// If you don't want to extend BlockContainer, make sure to add the tile entity manually,
//   using hasTileEntity() and createTileEntity().  See BlockContainer for a couple of other important methods you may
//  need to implement.

public class BlockInventoryBasic extends ContainerBlock
{
	public BlockInventoryBasic()
	{
		super(Block.Properties.create(Material.ROCK)
          );
	}

  /**
   * Create the Tile Entity for this block.
   * If your block doesn't extend BlockContainer, use createTileEntity(World worldIn, IBlockState state) instead
   * Not needed if your block implements ITileEntityProvider (in this case implemented by BlockContainer), but it
   *   doesn't hurt to include it anyway...
   * @return
   */
  @Override
  public TileEntity createTileEntity(BlockState state, IBlockReader world) {
    return createNewTileEntity(world);
  }
  @Nullable
  @Override
  public TileEntity createNewTileEntity(IBlockReader worldIn) {
    return null;
  }

  // not needed if your block implements ITileEntityProvider (in this case implemented by BlockContainer), but it
  //  doesn't hurt to include it anyway...
	@Override
	public boolean hasTileEntity(BlockState state)
	{
		return true;
	}

	// Called when the block is right clicked
	// We use it to open the block gui when right clicked by a player
  // Copied from ChestBlock
	@Override
	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult rayTraceResult) {
    if (worldIn.isRemote) return ActionResultType.SUCCESS; // on client side, don't do anything

    INamedContainerProvider namedContainerProvider = this.getContainer(state, worldIn, pos);
    if (namedContainerProvider != null) {
      if (!(player instanceof ServerPlayerEntity)) return ActionResultType.FAIL;  // should always be true, but just in case...
      ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity)player;
      NetworkHooks.openGui(serverPlayerEntity, namedContainerProvider, (packetBuffer)->{});
    }
    return ActionResultType.SUCCESS;
	}

  // This is where you can do something when the block is broken. In this case drop the inventory's contents
  // Code is copied directly from vanilla eg ChestBlock, CampfireBlock
  public void onReplaced(BlockState state, World world, BlockPos blockPos, BlockState newState, boolean isMoving) {
    if (state.getBlock() != newState.getBlock()) {
      TileEntity tileentity = world.getTileEntity(blockPos);
      if (tileentity instanceof TileEntityInventoryBasic) {
        TileEntityInventoryBasic tileEntityInventoryBasic = (TileEntityInventoryBasic)tileentity;
        tileEntityInventoryBasic.dropAllContents(world, blockPos);
      }
//      worldIn.updateComparatorOutputLevel(pos, this);  if the inventory is used to set redstone power for comparators
      super.onReplaced(state, world, blockPos, newState, isMoving);  // call it last, because it removes the TileEntity
    }
	}

	//---------------------------------------------------------

  // render using a BakedModel (mbe30_inventory_basic.json --> mbe30_inventory_basic_model.json)
  // required because the default (super method) is INVISIBLE for BlockContainers.
  @Override
  public BlockRenderType getRenderType(BlockState iBlockState) {
    return BlockRenderType.MODEL;
  }
}
