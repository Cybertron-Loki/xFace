package ming.test.xface.controller;

import ming.test.xface.enity.dto.Result;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/demo")
public class DemoController {

    @RequestMapping("/")
    public Result demo() {
        return Result.success("Hello,xFace!!");
    }

}
