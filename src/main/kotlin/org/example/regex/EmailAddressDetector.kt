package state.regex

class EmailAddressDetector : ContextDetector() {
    private val startState = StartState()
    private val part1State = Part1State()
    private val needPart2State = NeedPart2State()
    private val part2State = Part2State()
    private val needPart3State = NeedPart3State()
    private val part3State = Part3State()

    override fun initialState(): State = startState

    private fun transitionTo(state: EmailAddressState) {
        super.transitionTo(state)
    }

    private abstract inner class EmailAddressState : State

    private fun isPart1Char(token: String): Boolean {
        return token != "@" && !TokenRules.isSpace(token)
    }

    private fun isPart2Or3Char(token: String): Boolean {
        return token != "@" && token != "." && !TokenRules.isSpace(token)
    }

    private inner class StartState : EmailAddressState() {
        override fun handle(context: ContextDetector, token: String) {
            if (isPart1Char(token)) {
                transitionTo(part1State)
            } else {
                context.reject()
            }
        }

        override fun onEnd(context: ContextDetector) {
            context.reject()
        }
    }

    private inner class Part1State : EmailAddressState() {
        override fun handle(context: ContextDetector, token: String) {
            when {
                token == "@" -> transitionTo(needPart2State)
                isPart1Char(token) -> Unit
                else -> context.reject()
            }
        }

        override fun onEnd(context: ContextDetector) {
            context.reject()
        }
    }

    private inner class NeedPart2State : EmailAddressState() {
        override fun handle(context: ContextDetector, token: String) {
            if (isPart2Or3Char(token)) {
                transitionTo(part2State)
            } else {
                context.reject()
            }
        }

        override fun onEnd(context: ContextDetector) {
            context.reject()
        }
    }

    private inner class Part2State : EmailAddressState() {
        override fun handle(context: ContextDetector, token: String) {
            when {
                token == "." -> transitionTo(needPart3State)
                isPart2Or3Char(token) -> Unit
                else -> context.reject()
            }
        }

        override fun onEnd(context: ContextDetector) {
            context.reject()
        }
    }

    private inner class NeedPart3State : EmailAddressState() {
        override fun handle(context: ContextDetector, token: String) {
            if (isPart2Or3Char(token)) {
                transitionTo(part3State)
            } else {
                context.reject()
            }
        }

        override fun onEnd(context: ContextDetector) {
            context.reject()
        }
    }

    private inner class Part3State : EmailAddressState() {
        override fun handle(context: ContextDetector, token: String) {
            if (!isPart2Or3Char(token)) {
                context.reject()
            }
        }

        override fun onEnd(context: ContextDetector) {
            context.accept()
        }
    }
}
