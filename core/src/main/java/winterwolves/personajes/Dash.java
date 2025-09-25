package winterwolves.personajes;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class Dash {
    private final float cooldown;
    private final float duracion;
    private final float velocidad;

    private float tiempoDesdeUltimo = 0f;
    private float tiempoActivo = 0f;
    private boolean activo = false;

    public Dash(float cooldown, float duracion, float velocidad) {
        this.cooldown = cooldown;
        this.duracion = duracion;
        this.velocidad = velocidad;
    }

    public void update(float delta, Body body, Vector2 direccion) {

        tiempoDesdeUltimo += delta;

        if (activo) {
            tiempoActivo += delta;
            body.setLinearVelocity(direccion.x * velocidad, direccion.y * velocidad);
            if (tiempoActivo >= duracion) {
                activo = false;
                tiempoActivo = 0f;
            }
        }

    }

    public boolean intentarActivar(Vector2 direccion) {
        if (!activo && tiempoDesdeUltimo >= cooldown && direccion.len() > 0) {
            activo = true;
            tiempoDesdeUltimo = 0f;
            tiempoActivo = 0f;
            return true;
        }
        return false;
    }

    public boolean isActivo() { return activo; }
    public float getTiempoDesdeUltimo() { return tiempoDesdeUltimo; }
    public float getCooldown() { return cooldown; }
}
