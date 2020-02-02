package com.deniskrr.exam.ui

import androidx.compose.state
import androidx.ui.material.Tab
import androidx.ui.material.TabRow

enum class Tabs(val displayName: String) {
    MY_SECTION("My Section"), ALL_SECTION("All Section"), REPORTS("Reports")
}

fun TabBar() {
    var tab by state { Tabs.MY_SECTION }
    val tabTitles = Tabs.values().map { it.displayName }

    TabRow(
        items = tabTitles, selectedIndex = tab.ordinal
    ) { index, text ->
        Tab(
            text = text,
            selected = tab.ordinal == index
        )
        {
            tab = Tabs.values()[index]
        }
    }
    when (tab) {
        Tabs.MY_SECTION -> navigateTo(Screen.MySection)
        Tabs.ALL_SECTION -> navigateTo(Screen.AllSection)
        Tabs.REPORTS -> navigateTo(Screen.Reports)
    }
}