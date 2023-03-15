import java.util.Map;

public class CharacterCountResult {
    private final Map<Byte, Integer> characterCount;
    private final int totalCharacterCount;

    CharacterCountResult(Map<Byte, Integer> characterCount, int totalCharacterCount) {
        this.characterCount = characterCount;
        this.totalCharacterCount = totalCharacterCount;
    }

    public Map<Byte, Integer> getCharacterCount() {
        return characterCount;
    }

    public int getTotalCharacterCount() {
        return totalCharacterCount;
    }
}