import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class ClientHandler implements Runnable {
    Socket socket;
    DataInputStream in;
    DataOutputStream out;
    String userName;
    User user;
    RoomManager roomManager;
    Room room = null;

    public ClientHandler(User user, Socket socket, RoomManager roomManager) {
        this.socket = socket;
        this.user = user;
        this.roomManager = roomManager;
    }

    @Override
    public void run() {
        try {
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());

            out.writeUTF("[Server] 닉네임을 입력하세요: ");
            this.userName = in.readUTF();

            // 유저 정보 추가
            user.addClient(userName, socket);

            // 방 목록 전송 및 생성/참가
            roomManager.roomList(userName, socket);

            // 방 정보 등록
            room = roomManager.roomInfo();

            // 메시지 전송
            while (true) {
                String message = in.readUTF();
                if (room != null) {
                    room.sendMessage(userName, ": " + message);
                }
            }
        } catch (Exception e) {
            user.removeClient(userName);
        }
    }
}
