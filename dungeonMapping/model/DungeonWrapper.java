package dungeonMapping.model;

import java.io.Serializable;
import dungeonMapping.model.v1_2.DungeonMap;

public class DungeonWrapper implements Serializable {

	private static final long serialVersionUID = -6017107472768512187L;
	public final Object dungeon;
	private String version = "v1.2";
	public static final String currentVersion = "v1.2";

	public static int getMajorVersion(String version) {
		version = version.replaceFirst("v", "");
		String versions[] = version.split("\\.");
		return Integer.parseInt(versions[0]);
	}

	public static int getMinorVersion(String version) {
		String versions[] = version.split("\\.");
		return Integer.parseInt(versions[1]);
	}

	public DungeonWrapper(DungeonMap dungeon) {
		super();
		this.dungeon = dungeon;
	}

	public DungeonWrapper(DungeonMap dungeon, String version) {
		super();
		this.dungeon = dungeon;
		this.version = version;
	}

	public String getVersion() {
		return version;
	}
}
