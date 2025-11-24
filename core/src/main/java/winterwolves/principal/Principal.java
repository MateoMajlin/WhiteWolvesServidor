package winterwolves.principal;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import winterwolves.pantallas.Menu;
import winterwolves.utilidades.Recursos;
import winterwolves.utilidades.Render;


public class Principal extends Game {
    private SpriteBatch batch; // Dibuja el entorno
    private Texture image; //Crea contexto de imagenes

    @Override
    public void create() { // Sobre escribe Metodo de inicio de clase padre
        Render.app = this; // Apunta el renderizado a la propia app
        Render.batch = new SpriteBatch(); // Instancia un nuevo Batch
        // setearMusica(); // Esta hecho pero es molesto.
        this.setScreen(new Menu()); // inicializa el juego con el metodo setScreen(), iniciando desde el Menu()
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
