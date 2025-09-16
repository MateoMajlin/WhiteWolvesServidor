package winterwolves.personajes.habilidadesGuerrero;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class ConcentracionMaxima {

    private boolean activa = false;       // true cuando el bonus está activo
    private boolean cargando = false;     // true durante los 2s de carga
    private float duracion;               // duración del bonus
    private float tiempoActual = 0f;
    private final float tiempoCarga = 2f; // 2 segundos de carga
    private float tiempoDesdeUltimoUso;

    private final float cooldown;
    private final float bonusVelocidad;
    private final float bonusAtaque;

    private Animation<TextureRegion> animacion;
    private float tiempoAnimacion = 0f;

    public ConcentracionMaxima(float duracion, float cooldown, float bonusVelocidad, float bonusAtaque) {
        this.duracion = duracion;
        this.cooldown = cooldown;
        this.bonusVelocidad = bonusVelocidad;
        this.bonusAtaque = bonusAtaque;
        this.tiempoDesdeUltimoUso = cooldown; // inicia como usable

        cargarAnimacion();
    }

    private void cargarAnimacion() {
        Texture textura = new Texture(Gdx.files.internal("curacion.png"));
        int anchoFrame = 64;
        int altoFrame = 64;
        TextureRegion[][] tmp = TextureRegion.split(textura, anchoFrame, altoFrame);
        TextureRegion[] frames = tmp[0];
        animacion = new Animation<>(0.1f, frames);
    }

    public void actualizar(float delta) {
        tiempoDesdeUltimoUso += delta;

        if (cargando) {
            tiempoActual += delta;
            if (tiempoActual >= tiempoCarga) {
                cargando = false;
                activa = true;
                tiempoActual = 0f;
                tiempoAnimacion = 0f;
            }
        } else if (activa) {
            tiempoActual += delta;
            tiempoAnimacion += delta;
            if (tiempoActual >= duracion) {
                activa = false;
                tiempoAnimacion = 0f;
            }
        }
    }

    public boolean puedeUsarse() {
        return !activa && !cargando && tiempoDesdeUltimoUso >= cooldown;
    }

    public boolean usar() {
        if (puedeUsarse()) {
            cargando = true;
            tiempoActual = 0f;
            tiempoDesdeUltimoUso = 0f;
            tiempoAnimacion = 0f;
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

    public float getBonusVelocidad() {
        return activa ? bonusVelocidad : 0f;
    }

    public float getBonusAtaque() {
        return activa ? bonusAtaque : 0f;
    }

    public float getTiempoDesdeUltimoUso() {
        return tiempoDesdeUltimoUso;
    }

    public float getCooldown() {
        return cooldown;
    }

    public void dibujar(Batch batch, float x, float y, float width, float height) {
        if ((cargando || activa) && animacion != null) {
            TextureRegion frame = animacion.getKeyFrame(tiempoAnimacion, true);
            batch.draw(frame, x, y, width, height);
        }
    }

    public void dispose() {
        if (animacion != null && animacion.getKeyFrames().length > 0) {
            animacion.getKeyFrames()[0].getTexture().dispose();
        }
    }
}
