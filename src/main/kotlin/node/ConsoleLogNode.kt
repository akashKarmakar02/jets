package com.amberj.node

import com.amberj.ast.ExpressionNode
import com.amberj.ast.StatementNode

data class ConsoleLogNode(val argument: ExpressionNode): StatementNode
