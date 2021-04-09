package com.idean.snackshtml.errors

/**
 * When a necessary color field is missing or null
 */
class MalformedColorCSSException(key: String?) : Exception("Color malformed at: $key")
