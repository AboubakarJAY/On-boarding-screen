package com.example.onboadingscreenapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.LocalOverscrollConfiguration
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.onboadingscreenapp.ui.theme.OnboadingScreenAppTheme
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Onboarding()
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Onboarding(modifier: Modifier = Modifier) {
    val darkMagenta = colorResource(id = R.color.darkMagenta)
    val pagerState = rememberPagerState{4}
    val coroutineScope = rememberCoroutineScope()
    val walletText = TextWithBackground(R.string.wallets)
    val cardList = listOf(
        R.drawable.card1,
        R.drawable.card2,
        R.drawable.card3,
        R.drawable.card4,
    )
    val textList = listOf(
        R.string.all_wallet,
        R.string.secure_payment,
        R.string.track_expenses,
        R.string.smart_saving
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xff1d1f20))
            .statusBarsPadding()
            .padding(start = 20.dp, end = 20.dp, top = 2.dp)
    ) {
        Row {
            Image(
                painter = painterResource(R.drawable.logo),
                contentDescription = stringResource(R.string.logo),
                modifier = Modifier.padding(end = 5.dp).size(28.dp)
            )
            Text(
                text = stringResource(R.string.name),
                fontSize = 28.sp,
                color = Color.Magenta,
                fontWeight = FontWeight.SemiBold
            )
        }

        CompositionLocalProvider(LocalOverscrollConfiguration provides null) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxWidth().height(400.dp).border(2.dp, Color.Red)
            ) { page ->
                cardList.getOrNull(page)?.let { content ->
                    ContentView(content)
                }
            }
        }
        Text(
            text = stringResource(R.string.effortless),
            color = Color.White,
            fontSize = 16.sp
        )
        Box(
            modifier = Modifier.padding(top = 20.dp)
        ) {
            AnimatedBox(pagerState)
            Text(
                text = stringResource(textList[pagerState.currentPage]),
                color = Color.White,
                fontSize = 38.sp,
                fontWeight = FontWeight.SemiBold,
                lineHeight = 38.sp,
                modifier = Modifier.padding(start = 5.dp)
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 30.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {

            repeat(4) { index ->
                val indicatorSize by animateDpAsState(
                    targetValue = if (index == pagerState.currentPage) 30.dp else 10.dp
                )
                Box(
                    modifier = Modifier
                        .padding(5.dp)
                        .clip(CircleShape)
                        .background(
                            if (index == pagerState.currentPage) Color.Magenta else Color.White
                        )
                        .height(10.dp)
                        .width(indicatorSize)
                        .clickable {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                        }
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = {
                    coroutineScope.launch {
                        if (pagerState.currentPage < 4) {
                            pagerState.animateScrollToPage(
                                pagerState.currentPage + 1,
                                animationSpec = tween(
                                    delayMillis = 500,
                                    easing = FastOutLinearInEasing
                                )
                            )
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    contentColor = Color.White,
                    containerColor = Color.Magenta
                )
            ) {
                Text(
                    text = "Next"
                )
            }
        }
    }
}

@Composable
fun ContentView(@DrawableRes content : Int, modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(content),
        contentDescription = null,
        contentScale = ContentScale.Fit,
        modifier = Modifier.fillMaxSize()
    )
}

@Composable
fun TextWithBackground(@StringRes text : Int, modifier: Modifier = Modifier) {
    return Text(
        text = stringResource(text),
        color = Color.White,
        fontSize = 38.sp,
        fontWeight = FontWeight.SemiBold,
        lineHeight = 38.sp,
        modifier = Modifier.clip(RoundedCornerShape(10.dp))
            .background(Color.Magenta)
            .padding(5.dp)
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AnimatedBox(pagerState: PagerState) {
    // Variables pour la largeur et le padding top en fonction de la page actuelle
    val width by animateDpAsState(
        targetValue = when (pagerState.currentPage) {
            1 -> 50.dp // Agrandir la largeur à la page 2
            2 -> 100.dp // Réduire la largeur à la page 3
            else -> 133.dp // Taille initiale pour la première page
        }
    )

    val topPadding by animateDpAsState(
        targetValue = when (pagerState.currentPage) {
            2 -> 36.dp // Padding top inchangé à la page 2
            3 -> 10.dp // Réduire padding top pour la page 3
            else -> 36.dp // Padding top initial
        }
    )

    val bottomPadding by animateDpAsState(
        targetValue = when (pagerState.currentPage) {
            4 -> 50.dp // Descendre la Box à la page 4
            else -> 0.dp // Pas de padding en bas sur les autres pages
        }
    )

    val startPadding by animateDpAsState(
        targetValue = when(pagerState.currentPage) {
            2 -> 200.dp
            3 -> 0.dp
            else -> 0.dp
        }
    )

    Box(
        modifier = Modifier
            .padding(top = topPadding, bottom = bottomPadding, start = startPadding)
            .clip(RoundedCornerShape(10.dp))
            .background(Color.Magenta)
            .width(width)
            .height(50.dp)
    )
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GreetingPreview() {
    OnboadingScreenAppTheme {
        Onboarding()
    }
}