package thomasWilliams.SerpentineSpaceShooter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import android.R.bool;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.AudioManager;
import android.os.Vibrator;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

public class GameView extends SurfaceView {

	private SurfaceHolder holder;
	private GameLoopThread gameLoopThread;
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
	private List<EnemyMissileDraw> lstEnemyMissiles = new ArrayList<EnemyMissileDraw>();
	private ArrayList<EnemyMissileDraw> lstEnemyMissilesRemove = new ArrayList<EnemyMissileDraw>();
	private List<EnemiesDraw> lstEnemies = new ArrayList<EnemiesDraw>();
	private ArrayList<EnemiesDraw> lstEnemyRemove = new ArrayList<EnemiesDraw>();
	private List<EnemyExplosion> lstEnemyExplosion = new ArrayList<EnemyExplosion>();
	private ArrayList<EnemyExplosion> lstSpriteExplosion = new ArrayList<EnemyExplosion>();
	private ArrayList<EnemyExplosion> lstEnemyExplosionRemove = new ArrayList<EnemyExplosion>();
	private ArrayList<EnemyExplosion> lstSpriteExplosionRemove = new ArrayList<EnemyExplosion>();
	private ArrayList<EnemyPath> lstEnemyPath = new ArrayList<EnemyPath>();
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
	private int intScaleSizePad;
	private Bitmap bmpGameButton;
	private GameButton GameButton;
	private Bitmap bmpMainSpriteHit;
	private Bitmap bmpBackgroundImage;
	private Paint pntGameDrawBackdrop = new Paint();
	private Paint pntBackdropStars = new Paint();
	private int intBitmapRadius;
	private Bitmap bmpBackdropStars;
	private long lastClick;
	private int intPulsarFrame = 0;
	private int intCreateMissileFrame = 0;
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
	private Bitmap bmpExplosion;
	private SetLevelState setLevelState;
	private PausePaint pausePaint;
	private Paint pntStartLevel;
	private Paint pntGamePaused;
	private int intEndLevelDuration = 0;
	private int intHealth;
	private Bitmap bmpEnemyResource;
	private HealthBar healthBar;
	private int intEnemyHitCounter;
	private Bitmap bmpEnemyHitCounter;
	private Rect srcEnemyHitCounter;
	private Rect dstEnemyHitCounter;
	private int intEnemyHitCounterX;
	private int intEnemyHitCounterY;
	private boolean booMainSpriteDead = false;
	private Context applicationContext;
	private GameMenu gameMenu;
	private boolean booSoundOn;
	public static int intStarsCreated = 6;
	public static int intLetterBoxWidth;
	public static int intLetterBoxHeight;
	public static int intGameDrawWidth;
	public static int intGameDrawHeight;
	public static double dblScaleRatio;

	public GameView(GameMenu gameMenu, SetLevelState setLevelState) {
		super(gameMenu.getApplicationContext());
		this.setLevelState = setLevelState;
		this.applicationContext = gameMenu.getApplicationContext();
		this.gameMenu = gameMenu;
		this.booSoundOn = setLevelState.getSoundOn();

		gameLoopThread = new GameLoopThread(this);

		holder = getHolder();
		holder.addCallback(new Callback() {

			public void surfaceDestroyed(SurfaceHolder holder) {
			}

			public void surfaceCreated(SurfaceHolder holder) {
				CreateLetterBox();
				CreateBackgroundImage();
				CreateInitialBackdropStars();
				CreateDirectionalPad();
				CreateGameButton();
				CreateMainSprite();
				// CreateEnemies();
				CreateExtras();
				if (booSoundOn)
					CreateBackgroundMusic();

				if (!gameLoopThread.isAlive()) {
					gameLoopThread.setRunning(true);
					// Starts the GameLoopThread run() method
					gameLoopThread.start();
				}
			}

			public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
			}
		});

		// lstBmpSprite = BitmapFactory.decodeResource(getResources(),
		// R.drawable.sprite_set_01);
		// SpriteDraw = new SpriteDraw(this, lstBmpSprite);
		bmpIcon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
	}

	private void CreateBackgroundMusic() {
		PlayMediaPlayer backgroundMusic = new PlayMediaPlayer(applicationContext);
		// backgroundMusic.playMusicLoop(R.raw.background_menu);
		backgroundMusic.playMusicLoop(R.raw.background_gameplay01);
	}

	private void CreateExtras() {
		pausePaint = new PausePaint(0x88FFFFFF);

		// Create Health Bar
		healthBar = new HealthBar(this.getWidth() - (intLetterBoxWidth * 2));
		// Create Enemy Hit Counter
		intEnemyHitCounter = setLevelState.getEnemyHitCounter();

		Bitmap bmpImageRaw = BitmapFactory.decodeResource(getResources(), R.drawable.enemy_set_12);
		int imgWidth = bmpImageRaw.getWidth();
		int imgHeight = bmpImageRaw.getHeight();
		final int intBmpColumns = 3;
		final int intBmpRows = 4;

		int intScaleSize = (int) (AppConfig.HealthBarHeight * intBmpRows * dblScaleRatio);
		float scaleFactorPad = Math.min(((float) intScaleSize) / imgWidth, ((float) intScaleSize) / imgHeight);
		Matrix scalePad = new Matrix();
		scalePad.postScale(scaleFactorPad, scaleFactorPad);
		bmpEnemyHitCounter = Bitmap.createBitmap(bmpImageRaw, 0, 0, imgWidth, imgHeight, scalePad, false);
		bmpImageRaw.recycle();

		int width = bmpEnemyHitCounter.getWidth() / intBmpColumns;
		int height = bmpEnemyHitCounter.getHeight() / intBmpRows;
		intEnemyHitCounterX = (int) (this.getWidth() - intLetterBoxWidth - (90 * dblScaleRatio));
		intEnemyHitCounterY = (int) (0 + (10 * dblScaleRatio));
		srcEnemyHitCounter = new Rect(width, 0, width + width, 0 + height);
		dstEnemyHitCounter = new Rect(intEnemyHitCounterX, intEnemyHitCounterY, intEnemyHitCounterX + width, intEnemyHitCounterY + height);
	}

	private void CreateEnemies() {
		// Create Bitmap Explosion
		bmpExplosion = BitmapFactory.decodeResource(getResources(), R.drawable.explosion_set_02);
		// Create Bitmap Enemy
		bmpEnemyResource = BitmapFactory.decodeResource(getResources(), R.drawable.enemy_set_11);

		// Scale Bitmap to Raw Size
		int intScaleSizePad = (int) (bmpEnemyResource.getHeight() * dblScaleRatio);
		float scaleFactorPad = Math.min(((float) intScaleSizePad) / bmpEnemyResource.getWidth(), ((float) intScaleSizePad) / bmpEnemyResource.getHeight());
		Matrix scalePad = new Matrix();
		scalePad.postScale(scaleFactorPad, scaleFactorPad);
		// Scale to the Phone
		bmpEnemyResource = Bitmap.createBitmap(bmpEnemyResource, 0, 0, bmpEnemyResource.getWidth(), bmpEnemyResource.getHeight(), scalePad, false);
		bmpExplosion = Bitmap.createBitmap(bmpExplosion, 0, 0, bmpExplosion.getWidth(), bmpExplosion.getHeight(), scalePad, false);

		// Create Bitmap Enemies
		// 1-100
		// for (int i = 0; i < 100; i++) {
		for (int i = 0; i < 100; i++) {
			lstEnemies.add(new EnemiesDraw(this, bmpEnemyResource, lstEnemies.size(), MainSprite));
		}

		// Create the Random Variables for the StoredPaths
		EnemyPathGeneration enemyPathGeneration = new EnemyPathGeneration();

		// Create Enemy Paths
		for (EnemiesDraw SpawnEnemies : lstEnemies) {
			lstEnemyPath.add(new EnemyPath(SpawnEnemies.getEnemySpawned(), SpawnEnemies, lstEnemies.size(), setLevelState.getCurrentLevel(), enemyPathGeneration));
		}
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

		// // Scale to phone size but keep aspect ratio
		// if (dblUserScreenRatio < AppConfig.dblScreenRatio) {
		// intScaleSizePad = this.getHeight() - (intLetterBoxHeight * 2);
		// scaleFactorPad = ((float) intScaleSizePad) / imgHeight;
		// } else {
		// // intScaleSizePad = this.getWidth() - (intLetterBoxWidth * 2);
		// // scaleFactorPad = ((float) intScaleSizePad) / imgWidth;
		//
		// intScaleSizePad = this.getHeight() - (intLetterBoxHeight * 2);
		// scaleFactorPad = ((float) intScaleSizePad) / imgHeight;
		// }

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

	private void CreateBackgroundImage() {
		// Create Bitmap
		Bitmap bmpBackgroundImageRaw = BitmapFactory.decodeResource(getResources(), R.drawable.galaxy_480x800);
		// Scale Bitmap to the screen size not the GameView Size
		int imgWidth = bmpBackgroundImageRaw.getWidth();
		int imgHeight = bmpBackgroundImageRaw.getHeight();
		int intScaleSizePad = 0;

		// determine optimal ratio for phone
		double dblOptimalRatio = imgWidth / imgHeight;
		double dblUserScreenRatio = (double) this.getWidth() / (double) this.getHeight();

		// Scale Bitmap to Phone size and Phone Ratio not GameView Size and Ratio
		// Scale to phone size but keep aspect ratio
		float scaleFactorPad;
		if (dblUserScreenRatio < AppConfig.dblScreenRatio) {
			intScaleSizePad = this.getHeight() + 1; // - (intLetterBoxHeight * 2));
		} else {
			intScaleSizePad = (int) (((this.getWidth() / (double) imgWidth) * imgHeight)) + 1;
		}
		scaleFactorPad = ((float) intScaleSizePad) / imgHeight;
		// intScaleSizePad = 540; // width in 480x400

		Matrix scalePad = new Matrix();
		scalePad.postScale(scaleFactorPad, scaleFactorPad);
		bmpBackgroundImage = Bitmap.createBitmap(bmpBackgroundImageRaw, 0, 0, imgWidth, imgHeight, scalePad, false);
		bmpBackgroundImageRaw.recycle();
	}

	private void CreateDirectionalPad() {
		Bitmap bmpDirectionalPadPreScale = BitmapFactory.decodeResource(getResources(), R.drawable.directional_pad_96x96);
		int imgWidth = bmpDirectionalPadPreScale.getWidth();
		int imgHeight = bmpDirectionalPadPreScale.getHeight();
		double a = bmpGameDraw.getWidth() * AppConfig.dblBoundX;
		double b = bmpGameDraw.getHeight() * AppConfig.dblBoundY;
		double c = Math.sqrt((a * a) + (b * b));
		double s = (a + b + c) / 2;
		double k = Math.sqrt(s * (s - a) * (s - b) * (s - c));
		intScaleSizePad = (int) (2 * ((2 * k) / (s * 2))); // The radius of a circle in the left triangle

		// Constrain to given size but keep aspect ratio
		float scaleFactorPad = Math.min(((float) intScaleSizePad) / imgWidth, ((float) intScaleSizePad) / imgHeight);

		Matrix scalePad = new Matrix();
		scalePad.postScale(scaleFactorPad, scaleFactorPad);
		bmpDirectionalPad = Bitmap.createBitmap(bmpDirectionalPadPreScale, 0, 0, imgWidth, imgHeight, scalePad, false);
		// bmpDirectionalPadPreScale.recycle();

		int intScaleSizeCursor = (int) (intScaleSizePad * 0.4);
		float scaleFactorCursor = Math.min(((float) intScaleSizeCursor) / imgWidth, ((float) intScaleSizeCursor) / imgHeight);
		Matrix scaleCursor = new Matrix();
		scaleCursor.postScale(scaleFactorCursor, scaleFactorCursor);
		bmpDirectionalCursor = Bitmap.createBitmap(bmpDirectionalPadPreScale, 0, 0, imgWidth, imgHeight, scaleCursor, false);
		// bmpDirectionalCursor = BitmapFactory.decodeResource(getResources(),
		// R.drawable.directional_pad_24x24);
		bmpDirectionalPadPreScale.recycle();

		DirectionalPad = new DirectionalPad(this, bmpDirectionalPad);
		DirectionalCursor = new DirectionalCursor(this, bmpDirectionalCursor);
	}

	private void CreateGameButton() {
		Bitmap bmpGameButtonPreScale = BitmapFactory.decodeResource(getResources(), R.drawable.game_button_96x96);
		int imgWidth = bmpGameButtonPreScale.getWidth();
		int imgHeight = bmpGameButtonPreScale.getHeight();

		// Use the same size as the Directional Pad
		// It scales by Width so I multiply by 2 to compensate for the rectangular image
		int intScaleSizeGameButton = intScaleSizePad * 2;

		// Constrain to given size but keep aspect ratio
		float scaleFactorPad = Math.min(((float) intScaleSizeGameButton) / (imgWidth), ((float) intScaleSizeGameButton) / imgHeight);

		Matrix scalePad = new Matrix();
		scalePad.postScale(scaleFactorPad, scaleFactorPad);
		bmpGameButton = Bitmap.createBitmap(bmpGameButtonPreScale, 0, 0, imgWidth, imgHeight, scalePad, false);
		bmpGameButtonPreScale.recycle();

		GameButton = new GameButton(this, bmpGameButton);
	}

	private void CreateMainSprite() {
		bmpMainSprite = BitmapFactory.decodeResource(getResources(), R.drawable.sprite_set_17);
		bmpMainSpriteHit = BitmapFactory.decodeResource(getResources(), R.drawable.sprite_set_15);
		// Scale Bitmap to Raw Size
		int intScaleSizePad = (int) (bmpMainSprite.getHeight() * dblScaleRatio);
		// intScaleSizePad=(int) (intScaleSizePad*1.5);
		float scaleFactorPad = Math.min(((float) intScaleSizePad) / bmpMainSprite.getWidth(), ((float) intScaleSizePad) / bmpMainSprite.getHeight());
		Matrix scalePad = new Matrix();
		scalePad.postScale(scaleFactorPad, scaleFactorPad);
		// Scale to the Phone
		bmpMainSprite = Bitmap.createBitmap(bmpMainSprite, 0, 0, bmpMainSprite.getWidth(), bmpMainSprite.getHeight(), scalePad, false);
		bmpMainSpriteHit = Bitmap.createBitmap(bmpMainSpriteHit, 0, 0, bmpMainSpriteHit.getWidth(), bmpMainSpriteHit.getHeight(), scalePad, false);

		setMainSprite();
	}

	private void setMainSprite() {
		intHealth = 100;
		MainSprite = new MainSprite(this, bmpMainSprite, bmpMainSpriteHit);
	}

	private void CreateInitialBackdropStars() {
		if (true)
			return;
		for (int i = 1; i <= intStarsCreated; i++) {
			// I dont know why but I need a getResouces() code to get this loop to work
			Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.directional_pad_96x96);

			Config cfgBackdropBitmap = Bitmap.Config.ARGB_8888;
			Random rndBitmapRadius = new Random();
			intBitmapRadius = rndBitmapRadius.nextInt(6) + 6;
			bmpBackdropStars = Bitmap.createBitmap(intBitmapRadius, intBitmapRadius, cfgBackdropBitmap);

			// This is causing a small memory leak so turn it off for now
			// lstBackdropStars.add(new BackdropDraw(this, bmpBackdropStars, intBitmapRadius));
		}
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
		canvas.drawLine(0 + intLetterBoxWidth, (float) ((bmpGameDraw.getHeight() * (1 - AppConfig.dblBoundY)) + intLetterBoxHeight), (float) ((bmpGameDraw.getWidth() * AppConfig.dblBoundX) + intLetterBoxWidth), bmpGameDraw.getHeight() + intLetterBoxHeight, pntGameDrawBitmap);
		canvas.drawLine(0 + intLetterBoxWidth, (float) ((bmpGameDraw.getHeight() * AppConfig.dblBoundY) + intLetterBoxHeight), (float) ((bmpGameDraw.getWidth() * AppConfig.dblBoundX) + intLetterBoxWidth), 0 + intLetterBoxHeight, pntGameDrawBitmap);
		canvas.drawLine((float) ((bmpGameDraw.getWidth() * (1 - AppConfig.dblBoundX)) + intLetterBoxWidth), 0 + intLetterBoxHeight, (float) (bmpGameDraw.getWidth() + intLetterBoxWidth), (float) ((bmpGameDraw.getHeight() * AppConfig.dblBoundY) + intLetterBoxHeight), pntGameDrawBitmap);
		canvas.drawLine((float) (bmpGameDraw.getWidth() + intLetterBoxWidth), (float) ((bmpGameDraw.getHeight() * (1 - AppConfig.dblBoundY)) + intLetterBoxHeight), (float) ((bmpGameDraw.getWidth() * (1 - AppConfig.dblBoundX)) + intLetterBoxWidth), bmpGameDraw.getHeight() + intLetterBoxHeight, pntGameDrawBitmap);
	}

	private void CreateStarBackdrop(Canvas canvas) {
		if (!AppConfig.booDrawPulsars)
			return;
		
		// Creates the pulsars
		pntGameDrawBackdrop.setColor(0xFFFFFFFF);
		Random rnd = new Random(System.currentTimeMillis());
		int intPulsarX;
		int intPulsarY;

		if (!booCreatePulsars) {
			intPulsarY01 = getintPulsarY(0.1);
			intPulsarX01 = getintPulsarX(0.1);
			canvas.drawCircle(intPulsarX01, intPulsarY01, (float) (((float) ((rnd.nextInt(6) + 3) * dblScaleRatio)) * dblScaleRatio), pntGameDrawBackdrop);
			intPulsarX02 = getintPulsarX(0.8);
			intPulsarY02 = getintPulsarY(0.2);
			canvas.drawCircle(intPulsarX02, intPulsarY02, (float) ((rnd.nextInt(6) + 3) * dblScaleRatio), pntGameDrawBackdrop);
			intPulsarX03 = getintPulsarX(0.5);
			intPulsarY03 = getintPulsarY(0.4);
			canvas.drawCircle(intPulsarX03, intPulsarY03, (float) ((rnd.nextInt(6) + 3) * dblScaleRatio), pntGameDrawBackdrop);
			intPulsarX04 = getintPulsarX(0.1);
			intPulsarY04 = getintPulsarY(0.6);
			canvas.drawCircle(intPulsarX04, intPulsarY04, (float) ((rnd.nextInt(6) + 3) * dblScaleRatio), pntGameDrawBackdrop);
			intPulsarX05 = getintPulsarX(0.9);
			intPulsarY05 = getintPulsarY(0.7);
			canvas.drawCircle(intPulsarX05, intPulsarY05, (float) ((rnd.nextInt(6) + 3) * dblScaleRatio), pntGameDrawBackdrop);
			intPulsarX06 = getintPulsarX(0.2);
			intPulsarY06 = getintPulsarY(0.3);
			canvas.drawCircle(intPulsarX06, intPulsarY06, (float) ((rnd.nextInt(6) + 3) * dblScaleRatio), pntGameDrawBackdrop);

			booCreatePulsars = true;
		}

		canvas.drawCircle(intPulsarX01, intPulsarY01, (float) ((rnd.nextInt(6) + 3) * dblScaleRatio), pntGameDrawBackdrop);
		canvas.drawCircle(intPulsarX02, intPulsarY02, (float) ((rnd.nextInt(6) + 3) * dblScaleRatio), pntGameDrawBackdrop);
		canvas.drawCircle(intPulsarX03, intPulsarY03, (float) ((rnd.nextInt(6) + 3) * dblScaleRatio), pntGameDrawBackdrop);
		canvas.drawCircle(intPulsarX04, intPulsarY04, (float) ((rnd.nextInt(6) + 3) * dblScaleRatio), pntGameDrawBackdrop);
		canvas.drawCircle(intPulsarX05, intPulsarY05, (float) ((rnd.nextInt(6) + 3) * dblScaleRatio), pntGameDrawBackdrop);
		canvas.drawCircle(intPulsarX06, intPulsarY06, (float) ((rnd.nextInt(6) + 3) * dblScaleRatio), pntGameDrawBackdrop);

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

	private void CreateMissile() {
		if (intCreateMissileFrame % AppConfig.intMissileFireFrequency == 0) {
			Config cfgBackdropBitmap = Bitmap.Config.ARGB_8888;
			int intBitmapRadius = (int) (AppConfig.intMissileRadius * dblScaleRatio) + 1;
			Bitmap bitBackdropMissile = Bitmap.createBitmap(intBitmapRadius, intBitmapRadius, cfgBackdropBitmap);

			lstMissiles.add(new MissileDraw(this, MainSprite, lstEnemies, bitBackdropMissile, intBitmapRadius));
		}
		intCreateMissileFrame++;
	}

	private void CreateEnemyMissile(int i) {
		Config cfgBackdropBitmap = Bitmap.Config.ARGB_8888;
		int intBitmapRadius = (int) (AppConfig.intMissileRadius * dblScaleRatio) + 1;
		Bitmap bitBackdropMissile = Bitmap.createBitmap(intBitmapRadius, intBitmapRadius, cfgBackdropBitmap);

		lstEnemyMissiles.add(new EnemyMissileDraw(this, MainSprite, lstEnemies, bitBackdropMissile, intBitmapRadius, i));
	}

	@Override
	public boolean onTouchEvent(MotionEvent TouchEvent) {
		final int intTouchEventAction = TouchEvent.getAction();
		// if (System.currentTimeMillis() - lngLastClick > (1000 /
		// gameLoopThread.FPS)) {
		if (true) {
			lngLastClick = System.currentTimeMillis();
			synchronized (getHolder()) {

				switch (intTouchEventAction & MotionEvent.ACTION_MASK) {
				case MotionEvent.ACTION_DOWN: {
					HandlePointerOrder(TouchEvent);
					break;
					/*
					 * booCursorDrag = false; // Remove Sprite on Touch for (int i = lstBmpSprite.size() - 1; i >= 0; i--) { SpriteDraw SpriteInList = lstBmpSprite.get(i); if (SpriteInList.isCollition(TouchEvent.getX(), TouchEvent.getY())) { lstBmpSprite.remove(SpriteInList); lstBmpCollision.add(new AfterCollisionWithSprite(lstBmpCollision, this, intPointerIndexX_00, intPointerIndexY_00, bmpIcon)); } } // check if inside the bounds of the DirectionalPad // Cursor(circle) // get the center for the DirectionalPad and Cursor int intCursorX = DirectionalCursor.getIntCursorX() + (DirectionalCursor.width / 2); int intCursorY = DirectionalCursor.getIntCursorY() + (DirectionalCursor.height / 2);
					 * 
					 * // calculate the radius from the touch to the center of the // Cursor double dblRadCircle = Math.sqrt((double) (((intCursorX - intPointerIndexX_00) * (intCursorX - intPointerIndexX_00)) + (intCursorY - intPointerIndexY_00) * (intCursorY - intPointerIndexY_00)));
					 * 
					 * // if the radius is smaller then radius of the // DirectionalPad is then it must inside the circle // Make it 1 for larger hit area if (dblRadCircle < (DirectionalPad.width / 1)) { DirectionalCursor.setPointerIndex(0); booCursorDrag = true; break; } else { dblRadCircle = Math.sqrt((double) (((intCursorX - intPointerIndexX_01) * (intCursorX - intPointerIndexX_01)) + (intCursorY - intPointerIndexY_01) * (intCursorY - intPointerIndexY_01))); if (dblRadCircle < (DirectionalPad.width / 1)) { DirectionalCursor.setPointerIndex(1); booCursorDrag = true; break; } }
					 * 
					 * break;
					 */
				}

				case MotionEvent.ACTION_MOVE: {
					// move the cursor the same as the finger
					if (booCursorDrag) {
						int intCursorPointerIndex = DirectionalCursor.getPointerIndex();
						intDirectionalCursorX = TouchEvent.getX(intCursorPointerIndex);
						intDirectionalCursorY = TouchEvent.getY(intCursorPointerIndex);

						// If pointer is greater right then 75% of the screen turn the drag off
						// Simultaneous Cursor lift with Button press; Switch the indexes

						if (intDirectionalCursorX > this.getWidth() / 1.25 && TouchEvent.getPointerCount() == 1) {
							DirectionalCursor.setPointerIndex(-1);
							DirectionalCursor.setButtonIndex(0);
							booCursorDrag = false;
							booButtonPress = true;
							HandlePointerOrder(TouchEvent);
							DirectionalCursor.setCursorReturn(true);
							break;
						} else if (intDirectionalCursorX > this.getWidth() / 1.5 && TouchEvent.getPointerCount() >= 1) {
							if (DirectionalCursor.getPointerIndex() == 0) {
								DirectionalCursor.setButtonIndex(0);
								// DirectionalCursor.setPointerIndex(1);
							} else if (DirectionalCursor.getPointerIndex() > 0) {
								DirectionalCursor.setButtonIndex(1);
								// DirectionalCursor.setPointerIndex(0);
							}
							DirectionalCursor.setPointerIndex(-1);
							booCursorDrag = false;
							booButtonPress = true;
							HandlePointerOrder(TouchEvent);
							DirectionalCursor.setCursorReturn(true);
							break;
						}
						// Get the Directional Cursors Coordinates
						dblCordinateCursorX = intDirectionalCursorX - (getDirectionalPadX() + (getDirectionalWidth() / 2));
						dblCordinateCursorY = intDirectionalCursorY - (getDirectionalPadY() + (getDirectionalHeight() / 2));
						// This is the Actual Cursor Position; above is the touch event position
						dblRealCordinateCursorX = DirectionalCursor.getIntCursorX() - getDirectionalPadX() - (DirectionalPad.width / 2) + (DirectionalCursor.width / 2); // 144
						dblRealCordinateCursorY = DirectionalCursor.getIntCursorY() - getDirectionalPadY() - (DirectionalPad.height / 2) + (DirectionalCursor.height / 2);
						// Calc Angle from Arc (Touch Event not Cursor)
						// dblArcCordinate = (Math.atan2(dblCordinateCursorX,
						// dblCordinateCursorY) / (Math.PI / 2) + 2);
						dblArcCordinate = (Math.atan2(dblCordinateCursorX, dblCordinateCursorY) / (Math.PI / 2) + 2);
						dblCursorAngle = Math.atan2(dblCordinateCursorY, dblCordinateCursorX) / (Math.PI / 180);

						// Clac the Adjusted Bound from the Boarder of the Circle
						double dblRadiusDirectionalPad = (getDirectionalWidth() / 2);
						double dblRadiusTouchEvent = Math.sqrt((dblCordinateCursorX * dblCordinateCursorX) + (dblCordinateCursorY * dblCordinateCursorY));

						double dblCursorBoundAdjustX = Math.cos(dblCursorAngle * (Math.PI / 180)) * (dblRadiusTouchEvent - dblRadiusDirectionalPad + (DirectionalCursor.width / 2));
						double dblCursorBoundAdjustY = Math.sin(dblCursorAngle * (Math.PI / 180)) * (dblRadiusTouchEvent - dblRadiusDirectionalPad + (DirectionalCursor.height / 2));

						// Put the cursor within the correct bound of the circle
						if (dblRadiusTouchEvent + (DirectionalCursor.width / 2) < dblRadiusDirectionalPad) {
							booSpriteMove = false;
							DirectionalCursor.setX((int) (intDirectionalCursorX - (DirectionalCursor.width / 2)));
							DirectionalCursor.setY((int) (intDirectionalCursorY - (DirectionalCursor.height / 2)));
						} else {
							booSpriteMove = true;
							DirectionalCursor.setX((int) (intDirectionalCursorX - (DirectionalCursor.width / 2) - dblCursorBoundAdjustX));
							DirectionalCursor.setY((int) (intDirectionalCursorY - (DirectionalCursor.height / 2) - dblCursorBoundAdjustY));
						}
					}
					break;
				}

				case MotionEvent.ACTION_UP: {
					resetAllPointerIndexes(TouchEvent);
					break;
				}

				case MotionEvent.ACTION_CANCEL: {
					resetAllPointerIndexes(TouchEvent);
					break;
				}

				case MotionEvent.ACTION_POINTER_UP: {
					resetAllPointerIndexes(TouchEvent);
					break;
				}
				case MotionEvent.ACTION_POINTER_DOWN: {
					HandlePointerOrder(TouchEvent);
					break;
					/*
					 * // The Original Pointer Coordinates // lstBmpCollision.add(new // AfterCollisionWithSprite(lstBmpCollision, this, // intPointerIndexX_00, intPointerIndexY_00, bmpIcon)); // The Pointer Coordinates for the second press
					 * 
					 * lstBmpCollision.add(new AfterCollisionWithSprite(lstBmpCollision, this, intPointerIndexX_01, intPointerIndexY_01, bmpIcon)); break;
					 */
				}
				}
			}
		}
		return true;
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
			final float intPointerIndexX_01 = TouchEvent.getX(1);
			final float intPointerIndexY_01 = TouchEvent.getY(1);
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

	public boolean getBooMainSpriteDead() {
		return booMainSpriteDead;
	}

	public Context getApplicationContext() {
		return applicationContext;
	}

	public GameMenu getGameMenu() {
		return gameMenu;
	}

	public boolean getSoundOn() {
		return booSoundOn;
	}

	public GameView getGameView() {
		return this;
	}

	@SuppressLint("WrongCall")
	@Override
	protected void onDraw(Canvas canvas) {
		if (booMainSpriteDead) {
			// So it is completely off the screen even though it is not going to be drawn
			// Probably dont need this but just to be safe
			MainSprite.setOverrideMainSpriteX(-999);
			MainSprite.setOverrideMainSpriteY(-999);
		}
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
		// Create the new missiles fired from MainSprite
		if (GameButton.getBooButtonDown())
			CreateMissile();
		else if (intCreateMissileFrame % AppConfig.intMissileFireFrequency != 0)
			intCreateMissileFrame++;

		// Draw Background Galaxy Image
		if (AppConfig.booDrawGalaxyBackground)
			canvas.drawBitmap(bmpBackgroundImage, 0, 0, null);
		// Draw Pulsars and moving stars
		CreateStarBackdrop(canvas);
		for (BackdropDraw SpawnStars : lstBackdropStars) {
			SpawnStars.onDraw(canvas);
		}

		// Draw the DirectionalPad and Directional Cursor and Button
		DirectionalPad.onDraw(canvas);
		DirectionalCursor.onDraw(canvas);
		GameButton.onDraw(canvas);

		// Opening the New level with an Opening screen for a few seconds
		if (!setLevelState.getStartLevel()) {
			setLevelState.onDraw(canvas);
			// Game starts on the next Frame
			if (setLevelState.getStartLevel()) {
				// Garbage all old collections
				// System.gc();
				// I have to reset the MainSprite and give a delay of a few seconds before I end the level
				// intEnemyHitCounter = 0;
				booMainSpriteDead = false;
				lstMissiles.clear();
				setMainSprite();
				CreateEnemies();
			}
			canvas.drawColor(0x44FFFFFF);
			pntStartLevel = pausePaint.getPausePaint();

			strPaintText = "LEVEL " + setLevelState.getCurrentLevel();
			canvas.drawText(strPaintText, this.getWidth() / 2, this.getHeight() / 2, pntStartLevel);

			strPaintText = "Ready in " + setLevelState.getStartLevelDuration();
			canvas.drawText(strPaintText, this.getWidth() / 2, (float) (this.getHeight() / 2 + (pntStartLevel.getTextSize() * 1.5)), pntStartLevel);
			// //////////////////////////////////////////////////
			// RETURN and do not load any other onDraw Graphics//
			// //////////////////////////////////////////////////
			return;
		}

		if (lstEnemies.size() == 0) {
			// Delay some frames after the last enemy is removed then start next level
			if (++intEndLevelDuration > AppConfig.FPS * 4) { // 4 seconds
				intEndLevelDuration = 0;

				// Set the new High Score
				int intNewHighScore = setLevelState.getEnemyHitCounter() + intEnemyHitCounter;
				if (intNewHighScore > setLevelState.getHighScore())
					setLevelState.setHighScore(intNewHighScore);

				// No more Enemies so increase Level by one
				setLevelState.setGameLevel(setLevelState.getCurrentLevel() + 1, intEnemyHitCounter);
				// gamePauseResumeFinish.setRunning(false);
				// I am just going to have to use the global paramaters and intents to get the start level as you dont seem to be able to kill a class without reseting the whole damn application
				// See what I did in RunningCalculator
				android.os.Process.killProcess(android.os.Process.myPid());
				// System.exit(0);
			}
		}

		// Calc Enemies via programmed path
		for (int i = 0; i < lstEnemyPath.size(); i++) {
			EnemyPath CalcPath = lstEnemyPath.get(i);
			CalcPath.DrawStoredPaths(i);
		}

		// Draw the Enemies on Path
		// for (EnemiesDraw SpawnEnemies : lstEnemies) {
		for (int i = 0; i < lstEnemies.size(); i++) {
			EnemiesDraw SpawnEnemies = lstEnemies.get(i);
			// Draw the Enemies on the Path
			SpawnEnemies.onDraw(canvas);

			// Does Enemy collide with MainSprite
			if (SpawnEnemies.getBooSpriteCollision()) {
				// Vibrate the Phone
				if (setLevelState.getVibrateOn()) {
					Vibrator vibSpriteCollision = (Vibrator) gameMenu.getSystemService(Context.VIBRATOR_SERVICE);
					vibSpriteCollision.vibrate(200);
				}

				// Create Explosion Bitmap over Sprite
				lstSpriteExplosion.add(new EnemyExplosion(this, MainSprite.getMainSpriteX(), MainSprite.getMainSpriteY(), bmpExplosion));
				MainSprite.setSpriteHit();
				// Remove Enemy that Collided with MainSprite
				lstEnemyRemove.add(SpawnEnemies);
				lstEnemyExplosion.add(new EnemyExplosion(this, SpawnEnemies.getEnemyX(), SpawnEnemies.getEnemyY(), bmpExplosion));
				if (!AppConfig.booMainSpriteInvincible)
					intHealth -= 11 + setLevelState.getCurrentLevel();
				intEnemyHitCounter++;
			}

			// Fire Missiles at MainSprite
			if (SpawnEnemies.getBooMissileFire(setLevelState.getCurrentLevel()))
				CreateEnemyMissile(i);

			// Remove Enemies out of Bounds
			if (SpawnEnemies.getEnemyY() > this.getHeight() - intLetterBoxHeight) // I could subtract SpawnEnemies.getHeight() to
				lstEnemyRemove.add(SpawnEnemies);
		}

		// Draw the Enemy Missiles fired at Sprite
		for (EnemyMissileDraw SpawnMissiles : lstEnemyMissiles) {
			if (AppConfig.booDrawEnemyMissiles)
				SpawnMissiles.onDraw(canvas);
			if (SpawnMissiles.getBooRemoveMissile()) {
				// Set Missile Bitmaps toRemove
				lstEnemyMissilesRemove.add(SpawnMissiles);
				// If Missile Hits MainSprite
				if (SpawnMissiles.getBooSpriteHit()) {
					// Vibrate the Phone
					if (setLevelState.getVibrateOn()) {
						Vibrator vibMissileHit = (Vibrator) gameMenu.getSystemService(Context.VIBRATOR_SERVICE);
						vibMissileHit.vibrate(200);
					}
					// Create Explosion Bitmap over Sprite
					lstSpriteExplosion.add(new EnemyExplosion(this, SpawnMissiles.getSpriteHitX(), SpawnMissiles.getSpriteHitY(), bmpExplosion));
					MainSprite.setSpriteHit();
					if (!AppConfig.booMainSpriteInvincible)
						intHealth -= 6 + setLevelState.getCurrentLevel();
				}
			}
		}

		// Draw Enemy Hit Counter at Top
		canvas.drawBitmap(bmpEnemyHitCounter, srcEnemyHitCounter, dstEnemyHitCounter, null);
		canvas.drawText(Integer.toString(intEnemyHitCounter), (float) ((5 * dblScaleRatio) + this.getWidth() - ((this.getWidth() - intEnemyHitCounterX) / 2)), intEnemyHitCounterY + pntStartLevel.getTextSize() - 3, pntStartLevel);

		// Draw the Health Bar at Top
		healthBar.onDraw(canvas, intHealth / 100.0);
		if (intHealth <= 0) {
			booMainSpriteDead = true;
			// Delay some frames after the last enemy is removed then start next level
			if (++intEndLevelDuration > AppConfig.FPS * (AppConfig.intDelayShowGameOver + AppConfig.intDelayGameOver)) { // 10 seconds
				intEndLevelDuration = 0;
				setLevelState.onDraw(canvas);
				// Game starts on the next Frame
				if (setLevelState.getStartLevel()) {
					// Garbage all old collections
					System.gc();

					// Set the new High Score
					int intNewHighScore = setLevelState.getEnemyHitCounter() + intEnemyHitCounter;
					if (intNewHighScore > setLevelState.getHighScore())
						setLevelState.setHighScore(intNewHighScore);

					// Dont reset the game back to 1,0 if I want to allow a replay of level on Game Over
					setLevelState.setGameLevel(1, 0);
					// finish();
					android.os.Process.killProcess(android.os.Process.myPid());
					// System.exit(0);

					// I have to reset the MainSprite and give a delay of a few seconds before I end the level
					// intEnemyHitCounter = 0;
					booMainSpriteDead = false;
					lstBmpCollision.clear();
					lstBmpSprite.clear();
					lstEnemies.clear();
					lstEnemyExplosion.clear();
					lstEnemyExplosionRemove.clear();
					lstEnemyMissiles.clear();
					lstEnemyMissilesRemove.clear();
					lstEnemyPath.clear();
					lstEnemyRemove.clear();
					lstMissiles.clear();
					lstMissilesRemove.clear();
					lstSpriteExplosion.clear();
					lstSpriteExplosionRemove.clear();
					setMainSprite();
					CreateEnemies();
				}
			} else if (++intEndLevelDuration > AppConfig.FPS * AppConfig.intDelayShowGameOver) { // 4 seconds
				canvas.drawColor(0x44FFFFFF);
				pntStartLevel = pausePaint.getPausePaint();

				strPaintText = "GAME OVER";
				canvas.drawText(strPaintText, this.getWidth() / 2, this.getHeight() / 2, pntStartLevel);

				strPaintText = "Replay Level " + setLevelState.getCurrentLevel();
				// canvas.drawText(strPaintText, this.getWidth() / 2, (float) (this.getHeight() / 2 + (pntStartLevel.getTextSize() * 1.5)), pntStartLevel);

				// //////////////////////////////////////////////////
				// RETURN and do not load any other onDraw Graphics//
				// //////////////////////////////////////////////////
			}

			return;
		}

		// Draw the Sprite Missiles fired at Enemy
		for (MissileDraw SpawnMissiles : lstMissiles) {
			SpawnMissiles.onDraw(canvas);
			if (SpawnMissiles.getBooRemoveMissile()) {
				// Set Missile Bitmaps toRemove
				lstMissilesRemove.add(SpawnMissiles);
				if (SpawnMissiles.getBooEnemyHit()) {
					// Create Explosion Bitmap over Enemy
					lstEnemyExplosion.add(new EnemyExplosion(this, SpawnMissiles.getEnemyHitX(), SpawnMissiles.getEnemyHitY(), bmpExplosion));
					intEnemyHitCounter++;
				}
			}
		}
		// Draw the Enemy Explosion
		for (EnemyExplosion SpawnExplosion : lstEnemyExplosion) {
			SpawnExplosion.onDraw(canvas);
			if (SpawnExplosion.getBooRemoveEnemyExplosion())
				// Set Explosion Bitmaps toRemove
				lstEnemyExplosionRemove.add(SpawnExplosion);
		}

		// Draw the MainSprite
		MainSprite.onDraw(canvas);

		// Draw the Sprite Explosion
		for (EnemyExplosion SpawnExplosion : lstSpriteExplosion) {
			SpawnExplosion.onDraw(canvas);
			if (SpawnExplosion.getBooRemoveEnemyExplosion())
				// Set Explosion Bitmaps toRemove
				lstSpriteExplosionRemove.add(SpawnExplosion);
		}

		// Remove the Sprite Explosions
		for (int i = lstSpriteExplosionRemove.size() - 1; i >= 0; i--) {
			lstSpriteExplosion.remove(lstSpriteExplosionRemove.get(i));
		}
		// Remove the Enemy Explosions
		for (int i = lstEnemyExplosionRemove.size() - 1; i >= 0; i--) {
			lstEnemyExplosion.remove(lstEnemyExplosionRemove.get(i));
		}
		// Remove the Sprite Missiles
		for (int i = lstMissilesRemove.size() - 1; i >= 0; i--) {
			lstMissiles.remove(lstMissilesRemove.get(i));
		}
		// Remove the Enemy Missiles
		for (int i = lstEnemyMissilesRemove.size() - 1; i >= 0; i--) {
			lstEnemyMissiles.remove(lstEnemyMissilesRemove.get(i));
		}
		// Remove the out of Bounds Enemies
		for (int i = lstEnemyRemove.size() - 1; i >= 0; i--) {
			lstEnemies.remove(lstEnemyRemove.get(i));
		}
		if (GameLoopThread.mPaused) {
			canvas.drawColor(0x44FFFFFF);
			pntGamePaused = pausePaint.getPausePaint();
			strPaintText = "PAUSED";
			canvas.drawText(strPaintText, this.getWidth() / 2, this.getHeight() / 2, pntGamePaused);
		}
		// strPaintText = String.valueOf(Math.round(dblCursorAngle));
		// canvas.drawText(strPaintText, 30, 50, pntStartLevel);
	}

	// public int getEnemiesRemaining() {
	// return lstEnemies.size();
	// }
}
// x Put in Enemy paths
// I am not going to have levels with bosses. I am going to have checkpoints accessible by the menu button
// At a checkpoint refill health and show an Advertisement
// After the check point increase the rate of fire from the enemies

// Need to add missile firing from Enemy
// Need to program Enemy Patterns in Levels
// Need Scoring system and Life Meter

// Also need a hit when Enemy Collides
// Health Bar
// Enemy Hit Counter

// Add total score at end of level
// Total EnemyHit plus Health Bonus plus level Bonus
// Add TapJoy advertisment every two levels
// add random pattern for enemy path

// Opening Menu
// New to add a place to submit highscore
// LeadBolt Advertisements