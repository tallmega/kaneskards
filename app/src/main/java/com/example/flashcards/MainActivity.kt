package com.kaneskards.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

private val Navy = Color(0xFF15324A)
private val Sky = Color(0xFFDDF2FF)
private val Coral = Color(0xFFFF8A70)
private val Gold = Color(0xFFFFD166)
private const val RoundSize = 20
private const val InitialRound = "initial"
private const val ReviewRound = "review"
private const val CompleteRound = "complete"

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent { KanesKardsApp() }
    }
}

@Composable
private fun KanesKardsApp() {
    var selectedLevelNumber by rememberSaveable { mutableStateOf<Int?>(null) }
    val selectedLevel = selectedLevelNumber?.let { number ->
        FlashcardData.levels.firstOrNull { it.number == number }
    }

    MaterialTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = Sky) {
            selectedLevel?.let { level ->
                StudyScreen(level = level, onBack = { selectedLevelNumber = null })
            } ?: LevelMenu(onLevelSelected = { selectedLevelNumber = it.number })
        }
    }
}

@Composable
private fun LevelMenu(onLevelSelected: (CardLevel) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 42.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text("Kane's Kards", fontSize = 38.sp, fontWeight = FontWeight.ExtraBold, color = Navy)
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
    var sessionNumber by rememberSaveable(level.number) { mutableIntStateOf(0) }
    val shuffleSeed = rememberSaveable(level.number, sessionNumber) { Random.nextInt() }
    val sessionCards = remember(level, shuffleSeed) {
        level.cards.shuffled(Random(shuffleSeed)).take(RoundSize.coerceAtMost(level.cards.size))
    }
    var round by rememberSaveable(level.number, sessionNumber) { mutableStateOf(InitialRound) }
    var initialCardIndex by rememberSaveable(level.number, sessionNumber) { mutableIntStateOf(0) }
    var reviewQueue by rememberSaveable(level.number, sessionNumber) { mutableStateOf(emptyList<Int>()) }

    if (round == CompleteRound) {
        CompletionScreen(
            sessionKey = "${level.number}-$sessionNumber",
            onPlayAnother = { sessionNumber++ },
            onBack = onBack,
        )
        return
    }

    val currentCardIndex = if (round == ReviewRound) reviewQueue.first() else initialCardIndex
    val card = sessionCards[currentCardIndex]
    val progressText = if (round == ReviewRound) {
        "${reviewQueue.size} ${if (reviewQueue.size == 1) "word" else "words"} left to practice"
    } else {
        "${initialCardIndex + 1} of ${sessionCards.size}"
    }

    fun advanceInitialRound() {
        if (initialCardIndex == sessionCards.lastIndex) {
            round = if (reviewQueue.isEmpty()) CompleteRound else ReviewRound
        } else {
            initialCardIndex++
        }
    }

    fun markGotIt() {
        if (round == ReviewRound) {
            reviewQueue = reviewQueue.drop(1)
            if (reviewQueue.isEmpty()) round = CompleteRound
        } else {
            advanceInitialRound()
        }
    }

    fun markTryAgain() {
        if (round == ReviewRound) {
            reviewQueue = reviewQueue.drop(1) + currentCardIndex
        } else {
            reviewQueue = reviewQueue + currentCardIndex
            advanceInitialRound()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(horizontal = 24.dp, vertical = 36.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        OutlinedButton(onClick = onBack, modifier = Modifier.align(Alignment.Start)) { Text("← Levels") }
        Spacer(Modifier.height(18.dp))
        Text("Level ${level.number}: ${level.title}", fontSize = 23.sp, fontWeight = FontWeight.Bold, color = Navy)
        Text(progressText, color = Navy)
        Spacer(Modifier.height(28.dp))
        Card(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            shape = RoundedCornerShape(30.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        ) {
            Box(modifier = Modifier.fillMaxSize().padding(28.dp), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = card.prompt,
                        fontSize = 58.sp,
                        lineHeight = 68.sp,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.ExtraBold,
                        color = Navy,
                    )
                }
            }
        }
        Spacer(Modifier.height(22.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Button(
                onClick = ::markTryAgain,
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = Coral, contentColor = Navy),
            ) { Text("Try Again") }
            Spacer(Modifier.width(16.dp))
            Button(
                onClick = ::markGotIt,
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = Navy),
            ) { Text("Got it!") }
        }
    }
}

@Composable
private fun CompletionScreen(sessionKey: String, onPlayAnother: () -> Unit, onBack: () -> Unit) {
    var animationProgress by rememberSaveable(sessionKey) { mutableFloatStateOf(0f) }

    LaunchedEffect(sessionKey) {
        if (animationProgress < 1f) {
            Animatable(animationProgress).animateTo(1f, animationSpec = tween(durationMillis = 1_500)) {
                animationProgress = value
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(horizontal = 24.dp, vertical = 36.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text("Great work!", fontSize = 40.sp, fontWeight = FontWeight.ExtraBold, color = Navy)
        Spacer(Modifier.height(10.dp))
        Text("You got all 20 words!", fontSize = 20.sp, color = Navy)
        Fireworks(progress = animationProgress)
        Button(
            onClick = onPlayAnother,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Navy),
        ) { Text("Play another 20") }
        Spacer(Modifier.height(12.dp))
        OutlinedButton(onClick = onBack, modifier = Modifier.fillMaxWidth()) { Text("Levels") }
    }
}

@Composable
private fun Fireworks(progress: Float) {
    val colors = listOf(Coral, Gold, Color(0xFF6FCF97))
    Canvas(modifier = Modifier.fillMaxWidth().height(250.dp).padding(vertical = 12.dp)) {
        val centers = listOf(
            Offset(size.width * 0.23f, size.height * 0.60f),
            Offset(size.width * 0.50f, size.height * 0.30f),
            Offset(size.width * 0.77f, size.height * 0.62f),
        )
        centers.forEachIndexed { burstIndex, center ->
            val delay = burstIndex * 0.18f
            val burstProgress = ((progress - delay) / 0.55f).coerceIn(0f, 1f)
            if (burstProgress > 0f) {
                val color = colors[burstIndex]
                val radius = size.minDimension * (0.05f + (0.22f * burstProgress))
                val alpha = 1f - (burstProgress * 0.65f)
                repeat(14) { rayIndex ->
                    val angle = ((rayIndex * 2 * PI) / 14.0).toFloat()
                    val end = Offset(
                        x = center.x + (cos(angle.toDouble()).toFloat() * radius),
                        y = center.y + (sin(angle.toDouble()).toFloat() * radius),
                    )
                    drawLine(
                        color = color.copy(alpha = alpha),
                        start = center,
                        end = end,
                        strokeWidth = 7f * (1f - (burstProgress * 0.35f)),
                        cap = StrokeCap.Round,
                    )
                    drawCircle(color = color.copy(alpha = alpha), radius = 5f, center = end)
                }
            }
        }
    }
}
