package winterwolves.props;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class Caja extends Sprite {

    private Body body;
    private float ppm;

    public Caja(World world, float x, float y, float ppm) {
        super(new Texture("caja.png"));
        this.ppm = ppm;

        setSize(32, 32);

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(x, y);

        body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox((getWidth() / 4) / ppm, (getHeight() / 4) / ppm);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.friction = 0.5f;
        fixtureDef.density = 1f;
        fixtureDef.restitution = 0f;

        Fixture fixture = body.createFixture(fixtureDef);
        fixture.setUserData(this); // importante para identificarla en colisiones

        shape.dispose();
    }

    @Override
    public void draw(Batch batch) {
            Vector2 pos = body.getPosition();
            setPosition(pos.x * ppm - getWidth() / 2, pos.y * ppm - getHeight() / 2);
            super.draw(batch);
    }
}
