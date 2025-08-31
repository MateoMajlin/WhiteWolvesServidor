package winterwolves.personajes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import winterwolves.io.EntradasJugador;

public class Personaje extends Sprite {

    protected Body body;
    protected EntradasJugador entradas;
    protected int vida = 100;
    protected float speedBase = 2.5f;
    protected float speed = speedBase;
    protected float multiplicadorCorrer = 1.7f;
    protected float ppm;

    protected AnimacionJugador animaciones;
    protected Vector2 movimiento = new Vector2();
    protected Vector2 direccionMirando = new Vector2(0, -1);

    // Dash
    public final float COOLDOWN_DASH = 5f;
    protected float tiempoDesdeUltimoDash = 0f;
    protected boolean dashActivo = false;
    protected float duracionDash = 0.2f;
    protected float tiempoDash = 0f;
    protected float velocidadDash = 10f;

    public Personaje(World world, EntradasJugador entradas, float x, float y, float ppm) {
        super();
        this.entradas = entradas;
        this.ppm = ppm;

        animaciones = new AnimacionJugador();

        setSize(30, 30);

        // Crear cuerpo del personaje
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x, y);
        body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(getWidth() / 2 / ppm, getHeight() / 2 / ppm);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;
        fixtureDef.friction = 0.2f;
        fixtureDef.restitution = 0f;
        body.createFixture(fixtureDef);
        shape.dispose();
    }

    @Override
    public void draw(Batch batch) {
        float delta = Gdx.graphics.getDeltaTime();
        tiempoDesdeUltimoDash += delta;

        mover();

        Vector2 pos = body.getPosition();
        setPosition(pos.x * ppm - getWidth() / 2, pos.y * ppm - getHeight() / 2);

        TextureRegion frame = animaciones.getFrame(movimiento, delta);
        batch.draw(frame, getX(), getY(), getWidth(), getHeight());
    }

    protected void mover() {
        float delta = Gdx.graphics.getDeltaTime();

        if (dashActivo) {
            tiempoDash += delta;
            body.setLinearVelocity(direccionMirando.x * velocidadDash, direccionMirando.y * velocidadDash);

            if (tiempoDash >= duracionDash) {
                dashActivo = false;
                tiempoDash = 0f;
                body.setLinearVelocity(0, 0);
            }
            return;
        }

        // --- Activar dash ---
        if (entradas.isDash() && tiempoDesdeUltimoDash >= COOLDOWN_DASH) {
            dashActivo = true;
            tiempoDesdeUltimoDash = 0f;
            tiempoDash = 0f;
            return;
        }

        movimiento.set(0, 0);
        speed = entradas.isCorrer() ? speedBase * multiplicadorCorrer : speedBase;

        if (entradas.isArriba()) movimiento.y = 1;
        if (entradas.isAbajo()) movimiento.y = -1;
        if (entradas.isIzquierda()) movimiento.x = -1;
        if (entradas.isDerecha()) movimiento.x = 1;

        if (movimiento.len() > 0) {
            direccionMirando.set(movimiento).nor();
        }

        movimiento.nor().scl(speed);
        body.setLinearVelocity(movimiento.x, movimiento.y);
    }

    protected GolpeEspada.Direccion vectorADireccion(Vector2 dir) {
        float x = dir.x;
        float y = dir.y;

        if (x >= 0.5f && y >= 0.5f) return GolpeEspada.Direccion.UP_RIGHT;
        if (x <= -0.5f && y >= 0.5f) return GolpeEspada.Direccion.UP_LEFT;
        if (x >= 0.5f && y <= -0.5f) return GolpeEspada.Direccion.DOWN_RIGHT;
        if (x <= -0.5f && y <= -0.5f) return GolpeEspada.Direccion.DOWN_LEFT;
        if (x > 0.5f) return GolpeEspada.Direccion.RIGHT;
        if (x < -0.5f) return GolpeEspada.Direccion.LEFT;
        if (y > 0.5f) return GolpeEspada.Direccion.UP;
        return GolpeEspada.Direccion.DOWN;
    }

    public float getTiempoDesdeUltimoDash() { return tiempoDesdeUltimoDash; }
    public int getVida() { return vida; }

    public void dispose() { animaciones.dispose(); }
}
