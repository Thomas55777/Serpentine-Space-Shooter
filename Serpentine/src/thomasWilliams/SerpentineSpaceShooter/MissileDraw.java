package thomasWilliams.SerpentineSpaceShooter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.R.color;
import android.R.plurals;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class MissileDraw {
	// direction = 0 up, 1 left, 2 down, 3 right,
	// animation = 3 up, 1 left, 0 down, 2 right
	int[] intDirection = { 3, 1, 0, 2 };
	private static final int BMP_COLUMNS = 3;
	private static final int BMP_ROWS = 4;
	private int x = 0;
	private int y = 0;
	private int xSpeed = 0;
	private int ySpeed = 0;
	private GameView gameView;
	private Bitmap bmpMissile;
	private int width;
	private int height;
	private int currentFrame;
	private int intBitmapRadius;
	private boolean booRemoveMissile = false;
	private List<EnemiesDraw> lstEnemies;
	private List<EnemyExplosion> lstEnemiesExplosion = new ArrayList<EnemyExplosion>();
	private int intEnemyHitX;
	private int intEnemyHitY;
	private boolean booEnemyHit = false;
	private boolean booCreated = true;

	public MissileDraw(GameView gameView, MainSprite mainSprite, List<EnemiesDraw> lstEnemies, Bitmap bmpMissile, int intBitmapRadius) {
		this.gameView = gameView;
		this.lstEnemies = lstEnemies;
		this.bmpMissile = bmpMissile;
		this.intBitmapRadius = intBitmapRadius;
		this.width = bmpMissile.getWidth() / BMP_COLUMNS;
		this.height = bmpMissile.getHeight() / BMP_ROWS;
		x = mainSprite.getMainSpriteX() + (mainSprite.getMainSpriteWidth() / 2) - (AppConfig.intMissileRadius / 2);
		y = mainSprite.getMainSpriteY();
		ySpeed = (int) (AppConfig.intSpriteMissileSpeed * GameView.dblScaleRatio);
	}

	private void update() {
		if (y < GameView.intLetterBoxHeight) {
			booRemoveMissile = true;
			// y = -500;
		}
		y = y + ySpeed;
		currentFrame = ++currentFrame % BMP_COLUMNS;
	}

	public void onDraw(Canvas canvas) {
		// Play Sound on when first Drawn
		if (gameView.getSoundOn() && booCreated) {
			gameView.getGameMenu().SoundPoolSound(2);
			booCreated = false;
		}

		update();
		getEnemyHit();
		Rect srcSprite = new Rect(0, 0, width * BMP_COLUMNS, height * BMP_ROWS);
		Rect dstSprite = new Rect(x, y, x + width * BMP_COLUMNS, y + height * BMP_ROWS);

		Random rndColor = new Random(System.currentTimeMillis());
//		int intRndColorR = 255 - rndColor.nextInt(70);
//		int intRndColorG = 255 - rndColor.nextInt(70);
//		int intRndColorB = 255 - rndColor.nextInt(70);
		int intRndColorR = 255; 
		//255, 102, 0 orange red
		//255, 153, 0 orange
		int intRndColorG = 160 - rndColor.nextInt(70);  // Between 160 and 90  // Red Orange
		int intRndColorB = 0;
		Paint pntBackdropStars = new Paint();
		pntBackdropStars.setColor(0xFFFFFFFF);
		pntBackdropStars.setColor(Color.rgb(intRndColorR, intRndColorG, intRndColorB));
		Canvas cvsBackdropStars = new Canvas(bmpMissile);

		Random rndRadius = new Random(System.currentTimeMillis());
		int intRndRadius = rndRadius.nextInt(intBitmapRadius / 2) + (intBitmapRadius / 2);
		// This gives a cool effect of circles in circles but because there is no way to undraw a canvas I cannot change radius much
		cvsBackdropStars.drawCircle((intBitmapRadius / 2), (intBitmapRadius / 2), ((intRndRadius) / 2), pntBackdropStars);

		canvas.drawBitmap(bmpMissile, srcSprite, dstSprite, null);
	}

	// use this code if I want to keep everything in GameView OnDraw
	// public EnemiesDraw getEnemyHit() {
	// for (EnemiesDraw EnemiesHit : lstEnemies) {
	// int intTemp = EnemiesHit.getEnemyY();
	// if (y < EnemiesHit.getEnemyY()) {
	// return EnemiesHit;
	// }
	// }
	// return null;
	// }

	// Loops through all the Remaining Enemies to see if this specific Missile has hit them. In Order By first Enemy Created
	// One Missile can Hit Only one Enemy. Reason why it has a break loop
	public void getEnemyHit() {
		for (EnemiesDraw EnemiesHit : lstEnemies) {
			if (EnemiesHit.getEnemyY() + EnemiesHit.getHeight() > y + (height / 2) && EnemiesHit.getEnemyY() < y + (height / 2) && EnemiesHit.getEnemyX() < x + (width / 2) && EnemiesHit.getEnemyX() + EnemiesHit.getWidth() > x + (width / 2)) {
				lstEnemies.remove(EnemiesHit);
				booRemoveMissile = true;
				booEnemyHit = true;
				intEnemyHitX = EnemiesHit.getEnemyX();
				intEnemyHitY = EnemiesHit.getEnemyY();
				break;
			}
		}
	}

	public int getEnemyHitX() {
		return intEnemyHitX;
	}

	public int getEnemyHitY() {
		return intEnemyHitY;
	}

	public boolean getBooEnemyHit() {
		return booEnemyHit;
	}

	public boolean getBooRemoveMissile() {
		return booRemoveMissile;
	}

	public void toRemove(List<MissileDraw> lstMissiles) {
		lstMissiles.remove(this);
	}
}
