import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Stack;
import java.util.TreeSet;

public class TreeBuilder {

    public static Node build(Map<Byte, Integer> characterCount) {
        if (characterCount == null) {
            throw new IllegalArgumentException("Character count must be specified.");
        }

        if (characterCount.size() == 0) {
            return new Node();
        }

        TreeSet<Node> nodes = new TreeSet<>((nodeOne, nodeTwo) -> {
            if (nodeOne.getWeight() > nodeTwo.getWeight()) {
                return 1;
            } else {
                return -1;
            }
        });

        for (Byte characterCode : characterCount.keySet()) {
            nodes.add(new CharacterNode(characterCode, characterCount.get(characterCode), null, null, null));
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

    public static HashMap<Byte, ArrayList<Integer>> parse(Node rootNode) {
        HashMap<Byte, ArrayList<Integer>> characterCode = new HashMap<>();
        LinkedList<Integer> currentPath = new LinkedList<>();

        HashSet<Node> processedNodes = new HashSet<>();
        Node currentNode = rootNode;

        while (currentNode != null) {
            processedNodes.add(currentNode);

            if (currentNode instanceof CharacterNode) {
                CharacterNode currentCharacterNode = (CharacterNode) currentNode;
                characterCode.put(currentCharacterNode.characterCode, new ArrayList<>(currentPath));

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

    public static void serialize(OutputStream outputStream, Node rootNode) throws Exception {
        if (outputStream == null) {
            throw new IllegalArgumentException("Writer must be specified.");
        }

        if (rootNode == null) {
            throw new IllegalArgumentException("Root node must be specified.");
        }

        HashSet<Node> processedNodes = new HashSet<>();
        Node currentNode = rootNode;

        while (currentNode != null) {
            if (currentNode.leftNode != null && !processedNodes.contains(currentNode.leftNode)) {
                currentNode = currentNode.leftNode;
                continue;
            }

            if (currentNode.rightNode != null && !processedNodes.contains(currentNode.rightNode)) {
                currentNode = currentNode.rightNode;
                continue;
            }

            if (currentNode instanceof CharacterNode) {
                CharacterNode currentCharacterNode = (CharacterNode) currentNode;
                Logger.log("Tree Node Character Code: " + currentCharacterNode.characterCode);
                outputStream.write(currentCharacterNode.characterCode);
                outputStream.write('|');

            } else {
                if (currentNode.parentNode == null) {
                    break;
                }

                outputStream.write('N');
                outputStream.write('D');
                outputStream.write('|');
            }

            processedNodes.add(currentNode);
            currentNode = currentNode.parentNode;
        }

        outputStream.write('F');
        outputStream.write('S');
    }

    public static Node deserialize(InputStream inputStream) throws Exception {
        if (inputStream == null) {
            throw new IllegalArgumentException("Reader must be specified.");
        }

        byte currentCharacterCodeOne;
        byte currentCharacterCodeTwo;

        Stack<Node> nodeTree = new Stack<>();
        Node leftNode;
        Node rightNode;
        Node rootNode;

        while (true) {
            currentCharacterCodeOne = CodeConverter.getByte(inputStream.read());
            currentCharacterCodeTwo = CodeConverter.getByte(inputStream.read());

            if (currentCharacterCodeOne == 'F' && currentCharacterCodeTwo == 'S') {
                if (nodeTree.size() == 0) {
                    return new Node();
                }

                if (nodeTree.size() == 1) {
                    leftNode = nodeTree.pop();
                    rootNode = new Node(0, null, leftNode, null);
                    leftNode.setParent(rootNode);

                    return rootNode;
                }

                rightNode = nodeTree.pop();
                leftNode = nodeTree.pop();

                rootNode = new Node(0, null, leftNode, rightNode);
                leftNode.parentNode = rootNode;
                rightNode.parentNode = rootNode;

                if (nodeTree.size() != 0) {
                    throw new IllegalStateException("Illegal state of the tree.");
                }

                return rootNode;
            }

            if (currentCharacterCodeOne == 'N' && currentCharacterCodeTwo == 'D') {
                rightNode = nodeTree.pop();
                leftNode = nodeTree.pop();

                Node node = new Node(0, null, leftNode, rightNode);
                leftNode.parentNode = node;
                rightNode.parentNode = node;

                nodeTree.add(node);
                inputStream.read();
                continue;
            }

            CharacterNode characterNode = new CharacterNode(currentCharacterCodeOne);
            nodeTree.add(characterNode);
        }
    }
}