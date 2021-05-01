package dungeonMapping;

import java.io.Serializable;

public class Coords implements Serializable {
	private static final long serialVersionUID = -8842046141327193226L;

	public Coords(int x, int y) {
		this.x=x;
		this.y=y;
	}

	public int x,y;
}
