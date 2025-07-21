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

    public Jugador(World world, EntradasJugador entradas, float x, float y, float ppm) {
        super();
        this.entradas = entradas;
        this.ppm = ppm;
        this.vida = vida;
        this.golpe = new GolpeEspada(world, ppm);

        animaciones = new AnimacionJugador();

        setSize(30, 30);

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
        mover();
        Vector2 pos = body.getPosition();
        setPosition(pos.x * ppm - getWidth() / 2, pos.y * ppm - getHeight() / 2);

        TextureRegion frame = animaciones.getFrame(movimiento, Gdx.graphics.getDeltaTime());
        batch.draw(frame, getX(), getY(), getWidth(), getHeight());

        float desplazamiento = getWidth() * 0.6f;
        float espadaX = getX() + direccionMirando.x * desplazamiento;
        float espadaY = getY() + direccionMirando.y * desplazamiento;

        if (entradas.isAtacar() && !golpe.isActivo()) {
            golpe.activar(espadaX, espadaY);
        }

        golpe.update(Gdx.graphics.getDeltaTime(), espadaX, espadaY);
        golpe.draw(batch, espadaX, espadaY, getWidth(), getHeight(), direccionMirando.angleDeg());
    }

    private void mover() {
        movimiento.set(0, 0);

        if (entradas.isCorrer()) {
            speed = speedBase * multiplicadorCorrer;
        } else {
            speed = speedBase;
        }

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

    public void dispose() {
        animaciones.dispose();
        golpe.dispose();
    }

    public int getVida() {
        return vida;
    }
}
