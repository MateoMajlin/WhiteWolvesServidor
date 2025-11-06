package winterwolves.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;

public class ServerThread extends Thread {

    private DatagramSocket socket;
    private int serverPort = 5555;
    private boolean end = false;
    private final int MAX_CLIENTS = 2;
    private int connectedClients = 0;
    private ArrayList<Client> clients = new ArrayList<>();
    private ArrayList<Integer> personajesElegidos = new ArrayList<>();
    private GameController gameController;

    public ServerThread(GameController gameController) {
        this.gameController = gameController;
        try {
            socket = new DatagramSocket(serverPort);
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        do {
            DatagramPacket packet = new DatagramPacket(new byte[1024], 1024);
            try {
                socket.receive(packet);
                processMessage(packet);
            } catch (IOException e) {
//                throw new RuntimeException(e);
            }
        } while (!end);
    }

    private void processMessage(DatagramPacket packet) {
        String message = (new String(packet.getData())).trim();
        String[] parts = message.split(":");
        int index = findClientIndex(packet);
        System.out.println("Mensaje recibido " + message);

        if (parts[0].equals("Connect")) {
            int personajeIdx = 0;
            if (parts.length > 1) {
                try {
                    personajeIdx = Integer.parseInt(parts[1]); // el índice del personaje
                } catch (NumberFormatException e) {
                    System.out.println("Índice de personaje inválido");
                }
            }

            if (index != -1) {
                System.out.println("Client already connected");
                this.sendMessage("AlreadyConnected", packet.getAddress(), packet.getPort());
                return;
            }

            if (connectedClients < MAX_CLIENTS) {
                connectedClients++;
                Client newClient = new Client(connectedClients, packet.getAddress(), packet.getPort());
                clients.add(newClient);
                personajesElegidos.add(personajeIdx);

                System.out.println("Cliente conectado #" + connectedClients + " con personaje " + personajeIdx);
                sendMessage("Connected:" + connectedClients, packet.getAddress(), packet.getPort());

                if (connectedClients == MAX_CLIENTS) {
                    StringBuilder data = new StringBuilder();
                    for (int i = 0; i < personajesElegidos.size(); i++) {
                        data.append(personajesElegidos.get(i));
                        if (i < personajesElegidos.size() - 1)
                            data.append(",");
                    }

                    String mensajeStart = "Start:" + data;
                    for (Client client : clients) {
                        sendMessage(mensajeStart, client.getIp(), client.getPort());
                    }

                    if (gameController != null) {
                        gameController.startGame();
                    }
                }
            } else {
                sendMessage("Full", packet.getAddress(), packet.getPort());
            }

        } else if (index == -1) {
            System.out.println("Client not connected");
            this.sendMessage("NotConnected", packet.getAddress(), packet.getPort());
            return;
        } else {
            Client client = clients.get(index);
            switch (parts[0]) {
                case "MOVE":
                    System.out.println(message);
                    sendMessageToAll(message);
                    break;
            }
        }
    }

    private int findClientIndex(DatagramPacket packet) {
        int i = 0;
        int clientIndex = -1;
        while(i < clients.size() && clientIndex == -1) {
            Client client = clients.get(i);
            String id = packet.getAddress().toString()+":"+packet.getPort();
            if(id.equals(client.getId())){
                clientIndex = i;
            }
            i++;

        }
        return clientIndex;
    }

    public void sendMessage(String message, InetAddress clientIp, int clientPort) {
        byte[] byteMessage = message.getBytes();
        DatagramPacket packet = new DatagramPacket(byteMessage, byteMessage.length, clientIp, clientPort);
        try {
            socket.send(packet);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void terminate(){
        this.end = true;
        socket.close();
        this.interrupt();
    }

    public void sendMessageToAll(String message) {
        for (Client client : clients) {
            sendMessage(message, client.getIp(), client.getPort());
        }
    }

    public void disconnectClients() {
        for (Client client : clients) {
            sendMessage("Disconnect", client.getIp(), client.getPort());
        }
        this.clients.clear();
        this.connectedClients = 0;
    }

    public int getMaxClients() {
        return MAX_CLIENTS;
    }

    public Client getClientePorId(int i) {
        return clients.get(i);
    }
}
