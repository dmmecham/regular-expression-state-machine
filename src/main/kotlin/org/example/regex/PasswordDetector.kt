package state.regex

class PasswordDetector : ContextDetector() {
    private companion object {
        const val minimumLength = 8
    }

    override fun createInitialState(): State = CollectingState(0, false, false, false)

    private fun transitionTo(state: PasswordState) {
        super.transitionTo(state)
    }

    private abstract inner class PasswordState(
        protected val length: Int,
        protected val seenUpper: Boolean,
        protected val seenSpecial: Boolean,
        protected val lastWasSpecial: Boolean,
    ) : State {
        protected fun advance(token: String) {
            val tokenIsUppercase = TokenRules.isUppercase(token)
            val tokenIsSpecial = TokenRules.isSpecial(token)

            transitionTo(
                nextState(
                    length = length + 1,
                    seenUpper = seenUpper || tokenIsUppercase,
                    seenSpecial = seenSpecial || tokenIsSpecial,
                    lastWasSpecial = tokenIsSpecial,
                )
            )
        }

        final override fun onEnd(context: ContextDetector) {
            if (acceptsAtEnd()) {
                context.accept()
            } else {
                context.reject()
            }
        }

        protected abstract fun nextState(
            length: Int,
            seenUpper: Boolean,
            seenSpecial: Boolean,
            lastWasSpecial: Boolean,
        ): PasswordState

        protected abstract fun acceptsAtEnd(): Boolean
    }

    private inner class CollectingState(
        length: Int,
        seenUpper: Boolean,
        seenSpecial: Boolean,
        lastWasSpecial: Boolean,
    ) : PasswordState(length, seenUpper, seenSpecial, lastWasSpecial) {
        override fun handle(context: ContextDetector, token: String) {
            advance(token)
        }

        override fun nextState(
            length: Int,
            seenUpper: Boolean,
            seenSpecial: Boolean,
            lastWasSpecial: Boolean,
        ): PasswordState {
            return if (length >= minimumLength) {
                ValidatingState(length, seenUpper, seenSpecial, lastWasSpecial)
            } else {
                CollectingState(length, seenUpper, seenSpecial, lastWasSpecial)
            }
        }

        override fun acceptsAtEnd(): Boolean {
            return false
        }
    }

    private inner class ValidatingState(
        length: Int,
        seenUpper: Boolean,
        seenSpecial: Boolean,
        lastWasSpecial: Boolean,
    ) : PasswordState(length, seenUpper, seenSpecial, lastWasSpecial) {
        override fun handle(context: ContextDetector, token: String) {
            advance(token)
        }

        override fun nextState(
            length: Int,
            seenUpper: Boolean,
            seenSpecial: Boolean,
            lastWasSpecial: Boolean,
        ): PasswordState {
            return ValidatingState(length, seenUpper, seenSpecial, lastWasSpecial)
        }

        override fun acceptsAtEnd(): Boolean {
            return length >= minimumLength && seenUpper && seenSpecial && !lastWasSpecial
        }
    }
}
