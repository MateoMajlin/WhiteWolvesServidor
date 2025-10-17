package winterwolves.io;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

public class EntradasJugador2 extends EntradasJugador {

    // No redeclarar booleans, usamos los de la clase padre

    @Override
    public boolean isArriba() {
        return Gdx.input.isKeyPressed(Input.Keys.W);
    }

    @Override
    public boolean isAbajo() {
        return Gdx.input.isKeyPressed(Input.Keys.S);
    }

    @Override
    public boolean isIzquierda() {
        return Gdx.input.isKeyPressed(Input.Keys.A);
    }

    @Override
    public boolean isDerecha() {
        return Gdx.input.isKeyPressed(Input.Keys.D);
    }

    @Override
    public boolean isDash() {
        return Gdx.input.isKeyPressed(Input.Keys.SPACE);
    }

    @Override
    public boolean isCorrer() {
        return Gdx.input.isKeyPressed(Input.Keys.SHIFT_RIGHT);
    }

    @Override
    public boolean isGolpeBasico() {
        return Gdx.input.isKeyPressed(Input.Keys.R);
    }

    @Override
    public boolean isHabilidad1() {
        return Gdx.input.isKeyPressed(Input.Keys.T);
    }

    @Override
    public boolean isHabilidad2() {
        return Gdx.input.isKeyPressed(Input.Keys.Y);
    }
}
