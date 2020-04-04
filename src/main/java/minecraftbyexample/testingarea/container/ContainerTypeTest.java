/*
 * Minecraft Forge
 * Copyright (c) 2016-2019.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation version 2.1
 * of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */

package minecraftbyexample.testingarea.container;

import net.minecraft.block.Blocks;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.util.Hand;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.registries.ObjectHolder;

public class ContainerTypeTest
{
  public static ContainerType<TestContainer> TYPE = null;

  public ContainerTypeTest()
  {
    FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(ContainerType.class, this::registerContainers);
    FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
    MinecraftForge.EVENT_BUS.addListener(this::onRightClick);
  }

  private void registerContainers(final RegistryEvent.Register<ContainerType<?>> event)
  {
    TYPE = IForgeContainerType.create(TestContainer::new);
    TYPE.setRegistryName("test_container_registry_name");
    event.getRegistry().register(TYPE);
  }

  private void setup(FMLClientSetupEvent event)
  {
    ScreenManager.registerFactory(TYPE, TestGui::new);
  }

  private void onRightClick(PlayerInteractEvent.RightClickBlock event)
  {
    if (!event.getWorld().isRemote && event.getHand() == Hand.MAIN_HAND)
    {
      if (event.getWorld().getBlockState(event.getPos()).getBlock() == Blocks.SPONGE)
      {
        String text = "Hello World!";
        NetworkHooks.openGui((ServerPlayerEntity) event.getPlayer(), new MyINamedContainerProvider(text), extraData -> {
          extraData.writeString(text);
        });
      }
    }
  }

}

