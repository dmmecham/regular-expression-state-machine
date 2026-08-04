package state.regex

class BinaryOnesDetector : ContextDetector() {
    private val startState = StartState()
    private val lastWasOneState = LastWasOneState()
    private val lastWasZeroState = LastWasZeroState()

    override fun createInitialState(): State = startState

    private fun transitionTo(state: BinaryOnesState) {
        super.transitionTo(state)
    }

    private abstract inner class BinaryOnesState : State

    private inner class StartState : BinaryOnesState() {
        override fun handle(context: ContextDetector, token: String) {
            when (token) {
                "1" -> transitionTo(lastWasOneState)
                else -> context.reject()
            }
        }

        override fun onEnd(context: ContextDetector) {
            context.reject()
        }
    }

    private inner class LastWasOneState : BinaryOnesState() {
        override fun handle(context: ContextDetector, token: String) {
            when (token) {
                "1" -> Unit
                "0" -> transitionTo(lastWasZeroState)
                else -> context.reject()
            }
        }

        override fun onEnd(context: ContextDetector) {
            context.accept()
        }
    }

    private inner class LastWasZeroState : BinaryOnesState() {
        override fun handle(context: ContextDetector, token: String) {
            when (token) {
                "0" -> Unit
                "1" -> transitionTo(lastWasOneState)
                else -> context.reject()
            }
        }

        override fun onEnd(context: ContextDetector) {
            context.reject()
        }
    }
}
