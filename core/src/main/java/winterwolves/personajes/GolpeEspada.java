package winterwolves.personajes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import java.util.HashMap;
import java.util.Map;

public class GolpeEspada {

    public enum Direccion {
        UP, DOWN, LEFT, RIGHT,
        UP_LEFT, UP_RIGHT, DOWN_LEFT, DOWN_RIGHT
    }

    private Animation<TextureRegion> animacion;
    private float stateTime;
    private boolean activo;
    private Texture hoja;

    private World world;
    private Body body;
    private float ppm;

    // Configuración por dirección
    private static class HitboxConfig {
        float ancho, alto, offsetX, offsetY, angleDeg;
        HitboxConfig(float ancho, float alto, float offsetX, float offsetY, float angleDeg) {
            this.ancho = ancho;
            this.alto = alto;
            this.offsetX = offsetX;
            this.offsetY = offsetY;
            this.angleDeg = angleDeg;
        }
    }

    private Map<Direccion, HitboxConfig> hitboxes = new HashMap<>();

    public GolpeEspada(World world, float ppm) {
        this.world = world;
        this.ppm = ppm;

        hoja = new Texture(Gdx.files.internal("espadaAnimacion.png"));

        TextureRegion[][] tmp = TextureRegion.split(hoja, hoja.getWidth()/8, hoja.getHeight());
        TextureRegion[] frames = new TextureRegion[8];
        for (int i = 0; i < 8; i++) frames[i] = tmp[0][i];

        animacion = new Animation<>(0.05f, frames);
        animacion.setPlayMode(Animation.PlayMode.NORMAL);
        stateTime = 0;
        activo = false;

        // Configuraciones por defecto
        hitboxes.put(Direccion.RIGHT, new HitboxConfig(30, 40, 30, 15, 0));
        hitboxes.put(Direccion.LEFT, new HitboxConfig(30, 40, 0, 15, 0));
        hitboxes.put(Direccion.UP, new HitboxConfig(50, 25, 15, 30, 0));
        hitboxes.put(Direccion.DOWN, new HitboxConfig(50, 25, 15, 0, 0));
        hitboxes.put(Direccion.UP_RIGHT, new HitboxConfig(25, 50, 26, 26, 45));
        hitboxes.put(Direccion.UP_LEFT, new HitboxConfig(25, 50, 0, 26, -45));
        hitboxes.put(Direccion.DOWN_RIGHT, new HitboxConfig(25, 50, 26, 5, -45));
        hitboxes.put(Direccion.DOWN_LEFT, new HitboxConfig(25, 50, -5, 5, 45));
    }

    public void activar(float x, float y, Direccion dir) {
        if (!activo) {
            activo = true;
            stateTime = 0;

            HitboxConfig cfg = hitboxes.get(dir);

            BodyDef bodyDef = new BodyDef();
            bodyDef.type = BodyDef.BodyType.DynamicBody;
            bodyDef.position.set(x/ppm, y/ppm);
            body = world.createBody(bodyDef);

            PolygonShape shape = new PolygonShape();
            Vector2 offset = new Vector2(cfg.offsetX/ppm, cfg.offsetY/ppm);
            shape.setAsBox(cfg.ancho/2/ppm, cfg.alto/2/ppm, offset, (float)Math.toRadians(cfg.angleDeg));

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
            if (body != null) body.setTransform(x/ppm, y/ppm, 0);
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
            batch.draw(frame, x, y, width/2, height/2, width, height, 1, 1, angle);
        }
    }

    public boolean isActivo() { return activo; }
    public void dispose() { hoja.dispose(); }

    public void setHitbox(Direccion dir, float ancho, float alto, float offsetX, float offsetY, float angleDeg) {
        hitboxes.put(dir, new HitboxConfig(ancho, alto, offsetX, offsetY, angleDeg));
    }
}
