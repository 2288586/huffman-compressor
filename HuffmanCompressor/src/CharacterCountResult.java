import java.util.Map;

public class CharacterCountResult {
    private final Map<Character, Integer> characterCount;
    private final int totalCharacterCount;

    CharacterCountResult(Map<Character, Integer> characterCount, int totalCharacterCount) {
        this.characterCount = characterCount;
        this.totalCharacterCount = totalCharacterCount;
    }

    public Map<Character, Integer> getCharacterCount() {
        return characterCount;
    }

    public int getTotalCharacterCount() {
        return totalCharacterCount;
    }
}