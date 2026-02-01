package com.example.genui

import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import org.junit.Assert.*
import org.junit.Test

class A2UiValidatorTest {

    @Test
    fun `valid simple document passes validation`() {
        val doc = A2UiDocument(
            root = A2UiNode(
                type = "column",
                children = listOf(
                    A2UiNode(
                        type = "text",
                        props = buildJsonObject { put("text", "Hello") }
                    )
                )
            )
        )
        
        val result = validateA2Ui(doc)
        assertTrue("Document should be valid", result.isValid)
        assertTrue("Should have no errors", result.errors.isEmpty())
    }

    @Test
    fun `unknown component type is an error`() {
        val doc = A2UiDocument(
            root = A2UiNode(type = "unknownWidget")
        )
        
        val result = validateA2Ui(doc)
        assertFalse("Document should be invalid", result.isValid)
        assertEquals(1, result.errors.size)
        assertTrue(result.errors[0].message.contains("Unknown component type"))
    }

    @Test
    fun `missing required props is an error`() {
        val doc = A2UiDocument(
            root = A2UiNode(
                type = "image",
                props = JsonObject(emptyMap()) // missing required 'url' prop
            )
        )
        
        val result = validateA2Ui(doc)
        assertFalse("Document should be invalid", result.isValid)
        assertTrue(result.errors.any { it.message.contains("Missing required props") })
    }

    @Test
    fun `children on leaf component is a warning`() {
        val doc = A2UiDocument(
            root = A2UiNode(
                type = "text",
                props = buildJsonObject { put("text", "Hello") },
                children = listOf(
                    A2UiNode(type = "text", props = buildJsonObject { put("text", "Nested") })
                )
            )
        )
        
        val result = validateA2Ui(doc)
        assertTrue("Document should be valid (warnings only)", result.isValid)
        assertTrue("Should have warnings", result.hasWarnings)
        assertTrue(result.warnings.any { it.message.contains("should not have children") })
    }

    @Test
    fun `duplicate IDs are detected`() {
        val doc = A2UiDocument(
            root = A2UiNode(
                type = "column",
                children = listOf(
                    A2UiNode(type = "button", id = "myButton"),
                    A2UiNode(type = "button", id = "myButton") // duplicate
                )
            )
        )
        
        val result = validateA2Ui(doc, options = A2UiValidationOptions(checkIds = true))
        assertFalse("Document should be invalid", result.isValid)
        assertTrue(result.errors.any { it.message.contains("Duplicate ID") })
    }

    @Test
    fun `strict mode warns about unknown props`() {
        val doc = A2UiDocument(
            root = A2UiNode(
                type = "text",
                props = buildJsonObject {
                    put("text", "Hello")
                    put("unknownProp", "value")
                }
            )
        )
        
        val result = validateA2Ui(doc, strict = true)
        assertTrue("Document should be valid", result.isValid)
        assertTrue("Should have warnings", result.hasWarnings)
        assertTrue(result.warnings.any { it.message.contains("Unknown props") })
    }

    @Test
    fun `invalid variant value is warned`() {
        val doc = A2UiDocument(
            root = A2UiNode(
                type = "button",
                props = buildJsonObject { put("variant", "invalid") }
            )
        )
        
        val result = validateA2Ui(doc, options = A2UiValidationOptions(checkValues = true))
        assertTrue("Should have warnings", result.hasWarnings)
        assertTrue(result.warnings.any { it.message.contains("Invalid variant") })
    }

    @Test
    fun `segment outside segmentedButton is warned`() {
        val doc = A2UiDocument(
            root = A2UiNode(
                type = "column",
                children = listOf(
                    A2UiNode(type = "segment", props = buildJsonObject { put("label", "Test") })
                )
            )
        )
        
        val result = validateA2Ui(doc, options = A2UiValidationOptions(checkStructure = true))
        assertTrue("Should have warnings", result.hasWarnings)
        assertTrue(result.warnings.any { it.message.contains("should be a direct child of segmentedButton") })
    }

    @Test
    fun `unknown icon name is warned`() {
        val doc = A2UiDocument(
            root = A2UiNode(
                type = "icon",
                props = buildJsonObject { put("name", "nonexistentIcon") }
            )
        )
        
        val result = validateA2Ui(doc, options = A2UiValidationOptions(checkIcons = true))
        assertTrue("Should have warnings", result.hasWarnings)
        assertTrue(result.warnings.any { it.message.contains("Unknown icon name") })
    }

    @Test
    fun `valid icon name passes`() {
        val doc = A2UiDocument(
            root = A2UiNode(
                type = "icon",
                props = buildJsonObject { put("name", "home") }
            )
        )
        
        val result = validateA2Ui(doc, options = A2UiValidationOptions(checkIcons = true))
        assertFalse("Should not have icon warnings", result.warnings.any { it.message.contains("Unknown icon") })
    }

    @Test
    fun `negative padding is warned`() {
        val doc = A2UiDocument(
            root = A2UiNode(
                type = "column",
                props = buildJsonObject { put("padding", -10) }
            )
        )
        
        val result = validateA2Ui(doc, options = A2UiValidationOptions(checkValues = true))
        assertTrue("Should have warnings", result.hasWarnings)
        assertTrue(result.warnings.any { it.message.contains("positive number") })
    }

    @Test
    fun `complex nested document validates correctly`() {
        val doc = A2UiDocument(
            root = A2UiNode(
                type = "scaffold",
                children = listOf(
                    A2UiNode(type = "topAppBar", props = buildJsonObject { put("title", "App") }),
                    A2UiNode(
                        type = "column",
                        props = buildJsonObject { 
                            put("padding", 16)
                            put("spacing", 8)
                        },
                        children = listOf(
                            A2UiNode(type = "text", props = buildJsonObject { put("text", "Hello") }),
                            A2UiNode(type = "button", id = "submit", props = buildJsonObject { put("label", "Submit") }),
                            A2UiNode(
                                type = "segmentedButton",
                                id = "picker",
                                children = listOf(
                                    A2UiNode(type = "segment", props = buildJsonObject { put("label", "A") }),
                                    A2UiNode(type = "segment", props = buildJsonObject { put("label", "B") })
                                )
                            )
                        )
                    ),
                    A2UiNode(
                        type = "navigationBar",
                        children = listOf(
                            A2UiNode(type = "navItem", props = buildJsonObject { 
                                put("label", "Home")
                                put("icon", "home")
                            }),
                            A2UiNode(type = "navItem", props = buildJsonObject { 
                                put("label", "Settings")
                                put("icon", "settings")
                            })
                        )
                    )
                )
            )
        )
        
        val result = validateA2Ui(doc, options = A2UiValidationOptions(
            strict = false,
            checkIds = true,
            checkStructure = true,
            checkValues = true,
            checkIcons = true
        ))
        
        assertTrue("Complex document should be valid", result.isValid)
    }
}
