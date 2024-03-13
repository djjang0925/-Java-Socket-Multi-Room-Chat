import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;

public class Room {
    HashMap<String, DataOutputStream> roomInfo = new HashMap<>();

    public synchronized void joinUser(String userName, Socket socket) {
        try {
            DataOutputStream userOutStream = new DataOutputStream(socket.getOutputStream());
            roomInfo.put(userName, userOutStream);
            for (DataOutputStream out : roomInfo.values()) {
                out.writeUTF( "[Server] " + userName + "님이 방에 참가하셨습니다.");
            }
        } catch (IOException e) {}
    }

    public synchronized void exitUser(String userName) {

    }

    public synchronized void sendMessage(String userName, String message) throws Exception {
        Iterator iterator = roomInfo.keySet().iterator();

        while(iterator.hasNext()) {
            String clientName = (String) iterator.next();
            roomInfo.get(clientName).writeUTF(userName + ": " + message);
        }
    }
}
