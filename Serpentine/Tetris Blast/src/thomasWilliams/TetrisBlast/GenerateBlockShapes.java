package thomasWilliams.TetrisBlast;

import java.util.List;
import java.util.Random;

import android.graphics.Bitmap;

public class GenerateBlockShapes {

	private GameView gameView;
	private List<DrawBlocks> lstDrawMovingBlocks;

	public GenerateBlockShapes(GameView gameView, List<DrawBlocks> lstDrawMovingBlocks, int intRnd,float fltLineSpacing) {
		this.gameView = gameView;
		this.lstDrawMovingBlocks = lstDrawMovingBlocks;
		float fltLeftBound = gameView.getGridBounds()[0];
		float fltUpperBound = gameView.getGridBounds()[1];
		
		Bitmap[] bmpBlocks = gameView.getBlocks();
		Bitmap bmpBlockGreen = bmpBlocks[0];
		Bitmap bmpBlockYellow = bmpBlocks[1];
		Bitmap bmpBlockPurple = bmpBlocks[2];
		Bitmap bmpBlockWhite = bmpBlocks[3];
		Bitmap bmpBlockRed = bmpBlocks[4];
		Bitmap bmpBlockCyan = bmpBlocks[5];
		Bitmap bmpBlockBlue = bmpBlocks[6];

		if (intRnd == 0) {
			// The Square
			Bitmap bmpBlock = bmpBlockGreen;
			Random rndBlockX = new Random();
			int intBlockX = rndBlockX.nextInt(AppConfig.intGridCountX - 1);

			AddBlock(bmpBlock, (int) (fltLeftBound + (fltLineSpacing * intBlockX) + fltLineSpacing * 0), (int) (fltUpperBound - fltLineSpacing * 1));
			AddBlock(bmpBlock, (int) (fltLeftBound + (fltLineSpacing * intBlockX) + fltLineSpacing * 1), (int) (fltUpperBound - fltLineSpacing * 1));
			AddBlock(bmpBlock, (int) (fltLeftBound + (fltLineSpacing * intBlockX) + fltLineSpacing * 0), (int) (fltUpperBound - fltLineSpacing * 0));
			AddBlock(bmpBlock, (int) (fltLeftBound + (fltLineSpacing * intBlockX) + fltLineSpacing * 1), (int) (fltUpperBound - fltLineSpacing * 0));
		} else if (intRnd == 1) {
			// The Reverse L Shape - Sideways
			Bitmap bmpBlock = bmpBlockRed;
			Random rndBlockX = new Random();
			int intBlockX = rndBlockX.nextInt(AppConfig.intGridCountX - 2);

			AddBlock(bmpBlock, (int) (fltLeftBound + (fltLineSpacing * intBlockX) + fltLineSpacing * 0), (int) (fltUpperBound - fltLineSpacing * 1));
			AddBlock(bmpBlock, (int) (fltLeftBound + (fltLineSpacing * intBlockX) + fltLineSpacing * 1), (int) (fltUpperBound - fltLineSpacing * 1));
			AddBlock(bmpBlock, (int) (fltLeftBound + (fltLineSpacing * intBlockX) + fltLineSpacing * 2), (int) (fltUpperBound - fltLineSpacing * 1));
			AddBlock(bmpBlock, (int) (fltLeftBound + (fltLineSpacing * intBlockX) + fltLineSpacing * 2), (int) (fltUpperBound - fltLineSpacing * 0));

		} else if (intRnd == 2) {
			// The L Shape - SideWays
			Bitmap bmpBlock = bmpBlockYellow;
			Random rndBlockX = new Random();
			int intBlockX = rndBlockX.nextInt(AppConfig.intGridCountX - 2);

			AddBlock(bmpBlock, (int) (fltLeftBound + (fltLineSpacing * intBlockX) + fltLineSpacing * 0), (int) (fltUpperBound - fltLineSpacing * 1));
			AddBlock(bmpBlock, (int) (fltLeftBound + (fltLineSpacing * intBlockX) + fltLineSpacing * 1), (int) (fltUpperBound - fltLineSpacing * 1));
			AddBlock(bmpBlock, (int) (fltLeftBound + (fltLineSpacing * intBlockX) + fltLineSpacing * 2), (int) (fltUpperBound - fltLineSpacing * 1));
			AddBlock(bmpBlock, (int) (fltLeftBound + (fltLineSpacing * intBlockX) + fltLineSpacing * 0), (int) (fltUpperBound - fltLineSpacing * 0));
		} else if (intRnd == 3) {
			// The 4 in a row Line - SideWays
			Bitmap bmpBlock = bmpBlockBlue;
			Random rndBlockX = new Random();
			int intBlockX = rndBlockX.nextInt(AppConfig.intGridCountX - 3);

			AddBlock(bmpBlock, (int) (fltLeftBound + (fltLineSpacing * intBlockX) + fltLineSpacing * 0), (int) (fltUpperBound - fltLineSpacing * 1));
			AddBlock(bmpBlock, (int) (fltLeftBound + (fltLineSpacing * intBlockX) + fltLineSpacing * 1), (int) (fltUpperBound - fltLineSpacing * 1));
			AddBlock(bmpBlock, (int) (fltLeftBound + (fltLineSpacing * intBlockX) + fltLineSpacing * 2), (int) (fltUpperBound - fltLineSpacing * 1));
			AddBlock(bmpBlock, (int) (fltLeftBound + (fltLineSpacing * intBlockX) + fltLineSpacing * 3), (int) (fltUpperBound - fltLineSpacing * 1));
		} else if (intRnd == 4) {
			// The Reverse Z Shape - SideWays
			Bitmap bmpBlock = bmpBlockPurple;
			Random rndBlockX = new Random();
			int intBlockX = rndBlockX.nextInt(AppConfig.intGridCountX - 2);

			AddBlock(bmpBlock, (int) (fltLeftBound + (fltLineSpacing * intBlockX) + fltLineSpacing * 1), (int) (fltUpperBound - fltLineSpacing * 1));
			AddBlock(bmpBlock, (int) (fltLeftBound + (fltLineSpacing * intBlockX) + fltLineSpacing * 2), (int) (fltUpperBound - fltLineSpacing * 1));
			AddBlock(bmpBlock, (int) (fltLeftBound + (fltLineSpacing * intBlockX) + fltLineSpacing * 1), (int) (fltUpperBound - fltLineSpacing * 0));
			AddBlock(bmpBlock, (int) (fltLeftBound + (fltLineSpacing * intBlockX) + fltLineSpacing * 0), (int) (fltUpperBound - fltLineSpacing * 0));
		} else if (intRnd == 5) {
			// The Z Shape - SideWays
			Bitmap bmpBlock = bmpBlockWhite;
			Random rndBlockX = new Random();
			int intBlockX = rndBlockX.nextInt(AppConfig.intGridCountX - 2);

			AddBlock(bmpBlock, (int) (fltLeftBound + (fltLineSpacing * intBlockX) + fltLineSpacing * 0), (int) (fltUpperBound - fltLineSpacing * 1));
			AddBlock(bmpBlock, (int) (fltLeftBound + (fltLineSpacing * intBlockX) + fltLineSpacing * 1), (int) (fltUpperBound - fltLineSpacing * 1));
			AddBlock(bmpBlock, (int) (fltLeftBound + (fltLineSpacing * intBlockX) + fltLineSpacing * 1), (int) (fltUpperBound - fltLineSpacing * 0));
			AddBlock(bmpBlock, (int) (fltLeftBound + (fltLineSpacing * intBlockX) + fltLineSpacing * 2), (int) (fltUpperBound - fltLineSpacing * 0));
		} else if (intRnd == 6) {
			// The T Shape
			Bitmap bmpBlock = bmpBlockCyan;
			Random rndBlockX = new Random();
			int intBlockX = rndBlockX.nextInt(AppConfig.intGridCountX - 2);

			AddBlock(bmpBlock, (int) (fltLeftBound + (fltLineSpacing * intBlockX) + fltLineSpacing * 0), (int) (fltUpperBound - fltLineSpacing * 1));
			AddBlock(bmpBlock, (int) (fltLeftBound + (fltLineSpacing * intBlockX) + fltLineSpacing * 1), (int) (fltUpperBound - fltLineSpacing * 1));
			AddBlock(bmpBlock, (int) (fltLeftBound + (fltLineSpacing * intBlockX) + fltLineSpacing * 2), (int) (fltUpperBound - fltLineSpacing * 1));
			AddBlock(bmpBlock, (int) (fltLeftBound + (fltLineSpacing * intBlockX) + fltLineSpacing * 1), (int) (fltUpperBound - fltLineSpacing * 0));
		}
	}

	private void AddBlock(Bitmap bmpBlock, int intX, int intY) {
		DrawBlocks drawBlocks = new DrawBlocks(gameView, bmpBlock);
		drawBlocks.setX(intX);
		drawBlocks.setY(intY);
		lstDrawMovingBlocks.add(drawBlocks);
	}
}
