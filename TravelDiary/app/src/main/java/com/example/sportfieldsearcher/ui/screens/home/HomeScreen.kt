package com.example.sportfieldsearcher.ui.screens.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.navigation.NavHostController
import com.example.sportfieldsearcher.data.database.entities.FieldWithUsers
import com.example.sportfieldsearcher.ui.composables.FieldSize
import com.example.sportfieldsearcher.ui.composables.ImageForField
import com.example.sportfieldsearcher.ui.utils.SportFieldSearcherRoute
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    publicFields: List<FieldWithUsers>,
    privateFields: List<FieldWithUsers>,
    navController: NavHostController
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                containerColor = MaterialTheme.colorScheme.primary,
                onClick = { navController.navigate(SportFieldSearcherRoute.FieldsMap.route) }
            ) {
                Icon(Icons.Outlined.LocationOn, "Field Map")
            }
        },
        contentWindowInsets = ScaffoldDefaults.contentWindowInsets.exclude(NavigationBarDefaults.windowInsets)
    ) { contentPadding ->
        TabLayout(
            contentPadding = contentPadding,
            publicFields = publicFields,
            privateFields = privateFields,
            navController = navController,
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TabLayout(
    contentPadding: PaddingValues,
    publicFields: List<FieldWithUsers>,
    privateFields: List<FieldWithUsers>,
    navController: NavHostController,
) {
    val pagerState = rememberPagerState(pageCount = { 2 })

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
            contentPadding = contentPadding,
            pagerState = pagerState,
            publicFields = publicFields,
            privateFields = privateFields,
            navController = navController,
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Tabs(pagerState: PagerState) {
    val list = listOf("Public fields", "Private fields")
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
    publicFields: List<FieldWithUsers>,
    privateFields: List<FieldWithUsers>,
    navController: NavHostController,
    contentPadding: PaddingValues,
) {
    HorizontalPager(state = pagerState) { page ->
        when (page) {
            0 -> TabContent(
                navController = navController,
                fields = publicFields,
                contentPadding = contentPadding,
            )
            1 -> TabContent(
                navController = navController,
                fields = privateFields,
                contentPadding = contentPadding,
            )
        }
    }
}

@Composable
fun TabContent(
    navController: NavHostController,
    fields: List<FieldWithUsers>,
    contentPadding: PaddingValues,
) {
    Box(
        Modifier
            .fillMaxSize()
            .semantics { isTraversalGroup = true },
    ) {
        if (fields.isNotEmpty()) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(8.dp, 8.dp, 8.dp, 80.dp)
            ) {
                items(fields) { field ->
                    FieldItem(
                        field,
                        onClick = {
                            navController.navigate(SportFieldSearcherRoute.FieldDetails.buildRoute(field.field.fieldId))
                        }
                    )
                }
            }
        }  else {
            Text(
                text = "No fields found.\nTap the button to add a new field.",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .wrapContentSize(Alignment.Center)
            )
        }
    }
}

@Composable
fun FieldItem(fieldWithUsers: FieldWithUsers, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .size(150.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (fieldWithUsers.field.fieldPicture == null) {
                Image(
                    Icons.Outlined.Image,
                    contentDescription = "Field picture",
                    contentScale = ContentScale.Crop,
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSecondary),
                    modifier = Modifier
                        .padding(vertical = 10.dp)
                        .height(400.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(MaterialTheme.colorScheme.secondary)
                        .padding(30.dp)
                )
            } else {
                ImageForField(
                    uri = fieldWithUsers.field.fieldPicture.toUri(),
                    size = FieldSize.Small
                )
            }
            Spacer(Modifier.size(4.dp))
            Text(
                text = fieldWithUsers.field.name,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.size(4.dp))
            Text(
                text = fieldWithUsers.field.category.name,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )
        }
    }
}