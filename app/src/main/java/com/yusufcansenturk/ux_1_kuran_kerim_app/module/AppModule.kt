package com.yusufcansenturk.ux_1_kuran_kerim_app.module

import com.google.firebase.auth.FirebaseAuth
import com.yusufcansenturk.ux_1_kuran_kerim_app.repository.AuthRepository
import com.yusufcansenturk.ux_1_kuran_kerim_app.repository.AuthRepositoryImpl
import com.yusufcansenturk.ux_1_kuran_kerim_app.repository.SureRepository
import com.yusufcansenturk.ux_1_kuran_kerim_app.service.ServiceAPI
import com.yusufcansenturk.ux_1_kuran_kerim_app.util.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Singleton
    @Provides
    fun provideSureRepository(api: ServiceAPI) = SureRepository(api)


    @Singleton
    @Provides
    fun provideSureAPI(): ServiceAPI {
        return  Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(ServiceAPI::class.java)
    }

    @Provides
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    fun provideFirebaseRepository(imp: AuthRepositoryImpl): AuthRepository = imp





}