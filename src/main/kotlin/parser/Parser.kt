package com.amberj.parser

import com.amberj.Token
import com.amberj.TokenType
import com.amberj.ast.ExpressionNode
import com.amberj.ast.StatementNode
import com.amberj.node.*

class Parser(private val tokens: List<Token>) {
    private var position = 0

    // Helper to get the current token
    private fun currentToken(): Token = tokens[position]

    // Helper to advance to the next token
    private fun advance() {
        if (position < tokens.size) {
            position++
        }
    }

    // Main function to parse a list of statements
    fun parse(): List<StatementNode> {
        val statements = mutableListOf<StatementNode>()
        while (currentToken().type != TokenType.EOF) {
            statements.add(parseStatement())
        }
        return statements
    }

    // Parse a single statement
    private fun parseStatement(): StatementNode {
        return when (val token = currentToken()) {
            Token(TokenType.KEYWORD, "const") -> parseVariableDeclaration(isConstant = true)
            Token(TokenType.KEYWORD, "let"), Token(TokenType.KEYWORD, "var") -> parseVariableDeclaration(isConstant = false)
            Token(TokenType.KEYWORD, "console") -> parseConsoleLog()
            else -> throw IllegalArgumentException("Unexpected token: ${token.value}")
        }
    }

    // Parse a variable declaration (const, let, var)
    private fun parseVariableDeclaration(isConstant: Boolean): VariableDeclarationNode {
        advance()  // Skip the 'const', 'let', or 'var' keyword
        val name = currentToken().value
        advance()  // Skip the identifier
        expect("=")  // Expect '='
        val initializer = parseExpression()
        expect(";")  // Expect ';'
        return VariableDeclarationNode(name, initializer, isConstant)
    }

    // Parse console.log() statement
    private fun parseConsoleLog(): ConsoleLogNode {
        advance()  // Skip 'console'
        expect(".")
        expect("log")
        expect("(")
        val argument = parseExpression()  // Parse the argument inside console.log()
        expect(")")
        expect(";")
        return ConsoleLogNode(argument)
    }

    // Parse an expression (for now, we'll only handle literals and binary expressions)
    private fun parseExpression(): ExpressionNode {
        val left = parsePrimary()  // Parse the left-hand side (literal or variable)

        // Check if there's a binary operator
        if (currentToken().type == TokenType.OPERATOR) {
            val operator = currentToken().value
            advance()  // Skip the operator
            val right = parsePrimary()  // Parse the right-hand side
            return BinaryOperationNode(left, operator, right)
        }

        return left  // If no operator, return the left-hand side as the expression
    }

    // Parse primary expressions (literals or variables)
    private fun parsePrimary(): ExpressionNode {
        return when (val token = currentToken()) {
            Token(TokenType.LITERAL, token.value) -> {  // TokenType.LITERAL
                advance()
                LiteralNode(token.value)
            }
            Token(TokenType.IDENTIFIER, token.value) -> {  // TokenType.IDENTIFIER
                advance()
                VariableReferenceNode(token.value)
            }
            else -> throw IllegalArgumentException("Unexpected token: ${token.value}")
        }
    }

    // Helper to expect a specific token and throw an error if it's not found
    private fun expect(expected: String) {
        if (currentToken().value != expected) {
            throw IllegalArgumentException("Expected $expected but found ${currentToken().value}")
        }
        advance()
    }
}
