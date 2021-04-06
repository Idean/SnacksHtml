package com.idean.snackshtml.errors

/**
 * When a necessary field (like url or font-face) is missing or null
 * And specified as val nonnull in kotlin
 */
class MissingCSSMandatoryFieldException(key: String) : Exception("Missing CSS mandatory value at: $key : ???")
