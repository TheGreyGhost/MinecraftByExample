package minecraftbyexample.mbe20_tileentity_data;

import net.minecraft.block.*;
import net.minecraft.block.SaplingBlock;
import net.minecraft.block.TNTBlock;
import net.minecraft.block.Blocks;
import net.minecraft.item.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.DoubleNBT;
import net.minecraft.nbt.IntNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Random;

/**
 * User: brandon3055
 * Date: 06/01/2015
 *
 * This is a simple tile entity which stores some data
 * When placed, it waits for 10 seconds then replaces itself with a random block
 */
public class TileEntityData extends TileEntity implements ITickableTileEntity {

	private final int INVALID_VALUE = -1;
	private int ticksLeftTillDisappear = INVALID_VALUE;  // the time (in ticks) left until the block disappears

	// set by the block upon creation
	public void setTicksLeftTillDisappear(int ticks)
	{
		ticksLeftTillDisappear = ticks;
	}

	// When the world loads from disk, the server needs to send the TileEntity information to the client
	//  it uses getUpdatePacket(), getUpdateTag(), onDataPacket(), and handleUpdateTag() to do this:
  //  getUpdatePacket() and onDataPacket() are used for one-at-a-time TileEntity updates
  //  getUpdateTag() and handleUpdateTag() are used by vanilla to collate together into a single chunk update packet
	//  Not really required for this example since we only use the timer on the client, but included anyway for illustration
	@Override
  @Nullable
  public SUpdateTileEntityPacket getUpdatePacket()
  {
		CompoundNBT nbtTagCompound = new CompoundNBT();
		write(nbtTagCompound);
		int metadata = getBlockMetadata();
		return new SUpdateTileEntityPacket(this.pos, metadata, nbtTagCompound);
	}

	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
		read(pkt.getNbtCompound());
	}

  /* Creates a tag containing the TileEntity information, used by vanilla to transmit from server to client
 */
  @Override
  public CompoundNBT getUpdateTag()
  {
    CompoundNBT nbtTagCompound = new CompoundNBT();
    write(nbtTagCompound);
    return nbtTagCompound;
  }

  /* Populates this TileEntity with information from the tag, used by vanilla to transmit from server to client
 */
  @Override
  public void handleUpdateTag(CompoundNBT tag)
  {
    this.read(tag);
  }

  // This is where you save any data that you don't want to lose when the tile entity unloads
	// In this case, we only need to store the ticks left until explosion, but we store a bunch of other
	//  data as well to serve as an example.
	// NBTexplorer is a very useful tool to examine the structure of your NBT saved data and make sure it's correct:
	//   http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-tools/1262665-nbtexplorer-nbt-editor-for-windows-and-mac
	@Override
	public CompoundNBT write(CompoundNBT parentNBTTagCompound)
	{
		super.write(parentNBTTagCompound); // The super call is required to save the tile's location

		parentNBTTagCompound.putInt("ticksLeft", ticksLeftTillDisappear);
		// alternatively - could use parentNBTTagCompound.setTag("ticksLeft", IntNBT.func_229692_a_(ticksLeftTillDisappear));

		// some examples of other NBT tags - browse NBTTagCompound or search for the subclasses of INBT for more examples

		parentNBTTagCompound.putString("testString", testString);

		// group x,y,z together under a "testBlockPos" tag
		CompoundNBT blockPosNBT = new CompoundNBT();        // NBTTagCompound is similar to a Java HashMap
		blockPosNBT.putInt("x", testBlockPos.getX());
		blockPosNBT.putInt("y", testBlockPos.getY());
		blockPosNBT.putInt("z", testBlockPos.getZ());
		parentNBTTagCompound.put("testBlockPos", blockPosNBT);

		// ItemStack
		CompoundNBT itemStackNBT = new CompoundNBT();
		testItemStack.write(itemStackNBT);                     // make sure testItemStack is not null first!
		parentNBTTagCompound.put("testItemStack", itemStackNBT);

		// IntArray
		parentNBTTagCompound.putIntArray("testIntArray", testIntArray);

    // List of Doubles
		ListNBT doubleArrayNBT = new ListNBT();                     // an NBTTagList is similar to a Java ArrayList
		for (double value : testDoubleArray) {
			doubleArrayNBT.add(DoubleNBT.func_229684_a_(value)); //todo update when MCP updates
		}
		parentNBTTagCompound.put("testDoubleArray", doubleArrayNBT);

		// List of (integer, double) pairs

		ListNBT doubleArrayWithNullsNBT = new ListNBT();
		for (int i = 0; i < testDoubleArrayWithNulls.length; ++i) {
			Double value = testDoubleArrayWithNulls[i];
			if (value != null) {
				CompoundNBT dataForThisSlot = new CompoundNBT();
				dataForThisSlot.putInt("i", i+1);   // avoid using 0, so the default when reading a missing value (0) is obviously invalid
				dataForThisSlot.putDouble("v", value);
				doubleArrayWithNullsNBT.add(dataForThisSlot);
			}
		}
		parentNBTTagCompound.put("testDoubleArrayWithNulls", doubleArrayWithNullsNBT);
    return parentNBTTagCompound;
	}

	// This is where you load the data that you saved in writeToNBT
	@Override
	public void read(CompoundNBT parentNBTTagCompound)
	{
		super.read(parentNBTTagCompound); // The super call is required to load the tiles location

		// important rule: never trust the data you read from NBT, make sure it can't cause a crash

		final int NBT_INT_ID = 3;					// see NBTBase.createNewByType()
		int readTicks = INVALID_VALUE;
		if (parentNBTTagCompound.hasKey("ticksLeft", NBT_INT_ID)) {  // check if the key exists and is an Int. You can omit this if a default value of 0 is ok.
			readTicks = parentNBTTagCompound.getInteger("ticksLeft");
			if (readTicks < 0) readTicks = INVALID_VALUE;
		}
		ticksLeftTillDisappear = readTicks;

		// some examples of other NBT tags - browse NBTTagCompound or search for the subclasses of NBTBase for more

		String readTestString = null;
		final int NBT_STRING_ID = 8;          // see NBTBase.createNewByType()
		if (parentNBTTagCompound.hasKey("testString", NBT_STRING_ID)) {
			readTestString = parentNBTTagCompound.getString("testString");
		}
		if (!testString.equals(readTestString)) {
			System.err.println("testString mismatch:" + readTestString);
		}

		CompoundNBT blockPosNBT = parentNBTTagCompound.getCompoundTag("testBlockPos");
		BlockPos readBlockPos = null;
		if (blockPosNBT.hasKey("x", NBT_INT_ID) && blockPosNBT.hasKey("y", NBT_INT_ID) && blockPosNBT.hasKey("z", NBT_INT_ID) ) {
			readBlockPos = new BlockPos(blockPosNBT.getInteger("x"), blockPosNBT.getInteger("y"), blockPosNBT.getInteger("z"));
		}
		if (readBlockPos == null || !testBlockPos.equals(readBlockPos)) {
			System.err.println("testBlockPos mismatch:" + readBlockPos);
		}

		CompoundNBT itemStackNBT = parentNBTTagCompound.getCompoundTag("testItemStack");
		ItemStack readItemStack = new ItemStack(itemStackNBT);
		if (!ItemStack.areItemStacksEqual(testItemStack, readItemStack)) {
			System.err.println("testItemStack mismatch:" + readItemStack);
		}

		int [] readIntArray = parentNBTTagCompound.getIntArray("testIntArray");
		if (!Arrays.equals(testIntArray, readIntArray)) {
			System.err.println("testIntArray mismatch:" + readIntArray);
		}

		final int NBT_DOUBLE_ID = 6;					// see NBTBase.createNewByType()
		ListNBT doubleArrayNBT = parentNBTTagCompound.getTagList("testDoubleArray", NBT_DOUBLE_ID);
		int numberOfEntries = Math.min(doubleArrayNBT.tagCount(), testDoubleArray.length);
		double [] readDoubleArray = new double[numberOfEntries];
		for (int i = 0; i < numberOfEntries; ++i) {
			 readDoubleArray[i] = doubleArrayNBT.getDoubleAt(i);
		}
		if (doubleArrayNBT.tagCount() != numberOfEntries || !Arrays.equals(readDoubleArray, testDoubleArray)) {
			System.err.println("testDoubleArray mismatch:" + readDoubleArray);
		}

		final int NBT_COMPOUND_ID = 10;					// see NBTBase.createNewByType()
		ListNBT doubleNullArrayNBT = parentNBTTagCompound.getTagList("testDoubleArrayWithNulls", NBT_COMPOUND_ID);
		numberOfEntries = Math.min(doubleArrayNBT.tagCount(), testDoubleArrayWithNulls.length);
		Double [] readDoubleNullArray = new Double[numberOfEntries];
		for (int i = 0; i < doubleNullArrayNBT.tagCount(); ++i)	{
			CompoundNBT nbtEntry = doubleNullArrayNBT.getCompoundTagAt(i);
			int idx = nbtEntry.getInteger("i") - 1;
			if (nbtEntry.hasKey("v", NBT_DOUBLE_ID) && idx >= 0 && idx < numberOfEntries) {
				readDoubleNullArray[idx] = nbtEntry.getDouble("v");
			}
		}
		if (!Arrays.equals(testDoubleArrayWithNulls, readDoubleNullArray)) {
			System.err.println("testDoubleArrayWithNulls mismatch:" + readDoubleNullArray);
		}
	}

	// Since our TileEntity implements ITickableTileEntity, we get an update method which is called once per tick (20 times / second)
	// When the timer elapses, replace our block with a random one.
	@Override
	public void tick() {
		if (!this.hasWorld()) return;  // prevent crash
		World world = this.getWorld();
		if (world.isRemote) return;   // don't bother doing anything on the client side.
    ServerWorld serverWorld = (ServerWorld)world;
		if (ticksLeftTillDisappear == INVALID_VALUE) return;  // do nothing until the time is valid
		--ticksLeftTillDisappear;
//		this.markDirty();            // if you update a tileentity variable on the server and this should be communicated to the client,
// 																		you need to markDirty() to force a resend.  In this case, the client doesn't need to know because
                                   // nothing happens on the client until the timer expires
		if (ticksLeftTillDisappear > 0) return;   // not ready yet

		Block [] blockChoices = {Blocks.DIAMOND_BLOCK, Blocks.OBSIDIAN, Blocks.AIR, Blocks.TNT, Blocks.CORNFLOWER, Blocks.OAK_SAPLING, Blocks.WATER};
		Random random = new Random();
		Block chosenBlock = blockChoices[random.nextInt(blockChoices.length)];
	  world.setBlockState(this.pos, chosenBlock.getDefaultState());
		if (chosenBlock == Blocks.TNT) {
			Blocks.TNT.catchFire(Blocks.TNT.getDefaultState().with(TNTBlock.UNSTABLE, true), world, pos, null, null);
			world.removeBlock(pos, false);
		} else if (chosenBlock == Blocks.OAK_SAPLING) {
			SaplingBlock blockSapling = (SaplingBlock)Blocks.OAK_SAPLING;
			// blockSapling.generateTree(world, this.pos, blockSapling.getDefaultState(),random);
      blockSapling.func_226942_a_(serverWorld, this.pos, blockSapling.getDefaultState(),random);  //todo rename at next MCP update
		}
	}

	private final int [] testIntArray = {5, 4, 3, 2, 1};
	private final double [] testDoubleArray = {1, 2, 3, 4, 5, 6};
	private final Double [] testDoubleArrayWithNulls = {61.1, 62.2, null, 64.4, 65.5};
	private final ItemStack testItemStack = new ItemStack(Items.COOKED_CHICKEN, 23);
	private final String testString = "supermouse";
	private final BlockPos testBlockPos = new BlockPos(10, 11, 12);
}
