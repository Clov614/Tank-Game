import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

/**
 * @author Clov614
 * @version 1.0
 * DATE 2023/9/20
 * 坦克大战的绘图区
 */
public class MyPanel extends JPanel implements KeyListener, Runnable {
    // 定义我的坦克
    Hero hero = null;
    /*Vector<EnemyTank> enemyTanks = new Vector<>();*/ // 废弃
    // 定义一个Vector用于存放炸弹
    // 当子弹击中tank加入一个Bomb对象
    Vector<Bomb> bombs = new Vector<>();
    // 用于恢复数据的Nodes
    private Vector<Node> nodes = new Vector<>();

    // 初始化炸弹图片
    Image image1 = null;
    Image image2 = null;
    Image image3 = null;
    private int enemySize = 7;
    public MyPanel(String key) {
        File file = null;
        try {
            file = new File(Recorder.getRecordFile());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (file.exists()) {
            nodes = Recorder.getNodesAndEnemyTankRec();
        } else {
            System.out.println("文件不存在，只能开启新游戏");
            key = "1";
        }

        hero = new Hero(150, 500);
        Tank.tanks.add(hero);
        // 初始化敌人坦克
        switch (key) {
            case "1":   // 初始化Etanks
                initEnemyTanks();
                break;
            case "2":   // rec Etanks
                initEnemyTanks(nodes);
                break;
            default:
                System.out.println("输入的参数有误(默认初始化游戏)");
                initEnemyTanks();
        }
        // init image
        String basePath = System.getProperty("user.dir") + "/source/";

        image1 = Toolkit.getDefaultToolkit().getImage(basePath + "bomb_1.gif");
        image2 = Toolkit.getDefaultToolkit().getImage(basePath + "bomb_2.gif");
        image3 = Toolkit.getDefaultToolkit().getImage(basePath + "bomb_3.gif");

        try {
            new AePlayWave(Recorder.getDir().getCanonicalFile() + "\\source\\111.wav").start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private void initEnemyTanks() {
        // 初始化敌人坦克
        for (int i = 0; i < enemySize; i++) {
            EnemyTank enemyTank = new EnemyTank(100 * (i + 1), 0);
            // 启动敌方坦克线程
            new Thread(enemyTank).start();
            enemyTank.setDirect(2);
            // 给敌人坦克加入一颗子弹
            Shot shot = new Shot(enemyTank.getX() + 20, enemyTank.getY() + 60, enemyTank.getDirect());
            // 加入子弹
            enemyTank.shots.add(shot);
            // 启动子弹线程
            new Thread(shot).start();
            Tank.tanks.add(enemyTank); // 将坦克加入坦克向量中
        }
    }
    private void initEnemyTanks(Vector<Node> nodes) {
        // 恢复敌人坦克
        for (int i = 0; i < nodes.size(); i++) {
            Node node = nodes.get(i);
            EnemyTank enemyTank = new EnemyTank(node.getX(), node.getY());
            // 启动敌方坦克线程
            new Thread(enemyTank).start();
            enemyTank.setDirect(node.getDirect());
            // 给敌人坦克加入一颗子弹
            Shot shot = new Shot(enemyTank.getX() + 20, enemyTank.getY() + 60, enemyTank.getDirect());
            // 加入子弹
            enemyTank.shots.add(shot);
            // 启动子弹线程
            new Thread(shot).start();
            Tank.tanks.add(enemyTank); // 将坦克加入坦克向量中
        }
    }
    /*显示游戏信息*/
    private void showInfo(Graphics g) {

        // 绘制玩家成绩
        g.setColor(Color.BLACK);
        Font font = new Font("宋体", Font.BOLD, 25);
        g.setFont(font);

        g.drawString("您累计击毁敌方坦克", 1020, 30);

        drawTank(1020, 60, g, 0, 0); // 绘制一个敌方坦克
        g.setColor(Color.BLACK);
        g.drawString( Recorder.getAllEnemyTankNum()+"", 1080, 100);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.fillRect(0, 0, 1000, 750); // 填充矩形 默认黑
        // 绘制Info
        showInfo(g);
        // 画出坦克-封装方法
        // 画出tanks
        Tank tank;
        Vector<Shot> shots;
        int tankT = 0;
        for (int i = 0; i < Tank.tanks.size(); i++) {
            tank = Tank.tanks.get(i);
            if (tank.isLived) { // tank存活着
                if (tank instanceof Hero) { // 如果是hero 换个type
                    tankT = 1;
                    shots = ((Hero) tank).shots;
                } else {
                    tankT = 0;
                    shots = ((EnemyTank) tank).shots;
                }
                drawTank(tank.getX(), tank.getY(), g, tank.getDirect(), tankT);
                for (int j = 0; j < shots.size(); j++) {
                    Shot shot = shots.get(j);
                    if (shot.isLive) { // 画出存活子弹
                        g.draw3DRect(shot.x, shot.y, 2, 2, false);
                    } else {  // 删除死亡子弹
                        if (tankT == 0) ((EnemyTank) tank).shots.remove(shot);
                        if (tankT == 1) ((Hero) tank).shots.remove(shot);
                    }
                }
            }
        }
        // 画出炸弹集合
        for (int i = 0; i < bombs.size(); i++) {
            // 取出炸弹
            Bomb bomb = bombs.get(i);
            if (bomb.life > 6) {
                g.drawImage(image1, bomb.x, bomb.y, 60, 60, this);
            } else if (bomb.life > 3) {
                g.drawImage(image2, bomb.x, bomb.y, 60, 60, this);
            } else {
                g.drawImage(image3, bomb.x, bomb.y, 60, 60, this);
            }
            bomb.lifeDown();
            if (bomb.life <= 0) {
                bombs.remove(bomb);
            }
        }
    }

    /**
     * 绘制坦克
     * @param x 坦克的左上角 x 坐标
     * @param y 坦克的左上角 y 坐标
     * @param g 画笔
     * @param direct 坦克方向(上下左右)
     * @param type 坦克类型 0: 我们的坦克 1: 敌人的坦克
     */
    public void drawTank(int x, int y, Graphics g, int direct, int type) {
        switch (type) {
            case 0: // 敌人的坦克
                g.setColor(Color.cyan);
                break;
            case 1: // 我们的坦克
                g.setColor(Color.yellow);
                break;
        }

        // 根据坦克的方向绘制tank 上 右 下 左 0 1 2 3
        switch (direct) {
            case 0:
                g.fill3DRect(x, y, 10, 60, false);  // left履带
                g.fill3DRect(x + 30, y, 10, 60, false); // right履带
                g.fill3DRect(x + 10, y + 10, 20, 40, false); // body
                g.fillOval(x + 10, y + 20, 20, 20);         // 盖子
                g.drawLine(x + 20, y + 30, x + 20, y);              // 炮管
                break;
            case 1:
                g.fill3DRect(x, y, 60, 10, false);  // left履带
                g.fill3DRect(x, y + 30, 60, 10, false); // right履带
                g.fill3DRect(x + 10, y + 10, 40, 20, false); // body
                g.fillOval(x + 20, y + 10, 20, 20);         // 盖子
                g.drawLine(x + 30, y + 20, x + 60, y + 20);              // 炮管
                break;
            case 2:
                g.fill3DRect(x, y, 10, 60, false);  // left履带
                g.fill3DRect(x + 30, y, 10, 60, false); // right履带
                g.fill3DRect(x + 10, y + 10, 20, 40, false); // body
                g.fillOval(x + 10, y + 20, 20, 20);         // 盖子
                g.drawLine(x + 20, y + 30, x + 20, y + 60);              // 炮管
                break;
            case 3:
                g.fill3DRect(x, y, 60, 10, false);  // left履带
                g.fill3DRect(x, y + 30, 60, 10, false); // right履带
                g.fill3DRect(x + 10, y + 10, 40, 20, false); // body
                g.fillOval(x + 20, y + 10, 20, 20);         // 盖子
                g.drawLine(x + 30, y + 20, x, y + 20);              // 炮管
                break;
            default:
                System.out.println("未作任何处理");
        }
    }

    // 判断子弹是否击中敌方坦克
    public void hitTank(Shot s, Tank tank) {
        int isHit = 0;
        switch (tank.getDirect()) {
            case 0:
            case 2:
                if (s.x > tank.getX() && s.x < tank.getX() + 40
                && s.y > tank.getY() && s.y < tank.getY() + 60) {
                    System.out.println("击中坦克");
                    s.isLive = false;
                    tank.isLived = false;
                    // 创建Bomb对象, and加入集合
                    Bomb bomb = new Bomb(tank.getX(), tank.getY());
                    bombs.add(bomb);
                    // 击中坦克删除坦克
                    removeTank(tank);
                    isHit = 1;
                }
                break;
            case 1:
            case 3:
                if (s.x > tank.getX() && s.x < tank.getX() + 60
                        && s.y > tank.getY() && s.y < tank.getY() + 40) {
                    System.out.println("击中坦克");
                    s.isLive = false;
                    tank.isLived = false;
                    // 创建Bomb对象, and加入集合
                    Bomb bomb = new Bomb(tank.getX(), tank.getY());
                    bombs.add(bomb);
                    // 击中坦克删除坦克
                    removeTank(tank);
                    isHit = 1;
                }
                break;
        }
        if (isHit == 1 && tank instanceof EnemyTank) {   // 击中了EnemyTank
            Recorder.addAllEnemyTankNum();
        }
    }

    public void removeTank(Tank tank) {
        if (tank instanceof Hero)
            hero = null;
        Tank.tanks.remove(tank);
        System.out.println("删除"+ tank.getClass().getName() +"坦克");
    }

    public void hitEnemyTank() {
        if (hero == null)
            return;
        for (int j = 0; j < hero.shots.size(); j++) {
            Shot hshot = hero.shots.get(j);
            // 判断是否击中敌人坦克
            if (hshot != null && hshot.isLive) { // 坦克是否存活
                // 遍历所有敌方坦克
                for (int i = 0; i < Tank.tanks.size(); i++) {
                    Tank tank = Tank.tanks.get(i);
                    if (tank instanceof Hero) continue; // 跳过hero
                    EnemyTank enemyTank = (EnemyTank) tank;
                    hitTank(hshot, enemyTank);
                }
            }
        }
    }

    public void hitHero() {
        for (int i = 0; i < Tank.tanks.size(); i++) {
            Tank tank = Tank.tanks.get(i);
            if (tank instanceof Hero) continue; // 跳过hero
            EnemyTank enemyTank = (EnemyTank) tank;
            for (int j = 0; j < enemyTank.shots.size(); j++) {
                Shot shot = enemyTank.shots.get(j);
                if (hero != null && shot.isLive && hero.isLived) {
                    hitTank(shot, hero);
                }
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }
    public void heroControl(KeyEvent e) {
        if (hero == null) return;
        if (e.getKeyCode() == KeyEvent.VK_W) {
            hero.setDirect(0);
            hero.moveUp();
        } else if (e.getKeyCode() == KeyEvent.VK_D) {
            hero.setDirect(1);
            hero.moveRight();
        } else if (e.getKeyCode() == KeyEvent.VK_S) {
            hero.setDirect(2);
            hero.moveDown();
        } else if (e.getKeyCode() == KeyEvent.VK_A) {
            hero.setDirect(3);
            hero.moveLeft();
        }
        // 设计监听
        if (e.getKeyCode() == KeyEvent.VK_J) {
            hero.shotEnemyTank();
        }
    }
    @Override
    public void keyPressed(KeyEvent e) {
        heroControl(e);
        this.repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            hitEnemyTank();
            hitHero();
            this.repaint();
        }
    }
}
