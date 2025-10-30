package winterwolves;

import com.badlogic.gdx.graphics.Color;
import winterwolves.elementos.Texto;
import winterwolves.personajes.Personaje;
import winterwolves.utilidades.Config;
import winterwolves.utilidades.Recursos;

public class Partida {

    private Personaje pj1, pj2, pj3, pj4;
    private String nombrePj1, nombrePj2, nombrePj3, nombrePj4;

    private float tiempoRestante;
    private boolean partidaFinalizada;

    // Evitar conteos múltiples por muerte
    private boolean pj1YaContado, pj2YaContado, pj3YaContado, pj4YaContado;

    // Puntuación por equipo
    private int killsEquipo1;
    private int killsEquipo2;

    private Texto textoGanador;
    private Texto textoEquipo1, textoEquipo2, textoTiempo;

    public Partida(String nombrePj1, Personaje pj1,
                   String nombrePj2, Personaje pj2,
                   String nombrePj3, Personaje pj3,
                   String nombrePj4, Personaje pj4,
                   float duracionSegundos) {

        this.nombrePj1 = nombrePj1;
        this.nombrePj2 = nombrePj2;
        this.nombrePj3 = nombrePj3;
        this.nombrePj4 = nombrePj4;
        this.pj1 = pj1;
        this.pj2 = pj2;
        this.pj3 = pj3;
        this.pj4 = pj4;

        this.tiempoRestante = duracionSegundos;
        this.partidaFinalizada = false;
        this.killsEquipo1 = 0;
        this.killsEquipo2 = 0;

        textoEquipo1 = new Texto(Recursos.FUENTEMENU, 20, Color.BLUE, true);
        textoEquipo2 = new Texto(Recursos.FUENTEMENU, 20, Color.RED, true);
        textoTiempo   = new Texto(Recursos.FUENTEMENU, 20, Color.YELLOW, true);
    }

    public void actualizar(float delta) {
        if (partidaFinalizada) return;

        tiempoRestante -= delta;

        // ---- DETECCIÓN DE MUERTES ----
        // Si muere alguien del equipo 1 → suma kill equipo 2
        if (pj1.estaMuerto() && !pj1YaContado) {
            killsEquipo2++;
            pj1YaContado = true;
            pj1.respawn(4f, 2f);
        }
        if (pj2.estaMuerto() && !pj2YaContado) {
            killsEquipo2++;
            pj2YaContado = true;
            pj2.respawn(6f, 3f);
        }

        // Si muere alguien del equipo 2 → suma kill equipo 1
        if (pj3.estaMuerto() && !pj3YaContado) {
            killsEquipo1++;
            pj3YaContado = true;
            pj3.respawn(18f, 8f);
        }
        if (pj4.estaMuerto() && !pj4YaContado) {
            killsEquipo1++;
            pj4YaContado = true;
            pj4.respawn(20f, 10f);
        }

        // Resetear los flags cuando reviven
        if (!pj1.estaMuerto()) pj1YaContado = false;
        if (!pj2.estaMuerto()) pj2YaContado = false;
        if (!pj3.estaMuerto()) pj3YaContado = false;
        if (!pj4.estaMuerto()) pj4YaContado = false;

        // ---- FINAL DE PARTIDA ----
        if (tiempoRestante <= 0) {
            finalizarPartida();
        }
    }

    private void finalizarPartida() {
        partidaFinalizada = true;

        String ganador;
        if (killsEquipo1 > killsEquipo2) ganador = "Equipo Azul";
        else if (killsEquipo2 > killsEquipo1) ganador = "Equipo Rojo";
        else ganador = "EMPATE";

        textoGanador = new Texto(Recursos.FUENTEMENU, 50, Color.WHITE, true);
        textoGanador.setTexto("Ganador: " + ganador);
        textoGanador.centrar();
    }

    public void dibujarHUD() {
        // Mostrar puntaje de cada equipo
        textoEquipo1.setTexto("Equipo Azul: " + killsEquipo1);
        textoEquipo1.setPosition(20, Config.HEIGTH - 150);
        textoEquipo1.dibujar();

        textoEquipo2.setTexto("Equipo Rojo: " + killsEquipo2);
        textoEquipo2.setPosition(Config.WIDTH - 250, Config.HEIGTH - 150);
        textoEquipo2.dibujar();

        // Tiempo centrado arriba
        int min = (int) (tiempoRestante / 60);
        int sec = (int) (tiempoRestante % 60);
        textoTiempo.setTexto(String.format("%02d:%02d", min, sec));
        textoTiempo.setPosition(Config.WIDTH / 2f - textoTiempo.getAncho() / 2f, Config.HEIGTH - 20);
        textoTiempo.dibujar();

        if (partidaFinalizada && textoGanador != null) {
            textoGanador.dibujar();
        }
    }

    // Getters
    public boolean isPartidaFinalizada() { return partidaFinalizada; }
    public Texto getTextoGanador() { return textoGanador; }
    public float getTiempoRestante() { return tiempoRestante; }

    public int getKillsEquipo1() { return killsEquipo1; }
    public int getKillsEquipo2() { return killsEquipo2; }
}
