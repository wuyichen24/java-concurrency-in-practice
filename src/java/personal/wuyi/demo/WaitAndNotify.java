package personal.wuyi.demo;

public class WaitAndNotify {
    public static void main(String[] args) throws InterruptedException{
        ThreadX b = new ThreadX();
        b.start();
 
        synchronized(b){
        	System.out.println("Waiting for b to complete...");
        	b.wait();
            System.out.println("Execution back to main Thread");
        }
    }
}
 
class ThreadX extends Thread{
    int total;
    @Override
    public void run(){
        synchronized(this){
        	try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
            notify();
        }
    }
}
