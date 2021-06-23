import java.util.*;

//泛型T代表此B树结点的元素类型，泛型E代表可以比较T对象大小的类，其实现了接口Comparator的compare方法
public class BTree <T, E extends Comparator<T> > {
    //B树阶数，默认为2
    private int M;
    //B树根结点
    BTreeNode<T> root;
    //泛型比较对象，用于比较泛型结点中元素的大小
    E cmp;

    /*以下为B树的各种辅助算法*/

    /**
     * @brief 创建一棵空的B树
     * @param c 实现Comparator接口的对象，用于比较B树结点元素值的大小
     * @param m B树阶数
     */
    public BTree(E c, int m){
        if (m < 2){
            System.out.println("B树阶数不合法！");
            System.out.print("创建B树失败！");
        }
        else{
            cmp = c;
            root =  new BTreeNode<>();
            M = m;
        }
    }

    /**
     * @brief B树的中序遍历
     * @param baseNode B树根结点
     */
    public void inorderTravel(BTreeNode<T> baseNode){
        if (baseNode != null){
            //递归遍历当前baseNode第一棵子树
            inorderTravel(baseNode.pointer.get(0));
            for(int i = 0; i < baseNode.num; i++){
                //访问当前结点的第i个键值
                System.out.println(baseNode.key.get(i) + " ");
                //递归访问当前baseNode的下一棵子树
                inorderTravel(baseNode.pointer.get(i+1));
            }
        }
    }

    /**
     * @brief  B树的层次遍历
     * @param baseNode 根结点
     */
    public void levelTravel(BTreeNode<T> baseNode){
        Queue<BTreeNode<T>> tempQueue = new ArrayDeque<>();
        List<BTreeNode<T>> list = new ArrayList<>();
        BTreeNode<T> node;

        //初始化
        if (baseNode != null){
            tempQueue.add(baseNode);
        }

        while(!tempQueue.isEmpty()){

            //遍历一层，并将此层所有子节点存储到list中
            while(!tempQueue.isEmpty()){
                node = tempQueue.remove();

                for(int i = 0; i < node.key.size(); i++){
                    //每个键值以一个空格分割
                    System.out.print(node.key.get(i) + " ");
                }

                //每个结点以4个空格分割
                System.out.print("    ");

                for(int i = 0; i < node.pointer.size(); i++){
                    //保存子结点
                    if (node.pointer.get(i) != null){
                        list.add(node.pointer.get(i));
                    }
                }

            }

            //每层为一行
            System.out.println();

            while(!list.isEmpty()){
                tempQueue.add(list.remove(0));
            }

        }
    }

    /**
     * @brief 返回根节点
     * @return 根结点
     */
    public BTreeNode<T> getRoot(){
        return root;
    }


    /*以下为B树的插入元素算法*/
    /**
     * @brief 分裂已满结点
     * @param parent 已满结点的父结点
     * @param pos 将已满结点的中间元素上移至parent的键值的pos位置
     * @param node 已满结点
     */
    public void splitNode(BTreeNode<T> parent, int pos, BTreeNode<T> node){
        //默认node元素已满
        BTreeNode<T> newNode = new BTreeNode<>();
        newNode.isLeave = node.isLeave;
        newNode.num = M-1;
        int mid = M-1;

        for(int i = M; i < node.key.size(); i++) {
            //将node的后一半元素转移到新结点中
            try {
                newNode.key.add(node.key.remove(i));
            } catch (Exception e) {
                System.out.println("元素转移出错！");
                return;
            }
        }

        //将node的中间元素上移到其父节点的pos位置
        parent.key.add(pos, node.key.remove(mid));
        //更新node中的键值数
        node.num = M - 1;

        //将node的后一半子节点转移到newNode
       while(node.pointer.size() > M){
           newNode.pointer.add(node.pointer.remove(M));
       }

        //newNode为parent的一个新的子节点
        parent.pointer.add(pos+1, newNode);
        //更新parent的键值数
        parent.num++;
    }

    /**
     * @brief 进行插入元素时，若发现根节点已满，需进行此特殊处理
     */
    private void changeRootI(){
        //创建一个新的结点作为新的根结点
        BTreeNode<T> newRoot = new BTreeNode<>();
        //显然新的根结点不为叶子结点
        newRoot.isLeave = false;
        //原根结点作为新根结点的第一个子结点
        newRoot.pointer.add(0, root);
        //分裂原根结点
        splitNode(newRoot, 0, root);
        //得到新根结点
        root = newRoot;
    }

    /**
     * 搜索合适插入位置
     * @param  node 合适的插入结点
     * @param target  待插值
     */
    private int searchPos(BTreeNode<T> node, T target){
        int pos = node.num;

        for (int i = 0; i < node.key.size(); i++){
            if (cmp.compare(target, node.key.get(i)) < 0){
              pos = i;
              break;
            }
        }

        return pos;
    }

    /**
     * @brief 插入新元素
     * @param node 待插结点(未满)
     * @param target 待插值
     */
    private void nodeInsert(BTreeNode<T> node, T target){
        if(node.isLeave){
            //node为叶子结点则直接插入
            if (node.pointer.size() == 0){
                node.pointer.add(searchPos(node, target), null);
            }
            node.key.add(searchPos(node, target), target);
            node.pointer.add(searchPos(node, target), null);
            node.num++;
        }
        else{
            //node不是叶子结点则在其子合适子树中寻找合适的结点插入
            BTreeNode<T> temp = node.pointer.get(searchPos(node, target));

            if (temp.num == 2*M - 1){
                //若结点已满,将之分裂
                splitNode(node, searchPos(node, temp.key.get(M-1)), temp);
                //由于分裂后B树已更新，则从node从新开始搜索
                temp = node;
            }

            //插入
            nodeInsert(temp, target);
        }
    }

    /**
     * @brief 从根结点开始搜索插入位置
     * @param baseNode B树根结点
     * @param target   插入目标
     */
    public void insert(BTreeNode<T> baseNode, T target) {
        if (baseNode == null || baseNode != root) {
            //B树未创建或base不是此B树的根结点
            System.out.println("B树未创建或base不是此B树的根结点");
        } else if (baseNode.num == 2 * M - 1) {
            //若根节点已满
            changeRootI();
            //处理根结点后从新的根结点处搜索插入位置
            nodeInsert(root, target);
        } else {
            //若根结点未满，则从根结点开始寻找插入位置
            nodeInsert(baseNode, target);
        }
    }

    /*以下为B树的删除算法*/

    /**
     * @brief 合并两个关键字个数为M-1的结点与它们父节点的一个关键字
     * @param baseNode 父节点
     * @param pos 父节点中要与两个子节点合并的关键字下标
     * @param node1 需合并结点1
     * @param node2 需合并结点2
     */
    private void mergeNode(BTreeNode<T> baseNode, int pos, BTreeNode<T> node1, BTreeNode<T>node2){
        //将父节点pos位置的关键字添加到node1
        node1.key.add(baseNode.key.remove(pos));
        baseNode.pointer.remove(pos+1);
        baseNode.num--;

        //将node2的关键字全部转移到node1
        while(!node2.key.isEmpty()){
            node1.key.add(node2.key.remove(0));
        }

        //将node2的子节点转移到node1
        while(!node2.pointer.isEmpty()){
            node1.pointer.add(node2.pointer.remove(0));
            node1.num++;
        }
    }

    /**
     * @brief 进行删除元素时，若发现根结点只有一个元素，孩子结点全不为空，且孩子结点的关键字个数都为M-1,需进行特殊处理
     */
    private void changeRootD(){
        BTreeNode<T> tempNode1 = root.pointer.get(0);
        BTreeNode<T> tempNode2 = root.pointer.get(1);

        if (tempNode1 != null && tempNode2 != null){
            if (tempNode1.key.size() < M && tempNode2.key.size() < M){
                mergeNode(root, 0, tempNode1, tempNode2);
                root = tempNode1;
            }
        }
    }

    /**
     * @brief 从根结点开始删除位置
     * @param baseNode B树根结点
     * @param target   删除目标
     */
    public void delete(BTreeNode<T> baseNode, T target){
        if (baseNode == null || baseNode != root) {
            //B树未创建或baseNode不是此B树的根结点
            System.out.println("B树未创建或baseNode不是此B树的根结点");
        } else if(baseNode.num == 1){
            changeRootD();
            nodeDelete(root, target);
        } else{
            nodeDelete(baseNode, target);
        }
    }

    /**
     * @brief 在合法结点(关键字个数大于M-1，且为叶子结点)中删除目标值
     * @param node 待删结点(其关键字个数大于M-1)
     * @param target 待删值
     */
    private void nodeDelete(BTreeNode<T> node, T target){
        int i = 0;

        //搜素目标元素
        while(i < node.key.size() && cmp.compare(target, node.key.get(i)) > 0){
            i++;
        }

        //node为叶子结点时
        if (node.isLeave){
            //判断node中是否有此元素
            if (i < node.key.size() && node.key.get(i) == target){
                //有则直接删除
                node.key.remove(i);
                node.pointer.remove(i);
            }
            else{
                System.out.println("无此元素！");
            }

        }
        //node不是叶子结点时
        else{
            //若目标值在当前结点中
            if (i < node.key.size() && node.key.get(i) == target){
                if (node.pointer.get(i).num > M-1){
                    /*
                    当目标关键字的左结点的关键字个数大于M-1，
                    用该左结点的最大关键字替换当前结点中的目标关键字
                    然后在该左结点中递归地删除它本身的最大关键字
                     */
                    node.key.remove(i);
                    node.key.add(i, getNodeMax(node.pointer.get(i)));
                    nodeDelete(node.pointer.get(i), getNodeMax(node.pointer.get(i)));
                } else if (node.pointer.get(i+1).num > M-1){
                    /*
                    当目标关键字的右结点的关键字个数大于M-1，
                    用该右结点的最小关键字替换当前结点中的目标关键字
                    然后在该左结点中递归地删除它本身的最小关键字
                     */
                    node.key.remove(i);
                    node.key.add(i, getNodeMin(node.pointer.get(i+1)));
                    nodeDelete(node.pointer.get(i+1), getNodeMin(node.pointer.get(i+1)));
                }else{
                    /*
                    若待删除关键字相邻的两个结点的关键字个数都为M-1
                    则将两相邻结点与待删除关键字合并为一个新结点
                    递归地在此结点上删除目标关键字
                     */
                    mergeNode(node, i, node.pointer.get(i), node.pointer.get(i+1));
                    nodeDelete(node.pointer.get(i), target);
                }
            }
            //若目标值不在当前结点中
            else{
                BTreeNode<T> midNode = node.pointer.get(i);
                BTreeNode<T> leftNode = null;
                BTreeNode<T> rightNode = null;

                if (i > 0){
                    leftNode = node.pointer.get(i-1);
                }

                if (i < node.key.size()){
                    rightNode = node.pointer.get(i+1);
                }

                //则一定在其子树上(如果存在的话)
                if (midNode.key.size() > M-1){
                    /*
                    若目标关键字所在子结点的关键字个数大于M-1
                    则第归地在该结点上删除目标关键字
                     */
                    nodeDelete(midNode, target);
                } else if(leftNode != null && leftNode.key.size() > M-1){
                     /*
                    若目标关键字所在子结点的关键字个数小于M-1
                    但其相邻左兄弟结点的关键字个数大于M-1
                    将当前结点的两个子节点所夹关键字下移到目标关键字所在子节点
                    将左兄弟的最大关键字上移到当前结点顶替下移的关键字位置
                    且需将左兄弟的最后一个子节点转移到目标关键字所在子节点的第一个子节点
                    则第归地在该子结点上删除目标关键字
                     */
                    getFromLeft(node, node.pointer.get(i-1), node.pointer.get(i), i-1);
                    nodeDelete(node.pointer.get(i), target);
                } else if(rightNode != null &&rightNode.key.size() > M-1){
                      /*
                    若目标关键字所在子结点的关键字个数小于M-1
                    但其相邻右兄弟结点的关键字个数大于M-1
                    将当前结点的两个子节点所夹关键字下移到目标关键字所在子节点
                    将右兄弟的最小关键字上移到当前结点顶替下移的关键字位置
                    且需将右兄弟的第一个子节点转移到目标关键字所在子节点的最后一个子节点
                    则第归地在该子结点上删除目标关键字
                     */
                    getFromRight(node, node.pointer.get(i+1), node.pointer.get(i), i);
                    nodeDelete(node.pointer.get(i), target);
                } else{
                    //若目标关键字所在子节点的所有相邻兄弟结点关键字个数都为M-1
                    if (leftNode != null){
                       //左兄弟存在，则向左合并
                        mergeNode(node,i-1, node.pointer.get(i-1), node.pointer.get(i));
                        nodeDelete(node.pointer.get(i-1), target);
                    }
                    else{
                        //否则向右合并
                        mergeNode(node, i, node.pointer.get(i), node.pointer.get(i+1));
                        nodeDelete(node.pointer.get(i), target);
                    }
                }
            }
        }
    }

    /**
     * @brief 获取此结点的最大关键字
     * @param node 搜索关键字的结点
     * @return node的最大关键字
     */
    private T getNodeMax(BTreeNode<T> node){
        return node.key.get(node.key.size()-1);
    }

    /**
     * @brief 获取结点的最小关键字
     * @param node 搜索关键字的结点
     * @return node的最小关键字
     */
    private T getNodeMin(BTreeNode<T> node){
        return node.key.get(0);
    }

    /**
     * @brief 向相邻左兄弟借关键字
     * @param baseNode node与leftNode的父节点
     * @param leftNode 左结点
     * @param node 此结点
     */
    private void getFromLeft(BTreeNode<T> baseNode, BTreeNode<T> leftNode, BTreeNode<T> node, int pos){
        //将baseNode的pos位置的关键字下移到node
        node.key.add(0, baseNode.key.remove(pos));
        //将leftNode的最后一个子节点转移到node的第一个子节点
        node.pointer.add(0, leftNode.pointer.remove(leftNode.pointer.size()-1));
        //将left的最后一个关键字上移到baseNode的pos位置
        baseNode.key.add(pos, leftNode.key.remove(leftNode.key.size() - 1));
    }

    /**
     * @brief 向相邻右兄弟借关键字
     * @param baseNode node与leftNode的父节点
     * @param rightNode 左结点
     * @param node 此结点
     */
    private void getFromRight(BTreeNode<T> baseNode, BTreeNode<T> rightNode, BTreeNode<T> node, int pos){
        //将baseNode的pos位置的关键字下移到node
        node.key.add(baseNode.key.remove(pos));
        //将rightNode的第一个子节点转移到node的最后一个子节点
        node.pointer.add(rightNode.pointer.remove(0));
        //将rightNode的第一个一个关键字上移到baseNode的pos位置
        baseNode.key.add(pos, rightNode.key.remove(0));
    }

}

