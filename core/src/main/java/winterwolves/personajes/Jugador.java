package winterwolves.personajes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import winterwolves.io.EntradasJugador;
import winterwolves.utilidades.GestorColisionesJugador;

public class Jugador extends Sprite {

    private Vector2 movimiento = new Vector2();
    private float speed = 60 * 2;
    private float speedBase = speed;
    public float correrMulti = 1.7f;
    private EntradasJugador entradas;

    private GestorColisionesJugador colisiones;
    private AnimacionJugador animaciones;

    public Jugador(Sprite sprite, GestorColisionesJugador colisiones, EntradasJugador entradas) {
        super(sprite);
        this.colisiones = colisiones;
        this.entradas = entradas;
        this.animaciones = new AnimacionJugador();

        setSize(32, 32);
    }

    @Override
    public void draw(Batch batch) {
        update(Gdx.graphics.getDeltaTime());
        batch.draw(animaciones.getFrame(movimiento, Gdx.graphics.getDeltaTime()), getX(), getY(), getWidth(), getHeight());
    }

    public void update(float delta) {
        movimiento.set(0, 0);

        if (entradas.isCorrer()) {
            speed = speedBase * correrMulti;
        } else {
            speed = speedBase;
        }

        if (entradas.isArriba()) movimiento.y = 1;
        if (entradas.isAbajo()) movimiento.y = -1;
        if (entradas.isIzquierda()) movimiento.x = -1;
        if (entradas.isDerecha()) movimiento.x = 1;

        if (movimiento.len() > 0) {
            movimiento.nor().scl(speed);
        }

        moverConColisiones(delta);
    }

    private void moverConColisiones(float delta) {
        float oldX = getX(), oldY = getY();
        boolean collisionX = false, collisionY = false;

        // Movimiento en X
        setX(getX() + movimiento.x * delta);
        if (movimiento.x < 0) collisionX = colisiones.collidesLeft(getX(), getY(), getWidth(), getHeight());
        else if (movimiento.x > 0) collisionX = colisiones.collidesRight(getX(), getY(), getWidth(), getHeight());
        if (collisionX) {
            setX(oldX);
            movimiento.x = 0;
        }

        // Movimiento en Y
        setY(getY() + movimiento.y * delta);
        if (movimiento.y < 0) collisionY = colisiones.collidesBottom(getX(), getY(), getWidth(), getHeight());
        else if (movimiento.y > 0) collisionY = colisiones.collidesTop(getX(), getY(), getWidth(), getHeight());
        if (collisionY) {
            setY(oldY);
            movimiento.y = 0;
        }
    }
}
