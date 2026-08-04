package state.regex

abstract class ContextDetector : Detector {
    private var tokens: List<String> = emptyList()
    private var index: Int = 0
    private var terminal: Boolean = false

    private var accepted: Boolean = false
    protected var currentState: State = initialState()

    protected abstract fun initialState(): State

    protected open fun endOfInput() {
        currentState.onEnd(this)
    }

    override fun detect(input: String): Boolean {
        tokens = splitToSingleCharacterTokens(input)
        index = 0
        terminal = false
        accepted = false
        currentState = initialState()
        resetForInput()

        while (!terminal) {
            val token = nextToken()
            if (token == null) {
                break
            }
            currentState.handle(this, token)
        }

        if (!terminal) {
            endOfInput()
        }

        return terminal && accepted
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

    protected open fun resetForInput() {
        // Hook for detectors that track additional flags.
    }

    private fun splitToSingleCharacterTokens(input: String): List<String> {
        return input.indices.map { i -> input.substring(i, i + 1) }
    }
}
