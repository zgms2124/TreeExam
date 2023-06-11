/**
 * 学习JAVA
 *
 * @项目名称：计算机目录树的基本操作
 * @计通2204李志杨
 * @Date：2023/5/22 - 05 - 22 - 20:49
 * @version： 1.0
 * @功能：实现计算机目录树的基本操作
 */
import java.io.File;
public class FileExplorer{
    public static class FileNode {
        private File file;
        private FileNode child;
        private FileNode brother;

        public FileNode(File file) {
            this.file = file;
            this.child = null;
            this.brother = null;
        }

        public File getFile() {
            return file;
        }

        public FileNode getchild() {
            return child;
        }

        public FileNode getbrother() {
            return brother;
        }

        public void setchild(FileNode child) {
            this.child = child;
        }

        public void setbrother(FileNode brother) {
            this.brother = brother;
        }
    }

    public static class FileSystem {
        private FileNode root;

        public FileSystem(File rootDirectory) {
            root = new FileNode(rootDirectory);
        }

        public void create(String path, boolean isDirectory) {
            File file = new File(root.getFile(), path);
            if (file.exists()) {
                System.out.println("当前文件（夹）已存在: " + path);
                return;
            }

            if (isDirectory) {
                if (!file.mkdirs()) {
                    System.out.println("无法创建文件夹: " + path);
                }
            } else {
                try {
                    if (!file.createNewFile()) {
                        System.out.println("无法创建文件: " + path);
                    }
                } catch (Exception e) {
                    System.out.println("无法创建文件: " + path);
                }
            }

            FileNode parentNode = findParentNode(file);
            if (parentNode != null) {
                //父结点不为空才置入
                FileNode newNode = new FileNode(file);
                if (parentNode.getchild() == null) {
                    //是第一个孩子
                    parentNode.setchild(newNode);
                } else {
                    //通过循环，找到最右边的，还没有兄弟结点的兄弟
                    FileNode brotherNode = parentNode.getchild();
                    while (brotherNode.getbrother() != null) {
                        brotherNode = brotherNode.getbrother();
                    }
                    brotherNode.setbrother(newNode);
                }
            }
        }

        public void delete(String path) {
            File file = new File(root.getFile(), path);
            if (!file.exists()) {
                System.out.println("未找到文件（夹）: " + path);
                return;
            }

            if (file.isDirectory()) {
                for (File child : file.listFiles()) {
                    delete(path + "/" + child.getName());
                }
            }

            if (!file.delete()) {
                System.out.println("无法删除文件（夹）: " + path);
            }

            FileNode parentNode = findParentNode(file);
            if (parentNode != null) {
                FileNode nodeToRemove = null;
                FileNode brotherNode = parentNode.getchild();
                if (brotherNode.getFile().equals(file)) {
                    //如果自己就是第一个孩子，那么把自己删除，父结点的第一个孩子变成自己的兄弟
                    parentNode.setchild(brotherNode.getbrother());
                    nodeToRemove = brotherNode;
                } else {
                    //通过循环，找到需要删除的结点
                    while (brotherNode.getbrother() != null) {
                        if (brotherNode.getbrother().getFile().equals(file)) {
                            nodeToRemove = brotherNode.getbrother();
                            brotherNode.setbrother(nodeToRemove.getbrother());
                            break;
                        }
                        brotherNode = brotherNode.getbrother();
                    }
                }

                //需要删除的结点的孩子、兄弟都置空
                if (nodeToRemove != null) {
                    nodeToRemove.setchild(null);
                    nodeToRemove.setbrother(null);
                }
            }
        }

        public void rename(String path, String newName) {
            File file = new File(root.getFile(), path);
            if (!file.exists()) {
                System.out.println("文件（夹）未找到: " + path);
                return;
            }

            String parentPath = file.getParent();
            String newFilePath = parentPath + File.separator + newName;
            File newFile = new File(newFilePath);
            if (file.renameTo(newFile)) {
                FileNode nodeToRename = findParentNode(file);
                if (nodeToRename != null) {
                    nodeToRename.getFile().renameTo(newFile);
                }
            } else {
                System.out.println("重命名失败: " + path);
            }
        }

        public void search(String path) {
            File file = new File(root.getFile(), path);
            if (file.exists()) {
                System.out.println("文件（夹）已找到: " + path);
            } else {
                System.out.println("文件（夹）未找到: " + path);
            }
        }

        public void traverseFileSystem() {
            traverse(root, 0);
        }

        private void traverse(FileNode node, int depth) {
            StringBuilder 缩进 = new StringBuilder();
            for (int i = 0; i < depth; i++) {
                缩进.append("  ");
            }

            System.out.println(缩进 + "- " + node.getFile().getName());

            FileNode child = node.getchild();
            while (child != null) {
                //通过循环遍历，先将一个结点的孩子全部遍历完，再退回上一层，将其孩子遍历完
                //也就是一种深度优先遍历
                traverse(child, depth + 1);
                child = child.getbrother();
            }
        }

        private FileNode findParentNode(File file) {
            File parentFile = file.getParentFile();
            if (parentFile != null) {
                FileNode parentNode = findNode(root, parentFile);
                return parentNode;
            }
            return null;
        }

        private FileNode findNode(FileNode currentNode, File file) {
            if (currentNode.getFile().equals(file)) {
                return currentNode;
            }

            FileNode child = currentNode.getchild();
            //通过循环遍历，找到目标文件
            while (child != null) {
                FileNode foundNode = findNode(child, file);
                if (foundNode != null) {
                    return foundNode;
                }
                child = child.getbrother();
            }

            return null;
        }
    }

    public static void main(String[] args) {
        File rootDirectory = new File("E:/文件夹测试");
        FileSystem fileSystem = new FileSystem(rootDirectory);

        fileSystem.create("文本夹", true);
        fileSystem.create("文本夹", true);
        fileSystem.create("文本夹/文本.txt", false);
        fileSystem.create("文本夹/文本.txt", false);
        fileSystem.create("音乐夹", true);
        fileSystem.create("音乐/歌曲.mp3", false);
        fileSystem.create("音乐夹/周杰伦", true);
        fileSystem.create("音乐夹/周杰伦/夜曲.mp3", false);
        fileSystem.create("图片", true);

        System.out.println("文件结构:");
        fileSystem.traverseFileSystem();

        fileSystem.delete("测试/文本.txt");

        System.out.println("\n删除后的结构:");
        fileSystem.traverseFileSystem();

        fileSystem.rename("音乐/歌曲.mp3", "新的歌名.mp3");
        fileSystem.rename("图片", "图库");

        System.out.println("\n重命名后的结构:");
        fileSystem.traverseFileSystem();

        fileSystem.search("音乐夹/歌曲.mp3");
        fileSystem.search("文本夹");
        fileSystem.search("音乐夹/歌曲.mp3");
        fileSystem.search("音乐夹/周杰伦/夜曲.mp3");
        fileSystem.search("音乐夹/新的歌名.mp3");
        fileSystem.search("测试");
        fileSystem.search("图片/湖.jpg");
    }
}




