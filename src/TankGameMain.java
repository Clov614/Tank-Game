import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Scanner;

/**
 * @author Clov614
 * @version 1.0
 * DATE 2023/9/20
 * 坦克游戏框架
 */
public class TankGameMain extends JFrame {
    // 定义MyPanel
    MyPanel mp = null;
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        TankGameMain tankGame = new TankGameMain();
    }

    public TankGameMain() {
        System.out.println("请选择是否继续游戏(1.重新开始 2.继续游戏): ");
        String next = sc.next();
        mp = new MyPanel(next);
        // 启动重绘线程
        Thread thread = new Thread(mp);
        thread.start();
        this.add(mp); // 游戏绘图区域
        this.addKeyListener(mp);
        this.setSize(1300, 900);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);

        // 在JFrame中增加相应关闭窗口的事件监听
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.out.println("Listened to the window close");
                Recorder.keepRecord();
                System.exit(0);
            }
        });
    }
}

// TODO 完善清空敌方坦克显示 victory
// TODO 击杀坦克声音 计时
// TODO 完善地形方块河流类 // TODO 完善关卡地图
