import java.util.LinkedList;
import java.util.List;

public class CodeConverter {

    static final int CodeSize = 8;

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
}