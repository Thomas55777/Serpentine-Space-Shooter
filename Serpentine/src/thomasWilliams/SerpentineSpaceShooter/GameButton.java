package thomasWilliams.SerpentineSpaceShooter;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

public class GameButton {
	// direction = 0 up, 1 left, 2 down, 3 right,
	// animation = 3 up, 1 left, 0 down, 2 right
	int[] intDirection = { 3, 1, 0, 2 };
	private static final int BMP_COLUMNS = 2;
	private static final int BMP_ROWS = 1;
	private int x = 0;
	private int y = 0;
	private int xSpeed = 0;
	private int ySpeed = 0;
	private GameView gameView;
	private Bitmap bmpGameButton;
	public int width;
	public int height;
	private int currentFrame;
	private double dblCursorAngle;
	// adjust speed for pixel difference
	private int intSpeed = (int) Math.round((8 * GameView.dblScaleRatio));
	private int intCursorSensitivity = 45;
	private int intDuration;
	private int intSetDuration = MainSprite.intSetDuration; // 5; // number of times that update() is call before returning Button Graphic to Normal
	private boolean booButtonDown = false;
	private int intScreenMargin = (int) (50 * GameView.dblScaleRatio);

	public GameButton(GameView gameView, Bitmap bmpGameButton) {
		this.gameView = gameView;
		this.bmpGameButton = bmpGameButton;
		this.width = bmpGameButton.getWidth() / BMP_COLUMNS;
		this.height = bmpGameButton.getHeight() / BMP_ROWS;
		x = GameView.intGameDrawWidth - width - intScreenMargin + (GameView.intLetterBoxWidth * 2);
		y = GameView.intGameDrawHeight + (GameView.intLetterBoxHeight * 2) - height - intScreenMargin;
	}

	private void update() {
		if (booButtonDown) {
			// fire missiles from Sprite
		}
		return;
		// if (--intDuration < 1) {
		// currentFrame = 0;
		// }
	}

	public void onDraw(Canvas canvas) {
		update();
		int srcX = currentFrame * width;
		int srcY = height * 0;
		Rect srcSprite = new Rect(srcX, srcY, srcX + width, srcY + height);
		Rect dstSprite = new Rect(x, y, x + width, y + height);
		canvas.drawBitmap(bmpGameButton, srcSprite, dstSprite, null);
	}

	public void setButtonDown() {
		booButtonDown = true;
		currentFrame = 1; // Down Graphic
		intDuration = intSetDuration;
	}

	public void setButtonUp() {
		booButtonDown = false;
		currentFrame = 0;
	}

	public int getGameButtonX() {
		return x;
	}

	public int getGameButtonY() {
		return y;
	}

	public boolean getBooButtonDown() {
		return booButtonDown;
	}
}
