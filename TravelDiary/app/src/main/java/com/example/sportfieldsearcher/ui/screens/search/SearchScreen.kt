package com.example.sportfieldsearcher.ui.screens.search

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.navigation.NavHostController
import com.example.sportfieldsearcher.data.database.entities.PrivacyType
import com.example.sportfieldsearcher.ui.composables.FieldSize
import com.example.sportfieldsearcher.ui.composables.ImageForField
import com.example.sportfieldsearcher.ui.utils.SportFieldSearcherRoute
import com.example.sportfieldsearcher.ui.composables.ImageWithPlaceholder
import com.example.sportfieldsearcher.ui.composables.Size
import com.example.sportfieldsearcher.ui.controllers.FieldsState
import com.example.sportfieldsearcher.ui.controllers.UsersState
import kotlinx.coroutines.launch

@Composable
fun SearchScreen(
    fieldsState: FieldsState,
    usersState: UsersState,
    navController: NavHostController
) {
    Scaffold(

    ) { contentPadding ->
        TabLayout(
            contentPadding = contentPadding,
            usersState = usersState,
            fieldsState = fieldsState,
            navController = navController,
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TabLayout(
    contentPadding: PaddingValues,
    usersState: UsersState,
    fieldsState : FieldsState,
    navController: NavHostController
) {
    val pagerState = rememberPagerState(pageCount = { 3 })

    Column(
        modifier = Modifier
            .padding(
                top = 0.dp,
                bottom = 0.dp,
                start = contentPadding.calculateStartPadding(LayoutDirection.Ltr),
                end = contentPadding.calculateEndPadding(LayoutDirection.Rtl)
            )
            .fillMaxSize()
    ) {
        Tabs(pagerState = pagerState)
        TabsContent(
            pagerState = pagerState,
            usersState = usersState,
            fieldsState = fieldsState,
            navController = navController
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Tabs(pagerState: PagerState) {
    val list = listOf("Fields", "Users", "Category")
    val scope = rememberCoroutineScope()
    TabRow(
        selectedTabIndex = pagerState.currentPage,
        indicator = { tabPositions ->
            TabRowDefaults.SecondaryIndicator(Modifier.tabIndicatorOffset(tabPositions[pagerState.currentPage]))
        }
    ) {
        list.forEachIndexed { index, _ ->
            Tab(
                text = {
                    Text(
                        list[index],
                        fontWeight = if (pagerState.currentPage == index) FontWeight.Bold else FontWeight.Normal
                    )
                },
                selected = pagerState.currentPage == index,
                onClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(index)
                    }
                }
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TabsContent(
    pagerState: PagerState,
    usersState: UsersState,
    fieldsState : FieldsState,
    navController: NavHostController
) {
    HorizontalPager(state = pagerState) { page ->
        when (page) {
            0 -> TabContentFields(
                navController = navController,
                fieldsState = fieldsState,
            )

            1 -> TabContentUsers(
                navController = navController,
                usersState = usersState,
            )

            2 -> TabContentCategory(
                navController = navController,
                fieldsState = fieldsState,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TabContentFields(
    navController: NavHostController,
    fieldsState : FieldsState,
) {
    var text by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    Box(
        Modifier
            .fillMaxSize()
            .semantics { isTraversalGroup = true },
    ) {
        SearchBar(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .semantics { traversalIndex = -1f }
                .padding(5.dp),
            windowInsets = SearchBarDefaults.windowInsets.exclude(SearchBarDefaults.windowInsets),
            query = text,
            onQueryChange = { text = it },
            onSearch = { expanded = false },
            active = expanded,
            onActiveChange = { expanded = it },
            placeholder = { Text("Search fields") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
        ) {
            if (expanded) {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val filteredFields = fieldsState.fieldsWithUsers.filter { fieldWithUsers ->
                        fieldWithUsers.field.name.contains(text, ignoreCase = true)
                    }
                    items(filteredFields) { fieldWithUsers ->
                        if (fieldWithUsers.field.privacyType == PrivacyType.PUBLIC
                            || fieldWithUsers.field.privacyType == PrivacyType.PRIVATE) {
                            ListItem(
                                modifier = Modifier.clickable {
                                    navController.navigate(
                                        SportFieldSearcherRoute.FieldDetails.buildRoute(
                                            fieldWithUsers.field.fieldId
                                        )
                                    )
                                },
                                headlineContent = { Text(text = fieldWithUsers.field.name, color = MaterialTheme.colorScheme.onPrimaryContainer) },
                                leadingContent = {
                                    ImageForField(
                                        uri = fieldWithUsers.field.fieldPicture?.toUri(),
                                        size = FieldSize.Small
                                    )
                                },
                                supportingContent = {
                                    Column {
                                        Text(text = "City: " + fieldWithUsers.field.city)
                                        if (fieldWithUsers.field.description.isNotBlank()) {
                                            Text(text = "Description: " + fieldWithUsers.field.description)
                                        } else Text(text = "Description: NONE")
                                    }
                                },
                            )
                            HorizontalDivider()
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TabContentUsers(
    navController: NavHostController,
    usersState: UsersState,
) {
    var text by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    Box(
        Modifier
            .fillMaxSize()
            .semantics { isTraversalGroup = true }
    ) {
        SearchBar(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .semantics { traversalIndex = -1f }
                .padding(5.dp),
            windowInsets = SearchBarDefaults.windowInsets.exclude(SearchBarDefaults.windowInsets),
            query = text,
            onQueryChange = { text = it },
            onSearch = { expanded = false },
            active = expanded,
            onActiveChange = { expanded = it },
            placeholder = { Text("Search users") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
        ) {
            if (expanded) {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val filteredUsers = usersState.users.filter { users ->
                        users.username.contains(text, ignoreCase = true)
                    }
                    items(filteredUsers) { users ->
                        ListItem(
                            modifier = Modifier.clickable {
                                navController.navigate(
                                    SportFieldSearcherRoute.Profile.buildRoute(
                                        users.userId
                                    )
                                )
                            },
                            headlineContent = { Text(text = users.username, color = MaterialTheme.colorScheme.onPrimaryContainer) },
                            leadingContent = {
                                ImageWithPlaceholder(
                                    uri = users.profilePicture?.toUri(),
                                    size = Size.Small
                                )
                            },
                        )
                        HorizontalDivider()
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TabContentCategory(
    navController: NavHostController,
    fieldsState : FieldsState,
) {
    var text by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    Box(
        Modifier
            .fillMaxSize()
            .semantics { isTraversalGroup = true },
    ) {
        SearchBar(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .semantics { traversalIndex = -1f }
                .padding(5.dp),
            windowInsets = SearchBarDefaults.windowInsets.exclude(SearchBarDefaults.windowInsets),
            query = text,
            onQueryChange = { text = it },
            onSearch = { expanded = false },
            active = expanded,
            onActiveChange = { expanded = it },
            placeholder = { Text("Search category") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
        ) {
            if (expanded) {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val filteredFields = fieldsState.fieldsWithUsers.filter { fieldWithUsers ->
                        fieldWithUsers.field.category.name.contains(text, ignoreCase = true)
                    }
                    items(filteredFields) { fieldWithUsers ->
                        if (fieldWithUsers.field.privacyType == PrivacyType.PUBLIC
                            || fieldWithUsers.field.privacyType == PrivacyType.PRIVATE) {
                            ListItem(
                                modifier = Modifier.clickable {
                                    navController.navigate(
                                        SportFieldSearcherRoute.FieldDetails.buildRoute(
                                            fieldWithUsers.field.fieldId
                                        )
                                    )
                                },
                                headlineContent = { Text(text = fieldWithUsers.field.name, color = MaterialTheme.colorScheme.onPrimaryContainer) },
                                leadingContent = {
                                    ImageForField(
                                        uri = fieldWithUsers.field.fieldPicture?.toUri(),
                                        size = FieldSize.Small
                                    )
                                },
                                supportingContent = {
                                    Column {
                                        Text(text = "Category: " + fieldWithUsers.field.category.name)
                                        Text(text = "Privacy: " + fieldWithUsers.field.privacyType.name)
                                    }
                                },
                            )
                            HorizontalDivider()
                        }
                    }
                }
            }
        }
    }
}