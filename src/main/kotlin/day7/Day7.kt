package day7

import Util
import java.io.File

fun main(args: Array<String>) {
    val fileFromClasspath = Util.getFileFromClasspath("Day7.txt")

    val treeRoot = TreeBuilder(fileFromClasspath).tree
    println("A: ${treeRoot.name}")

    val (incorrectNode, weightAdjust) = treeRoot.findUnbalancedNode()
    println("B: ${incorrectNode!!.weight + weightAdjust}")
}

val leafNodeRegex = """([a-z]+) \((\d+)\)""".toRegex()
val childNodeRegex = " ([a-z]+)(?:,|$)".toRegex()

fun buildTree(file: File): Node {
    return TreeBuilder(file).tree
}

class TreeBuilder(file: File) {
    private val nonLeafs = mutableMapOf<String, String>()
    private val nodes: MutableMap<String, Node> = mutableMapOf()

    val tree: Node

    private fun getChild(name: String): Node {
        return if (nodes.contains(name)) {
            nodes.getAndRemove(name)!!
        } else {
            val string = nonLeafs.getAndRemove(name)
            buildNonLeafNode(string!!)
        }
    }

    private fun buildNonLeafNode(nodeRepresentation: String): Node {
        val (name, weight) = leafNodeRegex.find(nodeRepresentation)!!.destructured
        val children = childNodeRegex.findAll(nodeRepresentation)
                .map { it.destructured }
                .map { (childName) -> getChild(childName) }
                .toList()

        return Node(name, weight.toInt(), children)
    }

    init {
        initMaps(file)
        buildTree()

        if (nodes.size > 1) {
            throw Exception("invalid Tree")
        }

        tree = nodes.iterator().next().value
    }

    private fun buildTree() {
        while (!nonLeafs.isEmpty()) {
            val nonLeaf = nonLeafs.entries.elementAt(0)
            nonLeafs.remove(nonLeaf.key)

            val newNode = buildNonLeafNode(nonLeaf.value)
            nodes.put(newNode.name, newNode)
        }
    }

    private fun initMaps(file: File) {
        file.forEachLine {
            if (it.matches(leafNodeRegex)) {
                val (name, weight) = leafNodeRegex.matchEntire(it)!!.destructured
                val node = Node(name, weight.toInt())
                nodes.put(node.name, node)
            } else {
                val (name, _) = leafNodeRegex.find(it)!!.destructured
                nonLeafs.put(name, it)
            }
        }
    }
}

data class Node(val name: String, val weight: Int, private val children: List<Node>) {
    constructor(name: String, weight: Int) : this(name, weight, emptyList())

    private val childWeights: List<Int> = children.map { it.totalWeight }

    private val totalWeight: Int = childWeights.sum() + weight

    private val isBalanced: Boolean = children.isEmpty() || childWeights.distinct().size == 1
    
    fun findUnbalancedNode(): Pair<Node?, Int> {
        val unbalancedChild = children.find { !it.isBalanced }

        return when {
            isBalanced -> Pair(null, 0)
            unbalancedChild != null -> unbalancedChild.findUnbalancedNode()
            else -> {
                val childrenByWeight = children.groupBy { it.totalWeight }.map { it.value }

                val incorrectChild = childrenByWeight.first { it.size == 1 }.first()
                val correctChild = childrenByWeight.first { it.size != 1 }.first()

                Pair(incorrectChild, correctChild.totalWeight - incorrectChild.totalWeight)
            }
        }
    }
}

fun <K, V> MutableMap<K, V>.getAndRemove(key: K): V? {
    val value = this[key]
    remove(key)
    return value
}
