package jx.lessons.firebaseSmsChatWithMvvm.data.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.StorageReference
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jx.lessons.firebaseSmsChatWithMvvm.domain.chatAc.global.GlobalRepository
import jx.lessons.firebaseSmsChatWithMvvm.domain.chatAc.global.GlobalRepositoryImp
import jx.lessons.firebaseSmsChatWithMvvm.domain.chatAc.random.RandomChatRepository
import jx.lessons.firebaseSmsChatWithMvvm.domain.chatAc.random.RandomChatRepositoryImp
import jx.lessons.firebaseSmsChatWithMvvm.domain.mainAc.*
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
    ): PostRepository {
        return PostRepositoryImp(myRef,storageReference)
    }
    @Provides
    @Singleton
    fun providesHomeRepository(
        myRef: FirebaseDatabase
    ): HomeRepository {
        return  HomeRepositoryImp(myRef)
    }
    @Provides
    @Singleton
    fun providesPersonRespository(
        myRef: FirebaseDatabase,
        auth: FirebaseAuth
    ): PersonRepostiroy {
        return PersonRepositoryImp(auth,myRef)
    }
    @Provides
    @Singleton
    fun providesGlobalRepository(
        myRef: FirebaseDatabase,
        storageReference: StorageReference
    ): GlobalRepository {
        return GlobalRepositoryImp(myRef,storageReference)
    }
    @Provides
    @Singleton
    fun providesRandomRepository(
        myRef: FirebaseDatabase
    ):RandomChatRepository{
        return RandomChatRepositoryImp(myRef)
    }
}