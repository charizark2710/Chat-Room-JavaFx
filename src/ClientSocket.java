import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import javafx.concurrent.Task;


class Send implements Runnable {

	Socket s;
	public Send(Socket s) {
		this.s = s;
	}
	static String msg;
	@Override
	public void run() {
		DataOutputStream dos = null;	
		try{
				dos = new DataOutputStream(s.getOutputStream());
				dos.writeUTF(msg);
			} catch (Exception e) {
				try {
					dos.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				e.printStackTrace();
			}
		}
	
}
class Receive extends Task<String>{
	String receiveMsg;
	Socket s;
	public Receive(Socket s) {
		this.s = s;
	}
	@Override
	protected String call() throws Exception {
		{
			DataInputStream dis = null;
			while(true) {
				try{
							dis = new DataInputStream(s.getInputStream());
							receiveMsg = dis.readUTF();
							System.out.println("From Server: "+ receiveMsg);
							ChatBox.display.appendText("Others: "+ receiveMsg + "\n");
				} catch (Exception e) {
					dis.close();
					e.printStackTrace();
					return null;
				}
			}
		}		
	}
	
	
}