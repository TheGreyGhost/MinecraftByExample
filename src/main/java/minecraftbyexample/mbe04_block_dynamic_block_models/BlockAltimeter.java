package minecraftbyexample.mbe04_block_dynamic_block_models;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.ILightReader;

import javax.annotation.Nonnull;
import java.util.*;

/**
 * Created by TheGreyGhost on 19/04/2015.
 *
 * This altimeter displays the elevation of the block (its y coordinate) and also a needle that points towards [x=0, y=0]
 */
public class BlockAltimeter extends Block {
  public BlockAltimeter() {
    super(Properties.create(Material.MISCELLANEOUS)  // look at Block.Properties for further options

    );
  }

  // render using an IBakedModel
  // not strictly required because the default (super method) is MODEL.
  @Override
  public BlockRenderType getRenderType(BlockState iBlockState) {
    return BlockRenderType.MODEL;
  }

  // Return the GPS coordinates of this block
  public static Optional<GPScoordinate> getGPScoordinate(@Nonnull ILightReader world, @Nonnull BlockPos blockPos) {
    GPScoordinate gpsCoordinate = new GPScoordinate();
    gpsCoordinate.altitude = blockPos.getY();

    // use some high school trigonometry to calculate the angle pointing towards the origin [x=0, z=0]
    double rawAngleRadians = MathHelper.atan2(blockPos.getX(), -blockPos.getZ());
    double rawAngleDegrees = Math.toDegrees(rawAngleRadians);
    double bearing = 180 + rawAngleDegrees;
    double wrappedBearing = MathHelper.wrapDegrees(bearing);
    gpsCoordinate.bearingToOrigin = (float)wrappedBearing;
    return Optional.of(gpsCoordinate);
  }

  public static class GPScoordinate {
    int altitude;            // altitude in metres
    float bearingToOrigin;   // points towards the origin [x=0, z=0]- in degrees clockwise from north
  }

  private static final Vector3d BASE_MIN_CORNER = new Vector3d(1.5, 0.0, 1.5);
  private static final Vector3d BASE_MAX_CORNER = new Vector3d(14.5, 10.0, 14.5);

  private static final VoxelShape BASE = Block.makeCuboidShape(BASE_MIN_CORNER.getX(), BASE_MIN_CORNER.getY(), BASE_MIN_CORNER.getZ(),
          BASE_MAX_CORNER.getX(), BASE_MAX_CORNER.getY(), BASE_MAX_CORNER.getZ());

  // returns the shape of the block: (just the base unit without digits)
  //  The image that you see on the screen (when a block is rendered) is determined by the block model (i.e. the model json file).
  //  But Minecraft also uses a number of other "shapes" to control the interaction of the block with its environment and with the player.
  // See  https://greyminecraftcoder.blogspot.com/2020/02/block-shapes-voxelshapes-1144.html
  @Override
  public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
    return BASE;
  }

}
