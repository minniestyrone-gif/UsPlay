package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.LevelSystem
import com.example.ui.UsPlayViewModel
import com.example.ui.components.LevelProgressCard
import com.example.ui.theme.UsPlayGoldXP
import com.example.ui.theme.UsPlayPlumCard
import com.example.ui.theme.UsPlayPlumCardElevated
import com.example.ui.theme.UsPlayRoseDark
import com.example.ui.theme.UsPlayRosePrimary
import com.example.ui.theme.UsPlayTextMuted
import com.example.ui.theme.UsPlayTextSecondary

@Composable
fun ProfileScreen(
    viewModel: UsPlayViewModel,
    modifier: Modifier = Modifier
) {
    val profile by viewModel.coupleProfile.collectAsState()
    val levelInfo by viewModel.levelInfo.collectAsState()
    val completedDates by viewModel.completedDateIdeas.collectAsState()

    var showEditProfileDialog by remember { mutableStateOf(false) }

    var p1Name by remember { mutableStateOf(profile?.partner1Name ?: "Sipho") }
    var p2Name by remember { mutableStateOf(profile?.partner2Name ?: "Lerato") }
    var annivDate by remember { mutableStateOf(profile?.relationshipStartDate ?: "Oct 14, 2023") }
    var coupleBio by remember { mutableStateOf(profile?.bio ?: "Exploring Cape Town together, one Mother City adventure at a time! 💕 Cape Town Vibe 🇿🇦") }

    val levelMilestones = remember {
        listOf(
            Triple(1, "Level 1 — First Date", "Starting your journey together! 🌱"),
            Triple(5, "Level 5 — Getting Serious", "Building unforgettable memories! 🔥"),
            Triple(10, "Level 10 — Power Couple", "Unstoppable romantic synergy! 💎"),
            Triple(25, "Level 25 — Relationship Adventurers", "Master date night planners! 🌟"),
            Triple(50, "Level 50 — Legendary Couple", "Hall of Fame Couple status! 👑")
        )
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Relationship Profile Header Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
                    .testTag("relationship_profile_card"),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = UsPlayPlumCardElevated),
                border = androidx.compose.foundation.BorderStroke(1.dp, UsPlayRosePrimary)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.linearGradient(
                                    listOf(UsPlayRosePrimary, UsPlayRoseDark)
                                )
                            )
                            .border(3.dp, Color.White, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "${(profile?.partner1Name ?: "S").take(1)}&${(profile?.partner2Name ?: "L").take(1)}",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "${profile?.partner1Name ?: "Sipho"} & ${profile?.partner2Name ?: "Lerato"}",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = com.example.ui.theme.UsPlayTextPrimary
                    )

                    Text(
                        text = "Together since ${profile?.relationshipStartDate ?: "Oct 14, 2023"}",
                        fontSize = 12.sp,
                        color = UsPlayGoldXP,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = profile?.bio ?: "Exploring the world together!",
                        fontSize = 13.sp,
                        color = UsPlayTextSecondary,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = { showEditProfileDialog = !showEditProfileDialog },
                        colors = ButtonDefaults.buttonColors(containerColor = UsPlayRoseDark),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.testTag("edit_profile_button")
                    ) {
                        Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit", modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = "Edit Relationship Profile", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        if (showEditProfileDialog) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = UsPlayPlumCard)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(text = "Edit Relationship Profile", fontWeight = FontWeight.Bold, color = com.example.ui.theme.UsPlayTextPrimary, fontSize = 16.sp)

                        OutlinedTextField(
                            value = p1Name,
                            onValueChange = { p1Name = it },
                            label = { Text("Partner 1 Name") },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(focusedTextColor = com.example.ui.theme.UsPlayTextPrimary, unfocusedTextColor = com.example.ui.theme.UsPlayTextPrimary)
                        )

                        OutlinedTextField(
                            value = p2Name,
                            onValueChange = { p2Name = it },
                            label = { Text("Partner 2 Name") },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(focusedTextColor = com.example.ui.theme.UsPlayTextPrimary, unfocusedTextColor = com.example.ui.theme.UsPlayTextPrimary)
                        )

                        OutlinedTextField(
                            value = annivDate,
                            onValueChange = { annivDate = it },
                            label = { Text("Anniversary Date") },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(focusedTextColor = com.example.ui.theme.UsPlayTextPrimary, unfocusedTextColor = com.example.ui.theme.UsPlayTextPrimary)
                        )

                        OutlinedTextField(
                            value = coupleBio,
                            onValueChange = { coupleBio = it },
                            label = { Text("Couple Motto / Bio") },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(focusedTextColor = com.example.ui.theme.UsPlayTextPrimary, unfocusedTextColor = com.example.ui.theme.UsPlayTextPrimary)
                        )

                        Button(
                            onClick = {
                                viewModel.updateRelationshipProfile(p1Name, p2Name, annivDate, coupleBio)
                                showEditProfileDialog = false
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = UsPlayRosePrimary),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("SAVE CHANGES", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Stats Overview Bar
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = UsPlayPlumCard)
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        val streak = profile?.streakDays ?: 1
                        Text(text = "🔥 $streak ${if (streak == 1) "Day" else "Days"}", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = com.example.ui.theme.UsPlayTextPrimary)
                        Text(text = "Current Streak", fontSize = 11.sp, color = UsPlayTextMuted)
                    }
                }

                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = UsPlayPlumCard)
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "✨ ${levelInfo.currentXp} XP", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = UsPlayGoldXP)
                        Text(text = "Total XP", fontSize = 11.sp, color = UsPlayTextMuted)
                    }
                }

                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = UsPlayPlumCard)
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "🎉 ${completedDates.size}", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = com.example.ui.theme.UsPlayTextPrimary)
                        Text(text = "Dates Completed", fontSize = 11.sp, color = UsPlayTextMuted)
                    }
                }
            }
        }

        // Level Milestones Progression
        item {
            var showDetailedBreakdown by remember { mutableStateOf(false) }

            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Couple XP Level Tiers & Milestones 👑",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = com.example.ui.theme.UsPlayTextPrimary
                    )
                    
                    IconButton(onClick = { showDetailedBreakdown = !showDetailedBreakdown }) {
                        Icon(
                            imageVector = if (showDetailedBreakdown) Icons.Default.Star else Icons.Default.Star,
                            contentDescription = "Toggle Level List",
                            tint = UsPlayGoldXP
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                levelMilestones.forEach { milestone ->
                    val targetLevel = milestone.first
                    val requiredTotalXp = LevelSystem.xpRequiredForLevel(targetLevel)
                    val isUnlocked = levelInfo.level >= targetLevel
                    val xpNeeded = LevelSystem.xpNeededToUnlock(levelInfo.currentXp, targetLevel)

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isUnlocked) UsPlayPlumCardElevated else UsPlayPlumCard.copy(alpha = 0.5f)
                        ),
                        border = if (isUnlocked) androidx.compose.foundation.BorderStroke(1.dp, UsPlayGoldXP) else null
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                modifier = Modifier.weight(1f),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(CircleShape)
                                        .background(if (isUnlocked) UsPlayGoldXP else Color.White.copy(alpha = 0.1f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = if (isUnlocked) Icons.Default.EmojiEvents else Icons.Default.Lock,
                                        contentDescription = "Milestone",
                                        tint = if (isUnlocked) Color.Black else Color.White.copy(alpha = 0.4f),
                                        modifier = Modifier.size(22.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.width(12.dp))

                                Column {
                                    Text(
                                        text = milestone.second,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isUnlocked) com.example.ui.theme.UsPlayTextPrimary else UsPlayTextMuted
                                    )
                                    Text(
                                        text = milestone.third,
                                        fontSize = 12.sp,
                                        color = if (isUnlocked) UsPlayGoldXP else UsPlayTextMuted
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = if (isUnlocked) "Requires $requiredTotalXp Total XP (Unlocked! 🎉)" else "Requires $requiredTotalXp Total XP • $xpNeeded XP needed to unlock",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isUnlocked) UsPlayRosePrimary else UsPlayGoldXP
                                    )
                                }
                            }

                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = if (isUnlocked) UsPlayRosePrimary else Color.White.copy(alpha = 0.1f)
                            ) {
                                Text(
                                    text = if (isUnlocked) "UNLOCKED" else "$xpNeeded XP NEEDED",
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Toggleable detailed XP list for upcoming levels
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = UsPlayPlumCard)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "⚡ Upcoming Levels XP Guide",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = com.example.ui.theme.UsPlayTextPrimary
                            )
                            Button(
                                onClick = { showDetailedBreakdown = !showDetailedBreakdown },
                                colors = ButtonDefaults.buttonColors(containerColor = UsPlayRoseDark),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = if (showDetailedBreakdown) "Hide List" else "Show All Levels",
                                    fontSize = 11.sp
                                )
                            }
                        }

                        if (showDetailedBreakdown) {
                            Spacer(modifier = Modifier.height(12.dp))
                            val upcomingLevels = (1..15).toList()
                            upcomingLevels.forEach { lvl ->
                                val reqXp = LevelSystem.xpRequiredForLevel(lvl)
                                val needed = LevelSystem.xpNeededToUnlock(levelInfo.currentXp, lvl)
                                val isCurrent = levelInfo.level == lvl
                                val isPassed = levelInfo.level > lvl

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp)
                                        .background(
                                            if (isCurrent) UsPlayGoldXP.copy(alpha = 0.15f) else Color.Transparent,
                                            shape = RoundedCornerShape(8.dp)
                                        )
                                        .padding(horizontal = 8.dp, vertical = 6.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = LevelSystem.getTierIcon(lvl),
                                            fontSize = 16.sp
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = "Level $lvl",
                                            fontWeight = if (isCurrent) FontWeight.ExtraBold else FontWeight.Medium,
                                            fontSize = 13.sp,
                                            color = if (isCurrent) UsPlayGoldXP else com.example.ui.theme.UsPlayTextPrimary
                                        )
                                        if (isCurrent) {
                                            Text(
                                                text = " (Current)",
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = UsPlayRosePrimary
                                            )
                                        }
                                    }

                                    Text(
                                        text = when {
                                            isPassed -> "Unlocked ($reqXp XP)"
                                            isCurrent -> "${levelInfo.xpNeededForNextLevel} XP needed for Level ${lvl + 1}"
                                            else -> "$reqXp Total XP ($needed XP needed)"
                                        },
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = if (isPassed) UsPlayTextMuted else UsPlayGoldXP
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Account & Security Card (Logout)
        item {
            var showLogoutDialog by remember { mutableStateOf(false) }

            if (showLogoutDialog) {
                androidx.compose.material3.AlertDialog(
                    onDismissRequest = { showLogoutDialog = false },
                    title = { Text(text = "Log Out of UsPlay?", color = Color.White, fontWeight = FontWeight.Bold) },
                    text = { Text(text = "Are you sure you want to log out of your couple account (${profile?.partner1Name} & ${profile?.partner2Name})?", color = UsPlayTextMuted) },
                    confirmButton = {
                        Button(
                            onClick = {
                                showLogoutDialog = false
                                viewModel.logout()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = UsPlayRosePrimary)
                        ) {
                            Text("Log Out", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    },
                    dismissButton = {
                        androidx.compose.material3.TextButton(
                            onClick = { showLogoutDialog = false }
                        ) {
                            Text("Cancel", color = UsPlayTextMuted)
                        }
                    },
                    containerColor = UsPlayPlumCardElevated,
                    shape = RoundedCornerShape(20.dp)
                )
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = UsPlayPlumCard)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "Account & Security 🔒",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Signed in as",
                                fontSize = 11.sp,
                                color = UsPlayTextMuted
                            )
                            Text(
                                text = profile?.email ?: "sipho.lerato@usplay.com",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White
                            )
                        }

                        Button(
                            onClick = { showLogoutDialog = true },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F)),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.testTag("logout_btn")
                        ) {
                            Text(text = "Log Out", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 13.sp)
                        }
                    }
                }
            }
        }


        item { Spacer(modifier = Modifier.height(80.dp)) }
    }
}
