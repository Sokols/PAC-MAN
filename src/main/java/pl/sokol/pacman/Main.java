package pl.sokol.pacman;

import pl.sokol.pacman.gui.GameFrame;
import pl.sokol.pacman.game.GameThread;

public class Main {

    public static void main(String[] args) {

        new GameFrame(new GameThread());
    }
}
