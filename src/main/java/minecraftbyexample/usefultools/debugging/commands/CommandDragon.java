///*
// ** 2012 August 24
// **
// ** The author disclaims copyright to this source code.  In place of
// ** a legal notice, here is a blessing:
// **    May you do good and not evil.
// **    May you find forgiveness for yourself and forgive others.
// **    May you share freely, never taking more than you give.
// */
//package minecraftbyexample.usefultools.debugging.commands;
//
//import com.TheRPGAdventurer.ROTD.DragonMounts;
//import com.TheRPGAdventurer.ROTD.common.entity.EntityTameableDragon;
//import com.TheRPGAdventurer.ROTD.common.entity.breeds.EnumDragonBreed;
//import com.TheRPGAdventurer.ROTD.common.entity.helper.DragonLifeStage;
//import net.minecraft.command.ICommandSender;
//
//import java.util.function.BiConsumer;
//
///**
// * @author Nico Bergemann <barracuda415 at yahoo.de>
// */
//public class CommandDragon extends CommandBaseNested { //implements IDragonModifier {
//
//  public CommandDragon() {
//    BiConsumer<EntityTameableDragon, EnumDragonBreed> breedConsumer = (dragon, enumValue) -> dragon.setBreedType(enumValue);
//    addCommand(new CommandDragonEnumSetter<EnumDragonBreed>("breed", EnumDragonBreed.class, breedConsumer));
//
//    BiConsumer<EntityTameableDragon, DragonLifeStage> lifeStageConsumer = (dragon, enumValue) -> dragon.lifeStage().setLifeStage(enumValue);
//    addCommand(new CommandDragonEnumSetter<DragonLifeStage>("stage", DragonLifeStage.class, lifeStageConsumer));
//
//    addCommand(new CommandDragonTame());
//    addCommand(new CommandDragonUnlock());
//
//    if (DragonMounts.instance.getConfig().isDebug()) {
//      addCommand(new CommandDragonDebug());
//    }
//  }
//
//  @Override
//  public String getName() {
//    return "dragon";
//  }
//
//  @Override
//  public String getUsage(ICommandSender sender) {
//    return String.format("/%s [global]", super.getUsage(sender));
//  }
//
//  /**
//   * Return the required permission level for this command.
//   */
//  @Override
//  public int getRequiredPermissionLevel() {
//    return 3;
//  }
//}
