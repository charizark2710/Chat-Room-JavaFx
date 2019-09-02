import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class ChatBox extends Application {
	static Socket s;
	static TextArea display = new TextArea();

	public static void main(String[] args) throws UnknownHostException, IOException {
		s = new Socket("localhost", 8080);
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		BorderPane root = new BorderPane();
		root.setCenter(rootLayout());
		primaryStage.setScene(new Scene(root, 600, 400));
		primaryStage.show();
	}
	
	Parent rootLayout() {
		TextField input = new TextField();
		GridPane root = new GridPane();
		Label user = new Label();
		root.add(user, 0, 0);
		root.add(display, 0, 1);
		root.add(input, 0, 2);
		Button sendBtn = new Button("Send");
		display.setDisable(true);
		display.setPadding(new Insets(0,0,0,10));
		display.appendText(s.toString() + "\n");

		Thread t = new Thread(new Receive(s));
		t.start();
		sendBtn.setOnAction(e ->{
			Send.msg = null;
			Send.msg = input.getText();
			if(Send.msg.trim()!=null) {
				ExecutorService executor = Executors.newFixedThreadPool(1);
				executor.execute(new Send(s));
				executor.shutdown();
			display.appendText(Send.msg + "\n");
			input.clear();
			}
		});
		
		input.setOnKeyPressed(e ->{
			if(e.getCode().equals(KeyCode.ENTER)) {
				Send.msg = null;
				Send.msg = input.getText();
				if(Send.msg.trim()!=null) {
					ExecutorService executor = Executors.newFixedThreadPool(1);
					executor.execute(new Send(s));
					executor.shutdown();
				display.appendText(Send.msg + "\n");
				input.clear();
				}
			}
		});
		root.add(sendBtn, 1, 2);
		
		return root;
	}
	
}


