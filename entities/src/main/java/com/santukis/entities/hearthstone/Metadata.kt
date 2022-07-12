package com.santukis.entities.hearthstone

data class Metadata(
    val sets: List<CardSet> = emptyList(),
    val gameModes: List<GameMode> = emptyList(),
    val arenaIds: List<Int> = emptyList(),
    val types: List<CardType> = emptyList(),
    val rarities: List<Rarity> = emptyList(),
    val classes: List<CardClass> = emptyList(),
    val minionTypes: List<MinionType> = emptyList(),
    val spellSchools: List<SpellSchool> = emptyList(),
    val keywords: List<Keyword> = emptyList()
) {
    fun isEmpty(): Boolean = sets.isEmpty()
}