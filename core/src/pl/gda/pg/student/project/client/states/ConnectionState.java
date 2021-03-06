package pl.gda.pg.student.project.client.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.TextInputListener;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.esotericsoftware.kryonet.Client;
import pl.gda.pg.student.project.client.GameClient;
import pl.gda.pg.student.project.kryonetcommon.ConnectionSettings;
import pl.gda.pg.student.project.libgdxcommon.State;

import java.io.IOException;

public class ConnectionState extends State implements TextInputListener
{
    private BitmapFont font = GameClient.assets.getFont();
    private String messageForUser = "Input adress ip";
    private Client client;
    
    public ConnectionState(Client client)
    {
        this.client = client;
        font.setColor(new Color(1, 0, 0, 0.8f));
        askForIp();
    }

    private void askForIp()
    {
        Gdx.input.getTextInput(this, "Ip adress", "localhost", "");
    }
    
    @Override
    public void render(SpriteBatch batch)
    {
        font.draw(batch, messageForUser, 0, 0);
    }

    @Override
    public void update()
    {
    }

    @Override
    public void input(String ip)
    {
        tryConnecting(ip);
        ClientPlayState clientPlayState = new ClientPlayState(client);
        if(client.isConnected())
            GameClient.states.set(clientPlayState);
    }

    private void tryConnecting(String ip)
    {
        try
        {
            client.start();
            client.connect(2000, ip, ConnectionSettings.TCP_PORT, ConnectionSettings.UDP_PORT);
        } catch (IOException e)
        {
            System.out.print(e.getMessage());
            messageForUser = "Connection failed, try again";
            askForIp();
        }        
    }

    @Override
    public void canceled()
    {
        askForIp();
    }
    
}
