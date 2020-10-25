package minecraftbyexample.mbe06_redstone.input;

import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockDisplayReader;

/**
 * Created by TGG on 17/08/2016.
 */
public class LampColour implements IBlockColor {

  /**
   * Returns the lamp colour for rendering, based on
   * 1) the block state
   * 2) the "tintindex" (tintindex in the block model json)
   *
   *  This is the technique used by BlockGrass to change colour in different biomes.
   *  It's also used by RedstoneWireBlock to change the redness of the wire based on the power level
   * For example:
   * the grassy dirt block,  grass.json contains
       "up":    { "uv": [ 0, 0, 16, 16 ], "texture": "#top", "cullface": "up", "tintindex": 0 },
     "down":  { "uv": [ 0, 0, 16, 16 ], "texture": "#bottom", "cullface": "down" },
   * the top of the block, "up", has tintindex 0 - its colour changes with the biome, which is calculated by a call
   *    to colorMultiplier()
   * the bottom of the block, "down", has no tintindex flag, so colorMultiplier isn't called for this face.
   *  See BlockColors.init() for examples (the handlers are all implemented as anonymous classes so your IDE may not
   *     find them with an ordinary search).
   * @param tintIndex
   * @return an RGB colour (to be multiplied by the texture colours)
   */
  @Override
  public int getColor(BlockState blockState, IBlockDisplayReader iLightReader, BlockPos blockPos, int tintIndex) {
      int rgbColour = BlockRedstoneColouredLamp.getRGBlampColour(blockState);
      return rgbColour;
  }
}
