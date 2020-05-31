package minecraftbyexample.mbe06_redstone.input_and_output;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.minecraft.block.*;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.particles.RedstoneParticleData;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.properties.RedstoneSide;
import net.minecraft.util.Direction;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.*;

/** Some parts of the vanilla code refactored to make it easier to understand
 *
 */

public class RedstoneWireBlockRefactored extends Block {
   public static final EnumProperty<RedstoneSide> NORTH = BlockStateProperties.REDSTONE_NORTH;
   public static final EnumProperty<RedstoneSide> EAST = BlockStateProperties.REDSTONE_EAST;
   public static final EnumProperty<RedstoneSide> SOUTH = BlockStateProperties.REDSTONE_SOUTH;
   public static final EnumProperty<RedstoneSide> WEST = BlockStateProperties.REDSTONE_WEST;
   public static final IntegerProperty POWER = BlockStateProperties.POWER_0_15;
   public static final Map<Direction, EnumProperty<RedstoneSide>> FACING_PROPERTY_MAP = Maps.newEnumMap(ImmutableMap.of(Direction.NORTH, NORTH, Direction.EAST, EAST, Direction.SOUTH, SOUTH, Direction.WEST, WEST));
   protected static final VoxelShape[] SHAPES = new VoxelShape[]{Block.makeCuboidShape(3.0D, 0.0D, 3.0D, 13.0D, 1.0D, 13.0D), Block.makeCuboidShape(3.0D, 0.0D, 3.0D, 13.0D, 1.0D, 16.0D), Block.makeCuboidShape(0.0D, 0.0D, 3.0D, 13.0D, 1.0D, 13.0D), Block.makeCuboidShape(0.0D, 0.0D, 3.0D, 13.0D, 1.0D, 16.0D), Block.makeCuboidShape(3.0D, 0.0D, 0.0D, 13.0D, 1.0D, 13.0D), Block.makeCuboidShape(3.0D, 0.0D, 0.0D, 13.0D, 1.0D, 16.0D), Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 13.0D, 1.0D, 13.0D), Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 13.0D, 1.0D, 16.0D), Block.makeCuboidShape(3.0D, 0.0D, 3.0D, 16.0D, 1.0D, 13.0D), Block.makeCuboidShape(3.0D, 0.0D, 3.0D, 16.0D, 1.0D, 16.0D), Block.makeCuboidShape(0.0D, 0.0D, 3.0D, 16.0D, 1.0D, 13.0D), Block.makeCuboidShape(0.0D, 0.0D, 3.0D, 16.0D, 1.0D, 16.0D), Block.makeCuboidShape(3.0D, 0.0D, 0.0D, 16.0D, 1.0D, 13.0D), Block.makeCuboidShape(3.0D, 0.0D, 0.0D, 16.0D, 1.0D, 16.0D), Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 1.0D, 13.0D), Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 1.0D, 16.0D)};
   private boolean canProvidePower = true;
   /** List of blocks to update with redstone. */
   private final Set<BlockPos> blocksNeedingUpdate = Sets.newHashSet();

   public RedstoneWireBlockRefactored(Properties properties) {
      super(properties);
      this.setDefaultState(this.stateContainer.getBaseState()
              .with(NORTH, RedstoneSide.NONE)
              .with(EAST, RedstoneSide.NONE)
              .with(SOUTH, RedstoneSide.NONE)
              .with(WEST, RedstoneSide.NONE)
              .with(POWER, 0));
   }

   public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
      return SHAPES[getAABBIndex(state)];
   }

   private static int getAABBIndex(BlockState state) {
      int bitSet = 0;
      boolean wireNorth = state.get(NORTH) != RedstoneSide.NONE;
      boolean wireEast = state.get(EAST) != RedstoneSide.NONE;
      boolean wireSouth = state.get(SOUTH) != RedstoneSide.NONE;
      boolean wireWest = state.get(WEST) != RedstoneSide.NONE;
      if (wireNorth || wireSouth && !wireNorth && !wireEast && !wireWest) {
         bitSet |= 1 << Direction.NORTH.getHorizontalIndex();
      }

      if (wireEast || wireWest && !wireNorth && !wireEast && !wireSouth) {
         bitSet |= 1 << Direction.EAST.getHorizontalIndex();
      }

      if (wireSouth || wireNorth && !wireEast && !wireSouth && !wireWest) {
         bitSet |= 1 << Direction.SOUTH.getHorizontalIndex();
      }

      if (wireWest || wireEast && !wireNorth && !wireSouth && !wireWest) {
         bitSet |= 1 << Direction.WEST.getHorizontalIndex();
      }

      return bitSet;
   }

   public BlockState getStateForPlacement(BlockItemUseContext context) {
      IBlockReader iblockreader = context.getWorld();
      BlockPos blockpos = context.getPos();
      return this.getDefaultState()
              .with(WEST, this.getWireSideState(iblockreader, blockpos, Direction.WEST))
              .with(EAST, this.getWireSideState(iblockreader, blockpos, Direction.EAST))
              .with(NORTH, this.getWireSideState(iblockreader, blockpos, Direction.NORTH))
              .with(SOUTH, this.getWireSideState(iblockreader, blockpos, Direction.SOUTH));
   }

   /**
    * Update the provided state given the provided neighbor facing and neighbor state, returning a new state.
    * For example, fences make their connections to the passed in state if possible, and wet concrete powder immediately
    * returns its solidified counterpart.
    * Note that this method should ideally consider only the specific face passed in.
    */
   public BlockState updatePostPlacement(BlockState thisBlockstate, Direction directionFromNeighborToThis, BlockState neighborState,
                                         IWorld worldIn, BlockPos thisBlockPos, BlockPos neighborBlockpos) {
      if (directionFromNeighborToThis == Direction.DOWN) {  // our state doesn't depend on the block above us
        return thisBlockstate;
      }
      if (directionFromNeighborToThis == Direction.UP ) {
        BlockState newBlockState = thisBlockstate
                .with(WEST, this.getWireSideState(worldIn, thisBlockPos, Direction.WEST))
                .with(EAST, this.getWireSideState(worldIn, thisBlockPos, Direction.EAST))
                .with(NORTH, this.getWireSideState(worldIn, thisBlockPos, Direction.NORTH))
                .with(SOUTH, this.getWireSideState(worldIn, thisBlockPos, Direction.SOUTH));
        return newBlockState;
      }
     BlockState newBlockState = thisBlockstate
        .with(FACING_PROPERTY_MAP.get(directionFromNeighborToThis), this.getWireSideState(worldIn, thisBlockPos, directionFromNeighborToThis));
     return newBlockState;
   }

   /**
    * performs updates on diagonal neighbors of the target position and passes in the flags. The flags can be referenced
    */
   public void updateDiagonalNeighbors(BlockState state, IWorld worldIn, BlockPos pos, int flags) {
      try (BlockPos.PooledMutable blockpos$pooledmutable = BlockPos.PooledMutable.retain()) {
         for(Direction direction : Direction.Plane.HORIZONTAL) {
            RedstoneSide redstoneside = state.get(FACING_PROPERTY_MAP.get(direction));
            if (redstoneside != RedstoneSide.NONE && worldIn.getBlockState(blockpos$pooledmutable.setPos(pos).move(direction)).getBlock() != this) {
               blockpos$pooledmutable.move(Direction.DOWN);
               BlockState blockstate = worldIn.getBlockState(blockpos$pooledmutable);
               if (blockstate.getBlock() != Blocks.OBSERVER) {
                  BlockPos blockpos = blockpos$pooledmutable.offset(direction.getOpposite());
                  BlockState blockstate1 = blockstate.updatePostPlacement(direction.getOpposite(), worldIn.getBlockState(blockpos), worldIn, blockpos$pooledmutable, blockpos);
                  replaceBlock(blockstate, blockstate1, worldIn, blockpos$pooledmutable, flags);
               }

               blockpos$pooledmutable.setPos(pos).move(direction).move(Direction.UP);
               BlockState blockstate3 = worldIn.getBlockState(blockpos$pooledmutable);
               if (blockstate3.getBlock() != Blocks.OBSERVER) {
                  BlockPos blockpos1 = blockpos$pooledmutable.offset(direction.getOpposite());
                  BlockState blockstate2 = blockstate3.updatePostPlacement(direction.getOpposite(), worldIn.getBlockState(blockpos1), worldIn, blockpos$pooledmutable, blockpos1);
                  replaceBlock(blockstate3, blockstate2, worldIn, blockpos$pooledmutable, flags);
               }
            }
         }
      }

   }

 private RedstoneSide getWireSideState(IBlockReader worldIn, BlockPos thisPos, Direction whichSide) {
    BlockPos sideBlockPos = thisPos.offset(whichSide);
    BlockState sideBlockState = worldIn.getBlockState(sideBlockPos);
    BlockPos upBlockPos = thisPos.up();
    BlockState upBlockState = worldIn.getBlockState(upBlockPos);

    // first: check if the wire can go up:
    //     the block above the wire is empty, and
    //     the block to the side has a solid top (eg stone block, or slab placed in upper half space), and
    //     the block on top of the block to the side can be connected to using side==null
    //   if so: check the side of the side block: if solid (eg stone block), redstone runs up the side of the block
    //            if side is not solid (eg slab): redstone goes to the side of the block but does not run up the face
    if (!upBlockState.isNormalCube(worldIn, upBlockPos)) {
       boolean sideBlockHasSolidTop = sideBlockState.isSolidSide(worldIn, sideBlockPos, Direction.UP) || sideBlockState.getBlock() == Blocks.HOPPER;
       BlockPos aboveSideBlockPos = sideBlockPos.up();
       BlockState aboveSideBlockState = worldIn.getBlockState(aboveSideBlockPos);

       if (sideBlockHasSolidTop && canConnectTo(aboveSideBlockState, worldIn, aboveSideBlockPos, null)) {
          if (sideBlockState.isCollisionShapeOpaque(worldIn, sideBlockPos)) {
             return RedstoneSide.UP;
          }

          return RedstoneSide.SIDE;
       }
    }

    // if we can connect to the side of the block, return SIDE
   //  otherwise, if the block to the side is empty and the block below the block to the side can be connected to side==null, SIDE

    if (canConnectTo(sideBlockState, worldIn, sideBlockPos, whichSide)) return RedstoneSide.SIDE;
    if (sideBlockState.isNormalCube(worldIn, sideBlockPos)) return RedstoneSide.NONE;
    if (canConnectTo(worldIn.getBlockState(sideBlockPos.down()), worldIn, sideBlockPos.down(), null)) return RedstoneSide.SIDE;
    return RedstoneSide.NONE;
 }

   public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
      BlockPos blockpos = pos.down();
      BlockState blockstate = worldIn.getBlockState(blockpos);
      return blockstate.isSolidSide(worldIn, blockpos, Direction.UP) || blockstate.getBlock() == Blocks.HOPPER;
   }

   private BlockState updateSurroundingRedstone(World worldIn, BlockPos pos, BlockState state) {
      state = this.updatePower(worldIn, pos, state);
      List<BlockPos> list = Lists.newArrayList(this.blocksNeedingUpdate);
      this.blocksNeedingUpdate.clear();

      for(BlockPos blockpos : list) {
         worldIn.notifyNeighborsOfStateChange(blockpos, this);
      }

      return state;
   }

   private BlockState updatePower(World worldIn, BlockPos pos, BlockState state) {
      BlockState blockstate = state;
      int i = state.get(POWER);
      this.canProvidePower = false;
      int j = worldIn.getRedstonePowerFromNeighbors(pos);
      this.canProvidePower = true;
      int k = 0;
      if (j < 15) {
         for(Direction direction : Direction.Plane.HORIZONTAL) {
            BlockPos blockpos = pos.offset(direction);
            BlockState blockstate1 = worldIn.getBlockState(blockpos);
            k = this.maxSignal(k, blockstate1);
            BlockPos blockpos1 = pos.up();
            if (blockstate1.isNormalCube(worldIn, blockpos) && !worldIn.getBlockState(blockpos1).isNormalCube(worldIn, blockpos1)) {
               k = this.maxSignal(k, worldIn.getBlockState(blockpos.up()));
            } else if (!blockstate1.isNormalCube(worldIn, blockpos)) {
               k = this.maxSignal(k, worldIn.getBlockState(blockpos.down()));
            }
         }
      }

      int l = k - 1;
      if (j > l) {
         l = j;
      }

      if (i != l) {
         state = state.with(POWER, Integer.valueOf(l));
         if (worldIn.getBlockState(pos) == blockstate) {
            worldIn.setBlockState(pos, state, 2);
         }

         this.blocksNeedingUpdate.add(pos);

         for(Direction direction1 : Direction.values()) {
            this.blocksNeedingUpdate.add(pos.offset(direction1));
         }
      }

      return state;
   }

   /**
    * Calls World.notifyNeighborsOfStateChange() for all neighboring blocks, but only if the given block is a redstone
    * wire.
    */
   private void notifyWireNeighborsOfStateChange(World worldIn, BlockPos pos) {
      if (worldIn.getBlockState(pos).getBlock() == this) {
         worldIn.notifyNeighborsOfStateChange(pos, this);

         for(Direction direction : Direction.values()) {
            worldIn.notifyNeighborsOfStateChange(pos.offset(direction), this);
         }

      }
   }

   public void onBlockAdded(BlockState state, World worldIn, BlockPos pos, BlockState oldState, boolean isMoving) {
      if (oldState.getBlock() != state.getBlock() && !worldIn.isRemote) {
         this.updateSurroundingRedstone(worldIn, pos, state);

         for(Direction direction : Direction.Plane.VERTICAL) {
            worldIn.notifyNeighborsOfStateChange(pos.offset(direction), this);
         }

         for(Direction direction1 : Direction.Plane.HORIZONTAL) {
            this.notifyWireNeighborsOfStateChange(worldIn, pos.offset(direction1));
         }

         for(Direction direction2 : Direction.Plane.HORIZONTAL) {
            BlockPos blockpos = pos.offset(direction2);
            if (worldIn.getBlockState(blockpos).isNormalCube(worldIn, blockpos)) {
               this.notifyWireNeighborsOfStateChange(worldIn, blockpos.up());
            } else {
               this.notifyWireNeighborsOfStateChange(worldIn, blockpos.down());
            }
         }

      }
   }

   public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
      if (!isMoving && state.getBlock() != newState.getBlock()) {
         super.onReplaced(state, worldIn, pos, newState, isMoving);
         if (!worldIn.isRemote) {
            for(Direction direction : Direction.values()) {
               worldIn.notifyNeighborsOfStateChange(pos.offset(direction), this);
            }

            this.updateSurroundingRedstone(worldIn, pos, state);

            for(Direction direction1 : Direction.Plane.HORIZONTAL) {
               this.notifyWireNeighborsOfStateChange(worldIn, pos.offset(direction1));
            }

            for(Direction direction2 : Direction.Plane.HORIZONTAL) {
               BlockPos blockpos = pos.offset(direction2);
               if (worldIn.getBlockState(blockpos).isNormalCube(worldIn, blockpos)) {
                  this.notifyWireNeighborsOfStateChange(worldIn, blockpos.up());
               } else {
                  this.notifyWireNeighborsOfStateChange(worldIn, blockpos.down());
               }
            }

         }
      }
   }

   private int maxSignal(int existingSignal, BlockState neighbor) {
      if (neighbor.getBlock() != this) {
         return existingSignal;
      } else {
         int i = neighbor.get(POWER);
         return i > existingSignal ? i : existingSignal;
      }
   }

   public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
      if (!worldIn.isRemote) {
         if (state.isValidPosition(worldIn, pos)) {
            this.updateSurroundingRedstone(worldIn, pos, state);
         } else {
            spawnDrops(state, worldIn, pos);
            worldIn.removeBlock(pos, false);
         }

      }
   }

   /**
    * Implementing/overriding is fine.
    */
   public int getStrongPower(BlockState blockState, IBlockReader blockAccess, BlockPos pos, Direction side) {
      return !this.canProvidePower ? 0 : blockState.getWeakPower(blockAccess, pos, side);
   }

   /**
    * Implementing/overriding is fine.
    */
   public int getWeakPower(BlockState blockState, IBlockReader blockAccess, BlockPos pos, Direction side) {
      if (!this.canProvidePower) {
         return 0;
      } else {
         int i = blockState.get(POWER);
         if (i == 0) {
            return 0;
         } else if (side == Direction.UP) {
            return i;
         } else {
            EnumSet<Direction> enumset = EnumSet.noneOf(Direction.class);

            for(Direction direction : Direction.Plane.HORIZONTAL) {
               if (this.isPowerSourceAt(blockAccess, pos, direction)) {
                  enumset.add(direction);
               }
            }

            if (side.getAxis().isHorizontal() && enumset.isEmpty()) {
               return i;
            } else {
               return enumset.contains(side) && !enumset.contains(side.rotateYCCW()) && !enumset.contains(side.rotateY()) ? i : 0;
            }
         }
      }
   }

   // for logic: see getWireSideState
   private boolean isPowerSourceAt(IBlockReader worldIn, BlockPos blockPos, Direction side) {
      BlockPos sideBlockPos = blockPos.offset(side);
      BlockState sideBlockState = worldIn.getBlockState(sideBlockPos);
      boolean sideBlockIsSolid = sideBlockState.isNormalCube(worldIn, sideBlockPos);
      BlockPos upBlockPos = blockPos.up();
      boolean upBlockIsSolid = worldIn.getBlockState(upBlockPos).isNormalCube(worldIn, upBlockPos);
      if (!upBlockIsSolid && sideBlockIsSolid && canConnectTo(worldIn.getBlockState(sideBlockPos.up()), worldIn, sideBlockPos.up(), null)) {
         return true;
      } else if (canConnectTo(sideBlockState, worldIn, sideBlockPos, side)) {
         return true;
      } else if (sideBlockState.getBlock() == Blocks.REPEATER && sideBlockState.get(RedstoneDiodeBlock.POWERED) && sideBlockState.get(RedstoneDiodeBlock.HORIZONTAL_FACING) == side) {
         return true;
      } else {
         return !sideBlockIsSolid && canConnectTo(worldIn.getBlockState(sideBlockPos.down()), worldIn, sideBlockPos.down(), null);
      }
   }

   protected static boolean canConnectTo(BlockState blockState, IBlockReader world, BlockPos pos, @Nullable Direction side) {
      Block block = blockState.getBlock();
      if (block == Blocks.REDSTONE_WIRE) {
         return true;
      } else if (blockState.getBlock() == Blocks.REPEATER) {
         Direction direction = blockState.get(RepeaterBlock.HORIZONTAL_FACING);
         return direction == side || direction.getOpposite() == side;
      } else if (Blocks.OBSERVER == blockState.getBlock()) {
         return side == blockState.get(ObserverBlock.FACING);
      } else {
         return blockState.canConnectRedstone(world, pos, side) && side != null;
      }
   }

   /**
    * Can this block provide power. Only wire currently seems to have this change based on its state.
    */
   public boolean canProvidePower(BlockState state) {
      return this.canProvidePower;
   }

   @OnlyIn(Dist.CLIENT)
   public static int colorMultiplier(int p_176337_0_) {
      float f = (float)p_176337_0_ / 15.0F;
      float f1 = f * 0.6F + 0.4F;
      if (p_176337_0_ == 0) {
         f1 = 0.3F;
      }

      float f2 = f * f * 0.7F - 0.5F;
      float f3 = f * f * 0.6F - 0.7F;
      if (f2 < 0.0F) {
         f2 = 0.0F;
      }

      if (f3 < 0.0F) {
         f3 = 0.0F;
      }

      int i = MathHelper.clamp((int)(f1 * 255.0F), 0, 255);
      int j = MathHelper.clamp((int)(f2 * 255.0F), 0, 255);
      int k = MathHelper.clamp((int)(f3 * 255.0F), 0, 255);
      return -16777216 | i << 16 | j << 8 | k;
   }

   /**
    * Called periodically clientside on blocks near the player to show effects (like furnace fire particles). Note that
    * of whether the block can receive random update ticks
    */
   @OnlyIn(Dist.CLIENT)
   public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
      int i = stateIn.get(POWER);
      if (i != 0) {
         double d0 = (double)pos.getX() + 0.5D + ((double)rand.nextFloat() - 0.5D) * 0.2D;
         double d1 = (double)((float)pos.getY() + 0.0625F);
         double d2 = (double)pos.getZ() + 0.5D + ((double)rand.nextFloat() - 0.5D) * 0.2D;
         float f = (float)i / 15.0F;
         float f1 = f * 0.6F + 0.4F;
         float f2 = Math.max(0.0F, f * f * 0.7F - 0.5F);
         float f3 = Math.max(0.0F, f * f * 0.6F - 0.7F);
         worldIn.addParticle(new RedstoneParticleData(f1, f2, f3, 1.0F), d0, d1, d2, 0.0D, 0.0D, 0.0D);
      }
   }

   /**
    * Returns the blockstate with the given rotation from the passed blockstate. If inapplicable, returns the passed
    * blockstate.
    * fine.
    */
   public BlockState rotate(BlockState state, Rotation rot) {
      switch(rot) {
      case CLOCKWISE_180:
         return state.with(NORTH, state.get(SOUTH)).with(EAST, state.get(WEST)).with(SOUTH, state.get(NORTH)).with(WEST, state.get(EAST));
      case COUNTERCLOCKWISE_90:
         return state.with(NORTH, state.get(EAST)).with(EAST, state.get(SOUTH)).with(SOUTH, state.get(WEST)).with(WEST, state.get(NORTH));
      case CLOCKWISE_90:
         return state.with(NORTH, state.get(WEST)).with(EAST, state.get(NORTH)).with(SOUTH, state.get(EAST)).with(WEST, state.get(SOUTH));
      default:
         return state;
      }
   }

   /**
    * Returns the blockstate with the given mirror of the passed blockstate. If inapplicable, returns the passed
    * blockstate.
    */
   public BlockState mirror(BlockState state, Mirror mirrorIn) {
      switch(mirrorIn) {
      case LEFT_RIGHT:
         return state.with(NORTH, state.get(SOUTH)).with(SOUTH, state.get(NORTH));
      case FRONT_BACK:
         return state.with(EAST, state.get(WEST)).with(WEST, state.get(EAST));
      default:
         return super.mirror(state, mirrorIn);
      }
   }

   protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
      builder.add(NORTH, EAST, SOUTH, WEST, POWER);
   }
}