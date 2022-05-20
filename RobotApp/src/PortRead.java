import com.fazecast.jSerialComm.*;
import java.util.*;
import java.io.*;

public class PortRead implements Runnable{
	ArrayList<String> messages = new ArrayList<String>();
	String lastMessage;
	volatile double angle, distance;
	volatile int quality;
	public static void main(String args[]) {
		new PortRead().run();
		//new PortRead().processString("angle:24.41324 distance:234.00 quality:15 ");

	}
	public void processString(String s) {
		String angle = "",distance = "",quality= "";
		int flag = 0;
		for(int i = 0; i < s.length();i++) {

			//System.out.print(s.charAt(i));
			if(s.charAt(i) ==  ':') {
				flag++;
				continue;
			}
			if(flag == 1) {
				if(s.charAt(i) <= '9' && s.charAt(i) >= '0' || s.charAt(i) == '.') {
					angle+= s.charAt(i);
				}
			}
			if(flag == 2) {
				if(s.charAt(i) <= '9' && s.charAt(i) >= '0' || s.charAt(i) == '.') {
					distance+= s.charAt(i);
				}
			}
			if(flag == 3) {
				if(s.charAt(i) <= '9' && s.charAt(i) >= '0') {
					quality+= s.charAt(i);
				}
			}
		}
		try {
			this.angle = Double.parseDouble(angle);
			this.distance = Double.parseDouble(distance);
			this.quality = Integer.parseInt(quality);
			if(this.distance > 0 && this.distance < 500 && this.angle < 360) {
				//System.out.println(this.angle + " "+ this.distance + " "+ this.quality);
				//System.out.println(s);
				SensorWindow.lines.add(new Line(this.angle, this.quality));
			}
		}
		catch(Exception e) {
			//System.out.println("bad point");
		}
	}
	public void run() {
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
					if(character == '\n' && message.length() > 3) {
						messages.add(message);
						//System.out.println(lastMessage);
						processString(message);
						message = "";
						continue;
					}
					message+=character;
					lastMessage += character;
				}

				if(message.length() > 3) {
					messages.add(message);
					processString(message);
					//System.out.println(lastMessage);
				}

				if(messages.size() > 30) {
					for(int i = 0; i < messages.size(); i++)
						messages.get(i).replace('\n', '\u0000').replace('\r', '\u0000');
					messages.remove(0);
					//System.out.println(Arrays.toString(messages.toArray()));
					//System.exit(1);
				}
			}
		});

	}
}