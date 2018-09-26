package thomasWilliams.SerpentineSpaceShooter;

import java.util.List;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.KeyEvent;

public class AfterCollisionWithSprite {
	private float x;
	private float y;
	private Bitmap bmpCollisionInList;
	private int intDuration = 15; // number of times that update() is call
	private List<AfterCollisionWithSprite> bmpCollision;

	public AfterCollisionWithSprite(List<AfterCollisionWithSprite> bmpSpriteInList, GameView gameView, float x, float y, Bitmap bmpSprite) {
		this.x = Math.min(Math.max(x - bmpSprite.getWidth() / 2, 0),
				gameView.getWidth() - bmpSprite.getWidth());
		this.y = Math.min(Math.max(y - bmpSprite.getHeight() / 2, 0),
				gameView.getHeight() - bmpSprite.getHeight());
		this.bmpCollisionInList = bmpSprite;
		this.bmpCollision = bmpSpriteInList;
	}

	public void onDraw(Canvas canvas) {
		update();
		canvas.drawBitmap(bmpCollisionInList, x, y, null);
	}

	private void update() {
		if (--intDuration < 1) {
			bmpCollision.remove(this);
		}
	}

}
