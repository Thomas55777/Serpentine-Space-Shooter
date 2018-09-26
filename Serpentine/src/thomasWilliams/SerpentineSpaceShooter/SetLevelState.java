package thomasWilliams.SerpentineSpaceShooter;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.preference.PreferenceManager;

public class SetLevelState extends Activity {

	private int intCurrentLevel;
	private boolean booStartLevel = false;
	private int intDurationCountDown;
	private SharedPreferences app_preferences;
	private GameMenu gameMenu;
	private int intEnemyHitCounter;
	private int intLevelCounter;
	private boolean booSoundOn;
	private boolean booVibrateOn;
	private int intHighScore;

	public SetLevelState(GameMenu gameMenu) {
		this.gameMenu = gameMenu;
		// intCurrentLevel = 1;

		intDurationCountDown = AppConfig.intBeginningLevelCountdown;
	}

	public void onDraw(Canvas canvas) {
		update();
	}

	private void update() {
		if (--intDurationCountDown < 1) {
			booStartLevel = true;
		}
	}

	public void setGameLevel(int intNewGameLevel, int intNewEnemyHitCounter) {
		intCurrentLevel = intNewGameLevel;
		intEnemyHitCounter = intNewEnemyHitCounter;

		gameMenu.setGameLevel(intCurrentLevel, intEnemyHitCounter);

		intDurationCountDown = AppConfig.intBeginningLevelCountdown;
		booStartLevel = false;
	}

	public void setHighScore(int intNewHighScore) {
		intHighScore = intNewHighScore;
		gameMenu.setHighScore(intHighScore);
	}

	public void setLevelCounter(int intNewLevelCounter) {
		intLevelCounter = intNewLevelCounter;
	}

	public void setOptionState(boolean booNewSoundOn, boolean booNewVibrateOn) {
		booSoundOn = booNewSoundOn;
		booVibrateOn = booNewVibrateOn;
	}

	public boolean getSoundOn() {
		return booSoundOn;
	}

	public boolean getVibrateOn() {
		return booVibrateOn;
	}

	public int getHighScore() {
		return intHighScore;
	}

	public int getCurrentLevel() {
		return intCurrentLevel;
	}

	public int getEnemyHitCounter() {
		return intEnemyHitCounter;
	}

	public int getLevelCounter() {
		return intLevelCounter;
	}

	public boolean getStartLevel() {
		return booStartLevel;
	}

	public int getStartLevelDuration() {
		return (int) (intDurationCountDown / AppConfig.FPS) + 1;
	}

}
