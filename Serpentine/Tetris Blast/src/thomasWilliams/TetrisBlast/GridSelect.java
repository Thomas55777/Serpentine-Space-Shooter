package thomasWilliams.TetrisBlast;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.LinearLayout;

public class GridSelect extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.grid_select);

		final int intClickColor = 0x8800FFFF; // Cyan

		final LinearLayout linTutorial = (LinearLayout) findViewById(R.id.gridTutorial);
		linTutorial.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				linTutorial.setBackgroundColor(intClickColor);
				StartGameMenu(-1);
				// Set Tutorial GridSize
				// int intGridSize = 5;
				// CommitGridSize(intGridSize);

				// Start Tutorial
				// GameMenu gameMenu = new GameMenu();
				// gameMenu.LoadLevel(-1);
				return false;
			}
		});
		final LinearLayout linGrid5 = (LinearLayout) findViewById(R.id.grid5);
		linGrid5.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				linGrid5.setBackgroundColor(intClickColor);
				StartGameMenu(5);
				return false;
			}
		});
		final LinearLayout linGrid6 = (LinearLayout) findViewById(R.id.grid6);
		linGrid6.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				linGrid6.setBackgroundColor(intClickColor);
				StartGameMenu(6);
				return false;
			}
		});
		final LinearLayout linGrid7 = (LinearLayout) findViewById(R.id.grid7);
		linGrid7.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				linGrid7.setBackgroundColor(intClickColor);
				StartGameMenu(7);
				return false;
			}
		});
		final LinearLayout linGrid8 = (LinearLayout) findViewById(R.id.grid8);
		linGrid8.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				linGrid8.setBackgroundColor(intClickColor);
				StartGameMenu(8);
				return false;
			}
		});
		final LinearLayout linGrid9 = (LinearLayout) findViewById(R.id.grid9);
		linGrid9.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				linGrid9.setBackgroundColor(intClickColor);
				StartGameMenu(9);
				return false;
			}
		});
		final LinearLayout linGrid10 = (LinearLayout) findViewById(R.id.grid10);
		linGrid10.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				linGrid10.setBackgroundColor(intClickColor);
				StartGameMenu(10);
				return false;
			}
		});
		linGrid7.setVisibility(View.GONE);
		linGrid8.setVisibility(View.GONE);
		linGrid9.setVisibility(View.GONE);
		linGrid10.setVisibility(View.GONE);
	}

	private void StartGameMenu(int intGridSize) {
		CommitGridSize(intGridSize);

		Intent entGameMenu = new Intent(this, GameMenu.class);
		entGameMenu.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		startActivity(entGameMenu);
		finish();
	}

	private void CommitGridSize(int intGridSize) {
		SharedPreferences app_preferences = PreferenceManager.getDefaultSharedPreferences(this);
		SharedPreferences.Editor editor = app_preferences.edit();
		editor.putInt("intGridSize", intGridSize);
		editor.commit();
	}
}
