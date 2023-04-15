package dda.reactor.async;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class AsyncTestMain {
    public static void main(String[] args) {
        Addata addata = new Addata();
        Back back = new Back();
        IntStream.range(0, 10)
                .boxed()
                .forEach(i -> {
                    long start = System.currentTimeMillis();
                    System.out.println(get(addata, back, i));
                    System.out.println("END: " + (System.currentTimeMillis() - start) + " ms");
                });
    }


    public static List<String> get(Addata addata, Back back, int i) {
        Mono<List<String>> addataMono = Mono.fromFuture(new CompletableFuture<List<String>>()
                .completeAsync(() -> addata.getData(i)));
        return Mono.just(back.getData(i)).flatMapIterable(list -> list)
                .concatWith(addataMono.flatMapIterable(list -> list))
                .map(String::toUpperCase)
                .collectList()
                .block();
    }
}
