package helppers;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class RSA {
	private Cipher cipher;
	private static RSA rsa = null;

	public RSA() {
		try {
			cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		}
	}

	public byte[] encrypt(byte[] byteText, Object objects) throws IllegalBlockSizeException, InvalidKeyException,
			ClassCastException, BadPaddingException, IOException {
		if (objects instanceof PublicKey) {
			cipher.init(Cipher.ENCRYPT_MODE, (PublicKey) objects);
		} else {
			cipher.init(Cipher.ENCRYPT_MODE, (PrivateKey) objects);
		}
		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteText);
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		byte[] buffered = new byte[245];
		int byteRead = 0;
		while ((byteRead = byteArrayInputStream.read(buffered)) != -1) {
			byteArrayOutputStream.write(cipher.doFinal(buffered, 0, byteRead));
		}
		byteArrayOutputStream.flush();
		return byteArrayOutputStream.toByteArray();
	}

	public byte[] decrypt(byte[] input, Object objects) throws IllegalBlockSizeException, InvalidKeyException,
			ClassCastException, BadPaddingException, IOException {
		if (objects instanceof PublicKey) {
			cipher.init(Cipher.DECRYPT_MODE, (PublicKey) objects);
		} else {
			cipher.init(Cipher.DECRYPT_MODE, (PrivateKey) objects);
		}
		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(input);
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		byte[] buffered = new byte[256];
		int byteRead = 0;
		while ((byteRead = byteArrayInputStream.read(buffered)) != -1) {
			byteArrayOutputStream.write(cipher.doFinal(buffered));
		}
		return byteArrayOutputStream.toByteArray();
	}
}
