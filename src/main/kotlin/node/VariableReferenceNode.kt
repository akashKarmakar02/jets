package com.amberj.node

import com.amberj.ast.ExpressionNode

data class VariableReferenceNode(val name: String): ExpressionNode
