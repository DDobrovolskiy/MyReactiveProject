package dda.reactor;

import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Arrays;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws InterruptedException {
        System.out.println( "Hello World!" );

        Mono.empty();
        Flux.empty();

        Mono<Integer> mono = Mono.just(1);
        Flux<Integer> flux = Flux.just(1, 2, 3, 4, 5);

        Flux<Integer> fluxFromMono = mono.flux();
        Mono<Boolean> monoFromFlux = flux.any(i -> i.equals(41));
        Mono<Integer> monoFromFlux2 = flux.elementAt(1);

        Flux<Integer> range = Flux.range(0, 4);
        range.subscribe(System.out::println);

        Flux.fromIterable(Arrays.asList(4, 5)).subscribe(System.out::println);

        Flux.<String>generate(sink -> {
            sink.next("Hello Flux!");
        })
                .delayElements(Duration.ofMillis(500))      //Теперь выполняется в параллельном потоке
                .take(3)
                .subscribe(System.out::println);

        Thread.sleep(2000); //Ждем пока выполнится

        Flux.generate(
                () -> 100,
                (state, sink) -> {
                    if (state > 300) {
                        sink.complete();
                    } else {
                        sink.next("Turn: " + state);
                    }
                    return state + 100;
                }
        )
                .subscribe(System.out::println);

        Flux<Integer> producer = Flux.range(0, 10);

        Flux.create(fluxSink -> {
                producer.subscribe(new BaseSubscriber<Integer>() {
                    @Override
                    protected void hookOnNext(Integer value) {
                        fluxSink.next(value);
                    }

                    @Override
                    protected void hookOnComplete() {
                        fluxSink.complete();
                    }
                });
        })
                .subscribe(System.out::println);


        Flux.create(fluxSink -> {
            fluxSink.onRequest(r -> {
                fluxSink.next("DB return data! - " + producer.blockFirst());
            });
        })
                .subscribe(System.out::println);

        Flux<String> first = Flux.just("World", "code");
        Flux<String> second = Flux.just("Hello", "World", "Java", "Linux");

        second
                .zipWith(first, (s, f) -> String.format("%s, %s", f, s))
                .delayElements(Duration.ofMillis(1000))
                .timeout(Duration.ofMillis(800))
                .retry(3)
                .onErrorReturn("Too slow")
                .subscribe(System.out::println, System.err::println, () -> System.out.println("Finished"));

        Thread.sleep(4000);
    }
}
