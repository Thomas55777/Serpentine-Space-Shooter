package thomasWilliams.TetrisBlast;

import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Typeface;

public class PaintClass {

	private Paint paint;

	public PaintClass() {
		paint = new Paint();
	}

	public Paint getPaint(int intColor) {
		paint.setColor(intColor);
		return paint;
	}

	public Paint getPaintText(int intColor, float fltFontSize, boolean booBold, Align txtAlign, Typeface FontStyle) {
		// To use a Typeface that is not on the Phone. Put it in the Assets Folder and reference it like below
		// Typeface plain = Typeface.createFromAsset(assetManager, pathToFont); Typeface bold = Typeface.create(plain, Typeface.DEFAULT_BOLD)

		Typeface typeface = Typeface.create(FontStyle, Typeface.NORMAL);
		paint.setTypeface(typeface);
		paint.setColor(intColor); // android.graphics.Color.BLACK
		paint.setTextSize((float) (fltFontSize * GameView.dblScaleRatio));
		paint.setFakeBoldText(booBold);
		paint.setTextAlign(txtAlign);
		return paint;
	}
}
