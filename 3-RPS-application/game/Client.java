package game;

import java.io.IOException;
import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;

public class Client extends UnicastRemoteObject implements ClientInterface {

	private String name;
	private UI ui;

	public Client(String n) throws RemoteException {
		name = n;
	}

	public void tell(String st) throws RemoteException {
		System.out.println(st);
		ui.writeMsg(st);
	}

	public void logout()  throws RemoteException {
		ui.doDisconnect();
	}

	public String getName() throws RemoteException {
		return name;
	}

	public void unmute() throws IOException {
		ui.tf.setEnabled(true);
	}

	public void setGUI(UI t) {
		ui = t;
	}
}