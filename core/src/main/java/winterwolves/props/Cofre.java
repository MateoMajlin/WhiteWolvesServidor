package winterwolves.props;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import winterwolves.items.Inventario;
import winterwolves.items.Item;

public class Cofre extends Sprite {

    private Body body;
    private float ppm;
    private Inventario inventario;

    public Cofre(World world, float x, float y, float ppm) {
        super(new TextureRegion(new Texture("chests.png"), 0, 0, 16, 16));
        this.ppm = ppm;

        setSize(32,32);

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(x, y);

        body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(getWidth()/2f / ppm, getHeight()/2f / ppm);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.friction = 0.5f;
        fixtureDef.density = 1f;
        fixtureDef.restitution = 0f;

        body.createFixture(fixtureDef).setUserData(this);
        body.setUserData(this);

        shape.dispose();

        inventario = new Inventario();
    }

    public Inventario getInventario() {
        return inventario;
    }

    public boolean estaCerca(Vector2 posicionPersonaje, float rango) {
        Vector2 posCofre = body.getPosition().scl(ppm);
        return posCofre.dst(posicionPersonaje) <= rango;
    }

    @Override
    public void draw(Batch batch) {
        Vector2 pos = body.getPosition();
        setPosition(pos.x * ppm - getWidth() / 2, pos.y * ppm - getHeight() / 2);
        super.draw(batch);
    }

    public void dispose() {
        if (getTexture() != null) {
            getTexture().dispose();
        }
    }
}
