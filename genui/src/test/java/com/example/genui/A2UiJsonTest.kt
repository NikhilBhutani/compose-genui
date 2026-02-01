package com.example.genui

import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import org.junit.Assert.*
import org.junit.Test

class A2UiJsonTest {

    @Test
    fun `parse simple document`() {
        val json = """
        {
            "root": {
                "type": "text",
                "props": { "text": "Hello World" }
            }
        }
        """.trimIndent()
        
        val doc = parseA2UiDocument(json)
        assertEquals("text", doc.root.type)
        assertEquals("Hello World", doc.root.props["text"]?.toString()?.trim('"'))
    }

    @Test
    fun `parse document with children`() {
        val json = """
        {
            "root": {
                "type": "column",
                "children": [
                    { "type": "text", "props": { "text": "Line 1" } },
                    { "type": "text", "props": { "text": "Line 2" } }
                ]
            }
        }
        """.trimIndent()
        
        val doc = parseA2UiDocument(json)
        assertEquals("column", doc.root.type)
        assertEquals(2, doc.root.children.size)
        assertEquals("text", doc.root.children[0].type)
        assertEquals("text", doc.root.children[1].type)
    }

    @Test
    fun `parse document with id`() {
        val json = """
        {
            "root": {
                "type": "button",
                "id": "submitButton",
                "props": { "label": "Submit" }
            }
        }
        """.trimIndent()
        
        val doc = parseA2UiDocument(json)
        assertEquals("submitButton", doc.root.id)
    }

    @Test
    fun `parse document with nested props`() {
        val json = """
        {
            "root": {
                "type": "column",
                "props": {
                    "padding": 16,
                    "spacing": 8,
                    "fill": true
                }
            }
        }
        """.trimIndent()
        
        val doc = parseA2UiDocument(json)
        assertNotNull(doc.root.props["padding"])
        assertNotNull(doc.root.props["spacing"])
        assertNotNull(doc.root.props["fill"])
    }

    @Test
    fun `serialize document to json`() {
        val doc = A2UiDocument(
            root = A2UiNode(
                type = "text",
                props = buildJsonObject { put("text", "Hello") }
            )
        )
        
        val json = doc.toJson()
        assertTrue(json.contains("text"))
        assertTrue(json.contains("Hello"))
    }

    @Test
    fun `roundtrip serialization`() {
        val original = A2UiDocument(
            root = A2UiNode(
                type = "column",
                id = "mainColumn",
                props = buildJsonObject {
                    put("padding", 16)
                    put("spacing", 8)
                },
                children = listOf(
                    A2UiNode(
                        type = "text",
                        props = buildJsonObject { put("text", "Hello") }
                    ),
                    A2UiNode(
                        type = "button",
                        id = "btn",
                        props = buildJsonObject { put("label", "Click") }
                    )
                )
            )
        )
        
        val json = original.toJson()
        val parsed = parseA2UiDocument(json)
        
        assertEquals(original.root.type, parsed.root.type)
        assertEquals(original.root.id, parsed.root.id)
        assertEquals(original.root.children.size, parsed.root.children.size)
    }

    @Test
    fun `parse ignores unknown fields`() {
        val json = """
        {
            "root": {
                "type": "text",
                "unknownField": "ignored",
                "props": { "text": "Hello", "unknownProp": 123 }
            },
            "metadata": { "version": "1.0" }
        }
        """.trimIndent()
        
        // Should not throw
        val doc = parseA2UiDocument(json)
        assertEquals("text", doc.root.type)
    }

    @Test
    fun `parse deeply nested structure`() {
        val json = """
        {
            "root": {
                "type": "scaffold",
                "children": [
                    {
                        "type": "column",
                        "children": [
                            {
                                "type": "card",
                                "children": [
                                    {
                                        "type": "row",
                                        "children": [
                                            { "type": "icon", "props": { "name": "star" } },
                                            { "type": "text", "props": { "text": "Rating" } }
                                        ]
                                    }
                                ]
                            }
                        ]
                    }
                ]
            }
        }
        """.trimIndent()
        
        val doc = parseA2UiDocument(json)
        assertEquals("scaffold", doc.root.type)
        assertEquals("column", doc.root.children[0].type)
        assertEquals("card", doc.root.children[0].children[0].type)
        assertEquals("row", doc.root.children[0].children[0].children[0].type)
        assertEquals(2, doc.root.children[0].children[0].children[0].children.size)
    }
}
