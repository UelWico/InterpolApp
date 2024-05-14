package com.example.oop

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [Card::class],
    version = 1
)
abstract class CardDB: RoomDatabase() {
    abstract val dao: CardDao
}