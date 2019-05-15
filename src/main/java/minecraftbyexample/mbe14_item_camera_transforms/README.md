# MBE14_ITEM_CAMERA_TRANSFORMS

When you create a custom item, you need to provide `ItemCameraTransforms` for it. This example is an interactive tool to help you get the transforms right.

Code has been moved to a separate project to keep it up to date more easily:

See [https://github.com/TheGreyGhost/ItemTransformHelper](https://github.com/TheGreyGhost/ItemTransformHelper)
See also [http://www.planetminecraft.com/mod/item-transform-helper---interactively-rotate-scale-translate/](http://www.planetminecraft.com/mod/item-transform-helper---interactively-rotate-scale-translate/)

How to use:

1. Place the `ItemCamera` somewhere in the player's hotbar
1. Hold the `Item` you want to modify in the player's hand
1. Use the up/down keys to move up and down the menu.
1. Use the left/right keys to edit the value (eg scaleX)
1. To select a different view (first person, third person, gui, head (for helmet)) change the VIEW menu item
1. To reset the parameters for the current view, select the RESET menu item and press left or right. You can also copy parameters from a vanilla item to your custom item:
    1. Hold the vanilla item in your hand
    1. Select RESET, press left or right.
    1. Hold your custom item in your hand.
1. When your item looks right, select PRINT and press left or right to print the current parameters to the console
1. Copy the appropriate lines from the console to your item model json file

The item will appear in the Helpers tab in the creative inventory.

For background information on:

* items: see [http://greyminecraftcoder.blogspot.com/2013/12/items.html](http://greyminecraftcoder.blogspot.com/2013/12/items.html)
* rendering items: see [http://greyminecraftcoder.blogspot.com.au/2014/12/item-rendering-18.html](http://greyminecraftcoder.blogspot.com.au/2014/12/item-rendering-18.html)
* This link describes the structure of the JSON Item Model file (see the Item Models section halfway down): [http://minecraft.gamepedia.com/Block_models](http://minecraft.gamepedia.com/Block_models)


