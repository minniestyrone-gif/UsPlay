package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.CompassCalibration
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.example.data.PlannedDate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.DateIdea
import com.example.ui.UsPlayViewModel
import com.example.ui.components.DateIdeaCard
import com.example.ui.components.MysteryDiceRoller
import com.example.ui.theme.UsPlayGoldXP
import com.example.ui.theme.UsPlayPlumCard
import com.example.ui.theme.UsPlayPlumCardElevated
import com.example.ui.theme.UsPlayRoseDark
import com.example.ui.theme.UsPlayRosePrimary
import com.example.ui.theme.UsPlayTextMuted
import com.example.ui.theme.UsPlayTextSecondary

@Composable
fun DatesScreen(
    viewModel: UsPlayViewModel,
    initialTab: Int = 0,
    modifier: Modifier = Modifier
) {
    var selectedSubTab by remember { mutableStateOf(initialTab) }
    val subTabs = listOf("Discover", "Surprise Me", "Date Planner", "Categories", "Saved Dates", "Nearby")

    val allDates by viewModel.allDateIdeas.collectAsState()
    val savedDates by viewModel.savedDateIdeas.collectAsState()
    val plannedDates by viewModel.plannedDates.collectAsState()
    val isRolling by viewModel.isRollingDice.collectAsState()
    val rolledMystery by viewModel.rolledMysteryDate.collectAsState()

    var selectedCategoryFilter by remember { mutableStateOf("❤️ Romantic") }
    val categories = listOf("❤️ Romantic", "😂 Funny", "🔥 Spicy", "🧘 Relaxing", "🎉 Adventurous", "🧠 Intellectual")

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 8.dp)
    ) {
        // Scrollable Top Sub-Tab Row
        ScrollableTabRow(
            selectedTabIndex = selectedSubTab,
            containerColor = Color.Transparent,
            contentColor = UsPlayRosePrimary,
            edgePadding = 16.dp,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    Modifier.tabIndicatorOffset(tabPositions[selectedSubTab]),
                    color = UsPlayRosePrimary,
                    height = 3.dp
                )
            }
        ) {
            subTabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedSubTab == index,
                    onClick = { selectedSubTab = index },
                    modifier = Modifier.testTag("dates_subtab_$index"),
                    text = {
                        Text(
                            text = title,
                            fontWeight = if (selectedSubTab == index) FontWeight.Bold else FontWeight.Medium,
                            fontSize = 14.sp,
                            color = if (selectedSubTab == index) com.example.ui.theme.UsPlayRoseDark else UsPlayTextMuted
                        )
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Box(modifier = Modifier.fillMaxSize()) {
            when (selectedSubTab) {
                0 -> DiscoverSection(
                    allDates = allDates,
                    onSaveToggle = { viewModel.toggleSaveDateIdea(it) },
                    onComplete = { viewModel.completeDateIdea(it) },
                    onSelectCategory = { cat ->
                        selectedCategoryFilter = cat
                        selectedSubTab = 3 // Jump to Categories
                    }
                )
                1 -> SurpriseMeSection(
                    isRolling = isRolling,
                    rolledMysteryDate = rolledMystery,
                    onRollDice = { viewModel.rollSurpriseDice() },
                    onSaveToggle = { viewModel.toggleSaveDateIdea(it) },
                    onComplete = { viewModel.completeDateIdea(it) }
                )
                2 -> DatePlannerSection(
                    plannedDates = plannedDates,
                    onAddPlannedDate = { title, dateText, timeText, location, budget, notes ->
                        viewModel.addPlannedDate(title, dateText, timeText, location, budget, notes)
                    },
                    onDeletePlannedDate = { id -> viewModel.deletePlannedDate(id) }
                )
                3 -> CategoriesSection(
                    categories = categories,
                    selectedCategory = selectedCategoryFilter,
                    onCategorySelected = { selectedCategoryFilter = it },
                    allDates = allDates.filter { it.category.contains(selectedCategoryFilter, ignoreCase = true) },
                    onSaveToggle = { viewModel.toggleSaveDateIdea(it) },
                    onComplete = { viewModel.completeDateIdea(it) }
                )
                4 -> SavedDatesSection(
                    savedDates = savedDates,
                    onSaveToggle = { viewModel.toggleSaveDateIdea(it) },
                    onComplete = { viewModel.completeDateIdea(it) }
                )
                5 -> NearbySection()
            }
        }
    }
}

@Composable
fun DiscoverSection(
    allDates: List<DateIdea>,
    onSaveToggle: (DateIdea) -> Unit,
    onComplete: (DateIdea) -> Unit,
    onSelectCategory: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp),
                shape = RoundedCornerShape(20.dp)
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Image(
                        painter = painterResource(id = R.drawable.img_dice_mystery),
                        contentDescription = "Discover Dates",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.6f))
                    )
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Cape Town Date Ideas 🇿🇦",
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                        Text(
                            text = "Curated Mother City adventures for every mood & budget",
                            color = UsPlayTextSecondary,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }

        // Category Pills Row
        item {
            Column {
                Text(
                    text = "Explore Categories 🎭",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = com.example.ui.theme.UsPlayTextPrimary
                )
                Spacer(modifier = Modifier.height(8.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    val cats = listOf("❤️ Romantic", "😂 Funny", "🔥 Spicy", "🧘 Relaxing", "🎉 Adventurous", "🧠 Intellectual")
                    items(cats) { cat ->
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = UsPlayPlumCardElevated,
                            modifier = Modifier.clickable { onSelectCategory(cat) }
                        ) {
                            Text(
                                text = cat,
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = com.example.ui.theme.UsPlayTextPrimary
                            )
                        }
                    }
                }
            }
        }

        item {
            Text(
                text = "Trending Date Night Ideas 🔥",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = com.example.ui.theme.UsPlayTextPrimary
            )
        }

        items(allDates.take(6)) { idea ->
            DateIdeaCard(
                dateIdea = idea,
                onSaveToggle = { onSaveToggle(idea) },
                onComplete = { onComplete(idea) }
            )
        }

        item { Spacer(modifier = Modifier.height(80.dp)) }
    }
}

@Composable
fun SurpriseMeSection(
    isRolling: Boolean,
    rolledMysteryDate: DateIdea?,
    onRollDice: () -> Unit,
    onSaveToggle: (DateIdea) -> Unit,
    onComplete: (DateIdea) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            MysteryDiceRoller(
                isRolling = isRolling,
                onRollDice = onRollDice
            )
        }

        item {
            rolledMysteryDate?.let { date ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("rolled_mystery_result_card"),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = UsPlayPlumCardElevated),
                    border = androidx.compose.foundation.BorderStroke(1.5.dp, UsPlayGoldXP)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = UsPlayGoldXP
                        ) {
                            Text(
                                text = "✨ MYSTERY QUEST UNLOCKED ✨",
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.Black
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        DateIdeaCard(
                            dateIdea = date,
                            onSaveToggle = { onSaveToggle(date) },
                            onComplete = { onComplete(date) }
                        )
                    }
                }
            }
        }

        item { Spacer(modifier = Modifier.height(80.dp)) }
    }
}

@Composable
fun DatePlannerSection(
    plannedDates: List<PlannedDate>,
    onAddPlannedDate: (title: String, dateText: String, timeText: String, location: String, budget: String, notes: String) -> Unit,
    onDeletePlannedDate: (String) -> Unit
) {
    var showForm by remember { mutableStateOf(false) }

    var title by remember { mutableStateOf("") }
    var dateText by remember { mutableStateOf("") }
    var timeText by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var budget by remember { mutableStateOf("$$") }
    var notes by remember { mutableStateOf("") }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Date Planner 🗓️",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = com.example.ui.theme.UsPlayTextPrimary
                )

                Button(
                    onClick = { showForm = !showForm },
                    colors = ButtonDefaults.buttonColors(containerColor = UsPlayRosePrimary),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Add")
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = if (showForm) "Close" else "Plan Date", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        if (showForm) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = UsPlayPlumCardElevated)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = "Schedule a Date Night",
                            fontWeight = FontWeight.Bold,
                            color = com.example.ui.theme.UsPlayTextPrimary,
                            fontSize = 16.sp
                        )

                        OutlinedTextField(
                            value = title,
                            onValueChange = { title = it },
                            label = { Text("Date Title (e.g. Skyline Rooftop Wine)") },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(focusedTextColor = com.example.ui.theme.UsPlayTextPrimary, unfocusedTextColor = com.example.ui.theme.UsPlayTextPrimary)
                        )

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(
                                value = dateText,
                                onValueChange = { dateText = it },
                                label = { Text("Date (e.g. This Saturday)") },
                                modifier = Modifier.weight(1f),
                                colors = OutlinedTextFieldDefaults.colors(focusedTextColor = com.example.ui.theme.UsPlayTextPrimary, unfocusedTextColor = com.example.ui.theme.UsPlayTextPrimary)
                            )
                            OutlinedTextField(
                                value = timeText,
                                onValueChange = { timeText = it },
                                label = { Text("Time (e.g. 8:00 PM)") },
                                modifier = Modifier.weight(1f),
                                colors = OutlinedTextFieldDefaults.colors(focusedTextColor = com.example.ui.theme.UsPlayTextPrimary, unfocusedTextColor = com.example.ui.theme.UsPlayTextPrimary)
                            )
                        }

                        OutlinedTextField(
                            value = location,
                            onValueChange = { location = it },
                            label = { Text("Location / Spot") },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(focusedTextColor = com.example.ui.theme.UsPlayTextPrimary, unfocusedTextColor = com.example.ui.theme.UsPlayTextPrimary)
                        )

                        OutlinedTextField(
                            value = notes,
                            onValueChange = { notes = it },
                            label = { Text("Notes & Ideas") },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(focusedTextColor = com.example.ui.theme.UsPlayTextPrimary, unfocusedTextColor = com.example.ui.theme.UsPlayTextPrimary)
                        )

                        Button(
                            onClick = {
                                if (title.isNotBlank()) {
                                    onAddPlannedDate(
                                        title,
                                        if (dateText.isBlank()) "This Weekend" else dateText,
                                        if (timeText.isBlank()) "7:30 PM" else timeText,
                                        if (location.isBlank()) "Secret Romantic Spot" else location,
                                        budget,
                                        notes
                                    )
                                    title = ""; dateText = ""; timeText = ""; location = ""; notes = ""
                                    showForm = false
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = UsPlayRosePrimary),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("SAVE SCHEDULED DATE NIGHT", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        if (plannedDates.isEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = UsPlayPlumCard)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "🗓️", fontSize = 36.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "No Scheduled Dates Yet", fontWeight = FontWeight.Bold, color = com.example.ui.theme.UsPlayTextPrimary)
                        Text(text = "Tap 'Plan Date' to schedule your next adventure!", fontSize = 12.sp, color = UsPlayTextMuted)
                    }
                }
            }
        } else {
            items(plannedDates) { planned ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = UsPlayPlumCard)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = planned.title, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = com.example.ui.theme.UsPlayTextPrimary)
                            IconButton(onClick = { onDeletePlannedDate(planned.id) }) {
                                Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete", tint = UsPlayRoseDark)
                            }
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.DateRange, contentDescription = "Time", tint = UsPlayGoldXP, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "${planned.dateText} at ${planned.timeText}", fontSize = 12.sp, color = UsPlayTextSecondary)
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.LocationOn, contentDescription = "Location", tint = UsPlayRosePrimary, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = planned.location, fontSize = 12.sp, color = UsPlayTextMuted)
                        }

                        if (planned.notes.isNotBlank()) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(text = "Notes: ${planned.notes}", fontSize = 12.sp, color = UsPlayTextSecondary)
                        }
                    }
                }
            }
        }

        item { Spacer(modifier = Modifier.height(80.dp)) }
    }
}

@Composable
fun CategoriesSection(
    categories: List<String>,
    selectedCategory: String,
    onCategorySelected: (String) -> Unit,
    allDates: List<DateIdea>,
    onSaveToggle: (DateIdea) -> Unit,
    onComplete: (DateIdea) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "Categories 🎭",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = com.example.ui.theme.UsPlayTextPrimary
            )
        }

        item {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(categories) { cat ->
                    val isSel = cat == selectedCategory
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = if (isSel) UsPlayRosePrimary else UsPlayPlumCardElevated,
                        modifier = Modifier.clickable { onCategorySelected(cat) }
                    ) {
                        Text(
                            text = cat,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isSel) Color.White else com.example.ui.theme.UsPlayTextPrimary
                        )
                    }
                }
            }
        }

        items(allDates) { idea ->
            DateIdeaCard(
                dateIdea = idea,
                onSaveToggle = { onSaveToggle(idea) },
                onComplete = { onComplete(idea) }
            )
        }

        item { Spacer(modifier = Modifier.height(80.dp)) }
    }
}

@Composable
fun SavedDatesSection(
    savedDates: List<DateIdea>,
    onSaveToggle: (DateIdea) -> Unit,
    onComplete: (DateIdea) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "Saved Dates 🔖 (${savedDates.size})",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = com.example.ui.theme.UsPlayTextPrimary
            )
        }

        if (savedDates.isEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = UsPlayPlumCard)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "🔖", fontSize = 36.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "No Saved Dates Yet", fontWeight = FontWeight.Bold, color = com.example.ui.theme.UsPlayTextPrimary)
                        Text(text = "Tap the bookmark icon on any date idea to save it here!", fontSize = 12.sp, color = UsPlayTextMuted)
                    }
                }
            }
        } else {
            items(savedDates) { idea ->
                DateIdeaCard(
                    dateIdea = idea,
                    onSaveToggle = { onSaveToggle(idea) },
                    onComplete = { onComplete(idea) }
                )
            }
        }

        item { Spacer(modifier = Modifier.height(80.dp)) }
    }
}

@Composable
fun NearbySection() {
    val localSpots = remember {
        listOf(
            Triple("The Galileo Open Air Cinema (Kirstenbosch)", "Watch romantic movie classics under Table Mountain stars with biltong & MCC wine", "4.9 ★ • R140"),
            Triple("Asoka Lounge & Cocktail Bar (Kloof St)", "Romantic lounge centered around an illuminated olive tree with live jazz & tapas", "4.8 ★ • R250"),
            Triple("Mojo Market (Sea Point Promenade)", "Lively oceanfront market with live music, artisanal bites & local craft beer", "4.7 ★ • R150"),
            Triple("The Secret Gin Bar (Honest Chocolate)", "Tucked behind a secret chocolate shop courtyard serving artisanal Fynbos gins", "4.9 ★ • R180"),
            Triple("Kalkys Fish & Chips (Kalk Bay Harbour)", "Iconic seaside spot for fresh fish & chips right on the rocky harbour with seals", "4.8 ★ • R120")
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "Nearby Cape Town Spots 📍",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = com.example.ui.theme.UsPlayTextPrimary
            )
        }

        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = UsPlayPlumCardElevated)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.linearGradient(
                                listOf(Color(0xFF00838F), Color(0xFF00ACC1))
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(imageVector = Icons.Default.Map, contentDescription = "Map", tint = Color.White, modifier = Modifier.size(36.dp))
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(text = "Interactive Cape Town Map Active 🇿🇦", fontWeight = FontWeight.Bold, color = Color.White)
                        Text(text = "Showing top recommended venues within 10 km in Cape Town", fontSize = 12.sp, color = Color.White.copy(alpha = 0.8f))
                    }
                }
            }
        }

        items(localSpots) { spot ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = UsPlayPlumCard)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = spot.first, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = com.example.ui.theme.UsPlayTextPrimary)
                        Text(text = spot.third, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = UsPlayGoldXP)
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(text = spot.second, fontSize = 12.sp, color = UsPlayTextSecondary)

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Button(
                            onClick = { },
                            colors = ButtonDefaults.buttonColors(containerColor = UsPlayRosePrimary),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.height(34.dp)
                        ) {
                            Icon(imageVector = Icons.Default.LocationOn, contentDescription = "Directions", modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "Get Directions", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        item { Spacer(modifier = Modifier.height(80.dp)) }
    }
}
