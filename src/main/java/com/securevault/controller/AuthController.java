package com.securevault.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthController {

    public static void main(String[] args) {

//        Function<String, Integer> f = a -> a.length();
//
//        System.out.print(f.apply("abc"));
//
//        Consumer<Integer> c = System.out::print;
//
//        c.accept(4);
//
//        Supplier<Integer> s = () -> 4;
//
//
//        System.out.print(s.get());

        List<Integer> list = new ArrayList<>();
        for(int i = 1;i<=1000;i++) {
            list.add(i);
        }

        list.parallelStream().forEach(System.out::println);
    }


}
