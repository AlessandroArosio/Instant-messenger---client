/**
 * Created by Alessandro Arosio on 02/05/2017.
 */
import java.io.*;
import java.net.*;
import java.awt.*;
import javax.swing.*;

public class Client extends JFrame {

    private JTextField userText;
    private JTextArea chatWindow;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private String message = "";
    private String serverIP;
    private Socket connection;

    //constructor
    public Client(String host) {
        super("Client side");
        serverIP = host;
        userText = new JTextField();
        userText.setEditable(false);
        userText.addActionListener(event -> {
                    sendMessage(event.getActionCommand());
                    userText.setText("");
                }
        );
        add(userText, BorderLayout.NORTH);
        chatWindow = new JTextArea();
        add(new JScrollPane(chatWindow), BorderLayout.CENTER);
        setSize(300,150);
        setVisible(true);
    }

    //connect to server
    public void startRunning() {
        try{
            connectToServer();
            setupStreams();
            whileChatting();
        }catch(EOFException eofException){
            showMessage("\n client terminated connection");
        }catch(IOException ioException){
            ioException.printStackTrace();
        }finally {
            closeCrap();
        }
    }

    private void connectToServer() throws IOException {
        showMessage("Attempting connection... \n");
        connection = new Socket(InetAddress.getByName(serverIP),6789);
        showMessage("Connected to: " + connection.getInetAddress().getHostName());
    }

    //set up streams to send and receive messages
    private void setupStreams() throws IOException{
        output = new ObjectOutputStream(connection.getOutputStream());
        output.flush();
        input = new ObjectInputStream(connection.getInputStream());
        showMessage("\n Your streams are good to go! \n");
    }

    //while chatting with server
    private void whileChatting() throws IOException {
        ableToType(true);
        do{
            try {
                message = (String) input.readObject();
                showMessage("\n " + message);
            } catch (ClassNotFoundException classNotFoundException){
                showMessage("\n Unexpected object type");
            }
        }while (!message.equals("SERVER - END"));
    }

    //close the streams and sockets
    private void closeCrap() {
        showMessage("\n Closing everything down");
        ableToType(false);
        try {
            output.close();
            input.close();
            connection.close();

        } catch(IOException ioException){
            ioException.printStackTrace();
        }
    }

    //send message to the server
    private void sendMessage(String message){
        try{
            output.writeObject("CLIENT - " + message);
            output.flush();
            showMessage("\nCLIENT - " + message);
        } catch (IOException ioException){
            chatWindow.append(("\n something went  wrong"));
        }
    }

    // update the GUI
    private void showMessage(final String m){
        SwingUtilities.invokeLater(() -> chatWindow.append(m)); // previous code replaced by lambda expression
    }

    //gives user permission to type
    private void ableToType(final boolean tof){
        SwingUtilities.invokeLater(() -> userText.setEditable(tof)); // previous code replaced by lambda expression
    }
}
