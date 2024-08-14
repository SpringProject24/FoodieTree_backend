package org.nmfw.foodietree.domain.notification.controller;

import org.nmfw.foodietree.domain.notification.dto.req.Greeting;
import org.nmfw.foodietree.domain.notification.dto.req.HelloMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

@Controller
public class NotifyController {

    @MessageMapping("/hello")
    @SendTo("/topic/greetings") // 정해둔 이름
    public Greeting greeting(HelloMessage message) throws Exception {
        Thread.sleep(500); // simulated delay
        return new Greeting("Hello, " + HtmlUtils.htmlEscape(message.getName()) + "!");
    }
}
