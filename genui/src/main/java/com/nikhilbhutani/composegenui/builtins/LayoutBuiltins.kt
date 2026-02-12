@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package com.nikhilbhutani.composegenui.builtins

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nikhilbhutani.composegenui.A2UiRenderer
import com.nikhilbhutani.composegenui.contentAlignment
import com.nikhilbhutani.composegenui.cornerRadius
import com.nikhilbhutani.composegenui.dp
import com.nikhilbhutani.composegenui.hAlignment
import com.nikhilbhutani.composegenui.spacingDp
import com.nikhilbhutani.composegenui.toContentModifier
import com.nikhilbhutani.composegenui.toLayoutModifier
import com.nikhilbhutani.composegenui.toModifier
import com.nikhilbhutani.composegenui.vAlignment

internal fun layoutBuiltins(): Map<String, A2UiRenderer> = mapOf(
    "column" to { node, _, _, renderChild ->
        val props = node.props
        val modifier = props.toModifier()
        val spacing = props.spacingDp()
        val arrangement = spacing?.let { Arrangement.spacedBy(it) } ?: Arrangement.Top
        val hAlign = props.hAlignment() ?: Alignment.Start
        Column(
            modifier = modifier,
            verticalArrangement = arrangement,
            horizontalAlignment = hAlign
        ) {
            for (child in node.children) {
                renderChild(child)
            }
        }
    },
    "row" to { node, _, _, renderChild ->
        val props = node.props
        val modifier = props.toModifier()
        val spacing = props.spacingDp()
        val arrangement = spacing?.let { Arrangement.spacedBy(it) } ?: Arrangement.Start
        val vAlign = props.vAlignment() ?: Alignment.CenterVertically
        Row(
            modifier = modifier,
            horizontalArrangement = arrangement,
            verticalAlignment = vAlign
        ) {
            for (child in node.children) {
                renderChild(child)
            }
        }
    },
    "box" to { node, _, _, renderChild ->
        val props = node.props
        val modifier = props.toModifier()
        val alignment = props.contentAlignment() ?: Alignment.TopStart
        Box(
            modifier = modifier,
            contentAlignment = alignment
        ) {
            for (child in node.children) {
                renderChild(child)
            }
        }
    },
    "surface" to { node, _, _, renderChild ->
        val elevation = node.props.dp("elevation") ?: 0.dp
        val radius = node.props.cornerRadius()
        val shape = radius?.let { RoundedCornerShape(it) } ?: MaterialTheme.shapes.medium
        Surface(
            modifier = node.props.toModifier(),
            tonalElevation = elevation,
            shape = shape
        ) {
            for (child in node.children) {
                renderChild(child)
            }
        }
    },
    "divider" to { node, _, _, _ ->
        HorizontalDivider(modifier = node.props.toModifier())
    },
    "card" to { node, _, _, renderChild ->
        val elevation = node.props.dp("elevation") ?: 0.dp
        val radius = node.props.cornerRadius()
        val shape = radius?.let { RoundedCornerShape(it) } ?: MaterialTheme.shapes.medium
        Card(
            modifier = node.props.toLayoutModifier(),
            shape = shape,
            elevation = CardDefaults.cardElevation(defaultElevation = elevation)
        ) {
            Column(modifier = node.props.toContentModifier()) {
                for (child in node.children) {
                    renderChild(child)
                }
            }
        }
    },
    "elevatedCard" to { node, _, _, renderChild ->
        val elevation = node.props.dp("elevation") ?: 1.dp
        val radius = node.props.cornerRadius()
        val shape = radius?.let { RoundedCornerShape(it) } ?: MaterialTheme.shapes.medium
        ElevatedCard(
            modifier = node.props.toLayoutModifier(),
            shape = shape,
            elevation = CardDefaults.elevatedCardElevation(defaultElevation = elevation)
        ) {
            Column(modifier = node.props.toContentModifier()) {
                for (child in node.children) {
                    renderChild(child)
                }
            }
        }
    },
    "outlinedCard" to { node, _, _, renderChild ->
        val radius = node.props.cornerRadius()
        val shape = radius?.let { RoundedCornerShape(it) } ?: MaterialTheme.shapes.medium
        OutlinedCard(
            modifier = node.props.toLayoutModifier(),
            shape = shape
        ) {
            Column(modifier = node.props.toContentModifier()) {
                for (child in node.children) {
                    renderChild(child)
                }
            }
        }
    },
    "spacer" to { node, _, _, _ ->
        val width = node.props.dp("width")
        val height = node.props.dp("height")
        when {
            width != null && height != null -> Spacer(modifier = Modifier.width(width).height(height))
            width != null -> Spacer(modifier = Modifier.width(width))
            height != null -> Spacer(modifier = Modifier.height(height))
            else -> Spacer(modifier = Modifier.height(8.dp))
        }
    },
    "scaffold" to { node, _, _, renderChild ->
        val topBarNode = node.children.find { it.type == "topAppBar" }
        val bottomBarNode = node.children.find { it.type == "navigationBar" }
        val fabNode = node.children.find { it.type == "fab" }
        val contentNodes = node.children.filter {
            it.type != "topAppBar" && it.type != "navigationBar" && it.type != "fab"
        }
        Scaffold(
            modifier = node.props.toModifier(),
            topBar = {
                if (topBarNode != null) {
                    renderChild(topBarNode)
                }
            },
            bottomBar = {
                if (bottomBarNode != null) {
                    renderChild(bottomBarNode)
                }
            },
            floatingActionButton = {
                if (fabNode != null) {
                    renderChild(fabNode)
                }
            }
        ) { paddingValues ->
            Column(modifier = Modifier.padding(paddingValues)) {
                for (child in contentNodes) {
                    renderChild(child)
                }
            }
        }
    },
    "scrollColumn" to { node, _, _, renderChild ->
        val scrollState = rememberScrollState()
        Column(
            modifier = node.props.toModifier().verticalScroll(scrollState),
            verticalArrangement = node.props.spacingDp()?.let { Arrangement.spacedBy(it) } ?: Arrangement.Top
        ) {
            for (child in node.children) {
                renderChild(child)
            }
        }
    },
    "scrollRow" to { node, _, _, renderChild ->
        val scrollState = rememberScrollState()
        Row(
            modifier = node.props.toModifier().horizontalScroll(scrollState),
            horizontalArrangement = node.props.spacingDp()?.let { Arrangement.spacedBy(it) } ?: Arrangement.Start
        ) {
            for (child in node.children) {
                renderChild(child)
            }
        }
    }
)
