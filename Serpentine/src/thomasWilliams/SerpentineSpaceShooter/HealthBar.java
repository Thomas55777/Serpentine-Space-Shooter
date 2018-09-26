package thomasWilliams.SerpentineSpaceShooter;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class HealthBar {
	private int width;
	private int height;
	private Paint pntHealthBar = new Paint();
	private Paint pntHealthBarBackground = new Paint();
	private int widthHealth;
	private int intScreenMargin = (int) (10 * GameView.dblScaleRatio);

	public HealthBar(int intScreenWidth) {
		width = (int) (intScreenWidth * .2);
		height = (int) (AppConfig.HealthBarHeight * GameView.dblScaleRatio);
		pntHealthBarBackground.setColor(0xAAFFFFFF);
		pntHealthBarBackground.setStrokeWidth(5);
		pntHealthBar.setColor(0xAAFF0000);
		pntHealthBar.setStrokeWidth(5);
	}

	public void onDraw(Canvas canvas, double dblRemainingHealth) {
		widthHealth = (int) (width * dblRemainingHealth);
		Rect rctHealthBarBackground = new Rect(intScreenMargin + GameView.intLetterBoxWidth, intScreenMargin , intScreenMargin + GameView.intLetterBoxWidth + width, intScreenMargin  + height);
		Rect rctHealthBar = new Rect(intScreenMargin + GameView.intLetterBoxWidth, intScreenMargin , intScreenMargin + GameView.intLetterBoxWidth + widthHealth, intScreenMargin  + height);
		canvas.drawRect(rctHealthBarBackground, pntHealthBarBackground);
		if (!(dblRemainingHealth <= 0))
			canvas.drawRect(rctHealthBar, pntHealthBar);
	}
}
