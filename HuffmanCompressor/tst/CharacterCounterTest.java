import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CharacterCounterTest {

    @Test
    void countNullFile() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> CharacterCounter.count(null));
        assertEquals("File must be specified.", exception.getMessage());
    }

    @Test
    void countNonExistentFile() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> CharacterCounter.count(new File("")));
        assertEquals("File '' was not found.", exception.getMessage());
    }

    @Test
    void countEmptyFile() throws Exception {
        File file = new File("tst/EmptyFile.txt");

        HashMap<Integer, Integer> expectedCharacterCount = new HashMap<>();
        CharacterCountResult actualCharacterCountResult = CharacterCounter.count(file);

        Map<Integer, Integer> actualCharacterCount = actualCharacterCountResult.getCharacterCount();
        int actualTotalCharacterCount = actualCharacterCountResult.getTotalCharacterCount();

        assertEquals(expectedCharacterCount, actualCharacterCount);
        assertEquals(0, actualTotalCharacterCount);
    }

    @Test
    void countNonEmptyFile() throws Exception {
        File file = new File("tst/NonEmptyFile.txt");

        HashMap<Integer, Integer> expectedCharacterCount = new HashMap<>();
        expectedCharacterCount.put(CodeConverter.getInteger(' '), 10);
        expectedCharacterCount.put(CodeConverter.getInteger('A'), 5);
        expectedCharacterCount.put(CodeConverter.getInteger('a'), 5);
        expectedCharacterCount.put(CodeConverter.getInteger('B'), 3);
        expectedCharacterCount.put(CodeConverter.getInteger('b'), 3);
        expectedCharacterCount.put(CodeConverter.getInteger('C'), 1);
        expectedCharacterCount.put(CodeConverter.getInteger('c'), 1);
        expectedCharacterCount.put(CodeConverter.getInteger('.'), 1);
        expectedCharacterCount.put(CodeConverter.getInteger('\n'), 3);

        CharacterCountResult actualCharacterCountResult = CharacterCounter.count(file);
        Map<Integer, Integer> actualCharacterCount = actualCharacterCountResult.getCharacterCount();
        int actualTotalCharacterCount = actualCharacterCountResult.getTotalCharacterCount();

        assertEquals(expectedCharacterCount, actualCharacterCount);
        assertEquals(32, actualTotalCharacterCount);
    }
}