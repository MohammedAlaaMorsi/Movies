package io.mohammedalaamorsi.movies.presentation.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.focusTarget
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.mohammedalaamorsi.movies.R
import kotlinx.coroutines.delay

@Composable
fun SearchComponent(
    modifier: Modifier = Modifier,
    onFocusChanged: (Boolean) -> Unit = {},
    onSearch: (String) -> Unit,
    text: String
) {
    var searchText by remember { mutableStateOf(text) }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }
    var shouldShowSearchHint by remember { mutableStateOf(false) }
    var isFocused by remember { mutableStateOf(false) }

    LaunchedEffect(searchText, isFocused) {
        shouldShowSearchHint = if (searchText.length in 1..2 && isFocused) {
            delay(300) // Short delay before showing hint
            true
        } else {
            false
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    elevation = if (isFocused) 6.dp else 2.dp,
                    shape = RoundedCornerShape(24.dp)
                )
                .clip(RoundedCornerShape(24.dp))
                .border(
                    width = if (isFocused) 1.dp else 0.dp,
                    color = if (isFocused) MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                    else Color.Transparent,
                    shape = RoundedCornerShape(24.dp)
                ),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 2.dp
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp)
            ) {
                CustomTextField(
                    modifier = Modifier
                        .focusRequester(focusRequester)
                        .focusTarget()
                        .height(50.dp)
                        .weight(1f)
                        .animateContentSize()
                        .onFocusChanged { focusState ->
                            isFocused = focusState.isFocused
                            onFocusChanged(focusState.isFocused)
                        },
                    keyboardController = keyboardController,
                    initialValue = searchText,
                    onSearch = { query ->
                        if (query.isEmpty() || query.length > 2) {
                            onSearch(query)
                        }
                    },
                    placeholder = stringResource(R.string.search_movies),
                    onValueChange = {
                        searchText = it.text
                        // Only trigger search if empty or more than 2 chars
                        if (searchText.isEmpty() || searchText.length > 2) {
                            onSearch(searchText)
                        }
                    },
                )
            }
        }
        
        AnimatedVisibility(
            visible = shouldShowSearchHint,
            enter = fadeIn() + slideInVertically(),
            exit = fadeOut() + slideOutVertically(),
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            Surface(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.padding(top = 4.dp)
            ) {
                Text(
                    text = "Type at least 3 characters to search",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                )
            }
        }
    }
}
