package edu.ccsu.cs492.kerberoschat.webapp;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class WebAppController {

    @RequestMapping(value = "/",  method = RequestMethod.GET)
    public static String serveWebApp() {
        return "app/index";
    }
}
