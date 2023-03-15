import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class HuffmanCompressor {

    public static void compress(File decompressedFile, File compressedFile) throws Exception {
        if (decompressedFile == null || compressedFile == null) {
            throw new IllegalArgumentException("Input and output file must be specified.");
        }

        try {
            CharacterCountResult characterCountResult = CharacterCounter.count(decompressedFile);
            Map<Integer, Integer> characterCount = characterCountResult.getCharacterCount();
            Logger.log("Character Count: " + characterCount);
            int totalCharacterCount = characterCountResult.getTotalCharacterCount();
            Logger.log("Total Character Count: " + totalCharacterCount + "\n");

            Node rootNode = TreeBuilder.build(characterCount);
            Logger.log("Character Code Tree: " + rootNode);
            Map<Integer, ArrayList<Integer>> characterCode = TreeBuilder.parse(rootNode);
            Logger.log("Character Codes: " + characterCode + "\n");

            compressToFile(decompressedFile, compressedFile, rootNode, characterCode, totalCharacterCount);

        } catch (Exception exception) {
            throw new Exception("Failed to compress '" + decompressedFile + "' file due to '" + exception.getMessage() + "'.");
        }
    }

    private static void compressToFile(File decompressedFile, File compressedFile, Node rootNode, Map<Integer, ArrayList<Integer>> characterCode, int totalCharacterCount) throws Exception {
        try {
            FileInputStream fileInputStream = new FileInputStream(decompressedFile);
            FileOutputStream fileOutputStream = new FileOutputStream(compressedFile);

            TreeBuilder.serialize(fileOutputStream, rootNode);
            fileOutputStream.write(CodeConverter.getBytes(totalCharacterCount));

            int currentCharacterCode;
            LinkedList<Integer> buffer = new LinkedList<>();

            while ((currentCharacterCode = fileInputStream.read()) != -1) {
                buffer.addAll(characterCode.get(currentCharacterCode));

                while (buffer.size() >= CodeConverter.CodeSize) {
                    ArrayList<Integer> binaryCode = new ArrayList<>(CodeConverter.CodeSize);

                    for (int i = 0; i < CodeConverter.CodeSize; i++) {
                        binaryCode.add(buffer.pollFirst());
                    }

                    Logger.log("Current Character Binary Code: " + binaryCode);
                    int code = CodeConverter.convertBinaryCodeToCode(binaryCode);
                    Logger.log("Current Character Code: " + code + "\n");
                    fileOutputStream.write(CodeConverter.getBytes(code));
                }
            }

            if (buffer.size() > 0) {
                int difference = CodeConverter.CodeSize - buffer.size();

                for (int i = 0; i < difference; i++) {
                    buffer.add(0);
                }

                Logger.log("Leftover Character Binary Code: " + buffer);
                int code = CodeConverter.convertBinaryCodeToCode(buffer);
                Logger.log("Leftover Character Code: " + code + "\n");
                fileOutputStream.write(CodeConverter.getBytes(code));
            }

            fileOutputStream.write(CodeConverter.getBytes('\n'));

            fileOutputStream.close();
            fileInputStream.close();

        } catch (FileNotFoundException exception) {
            throw new IllegalArgumentException("File was not found.");

        } catch (Exception exception) {
            throw new Exception("Failed to compress '" + compressedFile + "' file due to '" + exception.getMessage() + "'.");
        }
    }

    public static void decompress(File compressedFile, File decompressedFile) throws Exception {
        if (compressedFile == null || decompressedFile == null) {
            throw new IllegalArgumentException("Input and output file must be specified.");
        }

        try {
            decompressToFile(compressedFile, decompressedFile);

        } catch (Exception exception) {
            throw new Exception("Failed to decompress '" + compressedFile + "' file due to '" + exception.getMessage() + "'.");
        }
    }

    private static void decompressToFile(File compressedFile, File decompressedFile) throws Exception {
        try {
            FileInputStream fileInputStream = new FileInputStream(compressedFile);
            FileOutputStream fileOutputStream = new FileOutputStream(decompressedFile);

            Node rootNode = TreeBuilder.deserialize(fileInputStream);
            Logger.log("Character Code Tree: " + rootNode);
            Node currentNode = rootNode;
            Logger.log("Character Codes: " + TreeBuilder.parse(rootNode));

            byte[] totalCharacterCountBytes = new byte[Settings.CompressionCharacterSize];
            fileInputStream.read(totalCharacterCountBytes);
            int totalCharacterCount = CodeConverter.getInteger(totalCharacterCountBytes);
            Logger.log("Total Character Count: " + totalCharacterCount + "\n");

            int currentCharacterCode;
            byte[] currentCharacterBytes = new byte[Settings.CompressionCharacterSize];
            List<Integer> currentCharacterBinaryCode;
            LinkedList<Integer> buffer = new LinkedList<>();

            while ((fileInputStream.read(currentCharacterBytes)) != -1) {
                currentCharacterCode = CodeConverter.getInteger(currentCharacterBytes);
                currentCharacterBinaryCode = CodeConverter.convertCodeToBinaryCode(currentCharacterCode);
                Logger.log("Current Character Code: " + currentCharacterCode);
                Logger.log("Current Character Binary Code: " + currentCharacterBinaryCode + "\n");
                buffer.addAll(currentCharacterBinaryCode);

                if (buffer.size() > 0 && totalCharacterCount > 0) {
                    for (int i = 0; i < buffer.size(); i++) {

                        if (buffer.pollFirst() == 0) {
                            currentNode = currentNode.leftNode;
                        } else {
                            currentNode = currentNode.rightNode;
                        }

                        if (currentNode instanceof CharacterNode) {
                            CharacterNode currentCharacterNode = (CharacterNode) currentNode;

                            byte[] characterBytes = CodeConverter.getBytes(currentCharacterNode.characterCode);
                            fileOutputStream.write(characterBytes[3]);
                            totalCharacterCount--;

                            if (totalCharacterCount == 0) {
                                break;
                            }
                            currentNode = rootNode;
                        }
                    }
                }
            }

            fileOutputStream.close();
            fileInputStream.close();

        } catch (FileNotFoundException exception) {
            throw new IllegalArgumentException("File was not found.");

        } catch (Exception exception) {
            throw new Exception("Failed to decompress '" + compressedFile + "' file due to '" + exception.getMessage() + "'.");
        }
    }
}