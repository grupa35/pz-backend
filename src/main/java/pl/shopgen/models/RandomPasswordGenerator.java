package pl.shopgen.models;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class RandomPasswordGenerator {

    public String generatePassword(int len)
    {
        String chars = "0123456789ABCDEFGHIJKLMNOPRSTUWYZ";
        Random random = new Random();

        StringBuilder sb = new StringBuilder(len);

        for(int i=0; i<len; i++)
        {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }
}
