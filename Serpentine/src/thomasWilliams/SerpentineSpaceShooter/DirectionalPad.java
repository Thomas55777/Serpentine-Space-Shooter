package thomasWilliams.SerpentineSpaceShooter;

import java.util.Random;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

public class DirectionalPad {
	// direction = 0 up, 1 left, 2 down, 3 right,
	// animation = 3 up, 1 left, 0 down, 2 right
	int[] intDirection = { 3, 1, 0, 2 };
	private int x = 0;
	private int y = 0;
	private int xSpeed = 0;
	private int ySpeed = 0;
	private GameView gameView;
	private Bitmap bmpDirectionalPad;
	public int width;
	public int height;
	private int currentFrame;
	private int intLeftMargin = (int) (0 * GameView.dblScaleRatio);
	private int intPadX;
	private int intPadY;
	private int intDirectionalPadScreenMargin = (int) (50 * GameView.dblScaleRatio);

	public DirectionalPad(GameView gameView, Bitmap bmpDirectionalPad) {
		this.gameView = gameView;
		this.bmpDirectionalPad = bmpDirectionalPad;
		this.width = bmpDirectionalPad.getWidth();
		this.height = bmpDirectionalPad.getHeight();
	}

	public void onDraw(Canvas canvas) {
		intPadX = getIntPadX();
		intPadY = getIntPadY();
		canvas.drawBitmap(bmpDirectionalPad, intPadX, intPadY, null);
	}

	public int getIntPadX() {
		return intLeftMargin + intDirectionalPadScreenMargin; // + GameView.intLetterBoxWidth;
	}

	public int getIntPadY() {
		// return gameView.getHeight() - height - intLeftMargin;
		return GameView.intGameDrawHeight + (GameView.intLetterBoxHeight * 2) - height - intLeftMargin - intDirectionalPadScreenMargin;
		// return (int) ((gameView.getHeight() - height) * 0.75);
	}
}
