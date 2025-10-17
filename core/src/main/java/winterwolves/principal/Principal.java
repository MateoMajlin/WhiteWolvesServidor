package winterwolves.principal;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import winterwolves.pantallas.Menu;
import winterwolves.pantallas.PantallaCarga;
import winterwolves.pantallas.TerrenoPractica;
import winterwolves.utilidades.Recursos;
import winterwolves.utilidades.Render;


public class Principal extends Game {
    private SpriteBatch batch;
    private Texture image;

    @Override
    public void create() {
        Render.app = this;
        Render.batch = new SpriteBatch();
        // setearMusica();
        this.setScreen(new Menu());
    }

    @Override
    public void render() {
        super.render();
    }

    private void update(){

    }

    @Override
    public void dispose() {
        Render.batch.dispose();
    }

    private void setearMusica() {
        Recursos.musica.play();
        Recursos.musica.setLooping(true);
        Recursos.musica.setVolume(0.3f);
    }
}
