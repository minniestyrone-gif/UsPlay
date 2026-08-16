package com.example.ui.screens

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.UsPlayViewModel
import com.example.ui.theme.UsPlayGoldXP
import com.example.ui.theme.UsPlayPlumBackground
import com.example.ui.theme.UsPlayPlumCard
import com.example.ui.theme.UsPlayPlumCardElevated
import com.example.ui.theme.UsPlayRoseDark
import com.example.ui.theme.UsPlayRosePrimary
import com.example.ui.theme.UsPlayTextMuted

@Composable
fun AuthScreen(
    viewModel: UsPlayViewModel,
    modifier: Modifier = Modifier
) {
    val allCouples by viewModel.allCouples.collectAsState()

    var selectedTabIndex by remember { mutableIntStateOf(0) } // 0 = Login, 1 = Join Us

    // Login Form State
    var loginEmail by remember { mutableStateOf("sipho.lerato@usplay.com") }
    var loginPassword by remember { mutableStateOf("cape123") }
    var isLoginPasswordVisible by remember { mutableStateOf(false) }

    // Join Us Form State
    var p1Name by remember { mutableStateOf("") }
    var p2Name by remember { mutableStateOf("") }
    var signUpEmail by remember { mutableStateOf("") }
    var signUpPassword by remember { mutableStateOf("") }
    var anniversary by remember { mutableStateOf("") }
    var coupleBio by remember { mutableStateOf("") }
    var isSignUpPasswordVisible by remember { mutableStateOf(false) }

    var errorMessage by remember { mutableStateOf<String?>(null) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(UsPlayPlumBackground)
            .padding(16.dp)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(24.dp))

                // Brand Header
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(Color.Black)
                        .border(2.dp, UsPlayRosePrimary, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.img_app_icon),
                        contentDescription = "UsPlay Logo",
                        modifier = Modifier
                            .size(76.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "UsPlay 💕",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Text(
                    text = "The Couple Date Night & Leveling App",
                    fontSize = 14.sp,
                    color = UsPlayTextMuted
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Tab Row for Login vs Join Us
                TabRow(
                    selectedTabIndex = selectedTabIndex,
                    containerColor = UsPlayPlumCard,
                    contentColor = UsPlayRosePrimary,
                    indicator = { tabPositions ->
                        if (selectedTabIndex < tabPositions.size) {
                            TabRowDefaults.SecondaryIndicator(
                                modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                                color = UsPlayRosePrimary,
                                height = 3.dp
                            )
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, UsPlayRoseDark.copy(alpha = 0.3f), RoundedCornerShape(16.dp))
                ) {
                    Tab(
                        selected = selectedTabIndex == 0,
                        onClick = {
                            selectedTabIndex = 0
                            errorMessage = null
                        },
                        modifier = Modifier.testTag("auth_tab_login")
                    ) {
                        Text(
                            text = "SIGN IN",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (selectedTabIndex == 0) UsPlayRosePrimary else UsPlayTextMuted,
                            modifier = Modifier.padding(vertical = 12.dp)
                        )
                    }

                    Tab(
                        selected = selectedTabIndex == 1,
                        onClick = {
                            selectedTabIndex = 1
                            errorMessage = null
                        },
                        modifier = Modifier.testTag("auth_tab_join")
                    ) {
                        Text(
                            text = "JOIN US 💕",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (selectedTabIndex == 1) UsPlayRosePrimary else UsPlayTextMuted,
                            modifier = Modifier.padding(vertical = 12.dp)
                        )
                    }
                }
            }

            // Form Section
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = UsPlayPlumCard)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        if (selectedTabIndex == 0) {
                            // SIGN IN FORM
                            Text(
                                text = "Welcome Back! 👋",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )

                            OutlinedTextField(
                                value = loginEmail,
                                onValueChange = { loginEmail = it },
                                label = { Text("Couple Email") },
                                leadingIcon = {
                                    Icon(imageVector = Icons.Default.Email, contentDescription = null, tint = UsPlayRosePrimary)
                                },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("login_email_input"),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = UsPlayRosePrimary,
                                    unfocusedBorderColor = UsPlayRoseDark.copy(alpha = 0.5f),
                                    focusedLabelColor = UsPlayRosePrimary,
                                    unfocusedLabelColor = UsPlayTextMuted,
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White
                                )
                            )

                            OutlinedTextField(
                                value = loginPassword,
                                onValueChange = { loginPassword = it },
                                label = { Text("Password") },
                                leadingIcon = {
                                    Icon(imageVector = Icons.Default.Lock, contentDescription = null, tint = UsPlayRosePrimary)
                                },
                                trailingIcon = {
                                    IconButton(onClick = { isLoginPasswordVisible = !isLoginPasswordVisible }) {
                                        Icon(
                                            imageVector = if (isLoginPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                            contentDescription = "Toggle password visibility",
                                            tint = UsPlayTextMuted
                                        )
                                    }
                                },
                                visualTransformation = if (isLoginPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("login_password_input"),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = UsPlayRosePrimary,
                                    unfocusedBorderColor = UsPlayRoseDark.copy(alpha = 0.5f),
                                    focusedLabelColor = UsPlayRosePrimary,
                                    unfocusedLabelColor = UsPlayTextMuted,
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White
                                )
                            )

                            Button(
                                onClick = {
                                    if (loginEmail.isBlank()) {
                                        errorMessage = "Please enter your couple email."
                                    } else {
                                        viewModel.login(loginEmail, loginPassword)
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp)
                                    .testTag("login_submit_btn"),
                                colors = ButtonDefaults.buttonColors(containerColor = UsPlayRosePrimary),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text(
                                    text = "Sign In & Enter UsPlay",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                )
                            }
                        } else {
                            // JOIN US (NEW COUPLE SIGN UP) FORM
                            Text(
                                text = "Start Your Couple Journey 💕",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                OutlinedTextField(
                                    value = p1Name,
                                    onValueChange = { p1Name = it },
                                    label = { Text("Partner 1") },
                                    leadingIcon = {
                                        Icon(imageVector = Icons.Default.Person, contentDescription = null, tint = UsPlayRosePrimary)
                                    },
                                    singleLine = true,
                                    modifier = Modifier
                                        .weight(1f)
                                        .testTag("signup_p1_input"),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = UsPlayRosePrimary,
                                        unfocusedBorderColor = UsPlayRoseDark.copy(alpha = 0.5f),
                                        focusedLabelColor = UsPlayRosePrimary,
                                        unfocusedLabelColor = UsPlayTextMuted,
                                        focusedTextColor = Color.White,
                                        unfocusedTextColor = Color.White
                                    )
                                )

                                OutlinedTextField(
                                    value = p2Name,
                                    onValueChange = { p2Name = it },
                                    label = { Text("Partner 2") },
                                    leadingIcon = {
                                        Icon(imageVector = Icons.Default.Person, contentDescription = null, tint = UsPlayRosePrimary)
                                    },
                                    singleLine = true,
                                    modifier = Modifier
                                        .weight(1f)
                                        .testTag("signup_p2_input"),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = UsPlayRosePrimary,
                                        unfocusedBorderColor = UsPlayRoseDark.copy(alpha = 0.5f),
                                        focusedLabelColor = UsPlayRosePrimary,
                                        unfocusedLabelColor = UsPlayTextMuted,
                                        focusedTextColor = Color.White,
                                        unfocusedTextColor = Color.White
                                    )
                                )
                            }

                            OutlinedTextField(
                                value = signUpEmail,
                                onValueChange = { signUpEmail = it },
                                label = { Text("Couple Email") },
                                leadingIcon = {
                                    Icon(imageVector = Icons.Default.Email, contentDescription = null, tint = UsPlayRosePrimary)
                                },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("signup_email_input"),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = UsPlayRosePrimary,
                                    unfocusedBorderColor = UsPlayRoseDark.copy(alpha = 0.5f),
                                    focusedLabelColor = UsPlayRosePrimary,
                                    unfocusedLabelColor = UsPlayTextMuted,
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White
                                )
                            )

                            OutlinedTextField(
                                value = signUpPassword,
                                onValueChange = { signUpPassword = it },
                                label = { Text("Password") },
                                leadingIcon = {
                                    Icon(imageVector = Icons.Default.Lock, contentDescription = null, tint = UsPlayRosePrimary)
                                },
                                trailingIcon = {
                                    IconButton(onClick = { isSignUpPasswordVisible = !isSignUpPasswordVisible }) {
                                        Icon(
                                            imageVector = if (isSignUpPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                            contentDescription = "Toggle password visibility",
                                            tint = UsPlayTextMuted
                                        )
                                    }
                                },
                                visualTransformation = if (isSignUpPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("signup_password_input"),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = UsPlayRosePrimary,
                                    unfocusedBorderColor = UsPlayRoseDark.copy(alpha = 0.5f),
                                    focusedLabelColor = UsPlayRosePrimary,
                                    unfocusedLabelColor = UsPlayTextMuted,
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White
                                )
                            )

                            OutlinedTextField(
                                value = anniversary,
                                onValueChange = { anniversary = it },
                                label = { Text("Anniversary / Start Date (e.g., Oct 14, 2023)") },
                                leadingIcon = {
                                    Icon(imageVector = Icons.Default.DateRange, contentDescription = null, tint = UsPlayRosePrimary)
                                },
                                singleLine = true,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("signup_anniversary_input"),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = UsPlayRosePrimary,
                                    unfocusedBorderColor = UsPlayRoseDark.copy(alpha = 0.5f),
                                    focusedLabelColor = UsPlayRosePrimary,
                                    unfocusedLabelColor = UsPlayTextMuted,
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White
                                )
                            )

                            OutlinedTextField(
                                value = coupleBio,
                                onValueChange = { coupleBio = it },
                                label = { Text("Couple Bio / City") },
                                leadingIcon = {
                                    Icon(imageVector = Icons.Default.Info, contentDescription = null, tint = UsPlayRosePrimary)
                                },
                                singleLine = true,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("signup_bio_input"),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = UsPlayRosePrimary,
                                    unfocusedBorderColor = UsPlayRoseDark.copy(alpha = 0.5f),
                                    focusedLabelColor = UsPlayRosePrimary,
                                    unfocusedLabelColor = UsPlayTextMuted,
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White
                                )
                            )

                            Button(
                                onClick = {
                                    if (p1Name.isBlank() || p2Name.isBlank() || signUpEmail.isBlank() || signUpPassword.isBlank()) {
                                        errorMessage = "Please enter partner names, email, and password."
                                    } else {
                                        viewModel.signUp(p1Name, p2Name, signUpEmail, signUpPassword, anniversary, coupleBio)
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp)
                                    .testTag("signup_submit_btn"),
                                colors = ButtonDefaults.buttonColors(containerColor = UsPlayRosePrimary),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text(
                                    text = "Join Us & Claim +100 XP! ✨",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                )
                            }
                        }

                        errorMessage?.let { err ->
                            Text(
                                text = err,
                                color = Color(0xFFFF8A80),
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }

            // Quick Demo Couple Accounts Section
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Or Sign In as Demo Couple:",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = UsPlayTextMuted
                    )

                    allCouples.forEach { couple ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    viewModel.loginAsDemoAccount(couple)
                                },
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = UsPlayPlumCardElevated)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Surface(
                                        shape = CircleShape,
                                        color = UsPlayRoseDark.copy(alpha = 0.5f),
                                        modifier = Modifier.size(40.dp)
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Text(text = "💑", fontSize = 20.sp)
                                        }
                                    }
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column {
                                        Text(
                                            text = "${couple.partner1Name} & ${couple.partner2Name}",
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White,
                                            fontSize = 15.sp
                                        )
                                        Text(
                                            text = couple.email,
                                            fontSize = 12.sp,
                                            color = UsPlayTextMuted
                                        )
                                    }
                                }

                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = UsPlayRosePrimary.copy(alpha = 0.2f),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, UsPlayRosePrimary)
                                ) {
                                    Text(
                                        text = "1-Tap Sign In",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = UsPlayRosePrimary,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}
