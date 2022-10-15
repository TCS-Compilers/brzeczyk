package compiler.lexer.lexer_grammar

enum class TokenType {
    // Parenthesis and braces
    LEFT_PAREN, RIGHT_PAREN,
    LEFT_BRACE, RIGHT_BRACE,
    // Variable types
    VARIABLE, VALUE, CONSTANT,
    // Control flow
    IF, ELSE_IF, ELSE,
    WHILE, BREAK, CONTINUE,
    // Function related keywords
    RETURN, RETURN_UNIT, FUNCTION,
    // Special characters
    COLON, SEMICOLON, QUESTION_MARK, COMMA, NEWLINE,
    // Arithmetic operators
    PLUS, MINUS, MULTIPLY, DIVIDE, MODULO,
    // Increment and decrement operators
    INCREMENT, DECREMENT,
    // Bitwise operators
    BIT_NOT, BIT_OR, BIT_AND, BIT_XOR, SHIFT_LEFT, SHIFT_RIGHT,
    // Comparison operators
    EQUAL, NOT_EQUAL, LESS_THAN, LESS_THAN_EQ, GREATER_THAN, GREATER_THAN_EQ,
    // Assignment operator
    ASSIGNMENT,
    // Logical operators
    NOT, OR, AND,
    // Boolean constants
    TRUE_CONSTANT, FALSE_CONSTANT,
    // Integers
    INTEGER,
    // Identifiers
    IDENTIFIER, TYPE_IDENTIFIER,
    // Whitespace and comments
    // Technically not real token
    // Should whitespace and comments be handled this way
    // or should they be filtered out in another way?
    WHITESPACE,
}