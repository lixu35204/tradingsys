package util;

import java.util.ArrayList;
import java.util.List;

public final class MultiRunner implements Publisher<Runnable> {

    private final Publisher<Runnable> underlyingRunner;

    private List<Runnable> waitingQueue = new ArrayList<>();

    private List<Runnable> workingQueue = new ArrayList<>();

    private boolean pending = false;

    public MultiRunner(Publisher<Runnable> runner) {
        underlyingRunner = runner;
    }

    @Override
    public synchronized void publish(Runnable item) {
        waitingQueue.add(item);
        tryRun();
    }

    private void tryRun() {
        if (!pending) {
            swapQueues();
            underlyingRunner.publish(task);
        }
    }

    private synchronized boolean swapQueues() {
        final List<Runnable> temp = workingQueue;
        workingQueue = waitingQueue;
        waitingQueue = temp;
        pending = !workingQueue.isEmpty();
        return pending;
    }

    private final Runnable task = () -> {
        do {
            workingQueue.forEach(r -> r.run());
            workingQueue = new ArrayList<>();
        } while (swapQueues());
    };

    public static MultiRunner multiRunner(Publisher<Runnable> r){
        if (r instanceof MultiRunner){
            return (MultiRunner) r;
        }
        return new MultiRunner(r);
    }
}
