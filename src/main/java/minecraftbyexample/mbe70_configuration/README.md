# MBE70_CONFIGURATION

Code by twrightsman (greekphysique)

This example shows how to implement simple configuration scheme, specifically:

1. How to create/load/save a configuration file and its values
1. How to create a configuration screen that can be accessed through the Forge Mod List

Parts to understand are:

* `MBEGuiFactory`
* Both `Startup` Classes
* Main mod file `MinecraftByExample`:
    * guiFactory = `MinecraftByExample.GUIFACTORY` in the @Mod annotation
* `resources\assets\minecraftbyexample\lang\en_US.lang` -- for displayed names in the GUI

## Common errors

1. Forgot to define guiFactory variable in @Mod