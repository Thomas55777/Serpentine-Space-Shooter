package thomasWilliams.SerpentineSpaceShooter;

import java.util.Date;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.TextView;

import com.pad.android.iappad.AdController;
import com.pad.android.listener.AdListener;

public class AdvacedOverlay extends Activity {
	private boolean booUnlocked = false;
	private AdController myController;
	private TextView txtAdvancedOverlayClicked;
	private CountDownTimer countDownTimer;
	// double dblMillisecs = 1.0 * 1000 * 60 * 60 * 24 * 15695;
	double dblMillisecs = new Date().getTime() + (1.0 * 1000 * AppConfig.intSecondsCountDown);
	long lngMillisecs = (long) dblMillisecs;// 1356048000000L;
	private String strDays;
	private String strHours;
	private String strMinutes;
	private String strSeconds;
	private String strMilliseconds;
	private long lngTimeCountdown;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.advanced_overlay);

		// Allow user to exit with back button; In case they dont have Internet for the Ad to display
		booUnlocked = true;

		txtAdvancedOverlayClicked = (TextView) findViewById(R.id.txtAdvancedOverlayClicked);

		CallSetTimer();

		// LeadBolt Overlay Ad
		DisplayMetrics dm = new DisplayMetrics();
		this.getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenWidth = dm.widthPixels;
		String myAdId = AppConfig.strAdvancedOverlay320x480; // (320x800)
		if (screenWidth >= 480) {
			myAdId = AppConfig.strAdvancedOverlay480x800; // (480x800)
		}

		myController = new AdController(this, myAdId, new AdListener() {
			@Override
			public void onAdProgress() {
			}

			@Override
			public void onAdLoaded() {
				// once loaded – don’t show again to user
				// myController.hideAd();
			}

			@Override
			public void onAdFailed() {
				// if ad fails to load – launch the main activity
				launchMain();
			}

			@Override
			public void onAdCompleted() {
				// if capture form completed successfully,
				// don't show ad again & launch main activity
				// myController.hideAd();
				launchMain();
			}

			@Override
			public void onAdClosed() {
				// if user closes ad – launch main activity
				launchMain();
			}

			@Override
			public void onAdClicked() {
				// SLEEP 4 SECONDS HERE ...
				Handler handler = new Handler();
				handler.postDelayed(new Runnable() {

					public void run() {
						countDownTimer.cancel();
						// Not doing this anymore because Google now requires the X to be shown in the Ad
						// CallUnlockScreen();
					}
				}, 4000);
			}

			@Override
			public void onAdAlreadyCompleted() {
				// triggered if user has already completed the capture
				// form, so just launch main activity
				launchMain();
			}

			@Override
			public void onAdHidden() {
				// ad set to be hidden, so just launch main activity
				launchMain();
			}

			@Override
			public void onAdPaused() {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAdResumed() {
				// TODO Auto-generated method stub

			}

		});
		myController.setAsynchTask(true);
		myController.loadAd();

		TextView txtLevelsPlayed = (TextView) findViewById(R.id.txtLevelsPlayed);
		TextView txtAdvancedOverlay = (TextView) findViewById(R.id.txtAdvancedOverlay);

		txtLevelsPlayed.setText("You have played through " + AppConfig.intOverlayFrequency + " levels!");
		txtAdvancedOverlay.setText("Please help support by viewing the Ad below:");
	}

	private void launchMain() {
		finish();
		android.os.Process.killProcess(android.os.Process.myPid());
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// if a key as been pressed down AND this is the key BACK
		if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_BACK)) {
			if (booUnlocked) {
				finish();
				android.os.Process.killProcess(android.os.Process.myPid());
			}
			return true;
		}
		if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_HOME)) {
			return true;
		}
		if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_MENU)) {
			return true;
		}
		if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_SEARCH)) {
			return true;
		}
		// else
		return false;
	}

	private void CallSetTimer() {
		countDownTimer = new CountDownTimer(lngMillisecs - new Date().getTime(), 500) {
			public void onTick(long lngMillisecs) {
				CalculateTime();
				UpdateScreen(strDays, strHours, strMinutes, strSeconds, strMilliseconds);
			}

			public void onFinish() {
				// Not doing this anymore because Google now requires the X to be shown in the Ad
				// CallUnlockScreen();
			}
		}.start();
	}

	public void CalculateTime() {
		Date date = new Date();
		long lngTimeNow = date.getTime();
		lngTimeCountdown = lngMillisecs - lngTimeNow;

		double dblDays = (lngTimeCountdown / (1.0 * 1000 * 60 * 60 * 24));
		int intDays = (int) dblDays;

		double dblHours = (dblDays - intDays) * 24;
		int intHours = (int) dblHours;

		double dblMinutes = (dblHours - intHours) * 60;
		int intMinutes = (int) dblMinutes;

		double dblSeconds = (dblMinutes - intMinutes) * 60;
		int intSeconds = (int) dblSeconds;

		double dblMilliseconds = (dblSeconds - intSeconds) * 1000;
		int intMilliseconds = (int) dblMilliseconds;

		strDays = String.valueOf(intDays);
		strHours = String.valueOf(intHours);
		strMinutes = String.valueOf(intMinutes);
		strSeconds = String.valueOf(intSeconds);
		strMilliseconds = String.valueOf(intMilliseconds);

		while (strHours.length() < 2) {
			strHours = "0" + strHours;
		}
		while (strMinutes.length() < 2) {
			strMinutes = "0" + strMinutes;
		}
		while (strSeconds.length() < 2) {
			strSeconds = "0" + strSeconds;
		}
		while (strMilliseconds.length() < 3) {
			strMilliseconds = "0" + strMilliseconds;
		}
	}

	private void UpdateScreen(String strDays, String strHours, String strMinutes, String strSeconds, String strMilliseconds) {
		txtAdvancedOverlayClicked.setVisibility(1);
		// txtAdvancedOverlayClicked.setText("The screen will unlock in " + strSeconds + " seconds.");
		txtAdvancedOverlayClicked.setText("View the Ad or Tap the 'X' to Continue GamePlay");
	}

	private void CallUnlockScreen() {
		booUnlocked = true;
		txtAdvancedOverlayClicked.setVisibility(1);
		txtAdvancedOverlayClicked.setText("Unlocked!  Press the back button to continue");
	}
}