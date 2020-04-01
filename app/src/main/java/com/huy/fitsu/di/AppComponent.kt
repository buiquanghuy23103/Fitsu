package com.huy.fitsu.di

import android.content.Context
import com.huy.fitsu.addEditCategory.di.AddEditCategoryComponent
import com.huy.fitsu.categories.di.CategoriesComponent
import com.huy.fitsu.data.repository.RepositoryModule
import com.huy.fitsu.scheduler.SchedulerModule
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class,
        SubComponentModule::class,
        ViewModelBuilderModule::class,
        RepositoryModule::class,
        SchedulerModule::class
    ]
)
interface AppComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }

    fun categoriesComponent(): CategoriesComponent.Factory
    fun addEditCategoryComponent(): AddEditCategoryComponent.Factory

}

@Module(
    subcomponents = [
        CategoriesComponent::class,
        AddEditCategoryComponent::class
    ]
)
object SubComponentModule