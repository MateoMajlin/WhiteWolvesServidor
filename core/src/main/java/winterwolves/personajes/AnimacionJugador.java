package winterwolves.personajes;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class AnimacionJugador {

    private Texture spriteSheet;
    private Animation<TextureRegion> animIdle, animRight, animLeft, animUp, animDown;
    private float stateTime;

    public AnimacionJugador() {
        spriteSheet = new Texture("zorrito.png");
        TextureRegion[][] tmp = TextureRegion.split(spriteSheet, 32, 32);

        animIdle = crearAnimacionFila(tmp, 0, 0.2f);
        animRight = crearAnimacionFila(tmp, 1, 0.1f);
        animLeft = crearAnimacionFila(tmp, 2, 0.1f);
        animUp = crearAnimacionFila(tmp, 3, 0.1f);
        animDown = crearAnimacionFila(tmp, 5, 0.1f);

        stateTime = 0f;
    }

    private Animation<TextureRegion> crearAnimacionFila(TextureRegion[][] tmp, int fila, float frameTime) {
        TextureRegion[] frames = new TextureRegion[4];
        for (int i = 0; i < 4; i++) {
            frames[i] = tmp[fila][i];
        }
        return new Animation<TextureRegion>(frameTime, frames);
    }

    public TextureRegion getFrame(Vector2 movimiento, float delta) {
        stateTime += delta;

        if (movimiento.len() == 0) return animIdle.getKeyFrame(stateTime, true);
        if (movimiento.x > 0) return animRight.getKeyFrame(stateTime, true);
        if (movimiento.x < 0) return animLeft.getKeyFrame(stateTime, true);
        if (movimiento.y > 0) return animUp.getKeyFrame(stateTime, true);
        if (movimiento.y < 0) return animDown.getKeyFrame(stateTime, true);

        return animIdle.getKeyFrame(stateTime, true);
    }

    public void dispose() {
        spriteSheet.dispose();
    }
}
