import java.io.*;
import java.net.Socket;

public class Main {
    public static void main(String[] args) {
        Socket socket;
        DataInputStream in = null;
        BufferedReader in2;
        DataOutputStream out;

        try {
            socket = new Socket("192.168.100.107", 4444);

            in = new DataInputStream(socket.getInputStream());
            in2 = new BufferedReader(new InputStreamReader(System.in));
            out = new DataOutputStream(socket.getOutputStream());

            System.out.println("[Client] 서버 접속 성공");

            // 닉네임 설정
            // 서버 알림
            System.out.print(in.readUTF());
            // 클라 입력
            String userName = in2.readLine();
            out.writeUTF(userName);
            System.out.println(in.readUTF());

            // 방 리스트 출력
            System.out.println(in.readUTF());

            // 커맨드
            // 방이 존재하지 않는 경우
            if (!in.readBoolean()) {
                System.out.print(in.readUTF());

                while (true) {
                    String command = in2.readLine();

                    if (!command.equals("y") && !command.equals("n")) {
                        System.out.println("[Client] 잘못된 커맨드입니다. 다시 입력해주세요.");
                    } else {
                        out.writeUTF(command);
                        System.out.print(in.readUTF());

                        out.writeUTF(in2.readLine());
                        System.out.println(in.readUTF());
                        break;
                    }
                }
            } else /* 방이 존재하는 경우 */ {
                System.out.print(in.readUTF());

                while (true) {
                    out.writeUTF(in2.readLine());

                    // 방 생성 커맨드를 입력한 경우
                    if (in.readUTF().equals("create")) {
                        System.out.print(in.readUTF());

                        out.writeUTF(in2.readLine());
                        System.out.println(in.readUTF());
                        break;
                    } else /* 방에 참가하는 경우 */ {
                        break;
                        // 잘못된 커맨드 처리 필요
                    }
                }
            }

            // 클라이언트 쓰레드 시작
            Thread th = new Thread(new Send(out));
            th.start();
        } catch (IOException e) {
        }

        try {
            while (true) {
                String roomMessage = in.readUTF();
                System.out.println(roomMessage);
            }
        } catch (IOException e) {
        }
    }
}