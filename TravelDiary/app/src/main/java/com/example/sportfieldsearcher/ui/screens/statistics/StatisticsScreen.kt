package com.example.sportfieldsearcher.ui.screens.statistics

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.example.sportfieldsearcher.data.database.entities.FieldWithUsers

data class PieChartData(
    val name: String,
    val value: Int,
    val color: Color
)

@Composable
fun StatisticsScreen(fields: List<FieldWithUsers>) {
    val data = calculateCategoryPercentages(fields)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Sport Categories",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Spacer(Modifier.size(20.dp))
        PieChart(data = data)
        Spacer(modifier = Modifier.height(16.dp))
        Spacer(Modifier.size(20.dp))
        Legend(data = data)
        Spacer(Modifier.size(20.dp))
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.Start
        ) {
            data.forEach { item ->
                Text(
                    text = "Current percentage of ${item.name}: ${item.value}%",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        }
    }
}

@Composable
fun Legend(data: List<PieChartData>) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start
    ) {
        data.forEach { item ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .background(item.color, CircleShape)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = item.name, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

fun calculateCategoryPercentages(fields: List<FieldWithUsers>): List<PieChartData> {
    val categoryCount = fields.groupBy { it.field.category.name }.mapValues { it.value.size }
    val total = categoryCount.values.sum()
    val colors = listOf(Color.Red, Color.Green, Color.Blue, Color(0xFFFFA500), Color(0xFF800080))

    return categoryCount.entries.mapIndexed { index, entry ->
        PieChartData(
            name = entry.key,
            value = (entry.value * 100) / total,
            color = colors[index % colors.size]
        )
    }
}


@Composable
fun PieChart(data: List<PieChartData>) {
    Canvas(
        modifier = Modifier
            .fillMaxWidth(.8f)
            .aspectRatio(1f)
    ) {
        val total = data.sumOf { it.value }.toFloat()
        var startAngle = -90f
        val strokeWidth = 45.dp.toPx()
        data.filter { it.value > 0 }.forEach { value ->
            val sweepAngle = 360f * value.value.toFloat() / total
            drawArc(
                color = value.color,
                startAngle = startAngle,
                sweepAngle = sweepAngle,
                useCenter = false,
                style = Stroke(strokeWidth, cap = StrokeCap.Butt)
            )
            startAngle += sweepAngle
        }
    }
}
