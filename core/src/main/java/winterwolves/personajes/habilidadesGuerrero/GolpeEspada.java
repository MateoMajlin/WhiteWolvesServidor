package winterwolves.personajes.habilidadesGuerrero;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

import java.util.HashMap;
import java.util.Map;

public class GolpeEspada extends GolpeArma {

    private Animation<TextureRegion> animacion;
    private float stateTime;
    protected boolean activo;
    private Texture hoja;

    private World world;
    private Body body;
    private float ppm;

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

        // Configuración por dirección
        hitboxes.put(Direccion.RIGHT, new HitboxConfig(20, 35, 20, 15, 0));
        hitboxes.put(Direccion.LEFT, new HitboxConfig(20, 35, 10, 15, 0));
        hitboxes.put(Direccion.UP, new HitboxConfig(35, 20, 15, 20, 0));
        hitboxes.put(Direccion.DOWN, new HitboxConfig(35, 20, 15, 10, 0));
        hitboxes.put(Direccion.UP_RIGHT, new HitboxConfig(20, 35, 20, 20, 45));
        hitboxes.put(Direccion.UP_LEFT, new HitboxConfig(20, 35, 10, 20, -45));
        hitboxes.put(Direccion.DOWN_RIGHT, new HitboxConfig(20, 35, 20, 10, -45));
        hitboxes.put(Direccion.DOWN_LEFT, new HitboxConfig(20, 35, 10, 10, 45));
    }

    @Override
    public void activar(float x, float y, Direccion dir) {
        if (!activo) {
            activo = true;
            stateTime = 0;
            tiempoDesdeUltimoGolpe = 0f;

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

    @Override
    public void update(float delta, float x, float y) {
        tiempoDesdeUltimoGolpe += delta;
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

    @Override
    public void draw(Batch batch, float x, float y, float width, float height, float angle) {
        if (activo) {
            TextureRegion frame = animacion.getKeyFrame(stateTime);
            batch.draw(frame, x, y, width/2, height/2, width, height, 1, 1, angle);
        }
    }

    @Override
    public boolean isActivo() {
        return activo;
    }

    @Override
    public void dispose() {
        hoja.dispose();
    }
}
