package client;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

public class ClientHandler implements Runnable{

    private Socket socket;
    private BufferedReader bufferedReader;
    private String clientUsername;
    private List<List<Float>> data = new ArrayList<>();

    public ClientHandler(Socket socket) {

        try {
            this.socket = socket;
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.clientUsername = bufferedReader.readLine();

        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        String message;
        int index = 0;

        try{
            while((message = bufferedReader.readLine()) != null){

                if(message.equalsIgnoreCase("BYE")){
                    System.out.println("Bye");
                    break;
                }

                parseMessage(message);
                BufferedImage image = generateImage(index);
                String base64Code = encodeBase64(image);
                saveToDataBase(index, base64Code);
                index++;
            }
        }catch (IOException e){
            throw new RuntimeException();
        }
    }

    private void saveToDataBase(int electrode, String encoded64){
        String insertSQL = "INSERT INTO user_eeg(id, username, electrode_number, image) VALUES (?, ?, ?, ?)";
        String URL = "jdbc:sqlite:C:\\Users\\User\\Desktop\\programowanie\\Java_projects\\imageServer\\usereeg.db";

        try{
            Connection conn = DriverManager.getConnection(URL);
            PreparedStatement preparedStatement = conn.prepareStatement(insertSQL);
            preparedStatement.setInt(1, electrode+1);
            preparedStatement.setString(2, clientUsername);
            preparedStatement.setInt(3, electrode);
            preparedStatement.setString(4, encoded64);
            preparedStatement.executeUpdate();
            System.out.println("Data saved!");
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public BufferedImage generateImage(int index){
        List<Float> dataLine = data.get(index);
        BufferedImage image = new BufferedImage(dataLine.size(), 140, BufferedImage.TYPE_INT_ARGB);

        for(int i = 0; i < dataLine.size(); i++){
            int y0 = image.getHeight() / 2;
            int y = (int)(-dataLine.get(i) + y0);
            image.setRGB(i, y, 0xffff0000);
        }
        return image;
    }



    private static String encodeBase64(BufferedImage image) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(image, "png", outputStream);
        String base64Image = Base64.getEncoder().encodeToString(outputStream.toByteArray());
        return base64Image;
    }


    private void parseMessage(String message){
        List<Float> lineData = Arrays.stream(message.split(",")).map(Float::parseFloat).toList();
        data.add(lineData);
    }

}
