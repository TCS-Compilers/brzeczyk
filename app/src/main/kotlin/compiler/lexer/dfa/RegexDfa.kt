package compiler.lexer.dfa

import compiler.lexer.dfa.state_dfa.DfaState
import compiler.lexer.dfa.state_dfa.DfaWithStates
import compiler.lexer.dfa.state_dfa.PlainDfaStateType
import compiler.lexer.regex.Regex
import compiler.lexer.regex.RegexFactory

// TODO: replace Char with A and NormalDfaStateType with R
class RegexDfa(private val regex: Regex<Char>) : DfaWithStates<Char, PlainDfaStateType> {
    override val startState: DfaState<Char, PlainDfaStateType>
        get() = TODO("Not yet implemented")

    override fun newWalk(): DfaWalk<Char, PlainDfaStateType> {
        return object : DfaWalk<Char, PlainDfaStateType> {
            var currentStateRegex = regex
            override fun getResult(): PlainDfaStateType {
                return if (currentStateRegex.containsEpsilon()) PlainDfaStateType.ACCEPTING
                else PlainDfaStateType.NON_ACCEPTING
            }

            override fun isDead(): Boolean {
                return currentStateRegex == RegexFactory.createEmpty<Char>()
            }

            override fun step(a: Char) {
                currentStateRegex = currentStateRegex.derivative(a)
            }
        }
    }
}
