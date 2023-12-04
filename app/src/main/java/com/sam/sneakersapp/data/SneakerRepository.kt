package com.sam.sneakersapp.data

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.sam.sneakersapp.data.model.Sneaker
import com.sam.sneakersapp.data.model.SneakerList
import com.sam.sneakersapp.di.IoDispatcher
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class SneakerRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) {

    fun getSneakers() = flow {
        try {
        context.assets?.open("sneakersList.json")?.use { inputStream ->
                val size = inputStream.available()
                val buffer = ByteArray(size)
                inputStream.read(buffer)
                val json = String(buffer)
                val imagesData = Gson().fromJson(json, SneakerList::class.java)
                emit(imagesData)
            }
        }catch (e: Exception) {
            e.printStackTrace()
            Log.e("getSneakers", "getSneakers: ${e.message}")
            emit(SneakerList(listOf()))
        }
    }.flowOn(ioDispatcher)

}