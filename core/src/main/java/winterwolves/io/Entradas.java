package winterwolves.io;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import winterwolves.pantallas.Menu;

public class Entradas implements InputProcessor {

    private boolean abajo = false, arriba = false;
    private boolean enter = false;

    Menu menu;

    public Entradas(Menu menu){
        this.menu = menu;
    }

    public boolean isEnter() {
        return enter;
    }

    public boolean isAbajo() {
        return abajo;
    }

    public boolean isArriba() {
        return arriba;
    }

    @Override
    public boolean keyDown(int keycode) {

        menu.tiempo = 0.1f;

        if(keycode == Input.Keys.DOWN){
            abajo = true;
        }
        if(keycode == Input.Keys.UP){
            arriba = true;
        }

        if(keycode == Input.Keys.ENTER){
            enter = true;
        }

        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if(keycode == Input.Keys.DOWN){
            abajo = false;
        }
        if(keycode == Input.Keys.UP){
            arriba = false;
        }
        if(keycode == Input.Keys.ENTER){
            enter = false;
        }
        return false;
    }

    @Override
    public boolean keyTyped(char character) { return false; }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) { return false; }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) { return false; }

    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) { return false; }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) { return false; }

    @Override
    public boolean mouseMoved(int screenX, int screenY) { return false; }

    @Override
    public boolean scrolled(float amountX, float amountY) { return false; }
}
