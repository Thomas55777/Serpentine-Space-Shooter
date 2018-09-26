package thomasWilliams.SerpentineSpaceShooter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Vibrator;

public class EnemyMissileDraw {
	// direction = 0 up, 1 left, 2 down, 3 right,
	// animation = 3 up, 1 left, 0 down, 2 right
	int[] intDirection = { 3, 1, 0, 2 };
	private static final int BMP_COLUMNS = 1;
	private static final int BMP_ROWS = 1;
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
	private int intSpriteHitX;
	private int intSpriteHitY;
	private boolean booSpriteHit = false;
	private int intSpriteHitCount;
	private EnemiesDraw EnemyFired;
	private int TargetX;
	private int TargetY;
	private int TargetHeight;
	private int TargetWidth;
	private MainSprite mainSprite;

	public EnemyMissileDraw(GameView gameView, MainSprite mainSprite, List<EnemiesDraw> lstEnemies, Bitmap bmpMissile, int intBitmapRadius, int i) {
		this.gameView = gameView;
		this.lstEnemies = lstEnemies;
		this.bmpMissile = bmpMissile;
		this.intBitmapRadius = intBitmapRadius;
		this.width = bmpMissile.getWidth() / BMP_COLUMNS;
		this.height = bmpMissile.getHeight() / BMP_ROWS;
		this.EnemyFired = lstEnemies.get(i);
		this.mainSprite = mainSprite;

		x = EnemyFired.getEnemyX() + (EnemyFired.getWidth() / 2) - (width / 2);
		y = EnemyFired.getEnemyY() + EnemyFired.getHeight();

		TargetHeight = mainSprite.getMainSpriteHeight();
		TargetWidth = mainSprite.getMainSpriteWidth();
		ySpeed = (int) (AppConfig.intMissileSpeed * GameView.dblScaleRatio);

		// Find the Angle from Enemy to MainSprite and adjust the xSpeed to match
		double dblMainSpriteAngle = dblfindAngleToMainSprite();

		// Find the Tangent distance from the ySpeed to get xSpeed
		double dblTanDegreees = Math.tan(dblMainSpriteAngle * (Math.PI / 180));
		int intSpeedX = (int) Math.round(-ySpeed / dblTanDegreees);

		// Put a cap to not exceed intMaxSpeedX
		int intMaxSpeedX = (int) (AppConfig.intMaxSpeedX * GameView.dblScaleRatio);

		if (Math.abs(intSpeedX) >= Math.abs(intMaxSpeedX))
			if (intSpeedX > intMaxSpeedX)
				intSpeedX = intMaxSpeedX;
			else if (intSpeedX < -intMaxSpeedX)
				intSpeedX = -intMaxSpeedX;
		
		// Put a directional change if dblMainSpriteAngle is negative
		if (dblMainSpriteAngle < 0)
			intSpeedX = -intSpeedX;
		xSpeed = intSpeedX;
	}

	private double dblfindAngleToMainSprite() {
		int intEnemyCenterX = x;
		int intEnemyCenterY = y - (EnemyFired.getHeight() / 2) - (height / 2);
		int intMainSpriteCenterX = mainSprite.getMainSpriteX() + (mainSprite.getMainSpriteWidth() / 2);
		int intMainSpriteCenterY = mainSprite.getMainSpriteY() + (mainSprite.getMainSpriteHeight() / 2);

		int intDeltaX = intMainSpriteCenterX - intEnemyCenterX;
		int intDeltaY = intMainSpriteCenterY - intEnemyCenterY;

		double dblCursorAngle = Math.atan2(intDeltaY, intDeltaX) / (Math.PI / 180);

		return dblCursorAngle;
	}

	private void update() {
		if (y > gameView.getHeight() - GameView.intLetterBoxHeight) {
			booRemoveMissile = true;
		}
		x += xSpeed;
		y += -ySpeed;

		currentFrame = ++currentFrame % BMP_COLUMNS;
	}

	public void onDraw(Canvas canvas) {
		update();
		getSpriteHit();
		Rect srcSprite = new Rect(0, 0, width * BMP_COLUMNS, height * BMP_ROWS);
		Rect dstSprite = new Rect(x, y, x + width * BMP_COLUMNS, y + height * BMP_ROWS);

		Random rndColor = new Random(System.currentTimeMillis());
		int intRndColorR = 255 - rndColor.nextInt(70);
		int intRndColorG = 255 - rndColor.nextInt(70);
		int intRndColorB = 255 - rndColor.nextInt(70);
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

	// See if this Missile Hits the MainSprite
	public void getSpriteHit() {
		TargetX = mainSprite.getMainSpriteX();
		TargetY = mainSprite.getMainSpriteY();
		if (TargetY + TargetHeight > y + (height / 2) && TargetY < y + (height / 2) && TargetX + width < x + (width / 2) && TargetX + TargetWidth - width > x + (width / 2)) {
			booRemoveMissile = true;
			booSpriteHit = true;
			intSpriteHitX = TargetX;
			intSpriteHitY = TargetY;
			intSpriteHitCount++;
		}
		// Cannot Continue to hit Sprite after dead
		if (gameView.getBooMainSpriteDead())
			booSpriteHit = false;
	}

	public int getSpriteHitCount() {
		return intSpriteHitCount;
	}

	public int getSpriteHitX() {
		return intSpriteHitX;
	}

	public int getSpriteHitY() {
		return intSpriteHitY;
	}

	public boolean getBooSpriteHit() {
		return booSpriteHit;
	}

	public boolean getBooRemoveMissile() {
		return booRemoveMissile;
	}

	public void toRemove(List<EnemyMissileDraw> lstMissiles) {
		lstMissiles.remove(this);
	}
}
