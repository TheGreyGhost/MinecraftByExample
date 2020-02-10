package minecraftbyexample.usefultools.debugging;

import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.LogicalSidedProvider;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.HashMap;

/**
 * Log data values with time stamp to a datalog file stored in the local data directory for minecraft on this machine
 * Created by TGG on 22/07/2015.
 */
public class DataLogger {
  static public void logData(String datalogName, String valueToLog) {
    try {
      PrintStream printStream = getOrCreate(datalogName);

      final long ROUNDING_FACTOR = 1000L * 1000L; // to nearest ms
      long timeNow = System.nanoTime() / ROUNDING_FACTOR;
      if (timeZero == 0) timeZero = timeNow;
      timeNow -= timeZero;
      printStream.println(timeNow + ", " + valueToLog);

    } catch (FileNotFoundException fnfe) {  // fail silently
      return;
    }
  }

  static private PrintStream getOrCreate(String datalogName) throws FileNotFoundException {
    if (!dataLogs.containsKey(datalogName)) {
      MinecraftServer minecraftServer = LogicalSidedProvider.INSTANCE.get(LogicalSide.SERVER);
      File dataDirectory = minecraftServer.getDataDirectory();

      File newDataLog = new File(dataDirectory, datalogName + ".txt");
      final boolean AUTOFLUSH = true;
      PrintStream stream = new PrintStream(new FileOutputStream(newDataLog), AUTOFLUSH);
      dataLogs.put(datalogName, stream);
    }
    return dataLogs.get(datalogName);
  }

  private static HashMap<String, PrintStream> dataLogs = new HashMap<String, PrintStream>();
  private static long timeZero = 0;

}
