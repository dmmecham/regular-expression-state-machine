package state.regex

class EmailAddressDetector : ContextDetector() {
    private val startState = StartState()
    private val part1State = Part1State()
    private val needPart2State = NeedPart2State()
    private val part2State = Part2State()
    private val needPart3State = NeedPart3State()
    private val part3State = Part3State()

    override fun initialState(): State = startState

    private fun isPart1Char(token: String): Boolean {
        return token != "@" && !TokenRules.isSpace(token)
    }

    private fun isPart2Or3Char(token: String): Boolean {
        return token != "@" && token != "." && !TokenRules.isSpace(token)
    }

    private inner class StartState : State {
        override fun handle(context: ContextDetector, token: String) {
            if (isPart1Char(token)) {
                context.transitionTo(part1State)
            } else {
                context.reject()
            }
        }

        override fun onEnd(context: ContextDetector) {
            context.reject()
        }
    }

    private inner class Part1State : State {
        override fun handle(context: ContextDetector, token: String) {
            when {
                token == "@" -> context.transitionTo(needPart2State)
                isPart1Char(token) -> Unit
                else -> context.reject()
            }
        }

        override fun onEnd(context: ContextDetector) {
            context.reject()
        }
    }

    private inner class NeedPart2State : State {
        override fun handle(context: ContextDetector, token: String) {
            if (isPart2Or3Char(token)) {
                context.transitionTo(part2State)
            } else {
                context.reject()
            }
        }

        override fun onEnd(context: ContextDetector) {
            context.reject()
        }
    }

    private inner class Part2State : State {
        override fun handle(context: ContextDetector, token: String) {
            when {
                token == "." -> context.transitionTo(needPart3State)
                isPart2Or3Char(token) -> Unit
                else -> context.reject()
            }
        }

        override fun onEnd(context: ContextDetector) {
            context.reject()
        }
    }

    private inner class NeedPart3State : State {
        override fun handle(context: ContextDetector, token: String) {
            if (isPart2Or3Char(token)) {
                context.transitionTo(part3State)
            } else {
                context.reject()
            }
        }

        override fun onEnd(context: ContextDetector) {
            context.reject()
        }
    }

    private inner class Part3State : State {
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
