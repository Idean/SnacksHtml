package com.antartic.sudio.errors

/**
 * When a necessary field (like url or font-face) is missing or null
 * And specified as val nonnull in kotlin
 */
class MalformedCSSException(key: String) : Exception("CSS malformed at: $key")
