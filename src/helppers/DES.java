package helppers;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.JOptionPane;

public class DES {
	private Cipher cipher;
	private SecretKey secretKey;
	private static DES des = null;

	public DES() {
		try {
			this.cipher = Cipher.getInstance("DES");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		}
	}

	public static DES getInstance() {
		if (des == null) {
			des = new DES();
			return des;
		}
		return des;
	}

	public SecretKey createKey() {
		KeyGenerator generator;
		try {
			generator = KeyGenerator.getInstance("DES");
			generator.init(56);
			secretKey = generator.generateKey();
			return secretKey;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
	}

	public byte[] decypt(byte[] bytes) {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		System.out.println(secretKey.getEncoded().length);
		try {
			cipher.init(Cipher.DECRYPT_MODE, secretKey);
			CipherInputStream cipherInputStream = new CipherInputStream(new ByteArrayInputStream(bytes), cipher);
			byte[] buffered = new byte[1024];
			int byteRead = 0;
			while ((byteRead = cipherInputStream.read(buffered)) != -1) {
				byteArrayOutputStream.write(buffered, 0, byteRead);
			}
			byteArrayOutputStream.close();
			byteArrayOutputStream.flush();
			return byteArrayOutputStream.toByteArray();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Khóa không chính xác");
			return new byte[0];
		} catch (IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, e.getMessage());
			return new byte[0];
		}
	}

	public byte[] encrypt(byte[] bytes) {
		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		System.out.println(secretKey.getEncoded().length);
		try {
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);
			CipherOutputStream cipherOutputStream = new CipherOutputStream(byteArrayOutputStream, cipher);
			byte[] buffered = new byte[1024];
			int byteRead = 0;
			while ((byteRead = byteArrayInputStream.read(buffered)) != -1) {
				cipherOutputStream.write(buffered, 0, byteRead);
			}
			cipherOutputStream.flush();
			cipherOutputStream.close();
			byteArrayInputStream.close();
			byteArrayOutputStream.close();
			return byteArrayOutputStream.toByteArray();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
			return new byte[0];
		} catch (IOException e) {
			e.printStackTrace();
			return new byte[0];
		}
	}

	public byte[] convertObjectToByte(SecretKey sk) {
		return sk.getEncoded();
	}
	public SecretKey getSecrectKey() {
		return this.secretKey;
	}
	public void setSecrectKey(byte[] key) {
		SecretKey secretKeyClient = new SecretKeySpec(key, 0, key.length, "DES");
		this.secretKey= secretKeyClient;
	}
}
