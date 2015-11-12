package minecraftbyexample.mbe13_item_tools;

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
    if (   (event.state == null || event.state.getBlock() != StartupCommon.blockToolTest)
        && (event.entityPlayer == null || event.entityPlayer.getCurrentEquippedItem() == null || event.entityPlayer.getCurrentEquippedItem().getItem() != StartupCommon.itemToolsTest)) {
      return;
    }
    StartupCommon.methodCallLogger.enterMethod("Event.BreakSpeed", "blockstate=" + event.state + ", blockpos=" + event.pos + ", oldspeed = " + event.originalSpeed);
    // your code here
    StartupCommon.methodCallLogger.exitMethod("Event.BreakSpeed", "newspeed=" + event.newSpeed + ", cancelled=" + event.isCanceled());
  }

  // can be used to add harvesting = true for block/item combinations which aren't otherwise covered by the ToolClass.
  @SubscribeEvent
  public void harvestCheck(PlayerEvent.HarvestCheck event)
  {
    // only show event if either the test block or the test item is involved
    if (   (event.block != StartupCommon.blockToolTest)
            && (event.entityPlayer == null || event.entityPlayer.getCurrentEquippedItem() == null || event.entityPlayer.getCurrentEquippedItem().getItem() != StartupCommon.itemToolsTest)) {
    return;
  }
    StartupCommon.methodCallLogger.enterMethod("Event.HarvestCheck", "block=" + event.block);
    // your code here
    StartupCommon.methodCallLogger.exitMethod("Event.HarvestCheck", "success=" + event.success);
  }

  // can be used to cancel digging before it starts.
  // Either cancel the event, or set useBlock to deny.
  @SubscribeEvent
  public void playerInteractEvent(PlayerInteractEvent event)
  {
    // only show event if either the test block or the test item is involved
    if (event.pos != null && event.world.getBlockState(event.pos) == StartupCommon.blockToolTest) {
    } else {
      ItemStack heldItemStack = (event.entityPlayer == null) ? null : event.entityPlayer.getCurrentEquippedItem();
      if (heldItemStack == null) {
        return;
      }
      Item heldItem = heldItemStack.getItem();
      if (heldItem != StartupCommon.itemToolsTest) {
        return;
      }
    }

    StartupCommon.methodCallLogger.enterMethod("Event.PlayerInteractEvent", "action=" + event.action);
    // your code here
    StartupCommon.methodCallLogger.exitMethod("Event.PlayerInteractEvent",
            "cancelled=" + event.isCanceled() + "; useItem=" + event.useItem + "; useBlock=" + event.useBlock);
  }

  //called just before block destruction - if cancelled, abort without destroying the block.
  @SubscribeEvent
  public void breakEvent(BlockEvent.BreakEvent event)
  {
    // only show event if either the test block or the test item is involved
    if (   (event.world.getBlockState(event.pos) != StartupCommon.blockToolTest)
            && (event.getPlayer() == null || event.getPlayer().getCurrentEquippedItem() == null || event.getPlayer().getCurrentEquippedItem().getItem() != StartupCommon.itemToolsTest)) {
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
    if (   (event.world.getBlockState(event.pos) != StartupCommon.blockToolTest)
            && (event.harvester == null || event.harvester.getCurrentEquippedItem() == null || event.harvester.getCurrentEquippedItem().getItem() != StartupCommon.itemToolsTest)) {
      return;
    }
    String params = "";
    params += "harvester=" + event.harvester;
    params += "; isSilkTouching=" + event.isSilkTouching;
    params += "; dropChance=" + event.dropChance;
    params += "; fortuneLevel=" + event.fortuneLevel;
    params += "; drops={";
    for (ItemStack itemStack : event.drops) {
      params += String.valueOf(itemStack) + "; ";
    }
    params += "}";
    StartupCommon.methodCallLogger.enterMethod("Event.HarvestDropsEvent", params);

    params = "dropChance=" + event.dropChance;
    params += "; drops={";
    for (ItemStack itemStack : event.drops) {
      params += String.valueOf(itemStack) + "; ";
    }
    params += "}";
    StartupCommon.methodCallLogger.exitMethod("Event.HarvestDropsEvent", params);
  }
}
