package thomasWilliams.SerpentineSpaceShooter;

import android.app.Application;

import com.scoreloop.client.android.core.model.Client;

public class AppConfig extends Application {
	// GameLoopThread
	public static final long FPS = 30;
	// GameView
	public static final boolean booDrawGalaxyBackground = true;
	public static final boolean booGameDrawRaw = false;
	public static final boolean booDrawEnemyMissiles = true;
	public static final boolean booDrawPulsars = false;
	public static final boolean booMainSpriteInvincible = false;
	public static final int intBeginningLevelCountdown = (int) (FPS * 3); // 3 seconds
	public static final int intMissileRadius = 10; // raw radius; its gets scaled later
	public static final int intMissileFireFrequency = 6;
	public static final int intAspectWidth = 480;
	public static final int intAspectHeight = 800;
	public static final double dblScreenRatio = (double) intAspectWidth / (double) intAspectHeight;
	public static final double dblBoundX = 0.4; // originally 0.3; // To determine the size of the directional cursor and button
	public static final double dblBoundY = dblBoundX * dblScreenRatio;
	public static final int HealthBarHeight = 30;
	public static final int intDelayShowGameOver = 1;
	public static final int intDelayGameOver = 4;
	// MainSprite
	public static final int intSpriteSpeedX = 14;
	public static final int intSpriteSpeedY = intSpriteSpeedX; // 12;
	public static final int intSpriteMissileSpeed = -intSpriteSpeedX;
	public static final double dblSlowFactorSpeedY = 1; // 0.5;
	public static final int intCursorSensitivity = 45; // 20;
	// EnemyDraw
	public static final int intEnemyMissileFireFrequency = 990; // Out of 1000
	public static final int intMissileSpeed = -8; // Scaled later for speed
	public static final int intMaxSpeedX = 4; // Scaled later for speed
	public static final int intEnemySpeedX = 11;
	public static final int intEnemySpeedY = 5;
	// LeadBolt Ads
	public static final String strBanner300x250 = "757529169";
	public static final String strBanner468x60 = "871360192";
	public static final String strBanner640x100 = "263043343";
	public static final String strBanner728x90 = "149387946";
	// public static final String strAdvancedOverlay320x480 = "756312618";
	// public static final String strAdvancedOverlay480x800 = "668996509";
	public static final String strAdvancedOverlay320x480 = "889432449";
	public static final String strAdvancedOverlay480x800 = "457824037";
	public static final String strAppWall = "383008841";
	public static final String strPushNotification = "946544753";
	public static final boolean booShowAdvancedOverlay = false;
	public static final int intSecondsCountDown = 60;
	public static final int intOverlayFrequency = 5;
	public static final boolean booLoadPushNotification = false;
	public static final boolean booLoadBannerAd = false;
	// TapForTap Ads
	public static final String strTapForTapAppId = "d2e625b0-cb63-012f-fbd6-4040d804a637";
	public static final boolean booLoadTapForTap = false;
	// ScoreLoop
	private final String strScoreLoopSecret = "aP+D/xfZgUn10TGgSFKbnDmUrGrgW9lsUpZiWCH6h4ltgLJW0wZOcw=="; // SerpentineSpaceShooter

	public void onCreate() {
		// https://developer.scoreloop.com
		// Game ID has to be in the scoreloop.properties file (located in assets)

		// initialize the client using the context and game secret
		Client.init(this, strScoreLoopSecret, null);
	}
}

// Can also do BillPay where user can pay to
// -Remove Advertisements
// -Speed Up
// -Twin Missiles

// Add friend section in ScoreLoop
// Enemy Paths Programming