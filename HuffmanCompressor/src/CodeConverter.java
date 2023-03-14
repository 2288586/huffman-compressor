import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.List;

public class CodeConverter {

    static final int CodeSize = 32;

    public static int convertBinaryCodeToCode(List<Integer> binaryCode) {
        if (binaryCode == null) {
            throw new IllegalArgumentException("Binary code must be specified.");
        }

        if (binaryCode.size() != CodeSize) {
            throw new IllegalArgumentException("Binary code must be of size '" + CodeSize + "'.");
        }

        int code = 0;
        for (int i = 0; i < CodeSize; i++) {
            code = code << 1;
            code = code | binaryCode.get(i);
        }

        return code;
    }

    public static List<Integer> convertCodeToBinaryCode(int code) {
        LinkedList<Integer> binaryCode = new LinkedList<>();

        for (int i = 0; i < CodeSize; i++) {
            binaryCode.addFirst(code & 1);
            code = code >> 1;
        }

        return binaryCode;
    }

    public static byte[] getBytes(String input, String charset) throws Exception {
        try {
            byte[] result = input.getBytes(charset);
            return result;
        } catch (UnsupportedEncodingException exception) {
            throw new Exception(exception.getMessage());
        }
    }

    public static byte[] getBytes(Character input, String charset) throws Exception {
        return getBytes(input.toString(), charset);
    }

    public static byte[] getBytes(int input) {
        byte[] result = new byte[4];
        int length = 4;
        int shift = 0;

        while (length > 0) {
            length--;
            result[length] = (byte) ((input >> shift) & 0xFF);
            shift += 8;
        }

        return result;
    }

    public static String getString(byte[] input, String charset) throws Exception {
        try {
            String result = new String(input, charset);
            return result;
        } catch (UnsupportedEncodingException exception) {
            throw new Exception(exception.getMessage());
        }
    }

    public static Character getCharacter(byte[] input, String charset) throws Exception {
        Character result = getString(input, charset).charAt(0);
        return result;
    }

    public static int getInteger(byte[] input) {
        int result = 0;
        int length = input.length;
        int shift = 0;

        while (length > 0) {
            length--;
            result = result | ((input[length] & 0xFF) << shift);
            shift += 8;
        }

        return result;
    }

    public static int getInteger(Character input) throws Exception {
        int result = getInteger(getBytes(input));
        return result;
    }
}