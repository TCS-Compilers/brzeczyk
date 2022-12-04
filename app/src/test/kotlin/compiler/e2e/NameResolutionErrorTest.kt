package compiler.e2e

import compiler.common.diagnostics.Diagnostic
import compiler.e2e.common.E2eAsserter.assertErrorOfType
import kotlin.test.Test

class NameResolutionErrorTest {

    @Test
    fun `test undefined variable`() {
        assertErrorOfType(
            """
                    czynność f() {
                        x = 17
                    }
                    
                """,
            Diagnostic.ResolutionError.NameResolutionError.UndefinedVariable::class
        )
    }

    @Test
    fun `test undefined variable (defined later but not assigned)`() {
        assertErrorOfType(
            """
                    czynność f() {
                        x = 17
                        zm x: Liczba
                    }
                    
                """,
            Diagnostic.ResolutionError.NameResolutionError.UndefinedVariable::class
        )
    }

    @Test
    fun `test undefined variable (defined and assigned later)`() {
        assertErrorOfType(
            """
                    czynność f() {
                        x = 17
                        zm x: Liczba = 18
                    }
                    
                """,
            Diagnostic.ResolutionError.NameResolutionError.UndefinedVariable::class
        )
    }

    @Test
    fun `test variable defined in other scope`() {
        assertErrorOfType(
            """
                    czynność f() -> Liczba {
                        {
                            zm x: Liczba = 17
                        }
                        zwróć x
                    }
                    
                """,
            Diagnostic.ResolutionError.NameResolutionError.UndefinedVariable::class
        )
    }

    @Test
    fun `test variable defined in an inner function`() {
        assertErrorOfType(
            """
                    czynność f() -> Liczba {
                        czynność g() {
                            zm x: Liczba = 17
                        }
                        zwróć x
                    }
                    
                """,
            Diagnostic.ResolutionError.NameResolutionError.UndefinedVariable::class
        )
    }

    @Test
    fun `test undefined function`() {
        assertErrorOfType(
            """
                    czynność f() -> Liczba {
                        zwróć g()
                    }
                    
                """,
            Diagnostic.ResolutionError.NameResolutionError.UndefinedFunction::class
        )
    }

    @Test
    fun `test inner function`() {
        assertErrorOfType(
            """
                    czynność f() -> Liczba {
                        czynność g() {
                            czynność h() -> Liczba {
                                zwróć 17
                            }
                        }
                        zwróć h()
                    }
                    
                """,
            Diagnostic.ResolutionError.NameResolutionError.UndefinedFunction::class
        )
    }

    @Test
    fun `test conflicts (variables)`() {
        assertErrorOfType(
            """
                    czynność f() {
                        zm x: Liczba = 17
                        zm x: Liczba = 18
                    }
                    
                """,
            Diagnostic.ResolutionError.NameResolutionError.NameConflict::class
        )
    }

    @Test
    fun `test conflicts (different modifiers variables)`() {
        assertErrorOfType(
            """
                    czynność f() {
                        zm x: Liczba = 17
                        wart x: Liczba = 18
                    }
                    
                """,
            Diagnostic.ResolutionError.NameResolutionError.NameConflict::class
        )
        assertErrorOfType(
            """
                    czynność f() {
                        zm x: Liczba = 17
                        stała x: Liczba = 18
                    }
                    
                """,
            Diagnostic.ResolutionError.NameResolutionError.NameConflict::class
        )
    }

    @Test
    fun `test conflicts (variables of different type)`() {
        assertErrorOfType(
            """
                    czynność f() {
                        zm x: Liczba = 17
                        zm x: Czy = prawda
                    }
                    
                """,
            Diagnostic.ResolutionError.NameResolutionError.NameConflict::class
        )
    }

    @Test
    fun `test conflicts (functions)`() {
        assertErrorOfType(
            """
                    czynność f() {
                        czynność g() -> Liczba {
                            zwróć 17
                        }
                        czynność g() -> Liczba {
                            zwróć 18
                        }
                    }
                    
                """,
            Diagnostic.ResolutionError.NameResolutionError.NameConflict::class
        )
    }

    @Test
    fun `test conflicts (functions with different return types)`() {
        assertErrorOfType(
            """
                    czynność f() -> Liczba {
                        czynność g() -> Czy {
                            zwróć prawda
                        }
                        czynność g() -> Nic {
                            zakończ
                        }
                    }
                    
                """,
            Diagnostic.ResolutionError.NameResolutionError.NameConflict::class
        )
    }

    @Test
    fun `test conflicts (functions with different signatures)`() {
        assertErrorOfType(
            """
                    czynność f() -> Liczba {
                        czynność g(a: Liczba) { }
                        czynność g(a: Czy) { }
                    }
                    
                """,
            Diagnostic.ResolutionError.NameResolutionError.NameConflict::class
        )
    }

    // ----------- Parameters tests ---------------------------------------------

    @Test
    fun `test undefined default value`() {
        assertErrorOfType(
            """
                    czynność f(x: Czy = und) { }
                    
                """,
            Diagnostic.ResolutionError.NameResolutionError.UndefinedVariable::class
        )
    }

    @Test
    fun `test undefined default value (defined inside a function)`() {
        assertErrorOfType(
            """
                    czynność f(x: Liczba = y) {
                        zm y: Liczba = 17
                    }
                    
                """,
            Diagnostic.ResolutionError.NameResolutionError.UndefinedVariable::class
        )
        assertErrorOfType(
            """
                    czynność f(x: Liczba = g()) {
                        czynność g() -> Liczba {
                            zwróć 17
                        }
                    }
                    
                """,
            Diagnostic.ResolutionError.NameResolutionError.UndefinedFunction::class
        )
    }

    @Test
    fun `test undefined default value (defined later)`() {
        assertErrorOfType(
            """
                    czynność f(x: Liczba = y) { }
                    zm y: Liczba = 17
                    
                """,
            Diagnostic.ResolutionError.NameResolutionError.UndefinedVariable::class
        )
        assertErrorOfType(
            """
                    czynność f(x: Liczba = g()) { }
                    czynność g() -> Liczba {
                        zwróć 17
                    }
                    
                """,
            Diagnostic.ResolutionError.NameResolutionError.UndefinedFunction::class
        )
    }

    @Test
    fun `test looping default value`() {
        assertErrorOfType(
            """
                    czynność f(x: Czy = f()) -> Czy {
                        zwróć x
                    }
                    
                """,
            Diagnostic.ResolutionError.NameResolutionError.UndefinedFunction::class
        )
    }

    @Test
    fun `test parameters name conflicts`() {
        assertErrorOfType(
            """
                    czynność f(x: Liczba, x: Czy) { }
                    
                """,
            Diagnostic.ResolutionError.NameResolutionError.NameConflict::class
        )
    }

    @Test
    fun `test parameters with variables conflicts`() {
        assertErrorOfType(
            """
                    czynność f(x: Liczba) {
                        zm x: Czy = 17
                    }
                    
                """,
            Diagnostic.ResolutionError.NameResolutionError.NameConflict::class
        )
    }

    @Test
    fun `test parameters with functions conflicts`() {
        assertErrorOfType(
            """
                    czynność f(g: Liczba) {
                        czynność g() { }
                    }
                    
                """,
            Diagnostic.ResolutionError.NameResolutionError.NameConflict::class
        )
    }

    @Test
    fun `test parameters used outside a function`() {
        assertErrorOfType(
            """
                    czynność f() -> Liczba {
                        czynność g(x: Liczba) { }
                        zwróć x
                    }
                    
                """,
            Diagnostic.ResolutionError.NameResolutionError.UndefinedVariable::class
        )
    }

    // ----------Assuming no functional features--------------------------------------------------

    @Test
    fun `test calling variables`() {
        assertErrorOfType(
            """
                    czynność f() -> Czy {
                        zm x: Czy = prawda
                        zwróć x()
                    }
                    
                """,
            Diagnostic.ResolutionError.NameResolutionError.VariableIsNotCallable::class
        )
    }

    @Test
    fun `test using functions in assignment`() {
        assertErrorOfType(
            """
                    czynność f() {
                        czynność g() { }
                        zm x: Liczba = g
                    }
                    
                """,
            Diagnostic.ResolutionError.NameResolutionError.FunctionIsNotVariable::class
        )
    }

    @Test
    fun `test using functions in conditions`() {
        assertErrorOfType(
            """
                    czynność f() -> Czy {
                        czynność g() { }
                        zwróć (g == 17)
                    }
                    
                """,
            Diagnostic.ResolutionError.NameResolutionError.FunctionIsNotVariable::class
        )
    }

    @Test
    fun `test calling a parameter`() {
        assertErrorOfType(
            """
                    czynność f(x: Liczba) -> Liczba {
                        zwróć x()
                    }
                    
                """,
            Diagnostic.ResolutionError.NameResolutionError.VariableIsNotCallable::class
        )
    }
}
