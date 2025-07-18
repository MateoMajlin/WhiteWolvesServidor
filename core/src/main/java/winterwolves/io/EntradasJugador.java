package winterwolves.io;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

public class EntradasJugador implements InputProcessor {

    private boolean arriba, abajo, izquierda, derecha;

    public boolean isArriba() {
        return arriba;
    }

    public boolean isAbajo() {
        return abajo;
    }

    public boolean isIzquierda() {
        return izquierda;
    }

    public boolean isDerecha() {
        return derecha;
    }

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.UP:
                arriba = true;
                break;
            case Input.Keys.DOWN:
                abajo = true;
                break;
            case Input.Keys.LEFT:
                izquierda = true;
                break;
            case Input.Keys.RIGHT:
                derecha = true;
                break;
        }
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        switch (keycode) {
            case Input.Keys.UP:
                arriba = false;
                break;
            case Input.Keys.DOWN:
                abajo = false;
                break;
            case Input.Keys.LEFT:
                izquierda = false;
                break;
            case Input.Keys.RIGHT:
                derecha = false;
                break;
        }
        return true;
    }

    @Override public boolean keyTyped(char character) { return false; }
    @Override public boolean touchDown(int screenX, int screenY, int pointer, int button) { return false; }
    @Override public boolean touchUp(int screenX, int screenY, int pointer, int button) { return false; }
    @Override public boolean touchCancelled(int screenX, int screenY, int pointer, int button) { return false; }
    @Override public boolean touchDragged(int screenX, int screenY, int pointer) { return false; }
    @Override public boolean mouseMoved(int screenX, int screenY) { return false; }
    @Override public boolean scrolled(float amountX, float amountY) { return false; }
}
