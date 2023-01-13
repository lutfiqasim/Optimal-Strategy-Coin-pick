import java.util.Scanner;

public class OptimalStratigyForAGame {

	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		System.out.println("Enter Number of coins");
		int length = input.nextInt();
		int[] coins = new int[length];
		for (int i = 0; i < length; i++) {
			System.out.print("Enter value of coin at" + (i + 1) + ": ");
			coins[i] = input.nextInt();
		}
		PlayersChoices[][] moves = findMovesToOptimalSolution(coins);
		for (int i = 0; i < moves.length; i++) {
			for (int j = 0; j < moves.length; j++) {
				System.out.print(moves[i][j]);
			}
			System.out.println("\n___________________________");
		}
		printSteps(coins, moves);
	}

	// method to build DP table In a Bottom up approach
	// Each turn In DP has two values first player choice which is the best choice
	// for that current index and second player choice
	// Eg: for DP[1,1] first p1 choice = value[0], p2 choice is =0 since we only
	// have one value at those indexes
	private static PlayersChoices[][] findMovesToOptimalSolution(int[] coins) {
		PlayersChoices[][] DP = new PlayersChoices[coins.length][coins.length];
		for (int i = 0; i < DP.length; i++) {// Initially definition ofDynamic programming table
			for (int j = 0; j < DP[i].length; j++) {
				DP[i][j] = new PlayersChoices();
			}
		}
		// Diagonal of DP is equal to the index at i=j
		// since it represent a game of only one value where first player would get
		// coin[i] and
		// second gets value of zero since no more coins are left
		for (int i = 0; i < coins.length; i++) {
			DP[i][i].setP1Choice(coins[i]);
			// index at which value was taken from
			DP[i][i].setIndex(i);
		}
		// looping diagonally through DP
		// where k is the columns setter as to move to next cell through diagonal
		// we increment rows counter, and increment columns counter each by one
		// and each time the number of cells we need to loop through is decreased
		// so k is to keep us in range for the length of diagonal
		for (int k = 2; k <= coins.length; k++) {
			for (int rows = 0; rows <= coins.length - k; rows++) {// holds rows counter of current diagonal
				int column = rows + k - 1;// holds columns counter of current diagonal
				if (coins[rows] + DP[rows + 1][column].getP2Choice() > coins[column]
						+ DP[rows][column - 1].getP2Choice()) {
					DP[rows][column].setP1Choice(coins[rows] + DP[rows + 1][column].getP2Choice());
					DP[rows][column].setP2Choice(DP[rows + 1][column].getP1Choice());
					DP[rows][column].setIndex(rows);
				} else {
					DP[rows][column].setP1Choice(coins[column] + DP[rows][column - 1].getP2Choice());
					DP[rows][column].setP2Choice(DP[rows][column - 1].getP1Choice());
					DP[rows][column].setIndex(column);
				}
			}
		}
		return DP;
	}

	public static void printSteps(int[] coins, PlayersChoices[][] DP) {
		int rows = 0;
		int columns = coins.length - 1;
		int step;
		for (int k = 0; k < coins.length; k++) {// loops through all coins which is equal to rows length of DP
			step = DP[rows][columns].getIndex();
			// this is the value of pick and its index
			System.out.print("value: " + coins[step] + " " + "index: " + step + " ");
			if (step <= rows) {// coin was taken from left side of array
				rows = rows + 1;
			} else {// coin was taken from right side of array
				columns = columns - 1;
			}
		}
	}
}
