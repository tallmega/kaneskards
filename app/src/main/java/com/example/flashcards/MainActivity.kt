package com.kaneskards.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Navy = Color(0xFF15324A)
private val Sky = Color(0xFFDDF2FF)
private val Coral = Color(0xFFFF8A70)
private val Gold = Color(0xFFFFD166)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent { KanesKardsApp() }
    }
}

@Composable
private fun KanesKardsApp() {
    var selectedLevel by remember { mutableStateOf<CardLevel?>(null) }

    MaterialTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = Sky) {
            selectedLevel?.let { level ->
                StudyScreen(level = level, onBack = { selectedLevel = null })
            } ?: LevelMenu(onLevelSelected = { selectedLevel = it })
        }
    }
}

@Composable
private fun LevelMenu(onLevelSelected: (CardLevel) -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(horizontal = 24.dp, vertical = 42.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text("Kanes Kards", fontSize = 38.sp, fontWeight = FontWeight.ExtraBold, color = Navy)
        Text("Pick a level and start reading!", fontSize = 18.sp, color = Navy)
        Spacer(Modifier.height(34.dp))
        FlashcardData.levels.forEachIndexed { index, level ->
            LevelButton(level = level, color = if (index % 2 == 0) Coral else Gold) {
                onLevelSelected(level)
            }
            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
private fun LevelButton(level: CardLevel, color: Color, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = color),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(22.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text("${level.number}", fontSize = 42.sp, fontWeight = FontWeight.ExtraBold, color = Navy)
            Spacer(Modifier.width(20.dp))
            Column {
                Text(level.title, fontSize = 23.sp, fontWeight = FontWeight.Bold, color = Navy)
                Text(level.description, fontSize = 15.sp, color = Navy)
            }
        }
    }
}

@Composable
private fun StudyScreen(level: CardLevel, onBack: () -> Unit) {
    var cardIndex by remember(level) { mutableIntStateOf(0) }
    var answerVisible by remember(level, cardIndex) { mutableStateOf(false) }
    val card = level.cards[cardIndex]

    Column(
        modifier = Modifier.fillMaxSize().padding(horizontal = 24.dp, vertical = 36.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        OutlinedButton(onClick = onBack, modifier = Modifier.align(Alignment.Start)) { Text("← Levels") }
        Spacer(Modifier.height(18.dp))
        Text("Level ${level.number}: ${level.title}", fontSize = 23.sp, fontWeight = FontWeight.Bold, color = Navy)
        Text("${cardIndex + 1} of ${level.cards.size}", color = Navy)
        Spacer(Modifier.height(28.dp))
        Card(
            modifier = Modifier.weight(1f).fillMaxWidth().clickable { answerVisible = !answerVisible },
            shape = RoundedCornerShape(30.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        ) {
            Box(modifier = Modifier.fillMaxSize().padding(28.dp), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = if (answerVisible && card.answer != null) card.answer else card.prompt,
                        fontSize = 58.sp,
                        lineHeight = 68.sp,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.ExtraBold,
                        color = Navy,
                    )
                    val helper = if (answerVisible) card.hint else "Tap the card to flip it"
                    if (!helper.isNullOrBlank()) {
                        Spacer(Modifier.height(18.dp))
                        Text(helper, fontSize = 17.sp, textAlign = TextAlign.Center, color = Navy)
                    }
                }
            }
        }
        Spacer(Modifier.height(22.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Button(
                onClick = { cardIndex--; answerVisible = false },
                enabled = cardIndex > 0,
                colors = ButtonDefaults.buttonColors(containerColor = Navy),
            ) { Text("Previous") }
            Button(
                onClick = { cardIndex++; answerVisible = false },
                enabled = cardIndex < level.cards.lastIndex,
                colors = ButtonDefaults.buttonColors(containerColor = Navy),
            ) { Text("Next") }
        }
    }
}
