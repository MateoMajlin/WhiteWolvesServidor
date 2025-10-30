package winterwolves.network;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;

public class ServerThread extends Thread {

    private DatagramSocket socket;
    private final int serverPort = 5555;
    private boolean end = false;

    private final int MAX_CLIENTS = 4;
    private ArrayList<Client> clients = new ArrayList<>();
    private int connectedClients = 0;
    private int[] personajesElegidos = new int[MAX_CLIENTS]; // -1 = no elegido
    private int turnoSeleccion = 0; // qué jugador está eligiendo

    public ServerThread() {
        try {
            socket = new DatagramSocket(serverPort);
            System.out.println("Servidor iniciado en puerto " + serverPort);
        } catch (SocketException e) {
            throw new RuntimeException("Error al iniciar servidor: " + e.getMessage());
        }

        for (int i = 0; i < MAX_CLIENTS; i++) personajesElegidos[i] = -1;
    }

    @Override
    public void run() {
        while (!end) {
            DatagramPacket packet = new DatagramPacket(new byte[1024], 1024);
            try {
                socket.receive(packet);
                processMessage(packet);
            } catch (IOException ignored) {}
        }
    }

    private void processMessage(DatagramPacket packet) {
        String message = new String(packet.getData(), 0, packet.getLength()).trim();
        InetAddress ip = packet.getAddress();
        int port = packet.getPort();

        if (message.startsWith("Connect")) {
            if (connectedClients >= MAX_CLIENTS) {
                sendMessage("Full", ip, port);
                return;
            }

            connectedClients++;
            int id = connectedClients;
            clients.add(new Client(id, ip, port));
            sendMessage("Connected:" + id, ip, port);

            System.out.println("Jugador " + id + " conectado (" + connectedClients + "/" + MAX_CLIENTS + ")");

            // Si todos conectados, iniciar selección del jugador 1
            if (connectedClients == MAX_CLIENTS) {
                turnoSeleccion = 1;
                sendMessage("StartSelection:1", getClientById(turnoSeleccion).getIp(),
                    getClientById(turnoSeleccion).getPort());
            }

        } else if (message.startsWith("ChooseCharacter:")) {
            int id = getClientId(ip, port);
            if (id == -1) return;

            int personaje = Integer.parseInt(message.split(":")[1]);
            personajesElegidos[id - 1] = personaje;

            System.out.println("Jugador " + id + " eligió personaje " + personaje);

            // Pasar turno al siguiente jugador
            if (turnoSeleccion < MAX_CLIENTS) {
                turnoSeleccion++;
                sendMessage("StartSelection:" + turnoSeleccion,
                    getClientById(turnoSeleccion).getIp(),
                    getClientById(turnoSeleccion).getPort());
            }

            // Verificar si todos eligieron
            boolean todosElegidos = true;
            for (int p : personajesElegidos) {
                if (p == -1) {
                    todosElegidos = false;
                    break;
                }
            }

            if (todosElegidos) {
                String msg = "StartGame:" + personajesElegidos[0] + "," + personajesElegidos[1] + "," +
                    personajesElegidos[2] + "," + personajesElegidos[3];
                sendMessageToAll(msg);
                System.out.println("Todos eligieron. Partida iniciando.");
            }
        }
    }

    private Client getClientById(int id) {
        for (Client c : clients) {
            if (c.getId() == id) return c;
        }
        return null;
    }

    private int getClientId(InetAddress ip, int port) {
        for (Client c : clients) {
            if (c.getIp().equals(ip) && c.getPort() == port) return c.getId();
        }
        return -1;
    }

    public void sendMessage(String message, InetAddress ip, int port) {
        try {
            byte[] data = message.getBytes();
            DatagramPacket packet = new DatagramPacket(data, data.length, ip, port);
            socket.send(packet);
        } catch (IOException e) {
            System.out.println("Error enviando mensaje: " + e.getMessage());
        }
    }

    public void sendMessageToAll(String message) {
        for (Client c : clients) sendMessage(message, c.getIp(), c.getPort());
    }

    public int getConnectedClients() {
        return connectedClients;
    }

    public void terminate() {
        end = true;
        socket.close();
        this.interrupt();
    }
}
