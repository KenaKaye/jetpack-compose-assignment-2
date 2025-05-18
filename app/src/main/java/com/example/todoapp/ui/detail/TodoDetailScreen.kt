package com.example.todoapp.ui.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.todoapp.data.TodoRepository
import com.example.todoapp.ui.TodoDetailViewModelFactory

@Composable
fun TodoDetailScreen(
    repository: TodoRepository,
    todoId: Int?,
    onBackClick: () -> Unit,
    viewModel: TodoDetailViewModel = viewModel(factory = TodoDetailViewModelFactory(repository))
) {
    if (todoId != null) {
        viewModel.fetchTodoById(todoId)
    }

    val uiState = viewModel.uiState.collectAsState().value

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        when {
            uiState.isLoading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(
                        color = Color(0xFFFFD700)
                    )
                }
            }
            uiState.error != null || uiState.todo == null -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = uiState.error ?: "Todo not found",
                        color = Color.White, // White error text for contrast
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
                    Text(
                        text = "Todo Details",
                        style = MaterialTheme.typography.headlineSmall,
                        color = Color(0xFFFFD700), // Gold heading
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    Text(
                        text = "ID: ${uiState.todo.id}",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.White // White body text
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "User ID: ${uiState.todo.userId}",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.White // White body text
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Title: ${uiState.todo.title}",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.White // White body text
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Completed: ${if (uiState.todo.completed) "Yes" else "No"}",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.White // White body text
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = onBackClick,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFFFD700)
                        )
                    ) {
                        Text(
                            text = "Back",
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}