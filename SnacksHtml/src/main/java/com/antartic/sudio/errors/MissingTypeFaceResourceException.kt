package com.antartic.sudio.errors

/**
 * When a necessary field (like url or font-face) is missing or null
 * And specified as val nonnull in kotlin
 */
class MissingTypeFaceResourceException(path: String) : Exception("Typeface path in css not found at: $path")
