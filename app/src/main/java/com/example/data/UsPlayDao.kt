package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface UsPlayDao {

    // Couple Profile
    @Query("SELECT * FROM couple_profile WHERE isLoggedIn = 1 LIMIT 1")
    fun getCoupleProfile(): Flow<CoupleProfile?>

    @Query("SELECT * FROM couple_profile ORDER BY id ASC")
    fun getAllCoupleProfiles(): Flow<List<CoupleProfile>>

    @Query("SELECT * FROM couple_profile WHERE LOWER(email) = LOWER(:email) LIMIT 1")
    suspend fun getProfileByEmail(email: String): CoupleProfile?

    @Query("UPDATE couple_profile SET isLoggedIn = 0")
    suspend fun logoutAllCouples()

    @Query("UPDATE couple_profile SET isLoggedIn = 1 WHERE id = :id")
    suspend fun setLoggedInCouple(id: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateProfile(profile: CoupleProfile)

    // Date Ideas
    @Query("SELECT * FROM date_ideas ORDER BY title ASC")
    fun getAllDateIdeas(): Flow<List<DateIdea>>

    @Query("SELECT * FROM date_ideas WHERE category = :category ORDER BY title ASC")
    fun getDateIdeasByCategory(category: String): Flow<List<DateIdea>>

    @Query("SELECT * FROM date_ideas WHERE isSaved = 1 ORDER BY title ASC")
    fun getSavedDateIdeas(): Flow<List<DateIdea>>

    @Query("SELECT * FROM date_ideas WHERE isCompleted = 1 ORDER BY completedDateMillis DESC")
    fun getCompletedDateIdeas(): Flow<List<DateIdea>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDateIdeas(ideas: List<DateIdea>)

    @Query("DELETE FROM date_ideas WHERE category LIKE '%Naughty%'")
    suspend fun deleteNaughtyDates()

    @Update
    suspend fun updateDateIdea(idea: DateIdea)

    // Daily Challenge
    @Query("SELECT * FROM daily_challenges WHERE id = 1 LIMIT 1")
    fun getDailyChallenge(): Flow<DailyChallenge?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateDailyChallenge(challenge: DailyChallenge)

    // Weekly Missions
    @Query("SELECT * FROM weekly_missions")
    fun getWeeklyMissions(): Flow<List<WeeklyMission>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeeklyMissions(missions: List<WeeklyMission>)

    @Update
    suspend fun updateWeeklyMission(mission: WeeklyMission)

    // Community Posts
    @Query("SELECT * FROM community_posts ORDER BY likesCount DESC")
    fun getCommunityPosts(): Flow<List<CommunityPost>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCommunityPost(post: CommunityPost)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCommunityPosts(posts: List<CommunityPost>)

    @Update
    suspend fun updateCommunityPost(post: CommunityPost)

    // Planned Dates
    @Query("SELECT * FROM planned_dates ORDER BY dateText ASC")
    fun getPlannedDates(): Flow<List<PlannedDate>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlannedDate(plannedDate: PlannedDate)

    @Update
    suspend fun updatePlannedDate(plannedDate: PlannedDate)

    @Query("DELETE FROM planned_dates WHERE id = :id")
    suspend fun deletePlannedDate(id: String)
}
