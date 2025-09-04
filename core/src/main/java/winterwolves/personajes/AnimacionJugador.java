package winterwolves.personajes;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import java.util.ArrayList;
import java.util.List;

public class AnimacionJugador {

    private Texture spriteSheet;
    private Animation<TextureRegion> idleAbajo, idleDerecha, idleIzquierda, idleArriba;
    private Animation<TextureRegion> correrAbajo, correrDerecha, correrIzquierda, correrArriba;
    private float stateTime;

    public AnimacionJugador() {
        spriteSheet = new Texture("zorrito.png");
        TextureRegion[][] tmp = TextureRegion.split(spriteSheet, 32, 32);

        // --- Idle ---
        idleAbajo = crearAnimacionFila(tmp, 0, 0.2f);
        idleDerecha = crearAnimacionFila(tmp, 1, 0.2f);
        idleIzquierda = crearAnimacionFila(tmp, 2, 0.2f);
        idleArriba = crearAnimacionFila(tmp, 3, 0.2f);

        // --- Correr: filas dobles ---
        correrAbajo = crearAnimacionFilas(tmp, 5, 6, 0.07f);
        correrIzquierda = crearAnimacionFilas(tmp, 7, 8, 0.07f);
        correrDerecha = crearAnimacionFilas(tmp, 9, 10, 0.07f);
        correrArriba = crearAnimacionFilas(tmp, 11, 12, 0.07f);

        stateTime = 0f;
    }

    private Animation<TextureRegion> crearAnimacionFila(TextureRegion[][] tmp, int fila, float frameTime) {
        int columnas = 4;
        TextureRegion[] frames = new TextureRegion[columnas];
        for (int i = 0; i < columnas; i++) {
            frames[i] = tmp[fila][i];
        }
        return new Animation<>(frameTime, frames);
    }

    private Animation<TextureRegion> crearAnimacionFilas(TextureRegion[][] tmp, int filaInicio, int filaFin, float frameTime) {
        List<TextureRegion> framesList = new ArrayList<>();
        int columnas = 4;
        for (int fila = filaInicio; fila <= filaFin; fila++) {
            for (int i = 0; i < columnas; i++) {
                framesList.add(tmp[fila][i]);
            }
        }
        TextureRegion[] frames = framesList.toArray(new TextureRegion[0]);
        return new Animation<>(frameTime, frames);
    }

    public TextureRegion getFrame(Vector2 movimiento, Vector2 direccionMirando, float speed, float speedBase, float delta) {
        stateTime += delta;
        boolean seMueve = movimiento.len() > 0.1f;
        boolean corriendo = seMueve && speed > speedBase;

        if (direccionMirando.x > 0.5f) {
            return corriendo ? correrDerecha.getKeyFrame(stateTime, true) : idleDerecha.getKeyFrame(stateTime, true);
        } else if (direccionMirando.x < -0.5f) {
            return corriendo ? correrIzquierda.getKeyFrame(stateTime, true) : idleIzquierda.getKeyFrame(stateTime, true);
        } else if (direccionMirando.y > 0.5f) {
            return corriendo ? correrArriba.getKeyFrame(stateTime, true) : idleArriba.getKeyFrame(stateTime, true);
        } else {
            return corriendo ? correrAbajo.getKeyFrame(stateTime, true) : idleAbajo.getKeyFrame(stateTime, true);
        }
    }

    public void dispose() {
        spriteSheet.dispose();
    }
}
