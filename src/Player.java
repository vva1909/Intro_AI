import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;
import java.util.ArrayList;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class Player {
    int weight_limit;
    GamePanel gp;
    int x, y;
    int n;
    int[][] fog;
    Clip clip;
    File sound;

    // Biến cho DFS
    private ArrayList<Point> path;
    private boolean[][] visited;
    private int targetX, targetY;

    public Player(GamePanel gp) {
        this.gp = gp;
        x = 0;
        y = 0;
        n = gp.B_HEIGHT / gp.DOT_SIZE;
        fog = new int[n][n];
        weight_limit = 20;
        visited = new boolean[n][n];
        path = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                fog[i][j] = 1;
                visited[i][j] = false;
            }
        }
        sound = new File("coin.wav");
        try {
            this.clip = AudioSystem.getClip();
            this.clip.open(AudioSystem.getAudioInputStream(sound));
        } catch (Exception ignored){}

    }

    public void checkCollision() {
        int i = x/gp.DOT_SIZE;
        int j = y/gp.DOT_SIZE;

        for (int i1 = max(0, i - 1); i1 <= min(n - 1, i + 1); i1++) {
            for (int j1 = max(0, j - 1); j1 <= min(n - 1, j + 1); j1++) {
                fog[i1][j1] = 0;
            }
        }

        if (x == gp.map.treasure_x && y == gp.map.treasure_y) {
            gp.game_result = "Congratulation!!!";
            gp.inGame = false;
        }
    }

    public void FindSolution(int type) {
        // Reset visited và path
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                visited[i][j] = false;
            }
        }
        path.clear();

        // Lấy vị trí đích
        targetX = gp.map.treasure_x / gp.DOT_SIZE;
        targetY = gp.map.treasure_y / gp.DOT_SIZE;

        // Tìm đường đi bằng DFS
        if (type == 0) dfs(x / gp.DOT_SIZE, y / gp.DOT_SIZE);
        else dfs_A(x / gp.DOT_SIZE, y / gp.DOT_SIZE);

    }

    private boolean dfs(int currentX, int currentY) {
        // Đánh dấu ô hiện tại đã thăm
        visited[currentX][currentY] = true;
        System.out.println(currentX + " " + currentY);
        // Lưu lại bước đi hiện tại
        path.add(new Point(currentX, currentY));

        // Nếu đến đích thì trả về true
        if (currentX == targetX && currentY == targetY) {
            return true;
        }

        // Thử các hướng đi có thể
        int[] dx = {0, 1, 0, -1};  // lên, phải, xuống, trái
        int[] dy = {-1, 0, 1, 0};

        for (int i = 0; i < 4; i++) {
            int nextX = currentX + dx[i];
            int nextY = currentY + dy[i];

            // Kiểm tra ô tiếp theo có hợp lệ không
            if (nextX >= 0 && nextX < n && nextY >= 0 && nextY < n
                    && !visited[nextX][nextY] && gp.map.maze[nextX][nextY] != 0) {
                // Thử đi theo hướng này
                if (dfs(nextX, nextY)) {
                    return true;
                }
            }
        }

        // Khi quay lui, thêm lại ô hiện tại vào path
        path.add(new Point(currentX, currentY));
        return false;
    }

    private boolean dfs_A(int currentX, int currentY) {
        // Đánh dấu ô hiện tại đã thăm
        visited[currentX][currentY] = true;
        System.out.println(currentX + " " + currentY);
        // Lưu lại bước đi hiện tại
        path.add(new Point(currentX, currentY));

        // Nếu đến đích thì trả về true
        if (currentX == targetX && currentY == targetY) {
            return true;
        }

        // Thử các hướng đi có thể
        int[] dx = {0, 1, 0, -1};  // lên, phải, xuống, trái
        int[] dy = {-1, 0, 1, 0};

        // Lưu các hướng có thể đi và khoảng cách tới đích
        ArrayList<Direction> possibleDirections = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            int nextX = currentX + dx[i];
            int nextY = currentY + dy[i];

            // Kiểm tra ô tiếp theo có hợp lệ không
            if (nextX >= 0 && nextX < n && nextY >= 0 && nextY < n
                    && !visited[nextX][nextY] && gp.map.maze[nextX][nextY] != 0) {
                // Tính khoảng cách Manhattan tới đích
                int distance = Math.abs(nextX - targetX) + Math.abs(nextY - targetY);
                possibleDirections.add(new Direction(nextX, nextY, distance));
            }
        }

        // Sắp xếp các hướng theo khoảng cách tới đích (tăng dần)
        possibleDirections.sort((a, b) -> Integer.compare(a.distance, b.distance));

        // Thử các hướng theo thứ tự từ gần đến xa đích
        for (Direction dir : possibleDirections) {
            if (dfs_A(dir.x, dir.y)) {
                return true;
            }
        }

        // Khi quay lui, thêm lại ô hiện tại vào path
        path.add(new Point(currentX, currentY));
        return false;
    }

    // Class để lưu thông tin về hướng đi
    private static class Direction {
        int x, y, distance;
        Direction(int x, int y, int distance) {
            this.x = x;
            this.y = y;
            this.distance = distance;
        }
    }

    public void moveAgent() {
        if (!path.isEmpty()) {
            Point next = path.get(0);  // Lấy điểm đầu tiên trong danh sách
            int nextX = next.x * gp.DOT_SIZE;
            int nextY = next.y * gp.DOT_SIZE;

            // Di chuyển đến điểm tiếp theo
            if (x < nextX) x += gp.DOT_SIZE;
            else if (x > nextX) x -= gp.DOT_SIZE;
            else if (y < nextY) y += gp.DOT_SIZE;
            else if (y > nextY) y -= gp.DOT_SIZE;

            // Nếu đã đến điểm tiếp theo, xóa khỏi đường đi
            if (x == nextX && y == nextY) {
                path.remove(0);  // Xóa điểm đầu tiên
            }

            // Cập nhật fog of war
            checkCollision();
        }
    }

    // Class Point để lưu tọa độ
    private static class Point {
        int x, y;
        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
}