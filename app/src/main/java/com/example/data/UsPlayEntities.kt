package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "couple_profile")
data class CoupleProfile(
    @PrimaryKey val id: String = "couple_1",
    val email: String = "sipho.lerato@usplay.com",
    val password: String = "cape123",
    val partner1Name: String = "Sipho",
    val partner2Name: String = "Lerato",
    val relationshipStartDate: String = "Oct 14, 2023",
    val currentXp: Int = 1250,
    val streakDays: Int = 6,
    val lastCheckInDateMillis: Long = System.currentTimeMillis(),
    val avatarStyle: String = "romantic_duo",
    val bio: String = "Exploring Cape Town together, one Mother City adventure at a time! 💕 Cape Town Vibe 🇿🇦",
    val isLoggedIn: Boolean = true
)

@Entity(tableName = "date_ideas")
data class DateIdea(
    @PrimaryKey val id: String,
    val title: String,
    val category: String, // Romantic, Funny, Spicy, Relaxing, Adventurous, Intellectual, Naughty
    val description: String,
    val xpReward: Int,
    val duration: String, // e.g., "1-2 hrs", "Evening", "All Day"
    val budget: String,   // e.g., "Free", "$", "$$", "$$$"
    val isSaved: Boolean = false,
    val isCompleted: Boolean = false,
    val completedDateMillis: Long? = null,
    val isMystery: Boolean = false,
    val locationName: String? = null
)

@Entity(tableName = "daily_challenges")
data class DailyChallenge(
    @PrimaryKey val id: Int = 1,
    val title: String,
    val description: String,
    val xpReward: Int,
    val category: String,
    val isCompleted: Boolean = false,
    val dateFormatted: String = "Today"
)

@Entity(tableName = "weekly_missions")
data class WeeklyMission(
    @PrimaryKey val id: String,
    val title: String,
    val description: String,
    val xpReward: Int,
    val currentProgress: Int,
    val totalProgress: Int,
    val isCompleted: Boolean = false
)

@Entity(tableName = "community_posts")
data class CommunityPost(
    @PrimaryKey val id: String,
    val coupleNames: String,
    val title: String,
    val category: String,
    val description: String,
    val likesCount: Int,
    val isLiked: Boolean = false,
    val achievementBadge: String? = null,
    val timeAgo: String
)

@Entity(tableName = "planned_dates")
data class PlannedDate(
    @PrimaryKey val id: String,
    val title: String,
    val dateText: String,
    val timeText: String,
    val location: String,
    val budget: String,
    val notes: String,
    val checklistItems: String, // Pipe-separated list
    val isCompleted: Boolean = false
)
