package winterwolves.personajes.habilidades;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import winterwolves.personajes.Personaje;

public abstract class Habilidad {

    protected boolean activa = false;       // Habilidad en ejecución
    protected boolean cargando = false;     // Solo si tiene fase de carga
    protected float duracion;               // Duración del efecto
    protected float tiempoActual = 0f;      // Contador interno
    protected float tiempoDesdeUltimoUso;   // Para cooldown
    protected final float cooldown;         // Cooldown total
    protected Animation<TextureRegion> animacion;
    protected float tiempoAnimacion = 0f;
    protected Personaje personaje;

    public Habilidad(float duracion, float cooldown) {
        this.duracion = duracion;
        this.cooldown = cooldown;
        this.tiempoDesdeUltimoUso = cooldown; // inicia lista para usar
    }

    public void setPersonaje(Personaje p) {
        this.personaje = p;
    }

    protected abstract void iniciarEfecto();

    protected abstract void finalizarEfecto();

    public void actualizar(float delta) {
        tiempoDesdeUltimoUso += delta;

        if (cargando) {
            tiempoActual += delta;
            if (tiempoActual >= getTiempoCarga()) {
                cargando = false;
                activa = true;
                tiempoActual = 0f;
                tiempoAnimacion = 0f;
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

    protected float getTiempoCarga() {
        return 0f;
    }

    public boolean puedeUsarse() {
        return !activa && !cargando && tiempoDesdeUltimoUso >= cooldown;
    }

    public boolean usar() {
        if (puedeUsarse()) {
            tiempoActual = 0f;
            tiempoDesdeUltimoUso = 0f;
            tiempoAnimacion = 0f;
            if (getTiempoCarga() > 0) {
                cargando = true;
            } else {
                activa = true;
                iniciarEfecto();
            }
            return true;
        }
        return false;
    }

    public boolean isActiva() {
        return activa;
    }

    public boolean isCargando() {
        return cargando;
    }

    public float getTiempoDesdeUltimoUso() {
        return tiempoDesdeUltimoUso;
    }

    public float getCooldown() {
        return cooldown;
    }

    public void dibujar(Batch batch, float x, float y, float width, float height) {
        if ((activa || cargando) && animacion != null) {
            TextureRegion frame = animacion.getKeyFrame(tiempoAnimacion, true);
            batch.draw(frame, x, y, width, height);
        }
    }

    public void dispose() {
        if (animacion != null && animacion.getKeyFrames().length > 0) {
            animacion.getKeyFrames()[0].getTexture().dispose();
        }
    }

    public float getBonusVelocidad() {
        return 0;
    }

    public float getBonusAtaque() {
        return 0;
    }
}
