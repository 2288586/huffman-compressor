public class Node {
    protected int weight;
    protected Node parentNode;
    protected Node leftNode;
    protected Node rightNode;

    Node() {
        this.weight = 0;
        this.parentNode = null;
        this.leftNode = null;
        this.rightNode = null;
    }

    Node(int weight, Node parentNode, Node leftNode, Node rightNode) {
        this.weight = weight;
        this.parentNode = parentNode;
        this.leftNode = leftNode;
        this.rightNode = rightNode;
    }

    public int getWeight() {
        return weight;
    }

    public void setParent(Node parentNode) {
        this.parentNode = parentNode;
    }

    public static Node link(Node nodeOne, Node nodeTwo) {
        Node node = new Node(nodeOne.weight + nodeTwo.weight, null, nodeOne, nodeTwo);

        nodeOne.parentNode = node;
        nodeTwo.parentNode = node;

        return node;
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof Node) {
            Node other = (Node) object;

            if (this.weight != other.weight) {
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
        return "Node{Weight = " + weight + ", leftNode = " + leftNode + ", rightNode = " + rightNode + "}";
    }
}