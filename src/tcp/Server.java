package tcp;

import java.net.*;
import java.io.*;
import java.util.Scanner;

/**
 *
 * @author Christian
 */
public class Server {

    private Socket socket;
    private ServerSocket serverSocket;
    private DataInputStream in = null;
    private DataOutputStream out = null;
    Scanner scan = new Scanner(System.in);

    final static int PORT = 6666;

    public void initConnection() {
        try {
            serverSocket = new ServerSocket(PORT);
            printText("Waiting for client...");
            socket = serverSocket.accept();
            printText("\nClient is connected");
        } catch (Exception e) {
            printText(e.getMessage());
            System.exit(0);
        }
    }

    public void flow() {
        try {
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            out.flush();
        } catch (IOException e) {
            printText(e.getMessage());
        }
    }

    public void receiveData() {
        String st;
        try {
            do {
                st = (String) in.readUTF();
                printText("\n[Client] : " + st);
            } while (true);
        } catch (IOException e) {
        }
    }

    public void send(String s) {
        try {
            out.writeUTF(s);
            out.flush();
        } catch (IOException e) {
            printText(e.getMessage());
        }
    }

    public static void printText(String s) {
        System.out.print(s);
    }

    public void writeMessage() {
        while (true) {
            System.out.print("[Server] : ");
            send(scan.nextLine());
        }
    }

    public void executeConnection() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        initConnection();
                        flow();
                        receiveData();
                    } finally {
                        System.exit(0);
                    }
                }
            }
        });
        thread.start();
    }

    public static void main(String[] args) throws IOException {
        Server s = new Server();
        s.executeConnection();
        s.writeMessage();
    }
}
