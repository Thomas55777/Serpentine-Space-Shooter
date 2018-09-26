package thomasWilliams.TetrisBlast;

import android.app.Application;

public class AppConfig extends Application {
	// GameLoopThread
	public static final long FPS = 20;
	// GameMenu
	public static final int intLevelsPerPage = 50;
	public static final float fltMenuTextSize = 22;
	public static boolean booLoadGameMenu = true;
	// GameView
	public static final int intBackgroundColor = 0xFF111111;

	public static final boolean booDrawGalaxyBackground = false;
	public static final boolean booBackgroundMusic = false;
	public static final float fltGameViewTextSize = 18;
	public static final int intBackgroundBaseColor = 0xFF111111;

	// Create the Base GameView
	public static int intFrameCounter = 0;
	public static int intFrameCounterFrequency = 2;
	public static final boolean booGameOver = true;
	public static final boolean booGenerateBlocks = true;
	public static float fltTextSize = 30;
	public static final boolean booScreenBestFit = false;
	public static final int intGridCountX = 10;
	public static final int intGridCountY = 20;
	public static final int intHighlightSize = 4;
	public static final float fltGameGridPadding = 0.02f;
	public static final double dblGameMarginLeft = 0.1; // .1
	public static final double dblGameMarginRight = 0.1;
	public static final double dblGameMarginTop = 0.02; // .22
	public static final double dblGameMarginBottom = 0.02;
	public static final double dblGameBoardBorderWidth = 0.03; // 0.07 Percent of screen used for the Boarder Color

	public static final boolean booGameDrawRaw = false;
	public static final boolean booDrawEnemyMissiles = true;
	public static final boolean booMainSpriteInvincible = false;
	public static final int intBeginningLevelCountdown = (int) (FPS * 3); // 3 seconds
	public static final int intAspectWidth = 480;
	public static final int intAspectHeight = 800;
	public static final double dblScreenRatio = (double) intAspectWidth / (double) intAspectHeight;
	public static final double dblBoundX = 0.4; // originally 0.3; // To determine the size of the directional cursor and button
	public static final double dblBoundY = dblBoundX * dblScreenRatio;

	// LeadBolt Ads
	public static final String strBanner300x250 = "164302032";
	public static final String strBanner468x60 = "787568120";
	public static final String strBanner640x100 = "257734435";
	public static final String strBanner728x90 = "513474731";
	public static final String strAppWall = "765974746";
	public static final String strPushNotification = "946544753";
	public static final int intSecondsCountDown = 60;
	public static final int intOverlayFrequency = 5;
	public static final boolean booAppWall = false;
	public static final boolean booShowAdvancedOverlay = false;
	public static final boolean booLoadPushNotification = false;
	public static final boolean booLoadBannerAds = false;
	// TapForTap Ads
	// public static final String strTapForTapAppId = "d2e625b0-cb63-012f-fbd6-4040d804a637";
	public static final boolean booLoadTapForTap = false;

	// ScoreLoop
	// private final String strScoreLoopSecret = "aP+D/xfZgUn10TGgSFKbnDmUrGrgW9lsUpZiWCH6h4ltgLJW0wZOcw=="; // SerpentineSpaceShooter

	public void onCreate() {
		// https://developer.scoreloop.com
		// Game ID has to be in the scoreloop.properties file (located in assets)

		// initialize the client using the context and game secret
		// Client.init(this, strScoreLoopSecret, null);
	}
}

// Can also do BillPay where user can pay to
// -Remove Advertisements
// -Speed Up
// -Twin Missiles

// Add friend section in ScoreLoop
// Enemy Paths Programming