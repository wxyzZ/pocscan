package pocscan;

import scan.ScanTask;

public class Test {


	public static void main(String args[]){
		new ScanTask("test", "127.0.0.1/24,61.57.227.226-61.57.228.226", 80).start();
	}

}
