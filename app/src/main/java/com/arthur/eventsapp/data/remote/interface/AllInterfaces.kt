package com.arthur.eventsapp.data.remote.`interface`

import com.arthur.eventsapp.data.domain.DTO.*
import com.arthur.eventsapp.data.domain.Event
import com.arthur.eventsapp.data.remote.response.LoginResponse
import com.arthur.eventsapp.data.remote.response.UserResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT

interface AllInterfaces {

    @POST("user/login")
    fun login(@Body loginDTO: LoginDTO): Call<LoginResponse>

    @GET("user")
    fun getUser (): Call<UserResponse>

    @GET("events")
    fun getEvents (): Call<ArrayList<Event>>

    @PUT("user")
    fun updateCpf (@Body cpfDTO: CpfDTO): Call<Void>

//    @POST("auth/trocarSenha")
//    fun updatePassword(@Body changePasswordDTO: ChangePasswordDTO): Call<PasswordResponse>
//
//    @POST("auth/novaSenha")
//    fun newPassword(@Body alterarSenhaDTO: RecoverPasswordDTO): Call<PasswordResponse>
//
//    @POST("auth/recuperarSenha")
//    fun recoverPassword(@Body emailDTO: EmailDTO): Call<Void>
}