package winterwolves.personajes.habilidadesGuerrero;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import winterwolves.personajes.Personaje;
import winterwolves.personajes.habilidades.Habilidad;

public class HabilidadCuracion extends Habilidad {

    private int curacion;

    public HabilidadCuracion(float duracion, float cooldown, int curacion) {
        super(duracion, cooldown);
        this.curacion = curacion;
        cargarAnimacion();
    }

    private void cargarAnimacion() {
        Texture textura = new Texture(Gdx.files.internal("curacion.png"));
        int anchoFrame = 64;
        int altoFrame = 64;
        TextureRegion[][] tmp = TextureRegion.split(textura, anchoFrame, altoFrame);
        TextureRegion[] frames = tmp[3];
        animacion = new com.badlogic.gdx.graphics.g2d.Animation<>(0.1f, frames);
    }

    @Override
    protected void iniciarEfecto() {
        if (personaje != null) {
            int nuevaVida = personaje.getVida() + curacion;
            personaje.setVida(nuevaVida); // setVida ya limita a 100
        }
    }


    @Override
    protected void finalizarEfecto() {
    }
}
