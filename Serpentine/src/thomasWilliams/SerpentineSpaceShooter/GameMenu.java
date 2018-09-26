package thomasWilliams.SerpentineSpaceShooter;

import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Parcel;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.pad.android.iappad.AdController;
import com.tapfortap.AdView;
import com.tapfortap.TapForTap;

public class GameMenu extends Activity implements OnClickListener {
	private GameView gameView;
	private SharedPreferences app_preferences;
	private SetLevelState setLevelState;
	private GameLoopThread gamePauseResumeFinish;
	private Button btnDistance;
	private Button btnSound;
	private final int SOUND_EXPLOSION = 1;
	private final int SOUND_LASER_FIRE = 2;
	private final int SOUND_LEVEL_UP = 3;
	private SoundPool soundPool;
	private HashMap<Integer, Integer> soundPoolMap;
	private boolean booOptionToggle = false;
	private boolean booGameView = false;
	private boolean booSoundOn;
	private boolean booVibrateOn;

	private PlayMediaPlayer backgroundMusicMenu;
	private Intent entScoreLoop;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		// setContentView(R.layout.opening_menu);
		setContentView(R.layout.tap_to_start);

		// int intBuildVersion = Integer.valueOf(android.os.Build.VERSION.SDK); Bionic is Level 10 Codename (Gingerbread)

		if (AppConfig.booLoadTapForTap) {
			// TapForTap Banner Ad at Top
			TapForTap.setDefaultAppId(AppConfig.strTapForTapAppId);
			TapForTap.checkIn(this);

			// Now get the AdView and load TapForTap ads!
			AdView adView = (AdView) findViewById(R.id.TapForTapAd);
			adView.loadAds();
		}
		// LeadBolt Banner Ad at Bottom
		DisplayMetrics dm = new DisplayMetrics();
		this.getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenWidth = dm.widthPixels;
		String myAdId = AppConfig.strBanner300x250; // (320x50)
		if (screenWidth >= 468 && screenWidth < 640) {
			myAdId = AppConfig.strBanner468x60; // (468x60)
		} else if (screenWidth >= 640 && screenWidth < 728) {
			myAdId = AppConfig.strBanner640x100; // (640x100)
		} else if (screenWidth >= 728) {
			myAdId = AppConfig.strBanner728x90; // (728x90)
		}

		final AdController myController = new AdController(this, myAdId);
		// myController.setAsynchTask(true);
		if (AppConfig.booLoadBannerAd)
			myController.loadAd();

		// To store the Add I could use a Parcel and write the myController to it. Get the Binary Data and use SharedPreferenced to put the string of the Binary
		// http://stackoverflow.com/questions/6285169/how-to-persist-nested-bundle-in-android

		// Set up Intent for ScoreLoop Activity
		entScoreLoop = new Intent(this, ScoreLoopActivity.class);

		// Get Level where it left off
		app_preferences = PreferenceManager.getDefaultSharedPreferences(this);
		setLevelState = new SetLevelState(this);
		setLevelState.setGameLevel(app_preferences.getInt("intLevel", 1), app_preferences.getInt("intEnemyHitCounter", 0));
		setLevelState.setHighScore(app_preferences.getInt("intHighScore", 0));
		booSoundOn = app_preferences.getBoolean("booSoundOn", true);
		booVibrateOn = app_preferences.getBoolean("booVibrateOn", true);
		setLevelState.setOptionState(booSoundOn, booVibrateOn);

		int intLevelCounter = app_preferences.getInt("intLevelCounter", 0);
		intLevelCounter++;
		SharedPreferences.Editor editor = app_preferences.edit();
		editor.putInt("intLevelCounter", intLevelCounter);
		setLevelState.setLevelCounter(intLevelCounter);
		editor.commit();

		// Load Sound Files
		initSounds();

		gameView = new GameView(this, setLevelState);

		btnDistance = (Button) findViewById(R.id.btnStartGame);
		btnSound = (Button) findViewById(R.id.btnSound);
		btnDistance.setOnClickListener((OnClickListener) this);
		btnSound.setOnClickListener((OnClickListener) this);

		TextView txtLevelId = (TextView) findViewById(R.id.txtLevelId);
		TextView txtEnemyHitId = (TextView) findViewById(R.id.txtEnemyHitId);
		TextView txtHighScore = (TextView) findViewById(R.id.txtHighScore);
		txtLevelId.setText(Integer.toString(setLevelState.getCurrentLevel()));
		txtEnemyHitId.setText(Integer.toString(setLevelState.getEnemyHitCounter()));
		txtHighScore.setText(Integer.toString(setLevelState.getHighScore()));

		LinearLayout linProgressSaved = (LinearLayout) findViewById(R.id.linProgressSaved);
		if (txtLevelId.getText().toString().equals("1"))
			linProgressSaved.setVisibility(View.INVISIBLE);
		else
			linProgressSaved.setVisibility(View.VISIBLE);
		LinearLayout MainLayout = (LinearLayout) findViewById(R.id.MainLinearLayout);
		MainLayout.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				if (!booOptionToggle) {
					booGameView = true;
					if (booSoundOn)
						backgroundMusicMenu.stopMusicLoop();
					System.gc();
					// destroy the banner ads before start GameView
					myController.destroyAd();
					gamePauseResumeFinish = new GameLoopThread(gameView);
					setContentView(gameView);
				}
				booOptionToggle = false;
				return false;
			}
		});
		LinearLayout SumbitScore = (LinearLayout) findViewById(R.id.linSumbitScore);
		// SumbitScore.setAlpha(50);
		// SumbitScore.setBackgroundColor(0x11FFFFFF);
		SumbitScore.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				if (!booGameView) {
					booOptionToggle = true;
					startActivity(entScoreLoop);
				}
				return false;
			}
		});
		LinearLayout SoundOnOff = (LinearLayout) findViewById(R.id.linSoundOnOff);
		SoundOnOff.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				if (!booGameView)
					ToggleSoundOnOff();
				return false;
			}
		});
		LinearLayout VibrateOnOff = (LinearLayout) findViewById(R.id.linVibrateOnOff);
		VibrateOnOff.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				if (!booGameView)
					ToggleVibrateOnOff();
				return false;
			}
		});

		ViewFlipper flpTapToStart = ((ViewFlipper) findViewById(R.id.flpTapToStart));
		flpTapToStart.startFlipping();
		flpTapToStart.setInAnimation(AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_in));
		flpTapToStart.setOutAnimation(AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_out));

		// Start Menu Background Music
		if (booSoundOn)
			CallBackgroundMusic();
		else {
			TextView txtSoundOnOff = (TextView) findViewById(R.id.txtSoundOnOff);
			txtSoundOnOff.setText("OFF");
		}
		if (!booVibrateOn) {
			TextView txtVibrateOnOff = (TextView) findViewById(R.id.txtVibrateOnOff);
			txtVibrateOnOff.setText("OFF");
		}
		if (AppConfig.booShowAdvancedOverlay)
			if (intLevelCounter % (AppConfig.intOverlayFrequency + 1) == 0)
				CallAdvacedOverlay();

		setTypeface();
	}

	private void setTypeface() {
		int intShadowRadius;
		int intShadowColor;

		Typeface typeAdamant = Typeface.createFromAsset(getAssets(), "fonts/Adamant_BG.otf");
		Typeface typeAdamantBold = Typeface.createFromAsset(getAssets(), "fonts/Adamant_BG_B.otf");

		TextView txtLevel = (TextView) findViewById(R.id.txtLevel);
		TextView txtLevelId = (TextView) findViewById(R.id.txtLevelId);
		TextView txtEnemyHit = (TextView) findViewById(R.id.txtEnemyHit);
		TextView txtEnemyHitId = (TextView) findViewById(R.id.txtEnemyHitId);
		TextView txtHighScore = (TextView) findViewById(R.id.txtHighScore);
		TextView txtHighScoreText = (TextView) findViewById(R.id.txtHighScoreText);
		TextView txtScoreOptions = (TextView) findViewById(R.id.txtScoreOptions);
		TextView txtScoreOptionsText = (TextView) findViewById(R.id.txtScoreOptionsText);
		TextView txtSoundOnOff = (TextView) findViewById(R.id.txtSoundOnOff);
		TextView txtSoundOnOffText = (TextView) findViewById(R.id.txtSoundOnOffText);
		TextView txtTapToStart1 = (TextView) findViewById(R.id.txtTapToStart1);
		TextView txtTapToStart2 = (TextView) findViewById(R.id.txtTapToStart2);
		TextView txtVibrateOnOff = (TextView) findViewById(R.id.txtVibrateOnOff);
		TextView txtVibrateOnOffText = (TextView) findViewById(R.id.txtVibrateOnOffText);

		txtLevel.setTypeface(typeAdamant);
		txtLevelId.setTypeface(typeAdamant);
		txtEnemyHit.setTypeface(typeAdamant);
		txtEnemyHitId.setTypeface(typeAdamant);
		txtHighScore.setTypeface(typeAdamant);
		txtHighScoreText.setTypeface(typeAdamant);
		txtScoreOptions.setTypeface(typeAdamant);
		txtScoreOptionsText.setTypeface(typeAdamant);
		txtSoundOnOff.setTypeface(typeAdamant);
		txtSoundOnOffText.setTypeface(typeAdamant);
		txtTapToStart1.setTypeface(typeAdamantBold);
		txtTapToStart2.setTypeface(typeAdamantBold);
		txtVibrateOnOff.setTypeface(typeAdamant);
		txtVibrateOnOffText.setTypeface(typeAdamant);

		intShadowRadius = 5;
		intShadowColor = 0xFF000000;
		txtLevel.setShadowLayer(intShadowRadius, 0, 0, intShadowColor);
		txtLevelId.setShadowLayer(intShadowRadius, 0, 0, intShadowColor);
		txtEnemyHit.setShadowLayer(intShadowRadius, 0, 0, intShadowColor);
		txtEnemyHitId.setShadowLayer(intShadowRadius, 0, 0, intShadowColor);
		txtHighScore.setShadowLayer(intShadowRadius, 0, 0, intShadowColor);
		txtHighScoreText.setShadowLayer(intShadowRadius, 0, 0, intShadowColor);
		txtScoreOptions.setShadowLayer(intShadowRadius, 0, 0, intShadowColor);
		txtScoreOptionsText.setShadowLayer(intShadowRadius, 0, 0, intShadowColor);
		txtSoundOnOff.setShadowLayer(intShadowRadius, 0, 0, intShadowColor);
		txtSoundOnOffText.setShadowLayer(intShadowRadius, 0, 0, intShadowColor);
		txtVibrateOnOff.setShadowLayer(intShadowRadius, 0, 0, intShadowColor);
		txtVibrateOnOffText.setShadowLayer(intShadowRadius, 0, 0, intShadowColor);

		
		txtTapToStart1.setTextSize(36);
		txtTapToStart2.setTextSize(36);

		intShadowRadius = 6;
		intShadowColor = 0xFF0000AA;
		txtTapToStart1.setShadowLayer(intShadowRadius, 0, 0, intShadowColor);
		txtTapToStart2.setShadowLayer(intShadowRadius, 0, 0, intShadowColor);
	}

	private void ToggleSoundOnOff() {
		booOptionToggle = true;
		TextView txtSoundOnOff = (TextView) findViewById(R.id.txtSoundOnOff);

		// Toggle Sound On Off
		if (booSoundOn) {
			backgroundMusicMenu.stopMusicLoop();
			System.gc();
			txtSoundOnOff.setText("OFF");
			booSoundOn = false;
		} else {
			CallBackgroundMusic();
			txtSoundOnOff.setText("ON ");
			booSoundOn = true;
		}

		setLevelState.setOptionState(booSoundOn, booVibrateOn);
		gameView = new GameView(this, setLevelState);
		SharedPreferences.Editor editor = app_preferences.edit();
		editor.putBoolean("booSoundOn", booSoundOn);
		editor.commit();
	}

	private void ToggleVibrateOnOff() {
		booOptionToggle = true;
		TextView txtVibrateOnOff = (TextView) findViewById(R.id.txtVibrateOnOff);

		// Toggle Vibrate On Off
		if (booVibrateOn) {
			txtVibrateOnOff.setText("OFF");
			booVibrateOn = false;
		} else {
			txtVibrateOnOff.setText("ON ");
			booVibrateOn = true;
		}

		setLevelState.setOptionState(booSoundOn, booVibrateOn);
		gameView = new GameView(this, setLevelState);
		SharedPreferences.Editor editor = app_preferences.edit();
		editor.putBoolean("booVibrateOn", booVibrateOn);
		editor.commit();
	}

	private void CallBackgroundMusic() {
		backgroundMusicMenu = new PlayMediaPlayer(getApplicationContext());
		backgroundMusicMenu.playMusicLoop(R.raw.background_menu);
	}

	private void CallAdvacedOverlay() {
		// If ten levels have been played
		Intent entAdvacedOverlay = new Intent(this, AdvacedOverlay.class);
		// entAdvacedOverlay.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
		// entAdvacedOverlay.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
		startActivity(entAdvacedOverlay);
		finish();
	}

	public void setGameLevel(int intCurrentLevel, int intEnemyHitCounter) {
		// Set Level where it left off
		SharedPreferences.Editor editor = app_preferences.edit();
		editor.putInt("intLevel", intCurrentLevel);
		editor.putInt("intEnemyHitCounter", intEnemyHitCounter);

		editor.commit();
	}

	public void setHighScore(int intHighScore) {
		// Set New High Score
		SharedPreferences.Editor editor = app_preferences.edit();
		editor.putInt("intHighScore", intHighScore);

		editor.commit();
	}

	private void resetLevel() {
		SharedPreferences.Editor editor = app_preferences.edit();
		editor.putInt("intLevel", 1);
		editor.putInt("intEnemyHitCounter", 0);
		editor.commit();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
	}

	public void onClick(View v) {
		try {
			if (v.getId() == R.id.btnStartGame) {
				gamePauseResumeFinish = new GameLoopThread(gameView);
				setContentView(gameView);
			} else if (v.getId() == R.id.btnSound) {
				// SoundPool
				SoundPoolSound(SOUND_EXPLOSION);

				// MediaPlayer
				Context applicationContext = getApplicationContext();
				PlayMediaPlayer psd = new PlayMediaPlayer(applicationContext);
				psd.playSound(R.raw.laser_fire);
				psd.playMusicLoop(R.raw.level_up);
			}
		} catch (Exception e) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("There has been an Error");
			builder.setMessage(e.getMessage());
			builder.setPositiveButton("OK", null);
			AlertDialog alert = builder.create();
			alert.show();
		}

	}

	@Override
	public void onAttachedToWindow() {
		// Disables the HomeKey
		this.getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD_DIALOG);
		super.onAttachedToWindow();

		// KeyguardManager keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
		// KeyguardLock lock = keyguardManager.newKeyguardLock(KEYGUARD_SERVICE);
		// lock.disableKeyguard();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// if a key as been pressed down AND this is the key BACK
		if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK) {
			if (gamePauseResumeFinish == null) {
				if (booSoundOn)
					backgroundMusicMenu.stopMusicLoop();
				System.gc();
				finish();
				// android.os.Process.killProcess(android.os.Process.myPid());
				// System.exit(0);
				return true;
			} else {
				gamePauseResumeFinish.setRunning(false);
				finish();
				resetLevel();
				android.os.Process.killProcess(android.os.Process.myPid());
				// System.exit(0);
				return true;
			}
		}
		if ((event.getAction() == KeyEvent.ACTION_DOWN))
			if (keyCode == KeyEvent.KEYCODE_HOME || keyCode == KeyEvent.KEYCODE_MENU || keyCode == KeyEvent.KEYCODE_SEARCH) {
				if (gamePauseResumeFinish == null) {
					// for the menu Inflater
					if (keyCode == KeyEvent.KEYCODE_HOME) {
						// closeOptionsMenu();
						// openOptionsMenu();
					}
					// Dont do the Menu; find another way; set to fasle if you want to launch the menu
					return true;
				}

				// Pause the Game by Pressing the Menu Button
				if (GameLoopThread.mPaused) {
					gamePauseResumeFinish.onResume();
				} else {
					gamePauseResumeFinish.onPause();
					// This would be to return to the Home Screen on Key_Menu but that would require too much work at the moment
					if (false) {
						Intent startMain = new Intent(Intent.ACTION_MAIN);
						startMain.addCategory(Intent.CATEGORY_HOME);
						startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						startActivity(startMain);
					}
				}
				return true;
			}
		// else
		return false;
	}

	@Override
	public boolean onTouchEvent(MotionEvent TouchEvent) {
		final int intTouchEventAction = TouchEvent.getAction();
		if (true) {

			switch (intTouchEventAction & MotionEvent.ACTION_MASK) {
			case MotionEvent.ACTION_DOWN: {
				break;
			}

			case MotionEvent.ACTION_MOVE: {
				break;
			}

			case MotionEvent.ACTION_UP: {
				break;
			}

			case MotionEvent.ACTION_CANCEL: {
				break;
			}

			case MotionEvent.ACTION_POINTER_UP: {
				break;
			}
			case MotionEvent.ACTION_POINTER_DOWN: {
				break;
			}
			}
		}
		return true;
	}

	private void initSounds() {
		soundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 100);
		soundPoolMap = new HashMap<Integer, Integer>();
		soundPoolMap.put(SOUND_EXPLOSION, soundPool.load(getApplicationContext(), R.raw.explosion, 2));
		soundPoolMap.put(SOUND_LASER_FIRE, soundPool.load(getApplicationContext(), R.raw.laser_fire_soft, 3));
		soundPoolMap.put(SOUND_LEVEL_UP, soundPool.load(getApplicationContext(), R.raw.level_up, 1));
	}

	public void SoundPoolSound(int intSound) {
		AudioManager mgr = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
		int streamVolume = mgr.getStreamVolume(AudioManager.STREAM_MUSIC);

		soundPool.play(soundPoolMap.get(intSound), streamVolume, streamVolume, 1, 0, 1f);
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater mnuInflater = getMenuInflater();
		mnuInflater.inflate(R.menu.main_menu, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menuMainHistory:
			// startActivity(new Intent(this, SrnHistory.class));
			return true;
		case R.id.menuMainHelp:
			// startActivity(new Intent(this, SrnHelp.class));
			return true;
		}
		return false;
	}
}
// Need to put save game stats on back button from null screen and message box to confirm data erased in game screen
// Need adds from leadBolt
// advanced overlay every ten levels
// sound effects
// better enemy graphics
// Got to do the Icon as well
// submit high scores online ***
// better enemy paths for the levels
// Need to have the LeadBolt Add load instantly from what previous loaded