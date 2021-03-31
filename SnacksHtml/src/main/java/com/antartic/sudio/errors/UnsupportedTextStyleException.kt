package com.antartic.sudio.errors

/**
 * When a field is not supported
 */
class UnsupportedTextStyleException(unsupportedField: String) : Exception("$unsupportedField is not supported.")
