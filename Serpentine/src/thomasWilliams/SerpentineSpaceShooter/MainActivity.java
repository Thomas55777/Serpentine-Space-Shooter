package thomasWilliams.SerpentineSpaceShooter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;

import com.pad.android.iappad.AdController;

public class MainActivity extends Activity {
	private GameView gameView;
	private GameLoopThread gamePauseResumeFinish;
	private OpeningMenu openingMenu;
	private SetLevelState setLevelState;
	private boolean booShowOpeningScreen = true;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.main);

		// setLevelState = new SetLevelState();
		CallOpeningMenu();

		if (!booShowOpeningScreen)
			CallGameView();
		// finish();
		// System.exit(0);

		// WebView webview = new WebView(this);
		// setContentView(webview);
		// webview.loadUrl("http://ad.leadboltads.net/show_app_wall?section_id=449091827");

		// String url = "http://ad.leadboltads.net/show_app_wall?section_id=449091827"; // YOUR LEADBOLT APPWALL ADRESS HERE!!
		// Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
		// startActivity(intent);

		// Load LeadBolt App Wall
		AdController myController = new AdController(this, AppConfig.strAppWall);
		myController.loadAd();

		// Load LeadBolt Push Notification
		if (AppConfig.booLoadPushNotification) {
			com.pad.android.xappad.AdController nCont = new com.pad.android.xappad.AdController(getApplicationContext(), AppConfig.strPushNotification);
			nCont.loadNotification();
		}

		// myController = new AdController(getApplicationContext(), "MY_LB_SECTION_ID");
		// myController.loadNotification();
	}

	private void CallOpeningMenu() {
		// openingMenu = new OpeningMenu(this);
		// setContentView(openingMenu);
		Intent entGameMenu = new Intent(this, GameMenu.class);
		// entGameMenu.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
		// entGameMenu.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
		startActivity(entGameMenu);
	}

	private void CallGameView() {
		// gameView = new GameView(this, setLevelState);
		// gamePauseResumeFinish = new GameLoopThread(gameView);
		// setContentView(gameView);
	}

	@Override
	public void onAttachedToWindow() {
		// Disables the HomeKey
		this.getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD_DIALOG);
		super.onAttachedToWindow();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// if a key as been pressed down AND this is the key BACK
		if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_BACK)) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			// builder.setTitle("Test"); //IF YOU WANT TO SET SEPARATE TITLE
			// builder.setIcon(R.drawable.ic_launcher); // ICON FOR YOUR ALERTDIALOG
			builder.setCancelable(false); // disable back-button
			builder.setMessage(Html.fromHtml("Before you leave please help to support this free game.<br /><br />" + "<small>Thank you for playing!</small>"));
			Spanned htmlText = Html.fromHtml("<small>Top</small>" + "<br />" + "<small>Advertised</small>" + "<br />" + "<normal>Apps</normal>");
			builder.setPositiveButton("Top Apps", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					String url = "http://ad.leadboltads.net/show_app_wall?section_id=" + AppConfig.strAppWall; // YOUR LEADBOLT APPWALL ADRESS HERE!!
					Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
					startActivity(intent);
				}
			});
			builder.setNeutralButton("Quit", // Quit-button
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							finish();
							System.exit(0);
						}
					});
			builder.setNegativeButton("Cancel", // Cancel-button
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							dialog.cancel();
						}
					});
			builder.show();
			return true;

			// finish();
			// System.exit(0);
			// return true;
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
		// CallGameView();
		return true;
	}
}