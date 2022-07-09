package com.santukis.entities.hearthstone

sealed class SortFilter(val order: Order) {

    class ManaCost(order: Order): SortFilter(order)

    class Attack(order: Order): SortFilter(order)

    class Health(order: Order): SortFilter(order)

    class HeroClass(order: Order): SortFilter(order)

    class GroupByClass(order: Order): SortFilter(order)

    class Name(order: Order): SortFilter(order)

    override fun toString(): String =
        this::class.simpleName + order.name

    override fun equals(other: Any?): Boolean =
        this.toString() == other?.toString()

    override fun hashCode(): Int = toString().hashCode()
}

enum class Order {
    Ascendent,
    Descendent
}