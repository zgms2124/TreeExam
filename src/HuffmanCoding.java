/**
 * 学习JAVA
 *
 * @项目名称：哈夫曼编码
 * @计通2204李志杨
 * @Date：2023/5/23 - 05 - 23 - 0:33
 * @version： 1.0
 * @功能：实现哈夫曼编码、并且实现文件的写入、读取等
 */
import java.util.*;

public class HuffmanCoding {
    public static class HuffmanNode{
        char value;
        int frequency;
        HuffmanNode left;
        HuffmanNode right;

        public HuffmanNode(char value, int frequency) {
            this.value = value;
            this.frequency = frequency;
        }
        public HuffmanNode(){

        }
    }
    public static Map<Character, String> huffmanCodes = new HashMap<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("请选择输入方式（1 - 文本输入；2 - 字符及频率输入）：");
        int option = scanner.nextInt();
        scanner.nextLine(); // 读取换行符

        Map<Character, Integer> frequencyMap;
        HuffmanNode root;

        switch (option) {
            case 1:
                System.out.println("请输入一段话：");
                String input = scanner.nextLine();

                if (input.isEmpty()) {
                    System.out.println("输入的内容不能为空");
                    return;
                }

                frequencyMap = buildFrequencyMap(input);
                root = buildHuffmanTree(frequencyMap);
                break;
            case 2:
                root = buildHuffmanTreeFromInput();
                if (root == null) {
                    return;
                }
                break;
            default:
                System.out.println("选择无效");
                return;
        }

        generateHuffmanCodes(root, "");

        System.out.println("哈夫曼编码表:");
        for (Map.Entry<Character, String> entry : huffmanCodes.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
    }



    public static Map<Character, Integer> buildFrequencyMap(String input) {
        Map<Character, Integer> frequencyMap = new HashMap<>();
        for (char c : input.toCharArray()) {
            if (Character.isLetter(c)) {
                frequencyMap.put(c, frequencyMap.getOrDefault(c, 0) + 1);
            }
        }
        return frequencyMap;
    }
    public static class myCmp implements Comparator<HuffmanNode>{
        @Override
        public int compare(HuffmanNode o1,HuffmanNode o2){
            return o1.frequency-o2.frequency;
        }
    }
    public static HuffmanNode buildHuffmanTree(Map<Character, Integer> frequencyMap) {
        PriorityQueue<HuffmanNode> pq = new PriorityQueue<>(new myCmp());

        for (Map.Entry<Character, Integer> entry : frequencyMap.entrySet()) {
            pq.add(new HuffmanNode(entry.getKey(), entry.getValue()));
        }

        while (pq.size() > 1) {
            HuffmanNode leftNode = pq.poll();
            HuffmanNode rightNode = pq.poll();

            HuffmanNode parentNode = new HuffmanNode('\0', leftNode.frequency + rightNode.frequency);
            parentNode.left = leftNode;
            parentNode.right = rightNode;

            pq.add(parentNode);
        }

        return pq.poll();
    }

    public static void generateHuffmanCodes(HuffmanNode node, String code) {
        if (node == null) {
            return;
        }

        if (node.left == null && node.right == null) {
            huffmanCodes.put(node.value, code);
        }

        generateHuffmanCodes(node.left, code + "0");
        generateHuffmanCodes(node.right, code + "1");
    }
       public static HuffmanNode buildHuffmanTreeFromInput() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("请输入字符及其频率（格式：字符1 频率1 字符2 频率2 ...）：");
        String input = scanner.nextLine();

        scanner.close();

        String[] tokens = input.split(" ");
        if (tokens.length % 2 != 0) {
            System.out.println("输入格式错误");
            return null;
        }

        Map<Character, Integer> frequencyMap = new HashMap<>();
        for (int i = 0; i < tokens.length; i += 2) {
            char character = tokens[i].charAt(0);
            int frequency = Integer.parseInt(tokens[i + 1]);
            frequencyMap.put(character, frequency);
        }

        return buildHuffmanTree(frequencyMap);
    }

}




