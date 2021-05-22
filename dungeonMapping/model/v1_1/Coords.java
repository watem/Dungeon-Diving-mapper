package dungeonMapping.model.v1_1;

import java.io.Serializable;

public class Coords implements Serializable {
	private static final long serialVersionUID = -8842046141327193226L;

	public Coords(int x, int y) {
		this.x=x;
		this.y=y;
	}

	public int x,y;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Coords other = (Coords) obj;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}
}
