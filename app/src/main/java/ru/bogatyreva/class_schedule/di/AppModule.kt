package ru.bogatyreva.class_schedule.di

import android.app.Application
import android.content.Context
import android.content.res.Resources
import android.net.ConnectivityManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.bogatyreva.class_schedule.data.AuthRepositoryImpl
import ru.bogatyreva.class_schedule.data.TestCareerRepositoryImpl
import ru.bogatyreva.class_schedule.data.TestProfileRepositoryImpl
import ru.bogatyreva.class_schedule.data.TestScheduleRepositoryImpl
import ru.bogatyreva.class_schedule.data.utils.SessionManager
import ru.bogatyreva.class_schedule.domain.repository.AuthRepository
import ru.bogatyreva.class_schedule.domain.repository.CareerRepository
import ru.bogatyreva.class_schedule.domain.repository.ProfileRepository
import ru.bogatyreva.class_schedule.domain.repository.ScheduleRepository
import ru.bogatyreva.class_schedule.domain.usecase.LoginUseCase
import ru.bogatyreva.class_schedule.domain.usecase.LogoutUseCase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Singleton
    @Provides
    fun provideContext(application: Application): Context = application.applicationContext

    @Singleton
    @Provides
    fun provideResources(context: Context): Resources = context.resources

    @Singleton
    @Provides
    fun provideNetworkManager(application: Application): ConnectivityManager =
        application.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    @Singleton
    @Provides
    fun provideScheduleRepo(): ScheduleRepository = TestScheduleRepositoryImpl()

    @Singleton
    @Provides
    fun provideAuthRepository(sessionManager: SessionManager): AuthRepository = AuthRepositoryImpl(sessionManager)

    @Singleton
    @Provides
    fun provideLoginUseCase(repository: AuthRepository): LoginUseCase = LoginUseCase(repository)

    @Singleton
    @Provides
    fun provideLogoutUseCase(repository: AuthRepository): LogoutUseCase = LogoutUseCase(repository)

    @Singleton
    @Provides
    fun provideCareerRepository(): CareerRepository = TestCareerRepositoryImpl()

    @Singleton
    @Provides
    fun provideProfileRepository(
        authRepository: AuthRepository
    ): ProfileRepository = TestProfileRepositoryImpl(authRepository)
}
