package cn.fooltech.fool_ops.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public abstract class MD5Util {

    private static final char[] HEX_CHARS = {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'
    };

    /**
     * 加密字符串
     *
     * @param str 字符串
     * @return 加密后的串
     */
    public static String encrypt(String str) {
        if (str == null) return null;

        MessageDigest messageDigest = getDigest("MD5");
        messageDigest.update(str.getBytes());

        return toHex(messageDigest.digest());
    }

    public static MessageDigest getDigest(String algorithm) {
        try {
            return MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * Returns 32-character hex representation of this objects hash
     *
     * @return String of this object's hash
     */
    public static final String toHex(final byte[] hash) {
        char buf[] = new char[hash.length * 2];
        for (int i = 0, x = 0; i < hash.length; i++) {
            buf[x++] = HEX_CHARS[(hash[i] >>> 4) & 0xf];
            buf[x++] = HEX_CHARS[hash[i] & 0xf];
        }
        return new String(buf);
    }


//	public static void main(String[] args) throws NoSuchAlgorithmException {
//		MessageDigest md = MessageDigest.getInstance("MD5");
//		md.update("admin".getBytes());
//		System.out.println(toHex(md.digest()));
//		System.out.println(MD5Util.encrypt("admin"));
//	}
}
