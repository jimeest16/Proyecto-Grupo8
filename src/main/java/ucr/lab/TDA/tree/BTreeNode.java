package ucr.lab.TDA.tree;

public class BTreeNode {
    public Object data;
    public BTreeNode left;
    public BTreeNode right;
    public String path;

    // contador para los recorridos
    public int counterTranversal;

    public BTreeNode(Object data) {
        this.data = data;
        left=right=null;
        this.counterTranversal=0;

    }

    public BTreeNode(Object data, String path) {
        this.data = data;
        this.left = this.right = null;
        this.path= path;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public BTreeNode getLeft() {
        return left;
    }

    public void setLeft(BTreeNode left) {
        this.left = left;
    }

    public BTreeNode getRight() {
        return right;
    }

    public void setRight(BTreeNode right) {
        this.right = right;
    }
}

