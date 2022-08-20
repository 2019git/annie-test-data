package com.example.annie;

import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * @author wzj
 * @date 2022/8/17 21:58
 */
@RestController
@RequestMapping("/data")
public class TestDataController {

    @Autowired
    private TestData testData;

    @PostMapping("/getTestData")
    public JSONObject getTestData(HttpServletRequest request){
        String headerPath = request.getHeader("path");
        JSONObject data = testData.getData(headerPath);
        return data;
    }

    /**
     * 参考网页：https://www.codercto.com/a/68006.html
     * @author wzj
     * @date 2022/8/20 15:19
     */
    @PostMapping("/test")
    public Object test(@RequestBody User user){
        if (user.getUserId() == null){
            user = null;
        }
        //对应业务-----查询数据库是否有数据
        Mono<User> just = Mono.justOrEmpty(user);
        just
          .flatMap(o -> Mono.just(Optional.ofNullable(o)))
          .defaultIfEmpty(Optional.empty())
          .flatMap(o -> {
             //对应业务----数据库无数据----执行新增
             if (!o.isPresent()){
                 System.out.println("user对象为空！");
                 return Mono.empty();
             }
             User optionalUser = o.get();
             //对应业务----数据库有数据----执行修改
             if (optionalUser.getUserName() == null){
                 System.out.println("user对象的UserName为空！");
             }else {
                 System.out.println("user对象的UserId不为空！");
             }
             return Mono.empty();
          }).subscribe();
        return just;
    }
}
