import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class HuffmanCompressor {

    public static void compress(File decompressedFile, File compressedFile) throws Exception {
        if (decompressedFile == null || compressedFile == null) {
            throw new IllegalArgumentException("Input and output file must be specified.");
        }

        try {
            HashMap<Character, Integer> characterCount = CharacterCounter.count(decompressedFile);
            Node rootNode = TreeBuilder.build(characterCount);
            HashMap<Character, ArrayList<Integer>> characterCode = TreeBuilder.parse(rootNode);

            writeToFile(decompressedFile, compressedFile, rootNode, characterCode);

        } catch (Exception exception) {
            throw new Exception("Failed to compress '" + decompressedFile + "' file due to '" + exception.getMessage() + "'.");
        }
    }

    private static void writeToFile(File decompressedFile, File compressedFile, Node rootNode, HashMap<Character, ArrayList<Integer>> characterCode) throws Exception {
        try {
            FileReader fileReader = new FileReader(decompressedFile);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            FileWriter fileWriter = new FileWriter(compressedFile);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            int currentCharacterCode;
            Character currentCharacterValue;
            LinkedList<Integer> buffer = new LinkedList<>();

            while ((currentCharacterCode = bufferedReader.read()) != -1) {
                currentCharacterValue = Character.toString(currentCharacterCode).charAt(0);
                buffer.addAll(characterCode.get(currentCharacterValue));

                while (buffer.size() >= CodeConverter.CodeSize) {
                    ArrayList<Integer> binaryCode = new ArrayList<>(CodeConverter.CodeSize);

                    for (int i = 0; i < CodeConverter.CodeSize; i++) {
                        binaryCode.add(buffer.pollFirst());
                    }

                    int code = CodeConverter.convertBinaryCodeToCode(binaryCode);
                    bufferedWriter.write(code);
                }
            }

            if (buffer.size() > 0) {
                int difference = CodeConverter.CodeSize - buffer.size();

                for (int i = 0; i < difference; i++) {
                    buffer.add(0);
                }

                int code = CodeConverter.convertBinaryCodeToCode(buffer);
                bufferedWriter.write(code);

                bufferedWriter.newLine();
                bufferedWriter.write(difference);
            }

            bufferedWriter.close();
            fileWriter.close();
            bufferedReader.close();
            fileReader.close();

        } catch (FileNotFoundException exception) {
            throw new IllegalArgumentException("File was not found.");

        } catch (Exception exception) {
            throw new Exception("Failed to compress '" + compressedFile + "' file due to '" + exception.getMessage() + "'.");
        }
    }

    public static void decompress(File compressedFile, File decompressedFile) {
        if (compressedFile == null || decompressedFile == null) {
            throw new IllegalArgumentException("Input and output file must be specified.");
        }
    }
}