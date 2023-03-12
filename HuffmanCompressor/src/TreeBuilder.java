import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeSet;

public class TreeBuilder {
    public static Node build(Map<Character, Integer> characterCount) {
        if (characterCount == null || characterCount.size() == 0) {
            return null;
        }

        TreeSet<Node> nodes = new TreeSet<>((nodeOne, nodeTwo) -> {
            if (nodeOne.getWeight() > nodeTwo.getWeight()) {
                return 1;
            } else {
                return -1;
            }
        });

        for (Character character : characterCount.keySet()) {
            nodes.add(new CharacterNode(character, characterCount.get(character), null, null, null));
        }

        if (nodes.size() == 1) {
            Node node = nodes.pollFirst();
            Node rootNode = new Node(node.weight, null, node, null);
            return rootNode;
        }

        while (nodes.size() >= 2) {
            Node nodeOne = nodes.pollFirst();
            Node nodeTwo = nodes.pollFirst();

            Node node = Node.link(nodeOne, nodeTwo);
            nodes.add(node);
        }

        return nodes.pollFirst();
    }

    public static HashMap<Character, ArrayList<Integer>> parse(Node rootNode) {
        HashMap<Character, ArrayList<Integer>> characterCode = new HashMap<>();
        LinkedList<Integer> currentPath = new LinkedList<>();

        HashSet<Node> processedNodes = new HashSet<>();
        Node currentNode = rootNode;

        while (currentNode != null) {
            processedNodes.add(currentNode);

            if (currentNode instanceof CharacterNode) {
                CharacterNode currentCharacterNode = (CharacterNode) currentNode;
                characterCode.put(currentCharacterNode.character, new ArrayList<>(currentPath));

                currentNode = currentNode.parentNode;
                currentPath.removeLast();

            } else {
                if (currentNode.leftNode != null && !processedNodes.contains(currentNode.leftNode)) {
                    currentNode = currentNode.leftNode;
                    currentPath.addLast(0);

                } else if (currentNode.rightNode != null && !processedNodes.contains(currentNode.rightNode)) {
                    currentNode = currentNode.rightNode;
                    currentPath.addLast(1);

                } else {
                    currentNode = currentNode.parentNode;
                    if (currentPath.size() > 0) {
                        currentPath.removeLast();
                    }
                }
            }
        }

        return characterCode;
    }
}