import java.util.Vector;

/**
 * @author Clov614
 * @version 1.0
 * DATE 2023/9/20
 * 自己的坦克
 */
public class Hero extends Tank {
    Shot shot = null;
    Vector<Shot> shots = new Vector<>();
    public Hero(int x, int y) {
        super(x, y);
    }

    // 射击
    public void shotEnemyTank() {
        // 创建shot对象，根据Hero对象的位置和方向来创建shot
        if (shots.size() >= 5)  // 子弹最多5颗
            return;
        switch (getDirect()) {
            case 0:
                shot = new Shot(getX() + 20, getY(), 0);
                break;
            case 1:
                shot = new Shot(getX() + 60, getY() + 20, 1);
                break;
            case 2:
                shot = new Shot(getX() + 20, getY() + 60, 2);
                break;
            case 3:
                shot = new Shot(getX(), getY() + 20, 3);
                break;
        }
        shots.add(shot);
        // 启动线程
        new Thread(shot).start();
    }
}
