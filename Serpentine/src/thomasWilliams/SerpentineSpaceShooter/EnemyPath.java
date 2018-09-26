package thomasWilliams.SerpentineSpaceShooter;

import java.util.Random;

public class EnemyPath {
	private int xRightBound;
	private int xLeftBound;
	private int yUpperBound;
	private int x;
	private int y;
	private int intEnemyFrameSpacing = 7;
	private int intEnemyX;
	private int intEnemyY;
	private int intEnemySpeedX;
	private int intEnemySpeedY;
	private int intFrameCount;
	private EnemiesDraw SpawnEnemies;
	private int xSpeed = (int) (AppConfig.intEnemySpeedX * GameView.dblScaleRatio);
	private int ySpeed = (int) (AppConfig.intEnemySpeedY * GameView.dblScaleRatio);
	private int[] aryFrameSpacing;

	public EnemyPath(int EnemySpawned, EnemiesDraw SpawnEnemies, int intEnemySize, int intCurrentLevel, EnemyPathGeneration enemyPathGeneration) {
		this.SpawnEnemies = SpawnEnemies;
		this.xRightBound = SpawnEnemies.xRightBound;
		this.xLeftBound = SpawnEnemies.xLeftBound;
		this.yUpperBound = SpawnEnemies.yUpperBound;
		this.x = SpawnEnemies.getEnemyX();
		this.y = SpawnEnemies.getEnemyY();

		intFrameCount = 0;

		aryFrameSpacing = new int[20];
		aryFrameSpacing[0] = 30;
		aryFrameSpacing[1] = 50;
		aryFrameSpacing[2] = 40;
		aryFrameSpacing[3] = 120;
		aryFrameSpacing[4] = 45;
		aryFrameSpacing[5] = 55;
		aryFrameSpacing[6] = 45;
		aryFrameSpacing[7] = 50;
		aryFrameSpacing[8] = 180;
		aryFrameSpacing[9] = 40;
		aryFrameSpacing[10] = 50;
		aryFrameSpacing[11] = 45;
		aryFrameSpacing[12] = 60;
		aryFrameSpacing[13] = 40;
		aryFrameSpacing[14] = 55;
		aryFrameSpacing[15] = 45;
		aryFrameSpacing[16] = 65;
		aryFrameSpacing[17] = 70;
		aryFrameSpacing[18] = 45;
		aryFrameSpacing[19] = 40;

		if (intCurrentLevel % 1 == 0) {
			// Set StoredPath for 5 Enemies
			if (EnemySpawned >= 0 && EnemySpawned <= 4) {
				// Get the Random Path generated on Creation
				int intRandomPath = enemyPathGeneration.getEnemyPathSet01();
				setStoredPaths(EnemySpawned, intRandomPath);
			}
			// Set StoredPath for 5 Enemies
			else if (EnemySpawned >= 5 && EnemySpawned <= 9) {
				// Get the Random Path generated on Creation
				int intRandomPath = enemyPathGeneration.getEnemyPathSet02();
				setStoredPaths(EnemySpawned, intRandomPath);
			}
			// Set StoredPath for 5 Enemies
			else if (EnemySpawned >= 10 && EnemySpawned <= 14) {
				// Get the Random Path generated on Creation
				int intRandomPath = enemyPathGeneration.getEnemyPathSet03();
				setStoredPaths(EnemySpawned, intRandomPath);
			}
			// Set StoredPath for 5 Enemies
			else if (EnemySpawned >= 15 && EnemySpawned <= 19) {
				// Get the Random Path generated on Creation
				int intRandomPath = enemyPathGeneration.getEnemyPathSet04();
				setStoredPaths(EnemySpawned, intRandomPath);
			}
			// Set StoredPath for 5 Enemies
			else if (EnemySpawned >= 20 && EnemySpawned <= 24) {
				// Get the Random Path generated on Creation
				int intRandomPath = enemyPathGeneration.getEnemyPathSet05();
				setStoredPaths(EnemySpawned, intRandomPath);
			}
			// Set StoredPath for 5 Enemies
			else if (EnemySpawned >= 25 && EnemySpawned <= 29) {
				// Get the Random Path generated on Creation
				int intRandomPath = enemyPathGeneration.getEnemyPathSet06();
				setStoredPaths(EnemySpawned, intRandomPath);
			}
			// Set StoredPath for 5 Enemies
			else if (EnemySpawned >= 30 && EnemySpawned <= 34) {
				// Get the Random Path generated on Creation
				int intRandomPath = enemyPathGeneration.getEnemyPathSet07();
				setStoredPaths(EnemySpawned, intRandomPath);
			}
			// Set StoredPath for 5 Enemies
			else if (EnemySpawned >= 35 && EnemySpawned <= 39) {
				// Get the Random Path generated on Creation
				int intRandomPath = enemyPathGeneration.getEnemyPathSet08();
				setStoredPaths(EnemySpawned, intRandomPath);
			}
			// Set StoredPath for 5 Enemies
			else if (EnemySpawned >= 40 && EnemySpawned <= 44) {
				// Get the Random Path generated on Creation
				int intRandomPath = enemyPathGeneration.getEnemyPathSet09();
				setStoredPaths(EnemySpawned, intRandomPath);
			}
			// Set StoredPath for 5 Enemies
			else if (EnemySpawned >= 45 && EnemySpawned <= 49) {
				// Get the Random Path generated on Creation
				int intRandomPath = enemyPathGeneration.getEnemyPathSet10();
				setStoredPaths(EnemySpawned, intRandomPath);
			}
			// Set StoredPath for 5 Enemies
			else if (EnemySpawned >= 50 && EnemySpawned <= 54) {
				// Get the Random Path generated on Creation
				int intRandomPath = enemyPathGeneration.getEnemyPathSet11();
				setStoredPaths(EnemySpawned, intRandomPath);
			}
			// Set StoredPath for 5 Enemies
			else if (EnemySpawned >= 55 && EnemySpawned <= 59) {
				// Get the Random Path generated on Creation
				int intRandomPath = enemyPathGeneration.getEnemyPathSet12();
				setStoredPaths(EnemySpawned, intRandomPath);
			}
			// Set StoredPath for 5 Enemies
			else if (EnemySpawned >= 60 && EnemySpawned <= 64) {
				// Get the Random Path generated on Creation
				int intRandomPath = enemyPathGeneration.getEnemyPathSet13();
				setStoredPaths(EnemySpawned, intRandomPath);
			}
			// Set StoredPath for 5 Enemies
			else if (EnemySpawned >= 65 && EnemySpawned <= 69) {
				// Get the Random Path generated on Creation
				int intRandomPath = enemyPathGeneration.getEnemyPathSet14();
				setStoredPaths(EnemySpawned, intRandomPath);
			}
			// Set StoredPath for 5 Enemies
			else if (EnemySpawned >= 70 && EnemySpawned <= 74) {
				// Get the Random Path generated on Creation
				int intRandomPath = enemyPathGeneration.getEnemyPathSet15();
				setStoredPaths(EnemySpawned, intRandomPath);
			}
			// Set StoredPath for 5 Enemies
			else if (EnemySpawned >= 75 && EnemySpawned <= 79) {
				// Get the Random Path generated on Creation
				int intRandomPath = enemyPathGeneration.getEnemyPathSet16();
				setStoredPaths(EnemySpawned, intRandomPath);
			}
			// Set StoredPath for 5 Enemies
			else if (EnemySpawned >= 80 && EnemySpawned <= 84) {
				// Get the Random Path generated on Creation
				int intRandomPath = enemyPathGeneration.getEnemyPathSet17();
				setStoredPaths(EnemySpawned, intRandomPath);
			}
			// Set StoredPath for 5 Enemies
			else if (EnemySpawned >= 85 && EnemySpawned <= 89) {
				// Get the Random Path generated on Creation
				int intRandomPath = enemyPathGeneration.getEnemyPathSet18();
				setStoredPaths(EnemySpawned, intRandomPath);
			}
			// Set StoredPath for 5 Enemies
			else if (EnemySpawned >= 90 && EnemySpawned <= 94) {
				// Get the Random Path generated on Creation
				int intRandomPath = enemyPathGeneration.getEnemyPathSet19();
				setStoredPaths(EnemySpawned, intRandomPath);
			}
			// Set StoredPath for 5 Enemies
			else if (EnemySpawned >= 95 && EnemySpawned <= 99) {
				// Get the Random Path generated on Creation
				int intRandomPath = enemyPathGeneration.getEnemyPathSet20();
				setStoredPaths(EnemySpawned, intRandomPath);
			}
		} else {
			StoredPathRandom(EnemySpawned);
		}
	}

	// Add Stored Path for wide gap (Lone Enemy)
	private void setStoredPaths(int EnemySpawned, int intRandomPath) {
		double dblCoefficientSpeed = intRandomPath / 100.0;
		double dblCoefficientBound = dblCoefficientSpeed;
		if (intRandomPath % 2 == 0) {
			dblCoefficientBound = 1 - dblCoefficientSpeed;
			dblCoefficientSpeed *= -1;
		}
		// Set the StoredPath to Follow
		if (intRandomPath % 3 == 0)
			StoredPathZigZag(EnemySpawned, (int) Math.round((xSpeed * dblCoefficientSpeed)), Math.abs(dblCoefficientBound));
		else if (intRandomPath % 2 == 0)
			StoredPathRandom(EnemySpawned);
		else {
			Random rndX = new Random();
			StoredPathForcedLine(EnemySpawned, rndX.nextInt(xSpeed * 2) - xSpeed, rndX.nextInt(xRightBound));
		}
	}

	private void StoredPathForcedLine(int EnemySpawned, int intSpeedX, double dblPositionX) {
		int intFrameCountStart = 0;
		intEnemyX = (int) dblPositionX;
		intEnemyY = yUpperBound;
		intEnemySpeedX = intSpeedX;
		intEnemySpeedY = ySpeed;
		// intFrameCountStart *= (int) EnemySpawned / 5;
		for (int i = 0; i <= (int) EnemySpawned / 5; i++)
			intFrameCountStart += aryFrameSpacing[i];
		intFrameCount = intFrameCountStart + (intEnemyFrameSpacing);
	}

	private void StoredPathZigZag(int EnemySpawned, int intSpeedX, double dblPositionX) {
		int intFrameCountStart = 0;
		intEnemyX = (int) (xRightBound * dblPositionX);
		intEnemyY = yUpperBound;
		intEnemySpeedX = intSpeedX;
		intEnemySpeedY = ySpeed;
		for (int i = 0; i <= (int) EnemySpawned / 5; i++)
			intFrameCountStart += aryFrameSpacing[i];
		int intEnemySpawnedInSet = EnemySpawned % 5;
		intFrameCount = intFrameCountStart + (intEnemyFrameSpacing * intEnemySpawnedInSet);
	}

	private void StoredPathRandom(int EnemySpawned) {
		int intFrameCountStart = 0;
		Random rndX = new Random();
		intEnemyX = rndX.nextInt(xRightBound);
		intEnemyY = yUpperBound;
		intEnemySpeedX = rndX.nextInt(xSpeed * 2) - xSpeed;
		intEnemySpeedY = ySpeed;
		for (int i = 0; i <= (int) EnemySpawned / 5; i++)
			intFrameCountStart += aryFrameSpacing[i];
		int intEnemySpawnedInSet = EnemySpawned % 5;
		intFrameCount = intFrameCountStart + (intEnemyFrameSpacing * intEnemySpawnedInSet);
	}

	public void DrawStoredPaths(int i) {
		// Nothing gets drawn until the frame count set during creation reaches the current Frame
		if (SpawnEnemies.intFrameCount == intFrameCount) {
			x = intEnemyX;
			y = intEnemyY;
			SpawnEnemies.setX(x);
			SpawnEnemies.setY(y);
		} else if (SpawnEnemies.intFrameCount > intFrameCount) {
			if (x < xLeftBound) {
				// intEnemySpeedX = Math.abs(intEnemySpeedX);
				intEnemySpeedX = Math.abs(intEnemySpeedX);
			} else if (x > xRightBound)
				intEnemySpeedX = -Math.abs(intEnemySpeedX);
			x = x + intEnemySpeedX;
			y = y + intEnemySpeedY;
			SpawnEnemies.setX(x);
			SpawnEnemies.setY(y);
		}
	}
}

// We Make each level go through the same EnemyPath but different Enemy Paths such as ZigZag will get called in a random order so each level will appear unique