package modelv2;

import modelv2.serializing.Persistence;

public class Modelv2Mapping {
	private static Dungeon d;
	public static void main(String[] args) {
		getDungeon();
		java.awt.EventQueue.invokeLater(new Runnable() {
	        public void run() {
	          new Model2Page().setVisible(true);
	        }
	    });
	}

	public static void setDungeon(Dungeon dungeon) {
		d=dungeon;
	}
	public static void save() {
		Persistence.save(d);
//		for(DungeonMap m:d.maps) {
//			Persistence.saveMap(m);
//		}
	}
	 public static Dungeon getDungeon() {
		    if (d == null) {
		      // load model
		      d = Persistence.load();
		    }
		    return d;
		  }

		  public static Dungeon resetBlock223(){
		    d = Persistence.load();
		    return d;
		  }
}

