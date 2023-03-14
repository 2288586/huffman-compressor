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
    void convertCharacterToCharacter() throws Exception {
        char expectedResult = 'A';
        int intermediateResult = CodeConverter.getInteger(expectedResult);
        char actualResult = CodeConverter.getCharacter(CodeConverter.getBytes(intermediateResult));

        assertEquals(expectedResult, actualResult);
    }
}