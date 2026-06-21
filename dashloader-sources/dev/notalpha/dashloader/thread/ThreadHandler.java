/*
 * Decompiled with CFR 0.152.
 */
package dev.notalpha.dashloader.thread;

import dev.notalpha.dashloader.api.DashObject;
import dev.notalpha.dashloader.api.registry.RegistryReader;
import dev.notalpha.dashloader.registry.data.ChunkData;
import dev.notalpha.dashloader.thread.IndexedArrayMapTask;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinWorkerThread;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.IntFunction;

public final class ThreadHandler {
    public static final int THREADS = Runtime.getRuntime().availableProcessors();
    public static final ThreadHandler INSTANCE = new ThreadHandler();
    private final ForkJoinPool threadPool = new ForkJoinPool(THREADS, new ForkJoinPool.ForkJoinWorkerThreadFactory(){
        private final AtomicInteger threadNumber = new AtomicInteger(0);

        @Override
        public ForkJoinWorkerThread newThread(ForkJoinPool pool) {
            ForkJoinWorkerThread dashThread = ForkJoinPool.defaultForkJoinWorkerThreadFactory.newThread(pool);
            dashThread.setDaemon(true);
            dashThread.setName("dlc-thread-" + this.threadNumber.getAndIncrement());
            return dashThread;
        }
    }, null, true);

    private ThreadHandler() {
    }

    public static int calcThreshold(int tasks) {
        return Math.max(tasks / (THREADS * 8), 4);
    }

    public <R, D extends DashObject<? extends R>> void parallelExport(ChunkData.Entry<D>[] in, R[] out, RegistryReader reader) {
        this.threadPool.invoke(new IndexedArrayMapTask<DashObject, Object>(in, out, d -> d.export(reader)));
    }

    public void parallelRunnable(Runnable ... runnables) {
        this.parallelRunnable(List.of(runnables));
    }

    public void parallelRunnable(Collection<Runnable> runnables) {
        for (Future future : this.threadPool.invokeAll(runnables.stream().map(Executors::callable).toList())) {
            this.acquire(future);
        }
    }

    @SafeVarargs
    public final <O> O[] parallelCallable(IntFunction<O[]> creator, Callable<O> ... callables) {
        O[] out = creator.apply(callables.length);
        List<Future<O>> futures = this.threadPool.invokeAll(List.of(callables));
        int futuresSize = futures.size();
        for (int i = 0; i < futuresSize; ++i) {
            out[i] = this.acquire(futures.get(i));
        }
        return out;
    }

    public <O> Collection<O> parallelCallable(Collection<Callable<O>> callables) {
        ArrayList<O> out = new ArrayList<O>();
        List<Future<O>> futures = this.threadPool.invokeAll(callables);
        for (Future<O> future : futures) {
            out.add(this.acquire(future));
        }
        return out;
    }

    private <O> O acquire(Future<O> future) {
        try {
            return future.get();
        }
        catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}

