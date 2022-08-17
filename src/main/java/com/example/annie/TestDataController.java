package com.example.annie;

import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

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
}
