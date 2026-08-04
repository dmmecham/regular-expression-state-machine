package state.regex

interface Detector {
    fun detect(input: String): Boolean
}
