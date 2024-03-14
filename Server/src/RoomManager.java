import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RoomManager {
    HashMap<String, Room> rooms = new HashMap<String, Room>();
    List<String> roomsList;
    DataInputStream in;
    DataOutputStream out;
    String userName;
    Socket socket;
    Room roomInfo = null;

    public void roomList(String userName, Socket socket) {
        this.userName = userName;
        this.socket = socket;

        StringBuilder sb = new StringBuilder();

        try {
            out = new DataOutputStream(socket.getOutputStream());
            roomsList = new ArrayList<>(rooms.keySet());

            if (!roomsList.isEmpty()) {
                for (int i = 1; i < roomsList.size() + 1; i++) {
                    if (i != roomsList.size()) {
                        sb.append(i).append(". ").append(roomsList.get(i - 1)).append("\n");
                    } else {
                        sb.append(i).append(" .").append(roomsList.get(i - 1));
                    }
                }
                String res = sb.toString();
                out.writeUTF("[Server] 방목록" + "\n" + res);
            } else {
                out.writeUTF("[Server] 방이 존재하지 않습니다.");
            }

            roomCommand();
        } catch (IOException e) {
        }
    }

    public void roomCommand() {
        try {
            this.out = new DataOutputStream(socket.getOutputStream());
            this.in = new DataInputStream(socket.getInputStream());

            if (rooms.isEmpty()) {
                out.writeBoolean(false);
                out.writeUTF("[Server] 방을 생성하시겠습니까? [y/n]: ");

                String res = in.readUTF();

                if (res.equals("y")) {
                    roomCreate();
                } else {
                    out.writeUTF("[Server] n");
                }
            } else {
                out.writeBoolean(true);
                out.writeUTF("[Server] 방 생성(c) / 방 참가(방 번호): ");

                String res = in.readUTF();

                if (res.equals("c")) {
                    out.writeUTF("create");
                    roomCreate();
                } else {
                    out.writeUTF("join");

                    int index = Integer.parseInt(res) - 1;

                    if (index < 0 || index > roomsList.size()) {
                        out.writeBoolean(false);
                        out.writeUTF("[Server] 옳바른 방 번호를 입력해 주세요.");
                        index = Integer.parseInt(in.readUTF()) - 1;
                    }
//                    out.writeBoolean(true);
                    String roomName = roomsList.get(index);
                    Room room = rooms.get(roomName);
                    room.joinUser(userName, socket);
                }
            }
        } catch (IOException e) {
        }
    }

    private void roomCreate() {
        Room room = new Room();
        this.roomInfo = room;

        try {
            out.writeUTF("[Server] 방 제목을 입력하세요: ");
            String name = in.readUTF();

            rooms.put(name, room);
            System.out.println(name + "방 생성");
            out.writeUTF("[Server] " + name + "방이 성공적으로 생성되었습니다.");

            room.joinUser(userName, socket);
        } catch (Exception e) {
        }
    }

    public Room roomInfo() {
        return roomInfo;
    }
}
