import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;

public class CharacterCounter {

    public static CharacterCountResult count(File file) throws Exception {

        if (file == null) {
            throw new IllegalArgumentException("File must be specified.");
        }

        HashMap<Character, Integer> characterCount = new HashMap<>();
        int totalCharacterCount = 0;

        try {
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            int characterCode;
            Character characterValue;

            while ((characterCode = bufferedReader.read()) != -1) {
                characterValue = Character.toString(characterCode).charAt(0);

                if (characterCount.containsKey(characterValue)) {
                    characterCount.put(characterValue, characterCount.get(characterValue) + 1);
                } else {
                    characterCount.put(characterValue, 1);
                }

                totalCharacterCount++;
            }

            bufferedReader.close();
            fileReader.close();

        } catch (FileNotFoundException exception) {
            throw new IllegalArgumentException("File '" + file + "' was not found.");

        } catch (Exception exception) {
            throw new Exception("Failed to count characters in '" + file + "' file due to '" + exception.getMessage() + "'.");
        }

        return new CharacterCountResult(characterCount, totalCharacterCount);
    }
}