package thomasWilliams.SerpentineSpaceShooter;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class DirectionalCursor {
	// direction = 0 up, 1 left, 2 down, 3 right,
	// animation = 3 up, 1 left, 0 down, 2 right
	int[] intDirection = { 3, 1, 0, 2 };
	private Bitmap bmpDirectionalCursor;
	public int width;
	public int height;
	private int intCursorX;
	private int intCursorY;
	private int intCenterX;
	private int intCenterY;
	private int intPointerIndex;
	private int intPointerCount;
	private int intButtonIndex;

	public DirectionalCursor(GameView gameView, Bitmap bmpDirectionalCursor) {
		this.bmpDirectionalCursor = bmpDirectionalCursor;
		this.width = bmpDirectionalCursor.getWidth();
		this.height = bmpDirectionalCursor.getHeight();
		// Initial Setup Beg /////////
		intCenterX = gameView.getDirectionalPadX() + (gameView.getDirectionalWidth() / 2) - (width / 2);
		intCenterY = gameView.getDirectionalPadY() + (gameView.getDirectionalHeight() / 2) - (height / 2);
		intCursorX = intCenterX;
		intCursorY = intCenterY;
		// Initial Setup End /////////
	}

	private void update() {
//		 if (intCursorX >= intCenterX) {
//		 intCursorX = intCursorX - xSpeed;
//		 }
//		 if (intCursorY >= intCenterY) {
//		 intCursorY = intCursorY - ySpeed;
//		 }
		intCursorX = intCenterX;
		intCursorY = intCenterY;
	}

	public void onDraw(Canvas canvas) {
		canvas.drawBitmap(bmpDirectionalCursor, intCursorX, intCursorY, null);
	}

	public int getIntCursorX() {
		return intCursorX;
	}

	public int getIntCursorY() {
		return intCursorY;
	}

	public void setX(int intNewX) {
		intCursorX = intNewX;
	}

	public void setY(int intNewY) {
		intCursorY = intNewY;
	}

	public void setPointerIndex(int intNewPointerIndex) {
		intPointerIndex = intNewPointerIndex;
	}

	public int getPointerIndex() {
		return intPointerIndex;
	}
	public void setButtonIndex(int intNewPointerIndex) {
		intButtonIndex = intNewPointerIndex;
	}
	public int getButtonIndex() {
		return intButtonIndex;
	}
	public void setPointerCount(int intNewPointerCount) {
		intPointerCount = intNewPointerCount;
	}
	public int getPointerCount() {
		return intPointerCount;
	}

	public void setCursorReturn(boolean booReturn) {
		if (booReturn)
			update();
	}
}
