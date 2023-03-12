import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class HuffmanCompressor {

    public static void compress(File decompressedFile, File compressedFile) throws Exception {
        if (decompressedFile == null || compressedFile == null) {
            throw new IllegalArgumentException("Input and output file must be specified.");
        }

        try {
            HashMap<Character, Integer> characterCount = CharacterCounter.count(decompressedFile);
            Node rootNode = TreeBuilder.build(characterCount);
            HashMap<Character, ArrayList<Integer>> characterCode = TreeBuilder.parse(rootNode);

        } catch (Exception exception) {
            throw new Exception("Failed to compress '" + decompressedFile + "' file due to '" + exception.getMessage() + "'.");
        }
    }

    public static void decompress(File compressedFile, File decompressedFile) {
        if (compressedFile == null || decompressedFile == null) {
            throw new IllegalArgumentException("Input and output file must be specified.");
        }
    }
}