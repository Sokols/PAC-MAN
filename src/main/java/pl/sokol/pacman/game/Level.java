package pl.sokol.pacman.game;

import pl.sokol.pacman.elements.Junction;
import pl.sokol.pacman.elements.Renderable;
import pl.sokol.pacman.elements.dynamic.Enemy;
import pl.sokol.pacman.elements.Point;
import pl.sokol.pacman.elements.Tile;
import pl.sokol.pacman.elements.dynamic.Player;
import pl.sokol.pacman.gui.GamePanel;

import javax.imageio.ImageIO;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Level implements Renderable {

    private BufferedImage map;

    private List<Tile> tiles;
    private List<Point> points;
    private List<Enemy> enemies;
    private List<Junction> junctions;

    private GameThread gameThread;
    private Player player;

    Level(String path, Player player, GameThread gameThread) {
        try {
            this.map = ImageIO.read(getClass().getResourceAsStream(path));
            this.gameThread = gameThread;
            this.player = player;
            this.points = new ArrayList<>();
            this.enemies = new ArrayList<>();
            this.junctions = new ArrayList<>();
            this.tiles = new ArrayList<>();

            setElements();

        } catch (IOException | ExceptionInInitializerError e)  {
            e.printStackTrace();
        }
    }

    private void setElements() throws IOException, ExceptionInInitializerError {

        int mapWidth = map.getWidth();
        int mapHeight = map.getHeight();

        int mapWidthProportion = GamePanel.GAME_WIDTH / mapWidth;
        int mapHeightProportion = GamePanel.GAME_HEIGHT / mapHeight;

        for (int xx = 0; xx < mapWidth; xx++) {
            for (int yy = 0; yy < mapHeight; yy++) {

                int x = xx * mapWidthProportion;
                int y = yy * mapHeightProportion;

                // switch by RGB value
                switch (map.getRGB(xx, yy)) {
                    // Black - Tile
                    case 0xFF000000:
                        tiles.add(new Tile(x, y));
                        break;

                    // Blue - Player
                    case 0xFF0000FF:
                        player.setLocation(x, y);
                        break;

                    // Green - Junction & Point
                    case 0xFF00FF00:
                        points.add(new Point(x, y));
                        junctions.add(new Junction(x, y));
                        break;

                    // Red - Enemy
                    case 0xFFFF0000:
                        enemies.add(new Enemy(x, y, player, null, new Random().nextInt(5)));
                        break;

                    // White - Point
                    case 0xFFFFFFFF:
                        points.add(new Point(x, y));
                        break;

                    default:
                        throw new ExceptionInInitializerError("Error loading image!");
                }
            }
        }

        // set prepared junctions in every enemy
        for (Enemy enemy : enemies) {
            enemy.setJunctions(junctions);
        }

    }

    @Override
    public void render(Graphics g) {

        // render tiles
        for (Tile tile : tiles) {
            tile.render(g);
        }

        // render points
        for (Point point : points) {
            point.render(g);
        }

        // render enemies
        for (Enemy enemy : enemies) {
            enemy.render(g);
        }
    }

    public List<Tile> getTiles() {
        return tiles;
    }

    public List<Point> getPoints() {
        return points;
    }

    public List<Enemy> getEnemies() {
        return enemies;
    }
}