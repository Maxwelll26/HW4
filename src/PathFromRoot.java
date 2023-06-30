/**
 * The PathFromRoot class provides a method to check if a path exists in a binary tree.
 * The code uses recursion to traverse the binary tree and check if the path exists.
 */
public class PathFromRoot {

    /**
     * Checks if a given path exists in a binary tree.
     * If the given path string is empty  it means we have reached the end of the path
     * and the method returns true - base case.
     * If the root node is not null and the data stored in the root node is equal to the first character
     * of the path string - It creates a new string by excluding the first character from the original path string.
     * calls recursively for the left child or the right child with the remaining str, until the sty is empty or false.
     * @param root The root node of the binary tree.
     * @param str  The path string to be checked.
     * @return True if the path exists, false otherwise.
     */
    public static boolean doesPathExist(BinNode<Character> root, String str) {
        if (str.isEmpty())
            return true;
        if (root != null && root.getData() == str.charAt(0)) {
            String remainingStr = str.substring(1);
            return doesPathExist(root.getLeft(), remainingStr) || doesPathExist(root.getRight(), remainingStr);
        }
        return false;
    }
}
