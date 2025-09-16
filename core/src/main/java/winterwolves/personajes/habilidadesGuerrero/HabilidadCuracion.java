package winterwolves.personajes.habilidadesGuerrero;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class HabilidadCuracion {

    private boolean activa = false;
    private float duracion;
    private float tiempoActual = 0f;

    private final float cooldown;
    private float tiempoDesdeUltimoUso;

    private int curacion;

    private Animation<TextureRegion> animacion;
    private float tiempoAnimacion = 0f;

    public HabilidadCuracion(float duracion, float cooldown, int curacion) {
        this.duracion = duracion;
        this.cooldown = cooldown;
        this.curacion = curacion;
        this.tiempoDesdeUltimoUso = cooldown;

        cargarAnimacion();
    }

    private void cargarAnimacion() {
        Texture textura = new Texture(Gdx.files.internal("curacion.png"));
        int anchoFrame = 64;
        int altoFrame = 64;

        TextureRegion[][] tmp = TextureRegion.split(textura, anchoFrame, altoFrame);
        TextureRegion[] frames = tmp[3];

        animacion = new Animation<>(0.1f, frames); // cada frame dura 0.2s
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

    public int usar() {
        if (puedeUsarse()) {
            activa = true;
            tiempoActual = 0f;
            tiempoDesdeUltimoUso = 0f;
            tiempoAnimacion = 0f;
            return curacion;
        }
        return 0;
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

    public void dispose() {
        if (animacion != null && animacion.getKeyFrames().length > 0) {
            animacion.getKeyFrames()[0].getTexture().dispose();
        }
    }
}
