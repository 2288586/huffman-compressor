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
            System.out.println("Character Count: " + characterCount);
            int totalCharacterCount = characterCountResult.getTotalCharacterCount();
            System.out.println("Total Character Count: " + totalCharacterCount);

            Node rootNode = TreeBuilder.build(characterCount);
            Map<Integer, ArrayList<Integer>> characterCode = TreeBuilder.parse(rootNode);
            System.out.println("Character Codes: " + characterCode);

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
            byte[] currentCharacterBytes = new byte[Settings.InputCharacterSize];

            LinkedList<Integer> buffer = new LinkedList<>();

            while (fileInputStream.read(currentCharacterBytes) != -1) {
                currentCharacterCode = CodeConverter.getInteger(currentCharacterBytes);
                buffer.addAll(characterCode.get(currentCharacterCode));

                while (buffer.size() >= CodeConverter.CodeSize) {
                    ArrayList<Integer> binaryCode = new ArrayList<>(CodeConverter.CodeSize);

                    for (int i = 0; i < CodeConverter.CodeSize; i++) {
                        binaryCode.add(buffer.pollFirst());
                    }

                    System.out.println(binaryCode);
                    int code = CodeConverter.convertBinaryCodeToCode(binaryCode);
                    System.out.println(code);
                    fileOutputStream.write(CodeConverter.getBytes(code));
                }
            }

            if (buffer.size() > 0) {
                int difference = CodeConverter.CodeSize - buffer.size();

                for (int i = 0; i < difference; i++) {
                    buffer.add(0);
                }

                System.out.println(buffer);
                int code = CodeConverter.convertBinaryCodeToCode(buffer);
                System.out.println(code);
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
            Node currentNode = rootNode;
            System.out.println(TreeBuilder.parse(rootNode));

            byte[] totalCharacterCountBytes = new byte[Settings.CompressionCharacterSize];
            fileInputStream.read(totalCharacterCountBytes);
            int totalCharacterCount = CodeConverter.getInteger(totalCharacterCountBytes);
            System.out.println("Total Character Count: " + totalCharacterCount);

            int currentCharacterCode;
            byte[] currentCharacterBytes = new byte[Settings.CompressionCharacterSize];
            List<Integer> currentCharacterBinaryCode;
            LinkedList<Integer> buffer = new LinkedList<>();

            while ((fileInputStream.read(currentCharacterBytes)) != -1) {
                currentCharacterCode = CodeConverter.getInteger(currentCharacterBytes);
                currentCharacterBinaryCode = CodeConverter.convertCodeToBinaryCode(currentCharacterCode);
                System.out.println(currentCharacterBinaryCode);
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
                            int shift = Settings.CompressionCharacterSize - Settings.InputCharacterSize;

                            for (int j = 0; j < Settings.InputCharacterSize; j++) {
                                fileOutputStream.write(characterBytes[shift]);
                                shift++;
                            }
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