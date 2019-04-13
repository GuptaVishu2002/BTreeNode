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
    String data1,data2,data3;
    LeafNode(String key, String data1, String data2, String data3){
        nextSerialNumber++;
        serial = nextSerialNumber;
        this.key  = key;
        this.data1 = data1;
        this.data2 = data2;
        this.data3 = data3;
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
        lf1 = new LeafNode("April", "The fourth month of the year","000","000");
        lf2 = new LeafNode("July",  "The seventh month of the year","010","010");
        lf3 = new LeafNode("joy",   "The passion or emotion excited by the acquisition or expectation of good","020","020");
        lf4 = new LeafNode("kid",   "A young goat","030","030");
        lf5 = new LeafNode("work",  "to use physical or mental effort to make or do something","040","040");
        lf6 = new LeafNode("year",  "a period of time equal to twelve months","050","050");
        lf7 = new LeafNode("zoo",   "a place where animals are kept for the public to look at and study","060","060");

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

    public LeafNode findNode(String key){
        if (root == null) return null;
        Node p = root;
        while (p instanceof InternalNode){
            InternalNode in = (InternalNode)p;
            int i = in.locateSubtree(key);
            p = in.child[i];
        }
        LeafNode ln = (LeafNode)p;
        if (key.equals(ln.key)){
            return ln;
        }else{
            return null;
        }
    }

    public boolean insertNode(String key, String data1, String data2, String data3){
        currentLeaf = null;

        if (root == null) {
            root = new LeafNode(key, data1, data2, data3);
            System.out.println("  insert case(1): btree is empty.");
            return true;
        } else {
            InsertAuxResult result = insertAux(null, -1, key, data1, data2, data3);
            if (result == null) {
                return false;
            }
            if (result.newNode != null) {
                InternalNode newNode = new InternalNode();
                System.out.println("  insert case(2): depth of btree increments.");
                newNode.nChilds  = 2;
                newNode.child[0] = root;
                newNode.child[1] = result.newNode;
                newNode.low[1]   = result.lowest;
                root = newNode;
            }
            return true;
        }
    }

    private InsertAuxResult insertAux(InternalNode pnode, int nth,
                                      String key, String data1, String data2, String data3) {
        Node thisNode;
        if (pnode == null) {
            thisNode = root;
        } else {
            thisNode = pnode.child[nth];
        }

        if (thisNode instanceof LeafNode) {
            LeafNode leaf = (LeafNode)thisNode;
            if (key.equals(leaf.key)){
                return null;
            } else {
                LeafNode newLeaf = new LeafNode(key, data1, data2, data3);
                if (key.compareTo(leaf.key) < 0) {
                    if (pnode == null) {
                        root = newLeaf;
                    } else {
                        pnode.child[nth] = newLeaf;
                    }
                    return new InsertAuxResult(leaf, leaf.key);
                } else {
                    return new InsertAuxResult(newLeaf, key);
                }
            }
        } else {
            InternalNode node = (InternalNode)thisNode;
            int pos = node.locateSubtree(key);
            InsertAuxResult result = insertAux(node, pos, key, data1, data2, data3);
            if (result == null || result.newNode == null) {
                return result;
            }
            if (node.nChilds < MAX_CHILD) {
            	System.out.println("  insert case(3-1): node don't separate.");
                for (int i = node.nChilds - 1; i > pos; i--) {
                    node.child[i+1] = node.child[i];
                    node.low  [i+1] = node.low  [i];
                }
                node.child[pos+1] = result.newNode;
                node.low  [pos+1] = result.lowest;
                node.nChilds++;
                return new InsertAuxResult(null, null);
            } else {
                InternalNode newNode = new InternalNode();
                if (pos < HALF_CHILD - 1) {
                	System.out.println("  insert case(3-2-1): new node regists exist node(left node).");
                    for (int i = HALF_CHILD-1, j = 0; i < MAX_CHILD; i++, j++) {
                        newNode.child[j] = node.child[i];
                        newNode.low  [j] = node.low  [i];
                    }
                    for (int i = HALF_CHILD-2; i > pos; i--) {
                        node.child[i+1] = node.child[i];
                        node.low  [i+1] = node.low  [i];
                    }
                    node.child[pos+1] = result.newNode;
                    node.low  [pos+1] = result.lowest;
                } else {
                	System.out.println("  insert case(3-2-2): new node regists exist node(right node).");
                    int j = MAX_CHILD - HALF_CHILD;
                    for (int i = MAX_CHILD-1; i >= HALF_CHILD; i--) {
                        if (i == pos) {
                            newNode.child[j]   = result.newNode;
                            newNode.low  [j--] = result.lowest;
                        }
                        newNode.child[j]   = node.child[i];
                        newNode.low  [j--] = node.low  [i];
                    }
                    if (pos < HALF_CHILD) {
                        newNode.child[0] = result.newNode;
                        newNode.low  [0] = result.lowest;
                    }
                }
                node   .nChilds = HALF_CHILD;
                newNode.nChilds = (MAX_CHILD + 1) - HALF_CHILD;
                return new InsertAuxResult(newNode, newNode.low[0]);
            }
        }
    }

    private boolean mergeNodes(InternalNode p, int x) {
        InternalNode  a = (InternalNode)p.child[x];
        InternalNode  b = (InternalNode)p.child[x+1];
        b.low[0] = p.low[x+1];

        int   an = a.nChilds;
        int   bn = b.nChilds;

        if (an + bn <= MAX_CHILD) {
            for (int i = 0; i < bn; i++) {
                a.child[i+an] = b.child[i];
                b.child[i]    = null;
                a.low  [i+an] = b.low  [i];
            }
            a.nChilds += bn;
            return true;
        } else {
            int  move;
            int n = (an + bn) / 2;
            if (an > n) {
                move = an - n;
                for (int i = bn - 1; i >= 0; i--) {
                    b.child[i+move] = b.child[i];
                    b.low  [i+move] = b.low  [i];
                }
                for (int i = 0; i < move; i++) {
                    b.child[i] = a.child[i+n];
                    a.child[i+n] = null;
                    b.low  [i] = a.low  [i+n];
                }
            } else {
                move = n - an;
                for (int i = 0; i < move; i++) {
                    a.child[i+an] = b.child[i];
                    a.low  [i+an] = b.low  [i];
                }
                for (int i = 0; i < bn - move; i++) {
                    b.child[i] = b.child[i+move];
                    b.child[i+move] = null;
                    b.low  [i] = b.low  [i+move];
                }
            }

            a.nChilds = n;
            b.nChilds = an + bn - n;

            p.low[x+1] = b.low[0];
            return false;
        }
    }

    private DeleteAuxResult deleteAux(Node thisNode, String key) {
    	if (thisNode instanceof LeafNode) {
		  LeafNode leaf = (LeafNode)thisNode;
		  if (leaf.key.equals(key)) {
		return new DeleteAuxResult(DeleteAuxResult.OK_REMOVED);
		  } else {
		return new DeleteAuxResult(DeleteAuxResult.NOT_FOUND);
		  }
		} else {
		  InternalNode node = (InternalNode)thisNode;
		  
		  boolean joined = false;
		  
		  int pos = node.locateSubtree(key);
		  DeleteAuxResult result = deleteAux(node.child[pos], key);
		  if (result.status == DeleteAuxResult.NOT_FOUND ||
		      result.status == DeleteAuxResult.OK) {
		    return result;
		  }
		  if (result.status == DeleteAuxResult.OK_NEED_REORG) {
		    int sub = (pos == 0) ? 0 : pos - 1;
		    joined = mergeNodes(node, sub);
		    if (joined) {
		      pos = sub + 1;
		    }
		  }
		  DeleteAuxResult myResult = new DeleteAuxResult(DeleteAuxResult.OK);
		  if (result.status == DeleteAuxResult.OK_REMOVED || joined) {
		    for (int i = pos; i < node.nChilds - 1; i++) {
		      node.child[i] = node.child[i+1];
		      node.low[i]   = node.low[i+1];
		    }
		    node.child[node.nChilds-1] = null;
		    node.nChilds--;
		    if (node.nChilds < HALF_CHILD) {
		      myResult.status = DeleteAuxResult.OK_NEED_REORG;
		    }
		  }
		  return myResult;
		}
    }

    public boolean deleteNode(String key){
    	currentLeaf = null;
 
		if (root == null) {
		  return false;
		} else {
		  DeleteAuxResult result = deleteAux(root, key);
		  if (result.status == DeleteAuxResult.NOT_FOUND) {
		    return false;
		  }
		  if (result.status == DeleteAuxResult.OK_REMOVED) {
		    root = null;
		  } else if (result.status == DeleteAuxResult.OK_NEED_REORG
		             && ((InternalNode)root).nChilds == 1) {
		    root = ((InternalNode)root).child[0];
		  }
		  return true;
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

class InsertAuxResult {
    Node   newNode;
    String lowest;
    InsertAuxResult(Node newNode, String lowest) {
        this.newNode = newNode;
        this.lowest  = lowest;
    }
}

class DeleteAuxResult {
    static final int OK            = 1;
    static final int OK_REMOVED    = 2;
    static final int OK_NEED_REORG = 3;
    static final int NOT_FOUND     = 4;
    int status;
    DeleteAuxResult(int a){
        status = a;
    }
}

class rp9_1 {
    public static void main (String[] argv){
        Scanner scan = new Scanner(System.in);
        MyBTree btree = new MyBTree();
        Node currentNode = null;

        while(true){
            System.out.print("command: ");
            String str = scan.nextLine();
            String[] param = str.split(" ", 0);
            
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
                    System.out.println("      data1: " + result.data1);
                    System.out.println("      data2: " + result.data2);
                    System.out.println("      data3: " + result.data3);
                }
            } else if ("locate".equals(param[0])){
                if (currentNode instanceof LeafNode){
                    System.out.println("  currentNode is LeafNode.");
                }else{
                    InternalNode in = (InternalNode)currentNode;
                    System.out.print("  key: ");
                    String str2;
                    if (param.length > 1){
                        System.out.println(param[1]);
                        str2 = new String(param[1]);
                    } else {
                        str2 = scan.nextLine();
                    }
                    System.out.println("  locateIndex(" + str2 + ") is " + in.locateSubtree(str2) + ".");
                }
            } else if ("insert".equals(param[0]) || "ins".equals(param[0])){
                System.out.print("   key: ");
                String str2;
                if (param.length > 1){
                    System.out.println(param[1]);
                    str2 = new String(param[1]);
                } else {
                    str2 = scan.nextLine();
                }
                System.out.print("  data1: ");
                String str3;
                if (param.length > 2){
                    System.out.println(param[2]);
                    str3 = new String(param[2]);
                } else {
                    str3 = scan.nextLine();
                }
                System.out.print("  data2: ");
                String str4;
                if (param.length > 3){
                    System.out.println(param[3]);
                    str4 = new String(param[3]);
                } else {
                    str4 = scan.nextLine();
                }
                System.out.print("  data3: ");
                String str5;
                if (param.length > 4){
                    System.out.println(param[4]);
                    str5 = new String(param[4]);
                } else {
                    str5 = scan.nextLine();
                }
                if (btree.insertNode(str2, str3, str4, str5)){
                    System.out.println("  New node(" + str2 + "," + str3 + "," + str4 + "," + str5 + ") is inserted.");
                }else{
                    System.out.println("  Insert is wrong.");
                }
            } else if ("delete".equals(param[0]) || "del".equals(param[0])){
                System.out.print("   key: ");
                String str2;
                if (param.length > 1){
                    System.out.println(param[1]);
                    str2 = new String(param[1]);
                } else {
                    str2 = scan.nextLine();
                }
                if (btree.deleteNode(str2)){
                    System.out.println("  node(" + str2 + ") is deleted.");
                }else{
                    System.out.println("  node(" + str2 + ") is not found.");
                }
            } else if ("equals".equals(param[0])){
                if (currentNode instanceof InternalNode){
                    System.out.println("  currentNode is InternalNode.");
                }else{
                    LeafNode ln = (LeafNode)currentNode;
                    System.out.print("  key: ");
                    String str2;
                    if (param.length > 1){
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
                    if (param.length > 1){
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
            }

        }
    }
}
