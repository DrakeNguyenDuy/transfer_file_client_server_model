package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.PublicKey;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;

import helppers.DES;
import helppers.HepperStream;
import helppers.RSA;
import helppers.RSAKey;

public class OneConnection extends Thread {
	private Socket socket;
	private DataInputStream dataInputStream;
	private DataOutputStream dataOutputStream;
	private RSAKey rsaKey;
	private RSA rsa;

	public OneConnection(Socket socket) {
		try {
			this.socket = socket;
			dataInputStream = new DataInputStream(this.socket.getInputStream());
			dataOutputStream = new DataOutputStream(this.socket.getOutputStream());
			rsaKey = new RSAKey();
			rsaKey.prepairKey();
			rsa = new RSA();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		try {
			PublicKey key = rsaKey.getPublicKey();
			dataOutputStream.writeUTF(Base64.getEncoder().encodeToString(key.getEncoded()));
			dataOutputStream.flush();
			String keyClient = dataInputStream.readUTF();
			DES des = new DES();
			RSA rsa = new RSA();
			byte[] dKey = rsa.decrypt(Base64.getDecoder().decode(keyClient.getBytes()), rsaKey.getPrivateKey());
			des.setSecrectKey(dKey);
			while (true) {
				String nameFile = dataInputStream.readUTF();
				if (nameFile.equalsIgnoreCase("QUIT"))
					break;
				File file = new File("src/store", nameFile);
				String byteFileString = dataInputStream.readUTF();
				byte[] byteFileDe = des.decypt(Base64.getDecoder().decode(byteFileString.getBytes()));
				System.out.println(new String(byteFileDe));
				HepperStream.saveFile(nameFile, byteFileDe);
			}
		} catch (ClassCastException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
