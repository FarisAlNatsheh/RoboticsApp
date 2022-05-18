import com.fazecast.jSerialComm.*;
import java.util.*;
import java.io.*;

public class PortRead {

	public static void main(String args[]) {

		SerialPort userPort = SerialPort.getCommPorts()[3];

		//Initializing port
		userPort.openPort();
		if (userPort.isOpen()) {
			System.out.println("Port initialized");
			userPort.setBaudRate(230400);

			//timeout not needed for event based reading
			//userPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 100, 0);
		} else {
			System.out.println("Port not available");
			return;
		}

		userPort.addDataListener(new SerialPortDataListener() {
			@Override
			public int getListeningEvents() {
				return SerialPort.LISTENING_EVENT_DATA_AVAILABLE;
			}
			public void serialEvent(SerialPortEvent event) {
				if (event.getEventType() != SerialPort.LISTENING_EVENT_DATA_AVAILABLE)
					return;
				byte[] newData = new byte[userPort.bytesAvailable()];
				userPort.readBytes(newData, newData.length);
				String message = "";
				for(int i=0; i < newData.length; i++) {

					char character = (char)(newData[i]);

				//System.out.print(character);
					message+=character;
				}
				System.out.println(message);
			}
		});
	}
}