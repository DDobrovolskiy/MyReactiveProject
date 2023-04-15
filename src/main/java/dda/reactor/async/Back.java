package dda.reactor.async;

import java.util.List;

public class Back {
    public List<String> getData(int i) {
//        System.out.println("BACK : " + Thread.currentThread().getName());
        try {
            Thread.sleep(i * 100L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return List.of(String.valueOf(i));
    }
}
