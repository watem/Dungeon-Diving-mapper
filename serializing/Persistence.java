package modelv2.serializing;


import modelv2.Dungeon;
import modelv2.DungeonMap;
import modelv2.Modelv2Mapping;

public class Persistence {

  private static String filename = "dungeon.data";

  public static void save(Dungeon dungeon){
//    PersistenceObjectStream.serialize(dungeon);
  }

  public static Dungeon load() {
    PersistenceObjectStream.setFilename(filename);
    Dungeon dungeon = (Dungeon) PersistenceObjectStream.deserialize();
    // model cannot be loaded - create empty Block223
    if (dungeon == null) {
    	dungeon = new Dungeon();
    }
//    else {
//    	dungeon.reinitialize();
//    }
    Modelv2Mapping.setDungeon(dungeon);
    return dungeon;
  }

  public static void setFilename(String newFilename) {
		filename = newFilename;
	}
  
  public static void saveMap(DungeonMap m) {
	  
  }
}
