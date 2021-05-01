package modelv2.serializing;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class PersistenceObjectStream {

	public static String filename = "data.dungeon";

	public static void serialize(Object object) {
//		FileOutputStream fileOut;
//		try {
//			fileOut = new FileOutputStream(filename);
//			ObjectOutputStream out = new ObjectOutputStream(fileOut);
//			out.writeObject(object);
//			out.close();
//			fileOut.close();
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw new RuntimeException("Could not save data to file '" + filename + "'.");
//		}

	}

	public static Object deserialize() {
		Object o = null;
		ObjectInputStream in;
		FileInputStream fileIn;
		try {
			fileIn = new FileInputStream(filename);
			in = new ObjectInputStream(fileIn);
			o = in.readObject();
			in.close();
			fileIn.close();
		} catch (Exception e) {
			o = null;
		}
		return o;
	}

	public static void setFilename(String newFilename) {
		filename = newFilename;
	}

}
