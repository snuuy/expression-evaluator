/**
 * Represents a node in a binary expression tree
 */
class ExpNode {
    ExpNode left;
    ExpNode right;
    String value;
    /**
     * Constructor for a new expression node
     * @param left Left node
     * @param right Right node
     * @param value Operator or operand
     */
    ExpNode(ExpNode left, ExpNode right, String value) {
        this.left = left;
        this.right = right;
        this.value = value;
    }
}
