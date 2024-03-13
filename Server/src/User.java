import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;

public class User {
    HashMap<String, DataOutputStream> clients = new HashMap<String, DataOutputStream>();
    DataOutputStream out;

    public synchronized void addClient(String userName, Socket socket) {
        try {
            out = new DataOutputStream(socket.getOutputStream());

            clients.put(userName, out);
            System.out.println(clients);
            System.out.println("서버 접속 인원: " + clients.size());
            out.writeUTF("[Server] " + userName + "님 환영합니다.");
        } catch (IOException e){}
    }

    public synchronized void removeClient(String userName) {
        clients.remove(userName);
        System.out.println("서버 접속 인원: " + clients.size());
    }
}
