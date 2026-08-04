package state.regex

abstract class ContextDetector : Detector {
    private var tokens: List<String> = emptyList()
    private var index: Int = 0
    private var terminal: Boolean = false

    private var accepted: Boolean = false
    protected var currentState: State = createInitialState()

    protected abstract fun createInitialState(): State

    protected open fun beforeDetection() {
    }

    protected open fun afterInputConsumed() {
        currentState.onEnd(this)
    }

    override fun detect(input: String): Boolean {
        prepareDetection(input)
        runDetectionLoop()

        if (!terminal) {
            afterInputConsumed()
        }

        return terminal && accepted
    }

    private fun prepareDetection(input: String) {
        tokens = splitToSingleCharacterTokens(input)
        index = 0
        terminal = false
        accepted = false
        currentState = createInitialState()
        beforeDetection()
    }

    private fun runDetectionLoop() {
        while (!terminal) {
            val token = nextToken()
            if (token == null) {
                break
            }
            currentState.handle(this, token)
        }
    }

    fun nextToken(): String? {
        if (index >= tokens.size) {
            return null
        }

        val token = tokens[index]
        index += 1
        return token
    }

    fun transitionTo(state: State) {
        currentState = state
    }

    fun reject() {
        accepted = false
        terminal = true
    }

    fun accept() {
        accepted = true
        terminal = true
    }

    private fun splitToSingleCharacterTokens(input: String): List<String> {
        return input.indices.map { i -> input.substring(i, i + 1) }
    }
}
