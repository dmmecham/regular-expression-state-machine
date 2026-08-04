package state.regex

class FloatingPointDetector : ContextDetector() {
    private val startState = StartState()
    private val leadingZeroState = LeadingZeroState()
    private val integerPartState = IntegerPartState()
    private val needFractionDigitState = NeedFractionDigitState()
    private val fractionPartState = FractionPartState()

    override fun initialState(): State = startState

    private inner class StartState : State {
        override fun handle(context: ContextDetector, token: String) {
            when {
                token == "0" -> context.transitionTo(leadingZeroState)
                TokenRules.isNonZeroDigit(token) -> context.transitionTo(integerPartState)
                token == "." -> context.transitionTo(needFractionDigitState)
                else -> context.reject()
            }
        }

        override fun onEnd(context: ContextDetector) {
            context.reject()
        }
    }

    private inner class LeadingZeroState : State {
        override fun handle(context: ContextDetector, token: String) {
            if (token == ".") {
                context.transitionTo(needFractionDigitState)
            } else {
                context.reject()
            }
        }

        override fun onEnd(context: ContextDetector) {
            context.reject()
        }
    }

    private inner class IntegerPartState : State {
        override fun handle(context: ContextDetector, token: String) {
            when {
                TokenRules.isDigit(token) -> Unit
                token == "." -> context.transitionTo(needFractionDigitState)
                else -> context.reject()
            }
        }

        override fun onEnd(context: ContextDetector) {
            context.reject()
        }
    }

    private inner class NeedFractionDigitState : State {
        override fun handle(context: ContextDetector, token: String) {
            if (TokenRules.isDigit(token)) {
                context.transitionTo(fractionPartState)
            } else {
                context.reject()
            }
        }

        override fun onEnd(context: ContextDetector) {
            context.reject()
        }
    }

    private inner class FractionPartState : State {
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
