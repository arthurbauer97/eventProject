package com.arthur.eventsapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.arthur.eventsapp.data.domain.DTO.CpfDTO
import com.arthur.eventsapp.data.domain.DTO.LoginDTO
import com.arthur.eventsapp.data.domain.Event
import com.arthur.eventsapp.util.Resource
import com.arthur.eventsapp.data.remote.ApiService
import com.arthur.eventsapp.data.remote.`interface`.AllInterfaces
import com.arthur.eventsapp.data.remote.response.ErrorResponse
import com.arthur.eventsapp.data.remote.response.LoginResponse
import com.arthur.eventsapp.data.remote.response.UserResponse
import com.arthur.eventsapp.util.ErrorResource
import com.arthur.eventsapp.util.InternetCheck
import com.arthur.eventsapp.util.SuccessResource
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AllViewModel : ViewModel() {

    var loginSuccess = MutableLiveData<Resource<LoginResponse>>()
    var userData = MutableLiveData<Resource<UserResponse>>()
    var eventList = MutableLiveData<Resource<ArrayList<Event>>>()
    var cpfUpdate = MutableLiveData<Resource<String>>()

    fun login(user: LoginDTO): MutableLiveData<Resource<LoginResponse>> {
        val api = ApiService.client!!.create(AllInterfaces::class.java)

        api.login(user).enqueue(object : Callback<LoginResponse> {
            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                InternetCheck(object : InternetCheck.Consumer {
                    override fun accept(internet: Boolean?) {
                        if (!internet!!) {
                            loginSuccess.value = ErrorResource("Verifique sua conexão com a internet!")
                        } else {
                            loginSuccess.value = ErrorResource("Estamos com problemas no servidor, tente novamente mais tarde!")
                        }
                    }
                })
            }

            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    loginSuccess.value = SuccessResource(data = response.body()!!)
                } else {
                    val gson = Gson()
                    try {
                        val error: ErrorResponse? = gson.fromJson(
                            response.errorBody()!!.string(),
                            ErrorResponse::class.java
                        )
                        if (error != null) {
                            loginSuccess.value = ErrorResource(error =  error.debugMessage)
                        } else {
                            loginSuccess.value = ErrorResource("Erro desconhecido!")
                        }
                    } catch (exception: Exception) {
                        loginSuccess.value = ErrorResource("Problema no servidor!")
                    }
                }
            }
        })
        return loginSuccess
    }

    fun updateCpf(cpf : CpfDTO): MutableLiveData<Resource<String>> {
        val api = ApiService.client!!.create(AllInterfaces::class.java)

        api.updateCpf(cpf).enqueue(object : Callback<Void> {
            override fun onFailure(call: Call<Void>, t: Throwable) {
                t
                InternetCheck(object : InternetCheck.Consumer {
                    override fun accept(internet: Boolean?) {
                        if (!internet!!) {
                            cpfUpdate.value = ErrorResource("Verifique sua conexão com a internet!")
                        } else {
                            cpfUpdate.value = ErrorResource("Estamos com problemas no servidor, tente novamente mais tarde!")
                        }
                    }
                })
            }

            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    cpfUpdate.value = SuccessResource("Sucesso!")
                } else {
                    val gson = Gson()
                    try {
                        val error: ErrorResponse? = gson.fromJson(
                            response.errorBody()!!.string(),
                            ErrorResponse::class.java
                        )
                        if (error != null) {
                            cpfUpdate.value = ErrorResource("Você precisa relogar para trocar o CPF novamente!")
                        } else {
                            cpfUpdate.value = ErrorResource("Erro desconhecido!")
                        }
                    } catch (exception: Exception) {
                        cpfUpdate.value = ErrorResource("Problema no servidor!")
                    }
                }
            }
        })
        return cpfUpdate
    }

    fun user(): MutableLiveData<Resource<UserResponse>> {
        val api = ApiService.client!!.create(AllInterfaces::class.java)

        api.getUser().enqueue(object : Callback<UserResponse> {
            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                InternetCheck(object : InternetCheck.Consumer {
                    override fun accept(internet: Boolean?) {
                        if (!internet!!) {
                            userData.value = ErrorResource("Verifique sua conexão com a internet!")
                        } else {
                            userData.value = ErrorResource("Estamos com problemas no servidor, tente novamente mais tarde!")
                        }
                    }
                })
            }

            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if (response.isSuccessful) {
                    userData.value = SuccessResource(data = response.body()!!)
                } else {
                    val gson = Gson()
                    try {
                        val error: ErrorResponse? = gson.fromJson(
                            response.errorBody()!!.string(),
                            ErrorResponse::class.java
                        )
                        if (error != null) {
                            userData.value = ErrorResource(error =  error.debugMessage)
                        } else {
                            userData.value = ErrorResource("Erro desconhecido!")
                        }
                    } catch (exception: Exception) {
                        userData.value = ErrorResource("Problema no servidor!")
                    }
                }
            }
        })
        return userData
    }

    fun getAllEvents(): MutableLiveData<Resource<ArrayList<Event>>> {
        val api = ApiService.client!!.create(AllInterfaces::class.java)

        api.getEvents().enqueue(object : Callback<ArrayList<Event>> {
            override fun onFailure(call: Call<ArrayList<Event>>, t: Throwable) {
                InternetCheck(object : InternetCheck.Consumer {
                    override fun accept(internet: Boolean?) {
                        if (!internet!!) {
                            eventList.value = ErrorResource("Verifique sua conexão com a internet!")
                        } else {
                            eventList.value = ErrorResource("Estamos com problemas no servidor, tente novamente mais tarde!")
                        }
                    }
                })
            }

            override fun onResponse(call: Call<ArrayList<Event>>, response: Response<ArrayList<Event>>) {
                if (response.isSuccessful) {
                    eventList.value = SuccessResource(data = response.body()!!)
                } else {
                    val gson = Gson()
                    try {
                        val error: ErrorResponse? = gson.fromJson(
                            response.errorBody()!!.string(),
                            ErrorResponse::class.java
                        )
                        if (error != null) {
                            eventList.value = ErrorResource(error =  error.debugMessage)
                        } else {
                            eventList.value = ErrorResource("Erro desconhecido!")
                        }
                    } catch (exception: Exception) {
                        eventList.value = ErrorResource("Problema no servidor!")
                    }
                }
            }
        })
        return eventList
    }

}