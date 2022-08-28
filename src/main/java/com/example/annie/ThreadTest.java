package com.example.annie;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.CoreSubscriber;
import reactor.core.publisher.Flux;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

/**
 * @author wzj
 * @date 2022/8/28 11:05
 */
@RestController
@RequestMapping("/thread/test")
public class ThreadTest {


    public Flux<User> getKey(){
        List<User> list = new ArrayList<>();
        for (int i = 0; i < 20000; i+=2) {
            User user = new User();
            user.setUserId(i+1 + "");
            user.setUserName("王志军" +i);
            list.add(user);
        }
        Flux<User> just = Flux.fromIterable(list);
        return just;
    }

    @GetMapping("/testV1")
    public void testV1() throws InterruptedException {
        List<User> list = new ArrayList<>();
        for (int i = 0; i < 20000; i++) {
            User user = new User();
            user.setUserId(i+1 + "");
            user.setUserName("王志军" +i);
            list.add(user);
        }

        List<String> collect = list.stream().map(User::getUserId).collect(Collectors.toList());
        CountDownLatch latch = new CountDownLatch(1);
        new Thread(() -> {
            Flux<User> flux = getKey();
            try {
                    System.out.println("sleep=======5秒！");
                    Thread.sleep(5 * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            flux.defaultIfEmpty(new User("default","default")).subscribe(item -> {
                System.out.println("开始处理业务数据");
                if (!"default".equals(item.getUserId()) && collect.contains(item.getUserId())){
                    collect.remove(item.getUserId());
                }
                System.out.println("子线程执行！");
                System.out.println("当前collect数量："+collect.size());
            });
            latch.countDown();//让latch中的数值减一
        }).start();
        //主线程
        latch.await();//阻塞当前线程直到latch中数值为零才执行
        System.out.println("主线程执行！");
        System.out.println("当前collect数量："+collect.size());
    }
}
