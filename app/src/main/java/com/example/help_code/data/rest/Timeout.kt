package com.example.help_code.data.rest

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class Timeout(val duration: Int)