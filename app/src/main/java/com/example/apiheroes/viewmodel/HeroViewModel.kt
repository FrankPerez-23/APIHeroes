package com.example.apiheroes.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apiheroes.model.HeroApiResponse
import com.example.apiheroes.network.RetrofitInstance
import kotlinx.coroutines.launch

class HeroViewModel : ViewModel(){
    private val _heroe = MutableLiveData<HeroApiResponse?>()
    private val _Allheroes = MutableLiveData<List<HeroApiResponse>?>()
    val heroe: LiveData<HeroApiResponse?> = _heroe
    val Allheroes: LiveData<List<HeroApiResponse>?> = _Allheroes



    fun obtenerHeroe(idHero:Int){
        viewModelScope.launch{
            try {
                val response = RetrofitInstance.api.getHero(idHero)

                if(response.isSuccessful){
                    _heroe.postValue(response.body())
                } else {
                    _heroe.postValue(null) //Manejo de error
                }
            }catch (e: Exception){
                _heroe.postValue(null) //Manejo de excepcion
            }
        }
    }

    fun obtenerTodosHeroes(){
        viewModelScope.launch{
            try {
                val response = RetrofitInstance.api.getAll()

                if(response.isSuccessful){
                    _Allheroes.postValue(response.body())
                } else {
                    _Allheroes.postValue(null) //Manejo de error
                }
            }catch (e: Exception){
                _Allheroes.postValue(null) //Manejo de excepcion
            }
        }
    }
}