/**
 * Created by Alessandro Arosio on 02/05/2017.
 */
import javax.swing.JFrame;

public class ClientTest {
    public static void main(String[] args){
        Client clientApp;
        clientApp = new Client("127.0.0.1");
        clientApp.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        clientApp.startRunning();
    }
}
