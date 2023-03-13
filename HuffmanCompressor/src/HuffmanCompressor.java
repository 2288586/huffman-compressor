import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
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
            Map<Character, Integer> characterCount = characterCountResult.getCharacterCount();
            System.out.println(characterCount);
            int totalCharacterCount = characterCountResult.getTotalCharacterCount();

            Node rootNode = TreeBuilder.build(characterCount);
            Map<Character, ArrayList<Integer>> characterCode = TreeBuilder.parse(rootNode);
            System.out.println(characterCode);

            compressToFile(decompressedFile, compressedFile, rootNode, characterCode, totalCharacterCount);

        } catch (Exception exception) {
            throw new Exception("Failed to compress '" + decompressedFile + "' file due to '" + exception.getMessage() + "'.");
        }
    }

    private static void compressToFile(File decompressedFile, File compressedFile, Node rootNode, Map<Character, ArrayList<Integer>> characterCode, int totalCharacterCount) throws Exception {
        try {
            FileInputStream fileInputStream = new FileInputStream(decompressedFile);
            DataInputStream dataInputStream = new DataInputStream(fileInputStream);
            BufferedInputStream bufferedInputStream = new BufferedInputStream(dataInputStream);

            FileOutputStream fileOutputStream = new FileOutputStream(compressedFile);
            DataOutputStream dataOutputStream = new DataOutputStream(fileOutputStream);
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(dataOutputStream);

            TreeBuilder.serialize(bufferedOutputStream, rootNode);
            bufferedOutputStream.write(totalCharacterCount);

            int currentCharacterCode;
            Character currentCharacterValue;
            LinkedList<Integer> buffer = new LinkedList<>();

            while ((currentCharacterCode = bufferedInputStream.read()) != -1) {
                currentCharacterValue = Character.toString(currentCharacterCode).charAt(0);
                buffer.addAll(characterCode.get(currentCharacterValue));

                while (buffer.size() >= CodeConverter.CodeSize) {
                    ArrayList<Integer> binaryCode = new ArrayList<>(CodeConverter.CodeSize);

                    for (int i = 0; i < CodeConverter.CodeSize; i++) {
                        binaryCode.add(buffer.pollFirst());
                    }

                    System.out.println(binaryCode);
                    int code = CodeConverter.convertBinaryCodeToCode(binaryCode);
                    System.out.println(code);
                    System.out.println(Character.toString(code));
                    bufferedOutputStream.write(code);
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
                System.out.println(Character.toString(code));
                bufferedOutputStream.write(code);
            }

            bufferedOutputStream.write('\n');

            bufferedOutputStream.close();
            dataOutputStream.close();
            fileOutputStream.close();

            bufferedInputStream.close();
            dataInputStream.close();
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
            DataInputStream dataInputStream = new DataInputStream(fileInputStream);
            BufferedInputStream bufferedInputStream = new BufferedInputStream(dataInputStream);

            FileOutputStream fileOutputStream = new FileOutputStream(decompressedFile);
            DataOutputStream dataOutputStream = new DataOutputStream(fileOutputStream);
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(dataOutputStream);

            Node rootNode = TreeBuilder.deserialize(bufferedInputStream);
            Node currentNode = rootNode;
            System.out.println(TreeBuilder.parse(rootNode));

            int totalCharacterCount = bufferedInputStream.read();

            int currentCharacterCode;
            List<Integer> currentCharacterBinaryCode;
            LinkedList<Integer> buffer = new LinkedList<>();

            while ((currentCharacterCode = bufferedInputStream.read()) != -1) {
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

                            bufferedOutputStream.write(currentCharacterNode.character);
                            totalCharacterCount--;

                            if (totalCharacterCount == 0) {
                                break;
                            }

                            currentNode = rootNode;
                        }
                    }
                }
            }

            bufferedOutputStream.close();
            dataOutputStream.close();
            fileOutputStream.close();

            bufferedInputStream.close();
            dataInputStream.close();
            fileInputStream.close();

        } catch (FileNotFoundException exception) {
            throw new IllegalArgumentException("File was not found.");

        } catch (Exception exception) {
            throw new Exception("Failed to decompress '" + compressedFile + "' file due to '" + exception.getMessage() + "'.");
        }
    }
}