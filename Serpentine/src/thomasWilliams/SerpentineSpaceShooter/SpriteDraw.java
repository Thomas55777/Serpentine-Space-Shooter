package thomasWilliams.SerpentineSpaceShooter;

import java.util.Random;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

public class SpriteDraw {
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
	private Bitmap bmpSprite;
	private int width;
	private int height;
	private int currentFrame;

	public SpriteDraw(GameView gameView, Bitmap bmpSprite) {
		this.gameView = gameView;
		this.bmpSprite = bmpSprite;
		this.width = bmpSprite.getWidth() / BMP_COLUMNS;
		this.height = bmpSprite.getHeight() / BMP_ROWS;
		Random rnd = new Random(System.currentTimeMillis());
		// Set the random start position and speed		
		x = rnd.nextInt(gameView.getWidth() - width);
		y = rnd.nextInt(gameView.getHeight() - height);
		xSpeed = rnd.nextInt(50) - 5;
		ySpeed = rnd.nextInt(50) - 5;
		
		// Set the start position to the center of the screen 
//		x = (gameView.getWidth() - width)/2;
//		y = (gameView.getHeight() - height)/2;
		// Set to face upwards
//		xSpeed = 0;
//		ySpeed = -5;
	}

	private void update() {
		
		currentFrame = ++currentFrame % BMP_COLUMNS;

		if (x >= gameView.getWidth() - width - xSpeed || x + xSpeed <= 0) {
			xSpeed = -xSpeed;
		}
		x = x + xSpeed;
		if (y >= gameView.getHeight() - height - ySpeed || y + ySpeed <= 0) {
			ySpeed = -ySpeed;
		}
		y = y + ySpeed;
		currentFrame = ++currentFrame % BMP_COLUMNS;
	}

	public void onDraw(Canvas canvas) {
		update();
		int srcX = currentFrame * width;
		int srcY = getAnimationRow() * height;
		Rect srcSprite = new Rect(srcX, srcY, srcX + width, srcY + height);
		Rect dstSprite = new Rect(x, y, x + width, y + height);
		canvas.drawBitmap(bmpSprite, srcSprite, dstSprite, null);
	}

	// animation = 3 up, 1 left, 0 down, 2 right
	private int getAnimationRow() {
		double dblArcCordinate = (Math.atan2(xSpeed, ySpeed) / (Math.PI / 2) + 2);
		int intArcCordinate = (int) Math.round(dblArcCordinate) % BMP_ROWS;
		return intDirection[intArcCordinate];
	}

	public boolean isCollition(float x2, float y2) {
		return x2 > x && x2 < x + width && y2 > y && y2 < y + height;
	}

}
