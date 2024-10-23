package com.amberj

enum class TokenType {
    KEYWORD,       // const, let, var
    IDENTIFIER,    // variable names like x, y
    OPERATOR,      // +, -, =, etc.
    LITERAL,       // numbers, strings
    PUNCTUATION,   // (, ), ;
    EOF            // End of file
}

data class Token(val type: TokenType, val value: String)