package pl.gda.pg.student.project.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import pl.gda.pg.student.project.server.GameServer;

public class ServerLauncher
{
    public static void main(String[] arg)
    {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = 837;
        config.height = 567;
        config.resizable = false;
        new LwjglApplication(new GameServer(), config);
    }
}
