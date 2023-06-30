import java.util.ArrayDeque;

/**
 * The LevelLargestSum class provides a method to find the level with the largest sum in a binary tree.
 */
public class LevelLargestSum {

    /**
     * Finds the level with the largest sum in a binary tree.
     * If the tree is empty (root is null), it returns -1.
     * queue - to store the nodes during the traversal. (an array that represents queue).
     * the code processes each level of the binary tree until the queue is empty.
     * It retrieves the first node from the front of the queue using the poll() method.
     * It adds the value of the current node to levelSum.
     * If the node has a left child, it adds the left child to the queue.
     * If the node has a right child, it adds the right child to the queue.
     * After processing each level, it compares the levelSum with the maxLevelSum
     * @param root The root node of the binary tree.
     * @return The level with the largest sum. If the tree is empty (root is null), returns -1.
     */
    public static int getLevelWithLargestSum(BinNode<Integer> root) {
        if (root == null)
            return -1;
        ArrayDeque<BinNode<Integer>> queue = new ArrayDeque<>();
        queue.add(root);
        int maxLevelSum = Integer.MIN_VALUE;
        int levelWithMaxSum = 0;
        int currentLevel = 0;
        while (!queue.isEmpty()) {
            int levelSize = queue.size();
            int levelSum = 0;
            for (int i = 0; i < levelSize; i++) {
                BinNode<Integer> node = queue.poll();
                levelSum += node.getData();
                if (node.getLeft() != null) {
                    queue.add(node.getLeft());
                }
                if (node.getRight() != null) {
                    queue.add(node.getRight());
                }
            }
            if (levelSum > maxLevelSum) {
                maxLevelSum = levelSum;
                levelWithMaxSum = currentLevel;
            }
            currentLevel++;
        }
        return levelWithMaxSum;
    }
}
