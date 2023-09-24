/**
 * @author Clov614
 * @version 1.0
 * DATE 2023/9/22
 * 炸弹
 */
public class Bomb {
    int x, y;
    int life = 12; // 炸弹的生命周期
    boolean isLived = true; // 是否存活

    public Bomb(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void lifeDown() {
        if (life > 0) {
            life--;
        } else {
            isLived = false;
        }
    }
}
