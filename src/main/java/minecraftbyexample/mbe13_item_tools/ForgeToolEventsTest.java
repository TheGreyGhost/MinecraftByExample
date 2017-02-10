package minecraftbyexample.mbe13_item_tools;

import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * User: The Grey Ghost
 * Date: 6/01/2015
 * Events can be used if you want to modify the behaviour of vanilla blocks with vanilla items.
 * Otherwise, you should override the appropriate methods in your custom Item or Block instead.
 */

public class ForgeToolEventsTest
{

  // change the digging speed (block damage per tick).  Vanilla provides the calculated damage rate and provides this
  //   opportunity to modify it.  Cancelling it leads to zero damage.
  @SubscribeEvent
  public void breakSpeed(PlayerEvent.BreakSpeed event)
  {
    // only show event if either the test block or the test item is involved
    if (   (event.getState() == null || event.getState().getBlock() != StartupCommon.blockToolTest)
        && (event.getEntityPlayer() == null
            || event.getEntityPlayer().getItemStackFromSlot(EntityEquipmentSlot.MAINHAND).getItem() != StartupCommon.itemToolsTest)) {
      return;
    }
    StartupCommon.methodCallLogger.enterMethod("Event.BreakSpeed", "blockstate=" + event.getState()
            + ", blockpos=" + event.getPos() + ", oldspeed = " + event.getOriginalSpeed());
    // your code here
    StartupCommon.methodCallLogger.exitMethod("Event.BreakSpeed", "newspeed=" + event.getNewSpeed() + ", cancelled=" + event.isCanceled());
  }

  // can be used to add harvesting = true for block/item combinations which aren't otherwise covered by the ToolClass.
  @SubscribeEvent
  public void harvestCheck(PlayerEvent.HarvestCheck event)
  {
    // only show event if either the test block or the test item is involved
    if (   (event.getTargetBlock() != StartupCommon.blockToolTest)
            && (event.getEntityPlayer() == null
            || event.getEntityPlayer().getItemStackFromSlot(EntityEquipmentSlot.MAINHAND).getItem() != StartupCommon.itemToolsTest)) {
    return;
  }
    StartupCommon.methodCallLogger.enterMethod("Event.HarvestCheck", "targetblock=" + event.getTargetBlock());
    // your code here
    StartupCommon.methodCallLogger.exitMethod("Event.HarvestCheck", "canHarvest=" + event.canHarvest());
  }

  // can be used to cancel digging before it starts.
  // Either cancel the event, or set useBlock to deny.
  @SubscribeEvent
  public void playerInteractEvent(PlayerInteractEvent.LeftClickBlock event)
  {
    // only show event if either the test block or the test item is involved
    if (event.getPos() != null && event.getWorld().getBlockState(event.getPos()) == StartupCommon.blockToolTest) {
    } else {
      ItemStack heldItemStack = (event.getEntityPlayer() == null) ?
                                ItemStack.EMPTY :  //EMPTY_ITEM
                                event.getEntityPlayer().getItemStackFromSlot(EntityEquipmentSlot.MAINHAND);
      Item heldItem = heldItemStack.getItem();
      if (heldItem != StartupCommon.itemToolsTest) {
        return;
      }
    }

    StartupCommon.methodCallLogger.enterMethod("Event.PlayerInteractEvent.LeftClickBlock",
                                               "pos=" + event.getPos() + ", hitVec=" + event.getHitVec()
                                                +", face=" + event.getFace()
                                               );
    // your code here
    StartupCommon.methodCallLogger.exitMethod("Event.PlayerInteractEvent.LeftClickBlock",
            "cancelled=" + event.isCanceled() + "; useItem=" + event.getUseItem() + "; useBlock=" + event.getUseBlock());
  }

  //called just before block destruction - if cancelled, abort without destroying the block.
  @SubscribeEvent
  public void breakEvent(BlockEvent.BreakEvent event)
  {
    // only show event if either the test block or the test item is involved
    if (   (event.getWorld().getBlockState(event.getPos()) != StartupCommon.blockToolTest)
            && (event.getPlayer() == null
            || event.getPlayer().getItemStackFromSlot(EntityEquipmentSlot.MAINHAND).getItem() != StartupCommon.itemToolsTest)) {
      return;
    }
    StartupCommon.methodCallLogger.enterMethod("Event.BreakEvent", "player=" + event.getPlayer().getDisplayNameString() + ", exp=" + event.getExpToDrop());
    // your code here
    StartupCommon.methodCallLogger.exitMethod("Event.BreakEvent", "cancelled=" + event.isCanceled() + ";exp after = " + event.getExpToDrop());
  }

  // modify the list of items that might be dropped when the block is harvested.
  @SubscribeEvent
  public void harvestDropsEvent(BlockEvent.HarvestDropsEvent event)
  {
    // only show event if either the test block or the test item is involved
    if (   (event.getWorld().getBlockState(event.getPos()) != StartupCommon.blockToolTest)
            && (event.getHarvester() == null
            || event.getHarvester().getItemStackFromSlot(EntityEquipmentSlot.MAINHAND).getItem() != StartupCommon.itemToolsTest)) {
      return;
    }
    String params = "";
    params += "harvester=" + event.getHarvester();
    params += "; isSilkTouching=" + event.isSilkTouching();
    params += "; dropChance=" + event.getDropChance();
    params += "; fortuneLevel=" + event.getFortuneLevel();
    params += "; drops={";
    for (ItemStack itemStack : event.getDrops()) {
      params += String.valueOf(itemStack) + "; ";
    }
    params += "}";
    StartupCommon.methodCallLogger.enterMethod("Event.HarvestDropsEvent", params);

    params = "dropChance=" + event.getDropChance();
    params += "; drops={";
    for (ItemStack itemStack : event.getDrops()) {
      params += String.valueOf(itemStack) + "; ";
    }
    params += "}";
    StartupCommon.methodCallLogger.exitMethod("Event.HarvestDropsEvent", params);
  }
}
