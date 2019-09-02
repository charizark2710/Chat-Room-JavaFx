import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ServerMain {
	static ArrayList<Socket> clients = new ArrayList<Socket>();
	static ServerSocket ss;
	public static void main(String[] args) throws IOException {
		ss = new ServerSocket(8080);
		System.out.println("Server start: " + ss.getLocalPort());
		
		while(true) {
			Socket client = ss.accept();
			clients.add(client);
			System.out.println("New Client "+ client);
			Thread t = new Thread(new ClientListener(client));
			t.start();
		}
	}
	
}
class ClientListener implements Runnable {
	
	Socket s;
	
	public ClientListener(Socket s) {
		super();
		this.s = s;
	}

	@Override
	public void run() {
		while(true) {
		try {
			String msg = receive();

			if(msg!=null) {
				System.out.println(s + msg);
				Send(msg);
			}
			else {
			return;
			}
		}catch (Exception e) {
			e.printStackTrace();
			return;
		}
		}
	}
	
	String receive() throws IOException {
		DataInputStream dis = null;
		try{
			dis = new DataInputStream(s.getInputStream());
			String fromClient = dis.readUTF();
			return fromClient;
		}catch (Exception e) {
			dis.close();
			e.printStackTrace();
			return null;
		}
	}
	
	void Send(String msg) throws IOException {
		DataOutputStream dos = null;
		try{
		for(Socket client: ServerMain.clients) {
			if(s.getPort()!=client.getPort()) {
				System.out.println(client);
				dos = new DataOutputStream(client.getOutputStream());
				dos.writeUTF(msg);
			}
		}
		}catch (Exception e) {
			e.printStackTrace();
			dos.close();
		}
	}
	
}
