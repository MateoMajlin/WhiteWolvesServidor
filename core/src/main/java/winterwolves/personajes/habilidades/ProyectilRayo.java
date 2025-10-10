package winterwolves.personajes.habilidades;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import winterwolves.personajes.Personaje;

public class ProyectilRayo {

    private Body body;
    private Animation<TextureRegion> animacion;
    private float stateTime = 0f;
    private float velocidad;
    public int daño;
    private Vector2 dir;
    public boolean muerto = false;
    private float tiempoRestante;
    private Personaje lanzador;
    private float ancho = 256f;
    private float alto = 128f;
    private Vector2 offsetHitbox;

    public ProyectilRayo(World world, Vector2 pos, Vector2 dir, float velocidad, int daño,
                         Animation<TextureRegion> animacion, float duracion, Personaje lanzador,
                         Vector2 offsetHitbox) {

        this.daño = daño;
        this.dir = new Vector2(dir).nor();
        this.velocidad = velocidad;
        this.animacion = animacion;
        this.tiempoRestante = duracion;
        this.lanzador = lanzador;
        this.offsetHitbox = offsetHitbox != null ? offsetHitbox : new Vector2(0, 0);

        // Posición inicial con offset
        Vector2 spawnPos = pos.cpy().add(this.offsetHitbox);

        BodyDef def = new BodyDef();
        def.type = BodyDef.BodyType.DynamicBody;
        def.position.set(spawnPos.x / lanzador.ppm, spawnPos.y / lanzador.ppm);
        def.bullet = true;
        def.gravityScale = 0;
        body = world.createBody(def);

        PolygonShape shape = new PolygonShape();
        float halfW = (ancho / 3f) / lanzador.ppm;
        float halfH = (alto / 8f) / lanzador.ppm;

        // Rotar hitbox según dirección
        float angleRad = dir.angleRad();
        shape.setAsBox(halfW, halfH, new Vector2(0,0), angleRad);

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

        batch.draw(frame,
            pos.x - ancho / 2f,
            pos.y - alto / 2f,
            ancho / 2f,
            alto / 2f,
            ancho,
            alto,
            1f,
            1f,
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
