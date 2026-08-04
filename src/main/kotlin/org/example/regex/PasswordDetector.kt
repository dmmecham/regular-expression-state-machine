package state.regex

class PasswordDetector : ContextDetector() {
    private companion object {
        const val minimumLength = 8
    }

    override fun createInitialState(): State = CollectingState(0, MissingUpperAndSpecial)

    private fun transitionTo(state: PasswordState) {
        super.transitionTo(state)
    }

    private abstract inner class PasswordState(
        protected val complexity: ComplexityState,
    ) : State {
    }

    private inner class CollectingState(
        private val length: Int,
        complexity: ComplexityState,
    ) : PasswordState(complexity) {
        override fun handle(context: ContextDetector, token: String) {
            val nextLength = length + 1
            val nextComplexity = complexity.onToken(token)

            transitionTo(
                if (nextLength >= minimumLength) {
                    EnforcingState(nextComplexity)
                } else {
                    CollectingState(nextLength, nextComplexity)
                }
            )
        }

        override fun onEnd(context: ContextDetector) {
            context.reject()
        }
    }

    private inner class EnforcingState(
        complexity: ComplexityState,
    ) : PasswordState(complexity) {
        override fun handle(context: ContextDetector, token: String) {
            transitionTo(EnforcingState(complexity.onToken(token)))
        }

        override fun onEnd(context: ContextDetector) {
            complexity.onEnd(context)
        }
    }

    private interface ComplexityState {
        fun onToken(token: String): ComplexityState
        fun onEnd(context: ContextDetector)
    }

    private abstract class RejectingComplexityState : ComplexityState {
        final override fun onEnd(context: ContextDetector) {
            context.reject()
        }
    }

    private object MissingUpperAndSpecial : RejectingComplexityState() {
        override fun onToken(token: String): ComplexityState {
            val tokenIsUppercase = TokenRules.isUppercase(token)
            val tokenIsSpecial = TokenRules.isSpecial(token)

            return when {
                tokenIsUppercase && tokenIsSpecial -> UpperAndSpecialTrailing
                tokenIsUppercase -> MissingSpecial
                tokenIsSpecial -> MissingUpperWithTrailingSpecial
                else -> MissingUpperAndSpecial
            }
        }
    }

    private object MissingUpperWithTrailingSpecial : RejectingComplexityState() {
        override fun onToken(token: String): ComplexityState {
            val tokenIsUppercase = TokenRules.isUppercase(token)
            val tokenIsSpecial = TokenRules.isSpecial(token)

            return when {
                tokenIsUppercase && tokenIsSpecial -> UpperAndSpecialTrailing
                tokenIsUppercase -> UpperAndSpecialReady
                tokenIsSpecial -> MissingUpperWithTrailingSpecial
                else -> MissingUpperNoTrailingSpecial
            }
        }
    }

    private object MissingUpperNoTrailingSpecial : RejectingComplexityState() {
        override fun onToken(token: String): ComplexityState {
            val tokenIsUppercase = TokenRules.isUppercase(token)
            val tokenIsSpecial = TokenRules.isSpecial(token)

            return when {
                tokenIsUppercase && tokenIsSpecial -> UpperAndSpecialTrailing
                tokenIsUppercase -> UpperAndSpecialReady
                tokenIsSpecial -> MissingUpperWithTrailingSpecial
                else -> MissingUpperNoTrailingSpecial
            }
        }
    }

    private object MissingSpecial : RejectingComplexityState() {
        override fun onToken(token: String): ComplexityState {
            return if (TokenRules.isSpecial(token)) {
                UpperAndSpecialTrailing
            } else {
                MissingSpecial
            }
        }
    }

    private object UpperAndSpecialTrailing : RejectingComplexityState() {
        override fun onToken(token: String): ComplexityState {
            return if (TokenRules.isSpecial(token)) {
                UpperAndSpecialTrailing
            } else {
                UpperAndSpecialReady
            }
        }
    }

    private object UpperAndSpecialReady : ComplexityState {
        override fun onToken(token: String): ComplexityState {
            return if (TokenRules.isSpecial(token)) {
                UpperAndSpecialTrailing
            } else {
                UpperAndSpecialReady
            }
        }

        override fun onEnd(context: ContextDetector) {
            context.accept()
        }
    }
}
