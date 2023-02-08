package mob.com.project.remindme.module

import android.app.Application
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import mob.com.project.remindme.database.AppDatabase
import mob.com.project.remindme.database.ReminderDao
import mob.com.project.remindme.database.repository.ReminderRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    //method for providing AppDatabase
    @Singleton
    @Provides
    fun provideAppDatabase(app: Application): AppDatabase {
        return AppDatabase.getAppInstance(context = app)
    }

    //method for providing ReminderDao
    @Singleton
    @Provides
    fun provideReminderDao(appDatabase: AppDatabase): ReminderDao {
        return appDatabase.reminderDao()
    }

    //method for providing ReminderRepository
    @Singleton
    @Provides
    fun provideReminderRepository(
        reminderDao: ReminderDao
    ): ReminderRepository {
        return ReminderRepository(reminderDao = reminderDao)
    }
}