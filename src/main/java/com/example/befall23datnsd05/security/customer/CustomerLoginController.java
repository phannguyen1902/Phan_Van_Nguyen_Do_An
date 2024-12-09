package com.example.befall23datnsd05.security.customer;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CustomerLoginController {

    @GetMapping("/haven/login")
    public String getFormLoginTrangChu(){
        return "customer-template/dangnhap";
    }

    @GetMapping("/haven/dang-ky")
    public String getFormDangKyTrangChu(){
        return "customer-template/dangky";
    }
}
