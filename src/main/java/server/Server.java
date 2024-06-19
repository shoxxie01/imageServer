package server;

import javax.imageio.spi.IIOServiceProvider;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private ServerSocket serverSocket;
    private BufferedWriter bufferedWriter;
    private BufferedReader bufferedReader;
    private Socket socket;

    public Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;

        try {
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void start(){
        while(true) {
            try {
                socket = serverSocket.accept();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}
