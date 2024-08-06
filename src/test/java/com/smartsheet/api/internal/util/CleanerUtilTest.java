package com.smartsheet.api.internal.util;

import org.junit.jupiter.api.Test;

import java.io.Closeable;
import java.lang.ref.Cleaner;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class CleanerUtilTest {

    @Test
    void register_then_clean() {
        AtomicBoolean closed = new AtomicBoolean();
        // in general this is NOT recommended since the lambda code is holding a strong ref to the watched object;
        // in this test it doesn't matter and i'm just being lazy
        Cleaner.Cleanable cleanable = CleanerUtil.register(closed, () -> closed.set(true));
        assertThat(closed).isFalse();
        cleanable.clean();
        assertThat(closed).isTrue();
    }

    @Test
    void register_then_gc() {
        Object object = new Object();
        AtomicBoolean closed = new AtomicBoolean();
        CleanerUtil.register(object, () -> {
            synchronized (closed) {
                closed.set(true);
                closed.notify();    // wake up the sleeping main thread
            }
        });
        assertThat(closed).isFalse();

        // synchronized should prevent byte-code reordering
        assertThat(object).isNotNull();;  // not dead yet
        assertThat(closed).isFalse();
        object = null;  // force release the ref
        assertThat(object).isNull();

        // ask the JVM to clean up heap, please :)
        System.gc();

        // unfortunately there's no way to force the PhantomRef pointing to object to trigger immediately after a GC so
        // we use wait-notify - there is an edge-case here where the GC fires and the Cleaner cleans BEFORE we block but
        // that's not really an issue; this wait-notify is just to minimize the wait for closed to be updated
        synchronized (closed) {
            assertDoesNotThrow(() -> closed.wait(1000));  // wait one second (or less)
        }
        assertThat(closed).isTrue();
    }

    @Test
    void closeQuietly() {
        AtomicBoolean closed = new AtomicBoolean();
        Closeable closeable = () -> closed.set(true);
        assertThat(closed).isFalse();
        Runnable runnable = CleanerUtil.closeQuietly(closeable);
        assertThat(closed).isFalse();
        runnable.run();
        assertThat(closed).isTrue();
    }
}