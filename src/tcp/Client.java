package tcp;

import java.net.*;
import java.io.*;
import java.util.Scanner;

/**
 *
 * @author Christian
 */
public class Client {

    private Socket socket;
    private DataInputStream in = null;
    private DataOutputStream out = null;
    Scanner scan = new Scanner(System.in);
    final static String IP = "localhost";
    final static int PORT = 6666;

    public void initConnection() {
        try {
            socket = new Socket(IP, PORT);
            printText("connection to server DONE!!!");
        } catch (Exception e) {
            printText(e.getMessage());
            System.exit(0);
        }
    }

    public static void printText(String s) {
        System.out.println(s);
    }

    public void abrirFlujos() {
        try {
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            out.flush();
        } catch (IOException e) {
            printText(e.getMessage());
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

    public void executeConnection() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    initConnection();
                    abrirFlujos();
                    receiveData();
                } finally {
                    System.exit(0);
                }
            }
        });
        thread.start();
    }

    public void receiveData() {
        String st = "";
        try {
            do {
                st = (String) in.readUTF();
                printText("\n[Server] : " + st);
            } while (true);
        } catch (IOException e) {
        }
    }

    public void writeMessage() {
        String entrada = "";

        while (true) {
            System.out.print("[You] : ");
            entrada = scan.nextLine();
            if (entrada.length() > 0) {
                send(entrada);
            }
        }
    }

    public static void main(String[] argumentos) {
        Client cliente = new Client();

        cliente.executeConnection();
        cliente.writeMessage();
    }
}
