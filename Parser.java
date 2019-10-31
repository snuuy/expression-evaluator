import java.util.*;
/**
 * Class for parsing expression string into an expression tree
 */
class Parser {

    /**
     * Recursively generates a binary expression tree from a string by finding the operator
     * with highest precedence and splitting expression into two sub-expressions at that operator
     * @param exp Expression string
     * @return Root node of tree
     */
    static ExpNode createExpTree(String exp) {
        validateExpression(exp);

        //if expression is a single integer, return a leaf node
        if (!exp.contains("&") && !exp.contains("@")) {
            exp = exp.replace("(", "").replace(")", ""); //remove brackets
            return new ExpNode(null,null, exp);
        }

        //generate map of sub-expressions to their depth level
        Map<Integer, List<int[]>> subExps = getSubExpressions(exp);

        int splitDepth = -1;
        for(Map.Entry<Integer, List<int[]>> entry : subExps.entrySet()) {
            //find shallowest depth containing two or more sub-expressions
            if (entry.getValue().size() >= 2 && (entry.getKey() < splitDepth || splitDepth == -1)) {
                splitDepth = entry.getKey();
            }
        }
        //if depth contains exactly two sub-expressions, create left and right trees
        if (subExps.get(splitDepth).size() == 2) {
            int[] subExp1 = subExps.get(splitDepth).get(0);
            int[] subExp2 = subExps.get(splitDepth).get(1);
            String operator = Character.toString(exp.charAt(subExp2[0] - 1)); //operator is one position before start of second sub-expression
            ExpNode left = createExpTree(exp.substring(subExp1[0],subExp1[1]));
            ExpNode right = createExpTree(exp.substring(subExp2[0],subExp2[1]));
            return new ExpNode(left, right, operator);
        }
        //if depth contains more then two sub-expressions, insert brackets following left-associativity
        else {
            int[] subExp2 = subExps.get(splitDepth).get(1);
            exp = "(" + new StringBuilder(exp).insert(subExp2[1], ")").toString(); //enclose first sub-expression in brackets
            exp = new StringBuilder(exp).insert(subExp2[1] + 3, "(").toString() + ")"; //enclose rest of expression in brackets
            return createExpTree(exp); //recreate expression tree with the additional brackets
        }
    }

    /***
     * Generate sub-expressions strings from expression string.
     * @param exp Expression string
     * @return HashMap of depth level to list of start and end positions of all sub-expressions at that depth
     */
    private static Map<Integer,List<int[]>> getSubExpressions(String exp) {
        Stack<Integer> brackets = new Stack<>(); //stack containing positions of opening brackets
        Map<Integer,List<int[]>> subExps = new HashMap<>();

        for (int i = 0; i < exp.length(); i++ ) {
            if (exp.charAt(i) == '(') {
                brackets.push(i);
            }
            if (exp.charAt(i) == ')') {
                //if there is no matching opening bracket
                if(brackets.empty()) {
                    System.out.print("INVALID EXPRESSION");
                    System.exit(0);
                }

                int depth = brackets.size(); //the sub-expression depth is equal to how many opening brackets are in the stack

                //add sub-expression to map
                int[] subExp = new int[] { brackets.pop(), i + 1 };
                List<int[]> subExpsAtDepth = subExps.get(depth);
                if(subExpsAtDepth == null) subExpsAtDepth = new ArrayList<>();
                subExpsAtDepth.add(subExp);
                subExps.put(depth,subExpsAtDepth);
            }
        }
        //if extra opening brackets remain in the stack
        if (!brackets.empty()) {
            System.out.print("INVALID EXPRESSION");
            System.exit(0);
        }
        return subExps;
    }

    /**
     * Exit program if expression string is invalid
     * @param exp Expression string
     */
    private static void validateExpression(String exp) {
        String invalidOperator = ".*\\)+[0-9].*|.*[0-9]+\\(.*|.*[@&]+\\).*|.*\\(+[@&].*"; //matches (@, (&, @), &), )n, n(
        String containsNumber = ".*\\d.*";
        if(exp.matches(invalidOperator) || !exp.matches(containsNumber) || exp.contains("@@") || exp.contains("@&") || exp.contains("&@") || exp.contains("&&")) {
            System.out.print("INVALID EXPRESSION");
            System.exit(0);
        }
    }

    /**
     * Exit program if expression string contains invalid characters
     * @param exp Expression string
     */
    static void validateCharacters(String exp) {
        //if expression contains any characters other than ()&@
        if (!exp.matches("^[0-9()@&]*$")) {
            System.out.print("INVALID CHARACTERS");
            System.exit(0);
        }
    }

    /**
     * Parenthesize all numbers in expression string to ensure every expression is always made up of sub-expressions
     * @param exp Expression string
     * @return Expression string with additional brackets
     */
    static String addParentheses(String exp) {
        exp = "(" + exp + ")";
        boolean isCurInt = false; //is current index on an integer
        for (int i = 0; i < exp.length(); i++) {
            char cur = exp.charAt(i);
            //if number is encountered insert opening bracket
            if ("()&@".indexOf(cur) == -1 && !isCurInt) {
                exp = new StringBuilder(exp).insert(i, "(").toString();
                isCurInt = true;
            }
            //insert closing bracket after number
            else if ("()&@".indexOf(cur) != -1 && isCurInt) {
                exp = new StringBuilder(exp).insert(i, ")").toString();
                isCurInt = false;
            }
        }
        return exp;
    }
}
