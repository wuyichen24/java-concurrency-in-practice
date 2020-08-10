package personal.wuyi.demo;

public class CreateThread2 implements Runnable{  
	public void run(){  
		System.out.println("thread is running...");  
	}   
		  
	public static void main(String args[]){  
		CreateThread2 obj = new CreateThread2();  
		Thread trd =new Thread(obj);  
		trd.start();  
	}  
}
