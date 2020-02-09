///*
//** 2016 March 11
//**
//** The author disclaims copyright to this source code. In place of
//** a legal notice, here is a blessing:
//**    May you do good and not evil.
//**    May you find forgiveness for yourself and forgive others.
//**    May you share freely, never taking more than you give.
// */
//package minecraftbyexample.usefultools.debugging.commands;
//
//import com.TheRPGAdventurer.ROTD.common.entity.breeds.EnumDragonBreed;
//import com.TheRPGAdventurer.ROTD.common.entity.helper.DragonLifeStage;
//import com.TheRPGAdventurer.ROTD.util.debugging.DebugSettings;
//import net.minecraft.client.Minecraft;
//import net.minecraft.entity.player.EntityPlayerMP;
//import net.minecraft.nbt.CompressedStreamTools;
//import net.minecraft.nbt.NBTTagCompound;
//
//import java.io.File;
//import java.io.IOException;
//import java.io.UncheckedIOException;
//
///**
// * @author Nico Bergemann <barracuda415 at yahoo.de>
// */
//public class CommandDragonDebug extends CommandBaseNested implements IDragonModifier {
//
//  public CommandDragonDebug() {
////    addCommand(new CommandDragonLambda("toItem", dragon -> {
////      dragon.lifeStage().transformToEgg();
////    }));
//
//    addCommand(new CommandDragonLambda("dumpNBT", dragon -> {
//      File dumpFile = new File(Minecraft.getMinecraft().mcDataDir,
//              String.format("dragon_%08x.nbt", dragon.getEntityId()));
//
//      try {
//        NBTTagCompound nbt = dragon.serializeNBT();
//        CompressedStreamTools.write(nbt, dumpFile);
//      } catch (IOException ex) {
//        throw new UncheckedIOException(ex);
//      }
//    }));
//
//    addCommand(new CommandDragonLambda("overlay", (server, sender, args) -> {
//      DebugSettings.setDebugGuiEnabled(!DebugSettings.isDebugGuiEnabled());
//    }));
//
//    addCommand(new CommandDragonLambda("spawnInhibitor", (server, sender, args) -> {
//      DebugSettings.setSpawningInhibited(!DebugSettings.isSpawningInhibited());
//    }));
//
//    addCommand(new CommandDragonLambda("animationFrozen", (server, sender, args) -> {
//      DebugSettings.setAnimationFreezeEnabled(!DebugSettings.isAnimationFreezeEnabled());
//    }));
//
//    addCommand(new CommandDragonLambda("renderCentrepoints", (server, sender, args) -> {
//      DebugSettings.setRenderCentrePoints(!DebugSettings.isRenderCentrePoints());
//    }));
//
//    addCommand(new CommandDragonLambda("renderDragonPoints", (server, sender, args) -> {
//      DebugSettings.setRenderDragonPoints(!DebugSettings.isRenderDragonPoints());
//    }));
//
//    addCommand(new CommandDragonLambda("renderXYZmarkers", (server, sender, args) -> {
//      DebugSettings.setRenderXYZmarkers(!DebugSettings.isRenderXYZmarkers());
//    }));
//
//    addCommand(new CommandDragonLambda("setRiderPositionTweak", (server, sender, args) -> {
//      DebugSettings.setRiderPositionTweak(!DebugSettings.isRiderPositionTweak());
//    }));
//
//    addCommand(new CommandDragonLambda("forceDragonWalk", (server, sender, args) -> {
//      DebugSettings.setForceDragonWalk(!DebugSettings.isForceDragonWalk());
//    }));
//
//    addCommand(new CommandDragonLambda("dragonWalkStraightLine", (server, sender, args) -> {
//      DebugSettings.setDragonWalkStraightLine(!DebugSettings.isDragonWalkStraightLine());
//    }));
//
//    addCommand(new CommandDragonLambda("parameter", (server, sender, args) -> {
//      String paramName = args[0];
//      double value = parseDouble(args[1], -1e10);
//      DebugSettings.setDebugParameter(paramName, value);
//    }));
//
//    addCommand(new CommandDragonLambda("testBreeds", dragon -> {
//      new Thread(() -> {
//        try {
//          for (EnumDragonBreed breed : EnumDragonBreed.values()) {
//            dragon.setBreedType(breed);
//            Thread.sleep(1000);
//          }
//        } catch (InterruptedException ex) {
//        }
//      }).start();
//    }));
//
//    addCommand(new CommandDragonLambda("printDefaultVariantsFile", dragon -> {
//    }));
//
//    addCommand(new CommandDragonLambda("testStages", dragon -> {
//      new Thread(() -> {
//        try {
//          for (DragonLifeStage stage : DragonLifeStage.values()) {
//            dragon.lifeStage().setLifeStage(stage);
//            Thread.sleep(1000);
//          }
//        } catch (InterruptedException ex) {
//        }
//      }).start();
//    }));
//
//    addCommand(new CommandDragonLambda("testAge", dragon -> {
//      dragon.lifeStage().setTicksSinceCreation(18000);
//    }));
//
//    addCommand(new CommandDragonLambda("testMount", (server, sender, args) -> {
//      applyModifier(server, sender, dragon -> {
//        if (!(sender instanceof EntityPlayerMP)) {
//          return;
//        }
//
//        EntityPlayerMP player = (EntityPlayerMP) sender;
//        dragon.tamedFor(player, true);
//        dragon.setSaddled(true);
//        dragon.setCustomNameTag("Puff");
//        player.startRiding(dragon);
//      });
//    }));
//
//    addCommand(new CommandDragonLambda("kill", dragon -> {
//      dragon.setHealth(0);
//    }));
//  }
//
//  @Override
//  public String getName() {
//    return "debug";
//  }
//}
