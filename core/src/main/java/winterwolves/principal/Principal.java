package winterwolves.principal;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import winterwolves.pantallas.Menu;
import winterwolves.utilidades.Recursos;
import winterwolves.utilidades.Render;


public class Principal extends Game {

    @Override
    public void create() {
        Render.app = this;
        Render.batch = new SpriteBatch();
        // setearMusica();
        this.setScreen(new Menu());
    }

    @Override
    public void render() { // Usa el render de la Game
        super.render();
    }

    private void update(){

    }

    @Override
    public void dispose() {
        super.dispose();

        if (getScreen() != null) {
            getScreen().dispose();
        }

        Render.batch.dispose();
    }


    private void setearMusica() {
        Recursos.musica.play();
        Recursos.musica.setLooping(true);
        Recursos.musica.setVolume(0.3f);
    }
}
