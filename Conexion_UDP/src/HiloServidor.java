import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;

public class HiloServidor extends Thread {
    private DatagramSocket socket;
    private ArrayList<Usuario> direccion = new ArrayList<>();

    public HiloServidor() {
        try {
            socket = new DatagramSocket(25565);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        byte[] msg = new byte[1024];
        while (true) {
            try {
                DatagramPacket dp = new DatagramPacket(msg, msg.length);
                socket.receive(dp);
                procesarDatagrama(dp);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void procesarDatagrama(DatagramPacket dp) {
        String contenido = new String(dp.getData(), 0, dp.getLength()).trim();
        String mensajeComplejo[] = contenido.split("\\$");

        if (mensajeComplejo.length < 2) {
            return;
        }

        if (mensajeComplejo[1].equals("hola")) {
            if (direccion.size() < 2) {
                Usuario u = new Usuario(dp.getAddress(), dp.getPort(), direccion.size());
                direccion.add(u);
                enviarMensaje("Buenas$" + u.getNUsuario(), u);
            }
            return;
        }

        if (mensajeComplejo[1].equals("Chat") && mensajeComplejo.length >= 3) {
            int remitente = Integer.parseInt(mensajeComplejo[0]);
            int destinatario = (remitente == 0) ? 1 : 0;
            if (direccion.size() > destinatario) {
                enviarMensaje("Mensaje$" + remitente + "$" + mensajeComplejo[2], direccion.get(destinatario));
            }
        }
    }
    public void enviarMensaje(String data, Usuario u) {
        byte[] msg = data.getBytes();
        DatagramPacket dp = new DatagramPacket(msg, msg.length, u.getAddress(), u.getPort());
        try {
            socket.send(dp);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}