package winterwolves.personajes.habilidades;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import java.util.*;
import winterwolves.personajes.Personaje;

public class BolaDeFuego extends Habilidad {

    private final float velocidad = 100f;
    private final int daño;
    private final List<Proyectil> proyectiles = new ArrayList<>();
    private final Texture textura;
    private final Animation<TextureRegion> animacionBola;

    public enum Direccion { UP, DOWN, LEFT, RIGHT, UP_LEFT, UP_RIGHT, DOWN_LEFT, DOWN_RIGHT }

    private static final Map<Direccion, Vector2> startOffsets = new HashMap<>();
    static {
        startOffsets.put(Direccion.UP, new Vector2(0, 20));
        startOffsets.put(Direccion.DOWN, new Vector2(0, -20));
        startOffsets.put(Direccion.LEFT, new Vector2(-20, 0));
        startOffsets.put(Direccion.RIGHT, new Vector2(20, 0));
        startOffsets.put(Direccion.UP_LEFT, new Vector2(-15, 15));
        startOffsets.put(Direccion.UP_RIGHT, new Vector2(15, 15));
        startOffsets.put(Direccion.DOWN_LEFT, new Vector2(-15, -15));
        startOffsets.put(Direccion.DOWN_RIGHT, new Vector2(15, -15));
    }

    public BolaDeFuego(float duracion, float cooldown, int daño) {
        super(duracion, cooldown);
        this.daño = daño;

        textura = new Texture("bolaDeFuego.png");
        TextureRegion[][] tmp = TextureRegion.split(textura, 32, 32);

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
        Vector2 centro = new Vector2(
            personaje.body.getPosition().x * personaje.ppm,
            personaje.body.getPosition().y * personaje.ppm
        );

        Vector2 dir = new Vector2(personaje.direccionMirando).nor();
        Direccion direccionEnum = vectorADireccion(dir);
        Vector2 offset = startOffsets.getOrDefault(direccionEnum, new Vector2(0, 0));

        Vector2 pos = centro.cpy().add(dir.cpy().scl(40f)).add(offset);

        proyectiles.add(new Proyectil(
            personaje.world,
            pos,
            dir,
            velocidad,
            daño,
            animacionBola,
            duracion,
            personaje,
            direccionEnum
        ));
    }

    @Override
    protected void finalizarEfecto() {}

    @Override
    public void actualizar(float delta) {
        super.actualizar(delta);

        Iterator<Proyectil> it = proyectiles.iterator();
        while (it.hasNext()) {
            Proyectil p = it.next();
            p.actualizar(delta);
            if (p.estaMuerto()) {
                p.destruir(personaje.world);
                it.remove();
            }
        }
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
}
