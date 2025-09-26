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

public class Personaje extends Sprite {

    protected Body body;
    protected EntradasJugador entradas;
    public float speedBase = 2.5f;
    protected float speed = speedBase;
    protected float multiplicadorCorrer = 1.7f;
    protected float ppm;

    protected int vida = 100;
    public float ataque,ataqueMagico,defensa;

    protected AnimacionJugador animaciones;
    protected Vector2 movimiento = new Vector2();
    protected Vector2 direccionMirando = new Vector2(0, -1);

    protected Dash dash = new Dash(5f, 0.2f, 10f);

    protected boolean puedeMoverse = true;
    protected Inventario inventario;

    protected Item slotArma;
    protected Item slotHabilidad1;
    protected Item slotHabilidad2;

    public Personaje(World world, EntradasJugador entradas, float x, float y, float ppm) {
        super();
        this.entradas = entradas;
        this.inventario = new Inventario();
        this.ppm = ppm;

        animaciones = new AnimacionJugador();

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x, y);
        body = world.createBody(bodyDef);

        setSize(30, 30);

        float margen = 0.7f; // 70% del sprite
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

    @Override
    public void draw(Batch batch) {
        float delta = Gdx.graphics.getDeltaTime();

        mover();
        procesarHabilidades();

        Vector2 pos = body.getPosition();
        setPosition(pos.x * ppm - getWidth() / 2, pos.y * ppm - getHeight() / 2);

        TextureRegion frame;

        if (!puedeMoverse) {
            frame = animaciones.getIdleFrame(direccionMirando);
        } else {
            frame = animaciones.getFrame(movimiento, direccionMirando, speed, speedBase, delta);
        }

        batch.draw(frame, getX(), getY(), getWidth(), getHeight());
    }

    protected void mover() {
        if (!puedeMoverse) {
            body.setLinearVelocity(0, 0);
            return;
        }

        float delta = Gdx.graphics.getDeltaTime();

        dash.update(delta, body, direccionMirando);

        if (entradas.isDash() && dash.intentarActivar(direccionMirando)) {
            return;
        }

        if (dash.isActivo()) return;

        movimiento.set(0, 0);

        speed = entradas.isCorrer() ? speedBase * multiplicadorCorrer : speedBase;

        if (entradas.isArriba()) movimiento.y = 1;
        if (entradas.isAbajo()) movimiento.y = -1;
        if (entradas.isIzquierda()) movimiento.x = -1;
        if (entradas.isDerecha()) movimiento.x = 1;

        if (movimiento.len() > 0) {
            direccionMirando.set(movimiento).nor();
        }
        movimiento.nor().scl(speed);
        body.setLinearVelocity(movimiento.x, movimiento.y);
    }


    protected void procesarHabilidades() {
        if (entradas.isGolpeBasico()) usarHabilidadBasica();
        if (entradas.isHabilidad1()) usarHabilidadEspecial();
        if (entradas.isHabilidad2()) usarUltimate();
    }

    public void usarHabilidadBasica() {}
    public void usarHabilidadEspecial() {}
    public void usarUltimate() {}

    public float getTiempoDesdeUltimoDash() { return dash.getTiempoDesdeUltimo(); }
    public int getVida() { return vida; }
    public void setPuedeMoverse(boolean valor) { this.puedeMoverse = valor; }
    public void setVida(int nuevaVida) {
        vida = Math.min(nuevaVida, 100);
        if (vida < 0) vida = 0;
    }
    public Inventario getInventario() { return inventario; }
    public boolean getPuedeMoverse() { return puedeMoverse; }

    public void equiparArma(Item item) {
        if (inventario.getItems().contains(item)) {
            slotArma = item;
        }
    }

    public void equiparItem1(Item item) {
        if (inventario.getItems().contains(item)) {
            slotHabilidad1 = item;
        }
    }

    public void equiparItem2(Item item) {
        if (inventario.getItems().contains(item)) {
            slotHabilidad2 = item;
        }
    }

    public void quitarArma() { slotArma = null; }
    public void quitarItem1() { slotHabilidad1 = null; }
    public void quitarItem2() { slotHabilidad2 = null; }

    public void dispose() { animaciones.dispose(); }

    public String getClase() {
        return "";
    }

    public float getAtaque() {
        return ataque;
    }

    public float getAtaqueMagico() {
        return ataqueMagico;
    }

    public float getDefensa() {
        return defensa;
    }
}
