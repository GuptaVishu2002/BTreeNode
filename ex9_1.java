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
            	System.out.println("  Message: currentNode became node " + child[i] + ".\n");
                return i;
            }
            else
            System.out.println("  Message: currentNode became node " + child[i-1] + ".\n");
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
        LeafNode     lf1,lf2,lf3,lf4,lf5,lf6,lf7,lf8;
        InternalNode in1, in2, in3, in4, in5, in6, in7;

        /* LeafNode */
        lf1 = new LeafNode("April", "The fourth month of the year");
        lf2 = new LeafNode("July",  "The seventh month of the year");
        lf3 = new LeafNode("joy",   "The passion or emotion excited by the acquisition or expectation of good");
        lf4 = new LeafNode("kid",   "A young goat");
        lf5 = new LeafNode("temple",   "a building devoted to the worship of a god or gods");
        lf6 = new LeafNode("work",  "to use physical or mental effort to make or do something");
        lf7 = new LeafNode("year",  "a period of time equal to twelve months");
        lf8 = new LeafNode("zoo",   "a place where animals are kept for the public to look at and study");


        /* Internal Node */
        in1 = new InternalNode();
        in2 = new InternalNode();
        in3 = new InternalNode();
        in4 = new InternalNode();
        in5 = new InternalNode();
        in6 = new InternalNode();
        in7 = new InternalNode();

        /* set child node */
        in4.nChilds = 2;
        in4.child[0] = lf1;
        in4.low[1] = lf2.getMinKey();
        in4.child[1] = lf2;

        in5.nChilds = 2;
        in5.child[0] = lf3;
        in5.low[1] = lf4.getMinKey();
        in5.child[1] = lf4;
        
        in6.nChilds = 2;
        in6.child[0] = lf5;
        in6.low[1] = lf6.getMinKey();
        in6.child[1] = lf6;
        
        in7.nChilds = 2;
        in7.child[0] = lf7;
        in7.low[1] = lf8.getMinKey();
        in7.child[1] = lf8;

		in2.nChilds = 2;
        in2.child[0] = in4;
        in2.low[1] = in5.getMinKey();
        in2.child[1] = in5;
        
        in3.nChilds = 2;
        in3.child[0] = in6;
        in3.low[1] = in7.getMinKey();
        in3.child[1] = in7;
        
        in1.nChilds = 2;
        in1.child[0] = in2;
        in1.low[1] = in3.getMinKey();
        in1.child[1] = in3;
       

        /* root NODE */
        root = in1;

        return;

    }

    public LeafNode findNode(String key){
    	currentLeaf = null;
    	if (root == null) return null;
		Node p = root;
		
		while (p instanceof InternalNode){  
		  InternalNode in = (InternalNode)p;
		  p = in.child[in.locateSubtree(key)];  
		}
		LeafNode ln = (LeafNode)p;
		if (key.compareTo(ln.key) == 0){ 
			currentLeaf = ln;
			return currentLeaf;
		}else{
			return null;
		}
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

class ex9_1 {
    public static void main (String[] argv){
        Scanner scan = new Scanner(System.in);
        MyBTree btree = new MyBTree();
        Node currentNode = null;

        while(true){
            System.out.print("command: ");
            String str = scan.nextLine();
            String[] param = str.split(" ",0);

            if ("quit".equals(param[0]) || "halt".equals(param[0]) || "exit".equals(param[0])) {
                break;
            } else if ("sample".equals(param[0]) || "sample1".equals(param[0])) {
                System.out.println("  SampleData is registed.");
                btree.set_sample1();
                currentNode = btree.root;
            } else if ("root".equals(param[0])){
                System.out.println("  Message: currentNode became root.");
                currentNode = btree.root;
                if (currentNode != null) System.out.println("  currentNode: " + currentNode);
            } else if ("show".equals(param[0])){
                System.out.println("" + btree);
            } else if ("find".equals(param[0]) || "search".equals(param[0])){
                System.out.print("  key: ");
                String str2;
                if (param.length > 1){
                    System.out.println(param[1]);
                    str2 = new String(param[1]);
                } else {
                    str2 = scan.nextLine();
                }
                LeafNode result = btree.findNode(str2);
                if (result == null) {
                    System.out.println("  " + str2 + " is not found.");
                }else{
                    System.out.println("  " + str2 + " is found.");
                    System.out.println("  LeafNode: " + result);
                    System.out.println("      data: " + result.data);
                }
            } else if ("locate".equals(param[0])){
                if (currentNode instanceof LeafNode){
                    System.out.println("  currentNode is LeafNode.");
                }else{
                    InternalNode in = (InternalNode)currentNode;
                    System.out.print("  key: ");
                    String str2;
                    if (param.length > 1) {
                        System.out.println(param[1]);
                        str2 = new String(param[1]);
                    } else {
                        str2 = scan.nextLine();
                    }
                    System.out.println("  locateIndex(" + str2 + ") is " + in.locateSubtree(str2) + ".");
                }
            } else if ("equals".equals(param[0])){
                if (currentNode instanceof InternalNode){
                    System.out.println("  currentNode is InternalNode.");
                }else{
                    LeafNode ln = (LeafNode)currentNode;
                    System.out.print("  key: ");
                    String str2;
                    if (param.length > 1) {
                        System.out.println(param[1]);
                        str2 = new String(param[1]);
                    } else {
                        str2 = scan.nextLine();
                    }
                    System.out.println("  \"" + ln.key + "\".equals(\"" + str2 + "\") = "
                                       + ln.key.equals(str2) + ".");
                }
            } else if ("child".equals(param[0])) {
                if (currentNode instanceof LeafNode) {
                    System.out.println("  currentNode is LeafNode.");
                }else{
                    InternalNode in = (InternalNode)currentNode;
                    System.out.print("  childIndex: ");
                    String str2;
                    if (param.length > 1) {
                        System.out.println(param[1]);
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
                        if (currentNode != null) System.out.println("  currentNode: " + currentNode);
                    }
                }
            } else {
                System.err.println("  I don't understand command: " + param[0]);
            }

        }
    }
}
