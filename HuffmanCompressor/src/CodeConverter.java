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

    public static byte[] getBytes(String input) throws Exception {
        try {
            byte[] result = input.getBytes(Settings.Charset);
            return result;
        } catch (UnsupportedEncodingException exception) {
            throw new Exception(exception.getMessage());
        }
    }

    public static byte[] getBytes(Character input) throws Exception {
        return getBytes(input.toString());
    }

    public static byte[] getBytes(int input) {
        byte[] result = new byte[]{
                (byte) ((input >> 24) & 0xFF),
                (byte) ((input >> 16) & 0xFF),
                (byte) ((input >> 8) & 0xFF),
                (byte) (input & 0xFF)
        };

        return result;
    }

    public static String getString(byte[] input) throws Exception {
        try {
            String result = new String(input, Settings.Charset);
            return result;
        } catch (UnsupportedEncodingException exception) {
            throw new Exception(exception.getMessage());
        }
    }

    public static Character getCharacter(byte[] input) throws Exception {
        Character result = getString(input).charAt(0);
        return result;
    }

    public static int getInteger(byte[] input) {
        int result = input[3] & 0xFF |
                (input[2] & 0xFF) << 8 |
                (input[1] & 0xFF) << 16 |
                (input[0] & 0xFF) << 24;

        return result;
    }

    public static int getInteger(Character input) throws Exception {
        int result = getInteger(getBytes(input));
        return result;
    }
}