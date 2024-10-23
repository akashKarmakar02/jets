package com.amberj

import CodeGenerator
import com.amberj.parser.Parser
import java.nio.file.Files
import java.nio.file.Paths

fun main() {
    val code = """
        const x = 10;
        console.log(x + 5);
    """.trimIndent()

    // Generate tokens using the lexer
    val lexer = Lexer(code)
    val tokens = mutableListOf<Token>()
    var token: Token

    do {
        token = lexer.nextToken()
        tokens.add(token)
    } while (token.type != TokenType.EOF)

    // Parse tokens to create the AST
    val parser = Parser(tokens)
    val ast = parser.parse()

    // Generate bytecode using the CodeGenerator
    val codeGenerator = CodeGenerator()
    val bytecode = codeGenerator.generate(ast)

    // Save bytecode to a .class file
    val path = Paths.get("GeneratedClass.class")
    Files.write(path, bytecode)

    // Load and execute the class
    val classLoader = CustomClassLoader()
    val clazz = classLoader.defineClass("GeneratedClass", bytecode)
    val method = clazz.getDeclaredMethod("main", Array<String>::class.java)
    method.invoke(null, arrayOf<String>())
}

// Custom class loader to load generated class
class CustomClassLoader : ClassLoader() {
    fun defineClass(name: String, bytecode: ByteArray): Class<*> {
        return defineClass(name, bytecode, 0, bytecode.size)
    }
}
