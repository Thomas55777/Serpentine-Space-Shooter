package thomasWilliams.TetrisBlast;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;

public class DrawHighlight {
	private int x = 0;
	private int y = 0;
	private GameView gameView;
	private Rect srcSprite;
	private Rect dstSprite;
	private boolean booCreated = true;
	private int width;
	private int height;
	private Bitmap bmp;
	private int intGridID;

	public DrawHighlight(GameView gameView, Bitmap bmp) {
		this.gameView = gameView;
		this.width = bmp.getWidth();
		this.height = bmp.getHeight();
		this.bmp = bmp;
	}

	private void update() {
		// Set the src and dst
		srcSprite = new Rect(0, 0, width, height);
		dstSprite = new Rect(x, y, x + width, y + height);
	}

	public void setX(int intX) {
		x = intX;
	}

	public void setY(int intY) {
		y = intY;
	}
	
	public void setGridID(int intGridHighlightID) {
		intGridID = intGridHighlightID;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
	
	public int getGridID(){
		return intGridID;
	}

	public void onDraw(Canvas canvas) {
		update();
		canvas.drawBitmap(bmp, srcSprite, dstSprite, null);
		// PaintClass paint = new PaintClass();
		// canvas.drawRect(x, y, x + 200, y + 200, paint.getPaint(0xFFAAAAAA));
	}
}
