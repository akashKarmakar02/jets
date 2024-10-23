package com.amberj

val keywords = setOf("const", "let", "var", "console", "log")
val operators = setOf("+", "-", "=", "*", "/")
val punctuation = setOf("(", ")", ";", ".")


class Lexer(private val input: String) {
    private var position = 0


    fun nextToken(): Token {
        skipWhitespace()

        if (position >= input.length) {
            return Token(TokenType.EOF, "")
        }

        if (isLetter(input[position])) {
            val start = position
            while (position < input.length && isLetterOrDigit(input[position])) {
                position++
            }
            val value = input.substring(start, position)
            return if (value in keywords) {
                Token(TokenType.KEYWORD, value)
            } else {
                Token(TokenType.IDENTIFIER, value)
            }
        }

        // Check for numbers (literals)
        if (isDigit(input[position])) {
            val start = position
            while (position < input.length && isDigit(input[position])) {
                position++
            }
            val value = input.substring(start, position)
            return Token(TokenType.LITERAL, value)
        }

        // Check for operators
        if (operators.contains("${input[position]}")) {
            val operator = input[position].toString()
            position++
            return Token(TokenType.OPERATOR, operator)
        }

        // Check for punctuation
        if (punctuation.contains("${input[position]}")) {
            val punct = input[position].toString()
            position++
            return Token(TokenType.PUNCTUATION, punct)
        }

        throw IllegalArgumentException("Unexpected character: ${input[position]}")
    }

    // Helper functions to check characters
    private fun isLetter(c: Char) = c.isLetter()
    private fun isLetterOrDigit(c: Char) = c.isLetterOrDigit()
    private fun isDigit(c: Char) = c.isDigit()

    // Skips over any whitespace (spaces, tabs, newlines)
    private fun skipWhitespace() {
        while (position < input.length && input[position].isWhitespace()) {
            position++
        }
    }
}