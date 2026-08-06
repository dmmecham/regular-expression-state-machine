package state.regex

class FloatingPointDetector : ContextDetector() {
    private val startState = StartState()
    private val leadingZeroState = LeadingZeroState()
    private val integerPartState = IntegerPartState()
    private val needFractionDigitState = NeedFractionDigitState()
    private val fractionPartState = FractionPartState()

    override fun createInitialState(): State = startState

    private fun transitionTo(state: FloatingPointState) {
        super.transitionTo(state)
    }

    private abstract inner class FloatingPointState : State

    private inner class StartState : FloatingPointState() {
        override fun handle(context: ContextDetector, token: String) {
            when {
                token == "0" -> transitionTo(leadingZeroState)
                TokenRules.isNonZeroDigit(token) -> transitionTo(integerPartState)
                token == "." -> transitionTo(needFractionDigitState)
                else -> context.reject()
            }
        }

        override fun onEnd(context: ContextDetector) {
            context.reject()
        }
    }

    private inner class LeadingZeroState : FloatingPointState() {
        override fun handle(context: ContextDetector, token: String) {
            if (token == ".") {
                transitionTo(needFractionDigitState)
            } else {
                context.reject()
            }
        }

        override fun onEnd(context: ContextDetector) {
            context.reject()
        }
    }

    private inner class IntegerPartState : FloatingPointState() {
        override fun handle(context: ContextDetector, token: String) {
            when {
                TokenRules.isDigit(token) -> Unit
                token == "." -> transitionTo(needFractionDigitState)
                else -> context.reject()
            }
        }

        override fun onEnd(context: ContextDetector) {
            context.reject()
        }
    }

    private inner class NeedFractionDigitState : FloatingPointState() {
        override fun handle(context: ContextDetector, token: String) {
            if (TokenRules.isDigit(token)) {
                transitionTo(fractionPartState)
            } else {
                context.reject()
            }
        }

        override fun onEnd(context: ContextDetector) {
            context.reject()
        }
    }

    private inner class FractionPartState : FloatingPointState() {
        override fun handle(context: ContextDetector, token: String) {
            if (!TokenRules.isDigit(token)) {
                context.reject()
            }
        }

        override fun onEnd(context: ContextDetector) {
            context.accept()
        }
    }
}
