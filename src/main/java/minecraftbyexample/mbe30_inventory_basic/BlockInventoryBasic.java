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
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
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

public class BlockInventoryBasic extends ContainerBlock
{
	public BlockInventoryBasic()
	{
		super(Block.Properties.create(Material.ROCK)
          );
	}

  /**
   * Create the Tile Entity for this block.
   * Forge has a default but I've included it anyway for clarity
   * @return
   */
  @Override
  public TileEntity createTileEntity(BlockState state, IBlockReader world) {
    return createNewTileEntity(world);
  }
  @Nullable
  @Override
  public TileEntity createNewTileEntity(IBlockReader worldIn) {
    return new TileEntityInventoryBasic();
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
            // (packetBuffer)->{} is just a do-nothing because we have no extra data to send
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

  // returns the shape of the block:
  //  The image that you see on the screen (when a block is rendered) is determined by the block model (i.e. the model json file).
  //  But Minecraft also uses a number of other "shapes" to control the interaction of the block with its environment and with the player.
  // See  https://greyminecraftcoder.blogspot.com/2020/02/block-shapes-voxelshapes-1144.html
  @Override
  public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
    return CHEST_SHAPE;
  }

  private static final Vec3d CHEST_MIN_CORNER = new Vec3d(1.0, 0.0, 1.0);
  private static final Vec3d CHEST_MAX_CORNER = new Vec3d(15.0, 8.0, 15.0);
  private static final VoxelShape CHEST_SHAPE = Block.makeCuboidShape(
          CHEST_MIN_CORNER.getX(), CHEST_MIN_CORNER.getY(), CHEST_MIN_CORNER.getZ(),
          CHEST_MAX_CORNER.getX(), CHEST_MAX_CORNER.getY(), CHEST_MAX_CORNER.getZ());

  }
