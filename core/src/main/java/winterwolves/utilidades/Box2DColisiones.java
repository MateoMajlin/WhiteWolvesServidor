package winterwolves.utilidades;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class Box2DColisiones {

    /**
     * Crea los cuerpos estáticos de Box2D a partir de una capa de colisiones en Tiled,
     * con un margen opcional para ajustar hitboxes.
     *
     * @param map       Mapa Tiled
     * @param world     Mundo Box2D
     * @param layerName Nombre de la capa de colisiones
     * @param PPM       Pixels por metro (escala Box2D)
     * @param marginX   Margen en píxeles a agregar al ancho
     * @param marginY   Margen en píxeles a agregar al alto
     */
    public static void crearCuerposColisiones(TiledMap map, World world, String layerName, float PPM, float marginX, float marginY) {
        MapLayer layer = map.getLayers().get(layerName);
        if (layer == null) return;

        for (MapObject object : layer.getObjects()) {
            if (object instanceof RectangleMapObject) {
                Rectangle rect = ((RectangleMapObject) object).getRectangle();
                crearCuerpoRect(world, rect, PPM, marginX, marginY);
            } else if (object instanceof PolygonMapObject) {
                Polygon polygon = ((PolygonMapObject) object).getPolygon();
                crearCuerpoPolygon(world, polygon, PPM);
            }
        }
    }

    private static void crearCuerpoRect(World world, Rectangle rect, float PPM, float marginX, float marginY) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(
            (rect.x + rect.width / 2f) / PPM,
            (rect.y + rect.height / 2f) / PPM
        );

        Body body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(
            (rect.width / 2f + marginX) / PPM,
            (rect.height / 2f + marginY) / PPM
        );

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.friction = 0.5f;
        body.createFixture(fixtureDef);
        shape.dispose();
    }

    private static void crearCuerpoPolygon(World world, Polygon polygon, float PPM) {
        float[] vertices = polygon.getTransformedVertices();
        Vector2[] worldVertices = new Vector2[vertices.length / 2];

        for (int i = 0; i < vertices.length / 2; i++) {
            worldVertices[i] = new Vector2(vertices[i * 2] / PPM, vertices[i * 2 + 1] / PPM);
        }

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        Body body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.set(worldVertices);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.friction = 0.5f;
        body.createFixture(fixtureDef);
        shape.dispose();
    }
}
