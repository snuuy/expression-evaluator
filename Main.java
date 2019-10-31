import java.util.*;

class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String expression = scanner.nextLine();
        expression = Parser.addParentheses(expression); //parenthesize all numbers in expression
        Parser.validateCharacters(expression);
        ExpNode root = Parser.createExpTree(expression); //create expression tree
        System.out.print(evaluateExpTree(root)); //evaluate tree starting at root
    }

    /**
     * Recursive evaluation of a binary expression tree
     * @param root Expression node to evaluate
     * @return Evaluated integer
     */
    private static int evaluateExpTree(ExpNode root) {
        //if node is a leaf, return it's value
        if (root.left == null || root.right == null)  {
            return Integer.parseInt(root.value);
        }
        //evaluate left and right nodes and apply operator
        if (root.value.equals("@")) {
            return Integer.min(evaluateExpTree(root.left), evaluateExpTree(root.right));
        }
        else {
            return Integer.max(evaluateExpTree(root.left), evaluateExpTree(root.right));
        }
    }

}
