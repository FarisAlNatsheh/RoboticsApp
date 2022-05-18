import com.fazecast.jSerialComm.SerialPort;

public class Main {
	String portName = "COM11";
	SerialPort mainPort;
	public void findPort() {
		SerialPort[] ports = SerialPort.getCommPorts();

		for (SerialPort port: ports) {
			if(port.getSystemPortName().equals(portName)) {
				mainPort = port;
				break;
			}
		}
	}
	public static void main(String[] args) {
		Main runner = new Main();
		runner.findPort();

		System.out.println(runner.mainPort.readBytes(null, 0));
		//runner.mainPort.openPort(0, 0, 0)
		


	}



	// outputs:
	// COM5
	// COM9
	// ...

}
