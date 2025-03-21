package com.practicum.playlistmaker.ui.settings

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.ui.settings.view_model.SettingsViewModel
import com.practicum.playlistmaker.ui.theme.Blue
import com.practicum.playlistmaker.ui.theme.LightBlue
import com.practicum.playlistmaker.ui.theme.YsDisplayFont


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingScreen(vm: SettingsViewModel) {

    val isDarkTheme by vm.isDarkThemeEnabled.collectAsState()

        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = stringResource(R.string.setting),
                            style = TextStyle(
                                fontSize = 22.sp,
                                fontFamily = YsDisplayFont,
                                fontWeight = FontWeight.Medium
                            )
                        )
                    }
                )
            }
        ) { paddingValues ->
            Column(modifier = Modifier.padding(paddingValues)) {
                SwitchRow(
                    checked = isDarkTheme,
                    onCheckedChange = { vm.toggleTheme(it) }
                )
                InfoRow(
                    textRes = R.string.settings_share,
                    iconRes = painterResource(R.drawable.ic_share),
                    onClickAction = { vm.shareApp() }
                )
                InfoRow(
                    textRes = R.string.settings_support,
                    iconRes = painterResource(R.drawable.ic_support),
                    onClickAction = { vm.openSupport() }
                )
                InfoRow(
                    textRes = R.string.settings_user_agreement,
                    iconRes = painterResource(R.drawable.ic_arrow_forward),
                    onClickAction = { vm.openTerms() }
                )
            }
        }
    }


@Composable
fun InfoRow(@StringRes textRes: Int,
            iconRes: Painter,
            onClickAction: ()-> Unit
) {
    Row(
        modifier = Modifier
            .clickable {onClickAction()}
            .fillMaxWidth()
            .padding(start = 16.dp, end = 12.dp, top = 42.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(textRes),
            style = TextStyle(
                fontSize = 16.sp,
                fontFamily = YsDisplayFont,
                fontWeight = FontWeight.Normal
            ),
        )

        Spacer(modifier = Modifier.weight(1f))

        Image(
            painter = iconRes,
            contentDescription = null
        )
    }
}

@Composable
fun SwitchRow(checked: Boolean, onCheckedChange: (Boolean) -> Unit) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(start = 16.dp, top = 45.dp)
    ) {
        Text(
            text = stringResource(R.string.settings_dark_mode),
            style = TextStyle(
                fontSize = 16.sp,
            )
        )

        Spacer(modifier = Modifier.weight(1f))

       Switch(
           checked = checked,
           onCheckedChange = onCheckedChange,
           modifier = Modifier.padding(end = 6.dp),
           colors = SwitchDefaults.colors(
               checkedThumbColor = Blue,       // Цвет ползунка, когда включен
               uncheckedThumbColor = Color.Gray,     // Цвет ползунка, когда выключен
               checkedTrackColor = LightBlue,       // Цвет фона, когда включен
               uncheckedTrackColor = Color.LightGray // Цвет фона, когда выключен
           )
       )
    }
}

@Preview
@Composable
fun SettingScreenPreview() {

}