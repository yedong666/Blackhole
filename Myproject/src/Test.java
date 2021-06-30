import java.util.Comparator;

interface getNew<T>{
    T createT(String s);
}

class G implements getNew<Integer>{
    @Override
    public Integer createT(String s) {
        return Integer.parseInt(s);
    }
}

class student{
    int grade;
    String name;
    student(int g, String n){
        grade = g;
        name =n;
    }

    public String toString(){
        return name;
    }

}

class cmp implements Comparator<student> {
    @Override
     public int compare(student o1, student o2) {
        if (o1.grade > o2.grade){
            return 1;
        }
        else if (o1.grade == o2.grade){
            return 0;
        }
        else{
            return -1;
        }
    }
}

class cmp1 implements Comparator<Integer> {
    @Override
    public int compare(Integer o1, Integer o2) {
        if (o1 > o2){
            return 1;
        }
        else if (o1 == o2){
            return 0;
        }
        else{
            return -1;
        }
    }
}

class cmp2 implements Comparator<Double> {
    @Override
    public int compare(Double o1, Double o2) {
        if (o1 > o2){
            return 1;
        }
        else if (o1 == o2){
            return 0;
        }
        else{
            return -1;
        }
    }
}


class demo <T, E extends Comparator<T> >{
    T a, b;
    E e;
    demo(E o, T A, T B){
        b = B;
        a = A;
        e = o;
    }
    public void test(){
        if (e.compare(a, b) == 1){
            System.out.println(a.toString() + " > " + b.toString());
        }
        else if (e.compare(a, b) == 0){
            System.out.println(a.toString() + " == " + b.toString());
        }
        else{
            System.out.println(a.toString() + " < " + b.toString());
        }
    }
}





public class Test {
    public static void main(String[] args) {
//        student a = new student(98, "Jack");
//        student b = new student(87, "Tom");
//        cmp c = new cmp();
//        cmp1 c1 = new cmp1();
//        cmp2 c2 = new cmp2();
//        demo<Integer, cmp1> d1 = new demo<>(c1, 8, 8);
//        d1.test();
//        demo<student, cmp>  d2 = new demo<>(c, a, b);
//        d2.test();
//        demo<Double, cmp2> d3 = new demo<>(c2, 19.87, 20.65);
//        d3.test();
//        people p = new people(21, "James");
//        System.out.println(p.toString());
        cmp1 cmp = new cmp1();
        BTree<Integer, cmp1> testBtree = new BTree<>(cmp, 2);
        Integer[] testArray = new Integer[6];
        testBtree.insert(testBtree.getRoot(), 10);
        testBtree.insert(testBtree.getRoot(), 9);
       testBtree.insert(testBtree.getRoot(), 12);
//
//        testBtree.insert(testBtree.getRoot(), 13);
//        testBtree.insert(testBtree.getRoot(), 14);
//        testBtree.insert(testBtree.getRoot(), 11);
//        testBtree.insert(testBtree.getRoot(), 8);
//        testBtree.insert(testBtree.getRoot(), 7);
//        testBtree.insert(testBtree.getRoot(), 6);
//        testBtree.insert(testBtree.getRoot(), 5);
//        testBtree.insert(testBtree.getRoot(), 25);
//        testBtree.insert(testBtree.getRoot(), 56);
//        testBtree.insert(testBtree.getRoot(), 2);
        testBtree.insert(testBtree.getRoot(), 2);
        testBtree.insert(testBtree.getRoot(), 13);
        testBtree.delete(testBtree.getRoot(), 10);
        testBtree.delete(testBtree.getRoot(), 9);
        //testBtree.insert(testBtree.getRoot(), 9);
        //testBtree.insert(testBtree.getRoot(), 2);
        testBtree.levelTravel(testBtree.getRoot());
        //testBtree.search(testBtree.getRoot(), 9, 1);
        //testBtree.delete(testBtree.getRoot(), 8);
////        testBtree.inorderTravel(testBtree.getRoot());
//        testBtree.delete(testBtree.getRoot(), 9);
//        testBtree.delete(testBtree.getRoot(), 11);
//        testBtree.delete(testBtree.getRoot(), 12);
        //testBtree.levelTravel(testBtree.getRoot());
        G g = new G();

        Window w = new Window(testBtree, "测试窗口", g);
//        List<Integer> testList = new ArrayList<>();
//        testList.add(10);
//        System.out.println(testList.get(0));
    }

}
