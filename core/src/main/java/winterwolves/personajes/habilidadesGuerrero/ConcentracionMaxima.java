package winterwolves.personajes.habilidadesGuerrero;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import winterwolves.personajes.Guerrero;
import winterwolves.personajes.habilidades.Habilidad;

public class ConcentracionMaxima extends Habilidad {

    private float bonusVelocidad, bonusAtaque;
    private final float tiempoCarga = 2f;

    public ConcentracionMaxima(float duracion, float cooldown, float bonusVelocidad, float bonusAtaque) {
        super(duracion, cooldown);
        this.bonusVelocidad = bonusVelocidad;
        this.bonusAtaque = bonusAtaque;
        cargarAnimacion();
    }

    @Override
    protected float getTiempoCarga() {
        return tiempoCarga;
    }

    private void cargarAnimacion() {
        Texture textura = new Texture(Gdx.files.internal("curacion.png"));
        int anchoFrame = 64;
        int altoFrame = 64;
        TextureRegion[][] tmp = TextureRegion.split(textura, anchoFrame, altoFrame);
        TextureRegion[] frames = tmp[0];
        animacion = new com.badlogic.gdx.graphics.g2d.Animation<>(0.1f, frames);
    }

    @Override
    public boolean usar() {
        if (puedeUsarse()) {
            tiempoActual = 0f;
            tiempoDesdeUltimoUso = 0f;
            tiempoAnimacion = 0f;

            if (getTiempoCarga() > 0) {
                cargando = true;
                if (personaje != null) {
                    personaje.setPuedeMoverse(false);
                }
            } else {
                activa = true;
                iniciarEfecto();
            }
            return true;
        }
        return false;
    }

    @Override
    public void actualizar(float delta) {
        tiempoDesdeUltimoUso += delta;

        if (cargando) {
            tiempoActual += delta; //es como que pasa el tiempo, sin desfasarse de acuerdo a los fps
            if (tiempoActual >= getTiempoCarga()) {
                cargando = false;
                activa = true;
                tiempoActual = 0f;
                tiempoAnimacion = 0f;

                if (personaje != null) {
                    personaje.setPuedeMoverse(true);
                }

                iniciarEfecto();
            }
        } else if (activa) {
            tiempoActual += delta;
            tiempoAnimacion += delta;
            if (tiempoActual >= duracion) {
                activa = false;
                tiempoAnimacion = 0f;
                finalizarEfecto();
            }
        }
    }

    @Override
    protected void iniciarEfecto() {
        if (personaje != null && personaje instanceof Guerrero) {
            Guerrero g = (Guerrero) personaje;
            g.speedBase += bonusVelocidad;
        }
    }

    @Override
    protected void finalizarEfecto() {
        if (personaje != null && personaje instanceof Guerrero) {
            Guerrero g = (Guerrero) personaje;
            g.speedBase -= bonusVelocidad;
        }
    }

    public float getBonusVelocidad() {
        return activa ? bonusVelocidad : 0f;
    }

    public float getBonusAtaque() {
        return activa ? bonusAtaque : 0f;
    }
}
