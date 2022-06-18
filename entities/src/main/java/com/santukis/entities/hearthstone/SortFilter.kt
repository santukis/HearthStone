package com.santukis.entities.hearthstone

sealed class SortFilter(val order: Order) {

    class ManaCost(order: Order): SortFilter(order)

    class Attack(order: Order): SortFilter(order)

    class Health(order: Order): SortFilter(order)

    class HeroClass(order: Order): SortFilter(order)

    class GroupByClass(order: Order): SortFilter(order)

    class Name(order: Order): SortFilter(order)
}

enum class Order {
    Ascendent,
    Descendent
}