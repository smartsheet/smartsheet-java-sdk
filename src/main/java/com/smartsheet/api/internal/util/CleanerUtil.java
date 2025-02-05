/*
 * Copyright (C) 2025 Smartsheet
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.smartsheet.api.internal.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Closeable;
import java.lang.ref.Cleaner;

public class CleanerUtil {
    private static final Cleaner CLEANER = Cleaner.create();

    private CleanerUtil() {
    }

    /**
     * since Java9 finalize() has been deprecated and Cleaner is the replacement; it uses a PhantomReference to detect
     * when an object is no longer reachable via Strong, Soft, or Weak references (i.e., a candidate for the next GC)
     * and will execute a Runnable that performs the cleanup action that would have been in a finalize() method.
     * <p>
     * it is VITAL that the Runnable does NOT hold a reference to the objToWatch, directly or indirectly.  otherwise it
     * will inadvertently ensure the objToWatch will never be GCed, ironically creating a memory leak in the form of an
     * immortal object.  non-static lambdas and inner classes have an implicit reference to their parent objects (which
     * will likely be or have a reference to the objToWatch).
     *
     * @param objToWatch     - reference to object that, when it's enqueued for GC, will trigger the cleaningAction
     * @param cleaningAction - code to execute when the objToWatch becomes "phantom reachable" (i.e., about to be GCed)
     * @return a Cleanable which can be used to force a clean() action (and unregisters the objToWatch-cleaningAction)
     */
    public static Cleaner.Cleanable register(@NotNull Object objToWatch, @NotNull Runnable cleaningAction) {
        return CLEANER.register(objToWatch, cleaningAction);
    }

    /**
     * this method takes a ref value and returns a Runnable that, when invoked, will close it.  this method actually
     * makes the issue of ref-capture mentioned above impossible (since the Closeable ref is copied into the Runnable
     * lambda, thus ensuring no 'this' is inadvertently captured).
     *
     * @param closeable the Closeable to be closed when the returned Runnable is run
     * @return a Runnable that will close the provided Closeable and quietly discard any exceptions thrown.
     */
    public static Runnable closeQuietly(@Nullable Closeable closeable) {
        return () -> {
            try {
                closeable.close();
            } catch (Exception ignore) {
            }
        };
    }
}
