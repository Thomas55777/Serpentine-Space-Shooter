package thomasWilliams.TetrisBlast;

import java.util.Comparator;

public class DefineStructure {
	private int intGridID;
	private int intClassID;
	private int intTower;

	public DefineStructure(int intGridID, int intClassID, int intTower) {
		this.intGridID = intGridID;
		this.intClassID = intClassID;
		this.intTower = intTower;
	}

	public int getGridID() {
		return intGridID;
	}

	public int getClassID() {
		return intClassID;
	}

	public int getTower() {
		return intTower;
	}

	public void setTower(int intTowerID) {
		intTower = intTowerID;
	}

	public boolean equals(Object objDefineStructure) {
		DefineStructure defineStructure = (DefineStructure) objDefineStructure;
		if (defineStructure.getGridID() == intGridID)
			return true;
		return false;
	}

	public static Comparator<DefineStructure> ClassIDComparatorDesc = new Comparator<DefineStructure>() {
		@Override
		public int compare(DefineStructure defineStructure1, DefineStructure defineStructure2) {
			Integer intClassID1 = Integer.valueOf(defineStructure1.getClassID());
			Integer intClassID2 = Integer.valueOf(defineStructure2.getClassID());

			// descending order
			return intClassID2.compareTo(intClassID1);
		}

	};
	public static Comparator<DefineStructure> ClassIDComparatorAsc = new Comparator<DefineStructure>() {
		@Override
		public int compare(DefineStructure defineStructure1, DefineStructure defineStructure2) {
			Integer intClassID1 = Integer.valueOf(defineStructure1.getClassID());
			Integer intClassID2 = Integer.valueOf(defineStructure2.getClassID());

			// ascending order
			return intClassID1.compareTo(intClassID2);
		}

	};
}