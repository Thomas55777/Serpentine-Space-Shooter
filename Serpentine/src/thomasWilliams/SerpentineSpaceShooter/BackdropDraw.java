package thomasWilliams.SerpentineSpaceShooter;

import java.util.Random;

import android.R.color;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class BackdropDraw {
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
	private Bitmap bmpStar;
	private int width;
	private int height;
	private int currentFrame;
	private int intBitmapRadius;

	public BackdropDraw(GameView gameView, Bitmap bmpStar, int intBitmapRadius) {
		this.gameView = gameView;
		this.bmpStar = bmpStar;
		this.intBitmapRadius = intBitmapRadius;
		this.width = bmpStar.getWidth() / BMP_COLUMNS;
		this.height = bmpStar.getHeight() / BMP_ROWS;
		Random rnd = new Random(System.currentTimeMillis());
		// Set the random start position and speed
		x = rnd.nextInt(gameView.getWidth() - width);
		y = rnd.nextInt(gameView.getHeight() - height);
//		xSpeed = rnd.nextInt(4) -2;
		ySpeed = rnd.nextInt(5) +3;

		// Set the start position to the center of the screen
		// x = (gameView.getWidth() - width)/2;
		// y = (gameView.getHeight() - height)/2;
		// Set to face upwards
		// xSpeed = 0;
		// ySpeed = -5;
	}

	private void update() {

		currentFrame = ++currentFrame % BMP_COLUMNS;

//		if (x >= gameView.getWidth() - width - xSpeed || x + xSpeed <= 0) {
//			xSpeed = -xSpeed;
//		}
		x = x + xSpeed;
		// if (y >= gameView.getHeight() - height - ySpeed || y + ySpeed <= 0) {
		// ySpeed = -ySpeed;
		// }
		if (x >= gameView.getWidth() - width - xSpeed ){
			x=0;
		}
		if (y >= gameView.getHeight() - height - ySpeed) {
			y = 0;
		}
		y = y + ySpeed;
		currentFrame = ++currentFrame % BMP_COLUMNS;
	}

	public void onDraw(Canvas canvas) {
		update();
		int srcX = currentFrame * width;
		int srcY = getAnimationRow() * height;
		// Rect srcSprite = new Rect(srcX, srcY, srcX + width, srcY + height);
		Rect srcSprite = new Rect(0, 0, width * BMP_COLUMNS, height * BMP_ROWS);
		Rect dstSprite = new Rect(x, y, x + width * BMP_COLUMNS, y + height * BMP_ROWS);
		
//		Paint pntBackdropStars = new Paint();
//		pntBackdropStars.setColor(0xFFFFFFFF);
//		Canvas cvsBackdropStars = new Canvas(bmpStar);
//		Random rnd = new Random(System.currentTimeMillis());

//		cvsBackdropStars.drawCircle((20 / 2) - 1, (20 / 2) - 1, rnd.nextInt(17) + 3, pntBackdropStars );
//		canvas.drawCircle(10, 10, rnd.nextInt(200) + 3, pntBackdropStars);
		
		Random rndColor = new Random(System.currentTimeMillis());
		int intRndColorR = 255 - rndColor.nextInt(50);
		int intRndColorG = 255 - rndColor.nextInt(150);
		int intRndColorB = 255 - rndColor.nextInt(150);
		Paint pntBackdropStars = new Paint();
		pntBackdropStars.setColor(0xFFFFFFFF);
		pntBackdropStars.setColor(Color.rgb(intRndColorR, intRndColorG, intRndColorB));
		Canvas cvsBackdropStars = new Canvas(bmpStar);
		
		Random rndRadius = new Random(System.currentTimeMillis());
		int intRndRadius = rndRadius.nextInt(intBitmapRadius/2)+(intBitmapRadius/2);
//		This gives a cool effect of circles in circles but because there is no way to undraw a canvas I cannot change radius much 
		cvsBackdropStars.drawCircle((intBitmapRadius / 2) - 1, (intBitmapRadius / 2) - 1, ((intRndRadius)/ 2) - 1, pntBackdropStars);

		canvas.drawBitmap(bmpStar, srcSprite, dstSprite, null);
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
