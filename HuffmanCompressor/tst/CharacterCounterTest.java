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

        HashMap<Character, Integer> expectedCharacterCount = new HashMap<>();
        CharacterCountResult actualCharacterCountResult = CharacterCounter.count(file);

        Map<Character, Integer> actualCharacterCount = actualCharacterCountResult.getCharacterCount();
        int actualTotalCharacterCount = actualCharacterCountResult.getTotalCharacterCount();

        assertEquals(expectedCharacterCount, actualCharacterCount);
        assertEquals(0, actualTotalCharacterCount);
    }

    @Test
    void countNonEmptyFile() throws Exception {
        File file = new File("tst/NonEmptyFile.txt");

        HashMap<Character, Integer> expectedCharacterCount = new HashMap<>();
        expectedCharacterCount.put(' ', 10);
        expectedCharacterCount.put('A', 5);
        expectedCharacterCount.put('a', 5);
        expectedCharacterCount.put('B', 3);
        expectedCharacterCount.put('b', 3);
        expectedCharacterCount.put('C', 1);
        expectedCharacterCount.put('c', 1);
        expectedCharacterCount.put('.', 1);
        expectedCharacterCount.put('\n', 3);

        CharacterCountResult actualCharacterCountResult = CharacterCounter.count(file);
        Map<Character, Integer> actualCharacterCount = actualCharacterCountResult.getCharacterCount();
        int actualTotalCharacterCount = actualCharacterCountResult.getTotalCharacterCount();

        assertEquals(expectedCharacterCount, actualCharacterCount);
        assertEquals(32, actualTotalCharacterCount);
    }
}