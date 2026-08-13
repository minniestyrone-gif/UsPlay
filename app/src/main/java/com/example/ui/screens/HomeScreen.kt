package com.example.ui.screens

import androidx.compose.foundation.Image
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
    val missions by viewModel.weeklyMissions.collectAsState()
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
                                color = UsPlayGoldXP
                            ) {
                                Text(
                                    text = "+${currChallenge.xpReward} XP",
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black
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
                                        text = "Today's Quest Claimed! (+${currChallenge.xpReward} XP)",
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
                                    text = "COMPLETE TODAY'S QUEST (+${currChallenge.xpReward} XP)",
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
                    Row(verticalAlignment = Alignment.CenterVertically) {
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
                            Text(
                                text = "${profile?.streakDays ?: 1} Day Streak! 🔥",
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold,
                                color = com.example.ui.theme.UsPlayTextPrimary
                            )
                            Text(
                                text = "Check in daily or restart your streak",
                                fontSize = 12.sp,
                                color = UsPlayTextMuted
                            )
                        }
                    }

                    Button(
                        onClick = { viewModel.performDailyCheckIn() },
                        colors = ButtonDefaults.buttonColors(containerColor = UsPlayRoseDark),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.testTag("daily_checkin_button")
                    ) {
                        Text(
                            text = "CHECK IN (+50 XP)",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
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

        // Weekly Couples Missions
        item {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Weekly Couples Missions 🎯",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = com.example.ui.theme.UsPlayTextPrimary
                    )
                    Text(
                        text = "Resets in 2d",
                        fontSize = 12.sp,
                        color = UsPlayTextMuted
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                missions.forEach { mission ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = UsPlayPlumCard)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = mission.title,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = com.example.ui.theme.UsPlayTextPrimary
                                )
                                Text(
                                    text = "+${mission.xpReward} XP",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = UsPlayGoldXP
                                )
                            }

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = mission.description,
                                fontSize = 12.sp,
                                color = UsPlayTextSecondary
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                LinearProgressIndicator(
                                    progress = { (mission.currentProgress.toFloat() / mission.totalProgress.toFloat()).coerceIn(0f, 1f) },
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(8.dp)
                                        .clip(RoundedCornerShape(4.dp)),
                                    color = UsPlayRosePrimary,
                                    trackColor = com.example.ui.theme.UsPlayRoseLight.copy(alpha = 0.5f)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = "${mission.currentProgress}/${mission.totalProgress}",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = UsPlayTextMuted
                                )
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
