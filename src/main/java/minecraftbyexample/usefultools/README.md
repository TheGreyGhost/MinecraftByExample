# USEFUL TOOLS

This package is a bunch of functions and tools that I use occasionally; some of them make debugging easier, 
  the others are helpers for particular tasks or calculations
   
They're not really part of MBE but you might perhaps find them useful.

## Debugging tools
Console commands:

###VoxelShape debugging-
For the block that the player is looking at, draw the outline of its VoxelShape:
/mbedebug param showshape 1          --> draw the Block.getShape() in red
/mbedebug param showrendershape 1    --> draw the Block.getRenderShape() in blue
/mbedebug param showcollisionshape 1 --> draw the Block.getCollisionShape() in green
/mbedebug param showraytraceshape 1  --> draw the Block.getRayTraceShape() in magenta

###Entity spawning control
/mbedebug param preventspawning 1    --> stops any entities from spawning (useful if you are debugging an entity and you 
   don't want annoying slimes to keep spawning nearby)
   