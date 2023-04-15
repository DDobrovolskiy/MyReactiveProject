package dda.reactor.async;

import java.util.List;

public class Addata {
    public List<String> getData(int i) {
//        System.out.println("ADDATA : " + Thread.currentThread().getName());
        try {
            Thread.sleep(i * 150L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        System.out.println("ADDATA SLEEP END");
        return List.of(String.valueOf(i));
    }
}
