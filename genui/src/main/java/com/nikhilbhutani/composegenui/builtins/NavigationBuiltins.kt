@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package com.nikhilbhutani.composegenui.builtins

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.nikhilbhutani.composegenui.A2UiEvent
import com.nikhilbhutani.composegenui.A2UiRenderer
import com.nikhilbhutani.composegenui.bool
import com.nikhilbhutani.composegenui.int
import com.nikhilbhutani.composegenui.string
import com.nikhilbhutani.composegenui.toModifier

internal fun navigationBuiltins(): Map<String, A2UiRenderer> = mapOf(
    "topAppBar" to @Composable { node, _, onEvent, renderChild ->
        val title = node.props.string("title") ?: ""
        val navIconName = node.props.string("navIcon")
        TopAppBar(
            title = { Text(title) },
            navigationIcon = {
                if (navIconName != null) {
                    val vector = iconByName(navIconName)
                    if (vector != null) {
                        IconButton(onClick = {
                            val id = node.id ?: "topAppBar"
                            onEvent(A2UiEvent(id, "navClick"))
                        }) {
                            Icon(imageVector = vector, contentDescription = null)
                        }
                    }
                }
            },
            actions = {
                for (child in node.children) {
                    renderChild(child)
                }
            }
        )
    },
    "centerTopAppBar" to @Composable { node, _, onEvent, renderChild ->
        val title = node.props.string("title") ?: ""
        val navIconName = node.props.string("navIcon")
        CenterAlignedTopAppBar(
            title = { Text(title) },
            navigationIcon = {
                if (navIconName != null) {
                    val vector = iconByName(navIconName)
                    if (vector != null) {
                        IconButton(onClick = {
                            val id = node.id ?: "centerTopAppBar"
                            onEvent(A2UiEvent(id, "navClick"))
                        }) {
                            Icon(imageVector = vector, contentDescription = null)
                        }
                    }
                }
            },
            actions = {
                for (child in node.children) {
                    renderChild(child)
                }
            }
        )
    },
    "mediumTopAppBar" to @Composable { node, _, onEvent, renderChild ->
        val title = node.props.string("title") ?: ""
        val navIconName = node.props.string("navIcon")
        MediumTopAppBar(
            title = { Text(title) },
            navigationIcon = {
                if (navIconName != null) {
                    val vector = iconByName(navIconName)
                    if (vector != null) {
                        IconButton(onClick = {
                            val id = node.id ?: "mediumTopAppBar"
                            onEvent(A2UiEvent(id, "navClick"))
                        }) {
                            Icon(imageVector = vector, contentDescription = null)
                        }
                    }
                }
            },
            actions = {
                for (child in node.children) {
                    renderChild(child)
                }
            }
        )
    },
    "largeTopAppBar" to @Composable { node, _, onEvent, renderChild ->
        val title = node.props.string("title") ?: ""
        val navIconName = node.props.string("navIcon")
        LargeTopAppBar(
            title = { Text(title) },
            navigationIcon = {
                if (navIconName != null) {
                    val vector = iconByName(navIconName)
                    if (vector != null) {
                        IconButton(onClick = {
                            val id = node.id ?: "largeTopAppBar"
                            onEvent(A2UiEvent(id, "navClick"))
                        }) {
                            Icon(imageVector = vector, contentDescription = null)
                        }
                    }
                }
            },
            actions = {
                for (child in node.children) {
                    renderChild(child)
                }
            }
        )
    },
    "bottomAppBar" to { node, _, _, renderChild ->
        BottomAppBar(
            modifier = node.props.toModifier(),
            actions = {
                for (child in node.children.filter { it.type != "fab" }) {
                    renderChild(child)
                }
            },
            floatingActionButton = {
                val fabNode = node.children.find { it.type == "fab" }
                if (fabNode != null) {
                    renderChild(fabNode)
                }
            }
        )
    },
    "navigationBar" to { node, _, _, renderChild ->
        NavigationBar(modifier = node.props.toModifier()) {
            for (child in node.children) {
                renderChild(child)
            }
        }
    },
    "navItem" to { node, _, onEvent, _ ->
        val label = node.props.string("label") ?: ""
        val selected = node.props.bool("selected") ?: false
        val iconName = node.props.string("icon")
        val iconVector = iconName?.let { iconByName(it) }
        val id = node.id ?: "navItem"
        val background = if (selected) {
            MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
        } else {
            Color.Transparent
        }
        Column(
            modifier = node.props.toModifier()
                .padding(vertical = 8.dp, horizontal = 12.dp)
                .background(background, shape = MaterialTheme.shapes.medium)
                .clickable { onEvent(A2UiEvent(id, "select")) },
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            if (iconVector != null) {
                Icon(imageVector = iconVector, contentDescription = null)
            }
            Text(label, style = MaterialTheme.typography.labelSmall)
        }
    },
    "navigationRail" to { node, _, _, renderChild ->
        NavigationRail(modifier = node.props.toModifier()) {
            for (child in node.children) {
                renderChild(child)
            }
        }
    },
    "railItem" to { node, _, onEvent, _ ->
        val id = node.id ?: "railItem"
        val label = node.props.string("label") ?: ""
        val selected = node.props.bool("selected") ?: false
        val iconName = node.props.string("icon")
        val iconVector = iconName?.let { iconByName(it) }
        NavigationRailItem(
            selected = selected,
            onClick = { onEvent(A2UiEvent(id, "select")) },
            icon = {
                if (iconVector != null) {
                    Icon(imageVector = iconVector, contentDescription = null)
                }
            },
            label = if (label.isNotEmpty()) {{ Text(label) }} else null
        )
    },
    "navigationDrawer" to @Composable { node, _, _, renderChild ->
        val open = node.props.bool("open") ?: false
        val drawerState = rememberDrawerState(if (open) DrawerValue.Open else DrawerValue.Closed)
        val drawerContent = node.children.filter { it.type == "drawerItem" || it.type == "drawerSection" }
        val mainContent = node.children.filter { it.type != "drawerItem" && it.type != "drawerSection" }
        ModalNavigationDrawer(
            modifier = node.props.toModifier(),
            drawerState = drawerState,
            drawerContent = {
                ModalDrawerSheet {
                    for (child in drawerContent) {
                        renderChild(child)
                    }
                }
            }
        ) {
            for (child in mainContent) {
                renderChild(child)
            }
        }
    },
    "drawerItem" to { node, _, onEvent, _ ->
        val id = node.id ?: "drawerItem"
        val label = node.props.string("label") ?: ""
        val selected = node.props.bool("selected") ?: false
        val iconName = node.props.string("icon")
        val iconVector = iconName?.let { iconByName(it) }
        NavigationDrawerItem(
            modifier = node.props.toModifier(),
            label = { Text(label) },
            selected = selected,
            onClick = { onEvent(A2UiEvent(id, "select")) },
            icon = if (iconVector != null) {{ Icon(iconVector, contentDescription = null) }} else null
        )
    },
    "tabs" to { node, _, onEvent, _ ->
        val selectedIndex = node.props.int("selectedIndex") ?: 0
        TabRow(selectedTabIndex = selectedIndex) {
            node.children.forEachIndexed { index, child ->
                val label = child.props.string("label") ?: "Tab"
                Tab(
                    selected = index == selectedIndex,
                    onClick = {
                        val id = child.id ?: "tab_$index"
                        onEvent(A2UiEvent(id, "select"))
                    },
                    text = { Text(label) }
                )
            }
        }
    },
    "tab" to { _, _, _, _ ->
        // Placeholder - rendering handled by tabs parent
    },
    "menu" to { node, _, onEvent, renderChild ->
        val expanded = node.props.bool("expanded") ?: false
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                val id = node.id ?: "menu"
                onEvent(A2UiEvent(id, "dismiss"))
            },
            modifier = node.props.toModifier()
        ) {
            for (child in node.children) {
                renderChild(child)
            }
        }
    },
    "menuItem" to { node, _, onEvent, _ ->
        val label = node.props.string("label") ?: "Item"
        DropdownMenuItem(
            text = { Text(label) },
            onClick = {
                val id = node.id ?: "menuItem"
                onEvent(A2UiEvent(id, "select"))
            }
        )
    },
    "dialog" to { node, _, onEvent, _ ->
        val title = node.props.string("title")
        val text = node.props.string("text")
        val confirm = node.props.string("confirmLabel") ?: "OK"
        val dismiss = node.props.string("dismissLabel")
        AlertDialog(
            onDismissRequest = {
                val id = node.id ?: "dialog"
                onEvent(A2UiEvent(id, "dismiss"))
            },
            title = if (title != null) ({ Text(title) }) else null,
            text = if (text != null) ({ Text(text) }) else null,
            confirmButton = {
                TextButton(onClick = {
                    val id = node.id ?: "dialog"
                    onEvent(A2UiEvent(id, "confirm"))
                }) { Text(confirm) }
            },
            dismissButton = if (dismiss != null) ({
                TextButton(onClick = {
                    val id = node.id ?: "dialog"
                    onEvent(A2UiEvent(id, "dismiss"))
                }) { Text(dismiss) }
            }) else null
        )
    },
    "bottomSheet" to @Composable { node, _, onEvent, renderChild ->
        val id = node.id ?: "bottomSheet"
        val visible = node.props.bool("visible") ?: true
        if (visible) {
            ModalBottomSheet(
                modifier = node.props.toModifier(),
                onDismissRequest = {
                    onEvent(A2UiEvent(id, "dismiss"))
                }
            ) {
                for (child in node.children) {
                    renderChild(child)
                }
            }
        }
    },
    "bottomSheetScaffold" to @Composable { node, _, _, renderChild ->
        val sheetContent = node.children.filter { it.props.string("slot") == "sheet" }
        val mainContent = node.children.filter { it.props.string("slot") != "sheet" }
        val scaffoldState = rememberBottomSheetScaffoldState()
        BottomSheetScaffold(
            modifier = node.props.toModifier(),
            scaffoldState = scaffoldState,
            sheetContent = {
                for (child in sheetContent) {
                    renderChild(child)
                }
            }
        ) {
            Column {
                for (child in mainContent) {
                    renderChild(child)
                }
            }
        }
    }
)
