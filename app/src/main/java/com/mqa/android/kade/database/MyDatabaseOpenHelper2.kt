package com.mqa.android.kade.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import org.jetbrains.anko.db.*

class MyDatabaseOpenHelper2(ctx: Context) : ManagedSQLiteOpenHelper(ctx, "FavoriteTeam.db", null, 1) {
    companion object {
        private var instance: MyDatabaseOpenHelper2? = null

        @Synchronized
        fun getInstance(ctx: Context): MyDatabaseOpenHelper2 {
            if (instance == null) {
                instance = MyDatabaseOpenHelper2(ctx.applicationContext)
            }
            return instance as MyDatabaseOpenHelper2
        }
    }

    override fun onCreate(db: SQLiteDatabase?) {
        // Here you create tables

        db?.createTable(FavoriteTeam.TABLE_TEAM, true,
                FavoriteTeam.ID to INTEGER + PRIMARY_KEY + AUTOINCREMENT,
                FavoriteTeam.TEAM_ID to TEXT + UNIQUE,
                FavoriteTeam.TEAM_NAME to TEXT,
                FavoriteTeam.TEAM_BADGE to TEXT)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Here you can upgrade tables, as usual
        db.dropTable(FavoriteTeam.TABLE_TEAM, true)
    }
}

// Access property for Context
val Context.db: MyDatabaseOpenHelper2
    get() = MyDatabaseOpenHelper2.getInstance(applicationContext)