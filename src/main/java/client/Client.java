package client;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;

public class Client {

    private Socket socket;
    private BufferedWriter bufferedWriter;
    private String username;
    private List<String> dataLines;

    public Client(Socket socket) {
        this.socket = socket;
        Path url;

        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your username: ");
        this.username = scanner.nextLine();
        System.out.println("Enter url address: ");
        url = Path.of(scanner.nextLine());

        try {
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            bufferedWriter.write(username);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private void dataToString(Path path){
        try (Stream<String> fileLines = Files.lines(path)){
            dataLines = fileLines.toList();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 1234);
        Client client = new Client(socket);
    }
}
