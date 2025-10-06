package winterwolves.personajes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import winterwolves.io.EntradasJugador;
import winterwolves.items.Inventario;
import winterwolves.items.Item;
import winterwolves.personajes.habilidades.Habilidad;
import winterwolves.personajes.armas.Arma;

public class Personaje extends Sprite implements Hudeable {

    // --- Atributos básicos ---
    public Body body;
    protected EntradasJugador entradas;
    protected float speedBase = 2.5f;
    protected float speed = speedBase;
    protected float multiplicadorCorrer = 1.7f;
    public float ppm;

    protected int vida = 100;
    protected float ataque, ataqueMagico, defensa;

    protected AnimacionPersonaje animaciones;
    protected Vector2 movimiento = new Vector2();
    public Vector2 direccionMirando = new Vector2(0, -1);

    protected Dash dash = new Dash(5f, 0.2f, 10f);
    protected boolean puedeMoverse = true;

    protected Inventario inventario;

    // --- Slots de equipo ---
    protected Arma armaBasica;
    protected Habilidad habilidad1;
    protected Habilidad habilidad2;

    protected Item slotArma;
    protected Item slotHabilidad1;
    protected Item slotHabilidad2;

    protected String nombreClase = "Personaje";

    public World world;

    public Personaje(World world, EntradasJugador entradas, float x, float y, float ppm) {
        this.world = world;
        this.entradas = entradas;
        this.inventario = new Inventario();
        this.ppm = ppm;
        animaciones = new AnimacionPersonaje("zorrito.png");

        // Crear cuerpo en Box2D
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x, y);
        body = world.createBody(bodyDef);

        setSize(30, 30);
        float margen = 0.7f;
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(getWidth()/2f * margen / ppm, getHeight()/2f * margen / ppm);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;
        fixtureDef.friction = 0.2f;
        fixtureDef.restitution = 0f;
        body.createFixture(fixtureDef);
        shape.dispose();
    }

    // --- Draw ---
    @Override
    public void draw(Batch batch) {
        float delta = Gdx.graphics.getDeltaTime();

        // Actualizar habilidades
        if (habilidad1 != null) {
            habilidad1.actualizar(delta);
            if (entradas.isHabilidad1()) habilidad1.usar();
        }
        if (habilidad2 != null) {
            habilidad2.actualizar(delta);
            if (entradas.isHabilidad2()) habilidad2.usar();
        }

        // Movimiento y animaciones
        mover();
        procesarHabilidades();

        Vector2 pos = body.getPosition();
        setPosition(pos.x * ppm - getWidth()/2, pos.y * ppm - getHeight()/2);

        TextureRegion frame = (!puedeMoverse)
            ? animaciones.getIdleFrame(direccionMirando)
            : animaciones.getFrame(movimiento, direccionMirando, speed, speedBase, delta);
        batch.draw(frame, getX(), getY(), getWidth(), getHeight());

        // Dibujar arma
        if (armaBasica != null) {
            float desplazamiento = getWidth() * 0.6f;
            float armaX = getX() + direccionMirando.x * desplazamiento;
            float armaY = getY() + direccionMirando.y * desplazamiento;

            if (entradas.isGolpeBasico()) {
                armaBasica.atacar(armaX, armaY, direccionMirando, this);
            }

            armaBasica.actualizar(delta, armaX, armaY, this);
            armaBasica.draw(batch, armaX, armaY, getWidth(), getHeight(), direccionMirando.angleDeg());
        }

        // Dibujar habilidades
        if (habilidad1 != null) habilidad1.dibujar(batch, getX(), getY(), getWidth(), getHeight());
        if (habilidad2 != null) habilidad2.dibujar(batch, getX(), getY(), getWidth(), getHeight());
    }

    // --- Movimiento ---
    protected void mover() {
        if (!puedeMoverse) {
            body.setLinearVelocity(0,0);
            return;
        }

        float delta = Gdx.graphics.getDeltaTime();
        dash.update(delta, body, direccionMirando);

        if (entradas.isDash() && dash.intentarActivar(direccionMirando)) return;
        if (dash.isActivo()) return;

        movimiento.set(0,0);
        speed = entradas.isCorrer() ? speedBase * multiplicadorCorrer : speedBase;

        if (entradas.isArriba()) movimiento.y = 1;
        if (entradas.isAbajo()) movimiento.y = -1;
        if (entradas.isIzquierda()) movimiento.x = -1;
        if (entradas.isDerecha()) movimiento.x = 1;

        if (movimiento.len() > 0) direccionMirando.set(movimiento).nor();
        movimiento.nor().scl(speed);
        body.setLinearVelocity(movimiento.x, movimiento.y);
    }

    // --- Procesar habilidades ---
    protected void procesarHabilidades() {
        if (entradas.isGolpeBasico()) usarHabilidadBasica();
        if (entradas.isHabilidad1()) usarHabilidadEspecial();
        if (entradas.isHabilidad2()) usarUltimate();
    }

    public void usarHabilidadBasica() {}
    public void usarHabilidadEspecial() {}
    public void usarUltimate() {}

    // --- Getters cooldowns ---
    public float getTiempoHabilidad1() { return habilidad1 != null ? habilidad1.getTiempoDesdeUltimoUso() : 0f; }
    public float getCooldownHabilidad1() { return habilidad1 != null ? habilidad1.getCooldown() : 0f; }
    public float getTiempoHabilidad2() { return habilidad2 != null ? habilidad2.getTiempoDesdeUltimoUso() : 0f; }
    public float getCooldownHabilidad2() { return habilidad2 != null ? habilidad2.getCooldown() : 0f; }
    public float getTiempoDesdeUltimoDash() { return dash.getTiempoDesdeUltimo(); }
    public float getCooldownDash() { return dash.getCooldown(); }
    public Arma getArma() { return armaBasica; }
    public String getClase() { return nombreClase; }

    // --- Vida y stats ---
    public int getVida() { return vida; }
    public void setVida(int nuevaVida) {
        vida = Math.min(nuevaVida, 100);
        if (vida < 0) vida = 0;
    }


    public float getAtaque() { return ataque; }
    public float modifAtaque(float monto) {
        return ataque += monto;
    }

    public float modifSpeedBase(float monto) {
        return speedBase += monto;
    }

    public float getAtaqueMagico() { return ataqueMagico; }
    public float getDefensa() { return defensa; }
    public void setPuedeMoverse(boolean valor) { puedeMoverse = valor; }
    public boolean getPuedeMoverse() { return puedeMoverse; }

    // --- Inventario y slots ---
    public Inventario getInventario() { return inventario; }
    public void equiparArma(Item item) { if (inventario.getItems().contains(item)) slotArma = item; }
    public void equiparItem1(Item item) { if (inventario.getItems().contains(item)) slotHabilidad1 = item; }
    public void equiparItem2(Item item) { if (inventario.getItems().contains(item)) slotHabilidad2 = item; }
    public void quitarArma() { slotArma = null; }
    public void quitarItem1() { slotHabilidad1 = null; }
    public void quitarItem2() { slotHabilidad2 = null; }

    // --- Limpieza ---
    public void dispose() {
        animaciones.dispose();
        if (armaBasica != null) armaBasica.dispose();
        if (habilidad1 != null) habilidad1.dispose();
        if (habilidad2 != null) habilidad2.dispose();
    }
}
