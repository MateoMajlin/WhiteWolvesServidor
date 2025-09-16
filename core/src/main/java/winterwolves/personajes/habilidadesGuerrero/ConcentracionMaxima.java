package winterwolves.personajes.habilidadesGuerrero;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class ConcentracionMaxima {

    private boolean activa = false;
    private float duracion; // duración del efecto
    private float tiempoActual = 0f;

    private final float cooldown;
    private float tiempoDesdeUltimoUso;

    private float bonusVelocidad; // incremento de velocidad
    private float bonusAtaque;    // incremento de daño del arma

    private Animation<TextureRegion> animacion;
    private float tiempoAnimacion = 0f;

    public ConcentracionMaxima(float duracion, float cooldown, float bonusVelocidad, float bonusAtaque) {
        this.duracion = duracion;
        this.cooldown = cooldown;
        this.bonusVelocidad = bonusVelocidad;
        this.bonusAtaque = bonusAtaque;

        // Iniciamos como "usable"
        this.tiempoDesdeUltimoUso = cooldown;

        cargarAnimacion();
    }

    private void cargarAnimacion() {
        // Si querés, podés usar un sprite para mostrar el efecto
        Texture textura = new Texture(Gdx.files.internal("concentracion.png"));
        int anchoFrame = 64;
        int altoFrame = 64;
        TextureRegion[][] tmp = TextureRegion.split(textura, anchoFrame, altoFrame);
        TextureRegion[] frames = tmp[0];
        animacion = new Animation<>(0.1f, frames);
    }

    public void actualizar(float delta) {
        tiempoDesdeUltimoUso += delta;

        if (activa) {
            tiempoActual += delta;
            tiempoAnimacion += delta;

            if (tiempoActual >= duracion) {
                activa = false;
                tiempoAnimacion = 0f;
            }
        }
    }

    public boolean puedeUsarse() {
        return !activa && tiempoDesdeUltimoUso >= cooldown;
    }

    public boolean usar() {
        if (puedeUsarse()) {
            activa = true;
            tiempoActual = 0f;
            tiempoDesdeUltimoUso = 0f;
            tiempoAnimacion = 0f;
            return true;
        }
        return false;
    }

    public void dibujar(Batch batch, float x, float y, float width, float height) {
        if (activa && animacion != null) {
            TextureRegion frame = animacion.getKeyFrame(tiempoAnimacion, true);
            batch.draw(frame, x, y, width, height);
        }
    }

    public boolean isActiva() {
        return activa;
    }

    public float getTiempoDesdeUltimoUso() {
        return tiempoDesdeUltimoUso;
    }

    public float getCooldown() {
        return cooldown;
    }

    public float getBonusVelocidad() {
        return activa ? bonusVelocidad : 0f;
    }

    public float getBonusAtaque() {
        return activa ? bonusAtaque : 0f;
    }

    public void dispose() {
        if (animacion != null && animacion.getKeyFrames().length > 0) {
            animacion.getKeyFrames()[0].getTexture().dispose();
        }
    }
}
