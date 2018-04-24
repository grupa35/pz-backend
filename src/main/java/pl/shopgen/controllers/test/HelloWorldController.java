package pl.shopgen.controllers.test;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.shopgen.models.test.SimpleTestMessage;

@RestController
@RequestMapping("/test")
public class HelloWorldController {

    @RequestMapping("/hello")
    public SimpleTestMessage helloWorldTest(@RequestParam(value = "text", defaultValue = "Hello World") String text) {
        return new SimpleTestMessage(text);
    }

    @RequestMapping("/romic96")
    public SimpleTestMessage romic96Test() {
        String text = "romic96";
        return new SimpleTestMessage(text);
    }

    @RequestMapping("/justynapietryga")
    public SimpleTestMessage justynapietrygaTest() {
        String text = "justynapietryga";
        return new SimpleTestMessage(text);
    }

    @RequestMapping("/tpiwowarski")
    public SimpleTestMessage tpiwowarski() {
        return new SimpleTestMessage("tpiwowarski");
    }

    @RequestMapping("/wrup")
    public SimpleTestMessage wrupTest() {
        String text = "wrup";
        return new SimpleTestMessage(text);
    }

    @RequestMapping("/piotrpiedel")
    public SimpleTestMessage piotrpiedelTest() {
        String text = "piotrpiedel";
        return new SimpleTestMessage(text);
    }

    @RequestMapping("/przemyslawstrojny")
    public SimpleTestMessage przemyslawstrojnyTest() {
        String text = "przemyslawstrojny";
        return new SimpleTestMessage(text);
    }
}
