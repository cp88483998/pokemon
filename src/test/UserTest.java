package test;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import org.bson.Document;
import org.junit.Test;

import com.mongodb.client.MongoCollection;

import pocket.chat.dao.MongoAddMsgDao;
import pocket.chat.dao.MongoAddMsgDaoImpl;
import pocket.chat.service.PublicService;
import pocket.total.util.MongoDBUtil;

public class UserTest {
	MongoAddMsgDao dao = MongoAddMsgDaoImpl.getInstance();
	@Test
	public void findTest(){
		
		Document document = dao.findOneUserInfo(14883590927084l, 1);
		List<Object> arr = (List<Object>) document.get("handbook");
		
		System.out.println(arr.size());
		
	}
	@Test
	public void changeTest(){
		String msg = "0|F_G|test182,王路飞,70,15,2,0,2,2,7/7 03:32:27,63,14884231051702,1000251:3:70:;2000033:2:70:girl;1000282:2:70:girl;1000315:2:68:girl;2000032:2:70:girl;1000154:1:70:boy";
		String result = PublicService.findUserInfo(1, 14884231051702l, msg);
		System.out.println(result);
	}
	@Test
	public void taskTest(){
		
        Callable<Integer> myCallable = new MyCallable();    // 创建MyCallable对象
        FutureTask<Integer> ft = new FutureTask<Integer>(myCallable); //使用FutureTask来包装MyCallable对象

        for (int i = 0; i < 100; i++) {
            System.out.println(Thread.currentThread().getName() + " " + i);
            if (i == 30) {
                Thread thread = new Thread(ft);   //FutureTask对象作为Thread对象的target创建新的线程
                thread.start();                      //线程进入到就绪状态
            }
        }
        
        System.out.println("主线程for循环执行完毕..");
        
        try {
            int sum = ft.get();            //取得新创建的新线程中的call()方法返回的结果
            System.out.println("sum = " + sum);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

	}
	class MyCallable implements Callable<Integer> {
	    private int i = 0;

	    // 与run()方法不同的是，call()方法具有返回值
	    @Override
	    public Integer call() {
	        int sum = 0;
	        for (; i < 100; i++) {
//	            System.out.println(Thread.currentThread().getName() + " " + i);
	            sum += i;
	        }
	        return sum;
	    }

	}
	@Test
	public void threadPoolTest(){
		  ExecutorService pool = Executors.newFixedThreadPool(5);  
		  pool.execute(new Runnable() {
			
			@Override
			public void run() {
				System.out.println(Thread.currentThread().getName());
			}
		});
	}
}
