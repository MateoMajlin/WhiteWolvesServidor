package winterwolves.personajes;
import winterwolves.personajes.armas.Arma;

public interface Hudeable {

        int getVida();

        Arma getArma();

        float getTiempoHabilidad1();
        float getCooldownHabilidad1();

        float getTiempoHabilidad2();
        float getCooldownHabilidad2();

        float getTiempoDesdeUltimoDash();
        float getCooldownDash();

}
