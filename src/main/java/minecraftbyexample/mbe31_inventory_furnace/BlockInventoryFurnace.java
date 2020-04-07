package minecraftbyexample.mbe31_inventory_furnace;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;


/**
 * User: brandon3055
 * Date: 06/01/2015
 *
 * BlockInventoryAdvanced is an advanced furnace with 5 input, 5 output and 4 fuel slots that smelts at up to four times the
 * speed of a regular furnace. The block itself doesn't do much more then any regular block except create a tile entity when
 * placed, open a gui when right clicked and drop the inventory's contents when harvested. Everything else is handled
 * by the tile entity.
 *
 * The block model will change appearance depending on how many fuel slots are burning.
 * The amount of "block light" produced by the furnace will also depending on how many fuel slots are burning.
 *
 */
public class BlockInventoryFurnace extends ContainerBlock
{
	public BlockInventoryFurnace()
  {
    super(Block.Properties.create(Material.ROCK)
    );
    BlockState defaultBlockState = this.stateContainer.getBaseState().with(BURNING_SIDES_COUNT, 0);
    this.setDefaultState(defaultBlockState);
  }

  // --- The block changes its appearance depending on how many of the furnace slots have burning fuel in them
  //  In order to do that, we add a blockstate for each state (0 -> 4), each with a corresponding model.  We also change the blockLight emitted.

  final static int MAX_NUMBER_OF_BURNING_SIDES = 4;
  public static final IntegerProperty BURNING_SIDES_COUNT =
          IntegerProperty.create("burning_sides_count",0, MAX_NUMBER_OF_BURNING_SIDES);

  protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
    builder.add(BURNING_SIDES_COUNT);
  }

  // change the furnace emitted light ("block light") depending on how many slots are burning
  private static final int ALL_SIDES_LIGHT_VALUE = 15; // light value for four sides burning
  private static final int ONE_SIDE_LIGHT_VALUE = 8;  // light value for a single side burning

  /**
   * Amount of block light emitted by the furnace
   */
  public int getLightValue(BlockState state) {
    int lightValue = 0;
    Integer burningSidesCount = state.get(BURNING_SIDES_COUNT);

    if (burningSidesCount == 0) {
      lightValue = 0;
    } else {
      // linearly interpolate the light value depending on how many slots are burning
      lightValue = ONE_SIDE_LIGHT_VALUE +
              (ALL_SIDES_LIGHT_VALUE - ONE_SIDE_LIGHT_VALUE) * burningSidesCount / (MAX_NUMBER_OF_BURNING_SIDES - 1);
    }
    lightValue = MathHelper.clamp(lightValue, 0, ALL_SIDES_LIGHT_VALUE);
    return lightValue;
  }


  // ---------------------

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
    return new TileEntityFurnace();
  }

  // not needed if your block implements ITileEntityProvider (in this case implemented by BlockContainer), but it
  //  doesn't hurt to include it anyway...
  @Override
  public boolean hasTileEntity(BlockState state)
  {
    return true;
  }


  // Called when the block is right clicked
	// In this block it is used to open the block gui when right clicked by a player
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
	@Override
  public void onReplaced(BlockState state, World world, BlockPos blockPos, BlockState newState, boolean isMoving) {
    if (state.getBlock() != newState.getBlock()) {
      TileEntity tileentity = world.getTileEntity(blockPos);
      if (tileentity instanceof TileEntityFurnace) {
        TileEntityFurnace tileEntityFurnace = (TileEntityFurnace)tileentity;
        tileEntityFurnace.dropAllContents(world, blockPos);
      }
//      worldIn.updateComparatorOutputLevel(pos, this);  if the inventory is used to set redstone power for comparators
      super.onReplaced(state, world, blockPos, newState, isMoving);  // call it last, because it removes the TileEntity
    }
  }

  //------------------------------------------------------------
	//  The code below isn't necessary for illustrating the Inventory Furnace concepts, it's just used for rendering.
	//  For more background information see MBE03

	// render using a BakedModel
  // required because the default (super method) is INVISIBLE for BlockContainers.
	@Override
	public BlockRenderType getRenderType(BlockState iBlockState) {
		return BlockRenderType.MODEL;
	}
}
