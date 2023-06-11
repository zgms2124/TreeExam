import sun.reflect.generics.tree.Tree;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

/**
 * 学习JAVA
 *
 * @项目名称：二叉树的建立与遍历
 * @子庚木上
 * @Date：2023/5/22 - 05 - 22 - 20:12
 * @version： 1.0
 * @功能：二叉树的建立与遍历
 */
public class TreeCreatAndTraverse {
    public static void main(String[] args) {
        TreeNode root=null;
        Scanner sc=new Scanner(System.in);
        System.out.println("请输入先序序列：");
        String str=sc.nextLine();
        if(str.length()==0||str.charAt(0)==' '){
            System.out.println("请不要让根结点为空！");
            System.exit(0);
        }
        //输入字符串，将空格视为有效字符
        System.out.println(str);
        //打印验证输入正确
        Queue <Character>q=new LinkedList<>();
        //入队列，便于建树
        for(int i=0;i<str.length();i++){
            q.add(str.charAt(i));
        }
        root=createTree(q);
        System.out.println("先序序列如下：");
        preTraverse(root);
        System.out.println();
        System.out.println("中序序列如下：");
        inTraverse(root);
        System.out.println();
        System.out.println("后序序列如下：");
        posTraverse(root);
    }
    private static class TreeNode{
        char value;
        TreeNode left;
        TreeNode right;
        public TreeNode(char value) {
            this.value = value;
        }
        public TreeNode(){
        }

    }

    private static TreeNode createTree(Queue <Character>q) {
        if(q==null||q.size()==0) return null;
        char c=q.poll();
        if(c==' ')return null;
        TreeNode root=new TreeNode(c);
        root.left=createTree(q);
        root.right=createTree(q);
        //递归调用
        return root;
    }
    public static void preTraverse(TreeNode root){
        if(root==null){
            return;
        }
        System.out.print(root.value);
        preTraverse(root.left);
        preTraverse(root.right);
    }
    public static void inTraverse(TreeNode root){
        if(root==null){
            return;
        }
        inTraverse(root.left);
        System.out.print(root.value);
        inTraverse(root.right);
    }
    public static void posTraverse(TreeNode root){
        if(root==null){
            return;
        }
        posTraverse(root.left);
        posTraverse(root.right);
        System.out.print(root.value);
    }
}
