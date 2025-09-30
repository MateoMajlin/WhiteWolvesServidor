package winterwolves.personajes.habilidades;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import java.util.*;

import winterwolves.personajes.Personaje;

public class BolaDeFuego extends Habilidad {

    private final float velocidad = 50f;
    private final int daño;
    private final List<Proyectil> proyectiles = new ArrayList<>();
    private final Texture textura;
    private final Animation<TextureRegion> animacionBola;

    // --- Offsets por dirección ---
    private static final Map<Direccion, Vector2> offsets = new HashMap<>();
    static {
        offsets.put(Direccion.UP, new Vector2(-10, 10));
        offsets.put(Direccion.DOWN, new Vector2(-55, -70));
        offsets.put(Direccion.LEFT, new Vector2(-70, -10));
        offsets.put(Direccion.RIGHT, new Vector2(10, -50));
        offsets.put(Direccion.UP_LEFT, new Vector2(-44, 14));
        offsets.put(Direccion.UP_RIGHT, new Vector2(14, -14));
        offsets.put(Direccion.DOWN_LEFT, new Vector2(-77, -50));
        offsets.put(Direccion.DOWN_RIGHT, new Vector2(-14, -77));
    }

    public enum Direccion { UP, DOWN, LEFT, RIGHT, UP_LEFT, UP_RIGHT, DOWN_LEFT, DOWN_RIGHT }

    public BolaDeFuego(float duracion, float cooldown, int daño) {
        super(duracion, cooldown);
        this.daño = daño;

        textura = new Texture("bolaDeFuego.png"); // sprite sheet horizontal
        TextureRegion[][] tmp = TextureRegion.split(textura, 32, 32); // tamaño de cada frame

        int offsetX = 18;
        int offsetY = 4;
        int anchoFrame = 64;
        int altoFrame = 64;

        TextureRegion[] frames = new TextureRegion[5];
        for (int i = 0; i < 5; i++) {
            frames[i] = new TextureRegion(textura,
                offsetX + i * anchoFrame,
                offsetY,
                anchoFrame,
                altoFrame);
        }

        animacionBola = new Animation<>(0.08f, frames);
    }

    @Override
    protected void iniciarEfecto() {

        Vector2 centro = new Vector2(personaje.body.getPosition().x * personaje.ppm,
            personaje.body.getPosition().y * personaje.ppm);

        Vector2 dir = new Vector2(personaje.direccionMirando).nor();

        Direccion direccionEnum = vectorADireccion(dir);
        Vector2 offset = offsets.getOrDefault(direccionEnum, new Vector2(0, 0));

        Vector2 pos = new Vector2(centro.x + offset.x, centro.y + offset.y);

        proyectiles.add(new Proyectil(pos, dir, velocidad, daño, animacionBola, duracion));
    }

    @Override
    protected void finalizarEfecto() {}

    @Override
    public void actualizar(float delta) {
        super.actualizar(delta);
        proyectiles.removeIf(Proyectil::estaMuerto);
        for (Proyectil p : proyectiles) p.actualizar(delta);
    }

    @Override
    public void dibujar(Batch batch, float x, float y, float width, float height) {
        for (Proyectil p : proyectiles) p.dibujar(batch);
    }

    @Override
    public void dispose() { textura.dispose(); }

    private static Direccion vectorADireccion(Vector2 dir) {
        float angle = (float) Math.toDegrees(Math.atan2(dir.y, dir.x));
        if (angle >= -22.5 && angle < 22.5) return Direccion.RIGHT;
        if (angle >= 22.5 && angle < 67.5) return Direccion.UP_RIGHT;
        if (angle >= 67.5 && angle < 112.5) return Direccion.UP;
        if (angle >= 112.5 && angle < 157.5) return Direccion.UP_LEFT;
        if (angle >= -67.5 && angle < -22.5) return Direccion.DOWN_RIGHT;
        if (angle >= -112.5 && angle < -67.5) return Direccion.DOWN;
        if (angle >= -157.5 && angle < -112.5) return Direccion.DOWN_LEFT;
        return Direccion.LEFT;
    }

    private static class Proyectil {
        Vector2 pos;
        Vector2 dir;
        float velocidad;
        int daño;
        Animation<TextureRegion> animacion;
        float stateTime = 0f;
        boolean muerto = false;
        float tiempoRestante;

        public Proyectil(Vector2 pos, Vector2 dir, float velocidad, int daño, Animation<TextureRegion> animacion, float duracion) {
            this.pos = new Vector2(pos);
            this.dir = new Vector2(dir).nor();
            this.velocidad = velocidad;
            this.daño = daño;
            this.animacion = animacion;
            this.tiempoRestante = duracion;
        }

        public void actualizar(float delta) {
            pos.mulAdd(dir, velocidad * delta);
            tiempoRestante -= delta;

            if (tiempoRestante <= 0) muerto = true;
        }

        public void dibujar(Batch batch) {
            stateTime += 0.016f;
            TextureRegion frame = animacion.getKeyFrame(stateTime, true);

            float width = 64;
            float height = 64;
            float angle = dir.angleDeg();

            batch.draw(frame, pos.x, pos.y, width / 2f, height / 2f, width, height, 1f, 1f, angle);
        }

        public boolean estaMuerto() { return muerto; }
    }
}
