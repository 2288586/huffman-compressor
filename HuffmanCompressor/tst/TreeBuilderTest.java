import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TreeBuilderTest {

    @Test
    void buildNullTree() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> TreeBuilder.build(null));
        assertEquals("Character count must be specified.", exception.getMessage());
    }

    @Test
    void buildEmptyTree() {
        Node actualNode = TreeBuilder.build(new HashMap<>());
        assertEquals(new Node(), actualNode);
    }

    @Test
    void buildTree() {
        HashMap<Byte, Integer> characterCount = new HashMap<>();
        characterCount.put(CodeConverter.getByte('A'), 1);
        characterCount.put(CodeConverter.getByte('B'), 3);
        characterCount.put(CodeConverter.getByte('C'), 2);

        CharacterNode nodeA = new CharacterNode(CodeConverter.getByte('A'), 1, null, null, null);
        CharacterNode nodeB = new CharacterNode(CodeConverter.getByte('B'), 3, null, null, null);
        CharacterNode nodeC = new CharacterNode(CodeConverter.getByte('C'), 2, null, null, null);

        Node nodeAC = new Node(3, null, nodeA, nodeC);
        nodeA.setParent(nodeAC);
        nodeC.setParent(nodeAC);

        Node expectedNode = new Node(6, null, nodeAC, nodeB);
        nodeAC.setParent(expectedNode);
        nodeB.setParent(expectedNode);

        Node actualNode = TreeBuilder.build(characterCount);
        assertEquals(expectedNode, actualNode);
    }

    @Test
    void parseNullTree() {
        HashMap<Byte, ArrayList<Integer>> expectedMap = new HashMap<>();
        HashMap<Byte, ArrayList<Integer>> actualMap = TreeBuilder.parse(null);
        assertEquals(expectedMap, actualMap);
    }

    @Test
    void parseSingleNodeTree() {
        HashMap<Byte, ArrayList<Integer>> expectedMap = new HashMap<>();
        ArrayList<Integer> expectedCharacterPath = new ArrayList<>();
        expectedCharacterPath.add(0);
        expectedMap.put(CodeConverter.getByte('A'), expectedCharacterPath);

        CharacterNode characterNode = new CharacterNode(CodeConverter.getByte('A'), 1, null, null, null);
        Node rootNode = new Node(1, null, characterNode, null);

        HashMap<Byte, ArrayList<Integer>> actualMap = TreeBuilder.parse(rootNode);
        assertEquals(expectedMap, actualMap);
    }

    @Test
    void parseTree() {
        HashMap<Byte, ArrayList<Integer>> expectedMap = new HashMap<>();

        ArrayList<Integer> expectedArrayListA = new ArrayList<>();
        expectedArrayListA.add(0);
        expectedArrayListA.add(0);
        ArrayList<Integer> expectedArrayListB = new ArrayList<>();
        expectedArrayListB.add(1);
        ArrayList<Integer> expectedArrayListC = new ArrayList<>();
        expectedArrayListC.add(0);
        expectedArrayListC.add(1);

        expectedMap.put(CodeConverter.getByte('A'), expectedArrayListA);
        expectedMap.put(CodeConverter.getByte('B'), expectedArrayListB);
        expectedMap.put(CodeConverter.getByte('C'), expectedArrayListC);

        HashMap<Byte, Integer> characterCount = new HashMap<>();
        characterCount.put(CodeConverter.getByte('A'), 1);
        characterCount.put(CodeConverter.getByte('B'), 3);
        characterCount.put(CodeConverter.getByte('C'), 2);
        Node rootNode = TreeBuilder.build(characterCount);

        HashMap<Byte, ArrayList<Integer>> actualMap = TreeBuilder.parse(rootNode);
        assertEquals(expectedMap, actualMap);
    }

    @Test
    void serializeNullWriter() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> TreeBuilder.serialize(null, new Node()));
        assertEquals(exception.getMessage(), "Writer must be specified.");
    }

    @Test
    void serializeNullNode() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> TreeBuilder.serialize(new ByteArrayOutputStream(), null));
        assertEquals(exception.getMessage(), "Root node must be specified.");
    }

    @Test
    void serializeEmptyNode() throws Exception {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        TreeBuilder.serialize(byteArrayOutputStream, new Node());

        byte[] fsBytes = new byte[2];
        fsBytes[0] = 'F';
        fsBytes[1] = 'S';

        byteArrayOutputStream.close();
        assertEquals(Arrays.toString(fsBytes), Arrays.toString(byteArrayOutputStream.toByteArray()));
    }

    @Test
    void deserializeNullReader() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> TreeBuilder.deserialize(null));
        assertEquals(exception.getMessage(), "Reader must be specified.");
    }

    @Test
    void deserializeEmptyNode() throws Exception {
        byte[] fsBytes = new byte[2];
        fsBytes[0] = 'F';
        fsBytes[1] = 'S';

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(fsBytes);
        Node node = TreeBuilder.deserialize(byteArrayInputStream);

        byteArrayInputStream.close();
        assertEquals(new Node(), node);
    }

    @Test
    void serializeDeserializeNode() throws Exception {
        CharacterNode nodeA = new CharacterNode(CodeConverter.getByte('A'), 0, null, null, null);
        CharacterNode nodeB = new CharacterNode(CodeConverter.getByte('B'), 0, null, null, null);
        CharacterNode nodeC = new CharacterNode(CodeConverter.getByte('C'), 0, null, null, null);

        Node nodeAC = new Node(0, null, nodeA, nodeC);
        nodeA.setParent(nodeAC);
        nodeC.setParent(nodeAC);

        Node expectedRootNode = new Node(0, null, nodeAC, nodeB);
        nodeAC.setParent(expectedRootNode);
        nodeB.setParent(expectedRootNode);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        TreeBuilder.serialize(byteArrayOutputStream, expectedRootNode);
        byteArrayOutputStream.close();

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        Node actualRootNode = TreeBuilder.deserialize(byteArrayInputStream);
        byteArrayInputStream.close();

        assertEquals(expectedRootNode, actualRootNode);
    }
}