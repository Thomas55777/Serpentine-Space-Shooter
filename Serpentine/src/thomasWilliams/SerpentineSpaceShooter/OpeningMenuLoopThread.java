package thomasWilliams.SerpentineSpaceShooter;

import android.graphics.Canvas;
import android.view.MotionEvent;

public class OpeningMenuLoopThread extends Thread {
	static final long FPS = 30;
	private OpeningMenu view;
	public static boolean running = false;
	private MainActivity PauseResume;
	private int intCounter = 0;
	private static Object mPauseLock = new Object();
	public static boolean mPaused = false;

	public OpeningMenuLoopThread(OpeningMenu view) {
		this.view = view;
		// mPauseLock = new Object();
		// mPaused = false;
	}

	public void setRunning(boolean run) {
		// Call this with false to override and end the game
		running = run;
	}

	@Override
	public void run() {
		// All Code goes inside the 'try' this will eliminate all exit loop
		// errors
		try {
			long ticksPS = 1000 / FPS; // Standard 30 Frames Per Second FPS
			long startTime;
			long sleepTime;
			while (running) { // This is the loop that carries the whole thread
				Canvas c = null;
				startTime = System.currentTimeMillis();
				try {
					c = view.getHolder().lockCanvas();
					synchronized (view.getHolder()) {
						// Every Loop Redraws the screen with OnDraw Canvas in OpeningMenu
						view.onDraw(c);
					}
				} finally {
					if (c != null) {
						// Makes the changes visible to the canvas
						view.getHolder().unlockCanvasAndPost(c);
					}
				}
				sleepTime = ticksPS - (System.currentTimeMillis() - startTime);
				try {
					if (sleepTime > 0)
						sleep(sleepTime);
					else
						sleep(10);
				} catch (Exception e) {
				}
				// This is for Pausing the GameLoopThread Only Resumes with NotifyAll()
				synchronized (mPauseLock) {
					// Skip one frame and then Pause
					if (intCounter >= 1)
						while (mPaused) {
							try {
								intCounter = 0;
								mPauseLock.wait();
							} catch (InterruptedException e) {
							}
						}
					if (mPaused) {
						intCounter++;
					}
				}
			}
		} catch (Exception e) {
			// Catch all errors
			// Exit Loop Error when user Exits via BackButton
		}
	}

	// Call this on pause
	public void onPause() {
		synchronized (mPauseLock) {
			mPaused = true;
		}
	}

	// Call this on resume
	public void onResume() {
		synchronized (mPauseLock) {
			mPaused = false;
			mPauseLock.notifyAll();
		}
	}
}