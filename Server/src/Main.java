import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    public static void main(String[] args) {
        Socket socket = null;
        ServerSocket serverSocket = null;
        User user = new User();
        RoomManager roomManager = new RoomManager();

        try {
            serverSocket = new ServerSocket(4444);
            System.out.println("Server start");

            while(true){
                socket = serverSocket.accept();
                new Thread(new ClientHandler(user, socket, roomManager)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}