import java.io.*;
import java.util.Vector;

/**
 * @author Clov614
 * @version 1.0
 * DATE 2023/9/23
 * 该类用于记录相关信息，和文件交互
 */
public class Recorder {
    // 我方击毁坦克数
    private static int allEnemyTankNum = 0;

    // IO相关准备
    private static FileWriter fw = null;
    private static BufferedWriter bw = null;
    private static BufferedReader br = null;

    private static File dir = new File("");
    private static String recordFile = "\\source\\myRecord.txt";
    private static Vector<Tank> tanks = Tank.tanks;
    private static Vector<Node> nodes = new Vector<>();

    // 读取record文件方法、恢复相关信息
    public static Vector<Node> getNodesAndEnemyTankRec() {
        try {
            br = new BufferedReader(new FileReader(dir.getCanonicalFile() + recordFile));
            allEnemyTankNum = Integer.parseInt(br.readLine());
            String line;
            while ((line = br.readLine()) != null) {
                String[] xyd = line.split(" ");
                Node node = new Node(Integer.parseInt(xyd[0]),
                        Integer.parseInt(xyd[1]),
                        Integer.parseInt(xyd[2]));
                nodes.add(node);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (br != null) br.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return nodes;
    }

    // 游戏退出保存 allEnemyTankNum
    public static void keepRecord() {
        try {
            bw = new BufferedWriter(new FileWriter(dir.getCanonicalFile()  + recordFile));
            bw.write(allEnemyTankNum + "\r\n");
            // 遍历坦克记录坦克信息
            for (int i = 0; i < tanks.size(); i++) {
                Tank tank = tanks.get(i);
                if (tank instanceof EnemyTank && tank.isLived) {
                    String record = tank.getX() + " " + tank.getY() + " " + tank.getDirect();
                    bw.write(record + "\r\n");
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (bw != null) bw.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static int getAllEnemyTankNum() {
        return allEnemyTankNum;
    }

    public static void addAllEnemyTankNum() {
        Recorder.allEnemyTankNum++;
    }

    public static File getDir() {
        return dir;
    }

    public static String getRecordFile() throws IOException {
        return dir.getCanonicalPath() + recordFile;
    }
}
