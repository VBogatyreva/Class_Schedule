package ru.bogatyreva.class_schedule.presentation.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.bogatyreva.class_schedule.domain.CheckIsSelectedUseCase
import ru.bogatyreva.class_schedule.domain.CheckIsTodayUseCase
import ru.bogatyreva.class_schedule.domain.GetCalendarDatesUseCase
import ru.bogatyreva.class_schedule.domain.GetDayNumberUseCase
import ru.bogatyreva.class_schedule.domain.GetDayOfWeekUseCase
import ru.bogatyreva.class_schedule.domain.GetFormattedDateUseCase
import ru.bogatyreva.class_schedule.domain.GetLessonsCountForDateUseCase
import ru.bogatyreva.class_schedule.domain.GetLessonsForDateUseCase
import ru.bogatyreva.class_schedule.domain.GetSemesterDisplayNameUseCase
import ru.bogatyreva.class_schedule.domain.GetTodayUseCase
import ru.bogatyreva.class_schedule.domain.Lesson
import ru.bogatyreva.class_schedule.domain.ScheduleRepository
import java.time.Duration
import java.time.Instant

class ScheduleViewModel(
    private val repository: ScheduleRepository
) : ViewModel() {

    private val getLessonsForDateUseCase = GetLessonsForDateUseCase(repository)
    private val getFormattedDateUseCase = GetFormattedDateUseCase(repository)
    private val getSemesterDisplayNameUseCase = GetSemesterDisplayNameUseCase(repository)
    private val getLessonsCountForDateUseCase = GetLessonsCountForDateUseCase(repository)
    private val getCalendarDatesUseCase = GetCalendarDatesUseCase(repository)
    private val getDayNumberUseCase = GetDayNumberUseCase(repository)
    private val getDayOfWeekUseCase = GetDayOfWeekUseCase(repository)
    private val getTodayUseCase = GetTodayUseCase(repository)
    private val checkIsTodayUseCase = CheckIsTodayUseCase(repository)
    private val checkIsSelectedUseCase = CheckIsSelectedUseCase(repository)

    private val _state = MutableStateFlow(ScheduleScreenState())
    val state = _state.asStateFlow()

    private val _calendarDates = MutableStateFlow<List<Instant>>(emptyList())
    val calendarDates = _calendarDates.asStateFlow()

    init {
        loadInitialData()
    }

    // Загрузка начальных данных
    private fun loadInitialData() {
        viewModelScope.launch {
            try {
                val today = getTodayUseCase()
                loadCalendarDates(today)
                selectDate(today)
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        error = "Ошибка загрузки: ${e.message}",
                        isLoading = false
                    )
                }
            }
        }
    }

    // Выбор даты и загрузка данных
    private fun selectDate(date: Instant) {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    isLoading = true,
                    selectedDate = date
                )
            }

            try {
                // Загружаем уроки для выбранной даты
                getLessonsForDateUseCase(date).collect { lessons ->
                    _state.update { currentState ->
                        currentState.copy(
                            lessons = lessons,
                            isLoading = false
                        )
                    }
                }

                // Загружаем информацию для шапки
                val formattedDate = getFormattedDateUseCase(date)
                val semesterName = getSemesterDisplayNameUseCase()
                val lessonsCount = getLessonsCountForDateUseCase(date)

                _state.update {
                    it.copy(
                        formattedDate = formattedDate,
                        semesterName = semesterName,
                        lessonsCount = lessonsCount
                    )
                }

                // Обновляем календарь
                loadCalendarDates(date)

            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        error = "Неожиданная ошибка: ${e.message}",
                        isLoading = false
                    )
                }
            }
        }
    }

    //Загрузка дат для календаря
    private suspend fun loadCalendarDates(centerDate: Instant) {
        try {
            val dates = getCalendarDatesUseCase(centerDate)
            _calendarDates.value = dates
        } catch (e: Exception) {
            _state.update { it.copy(error = "Ошибка загрузки календаря: ${e.message}") }
        }
    }

    // Очистка сообщения об ошибке
    private fun clearError() {
        _state.update { it.copy(error = null) }
    }

    // Получение данных для отображения дня в календаре
    suspend fun getCalendarDayData(date: Instant): CalendarDayData {
        return CalendarDayData(
            date = date,
            dayNumber = getDayNumberUseCase(date),
            dayOfWeek = getDayOfWeekUseCase(date),
            isToday = checkIsTodayUseCase(date),
            isSelected = checkIsSelectedUseCase(date, _state.value.selectedDate),
            hasLessons = getLessonsCountForDateUseCase(date) > 0
        )
    }

    // Обработка команд от UI
    fun processCommand(command: ScheduleCommands) {
        viewModelScope.launch {
            when (command) {
                is ScheduleCommands.SelectDate -> {
                    selectDate(command.date)
                }

                ScheduleCommands.NextWeek -> {
                    val currentDate = _state.value.selectedDate
                    val newDate = currentDate.plus(Duration.ofDays(WEEK_DAYS))
                    selectDate(newDate)
                }

                ScheduleCommands.PreviousWeek -> {
                    val currentDate = _state.value.selectedDate
                    val newDate = currentDate.minus(Duration.ofDays(WEEK_DAYS))
                    selectDate(newDate)
                }

                ScheduleCommands.ClearError -> {
                    clearError()
                }
            }
        }
    }

    companion object {
        private const val WEEK_DAYS = 7L
    }
}


//Команды для экрана расписания
sealed interface ScheduleCommands {
    data class SelectDate(val date: Instant) : ScheduleCommands     // Выбор даты тапом
    data object NextWeek : ScheduleCommands                         // Следующая неделя
    data object PreviousWeek : ScheduleCommands                     // Предыдущая неделя
    data object ClearError : ScheduleCommands                       // Очищает сообщение об ошибке

}

//Состояние экрана расписания
data class ScheduleScreenState(
    val lessons: List<Lesson> = emptyList(),           // Уроки на выбранную дату
    val selectedDate: Instant = Instant.now(),         // Выбранная дата
    val formattedDate: String = "",                    // Отформатированная дата для шапки ("16 марта, понедельник")
    val semesterName: String = "",                     // Название семестра ("Весенний семестр 2026 год")
    val lessonsCount: Int = 0,                         // Количество пар на день
    val isLoading: Boolean = false,                    // Флаг загрузки
    val error: String? = null                          // Сообщение об ошибке
) {
    val isEmptyState: Boolean
        get() = lessons.isEmpty() && !isLoading && error == null
}


//Данные для отображения в календаре
data class CalendarDayData(
    val date: Instant,
    val dayNumber: String,      // "16", "17", "18"
    val dayOfWeek: String,      // "Пн", "Вт", "Ср"
    val isToday: Boolean,
    val isSelected: Boolean,
    val hasLessons: Boolean
)