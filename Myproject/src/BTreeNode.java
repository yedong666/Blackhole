import java.util.ArrayList;





public class BTreeNode <T> {
    //结点键值
    public ArrayList<T> key = new ArrayList<>();
    //子结点
    public ArrayList<BTreeNode<T>> pointer = new ArrayList<>();
    //结点键值个数
    public int num;
    //是否为叶子结点的标志
    public boolean isLeave;
    //前驱
    public BTreeNode<T> pre;
    //初始化一个结点
    public BTreeNode(){
        isLeave = true;
        num = 0;
    }
}
