package thomasWilliams.TetrisBlast;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.preference.PreferenceManager;

public class SetLevelState extends Activity {

	private int intCurrentLevel;
	private boolean booSoundOn;
	private int intGridSize;
	private int intMoves = 0;

	public SetLevelState() {
	}

	public void setGameLevel(int intNewGameLevel) {
		intCurrentLevel = intNewGameLevel;
	}

	public void setSoundState(boolean booNewSoundOn) {
		booSoundOn = booNewSoundOn;
	}

	public void setGridSize(int intSetGridSize) {
		intGridSize = intSetGridSize;
	}

	public void setBestMoves(int intBestMoves) {
		intMoves = intBestMoves;
	}

	public boolean getSoundOn() {
		return booSoundOn;
	}

	public int getCurrentLevel() {
		return intCurrentLevel;
	}

	public int getGridSize() {
		return intGridSize;
	}

	public String getBestMoves() {
		if (intMoves == 0)
			return "--";
		else
			return String.valueOf(intMoves);
	}

	public int getTotalCircles() {
//		return (intGridSize - 2) * (intGridSize - 2);
		return (intGridSize - 2) * 4;
	}
}
