package dungeonMapping.application;

import dungeonMapping.controller.MapController;
import dungeonMapping.model.v1_3.DungeonMap;
import dungeonMapping.view.MainPage;
import dungeonMapping.serializing.Persistence;

public class MappingApplication {
	public static final String version = "1.1";
	private static DungeonMap d;

	public static void main(String[] args) {
		try {
			getDungeon();
		} catch (Exception e) {
			System.err.println("unable to load autosave");
			MapController.deleteMap(Persistence.defaultPath);
			getDungeon();
		}
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				new MainPage().setVisible(true);
			}
		});
	}

	public static void setDungeon(DungeonMap dungeon) {
		d = dungeon;
	}

	public static void save() {
		Persistence.save(d);
	}

	public static void quickSave() {
		Persistence.quickSave(d);
	}

	public static DungeonMap getDungeon() {
		if (d == null) {
			// load model
			d = Persistence.load();
			System.out.println("load done");
//		      Persistence.save(d);
		}
		return d;
	}

	public static DungeonMap resetDungeon() {
		d = Persistence.load();
		return d;
	}
}
