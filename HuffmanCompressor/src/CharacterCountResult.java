import java.util.Map;

public class CharacterCountResult {
    private final Map<Integer, Integer> characterCount;
    private final int totalCharacterCount;

    CharacterCountResult(Map<Integer, Integer> characterCount, int totalCharacterCount) {
        this.characterCount = characterCount;
        this.totalCharacterCount = totalCharacterCount;
    }

    public Map<Integer, Integer> getCharacterCount() {
        return characterCount;
    }

    public int getTotalCharacterCount() {
        return totalCharacterCount;
    }
}