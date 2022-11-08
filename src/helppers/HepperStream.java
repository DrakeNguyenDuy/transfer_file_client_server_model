package helppers;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class HepperStream {
	public static byte[] getByteStream(InputStream inputStream) {
		byte[] buffered = new byte[1024];
		int byteRead = 0;
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
		try {
			while ((byteRead = bufferedInputStream.read(buffered)) != -1) {
				byteArrayOutputStream.write(buffered, 0, byteRead);
				if (buffered.length > byteRead)// to stop loop infinitive
					break;
			}
			byteArrayOutputStream.flush();
			return byteArrayOutputStream.toByteArray();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new byte[0];
		}
	}

	public static void saveFile(String nameFile, byte[] arrayData) {
		File file = new File("src/store/", nameFile);
		try {
			FileOutputStream fileOutputStream = new FileOutputStream(file);
			fileOutputStream.write(arrayData, 0, arrayData.length);
			fileOutputStream.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
