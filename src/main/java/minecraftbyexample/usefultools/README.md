# USEFUL TOOLS

This package is a bunch of functions and tools that I use occasionally; some of them make debugging easier, 
  the others are helpers for particular tasks or calculations
   
They're not really part of MBE but you might perhaps find them useful.

## Debugging tools
###Console commands:

####VoxelShape debugging-
For the block that the player is looking at, draw the outline of its VoxelShape:<br>
_/mbedebug param showshape 1_          --> draw the Block.getShape() in red<br>
_/mbedebug param showrendershape 1_    --> draw the Block.getRenderShape() in blue<br>
_/mbedebug param showcollisionshape 1_ --> draw the Block.getCollisionShape() in green<br>
_/mbedebug param showraytraceshape 1_  --> draw the Block.getRayTraceShape() in magenta<br>

####Entity spawning control
_/mbedebug param preventspawning 1_    --> stops any entities from spawning (useful if you are debugging an entity and you 
   don't want annoying slimes to keep spawning nearby)<br>
_/mbedebug action killallentities_  ---> kill all entities nearby (same as "/kill @e[type=!minecraft:player]")<br>
   