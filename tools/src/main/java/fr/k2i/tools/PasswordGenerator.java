package fr.k2i.tools;

import java.util.Random;

public class PasswordGenerator {
	public static String getRandomString(int length) {
		String charset = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789&@$%*!.,;:_-#";
		Random rand = new Random(System.currentTimeMillis());
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			int pos = rand.nextInt(charset.length());
			sb.append(charset.charAt(pos));
		}
		return sb.toString();
	}
	
	public static String getRandomPasswordHint(int length) {
		String charset = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		Random rand = new Random(System.currentTimeMillis());
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			int pos = rand.nextInt(charset.length());
			sb.append(charset.charAt(pos));
		}
		return sb.toString();
	}
}
