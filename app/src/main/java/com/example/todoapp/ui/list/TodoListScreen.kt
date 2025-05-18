package com.example.todoapp.ui.list

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.todoapp.data.Todo
import com.example.todoapp.data.TodoRepository

@Composable
fun TodoListScreen(
    repository: TodoRepository,
    onTodoClick: (Int) -> Unit,
    viewModel: TodoListViewModel = viewModel(factory = TodoListViewModelFactory(repository))
) {
    val uiState by viewModel.uiState.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black) // Black background
    ) {
        when {
            uiState.isLoading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(
                        color = Color(0xFFFFD700) // Gold loading indicator
                    )
                }
            }
            uiState.error != null -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = uiState.error ?: "Unknown error",
                        color = Color.White, // White error text
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
            uiState.todos.isEmpty() -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = "No todos available",
                        color = Color.White, // White empty state text
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
            else -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    // Search Bar
                    TextField(
                        value = searchQuery,
                        onValueChange = { query -> viewModel.updateSearchQuery(query) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        placeholder = {
                            Text(
                                text = "Search todos...",
                                color = Color.White.copy(alpha = 0.5f)
                            )
                        },
                        colors = TextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedContainerColor = Color.Black,
                            unfocusedContainerColor = Color.Black,
                            cursorColor = Color(0xFFFFD700), // Gold cursor
                            focusedIndicatorColor = Color(0xFFFFD700), // Gold underline when focused
                            unfocusedIndicatorColor = Color.White // White underline when unfocused
                        )
                    )

                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(uiState.todos) { todo ->
                            TodoItem(todo = todo, onClick = { onTodoClick(todo.id) })
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TodoItem(todo: Todo, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        ),
        border = BorderStroke(1.dp, Color(0xFFFFD700)), // Gold border
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = todo.title,
                style = MaterialTheme.typography.titleLarge,
                color = Color(0xFFFFD700), // Gold title
                modifier = Modifier.weight(1f)
            )
            Checkbox(
                checked = todo.completed,
                onCheckedChange = null,
                enabled = false,
                colors = CheckboxDefaults.colors(
                    checkedColor = Color(0xFFFFD700), // Gold checkbox
                    uncheckedColor = Color.White,
                    checkmarkColor = Color.Black
                )
            )
        }
    }
}