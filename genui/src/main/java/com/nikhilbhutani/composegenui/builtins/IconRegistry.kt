package com.nikhilbhutani.composegenui.builtins

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.filled.Warning
import androidx.compose.ui.graphics.vector.ImageVector

internal fun iconByName(name: String): ImageVector? = when (name) {
    "account", "accountCircle" -> Icons.Filled.AccountCircle
    "add" -> Icons.Filled.Add
    "arrowBack", "back" -> Icons.AutoMirrored.Filled.ArrowBack
    "arrowForward", "forward" -> Icons.AutoMirrored.Filled.ArrowForward
    "build" -> Icons.Filled.Build
    "call" -> Icons.Filled.Call
    "check" -> Icons.Filled.Check
    "clear" -> Icons.Filled.Clear
    "close" -> Icons.Filled.Close
    "create" -> Icons.Filled.Create
    "dateRange", "calendar" -> Icons.Filled.DateRange
    "delete" -> Icons.Filled.Delete
    "done" -> Icons.Filled.Done
    "edit" -> Icons.Filled.Edit
    "email", "mail" -> Icons.Filled.Email
    "exit", "logout" -> Icons.AutoMirrored.Filled.ExitToApp
    "face" -> Icons.Filled.Face
    "favorite" -> Icons.Filled.Favorite
    "favoriteBorder" -> Icons.Filled.FavoriteBorder
    "home" -> Icons.Filled.Home
    "info" -> Icons.Filled.Info
    "keyboardArrowDown", "arrowDown" -> Icons.Filled.KeyboardArrowDown
    "keyboardArrowLeft", "arrowLeft" -> Icons.AutoMirrored.Filled.KeyboardArrowLeft
    "keyboardArrowRight", "arrowRight" -> Icons.AutoMirrored.Filled.KeyboardArrowRight
    "keyboardArrowUp", "arrowUp" -> Icons.Filled.KeyboardArrowUp
    "list" -> Icons.AutoMirrored.Filled.List
    "location", "locationOn" -> Icons.Filled.LocationOn
    "lock" -> Icons.Filled.Lock
    "menu" -> Icons.Filled.Menu
    "moreVert", "more" -> Icons.Filled.MoreVert
    "notifications", "notification" -> Icons.Filled.Notifications
    "person" -> Icons.Filled.Person
    "phone" -> Icons.Filled.Phone
    "place" -> Icons.Filled.Place
    "play", "playArrow" -> Icons.Filled.PlayArrow
    "refresh" -> Icons.Filled.Refresh
    "search" -> Icons.Filled.Search
    "send" -> Icons.AutoMirrored.Filled.Send
    "settings" -> Icons.Filled.Settings
    "share" -> Icons.Filled.Share
    "shoppingCart", "cart" -> Icons.Filled.ShoppingCart
    "star" -> Icons.Filled.Star
    "thumbUp", "like" -> Icons.Filled.ThumbUp
    "warning" -> Icons.Filled.Warning
    else -> null
}
