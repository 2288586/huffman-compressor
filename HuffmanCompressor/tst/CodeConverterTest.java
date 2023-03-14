import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CodeConverterTest {

    @Test
    void convertNullToCode() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> CodeConverter.convertBinaryCodeToCode(null));
        assertEquals("Binary code must be specified.", exception.getMessage());
    }

    @Test
    void convertEmptyToCode() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> CodeConverter.convertBinaryCodeToCode(new ArrayList<>()));
        assertEquals("Binary code must be of size '" + CodeConverter.CodeSize + "'.", exception.getMessage());
    }

    @Test
    void convertCodeToBinaryCodeToCode() {
        int expectedCode = 130;
        List<Integer> binaryCode = CodeConverter.convertCodeToBinaryCode(expectedCode);
        int actualCode = CodeConverter.convertBinaryCodeToCode(binaryCode);

        assertEquals(expectedCode, actualCode);
    }

    @Test
    void convertIntegerToInteger() {
        int expectedResult = 22;
        int actualResult = CodeConverter.getInteger(CodeConverter.getBytes(expectedResult));

        assertEquals(expectedResult, actualResult);
    }

    @Test
    void convertStringToStringInputCharset() throws Exception {
        String expectedResult = "test";
        String actualResult = CodeConverter.getString(CodeConverter.getBytes(expectedResult, Settings.InputCharset), Settings.InputCharset);

        assertEquals(expectedResult, actualResult);
    }

    @Test
    void convertStringToStringCompressionCharset() throws Exception {
        String expectedResult = "test";
        String actualResult = CodeConverter.getString(CodeConverter.getBytes(expectedResult, Settings.CompressionCharset), Settings.CompressionCharset);

        assertEquals(expectedResult, actualResult);
    }

    @Test
    void convertCharacterToCharacterInputCharset() throws Exception {
        Character expectedResult = 'A';
        Character actualResult = CodeConverter.getCharacter(CodeConverter.getBytes(expectedResult, Settings.InputCharset), Settings.InputCharset);

        assertEquals(expectedResult, actualResult);
    }

    @Test
    void convertCharacterToCharacterCompressionCharset() throws Exception {
        Character expectedResult = 'A';
        Character actualResult = CodeConverter.getCharacter(CodeConverter.getBytes(expectedResult, Settings.CompressionCharset), Settings.CompressionCharset);

        assertEquals(expectedResult, actualResult);
    }
}