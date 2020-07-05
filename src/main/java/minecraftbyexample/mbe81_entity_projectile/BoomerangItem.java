package minecraftbyexample.mbe81_entity_projectile;

import net.minecraft.entity.EntityType;
import net.minecraft.world.World;

/**
 * Created by TGG on 24/06/2020.
 */
public class BoomerangItem  {

//         * By default, entities are spawned clientside via {@link EntityType#create(World)}.
//          * If you need finer control over the spawning process, use this to get read access to the spawn packet.
//          */
//  public EntityType.Builder<T> setCustomClientFactory(java.util.function.BiFunction<net.minecraftforge.fml.network.FMLPlayMessages.SpawnEntity, World,


  path taken:
  x(t) = 0.5 *(1-cos(t))
  side_deviation = 0.2
  y(t) = side_deviation*sin(t)*sin(t/2)


  parameters:
  startpos
  apexpos
  sideways_deviation
  time_for_full_loop




}
