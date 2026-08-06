package state.regex

interface State {
    fun handle(context: ContextDetector, token: String)
    fun onEnd(context: ContextDetector)
}
