package thomasWilliams.SerpentineSpaceShooter;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

public class EnemyExplosion {
	private int x;
	private int y;
	int[] intDirection = { 3, 1, 0, 2 };
	private Bitmap bitExplosion;
	private boolean booRemoveEnemyExplosion = false;
	private int width;
	private int height;
	private int currentFrame = 0;
	private int countFrame;
	private static final int BMP_COLUMNS = 3;
	private static final int BMP_ROWS = 4;
	private static int intFrameSkip = 3;
	private int intDuration = (BMP_ROWS + 1) * intFrameSkip; // number of frames that update() is called before Bitmap removed
	private int srcY;

	public EnemyExplosion(GameView gameView, int x, int y, Bitmap bitExplosion) {
		this.x = x;
		this.y = y;
		this.width = bitExplosion.getWidth() / BMP_COLUMNS;
		this.height = bitExplosion.getHeight() / BMP_ROWS;
		this.bitExplosion = bitExplosion;

		// Play Sound on create
		if (gameView.getSoundOn())
			gameView.getGameMenu().SoundPoolSound(1);
	}

	public void onDraw(Canvas canvas) {
		update();
		int srcX = 0; // currentFrame * width;
		// int srcY = getAnimationRow() * height;
		Rect srcSprite = new Rect(srcX, srcY, srcX + width, srcY + height);
		Rect dstSprite = new Rect(x, y, x + width, y + height);

		canvas.drawBitmap(bitExplosion, srcSprite, dstSprite, null);

		// canvas.drawBitmap(bitExplosion, x - (width / 2), y + (height / 2), null);
	}

	private void update() {
		RenderAnimation();
		if (--intDuration < 1) {
			booRemoveEnemyExplosion = true;
		}
	}

	private void RenderAnimation() {
		if (countFrame % intFrameSkip == 0)
			currentFrame++;
		if (currentFrame % 1 == 0)
			srcY = 1 * height;
		if (currentFrame % 2 == 0)
			srcY = 3 * height;
		if (currentFrame % 3 == 0)
			srcY = 2 * height;
		if (currentFrame % 4 == 0)
			srcY = 1 * height;
		if (currentFrame % 5 == 0)
			srcY = 0 * height;

		countFrame++;
	}

	public boolean getBooRemoveEnemyExplosion() {
		return booRemoveEnemyExplosion;
	}
}
