package state.regex

object TokenRules {
    private val digits = ('0'..'9').map { it.toString() }.toSet()
    private val nonZeroDigits = ('1'..'9').map { it.toString() }.toSet()
    private val uppercase = ('A'..'Z').map { it.toString() }.toSet()
    private val specials = setOf("!", "@", "#", "$", "%", "&", "*")

    fun isDigit(token: String): Boolean = token in digits

    fun isNonZeroDigit(token: String): Boolean = token in nonZeroDigits

    fun isUppercase(token: String): Boolean = token in uppercase

    fun isSpecial(token: String): Boolean = token in specials

    fun isSpace(token: String): Boolean = token == " "
}
