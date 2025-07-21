package winterwolves.personajes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.*;

public class GolpeEspada {

    private Animation<TextureRegion> animacion;
    private float stateTime;
    private boolean activo;
    private Texture hoja;

    private World world;
    private Body body;
    private float ppm;

    public GolpeEspada(World world, float ppm) {
        this.world = world;
        this.ppm = ppm;

        hoja = new Texture(Gdx.files.internal("espadaAnimacion.png"));

        TextureRegion[][] tmp = TextureRegion.split(hoja,
            hoja.getWidth() / 8,
            hoja.getHeight()
        );

        TextureRegion[] frames = new TextureRegion[8];
        for (int i = 0; i < 8; i++) {
            frames[i] = tmp[0][i];
        }

        animacion = new Animation<>(0.05f, frames);
        animacion.setPlayMode(Animation.PlayMode.NORMAL);

        stateTime = 0;
        activo = false;
    }

    public void activar(float x, float y) {
        if (!activo) {
            activo = true;
            stateTime = 0;

            BodyDef bodyDef = new BodyDef();
            bodyDef.type = BodyDef.BodyType.DynamicBody;
            bodyDef.position.set(x / ppm, y / ppm);
            body = world.createBody(bodyDef);

            PolygonShape shape = new PolygonShape();
            shape.setAsBox(16 / ppm, 16 / ppm);

            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.shape = shape;
            fixtureDef.isSensor = true;
            body.createFixture(fixtureDef).setUserData(this);

            shape.dispose();

            body.setUserData(this);
        }
    }

    public void update(float delta, float x, float y) {
        if (activo) {
            stateTime += delta;

            if (body != null) {
                body.setTransform(x / ppm, y / ppm, 0);
            }

            if (animacion.isAnimationFinished(stateTime)) {
                activo = false;
                if (body != null) {
                    world.destroyBody(body);
                    body = null;
                }
            }
        }
    }

    public void draw(Batch batch, float x, float y, float width, float height, float angle) {
        if (activo) {
            TextureRegion frame = animacion.getKeyFrame(stateTime);
            batch.draw(frame, x, y, width / 2, height / 2, width, height, 1, 1, angle);
        }
    }

    public boolean isActivo() {
        return activo;
    }

    public void dispose() {
        hoja.dispose();
    }
}
