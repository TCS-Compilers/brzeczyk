package compiler.e2e

import compiler.common.diagnostics.Diagnostic
import compiler.lexer.Location
import kotlin.test.Test

class LexicallyIncorrectPrograms {
    @Test
    fun `test identifier cannot contain special characters`() {
        E2eAsserter.assertProgramGeneratesDiagnostics(
            """
            zm test$$: Liczba = 123
            """,
            listOf(Diagnostic.LexerError(Location(1, 8), Location(1, 10), listOf("zm", " ", "test"), "$$"))
        )
        E2eAsserter.assertProgramGeneratesDiagnostics(
            """
            zm liczbaa: Liczba = 123
            zm licz#: Liczba = 123
            """,
            listOf(Diagnostic.LexerError(Location(2, 8), Location(2, 9), listOf("zm", " ", "licz"), "#"))
        )
    }

    @Test
    fun `test wpp cannot end with a dot`() {
        E2eAsserter.assertProgramGeneratesDiagnostics(
            """
            zm x: Liczba
            zm y: Liczba = 10
            jeśli (fałsz) {
                x = 1
            }
            zaś gdy (y == 10) {
                x = 2
            }
            wpp. {
                x = 3
            }
            napisz(x) //wypisze 2
            jeśli(x == 2) {
                napisz(14)
            }
            """,
            listOf(Diagnostic.LexerError(Location(9, 4), Location(9, 5), listOf("}", "\n", "wpp"), "."))
        )
    }

    /*       _\|/_
             (o o)
     +----oOO-{_}-OOo----------------------------------------------------------------------------------+
     |              In the remaining tests, we assume that there are no type identifiers.              |
     |             When we introduce type identifiers, they will have to be removed/moved.             |
     +------------------------------------------------------------------------------------------------*/

    @Test
    fun `test boolean values cannot start with capital letters`() {
        E2eAsserter.assertProgramGeneratesDiagnostics(
            """
            zm b: Czy = prawda
            b = fałsz
            b = Fałsz
            """,
            listOf(Diagnostic.LexerError(Location(3, 5), Location(3, 6), listOf(" ", "=", " "), "F"))
        )
    }

    @Test
    fun `test custom type identifiers are not allowed`() {
        E2eAsserter.assertProgramGeneratesDiagnostics(
            """
            zm x: Liczba
            zm y: Number
            """,
            listOf(Diagnostic.LexerError(Location(2, 7), Location(2, 8), listOf("y", ":", " "), "N"))
        )
    }
}
