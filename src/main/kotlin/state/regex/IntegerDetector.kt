package state.regex

class IntegerDetector : ContextDetector() {
    private val startState = StartState()
    private val digitsState = DigitsState()

    override fun createInitialState(): State = startState

    private fun transitionTo(state: IntegerState) {
        super.transitionTo(state)
    }

    private abstract inner class IntegerState : State

    private inner class StartState : IntegerState() {
        override fun handle(context: ContextDetector, token: String) {
            when {
                TokenRules.isNonZeroDigit(token) -> transitionTo(digitsState)
                else -> context.reject()
            }
        }

        override fun onEnd(context: ContextDetector) {
            context.reject()
        }
    }

    private inner class DigitsState : IntegerState() {
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
