package winterwolves.personajes.habilidades;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import winterwolves.personajes.Personaje;

import java.util.HashMap;
import java.util.Map;

public class Proyectil {

    Body body;
    Vector2 dir;
    float velocidad;
    public int daño;
    Animation<TextureRegion> animacion;
    float stateTime = 0f;
    public boolean muerto = false;
    float tiempoRestante;
    Personaje lanzador;
    BolaDeFuego.Direccion direccion;

    float ancho = 64f;
    float alto = 64f;

    private static final Map<BolaDeFuego.Direccion, Vector2> visualOffsets = new HashMap<>();
    static {
        visualOffsets.put(BolaDeFuego.Direccion.UP, new Vector2(32, 10));
        visualOffsets.put(BolaDeFuego.Direccion.DOWN, new Vector2(-32, -10));
        visualOffsets.put(BolaDeFuego.Direccion.LEFT, new Vector2(-10, 32));
        visualOffsets.put(BolaDeFuego.Direccion.RIGHT, new Vector2(10, -32));
        visualOffsets.put(BolaDeFuego.Direccion.UP_LEFT, new Vector2(15, 30));
        visualOffsets.put(BolaDeFuego.Direccion.UP_RIGHT, new Vector2(30, -15));
        visualOffsets.put(BolaDeFuego.Direccion.DOWN_LEFT, new Vector2(-30, 15));
        visualOffsets.put(BolaDeFuego.Direccion.DOWN_RIGHT, new Vector2(-15, -30));
    }

    public Proyectil(World world, Vector2 pos, Vector2 dir, float velocidad, int daño,
                     Animation<TextureRegion> animacion, float duracion, Personaje lanzador,
                     BolaDeFuego.Direccion direccion) {
        this.dir = new Vector2(dir).nor();
        this.velocidad = velocidad;
        this.daño = daño;
        this.animacion = animacion;
        this.tiempoRestante = duracion;
        this.lanzador = lanzador;
        this.direccion = direccion;

        BodyDef def = new BodyDef();
        def.type = BodyDef.BodyType.DynamicBody;
        def.position.set(pos.x / lanzador.ppm, pos.y / lanzador.ppm);
        def.bullet = true;
        def.gravityScale = 0;
        body = world.createBody(def);

        CircleShape shape = new CircleShape();
        shape.setRadius(15 / lanzador.ppm);

        FixtureDef fix = new FixtureDef();
        fix.shape = shape;
        fix.isSensor = true;
        fix.filter.categoryBits = 0x0004;
        fix.filter.maskBits = -1;

        body.createFixture(fix);
        shape.dispose();
        body.setUserData(this);
    }

    public void actualizar(float delta) {
        if (body == null) return;
        body.setLinearVelocity(dir.x * velocidad / lanzador.ppm, dir.y * velocidad / lanzador.ppm);
        tiempoRestante -= delta;
        if (tiempoRestante <= 0) muerto = true;
    }

    public void dibujar(Batch batch) {
        if (body == null) return;
        stateTime += Gdx.graphics.getDeltaTime();
        TextureRegion frame = animacion.getKeyFrame(stateTime, true);

        Vector2 pos = body.getPosition().cpy().scl(lanzador.ppm);
        float angle = dir.angleDeg();

        Vector2 offset = visualOffsets.getOrDefault(direccion, new Vector2(0,0));

        batch.draw(frame,
            pos.x - ancho / 2f + offset.x,
            pos.y - alto / 2f + offset.y,
            ancho / 2f,
            alto / 2f,
            ancho,
            alto,
            1.5f,
            1.5f,
            angle
        );
    }

    public boolean estaMuerto() { return muerto; }

    public void destruir(World world) {
        if (body != null && !world.isLocked()) {
            world.destroyBody(body);
            body = null;
        }
    }
}
