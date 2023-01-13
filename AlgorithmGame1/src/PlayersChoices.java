
public class PlayersChoices {
	// First player choice
	private int p1Choice;
	// Second player choice
	private int p2Choice;
	private int index;

	public PlayersChoices() {
	}

	public void setP1Choice(int p1Choice) {
		this.p1Choice = p1Choice;
	}

	public int getP1Choice() {
		return p1Choice;
	}

	public void setP2Choice(int p2Choice) {
		this.p2Choice = p2Choice;
	}

	public int getP2Choice() {
		return p2Choice;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public int getIndex() {
		return index;
	}

	@Override
	public String toString() {
		return "(" + p1Choice + "," + p2Choice + ") ";
	}
}
