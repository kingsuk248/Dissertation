package com.kafka.consumer;

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingController {

    private static final String helloTemplate = "Hello, %s!";
    private static final String goodbyeTemplate = "Goodbye, %s!";
    private final AtomicLong counter = new AtomicLong();

    @RequestMapping("/greeting")
    public Greeting greeting(@RequestParam(value="name", defaultValue="World") String name) {
    	long counterValue = counter.incrementAndGet();
    	Greeting greeting = null;
    	if (counterValue % 2 == 0) {
    		//greeting = new Greeting(counterValue, String.format(helloTemplate, name));
    	} else {
    		//greeting = new Greeting(counterValue, String.format(goodbyeTemplate, name));
    	}
        return greeting;
    }
}
