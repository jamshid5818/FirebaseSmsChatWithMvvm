package jx.lessons.firebasesmschatwithmvvm.data.di

import android.content.SharedPreferences
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.StorageReference
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jx.lessons.firebasesmschatwithmvvm.data.repository.*
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RepositoryModule {
    @Provides
    @Singleton
    fun provideAutghRepository(
        auth: FirebaseAuth,
        myRef: FirebaseDatabase,
    ): AuthRepository {
        return AuthRepositoryImp(auth,myRef)
    }
    @Provides
    @Singleton
    fun providePostRepostiroy(
        myRef: FirebaseDatabase,
        storageReference: StorageReference
    ):PostRepository{
        return PostRepositoryImp(myRef,storageReference)
    }
    @Provides
    @Singleton
    fun providesHomeRepository(
        myRef: FirebaseDatabase
    ):HomeRepository{
        return  HomeRepositoryImp(myRef)
    }
    @Provides
    @Singleton
    fun providesPersonRespository(
        myRef: FirebaseDatabase
    ):PersonRepostiroy{
        return PersonRepositoryImp(myRef)
    }
}