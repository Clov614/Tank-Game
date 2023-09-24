/**
 * @author Clov614
 * @version 1.0
 * DATE 2023/9/21
 * 射击子弹
 */
public class Shot implements Runnable {
    int x;
    int y;  // 子弹的x y 坐标
    int direct = 0;
    int speed = 6;
    boolean isLive = true;

    public Shot(int x, int y, int direct) {
        this.x = x;
        this.y = y;
        this.direct = direct;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            // 根据方向改变 x，y坐标
            switch (direct) {
                case 0:
                    y -= speed;
                    break;
                case 1:
                    x += speed;
                    break;
                case 2:
                    y += speed;
                    break;
                case 3:
                    x -= speed;
                    break;
                default:
                    System.out.println("错误子弹方向");
            }
            // 测试子弹坐标
//            System.out.println("子弹= x=" + x + " y=" + y);
            // 子弹触碰边界情况
            // 子弹已经击中坦克不存活，退出
            if (!(x >= 0 && x <= 1000 && y >= 0 && y <= 750 && isLive)) {
                isLive = false;
                break;
            }
        }
    }
}
