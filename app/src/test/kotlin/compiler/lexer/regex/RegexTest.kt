/*
 * This Kotlin source file was generated by the Gradle 'init' task.
 */
package compiler.lexer.regex

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

private val ATOMIC_AB = Regex.Atomic(setOf('a', 'b'))
private val ATOMIC_AC = Regex.Atomic(setOf('a', 'c'))
private val EMPTY = Regex.Empty()
private val EPSILON = Regex.Epsilon()
private val CONCAT_EP_EM = Regex.Concat(EPSILON, EMPTY)
private val CONCAT_EM_EP = Regex.Concat(EMPTY, EPSILON)
private val STAR_SIMPLE = Regex.Star(ATOMIC_AB)
private val UNION_EP_EM = Regex.Union(EPSILON, EMPTY)
private val UNION_EM_EP = Regex.Union(EMPTY, EPSILON)

class RegexTest {
    @Test fun testEmptyDoesNotContainEpsilon() {
        val reg = Regex.Empty()
        assertFalse(reg.containsEpsilon())
    }

    @Test fun testEpsilonContainsEpsilon() {
        val reg = Regex.Epsilon()
        assertTrue(reg.containsEpsilon())
    }

    @Test fun testAtomicDoesNotContainEpsilon() {
        val reg = Regex.Atomic(setOf('a', 'b', 'c'))
        assertFalse(reg.containsEpsilon())
    }

    @Test fun testStarContainsEpsilon() {
        val reg = Regex.Star(Regex.Empty())
        assertTrue(reg.containsEpsilon())
    }

    @Test fun testUnionsWithEpsilon() {
        val reg1 = Regex.Union(
            Regex.Epsilon(),
            Regex.Atomic(setOf('d', 'e', 'f')),
        )
        val reg2 = Regex.Union(
            Regex.Atomic(setOf('d', 'e', 'f')),
            Regex.Star(Regex.Empty()),
        )

        assertTrue(reg1.containsEpsilon())
        assertTrue(reg2.containsEpsilon())
    }

    @Test fun testUnionsWithNoEpsilon() {
        val reg1 = Regex.Union(
            Regex.Atomic(setOf('a', 'b', 'c')),
            Regex.Atomic(setOf('d', 'e', 'f')),
        )
        val reg2 = Regex.Union(
            Regex.Concat(Regex.Epsilon(), Regex.Atomic(setOf('x'))),
            Regex.Concat(Regex.Atomic(setOf('y')), Regex.Star(Regex.Empty())),
        )

        assertFalse(reg1.containsEpsilon())
        assertFalse(reg2.containsEpsilon())
    }

    @Test fun testConcatsWithEpsilon() {
        val reg1 = Regex.Concat(
            Regex.Epsilon(),
            Regex.Epsilon(),
        )
        val reg2 = Regex.Concat(
            Regex.Star(Regex.Atomic(setOf('q'))),
            Regex.Union(Regex.Epsilon(), Regex.Atomic(setOf('w')))
        )

        assertTrue(reg1.containsEpsilon())
        assertTrue(reg2.containsEpsilon())
    }

    @Test fun testConcatsWithNoEpsilon() {
        val reg1 = Regex.Concat(
            Regex.Epsilon(),
            Regex.Atomic(setOf('d', 'e', 'f')),
        )
        val reg2 = Regex.Concat(
            Regex.Empty(),
            Regex.Star(Regex.Empty()),
        )

        assertFalse(reg1.containsEpsilon())
        assertFalse(reg2.containsEpsilon())
    }

    @Test fun testDerivativeOfEmptyIsEmpty() {
        val reg = RegexFactory.createEmpty()
        assertTrue(reg.derivative('a') is Regex.Empty)
    }

    @Test fun testDerivativeOfEpsilonIsEmpty() {
        val reg = RegexFactory.createEpsilon()
        assertTrue(reg.derivative('a') is Regex.Empty)
    }

    @Test fun testDerivativeOfAtomicWithProperAtomIsEpsilon() {
        val reg = RegexFactory.createAtomic(setOf('a'))
        assertTrue(reg.derivative('a') is Regex.Epsilon)
    }

    @Test fun testDerivativeOfAtomicWithNoProperAtomIsEmpty() {
        val reg = RegexFactory.createAtomic(setOf('a'))
        assertTrue(reg.derivative('b') is Regex.Empty)
    }

    @Test fun testEqualsDifferentKind() {
        assertNotEquals<Regex>(ATOMIC_AB, CONCAT_EP_EM)
        assertNotEquals<Regex>(CONCAT_EP_EM, EMPTY)
        assertNotEquals<Regex>(EMPTY, EPSILON)
        assertNotEquals<Regex>(EPSILON, UNION_EP_EM)
        assertNotEquals<Regex>(UNION_EP_EM, STAR_SIMPLE)
    }

    @Test fun testEqualsSameKind() {
        assertEquals<Regex>(EMPTY, Regex.Empty())

        assertEquals<Regex>(EPSILON, Regex.Epsilon())

        assertEquals<Regex>(ATOMIC_AB, Regex.Atomic(setOf('a', 'b')))
        assertNotEquals<Regex>(ATOMIC_AB, ATOMIC_AC)

        assertEquals<Regex>(STAR_SIMPLE, Regex.Star(ATOMIC_AB))
        assertNotEquals<Regex>(STAR_SIMPLE, Regex.Star(EMPTY))

        assertEquals<Regex>(UNION_EP_EM, Regex.Union(EPSILON, EMPTY))
        assertNotEquals<Regex>(UNION_EP_EM, UNION_EM_EP)

        assertEquals<Regex>(CONCAT_EP_EM, Regex.Concat(EPSILON, EMPTY))
        assertNotEquals<Regex>(CONCAT_EP_EM, CONCAT_EM_EP)
    }

    @Test fun testOrderTypeAlphabetical() {
        assertTrue(ATOMIC_AB < CONCAT_EP_EM)
        assertTrue(CONCAT_EP_EM < EMPTY)
        assertTrue(EMPTY < EPSILON)
        assertTrue(EPSILON < STAR_SIMPLE)
        assertTrue(STAR_SIMPLE < UNION_EP_EM)

        assertFalse(ATOMIC_AB > CONCAT_EP_EM)
        assertFalse(CONCAT_EP_EM > EMPTY)
        assertFalse(EMPTY > EPSILON)
        assertFalse(EPSILON > STAR_SIMPLE)
        assertFalse(STAR_SIMPLE > UNION_EP_EM)
    }

    @Test fun testOrderSameType() {
        assertTrue(Regex.Atomic(setOf('a')) < ATOMIC_AC)
        assertTrue(ATOMIC_AC > Regex.Atomic(setOf('a')))
        assertTrue(ATOMIC_AB < ATOMIC_AC)
        assertTrue(ATOMIC_AC > ATOMIC_AB)

        assertFalse(EMPTY < Regex.Empty())
        assertFalse(EMPTY > Regex.Empty())

        assertFalse(EPSILON < Regex.Epsilon())
        assertFalse(EPSILON > Regex.Epsilon())

        assertTrue(CONCAT_EM_EP < CONCAT_EP_EM)
        assertTrue(CONCAT_EP_EM > CONCAT_EM_EP)
        assertTrue(Regex.Concat(EPSILON, ATOMIC_AB) < CONCAT_EP_EM)
        assertTrue(CONCAT_EP_EM > Regex.Concat(EPSILON, ATOMIC_AB))
        assertTrue(Regex.Concat(EPSILON, ATOMIC_AB) < Regex.Concat(EPSILON, ATOMIC_AC))
        assertTrue(Regex.Concat(EPSILON, ATOMIC_AC) > Regex.Concat(EPSILON, ATOMIC_AB))

        assertTrue(STAR_SIMPLE < Regex.Star(EPSILON))
        assertTrue(Regex.Star(EPSILON) > STAR_SIMPLE)
        assertTrue(STAR_SIMPLE < Regex.Star(ATOMIC_AC))
        assertTrue(Regex.Star(ATOMIC_AC) > STAR_SIMPLE)

        assertTrue(UNION_EM_EP < UNION_EP_EM)
        assertTrue(UNION_EP_EM > UNION_EM_EP)
        assertTrue(Regex.Union(EPSILON, ATOMIC_AB) < UNION_EP_EM)
        assertTrue(UNION_EP_EM > Regex.Union(EPSILON, ATOMIC_AB))
        assertTrue(Regex.Union(EPSILON, ATOMIC_AB) < Regex.Union(EPSILON, ATOMIC_AC))
        assertTrue(Regex.Union(EPSILON, ATOMIC_AC) > Regex.Union(EPSILON, ATOMIC_AB))
    }
}
