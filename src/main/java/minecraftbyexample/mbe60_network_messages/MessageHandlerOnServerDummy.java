package minecraftbyexample.mbe60_network_messages;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.monster.EntitySnowman;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityEgg;
import net.minecraft.entity.projectile.EntityLargeFireball;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

import java.util.Random;

/**
 * This MessageHandler does nothing; it is only used because the dedicated server must register at least one message
 *   handler in order for Forge to know what ID to use for this message.  See more explanation in StartupCommon.
 * User: The Grey Ghost
 * Date: 15/01/2015
 */
public class MessageHandlerOnServerDummy implements IMessageHandler<TargetEffectMessageToClient, IMessage>
{
  public IMessage onMessage(final TargetEffectMessageToClient message, MessageContext ctx) {
    System.err.println("TargetEffectMessageToClient received on wrong side:" + ctx.side);
    return null;
  }
}
