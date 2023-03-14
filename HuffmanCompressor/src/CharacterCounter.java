import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.HashMap;

public class CharacterCounter {

    public static CharacterCountResult count(File file) throws Exception {

        if (file == null) {
            throw new IllegalArgumentException("File must be specified.");
        }

        HashMap<Integer, Integer> characterCount = new HashMap<>();
        int totalCharacterCount = 0;

        try {
            FileInputStream fileInputStream = new FileInputStream(file);

            int characterCode;
            byte[] characterBytes = new byte[Settings.CharacterSize];

            while ((fileInputStream.read(characterBytes)) != -1) {

                characterCode = CodeConverter.getInteger(characterBytes);
                if (characterCount.containsKey(characterCode)) {
                    characterCount.put(characterCode, characterCount.get(characterCode) + 1);

                } else {
                    characterCount.put(characterCode, 1);
                }

                totalCharacterCount++;
            }

            fileInputStream.close();

        } catch (FileNotFoundException exception) {
            throw new IllegalArgumentException("File '" + file + "' was not found.");

        } catch (Exception exception) {
            throw new Exception("Failed to count characters in '" + file + "' file due to '" + exception.getMessage() + "'.");
        }

        return new CharacterCountResult(characterCount, totalCharacterCount);
    }
}