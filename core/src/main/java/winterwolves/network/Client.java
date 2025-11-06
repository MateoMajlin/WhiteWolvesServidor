    package winterwolves.network;

    import winterwolves.Jugador;

    import java.net.InetAddress;

    public class Client {

        private String id;
        private int num;
        private InetAddress ip;
        private int port;
        private Jugador jugador;

        public Client(int num, InetAddress ip, int port) {
            this.num = num;
            this.id = ip.toString() + ":" + port;
            this.ip = ip;
            this.port = port;
        }

        public String getId() {
            return id;
        }

        public InetAddress getIp() {
            return ip;
        }

        public int getPort() {
            return port;
        }

        public int getNum() {
            return num;
        }

        public void setJugador(Jugador jugador) {
            this.jugador = jugador;
        }

        public Jugador getJugador() {
            return jugador;
        }
    }
