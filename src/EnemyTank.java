import java.util.Vector;

/**
 * @author Clov614
 * @version 1.0
 * DATE 2023/9/21
 */
public class EnemyTank extends Tank implements Runnable {
    // 使用Vector 保存敌人的子弹
    Vector<Shot> shots = new Vector<>();
    boolean isLived = true;
    public EnemyTank(int x, int y) {
        super(x, y);
    }

    @Override
    public void run() {
        while (true) {
            // 判断 shots是否空，空了创建新子弹
            if (isLived && shots.isEmpty()) {
                Shot s = null;
                // 判断坦克方向 生成子弹
                switch (getDirect()) {
                    case 0:
                        s = new Shot(getX() + 20, getY(), 0);
                        break;
                    case 1:
                        s = new Shot(getX() + 60, getY() + 20, 1);
                        break;
                    case 2:
                        s = new Shot(getX() + 20, getY() + 60, 2);
                        break;
                    case 3:
                        s = new Shot(getX(), getY() + 20, 3);
                        break;
                }
                shots.add(s);
                new Thread(s).start();
            }
            // 根据tank方向来移动
            switch (getDirect()) {
                case 0:
                    for (int i = 0; i < 30; i++) {
                        moveUp();
                        timeSleep(50);
                    }
                    break;
                case 1:
                    for (int i = 0; i < 30; i++) {
                        moveRight();
                        // 休眠50ms
                        timeSleep(50);
                    }
                    break;
                case 2:
                    for (int i = 0; i < 30; i++) {
                        moveDown();
                        timeSleep(50);
                    }
                    break;
                case 3:
                    for (int i = 0; i < 30; i++) {
                        moveLeft();
                        timeSleep(50);
                    }
                    break;
            }
            // 随机改变坦克方向 0 - 3
            setDirect((int) (Math.random() * 4));
            // tank 死亡 结束线程
            if (!isLived) {
                break;
            }
        }
    }

    public void timeSleep(int m) {
        // 休眠50ms
        try {
            Thread.sleep(m);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
