package personal.wuyi.demo;

public class CreateThread1 extends Thread {
	public void run(){  
		System.out.println("thread is running...");  
	} 
		   
	public static void main(String[] args) {
		CreateThread1 trd = new CreateThread1();
		trd.start();
	}
}