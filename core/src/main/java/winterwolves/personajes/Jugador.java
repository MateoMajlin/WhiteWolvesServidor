package winterwolves.personajes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import winterwolves.io.EntradasJugador;

public class Jugador extends Sprite {

    private Body body;
    private EntradasJugador entradas;
    private int vida = 100;
    private float speedBase = 2.5f;
    private float speed = speedBase;
    private float multiplicadorCorrer = 1.7f;
    private float ppm;

    private AnimacionJugador animaciones;
    private Vector2 movimiento = new Vector2();
    private Vector2 direccionMirando = new Vector2(0, -1);

    private GolpeEspada golpe;

    // Cooldowns
    public final float COOLDOWN_GOLPE = 1f;
    private float tiempoDesdeUltimoGolpe = 0f;

    public final float COOLDOWN_DASH = 5f;
    private float tiempoDesdeUltimoDash = 0f;

    // Dash
    private boolean dashActivo = false;
    private float duracionDash = 0.2f;
    private float tiempoDash = 0f;
    private float velocidadDash = 10f;

    public Jugador(World world, EntradasJugador entradas, float x, float y, float ppm) {
        super();
        this.entradas = entradas;
        this.ppm = ppm;
        this.golpe = new GolpeEspada(world, ppm);

        animaciones = new AnimacionJugador();

        setSize(30, 30);

        // Crear cuerpo del jugador
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
        tiempoDesdeUltimoGolpe += delta;
        tiempoDesdeUltimoDash += delta;

        mover();

        Vector2 pos = body.getPosition();
        setPosition(pos.x * ppm - getWidth() / 2, pos.y * ppm - getHeight() / 2);

        TextureRegion frame = animaciones.getFrame(movimiento, delta);
        batch.draw(frame, getX(), getY(), getWidth(), getHeight());

        float desplazamiento = getWidth() * 0.6f;
        float espadaX = getX() + direccionMirando.x * desplazamiento;
        float espadaY = getY() + direccionMirando.y * desplazamiento;

        GolpeEspada.Direccion dir = vectorADireccion(direccionMirando);

        // Activar golpe
        if (entradas.isAtacar() && !golpe.isActivo() && tiempoDesdeUltimoGolpe >= COOLDOWN_GOLPE) {
            golpe.activar(espadaX, espadaY, dir);
            tiempoDesdeUltimoGolpe = 0f;
        }

        golpe.update(delta, espadaX, espadaY);
        golpe.draw(batch, espadaX, espadaY, getWidth(), getHeight(), direccionMirando.angleDeg());
    }

    private void mover() {
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

        if (golpe.isActivo()) {
            body.setLinearVelocity(0, 0);
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

    private GolpeEspada.Direccion vectorADireccion(Vector2 dir) {
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

    public float getTiempoDesdeUltimoGolpe() {
        return tiempoDesdeUltimoGolpe;
    }

    public float getTiempoDesdeUltimoDash() {
        return tiempoDesdeUltimoDash;
    }

    public void dispose() {
        animaciones.dispose();
        golpe.dispose();
    }

    public int getVida() {
        return vida;
    }
}
