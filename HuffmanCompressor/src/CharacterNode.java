public class CharacterNode extends Node {
    protected int characterCode;

    CharacterNode(int characterCode) {
        super();
        this.characterCode = characterCode;
    }

    CharacterNode(int characterCode, int weight, Node parentNode, Node leftNode, Node rightNode) {
        super(weight, parentNode, leftNode, rightNode);
        this.characterCode = characterCode;
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof CharacterNode) {
            CharacterNode other = (CharacterNode) object;

            if (this.characterCode != other.characterCode || this.weight != other.weight) {
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
        return "Node{Character Code = " + characterCode + ", Weight = " + weight + ", leftNode = " + leftNode + ", rightNode = " + rightNode + "}";
    }
}