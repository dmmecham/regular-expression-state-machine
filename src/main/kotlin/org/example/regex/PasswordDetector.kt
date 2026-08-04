package state.regex

class PasswordDetector : ContextDetector() {
    private val scanState = ScanState()

    private var seenUpper = false
    private var seenSpecial = false
    private var lastWasSpecial = false
    private var length = 0

    override fun initialState(): State = scanState

    private abstract inner class PasswordState : State

    override fun resetForInput() {
        seenUpper = false
        seenSpecial = false
        lastWasSpecial = false
        length = 0
    }

    private inner class ScanState : PasswordState() {
        override fun handle(context: ContextDetector, token: String) {
            length += 1

            if (TokenRules.isUppercase(token)) {
                seenUpper = true
            }

            if (TokenRules.isSpecial(token)) {
                seenSpecial = true
                lastWasSpecial = true
            } else {
                lastWasSpecial = false
            }
        }

        override fun onEnd(context: ContextDetector) {
            val valid = length >= 8 && seenUpper && seenSpecial && !lastWasSpecial
            if (valid) {
                context.accept()
            } else {
                context.reject()
            }
        }
    }
}
