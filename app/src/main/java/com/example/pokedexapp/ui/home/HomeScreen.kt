package com.example.pokedexapp.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

val PokeRed = Color(0xFFCC0000)
val PokeWhite = Color(0xFFFFFFFF)

// card theme colors
val CardGold = Color(0xFFE8C84A)
val CardDarkGold = Color(0xFFB8972A)
val CardCream = Color(0xFFFFFDE7)
val CardBrown = Color(0xFF5A3E00)
val CardDarkBg = Color(0xFF2A1A00)

@Composable
fun HomeScreen(
    searchText: String,
    onSearchTextChange: (String) -> Unit,
    onSearchClick: () -> Unit,
    isLoading: Boolean,
    errorMessage: String?,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(CardDarkBg)
            .padding(10.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .border(4.dp, CardDarkGold, RoundedCornerShape(16.dp))
                .clip(RoundedCornerShape(16.dp))
                .background(CardGold)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 28.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Pokédex",
                    fontSize = 52.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = CardBrown,
                    letterSpacing = 2.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Search for any Pokémon",
                    fontSize = 16.sp,
                    color = CardBrown.copy(alpha = 0.7f)
                )

                Spacer(modifier = Modifier.height(48.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .border(1.dp, CardDarkGold, RoundedCornerShape(12.dp))
                        .background(CardCream)
                        .padding(20.dp)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        OutlinedTextField(
                            value = searchText,
                            onValueChange = onSearchTextChange,
                            label = {
                                Text(
                                    "Pokémon name",
                                    color = CardBrown.copy(alpha = 0.7f)
                                )
                            },
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = CardDarkGold,
                                unfocusedBorderColor = CardDarkGold.copy(alpha = 0.5f),
                                focusedTextColor = CardBrown,
                                unfocusedTextColor = CardBrown,
                                cursorColor = CardDarkGold,
                                focusedLabelColor = CardBrown,
                                unfocusedLabelColor = CardBrown.copy(alpha = 0.6f)
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Button(
                            onClick = onSearchClick,
                            enabled = !isLoading,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = CardDarkGold,
                                contentColor = PokeWhite
                            )
                        ) {
                            Text(
                                text = if (isLoading) "Searching..." else "Search",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        if (isLoading) {
                            Spacer(modifier = Modifier.height(16.dp))
                            CircularProgressIndicator(color = CardDarkGold)
                        }

                        if (!errorMessage.isNullOrBlank()) {
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = errorMessage,
                                color = PokeRed
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(CardDarkGold)
                        .padding(10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Pokédex",
                        fontSize = 12.sp,
                        color = PokeWhite,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp
                    )
                }
            }
        }
    }
}