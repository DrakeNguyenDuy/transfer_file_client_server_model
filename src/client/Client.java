package client;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Scanner;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;

import helppers.DES;
import helppers.HepperStream;
import helppers.RSA;
import helppers.RSAKey;

public class Client {
	private Socket socket;
	private DataInputStream dataInputStream;
	private DataOutputStream dataOutputStream;
	private RSA rsa;

	public Client(Socket socket) {
		this.socket = socket;
		try {
			dataInputStream = new DataInputStream(this.socket.getInputStream());
			dataOutputStream = new DataOutputStream(this.socket.getOutputStream());
			this.rsa = new RSA();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void run() throws IOException, InvalidKeySpecException, NoSuchAlgorithmException, InvalidKeyException,
			IllegalBlockSizeException, ClassCastException, BadPaddingException {
		String s = dataInputStream.readUTF();
		PublicKey keyServer = KeyFactory.getInstance("RSA")
				.generatePublic(new X509EncodedKeySpec(Base64.getDecoder().decode(s)));
		DES des = new DES();
		RSA rsa = new RSA();
		des.createKey();
		byte[] eKey = rsa.encrypt(des.getSecrectKey().getEncoded(), keyServer);
		dataOutputStream.writeUTF(Base64.getEncoder().encodeToString(eKey));
		dataOutputStream.flush();
		Scanner scanner = new Scanner(System.in);
		while (true) {
			System.out.println("Nhap duong dan file");
			String path = scanner.nextLine();
			if (path.equalsIgnoreCase("QUIT")) {
				dataOutputStream.writeUTF("QUIT");
				dataOutputStream.flush();
				break;
			}
			File file = new File(path);
			FileInputStream fileInputStream = new FileInputStream(file);
			byte[] byteFile = HepperStream.getByteStream(fileInputStream);
			byte[] byteFileEn = des.encrypt(byteFile);
			dataOutputStream.writeUTF(file.getName());
			dataOutputStream.flush();
			dataOutputStream.writeUTF(Base64.getEncoder().encodeToString(byteFileEn));
			dataOutputStream.flush();
		}
	}

	public static void main(String[] args)
			throws UnknownHostException, IOException, InvalidKeyException, InvalidKeySpecException,
			NoSuchAlgorithmException, IllegalBlockSizeException, ClassCastException, BadPaddingException {
		Client client = new Client(new Socket("127.0.0.1", 1378));
		client.run();
	}
}
