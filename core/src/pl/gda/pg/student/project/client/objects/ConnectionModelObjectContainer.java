package pl.gda.pg.student.project.client.objects;

import java.util.Map;

public interface ConnectionModelObjectContainer
{
    void add(ConnectionModelObject object);

    void remove(ConnectionModelObject object);

    Map<Long, ConnectionModelObject> getGameObjects();
}