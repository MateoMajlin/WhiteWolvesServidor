package winterwolves.utilidades;

import com.badlogic.gdx.physics.box2d.*;
import winterwolves.personajes.habilidadesGuerrero.GolpeEspada;
import winterwolves.props.Caja;

public class CollisionListener implements ContactListener {

    @Override
    public void beginContact(Contact contact) {
        Object a = contact.getFixtureA().getBody().getUserData();
        Object b = contact.getFixtureB().getBody().getUserData();

        if (a instanceof GolpeEspada && b instanceof Caja) {
            ((Caja) b).destruir();
        } else if (a instanceof Caja && b instanceof GolpeEspada) {
            ((Caja) a).destruir();
        }
    }

    @Override
    public void endContact(Contact contact) {}
    @Override
    public void preSolve(Contact contact, Manifold manifold) {}
    @Override
    public void postSolve(Contact contact, ContactImpulse contactImpulse) {}
}
