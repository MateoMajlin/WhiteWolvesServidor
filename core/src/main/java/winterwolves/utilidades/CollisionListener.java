package winterwolves.utilidades;

import com.badlogic.gdx.physics.box2d.*;
import winterwolves.personajes.armas.Arma;
import winterwolves.personajes.habilidades.Proyectil;
import winterwolves.props.Caja;

public class CollisionListener implements ContactListener {

    @Override
    public void beginContact(Contact contact) {
        Object a = contact.getFixtureA().getBody().getUserData();
        Object b = contact.getFixtureB().getBody().getUserData();

        // Arma golpeando caja
        if (a instanceof Arma && b instanceof Caja) {
            ((Caja) b).recibirDaño(((Arma) a).getDañoReal());
        } else if (a instanceof Caja && b instanceof Arma) {
            ((Caja) a).recibirDaño(((Arma) b).getDañoReal());
        }

        // Proyectil golpeando caja
        if (a instanceof Proyectil && b instanceof Caja) {
            Proyectil p = (Proyectil) a;
            ((Caja) b).recibirDaño(p.daño);
            p.muerto = true;
        } else if (a instanceof Caja && b instanceof Proyectil) {
            Proyectil p = (Proyectil) b;
            ((Caja) a).recibirDaño(p.daño);
            p.muerto = true;
        }
    }

    @Override
    public void endContact(Contact contact) {}

    @Override
    public void preSolve(Contact contact, Manifold manifold) {}

    @Override
    public void postSolve(Contact contact, ContactImpulse contactImpulse) {}
}
