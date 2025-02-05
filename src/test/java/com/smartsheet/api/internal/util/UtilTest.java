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

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UtilTest {
    @Nested
    class ThrowIfNullTests {

        @Nested
        class ThrowIfNullOneArgTests {
            @Test
            void throwIfNullOneArg_null() {
                // Act & Assert
                assertThatThrownBy(() -> Util.throwIfNull((Object) null))
                        .isInstanceOf(IllegalArgumentException.class);
            }

            @Test
            void throwIfNullOneArg_nonnull() {
                //Arrange
                Object obj = new Object();

                // Act & Assert
                assertThatNoException().isThrownBy(() -> Util.throwIfNull(obj));
            }
        }

        @Nested
        class ThrowIfNullTwoArgsTests {
            @Test
            void throwIfNullTwoArgs_null_null() {
                // Act & Assert
                assertThatThrownBy(() -> Util.throwIfNull(null, null))
                        .isInstanceOf(IllegalArgumentException.class);
            }

            @Test
            void throwIfNullTwoArgs_nonnull_null() {
                //Arrange
                Object obj = new Object();

                // Act & Assert
                assertThatThrownBy(() -> Util.throwIfNull(obj, null))
                        .isInstanceOf(IllegalArgumentException.class);
            }

            @Test
            void throwIfNullTwoArgs_null_nonnull() {
                //Arrange
                Object obj = new Object();

                // Act & Assert
                assertThatThrownBy(() -> Util.throwIfNull(null, obj))
                        .isInstanceOf(IllegalArgumentException.class);
            }

            @Test
            void throwIfNullTwoArgs_nonnull_nonnull() {
                //Arrange
                Object obj1 = new Object();
                Object obj2 = new Object();

                // Act & Assert
                assertThatNoException().isThrownBy(() -> Util.throwIfNull(obj1, obj2));
            }
        }

        @Nested
        class ThrowIfNullVarArgsTests {
            @Test
            void throwIfNullVarArgs_null_null_null() {
                // Act & Assert
                assertThatThrownBy(() -> Util.throwIfNull(null, null, null))
                        .isInstanceOf(IllegalArgumentException.class);
            }

            @Test
            void throwIfNullVarArgs_null_null_nonnull() {
                //Arrange
                Object obj = new Object();

                // Act & Assert
                assertThatThrownBy(() -> Util.throwIfNull(null, null, obj))
                        .isInstanceOf(IllegalArgumentException.class);
            }

            @Test
            void throwIfNullVarArgs_nonnull_nonnull_nonnull() {
                //Arrange
                Object obj1 = new Object();
                Object obj2 = new Object();
                Object obj3 = new Object();

                // Act & Assert
                assertThatNoException().isThrownBy(() -> Util.throwIfNull(obj1, obj2, obj3));
            }

            @Test
            void throwIfNullVarArgs_nonnullArray() {
                //Arrange
                Object[] objArray = new Object[]{
                        new Object(),
                        new Object(),
                        new Object(),
                };

                // Act & Assert
                assertThatNoException().isThrownBy(() -> Util.throwIfNull(objArray));
            }

            @Test
            void throwIfNullVarArgs_nullArray() {
                //Arrange
                Object[] objArray = new Object[]{
                        new Object(),
                        new Object(),
                        null,
                };

                // Act & Assert
                assertThatThrownBy(() -> Util.throwIfNull(objArray))
                        .isInstanceOf(IllegalArgumentException.class);
            }
        }
    }

    @Nested
    class ThrowIfEmptyTests {
        @Nested
        class ThrowIfEmptyOneArgTests {
            @Test
            void throwIfEmptyOneArg_notEmpty() {
                // Act & Assert
                assertThatNoException().isThrownBy(() -> Util.throwIfEmpty("Not Empty"));
            }

            @Test
            void throwIfEmptyOneArg_null() {
                // Act & Assert
                assertThatNoException().isThrownBy(() -> Util.throwIfEmpty((String) null));
            }

            @Test
            void throwIfEmptyOneArg_empty() {
                // Act & Assert
                assertThatThrownBy(() -> Util.throwIfEmpty(""))
                        .isInstanceOf(IllegalArgumentException.class);
            }
        }

        @Nested
        class ThrowIfEmptyVarArgsTests {

            @Test
            void throwIfEmptyVarArgs_notEmpty_notEmpty() {
                // Act & Assert
                assertThatNoException().isThrownBy(() -> Util.throwIfEmpty("Not Empty", "Also Not"));
            }

            @Test
            void throwIfEmptyVarArgs_null_null() {
                // Act & Assert
                assertThatNoException().isThrownBy(() -> Util.throwIfEmpty(null, null));
            }

            @Test
            void throwIfEmptyVarArgs_notEmpty_null() {
                // Act & Assert
                assertThatNoException().isThrownBy(() -> Util.throwIfEmpty("Not Empty", null));
            }

            @Test
            void throwIfEmptyVarArgs_empty_empty() {
                // Act & Assert
                assertThatThrownBy(() -> Util.throwIfEmpty("", ""))
                        .isInstanceOf(IllegalArgumentException.class);
            }

            @Test
            void throwIfEmptyVarArgs_notEmpty_empty() {
                // Act & Assert
                assertThatThrownBy(() -> Util.throwIfEmpty("Not Empty", ""))
                        .isInstanceOf(IllegalArgumentException.class);
            }
        }
    }
}
