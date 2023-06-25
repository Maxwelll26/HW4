public class PathFromRoot {
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
