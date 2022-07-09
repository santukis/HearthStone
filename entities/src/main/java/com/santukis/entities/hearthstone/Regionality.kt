package com.santukis.entities.hearthstone

sealed class Regionality(
    val region: Region,
    val locale: Locale
) {
    class NorteAmerica(locale: NorthAmericaLocale) : Regionality(Region.US(), locale)
    class Europe(locale: EuropeLocale) : Regionality(Region.EU(), locale)
    class Korea : Regionality(Region.KR(), Korean())
    class Taiwan : Regionality(Region.TW(), Mandarin())
    class China : Regionality(Region.CN(), Chinese())

    override fun toString(): String =
        this::class.simpleName + region.value + locale.value

    override fun equals(other: Any?): Boolean =
        this.toString() == other?.toString()

    override fun hashCode(): Int =
        toString().hashCode()
}

sealed class Region(val value: String) {
    class US : Region("us")
    class EU : Region("eu")
    class KR : Region("kr")
    class TW : Region("tw")
    class CN : Region("cn")
}

sealed class Locale(val value: String)

class Korean : Locale("ko_KR")

class Mandarin : Locale("zh_TW")

class Chinese : Locale("zh_CN")

sealed class NorthAmericaLocale(value: String) : Locale(value) {
    class English : NorthAmericaLocale("en_US")
    class Spanish : NorthAmericaLocale("es_MX")
    class Portuguese : NorthAmericaLocale("pt_BR")
}

sealed class EuropeLocale(value: String) : Locale(value) {
    class English : EuropeLocale("en_GB")
    class Spanish : EuropeLocale("es_ES")
    class French : EuropeLocale("fr_FR")
    class Rumain : EuropeLocale("ru_RU")
    class German : EuropeLocale("de_DE")
    class Portuguese : EuropeLocale("pt_PT")
    class Italian : EuropeLocale("it_IT")
}