package winterwolves.network;

import java.net.InetAddress;

public class Client {
    private final int id;
    private final InetAddress ip;
    private final int port;

    public Client(int id, InetAddress ip, int port) {
        this.id = id;
        this.ip = ip;
        this.port = port;
    }

    public int getId() {
        return id;
    }

    public InetAddress getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }
}
