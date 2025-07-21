package winterwolves.utilidades;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class Box2DColisiones {

    public static void crearCuerposColisiones(TiledMap mapa, World world, String capaColision, float ppm) {
        for (MapObject object : mapa.getLayers().get(capaColision).getObjects()) {
            if (object instanceof RectangleMapObject) {
                Rectangle rect = ((RectangleMapObject) object).getRectangle();

                BodyDef bodyDef = new BodyDef();
                bodyDef.type = BodyDef.BodyType.StaticBody;

                bodyDef.position.set((rect.x + rect.width / 2) / ppm, (rect.y + rect.height / 2) / ppm);

                Body body = world.createBody(bodyDef);

                PolygonShape shape = new PolygonShape();
                shape.setAsBox(rect.width / 2 / ppm, rect.height / 2 / ppm);

                FixtureDef fixtureDef = new FixtureDef();
                fixtureDef.shape = shape;
                fixtureDef.friction = 0.5f;
                fixtureDef.density = 1f;

                body.createFixture(fixtureDef);
                shape.dispose();
            }
        }
    }
}
