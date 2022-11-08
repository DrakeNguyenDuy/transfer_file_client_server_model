package helppers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

public class RSAKey {
	private KeyPair keyPair;
	private PublicKey publicKey;
	private PrivateKey privateKey;

	public void prepairKey() {
		File filePublicKey = new File("src/server/public.txt");
		if (filePublicKey.length() == 0) {
			createKey(2048);
		} else {
			loadKey();
		}
	}

	public void createKey(int keySize) {
		try {
			KeyPairGenerator generatorKeyPairGenerator = KeyPairGenerator.getInstance("RSA");
			generatorKeyPairGenerator.initialize(keySize);
			keyPair = generatorKeyPairGenerator.generateKeyPair();
			publicKey = keyPair.getPublic();
			privateKey = keyPair.getPrivate();
			saveKey();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}

	public PublicKey getPublicKey() {
		return publicKey;
	}

	public PrivateKey getPrivateKey() {
		return privateKey;
	}

	public void saveKey() {
		try {
			ObjectOutputStream keyPublic = new ObjectOutputStream(new FileOutputStream(new File("src/server/public.txt")));
			keyPublic.writeObject(publicKey);
			ObjectOutputStream keyPrivate = new ObjectOutputStream(new FileOutputStream(new File("src/server/private.txt")));
			keyPrivate.writeObject(privateKey);
			keyPublic.flush();
			keyPrivate.flush();
			keyPublic.close();
			keyPrivate.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void loadKey() {
		try {
			ObjectInputStream keyPublicOIP = new ObjectInputStream(
					new FileInputStream(new File("src/server/public.txt")));
			ObjectInputStream keyPrivateOIP = new ObjectInputStream(
					new FileInputStream(new File("src/server/private.txt")));
			Object pk = keyPublicOIP.readObject();
			Object prk = keyPrivateOIP.readObject();
			this.publicKey = (PublicKey) pk;
			this.privateKey = (PrivateKey) prk;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}
