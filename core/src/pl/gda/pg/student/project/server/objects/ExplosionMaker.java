package pl.gda.pg.student.project.server.objects;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import pl.gda.pg.student.project.kryonetcommon.IdSupplier;
import pl.gda.pg.student.project.libgdxcommon.State;
import pl.gda.pg.student.project.libgdxcommon.objects.GameObject;
import pl.gda.pg.student.project.server.helpers.ExplosionNamesFactory;
import pl.gda.pg.student.project.server.objects.powerUps.PowerUp;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class ExplosionMaker
{
    private final static int TILE_SIZE = 27;
    private Collection<GameObject> collisionObjects;
    private Vector2 bombPosition;
    private State linkedState;
    private Collection<GameObject> explosionColliders = new LinkedList<>();

    public ExplosionMaker(Collection<GameObject> collisionObjects, Vector2 bombPosition, State linkedState)
    {
        this.collisionObjects = collisionObjects;
        this.bombPosition = bombPosition;
        this.linkedState = linkedState;
    }

    public List<Explosion> make(int explosionRange)
    {
        List<Explosion> explosionChunks = new LinkedList<>();
        Explosion centerExplosion = getMiddleExplosion(bombPosition);
        centerExplosionCollision(explosionChunks, centerExplosion);
        explosionChunks.addAll(createExplosion(explosionRange, new Vector2(-TILE_SIZE, 0)));
        explosionChunks.addAll(createExplosion(explosionRange, new Vector2(TILE_SIZE, 0)));
        explosionChunks.addAll(createExplosion(explosionRange, new Vector2(0, -TILE_SIZE)));
        explosionChunks.addAll(createExplosion(explosionRange, new Vector2(0, TILE_SIZE)));
        return explosionChunks;
    }

    private void centerExplosionCollision(List<Explosion> explosionChunks, Explosion centerExplosion)
    {
        Vector2 explosionPosition = new Vector2(centerExplosion.getX(), centerExplosion.getY());
        GameObject collision = isColliding(explosionPosition);
        if(collision != null)
            explosionColliders.add(collision);

		explosionChunks.add(centerExplosion);
    }

    private List<Explosion> createExplosion(int explosionRange, Vector2 translationChunk)
    {
        List<Explosion> explosionChunks = new LinkedList<>();
        Vector2 translation = translationChunk.cpy();
        boolean isEndOfExplosion;
        for (int i = 0; i < explosionRange; i++)
        {
            isEndOfExplosion = i + 1 == explosionRange;

            Vector2 explosionPosition = bombPosition.cpy().add(translation);
            GameObject collision = isColliding(explosionPosition);
            if(shouldCreateExplosion(collision))
            {
                Explosion explosion = createExplosion(explosionPosition, translationChunk, isEndOfExplosion);
                explosionChunks.add(explosion);
            }
            if(shouldAddToColliders(collision))
            {
                explosionColliders.add(collision);
            }
            if(shouldEndExplosion(collision)){
                return explosionChunks;
            }
            translation.add(translationChunk);
        }
        return explosionChunks;
    }

    private boolean shouldEndExplosion(GameObject collision) {
        return collision instanceof Wall || collision instanceof Box;
    }

    private boolean shouldAddToColliders(GameObject collision) {
        return collision != null;
    }

    private boolean shouldCreateExplosion(GameObject collision)
    {
        return collision == null || collision instanceof PowerUp || collision instanceof ServerPlayer || collision instanceof Bomb;
    }

    private Explosion getMiddleExplosion(Vector2 explosionPosition)
    {
        Explosion explosion = new Explosion("wyb_gdlp.png", linkedState);
        explosion.setId(IdSupplier.getId());
        explosion.setPosition(explosionPosition.x, explosionPosition.y);
        return explosion;
    }

    private Explosion createExplosion(Vector2 explosionPosition, Vector2 translationChunk, boolean isEnd)
    {
        String explosionTextureName = ExplosionNamesFactory.produce(translationChunk, isEnd);
        Explosion explosion = new Explosion(explosionTextureName, linkedState);
        explosion.setId(IdSupplier.getId());
        explosion.setPosition(explosionPosition.x, explosionPosition.y);
        return explosion;
    }

    public Collection<GameObject> getExplosionColliders()
    {
        return explosionColliders;
    }

    private GameObject isColliding(Vector2 point)
    {
        Rectangle rectangle = new Rectangle(point.x, point.y, TILE_SIZE, TILE_SIZE);
        for (GameObject collisionObject : collisionObjects)
            if (collisionObject.isColliding(rectangle))
                return collisionObject;
        return null;
    }
}
