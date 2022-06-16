package com.santukis.datasources.dataproviders


fun loadData(path: String): String =
    object {}.javaClass.getResource(path)?.readText().orEmpty()