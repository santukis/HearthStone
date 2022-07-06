package com.santukis.viewmodels.entities

import androidx.compose.ui.graphics.Color
import com.santukis.entities.hearthstone.*
import com.santukis.viewmodels.entities.CardFilterState.Companion.CARD_CLASS
import com.santukis.viewmodels.entities.CardFilterState.Companion.CARD_SET
import com.santukis.viewmodels.entities.CardFilterState.Companion.CARD_TYPE
import com.santukis.viewmodels.entities.CardFilterState.Companion.GAME_MODE
import com.santukis.viewmodels.entities.CardFilterState.Companion.KEYWORD
import com.santukis.viewmodels.entities.CardFilterState.Companion.MINION_TYPE
import com.santukis.viewmodels.entities.CardFilterState.Companion.RARITY
import com.santukis.viewmodels.entities.CardFilterState.Companion.SPELL_SCHOOL

interface CardFilter<Item> {

    fun getId(): Int

    fun getFilter(): Item

    fun getName(): String = ""

    fun getSelectedColor(): Color = Color.Unspecified

    fun getUnselectedColor(): Color = Color.Unspecified
}

fun CardSet.asCardFilter(): CardFilter<CardSet> =
    object : CardFilter<CardSet> {
        override fun getId(): Int = identity.id

        override fun getFilter(): CardSet = this@asCardFilter

        override fun getName(): String = identity.name

        override fun getSelectedColor(): Color = Color.Red
    }

fun GameMode.asCardFilter(): CardFilter<GameMode> =
    object : CardFilter<GameMode> {
        override fun getId(): Int = identity.id

        override fun getFilter(): GameMode = this@asCardFilter

        override fun getName(): String =
            identity.name
    }

fun CardType.asCardFilter(): CardFilter<CardType> =
    object : CardFilter<CardType> {
        override fun getId(): Int = identity.id

        override fun getFilter(): CardType = this@asCardFilter

        override fun getName(): String =
            identity.name
    }

fun Rarity.asCardFilter(): CardFilter<Rarity> =
    object : CardFilter<Rarity> {
        override fun getId(): Int = identity.id

        override fun getFilter(): Rarity = this@asCardFilter

        override fun getName(): String =
            identity.name
    }

fun CardClass.asCardFilter(): CardFilter<CardClass> =
    object : CardFilter<CardClass> {
        override fun getId(): Int = identity.id

        override fun getFilter(): CardClass = this@asCardFilter

        override fun getName(): String =
            identity.name
    }

fun MinionType.asCardFilter(): CardFilter<MinionType> =
    object : CardFilter<MinionType> {
        override fun getId(): Int = identity.id

        override fun getFilter(): MinionType = this@asCardFilter

        override fun getName(): String =
            identity.name
    }

fun SpellSchool.asCardFilter(): CardFilter<SpellSchool> =
    object : CardFilter<SpellSchool> {
        override fun getId(): Int = identity.id

        override fun getFilter(): SpellSchool = this@asCardFilter

        override fun getName(): String =
            identity.name
    }

fun Keyword.asCardFilter(): CardFilter<Keyword> =
    object : CardFilter<Keyword> {
        override fun getId(): Int = identity.id

        override fun getFilter(): Keyword = this@asCardFilter

        override fun getName(): String =
            identity.name
    }

fun Metadata.asCardFilters(): Map<Int, List<CardFilter<*>>> {
    val cardFilters = mutableMapOf<Int, List<CardFilter<*>>>()

    sets.map { it.asCardFilter() }.apply { cardFilters[CARD_SET] = this }
    gameModes.map { it.asCardFilter() }.apply { cardFilters[GAME_MODE] = this }
    types.map { it.asCardFilter() }.apply { cardFilters[CARD_TYPE] = this }
    rarities.map { it.asCardFilter() }.apply { cardFilters[RARITY] = this }
    classes.map { it.asCardFilter() }.apply { cardFilters[CARD_CLASS] = this }
    minionTypes.map { it.asCardFilter() }.apply { cardFilters[MINION_TYPE] = this }
    spellSchools.map { it.asCardFilter() }.apply { cardFilters[SPELL_SCHOOL] = this }
    keywords.map { it.asCardFilter() }.apply { cardFilters[KEYWORD] = this }

    return cardFilters
}