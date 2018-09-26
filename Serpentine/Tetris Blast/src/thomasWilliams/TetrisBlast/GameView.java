package thomasWilliams.TetrisBlast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Typeface;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

import com.pad.android.iappad.AdController;

public class GameView extends SurfaceView {

	// Create the GameView System Variable
	private SurfaceHolder holder;
	private boolean booSoundOn;
	private GameLoopThread gameLoopThread;
	private Context applicationContext;
	private PaintClass paintClass;

	// Create the GameView Variables
	private Bitmap bmpGameViewRaw;
	private Bitmap bmpGameViewScaled;
	private Bitmap bmpGameDraw;
	private Paint pntGameDrawBitmap = new Paint();
	private Paint pntLetterBox = new Paint();
	private Bitmap bmpBackgroundImage;
	private float fltLeftScreenBound;
	private float fltRightScreenBound;
	private float fltUpperScreenBound;
	private float fltLowerScreenBound;
	private float fltLeftBound;
	private float fltRightBound;
	private float fltUpperBound;
	private float fltLowerBound;
	private float fltLineSpacing;
	private float fltGameMarginLeft;
	private float fltGameMarginRight;
	private float fltGameMarginUpper;
	private float fltGameMarginBottom;
	private GridID gridID;
	private int intGridID;

	// Create the List and ArrayList Variables
	private List<DrawGrid> lstBitmapBackground = new ArrayList<DrawGrid>();
	private List<DrawHighlight> lstDrawHighlight = new ArrayList<DrawHighlight>();
	private List<DrawBlocks> lstDrawMovingBlocks = new ArrayList<DrawBlocks>();
	private List<DrawBlocks> lstDrawSetBlocks = new ArrayList<DrawBlocks>();
	private List<DrawBlocks> lstDrawFallingBlocks = new ArrayList<DrawBlocks>();
	private List<Integer> lstRemoveBlocks = new ArrayList<Integer>();

	private Bitmap bmpBackground;
	private Bitmap bmpGridHighlight;
	private Bitmap bmpBlockGreen;
	private Bitmap bmpBlockYellow;
	private Bitmap bmpBlockRed;
	private Bitmap bmpBlockPurple;
	private Bitmap bmpBlockWhite;
	private Bitmap bmpBlockCyan;
	private Bitmap bmpBlockBlue;
	private boolean booGenerateNewBlock = true;
	private boolean booGameOver = false;

	// Create the Static Variables
	public static int intLetterBoxWidth;
	public static int intLetterBoxHeight;
	public static int intGameDrawWidth;
	public static int intGameDrawHeight;
	public static double dblScaleRatio;

	public GameView(GameMenu gameMenu, SetLevelState setLevelState, final AdController myControllerGameView) {
		super(gameMenu.getApplicationContext());
		this.applicationContext = gameMenu.getApplicationContext();
		this.booSoundOn = setLevelState.getSoundOn();
		gameLoopThread = new GameLoopThread(this);

		holder = getHolder();
		holder.addCallback(new Callback() {

			public void surfaceDestroyed(SurfaceHolder holder) {
			}

			public void surfaceCreated(SurfaceHolder holder) {
				// Load the LeadBolt Ad
				if (AppConfig.booLoadBannerAds && myControllerGameView != null)
					myControllerGameView.loadAd();

				CreateLetterBox();
				if (booSoundOn && AppConfig.booBackgroundMusic)
					CreateBackgroundMusic();

				if (!gameLoopThread.isAlive()) {
					gameLoopThread.setRunning(true);
					// Starts the GameLoopThread run() method
					gameLoopThread.start();
				}

				CreateGameBoard(); // Do this first so that fltLineSpacing is defined
				CreateBitmaps();
				DrawBitmaps();
				CreateStartGridBlocks();
			}

			public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
			}
		});
	}

	private void GenerateStartGridBlocks(int intGridID) {
		DrawBlocks drawBlock = new DrawBlocks(this, bmpBlockWhite);
		drawBlock.setX((int) gridID.getGridX(intGridID));
		drawBlock.setY((int) gridID.getGridY(intGridID));
		drawBlock.CalcBlockSet();
		lstDrawSetBlocks.add(drawBlock);
		gridID.setBlockID(intGridID, true);
	}

	private void CreateBackgroundMusic() {
		PlayMediaPlayer backgroundMusic = new PlayMediaPlayer(applicationContext);
		backgroundMusic.playMusicLoop(R.raw.background_gameplay01);
	}

	private void DrawGameBoard(Canvas canvas) {
		// Draw the Black Screen Background
		pntGameDrawBitmap.setColor(AppConfig.intBackgroundBaseColor); // Black //FF111111
		canvas.drawRect(0, 0, this.getWidth(), this.getHeight(), pntGameDrawBitmap);

		// Draw the Outer Rectangle
		canvas.drawRect(fltLeftScreenBound, fltUpperScreenBound, fltRightScreenBound, fltLowerScreenBound, paintClass.getPaint(0xFFDDDDDD));

		for (int i = 0; i < AppConfig.intGridCountX; i++)
			for (int j = 0; j < AppConfig.intGridCountY; j++) {
				float fltOffsetX = fltLineSpacing * i;
				float fltOffsetY = fltLineSpacing * j;
				canvas.drawRect(fltLeftBound + fltOffsetX, fltUpperBound + fltOffsetY, (fltLeftBound + fltOffsetX) + fltLineSpacing, (fltUpperBound + fltOffsetY) + fltLineSpacing, paintClass.getPaint(0XFF333333));// paintClass.getPaint((int) (0xFF888888 + fltOffsetX + fltOffsetY)));
			}
	}

	private void CreateBitmaps() {
		// Create the PaintClass
		paintClass = new PaintClass();

		DrawBitmap drawBitmap = null;
		float fltBitmapGridRatio;

		// Create the Bitmap for the GameGrid
		bmpBackground = BitmapFactory.decodeResource(getResources(), R.drawable.gridview_66aaff);
		fltBitmapGridRatio = fltLineSpacing / bmpBackground.getWidth() - AppConfig.fltGameGridPadding;
		drawBitmap = new DrawBitmap(bmpBackground);
		drawBitmap.scaleBitmap(fltBitmapGridRatio, fltBitmapGridRatio);
		bmpBackground = drawBitmap.getBmp();

		// Create the Bitmap for Grid Highlight
		bmpGridHighlight = BitmapFactory.decodeResource(getResources(), R.drawable.gridview_66ffaa);
		drawBitmap = new DrawBitmap(bmpGridHighlight);
		drawBitmap.scaleBitmap(fltBitmapGridRatio, fltBitmapGridRatio);
		bmpGridHighlight = drawBitmap.getBmp();

		// Create the Bitmap for the Blocks
		Bitmap bmpBlocks = BitmapFactory.decodeResource(getResources(), R.drawable.blocks_grid);
		float fltBlocksRatio = fltLineSpacing / (bmpBlocks.getWidth() / 3);
		drawBitmap = new DrawBitmap(bmpBlocks);
		drawBitmap.scaleBitmap(fltBlocksRatio, fltBlocksRatio);

		// Get the Colored Blocks
		int intBmpWidth = (int) (drawBitmap.getWidth() / 3);
		int intBmpHeight = (int) (drawBitmap.getHeight() / 3);

		bmpBlockGreen = drawBitmap.getBmp(intBmpWidth * 0, intBmpHeight * 0, intBmpWidth, intBmpHeight);
		bmpBlockYellow = drawBitmap.getBmp(intBmpWidth * 1, intBmpHeight * 0, intBmpWidth, intBmpHeight);
		bmpBlockPurple = drawBitmap.getBmp(intBmpWidth * 2, intBmpHeight * 0, intBmpWidth, intBmpHeight);
		bmpBlockWhite = drawBitmap.getBmp(intBmpWidth * 0, intBmpHeight * 1, intBmpWidth, intBmpHeight);
		bmpBlockRed = drawBitmap.getBmp(intBmpWidth * 1, intBmpHeight * 1, intBmpWidth, intBmpHeight);
		bmpBlockCyan = drawBitmap.getBmp(intBmpWidth * 2, intBmpHeight * 1, intBmpWidth, intBmpHeight);
		bmpBlockBlue = drawBitmap.getBmp(intBmpWidth * 0, intBmpHeight * 2, intBmpWidth, intBmpHeight);
	}

	private void CreateGameBoard() {
		// Create a 10x20 Tetris Grid by drawing 200 rectangles

		// Calculate the four Minimum Margins
		fltGameMarginLeft = (float) (this.getWidth() * AppConfig.dblGameMarginLeft);
		fltGameMarginRight = (float) (this.getWidth() * AppConfig.dblGameMarginRight);
		fltGameMarginUpper = (float) (this.getHeight() * AppConfig.dblGameMarginTop);
		fltGameMarginBottom = (float) (this.getHeight() * AppConfig.dblGameMarginBottom);

		// Calculate the Outer Bounds
		fltLeftScreenBound = fltGameMarginLeft;
		fltRightScreenBound = this.getWidth() - fltGameMarginRight;
		fltUpperScreenBound = fltGameMarginUpper;
		fltLowerScreenBound = this.getHeight() - fltGameMarginBottom;

		// Calculate the Inner Bounds
		fltLeftBound = (float) (fltLeftScreenBound + (AppConfig.dblGameBoardBorderWidth * this.getWidth()));
		fltRightBound = (float) (fltRightScreenBound - (AppConfig.dblGameBoardBorderWidth * this.getWidth()));
		fltUpperBound = (float) (fltUpperScreenBound + (AppConfig.dblGameBoardBorderWidth * this.getHeight()));
		fltLowerBound = (float) (fltLowerScreenBound - (AppConfig.dblGameBoardBorderWidth * this.getHeight()));

		// Figure out the best fit for the phones screen
		// 40
		double dblMaxGridSizeX = (fltRightBound - fltLeftBound) / AppConfig.intGridCountX;
		// 24
		double dblMaxGridSizeY = (fltLowerBound - fltUpperBound) / AppConfig.intGridCountY;

		// Find the Center Point
		double dblGameBoardCenterX = (fltLeftBound + fltRightBound) / 2;
		double dblGameBoardCenterY = (fltUpperBound + fltLowerBound) / 2;

		// This means that you could push the X off the screen so use the X for the Grid
		if (AppConfig.booScreenBestFit)
			if (dblMaxGridSizeY < dblMaxGridSizeX) {
				fltLineSpacing = (float) dblMaxGridSizeX;

				// Adjust the Upper and Lower Y
				fltUpperBound = (float) (dblGameBoardCenterY - (fltLineSpacing * (AppConfig.intGridCountY / 2)));
				fltLowerBound = (float) (dblGameBoardCenterY + (fltLineSpacing * (AppConfig.intGridCountY / 2)));
				fltUpperScreenBound = (float) (fltUpperBound - (AppConfig.dblGameBoardBorderWidth * this.getHeight()));
				fltLowerScreenBound = (float) (fltLowerBound + (AppConfig.dblGameBoardBorderWidth * this.getHeight()));
			} else {
				fltLineSpacing = (float) dblMaxGridSizeY;

				// Adjust the Upper and Lower X
				fltLeftBound = (float) (dblGameBoardCenterX - (fltLineSpacing * (AppConfig.intGridCountX / 2)));
				fltRightBound = (float) (dblGameBoardCenterX + (fltLineSpacing * (AppConfig.intGridCountX / 2)));
				fltLeftScreenBound = (float) (fltLeftBound - (AppConfig.dblGameBoardBorderWidth * this.getWidth()));
				fltRightScreenBound = (float) (fltRightBound + (AppConfig.dblGameBoardBorderWidth * this.getWidth()));
			}
		else {
			if (dblMaxGridSizeY < dblMaxGridSizeX) {
				fltLineSpacing = (float) dblMaxGridSizeY;

				// Adjust the Upper and Lower X
				fltLeftBound = (float) (dblGameBoardCenterX - (fltLineSpacing * (AppConfig.intGridCountX / 2)));
				fltRightBound = (float) (dblGameBoardCenterX + (fltLineSpacing * (AppConfig.intGridCountX / 2)));
				fltLeftScreenBound = (float) (fltLeftBound - (AppConfig.dblGameBoardBorderWidth * this.getWidth()));
				fltRightScreenBound = (float) (fltRightBound + (AppConfig.dblGameBoardBorderWidth * this.getWidth()));
			} else {
				fltLineSpacing = (float) dblMaxGridSizeX;

				// Adjust the Upper and Lower Y
				fltUpperBound = (float) (dblGameBoardCenterY - (fltLineSpacing * (AppConfig.intGridCountY / 2)));
				fltLowerBound = (float) (dblGameBoardCenterY + (fltLineSpacing * (AppConfig.intGridCountY / 2)));
				fltUpperScreenBound = (float) (fltUpperBound - (AppConfig.dblGameBoardBorderWidth * this.getHeight()));
				fltLowerScreenBound = (float) (fltLowerBound + (AppConfig.dblGameBoardBorderWidth * this.getHeight()));
			}
		}

		// Establish the GridID dimentions
		gridID = new GridID(fltLineSpacing, fltLeftBound, fltUpperBound);
	}

	private void DrawBitmaps() {
		// Create the Bitmaps for Background
		for (int j = 0; j < AppConfig.intGridCountY; j++)
			for (int i = 0; i < AppConfig.intGridCountX; i++) {
				CreateBitmapsBackground(bmpBackground, gridID.getGridX(i), gridID.getGridY((j * AppConfig.intGridCountX) + i));
			}
	}

	private void CreateBitmapsBackground(Bitmap bmpBackground, float fltX, float fltY) {
		DrawGrid bitmapDraw = new DrawGrid(bmpBackground);
		bitmapDraw.setX((int) fltX);
		bitmapDraw.setY((int) fltY);

		lstBitmapBackground.add(bitmapDraw);
	}

	private void CreateLetterBox() {
		// Samsung Galaxy S I9000 480x800 5:3
		// Motorola Droid Bionic 540x960 16:9
		// Ideal Screen Ratio = 480/800 = 0.6
		// Calc User Screen Ratio
		// if larger then 0.6 Black Bars Top and Bottom
		// if less then then 0.6 Black Bars Left and Right

		double dblWidth = this.getWidth();
		double dblHeight = this.getHeight();

		double dblUserScreenRatio = dblWidth / dblHeight;

		if (dblUserScreenRatio < AppConfig.dblScreenRatio) {
			intLetterBoxWidth = 0;
			intLetterBoxHeight = (int) ((dblHeight - (dblWidth / AppConfig.dblScreenRatio)) / 2);
		} else if (dblUserScreenRatio > AppConfig.dblScreenRatio) {
			intLetterBoxWidth = (int) ((dblWidth - (dblHeight * AppConfig.dblScreenRatio)) / 2);
			intLetterBoxHeight = 0;
		}

		// Create a 480x800 bitmap and then scale it
		Bitmap bmpBackgroundRaw = Bitmap.createBitmap(AppConfig.intAspectWidth, AppConfig.intAspectHeight, Config.RGB_565); // Config.ARGB_8888 us for transparency
		// Bitmap bmpBackgroundRaw = BitmapFactory.decodeResource(getResources(), R.drawable.background_480x800);
		int imgWidth = bmpBackgroundRaw.getWidth();
		int imgHeight = bmpBackgroundRaw.getHeight();
		int intScaleSizePad = 0;

		// Scale Bitmap to Raw Size
		intScaleSizePad = AppConfig.intAspectHeight;
		float scaleFactorPad = Math.min(((float) intScaleSizePad) / imgWidth, ((float) intScaleSizePad) / imgHeight);
		Matrix scalePad = new Matrix();
		scalePad.postScale(scaleFactorPad, scaleFactorPad);
		bmpGameViewRaw = Bitmap.createBitmap(bmpBackgroundRaw, 0, 0, imgWidth, imgHeight, scalePad, false);

		// Scale to phone size but keep aspect ratio
		if (AppConfig.intAspectWidth > AppConfig.intAspectHeight) {
			intScaleSizePad = this.getWidth() - (intLetterBoxWidth * 2);
		} else {
			intScaleSizePad = this.getHeight() - (intLetterBoxHeight * 2);
		}

		// intScaleSizePad = 540; // width in 480x400
		scaleFactorPad = Math.min(((float) intScaleSizePad) / imgWidth, ((float) intScaleSizePad) / imgHeight);

		scalePad = new Matrix();
		scalePad.postScale(scaleFactorPad, scaleFactorPad);
		bmpGameViewScaled = Bitmap.createBitmap(bmpBackgroundRaw, 0, 0, imgWidth, imgHeight, scalePad, false);
		bmpBackgroundRaw.recycle();
		// /////////////////////////////////////////////////////////////////////////
		// What bmpGameDraw equals is very important as it determines the scaling //
		// /////////////////////////////////////////////////////////////////////////
		if (AppConfig.booGameDrawRaw)
			bmpGameDraw = bmpGameViewRaw;
		else
			bmpGameDraw = bmpGameViewScaled;
		dblScaleRatio = (double) bmpGameDraw.getHeight() / AppConfig.intAspectHeight;
		intGameDrawWidth = bmpGameDraw.getWidth();
		intGameDrawHeight = bmpGameDraw.getHeight();
	}

	@Override
	public boolean onTouchEvent(MotionEvent TouchEvent) {
		final int intTouchEventAction = TouchEvent.getAction();
		float fltTouchX = TouchEvent.getX();
		float fltTouchY = TouchEvent.getY();

		if (true) {
			synchronized (getHolder()) {
				switch (intTouchEventAction & MotionEvent.ACTION_MASK) {
				case MotionEvent.ACTION_DOWN: {
					// Set intGridID to -1 so only one touch can happen
					intGridID = -1;

					// Highlight GameGrid based on Touch Location
					HighlightGrid(fltTouchX, fltTouchY);
					break;
				}

				case MotionEvent.ACTION_MOVE: {
					// Move the Grid Highlight with the Touch Event
					HighlightGrid(fltTouchX, fltTouchY);
					break;
				}

				case MotionEvent.ACTION_UP: {
					if (lstDrawHighlight.size() > 0)
						for (DrawHighlight drawHighlight : lstDrawHighlight)
							lstRemoveBlocks.add(drawHighlight.getGridID());

					// Clear the Grid Highlight
					lstDrawHighlight.clear();
					break;
				}

				case MotionEvent.ACTION_CANCEL: {
					break;
				}

				case MotionEvent.ACTION_POINTER_UP: {
					break;
				}
				case MotionEvent.ACTION_POINTER_DOWN: {
					break;
				}
				}
				// redraw the canvas
				// invalidate();
			}
		}
		return true;
	}

	private void HighlightGrid(float fltTouchX, float fltTouchY) {
		// Find the Center for the Grid Highlight
		fltTouchX = fltTouchX + (fltLineSpacing / 2);
		fltTouchY = fltTouchY + (fltLineSpacing / 2);

		// Set the Touch inside the grid
		if (fltTouchX < fltLeftBound)
			fltTouchX = fltLeftBound + 1;
		if (fltTouchX > fltRightBound)
			fltTouchX = fltRightBound - 1;
		if (fltTouchY > fltLowerBound)
			fltTouchY = fltLowerBound - 1;
		if (fltTouchY < fltUpperBound)
			fltTouchY = fltUpperBound + 1;

		// Find which grid coordinate is being touched
		int intGridOldID = intGridID;
		intGridID = gridID.getGridID(fltTouchX, fltTouchY);

		// if the same Grid Coordinate is being touched; Save a little time in calculating
		if (intGridOldID == intGridID)
			return;

		if (intGridID >= 0) {
			// Clear the old Grid Highlight
			lstDrawHighlight.clear();

			// Add the sixteen grids surrounding the touch
			for (int i = 0; i < AppConfig.intHighlightSize * AppConfig.intHighlightSize; i++) {
				int intGridHighlightID = intGridID - ((AppConfig.intHighlightSize) / 2) - (((AppConfig.intHighlightSize) / 2) * AppConfig.intGridCountX);
				intGridHighlightID = intGridHighlightID + (i % AppConfig.intHighlightSize) + ((i / AppConfig.intHighlightSize) * AppConfig.intGridCountX);
				// Is intGridHighlightID on the Grid
				if (intGridHighlightID >= 0 && intGridHighlightID < AppConfig.intGridCountX * AppConfig.intGridCountY)
					// Is intGridHighlightID in the correct Row
					if ((int) (intGridHighlightID / AppConfig.intGridCountX) == (int) (intGridID / AppConfig.intGridCountX) + (int) (i / AppConfig.intHighlightSize) - ((AppConfig.intHighlightSize) / 2)) {
						DrawHighlight drawHighlight = new DrawHighlight(this, bmpGridHighlight);
						drawHighlight.setX((int) gridID.getGridX(intGridHighlightID));
						drawHighlight.setY((int) gridID.getGridY(intGridHighlightID));
						drawHighlight.setGridID(intGridHighlightID);
						lstDrawHighlight.add(drawHighlight);
					}
			}
		}
	}

	private void GenRandomBlock() {
		Random rnd = new Random();
		int intRnd = rnd.nextInt(7);
		new GenerateBlockShapes(this, lstDrawMovingBlocks, intRnd, fltLineSpacing);
	}

	private void GenBlockSet() {
		// Take all the Moving Blocks and transfer them into Set Blocks
		for (DrawBlocks drawBlocks : lstDrawMovingBlocks) {
			drawBlocks.setBlock(true);
			lstDrawSetBlocks.add(drawBlocks);
		}
		lstDrawMovingBlocks.clear();

		// Generate a new Block Set
		GenRandomBlock();
		booGenerateNewBlock = false;
	}

	public void setGenerateNewBlock(boolean booSet) {
		booGenerateNewBlock = booSet;
	}

	public boolean getGenerateNewBlock() {
		return booGenerateNewBlock;
	}

	public float[] getGridBounds() {
		float[] aryBounds = new float[] { fltLeftBound, fltUpperBound, fltRightBound, fltLowerBound };
		return aryBounds;
	}

	public Bitmap[] getBlocks() {
		Bitmap[] aryBitmaps = new Bitmap[] { bmpBlockGreen, bmpBlockYellow, bmpBlockPurple, bmpBlockWhite, bmpBlockRed, bmpBlockCyan, bmpBlockBlue };
		return aryBitmaps;
	}

	public GridID getGridID() {
		return gridID;
	}

	public void setGameOver() {
		booGameOver = true;
	}

	private void CreateStartGridBlocks() {
		int i = lstDrawSetBlocks.size();

		if (!AppConfig.booGenerateBlocks) {
			GenerateStartGridBlocks(63);
			GenerateStartGridBlocks(73);
			GenerateStartGridBlocks(74);
			GenerateStartGridBlocks(77);
			GenerateStartGridBlocks(78);
			GenerateStartGridBlocks(79);
			GenerateStartGridBlocks(80);
			GenerateStartGridBlocks(81);
			GenerateStartGridBlocks(82);
			GenerateStartGridBlocks(83);
			GenerateStartGridBlocks(86);
			GenerateStartGridBlocks(87);
			GenerateStartGridBlocks(88);
			GenerateStartGridBlocks(92);
			GenerateStartGridBlocks(93);
			// GenerateStartGridBlocks(94);
			GenerateStartGridBlocks(95);
			GenerateStartGridBlocks(96);
			GenerateStartGridBlocks(97);
			GenerateStartGridBlocks(107);

			booGenerateNewBlock = false;
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		try {
			// canvas.drawColor(0x44FFFFFF); // OR //canvas.drawColor(Color.BLACK);
			canvas.drawColor(0xFFFFFFFF); // white
			// Draw Black Bars to LetterBox Scaling
			pntLetterBox.setColor(0x44000000); // OR //Color.rgb(0,0,0);
			pntLetterBox.setStrokeWidth(0);
			// Top and Bottom Bars
			if (intLetterBoxHeight != 0) {
				canvas.drawRect(0, 0, this.getWidth(), intLetterBoxHeight, pntLetterBox);
				canvas.drawRect(0, this.getHeight() - intLetterBoxHeight, this.getWidth(), this.getHeight(), pntLetterBox);
			}
			// Left and Right Bars
			else if (intLetterBoxWidth != 0) {
				canvas.drawRect(0, 0, intLetterBoxWidth, this.getHeight(), pntLetterBox);
				canvas.drawRect(this.getWidth() - intLetterBoxWidth, 0, this.getWidth(), this.getHeight(), pntLetterBox);
			}

			// Create GameBoard
			DrawGameBoard(canvas);

			// Draw the Bitmaps Background
			for (DrawGrid BitmapBackground : lstBitmapBackground) {
				BitmapBackground.onDraw(canvas);
			}

			if (!AppConfig.booGameOver)
				booGameOver = false;

			if (AppConfig.booGenerateBlocks)
				if (!booGameOver)
					// Generate a new Block Set
					if (booGenerateNewBlock)
						GenBlockSet();

			// Calculate the Moving Blocks
			for (DrawBlocks calcBlocks : lstDrawMovingBlocks) {
				calcBlocks.CalcBlockSet();
			}

			// Sort the Set Blocks
			// Collections.sort(lstDrawSetBlocks, DrawBlocks.GridIDComparatorDesc);

			// Define each block tower and see if they are supported
			if (lstDrawSetBlocks.size() != 0) {
				CompileStructure compileStructure = new CompileStructure(lstDrawSetBlocks);
				HashMap<Integer, Integer> mapMaxBlocks = compileStructure.getMaxBlocks();
				ArrayList<DefineStructure> lstStructure = compileStructure.getStructure();

				// Got to sort by desc ClassID because the remove will knock it out of order
				Collections.sort(lstStructure, DefineStructure.ClassIDComparatorAsc);

				for (int i = lstStructure.size() - 1; i >= 0; i--) {
					DefineStructure defineStructure = lstStructure.get(i);
					// Calculate if the block needs to fall
					if (mapMaxBlocks.get(defineStructure.getTower()) < (AppConfig.intGridCountY - 1) * AppConfig.intGridCountX) {
						// I need to include the DrawBlock ClassID into the Compile Structure and use it below
						gridID.setBlockID(lstDrawSetBlocks.get(defineStructure.getClassID()).getGridID(), false);
						lstDrawFallingBlocks.add(lstDrawSetBlocks.get(defineStructure.getClassID()));
						lstDrawSetBlocks.remove(defineStructure.getClassID());
					}
				}
			}

			// Sort the lstDrawFallingBlocks
			Collections.sort(lstDrawFallingBlocks, DrawBlocks.GridIDComparatorAsc);

			// The blocks above the removed blocks get set to Falling so they fall down
			for (int i = lstDrawFallingBlocks.size() - 1; i >= 0; i--) {
				DrawBlocks drawBlocks = lstDrawFallingBlocks.get(i);
				drawBlocks.setBlockFalling(true);
				drawBlocks.FindNextGridID();
				if (drawBlocks.getNextGridID() == -1 || gridID.getBlockID(drawBlocks.getNextGridID())) {
					drawBlocks.setBlockFalling(false);
					gridID.setBlockID(drawBlocks.getGridID(), true);
					lstDrawSetBlocks.add(lstDrawFallingBlocks.get(i));
					lstDrawFallingBlocks.remove(i);
				} else {
					drawBlocks.MoveBlockDown();
					drawBlocks.onDraw(canvas);
				}
			}

			// Draw the Set Blocks
			for (int i = lstDrawSetBlocks.size() - 1; i >= 0; i--) {
				DrawBlocks drawBlocks = lstDrawSetBlocks.get(i);
				// Remove the blocks in the Highlight on onTouch ACTION_UP  // Do not Remove moving or falling blocks
				if (lstRemoveBlocks.contains(drawBlocks.getGridID())) {
					gridID.setBlockID(drawBlocks.getGridID(), false);
					lstDrawSetBlocks.remove(i);
				} else
					// Draw the blocks
					drawBlocks.onDraw(canvas);
			}

			// Clear the Highlight list
			lstRemoveBlocks.clear();

			// Draw the Moving Blocks
			for (DrawBlocks drawBlocks : lstDrawMovingBlocks) {
				drawBlocks.onDraw(canvas);
			}

			// Draw the Grid Highlighting
			for (DrawHighlight drawHighlight : lstDrawHighlight) {
				drawHighlight.onDraw(canvas);
			}

			if (booGameOver) {
				// Draw the GameOver screen
				canvas.drawColor(0x44FFFFFF);
				String strPaintText = "Game Over";
				canvas.drawText(strPaintText, this.getWidth() / 2, this.getHeight() / 2, paintClass.getPaintText(0xFFFFFFFF, (float) (AppConfig.fltTextSize * dblScaleRatio), true, Align.CENTER, Typeface.MONOSPACE));
			} else if (GameLoopThread.mPaused) {
				// Draw the Paused Screen
				canvas.drawColor(0x44FFFFFF);
				String strPaintText = "PAUSED";
				canvas.drawText(strPaintText, this.getWidth() / 2, this.getHeight() / 2, paintClass.getPaintText(0xFFFFFFFF, (float) (AppConfig.fltTextSize * dblScaleRatio), true, Align.CENTER, Typeface.MONOSPACE));
			}

			// Draw Background Galaxy Image
			if (AppConfig.booDrawGalaxyBackground)
				canvas.drawBitmap(bmpBackgroundImage, 0, 0, null);

			try {
			} catch (Exception ex) {
			}
		} catch (Exception e) {
			try {
				throw new Exception(e.getMessage());
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		} finally {
			if (AppConfig.intFrameCounter % AppConfig.intFrameCounterFrequency == 0)
				AppConfig.intFrameCounter = 0;
			AppConfig.intFrameCounter++;
		}
	}
}
