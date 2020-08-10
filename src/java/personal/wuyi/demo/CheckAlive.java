package personal.wuyi.demo;

public class CheckAlive implements Runnable{  
	public void run(){  
		System.out.println("My thread is in running state.");  
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}   
		  
	public static void main(String args[]) throws InterruptedException{  
		CheckAlive obj = new CheckAlive();  
		Thread     trd = new Thread(obj);  
		trd.start();  
		
		while (true) {
			if (trd.isAlive()) {
				System.out.println("Alive");
			} else {
				System.out.println("Dead");
			}
			Thread.sleep(500);
		}
	}  
}

