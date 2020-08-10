package personal.wuyi.demo;

public class MultiThread {
	 private static class Thread1 implements Runnable {
		 public void run() {
			 while (true) {
				 System.out.println("Thread1 is running.");
				 try {
					Thread.sleep(1000);
				 } catch (InterruptedException e) {
					e.printStackTrace();
				 }
			 }
		 }
	 }
	 
	 private static class Thread2 implements Runnable {
		 public void run() {
			 while (true) {
				 System.out.println("Thread2 is running.");
				 try {
					Thread.sleep(1000);
				 } catch (InterruptedException e) {
					e.printStackTrace();
				 }
			 }
		 }
	 }
	 
	 private static class Thread3 implements Runnable {
		 public void run() {
			 while (true) {
				 System.out.println("Thread3 is running.");
				 try {
					Thread.sleep(1000);
				 } catch (InterruptedException e) {
					e.printStackTrace();
				 }
			 }
		 }
	 }
	 
	 public static void main(String args[]) throws InterruptedException {
		 Thread t1 = new Thread(new Thread1());
		 Thread t2 = new Thread(new Thread2());
		 Thread t3 = new Thread(new Thread3());
		 
		 t1.start();
		 t2.start();
		 Thread.sleep(5000);
	 }

}
