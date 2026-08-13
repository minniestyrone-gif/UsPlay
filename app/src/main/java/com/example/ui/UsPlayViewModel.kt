package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.CommunityPost
import com.example.data.CoupleProfile
import com.example.data.DailyChallenge
import com.example.data.DateIdea
import com.example.data.LevelInfo
import com.example.data.LevelSystem
import com.example.data.PlannedDate
import com.example.data.UsPlayDatabase
import com.example.data.UsPlayRepository
import com.example.data.WeeklyMission
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID

class UsPlayViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: UsPlayRepository

    init {
        val database = UsPlayDatabase.getDatabase(application)
        repository = UsPlayRepository(database.usPlayDao())
        viewModelScope.launch {
            repository.initializeSeedDataIfEmpty()
        }
    }

    val coupleProfile: StateFlow<CoupleProfile?> = repository.coupleProfile
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val allCouples: StateFlow<List<CoupleProfile>> = repository.allProfiles
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val levelInfo: StateFlow<LevelInfo> = coupleProfile.map { profile ->
        LevelSystem.calculateLevel(profile?.currentXp ?: 0)
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        LevelSystem.calculateLevel(0)
    )

    val allDateIdeas: StateFlow<List<DateIdea>> = repository.allDateIdeas
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val savedDateIdeas: StateFlow<List<DateIdea>> = repository.savedDateIdeas
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val completedDateIdeas: StateFlow<List<DateIdea>> = repository.completedDateIdeas
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val dailyChallenge: StateFlow<DailyChallenge?> = repository.dailyChallenge
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val weeklyMissions: StateFlow<List<WeeklyMission>> = repository.weeklyMissions
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val communityPosts: StateFlow<List<CommunityPost>> = repository.communityPosts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val plannedDates: StateFlow<List<PlannedDate>> = repository.plannedDates
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Mystery Dice Rolling State
    private val _isRollingDice = MutableStateFlow(false)
    val isRollingDice: StateFlow<Boolean> = _isRollingDice.asStateFlow()

    private val _rolledMysteryDate = MutableStateFlow<DateIdea?>(null)
    val rolledMysteryDate: StateFlow<DateIdea?> = _rolledMysteryDate.asStateFlow()

    // Notification / Toast Message State
    private val _userMessage = MutableStateFlow<String?>(null)
    val userMessage: StateFlow<String?> = _userMessage.asStateFlow()

    fun clearUserMessage() {
        _userMessage.value = null
    }

    fun performDailyCheckIn() {
        viewModelScope.launch {
            repository.performDailyCheckIn()
            _userMessage.value = "Daily Streak Increased! 🔥 +50 XP Earned!"
        }
    }

    fun completeDailyChallenge() {
        viewModelScope.launch {
            repository.completeDailyChallenge()
            _userMessage.value = "Today's Challenge Completed! 🎉 +150 XP!"
        }
    }

    fun toggleSaveDateIdea(idea: DateIdea) {
        viewModelScope.launch {
            repository.toggleSaveDateIdea(idea)
            _userMessage.value = if (!idea.isSaved) "Saved to your Date Bucket List! ❤️" else "Removed from Saved Dates"
        }
    }

    fun completeDateIdea(idea: DateIdea) {
        viewModelScope.launch {
            repository.completeDateIdea(idea)
            _userMessage.value = "Date Completed! +${idea.xpReward} XP Added to Couple Level! ✨"
        }
    }

    fun rollSurpriseDice(selectedCategory: String? = null) {
        viewModelScope.launch {
            _isRollingDice.value = true
            _rolledMysteryDate.value = null
            delay(1200) // Animated dice roll duration

            val list = allDateIdeas.value
            val filtered = if (selectedCategory.isNullOrBlank() || selectedCategory == "All") {
                list
            } else {
                list.filter { it.category.contains(selectedCategory, ignoreCase = true) }
            }

            val picked = if (filtered.isNotEmpty()) filtered.random() else list.randomOrNull()
            _rolledMysteryDate.value = picked
            _isRollingDice.value = false
            _userMessage.value = "Surprise Date Unlocked! 🎲✨"
        }
    }

    fun addPlannedDate(title: String, dateText: String, timeText: String, location: String, budget: String, notes: String) {
        viewModelScope.launch {
            val newPlanned = PlannedDate(
                id = UUID.randomUUID().toString(),
                title = title,
                dateText = dateText,
                timeText = timeText,
                location = location,
                budget = budget,
                notes = notes,
                checklistItems = "Confirm Reservations|Prepare Outfits|Set Reminders",
                isCompleted = false
            )
            repository.addPlannedDate(newPlanned)
            _userMessage.value = "Date Night Scheduled! 🗓️"
        }
    }

    fun deletePlannedDate(id: String) {
        viewModelScope.launch {
            repository.deletePlannedDate(id)
            _userMessage.value = "Planned date removed"
        }
    }

    fun toggleLikeCommunityPost(post: CommunityPost) {
        viewModelScope.launch {
            repository.toggleLikeCommunityPost(post)
        }
    }

    fun addCommunityPost(title: String, category: String, description: String) {
        viewModelScope.launch {
            val profile = coupleProfile.value
            val names = "${profile?.partner1Name ?: "Alex"} & ${profile?.partner2Name ?: "Taylor"}"
            val lvl = levelInfo.value
            val newPost = CommunityPost(
                id = UUID.randomUUID().toString(),
                coupleNames = names,
                title = title,
                category = category,
                description = description,
                likesCount = 1,
                isLiked = true,
                achievementBadge = lvl.title,
                timeAgo = "Just now"
            )
            repository.addCommunityPost(newPost)
            repository.addXpAndStreak(50) // Bonus XP for sharing date ideas with community
            _userMessage.value = "Posted to Couples Socials! +50 Community XP! 🌟"
        }
    }

    fun login(email: String, pass: String) {
        viewModelScope.launch {
            val (success, msg) = repository.login(email, pass)
            _userMessage.value = msg
        }
    }

    fun signUp(
        partner1: String,
        partner2: String,
        email: String,
        pass: String,
        anniversary: String,
        bio: String
    ) {
        viewModelScope.launch {
            val (success, msg) = repository.signUpNewCouple(partner1, partner2, email, pass, anniversary, bio)
            _userMessage.value = msg
        }
    }

    fun loginAsDemoAccount(profile: CoupleProfile) {
        viewModelScope.launch {
            val (success, msg) = repository.loginAsProfile(profile.id)
            _userMessage.value = "Signed in as ${profile.partner1Name} & ${profile.partner2Name}! 💕"
        }
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
            _userMessage.value = "Logged out of UsPlay. See you soon! 👋"
        }
    }

    fun updateRelationshipProfile(partner1: String, partner2: String, anniversary: String, bio: String) {
        viewModelScope.launch {
            val current = coupleProfile.value ?: CoupleProfile()
            repository.updateProfile(
                current.copy(
                    partner1Name = partner1,
                    partner2Name = partner2,
                    relationshipStartDate = anniversary,
                    bio = bio
                )
            )
            _userMessage.value = "Profile Updated! 💕"
        }
    }
}
