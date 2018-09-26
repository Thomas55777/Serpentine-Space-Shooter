package thomasWilliams.TetrisBlast;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;

public class DrawBitmap {
	private Bitmap bmp;

	public DrawBitmap(Bitmap bmp) {
		this.bmp = bmp;
	}

	public void scaleBitmap(float fltScaleFactorX, float fltScaleFactorY) {
		Bitmap bmpRaw = bmp;
		Matrix scalePad = new Matrix();
		scalePad.postScale(fltScaleFactorX, fltScaleFactorY);
		bmp = Bitmap.createBitmap(bmpRaw, 0, 0, bmpRaw.getWidth(), bmpRaw.getHeight(), scalePad, false);
		bmpRaw.recycle();
	}

	public Bitmap getBmp() {
		return bmp;
	}

	public Bitmap getBmp(int x, int y, int width, int height) {
		// Set the src
		return Bitmap.createBitmap(bmp, x, y, width, height);
	}

	public double getWidth() {
		return bmp.getWidth();
	}

	public double getHeight() {
		return bmp.getHeight();
	}
}
