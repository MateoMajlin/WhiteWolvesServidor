package winterwolves.props;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class Caja extends Sprite {

    private Body body;
    private float ppm;
    private boolean activa = true;
    private boolean marcadaParaDestruir = false;

    public Caja(World world, float x, float y, float ppm) {
        super(new Texture("caja.png"));
        this.ppm = ppm;

        setSize(32, 32);

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(x, y);

        body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox((32 / 4f) / ppm, (32 / 4f) / ppm);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.friction = 0.5f;
        fixtureDef.density = 1f;
        fixtureDef.restitution = 0f;

        body.createFixture(fixtureDef).setUserData(this);
        body.setUserData(this);

        shape.dispose();
    }

    @Override
    public void draw(Batch batch) {
        if (!activa || body == null) return;

        Vector2 pos = body.getPosition();
        setPosition(pos.x * ppm - getWidth() / 2, pos.y * ppm - getHeight() / 2);
        super.draw(batch);
    }


    public void destruir() {
        marcadaParaDestruir = true;
    }

    public boolean isMarcadaParaDestruir() {
        return marcadaParaDestruir;
    }

    public void eliminarDelMundo() {
        if (body != null) {
            body.getWorld().destroyBody(body);
            body = null;
        }
        activa = false;
    }


    public Body getBody() {
        return body;
    }
}
