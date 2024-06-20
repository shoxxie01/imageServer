package client;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Client{

    private Socket socket;

    public Client(Socket socket) {
        this.socket = socket;
    }

    public void sendData(String name, String filepath){

        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            BufferedReader bufferedReader = new BufferedReader(new FileReader(filepath));

            bufferedWriter.write(name);
            bufferedWriter.newLine();
            bufferedWriter.flush();

            String line;
            while ((line = bufferedReader.readLine()) != null){
                bufferedWriter.write(line);
                bufferedWriter.newLine();
                bufferedWriter.flush();
                Thread.sleep(2000);
            }
            bufferedWriter.write("BYE");
            bufferedWriter.newLine();
            bufferedWriter.flush();

        }catch (IOException | InterruptedException e){
            e.printStackTrace();
        }
    }


    public static void main(String[] args) throws IOException {

        Socket socket = new Socket("localhost", 1234);

        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your username: ");
        String username = scanner.nextLine();

        System.out.println("Enter file address: ");
        String path = scanner.nextLine();

        Client client = new Client(socket);
        client.sendData(username, path);
        
    }
}