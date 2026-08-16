package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.UsPlayViewModel
import com.example.ui.components.LevelProgressCard
import com.example.ui.components.MysteryDiceRoller
import com.example.ui.components.UsPlayHeader
import com.example.ui.theme.UsPlayGoldXP
import com.example.ui.theme.UsPlayPlumCard
import com.example.ui.theme.UsPlayPlumCardElevated
import com.example.ui.theme.UsPlayRoseDark
import com.example.ui.theme.UsPlayRoseLight
import com.example.ui.theme.UsPlayRosePrimary
import com.example.ui.theme.UsPlayTextMuted
import com.example.ui.theme.UsPlayTextSecondary

@Composable
fun HomeScreen(
    viewModel: UsPlayViewModel,
    onNavigateToDatesTab: (subTab: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val profile by viewModel.coupleProfile.collectAsState()
    val levelInfo by viewModel.levelInfo.collectAsState()
    val challenge by viewModel.dailyChallenge.collectAsState()
    val recommendations by viewModel.coupleRecommendations.collectAsState()
    val refreshTime by viewModel.recommendationRefreshTime.collectAsState()
    val isRolling by viewModel.isRollingDice.collectAsState()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            UsPlayHeader(
                partner1 = profile?.partner1Name ?: "Sipho",
                partner2 = profile?.partner2Name ?: "Lerato",
                streakDays = profile?.streakDays ?: 1,
                levelTitle = levelInfo.title
            )
        }

        // Hero Image Banner
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .testTag("hero_banner_card"),
                shape = RoundedCornerShape(20.dp)
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Image(
                        painter = painterResource(id = R.drawable.img_hero_couples),
                        contentDescription = "Couples Date Night",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.8f))
                                )
                            )
                    )
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "Gamify Cape Town Date Nights 🇿🇦",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                        Text(
                            text = "Complete Mother City quests, earn XP & level up together!",
                            color = UsPlayTextSecondary,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }

        // Couple XP Level Card
        item {
            LevelProgressCard(levelInfo = levelInfo)
        }

        // Today's Date Challenge Card
        item {
            challenge?.let { currChallenge ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("todays_challenge_card"),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = UsPlayPlumCardElevated),
                    border = androidx.compose.foundation.BorderStroke(1.dp, UsPlayRosePrimary.copy(alpha = 0.5f))
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
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = UsPlayRosePrimary
                                ) {
                                    Text(
                                        text = "TODAY'S QUEST",
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = Color.White
                                    )
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = currChallenge.category,
                                    fontSize = 12.sp,
                                    color = UsPlayRosePrimary,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = UsPlayRoseDark.copy(alpha = 0.5f),
                                border = androidx.compose.foundation.BorderStroke(1.dp, UsPlayRosePrimary.copy(alpha = 0.4f))
                            ) {
                                Text(
                                    text = "✨ Free Daily Activity",
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = UsPlayRoseLight
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = currChallenge.title,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = com.example.ui.theme.UsPlayTextPrimary
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = currChallenge.description,
                            fontSize = 13.sp,
                            color = UsPlayTextSecondary,
                            lineHeight = 18.sp
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        if (currChallenge.isCompleted) {
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = Color(0xFF2E7D32).copy(alpha = 0.3f),
                                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF4CAF50)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 10.dp),
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = "Completed",
                                        tint = Color(0xFF81C784),
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "Today's Quest Completed! 💕",
                                        fontSize = 13.sp,
                                        color = Color(0xFF81C784),
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        } else {
                            Button(
                                onClick = { viewModel.completeDailyChallenge() },
                                colors = ButtonDefaults.buttonColors(containerColor = UsPlayRosePrimary),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(44.dp)
                                    .testTag("complete_todays_challenge_button")
                            ) {
                                Text(
                                    text = "COMPLETE TODAY'S QUEST",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                            }
                        }
                    }
                }
            }
        }

        // Current Streak Card
        item {
            val now = System.currentTimeMillis()
            val lastCheckIn = profile?.lastCheckInDateMillis ?: 0L
            val twentyFourHoursMs = 24 * 60 * 60 * 1000L
            val timeSinceCheckIn = now - lastCheckIn
            val canCheckIn = timeSinceCheckIn >= twentyFourHoursMs
            val hoursRemaining = if (canCheckIn) 0 else (((twentyFourHoursMs - timeSinceCheckIn) / (1000 * 60 * 60)) + 1).coerceAtLeast(1)

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("current_streak_card"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = UsPlayPlumCard)
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
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFFF5722).copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocalFireDepartment,
                                contentDescription = "Fire Streak",
                                tint = Color(0xFFFF5722),
                                modifier = Modifier.size(32.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            val streak = profile?.streakDays ?: 1
                            Text(
                                text = "$streak ${if (streak == 1) "Day" else "Days"} Streak! 🔥",
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold,
                                color = com.example.ui.theme.UsPlayTextPrimary
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = if (canCheckIn) "Check in daily to earn +5 XP & grow your streak" else "Checked in! Next check-in available in ${hoursRemaining}h",
                                fontSize = 12.sp,
                                color = UsPlayTextMuted,
                                lineHeight = 16.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(
                        onClick = { viewModel.performDailyCheckIn() },
                        enabled = canCheckIn,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = UsPlayRoseDark,
                            disabledContainerColor = UsPlayPlumCardElevated
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.testTag("daily_checkin_button")
                    ) {
                        Text(
                            text = if (canCheckIn) "CHECK IN (+5 XP)" else "CHECKED IN ✓",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (canCheckIn) Color.White else UsPlayTextMuted
                        )
                    }
                }
            }
        }

        // Surprise Us Mystery Dice section
        item {
            MysteryDiceRoller(
                isRolling = isRolling,
                onRollDice = {
                    viewModel.rollSurpriseDice()
                    onNavigateToDatesTab(1) // Navigate to Surprise Me tab in Dates
                }
            )
        }

        // 2-Day Couples Recommendations
        item {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Couples Recommendations ✨",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = com.example.ui.theme.UsPlayTextPrimary
                        )
                    }
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = UsPlayRoseDark.copy(alpha = 0.4f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, UsPlayRosePrimary.copy(alpha = 0.3f))
                    ) {
                        Text(
                            text = refreshTime,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = UsPlayRoseLight
                        )
                    }
                }

                Text(
                    text = "Fresh 100% free date recommendations curated for you every 2 days",
                    fontSize = 12.sp,
                    color = UsPlayTextMuted,
                    modifier = Modifier.padding(top = 2.dp, bottom = 10.dp)
                )

                recommendations.forEach { rec ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 5.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = UsPlayPlumCard)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = Color(0xFF2E7D32).copy(alpha = 0.35f),
                                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF4CAF50).copy(alpha = 0.5f))
                                    ) {
                                        Text(
                                            text = "💚 ${rec.cost}",
                                            modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp),
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF81C784)
                                        )
                                    }

                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = UsPlayRoseDark.copy(alpha = 0.5f)
                                    ) {
                                        Text(
                                            text = rec.category,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = UsPlayRoseLight
                                        )
                                    }

                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = Color.White.copy(alpha = 0.08f)
                                    ) {
                                        Text(
                                            text = rec.tag,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = UsPlayTextSecondary
                                        )
                                    }
                                }

                                Text(
                                    text = "⏱️ ${rec.duration}",
                                    fontSize = 11.sp,
                                    color = UsPlayTextMuted,
                                    fontWeight = FontWeight.Medium
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = rec.title,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = com.example.ui.theme.UsPlayTextPrimary
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = rec.description,
                                fontSize = 12.sp,
                                color = UsPlayTextSecondary,
                                lineHeight = 17.sp
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "📍 ${rec.location}",
                                    fontSize = 11.sp,
                                    color = UsPlayTextMuted
                                )

                                Button(
                                    onClick = { onNavigateToDatesTab(2) }, // Navigate to Date Planner
                                    colors = ButtonDefaults.buttonColors(containerColor = UsPlayRoseDark),
                                    shape = RoundedCornerShape(8.dp),
                                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                                    modifier = Modifier.height(32.dp)
                                ) {
                                    Text(
                                        text = "Plan This Date 📅",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}
