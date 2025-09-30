package winterwolves.personajes.armas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.World;

public class Espada extends Arma {

    public Espada(World world, float ppm) {
        super(world, ppm);

        this.daño = 25f;

        hoja = new Texture(Gdx.files.internal("espadaAnimacion.png"));
        TextureRegion[][] tmp = TextureRegion.split(hoja, hoja.getWidth()/8, hoja.getHeight());
        TextureRegion[] frames = new TextureRegion[8];
        for (int i = 0; i < 8; i++) frames[i] = tmp[0][i];

        animacion = new com.badlogic.gdx.graphics.g2d.Animation<>(0.05f, frames);
        animacion.setPlayMode(com.badlogic.gdx.graphics.g2d.Animation.PlayMode.NORMAL);

        cooldown = 0.6f; // Espada rápida

        hitboxes.put(Direccion.RIGHT, new HitboxConfig(20, 35, 20, 15, 0));
        hitboxes.put(Direccion.LEFT, new HitboxConfig(20, 35, 10, 15, 0));
        hitboxes.put(Direccion.UP, new HitboxConfig(35, 20, 15, 20, 0));
        hitboxes.put(Direccion.DOWN, new HitboxConfig(35, 20, 15, 10, 0));
        hitboxes.put(Direccion.UP_RIGHT, new HitboxConfig(20, 35, 20, 20, 45));
        hitboxes.put(Direccion.UP_LEFT, new HitboxConfig(20, 35, 10, 20, -45));
        hitboxes.put(Direccion.DOWN_RIGHT, new HitboxConfig(20, 35, 20, 10, -45));
        hitboxes.put(Direccion.DOWN_LEFT, new HitboxConfig(20, 35, 10, 10, 45));
    }
}
