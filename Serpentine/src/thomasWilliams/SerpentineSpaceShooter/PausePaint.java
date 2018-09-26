package thomasWilliams.SerpentineSpaceShooter;

import android.graphics.Paint;
import android.graphics.Paint.Align;

public class PausePaint {

	private Paint paint;

	public PausePaint(int intColor){
		paint = new Paint();
		
		paint.setColor(intColor); // android.graphics.Color.BLACK
		paint.setTextSize((float) (30 * GameView.dblScaleRatio));
		paint.setFakeBoldText(true);
		paint.setTextAlign(Align.CENTER);	
	}
	
	public Paint getPausePaint(){
		return paint;
	}
}
