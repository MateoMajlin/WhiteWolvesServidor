package winterwolves.personajes.habilidadesGuerrero;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;

public abstract class GolpeArma {

    protected float tiempoDesdeUltimoGolpe = 0f;
    protected float cooldown = 1f;

    // Métodos abstractos que cada arma debe implementar
    public abstract void activar(float x, float y, Direccion dir);
    public abstract void update(float delta, float x, float y);
    public abstract void draw(Batch batch, float x, float y, float width, float height, float angle);
    public abstract boolean isActivo();
    public abstract void dispose();

    // --- Métodos para HUD ---
    public float getTiempoDesdeUltimoGolpe() {
        return tiempoDesdeUltimoGolpe;
    }

    public float getCooldown() {
        return cooldown;
    }

    // Convertir vector a dirección (para todas las armas)
    public static Direccion vectorADireccion(Vector2 dir) {
        float x = dir.x;
        float y = dir.y;

        if (x >= 0.5f && y >= 0.5f) return Direccion.UP_RIGHT;
        if (x <= -0.5f && y >= 0.5f) return Direccion.UP_LEFT;
        if (x >= 0.5f && y <= -0.5f) return Direccion.DOWN_RIGHT;
        if (x <= -0.5f && y <= -0.5f) return Direccion.DOWN_LEFT;
        if (x > 0.5f) return Direccion.RIGHT;
        if (x < -0.5f) return Direccion.LEFT;
        if (y > 0.5f) return Direccion.UP;
        return Direccion.DOWN;
    }

    public void setCooldown(float cooldown) {
        this.cooldown = cooldown;
    }

    public enum Direccion {
        UP, DOWN, LEFT, RIGHT,
        UP_LEFT, UP_RIGHT, DOWN_LEFT, DOWN_RIGHT
    }
}
