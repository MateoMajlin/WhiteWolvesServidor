package winterwolves.personajes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class GolpeEspada {

    private Animation<TextureRegion> animacion;
    private float stateTime;
    private boolean activo;
    private Texture hoja;

    public GolpeEspada() {
        hoja = new Texture(Gdx.files.internal("espadaAnimacion.png"));

        TextureRegion[][] tmp = TextureRegion.split(hoja,
            hoja.getWidth() / 8,   // 8 columnas (según la imagen)
            hoja.getHeight()       // 1 fila
        );

        TextureRegion[] frames = new TextureRegion[8];
        for (int i = 0; i < 8; i++) {
            frames[i] = tmp[0][i];
        }

        animacion = new Animation<TextureRegion>(0.05f, frames); // 0.05 seg por frame
        animacion.setPlayMode(Animation.PlayMode.NORMAL);
        stateTime = 0;
        activo = false;
    }

    public void activar() {
        if (!activo) {
            activo = true;
            stateTime = 0;
        }
    }

    public void update(float delta) {
        if (activo) {
            stateTime += delta;
            if (animacion.isAnimationFinished(stateTime)) {
                activo = false;
            }
        }
    }

    public void draw(Batch batch, float x, float y, float width, float height, float angle) {
        if (activo) {
            TextureRegion frame = animacion.getKeyFrame(stateTime);

            batch.draw(
                frame, x, y, width / 2, height / 2, width, height, 1, 1, angle
            );
        }
    }

    public boolean isActivo() {
        return activo;
    }

    public void dispose() {
        hoja.dispose();
    }
}
