package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Share
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
import com.example.data.CommunityPost
import com.example.ui.UsPlayViewModel
import com.example.ui.theme.UsPlayGoldXP
import com.example.ui.theme.UsPlayPlumCard
import com.example.ui.theme.UsPlayPlumCardElevated
import com.example.ui.theme.UsPlayRoseDark
import com.example.ui.theme.UsPlayRosePrimary
import com.example.ui.theme.UsPlayTextMuted
import com.example.ui.theme.UsPlayTextSecondary

@Composable
fun UsScreen(
    viewModel: UsPlayViewModel,
    modifier: Modifier = Modifier
) {
    val posts by viewModel.communityPosts.collectAsState()
    var showShareDialog by remember { mutableStateOf(false) }

    var newTitle by remember { mutableStateOf("") }
    var newCategory by remember { mutableStateOf("❤️ Romantic") }
    var newDesc by remember { mutableStateOf("") }

    val publicAchievements = remember {
        listOf(
            "Sipho & Lerato reached Level 10 CPT Power Couple! 👑",
            "Liam & Chloe completed Kirstenbosch Galileo Night! 🔥",
            "Jabu & Tash hit a 14-Day Streak in Mother City! 🔥",
            "Keanu & Zola unlocked Legendary Cape Town Couple! 🌟"
        )
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Couples Socials & Us 💖",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = com.example.ui.theme.UsPlayTextPrimary
                    )
                    Text(
                        text = "Public achievements & community date ideas",
                        fontSize = 12.sp,
                        color = UsPlayTextMuted
                    )
                }

                Button(
                    onClick = { showShareDialog = !showShareDialog },
                    colors = ButtonDefaults.buttonColors(containerColor = UsPlayRosePrimary),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.testTag("share_idea_button")
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Share")
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Share Date", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        // Public Achievements Ticker Carousel
        item {
            Column {
                Text(
                    text = "Public Achievements 🏆",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = com.example.ui.theme.UsPlayTextPrimary
                )
                Spacer(modifier = Modifier.height(8.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    items(publicAchievements) { ach ->
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = UsPlayPlumCardElevated),
                            border = androidx.compose.foundation.BorderStroke(1.dp, UsPlayGoldXP.copy(alpha = 0.5f))
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.EmojiEvents,
                                    contentDescription = "Trophy",
                                    tint = UsPlayGoldXP,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = ach,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = com.example.ui.theme.UsPlayTextPrimary
                                )
                            }
                        }
                    }
                }
            }
        }

        if (showShareDialog) {
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
                            text = "Share Your Date Idea with Couples",
                            fontWeight = FontWeight.Bold,
                            color = com.example.ui.theme.UsPlayTextPrimary,
                            fontSize = 16.sp
                        )

                        OutlinedTextField(
                            value = newTitle,
                            onValueChange = { newTitle = it },
                            label = { Text("Date Idea Title") },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(focusedTextColor = com.example.ui.theme.UsPlayTextPrimary, unfocusedTextColor = com.example.ui.theme.UsPlayTextPrimary)
                        )

                        OutlinedTextField(
                            value = newDesc,
                            onValueChange = { newDesc = it },
                            label = { Text("Describe what you did & why it was awesome!") },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(focusedTextColor = com.example.ui.theme.UsPlayTextPrimary, unfocusedTextColor = com.example.ui.theme.UsPlayTextPrimary)
                        )

                        Button(
                            onClick = {
                                if (newTitle.isNotBlank() && newDesc.isNotBlank()) {
                                    viewModel.addCommunityPost(newTitle, newCategory, newDesc)
                                    newTitle = ""; newDesc = ""
                                    showShareDialog = false
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = UsPlayRosePrimary),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("POST TO COUPLES COMMUNITY (+50 XP)", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        item {
            Text(
                text = "Couples Sharing Date Ideas 💡",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = com.example.ui.theme.UsPlayTextPrimary
            )
        }

        items(posts) { post ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("community_post_card_${post.id}"),
                shape = RoundedCornerShape(18.dp),
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
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(UsPlayRoseDark),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = post.coupleNames.take(1),
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = post.coupleNames,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                    color = com.example.ui.theme.UsPlayTextPrimary
                                )
                                Text(
                                    text = post.achievementBadge ?: "Community Couple",
                                    fontSize = 11.sp,
                                    color = UsPlayGoldXP
                                )
                            }
                        }

                        Text(
                            text = post.timeAgo,
                            fontSize = 11.sp,
                            color = UsPlayTextMuted
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = post.title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = com.example.ui.theme.UsPlayTextPrimary
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = post.description,
                        fontSize = 13.sp,
                        color = UsPlayTextSecondary,
                        lineHeight = 18.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = UsPlayRosePrimary.copy(alpha = 0.2f)
                        ) {
                            Text(
                                text = post.category,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                fontSize = 11.sp,
                                color = UsPlayRosePrimary,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(onClick = { viewModel.toggleLikeCommunityPost(post) }) {
                                Icon(
                                    imageVector = if (post.isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                    contentDescription = "Like",
                                    tint = if (post.isLiked) UsPlayRosePrimary else com.example.ui.theme.UsPlayTextMuted
                                )
                            }
                            Text(
                                text = "${post.likesCount}",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = com.example.ui.theme.UsPlayTextPrimary
                            )
                        }
                    }
                }
            }
        }

        item { Spacer(modifier = Modifier.height(80.dp)) }
    }
}
