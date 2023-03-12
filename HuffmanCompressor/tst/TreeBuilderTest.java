import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class TreeBuilderTest {


    @Test
    void buildNullTree() {
        Node expectedNode = new Node(0, null, null, null);
        Node actualNode = TreeBuilder.build(null);
        assertEquals(expectedNode, actualNode);
    }

    @Test
    void buildEmptyTree() {
        Node expectedNode = new Node(0, null, null, null);
        Node actualNode = TreeBuilder.build(new HashMap<>());
        assertEquals(expectedNode, actualNode);
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
}