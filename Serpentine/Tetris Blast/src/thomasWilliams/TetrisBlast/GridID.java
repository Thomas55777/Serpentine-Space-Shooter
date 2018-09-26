package thomasWilliams.TetrisBlast;

public class GridID {
	private float fltLineSpacing;
	private int[] aryGridID;
	private float[] aryGridX;
	private float[] aryGridY;
	private boolean[] aryBlockID;

	public GridID(float fltLineSpacing, float fltLeftBound, float fltUpperBound) {
		this.fltLineSpacing = fltLineSpacing;

		aryGridID = new int[AppConfig.intGridCountX * AppConfig.intGridCountY];
		aryGridX = new float[AppConfig.intGridCountX * AppConfig.intGridCountY];
		aryGridY = new float[AppConfig.intGridCountX * AppConfig.intGridCountY];

		// Set the GridID and X Y coordinates incrementing left to right
		for (int i = 0; i < aryGridID.length; i++) {
			aryGridID[i] = i;
			aryGridX[i] = fltLeftBound + ((i % AppConfig.intGridCountX) * fltLineSpacing);
			// aryGridY[((i % AppConfig.intGridCountX) * AppConfig.intGridCountY) + (i / AppConfig.intGridCountX)] = fltUpperBound + ((i % AppConfig.intGridCountX) * fltLineSpacing);
			aryGridY[i] = fltUpperBound + ((i / AppConfig.intGridCountX) * fltLineSpacing);
		}

		// Set all the Block IDs to false
		aryBlockID = new boolean[AppConfig.intGridCountX * AppConfig.intGridCountY];
		for (boolean booBlockId : aryBlockID)
			booBlockId = false;
	}

	public int getGridID(float fltTouchX, float fltTouchY) {
		for (int i = 0; i < aryGridID.length; i++) {
			if (fltTouchX >= aryGridX[i] && fltTouchX <= aryGridX[i] + fltLineSpacing)
				if (fltTouchY >= aryGridY[i] && fltTouchY <= aryGridY[i] + fltLineSpacing)
					return aryGridID[i];
		}
		return -1;
	}

	public float getGridX(int ID) {
		return aryGridX[ID];
	}

	public float getGridY(int ID) {
		return aryGridY[ID];
	}

	public void setBlockID(int intBlockID, boolean booValue) {
		aryBlockID[intBlockID] = booValue;
	}

	public boolean getBlockID(int intBlockID) {
		return aryBlockID[intBlockID];
	}
}
