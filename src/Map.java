import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

public class Map {
    int[][] maze = new int[21][21];
    int treasure_x, treasure_y;
    GamePanel gp;
    public Map(GamePanel gp) {
        this.gp = gp;
        String filePath = "map.txt";
        Random random = new Random();
        
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            int row = 0;
            while ((line = br.readLine()) != null && row < 20) {
                String[] values = line.trim().split("\\s+");

                for (int col = 0; col < 20 && col < values.length; col++) {
                    maze[col][row] = Integer.parseInt(values[col]);
                }
                row++;
            }
            int random_x = random.nextInt(2);
            int random_y = random.nextInt(20);
            while (maze[random_x][random_y] != 1) {
                random_x = random.nextInt(20);
                random_y = random.nextInt(20);
            }
            maze[random_x][random_y] = 2;
            treasure_x = random_x * gp.DOT_SIZE;
            treasure_y = random_y * gp.DOT_SIZE;
        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Error parsing numbers in the file: " + e.getMessage());
        }
    }
}