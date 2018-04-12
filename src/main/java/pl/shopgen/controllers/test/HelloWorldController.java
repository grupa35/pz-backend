package pl.shopgen.controllers.test;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.shopgen.models.test.SimpleTestMessage;

@RestController
public class HelloWorldController {

    @RequestMapping("/api/test/hello")
    public SimpleTestMessage helloWorldTest(@RequestParam(value = "text", defaultValue = "Hello World") String text) {
        return new SimpleTestMessage(text);
    }

    @RequestMapping("/api/test/romic96")
    public SimpleTestMessage romic96Test() {
        String text = "romic96";
        return new SimpleTestMessage(text);
    }

    @RequestMapping("/api/test/justynapietryga")
    public SimpleTestMessage justynapietrygaTest() {
        String text = "justynapietryga";
        return new SimpleTestMessage(text);
    }

    @RequestMapping("/api/test/tpiwowarski")
    public SimpleTestMessage tpiwowarski() {
        return new SimpleTestMessage("tpiwowarski");
    }

    @RequestMapping("/api/test/wrup")
    public SimpleTestMessage wrupTest() {
        String text = "wrup";
        return new SimpleTestMessage(text);
    }
    @RequestMapping("/api/test/piotrpiedel")
    public SimpleTestMessage piotrpiedelTest() {
        String text = "piotrpiedel";
        return new SimpleTestMessage(text);
    }
}
