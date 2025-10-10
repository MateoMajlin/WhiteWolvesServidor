package winterwolves.personajes.habilidades;

import com.badlogic.gdx.math.Vector2;

public class DireccionUtil {

    public enum Direccion {
        UP, DOWN, LEFT, RIGHT,
        UP_LEFT, UP_RIGHT, DOWN_LEFT, DOWN_RIGHT
    }

    public static Direccion vectorADireccion(Vector2 dir) {
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
