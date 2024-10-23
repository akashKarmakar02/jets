package com.amberj.node

import com.amberj.ast.ExpressionNode

data class BinaryOperationNode(
    val left: ExpressionNode,
    val operator: String,
    val right: ExpressionNode
) : ExpressionNode
