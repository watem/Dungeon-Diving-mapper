package dungeonMapping.serializing;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class PersistenceObjectStream {

	public static void serialize(Object object, File file) {
		FileOutputStream fileOut;
		try {
			fileOut = new FileOutputStream(file);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(object);
			out.close();
			fileOut.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Could not save data to file '" + file.getName() + "'.");
		}

	}

	public static Object deserialize(File file) {
		Object o = null;
		ObjectInputStream in;
		FileInputStream fileIn;
		try {
			fileIn = new FileInputStream(file);
			in = new ObjectInputStream(fileIn);
			o = in.readObject();
			in.close();
			fileIn.close();
		} catch (Exception e) {
			o = null;
		}
		return o;
	}
}