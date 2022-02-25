/*
 * Copyright (c) 2022-present Jeroen Debois
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.joen.compose.ext

import androidx.compose.ui.platform.UriHandler

/**
 * @author jeroen
 * @since 25/02/2022 16:43
 */
/**
 * Convenience function to safely attempt to open a URI
 *
 * @param uri the URI that needs to be opened
 * @param onError function to be invoked with a throwable if the opening of the URI throws an error
 *
 * @return true if the opening was successful, false if not
 */
fun UriHandler.openUri(
    uri: String,
    onError: (Throwable) -> Unit = {},
): Boolean {
    try {
        this.openUri(uri = uri)
        return true
    } catch (throwable: Throwable) {
        onError(throwable)
    }
    return false
}
