package thomasWilliams.TetrisBlast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Html;
import android.text.Layout;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pad.android.iappad.AdController;

public class GameMenu extends Activity implements OnClickListener {
	private GameView gameView;
	private SharedPreferences app_preferences;
	private SetLevelState setLevelState;
	private GameLoopThread gamePauseResumeFinish;
	private final int CIRCLE_UP = 1;
	private final int CIRCLE_DOWN = 2;
	private final int SOUND_NEW_LEVEL = 3;
	private SoundPool soundPool;
	private HashMap<Integer, Integer> soundPoolMap;
	private boolean booOptionToggle = false;
	private boolean booGameView = false;
	private boolean booSoundOn;
	private boolean booVibrateOn;

	private PlayMediaPlayer backgroundMusicMenu;
	private AdController myController;
	private AdController myControllerGameView;
	private ArrayList<String> listCountry;
	private ArrayList<Integer> listFlag;
	private int intGridSize;
	private View gridView;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); // WAKE_LOCK Required
		// setContentView(R.layout.opening_menu);
		// setContentView(R.layout.tap_to_start);

		// setContentView(R.layout.game_menu);
		// GridView gridview = (GridView) findViewById(R.id.gridview);
		// gridview.setAdapter(new ImageAdapter(this));
		//
		// gridview.setOnItemClickListener(new OnItemClickListener() {
		// public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
		// LoadLevel(position);
		// }
		// });

		setContentView(R.layout.gridview);
		// gridView = findViewById(R.layout.gridview);
		gridView = new View(this);

		// int intBuildVersion = Integer.valueOf(android.os.Build.VERSION.SDK); Bionic is Level 10 Codename (Gingerbread)
		setLevelState = new SetLevelState();

		// Get Level where it left off
		app_preferences = PreferenceManager.getDefaultSharedPreferences(this);
		booSoundOn = app_preferences.getBoolean("booSoundOn", true);
		booVibrateOn = app_preferences.getBoolean("booVibrateOn", true);
		setLevelState.setSoundState(booSoundOn);

		// Get GridSize from Shared Preferences
		intGridSize = app_preferences.getInt("intGridSize", -1);

		// Load the Tutorial
		if (app_preferences.getInt("intGridSize", -1) == -1) {
			// Set the Grid Size
			intGridSize = 5;

			// if (booSoundOn)
			// CallBackgroundMusic();
			//
			// gameView = new GameView(this, setLevelState, null);
			// LoadLevel(-1);
			// return;
		}
		TextView txtGridSize = (TextView) findViewById(R.id.txtGridSize);
		txtGridSize.setText(intGridSize + " x " + intGridSize);

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

		myController = new AdController(this, myAdId);
		// myController.resumeAd();
		myControllerGameView = new AdController(this, myAdId);
		// myController.setAsynchTask(true);
		if (AppConfig.booLoadBannerAds)
			myController.loadAd();

		// To store the Add I could use a Parcel and write the myController to it. Get the Binary Data and use SharedPreferenced to put the string of the Binary
		// http://stackoverflow.com/questions/6285169/how-to-persist-nested-bundle-in-android

		// Load Sound Files
		initSounds();

		gameView = new GameView(this, setLevelState, myControllerGameView);

		LinearLayout SoundOnOff = (LinearLayout) findViewById(R.id.linSoundOnOff);
		SoundOnOff.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				if (!booGameView)
					ToggleSoundOnOff();
				return false;
			}
		});

		// Start Menu Background Music
		if (booSoundOn && app_preferences.getInt("intGridSize", -1) != -1)
			CallBackgroundMusic();
		else {
			TextView txtSoundOnOff = (TextView) findViewById(R.id.txtSoundOnOff);
			txtSoundOnOff.setText("OFF");
		}

		// Load GameView straight from Menu
		if (!AppConfig.booLoadGameMenu){
			booSoundOn = false;
			LoadLevel(1);
		}
	}

	public void LoadLevel(int position) {
		// Toast.makeText(GameMenu.this, "Level: " + (position + 1), Toast.LENGTH_SHORT).show();

		// Set GridSize and LevelSelected
		setLevelState.setGameLevel(position + 1);
		setLevelState.setGridSize(intGridSize); // This will be variable based on Level Selected
		if (!booOptionToggle) {
			SharedPreferences.Editor editor = app_preferences.edit();
			editor.putBoolean("booLoadGameMenu", true);
			editor.commit();

			booGameView = true;

			if (booSoundOn)
				backgroundMusicMenu.stopMusicLoop();
			System.gc();
			gamePauseResumeFinish = new GameLoopThread(gameView);
			setContentView(gameView);
		}
		booOptionToggle = false;
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

		setLevelState.setSoundState(booSoundOn);
		gameView = new GameView(this, setLevelState, myControllerGameView);
		SharedPreferences.Editor editor = app_preferences.edit();
		editor.putBoolean("booSoundOn", booSoundOn);
		editor.commit();
	}

	private void CallBackgroundMusic() {
		backgroundMusicMenu = new PlayMediaPlayer(getApplicationContext());
		backgroundMusicMenu.playMusicLoop(R.raw.background_menu);
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

	public void onClick(View v) {
		try {
			if (v.getId() == R.id.btnStartGame) {
				gamePauseResumeFinish = new GameLoopThread(gameView);
				setContentView(gameView);
			} else if (v.getId() == R.id.btnSound) {
				// SoundPool
				// SoundPoolSound(SOUND_EXPLOSION);

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
	public void onDestroy() {
		super.onDestroy();
		SharedPreferences.Editor editor = app_preferences.edit();
		editor.putBoolean("booLoadGameMenu", false);
		editor.commit();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// if a key as been pressed down AND this is the key BACK
		if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK) {
			if (gamePauseResumeFinish == null) { // || app_preferences.getInt("intGridSize", -1) == -1) {
				if (booSoundOn)
					backgroundMusicMenu.stopMusicLoop();
				System.gc();
				finish();
				// android.os.Process.killProcess(android.os.Process.myPid());
				// System.exit(0);

				// User presses back while still in the GameMenu Screen; Bring user to the GridSelect Menu
//				Intent entGame = new Intent(this, GridSelect.class);
//				startActivity(entGame);

				return true;
			} else {
				// Set Tutorial to goto Grid Size 5
				if (app_preferences.getInt("intGridSize", -1) == -1) {
					SharedPreferences.Editor editor = app_preferences.edit();
					editor.putInt("intGridSize", intGridSize);
					editor.commit();

					finish();
					android.os.Process.killProcess(android.os.Process.myPid());
					Intent entGame = new Intent(this, GridSelect.class);
					startActivity(entGame);
					return true;
				}
				gamePauseResumeFinish.setRunning(false);
				finish();
				resetLevel();

				android.os.Process.killProcess(android.os.Process.myPid());
				// android.os.Process.killProcess(android.os.Process.myTid());
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
					// Dont do the Menu; find another way; set to false if you want to launch the menu
					return true;
				}

				// Pause the Game by Pressing the Menu Button
				if (GameLoopThread.mPaused) {
					gamePauseResumeFinish.onResume();
				} else {
					gamePauseResumeFinish.onPause();
					// Dont Pause the ShuffleFour
					// gamePauseResumeFinish.onPause();
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
	public void onPause() {
		super.onPause();
		// On Menu Press during GamePlay the Game will go back to the menu when user returns
		// I can work with Saved States later if I want to
		if (gamePauseResumeFinish != null) {
			gamePauseResumeFinish.setRunning(false);
			System.gc();
			// finish();
			android.os.Process.killProcess(android.os.Process.myPid());
		}
		if (backgroundMusicMenu != null)
			backgroundMusicMenu.stopMusicLoop();
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (backgroundMusicMenu != null)
			backgroundMusicMenu.stopMusicLoop();
		// if (!backgroundMusicMenu.isPlaying() && booSoundOn)
		if (booSoundOn)
			CallBackgroundMusic();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	public void resetGameView() {
		if (gamePauseResumeFinish == null) {
			return;
		}
		// System.gc();
		// finish();
		// gamePauseResumeFinish.setRunning(false);
		// gamePauseResumeFinish = new GameLoopThread(gameView);
		// setContentView(gameView);
		// Pause the Game by Pressing the Menu Button
		// if (GameLoopThread.mPaused) {
		// gamePauseResumeFinish.onResume();
		// } else {
		// gamePauseResumeFinish.onPause();
		// }
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
				booSoundOn = false;
				LoadLevel(1);
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
		soundPoolMap.put(CIRCLE_UP, soundPool.load(getApplicationContext(), R.raw.circle_up, 2));
		soundPoolMap.put(CIRCLE_DOWN, soundPool.load(getApplicationContext(), R.raw.circle_down, 3));
		soundPoolMap.put(SOUND_NEW_LEVEL, soundPool.load(getApplicationContext(), R.raw.new_level, 1));
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

//	public String getBestMoves() {
//		StoreDataInSQLite DataStore = new StoreDataInSQLite(this);
//		DataStore.open();
//
//		// Find and Create or Update DB
//		Cursor curRow = DataStore.fetchRow(StoreDataInSQLite.KEY_GRIDSIZE + " = " + intGridSize + " and " + StoreDataInSQLite.KEY_LEVEL + " = " + setLevelState.getCurrentLevel());
//		DataStore.close();
//
//		// Get Best Moves
//		if (curRow.getCount() == 0)
//			return "--";
//		else
//			return Integer.toString(curRow.getInt(curRow.getColumnIndex(StoreDataInSQLite.KEY_MOVES)));
//	}

	public static void foo(final Builder builder) {
		new Thread() {
			public void run() {
				builder.show();
			}
		}.start();
	}

	public static class MyOtherAlertDialog {

		public static Builder create(Context context) {
			final TextView message = new TextView(context);
			// i.e.: R.string.dialog_message =>
			// "Test this dialog following the link to dtmilano.blogspot.com"
			final SpannableString s = new SpannableString("test");
			// Linkify.addLinks(s, Linkify.WEB_URLS);
			message.setText(s);
			message.setMovementMethod(LinkMovementMethod.getInstance());

			Builder builder = new AlertDialog.Builder(context);
			builder.setCancelable(false); // disable back-button
			builder.setMessage(Html.fromHtml("test"));

			Spanned htmlTextOK = Html.fromHtml("<big>Next</big>");
			builder.setPositiveButton(htmlTextOK, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					dialog.cancel();
					dialog.dismiss();
					// android.os.SystemClock.sleep(3000);
					OnClickListener();
				}
			});

			builder.show();
			return builder;
			// return new AlertDialog.Builder(context).setTitle("Title test").setCancelable(false).setIcon(android.R.drawable.ic_dialog_info).setPositiveButton("Next", OnClickListener() ).setView(message).create();
		}

		private static android.content.DialogInterface.OnClickListener OnClickListener() {

			return null;
		}

	}

	@Override
	protected Dialog onCreateDialog(int id) {
		return super.onCreateDialog(id);

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

// Put in Tutorial for first time play
// x Put in SQL DB
// x Put in Sound Effects
// x Put in indication for Level Completed
// x Replace all AppConfig.intGridSize to setLevelState.getGridSize
// x Prevent Phone screen from turning off