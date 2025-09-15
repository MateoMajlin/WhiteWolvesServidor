package winterwolves.utilidades;

import com.badlogic.gdx.physics.box2d.*;
import winterwolves.personajes.habilidadesGuerrero.Arma;
import winterwolves.props.Caja;

public class CollisionListener implements ContactListener {

    @Override
    public void beginContact(Contact contact) {
        Object a = contact.getFixtureA().getBody().getUserData();
        Object b = contact.getFixtureB().getBody().getUserData();

        // Arma golpeando caja
        if (a instanceof Arma && b instanceof Caja) {
            Caja caja = (Caja) b;
            Arma arma = (Arma) a;
            caja.recibirDaño(arma.getDaño());
        } else if (a instanceof Caja && b instanceof Arma) {
            Caja caja = (Caja) a;
            Arma arma = (Arma) b;
            caja.recibirDaño(arma.getDaño());
        }
    }

    @Override
    public void endContact(Contact contact) {}

    @Override
    public void preSolve(Contact contact, Manifold manifold) {}

    @Override
    public void postSolve(Contact contact, ContactImpulse contactImpulse) {}
}
