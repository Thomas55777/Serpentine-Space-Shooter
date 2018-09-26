package thomasWilliams.SerpentineSpaceShooter;

import java.util.Random;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

public class EnemiesDraw {
	// direction = 0 up, 1 left, 2 down, 3 right,
	// animation = 3 up, 1 left, 0 down, 2 right
	int[] intDirection = { 3, 1, 0, 2 };
	private static final int BMP_COLUMNS = 3;
	private static final int BMP_ROWS = 4;
	private GameView gameView;
	private int width;
	private int height;
	private int x = 0;
	private int y = 0; // Put Enemy above screen
	private int currentFrame;
	private int countFrame;
	private int intBitmapRadius;
	private boolean booRemoveEnemy = false;
	private boolean booCreateEnemy = false;
	private boolean booSpriteCollision = false;
	private Bitmap bmpEnemy;
	private MainSprite mainSprite;
	private int intEnemySpawned;
	public int xLeftBound;
	public int xRightBound;
	public int yUpperBound;
	public int yLowerBound;
	public int intBoundWidth;
	public int intBoundHeight;
	private int xSpeed = 0;
	private int ySpeed = 0;
	public int intFrameCount = 0;
	private int intEnemyFrameSpacing = 7;
	private int intSpawnFrame00 = 30;
	private int intSpawnFrame01 = intSpawnFrame00 + intEnemyFrameSpacing;
	private int intSpawnFrame02 = intSpawnFrame01 + intEnemyFrameSpacing;
	private int intSpawnFrame03 = intSpawnFrame02 + intEnemyFrameSpacing;
	private int intSpawnFrame04 = intSpawnFrame03 + intEnemyFrameSpacing;
	private int intSpawnFrame05 = intSpawnFrame04 + (intEnemyFrameSpacing * 3);
	private int intSpawnFrame06 = intSpawnFrame05 + intEnemyFrameSpacing;
	private int intSpawnFrame07 = intSpawnFrame06 + intEnemyFrameSpacing;
	private int intSpawnFrame08 = intSpawnFrame07 + intEnemyFrameSpacing;
	private int intSpawnFrame09 = intSpawnFrame08 + intEnemyFrameSpacing;
	private static int intFrameSkip = 3;

	public EnemiesDraw(GameView gameView, Bitmap bmpEnemy, int intEnemySpawned, MainSprite mainSprite) {
		this.gameView = gameView;
		this.bmpEnemy = bmpEnemy;
		this.width = bmpEnemy.getWidth() / BMP_COLUMNS;
		this.height = bmpEnemy.getHeight() / BMP_ROWS;
		this.intEnemySpawned = intEnemySpawned;
		this.mainSprite = mainSprite;
		xLeftBound = GameView.intLetterBoxWidth;
		xRightBound = gameView.getWidth() - GameView.intLetterBoxWidth - width;
		yUpperBound = GameView.intLetterBoxHeight;
		yLowerBound = gameView.getHeight() - GameView.intLetterBoxHeight - height;
		intBoundWidth = gameView.getWidth() - (GameView.intLetterBoxWidth * 2);
		intBoundHeight = gameView.getHeight() - (GameView.intLetterBoxHeight * 2);
		y = -height - 500; // Put Enemy above screen
		x = -width - 500;// Put Enemy left of screen

		// put a random color by giving it a random current frame
		Random rndFrame = new Random();
		currentFrame = rndFrame.nextInt(BMP_COLUMNS);
	}

	private void update() {
		// RenderAnimation();
		intFrameCount++;
	}

	private void RenderAnimation() {
		if (countFrame % intFrameSkip == 0)
			currentFrame = ++currentFrame % BMP_COLUMNS;
		countFrame++;
	}

	private void checkEnemyCollision() {
		if (gameView.getBooMainSpriteDead())
			booSpriteCollision = false;
		else if (mainSprite.getMainSpriteY() + (mainSprite.getMainSpriteHeight() / 2) > y && mainSprite.getMainSpriteY() - (mainSprite.getMainSpriteHeight() / 2) < y && mainSprite.getMainSpriteX() - (mainSprite.getMainSpriteWidth() / 2) < x && mainSprite.getMainSpriteX() + (mainSprite.getMainSpriteWidth() / 2) > x) {
			booSpriteCollision = true;
		}
	}

	public void onDraw(Canvas canvas) {
		update();
		checkEnemyCollision();
		// Rect srcSprite = new Rect(0, 0, width, height);
		// Rect dstSprite = new Rect(x, y, x + width, y + height);

		int srcX = currentFrame * width;
		int srcY = getAnimationRow() * height;
		Rect srcSprite = new Rect(srcX, srcY, srcX + width, srcY + height);
		Rect dstSprite = new Rect(x, y, x + width, y + height);

		canvas.drawBitmap(bmpEnemy, srcSprite, dstSprite, null);
	}

	private int getAnimationRow() {
		double dblArcCordinate = (Math.atan2(xSpeed, ySpeed) / (Math.PI / 2) + 2);
		int intArcCordinate = (int) Math.round(dblArcCordinate) % BMP_ROWS;
		return intDirection[intArcCordinate];
	}

	public boolean getBooCreateEnemy() {
		return booCreateEnemy;
	}

	public boolean getBooRemoveEnemy() {
		return booRemoveEnemy;
	}

	public int getEnemyX() {
		return x;
	}

	public int getEnemyY() {
		return y;
	}

	public int getHeight() {
		return height;
	}

	public int getWidth() {
		return width;
	}

	public boolean getBooSpriteCollision() {
		return booSpriteCollision;
	}

	public int getEnemySpawned() {
		return intEnemySpawned;
	}

	public void setX(int intNewX) {
		xSpeed = intNewX - x;
		x = intNewX;
	}

	public void setY(int intNewY) {
		ySpeed = intNewY - y;
		y = intNewY;
	}

	public boolean getBooMissileFire(int intCurrentLevel) {
		// If Enemy is in GameView Bounds
		if (x >= xLeftBound && x <= xRightBound && y >= yUpperBound && y <= yLowerBound) {
			Random rndMissileFire = new Random();
			int intMissileFire = rndMissileFire.nextInt(1000) + (intCurrentLevel * 2);
			if (intMissileFire > AppConfig.intEnemyMissileFireFrequency)
				return true;
		}
		return false;
	}
}
