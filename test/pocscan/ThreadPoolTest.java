package pocscan;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;  
  
/** 
 * Created on 2011-12-28 
 * <p>Description: [Java 线程池学习]</p> 
 * @author         shixing_11@sina.com 
 */  
public class ThreadPoolTest implements Runnable {   
     public void run() {   
          synchronized(this) {   
            try{  
                System.out.println(Thread.currentThread().getName());  
                Thread.sleep(3000);  
            }catch (InterruptedException e){  
                e.printStackTrace();  
            }  
          }   
     }   
       
     public static void main(String[] args) {   
         BlockingQueue<Runnable> queue = new ArrayBlockingQueue<>(8);  
         ThreadPoolExecutor executor = new ThreadPoolExecutor(2, 6, 1, TimeUnit.DAYS, queue);  
         for (int i = 0; i < 100; i++) {     
             executor.execute(new Thread(new ThreadPoolTest(), "TestThread".concat(""+i)));     
             int threadSize = queue.size();  
             System.out.println("线程队列大小为-->"+threadSize);  
         }     
         executor.shutdown();    
     }  
}   