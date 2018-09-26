package thomasWilliams.SerpentineSpaceShooter;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class MainSprite {
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
	private Bitmap bmpSpriteHit;
	private int width;
	private int height;
	private int currentFrame;
	private int countFrame;
	private double dblCursorAngle;
	private Paint pntMainSprite = new Paint();
	private Paint pntMainSpriteHit = new Paint();
	private boolean booMainSpriteHit;
	// adjust speed for pixel difference in scaling the GameView
	private static int intSpeedX = (int) Math.round((AppConfig.intSpriteSpeedX * GameView.dblScaleRatio));
	private static int intSpeedY = (int) Math.round((AppConfig.intSpriteSpeedY * GameView.dblScaleRatio));
	private static int intFrameSkip = 3; // How many frames to skip between next sprite frame
	public static int intSetDuration = intFrameSkip * BMP_COLUMNS; // number of times that update() is call
	private int intDuration = intSetDuration;
	private double dblArcCordinate;

	public MainSprite(GameView gameView, Bitmap bmpSprite, Bitmap bmpSpriteHit) {
		this.gameView = gameView;
		this.bmpSprite = bmpSprite;
		this.bmpSpriteHit = bmpSpriteHit;
		this.width = bmpSprite.getWidth() / BMP_COLUMNS;
		this.height = bmpSprite.getHeight() / BMP_ROWS;
		x = ((GameView.intGameDrawWidth - width) / 2) + GameView.intLetterBoxWidth;
		y = (int) ((GameView.intGameDrawHeight - height - height) + GameView.intLetterBoxHeight);
		xSpeed = 0;
		ySpeed = -1;
		pntMainSprite.setAlpha(255);
		pntMainSpriteHit.setAlpha(0);
	}

	private void update() {
		if (booMainSpriteHit) {
			pntMainSprite.setAlpha(0);
			pntMainSpriteHit.setAlpha(255);
			RenderAnimation();
			// Take 9 frames to Hit and then set booMainSpriteHit to false
			if (--intDuration < 1) {
				currentFrame = 1;
				pntMainSprite.setAlpha(255);
				pntMainSpriteHit.setAlpha(0);
				booMainSpriteHit = false;
			}
		}
		if (gameView.getCursorDrag()) {
			dblCursorAngle = gameView.getCursorAngle();
			if (dblCursorAngle >= (-90 + AppConfig.intCursorSensitivity) && dblCursorAngle <= (90 - AppConfig.intCursorSensitivity)) {
				if (!(x + intSpeedX >= GameView.intGameDrawWidth + GameView.intLetterBoxWidth - width))
					x += xSpeed = intSpeedX;
				RenderAnimation();
			}
			if (dblCursorAngle <= (-90 - AppConfig.intCursorSensitivity) || dblCursorAngle >= (90 + AppConfig.intCursorSensitivity)) {
				if (!(x - intSpeedX <= 0 + GameView.intLetterBoxWidth))
					x += xSpeed = -intSpeedX;
				RenderAnimation();
			}
			if (dblCursorAngle > (0 + AppConfig.intCursorSensitivity) && dblCursorAngle < 180 - AppConfig.intCursorSensitivity) {
				if (!(y + intSpeedY >= GameView.intGameDrawHeight + GameView.intLetterBoxHeight - height))
					y += ySpeed = intSpeedY;
				RenderAnimation();
			}
			if (dblCursorAngle < (0 - AppConfig.intCursorSensitivity) && dblCursorAngle > -180 + AppConfig.intCursorSensitivity) {
				if (!(y - intSpeedY <= 0 + GameView.intLetterBoxHeight))
					y += ySpeed = (int) (-intSpeedY * AppConfig.dblSlowFactorSpeedY);
				RenderAnimation();
			}

		} else {
			currentFrame = 1;
			countFrame = 0;
		}
	}

	private void RenderAnimation() {
		if (countFrame % intFrameSkip == 0)
			currentFrame = ++currentFrame % BMP_COLUMNS;
		countFrame++;
	}

	public void setSpriteHit() {
		intDuration = intSetDuration;
		booMainSpriteHit = true;
	}

	public void onDraw(Canvas canvas) {
		update();
		int srcX = currentFrame * width;
		int srcY = getAnimationRow() * height;
		Rect srcSprite = new Rect(srcX, srcY, srcX + width, srcY + height);
		Rect dstSprite = new Rect(x, y, x + width, y + height);
		// canvas.drawBitmap(bmpSprite, srcSprite, dstSprite, null);
		canvas.drawBitmap(bmpSprite, srcSprite, dstSprite, pntMainSprite);
		canvas.drawBitmap(bmpSpriteHit, srcSprite, dstSprite, pntMainSpriteHit);
	}

	// animation = 3 up, 1 left, 0 down, 2 right
	private int getAnimationRow() {
		if (!booMainSpriteHit)
			dblArcCordinate = gameView.getArcCordinate();
		int intArcCordinate = (int) Math.round(dblArcCordinate) % BMP_ROWS;
		return intDirection[intArcCordinate];
	}

	public int getMainSpriteX() {
		return x;
	}

	public int getMainSpriteY() {
		return y;
	}

	public int getMainSpriteWidth() {
		return width;
	}

	public int getMainSpriteHeight() {
		return height;
	}

	public void setOverrideMainSpriteX(int intOverrideX) {
		x = intOverrideX;
	}

	public void setOverrideMainSpriteY(int intOverrideY) {
		y = intOverrideY;
	}
}
