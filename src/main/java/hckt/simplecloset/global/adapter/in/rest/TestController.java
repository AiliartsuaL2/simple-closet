package hckt.simplecloset.global.adapter.in.rest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
public class TestController {
    @GetMapping("/member/login")
    public String loginPage(){
        return "member/login.html";
    }
}
