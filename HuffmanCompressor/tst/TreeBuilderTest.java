import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
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
        HashMap<Character, Integer> characterCount = new HashMap<>();
        characterCount.put('A', 1);
        characterCount.put('B', 3);
        characterCount.put('C', 2);

        CharacterNode nodeA = new CharacterNode('A', 1, null, null, null);
        CharacterNode nodeB = new CharacterNode('B', 3, null, null, null);
        CharacterNode nodeC = new CharacterNode('C', 2, null, null, null);

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
        HashMap<Character, ArrayList<Integer>> expectedMap = new HashMap<>();
        HashMap<Character, ArrayList<Integer>> actualMap = TreeBuilder.parse(null);
        assertEquals(expectedMap, actualMap);
    }

    @Test
    void parseSingleNodeTree() {
        HashMap<Character, ArrayList<Integer>> expectedMap = new HashMap<>();
        ArrayList<Integer> expectedCharacterPath = new ArrayList<>();
        expectedCharacterPath.add(0);
        expectedMap.put('A', expectedCharacterPath);

        CharacterNode characterNode = new CharacterNode('A', 1, null, null, null);
        Node rootNode = new Node(1, null, characterNode, null);

        HashMap<Character, ArrayList<Integer>> actualMap = TreeBuilder.parse(rootNode);
        assertEquals(expectedMap, actualMap);
    }

    @Test
    void parseTree() {
        HashMap<Character, ArrayList<Integer>> expectedMap = new HashMap<>();

        ArrayList<Integer> expectedArrayListA = new ArrayList<>();
        expectedArrayListA.add(0);
        expectedArrayListA.add(0);
        ArrayList<Integer> expectedArrayListB = new ArrayList<>();
        expectedArrayListB.add(1);
        ArrayList<Integer> expectedArrayListC = new ArrayList<>();
        expectedArrayListC.add(0);
        expectedArrayListC.add(1);

        expectedMap.put('A', expectedArrayListA);
        expectedMap.put('B', expectedArrayListB);
        expectedMap.put('C', expectedArrayListC);

        HashMap<Character, Integer> characterCount = new HashMap<>();
        characterCount.put('A', 1);
        characterCount.put('B', 3);
        characterCount.put('C', 2);
        Node rootNode = TreeBuilder.build(characterCount);

        HashMap<Character, ArrayList<Integer>> actualMap = TreeBuilder.parse(rootNode);
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
    void serializeEmptyNode() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        TreeBuilder.serialize(byteArrayOutputStream, new Node());

        byteArrayOutputStream.close();
        assertEquals("FS", byteArrayOutputStream.toString());
    }

    @Test
    void deserializeNullReader() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> TreeBuilder.deserialize(null));
        assertEquals(exception.getMessage(), "Reader must be specified.");
    }

    @Test
    void deserializeEmptyNode() throws IOException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream("FS".getBytes());
        Node node = TreeBuilder.deserialize(byteArrayInputStream);

        byteArrayInputStream.close();
        assertEquals(new Node(), node);
    }

    @Test
    void serializeDeserializeNode() throws IOException {
        CharacterNode nodeA = new CharacterNode('A', 0, null, null, null);
        CharacterNode nodeB = new CharacterNode('B', 0, null, null, null);
        CharacterNode nodeC = new CharacterNode('C', 0, null, null, null);

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