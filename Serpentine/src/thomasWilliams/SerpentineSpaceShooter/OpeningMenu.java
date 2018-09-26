package thomasWilliams.SerpentineSpaceShooter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

public class OpeningMenu extends SurfaceView {

	private boolean booGameDrawRaw = false;
	private boolean booDrawGalaxyBackground = true;
	private SurfaceHolder holder;
	private OpeningMenuLoopThread openingMenuLoopThread;
	private SpriteDraw SpriteDraw;
	private DirectionalPad DirectionalPad;;
	private DirectionalCursor DirectionalCursor;
	private Bitmap bmpIcon;
	private Bitmap bmpDirectionalPad;
	private long lngLastClick;
	private List<SpriteDraw> lstBmpSprite = new ArrayList<SpriteDraw>();
	private List<AfterCollisionWithSprite> lstBmpCollision = new ArrayList<AfterCollisionWithSprite>();
	private List<BackdropDraw> lstBackdropStars = new ArrayList<BackdropDraw>();
	private List<MissileDraw> lstMissiles = new ArrayList<MissileDraw>();
	private ArrayList<MissileDraw> lstMissilesRemove = new ArrayList<MissileDraw>();
	private List<EnemiesDraw> lstEnemies = new ArrayList<EnemiesDraw>();
	private List<EnemyExplosion> lstEnemyExplosion = new ArrayList<EnemyExplosion>();
	private ArrayList<EnemyExplosion> lstEnemyExplosionRemove = new ArrayList<EnemyExplosion>();
	private Bitmap bmpDirectionalCursor;
	private long lngOnDownClick;
	private boolean booCursorDrag;
	private static final int INVALID_POINTER_ID = -1;
	private int mActivePointerId = INVALID_POINTER_ID;
	private float mLastTouchX;
	private float mLastTouchY;
	private float intDirectionalCursorX;
	private float intDirectionalCursorY;
	private float intButtonCursorX;
	private float intButtonCursorY;
	private boolean booButtonPress;
	private String strPaintText;
	private float dblCordinateCursorX;
	private float dblCordinateCursorY;
	private double dblArcCordinate;
	private int dblRealCordinateCursorX;
	private int dblRealCordinateCursorY;
	private Bitmap bmpMainSprite;
	private MainSprite MainSprite;
	private double dblCursorAngle;
	private boolean booSpriteMove;
	private Paint pntLetterBox = new Paint();
	private boolean booPostBack = false;
	private Bitmap bmpBackground;
	private Bitmap bmpGameViewRaw;
	private Bitmap bmpGameViewScaled;
	private Bitmap bmpGameDraw;
	private Paint pntGameDrawBitmap = new Paint();
	private double dblBoundX = 0.3;
	private double dblBoundY = dblBoundX * dblScreenRatio;
	private int intScaleSizePad;
	private Bitmap bmpGameButton;
	private GameButton GameButton;
	private Bitmap bmpMainSpriteSwing;
	private Bitmap bitBackgroundImage;
	private Paint pntGameDrawBackdrop = new Paint();
	private Paint pntBackdropStars = new Paint();
	private int intBitmapRadius;
	private Bitmap bitBackdropStars;
	private long lastClick;
	private int intPulsarFrame = 0;
	private int intCreateMissileFrame = 0;
	private int intMissileFireFrequency = 6;
	private boolean booCreatePulsars = false;
	private int intPulsarY01;
	private int intPulsarY02;
	private int intPulsarY03;
	private int intPulsarY04;
	private int intPulsarY05;
	private int intPulsarY06;
	private int intPulsarX01;
	private int intPulsarX02;
	private int intPulsarX03;
	private int intPulsarX04;
	private int intPulsarX05;
	private int intPulsarX06;
	public int intMissileRadius = 10; // raw radius; its gets scaled later
	private Bitmap bitExplosion;
	public static int intStarsCreated = 6;
	public static int intLetterBoxWidth;
	public static int intLetterBoxHeight;
	public static int intAspectWidth = 480;
	public static int intAspectHeight = 800;
	public static int intGameDrawWidth;
	public static int intGameDrawHeight;
	public static double dblScreenRatio = (double) intAspectWidth / (double) intAspectHeight;
	public static double dblScaleRatio;

	public OpeningMenu(Context context) {
		super(context);
		openingMenuLoopThread = new OpeningMenuLoopThread(this);
		holder = getHolder();
		holder.addCallback(new Callback() {

			public void surfaceDestroyed(SurfaceHolder holder) {
			}

			public void surfaceCreated(SurfaceHolder holder) {
				CreateLetterBox();
				CreateBackgroundImage();

				openingMenuLoopThread.setRunning(true);
				// Starts the OpeningMenuLoopThread run() method
				openingMenuLoopThread.start();
			}

			public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
			}
		});

		// lstBmpSprite = BitmapFactory.decodeResource(getResources(),
		// R.drawable.sprite_set_01);
		// SpriteDraw = new SpriteDraw(this, lstBmpSprite);
		bmpIcon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
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

		if (dblUserScreenRatio < dblScreenRatio) {
			intLetterBoxWidth = 0;
			intLetterBoxHeight = (int) ((dblHeight - (dblWidth / dblScreenRatio)) / 2);
		} else if (dblUserScreenRatio > dblScreenRatio) {
			intLetterBoxWidth = (int) ((dblWidth - (dblHeight * dblScreenRatio)) / 2);
			intLetterBoxHeight = 0;
		}

		// Create a 480x800 bitmap and then scale it
		Bitmap bmpBackgroundRaw = Bitmap.createBitmap(intAspectWidth, intAspectHeight, Config.RGB_565); // Config.ARGB_8888 us for transparency
		// Bitmap bmpBackgroundRaw = BitmapFactory.decodeResource(getResources(), R.drawable.background_480x800);
		int imgWidth = bmpBackgroundRaw.getWidth();
		int imgHeight = bmpBackgroundRaw.getHeight();
		int intScaleSizePad = 0;

		// Scale Bitmap to Raw Size
		intScaleSizePad = intAspectHeight;
		float scaleFactorPad = Math.min(((float) intScaleSizePad) / imgWidth, ((float) intScaleSizePad) / imgHeight);
		Matrix scalePad = new Matrix();
		scalePad.postScale(scaleFactorPad, scaleFactorPad);
		bmpGameViewRaw = Bitmap.createBitmap(bmpBackgroundRaw, 0, 0, imgWidth, imgHeight, scalePad, false);

		// Scale to phone size but keep aspect ratio
		if (intAspectWidth > intAspectHeight) {
			intScaleSizePad = this.getWidth() - (intLetterBoxWidth * 2);
		} else {
			intScaleSizePad = this.getHeight() - (intLetterBoxHeight * 2);
		}
		// intScaleSizePad = 540; // width in 480x400
		scaleFactorPad = Math.min(((float) intScaleSizePad) / imgWidth, ((float) intScaleSizePad) / imgHeight);
		scalePad = new Matrix();
		scalePad.postScale(scaleFactorPad, scaleFactorPad);
		bmpGameViewScaled = Bitmap.createBitmap(bmpBackgroundRaw, 0, 0, imgWidth, imgHeight, scalePad, false);

		// /////////////////////////////////////////////////////////////////////////
		// What bmpGameDraw equals is very important as it determines the scaling //
		// /////////////////////////////////////////////////////////////////////////
		if (booGameDrawRaw)
			bmpGameDraw = bmpGameViewRaw;
		else
			bmpGameDraw = bmpGameViewScaled;
		dblScaleRatio = (double) bmpGameDraw.getHeight() / intAspectHeight;
		intGameDrawWidth = bmpGameDraw.getWidth();
		intGameDrawHeight = bmpGameDraw.getHeight();
	}

	private void CreateBackgroundImage() {
		// Create Bitmap
		Bitmap bitBackgroundImageRaw = BitmapFactory.decodeResource(getResources(), R.drawable.galaxy_480x800);
		// Scale Bitmap to the screen size not the GameView Size
		int imgWidth = bitBackgroundImageRaw.getWidth();
		int imgHeight = bitBackgroundImageRaw.getHeight();
		int intScaleSizePad = 0;

		// determine optimal ratio for phone
		double dblOptimalRatio = imgWidth / imgHeight;

		// Scale Bitmap to Phone size and Phone Ratio not GameView Size and Ratio
		// Scale to phone size but keep aspect ratio
		if (intAspectWidth > intAspectHeight) {
			intScaleSizePad = this.getWidth() + 1; // - (intLetterBoxWidth * 2));
		} else {
			intScaleSizePad = this.getHeight() + 1; // - (intLetterBoxHeight * 2));
		}
		// intScaleSizePad = 540; // width in 480x400
		float scaleFactorPad = Math.min(((float) intScaleSizePad) / imgWidth, ((float) intScaleSizePad) / imgHeight);
		Matrix scalePad = new Matrix();
		scalePad.postScale(scaleFactorPad, scaleFactorPad);
		bitBackgroundImage = Bitmap.createBitmap(bitBackgroundImageRaw, 0, 0, imgWidth, imgHeight, scalePad, false);

	}





	public void CreateGameBitmap(Canvas canvas) {
		// Create the GameDraw Background to hold the View
		// canvas.drawBitmap(bmpGameDraw, intLetterBoxWidth, intLetterBoxHeight, null);
		pntGameDrawBitmap.setColor(0x440000FF);
		// pntGameDrawBitmap.setColor(0xFF000000);
		canvas.drawRect(0 + intLetterBoxWidth, intLetterBoxHeight, bmpGameDraw.getWidth() + intLetterBoxWidth, bmpGameDraw.getHeight() + intLetterBoxHeight, pntGameDrawBitmap);

		// Create the middle Line
		pntGameDrawBitmap.setColor(0x44000000);
		canvas.drawRect(0 + intLetterBoxWidth, ((bmpGameDraw.getHeight()) / 2) + intLetterBoxHeight - 3, bmpGameDraw.getWidth() + intLetterBoxWidth, ((bmpGameDraw.getHeight() / 2) + intLetterBoxHeight) + 3, pntGameDrawBitmap);

		// Create the corners
		pntGameDrawBitmap.setStrokeWidth(5);
		canvas.drawLine(0 + intLetterBoxWidth, (float) ((bmpGameDraw.getHeight() * (1 - dblBoundY)) + intLetterBoxHeight), (float) ((bmpGameDraw.getWidth() * dblBoundX) + intLetterBoxWidth), bmpGameDraw.getHeight() + intLetterBoxHeight, pntGameDrawBitmap);
		canvas.drawLine(0 + intLetterBoxWidth, (float) ((bmpGameDraw.getHeight() * dblBoundY) + intLetterBoxHeight), (float) ((bmpGameDraw.getWidth() * dblBoundX) + intLetterBoxWidth), 0 + intLetterBoxHeight, pntGameDrawBitmap);
		canvas.drawLine((float) ((bmpGameDraw.getWidth() * (1 - dblBoundX)) + intLetterBoxWidth), 0 + intLetterBoxHeight, (float) (bmpGameDraw.getWidth() + intLetterBoxWidth), (float) ((bmpGameDraw.getHeight() * dblBoundY) + intLetterBoxHeight), pntGameDrawBitmap);
		canvas.drawLine((float) (bmpGameDraw.getWidth() + intLetterBoxWidth), (float) ((bmpGameDraw.getHeight() * (1 - dblBoundY)) + intLetterBoxHeight), (float) ((bmpGameDraw.getWidth() * (1 - dblBoundX)) + intLetterBoxWidth), bmpGameDraw.getHeight() + intLetterBoxHeight, pntGameDrawBitmap);
	}

	private void CreateStarBackdrop(Canvas canvas) {
		// Creates the pulsars
		pntGameDrawBackdrop.setColor(0xFFFFFFFF);
		Random rnd = new Random(System.currentTimeMillis());
		int intPulsarX;
		int intPulsarY;

		if (!booCreatePulsars) {
			intPulsarY01 = getintPulsarY(0.1);
			intPulsarX01 = getintPulsarX(0.1);
			canvas.drawCircle(intPulsarX01, intPulsarY01, rnd.nextInt(6) + 3, pntGameDrawBackdrop);
			intPulsarX02 = getintPulsarX(0.8);
			intPulsarY02 = getintPulsarY(0.2);
			canvas.drawCircle(intPulsarX02, intPulsarY02, rnd.nextInt(6) + 3, pntGameDrawBackdrop);
			intPulsarX03 = getintPulsarX(0.5);
			intPulsarY03 = getintPulsarY(0.4);
			canvas.drawCircle(intPulsarX03, intPulsarY03, rnd.nextInt(6) + 3, pntGameDrawBackdrop);
			intPulsarX04 = getintPulsarX(0.1);
			intPulsarY04 = getintPulsarY(0.6);
			canvas.drawCircle(intPulsarX04, intPulsarY04, rnd.nextInt(6) + 3, pntGameDrawBackdrop);
			intPulsarX05 = getintPulsarX(0.9);
			intPulsarY05 = getintPulsarY(0.7);
			canvas.drawCircle(intPulsarX05, intPulsarY05, rnd.nextInt(6) + 3, pntGameDrawBackdrop);
			intPulsarX06 = getintPulsarX(0.2);
			intPulsarY06 = getintPulsarY(0.3);
			canvas.drawCircle(intPulsarX06, intPulsarY06, rnd.nextInt(6) + 3, pntGameDrawBackdrop);

			booCreatePulsars = true;
		}

		canvas.drawCircle(intPulsarX01, intPulsarY01, rnd.nextInt(6) + 3, pntGameDrawBackdrop);
		canvas.drawCircle(intPulsarX02, intPulsarY02, rnd.nextInt(6) + 3, pntGameDrawBackdrop);
		canvas.drawCircle(intPulsarX03, intPulsarY03, rnd.nextInt(6) + 3, pntGameDrawBackdrop);
		canvas.drawCircle(intPulsarX04, intPulsarY04, rnd.nextInt(6) + 3, pntGameDrawBackdrop);
		canvas.drawCircle(intPulsarX05, intPulsarY05, rnd.nextInt(6) + 3, pntGameDrawBackdrop);
		canvas.drawCircle(intPulsarX06, intPulsarY06, rnd.nextInt(6) + 3, pntGameDrawBackdrop);

		intPulsarFrame++;
		intPulsarY01 = getPulsarY(intPulsarY01);
		intPulsarY02 = getPulsarY(intPulsarY02);
		intPulsarY03 = getPulsarY(intPulsarY03);
		intPulsarY04 = getPulsarY(intPulsarY04);
		intPulsarY05 = getPulsarY(intPulsarY05);
		intPulsarY06 = getPulsarY(intPulsarY06);
	}

	private int getPulsarY(int intPulsarY) {
		if (intPulsarFrame % 3 == 0) {
			if (intPulsarY++ > this.getHeight())
				return 0;
			else
				return intPulsarY++;
		} else
			return intPulsarY;
	}

	private int getintPulsarX(double dblCoefficient) {
		return (int) (this.getWidth() * dblCoefficient);
	}

	private int getintPulsarY(double dblCoefficient) {
		return (int) (this.getHeight() * dblCoefficient);
	}




	private void HandlePointerOrder(MotionEvent TouchEvent) {

		// The Pointer Index has must be reset correctly
		// if (TouchEvent.getPointerCount() != DirectionalCursor.getPointerCount() || TouchEvent.getPointerCount() == 1)
		setAllPointerIndexes(TouchEvent);

		// The GameButton has been pressed
		if (booButtonPress) {
			// if the radius is smaller then radius of the GameButton is then it must inside the circle Make it 1 for larger hit area
			int intButtonPointerIndex = DirectionalCursor.getButtonIndex();
			intButtonCursorX = TouchEvent.getX(intButtonPointerIndex);
			intButtonCursorY = TouchEvent.getY(intButtonPointerIndex);
			int intGameButtonX = GameButton.getGameButtonX() + (GameButton.width / 2);
			int intGameButtonY = GameButton.getGameButtonY() + (GameButton.height / 2);
			double dblRadCirclePointer00 = Math.sqrt((double) (((intGameButtonX - intButtonCursorX) * (intGameButtonX - intButtonCursorX)) + (intGameButtonY - intButtonCursorY) * (intGameButtonY - intButtonCursorY)));
			if (dblRadCirclePointer00 < (GameButton.width / 1)) {
				dblArcCordinate = 4; // Set MainSprite to look up *OPTIONAL LINE*
				GameButton.setButtonDown();
				// lstBmpCollision.add(new AfterCollisionWithSprite(lstBmpCollision, this, intButtonCursorX, intButtonCursorY, bmpIcon));
			}
		}
		DirectionalCursor.setPointerCount(TouchEvent.getPointerCount());
	}

	private void resetAllPointerIndexes(MotionEvent TouchEvent) {
		// gives me the number before release 1=no pointer down and 2 = 1 pointer down
		if (TouchEvent.getPointerCount() == 1) {// && DirectionalCursor.getPointerIndex() == 0) {
			DirectionalCursor.setPointerIndex(-1);
			DirectionalCursor.setButtonIndex(-1);
			booCursorDrag = false;
			booButtonPress = false;
		} else if (TouchEvent.getPointerCount() > 1) {
			// Need some way to identity which Index has lifted
			// Means the Cursor at PointerIndex 0 has been lifted
			if (TouchEvent.getActionIndex() == 0 && DirectionalCursor.getPointerIndex() == 0) {
				cursorLiftButtonDown();
			}
			// Means the Cursor at PointerIndex 1 has been lifted
			else if (TouchEvent.getActionIndex() == 1 && DirectionalCursor.getPointerIndex() == 1) {
				cursorLiftButtonDown();
			}
			// Means the Button at PointerIndex 0 has been lifted
			else if (TouchEvent.getActionIndex() == 0 && DirectionalCursor.getButtonIndex() == 0) {
				buttonLiftCursorDown();
			}
			// Means the Button at PointerIndex 1 has been lifted
			else if (TouchEvent.getActionIndex() == 1 && DirectionalCursor.getButtonIndex() == 1) {
				buttonLiftCursorDown();
			} else {
				int intTemp = TouchEvent.getActionIndex();
			}
		}
		if (!booCursorDrag)
			DirectionalCursor.setCursorReturn(true);
		if (!booButtonPress)
			GameButton.setButtonUp();
	}

	private void cursorLiftButtonDown() {
		DirectionalCursor.setPointerIndex(-1);
		DirectionalCursor.setButtonIndex(0);
		booCursorDrag = false;
		booButtonPress = true;
	}

	private void buttonLiftCursorDown() {
		DirectionalCursor.setButtonIndex(-1);
		DirectionalCursor.setPointerIndex(0);
		booButtonPress = false;
		booCursorDrag = true;
	}

	private void setAllPointerIndexes(MotionEvent TouchEvent) {
		// if (DirectionalCursor.getPointerIndex() >= 1 && booCursorDrag == true)
		// DirectionalCursor.setPointerIndex(0);

		int intCursorX = DirectionalCursor.getIntCursorX() + (DirectionalCursor.width / 2);
		int intCursorY = DirectionalCursor.getIntCursorY() + (DirectionalCursor.height / 2);
		final float intPointerIndexX_00 = TouchEvent.getX(0);
		final float intPointerIndexY_00 = TouchEvent.getY(0);
		final float intPointerIndexX_01 = TouchEvent.getX(1);
		final float intPointerIndexY_01 = TouchEvent.getY(1);

		// Break it down for each Pointer touch on screen
		if (TouchEvent.getPointerCount() == 1) {
			double dblRadCirclePointer00 = Math.sqrt((double) (((intCursorX - intPointerIndexX_00) * (intCursorX - intPointerIndexX_00)) + (intCursorY - intPointerIndexY_00) * (intCursorY - intPointerIndexY_00)));
			// if the radius is smaller then radius of the DirectionalPad is then it must inside the circle Make it 1 for larger hit area
			if (dblRadCirclePointer00 < (DirectionalPad.width / 1)) {
				DirectionalCursor.setPointerIndex(0);
				DirectionalCursor.setButtonIndex(-1);
				booCursorDrag = true;
				booButtonPress = false;
			} else {
				DirectionalCursor.setPointerIndex(-1);
				DirectionalCursor.setButtonIndex(0);
				booCursorDrag = false;
				booButtonPress = true;
			}
		} else if (TouchEvent.getPointerCount() > 1) {
			double dblRadCirclePointer00 = Math.sqrt((double) (((intCursorX - intPointerIndexX_00) * (intCursorX - intPointerIndexX_00)) + (intCursorY - intPointerIndexY_00) * (intCursorY - intPointerIndexY_00)));
			double dblRadCirclePointer01 = Math.sqrt((double) (((intCursorX - intPointerIndexX_01) * (intCursorX - intPointerIndexX_01)) + (intCursorY - intPointerIndexY_01) * (intCursorY - intPointerIndexY_01)));
			if (dblRadCirclePointer00 < (DirectionalPad.width / 1)) {
				DirectionalCursor.setPointerIndex(0);
				DirectionalCursor.setButtonIndex(1);
				booCursorDrag = true;
				booButtonPress = true;
			} else if (dblRadCirclePointer01 < (DirectionalPad.width / 1)) {
				DirectionalCursor.setPointerIndex(1);
				DirectionalCursor.setButtonIndex(0);
				if (booCursorDrag)
					booButtonPress = true;
				else
					booButtonPress = false;
				booCursorDrag = true;
			} else {
				if (booCursorDrag) {
					DirectionalCursor.setPointerIndex(0);
					DirectionalCursor.setButtonIndex(1);
					booCursorDrag = true;
					booButtonPress = true;
				} else {
					DirectionalCursor.setPointerIndex(-1);
					DirectionalCursor.setButtonIndex(0);
					booCursorDrag = false;
					booButtonPress = true;
				}
			}
		}
	}

	public int getDirectionalPadX() {
		return DirectionalPad.getIntPadX();
	}

	public int getDirectionalPadY() {
		return DirectionalPad.getIntPadY();
	}

	public int getDirectionalWidth() {
		return DirectionalPad.width;
	}

	public int getDirectionalHeight() {
		return DirectionalPad.height;
	}

	public double getArcCordinate() {
		return dblArcCordinate;
	}

	public double getCursorAngle() {
		return dblCursorAngle;
	}

	public boolean getCursorDrag() {
		return booCursorDrag;
	}

	public boolean getSpriteMove() {
		return booSpriteMove;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// canvas.drawColor(0x44FFFFFF); // OR //canvas.drawColor(Color.BLACK);
		canvas.drawColor(0xFFFFFFFF); // white
		// Create Bitmap to render a scalable view
		CreateGameBitmap(canvas);
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
		else if (intCreateMissileFrame % intMissileFireFrequency != 0)
			intCreateMissileFrame++;

		// Draw Background Galaxy Image
		if (booDrawGalaxyBackground)
			canvas.drawBitmap(bitBackgroundImage, 0, 0, null);
		// Draw Pulsars and moving stars
		CreateStarBackdrop(canvas);
		for (BackdropDraw SpawnStars : lstBackdropStars) {
			SpawnStars.onDraw(canvas);
		}

		// Draw the DirectionalPad and Directional Cursor and Button
		DirectionalPad.onDraw(canvas);
		DirectionalCursor.onDraw(canvas);
		GameButton.onDraw(canvas);

		for (EnemiesDraw SpawnEnemies : lstEnemies) {
			SpawnEnemies.onDraw(canvas);
		}

		// Draw the EnemyHit Explosions - located above MissileDraw to help with the Sprite flashing
		// for (int i = 0; i < lstEnemyExplosion.size(); i++) {
		// lstEnemyExplosion.get(i).onDraw(canvas);
		// if (lstEnemyExplosion.get(i).getBooRemoveEnemyExplosion()) {
		// lstEnemyExplosionRemove.add(lstEnemyExplosion.get(i));
		// }
		// }

		for (EnemyExplosion SpawnExplosion : lstEnemyExplosion) {
			SpawnExplosion.onDraw(canvas);
			if (SpawnExplosion.getBooRemoveEnemyExplosion())
				// Set Explosion Bitmaps toRemove
				lstEnemyExplosionRemove.add(SpawnExplosion);
		}


		// Draw the MainSprite
		MainSprite.onDraw(canvas);

		// Remove the Explosions
		for (int i = lstEnemyExplosionRemove.size() - 1; i >= 0; i--) {
			lstEnemyExplosion.remove(lstEnemyExplosionRemove.get(i));
		}
		// Remove the Missiles
		for (int i = lstMissilesRemove.size() - 1; i >= 0; i--) {
			lstMissiles.remove(lstMissilesRemove.get(i));
		}
		if (OpeningMenuLoopThread.mPaused) {
			canvas.drawColor(0x44FFFFFF);

			Paint paint = new Paint();
			paint.setColor(0x88FFFFFF); // android.graphics.Color.BLACK
			paint.setTextSize((float) (30 * dblScaleRatio));
			paint.setFakeBoldText(true);
			paint.setTextAlign(Align.CENTER);
			strPaintText = "PAUSED";
			canvas.drawText(strPaintText, this.getWidth() / 2, this.getHeight() / 2, paint);
		}
	}

	public int getEnemiesRemaining() {
		return lstEnemies.size();
	}
	
}
// Put in Enemy paths
// I am not going to have levels with bosses. I am going to have checkpoints accessible by the menu button
// At a checkpoint refill health and show an Advertisement
// After the check point increase the rate of fire from the enemies

// Need to add missile firing from Enemy
// Need to program Enemy Patterns in Levels
// Need Scoring system and Life Meter