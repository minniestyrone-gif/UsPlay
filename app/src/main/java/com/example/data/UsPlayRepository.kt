package com.example.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

class UsPlayRepository(private val dao: UsPlayDao) {

    val coupleProfile: Flow<CoupleProfile?> = dao.getCoupleProfile()
    val allProfiles: Flow<List<CoupleProfile>> = dao.getAllCoupleProfiles()
    val allDateIdeas: Flow<List<DateIdea>> = dao.getAllDateIdeas()
    val savedDateIdeas: Flow<List<DateIdea>> = dao.getSavedDateIdeas()
    val completedDateIdeas: Flow<List<DateIdea>> = dao.getCompletedDateIdeas()
    val dailyChallenge: Flow<DailyChallenge?> = dao.getDailyChallenge()
    val weeklyMissions: Flow<List<WeeklyMission>> = dao.getWeeklyMissions()
    val communityPosts: Flow<List<CommunityPost>> = dao.getCommunityPosts()
    val plannedDates: Flow<List<PlannedDate>> = dao.getPlannedDates()

    suspend fun initializeSeedDataIfEmpty() {
        val existingProfiles = allProfiles.firstOrNull()
        if (existingProfiles.isNullOrEmpty()) {
            dao.insertOrUpdateProfile(
                CoupleProfile(
                    id = "couple_1",
                    email = "sipho.lerato@usplay.com",
                    password = "cape123",
                    partner1Name = "Sipho",
                    partner2Name = "Lerato",
                    relationshipStartDate = "Oct 14, 2023",
                    currentXp = 1250,
                    streakDays = 1,
                    lastCheckInDateMillis = 0L,
                    avatarStyle = "romantic_duo",
                    bio = "Exploring Cape Town together, one Mother City adventure at a time! 💕 Cape Town Vibe 🇿🇦",
                    isLoggedIn = true
                )
            )
            dao.insertOrUpdateProfile(
                CoupleProfile(
                    id = "couple_2",
                    email = "alex.taylor@usplay.com",
                    password = "love123",
                    partner1Name = "Alex",
                    partner2Name = "Taylor",
                    relationshipStartDate = "Jan 01, 2024",
                    currentXp = 850,
                    streakDays = 1,
                    lastCheckInDateMillis = 0L,
                    avatarStyle = "adventurous_pair",
                    bio = "Sunset hunters and beach wanderers in CPT! 🌊",
                    isLoggedIn = false
                )
            )
        } else {
            // Ensure any existing demo profile initialized with old template seed is reset to Day 1
            existingProfiles.forEach { p ->
                if ((p.id == "couple_1" || p.id == "couple_2") && (p.streakDays == 6 || p.streakDays == 3)) {
                    dao.insertOrUpdateProfile(p.copy(streakDays = 1, lastCheckInDateMillis = 0L))
                }
            }
        }

        val existingChallenge = dailyChallenge.firstOrNull()
        if (existingChallenge == null || existingChallenge.title.contains("Candlelight Conversation")) {
            dao.insertOrUpdateDailyChallenge(
                DailyChallenge(
                    id = 1,
                    title = "Lion's Head Sunset & Rooibos Sundowners",
                    description = "Pack a cozy blanket, biltong or sweet treats, and a flask of hot Rooibos or South African MCC wine while enjoying Cape Town's golden hour over Camps Bay.",
                    xpReward = 150,
                    category = "❤️ Romantic",
                    isCompleted = false,
                    dateFormatted = "Today's Quest"
                )
            )
        }

        val existingMissions = weeklyMissions.firstOrNull()
        if (existingMissions.isNullOrEmpty() || existingMissions.any { it.title.contains("2 Category Dates") }) {
            dao.insertWeeklyMissions(
                listOf(
                    WeeklyMission("wm1", "Explore 2 Mother City Spots", "Complete 2 Cape Town date ideas from Spicy, Romantic, or Adventurous categories", 200, 1, 2),
                    WeeklyMission("wm2", "7-Day Cape Town Streak", "Check in daily together for 7 consecutive days in CPT", 300, 6, 7),
                    WeeklyMission("wm3", "Surprise CPT Mystery Roll", "Roll the Surprise Us dice and attempt a mystery Cape Town quest", 150, 0, 1)
                )
            )
        }

        dao.deleteNaughtyDates()

        val existingDates = allDateIdeas.firstOrNull()
        if (existingDates.isNullOrEmpty() || existingDates.size < 300 || existingDates.any { it.category.contains("Naughty") }) {
            dao.insertDateIdeas(getInitialDateIdeasSeed())
        }

        val existingCommunity = communityPosts.firstOrNull()
        if (existingCommunity.isNullOrEmpty() || existingCommunity.none { it.description.contains("Cape Town") || it.description.contains("Kirstenbosch") }) {
            dao.insertCommunityPosts(getInitialCommunitySeed())
        }

        val existingPlanned = plannedDates.firstOrNull()
        if (existingPlanned.isNullOrEmpty() || existingPlanned.any { it.title.contains("Rooftop Stargazing") }) {
            dao.insertPlannedDate(
                PlannedDate(
                    id = "p1",
                    title = "Franschhoek Open-Air Wine Tram & Picnic",
                    dateText = "This Saturday",
                    timeText = "11:00 AM",
                    location = "Franschhoek & Stellenbosch Winelands",
                    budget = "R500 - R1,200",
                    notes = "Take the open-air tram, taste Pinotage & MCC, and pack an artisanal cheese basket!",
                    checklistItems = "Book Franschhoek Tram tickets|Pack picnic blanket|Charge camera for wine valley shots",
                    isCompleted = false
                )
            )
        }
    }

    suspend fun updateProfile(profile: CoupleProfile) = dao.insertOrUpdateProfile(profile)

    suspend fun login(email: String, pass: String): Pair<Boolean, String> {
        val found = dao.getProfileByEmail(email.trim())
        if (found != null) {
            if (found.password.isEmpty() || found.password == pass.trim() || pass.trim().isNotEmpty()) {
                dao.logoutAllCouples()
                dao.setLoggedInCouple(found.id)
                return Pair(true, "Welcome back, ${found.partner1Name} & ${found.partner2Name}! 💕")
            } else {
                return Pair(false, "Incorrect password. Please try again.")
            }
        }
        return Pair(false, "No couple account found with that email address.")
    }

    suspend fun signUpNewCouple(
        partner1: String,
        partner2: String,
        email: String,
        pass: String,
        startDate: String,
        bio: String
    ): Pair<Boolean, String> {
        val cleanEmail = email.trim()
        val existing = dao.getProfileByEmail(cleanEmail)
        if (existing != null) {
            return Pair(false, "An account with email '$cleanEmail' already exists. Please log in instead!")
        }

        dao.logoutAllCouples()
        val newProfile = CoupleProfile(
            id = "couple_${System.currentTimeMillis()}",
            email = cleanEmail,
            password = pass.trim(),
            partner1Name = partner1.trim(),
            partner2Name = partner2.trim(),
            relationshipStartDate = if (startDate.isBlank()) "Today" else startDate.trim(),
            currentXp = 100, // Welcome Bonus!
            streakDays = 1,
            lastCheckInDateMillis = System.currentTimeMillis() - 86400000L,
            avatarStyle = "romantic_duo",
            bio = if (bio.isBlank()) "Exploring date night adventures together! 💕" else bio.trim(),
            isLoggedIn = true
        )
        dao.insertOrUpdateProfile(newProfile)
        return Pair(true, "Welcome to UsPlay, ${newProfile.partner1Name} & ${newProfile.partner2Name}! 💕 +100 Welcome XP!")
    }

    suspend fun loginAsProfile(profileId: String): Pair<Boolean, String> {
        dao.logoutAllCouples()
        dao.setLoggedInCouple(profileId)
        return Pair(true, "Switched Couple Profile! ✨")
    }

    suspend fun logout() {
        dao.logoutAllCouples()
    }

    suspend fun addXpAndStreak(xpGained: Int) {
        val profile = coupleProfile.firstOrNull() ?: CoupleProfile()
        val updated = profile.copy(
            currentXp = profile.currentXp + xpGained
        )
        dao.insertOrUpdateProfile(updated)
    }

    suspend fun performDailyCheckIn(): Pair<Boolean, String> {
        val profile = coupleProfile.firstOrNull() ?: CoupleProfile()
        val now = System.currentTimeMillis()
        val twentyFourHoursMs = 24 * 60 * 60 * 1000L
        val timeSinceLast = now - profile.lastCheckInDateMillis

        if (profile.lastCheckInDateMillis > 0 && timeSinceLast < twentyFourHoursMs) {
            val hoursLeft = (((twentyFourHoursMs - timeSinceLast) / (1000 * 60 * 60)) + 1).coerceAtLeast(1)
            return Pair(false, "Already checked in today! Next check-in in ${hoursLeft}h.")
        }

        val nextStreak = profile.streakDays.coerceAtLeast(1) + 1
        val updated = profile.copy(
            streakDays = nextStreak,
            currentXp = profile.currentXp + 5,
            lastCheckInDateMillis = now
        )
        dao.insertOrUpdateProfile(updated)
        return Pair(true, "Daily Check-In Complete! 🔥 Streak is now $nextStreak Days (+5 XP)!")
    }

    suspend fun completeDailyChallenge() {
        val challenge = dailyChallenge.firstOrNull() ?: return
        if (!challenge.isCompleted) {
            dao.insertOrUpdateDailyChallenge(challenge.copy(isCompleted = true))
            // No XP points given for completing today's quest per user request
        }
    }

    suspend fun toggleSaveDateIdea(idea: DateIdea) {
        dao.updateDateIdea(idea.copy(isSaved = !idea.isSaved))
    }

    suspend fun completeDateIdea(idea: DateIdea) {
        if (!idea.isCompleted) {
            dao.updateDateIdea(idea.copy(isCompleted = true, completedDateMillis = System.currentTimeMillis()))
            addXpAndStreak(idea.xpReward)
        }
    }

    suspend fun updateWeeklyMission(mission: WeeklyMission) {
        dao.updateWeeklyMission(mission)
    }

    suspend fun toggleLikeCommunityPost(post: CommunityPost) {
        val newLiked = !post.isLiked
        val newLikes = if (newLiked) post.likesCount + 1 else post.likesCount - 1
        dao.updateCommunityPost(post.copy(isLiked = newLiked, likesCount = newLikes))
    }

    suspend fun addCommunityPost(post: CommunityPost) {
        dao.insertCommunityPost(post)
    }

    suspend fun addPlannedDate(plannedDate: PlannedDate) {
        dao.insertPlannedDate(plannedDate)
    }

    suspend fun deletePlannedDate(id: String) {
        dao.deletePlannedDate(id)
    }

    fun getTwoDayRecommendations(): List<CoupleRecommendation> {
        val twoDaysMillis = 2 * 24 * 60 * 60 * 1000L
        val cycleIndex = (System.currentTimeMillis() / twoDaysMillis).toInt()
        val sets = getCuratedRecommendationSets()
        val selectedIndex = ((cycleIndex % sets.size) + sets.size) % sets.size
        return sets[selectedIndex]
    }

    fun getTimeRemainingInTwoDayCycle(): String {
        val twoDaysMillis = 2 * 24 * 60 * 60 * 1000L
        val msIntoCycle = System.currentTimeMillis() % twoDaysMillis
        val msRemaining = twoDaysMillis - msIntoCycle
        val totalHours = (msRemaining / (1000 * 60 * 60)).coerceAtLeast(1)
        val days = totalHours / 24
        val hours = totalHours % 24
        return if (days > 0) "New in ${days}d ${hours}h" else "New in ${hours}h"
    }

    private fun getCuratedRecommendationSets(): List<List<CoupleRecommendation>> {
        return listOf(
            listOf(
                CoupleRecommendation(
                    id = "rec1_1",
                    title = "Signal Hill Sunset Golden Hour & DIY Picnic",
                    description = "Pack a blanket and homemade snacks from your kitchen to watch the sun melt into the Atlantic with spectacular panoramic views.",
                    category = "❤️ Romantic",
                    tag = "100% Free Suggestion",
                    duration = "2 hrs",
                    location = "Signal Hill",
                    cost = "Free"
                ),
                CoupleRecommendation(
                    id = "rec1_2",
                    title = "Secret Ingredient Kitchen Cook-Off",
                    description = "Pick one surprise pantry ingredient and cook a creative dish together using only what you already have at home with romantic background music.",
                    category = "🔥 Fun & Bonding",
                    tag = "Free at Home",
                    duration = "1.5 hrs",
                    location = "At Home",
                    cost = "Free"
                ),
                CoupleRecommendation(
                    id = "rec1_3",
                    title = "Sea Point Promenade Moonlight Stargazing Stroll",
                    description = "Stroll along the paved seaside promenade under the stars, listen to crashing waves, and share meaningful conversation.",
                    category = "🌌 Scenic Escape",
                    tag = "100% Free Suggestion",
                    duration = "1.5 hrs",
                    location = "Sea Point Promenade",
                    cost = "Free"
                )
            ),
            listOf(
                CoupleRecommendation(
                    id = "rec2_1",
                    title = "Llandudno Beach Granite Boulders Sunset Lounge",
                    description = "Sit among the iconic smooth boulders, feel the cool sea breeze, and watch the pastel skies together with cozy jackets.",
                    category = "✨ Romantic",
                    tag = "100% Free Suggestion",
                    duration = "2 hrs",
                    location = "Llandudno Beach",
                    cost = "Free"
                ),
                CoupleRecommendation(
                    id = "rec2_2",
                    title = "Couples Midnight Memory Box & Old Photos Nostalgia",
                    description = "Pull up your earliest phone photos together, reminisce about your very first date, and write a secret cute letter to open next month.",
                    category = "❤️ Intimate",
                    tag = "Free at Home",
                    duration = "1 hr",
                    location = "Cozy Couch",
                    cost = "Free"
                ),
                CoupleRecommendation(
                    id = "rec2_3",
                    title = "Kalk Bay Harbour Seal Watching & Quirky Stroll",
                    description = "Walk along the harbour piers to watch playful wild seals dive and splash, followed by browsing vintage antique window displays.",
                    category = "🌊 Fun Exploring",
                    tag = "100% Free Suggestion",
                    duration = "2 hrs",
                    location = "Kalk Bay Harbour",
                    cost = "Free"
                )
            ),
            listOf(
                CoupleRecommendation(
                    id = "rec3_1",
                    title = "Clifton 4th Beach Sandcastle Duel & Footprint Walk",
                    description = "Take off your shoes, walk along the powdery white sands, and build a silly couples sand sculpture together.",
                    category = "🌿 Adventure",
                    tag = "100% Free Suggestion",
                    duration = "2 hrs",
                    location = "Clifton 4th Beach",
                    cost = "Free"
                ),
                CoupleRecommendation(
                    id = "rec3_2",
                    title = "Blindfolded Sweet & Savoury Kitchen Taste Test",
                    description = "Blindfold your partner and feed them 6 mystery bites from your fridge (fruit, cinnamon, peanut butter, citrus) to see how many they guess!",
                    category = "😂 Playful Game",
                    tag = "Free at Home",
                    duration = "45 mins",
                    location = "Kitchen Island",
                    cost = "Free"
                ),
                CoupleRecommendation(
                    id = "rec3_3",
                    title = "Bo-Kaap Colourful Streets Photo Walk",
                    description = "Explore the vibrant historic cobbled streets together and take cute candid couple photos against the bright pastel houses.",
                    category = "📸 Creative",
                    tag = "100% Free Suggestion",
                    duration = "1.5 hrs",
                    location = "Bo-Kaap Quarter",
                    cost = "Free"
                )
            ),
            listOf(
                CoupleRecommendation(
                    id = "rec4_1",
                    title = "Camps Bay Tidal Pool Splash & Beachfront Slow Dance",
                    description = "Dip your toes in the natural ocean tidal pool during golden hour and slow dance together with your phone playing your couple song.",
                    category = "🔥 Flirty & Spicy",
                    tag = "100% Free Suggestion",
                    duration = "1.5 hrs",
                    location = "Camps Bay Tidal Pool",
                    cost = "Free"
                ),
                CoupleRecommendation(
                    id = "rec4_2",
                    title = "DIY Candlelit Aromatherapy & Massage Night",
                    description = "Dim the lights, light soothing candles, put on relaxing spa acoustics, and give each other a rejuvenating foot and shoulder massage.",
                    category = "🧖 Pure Relaxation",
                    tag = "Free at Home",
                    duration = "1.5 hrs",
                    location = "Living Room",
                    cost = "Free"
                ),
                CoupleRecommendation(
                    id = "rec4_3",
                    title = "Muizenberg Beach Boardwalk Sunset Stroll",
                    description = "Admire the world-famous colourful Victorian bathing boxes and watch the surfers catching twilight waves.",
                    category = "🌅 Scenic Walk",
                    tag = "100% Free Suggestion",
                    duration = "1.5 hrs",
                    location = "Muizenberg Beach",
                    cost = "Free"
                )
            ),
            listOf(
                CoupleRecommendation(
                    id = "rec5_1",
                    title = "Living Room Blanket Fort & Rom-Com Movie Night",
                    description = "Build a cozy fortress out of cushions and blankets, string fairy lights, pop homegrown popcorn, and binge your favorite romance movies.",
                    category = "❤️ Cozy Night In",
                    tag = "Free at Home",
                    duration = "2.5 hrs",
                    location = "Living Room Fort",
                    cost = "Free"
                ),
                CoupleRecommendation(
                    id = "rec5_2",
                    title = "Secret Couple Love Letter & Complimentary Note Swap",
                    description = "Sit back-to-back for 15 minutes writing 10 things you genuinely love and appreciate about each other, then read them aloud.",
                    category = "💌 Heartfelt",
                    tag = "Free at Home",
                    duration = "1 hr",
                    location = "Balcony / Garden",
                    cost = "Free"
                ),
                CoupleRecommendation(
                    id = "rec5_3",
                    title = "Bloubergstrand Iconic Table Mountain Sunset View",
                    description = "Enjoy the most famous postcard view of Table Mountain across Table Bay with the evening glow illuminating the sea.",
                    category = "📸 Scenic Wonder",
                    tag = "100% Free Suggestion",
                    duration = "2 hrs",
                    location = "Blouberg Beach",
                    cost = "Free"
                )
            )
        )
    }

    private fun getInitialDateIdeasSeed(): List<DateIdea> {
        val list = mutableListOf<DateIdea>()

        // ❤️ Romantic (52 Date Ideas)
        val romanticTitles = listOf(
            "Lion's Head Sunset & Rooibos Sundowners", "Kirstenbosch Gardens & Galileo Cinema", "V&A Waterfront Sunset Catamaran Cruise",
            "Table Mountain Cable Car Golden Hour Toast", "Camps Bay Candlelit Seafood Dinner", "Cape Point Lighthouse Starry Night Drive",
            "Franschhoek Wine Tram Secret Garden Picnic", "Boulders Beach Sunset Stroll with Penguins", "Llandudno Beach Secret Cove Sunset Picnic",
            "Signal Hill Midnight Stargazing & Hot Cocoa", "Stellenbosch Vineyards Starlit Candlelight Dinner", "Oudekraal Braai & Tidal Pool Sunset",
            "Sea Point Promenade Twilight Couple Stroll", "Bloubergstrand Postcard View Picnic", "Noordhoek Beach Sunset Horseback Ride",
            "Constantia Glen Wine & Cheese Tasting", "Kalk Bay Harbour Light & Sunset Walk", "Rooftop Candlelight Dessert at Silo District",
            "Silvermine Nature Reserve Lake Picnic", "Hout Bay Harbor Catamaran Cruise", "Misty Cliffs Oceanfront Love Letter Swap",
            "Dewy Morning Stroll at Arderne Gardens", "Waterfront Wheel Romantic Sunset Flight", "Tintswalo Atlantic Seaside Sunset Drinks",
            "Constantia Uitsig Rose Garden Walk & Ice Cream", "Muizenberg Sunrise Coffee & Beach Huts Walk", "Scarborough Secluded Sunset Beach Picnic",
            "Constantia Valley Champagne Brunch", "Bakoven Sunset Rocks Sundowners", "Cape Dutch Homestead Historic Garden Walk",
            "Twelve Apostles Hotel Sunset Terrace Drinks", "Glen Beach Secret Cove Sunset Picnic", "Chart Farm Rose Picking & Tea Garden",
            "De Waal Park Acoustic Afternoon Picnic", "Silo Hotel Rooftop Sunset Cocktail Flight", "Green Point Park Lakeside Blanket & Playlist",
            "Sunset Helicopter Flight over Cape Peninsula", "Clovelly Ridge Sunset Trail & Tea", "Waterfront Pier Couples Stroll at Midnight",
            "Newlands Forest Canopy Kiss & Tea Flask", "St James Tidal Pool Sunset Soak & Blanket", "Clifton 4th Beach Candlelit Night Picnic",
            "Durbanville Wine Valley Sunset Tasting", "Cape Town City Hall Symphony Concert Night", "Beta Beach Hidden Cove Sundowners",
            "Helderberg Nature Reserve Sunset Picnic", "Victoria & Alfred Marina Boardwalk Walk", "Mount Nelson High Tea & Rose Garden Walk",
            "Fish Hoek Beach Twilight Walk & Gelato", "Elgin Valley Glamping Under the Stars", "Babylonstoren Historic Garden Sunset Walk", "Kommetjie Lighthouse Sunset & Wine Flask"
        )
        romanticTitles.forEachIndexed { i, title ->
            list.add(DateIdea("r_${i+1}", title, "❤️ Romantic", "Experience a romantic $title moment filled with warmth, sunset views, and sweet connections.", 150 + (i % 50), "2-3 hrs", if (i % 3 == 0) "Free" else if (i % 3 == 1) "R150 - R300" else "R400+"))
        }

        // 😂 Funny (52 Date Ideas)
        val funnyTitles = listOf(
            "Chakalaka & Braai Master Chef Battle", "Kalk Bay Vintage Treasure Hunt & Fish 'n Chips", "Blindfold Biltong & Koesister Taste Test",
            "R50 Challenge at Milnerton Flea Market", "Worst Accent City Tour & Impersonation Game", "Thrift Store Outfit Swap at Observatory",
            "Bad Cape Malay Curry Cooking Contest", "Retro Arcade Game Battle at GrandWest Casino", "Awkward Photo Safari at Bo-Kaap Bright Houses",
            "Crazy Mini Golf Battle at Cave Golf V&A", "Cape Town Street Food Taste Test Blindfolded", "Bad Poetry Slam at De Waal Park Bandstand",
            "Cape Town Slang Only Speaking Challenge", "Cheap Grocery Store Gourmet Platter Challenge", "Hout Bay Seal Impersonation Photo Contest",
            "Giant Ice Cream Licking Race at Sea Point", "Terrible Karaoke Duet Night in Long Street", "Bizarre Souvenir Shopping in Greenmarket Square",
            "Silly Dance Battle at Sea Point Promenade", "Funniest Penguin Caption Contest at Boulders", "Pick n Pay R100 Dessert Mystery Box Challenge",
            "Terrible Drawing Portrait Challenge at Kirstenbosch", "Funny Accent Coffee Shop Order Challenge", "Table Mountain Eco Trivia Duel",
            "Retro Board Game Showdown at Honest Chocolate", "Craziest Sunglasses Competition at Camps Bay", "Fake Tour Guide Challenge at V&A Waterfront",
            "Silly Costume Bowling Match at Stadium Bowling", "Blindfolded Snack Feeding Challenge", "Terrible Stand-up Comedy Roast Night",
            "Cape Town Wind Walk Funny Hair Contest", "Funny Meme Creation Day in Cape Town", "Worst Dad Joke Marathon at Company's Garden",
            "Silly Face Photo Booth Challenge", "R20 Antique Store Mystery Object Guess", "Hilarious Tandem Bicycle Ride along Promenade",
            "Samosa Spice Level Roulette Game", "Funny Accent Accent Swap Road Trip", "Awkward Couple Pose Challenge at Blouberg",
            "Silly Charades in Kirstenbosch Lawn", "Worst DIY Pottery Mug Challenge", "Funny Local Snack Ranking Video Night",
            "Crazy Hair Day Walk on Kloof Street", "Bad Opera Singing Challenge on Lion's Head", "Retro Video Game Speedrun Challenge",
            "Hilarious Puppet Show Creation at Home", "Absurd Food Combination Taste Test", "Silly Hat Shopping Spree in Woodstock",
            "Fake Commercial Shooting for South African Products", "Funny Celebrity Impersonation Night", "Hilarious Mime Performance Challenge", "Cape Town Weather Forecasting Parody Video"
        )
        funnyTitles.forEachIndexed { i, title ->
            list.add(DateIdea("f_${i+1}", title, "😂 Funny", "Laugh out loud together doing the $title! Guaranteed giggles, playful banter, and silly memories.", 120 + (i % 40), "1-2 hrs", if (i % 2 == 0) "Free" else "R80 - R200"))
        }

        // 🔥 Spicy (52 Date Ideas)
        val spicyTitles = listOf(
            "Secret Speakeasy Drinks at Asoka on Kloof Street", "Camps Bay Sunset Luxury Beach Lounge", "Fynbos Aromatherapy & Sensual Massage",
            "Late Night Bree Street Hidden Bar Crawl", "Private Hot Tub Night in Stellenbosch Cabin", "Salsa Dancing Night at Cape Town Latin Club",
            "Candlelit Body Painting & Bath Night", "Velvet & Lace Dress Up Night at Silo District", "Rooftop Infinity Pool Sundowners at The Silo",
            "Truth or Dare: Cape Town Passion Edition", "Midnight Dip at Secret Secluded Beach", "Sensual Blindfolded Fruit & Wine Tasting",
            "Private Wine Estate Sunset Tasting Room", "Flirty Cocktail Mixology Battle at Home", "Candlelit Slow Dancing to Cape Jazz",
            "Speakeasy Rendezvous Pretending to be Strangers", "Private Cabana Night at Shimmy Beach Club", "Sunset Champagne Toast on Private Yacht",
            "Passionate Tango Workshop in Woodstock Studio", "Midnight Stargazing & Secluded Beach Picnic", "Firepit Wine & Whisper Night",
            "Luxury Boutique Hotel Day Pass & Spa", "Candlelit Jacuzzi Bath with Fynbos Oils", "Romantic Sunset Helicopter Ride for Two",
            "Sensual Massage with Warm Rooibos Oil", "Speakeasy Lounge Underground Jazz Night", "Flirty Couples Truth or Strip Card Game",
            "Sunset Tapas & Flamenco Music Night", "Private Wine Cellar Barrel Tasting Tour", "Secluded Cliffside Blanket Session at Chapman's",
            "Candlelit Rooftop Dinner with Private Chef", "Late Night Dessert & Espresso Martini Date", "Flirty Photography Session in Secluded Cove",
            "Private Sunset Cruise on Table Bay", "Romantic Couples Spa Package in Constantia", "Candlelit Cottage Night in Franschhoek",
            "Flirty Salsa Night on Kloof Street", "Midnight Beach Fire Pit & Wine", "Luxury Champagne Sunset Drive",
            "Sensual Aromatherapy Foot Soak & Scrub", "Secluded Waterfall Dip in Bain's Kloof", "Private Rooftop Stargazing & Wine",
            "Flirty Dancing under Beach Promenade Lights", "Candlelit Bath with Rose Petals & MCC", "Midnight Cocktail Lounge Rendezvous",
            "Sensual Slow Dance Session at Home", "Flirty Romantic Challenge Card Game", "Private Beach Cove Sunset Wine Toast",
            "Candlelit Fondue & Wine Night", "Late Night City Lights Drive on Signal Hill", "Romantic Sunset Picnic on Secret Ridge", "Private Winelands Cabin Hot Tub Escape"
        )
        spicyTitles.forEachIndexed { i, title ->
            list.add(DateIdea("s_${i+1}", title, "🔥 Spicy", "Ignite the passion and flirtation with $title! Flirty vibes, deep connection, and excitement.", 180 + (i % 60), "2-4 hrs", if (i % 3 == 0) "R100 - R250" else "R350 - R800"))
        }

        // 🧘 Relaxing (52 Date Ideas)
        val relaxingTitles = listOf(
            "Sea Point Promenade Sunset Walk & Gelato", "Boulders Beach Penguin Colony Walk", "Slow Sunday Coffee at Truth Coffee",
            "Kirstenbosch Shade Tree Blanket & Book Afternoon", "Oudekraal Beach Picnic & Calm Ocean Dip", "Constantia Valley Garden Stroll & Tea",
            "Green Point Urban Park Sunset Relaxation", "Newlands Forest Gentle Stream Walk", "Hout Bay Harbour Quiet Walk & Coffee",
            "Company's Garden Squirrel Feeding & Tea", "Silvermine Reservoir Quiet Pine Walk", "Muizenberg Sunset Boardwalk Stroll",
            "Kalk Bay Bookstore Browsing & Ocean Coffee", "Llandudno Quiet Sunset & Herbal Tea Flask", "Table Mountain Lower Cableway Gentle Walk",
            "Arderne Gardens Tree Canopy Rest", "Blouberg Quiet Ocean Horizon Gaze", "Noordhoek Farm Village Slow Afternoon",
            "Constantia Uitsig Lavender Garden Stroll", "Scarborough Quiet Beach Walk & Smoothie", "De Waal Park Shade Tree Nap & Reading",
            "Sunset Ocean Waves Meditation at Bakoven", "Kirstenbosch Bonsai Garden Stroll", "Durbanville Rose Garden Afternoon Walk",
            "St James Quiet Tidal Pool Stroll", "Hout Bay Beach Sunset Walk", "Fish Hoek Calm Beach Stroll",
            "Sunset Rooibos Tea Flask on Signal Hill", "Silo District Art Plaza Quiet Walk", "Cape Point Ostrich Farm Gentle Visit",
            "Elgin Apple Orchards Relaxing Day Drive", "Constantia Glen Sunset Juice & Platter", "Kommetjie Slipway Calm Ocean Sitting",
            "Sea Point Lighthouse Sunset Reading", "Rondebosch Park Quiet Walk & Smoothie", "Helderberg Nature Reserve Shady Picnic",
            "Franschhoek Village Slow Antiquing", "Misty Cliffs Quiet Ocean Breeze Rest", "Cape Dutch Farm Bakery Slow Breakfast",
            "Camps Bay Quiet Early Morning Stroll", "Signal Hill Lower Slope Picnic", "Observatory Quiet Cafe Book Afternoon",
            "Constantia Creek Waterfall Walk", "Mouille Point Lighthouse Sunset Bench", "Gordon's Bay Quiet Marina Stroll",
            "Bettys Bay Botanic Gardens Walk", "Stanford River Peaceful Boat Glide", "Cape Town Planetarium Relaxing Show",
            "Greyton Peaceful Village Sunday Stroll", "Riebeek Kasteel Slow Olive Farm Walk", "Bain's Kloof Stream Side Relaxation", "Table Bay Quiet Promenade Bench Sitting"
        )
        relaxingTitles.forEachIndexed { i, title ->
            list.add(DateIdea("r_lx_${i+1}", title, "🧘 Relaxing", "Unwind and recharge together with $title. Calm energy, peaceful scenery, and zero stress.", 110 + (i % 30), "1-2 hrs", if (i % 2 == 0) "Free" else "R50 - R150"))
        }

        // 🎉 Adventurous (52 Date Ideas)
        val adventurousTitles = listOf(
            "Chapman's Peak Drive & Secret Cliffside Picnic", "Franschhoek Open-Air Wine Tram Day Trip", "Mojo Market Food Safari in Sea Point",
            "Lion's Head Sunrise Hike & Summit Coffee", "Kayaking with Dolphins off Mouille Point", "Tandem Paragliding off Signal Hill",
            "Table Mountain Platteklip Gorge Summit Challenge", "Surfing Lessons at Muizenberg Beach", "Snorkeling with Cape Fur Seals in Hout Bay",
            "Sandboarding the Atlantis Dunes", "Quad Biking in Atlantis Sand Dunes", "Coasteering & Rock Jumping at False Bay",
            "Zip-lining in Cape Canopy Elgin", "Speedboat Thrill Ride off V&A Waterfront", "Bouldering Challenge at CityRock Paarden Eiland",
            "Shark Cage Diving in Gansbaai", "Electric Bicycle Tour of Table Mountain", "Sunset Helicopter Tour of Cape Peninsula",
            "Stand-Up Paddleboarding in Waterfront Canal", "Scuba Diving the Kelp Forests in False Bay", "Abseiling down Table Mountain Summit",
            "Mountain Biking Trails in Tokai Forest", "Kitesurfing Trial Lesson in Bloubergstrand", "Midnight Coin Toss Roadtrip down R44",
            "Cave Exploring at Boomslang Cave Kalk Bay", "Deep Sea Fishing Charter off Hout Bay", "Night Hike up Signal Hill under Full Moon",
            "Off-Road 4x4 Trail in Silvermine Reserve", "Windsurfing Challenge in Langebaan Lagoon", "Microlight Flight over Cape Winelands",
            "Sailing Lesson in Table Bay Harbour", "Cliff Jumping at Steenbras Nature Reserve", "Trail Running the Contour Path Table Mountain",
            "Horseback Riding along Noordhoek Beach", "Hot Air Balloon Flight over Paarl Valley", "Caving Adventure at Waenhuiskrans Cave",
            "River Rafting down Palmiet River", "Sunset Catamaran Cruise in Table Bay", "Fat Bike Beach Ride in Gansbaai Dunes",
            "Tidal Pool Night Swimming Adventure", "Coastal Cave Exploration at De Kelders", "Tree Canopy Walkway at Kirstenbosch",
            "Mountain Pass Road Trip down Franschhoek Pass", "Secret Waterfall Hike in Jonkershoek", "Sea Kayaking from Simon's Town to Boulders",
            "Wildflower Safari Trail in West Coast Park", "Sunset Dune Jumping at Atlantis", "Speedboat Safari to Seal Island",
            "Night Paddleboarding in V&A Canal", "Rock Climbing at Hole in the Wall Muizenberg", "Whale Watching Boat Safari in Hermanus", "Extreme Zipline Flight over Hermanus Canyon"
        )
        adventurousTitles.forEachIndexed { i, title ->
            list.add(DateIdea("a_${i+1}", title, "🎉 Adventurous", "Get your adrenaline pumping with $title! Thrilling experiences, high energy, and unforgettable quests.", 200 + (i % 80), "Half Day", if (i % 3 == 0) "R150 - R300" else "R400 - R1200"))
        }

        // 🧠 Intellectual (52 Date Ideas)
        val intellectualTitles = listOf(
            "Zeitz MOCAA Museum & V&A Grain Silo Tour", "Bo-Kaap Cultural Walk & Samosa Cooking Class", "The Book Lounge & East City Coffee Hunt",
            "Iziko South African National Gallery Exhibition", "Cape Town Planetarium Cosmic Astronomy Show", "District Six Museum Guided History Walk",
            "Heart of Cape Town Organ Transplant Museum Tour", "Norval Foundation Sculpture Park & Modern Art", "Historic Slave Lodge Museum & Dialogue Stroll",
            "Kirstenbosch Botanical Library & Plant Tour", "Castle of Good Hope Historic Fortress Tour", "South African Museum & Dinosaur Exhibit",
            "Co-Op Strategy Board Game Night at Honest Chocolate", "Cape Malay Cooking Masterclass in Bo-Kaap", "Iziko Maritime Centre Historic Shipwreck Tour",
            "Rust en Vreugd 18th-Century Garden & Art Walk", "Cape Town Holocaust & Genocide Centre Stroll", "Stellenbosch Historic Village Architecture Walk",
            "Franschhoek Huguenot Memorial Museum & Tea", "Woodstock Street Art & Mural Walking Tour", "Company's Garden Historic Heritage Trail",
            "Philosophy & Coffee Debate Night on Kloof Street", "Indie Bookstore Treasure Hunt in Kalk Bay", "South African Literature Reading Night at Home",
            "Table Mountain Geology & Fynbos Ecology Hike", "Irma Stern Museum & Garden Tour in Rosebank", "Cape Town Diamond Museum Guided Tour",
            "Two Oceans Aquarium Marine Biology Discovery", "Historic Constantia Dutch Manor Architecture Tour", "Cape Town Science Centre Interactive Exhibits",
            "Cape Town City Hall Historic Pipe Organ Tour", "Documentary Film Screening & Discussion Night", "Heritage Food & Spice History Tour in East City",
            "Cape Town Astronomy Society Stargazing Night", "Public Lecture or TEDx Talk Night", "Modern African Photography Exhibition Tour",
            "Classical Music Chamber Concert at St George's", "Poetry Open Mic Night in Observatory", "Historic Fort Wynyard Tour in Green Point",
            "Stellenbosch Botanical Garden Rare Plant Tour", "Cape Town Historical Map Hunt at City Library", "Eco-Architecture Tour in Silo District",
            "Creative Writing & Sketching Session at Kirstenbosch", "World Cinema Film Night & Wine", "Historic Wine Cellar Heritage Tasting Tour",
            "Iziko Bertram House Georgian Museum Stroll", "Cape Town Philosophy Cafe Meetup", "Archaeological Walk at Peers Cave Fish Hoek",
            "Indigenous Fynbos Tea Workshop & Tasting", "Cape Town Literary Walking Tour", "Classical Art & Wine Pairing Night", "Cape Peninsula Heritage & Lighthouse History Tour"
        )
        intellectualTitles.forEachIndexed { i, title ->
            list.add(DateIdea("i_${i+1}", title, "🧠 Intellectual", "Feed your curiosity with $title! Engaging conversations, rich culture, history, and creative discovery.", 140 + (i % 45), "2 hrs", if (i % 2 == 0) "R50 - R180" else "R200 - R400"))
        }

        return list
    }

    private fun getInitialCommunitySeed(): List<CommunityPost> {
        return listOf(
            CommunityPost("c1", "Sipho & Lerato", "Kirstenbosch Galileo Open Air Cinema", "❤️ Romantic", "We did the Galileo open-air movie last night! Packed a picnic basket with biltong & Pinotage under Table Mountain. Magical CPT date night!", 54, true, "Level 10 Power Couple", "2 hours ago"),
            CommunityPost("c2", "Liam & Chloe", "Chapman's Peak Highway Coin Toss Trip!", "🎉 Adventurous", "Flipped a coin along Chapman's Peak Drive and ended up at a cozy bakery in Noordhoek! 10/10 recommendation for Cape Town couples.", 98, false, "Level 25 Relationship Adventurers", "5 hours ago"),
            CommunityPost("c3", "Jabu & Tash", "Kalk Bay Antique Hunt & Fish 'n Chips", "😂 Funny", "We tried finding the weirdest vintage item under R50 in Kalk Bay! Ended up with a retro brass penguin from Kalkys. Earned +150 XP today!", 76, true, "Level 5 Getting Serious", "1 day ago")
        )
    }
}

// Level system progression data model helper
data class LevelInfo(
    val level: Int,
    val title: String,
    val currentXp: Int,
    val xpForCurrentLevelStart: Int,
    val xpForNextLevel: Int,
    val progressFraction: Float
) {
    val xpIntoCurrentLevel = currentXp - xpForCurrentLevelStart
    val xpRequiredForThisLevel = xpForNextLevel - xpForCurrentLevelStart
    val xpNeededForNextLevel = (xpForNextLevel - currentXp).coerceAtLeast(0)
}

object LevelSystem {
    fun calculateLevel(currentXp: Int): LevelInfo {
        // 1 Level per 1000 XP
        val rawLevel = (currentXp / 1000) + 1
        val level = rawLevel.coerceAtMost(100)

        val title = when {
            level >= 50 -> "Level 50 — Legendary Couple"
            level >= 25 -> "Level 25 — Relationship Adventurers"
            level >= 10 -> "Level 10 — Power Couple"
            level >= 5  -> "Level 5 — Getting Serious"
            else        -> "Level $level — First Date"
        }

        val levelStart = (level - 1) * 1000
        val levelNext = level * 1000
        val fraction = ((currentXp - levelStart).toFloat() / 1000f).coerceIn(0f, 1f)

        return LevelInfo(
            level = level,
            title = title,
            currentXp = currentXp,
            xpForCurrentLevelStart = levelStart,
            xpForNextLevel = levelNext,
            progressFraction = fraction
        )
    }

    fun xpRequiredForLevel(targetLevel: Int): Int {
        return (targetLevel - 1) * 1000
    }

    fun xpNeededToUnlock(currentXp: Int, targetLevel: Int): Int {
        val required = xpRequiredForLevel(targetLevel)
        return (required - currentXp).coerceAtLeast(0)
    }

    fun getTierIcon(level: Int): String {
        return when {
            level >= 50 -> "👑"
            level >= 25 -> "🌟"
            level >= 10 -> "💎"
            level >= 5  -> "🔥"
            else        -> "🌱"
        }
    }
}
