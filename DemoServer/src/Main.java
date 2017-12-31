import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket=new ServerSocket(8080);
            ExecutorService executorService= Executors.newFixedThreadPool(10);

            while (true){
                Socket socket = serverSocket.accept();
                System.out.println("accept the request from "+socket.getRemoteSocketAddress());
                executorService.execute(new Processor(socket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
