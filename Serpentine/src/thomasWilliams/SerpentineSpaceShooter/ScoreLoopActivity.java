package thomasWilliams.SerpentineSpaceShooter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.scoreloop.client.android.core.controller.RequestController;
import com.scoreloop.client.android.core.controller.RequestControllerObserver;
import com.scoreloop.client.android.core.controller.ScoreController;
import com.scoreloop.client.android.core.model.Score;

public class ScoreLoopActivity extends Activity {
	// identifiers for our dialogues
	static final int DIALOG_PROGRESS = 0;
	static final int DIALOG_SUBMITTED = 1;
	static final int DIALOG_FAILED = 2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.scoreloop_options);

		// Get HighScore from Preferences
		SharedPreferences app_preferences = PreferenceManager.getDefaultSharedPreferences(this);
		final int intHighScore = app_preferences.getInt("intHighScore", 0);

		// "Leaderboard" button
		((Button) findViewById(R.id.btnLeaderboard)).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				startActivity(new Intent(ScoreLoopActivity.this, LeaderboardActivity.class));
			}
		});
		
		// "Profile" button
		((Button) findViewById(R.id.btnProfile)).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				startActivity(new Intent(ScoreLoopActivity.this, ProfileActivity.class));
			}
		});

		// set up click listener for the "Submit Score" button
		((Button) findViewById(R.id.btnSubmitScore)).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// this is where you should input your game's score
				// create the score object
				Score score = new Score(null, null);

				// Set the score values
				score.setResult((double) intHighScore);
				score.setLevel(1);
				score.setMinorResult(null);
				score.setMode(null);

				// set up an observer for our request
				RequestControllerObserver observer = new RequestControllerObserver() {

					@Override
					public void requestControllerDidFail(RequestController controller, Exception exception) {
						// something went wrong... possibly no internet connection
						dismissDialog(DIALOG_PROGRESS);
						showDialog(DIALOG_FAILED);
					}

					// this method is called when the request succeeds
					@Override
					public void requestControllerDidReceiveResponse(RequestController controller) {
						// remove the progress dialog
						dismissDialog(DIALOG_PROGRESS);
						// show the success dialog
						showDialog(DIALOG_SUBMITTED);
						// alternatively, you may want to return to the main screen
						// or start another round of the game at this point
					}
				};

				// with the observer, we can create a ScoreController to submit the score
				ScoreController scoreController = new ScoreController(observer);

				// show a progress dialog while we are submitting
				showDialog(DIALOG_PROGRESS);

				// this is the call that submits the score
				scoreController.submitScore(score);
				// please note that the above method will return immediately and reports to
				// the RequestControllerObserver when it's done/failed
			}
		});
	}

	// handler to create our dialogs
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOG_PROGRESS:
			return ProgressDialog.show(this, "", getString(R.string.submitting_your_score));
		case DIALOG_SUBMITTED:
			return (new AlertDialog.Builder(this)).setMessage(R.string.score_was_submitted).setTitle(R.string.scoreloop).setIcon(getResources().getDrawable(R.drawable.sl_icon_badge)).setPositiveButton(R.string.awesome, null).create();
		case DIALOG_FAILED:
			return (new AlertDialog.Builder(this)).setMessage(R.string.score_submit_error).setPositiveButton(R.string.too_bad, null).create();
		}
		return null;
	}
}
