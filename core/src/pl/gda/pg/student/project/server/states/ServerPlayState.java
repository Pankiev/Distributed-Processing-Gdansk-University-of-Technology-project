package pl.gda.pg.student.project.server.states;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import pl.gda.pg.student.project.kryonetcommon.IdSupplier;
import pl.gda.pg.student.project.libgdxcommon.State;
import pl.gda.pg.student.project.libgdxcommon.objects.GameObject;
import pl.gda.pg.student.project.server.objects.Box;
import pl.gda.pg.student.project.server.objects.GameObjectsContainer;
import pl.gda.pg.student.project.server.objects.Wall;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ServerPlayState extends State implements GameObjectsContainer
{
    private final static int TILE_SIZE = 27;
    private Map<Long, GameObject> objects;

    public ServerPlayState()
    {
        objects = Collections.synchronizedMap(new HashMap<Long, GameObject>());
        Wall wall;
        Box box;
        long id;
        for (int i = 0; i <= 20; i++)
        {
            for (int j = 0; j <= 16; j++)
            {
                id = IdSupplier.getId();
                if (i % 2 == 1 && j % 2 == 1)
                {
                    wall = new Wall(this, new Vector2(TILE_SIZE * i, TILE_SIZE * j));
                    wall.setId(id);
                    objects.put(id, wall);
                }
                else
                {
                    box = new Box(this, new Vector2(TILE_SIZE*i, TILE_SIZE*j));
                    box.setId(id);
                    objects.put(id, box);
                }
            }
        }
    }

    @Override
    public void render(SpriteBatch batch)
    {
        for (GameObject object : objects.values())
        {
            object.render(batch);
        }
    }

    @Override
    public void update()
    {

    }

    @Override
    public void add(GameObject object)
    {
        objects.put(object.getId(), object);
    }

    @Override
    public void remove(GameObject object)
    {
        objects.remove(object.getId());
    }

    @Override
    public Map<Long, GameObject> getGameObjects()
    {
        return objects;
    }
    
    @Override
    public GameObject getObject(long id)
    {
        return objects.get(id);
    }
    
    public void updateObjectPosition(long id, Vector2 position)
    {
        GameObject operationTarget = objects.get(id);
        operationTarget.setPosition(position.x, position.y);
    }
}
