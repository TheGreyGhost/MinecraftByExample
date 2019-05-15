# MBE75_TESTING_FRAMEWORK

This example is a useful tool that can help you automate testing of your classes in-game. This helps make debugging a whole lot easier, and is extremely useful for regression testing when you update your mod to the next Forge release (which always seems to break a pile of stuff you didn't expect!).

Unit testing using JUnit or similar is easy enough for standalone classes you write, but it's not much good for classes which rely on vanilla classes such as `World`, `EntityPlayer`, or similar. It's almost impossible to mock or stub these without all sorts of brittle tricks.

An alternative I've found useful is to start up Minecraft and run your automated test-cases in-game.

For example--if you want to test what happens when your block catches on fire, your test case can put the block in the world, set fire to it, then inspect the result and report success or failure.

One of the easiest ways to trigger a test case is using an item. In this example, using the `ItemTestRunner` item will trigger a test case.  Changing the stacksize will change the test case that is executed.

For example--stacksize of 2 will run test case 2; stacksize of 20 will run test case 20.

A couple of useful functions are also demonstrated:

1. teleporting the player to the test location
1. copying a region of the world to a test location. This can be useful if you have a complicated test pattern for your blocks, eg:
    1. generate a save game with a test pattern of blocks at the source region
    1. before each test, copy the source region to the test region and run your tests.

The pieces you need to understand are located in:

* `ItemTestRunner`
* `TestRunner`

See MBE10 for more information on simple items.
