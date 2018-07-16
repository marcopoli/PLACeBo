package ItemsEnchantment;


import java.io.File;
import java.net.URL;

import com.github.axet.vget.VGet;

public class Example {

    public static void main(String[] args) {
        try {
            VGet v = new VGet(new URL("https://www.youtube.com/watch?v=eUZdR0G20Qs"), new File("C:\\Users\\salv9\\Desktop\\Nuova\\"));
            v.download();
           // v.extract();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}