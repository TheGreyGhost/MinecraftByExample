package minecraftbyexample.mbe04_block_dynamic_block_model1;

import net.minecraft.block.BlockState;
import net.minecraftforge.common.property.IUnlistedProperty;

/**
 * Created by TheGreyGhost on 20/04/2015.
 */
public class UnlistedPropertyCopiedBlock implements IUnlistedProperty<BlockState>
{
  @Override
  public String getName() {
    return "UnlistedPropertyCopiedBlock";
  }

  @Override
  public boolean isValid(BlockState value) {
    return true;
  }

  @Override
  public Class<BlockState> getType() {
    return BlockState.class;
  }

  @Override
  public String valueToString(BlockState value) {
    return value.toString();
  }
}
