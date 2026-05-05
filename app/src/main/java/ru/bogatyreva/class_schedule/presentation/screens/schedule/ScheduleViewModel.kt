package ru.bogatyreva.class_schedule.presentation.screens.schedule

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.bogatyreva.class_schedule.domain.model.Lesson
import ru.bogatyreva.class_schedule.domain.repository.ScheduleRepository
import ru.bogatyreva.class_schedule.domain.usecase.GetCalendarDatesUseCase
import ru.bogatyreva.class_schedule.domain.usecase.GetLastLessonEndTimeUseCase
import ru.bogatyreva.class_schedule.domain.usecase.GetLessonsCountForDateUseCase
import ru.bogatyreva.class_schedule.domain.usecase.GetLessonsForDateUseCase
import ru.bogatyreva.class_schedule.domain.usecase.GetTodayUseCase
import ru.bogatyreva.class_schedule.utils.LessonTimeUtils
import ru.bogatyreva.class_schedule.utils.isSameDay
import java.time.Duration
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import javax.inject.Inject

@HiltViewModel
class ScheduleViewModel @Inject constructor(
    private val repository: ScheduleRepository
) : ViewModel() {
    private val getCalendarDatesUseCase = GetCalendarDatesUseCase(repository)
    private val getLastLessonEndTimeUseCase = GetLastLessonEndTimeUseCase()
    private val getLessonsCountForDateUseCase = GetLessonsCountForDateUseCase(repository)
    private val getLessonsForDateUseCase = GetLessonsForDateUseCase(repository)
    private val getTodayUseCase = GetTodayUseCase(repository)

    private val _state = MutableStateFlow(ScheduleScreenState())
    val state = _state.asStateFlow()

    private val _calendarDates = MutableStateFlow<List<Instant>>(emptyList())
    val calendarDates = _calendarDates.asStateFlow()

    private val _calendarCenterDate = MutableStateFlow<Instant?>(null)
    val calendarCenterDate = _calendarCenterDate.asStateFlow()

    private val _monthYear = MutableStateFlow("")
    val monthYear = _monthYear.asStateFlow()

    // Текущее время для подсветки уроков
    private val _currentTime = MutableStateFlow(LocalTime.now())
    val currentTime = _currentTime.asStateFlow()

    // Таймер для обновления подсветки
    private var timerJob: Job? = null

    init {
        loadInitialData()
        startTimeUpdater()
    }

    // Запускаем таймер для обновления текущего времени
    private fun startTimeUpdater() {
        timerJob = viewModelScope.launch {
            while (true) {
                _currentTime.value = LocalTime.now()
                updateLessonsHighlight()
                delay(60000) // Обновляем каждую минуту
            }
        }
    }

    // Обновляем подсветку уроков на основе текущего времени
    private fun updateLessonsHighlight() {
        val currentState = _state.value
        val today = LocalDate.now()

        // Проверяем, что выбранная дата - сегодня
        val selectedLocalDate = currentState.selectedDate
            .atZone(ZoneId.systemDefault())
            .toLocalDate()

        val highlightedLessonId = if (selectedLocalDate == today) {
            LessonTimeUtils.getCurrentOrNextLessonId(
                lessons = currentState.lessons,
                currentTime = _currentTime.value,
                currentDate = today
            )
        } else {
            null // Для других дат не подсвечиваем
        }

        _state.update {
            it.copy(highlightedLessonId = highlightedLessonId)
        }
    }

    // Загрузка начальных данных
    private fun loadInitialData() {
        viewModelScope.launch {
            try {
                val today = getTodayUseCase()
                _calendarCenterDate.value = today
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
                    selectedDate = date,
                    highlightedLessonId = null // Сбрасываем подсветку при смене даты
                )
            }

            try {
                // Загружаем уроки для выбранной даты
                getLessonsForDateUseCase(date).collect { lessons ->
                    // Добавляем информацию о следующем занятии
                    val enrichedLessons = enrichLessonsWithNextStartTime(lessons)
                    // Рассчитываем время окончания последнего занятия
                    val lastLessonEndTime = calculateLastLessonEndTime(enrichedLessons)
                    // Загружаем информацию для шапки
                    val lessonsCount = getLessonsCountForDateUseCase(date)

                    // Обновляем состояние
                    _state.update { currentState ->
                        currentState.copy(
                            lessons = enrichedLessons,
                            lessonsCount = lessonsCount,
                            lastLessonEndTime = lastLessonEndTime,
                            isLoading = false
                        )
                    }

                    // После загрузки уроков обновляем подсветку
                    updateLessonsHighlight()
                }

            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        error = "Ошибка: ${e.message}",
                        isLoading = false
                    )
                }
            }
        }
    }

    // Функция для добавления в урок информации о времени следующего занятия
    private fun enrichLessonsWithNextStartTime(lessons: List<Lesson>): List<Lesson> {
        if (lessons.size <= 1) return lessons

        return lessons.sortedBy { it.pairNumber }.mapIndexed { index, lesson ->
            if (index < lessons.size - 1) {
                val nextLesson = lessons[index + 1]
                lesson.copy(nextLessonStartTime = nextLesson.startTime)
            } else {
                lesson.copy(nextLessonStartTime = null)
            }
        }
    }

    // Функция для расчета времени окончания последнего занятия
    private fun calculateLastLessonEndTime(lessons: List<Lesson>): String? {
        return getLastLessonEndTimeUseCase(lessons)
    }

    //Функция для загрузки дат для календаря
    private suspend fun loadCalendarDates(centerDate: Instant) {
        try {
            val dates = getCalendarDatesUseCase(centerDate)
            _calendarDates.value = dates
        } catch (e: Exception) {
            _state.update { it.copy(error = "Ошибка загрузки календаря: ${e.message}") }
        }
    }

    // Функция для очистки сообщения об ошибке
    private fun clearError() {
        _state.update { it.copy(error = null) }
    }

    // Функция для проверки наличия занятий на дату - для индикации уроков для каждой даты в календаре
    suspend fun checkIfDateHasLessons(date: Instant): Boolean {
        return getLessonsCountForDateUseCase(date) > 0
    }

    // Обработка команд от UI
    fun processCommand(command: ScheduleCommands) {
        viewModelScope.launch {
            when (command) {
                is ScheduleCommands.SelectDate -> {
                    selectDate(command.date)
                }

                is ScheduleCommands.SelectMonth -> {
                    _calendarCenterDate.value = command.date
                    loadCalendarDates(command.date)
                }

                ScheduleCommands.NextWeek -> {
                    val currentCenter = _calendarCenterDate.value ?: _state.value.selectedDate
                    val newDate = currentCenter.plus(Duration.ofDays(WEEK_DAYS))
                    _calendarCenterDate.value = newDate
                    loadCalendarDates(newDate)
                }

                ScheduleCommands.PreviousWeek -> {
                    val currentCenter = _calendarCenterDate.value ?: _state.value.selectedDate
                    val newDate = currentCenter.minus(Duration.ofDays(WEEK_DAYS))
                    _calendarCenterDate.value = newDate
                    loadCalendarDates(newDate)
                }

                ScheduleCommands.NextMonth -> {
                    val currentCenter = _calendarCenterDate.value ?: _state.value.selectedDate

                    // добавление месяца в зависимости от количества дней в месяце
                    val localDate = currentCenter.atZone(ZoneId.systemDefault()).toLocalDate()
                    val nextMonth = localDate.plusMonths(1)
                    val newDate = nextMonth.atStartOfDay(ZoneId.systemDefault()).toInstant()

                    _calendarCenterDate.value = newDate
                    loadCalendarDates(newDate)
                }

                ScheduleCommands.PreviousMonth -> {
                    val currentCenter = _calendarCenterDate.value ?: _state.value.selectedDate

                    // добавление месяца в зависимости от количества дней в месяце
                    val localDate = currentCenter.atZone(ZoneId.systemDefault()).toLocalDate()
                    val previousMonth = localDate.minusMonths(1)
                    val newDate = previousMonth.atStartOfDay(ZoneId.systemDefault()).toInstant()

                    _calendarCenterDate.value = newDate
                    loadCalendarDates(newDate)
                }

                ScheduleCommands.ClearError -> {
                    clearError()
                }

                ScheduleCommands.GoToToday -> {
                    val today = getTodayUseCase()
                    selectDate(today)
                    _calendarCenterDate.value = today
                    loadCalendarDates(today)
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel() // Останавливаем таймер при уничтожении ViewModel
    }

    companion object {
        private const val WEEK_DAYS = 7L
    }
}


//Команды для экрана расписания
sealed interface ScheduleCommands {
    data class SelectDate(val date: Instant) : ScheduleCommands
    data class SelectMonth(val date: Instant) : ScheduleCommands
    data object NextWeek : ScheduleCommands
    data object PreviousWeek : ScheduleCommands
    data object NextMonth : ScheduleCommands
    data object PreviousMonth : ScheduleCommands
    data object ClearError : ScheduleCommands
    data object GoToToday : ScheduleCommands
}

//Состояние экрана расписания
data class ScheduleScreenState(
    val lessons: List<Lesson> = emptyList(),           // Уроки на выбранную дату
    val selectedDate: Instant = Instant.now(),         // Выбранная дата
    val lessonsCount: Int = 0,                         // Количество пар на день
    val lastLessonEndTime: String? = null,             // Время окончания последней пары
    val isLoading: Boolean = false,                    // Флаг загрузки
    val error: String? = null,                         // Сообщение об ошибке
    val highlightedLessonId: Int? = null               // ID подсвеченного урока
) {
    // Проверка: нет уроков, не идет загрузка, нет ошибок
    val isEmptyState: Boolean
        get() = lessons.isEmpty() && !isLoading && error == null

    // Проверка, нужно ли показывать кнопку Сегодня
    val showTodayButton: Boolean
        get() = !isSameDay(selectedDate, Instant.now())
}
