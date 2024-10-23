package com.amberj.node

import com.amberj.ast.ExpressionNode
import com.amberj.ast.StatementNode

data class AssignmentNode(
    val name: String,
    val value: ExpressionNode
): StatementNode
