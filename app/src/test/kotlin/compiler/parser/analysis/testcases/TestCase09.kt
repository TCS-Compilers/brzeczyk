package compiler.parser.analysis.testcases

import compiler.parser.analysis.GrammarAnalysis
import compiler.parser.analysis.GrammarAnalysisTest.DfaFactory
import compiler.parser.analysis.GrammarSymbol
import compiler.parser.grammar.AutomatonGrammar
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.assertEquals

class TestCase09 {

    // COMPLICATED NULLABLE GRAMMAR (WITH TERMINAL LAST)
    // To test if we can detect p \in FIRST(start) [first after complicated sequence of nullable]
    //
    // Grammar:
    //   Sigma = { start, L, M, N, o, p }
    //   Productions = {
    //      start --> L M M N N N (start) p + N M M L L L (dfaCNG)
    //      L --> eps (trivial dfa)
    //      M --> o* (dfaM)
    //      N --> o + eps (dfaN)
    //   }
    //

    private val start = "start"
    private val symL = "L"
    private val symM = "M"
    private val symN = "N"
    private val symo = "o"
    private val symp = "p"

    private val expectedNullable = setOf(
        start,
        symL,
        symM,
        symN,
    )

    private val expectedFirst = mapOf(
        start to setOf(start, symL, symM, symN, symo, symp),
        symL to setOf(symL),
        symM to setOf(symM, symo),
        symN to setOf(symN, symo),
        symo to setOf(symo),
        symp to setOf(symp),
    )

    private val expectedFollow: Map<GrammarSymbol, Set<GrammarSymbol>> = mapOf(
        start to setOf(),
        symL to setOf(start, symL, symM, symN, symo, symp),
        symM to setOf(start, symL, symM, symN, symo, symp),
        symN to setOf(start, symL, symM, symN, symo, symp),
        symo to setOf(start, symL, symM, symN, symo, symp),
        symp to setOf(symp),
    )

    private val dfaCNG = DfaFactory.createDfa(
        "startState",
        listOf(
            "startState",
            "state11", "state12", "state13", "state14", "state15", "state16", "state17",
            "state21", "state22", "state23", "state24", "state25",
            "accState",
        ),
        mapOf(
            Pair("startState", symL) to "state11",
            Pair("state11", symM) to "state12",
            Pair("state12", symM) to "state13",
            Pair("state13", symN) to "state14",
            Pair("state14", symN) to "state15",
            Pair("state15", symN) to "state16",
            Pair("state16", start) to "state17",
            Pair("state17", symp) to "accState",
            Pair("startState", symN) to "state21",
            Pair("state21", symM) to "state22",
            Pair("state22", symM) to "state23",
            Pair("state23", symL) to "state24",
            Pair("state24", symL) to "state25",
            Pair("state25", symL) to "accState",
        ),
    )

    private val dfaM = DfaFactory.createDfa(
        "accStartState",
        listOf("accStartState"),
        mapOf(
            Pair("accStartState", symo) to "accStartState",
        ),
    )

    private val dfaN = DfaFactory.createDfa(
        "accStartState",
        listOf("accStartState", "accState"),
        mapOf(
            Pair("accStartState", symo) to "accState",
        ),
    )

    private val grammar: AutomatonGrammar<String> = AutomatonGrammar(
        start,
        mapOf(
            start to dfaCNG,
            symL to DfaFactory.getTrivialDfa(),
            symM to dfaM,
            symN to dfaN,
        ),
    )

    @Ignore
    @Test
    fun `test nullable for trivial grammar`() {
        val actualNullable = GrammarAnalysis<GrammarSymbol>().computeNullable(grammar)
        assertEquals(expectedNullable, actualNullable)
    }

    @Ignore
    @Test
    fun `test first for trivial grammar`() {
        val actualFirst = GrammarAnalysis<GrammarSymbol>().computeFirst(grammar, expectedNullable)
        assertEquals(expectedFirst, actualFirst)
    }

    @Ignore
    @Test
    fun `test follow for trivial grammar`() {
        // In fact, the upper approximation of Follow.
        val actualFollow = GrammarAnalysis<GrammarSymbol>().computeFollow(grammar, expectedNullable, expectedFirst)
        assertEquals(expectedFollow, actualFollow)
    }
}
