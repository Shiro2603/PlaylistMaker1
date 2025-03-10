package com.practicum.playlistmaker.ui.settings

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.ui.settings.view_model.SettingsViewModel

@Composable
fun SettingScreen(vm: SettingsViewModel) {
    Scaffold(
        topBar = { TopAppBar(title = { stringResource(R.string.setting) }) }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            SwitchRow()
            InfoRow(
                textRes = R.string.settings_share,
                iconRes = painterResource(R.drawable.ic_share)
            ) { vm.shareApp() }
            InfoRow(
                textRes = R.string.settings_support,
                painterResource(R.drawable.ic_support)

            ) { vm.openSupport()}
            InfoRow(
                textRes = R.string.settings_user_agreement,
                painterResource(R.drawable.ic_arrow_forward)
            ) { vm.openTerms()}
        }
    }
}

@Composable
fun InfoRow(@StringRes textRes: Int,
            iconRes: Painter,
            onClickAction: ()-> Unit
) {
    Row(
        modifier = Modifier.clickable {onClickAction},
    ) {
        Text(
            text = stringResource(textRes)
        )

        Image(
            painter = iconRes,
            contentDescription = null
        )
    }
}

@Composable
fun SwitchRow() {

    var isChecked by remember { mutableStateOf(false) }

    Row(verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(16.dp)
    ) {
        Text(
            text = stringResource(R.string.settings_dark_mode)
        )

       Switch(
           checked = isChecked,
           onCheckedChange = { isChecked = it }
       )
    }
}

@Preview
@Composable
fun SettingScreenPreview() {

}