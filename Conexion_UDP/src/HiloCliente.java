import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class HiloCliente extends Thread {
    private Usuario usuario = new Usuario(25565);
    private DatagramSocket socket;
    private int miId = -1;

    public HiloCliente(String direccionServidor) {
        try {
            socket = new DatagramSocket();
            usuario.setAddress(InetAddress.getByName(direccionServidor));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void conectar() {
        enviarMensaje("-1$hola");
    }

    @Override
    public void run() {
        byte[] buffer = new byte[1024];
        while (true) {
            try {
                DatagramPacket dp = new DatagramPacket(buffer, buffer.length);
                socket.receive(dp);
                procesarDatagrama(dp);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void procesarDatagrama(DatagramPacket dp) {
        String msg = new String(dp.getData(), 0, dp.getLength()).trim();
        String[] partes = msg.split("\\$");

        if (partes[0].equals("Buenas")) {
            this.miId = Integer.parseInt(partes[1]);
            usuario.setAddress(dp.getAddress());
            System.out.println("Conectado. Asignado Usuario ID: " + miId);
        } else if (partes[0].equals("Mensaje")) {
            System.out.println("Usuario " + partes[1] + " dice: " + partes[2]);
        }
    }
    public void enviarChat(String texto) {
        if (miId == -1) {
            System.out.println("Todavia no te conectastse, espera...");
            return;
        }
        enviarMensaje(miId + "$Chat$" + texto);
    }
    public void enviarMensaje(String data) {
        byte[] msg = data.getBytes();
        DatagramPacket dp = new DatagramPacket(msg, msg.length, usuario.getAddress(), usuario.getPort());
        try {
            socket.send(dp);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public int getMiId() {
        return miId;
    }
}