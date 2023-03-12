import org.junit.jupiter.api.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class TreeBuilderTest {


    @Test
    void buildNullTree() {
        Node actualNode = TreeBuilder.build(null);
        assertEquals(null, actualNode);
    }

    @Test
    void buildEmptyTree() {
        Node actualNode = TreeBuilder.build(new HashMap<>());
        assertEquals(null, actualNode);
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
    void serializeNullWriter() throws IOException {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> TreeBuilder.serialize(null, new Node()));
        assertEquals(exception.getMessage(), "Writer must be specified.");
    }

    @Test
    void serializeNullNode() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> TreeBuilder.serialize(new StringWriter(), null));
        assertEquals(exception.getMessage(), "Root node must be specified.");
    }

    @Test
    void serializeEmptyNode() throws IOException {
        StringWriter stringWriter = new StringWriter();
        TreeBuilder.serialize(stringWriter, new Node());

        stringWriter.close();
        assertEquals("FS", stringWriter.toString());
    }

    @Test
    void deserializeNullReader() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> TreeBuilder.deserialize(null));
        assertEquals(exception.getMessage(), "Reader must be specified.");
    }

    @Test
    void deserializeEmptyNode() throws IOException {
        StringReader stringReader = new StringReader("FS");
        Node node = TreeBuilder.deserialize(stringReader);

        stringReader.close();
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

        StringWriter stringWriter = new StringWriter();
        TreeBuilder.serialize(stringWriter, expectedRootNode);
        stringWriter.close();

        StringReader stringReader = new StringReader(stringWriter.toString());
        Node actualRootNode = TreeBuilder.deserialize(stringReader);
        stringReader.close();

        assertEquals(expectedRootNode, actualRootNode);
    }
}