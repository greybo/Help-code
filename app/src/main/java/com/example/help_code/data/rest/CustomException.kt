package com.example.help_code.data.rest

sealed class CustomException : Exception() {
    class NoInternetException : CustomException()
    class NoPatronNumberException : CustomException()
    class TierErrorException : CustomException()
    class NotLoggedException : CustomException()
    class UnauthorizedError : CustomException()
    class RewardScreenError : CustomException()
    sealed class StructuredErrors : CustomException() {
        class SchemaCorrupted : StructuredErrors()
        class ContainerNotFound : StructuredErrors()
        class MediaFailed : StructuredErrors()
    }

    class PinComplexityError : CustomException()
    class PinMatchError : CustomException()
}