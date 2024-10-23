package com.amberj.node

import com.amberj.ast.ExpressionNode
import com.amberj.ast.StatementNode

data class VariableDeclarationNode(
    val name: String,
    val initializer: ExpressionNode,
    val isConstant: Boolean
): StatementNode
