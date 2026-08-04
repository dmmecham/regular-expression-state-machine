package state.regex

class IntegerDetector : ContextDetector() {
    private val startState = StartState()
    private val digitsState = DigitsState()

    override fun initialState(): State = startState

    private inner class StartState : State {
        override fun handle(context: ContextDetector, token: String) {
            when {
                TokenRules.isNonZeroDigit(token) -> context.transitionTo(digitsState)
                else -> context.reject()
            }
        }

        override fun onEnd(context: ContextDetector) {
            context.reject()
        }
    }

    private inner class DigitsState : State {
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
