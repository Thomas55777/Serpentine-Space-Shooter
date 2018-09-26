package thomasWilliams.TetrisBlast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import android.R.integer;

class BlockStructure {
	private int intGridID;
	private int intClassID;

	public BlockStructure(int intGridID, int intClassID) {
		this.intGridID = intGridID;
		this.intClassID = intClassID;
	}

	public int getGridID() {
		return intGridID;
	}

	public int getClassID() {
		return intClassID;
	}

	public boolean equals(Object objBlockStructure) {
		BlockStructure blockStructure = (BlockStructure) objBlockStructure;
		if (blockStructure.getGridID() == intGridID)
			return true;
		return false;
	}

	public static Comparator<BlockStructure> GridIDComparatorDesc = new Comparator<BlockStructure>() {
		@Override
		public int compare(BlockStructure blockStructure1, BlockStructure blockStructure2) {
			Integer intGridID1 = Integer.valueOf(blockStructure1.getGridID());
			Integer intGridID2 = Integer.valueOf(blockStructure2.getGridID());

			// ascending order
			// return intGridID1.compareTo(intGridID2);

			// descending order
			return intGridID2.compareTo(intGridID1);
		}

	};
}

public class CompileStructure {

	// private List<Integer> lstMaxBlocks = new ArrayList<Integer>();
	private HashMap<Integer, Integer> mapMaxBlocks = new HashMap<Integer, Integer>();
	ArrayList<DefineStructure> lstStructure = new ArrayList<DefineStructure>();
	List<BlockStructure> lstBlocks = new ArrayList<BlockStructure>();

	public CompileStructure(List<DrawBlocks> lstDrawSetBlocks) {
		for (int i = 0; i < lstDrawSetBlocks.size(); i++) {
			BlockStructure blockStructure = new BlockStructure(lstDrawSetBlocks.get(i).getGridID(), i);
			if (!lstBlocks.contains(blockStructure))
				lstBlocks.add(new BlockStructure(lstDrawSetBlocks.get(i).getGridID(), i));
		}

		Collections.sort(lstBlocks, BlockStructure.GridIDComparatorDesc);

		// Put the Most Top Left Block into the lstStructure
		int intTower = 1;
		AddStructure(lstBlocks.get(lstBlocks.size() - 1).getGridID(), lstBlocks.get(lstBlocks.size() - 1).getClassID(), intTower);

		// Loop Through the Remaining Blocks and Add them to lstStructure
		for (int i = lstBlocks.size() - 1; i >= 0; i--) {
			int intTowerID;
			int intGridID = lstBlocks.get(i).getGridID();
			int intClassID = lstBlocks.get(i).getClassID();
			DefineStructure findBlockLeft = new DefineStructure(intGridID - 1, -1, -1);
			DefineStructure findBlockUp = new DefineStructure(intGridID - AppConfig.intGridCountX, -1, -1);

			if (intGridID % AppConfig.intGridCountX != 0 && lstStructure.contains(findBlockLeft) && lstStructure.contains(findBlockUp)) {
				// Merge the two Towers
				intTowerID = findTower(lstStructure, findBlockUp.getGridID());
				int intTowerMerge = findTower(lstStructure, findBlockLeft.getGridID());
				for (DefineStructure defineStructure : lstStructure) {
					if (defineStructure.getTower() == intTowerMerge)
						defineStructure.setTower(intTowerID);
				}

				// Remove the Tower from the mapping
				mapMaxBlocks.remove(intTowerMerge);
			} else if (intGridID % AppConfig.intGridCountX != 0 && lstStructure.contains(findBlockLeft)) {
				// Add the block to the same Tower as the block to its Left
				intTowerID = findTower(lstStructure, findBlockLeft.getGridID());
			} else if (lstStructure.contains(findBlockUp)) {
				// Add the block to the same Tower as the block above it
				intTowerID = findTower(lstStructure, findBlockUp.getGridID());
			} else {
				// Add the block to a new Tower
				intTower++;
				intTowerID = intTower;
			}

			// Add the block to the Tower
			AddStructure(intGridID, intClassID, intTowerID);
		}
	}

	private void AddStructure(int intGridID, int intClassID, int intTowerID) {
		lstStructure.add(new DefineStructure(intGridID, intClassID, intTowerID));
		lstBlocks.remove(lstBlocks.size() - 1);
		mapMaxBlocks.put(intTowerID, intGridID);
	}

	private int findTower(ArrayList<DefineStructure> lstStructure, int intGridID) {
		for (DefineStructure defineStructure : lstStructure) {
			if (defineStructure.getGridID() == intGridID)
				return defineStructure.getTower();
		}
		return -1;
	}

	public HashMap<Integer, Integer> getMaxBlocks() {
		return mapMaxBlocks;
	}

	public ArrayList<DefineStructure> getStructure() {
		return lstStructure;
	}
}