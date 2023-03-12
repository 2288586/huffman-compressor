public class CharacterNode extends Node {
    private Character character;

    CharacterNode(Character character, int weight, Node parentNode, Node leftNode, Node rightNode) {
        super(weight, parentNode, leftNode, rightNode);
        this.character = character;
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof CharacterNode) {
            CharacterNode other = (CharacterNode) object;

            if (this.character != other.character || this.weight != other.weight) {
                return false;
            }

            if (this.leftNode == null && other.leftNode != null) {
                return false;
            }

            if (this.leftNode != null && !this.leftNode.equals(other.leftNode)) {
                return false;
            }

            if (this.rightNode == null && other.rightNode != null) {
                return false;
            }

            if (this.rightNode != null && !this.rightNode.equals(other.rightNode)) {
                return false;
            }

            return true;
        }

        return false;
    }

    @Override
    public String toString() {
        return "Node{Character = " + character + ", Weight = " + weight + ", leftNode = " + leftNode + ", rightNode = " + rightNode + "}";
    }
}