package winterwolves.utilidades;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;

public class Recursos {

    public static final String LOGO = "logoCarga.png";
    public static final String FONDO = "pearto.png";

    public static final String FUENTEMENU = "fuentes/fuenteMenu.ttf";

    public static final Music musica = Gdx.audio.newMusic(Gdx.files.internal("musica/musicaMenu.mp3"));
    public static final Music musicaBatalla = Gdx.audio.newMusic(Gdx.files.internal("musica/musicaBatalla.mp3"));

}
