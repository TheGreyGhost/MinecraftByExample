# MBE15_ITEM_DYNAMIC_ITEM_MODEL

Based on code by Herbix, RainWarrior, and rikka0w0

This example shows how to create a dynamic item model in conjunction with `ModelBakeEvent`

Normally, it can be difficult to customise your item's appearance without creating a new BakedModel for every different state the item has. For example, an item which shows the time in hours and minutes would need 24 * 60 different `BakedModels`.

`ItemOverrideList.handleItemState()` and `IBakedModel.getQuads()` lets you change the item's appearance, typically based on NBT information stored within the itemstack, by adding extra `BakedQuads` that will be rendered.

How it works:

1. An Item is defined and loaded from a BlockModel json file as normal (see `StartupClientOnly`).
1. `ModelBakeEvent` is then used to find the item's `IBakedModel` in the modelRegistry and replace it with `ChessboardModel`
1. Immediately before the render:
    1. `ChessboardModel#getOverrides()` is called and returns `ChessboardItemOverrideList`
    1. `ChessboardItemOverrideList#handleItemState(itemstack)` is called, it inspects the itemstack and returns a `ChessBoardFinalisedModel()` with the corresponding number of chess pieces
1. Vanilla then calls `ChessBoardFinalisedModel.getQuads()` as normal, to get a list of `BakedQuads` for rendering the item. `ChessBoardFinalisedModel` uses the stored `numberOfChessPieces` to create the extra `BakedQuads` and add them to the list used for drawing the chessboard.

The Chessboard appears in the miscellaneous tab in the creative inventory.

The pieces you need to understand are located in:

* `StartupClientOnly` and `StartupCommon`
* `ChessboardModel`
* `ChessboardFinalisedModel`
* `ChessboardItemOverrideList`
* `ItemChessBoard`
* `ModelBakeEventHandlerMBE15`
* `resources\assets\minecraftbyexample\lang\en_US.lang` -- for the displayed name of the item
* `resources\assets\minecraftbyexample\models\item\mbe15_item_chessboard` -- the model for rendering the chessboard
* `resources\assets\minecraftbyexample\textures\items\mbe15_chessboard` -- the texture used for the chessboard

More background information:

See also MBE04 and MBE05 for different ways of inserting your `IBlockModel` into the model registry and of combining existing models into your smart model.
