package util;

import java.util.Collection;
import java.util.concurrent.*;

public class ThreadDispatchCachePublisher<T> {
    private final ConcurrentLinkedQueue<T> storage = new ConcurrentLinkedQueue<>();
    private final Dispatcher dispatcher = new Dispatcher();
    private static final ExecutorService poolExecutor =
            new ThreadPoolExecutor(
                    10,
                    100,
                    5000,
                    TimeUnit.MILLISECONDS,
                    new LinkedBlockingQueue<>()
            );

    public synchronized void publish(T item) {
        storage.add(item);
        dispatcher.publish(item);
    }

    public void subscribe(Publisher<T> publisher) {
        dispatcher.register(publisher, item -> {
            poolExecutor.execute(item);
        });
    }

    public Collection<T> get() {
        return storage;
    }

    private class Dispatcher implements Publisher<T> {
        private final ConcurrentHashMap<Publisher<T>, MultiRunner> runnerRegistry = new ConcurrentHashMap<>();
        private final CopyOnWriteArraySet<Publisher<T>> publishers = new CopyOnWriteArraySet<>();

        void register(final Publisher<T> publisher, final Publisher<Runnable> r) {
            final MultiRunner runner = MultiRunner.multiRunner(r);
            runner.publish(() -> registerAndPublishInitialSnapshot(publisher, runner));
        }

        void registerAndPublishInitialSnapshot(final Publisher<T> publisher, final MultiRunner runner) {
            Collection<T> snapshot;
            synchronized (ThreadDispatchCachePublisher.this) {
                snapshot = get();
                runnerRegistry.put(publisher, runner);
                publishers.add(publisher);
            }
            for (T item : snapshot) {
                publisher.publish(item);
            }
        }

        @Override
        public void publish(final T item) {
            for (Publisher<T> publisher : publishers) {
                final MultiRunner runner = runnerRegistry.get(publisher);
                runner.publish(() -> publisher.publish(item));
            }
        }
    }
}
