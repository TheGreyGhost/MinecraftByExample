package minecraftbyexample.mbe20_tileenentity_data;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.*;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;

import java.util.Arrays;

/**
 * User: brandon3055
 * Date: 06/01/2015
 *
 * This is a simple tile entity which stores some data
 * When placed, it waits for 10 seconds then replaces itself with a random block
 */
public class TileEntityData extends TileEntity  {

	private final int INVALID_VALUE = -1;
	private int ticksLeftTillDisappear = INVALID_VALUE;  // the time (in ticks) left until the block disappears

	// set by the block upon creation
	public void setTicksLeftTillDisappear(int ticks)
	{
		ticksLeftTillDisappear = ticks;
	}

	// This is where you save any data that you don't want to lose when the tile entity unloads
	// In this case, we only need to store the ticks left until explosion, but we store a bunch of other
	//  data as well to serve as an example
	// Mention NBTExplorer here
	@Override
	public void writeToNBT(NBTTagCompound parentNBTTagCompound)
	{
		super.writeToNBT(parentNBTTagCompound); // The super call is required to save the tiles location

		parentNBTTagCompound.setInteger("ticksLeft", ticksLeftTillDisappear);
		// alternatively - could use parentNBTTagCompound.setTag("ticksLeft", new NBTTagInt(ticksLeftTillDisappear));

		// some examples of other NBT tags - browse NBTTagCompound or serach for the subclasses of NBTBase for more

		parentNBTTagCompound.setString("testString", testString);

		NBTTagCompound blockPosNBT = new NBTTagCompound();        // NBTTagCompound is equivalent to a Java HashMap
		blockPosNBT.setInteger("x", testBlockPos.getX());
		blockPosNBT.setInteger("y", testBlockPos.getY());
		blockPosNBT.setInteger("z", testBlockPos.getZ());
		parentNBTTagCompound.setTag("testBlockPos", blockPosNBT);

		NBTTagCompound itemStackNBT = new NBTTagCompound();
		testItemStack.writeToNBT(itemStackNBT);                     // make sure it's not null..
		parentNBTTagCompound.setTag("testItemStack", itemStackNBT);

		parentNBTTagCompound.setIntArray("testIntArray", testIntArray);

		NBTTagList doubleArrayNBT = new NBTTagList();                     // an NBTTagList is similar to a Java ArrayList
		for (double value : testDoubleArray) {
			doubleArrayNBT.appendTag(new NBTTagDouble(value));
		}
		parentNBTTagCompound.setTag("testDoubleArray", doubleArrayNBT);

		NBTTagList doubleArrayWithNullsNBT = new NBTTagList();
		for (int i = 0; i < testDoubleArrayWithNulls.length; ++i) {
			Double value = testDoubleArrayWithNulls[i];
			if (value != null) {
				NBTTagCompound dataForThisSlot = new NBTTagCompound();
				dataForThisSlot.setInteger("i", i);
				doubleArrayWithNullsNBT.appendTag(dataForThisSlot);
			}
		}
		parentNBTTagCompound.setTag("testDoubleArrayWithNulls", doubleArrayWithNullsNBT);
	}

	// This is where you load the data that you saved in writeToNBT
	@Override
	public void readFromNBT(NBTTagCompound parentNBTTagCompound)
	{
		super.readFromNBT(parentNBTTagCompound); // The super call is required to load the tiles location

		// important rule: never trust the data you read from NBT, make sure it can't cause a crash
		final int NBT_COMPOUND_ID = 10;					// see NBTBase.createNewByType()
		final int NBT_INTARRAY_ID = 11;					// see NBTBase.createNewByType()
		final int NBT_TAGLIST_ID = 9;					// see NBTBase.createNewByType()
		final int NBT_STRING_ID = 8;          // see NBTBase.createNewByType()

		final int NBT_INT_ID = 3;					// see NBTBase.createNewByType()
		int readTicks = INVALID_VALUE;
		if (parentNBTTagCompound.hasKey("ticksLeft", NBT_INT_ID)) {  // check if the key exists and is an Int. You can omit this if a default value of 0 is ok.
			readTicks = parentNBTTagCompound.getInteger("ticksLeft");
			if (readTicks < 0) readTicks = INVALID_VALUE;
		}
		ticksLeftTillDisappear = readTicks;

		// some examples of other NBT tags - browse NBTTagCompound or serach for the subclasses of NBTBase for more

		String readTestString = null;
		if (parentNBTTagCompound.hasKey("testString", NBT_STRING_ID)) {
			readTestString = parentNBTTagCompound.getString("testString");
		}
		if (!testString.equals(readTestString)) {
			System.err.println("testString mismatch:" + readTestString);
		}

		NBTTagCompound blockPosNBT = parentNBTTagCompound.getCompoundTag("testBlockPos");
		BlockPos readBlockPos = null;
		if (blockPosNBT.hasKey("x", NBT_INT_ID) && blockPosNBT.hasKey("y", NBT_INT_ID) && blockPosNBT.hasKey("z", NBT_INT_ID) ) {
			readBlockPos = new BlockPos(blockPosNBT.getInteger("x"), blockPosNBT.getInteger("y"), blockPosNBT.getInteger("z"));
		}
		if (readBlockPos == null || !testBlockPos.equals(readBlockPos)) {
			System.err.println("testBlockPos mismatch:" + readBlockPos);
		}

		NBTTagCompound itemStackNBT = parentNBTTagCompound.getCompoundTag("testItemStack");
		ItemStack readItemStack = ItemStack.loadItemStackFromNBT(itemStackNBT);
		if (!ItemStack.areItemStacksEqual(testItemStack, readItemStack)) {
			System.err.println("testItemStack mismatch:" + readItemStack);
		}
 UP TO HERE
		parentNBTTagCompound.setIntArray("testIntArray", testIntArray);

		NBTTagList doubleArrayNBT = new NBTTagList();                     // an NBTTagList is similar to a Java ArrayList
		for (double value : testDoubleArray) {
			doubleArrayNBT.appendTag(new NBTTagDouble(value));
		}
		parentNBTTagCompound.setTag("testDoubleArray", doubleArrayNBT);

		NBTTagList doubleArrayWithNullsNBT = new NBTTagList();
		for (int i = 0; i < testDoubleArrayWithNulls.length; ++i) {
			Double value = testDoubleArrayWithNulls[i];
			if (value != null) {
				NBTTagCompound dataForThisSlot = new NBTTagCompound();
				dataForThisSlot.setInteger("i", i);
				doubleArrayWithNullsNBT.appendTag(dataForThisSlot);
			}
		}
		parentNBTTagCompound.setTag("testDoubleArrayWithNulls", doubleArrayWithNullsNBT);



		final byte NBT_TYPE_COMPOUND = 10;       // See NBTBase.createNewByType() for a listing
		NBTTagList dataForAllSlots = parentNBTTagCompound.getTagList("Items", NBT_TYPE_COMPOUND);

		Arrays.fill(itemStacks, null);           // set all slots to empty
		for (int i = 0; i < dataForAllSlots.tagCount(); ++i) {
			NBTTagCompound dataForOneSlot = dataForAllSlots.getCompoundTagAt(i);
			byte slotNumber = dataForOneSlot.getByte("Slot");
			if (slotNumber >= 0 && slotNumber < this.itemStacks.length) {
				this.itemStacks[slotNumber] = ItemStack.loadItemStackFromNBT(dataForOneSlot);
			}
		}

		// Load everything else.  Trim the arrays (or pad with 0) to make sure they have the correct number of elements
		cookTime = parentNBTTagCompound.getShort("CookTime");
		burnTimeRemaining = Arrays.copyOf(parentNBTTagCompound.getIntArray("burnTimeRemaining"), FUEL_SLOTS_COUNT);
		burnTimeInitialValue = Arrays.copyOf(parentNBTTagCompound.getIntArray("burnTimeInitial"), FUEL_SLOTS_COUNT);
		cachedNumberOfBurningSlots = -1;
	}

	// When the world loads from disk, the server needs to send the TileEntity information to the client
	//  it uses getDescriptionPacket() and onDataPacket() to do this
	@Override
	public Packet getDescriptionPacket() {
		NBTTagCompound nbtTagCompound = new NBTTagCompound();
		writeToNBT(nbtTagCompound);
		int metadata = getBlockMetadata();
		return new S35PacketUpdateTileEntity(this.pos, metadata, nbtTagCompound);
	}

	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
		readFromNBT(pkt.getNbtCompound());
	}

	private final int [] testIntArray = {5, 4, 3, 2, 1};
	private final double [] testDoubleArray = {1, 2, 3, 4, 5, 6};
	private final Double [] testDoubleArrayWithNulls = {61.1, 62.2, null, 64.4, 65.5};
	private final ItemStack testItemStack = new ItemStack(Items.cooked_chicken, 23);
	private final String testString = "supermouse";
	private final BlockPos testBlockPos = new BlockPos(10, 11, 12);
}
