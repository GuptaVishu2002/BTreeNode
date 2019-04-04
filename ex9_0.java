import java.util.Scanner;

class Node {
    static int nextSerialNumber = 0;
    int serial = 0;

    String getMinKey(){
        return getMinKey(this);
    }

    String getMinKey(Node node){
        if (node instanceof LeafNode){
            LeafNode leaf = (LeafNode)node;
            return leaf.key;
        }else{
            InternalNode internal = (InternalNode)node;
            return getMinKey(internal.child[0]);
        }
    }
    public String toString(){
        return toString(0, this);
    }

    public String toString(int lv, Node p){
        if (p instanceof LeafNode) {
            LeafNode l = (LeafNode) p;
            return "#" + l.serial + " Node[Leaf]: (" + l.key + ")";
        } else {
            InternalNode n = (InternalNode)p;
            String s = "#" + n.serial + " Node[Internal] (" + n.nChilds + " childs): ";
            s = s + "| #" + n.child[0].serial + " |";
            for (int i = 1; i < n.nChilds; i++) {
                s = s + " [" + n.low[i] + "] | #" + n.child[i].serial + " |";
            }
            return s;
        }
    }
}

class LeafNode extends Node {
    String key;
    String data;
    LeafNode(String key, String data){
        nextSerialNumber++;
        serial = nextSerialNumber;
        this.key  = key;
        this.data = data;
    }
}

class InternalNode extends Node {
    static final int MAX_CHILD  = 5;
    static final int HALF_CHILD = (MAX_CHILD+1)/2 ;
    int nChilds;
    String[] low;
    Node[]   child;
    InternalNode(){
        nextSerialNumber++;
        serial = nextSerialNumber;
        nChilds = 0;
        child = new Node[MAX_CHILD];
        low   = new String[MAX_CHILD];
    }
    int locateSubtree(String key){
        int i;
        for (i = nChilds - 1; i > 0; i--){
            if (key.compareTo(low[i]) >= 0){
                return i;
            }
        }
        return 0;
    }
}

class MyBTree {
    static final int MAX_CHILD  = InternalNode.MAX_CHILD;
    static final int HALF_CHILD = InternalNode.HALF_CHILD;

    Node root;
    LeafNode currentLeaf;

    public MyBTree(){
        root = null;
    }

    void set_sample1(){
        LeafNode     lf1,lf2,lf3,lf4,lf5,lf6,lf7;
        InternalNode in1, in2, in3;

        /* LeafNode */
        lf1 = new LeafNode("April", "The fourth month of the year");
        lf2 = new LeafNode("July",  "The seventh month of the year");
        lf3 = new LeafNode("joy",   "The passion or emotion excited by the acquisition or expectation of good");
        lf4 = new LeafNode("kid",   "A young goat");
        lf5 = new LeafNode("work",  "to use physical or mental effort to make or do something");
        lf6 = new LeafNode("year",  "a period of time equal to twelve months");
        lf7 = new LeafNode("zoo",   "a place where animals are kept for the public to look at and study");

        /* Internal Node */
        in1 = new InternalNode();
        in2 = new InternalNode();
        in3 = new InternalNode();

        /* set child node */
        in2.nChilds = 4;
        in2.child[0] = lf1;
        in2.low[1] = lf2.getMinKey();
        in2.child[1] = lf2;
        in2.low[2] = lf3.getMinKey();
        in2.child[2] = lf3;
        in2.low[3] = lf4.getMinKey();
        in2.child[3] = lf4;

        in3.nChilds = 3;
        in3.child[0] = lf5;
        in3.low[1] = lf6.getMinKey();
        in3.child[1] = lf6;
        in3.low[2] = lf7.getMinKey();
        in3.child[2] = lf7;

        in1.nChilds = 2;
        in1.child[0] = in2;
        in1.low[1] = in3.getMinKey();
        in1.child[1] = in3;

        /* root NODE */
        root = in1;

        return;

    }

    public String toString(){
        if (root == null){
            return "null";
        } else {
            return toString(0, root);
        }
    }

    public String toString(int lv, Node p){
        if (p instanceof LeafNode) {
            LeafNode l = (LeafNode) p;
            return printSpace(lv) + l + "\n";
        } else {
            InternalNode n = (InternalNode)p;
            String s = printSpace(lv) + n + "\n";
            for (int i = 0; i < n.nChilds; i++) {
                s = s + toString(lv + 1, n.child[i]);
            }
            return s;
        }
    }

    public String printSpace(int num){
        String s = "";
        for(int i=0;i<num;i++){
            s = s + "  ";
        }
        return s;
    }
}

class ex9_0 {
    public static void main (String[] argv){
        Scanner scan = new Scanner(System.in);
        MyBTree btree = new MyBTree();
        btree.set_sample1();
        Node currentNode = btree.root;

        while(true){
            System.out.println("  currentNode: " + currentNode);
            System.out.print("command: ");
            String str = scan.nextLine();
            String[] param = str.split(" ",0);

            if ("quit".equals(param[0]) || "halt".equals(param[0]) || "exit".equals(param[0])) {
                break;
            } else if ("root".equals(param[0])){
                System.out.println("  Message: currentNode became root.");
                currentNode = btree.root;
            } else if ("show".equals(param[0])){
                System.out.println("" + btree);
            } else if ("locate".equals(param[0])){
                if (currentNode instanceof LeafNode){
                    System.out.println("  currentNode is LeafNode.");
                }else{
                    InternalNode in = (InternalNode)currentNode;
                    String str2;
                    System.out.print("  key: ");
                    if (param.length > 1){
                        System.out.print(param[1] + "\n");
                        str2 = new String(param[1]);
                    }else {
                        str2 = scan.nextLine();
                    }
                    System.out.println("  locateIndex(" + str2 + ") is " + in.locateSubtree(str2) + ".");
                }
            } else if ("child".equals(param[0])) {
                if (currentNode instanceof LeafNode) {
                    System.out.println("  currentNode is LeafNode.");
                }else{
                    InternalNode in = (InternalNode)currentNode;
                    System.out.print("  childIndex: ");
                    String str2;
                    if (param.length > 1){
                        System.out.print(param[1] + "\n");
                        str2 = new String(param[1]);
                    } else {
                        str2 = scan.nextLine();
                    }
                    int ci = Integer.parseInt(str2);
                    if (ci < 0 || MyBTree.MAX_CHILD-1 < ci) {
                        System.out.println("  Error: childIndex must be from 0 to " + (MyBTree.MAX_CHILD-1) + ".");
                    }else if ( in.nChilds - 1 < ci ){
                        System.out.println("  Error: child[" + ci + "] is null.");
                    }else{
                        currentNode = in.child[ci];
                        System.out.println("  Message: currentNode became node #" + currentNode.serial + ".");
                    }
                }
            } else {
                System.err.println("  I don't understand command: " + param[0]);
            }
        }
    }
}
