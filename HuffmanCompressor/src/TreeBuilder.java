import java.util.Map;
import java.util.TreeSet;

public class TreeBuilder {
    public static Node build(Map<Character, Integer> characterCount) {
        if (characterCount == null || characterCount.size() == 0) {
            return new Node(0, null, null, null);
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

        while (nodes.size() >= 2) {
            Node nodeOne = nodes.pollFirst();
            Node nodeTwo = nodes.pollFirst();

            Node node = Node.link(nodeOne, nodeTwo);
            nodes.add(node);
        }

        return nodes.pollFirst();
    }
}