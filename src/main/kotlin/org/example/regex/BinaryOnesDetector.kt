package state.regex

class BinaryOnesDetector : ContextDetector() {
    private val startState = StartState()
    private val lastWasOneState = LastWasOneState()
    private val lastWasZeroState = LastWasZeroState()

    override fun initialState(): State = startState

    private inner class StartState : State {
        override fun handle(context: ContextDetector, token: String) {
            when (token) {
                "1" -> context.transitionTo(lastWasOneState)
                else -> context.reject()
            }
        }

        override fun onEnd(context: ContextDetector) {
            context.reject()
        }
    }

    private inner class LastWasOneState : State {
        override fun handle(context: ContextDetector, token: String) {
            when (token) {
                "1" -> Unit
                "0" -> context.transitionTo(lastWasZeroState)
                else -> context.reject()
            }
        }

        override fun onEnd(context: ContextDetector) {
            context.accept()
        }
    }

    private inner class LastWasZeroState : State {
        override fun handle(context: ContextDetector, token: String) {
            when (token) {
                "0" -> Unit
                "1" -> context.transitionTo(lastWasOneState)
                else -> context.reject()
            }
        }

        override fun onEnd(context: ContextDetector) {
            context.reject()
        }
    }
}
