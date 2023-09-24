import java.util.Vector;

/**
 * @author Clov614
 * @version 1.0
 * DATE 2023/9/20
 */
public class Tank {
    private int x;  // 坦克的横坐标
    private int y;  // 坦克的纵坐标
    private int direct; // 坦克方向 0 1 2 3
    private int speed = 3;  // 默认速度为5
    boolean isLived = true;
    static Vector<Tank> tanks = new Vector<>(); // all tanks in here

    // 移动输出
    public void printxy() {
        System.out.println("坦克 x=" + x + " y=" + y);
    }

    // 判断坦克是否重叠
    public boolean isTouchTank() {
        // 判断当前坦克方向
        switch (this.direct) {
            case 0:
                if (compareXY(this.x, this.y, this.x + 40, this.y))
                    return true;
               break;
            case 1:
                if (compareXY(this.x + 60, this.y, this.x + 60, this.y + 40))
                    return true;
                break;
            case 2:
                if (compareXY(this.x, this.y + 60, this.x + 40, this.y + 60))
                    return true;
                break;
            case 3:
                if (compareXY(this.x, this.y, this.x, this.y + 40))
                    return true;
                break;
        }
        return false;
    }
    /*比较本tank两个角是否在othertank的矩形中*/
    private boolean compareXY(int cx1, int cy1, int cx2, int cy2) {
        int[] area = new int[2]; // x  y
        for (int i = 0; i < tanks.size(); i++) {
            Tank tank = tanks.get(i);
            if (tank == this) continue; // 如果是自己的坦克则不比较
            int d = tank.getDirect();
            if (d == 0 || d == 2) { // 上下情况
                area[0] = tank.x + 40;
                area[1] = tank.y + 60;
            } else if (d == 1 || d == 3) { // 左右情况
                area[0] = tank.x + 60;
                area[1] = tank.y + 40;
            }
            // 判断当前坦克边角是否进入area
            // 第一个角
            if (cx1 >= tank.x
                    && cx1 <= area[0]
                    && cy1 >= tank.y
                    && cy1 <= area[1])
                return true;
            // 第二个角
            if (cx2 >= tank.x
                    && cx2 <= area[0]
                    && cy2 >= tank.y
                    && cy2 <= area[1])
                return true;
        }
        return false;
    }

    // tank移动方法
    public void moveUp() {
        if (y > 0 && !isTouchTank()) {
            y -= speed;
        }
    }
    public void moveRight() {
        if (x + 60 < 1000 && !isTouchTank()) {
            x += speed;
        }
    }
    public void moveDown() {
        if (y + 60 < 750 && !isTouchTank()) {
            y += speed;
        }
    }
    public void moveLeft() {
        if (x > 0 && !isTouchTank()) {
            x -= speed;
        }
    }

    public Tank(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getDirect() {
        return direct;
    }

    public void setDirect(int direct) {
        this.direct = direct;
    }
    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

}
