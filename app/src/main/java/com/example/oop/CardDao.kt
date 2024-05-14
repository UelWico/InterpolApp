package com.example.oop

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface CardDao {
    @Upsert
    suspend fun upsertCard(card: Card)

    @Delete
    suspend fun deleteCard(card: Card)

    @Query("SELECT * FROM card where gender = :gender")
    fun getCardsByGender(gender: String): Flow<List<Card>>

    @Query("SELECT * FROM card where nationality = :nationality")
    fun getCardsByNationality(nationality: String): Flow<List<Card>>

    @Query("SELECT * FROM card where wantedBy = :wantedBy")
    fun getCardsByWB(wantedBy: String): Flow<List<Card>>

    @Query("SELECT * FROM card where age >= :ageMin & age <= :ageMax")
    fun getCardsByAge(ageMin: Int, ageMax: Int): Flow<List<Card>>

}
