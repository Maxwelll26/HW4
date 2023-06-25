import java.util.ArrayDeque;
public class LevelLargestSum {
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
