package minecraftbyexample.mbe04_block_dynamic_block_models;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.ILightReader;

import javax.annotation.Nonnull;
import java.util.*;

/**
 * Created by TheGreyGhost on 19/04/2015.
 *
 * This altimeter displays the elevation of the block (its y coordinate) and points towards [x=0, y=0]
 */
public class BlockAltimeter extends Block {
  public BlockAltimeter() {
    super(Properties.create(Material.MISCELLANEOUS).doesNotBlockMovement()  // look at Block.Properties for further options
            //notOpaque().notSolid()
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
    double bearing = 180 - rawAngleDegrees;
    double wrappedBearing = MathHelper.wrapDegrees(bearing);
    gpsCoordinate.bearingToOrigin = (float)wrappedBearing;
    return Optional.of(gpsCoordinate);
  }

  public static class GPScoordinate {
    int altitude;            // altitude in metres
    float bearingToOrigin;   // points towards the origin [x=0, z=0]- in degrees clockwise from north
  }
}
