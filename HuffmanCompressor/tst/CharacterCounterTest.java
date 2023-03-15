import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
        File file = new File("tst/CharacterCounterTest_EmptyFile.txt");

        HashMap<Integer, Integer> expectedCharacterCount = new HashMap<>();
        CharacterCountResult actualCharacterCountResult = CharacterCounter.count(file);

        Map<Byte, Integer> actualCharacterCount = actualCharacterCountResult.getCharacterCount();
        int actualTotalCharacterCount = actualCharacterCountResult.getTotalCharacterCount();

        assertEquals(expectedCharacterCount, actualCharacterCount);
        assertEquals(0, actualTotalCharacterCount);
    }

    @Test
    void countNonEmptyFile() throws Exception {
        File file = new File("tst/CharacterCounterTest_NonEmptyFile.txt");

        HashMap<Byte, Integer> expectedCharacterCount = new HashMap<>();
        expectedCharacterCount.put(CodeConverter.getByte(' '), 10);
        expectedCharacterCount.put(CodeConverter.getByte('A'), 5);
        expectedCharacterCount.put(CodeConverter.getByte('a'), 5);
        expectedCharacterCount.put(CodeConverter.getByte('B'), 3);
        expectedCharacterCount.put(CodeConverter.getByte('b'), 3);
        expectedCharacterCount.put(CodeConverter.getByte('C'), 1);
        expectedCharacterCount.put(CodeConverter.getByte('c'), 1);
        expectedCharacterCount.put(CodeConverter.getByte('.'), 1);
        expectedCharacterCount.put(CodeConverter.getByte('\n'), 3);

        CharacterCountResult actualCharacterCountResult = CharacterCounter.count(file);
        Map<Byte, Integer> actualCharacterCount = actualCharacterCountResult.getCharacterCount();

        //UTF-16 Encoding Contains Byte-Order Mark (BOM), Character Code = 65279, Unicode Symbol = ﻿
        for (Byte characterCode : expectedCharacterCount.keySet()) {
            assertTrue(actualCharacterCount.containsKey(characterCode));
            assertTrue(actualCharacterCount.containsValue(expectedCharacterCount.get(characterCode)));
        }
    }
}