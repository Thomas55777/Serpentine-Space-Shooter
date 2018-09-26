package thomasWilliams.TetrisBlast;

import java.util.Comparator;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;

public class DrawBlocks {
	private int x = 0;
	private int y = 0;
	private GameView gameView;
	private Rect srcSprite;
	private Rect dstSprite;
	private int intFrameCounter = 0;
	private int width;
	private int height;
	private Bitmap bmp;
	private int intGridID = -1;
	private int intNextGridID;
	private GridID gridID;
	private boolean booBlockSet = false;
	private boolean booBlockFalling = false;

	public static Comparator<DrawBlocks> GridIDComparatorDesc = new Comparator<DrawBlocks>() {
		@Override
		public int compare(DrawBlocks drawBlocks1, DrawBlocks drawBlocks2) {
			Integer intGridID1 = Integer.valueOf(drawBlocks1.getGridID());
			Integer intGridID2 = Integer.valueOf(drawBlocks2.getGridID());

			// descending order
			return intGridID2.compareTo(intGridID1);
		}
	};
	public static Comparator<DrawBlocks> GridIDComparatorAsc = new Comparator<DrawBlocks>() {
		@Override
		public int compare(DrawBlocks drawBlocks1, DrawBlocks drawBlocks2) {
			Integer intGridID1 = Integer.valueOf(drawBlocks1.getGridID());
			Integer intGridID2 = Integer.valueOf(drawBlocks2.getGridID());

			// ascending order
			 return intGridID1.compareTo(intGridID2);
		}
	};

	public DrawBlocks(GameView gameView, Bitmap bmp) {
		this.gameView = gameView;
		this.width = bmp.getWidth();
		this.height = bmp.getHeight();
		this.bmp = bmp;
	}

	public void scaleBitmap(float fltScaleFactorX, float fltScaleFactorY) {
		Bitmap bmpRaw = bmp;
		Matrix scalePad = new Matrix();
		scalePad.postScale(fltScaleFactorX, fltScaleFactorY);
		bmp = Bitmap.createBitmap(bmpRaw, 0, 0, bmpRaw.getWidth(), bmpRaw.getHeight(), scalePad, false);
		bmpRaw.recycle();
	}

	public void FindNextGridID() {
		gridID = gameView.getGridID();
		intNextGridID = gridID.getGridID(x + 1, y + height + 1);
	}

	public void CalcBlockSet() {
		FindNextGridID();

		// Stop the block from going below the Grid or into another Block
		if (intNextGridID == -1 || gridID.getBlockID(intNextGridID)) {
			gameView.setGenerateNewBlock(true);
		}
	}

	public void MoveBlockDown() {
		// Move the block down
		if (AppConfig.intFrameCounter % AppConfig.intFrameCounterFrequency == 0) {
			// Set the GridID
			intGridID = intNextGridID;

			// Set the Y for the Block
			y = (int) gridID.getGridY(intNextGridID);

			// Set the X for the Block
			x = (int) gridID.getGridX(intNextGridID);

			// Set the Frame Counter back to 0
//			intFrameCounter = 0;
		}
	}

	private void update() {
		if (gameView.getGenerateNewBlock())
			if (intGridID == -1)
				gameView.setGameOver();
			else
				gridID.setBlockID(intGridID, true);
		else {
			if (!booBlockFalling)
				MoveBlockDown();
		}

		// Set the src and dst
		srcSprite = new Rect(0, 0, width, height);
		dstSprite = new Rect(x, y, x + width, y + height);

		intFrameCounter++;
	}

	public Bitmap getBmp() {
		return bmp;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public void setX(int intX) {
		x = intX;
	}

	public void setY(int intY) {
		y = intY;
	}

	public void setBlock(boolean booSetBlock) {
		booBlockSet = booSetBlock;
	}

	public void setBlockFalling(boolean booFalling) {
		booBlockFalling = booFalling;
	}

	public int getGridID() {
		return intGridID;
	}

	public int getNextGridID() {
		return intNextGridID;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public void onDraw(Canvas canvas) {
		if (!booBlockSet || booBlockFalling)
			update();
		canvas.drawBitmap(bmp, srcSprite, dstSprite, null);
	}
}
