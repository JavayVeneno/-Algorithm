package DataStructure.tree;

import com.sun.javafx.font.t2k.T2KFactory;
import common.FileOperation;

import java.util.ArrayList;

public class AVLTree<K extends Comparable<K>,V> {
    private class Node{
        public K key;
        public V value;
        public Node left,right;
        public int height;
        public Node(K key,V value){
            this.key = key;
            this.value = value;
            this.left=null;
            this.right = null;
            this.height=1;
        }
    }

    private int size;
    private Node root;


    public int size() {
        return size;
    }


    public boolean isEmpty() {
        return size==0;
    }



    public V get(K key) {
        Node node = getNode(key);
        return null==node?null:node.value;
    }


    public boolean contains(K key) {

        return getNode(key)!=null;
    }


    private int getHeight(Node node){
        if(node==null){
            return 0;
        }
        return node.height;
    }

    public void add(K key, V value) {
        root = add(root,key,value);
    }

    private Node add(Node node, K key, V value) {
        if(node == null){
            size++;
            return new Node(key,value);
        }
        if(key.compareTo(node.key)<0){
            node.left = add(node.left,key,value);
        }
        else if(key.compareTo(node.key)>0){
            node.right = add(node.right,key,value);
        }
        else {
            node.value=value;
        }

        node.height = 1+Math.max(getHeight(node.left),getHeight(node.right));
        int balance = getBalanceFactor(node);
        if(Math.abs(balance)>1){
            System.out.println("unBalance: "+balance);
        }
        // 旋转时机
        if(balance>1 && getBalanceFactor(node.left)>=0){
            // 如果当前节点打破平衡了,并且左子大于右子(即左斜),将右旋转
            return rightRotate(node);
        }
        if(balance<-1 && getBalanceFactor(node.right)<=0){
            // 如果当前节点打破平衡了,并且左小于右子(即右斜),将左旋转
            return leftRotate(node);
        }

        return node;
    }

    //         y                         x
    //        / \                      /  \
    //      T4   x    左旋转          y     z
    //         / \     ----->       / \   / \
    //        T3  z               T4  T3 T1 T2
    //           / \
    //         T1  T2

    private Node leftRotate(Node y) {
        Node x = y.right;
        Node T3 = x.left;
        x.left = y;
        y.right = T3;
        // 更新height
        y.height = 1 + Math.max(getHeight(y.left),getHeight(y.right));
        x.height = 1 + Math.max(getHeight(x.left),getHeight(x.right));
        return x;
    }

    //         y                        x
    //        / \                      / \
    //       x   T4    右旋转         z    y
    //      / \        ----->       / \   / \
    //     z  T3                   T1 T2 T3 T4
    //    / \
    //   T1 T2

    private Node rightRotate(Node y) {

        Node x = y.left;
        Node T3 = x.right;
        x.right=y;
        y.left = T3;

        // 更新height
        y.height = 1+Math.max(getHeight(y.left),getHeight(y.right));
        x.height = 1+Math.max(getHeight(x.left),getHeight(x.right));

        return x;
    }

    private int getBalanceFactor(Node node) {
        if(node==null){
            return 0;
        }
        return getHeight(node.left)-getHeight(node.right);
    }


    public void set(K key, V newValue) {
        Node node = getNode(root, key);
//        if(node!=null){
//            add(key,newValue);
//        }
        if(node == null){
            throw new IllegalArgumentException("key doesn't exists!");
        }
        node.value=newValue;
    }


    public V remove(K key) {
        Node node = getNode(key);
        root = remove(root,key);
        return node.value;
    }

    private Node minimum(Node node){
        if(node == null){
            return null;
        }
        if(node.left !=null){
            return minimum(node.left);
        }
        return node;
    }

    private Node removeMin(Node node) {
        if(node.left == null){
            Node right = node.right;
            node.right=null;
            size--;
            return right;

        }
        node.left = removeMin(node.left);
        return node;
    }

    private Node remove(Node node, K key) {

        if(node==null){
            return null;
        }
        if(key.compareTo(node.key)>0){
            node.right = remove(node.right,key);
            return node;
        }
        else if(key.compareTo(node.key)<0){
            node.left = remove(node.left,key);
            return node;
        }
        else{
            if(node.left==null){
                Node right = node.right;
                node.right = null;
                size--;
                return right;
            }
            else if(node.right==null){
                Node left = node.left;
                node.left = null;
                size--;
                node = left;
                return node;
            }else{
                Node successor = minimum(node.right);
                successor.right = removeMin(node.right);
                successor.left = node.left;
                node.left = node.right = null;
                return successor;
            }
        }

    }



    private Node getNode(K key){

        return getNode(root, key);

    }

    private Node getNode(Node node, K key) {
        if(node==null){
            return null;
        }
        if(node.key.compareTo(key)==0){
            return node;
        }
        else if(node.key.compareTo(key)<0){
            return getNode(node.right,key);
        }
        else{
            return getNode(node.left,key);
        }
    }


    public boolean isBalanced() {

        return isBalanced(root);

    }

    private boolean isBalanced(Node node) {

        if(node==null){
            return true;
        }
        int balance = getBalanceFactor(node);
        if(Math.abs(balance)>1){
            return false;
        }
        return isBalanced(node.left) && isBalanced(node.right);

    }

    public boolean isBST() {
        ArrayList<K> keys = new ArrayList<>();
        inOrder(root,keys);
        for (int i = 1; i < keys.size(); i++) {
            if(keys.get(i).compareTo(keys.get(i-1))<0){
                return false;
            }
        }
        return true;
    }

    private void inOrder(Node node, ArrayList<K> keys) {
        if(node == null){
            return;
        }
        inOrder(node.left,keys);
        keys.add(node.key);
        inOrder(node.right,keys);
    }

    public static void main(String[] args) {
        ArrayList<String> words = new ArrayList<>(200000);
        FileOperation.readFile("src/PrideAndPrejudice.txt",words);

        long time = System.currentTimeMillis();
        AVLTree<String,Integer> map = new AVLTree<>();
        for (String word : words) {
            if(map.contains(word)){
                map.set(word,map.get(word)+1);
            }else{
                map.add(word,1);
            }
        }
        System.out.println(System.currentTimeMillis()-time+"ms");
        System.out.println("总词量为 :"+words.size());
        System.out.println("词汇量为 :"+map.size());
        System.out.println("pride 出现的次数 :"+map.get("pride"));
        System.out.println("prejudice 出现的次数:"+map.get("prejudice"));
        System.out.println("isBST: "+map.isBST());
        System.out.println("isBalance : "+map.isBalanced());

    }

}
