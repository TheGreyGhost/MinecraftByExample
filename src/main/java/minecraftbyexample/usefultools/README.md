# USEFUL TOOLS

This package is a bunch of functions and tools that I use occasionally; some of them make debugging easier, 
  the others are helpers for particular tasks or calculations
   
They're not really part of MBE but you might perhaps find them useful.

## Debugging tools
### Console commands:

#### VoxelShape debugging-
For the block that the player is looking at, draw the outline of its VoxelShape:<br>
* _/mbedebug param showshape 1_          --> draw the Block.getShape() in red<br>
* _/mbedebug param showrendershape 1_    --> draw the Block.getRenderShape() in blue<br>
* _/mbedebug param showcollisionshape 1_ --> draw the Block.getCollisionShape() in green<br>
* _/mbedebug param showraytraceshape 1_  --> draw the Block.getRayTraceShape() in magenta<br>

#### Entity spawning control
* _/mbedebug param preventspawning 1_    --> stops any entities from spawning (useful if you are debugging an entity and you 
   don't want annoying slimes to keep spawning nearby)<br>
* _/mbedebug trigger killallentities_  ---> kill all entities nearby (same as "/kill @e[type=!minecraft:player]")<br>
   
#### Run a test case
* _/mbedebug test testnumber_ --> run testnumber on the server- see mbe75
   
### Logging
MethodCallLogger - used to trace code execution when you can't use breakpoints because they affect the flow (eg user input
  debugging)
  
### Interactive tweaking of parameters
These commands are useful to adjust in-game parameters in real time without having to stop, edit code, recompile, and run again
They are especially useful when debugging rendering (eg translations, rotations, or other interactive adjustments).

* _/mbedebug param yourcustomname newvalue_ set variable yourcustomname to newvalue, which your code can retrieve using DebugSettings.getDebugParameter()
* _/mbedebug paramvec_3d yourcustomname newvalue Vecd_ set variable yourcustomname to newvalue Vec3d, which your code can retrieve using DebugSettings.getDebugParameterVec3d()
* _/mbedebug trigger yourcustomname_ set variable yourcustomname, which your code can retrieve using DebugSettings.getDebugTrigger() - value resets after the call

