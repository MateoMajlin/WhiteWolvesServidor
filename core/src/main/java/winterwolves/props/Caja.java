package winterwolves.props;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class Caja extends Sprite {

    private Body body;
    private float ppm;
    private boolean activa = true;
    private boolean marcadaParaDestruir = false;

    private float vida;     // vida actual
    private float vidaMax;  // vida máxima

    // === HUD de vida (texto) ===
    private static BitmapFont font = new BitmapFont(); // fuente por defecto
    private static GlyphLayout layout = new GlyphLayout();

    // === Mostrar vida solo al recibir daño ===
    private boolean mostrarVida = false;
    private float tiempoMostrarVida = 0f;
    private final float DURACION_MOSTRAR_VIDA = 2f;
    private final float VELOCIDAD_SUBIDA = 30f;

    public Caja(World world, float x, float y, float ppm, float vida) {
        super(new Texture("caja.png"));
        this.ppm = ppm;
        this.vida = vida;
        this.vidaMax = vida;

        setSize(32, 32);

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(x, y);

        body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(getWidth()/4f / ppm, getHeight()/4f / ppm); // hitbox coincide con sprite

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.friction = 0.5f;
        fixtureDef.density = 1f;
        fixtureDef.restitution = 0f;

        body.createFixture(fixtureDef).setUserData(this);
        body.setUserData(this);

        shape.dispose();
    }

    @Override
    public void draw(Batch batch) {
        if (!activa || body == null) return;

        Vector2 pos = body.getPosition();
        setPosition(pos.x * ppm - getWidth() / 2, pos.y * ppm - getHeight() / 2);
        super.draw(batch);
    }

    // === Actualizar estado de HUD ===
    public void actualizar(float delta) {
        if (mostrarVida) {
            tiempoMostrarVida += delta;
            if (tiempoMostrarVida >= DURACION_MOSTRAR_VIDA) {
                mostrarVida = false;
            }
        }
    }

    // === Vida ===
    public void recibirDaño(float cantidad) {
        if (!activa) return;

        vida -= cantidad;
        mostrarVida = true;        // mostrar HUD al recibir daño
        tiempoMostrarVida = 0f;    // reiniciar contador

        if (vida <= 0) {
            vida = 0;
            destruir();
        }
    }

    public float getVida() {
        return vida;
    }

    public float getVidaMax() {
        return vidaMax;
    }

    public void drawVidaTexto(Batch batch) {
        if (!activa || body == null || !mostrarVida) return;

        String texto = (int) vida + " / " + (int) vidaMax;
        layout.setText(font, texto);

        float x = getX() + getWidth() / 2f - layout.width / 2f;

        // Subida constante según delta
        float y = getY() + getHeight() + VELOCIDAD_SUBIDA * tiempoMostrarVida;

        // Alfa lineal
        float alfa = 1f - (tiempoMostrarVida / DURACION_MOSTRAR_VIDA);

        // Color según porcentaje de vida
        float porcentaje = vida / vidaMax;
        if (porcentaje > 0.6f) font.setColor(0, 1, 0, alfa);
        else if (porcentaje > 0.3f) font.setColor(1, 1, 0, alfa);
        else font.setColor(1, 0, 0, alfa);

        font.draw(batch, texto, x, y);
        font.setColor(1, 1, 1, 1);
    }



    public void destruir() {
        marcadaParaDestruir = true;
    }

    public boolean isMarcadaParaDestruir() {
        return marcadaParaDestruir;
    }

    public void eliminarDelMundo() {
        if (body != null) {
            body.getWorld().destroyBody(body);
            body = null;
        }
        activa = false;
    }

    public Body getBody() {
        return body;
    }

    public void dispose() {
        if (getTexture() != null) {
            getTexture().dispose();
        }
    }
}
